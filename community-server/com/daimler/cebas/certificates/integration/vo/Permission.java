/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.validation.failure.ValidationState
 *  com.daimler.cebas.certificates.integration.vo.EnhancedRightsPermission
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 */
package com.daimler.cebas.certificates.integration.vo;

import com.daimler.cebas.certificates.control.validation.failure.ValidationState;
import com.daimler.cebas.certificates.integration.vo.EnhancedRightsPermission;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.sql.Timestamp;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Permission
extends ValidationState {
    private String enrollmentId;
    private List<String> validCAs;
    private List<String> targetECU;
    private List<String> targetVIN;
    private String renewal;
    private String pkiRole;
    private String userRole;
    private String requester;
    private String assignee;
    private String assigner;
    private Timestamp validity;
    private String lifetime;
    private String review;
    private List<String> uniqueECUID;
    private String specialECU;
    private List<EnhancedRightsPermission> services;
    @JsonIgnore
    private boolean valid = true;

    public Permission() {
    }

    public Permission(String enrollmentId, List<String> validCAs, List<String> targetECU, List<String> targetVIN, String renewal, String pkiRole, String userRole, String requester, String assignee, String assigner, Timestamp validity, String lifetime, String review, List<String> uniqueECUID, String specialECU, List<EnhancedRightsPermission> services) {
        this.enrollmentId = enrollmentId;
        this.validCAs = validCAs;
        this.targetECU = targetECU;
        this.targetVIN = targetVIN;
        this.renewal = renewal;
        this.pkiRole = pkiRole;
        this.userRole = userRole;
        this.requester = requester;
        this.assignee = assignee;
        this.assigner = assigner;
        this.validity = validity;
        this.lifetime = lifetime;
        this.review = review;
        this.services = services;
        this.uniqueECUID = uniqueECUID;
        this.specialECU = specialECU;
    }

    public String getEnrollmentId() {
        return this.enrollmentId;
    }

    public String getRequester() {
        return this.requester;
    }

    public String getAssignee() {
        return this.assignee;
    }

    public String getAssigner() {
        return this.assigner;
    }

    public String getUserRole() {
        return this.userRole;
    }

    public String getPkiRole() {
        return this.pkiRole;
    }

    public Timestamp getValidity() {
        return this.validity;
    }

    public List<String> getTargetECU() {
        return this.targetECU;
    }

    public List<String> getTargetVIN() {
        return this.targetVIN;
    }

    public String getLifetime() {
        return this.lifetime;
    }

    public String getRenewal() {
        return this.renewal;
    }

    public String getReview() {
        return this.review;
    }

    public List<EnhancedRightsPermission> getServices() {
        return this.services;
    }

    public List<String> getUniqueECUID() {
        return this.uniqueECUID;
    }

    public String getSpecialECU() {
        return this.specialECU;
    }

    public List<String> getValidCAs() {
        return this.validCAs;
    }

    public void setValidCAs(List<String> validCAs) {
        this.validCAs = validCAs;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return this.valid;
    }

    public String toString() {
        return "Permission{enrollmentId='" + this.enrollmentId + '\'' + ", requester='" + this.requester + '\'' + ", validCAs='" + this.validCAs + '\'' + ", assignee='" + this.assignee + '\'' + ", assigner='" + this.assigner + '\'' + ", userRole='" + this.userRole + '\'' + ", pkiRole='" + this.pkiRole + '\'' + ", validity=" + this.validity + ", targetECU=" + this.targetECU + ", targetVIN=" + this.targetVIN + ", lifetime='" + this.lifetime + '\'' + ", renewal='" + this.renewal + '\'' + ", review='" + this.review + '\'' + ", services=" + this.services + ", uniqueECUID='" + this.uniqueECUID + "', specialECU='" + this.specialECU + '}';
    }
}
