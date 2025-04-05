/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.vo.CEBASResult
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.common.control.vo.CEBASResult;

public class EcuSignRequestResult
extends CEBASResult {
    private String signature;
    private String ecuCertificate;
    private Long expirationDate;
    private String serialNumber;

    public EcuSignRequestResult() {
    }

    public EcuSignRequestResult(String signature, String ecuCertificate, Long expirationDate, String serialNumber) {
        this.signature = signature;
        this.ecuCertificate = ecuCertificate;
        this.expirationDate = expirationDate;
        this.serialNumber = serialNumber;
    }

    public String getSignature() {
        return this.signature;
    }

    public String getEcuCertificate() {
        return this.ecuCertificate;
    }

    public Long getExpirationDate() {
        return this.expirationDate;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }
}
