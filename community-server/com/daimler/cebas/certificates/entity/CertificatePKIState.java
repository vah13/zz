/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.fasterxml.jackson.annotation.JsonValue
 */
package com.daimler.cebas.certificates.entity;

import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

public enum CertificatePKIState {
    ACTIVE("active"),
    PREACTIVE("preactivation"),
    NONE("");

    private String value;

    private CertificatePKIState(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }

    public static CertificatePKIState getFromValue(String value) {
        return Stream.of(CertificatePKIState.values()).filter(pkiState -> pkiState.getValue().equals(value)).findFirst().orElseThrow(() -> new CEBASCertificateException("Invalid certificate pki state: " + value));
    }
}
