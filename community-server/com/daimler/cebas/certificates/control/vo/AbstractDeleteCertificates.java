/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.common.entity.Versioned
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.common.entity.Versioned;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractDeleteCertificates
implements Versioned {
    private String certificateId;
    private boolean isCertificate;
    private CertificateType certificateType;
    private String serialNo;
    private String subjectKeyIdentifier;
    private String authKeyIdentifier;
    protected String publicKey;

    public AbstractDeleteCertificates(String certificateId, boolean isCertificate, CertificateType certificateType, String serialNo, String subjectKeyIdentifier, String authKeyIdentifier) {
        this.certificateId = certificateId;
        this.isCertificate = isCertificate;
        this.certificateType = certificateType;
        this.serialNo = serialNo;
        this.subjectKeyIdentifier = subjectKeyIdentifier;
        this.authKeyIdentifier = authKeyIdentifier;
    }

    public AbstractDeleteCertificates(String certificateId, boolean isCertificate, CertificateType certificateType, String authKeyIdentifier, String publicKey) {
        this.certificateId = certificateId;
        this.isCertificate = isCertificate;
        this.certificateType = certificateType;
        this.authKeyIdentifier = authKeyIdentifier;
        this.publicKey = publicKey;
    }

    public String getCertificateId() {
        return this.certificateId;
    }

    public boolean isCertificate() {
        return this.isCertificate;
    }

    @JsonIgnore
    public CertificateType getCertificateType() {
        return this.certificateType;
    }

    @JsonProperty(value="certificateType")
    public String getCertificateTypeForJson() {
        return this.certificateType.getText();
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public String getSubjectKeyIdentifier() {
        return this.subjectKeyIdentifier;
    }

    public String getAuthKeyIdentifier() {
        return this.authKeyIdentifier;
    }

    public String getPublicKey() {
        return this.publicKey;
    }
}
