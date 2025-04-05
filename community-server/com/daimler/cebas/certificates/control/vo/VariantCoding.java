/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.vo.CEBASResult
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.common.control.vo.CEBASResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

@ApiModel
public class VariantCoding
extends CEBASResult {
    @ApiModelProperty(dataType="java.lang.String", value="Signature base64 string encoded")
    private String signature;
    @ApiModelProperty(dataType="java.lang.String", value="Variant coding certificate base64 string encoded")
    private String varCodingCertificate;
    @ApiModelProperty(dataType="java.lang.Long", value="Expiration date of Variant Coding Certificate")
    private Long expirationDate;
    @ApiModelProperty(dataType="java.lang.String", value="Serial number base64 string encoded")
    private String serialNumber;

    public VariantCoding(String errorMessage) {
        super(errorMessage);
    }

    public VariantCoding(String signature, String varCodingCertificate, Date expirationDate, String serialNumber) {
        this.signature = signature;
        this.varCodingCertificate = varCodingCertificate;
        this.expirationDate = expirationDate.getTime();
        this.serialNumber = serialNumber;
    }

    public String getSignature() {
        return this.signature;
    }

    public String getVarCodingCertificate() {
        return this.varCodingCertificate;
    }

    public Long getExpirationDate() {
        return this.expirationDate;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }
}
