/*     */ package com.daimler.cebas.certificates.control.vo;
/*     */ 
/*     */ import io.swagger.annotations.ApiModel;
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
/*     */ 
/*     */ @ApiModel
/*     */ public class SecureVariantCodingInput
/*     */ {
/*     */   @ApiModelProperty(dataType = "java.lang.String", required = true, value = "The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes.")
/*     */   private String backendSubjectKeyIdentifier;
/*     */   @ApiModelProperty(dataType = "java.lang.String", required = true, value = "Base64 representation of the data to be signed.")
/*     */   private String data;
/*     */   @ApiModelProperty(dataType = "java.lang.String", value = "The target ECU. Maximum size of the field is 30 bytes.")
/*     */   private String targetECU;
/*     */   @ApiModelProperty(dataType = "java.lang.String", value = "The target VIN. The size of the field is 17 characters.")
/*     */   private String targetVIN;
/*     */   
/*     */   public SecureVariantCodingInput() {}
/*     */   
/*     */   public SecureVariantCodingInput(String backendSubjectKeyIdentifier, String data, String targetECU, String targetVIN) {
/*  61 */     this.backendSubjectKeyIdentifier = backendSubjectKeyIdentifier;
/*  62 */     this.data = data;
/*  63 */     this.targetECU = targetECU;
/*  64 */     this.targetVIN = targetVIN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBackendSubjectKeyIdentifier(String backendSubjectKeyIdentifier) {
/*  73 */     this.backendSubjectKeyIdentifier = backendSubjectKeyIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBackendSubjectKeyIdentifier() {
/*  82 */     return this.backendSubjectKeyIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getData() {
/*  91 */     return this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTargetECU() {
/* 100 */     return this.targetECU;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTargetVIN() {
/* 109 */     return this.targetVIN;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\SecureVariantCodingInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */