/**
 * Copyright (c) 2012, Serene Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.sereneproject.collector.service.impl;

import static org.rrd4j.ConsolFun.AVERAGE;
import static org.rrd4j.DsType.GAUGE;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import javax.annotation.Resource;

import net.sereneproject.collector.domain.Probe;
import net.sereneproject.collector.dto.AnalyzeQueueMessage;
import net.sereneproject.collector.dto.MonitoringMessageDto;
import net.sereneproject.collector.dto.ProbeDto;
import net.sereneproject.collector.dto.ValueDto;
import net.sereneproject.collector.service.ProbePublishingService;

import org.apache.log4j.Logger;
import org.rrd4j.core.RrdBackendFactory;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

/**
 * Service used to publish monitoring values.
 * 
 * @author gehel
 */
@Service
public class ProbePublishingServiceImpl implements ProbePublishingService {

    /** Logger. */
    private static final Logger LOG = Logger
            .getLogger(ProbePublishingServiceImpl.class);

    /** The message queue used to publish messages to analyzers. */
    @Resource(name = "probeToAnalyzeQueue")
    private BlockingQueue<AnalyzeQueueMessage> queue;

    private final RrdBackendFactory rrdBackendFactory;

    @Autowired
    public ProbePublishingServiceImpl(final RrdBackendFactory rrdBackendFactory) {
        this.rrdBackendFactory = rrdBackendFactory;
    }

    /**
     * Publish a monitoring message.
     * 
     * Takes care of storing values in persistent storage, creating intermedite
     * nodes if needed. Probe values are then dispatched to a message queue for
     * analysis.
     * 
     * @param message
     *            contains data about all probe values and where to store them
     * @throws IOException
     */
    @Override
    public final void publish(final MonitoringMessageDto message)
            throws IOException {
        Date now = new Date();

        for (ProbeDto probeDto : message.getProbes()) {
            RrdDb rrdDb = findOrCreateRrdDb(probeDto);
            Sample sample = rrdDb.createSample();
            for (ValueDto valueDto : probeDto.getValues()) {
                sample.setValue(valueDto.getName(), valueDto.getValue());
            }
            sample.update();

            // queue messages so they are processed by the analyzers
            AnalyzeQueueMessage toAnalyze = new AnalyzeQueueMessage(
                    UUID.fromString(probeDto.getUuid()), now,
                    probeDto.getValues());
            getQueue().add(toAnalyze);
        }
    }

    private RrdDb findOrCreateRrdDb(ProbeDto probeDto) throws IOException {
        Probe probe;
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Checking if probe [" + probeDto.getUuid()
                        + "] exist.");
            }
            probe = Probe.findProbeByUuidEquals(probeDto.getUuid());
        } catch (EmptyResultDataAccessException dataEx) {
            // probe does not exist
            if (LOG.isDebugEnabled()) {
                LOG.debug("Probe [" + probeDto.getUuid()
                        + "] not found, creating it.");
            }
            probe = new Probe();
            probe.setUuid(probeDto.getUuid());
            probe.setName(probeDto.getName());
            probe.persist();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Probe [" + probe + "] created.");
            }
        }
        if (probe.getRrd() == null) {
            LOG.debug("Initializing new RrdDb");
            RrdDef rrdDef = new RrdDef(probeDto.getUuid());
            rrdDef.addArchive(AVERAGE, 0.5, 1, 600);
            rrdDef.addArchive(AVERAGE, 0.5, 6, 700);
            rrdDef.addArchive(AVERAGE, 0.5, 24, 775);
            rrdDef.addArchive(AVERAGE, 0.5, 288, 797);
            for (ValueDto value : probeDto.getValues()) {
                rrdDef.addDatasource(value.getName(), GAUGE, 600, Double.NaN,
                        Double.NaN);
            }
            return new RrdDb(rrdDef, getRrrdBackendFactory());
        }
        return new RrdDb(probeDto.getUuid(), getRrrdBackendFactory());
    }

    /**
     * Get the message queue.
     * 
     * @return the queue
     */
    private BlockingQueue<AnalyzeQueueMessage> getQueue() {
        return this.queue;
    }

    private RrdBackendFactory getRrrdBackendFactory() {
        return this.rrdBackendFactory;
    }
}
