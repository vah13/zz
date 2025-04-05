/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.CertificateWithSNResult
 *  com.daimler.cebas.certificates.entity.parsing.CertificateParser
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.CertificateWithSNResult;
import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ExtendedCertificateWithSNResult
extends CertificateWithSNResult {
    @ApiModelProperty(dataType="java.lang.String", value="ZK Number value of backend parent")
    private String backendZkNo;
    @JsonIgnore
    private CertificateWithSNResult certificateWithSNResult;

    public ExtendedCertificateWithSNResult(String errorMessage) {
        super(errorMessage);
        this.certificateWithSNResult = new CertificateWithSNResult(errorMessage);
    }

    public ExtendedCertificateWithSNResult(CertificateWithSNResult certificateWithSNResult, String backendZkNo) {
        super(CertificateParser.decodeBase64((String)certificateWithSNResult.getCertificateData()), CertificateParser.decodeBase64((String)certificateWithSNResult.getSerialNumber()));
        this.certificateWithSNResult = certificateWithSNResult;
        this.backendZkNo = backendZkNo;
    }

    public String getBackendZkNo() {
        return this.backendZkNo;
    }

    public CertificateWithSNResult getCertificateWithSNResult() {
        return this.certificateWithSNResult;
    }
}
