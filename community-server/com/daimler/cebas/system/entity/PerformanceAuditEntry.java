/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.entity.AbstractEntity
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.NamedQueries
 *  javax.persistence.NamedQuery
 *  javax.persistence.Table
 *  javax.persistence.Transient
 */
package com.daimler.cebas.system.entity;

import com.daimler.cebas.common.entity.AbstractEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="r_performance_audit_entry")
@NamedQueries(value={@NamedQuery(name="PerformanceAuditEntry_FIND_LOGS_NEWER_THAN_TIMESTAMP ", query="select p from PerformanceAuditEntry p where p.createTimestamp >:createTimestamp")})
public class PerformanceAuditEntry
extends AbstractEntity {
    private static final long serialVersionUID = -5931344564447595458L;
    public static final String CLASS_NAME = "PerformanceAuditEntry";
    public static final String R_PERFORMANCE_AUDIT_ENTRY = "r_performance_audit_entry";
    public static final String NAMED_QUERY_NEWER_THAN_DATE = "PerformanceAuditEntry_FIND_LOGS_NEWER_THAN_TIMESTAMP ";
    public static final String QUERY_NEWER_THAN_TIMESTAMP = "select p from PerformanceAuditEntry p where p.createTimestamp >:createTimestamp";
    private static final String F_USER_NAME = "f_user_name";
    private static final String F_TARGET_CLASS_NAME = "f_target_class_name";
    private static final String F_TARGET_METHOD_NAME = "f_target_method_name";
    private static final String F_DURATION = "f_duration";
    private static final String F_PROJECT_VERSION = "f_project_version";
    private static final String F_PROJECT_REVISION = "f_project_revision";
    private static final String F_CORRELATION_ID = "f_correlation_id";
    private static final int LENGTH_128 = 128;
    private static final int LENGTH_256 = 256;
    private static final int LENGTH_50 = 50;
    private static final int LENGTH_200 = 200;
    private static final int LENGTH_36 = 36;
    @Column(name="f_user_name", length=128)
    private String userName;
    @Column(name="f_target_class_name", nullable=false, length=256)
    private String className;
    @Column(name="f_target_method_name", nullable=false, length=256)
    private String methodName;
    @Column(name="f_duration", nullable=false)
    private Long duration;
    @Column(name="f_project_version", nullable=false, length=50)
    private String projectVersion;
    @Column(name="f_project_revision", nullable=false, length=200)
    private String projectRevision;
    @Column(name="f_correlation_id", length=36)
    private String correlation_id;
    @Transient
    private Date startDate;
    @Transient
    private Date endDate;

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Long getDuration() {
        return this.duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getProjectVersion() {
        return this.projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    public String getProjectRevision() {
        return this.projectRevision;
    }

    public void setProjectRevision(String projectRevision) {
        this.projectRevision = projectRevision;
    }

    public String getCorrelation_id() {
        return this.correlation_id;
    }

    public void setCorrelation_id(String correlation_id) {
        this.correlation_id = correlation_id;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
