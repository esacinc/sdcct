package gov.hhs.onc.sdcct.ws.impl;

import gov.hhs.onc.sdcct.ws.WsClient;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import javax.annotation.Nullable;

public abstract class AbstractWsClient<T, U> implements WsClient<T, U> {
    protected static class WsClientInvocationTask<T> implements Runnable {
        private CountDownLatch latch;
        private Callable<T> delegate;
        private T resp;
        private Exception exception;

        public WsClientInvocationTask(CountDownLatch latch, Callable<T> delegate) {
            this.latch = latch;
            this.delegate = delegate;
        }

        @Override
        public void run() {
            try {
                this.resp = delegate.call();
            } catch (Exception e) {
                this.exception = e;
            } finally {
                this.latch.countDown();
            }
        }

        public boolean hasException() {
            return (this.exception != null);
        }

        @Nullable
        public Exception getException() {
            return this.exception;
        }

        public boolean hasResponse() {
            return (this.resp != null);
        }

        @Nullable
        public T getResponse() {
            return this.resp;
        }
    }

    protected T delegate;

    protected AbstractWsClient(T delegate) {
        this.delegate = delegate;
    }

    @Override
    public T getDelegate() {
        return this.delegate;
    }
}
