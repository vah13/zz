/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.CertificateWithSNAndIssuerSNResult
 *  com.daimler.cebas.certificates.entity.parsing.CertificateParser
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.CertificateWithSNAndIssuerSNResult;
import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ExtendedCertificateWithSNAndIssuerSNResult
extends CertificateWithSNAndIssuerSNResult {
    @ApiModelProperty(dataType="java.lang.String", value="ZK Number value of backend parent")
    private String backendZkNo;
    @JsonIgnore
    private CertificateWithSNAndIssuerSNResult certificateWithSNAndIssuerSNResult;

    public ExtendedCertificateWithSNAndIssuerSNResult(String errorMessage) {
        super(errorMessage);
        this.certificateWithSNAndIssuerSNResult = new CertificateWithSNAndIssuerSNResult(errorMessage);
    }

    public ExtendedCertificateWithSNAndIssuerSNResult(CertificateWithSNAndIssuerSNResult certificateWithSNAndIssuerSNResult, String backendZkNo) {
        super(CertificateParser.decodeBase64((String)certificateWithSNAndIssuerSNResult.getCertificateData()), CertificateParser.decodeBase64((String)certificateWithSNAndIssuerSNResult.getSerialNumber()), CertificateParser.decodeBase64((String)certificateWithSNAndIssuerSNResult.getIssuerSerialNumber()));
        this.certificateWithSNAndIssuerSNResult = certificateWithSNAndIssuerSNResult;
        this.backendZkNo = backendZkNo;
    }

    public String getBackendZkNo() {
        return this.backendZkNo;
    }

    public CertificateWithSNAndIssuerSNResult getCertificateWithSNAndIssuerSNResult() {
        return this.certificateWithSNAndIssuerSNResult;
    }
}
