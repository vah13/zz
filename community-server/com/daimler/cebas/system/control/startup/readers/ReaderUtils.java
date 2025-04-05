/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.system.control.exceptions.ConfigurationCheckException
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 */
package com.daimler.cebas.system.control.startup.readers;

import com.daimler.cebas.system.control.exceptions.ConfigurationCheckException;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReaderUtils {
    public static final String SECRET_VALID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?";
    public static final int SECRET_LENGTH = 32;
    private static final Log LOG = LogFactory.getLog(ReaderUtils.class);

    private ReaderUtils() {
    }

    public static byte[] getBytesFromImage(BufferedImage image, boolean overwriteHeaders) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write((RenderedImage)image, "bmp", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            if (!overwriteHeaders) return imageInByte;
            imageInByte[0] = 0;
            imageInByte[1] = 1;
            return imageInByte;
        }
        catch (IOException e) {
            LOG.error((Object)e);
            throw new ConfigurationCheckException("It was not possible to read data from the secret store: " + e.getMessage());
        }
    }

    public static String createNewRandomSecretString() {
        String password = RandomStringUtils.random(32, SECRET_VALID_CHARACTERS);
        if (ReaderUtils.isValidPassword(password)) return password;
        password = ReaderUtils.createNewRandomSecretString();
        return password;
    }

    public static boolean isValidPassword(String password) {
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasSpecial = !password.matches("[A-Za-z0-9]*");
        boolean hasNumeric = password.matches(".*\\d+.*");
        if (!hasUppercase) return false;
        if (!hasLowercase) return false;
        if (!hasSpecial) return false;
        if (hasNumeric) return true;
        return false;
    }
}
