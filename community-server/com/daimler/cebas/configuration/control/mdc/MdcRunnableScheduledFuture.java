/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.StartupStatus
 */
package com.daimler.cebas.configuration.control.mdc;

import com.daimler.cebas.common.control.StartupStatus;
import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.MDC;

public class MdcRunnableScheduledFuture<V>
implements RunnableScheduledFuture<V> {
    private static final Logger LOG = Logger.getLogger(MdcRunnableScheduledFuture.class.getName());
    RunnableScheduledFuture<V> delegate;
    private final Map<String, String> mdc;

    public MdcRunnableScheduledFuture(RunnableScheduledFuture<V> delegate) {
        this.delegate = delegate;
        this.mdc = MDC.getCopyOfContextMap();
    }

    @Override
    public void run() {
        if (!StartupStatus.isApplicationStarted()) {
            LOG.log(Level.INFO, "Application startup not finished yet - scheduled task execution will be skipped for now.");
        } else {
            if (this.mdc != null && !this.mdc.isEmpty()) {
                MDC.setContextMap(this.mdc);
            }
            this.delegate.run();
        }
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return this.delegate.getDelay(unit);
    }

    @Override
    public boolean isPeriodic() {
        return this.delegate.isPeriodic();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return this.delegate.cancel(mayInterruptIfRunning);
    }

    @Override
    public int compareTo(Delayed o) {
        return this.delegate.compareTo(o);
    }

    @Override
    public boolean isCancelled() {
        return this.delegate.isCancelled();
    }

    @Override
    public boolean isDone() {
        return this.delegate.isDone();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return this.delegate.get();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.delegate.get(timeout, unit);
    }
}
