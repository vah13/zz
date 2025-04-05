/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.validation.SystemIntegrityCheckError
 */
package com.daimler.cebas.certificates.control.validation;

import com.daimler.cebas.certificates.control.validation.SystemIntegrityCheckError;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SystemIntegrityCheckResultWithoutReport {
    private int totalNumberOfCheckedCertificates;
    private Map<String, List<String>> integrityCheckErrorMap;

    public SystemIntegrityCheckResultWithoutReport(int totalNumberOfCheckedCertificates, List<SystemIntegrityCheckError> integrityCheckErrors) {
        this.totalNumberOfCheckedCertificates = totalNumberOfCheckedCertificates;
        this.integrityCheckErrorMap = integrityCheckErrors.stream().collect(Collectors.toMap(SystemIntegrityCheckError::getCertificateId, SystemIntegrityCheckError::getMessageIds));
    }

    public int getTotalNumberOfCheckedCertificates() {
        return this.totalNumberOfCheckedCertificates;
    }

    public int getTotalNumberOfFailedChecks() {
        return this.integrityCheckErrorMap.size();
    }

    public Map<String, List<String>> getIntegrityCheckErrorMap() {
        return this.integrityCheckErrorMap;
    }
}
