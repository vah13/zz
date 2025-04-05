/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.integration.vo.PKICertificateRequest
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 */
package com.daimler.cebas.certificates.integration.vo;

import com.daimler.cebas.certificates.integration.vo.PKICertificateRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true, value={"certificateType"})
public class PKIEnhancedCertificateRequest
extends PKICertificateRequest {
    private static final long serialVersionUID = -2536556647923614772L;
    private String holderCertificate;

    public PKIEnhancedCertificateRequest() {
    }

    public PKIEnhancedCertificateRequest(String enrollmentId, String csr, String caIdentifier, String certificateType, String holderCertificate) {
        super(enrollmentId, csr, caIdentifier, certificateType);
        this.holderCertificate = holderCertificate;
    }

    public PKIEnhancedCertificateRequest(String csr, String caIdentifier, String certificateType, String holderCertificate) {
        super(csr, caIdentifier, certificateType);
        this.holderCertificate = holderCertificate;
    }

    public String getHolderCertificate() {
        return this.holderCertificate;
    }

    public String toString() {
        return "PKIEnhancedCertificateRequest{holderCertificate='" + this.holderCertificate + '\'' + ", csr='" + this.csr + '\'' + ", caIdentifier='" + this.caIdentifier + '\'' + ", certificateType='" + this.certificateType + '\'' + ", enrollmentId='" + this.getEnrollmentId() + '\'' + '}';
    }
}
