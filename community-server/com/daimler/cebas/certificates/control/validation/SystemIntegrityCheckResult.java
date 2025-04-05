/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.validation.SystemIntegrityCheckError
 */
package com.daimler.cebas.certificates.control.validation;

import com.daimler.cebas.certificates.control.validation.SystemIntegrityCheckError;
import java.util.ArrayList;
import java.util.List;

public class SystemIntegrityCheckResult {
    private int totalNumberOfCheckedCertificates = 0;
    private List<SystemIntegrityCheckError> integrityCheckErrors = new ArrayList<SystemIntegrityCheckError>();
    private String integrityCheckXML = "";

    public synchronized int getTotalNumberOfCheckedCertificates() {
        return this.totalNumberOfCheckedCertificates;
    }

    public synchronized int getTotalNumberOfFailedChecks() {
        return this.integrityCheckErrors.size();
    }

    public synchronized List<SystemIntegrityCheckError> getIntegrityCheckErrors() {
        return this.integrityCheckErrors;
    }

    public synchronized String getIntegrityCheckXML() {
        return this.integrityCheckXML;
    }

    public synchronized void updateErrosList(List<SystemIntegrityCheckError> newErrorsList) {
        if (this.integrityCheckErrors == null) return;
        this.integrityCheckErrors.clear();
        this.integrityCheckErrors.addAll(newErrorsList);
    }

    public synchronized void updateTotalNumberOfCheckedCertificates(int newTotalOfCheckedCertificates) {
        this.totalNumberOfCheckedCertificates = newTotalOfCheckedCertificates;
    }

    public synchronized void updateIntegrityCheckXML(String newXML) {
        this.integrityCheckXML = newXML;
    }

    public synchronized void clear() {
        if (this.integrityCheckErrors != null) {
            this.integrityCheckErrors.clear();
        }
        this.totalNumberOfCheckedCertificates = 0;
        this.integrityCheckXML = "";
    }

    public String toString() {
        return "SystemIntegrityCheckResult [totalNumberOfCheckedCertificates=" + this.totalNumberOfCheckedCertificates + ", totalNumberOfFailedChecks=" + this.integrityCheckErrors.size() + ", integrityCheckErrors=" + this.integrityCheckErrors + "]";
    }
}
