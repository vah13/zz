/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.entity.Versioned
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.common.entity.Versioned;

public class ImportResult
implements Versioned {
    private String fileName;
    private String subjectKeyIdentifier;
    private String authorityKeyIdentifier;
    private String message;
    private boolean isSuccess;

    public ImportResult() {
    }

    public ImportResult(String fileName, String subjectKeyIdentifier, String authorityKeyIdentifier, String message, boolean isSuccess) {
        this.fileName = fileName;
        this.subjectKeyIdentifier = subjectKeyIdentifier;
        this.authorityKeyIdentifier = authorityKeyIdentifier;
        this.message = message;
        this.isSuccess = isSuccess;
    }

    public ImportResult(String subjectKeyIdentifier, String authorityKeyIdentifier, String message, boolean isSuccess) {
        this.subjectKeyIdentifier = subjectKeyIdentifier;
        this.authorityKeyIdentifier = authorityKeyIdentifier;
        this.message = message;
        this.isSuccess = isSuccess;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getAuthorityKeyIdentifier() {
        return this.authorityKeyIdentifier;
    }

    public String getSubjectKeyIdentifier() {
        return this.subjectKeyIdentifier;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public Versioned toVersion(int version) {
        return this;
    }

    public String toString() {
        return "ImportResult{fileName='" + this.fileName + '\'' + ", subjectKeyIdentifier='" + this.subjectKeyIdentifier + '\'' + ", authorityKeyIdentifier='" + this.authorityKeyIdentifier + '\'' + ", message='" + this.message + '\'' + ", isSuccess=" + this.isSuccess + '}';
    }
}
