/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.configuration.control.mdc.MdcRunnableScheduledFuture
 */
package com.daimler.cebas.configuration.control.mdc;

import com.daimler.cebas.configuration.control.mdc.MdcRunnableScheduledFuture;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

public class MdcDecoratorScheduledThreadPoolExecutor
extends ScheduledThreadPoolExecutor {
    private final String CEBAS_PACKAGE = "com.daimler.cebas";

    public MdcDecoratorScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
        if (!runnable.toString().contains("com.daimler.cebas")) return task;
        return new MdcRunnableScheduledFuture(task);
    }

    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> callable, RunnableScheduledFuture<V> task) {
        if (!callable.toString().contains("com.daimler.cebas")) return task;
        return new MdcRunnableScheduledFuture(task);
    }
}
