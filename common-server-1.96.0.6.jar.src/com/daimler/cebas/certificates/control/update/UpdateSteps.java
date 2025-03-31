/*    */ package com.daimler.cebas.certificates.control.update;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum UpdateSteps
/*    */ {
/* 11 */   RETRIEVE_BACKEND_IDENTIFIERS("updateCertificatesStepRetrieveBackendIdentifiers"),
/* 12 */   RETRIEVE_BACKENDS("updateCertificatesStepRetrieveBackends"),
/* 13 */   UPDATE_BACKENDS("updateCertificatesStepUpdateBackends"),
/* 14 */   RETRIEVE_PERMISSIONS("updateCertificatesStepRetrievePermissions"),
/* 15 */   CREATE_CSRS("updateCertificatesStepCreateCSRs"),
/* 16 */   DOWNLOAD_DIAGNOSTIC_CERTIFICATES("updateCertificatesStepDownloadDiagnosticCertificates"),
/* 17 */   DOWNLOAD_OTHER_CERTIFICATES("updateCertificatesStepDownloadOtherCertificates"),
/* 18 */   CREATE_ENHANCED_CSRS("updateCertificatesStepCreateEnhancedCSRs"),
/* 19 */   DOWNLOAD_ENHANCED_CERTIFICATES("updateCertificatesStepDownloadEnhancedCertificates"),
/* 20 */   COLLECTING_TIME_AND_SECOCIS_CSRS_NOT_CREATED_BASED_ON_PERMISSIONS("collectingTimeAndSecocisCSRsNotCreatedBasedOnPermissions"),
/* 21 */   DOWNLOAD_TIME_AND_SECOCSI_CERTIFICATE_USING_CSRS_NOT_CREATED_BASED_ON_PERMISSIONS("dowloadingTimeAndSecocisCertificateUsingCSRsNotCreateBasedOnPermissions"),
/*    */   
/* 23 */   DOWNLOAD_CERTIFICATES("updateCertificatesStepDownloadCertificates"),
/* 24 */   DOWNLOAD_LINK_CERTIFICATES("updateCertificatesStepDownloadLinkCertificates"),
/* 25 */   UPDATE_NON_VSM_CERTIFICATES("updateCertificatesStepUpdateNonVSMCertificates"),
/* 26 */   NONE("updateCertificatesStepNone");
/*    */   
/*    */   private String text;
/*    */   
/*    */   UpdateSteps(String text) {
/* 31 */     this.text = text;
/*    */   }
/*    */   
/*    */   public String getText() {
/* 35 */     return this.text;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\contro\\update\UpdateSteps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */