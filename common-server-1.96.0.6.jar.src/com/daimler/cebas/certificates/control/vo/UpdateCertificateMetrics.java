/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UpdateCertificateMetrics
/*    */ {
/*    */   private String status;
/*    */   private String detailsStep;
/*    */   private String details;
/*    */   private UpdateCertificatesRetryInfo updateCertificatesRetryInfo;
/*    */   private boolean metricsAvailable;
/*    */   private boolean errorMetrics = false;
/*    */   private boolean didFailAllRetries;
/*    */   private boolean running;
/*    */   
/*    */   public UpdateCertificateMetrics() {}
/*    */   
/*    */   public UpdateCertificateMetrics(boolean metricsAvailable, String status, String updateStep, String details, UpdateCertificatesRetryInfo updateCertificatesRetryInfo, boolean didFailAllRetries, boolean running) {
/* 30 */     this.status = status;
/* 31 */     this.details = details;
/* 32 */     this.updateCertificatesRetryInfo = updateCertificatesRetryInfo;
/* 33 */     this.metricsAvailable = metricsAvailable;
/* 34 */     this.detailsStep = updateStep;
/* 35 */     this.didFailAllRetries = didFailAllRetries;
/* 36 */     this.running = running;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UpdateCertificateMetrics(boolean metricsAvailable, boolean errorMetrics, String status, String updateStep, String details, UpdateCertificatesRetryInfo updateCertificatesRetryInfo, boolean didFailAllRetries, boolean running) {
/* 44 */     this(metricsAvailable, status, updateStep, details, updateCertificatesRetryInfo, didFailAllRetries, running);
/*    */     
/* 46 */     this.errorMetrics = errorMetrics;
/*    */   }
/*    */   
/*    */   public String getStatus() {
/* 50 */     return this.status;
/*    */   }
/*    */   
/*    */   public String getDetailsStep() {
/* 54 */     return this.detailsStep;
/*    */   }
/*    */   
/*    */   public String getDetails() {
/* 58 */     return this.details;
/*    */   }
/*    */   
/*    */   public UpdateCertificatesRetryInfo getUpdateCertificatesRetryInfo() {
/* 62 */     return this.updateCertificatesRetryInfo;
/*    */   }
/*    */   
/*    */   public boolean isErrorMetrics() {
/* 66 */     return this.errorMetrics;
/*    */   }
/*    */   
/*    */   public boolean isMetricsAvailable() {
/* 70 */     return this.metricsAvailable;
/*    */   }
/*    */   
/*    */   public boolean isDidFailAllRetries() {
/* 74 */     return this.didFailAllRetries;
/*    */   }
/*    */   
/*    */   public boolean isRunning() {
/* 78 */     return this.running;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\UpdateCertificateMetrics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */