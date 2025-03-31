/*    */ package com.daimler.cebas.certificates.integration.vo;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JsonIgnoreProperties(ignoreUnknown = true)
/*    */ public class PKILinkCertificateResponse
/*    */ {
/*    */   private String linkCert;
/*    */   
/*    */   public PKILinkCertificateResponse() {}
/*    */   
/*    */   public PKILinkCertificateResponse(String linkCert) {
/* 17 */     this.linkCert = linkCert;
/*    */   }
/*    */   
/*    */   public String getLinkCert() {
/* 21 */     return this.linkCert;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 26 */     return "PKILinkCertificateResponse{linkCert='" + this.linkCert + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\integration\vo\PKILinkCertificateResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */