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
public class SignatureCheckHolder {
    @ApiModelProperty(dataType="java.lang.String", required=true, value="Base64 representation of the message which needs to be checked.")
    private String message;
    @ApiModelProperty(dataType="java.lang.String", required=true, value="Base64 representation of the signature.")
    private String signature;
    @ApiModelProperty(dataType="java.lang.String", required=true, value="Certificate bytes, Base64 encoded.")
    private String ecuCertificate;

    public SignatureCheckHolder() {
    }

    public SignatureCheckHolder(String message, String signature, String ecuCertificate) {
        this.message = message;
        this.signature = signature;
        this.ecuCertificate = ecuCertificate;
    }

    public String getMessage() {
        return this.message;
    }

    public String getSignature() {
        return this.signature;
    }

    public String getEcuCertificate() {
        return this.ecuCertificate;
    }
}
