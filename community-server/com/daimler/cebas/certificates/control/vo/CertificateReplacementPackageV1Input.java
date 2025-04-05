/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.ReplacementTarget
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.ReplacementTarget;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CertificateReplacementPackageV1Input {
    @ApiModelProperty(dataType="java.lang.String", required=true, value="Certificate bytes, Base64 encoded.")
    private String certificate;
    @ApiModelProperty(dataType="java.lang.String", required=true, value="Replacement target. E. g.: ECU, BACKEND, ROOT")
    private ReplacementTarget target;
    @ApiModelProperty(dataType="java.lang.String", value="The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes.")
    private String targetBackendCertSubjKeyId;

    public CertificateReplacementPackageV1Input() {
    }

    public CertificateReplacementPackageV1Input(String certificate, ReplacementTarget target, String targetBackendCertSubjKeyId) {
        this.certificate = certificate;
        this.target = target;
        this.targetBackendCertSubjKeyId = targetBackendCertSubjKeyId;
    }

    public String getCertificate() {
        return this.certificate;
    }

    public ReplacementTarget getTarget() {
        return this.target;
    }

    public String getTargetBackendCertSubjKeyId() {
        return this.targetBackendCertSubjKeyId;
    }
}
