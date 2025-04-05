/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.configuration.control.BusinessDivisionMapping$1
 *  com.daimler.cebas.system.control.validation.vo.BusinessEnvironment
 */
package com.daimler.cebas.configuration.control;

import com.daimler.cebas.configuration.control.BusinessDivisionMapping;
import com.daimler.cebas.system.control.validation.vo.BusinessEnvironment;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public enum BusinessDivisionMapping {
    RD("E"),
    PRODUCTION("P"),
    SUPPLIER("S"),
    AFTER_SALES("D");

    private String mapping;

    private BusinessDivisionMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getMapping() {
        return this.mapping;
    }

    public static String getMapping(BusinessEnvironment businessEnv) {
        switch (1.$SwitchMap$com$daimler$cebas$system$control$validation$vo$BusinessEnvironment[businessEnv.ordinal()]) {
            case 1: {
                return RD.getMapping();
            }
            case 2: {
                return AFTER_SALES.getMapping();
            }
            case 3: {
                return SUPPLIER.getMapping();
            }
            case 4: {
                return PRODUCTION.getMapping();
            }
        }
        return "";
    }

    public static String getMapping(BusinessEnvironment businessEnv, String pkiEnv, String pkiBaseUrl) {
        String pki = BusinessDivisionMapping.getPKIMapping(pkiEnv, pkiBaseUrl);
        return BusinessDivisionMapping.getMapping(businessEnv) + pki;
    }

    public static String getPKIMapping(String pkiEnv, String pkiBaseUrl) {
        if (!BusinessDivisionMapping.isLocalhostUrl(pkiBaseUrl)) return pkiEnv.equals("PROD") ? "P" : "I";
        return "L";
    }

    public static boolean isLocalhostUrl(String url) {
        if (url == null) {
            return false;
        }
        try {
            String host = new URL(url).getHost();
            InetAddress addr = InetAddress.getByName(host);
            if (addr.isAnyLocalAddress()) return true;
            if (!addr.isLoopbackAddress()) return false;
            return true;
        }
        catch (MalformedURLException | UnknownHostException e) {
            return false;
        }
    }

    public static String getPKIMappingFull(String pkiEnv, String pkiBaseUrl) {
        if ("-".equals(pkiEnv)) {
            return "-";
        }
        if (!BusinessDivisionMapping.isLocalhostUrl(pkiBaseUrl)) return pkiEnv.equals("PROD") ? "PROD" : "INT";
        return "LOCAL";
    }
}
