/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
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
/*    */ public abstract class AbstractCertificateReplacementPackageInput
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", required = true, value = "Certificate bytes, Base64 encoded.")
/*    */   private String certificate;
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes.")
/*    */   private String targetBackendCertSubjKeyId;
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "Unique ECU ID, not base64 encoded. Maximum length is 30 bytes.")
/*    */   private String uniqueEcuId;
/*    */   
/*    */   public AbstractCertificateReplacementPackageInput(String certificate, String targetBackendCertSubjKeyId, String uniqueEcuId) {
/* 37 */     this.certificate = certificate;
/* 38 */     this.targetBackendCertSubjKeyId = targetBackendCertSubjKeyId;
/* 39 */     this.uniqueEcuId = uniqueEcuId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCertificate() {
/* 48 */     return this.certificate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTargetBackendCertSubjKeyId(String targetBackendCertSubjKeyId) {
/* 57 */     this.targetBackendCertSubjKeyId = targetBackendCertSubjKeyId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTargetBackendCertSubjKeyId() {
/* 66 */     return this.targetBackendCertSubjKeyId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUniqueEcuId() {
/* 75 */     return this.uniqueEcuId;
/*    */   }
/*    */   
/*    */   @ApiModelProperty(hidden = true)
/*    */   public abstract ReplacementTarget getTarget();
/*    */   
/*    */   @ApiModelProperty(hidden = true)
/*    */   public abstract String getTargetVIN();
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\AbstractCertificateReplacementPackageInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */