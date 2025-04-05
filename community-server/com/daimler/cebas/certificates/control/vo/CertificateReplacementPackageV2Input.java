/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.AbstractCertificateReplacementPackageInput
 *  com.daimler.cebas.certificates.control.vo.ReplacementTarget
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.AbstractCertificateReplacementPackageInput;
import com.daimler.cebas.certificates.control.vo.ReplacementTarget;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CertificateReplacementPackageV2Input
extends AbstractCertificateReplacementPackageInput {
    @ApiModelProperty(dataType="java.lang.String", required=true, value="Replacement target. E. g.: ECU, BACKEND, ROOT")
    private ReplacementTarget target;

    public CertificateReplacementPackageV2Input(String certificate, String targetBackendCertSubjKeyId, ReplacementTarget target, String uniqueEcuId) {
        super(certificate, targetBackendCertSubjKeyId, uniqueEcuId);
        this.target = target;
    }

    public ReplacementTarget getTarget() {
        return this.target;
    }

    public String getTargetVIN() {
        return null;
    }
}
