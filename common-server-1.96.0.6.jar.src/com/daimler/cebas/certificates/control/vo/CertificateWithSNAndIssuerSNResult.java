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
/*    */ @ApiModel
/*    */ public class CertificateWithSNAndIssuerSNResult
/*    */   extends CertificateWithSNResult
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "Issuer serial number encoded bytes in base64 format")
/*    */   private String issuerSerialNumber;
/*    */   
/*    */   public CertificateWithSNAndIssuerSNResult(String errorMessage) {
/* 29 */     super(errorMessage);
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
/*    */   public CertificateWithSNAndIssuerSNResult(byte[] certificate, byte[] serialNumber) {
/* 41 */     super(certificate, serialNumber);
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
/*    */   public CertificateWithSNAndIssuerSNResult(byte[] certificate, byte[] serialNumber, byte[] issuerSerialNumber) {
/* 55 */     super(certificate, serialNumber);
/* 56 */     this.issuerSerialNumber = Base64.getEncoder().encodeToString(issuerSerialNumber);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getIssuerSerialNumber() {
/* 65 */     return this.issuerSerialNumber;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\CertificateWithSNAndIssuerSNResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */