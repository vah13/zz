/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.common.control;

import com.daimler.cebas.common.control.CEBASException;
import java.util.Base64;
import java.util.Locale;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

public final class HexUtil {
    public static final String GROUP_SEPARATOR = "-";
    private static final String _00_GROUP = "00";
    protected static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private HexUtil() {
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3];
        int j = 0;
        while (j < bytes.length) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = HEX_ARRAY[v >>> 4];
            hexChars[j * 3 + 1] = HEX_ARRAY[v & 0xF];
            if (j < bytes.length - 1) {
                hexChars[j * 3 + 2] = 45;
            }
            ++j;
        }
        return new String(hexChars).trim();
    }

    public static String base64ToHex(String base64) {
        byte[] decode = Base64.getDecoder().decode(base64);
        return HexUtil.bytesToHex(decode);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = (s = s.replace(GROUP_SEPARATOR, "")).length();
        if (len % 2 != 0) {
            return new byte[0];
        }
        byte[] data = new byte[len / 2];
        int i = 0;
        while (i < len) {
            data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
            i += 2;
        }
        return data;
    }

    public static String hexStringToSeparatedHexString(String value) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        value = RegExUtils.removeAll(value, "[-\\s]");
        value = value.toUpperCase(Locale.ENGLISH);
        boolean isHex = value.matches("[0-9A-F]+");
        boolean isEven = value.length() % 2 == 0;
        if (!isHex) throw new CEBASException("The string " + value + " is not a valid hex string and can not be converted.", "000541X");
        if (!isEven) {
            throw new CEBASException("The string " + value + " is not a valid hex string and can not be converted.", "000541X");
        }
        value = RegExUtils.replaceAll(value, "(.{2})", "$1-");
        value = StringUtils.stripEnd(value, GROUP_SEPARATOR);
        return value;
    }

    public boolean hexStringsEqual(String a, String b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null) return false;
        if (b == null) {
            return false;
        }
        a = RegExUtils.removeAll(a, "[-\\s]");
        b = RegExUtils.removeAll(b, "[-\\s]");
        return a.equalsIgnoreCase(b);
    }

    public static String omitLeadingZeros(String hexString) {
        if (hexString == null) {
            return null;
        }
        if (_00_GROUP.equals(hexString)) {
            return hexString;
        }
        String[] split = hexString.split(GROUP_SEPARATOR);
        boolean foundNonZero = false;
        if (!split[0].equals(_00_GROUP)) return hexString;
        StringBuilder buffer = new StringBuilder();
        int i = 1;
        while (i < split.length) {
            if (!split[i].equals(_00_GROUP) || !split[i - 1].equals(_00_GROUP) || foundNonZero) {
                foundNonZero = true;
                if (i == split.length - 1) {
                    buffer.append(split[i]);
                } else {
                    buffer.append(split[i] + GROUP_SEPARATOR);
                }
            }
            ++i;
        }
        return buffer.toString();
    }

    public static String hexToBase64(String someValue) {
        return Base64.getEncoder().encodeToString(HexUtil.hexStringToByteArray(someValue));
    }
}
