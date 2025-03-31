/*    */ package com.daimler.cebas.certificates.integration.vo;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
/*    */ @JsonIgnoreProperties(ignoreUnknown = true, value = {"certificateType"})
/*    */ public class PKIEnhancedCertificateRequest
/*    */   extends PKICertificateRequest
/*    */ {
/*    */   private static final long serialVersionUID = -2536556647923614772L;
/*    */   private String holderCertificate;
/*    */   
/*    */   public PKIEnhancedCertificateRequest() {}
/*    */   
/*    */   public PKIEnhancedCertificateRequest(String enrollmentId, String csr, String caIdentifier, String certificateType, String holderCertificate) {
/* 40 */     super(enrollmentId, csr, caIdentifier, certificateType);
/* 41 */     this.holderCertificate = holderCertificate;
/*    */   }
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
/*    */   public PKIEnhancedCertificateRequest(String csr, String caIdentifier, String certificateType, String holderCertificate) {
/* 54 */     super(csr, caIdentifier, certificateType);
/* 55 */     this.holderCertificate = holderCertificate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getHolderCertificate() {
/* 64 */     return this.holderCertificate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 72 */     return "PKIEnhancedCertificateRequest{holderCertificate='" + this.holderCertificate + '\'' + ", csr='" + this.csr + '\'' + ", caIdentifier='" + this.caIdentifier + '\'' + ", certificateType='" + this.certificateType + '\'' + ", enrollmentId='" + 
/*    */ 
/*    */ 
/*    */       
/* 76 */       getEnrollmentId() + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\integration\vo\PKIEnhancedCertificateRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */