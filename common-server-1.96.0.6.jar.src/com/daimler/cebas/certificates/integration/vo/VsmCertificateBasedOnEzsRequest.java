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
/*    */ public class VsmCertificateBasedOnEzsRequest
/*    */ {
/*    */   private String caId;
/*    */   private String vsmSn;
/*    */   
/*    */   public VsmCertificateBasedOnEzsRequest() {}
/*    */   
/*    */   public VsmCertificateBasedOnEzsRequest(String caId, String vsmSn) {
/* 19 */     this.caId = caId;
/* 20 */     this.vsmSn = vsmSn;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCaId(String caId) {
/* 27 */     this.caId = caId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCaId() {
/* 34 */     return this.caId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getVsmSn() {
/* 41 */     return this.vsmSn;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setVsmSn(String vsmSn) {
/* 48 */     this.vsmSn = vsmSn;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\integration\vo\VsmCertificateBasedOnEzsRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */