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
public class EcuSignRequestInput {
    @ApiModelProperty(dataType="java.lang.String", value="Challenge byte array to be signed, Base64 encoded.", required=true)
    private String challenge;
    @ApiModelProperty(dataType="java.lang.String", value="The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes.", required=true)
    private String backendSubjectKeyIdentifier;
    @ApiModelProperty(dataType="java.lang.String", value="ECU id", required=true)
    private String ecuId;
    @ApiModelProperty(dataType="java.lang.String", value="The serial number. It is sent as Base64 encoded bytes. The maximum length is 16 bytes.")
    private String ecuSerialNumber;

    public EcuSignRequestInput() {
    }

    public EcuSignRequestInput(String challenge, String backendSubjectKeyIdentifier, String ecuId, String ecuSerialNumber) {
        this.challenge = challenge;
        this.backendSubjectKeyIdentifier = backendSubjectKeyIdentifier;
        this.ecuId = ecuId;
        this.ecuSerialNumber = ecuSerialNumber;
    }

    public String getChallenge() {
        return this.challenge;
    }

    public void setBackendSubjectKeyIdentifier(String backendSubjectKeyIdentifier) {
        this.backendSubjectKeyIdentifier = backendSubjectKeyIdentifier;
    }

    public String getBackendSubjectKeyIdentifier() {
        return this.backendSubjectKeyIdentifier;
    }

    public String getEcuId() {
        return this.ecuId;
    }

    public String getEcuSerialNumber() {
        return this.ecuSerialNumber;
    }
}
