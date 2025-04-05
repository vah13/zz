/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.vo.CEBASResult
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.common.control.vo.CEBASResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Base64;

@ApiModel
public class CertificateResult
extends CEBASResult {
    @ApiModelProperty(dataType="java.lang.String", value="Certificate encoded bytes in base64 format")
    private String certificateData;

    public CertificateResult(String errorMessage) {
        super(errorMessage);
    }

    public CertificateResult(byte[] certificateData) {
        this.certificateData = Base64.getEncoder().encodeToString(certificateData);
    }

    public String getCertificateData() {
        return this.certificateData;
    }
}
