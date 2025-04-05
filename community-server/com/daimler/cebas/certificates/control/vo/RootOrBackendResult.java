/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.RootOrBackend
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.common.control.vo.CEBASResult
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.RootOrBackend;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.common.control.vo.CEBASResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Base64;

@ApiModel
public class RootOrBackendResult
extends CEBASResult {
    @ApiModelProperty(dataType="java.lang.String", value="Certificate type")
    private String type;
    @ApiModelProperty(dataType="java.lang.String", value="Certificate encoded bytes in base64 format")
    private String certificate;

    public RootOrBackendResult(String errorMessage) {
        super(errorMessage);
    }

    public RootOrBackendResult(String type, byte[] certificate) {
        this.type = type;
        this.certificate = Base64.getEncoder().encodeToString(certificate);
    }

    public RootOrBackendResult(Certificate certificate) {
        this.type = RootOrBackend.fromType((CertificateType)certificate.getType()).name();
        this.certificate = Base64.getEncoder().encodeToString(certificate.getCertificateData().getOriginalBytes());
    }

    public String getType() {
        return this.type;
    }

    public String getCertificate() {
        return this.certificate;
    }
}
