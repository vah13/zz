/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.system.control.validation.vo;

public enum CertificateTableColumns {
    DEFAULT("default"),
    ACTIVE_FOR_TESTING("activeForTesting"),
    SUBJECT("subject"),
    PKI_ROLE("pkirole"),
    USER_ROLE("userRole"),
    ISSUER("issuer"),
    SERIAL_NO("serialNo"),
    TARGET_ECU("targetECU"),
    TARGET_VIN("targetVIN"),
    VALID_TO("validTo"),
    VALIDITY_STRENGTHCOLOR("validityStrengthColor"),
    VALID_FROM("validFrom"),
    UNIQUE_ECU_ID("uniqueECUID"),
    SPECIAL_ECU("specialECU"),
    SUBJECT_PUBLIC_KEY("subjectPublicKey"),
    BASE_CERTIFICATE_ID("baseCertificateID"),
    ISSUER_SERIAL_NUMBER("issuerSerialNumber"),
    sUBJECT_KEY_IDENTIFIER("subjectKeyIdentifier"),
    AUTHORITY_KEY_IDENTIFIER("authorityKeyIdentifier"),
    SERVICES("services"),
    NONCE("nonce"),
    SIGNATURE("signature"),
    TARGET_SUBJECT_KEY_IDENTIFIER("targetSubjectKeyIdentifier");

    private String text;

    private CertificateTableColumns(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static boolean valueFromString(String column) {
        boolean isColumnValid = false;
        CertificateTableColumns[] certificateTableColumnsArray = CertificateTableColumns.values();
        int n = certificateTableColumnsArray.length;
        int n2 = 0;
        while (n2 < n) {
            CertificateTableColumns certificateTableColumns = certificateTableColumnsArray[n2];
            if (certificateTableColumns.getText().contains(column)) {
                isColumnValid = true;
                return isColumnValid;
            }
            isColumnValid = false;
            ++n2;
        }
        return isColumnValid;
    }
}
