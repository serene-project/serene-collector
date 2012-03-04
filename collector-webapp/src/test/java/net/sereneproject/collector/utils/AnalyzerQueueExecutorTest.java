package net.sereneproject.collector.utils;

import net.sereneproject.collector.dto.ProbeValueDateDto;
import net.sereneproject.collector.service.AnalyzerService;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.npstrandberg.simplemq.MessageInput;
import com.npstrandberg.simplemq.MessageQueue;
import com.npstrandberg.simplemq.MessageQueueService;

public class AnalyzerQueueExecutorTest {

    private Mockery context;
    private MessageQueue queue;
    private AnalyzerQueueExecutor executor;
    private AnalyzerService analyzerService;
    private Thread executorThread;

    @Before
    public final void init() {
        this.context = new Mockery();
        this.queue = MessageQueueService.getMessageQueue("testQueue");
        this.analyzerService = this.context.mock(AnalyzerService.class);
        this.executor = new AnalyzerQueueExecutor(this.queue,
                this.analyzerService);
        this.executorThread = new Thread(this.executor, "analyzerQueueExecutor");
        this.executorThread.start();
    }

    @Test
    public final void messagesDispatched() throws InterruptedException {
        final ProbeValueDateDto pv = new ProbeValueDateDto();

        this.context.checking(new Expectations() {
            {
                oneOf(AnalyzerQueueExecutorTest.this.analyzerService).analyze(
                        pv);
            }
        });

        MessageInput msg = new MessageInput();
        msg.setObject(pv);

        synchronized (this.queue) {
            this.queue.send(msg);
            this.queue.notifyAll();
        }

        Thread.sleep(100);

        this.context.assertIsSatisfied();
    }

    @After
    public final void tearDown() {
        this.executorThread.interrupt();
    }

}
