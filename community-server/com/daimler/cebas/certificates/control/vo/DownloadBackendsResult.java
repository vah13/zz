/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.ImportResult
 *  com.daimler.cebas.certificates.integration.vo.BackendIdentifier
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.ImportResult;
import com.daimler.cebas.certificates.integration.vo.BackendIdentifier;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DownloadBackendsResult {
    private List<ImportResult> importResult;
    private List<BackendIdentifier> backendIdentifiers;

    public DownloadBackendsResult() {
        this.importResult = Collections.emptyList();
        this.backendIdentifiers = Collections.emptyList();
    }

    public DownloadBackendsResult(List<ImportResult> importResult, List<BackendIdentifier> backendIdentifiers) {
        this.importResult = importResult;
        this.backendIdentifiers = backendIdentifiers;
    }

    public List<ImportResult> getImportResult() {
        return this.importResult;
    }

    public void setImportResult(List<ImportResult> importResult) {
        this.importResult = importResult;
    }

    public List<BackendIdentifier> getBackendIdentifiers() {
        return this.backendIdentifiers;
    }

    public void setBackendIdentifiers(List<BackendIdentifier> backendIdentifiers) {
        this.backendIdentifiers = backendIdentifiers;
    }

    public boolean importExecuted() {
        return this.importResult.size() > 0;
    }

    public Optional<BackendIdentifier> getBackendIdentifierBasedOnSki(String ski) {
        return this.backendIdentifiers.stream().filter(bi -> ski.replaceAll("-", "").equalsIgnoreCase(bi.getSubjectKeyIdentifier())).findFirst();
    }
}
