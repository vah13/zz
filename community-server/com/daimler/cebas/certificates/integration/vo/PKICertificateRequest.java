/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 */
package com.daimler.cebas.certificates.integration.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown=true, value={"certificateType"})
public class PKICertificateRequest
implements Serializable {
    private static final long serialVersionUID = 7012187663518573217L;
    @JsonIgnore
    private String internalCSRId;
    private String enrollmentId;
    protected String csr;
    protected String caIdentifier;
    protected String certificateType;
    @JsonIgnore
    protected boolean notBasedOnPermission;

    public PKICertificateRequest() {
    }

    public PKICertificateRequest(String csr, String caIdentifier, String certificateType) {
        this.csr = csr;
        this.caIdentifier = caIdentifier;
        this.certificateType = certificateType;
    }

    public PKICertificateRequest(String enrollmentId, String csr, String caIdentifier, String certificateType) {
        this.enrollmentId = enrollmentId;
        this.csr = csr;
        this.caIdentifier = caIdentifier;
        this.certificateType = certificateType;
    }

    public String getEnrollmentId() {
        return this.enrollmentId;
    }

    public String getCsr() {
        return this.csr;
    }

    public String getCaIdentifier() {
        return this.caIdentifier;
    }

    public String getCertificateType() {
        return this.certificateType;
    }

    public String toString() {
        return "PKICertificateRequest{enrollmentId='" + this.enrollmentId + '\'' + ", csr='" + this.csr + '\'' + ", caIdentifier='" + this.caIdentifier + '\'' + ", certificateType='" + this.certificateType + '\'' + '}';
    }

    public void setNotBasedOnPermission(boolean notBasedOnPermission) {
        this.notBasedOnPermission = notBasedOnPermission;
    }

    public boolean isNotBasedOnPermission() {
        return this.notBasedOnPermission;
    }

    public void setInternalCSRId(String internalCSRId) {
        this.internalCSRId = internalCSRId;
    }

    public String getInternalCSRId() {
        return this.internalCSRId;
    }
}
