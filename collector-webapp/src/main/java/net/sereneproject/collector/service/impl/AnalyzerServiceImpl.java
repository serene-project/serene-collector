/**
 * Copyright  (c) 2012, Serene Project
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

import java.io.IOException;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sereneproject.collector.domain.Plugin;
import net.sereneproject.collector.domain.Probe;
import net.sereneproject.collector.domain.ProbeValue;
import net.sereneproject.collector.dto.AnalyzerRequestDto;
import net.sereneproject.collector.dto.AnalyzerResponseDto;
import net.sereneproject.collector.dto.ProbeValueDateDto;
import net.sereneproject.collector.service.AnalyzerPluginCommunicationService;
import net.sereneproject.collector.service.AnalyzerService;

/**
 * Service used to dispatch probe values to analyzers.
 * 
 * @author gehel
 */
@Service
public class AnalyzerServiceImpl implements AnalyzerService {

    /** Logger. */
    private static final Logger LOG = Logger
            .getLogger(AnalyzerServiceImpl.class);

    /** The service used for communication with the analyzers. */
    private final AnalyzerPluginCommunicationService communicationService;

    /**
     * Construct the analyzer service.
     * 
     * @param communicationService
     *            the service used for communication with the analyzers
     */
    @Autowired
    public AnalyzerServiceImpl(
            final AnalyzerPluginCommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @Override
    public final void analyze(final ProbeValueDateDto probeValueDateDto) {
        Probe probe = Probe.findProbeByUuidEquals(probeValueDateDto
                .getProbeUUID());
        analyze(probe, probeValueDateDto.getDate(),
                probeValueDateDto.getValue());
    }

    @Override
    public final void analyze(final ProbeValue probeValue) {
        Probe probe = probeValue.getProbe();
        analyze(probe, probeValue.getDate(), probeValue.getValue());
    }

    /**
     * Dispatch a probe value to the configured analyzers.
     * 
     * @param probe
     *            the probe to which the value belongs
     * @param date
     *            the date when the value was received
     * @param value
     *            the value to analyze
     */
    private void analyze(final Probe probe, final Date date, final Double value) {
        for (Plugin plugin : probe.getPlugins()) {
            // create request
            AnalyzerRequestDto request = new AnalyzerRequestDto();
            request.setDate(date);
            request.setSavedState(plugin.getSavedState());
            request.setValue(value);

            // send probe to analyzer plugins
            AnalyzerResponseDto response;
            try {
                response = getCommunicationService().send(plugin.getUri(),
                        request);

                // save response
                plugin.setSavedState(response.getNewSavedState());
                plugin.setStatus(response.getStatus());
            } catch (ClientProtocolException e) {
                // TODO encapsulate and rethrow the exception
                LOG.error(e);
            } catch (IOException e) {
                // TODO encapsulate and rethrow the exception
                LOG.error(e);
            }
        }
    }

    /**
     * The service used for communication with the analyzers.
     * 
     * @return the communication service
     */
    private AnalyzerPluginCommunicationService getCommunicationService() {
        return this.communicationService;
    }

}
