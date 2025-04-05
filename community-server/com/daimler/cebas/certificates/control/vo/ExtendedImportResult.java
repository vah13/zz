/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.ImportResult
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.ImportResult;

public class ExtendedImportResult
extends ImportResult {
    private String internalId;
    private String backendZkNo;

    public ExtendedImportResult() {
    }

    public ExtendedImportResult(String subjectKeyIdentifier, String authorityKeyIdentifier, String message, boolean isSuccess, String internalId, String backendZkNo) {
        super(subjectKeyIdentifier, authorityKeyIdentifier, message, isSuccess);
        this.internalId = internalId;
        this.backendZkNo = backendZkNo;
    }

    public String getInternalId() {
        return this.internalId;
    }

    public String getBackendZkNo() {
        return this.backendZkNo;
    }
}
