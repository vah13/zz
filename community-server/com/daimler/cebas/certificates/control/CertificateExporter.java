/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.certificates.control;

import java.util.Formatter;
import org.apache.commons.lang3.StringUtils;

public class CertificateExporter {
    private CertificateExporter() {
    }

    public static String exportToConfigurator5Format(byte[] cert) {
        Formatter formatter = new Formatter();
        byte[] byArray = cert;
        int n = byArray.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) {
                String hexFormat = formatter.toString();
                formatter.close();
                return hexFormat;
            }
            byte b = byArray[n2];
            formatter.format("%02x", b);
            ++n2;
        }
    }

    public static String exportToCArrayFormat(byte[] cert) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unsigned char DATA[" + cert.length + "] = " + System.lineSeparator() + "{");
        for (int k = 0; k < cert.length - 1; ++k) {
            String value = (k % 22 == 0 ? System.lineSeparator() : "") + "0x" + String.format("%02X", cert[k]) + ", ";
            stringBuilder.append(value);
        }
        stringBuilder.append(((cert.length - 1) % 22 == 0 ? System.lineSeparator() : "") + "0x" + String.format("%02X ", cert[cert.length - 1]) + System.lineSeparator() + "};");
        return stringBuilder.toString();
    }

    public static String exportToCANoeFormat(byte[] cert) {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] byArray = cert;
        int n = byArray.length;
        int n2 = 0;
        while (n2 < n) {
            byte certByte = byArray[n2];
            stringBuilder.append("0x").append(String.format("%02X", certByte)).append(" ");
            ++n2;
        }
        return StringUtils.chop(stringBuilder.toString());
    }
}
