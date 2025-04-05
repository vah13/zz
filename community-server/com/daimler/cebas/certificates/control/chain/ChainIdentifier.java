/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.certificates.control.chain;

public class ChainIdentifier {
    private String authorityKeyIdentifier;
    private String subjectKeyIdentifier;
    private String serialNo;
    private String subjectPublicKey;

    public ChainIdentifier(String authorityKeyIdentifier, String subjectKeyIdentifier, String serialNo, String subjectPublicKey) {
        this.authorityKeyIdentifier = authorityKeyIdentifier;
        this.subjectKeyIdentifier = subjectKeyIdentifier;
        this.serialNo = serialNo;
        this.subjectPublicKey = subjectPublicKey;
    }

    public String getAuthorityKeyIdentifier() {
        return this.authorityKeyIdentifier;
    }

    public String getSubjectKeyIdentifier() {
        return this.subjectKeyIdentifier;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public String getSubjectPublicKey() {
        return this.subjectPublicKey;
    }
}
