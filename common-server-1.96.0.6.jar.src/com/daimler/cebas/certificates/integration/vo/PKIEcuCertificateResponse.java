/*    */ package com.daimler.cebas.certificates.integration.vo;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JsonIgnoreProperties(ignoreUnknown = true)
/*    */ public class PKIEcuCertificateResponse
/*    */ {
/*    */   private String ecuCert;
/*    */   
/*    */   public PKIEcuCertificateResponse() {}
/*    */   
/*    */   public PKIEcuCertificateResponse(String ecuCert) {
/* 17 */     this.ecuCert = ecuCert;
/*    */   }
/*    */   
/*    */   public String getEcuCert() {
/* 21 */     return this.ecuCert;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 26 */     return "PKIEcuCertificateResponse{ecuCert='" + this.ecuCert + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\integration\vo\PKIEcuCertificateResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */