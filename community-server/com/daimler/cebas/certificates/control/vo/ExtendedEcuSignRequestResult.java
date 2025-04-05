/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.EcuSignRequestResult
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.EcuSignRequestResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

public class ExtendedEcuSignRequestResult
extends EcuSignRequestResult {
    @ApiModelProperty(dataType="java.lang.String", value="ZK Number value of backend parent")
    private String backendZkNo;
    @JsonIgnore
    private EcuSignRequestResult ecuSignRequestResult;

    public ExtendedEcuSignRequestResult() {
        this.ecuSignRequestResult = new EcuSignRequestResult();
    }

    public ExtendedEcuSignRequestResult(EcuSignRequestResult ecuSignRequestResult, String backendZkNo) {
        super(ecuSignRequestResult.getSignature(), ecuSignRequestResult.getEcuCertificate(), ecuSignRequestResult.getExpirationDate(), ecuSignRequestResult.getSerialNumber());
        this.ecuSignRequestResult = ecuSignRequestResult;
        this.backendZkNo = backendZkNo;
    }

    public String getBackendZkNo() {
        return this.backendZkNo;
    }

    public EcuSignRequestResult getEcuSignRequestResult() {
        return this.ecuSignRequestResult;
    }
}
