/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.certificates.control.update;

public enum UpdateSteps {
    RETRIEVE_BACKEND_IDENTIFIERS("updateCertificatesStepRetrieveBackendIdentifiers"),
    RETRIEVE_BACKENDS("updateCertificatesStepRetrieveBackends"),
    UPDATE_BACKENDS("updateCertificatesStepUpdateBackends"),
    RETRIEVE_PERMISSIONS("updateCertificatesStepRetrievePermissions"),
    CREATE_CSRS("updateCertificatesStepCreateCSRs"),
    DOWNLOAD_DIAGNOSTIC_CERTIFICATES("updateCertificatesStepDownloadDiagnosticCertificates"),
    DOWNLOAD_OTHER_CERTIFICATES("updateCertificatesStepDownloadOtherCertificates"),
    CREATE_ENHANCED_CSRS("updateCertificatesStepCreateEnhancedCSRs"),
    DOWNLOAD_ENHANCED_CERTIFICATES("updateCertificatesStepDownloadEnhancedCertificates"),
    COLLECTING_TIME_AND_SECOCIS_CSRS_NOT_CREATED_BASED_ON_PERMISSIONS("collectingTimeAndSecocisCSRsNotCreatedBasedOnPermissions"),
    DOWNLOAD_TIME_AND_SECOCSI_CERTIFICATE_USING_CSRS_NOT_CREATED_BASED_ON_PERMISSIONS("dowloadingTimeAndSecocisCertificateUsingCSRsNotCreateBasedOnPermissions"),
    DOWNLOAD_CERTIFICATES("updateCertificatesStepDownloadCertificates"),
    DOWNLOAD_LINK_CERTIFICATES("updateCertificatesStepDownloadLinkCertificates"),
    UPDATE_NON_VSM_CERTIFICATES("updateCertificatesStepUpdateNonVSMCertificates"),
    NONE("updateCertificatesStepNone");

    private String text;

    private UpdateSteps(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
