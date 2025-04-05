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
public class CheckOwnershipInput {
    @ApiModelProperty(dataType="java.lang.String", value="ECU challenge, Base64 encoded", required=true)
    private String ecuChallenge;
    @ApiModelProperty(dataType="java.lang.String", value="The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes.", required=true)
    private String backendCertSubjKeyId;
    @ApiModelProperty(dataType="java.lang.String", value="The serial number. It is sent as Base64 encoded bytes. The maximum length is 16 bytes.", required=true)
    private String serialNumber;

    public CheckOwnershipInput() {
    }

    public CheckOwnershipInput(String ecuChallenge, String backendCertSubjKeyId, String serialNumber) {
        this.ecuChallenge = ecuChallenge;
        this.backendCertSubjKeyId = backendCertSubjKeyId;
        this.serialNumber = serialNumber;
    }

    public void setBackendCertSubjKeyId(String backendCertSubjKeyId) {
        this.backendCertSubjKeyId = backendCertSubjKeyId;
    }

    public String getBackendCertSubjKeyId() {
        return this.backendCertSubjKeyId;
    }

    public String getEcuChallenge() {
        return this.ecuChallenge;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }
}
