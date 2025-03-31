/*    */ package com.daimler.cebas.certificates.integration.vo;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ 
/*    */ 
/*    */ @JsonIgnoreProperties(ignoreUnknown = true)
/*    */ public class PKICertificateResponse
/*    */ {
/*    */   private String certificate;
/*    */   
/*    */   public PKICertificateResponse() {}
/*    */   
/*    */   public PKICertificateResponse(String certificate) {
/* 14 */     this.certificate = certificate;
/*    */   }
/*    */   
/*    */   public String getCertificate() {
/* 18 */     return this.certificate;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 23 */     return "PKICertificateResponse{certificate='" + this.certificate + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\integration\vo\PKICertificateResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */