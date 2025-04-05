/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.certificates.integration.vo;

public class VsmCertificateRequest {
    private String caId;
    private String vsmCert;
    private String vin;

    public VsmCertificateRequest() {
    }

    public VsmCertificateRequest(String caId, String vsmCert, String vin) {
        this.caId = caId;
        this.vsmCert = vsmCert;
        this.vin = vin;
    }

    public void setCaId(String caId) {
        this.caId = caId;
    }

    public String getCaId() {
        return this.caId;
    }

    public String getVsmCert() {
        return this.vsmCert;
    }

    public void setVsmCert(String vsmCert) {
        this.vsmCert = vsmCert;
    }

    public String getVin() {
        return this.vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
}
