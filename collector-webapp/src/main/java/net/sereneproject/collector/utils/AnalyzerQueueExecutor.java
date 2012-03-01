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
