/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.certificates.integration.vo;

public class VsmCertificateBasedOnEzsRequest {
    private String caId;
    private String vsmSn;

    public VsmCertificateBasedOnEzsRequest() {
    }

    public VsmCertificateBasedOnEzsRequest(String caId, String vsmSn) {
        this.caId = caId;
        this.vsmSn = vsmSn;
    }

    public void setCaId(String caId) {
        this.caId = caId;
    }

    public String getCaId() {
        return this.caId;
    }

    public String getVsmSn() {
        return this.vsmSn;
    }

    public void setVsmSn(String vsmSn) {
        this.vsmSn = vsmSn;
    }
}
