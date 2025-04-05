/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.security.jwt.vo;

public class JWTHeader {
    private String alg = "ES256";

    public String getAlg() {
        return this.alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }
}
