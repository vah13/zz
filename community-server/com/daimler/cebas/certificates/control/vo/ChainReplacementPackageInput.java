/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ChainReplacementPackageInput {
    @ApiModelProperty(dataType="java.lang.String", value="The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes. ZK Number is also supported (as 10 character string, NOT Base64 encoded).")
    private String targetBackendCertSubjKeyId;
    @ApiModelProperty(dataType="java.lang.String", value="The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes. ZK Number is also supported (as 10 character string, NOT Base64 encoded).")
    private String sourceBackendCertSubjKeyId;

    public ChainReplacementPackageInput() {
    }

    public ChainReplacementPackageInput(String targetBackendCertSubjKeyId, String sourceBackendCertSubjKeyId) {
        this.targetBackendCertSubjKeyId = targetBackendCertSubjKeyId;
        this.sourceBackendCertSubjKeyId = sourceBackendCertSubjKeyId;
    }

    public String getTargetBackendCertSubjKeyId() {
        return this.targetBackendCertSubjKeyId;
    }

    public void setTargetBackendCertSubjKeyId(String targetBackendCertSubjKeyId) {
        this.targetBackendCertSubjKeyId = targetBackendCertSubjKeyId;
    }

    public String getSourceBackendCertSubjKeyId() {
        return this.sourceBackendCertSubjKeyId;
    }

    public void setSourceBackendCertSubjKeyId(String sourceBackendCertSubjKeyId) {
        this.sourceBackendCertSubjKeyId = sourceBackendCertSubjKeyId;
    }
}
