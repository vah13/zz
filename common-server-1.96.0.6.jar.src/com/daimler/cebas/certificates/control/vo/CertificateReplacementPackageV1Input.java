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
/*    */ @ApiModel
/*    */ public class CertificateReplacementPackageV1Input
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", required = true, value = "Certificate bytes, Base64 encoded.")
/*    */   private String certificate;
/*    */   @ApiModelProperty(dataType = "java.lang.String", required = true, value = "Replacement target. E. g.: ECU, BACKEND, ROOT")
/*    */   private ReplacementTarget target;
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes.")
/*    */   private String targetBackendCertSubjKeyId;
/*    */   
/*    */   public CertificateReplacementPackageV1Input() {}
/*    */   
/*    */   public CertificateReplacementPackageV1Input(String certificate, ReplacementTarget target, String targetBackendCertSubjKeyId) {
/* 51 */     this.certificate = certificate;
/* 52 */     this.target = target;
/* 53 */     this.targetBackendCertSubjKeyId = targetBackendCertSubjKeyId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCertificate() {
/* 62 */     return this.certificate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ReplacementTarget getTarget() {
/* 71 */     return this.target;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTargetBackendCertSubjKeyId() {
/* 80 */     return this.targetBackendCertSubjKeyId;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\CertificateReplacementPackageV1Input.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */