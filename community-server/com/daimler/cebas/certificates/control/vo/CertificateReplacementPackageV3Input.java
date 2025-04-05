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
public class CertificateReplacementPackageV3Input
extends AbstractCertificateReplacementPackageInput {
    private ReplacementTarget determinedTarget;
    @ApiModelProperty(dataType="java.lang.String", value="The target VIN. The size of the field is 17 characters.")
    private String targetVIN;

    public CertificateReplacementPackageV3Input() {
        super("", "", "");
    }

    public CertificateReplacementPackageV3Input(String certificate, String targetBackendCertSubjKeyId, String uniqueEcuId) {
        super(certificate, targetBackendCertSubjKeyId, uniqueEcuId);
    }

    public CertificateReplacementPackageV3Input(String certificate, String targetBackendCertSubjKeyId, ReplacementTarget target, String uniqueEcuId) {
        this(certificate, targetBackendCertSubjKeyId, uniqueEcuId);
        this.determinedTarget = target;
    }

    public CertificateReplacementPackageV3Input(String certificate, String targetBackendCertSubjKeyId, String uniqueEcuId, String targetVIN) {
        this(certificate, targetBackendCertSubjKeyId, uniqueEcuId);
        this.targetVIN = targetVIN;
    }

    public CertificateReplacementPackageV3Input(String certificate, String targetBackendCertSubjKeyId, ReplacementTarget target, String uniqueEcuId, String targetVIN) {
        this(certificate, targetBackendCertSubjKeyId, target, uniqueEcuId);
        this.targetVIN = targetVIN;
    }

    public String getTargetVIN() {
        return this.targetVIN;
    }

    public ReplacementTarget getTarget() {
        return this.determinedTarget;
    }

    @ApiModelProperty(hidden=true)
    public void setDeterminedTarget(ReplacementTarget determinedTarget) {
        this.determinedTarget = determinedTarget;
    }
}
