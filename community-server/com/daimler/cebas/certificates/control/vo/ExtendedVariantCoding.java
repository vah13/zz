/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.VariantCoding
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.VariantCoding;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

@ApiModel
public class ExtendedVariantCoding
extends VariantCoding {
    @ApiModelProperty(dataType="java.lang.String", value="ZK Number value of backend parent")
    private String backendZkNo;
    @JsonIgnore
    private VariantCoding variantCoding;

    public ExtendedVariantCoding(String errorMessage) {
        super(errorMessage);
        this.variantCoding = new VariantCoding(errorMessage);
    }

    public ExtendedVariantCoding(VariantCoding variantCoding, String backendZkNo) {
        super(variantCoding.getSignature(), variantCoding.getVarCodingCertificate(), new Date(variantCoding.getExpirationDate()), variantCoding.getSerialNumber());
        this.variantCoding = variantCoding;
        this.backendZkNo = backendZkNo;
    }

    public String getBackendZkNo() {
        return this.backendZkNo;
    }

    public VariantCoding getVariantCoding() {
        return this.variantCoding;
    }
}
