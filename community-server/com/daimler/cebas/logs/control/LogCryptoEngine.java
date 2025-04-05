/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.CryptoTools
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.logs.control;

import com.daimler.cebas.common.CryptoTools;
import com.daimler.cebas.common.control.CEBASException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogCryptoEngine {
    private static final Logger LOG = Logger.getLogger(LogCryptoEngine.class.getName());

    private LogCryptoEngine() {
    }

    public static String hashWithSHA512(String logMessage) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] hashedString = messageDigest.digest(logMessage.getBytes());
            return Base64.getEncoder().encodeToString(hashedString);
        }
        catch (NoSuchAlgorithmException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASException(e.getMessage());
        }
    }

    public static String encryptAES(byte[] encryptionKey, byte[] text) {
        try {
            String string = Base64.getEncoder().encodeToString(CryptoTools.encryptAES((byte[])encryptionKey, (byte[])text));
            return string;
        }
        catch (Exception e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASException(e.getMessage());
        }
        finally {
            Arrays.fill(text, (byte)0);
        }
    }

    public static String decryptAES(byte[] decryptionKey, byte[] cipherText) {
        try {
            byte[] decryptAES = CryptoTools.decryptAES((byte[])decryptionKey, (byte[])cipherText);
            return new String(decryptAES, StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASException(e.getMessage());
        }
    }
}
