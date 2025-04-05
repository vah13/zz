/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import io.swagger.annotations.ApiModelProperty;

public abstract class AbstractSecOCISInput {
    @ApiModelProperty(dataType="java.lang.String", value="Certificate bytes, Base64 encoded.", required=true)
    private String ecuCertificate;
    @ApiModelProperty(dataType="java.lang.String", value="The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes.", required=true)
    private String backendCertSubjKeyId;
    @ApiModelProperty(dataType="java.lang.String", value="The serial number. It is sent as Base64 encoded bytes. The maximum length is 16 bytes.", required=true)
    private String diagCertSerialNumber;
    @ApiModelProperty(dataType="java.lang.String", value="The target ECU. Maximum size of the field is 30 bytes.", required=true)
    private String targetECU;

    public AbstractSecOCISInput() {
    }

    public AbstractSecOCISInput(String ecuCertificate, String backendCertSubjKeyId, String diagCertSerialNumber, String targetECU) {
        this.ecuCertificate = ecuCertificate;
        this.backendCertSubjKeyId = backendCertSubjKeyId;
        this.diagCertSerialNumber = diagCertSerialNumber;
        this.targetECU = targetECU;
    }

    public String getEcuCertificate() {
        return this.ecuCertificate;
    }

    public void setBackendCertSubjKeyId(String backendCertSubjKeyId) {
        this.backendCertSubjKeyId = backendCertSubjKeyId;
    }

    public String getBackendCertSubjKeyId() {
        return this.backendCertSubjKeyId;
    }

    public String getDiagCertSerialNumber() {
        return this.diagCertSerialNumber;
    }

    public String getTargetECU() {
        return this.targetECU;
    }
}
