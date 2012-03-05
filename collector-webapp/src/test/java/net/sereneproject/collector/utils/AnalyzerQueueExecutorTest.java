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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import net.sereneproject.collector.dto.ProbeValueDateDto;
import net.sereneproject.collector.service.AnalyzerService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AnalyzerQueueExecutorTest {

    private BlockingQueue<ProbeValueDateDto> queue;
    private AnalyzerQueueExecutor executor;
    private AnalyzerService analyzerService;
    private Thread executorThread;

    @Before
    public final void init() {
        this.queue = new ArrayBlockingQueue<ProbeValueDateDto>(10);
        this.analyzerService = mock(AnalyzerService.class);
        this.executor = new AnalyzerQueueExecutor(this.analyzerService);
        this.executor.setQueue(this.queue);
        this.executorThread = new Thread(this.executor, "analyzerQueueExecutor");
        this.executorThread.start();
    }

    @Test
    public final void messagesDispatched() throws InterruptedException {
        final ProbeValueDateDto pv = new ProbeValueDateDto();

        this.queue.add(pv);

        Thread.sleep(100);

        verify(this.analyzerService).analyze(pv);
    }

    @After
    public final void tearDown() {
        this.executorThread.interrupt();
    }

}
