/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificatePKIState
 *  com.daimler.cebas.common.control.vo.CEBASResult
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificatePKIState;
import com.daimler.cebas.common.control.vo.CEBASResult;

public abstract class CertificateSummary
extends CEBASResult {
    private String id;
    private String subject;
    private String authorityKeyIdentifier;
    private String issuer;
    private String serialNo;
    private String certificateType;
    private String userRole;
    private String targetVIN;
    private Long validTo;
    private Long validFrom;
    private String linkCertTs;
    private CertificatePKIState pkiState;

    public CertificateSummary(Certificate certificate) {
        this.id = certificate.getEntityId();
        this.subject = certificate.getSubject();
        this.authorityKeyIdentifier = certificate.getAuthorityKeyIdentifier();
        this.issuer = certificate.getIssuer();
        this.serialNo = certificate.getSerialNo();
        this.certificateType = certificate.getPKIRole();
        this.userRole = certificate.getUserRole();
        this.targetVIN = certificate.getTargetVIN();
        this.validTo = certificate.getValidTo().getTime();
        this.validFrom = certificate.getValidFrom().getTime();
        this.linkCertTs = certificate.getLinkCertTs();
        this.pkiState = certificate.getPkiState();
    }

    public String getId() {
        return this.id;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getAuthorityKeyIdentifier() {
        return this.authorityKeyIdentifier;
    }

    public String getIssuer() {
        return this.issuer;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public String getCertificateType() {
        return this.certificateType;
    }

    public String getUserRole() {
        return this.userRole;
    }

    public String getTargetVIN() {
        return this.targetVIN;
    }

    public Long getValidTo() {
        return this.validTo;
    }

    public Long getValidFrom() {
        return this.validFrom;
    }

    public String getLinkCertTs() {
        return this.linkCertTs;
    }

    public CertificatePKIState getPkiState() {
        return this.pkiState;
    }
}
