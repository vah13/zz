/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import io.swagger.annotations.ApiModel;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ApiModel
/*    */ public class CertificateReplacementPackageV3Input
/*    */   extends AbstractCertificateReplacementPackageInput
/*    */ {
/*    */   private ReplacementTarget determinedTarget;
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "The target VIN. The size of the field is 17 characters.")
/*    */   private String targetVIN;
/*    */   
/*    */   public CertificateReplacementPackageV3Input() {
/* 19 */     super("", "", "");
/*    */   }
/*    */   
/*    */   public CertificateReplacementPackageV3Input(String certificate, String targetBackendCertSubjKeyId, String uniqueEcuId) {
/* 23 */     super(certificate, targetBackendCertSubjKeyId, uniqueEcuId);
/*    */   }
/*    */   
/*    */   public CertificateReplacementPackageV3Input(String certificate, String targetBackendCertSubjKeyId, ReplacementTarget target, String uniqueEcuId) {
/* 27 */     this(certificate, targetBackendCertSubjKeyId, uniqueEcuId);
/* 28 */     this.determinedTarget = target;
/*    */   }
/*    */   
/*    */   public CertificateReplacementPackageV3Input(String certificate, String targetBackendCertSubjKeyId, String uniqueEcuId, String targetVIN) {
/* 32 */     this(certificate, targetBackendCertSubjKeyId, uniqueEcuId);
/* 33 */     this.targetVIN = targetVIN;
/*    */   }
/*    */   
/*    */   public CertificateReplacementPackageV3Input(String certificate, String targetBackendCertSubjKeyId, ReplacementTarget target, String uniqueEcuId, String targetVIN) {
/* 37 */     this(certificate, targetBackendCertSubjKeyId, target, uniqueEcuId);
/* 38 */     this.targetVIN = targetVIN;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTargetVIN() {
/* 48 */     return this.targetVIN;
/*    */   }
/*    */ 
/*    */   
/*    */   public ReplacementTarget getTarget() {
/* 53 */     return this.determinedTarget;
/*    */   }
/*    */   
/*    */   @ApiModelProperty(hidden = true)
/*    */   public void setDeterminedTarget(ReplacementTarget determinedTarget) {
/* 58 */     this.determinedTarget = determinedTarget;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\CertificateReplacementPackageV3Input.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */