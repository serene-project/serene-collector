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

import net.sereneproject.collector.dto.ProbeValueDateDto;
import net.sereneproject.collector.service.AnalyzerService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.npstrandberg.simplemq.Message;
import com.npstrandberg.simplemq.MessageQueue;

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
    private final MessageQueue queue;

    /** Service to which we send the {@link ProbeValueDateDto}s. */
    private final AnalyzerService analyzerService;

    /**
     * Construct the executor.
     * 
     * @param queue
     *            a queue of {@link ProbeValueDateDto}
     * @param analyzerService
     *            the service used to analyze probe values
     */
    @Autowired(required = true)
    public AnalyzerQueueExecutor(final MessageQueue queue,
            final AnalyzerService analyzerService) {
        this.queue = queue;
        this.analyzerService = analyzerService;
    }

    /**
     * Infinite loop that process messages.
     */
    @Override
    public final void run() {
        while (true) {
            Message msg = getQueue().receiveAndDelete();
            if (msg != null) {
                ProbeValueDateDto pv = (ProbeValueDateDto) msg.getObject();
                if (pv == null) {
                    LOG.error("Message queue returned empty message, "
                            + "it should have returned a value to analyze.");
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Processing message [" + pv + "]");
                }
                getAnalyzerService().analyze(pv);
            } else {
                try {
                    synchronized (getQueue()) {
                        getQueue().wait();
                    }
                } catch (InterruptedException ie) {
                    LOG.error("Analyzer Queue Executor has been interrupted !",
                            ie);
                }
            }
        }
    }

    /**
     * The message queue.
     * 
     * @return
     */
    private MessageQueue getQueue() {
        return this.queue;
    }

    /**
     * The analyzer service.
     * 
     * @return
     */
    private AnalyzerService getAnalyzerService() {
        return this.analyzerService;
    }
}
