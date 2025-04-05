/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.configuration.control;

public enum PkiUrlProperty {
    PKI_ENVIRONMENT("pki.environment", false),
    PKI_BASE_URL("pki.base.url", true),
    SUPPLIER_PKI_BASE_URL("supplier.pki.base.url", true),
    PKI_REVOKE_URL("pki.revoke.url", false),
    PKI_LOGOUT_URL("pki.logout.url", false),
    PKI_BACKEND_IDENTIFIERS_URL("pki.backends.url", false),
    PKI_BACKEND_IDENTIFIERS_URL_V2("pki.backends.url.v2", false),
    PKI_CERTIFICATES_CHAIN("pki.chain.url", false),
    PKI_USER_PERMISSIONS("pki.permissions.url", false),
    PKI_CERTIFICATES_URL("pki.certificate.end-user.url", false),
    PKI_TIME_CERTIFICATES_URL("pki.time.certificate.end-user.url", false),
    PKI_SECOCIS_CERTIFICATES_URL("pki.secocis.certificate.end-user.url", false),
    PKI_ENHANCED_RIGHTS_CERTIFICATES_URL("pki.enhanced.certificate.end-user.url", false),
    PKI_LINK_CERTIFICATES_URL("pki.link.certificate.end-user.url", false),
    PKI_CERTIFICATES_VSM_URL("pki.certificate.vsm.url", false),
    PKI_IDENTIFIERS_NON_VSM_URL("pki.identifiers.non.vsm.url", false),
    PKI_CERTIFICATES_NON_VSM_URL("pki.certificate.non.vsm.url", false),
    TEST_PKI_BASE_URL("test.pki.base.url", false),
    TEST_SUPPLIER_PKI_BASE_URL("test.supplier.pki.base.url", false),
    TEST_PKI_LOGOUT_URL("test.pki.logout.url", false),
    PROD_PKI_BASE_URL("prod.pki.base.url", false),
    PROD_SUPPLIER_PKI_BASE_URL("prod.supplier.pki.base.url", false),
    PROD_PKI_LOGOUT_URL("prod.pki.logout.url", false);

    private String property;
    private boolean runtimeOnly;

    private PkiUrlProperty(String property, boolean runtimeOnly) {
        this.property = property;
        this.runtimeOnly = runtimeOnly;
    }

    public String getProperty() {
        return this.property;
    }

    public boolean isRuntimeOnly() {
        return this.runtimeOnly;
    }

    public static PkiUrlProperty getFromProperty(String propertyName) {
        PkiUrlProperty[] v;
        PkiUrlProperty[] pkiUrlPropertyArray = v = PkiUrlProperty.values();
        int n = pkiUrlPropertyArray.length;
        int n2 = 0;
        while (n2 < n) {
            PkiUrlProperty p = pkiUrlPropertyArray[n2];
            if (p.getProperty().equals(propertyName)) {
                return p;
            }
            ++n2;
        }
        return null;
    }
}
