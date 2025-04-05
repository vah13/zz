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
public class SecureVariantCodingInput {
    @ApiModelProperty(dataType="java.lang.String", required=true, value="The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes.")
    private String backendSubjectKeyIdentifier;
    @ApiModelProperty(dataType="java.lang.String", required=true, value="Base64 representation of the data to be signed.")
    private String data;
    @ApiModelProperty(dataType="java.lang.String", value="The target ECU. Maximum size of the field is 30 bytes.")
    private String targetECU;
    @ApiModelProperty(dataType="java.lang.String", value="The target VIN. The size of the field is 17 characters.")
    private String targetVIN;

    public SecureVariantCodingInput() {
    }

    public SecureVariantCodingInput(String backendSubjectKeyIdentifier, String data, String targetECU, String targetVIN) {
        this.backendSubjectKeyIdentifier = backendSubjectKeyIdentifier;
        this.data = data;
        this.targetECU = targetECU;
        this.targetVIN = targetVIN;
    }

    public void setBackendSubjectKeyIdentifier(String backendSubjectKeyIdentifier) {
        this.backendSubjectKeyIdentifier = backendSubjectKeyIdentifier;
    }

    public String getBackendSubjectKeyIdentifier() {
        return this.backendSubjectKeyIdentifier;
    }

    public String getData() {
        return this.data;
    }

    public String getTargetECU() {
        return this.targetECU;
    }

    public String getTargetVIN() {
        return this.targetVIN;
    }
}
