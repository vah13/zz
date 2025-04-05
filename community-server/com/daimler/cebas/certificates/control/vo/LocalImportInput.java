/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class LocalImportInput {
    @ApiModelProperty(dataType="java.lang.String", required=true, value="Path of the file to be imported. The file path has to provided with single slashes as separator, e.g. c:/folder/folder.ext - mind: single backslashes can not be used.")
    private String filePath;
    @ApiModelProperty(dataType="java.lang.String", value="The certificate password, if needed.")
    private String password;

    public LocalImportInput() {
    }

    public LocalImportInput(String filePath) {
        this.filePath = filePath;
    }

    public LocalImportInput(String filePath, String password) {
        this.filePath = filePath;
        this.password = password;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean hasPassword() {
        return this.password != null;
    }
}
