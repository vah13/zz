/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.certificates.entity;

public class BackendContext {
    private final String zkNo;
    private final String ecuPackageTs;
    private String linkCertTs;

    public BackendContext(String zkNo, String ecuPackageTs, String linkCertTs) {
        this.zkNo = zkNo;
        this.ecuPackageTs = ecuPackageTs;
        this.linkCertTs = linkCertTs;
    }

    public String getZkNo() {
        return this.zkNo;
    }

    public String getEcuPackageTs() {
        return this.ecuPackageTs;
    }

    public String getLinkCertTs() {
        return this.linkCertTs;
    }
}
