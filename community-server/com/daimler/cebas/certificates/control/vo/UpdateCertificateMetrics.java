/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.UpdateCertificatesRetryInfo
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.UpdateCertificatesRetryInfo;

public class UpdateCertificateMetrics {
    private String status;
    private String detailsStep;
    private String details;
    private UpdateCertificatesRetryInfo updateCertificatesRetryInfo;
    private boolean metricsAvailable;
    private boolean errorMetrics = false;
    private boolean didFailAllRetries;
    private boolean running;

    public UpdateCertificateMetrics() {
    }

    public UpdateCertificateMetrics(boolean metricsAvailable, String status, String updateStep, String details, UpdateCertificatesRetryInfo updateCertificatesRetryInfo, boolean didFailAllRetries, boolean running) {
        this.status = status;
        this.details = details;
        this.updateCertificatesRetryInfo = updateCertificatesRetryInfo;
        this.metricsAvailable = metricsAvailable;
        this.detailsStep = updateStep;
        this.didFailAllRetries = didFailAllRetries;
        this.running = running;
    }

    public UpdateCertificateMetrics(boolean metricsAvailable, boolean errorMetrics, String status, String updateStep, String details, UpdateCertificatesRetryInfo updateCertificatesRetryInfo, boolean didFailAllRetries, boolean running) {
        this(metricsAvailable, status, updateStep, details, updateCertificatesRetryInfo, didFailAllRetries, running);
        this.errorMetrics = errorMetrics;
    }

    public String getStatus() {
        return this.status;
    }

    public String getDetailsStep() {
        return this.detailsStep;
    }

    public String getDetails() {
        return this.details;
    }

    public UpdateCertificatesRetryInfo getUpdateCertificatesRetryInfo() {
        return this.updateCertificatesRetryInfo;
    }

    public boolean isErrorMetrics() {
        return this.errorMetrics;
    }

    public boolean isMetricsAvailable() {
        return this.metricsAvailable;
    }

    public boolean isDidFailAllRetries() {
        return this.didFailAllRetries;
    }

    public boolean isRunning() {
        return this.running;
    }
}
