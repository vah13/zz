/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.security;

import com.daimler.cebas.common.control.CEBASException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class EncryptedString {
    private static final Logger LOG = Logger.getLogger(EncryptedString.class.getName());
    private byte[] encryptedSecret;
    private static Cipher cipher;
    private KeyGenerator keyGenerator;
    private SecretKey secretKey;

    public void setValue(String value) {
        if (value == null) {
            this.destroy();
            this.encryptedSecret = null;
        } else {
            try {
                this.secretKey = this.getKeyGenerator().generateKey();
                this.getCipher().init(1, this.secretKey);
                this.encryptedSecret = this.getCipher().doFinal(value.getBytes(StandardCharsets.UTF_8));
            }
            catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
                LOG.log(Level.FINEST, e.getMessage(), e);
                throw new CEBASException(e.getMessage());
            }
        }
    }

    public String getValue() {
        if (this.encryptedSecret == null) {
            return null;
        }
        try {
            this.getCipher().init(2, this.secretKey);
            byte[] decryptedByte = this.getCipher().doFinal(this.encryptedSecret);
            String decryptedText = new String(decryptedByte, StandardCharsets.UTF_8);
            return decryptedText;
        }
        catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASException(e.getMessage());
        }
    }

    public void destroy() {
        if (this.encryptedSecret == null) return;
        Arrays.fill(this.encryptedSecret, (byte)0);
    }

    private synchronized KeyGenerator getKeyGenerator() throws NoSuchAlgorithmException {
        if (this.keyGenerator != null) return this.keyGenerator;
        SecureRandom rand = new SecureRandom();
        this.keyGenerator = KeyGenerator.getInstance("AES");
        this.keyGenerator.init(256, rand);
        return this.keyGenerator;
    }

    private synchronized Cipher getCipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
        if (cipher != null) return cipher;
        cipher = Cipher.getInstance("AES");
        return cipher;
    }
}
