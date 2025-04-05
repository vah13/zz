/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.validation.failure.ValidationState
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package com.daimler.cebas.certificates.integration.vo;

import com.daimler.cebas.certificates.control.validation.failure.ValidationState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class EnhancedRightsPermission
extends ValidationState {
    private String enrollmentId;
    private List<String> targetECU;
    private List<String> targetVIN;
    @JsonProperty(value="sid")
    private List<String> serviceIds;
    private String renewal;
    private String lifeTime;

    public EnhancedRightsPermission() {
    }

    public EnhancedRightsPermission(String enrollmentId, List<String> targetECU, List<String> targetVIN, List<String> serviceIds, String renewal, String lifeTime) {
        this.enrollmentId = enrollmentId;
        this.targetECU = targetECU;
        this.targetVIN = targetVIN;
        this.serviceIds = serviceIds;
        this.renewal = renewal;
        this.lifeTime = lifeTime;
    }

    public String getEnrollmentId() {
        return this.enrollmentId;
    }

    public List<String> getTargetECU() {
        return this.targetECU;
    }

    public List<String> getTargetVIN() {
        return this.targetVIN;
    }

    public List<String> getServiceIds() {
        return this.serviceIds;
    }

    public String getRenewal() {
        return this.renewal;
    }

    public String getLifeTime() {
        return this.lifeTime;
    }

    public String toString() {
        return "EnhancedRightsPermission{enrollmentId='" + this.enrollmentId + '\'' + ", targetECU=" + this.targetECU + ", targetVIN=" + this.targetVIN + ", serviceIds=" + this.serviceIds + ", renewal='" + this.renewal + '\'' + ", lifeTime='" + this.lifeTime + '\'' + '}';
    }
}
