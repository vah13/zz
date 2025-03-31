/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import io.swagger.annotations.ApiModel;
/*    */ import io.swagger.annotations.ApiModelProperty;
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
/*    */ public class SignatureCheckHolder
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", required = true, value = "Base64 representation of the message which needs to be checked.")
/*    */   private String message;
/*    */   @ApiModelProperty(dataType = "java.lang.String", required = true, value = "Base64 representation of the signature.")
/*    */   private String signature;
/*    */   @ApiModelProperty(dataType = "java.lang.String", required = true, value = "Certificate bytes, Base64 encoded.")
/*    */   private String ecuCertificate;
/*    */   
/*    */   public SignatureCheckHolder() {}
/*    */   
/*    */   public SignatureCheckHolder(String message, String signature, String ecuCertificate) {
/* 55 */     this.message = message;
/* 56 */     this.signature = signature;
/* 57 */     this.ecuCertificate = ecuCertificate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 66 */     return this.message;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSignature() {
/* 75 */     return this.signature;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getEcuCertificate() {
/* 84 */     return this.ecuCertificate;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\SignatureCheckHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */