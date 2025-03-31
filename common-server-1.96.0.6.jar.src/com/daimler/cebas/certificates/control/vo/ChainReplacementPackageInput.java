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
/*    */ @ApiModel
/*    */ public class ChainReplacementPackageInput
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes. ZK Number is also supported (as 10 character string, NOT Base64 encoded).")
/*    */   private String targetBackendCertSubjKeyId;
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes. ZK Number is also supported (as 10 character string, NOT Base64 encoded).")
/*    */   private String sourceBackendCertSubjKeyId;
/*    */   
/*    */   public ChainReplacementPackageInput() {}
/*    */   
/*    */   public ChainReplacementPackageInput(String targetBackendCertSubjKeyId, String sourceBackendCertSubjKeyId) {
/* 35 */     this.targetBackendCertSubjKeyId = targetBackendCertSubjKeyId;
/* 36 */     this.sourceBackendCertSubjKeyId = sourceBackendCertSubjKeyId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTargetBackendCertSubjKeyId() {
/* 45 */     return this.targetBackendCertSubjKeyId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTargetBackendCertSubjKeyId(String targetBackendCertSubjKeyId) {
/* 54 */     this.targetBackendCertSubjKeyId = targetBackendCertSubjKeyId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSourceBackendCertSubjKeyId() {
/* 63 */     return this.sourceBackendCertSubjKeyId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSourceBackendCertSubjKeyId(String sourceBackendCertSubjKeyId) {
/* 72 */     this.sourceBackendCertSubjKeyId = sourceBackendCertSubjKeyId;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\ChainReplacementPackageInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */