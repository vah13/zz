/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.ExtendedCertificateWithSnSkiResult
 *  com.daimler.cebas.common.control.vo.CEBASResult
 *  com.daimler.cebas.common.entity.Versioned
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.ExtendedCertificateWithSnSkiResult;
import com.daimler.cebas.common.control.vo.CEBASResult;
import com.daimler.cebas.common.entity.Versioned;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ChainReplacementPackageResult
extends CEBASResult {
    @ApiModelProperty
    private ExtendedCertificateWithSnSkiResult rootCertificate;
    @ApiModelProperty
    private ExtendedCertificateWithSnSkiResult backendCertificate;
    @ApiModelProperty
    private ExtendedCertificateWithSnSkiResult linkCertificate;

    public ChainReplacementPackageResult() {
    }

    public ChainReplacementPackageResult(String errorMessage) {
        super(errorMessage);
    }

    public ExtendedCertificateWithSnSkiResult getRootCertificate() {
        return this.rootCertificate;
    }

    public void setRootCertificate(ExtendedCertificateWithSnSkiResult rootCertificate) {
        this.rootCertificate = rootCertificate;
    }

    public ExtendedCertificateWithSnSkiResult getBackendCertificate() {
        return this.backendCertificate;
    }

    public void setBackendCertificate(ExtendedCertificateWithSnSkiResult backendCertificate) {
        this.backendCertificate = backendCertificate;
    }

    public ExtendedCertificateWithSnSkiResult getLinkCertificate() {
        return this.linkCertificate;
    }

    public void setLinkCertificate(ExtendedCertificateWithSnSkiResult linkCertificate) {
        this.linkCertificate = linkCertificate;
    }

    public Versioned toVersion(int version) {
        if (version != 2) return super.toVersion(version);
        return this;
    }
}
