/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.configuration.control.AbstractConfigurator
 *  com.daimler.cebas.system.control.performance.PerformanceAuditManager
 *  com.daimler.cebas.system.entity.PerformanceAuditEntry
 *  com.daimler.cebas.users.control.Session
 *  javax.annotation.PostConstruct
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.context.annotation.RequestScope
 */
package com.daimler.cebas.system.control.performance;

import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.configuration.control.AbstractConfigurator;
import com.daimler.cebas.system.control.performance.PerformanceAuditManager;
import com.daimler.cebas.system.entity.PerformanceAuditEntry;
import com.daimler.cebas.users.control.Session;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.RequestScope;

@CEBASControl
@RequestScope
public class PerformanceAuditRequest {
    private Session session;
    private AbstractConfigurator configurator;
    private PerformanceAuditManager performanceAuditManager;
    private PerformanceAuditEntry performanceAuditEntry;

    @Autowired
    public PerformanceAuditRequest(Session session, AbstractConfigurator configurator, PerformanceAuditManager performanceAuditManager) {
        this.session = session;
        this.configurator = configurator;
        this.performanceAuditManager = performanceAuditManager;
    }

    @PostConstruct
    public void init() {
        this.performanceAuditEntry = new PerformanceAuditEntry();
    }

    public void setStartTime(Date startTimeDate) {
        this.performanceAuditEntry.setStartDate(startTimeDate);
    }

    public void setEndTime(Date endTimeDate) {
        this.performanceAuditEntry.setEndDate(endTimeDate);
    }

    public void setClassName(String className) {
        this.performanceAuditEntry.setClassName(className);
    }

    public void setMethodName(String methodName) {
        this.performanceAuditEntry.setMethodName(methodName);
    }

    public void setCorrelationId(String correlationID) {
        this.performanceAuditEntry.setCorrelation_id(correlationID);
    }

    public void add() {
        this.performanceAuditEntry.setUserName(this.session.getCurrentUser().getUserName());
        this.performanceAuditEntry.setProjectVersion(this.configurator.readProperty("info.build.version"));
        this.performanceAuditEntry.setProjectRevision(this.configurator.readProperty("info.build.svn.revision"));
        this.performanceAuditEntry.setDuration(Long.valueOf(this.performanceAuditEntry.getEndDate().getTime() - this.performanceAuditEntry.getStartDate().getTime()));
        this.performanceAuditManager.addToPerformanceList(this.performanceAuditEntry);
    }
}
