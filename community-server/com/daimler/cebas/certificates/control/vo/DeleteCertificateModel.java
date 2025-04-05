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
public class DeleteCertificateModel {
    @ApiModelProperty(dataType="java.lang.String", required=true, value="The authority key identifier. It is sent as Base64 encoded bytes, and the length must be 20 bytes.")
    private String authorityKeyIdentifier;
    @ApiModelProperty(dataType="java.lang.String", required=true, value="The serial number. It is sent as Base64 encoded bytes. The maximum length is 16 bytes.")
    private String serialNo;

    public DeleteCertificateModel() {
    }

    public DeleteCertificateModel(String authorityKeyIndetifier, String serialNo) {
        this.authorityKeyIdentifier = authorityKeyIndetifier;
        this.serialNo = serialNo;
    }

    public String getAuthorityKeyIdentifier() {
        return this.authorityKeyIdentifier;
    }

    public String getSerialNo() {
        return this.serialNo;
    }
}
