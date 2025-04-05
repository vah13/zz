/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.common.control;

import java.security.SecureRandom;
import java.util.Base64;

public final class GenerateSecureRandom {
    private GenerateSecureRandom() {
    }

    public static String generateSecureNumber(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
