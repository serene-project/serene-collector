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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import net.sereneproject.collector.dto.ProbeValueDateDto;
import net.sereneproject.collector.service.AnalyzerService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the {@link AnalyzerQueueExecutor}.
 * 
 * @author gehel
 */
public class AnalyzerQueueExecutorTest {

    /** Size of the queue used to transmit messages to the executor. */
    private static final int QUEUE_SIZE = 10;

    /** Name of the thread that will run the analyzer. */
    private static final String EXECUTOR_THREAD_NAME = "analyzerQueueExecutor";

    /**
     * A blocking queue that will be used to transmit messages to the executor.
     */
    private BlockingQueue<ProbeValueDateDto> queue;

    /** The executor that will be tested. */
    private AnalyzerQueueExecutor executor;

    /** A mock {@link AnalyzerService} used for tests. */
    private AnalyzerService analyzerService;

    /** The thread that will run the executor. */
    private Thread executorThread;

    /**
     * Initialize all needed objects and start an executor thread.
     */
    @Before
    public final void init() {
        this.queue = new ArrayBlockingQueue<ProbeValueDateDto>(QUEUE_SIZE);
        this.analyzerService = mock(AnalyzerService.class);
        this.executor = new AnalyzerQueueExecutor(this.analyzerService);
        this.executor.setQueue(this.queue);
        this.executorThread = new Thread(this.executor, EXECUTOR_THREAD_NAME);
        this.executorThread.start();
    }

    /**
     * Test sending a message to the executor and check if it is propagated to
     * the {@link AnalyzerService}.
     * 
     * @throws InterruptedException
     *             if thread is interrupted when sleeping
     */
    @Test
    public final void messagesDispatched() throws InterruptedException {
        final ProbeValueDateDto pv = new ProbeValueDateDto(UUID.randomUUID(),
                new Date(), 0.1);

        this.queue.add(pv);

        Thread.sleep(100);

        verify(this.analyzerService).analyze(pv);
    }

    /**
     * Shutdown executor thread.
     */
    @After
    public final void tearDown() {
        this.executorThread.interrupt();
    }

}
