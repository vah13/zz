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
/*    */ @ApiModel
/*    */ public class RemoteCertificateImportInput
/*    */ {
/*    */   @ApiModelProperty(value = "The file name of the certificate to be imported.", required = true)
/*    */   private String fileName;
/*    */   @ApiModelProperty(value = "The Base64 encoded bytes of the certificate to be imported.", required = true)
/*    */   private String certificateBytes;
/*    */   
/*    */   public RemoteCertificateImportInput() {}
/*    */   
/*    */   public RemoteCertificateImportInput(String fileName, String certificateBytes) {
/* 47 */     this.fileName = fileName;
/* 48 */     this.certificateBytes = certificateBytes;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getFileName() {
/* 57 */     return this.fileName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCertificateBytes() {
/* 66 */     return this.certificateBytes;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\RemoteCertificateImportInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */