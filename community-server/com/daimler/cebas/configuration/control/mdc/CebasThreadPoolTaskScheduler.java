/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.configuration.control.mdc.MdcDecoratorScheduledThreadPoolExecutor
 *  org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
 */
package com.daimler.cebas.configuration.control.mdc;

import com.daimler.cebas.configuration.control.mdc.MdcDecoratorScheduledThreadPoolExecutor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public class CebasThreadPoolTaskScheduler
extends ThreadPoolTaskScheduler {
    private static final long serialVersionUID = 7946527707921828825L;

    protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        return new MdcDecoratorScheduledThreadPoolExecutor(poolSize, threadFactory, rejectedExecutionHandler);
    }
}
