/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.common.entity.AbstractEntity
 *  com.daimler.cebas.system.control.performance.PerformanceAuditRepository
 *  com.daimler.cebas.system.entity.PerformanceAuditEntry
 *  javax.annotation.PostConstruct
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Qualifier
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
 */
package com.daimler.cebas.system.control.performance;

import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.common.entity.AbstractEntity;
import com.daimler.cebas.system.control.performance.PerformanceAuditRepository;
import com.daimler.cebas.system.entity.PerformanceAuditEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@CEBASControl
public class PerformanceAuditManager {
    @Autowired
    private PerformanceAuditRepository performanceAuditRepository;
    private final List<PerformanceAuditEntry> performanceAuditEntryList = Collections.synchronizedList(new ArrayList());
    @Value(value="${PERSIST_PERFORMANCE_AUDIT_ENTRY}")
    private String enablePerformanceAuditEntriesPersistance;
    @Autowired
    @Qualifier(value="taskScheduler")
    private ThreadPoolTaskScheduler taskScheduler;

    @PostConstruct
    public void init() {
        if (!Boolean.TRUE.toString().equalsIgnoreCase(this.enablePerformanceAuditEntriesPersistance)) return;
        this.taskScheduler.scheduleWithFixedDelay(this::pushAuditPerformanceEntries, 3000L);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addToPerformanceList(PerformanceAuditEntry performanceAuditEntry) {
        List<PerformanceAuditEntry> list = this.performanceAuditEntryList;
        synchronized (list) {
            this.performanceAuditEntryList.add(performanceAuditEntry);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void pushAuditPerformanceEntries() {
        if (!Boolean.TRUE.toString().equalsIgnoreCase(this.enablePerformanceAuditEntriesPersistance)) return;
        List<PerformanceAuditEntry> list = this.performanceAuditEntryList;
        synchronized (list) {
            this.performanceAuditEntryList.forEach(performanceAuditEntryToAdd -> {
                PerformanceAuditEntry cfr_ignored_0 = (PerformanceAuditEntry)this.performanceAuditRepository.create((AbstractEntity)performanceAuditEntryToAdd);
            });
            this.performanceAuditEntryList.clear();
        }
    }
}
