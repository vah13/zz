/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.ImportResult
 *  com.daimler.cebas.common.control.vo.CEBASResult
 *  io.swagger.annotations.ApiModelProperty
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.ImportResult;
import com.daimler.cebas.common.control.vo.CEBASResult;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

public class EncryptedPKCSImportResult
extends CEBASResult {
    @ApiModelProperty
    private String summary;
    @ApiModelProperty(position=1)
    private List<? extends ImportResult> importResult;

    public EncryptedPKCSImportResult() {
    }

    public EncryptedPKCSImportResult(String summary, List<? extends ImportResult> importResult) {
        this.summary = summary;
        this.importResult = importResult;
    }

    public String getSummary() {
        return this.summary;
    }

    public List<? extends ImportResult> getImportResult() {
        return this.importResult;
    }
}
