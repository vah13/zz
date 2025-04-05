/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASProperty
 *  com.daimler.cebas.configuration.control.AbstractConfigurator
 *  com.daimler.cebas.system.control.performance.PerformanceAuditCleanUp
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Qualifier
 *  org.springframework.scheduling.TaskScheduler
 *  org.springframework.scheduling.Trigger
 *  org.springframework.scheduling.support.CronTrigger
 *  org.springframework.stereotype.Component
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.daimler.cebas.system.control.performance;

import com.daimler.cebas.common.control.CEBASProperty;
import com.daimler.cebas.configuration.control.AbstractConfigurator;
import com.daimler.cebas.system.control.performance.PerformanceAuditCleanUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PerformanceAuditCronJob {
    @Autowired
    private PerformanceAuditCleanUp performanceAuditCleanUp;
    @Autowired
    @Qualifier(value="taskScheduler")
    private TaskScheduler taskScheduler;
    @Autowired
    private AbstractConfigurator configurator;

    @Transactional(propagation=Propagation.REQUIRED)
    public void scheduleCleanUpPerformanceEntries() {
        String maxAgePerformanceEntry = this.configurator.readProperty(CEBASProperty.PERFORMANCE_AUDIT_ENTRY_MAX_AGE.name());
        String schedulerDeletePerformanceEntry = this.configurator.readProperty(CEBASProperty.PERFORMANCE_AUDIT_CLEANUP_JOB.name());
        CronTrigger cronTrigger = new CronTrigger(schedulerDeletePerformanceEntry);
        Runnable task = () -> this.performanceAuditCleanUp.deletePerformanceEntries(maxAgePerformanceEntry);
        this.taskScheduler.schedule(task, (Trigger)cronTrigger);
    }
}
