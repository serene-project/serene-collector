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
package net.sereneproject.collector.utils;

import java.util.concurrent.BlockingQueue;

import javax.annotation.Resource;

import net.sereneproject.collector.dto.ProbeValueDateDto;
import net.sereneproject.collector.service.AnalyzerService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Simple {@link Runnable} class that consumes a {@link MessageQueue} and send
 * it to the appropriate {@link AnalyzerService}.
 * 
 * @author gehel
 */
public class AnalyzerQueueExecutor implements Runnable {

    /** Logger. */
    private static final Logger LOG = Logger
            .getLogger(AnalyzerQueueExecutor.class);

    /** Queue of {@link ProbeValueDateDto} to be consumed. */
    @Resource(name = "probeToAnalyzeQueue")
    private BlockingQueue<ProbeValueDateDto> queue;

    /** Service to which we send the {@link ProbeValueDateDto}s. */
    private final AnalyzerService analyzerService;

    /**
     * Construct the executor.
     * 
     * @param analyzerService
     *            the service used to analyze probe values
     */
    @Autowired(required = true)
    public AnalyzerQueueExecutor(final AnalyzerService analyzerService) {
        this.analyzerService = analyzerService;
    }

    /**
     * Infinite loop that process messages.
     */
    @Override
    public final void run() {
        try {
            while (true) {
                ProbeValueDateDto pv = getQueue().take();
                if (pv == null) {
                    LOG.error("Message queue returned empty message, "
                            + "it should have returned a value to analyze.");
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Processing message [" + pv + "]");
                    }
                    getAnalyzerService().analyze(pv);
                }
            }
        } catch (InterruptedException ie) {
            LOG.error("Analyzer Queue Executor has been interrupted !");
            Thread.currentThread().interrupt();
        }
    }

    /**
     * The message queue.
     * 
     * @return the queue
     */
    private BlockingQueue<ProbeValueDateDto> getQueue() {
        return this.queue;
    }

    /**
     * The analyzer service.
     * 
     * @return the service
     */
    private AnalyzerService getAnalyzerService() {
        return this.analyzerService;
    }

    /**
     * Set the queue.
     * 
     * @param queue the queue
     */
    final void setQueue(final BlockingQueue<ProbeValueDateDto> queue) {
        this.queue = queue;
    }
}
