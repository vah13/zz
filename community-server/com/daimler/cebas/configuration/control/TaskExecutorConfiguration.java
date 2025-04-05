/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.configuration.control.mdc.CebasThreadPoolTaskScheduler
 *  org.springframework.beans.factory.annotation.Qualifier
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.context.annotation.Primary
 *  org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
 */
package com.daimler.cebas.configuration.control;

import com.daimler.cebas.configuration.control.mdc.CebasThreadPoolTaskScheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class TaskExecutorConfiguration {
    @Value(value="${cebas.scheduler.tasks.poolsize}")
    private int taskPoolSize;

    @Bean
    @Primary
    @Qualifier(value="taskScheduler")
    public ThreadPoolTaskScheduler taskScheduler() {
        CebasThreadPoolTaskScheduler scheduler = new CebasThreadPoolTaskScheduler();
        scheduler.setPoolSize(this.taskPoolSize);
        scheduler.setThreadNamePrefix("CeBAS-Scheduler-");
        return scheduler;
    }
}
