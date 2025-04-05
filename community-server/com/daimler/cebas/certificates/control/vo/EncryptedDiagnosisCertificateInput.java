/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.certificates.control.vo;

public class EncryptedDiagnosisCertificateInput {
    private String testerPublicKey;

    public EncryptedDiagnosisCertificateInput() {
    }

    public EncryptedDiagnosisCertificateInput(String testerPublicKey) {
        this.testerPublicKey = testerPublicKey;
    }

    public String getTesterPublicKey() {
        return this.testerPublicKey;
    }
}
