/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 */
package com.daimler.cebas.certificates.integration.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PKILinkCertificateResponse {
    private String linkCert;

    public PKILinkCertificateResponse() {
    }

    public PKILinkCertificateResponse(String linkCert) {
        this.linkCert = linkCert;
    }

    public String getLinkCert() {
        return this.linkCert;
    }

    public String toString() {
        return "PKILinkCertificateResponse{linkCert='" + this.linkCert + '\'' + '}';
    }
}
