/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.ReplacementTarget
 *  com.daimler.cebas.common.control.vo.CEBASResult
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.ReplacementTarget;
import com.daimler.cebas.common.control.vo.CEBASResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CertificateReplacementPackageV1Result
extends CEBASResult {
    private ReplacementTarget target;
    @ApiModelProperty(dataType="java.lang.String")
    private byte[] ecuCertificate;
    @ApiModelProperty(dataType="java.lang.String")
    private byte[] backendCertificate;
    @ApiModelProperty(dataType="java.lang.String")
    private byte[] rootCertificate;
    @ApiModelProperty(dataType="java.lang.String")
    private byte[] linkCertificate;

    public CertificateReplacementPackageV1Result(String errorMessage) {
        super(errorMessage);
    }

    public CertificateReplacementPackageV1Result(ReplacementTarget replacementTarget, byte[] ecuCertificate) {
        this.target = replacementTarget;
        this.ecuCertificate = ecuCertificate;
    }

    public ReplacementTarget getTarget() {
        return this.target;
    }

    public byte[] getEcuCertificate() {
        return this.ecuCertificate;
    }

    public byte[] getBackendCertificate() {
        return this.backendCertificate;
    }

    public void setBackendCertificate(byte[] backendCertificate) {
        this.backendCertificate = backendCertificate;
    }

    public byte[] getRootCertificate() {
        return this.rootCertificate;
    }

    public void setRootCertificate(byte[] rootCertificate) {
        this.rootCertificate = rootCertificate;
    }

    public byte[] getLinkCertificate() {
        return this.linkCertificate;
    }

    public void setLinkCertificate(byte[] linkCertificate) {
        this.linkCertificate = linkCertificate;
    }
}
