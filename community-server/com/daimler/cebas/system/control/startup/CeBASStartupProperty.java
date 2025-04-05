/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.system.control.startup;

import java.util.HashSet;
import java.util.Set;

public enum CeBASStartupProperty {
    SECRET("SECRET", true, true, true, true, false, false),
    PKCS12_DEFAULT_PASSWORD("PKCS12_DEFAULT_PASSWORD", true, true, true, true, false, false),
    PKCS12_PACKAGE_PASSWORD("PKCS12_PACKAGE_PASSWORD", true, true, true, true, false, false),
    SPRING_DATASOURCE_PASSWORD("spring.datasource.password", true, true, true, true, false, false),
    SPRING_DATASOURCE_USERNAME("spring.datasource.username", true, true, true, true, false, false),
    SECURITY_OAUTH2_CLIENT_CLIENT_ID("spring.security.oauth2.client.registration.gas.client-id", false, true, true, true, false, false),
    SECURITY_OAUTH2_CLIENT_SECRET("spring.security.oauth2.client.registration.gas.client-secret", false, true, true, true, false, false),
    TEST_OIDC_CLIENT_ID("spring.security.oauth2.client.registration.gas.client-id", true, false, true, false, false, false),
    PROD_OIDC_CLIENT_ID("spring.security.oauth2.client.registration.gas.client-id", true, false, false, true, false, false),
    JWT_KEY("jwt.private.key", false, true, true, true, false, false),
    CSR_ENROLLMENT_ID("csr.enrollmentId", false, true, true, true, false, false),
    CSR_SUBJECT("csr.subject", false, true, true, true, true, true);

    private final String property;
    private boolean required;
    private boolean log;
    private boolean sigModul;
    private boolean zenzefi;
    private boolean test;
    private boolean prod;

    private CeBASStartupProperty(String property, boolean zenzefi, boolean sigModul, boolean test, boolean prod, boolean required, boolean log) {
        this.property = property;
        this.sigModul = sigModul;
        this.zenzefi = zenzefi;
        this.test = test;
        this.prod = prod;
        this.required = required;
        this.log = log;
    }

    public String getProperty() {
        return this.property;
    }

    public boolean isRequired() {
        return this.required;
    }

    public boolean isLog() {
        return this.log;
    }

    public boolean isTestEnvironmentRelevant() {
        return this.test;
    }

    public boolean isProdEnvironmentRelevant() {
        return this.prod;
    }

    public static Set<CeBASStartupProperty> getSigModul() {
        HashSet<CeBASStartupProperty> result = new HashSet<CeBASStartupProperty>();
        CeBASStartupProperty[] ceBASStartupPropertyArray = CeBASStartupProperty.values();
        int n = ceBASStartupPropertyArray.length;
        int n2 = 0;
        while (n2 < n) {
            CeBASStartupProperty p = ceBASStartupPropertyArray[n2];
            if (p.sigModul) {
                result.add(p);
            }
            ++n2;
        }
        return result;
    }

    public static Set<CeBASStartupProperty> getZenzefi() {
        HashSet<CeBASStartupProperty> result = new HashSet<CeBASStartupProperty>();
        CeBASStartupProperty[] ceBASStartupPropertyArray = CeBASStartupProperty.values();
        int n = ceBASStartupPropertyArray.length;
        int n2 = 0;
        while (n2 < n) {
            CeBASStartupProperty p = ceBASStartupPropertyArray[n2];
            if (p.zenzefi) {
                result.add(p);
            }
            ++n2;
        }
        return result;
    }
}
