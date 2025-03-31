/*    */ package com.daimler.cebas.certificates.integration.vo;
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
/*    */ public class VsmCertificateRequest
/*    */ {
/*    */   private String caId;
/*    */   private String vsmCert;
/*    */   private String vin;
/*    */   
/*    */   public VsmCertificateRequest() {}
/*    */   
/*    */   public VsmCertificateRequest(String caId, String vsmCert, String vin) {
/* 24 */     this.caId = caId;
/* 25 */     this.vsmCert = vsmCert;
/* 26 */     this.vin = vin;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCaId(String caId) {
/* 33 */     this.caId = caId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCaId() {
/* 40 */     return this.caId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getVsmCert() {
/* 47 */     return this.vsmCert;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setVsmCert(String vsmCert) {
/* 54 */     this.vsmCert = vsmCert;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getVin() {
/* 61 */     return this.vin;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setVin(String vin) {
/* 68 */     this.vin = vin;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\integration\vo\VsmCertificateRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */