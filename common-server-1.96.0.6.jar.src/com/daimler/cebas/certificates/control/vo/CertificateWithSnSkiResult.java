/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import io.swagger.annotations.ApiModel;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ import java.util.Base64;
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
/*    */ @ApiModel
/*    */ public class CertificateWithSnSkiResult
/*    */   extends CertificateWithSNResult
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "Subject key identifier encoded bytes in base64 format")
/*    */   private String subjectKeyIdentifier;
/*    */   
/*    */   public CertificateWithSnSkiResult(String errorMessage) {
/* 30 */     super(errorMessage);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CertificateWithSnSkiResult(byte[] certificate) {
/* 40 */     super(certificate, null);
/* 41 */     this.subjectKeyIdentifier = null;
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
/*    */   
/*    */   public CertificateWithSnSkiResult(byte[] certificate, byte[] serialNumber, byte[] subjectKeyIdentifier) {
/* 55 */     super(certificate, serialNumber);
/* 56 */     this.subjectKeyIdentifier = (subjectKeyIdentifier != null) ? Base64.getEncoder().encodeToString(subjectKeyIdentifier) : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSubjectKeyIdentifier() {
/* 65 */     return this.subjectKeyIdentifier;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\CertificateWithSnSkiResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */