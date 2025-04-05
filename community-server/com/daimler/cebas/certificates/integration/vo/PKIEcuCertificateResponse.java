/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 */
package com.daimler.cebas.certificates.integration.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PKIEcuCertificateResponse {
    private String ecuCert;

    public PKIEcuCertificateResponse() {
    }

    public PKIEcuCertificateResponse(String ecuCert) {
        this.ecuCert = ecuCert;
    }

    public String getEcuCert() {
        return this.ecuCert;
    }

    public String toString() {
        return "PKIEcuCertificateResponse{ecuCert='" + this.ecuCert + '\'' + '}';
    }
}
