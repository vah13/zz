/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.certificates.control.crypto;

import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

public class CustomPKCS8EncodedKeySpec
extends PKCS8EncodedKeySpec {
    private byte[] encodedKey;

    public CustomPKCS8EncodedKeySpec(byte[] encodedKey) {
        super(new byte[0]);
        this.encodedKey = encodedKey;
    }

    @Override
    public byte[] getEncoded() {
        return this.encodedKey;
    }

    public void destroy() {
        Arrays.fill(this.encodedKey, (byte)0);
    }
}
