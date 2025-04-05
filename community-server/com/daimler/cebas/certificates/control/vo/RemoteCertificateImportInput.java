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
public class RemoteCertificateImportInput {
    @ApiModelProperty(value="The file name of the certificate to be imported.", required=true)
    private String fileName;
    @ApiModelProperty(value="The Base64 encoded bytes of the certificate to be imported.", required=true)
    private String certificateBytes;

    public RemoteCertificateImportInput() {
    }

    public RemoteCertificateImportInput(String fileName, String certificateBytes) {
        this.fileName = fileName;
        this.certificateBytes = certificateBytes;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getCertificateBytes() {
        return this.certificateBytes;
    }
}
