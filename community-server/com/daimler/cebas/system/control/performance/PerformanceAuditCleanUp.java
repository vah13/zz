/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.system.control.performance.PerformanceAuditRepository
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.daimler.cebas.system.control.performance;

import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.system.control.performance.PerformanceAuditRepository;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@CEBASControl
public class PerformanceAuditCleanUp {
    private PerformanceAuditRepository performanceAuditRepository;

    @Autowired
    public PerformanceAuditCleanUp(PerformanceAuditRepository performanceAuditRepository) {
        this.performanceAuditRepository = performanceAuditRepository;
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void deletePerformanceEntries(String maxAgeLogEntry) {
        Date maxDate = new Date(System.currentTimeMillis() - (long)Integer.valueOf(maxAgeLogEntry).intValue());
        this.performanceAuditRepository.deleteEntriesOlderThanDate(maxDate);
    }
}
