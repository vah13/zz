/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 */
package com.daimler.cebas.certificates.integration.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PKICertificateResponse {
    private String certificate;

    public PKICertificateResponse() {
    }

    public PKICertificateResponse(String certificate) {
        this.certificate = certificate;
    }

    public String getCertificate() {
        return this.certificate;
    }

    public String toString() {
        return "PKICertificateResponse{certificate='" + this.certificate + '\'' + '}';
    }
}
