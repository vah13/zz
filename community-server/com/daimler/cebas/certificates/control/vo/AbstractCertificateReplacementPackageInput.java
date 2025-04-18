/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.ReplacementTarget
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.ReplacementTarget;
import io.swagger.annotations.ApiModelProperty;

public abstract class AbstractCertificateReplacementPackageInput {
    @ApiModelProperty(dataType="java.lang.String", required=true, value="Certificate bytes, Base64 encoded.")
    private String certificate;
    @ApiModelProperty(dataType="java.lang.String", value="The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes.")
    private String targetBackendCertSubjKeyId;
    @ApiModelProperty(dataType="java.lang.String", value="Unique ECU ID, not base64 encoded. Maximum length is 30 bytes.")
    private String uniqueEcuId;

    public AbstractCertificateReplacementPackageInput(String certificate, String targetBackendCertSubjKeyId, String uniqueEcuId) {
        this.certificate = certificate;
        this.targetBackendCertSubjKeyId = targetBackendCertSubjKeyId;
        this.uniqueEcuId = uniqueEcuId;
    }

    public String getCertificate() {
        return this.certificate;
    }

    public void setTargetBackendCertSubjKeyId(String targetBackendCertSubjKeyId) {
        this.targetBackendCertSubjKeyId = targetBackendCertSubjKeyId;
    }

    public String getTargetBackendCertSubjKeyId() {
        return this.targetBackendCertSubjKeyId;
    }

    public String getUniqueEcuId() {
        return this.uniqueEcuId;
    }

    @ApiModelProperty(hidden=true)
    public abstract ReplacementTarget getTarget();

    @ApiModelProperty(hidden=true)
    public abstract String getTargetVIN();
}
