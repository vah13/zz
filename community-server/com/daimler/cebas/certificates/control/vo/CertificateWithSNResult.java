/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.CertificateResult
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.CertificateResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Base64;

@ApiModel
public class CertificateWithSNResult
extends CertificateResult {
    @ApiModelProperty(dataType="java.lang.String", value="Serial number encoded bytes in base64 format")
    private String serialNumber;

    public CertificateWithSNResult(String errorMessage) {
        super(errorMessage);
    }

    public CertificateWithSNResult(byte[] certificate, byte[] serialNumber) {
        super(certificate);
        this.serialNumber = serialNumber != null ? Base64.getEncoder().encodeToString(serialNumber) : null;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }
}
