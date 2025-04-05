/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.certificates.integration.vo;

import java.util.List;

public class NonVsmCertificateRequest {
    private String caId;
    private String ski;
    private String cn;
    private List<String> ecuUniqueIds;

    public NonVsmCertificateRequest() {
    }

    public NonVsmCertificateRequest(String caId, String ski, String cn, List<String> ecuUniqueIds) {
        this.caId = caId;
        this.ski = ski;
        this.cn = cn;
        this.ecuUniqueIds = ecuUniqueIds;
    }

    public void setCaId(String caId) {
        this.caId = caId;
    }

    public String getCaId() {
        return this.caId;
    }

    public String getSki() {
        return this.ski;
    }

    public void setSki(String ski) {
        this.ski = ski;
    }

    public String getCn() {
        return this.cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public List<String> getEcuUniqueIds() {
        return this.ecuUniqueIds;
    }

    public void setEcuUniqueIds(List<String> ecuUniqueIds) {
        this.ecuUniqueIds = ecuUniqueIds;
    }
}
