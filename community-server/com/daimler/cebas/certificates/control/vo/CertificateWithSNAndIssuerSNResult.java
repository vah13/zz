/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.CertificateWithSNResult
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.CertificateWithSNResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Base64;

@ApiModel
public class CertificateWithSNAndIssuerSNResult
extends CertificateWithSNResult {
    @ApiModelProperty(dataType="java.lang.String", value="Issuer serial number encoded bytes in base64 format")
    private String issuerSerialNumber;

    public CertificateWithSNAndIssuerSNResult(String errorMessage) {
        super(errorMessage);
    }

    public CertificateWithSNAndIssuerSNResult(byte[] certificate, byte[] serialNumber) {
        super(certificate, serialNumber);
    }

    public CertificateWithSNAndIssuerSNResult(byte[] certificate, byte[] serialNumber, byte[] issuerSerialNumber) {
        super(certificate, serialNumber);
        this.issuerSerialNumber = Base64.getEncoder().encodeToString(issuerSerialNumber);
    }

    public String getIssuerSerialNumber() {
        return this.issuerSerialNumber;
    }
}
