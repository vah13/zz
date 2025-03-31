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
/*    */ public class DeleteCertificateModel
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", required = true, value = "The authority key identifier. It is sent as Base64 encoded bytes, and the length must be 20 bytes.")
/*    */   private String authorityKeyIdentifier;
/*    */   @ApiModelProperty(dataType = "java.lang.String", required = true, value = "The serial number. It is sent as Base64 encoded bytes. The maximum length is 16 bytes.")
/*    */   private String serialNo;
/*    */   
/*    */   public DeleteCertificateModel() {}
/*    */   
/*    */   public DeleteCertificateModel(String authorityKeyIndetifier, String serialNo) {
/* 47 */     this.authorityKeyIdentifier = authorityKeyIndetifier;
/* 48 */     this.serialNo = serialNo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAuthorityKeyIdentifier() {
/* 57 */     return this.authorityKeyIdentifier;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSerialNo() {
/* 66 */     return this.serialNo;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\DeleteCertificateModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */