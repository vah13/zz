/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.CertificateWithSnSkiResult
 *  com.daimler.cebas.certificates.entity.parsing.CertificateParser
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.CertificateWithSnSkiResult;
import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ExtendedCertificateWithSnSkiResult
extends CertificateWithSnSkiResult {
    @ApiModelProperty(dataType="java.lang.String", value="ZK Number value")
    private String zkNo;
    @JsonIgnore
    private CertificateWithSnSkiResult certificateWithSnSkiResult;

    public ExtendedCertificateWithSnSkiResult(String errorMessage) {
        super(errorMessage);
        this.certificateWithSnSkiResult = new CertificateWithSnSkiResult(errorMessage);
    }

    public ExtendedCertificateWithSnSkiResult(CertificateWithSnSkiResult certificateWithSnSkiResult, String zkNo) {
        super(CertificateParser.decodeBase64((String)certificateWithSnSkiResult.getCertificateData()), CertificateParser.decodeBase64((String)certificateWithSnSkiResult.getSerialNumber()), CertificateParser.decodeBase64((String)certificateWithSnSkiResult.getSubjectKeyIdentifier()));
        this.certificateWithSnSkiResult = certificateWithSnSkiResult;
        this.zkNo = zkNo;
    }

    public String getZkNo() {
        return this.zkNo;
    }

    public CertificateWithSnSkiResult getCertificateWithSnSkiResult() {
        return this.certificateWithSnSkiResult;
    }
}
