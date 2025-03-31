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
/*    */ public class CertificateWithSNResult
/*    */   extends CertificateResult
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "Serial number encoded bytes in base64 format")
/*    */   private String serialNumber;
/*    */   
/*    */   public CertificateWithSNResult(String errorMessage) {
/* 30 */     super(errorMessage);
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
/*    */   public CertificateWithSNResult(byte[] certificate, byte[] serialNumber) {
/* 42 */     super(certificate);
/* 43 */     this.serialNumber = (serialNumber != null) ? Base64.getEncoder().encodeToString(serialNumber) : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSerialNumber() {
/* 52 */     return this.serialNumber;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\CertificateWithSNResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */