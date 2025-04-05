/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.ExtendedCertificateWithSnSkiResult
 *  com.daimler.cebas.certificates.control.vo.ReplacementTarget
 *  com.daimler.cebas.common.control.vo.CEBASResult
 *  com.daimler.cebas.common.entity.Versioned
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.ExtendedCertificateWithSnSkiResult;
import com.daimler.cebas.certificates.control.vo.ReplacementTarget;
import com.daimler.cebas.common.control.vo.CEBASResult;
import com.daimler.cebas.common.entity.Versioned;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CertificateReplacementPackageExtendedResult
extends CEBASResult {
    private ReplacementTarget target;
    @ApiModelProperty
    private ExtendedCertificateWithSnSkiResult ecuCertificate;
    @ApiModelProperty
    private ExtendedCertificateWithSnSkiResult backendCertificate;
    @ApiModelProperty
    private ExtendedCertificateWithSnSkiResult rootCertificate;
    @ApiModelProperty
    private ExtendedCertificateWithSnSkiResult linkCertificate;

    public CertificateReplacementPackageExtendedResult() {
    }

    public CertificateReplacementPackageExtendedResult(String errorMessage) {
        super(errorMessage);
    }

    public CertificateReplacementPackageExtendedResult(ReplacementTarget replacementTarget, ExtendedCertificateWithSnSkiResult ecuCertificate) {
        this.target = replacementTarget;
        this.ecuCertificate = ecuCertificate;
    }

    public ReplacementTarget getTarget() {
        return this.target;
    }

    public ExtendedCertificateWithSnSkiResult getEcuCertificate() {
        return this.ecuCertificate;
    }

    public void setEcuCertificate(ExtendedCertificateWithSnSkiResult ecuCertificate) {
        this.ecuCertificate = ecuCertificate;
    }

    public ExtendedCertificateWithSnSkiResult getBackendCertificate() {
        return this.backendCertificate;
    }

    public void setBackendCertificate(ExtendedCertificateWithSnSkiResult backendCertificate) {
        this.backendCertificate = backendCertificate;
    }

    public ExtendedCertificateWithSnSkiResult getRootCertificate() {
        return this.rootCertificate;
    }

    public void setRootCertificate(ExtendedCertificateWithSnSkiResult rootCertificate) {
        this.rootCertificate = rootCertificate;
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
