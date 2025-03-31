/*     */ package com.daimler.cebas.certificates.control.vo;
/*     */ 
/*     */ import io.swagger.annotations.ApiModelProperty;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractSecOCISInput
/*     */ {
/*     */   @ApiModelProperty(dataType = "java.lang.String", value = "Certificate bytes, Base64 encoded.", required = true)
/*     */   private String ecuCertificate;
/*     */   @ApiModelProperty(dataType = "java.lang.String", value = "The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes.", required = true)
/*     */   private String backendCertSubjKeyId;
/*     */   @ApiModelProperty(dataType = "java.lang.String", value = "The serial number. It is sent as Base64 encoded bytes. The maximum length is 16 bytes.", required = true)
/*     */   private String diagCertSerialNumber;
/*     */   @ApiModelProperty(dataType = "java.lang.String", value = "The target ECU. Maximum size of the field is 30 bytes.", required = true)
/*     */   private String targetECU;
/*     */   
/*     */   public AbstractSecOCISInput() {}
/*     */   
/*     */   public AbstractSecOCISInput(String ecuCertificate, String backendCertSubjKeyId, String diagCertSerialNumber, String targetECU) {
/*  58 */     this.ecuCertificate = ecuCertificate;
/*  59 */     this.backendCertSubjKeyId = backendCertSubjKeyId;
/*  60 */     this.diagCertSerialNumber = diagCertSerialNumber;
/*  61 */     this.targetECU = targetECU;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEcuCertificate() {
/*  70 */     return this.ecuCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBackendCertSubjKeyId(String backendCertSubjKeyId) {
/*  79 */     this.backendCertSubjKeyId = backendCertSubjKeyId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBackendCertSubjKeyId() {
/*  88 */     return this.backendCertSubjKeyId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDiagCertSerialNumber() {
/*  97 */     return this.diagCertSerialNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTargetECU() {
/* 106 */     return this.targetECU;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\AbstractSecOCISInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */