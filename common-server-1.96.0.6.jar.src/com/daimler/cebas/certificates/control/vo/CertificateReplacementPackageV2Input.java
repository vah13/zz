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
/*    */ @ApiModel
/*    */ public class CertificateReplacementPackageV2Input
/*    */   extends AbstractCertificateReplacementPackageInput
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", required = true, value = "Replacement target. E. g.: ECU, BACKEND, ROOT")
/*    */   private ReplacementTarget target;
/*    */   
/*    */   public CertificateReplacementPackageV2Input(String certificate, String targetBackendCertSubjKeyId, ReplacementTarget target, String uniqueEcuId) {
/* 19 */     super(certificate, targetBackendCertSubjKeyId, uniqueEcuId);
/* 20 */     this.target = target;
/*    */   }
/*    */ 
/*    */   
/*    */   public ReplacementTarget getTarget() {
/* 25 */     return this.target;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTargetVIN() {
/* 30 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\CertificateReplacementPackageV2Input.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */