/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.DeleteCertificateModel
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.DeleteCertificateModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

@ApiModel
public class DeleteCertificates {
    private boolean all;
    @ApiModelProperty(dataType="java.lang.String", required=true, value="List of authorityKeyIdentifier/serial number, base64 encoded")
    private List<DeleteCertificateModel> pairs;

    public DeleteCertificates() {
    }

    public DeleteCertificates(List<DeleteCertificateModel> models, boolean all) {
        this.pairs = models;
        this.all = all;
    }

    public List<DeleteCertificateModel> getModels() {
        return this.pairs;
    }

    public boolean isAll() {
        return this.all;
    }
}
