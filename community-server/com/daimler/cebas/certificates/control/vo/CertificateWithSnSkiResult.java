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
public class CertificateWithSnSkiResult
extends CertificateWithSNResult {
    @ApiModelProperty(dataType="java.lang.String", value="Subject key identifier encoded bytes in base64 format")
    private String subjectKeyIdentifier;

    public CertificateWithSnSkiResult(String errorMessage) {
        super(errorMessage);
    }

    public CertificateWithSnSkiResult(byte[] certificate) {
        super(certificate, null);
        this.subjectKeyIdentifier = null;
    }

    public CertificateWithSnSkiResult(byte[] certificate, byte[] serialNumber, byte[] subjectKeyIdentifier) {
        super(certificate, serialNumber);
        this.subjectKeyIdentifier = subjectKeyIdentifier != null ? Base64.getEncoder().encodeToString(subjectKeyIdentifier) : null;
    }

    public String getSubjectKeyIdentifier() {
        return this.subjectKeyIdentifier;
    }
}
