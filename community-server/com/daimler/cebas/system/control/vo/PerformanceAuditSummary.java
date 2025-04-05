/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.system.entity.PerformanceAuditEntry
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.system.control.vo;

import com.daimler.cebas.system.entity.PerformanceAuditEntry;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

public class PerformanceAuditSummary {
    private String id;
    private String createUser;
    private Long createTimestamp;
    private String updateUser;
    @ApiModelProperty(dataType="java.lang.String")
    private Date updateTimestamp;
    private String userName;
    private String className;
    private String methodName;
    private Long duration;
    private String projectVersion;
    private String projectRevision;
    private String correlation_id;

    public PerformanceAuditSummary(PerformanceAuditEntry performanceAuditEntry) {
        this.id = performanceAuditEntry.getEntityId();
        this.createUser = performanceAuditEntry.getCreateUser();
        this.createTimestamp = performanceAuditEntry.getCreateTimestamp().getTime();
        this.updateUser = performanceAuditEntry.getUpdateUser();
        this.updateTimestamp = performanceAuditEntry.getUpdateTimestamp();
        this.userName = performanceAuditEntry.getUserName();
        this.className = performanceAuditEntry.getClassName();
        this.methodName = performanceAuditEntry.getMethodName();
        this.duration = performanceAuditEntry.getDuration();
        this.projectVersion = performanceAuditEntry.getProjectVersion();
        this.projectRevision = performanceAuditEntry.getProjectRevision();
        this.correlation_id = performanceAuditEntry.getCorrelation_id();
    }

    public String getId() {
        return this.id;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public Long getCreateTimestamp() {
        return this.createTimestamp;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }

    public Date getUpdateTimestamp() {
        return this.updateTimestamp;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getClassName() {
        return this.className;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public Long getDuration() {
        return this.duration;
    }

    public String getProjectVersion() {
        return this.projectVersion;
    }

    public String getProjectRevision() {
        return this.projectRevision;
    }

    public String getCorrelation_id() {
        return this.correlation_id;
    }
}
