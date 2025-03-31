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
/*     */ 
/*     */ 
/*     */ @ApiModel
/*     */ public class EcuSignRequestInput
/*     */ {
/*     */   @ApiModelProperty(dataType = "java.lang.String", value = "Challenge byte array to be signed, Base64 encoded.", required = true)
/*     */   private String challenge;
/*     */   @ApiModelProperty(dataType = "java.lang.String", value = "The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes.", required = true)
/*     */   private String backendSubjectKeyIdentifier;
/*     */   @ApiModelProperty(dataType = "java.lang.String", value = "ECU id", required = true)
/*     */   private String ecuId;
/*     */   @ApiModelProperty(dataType = "java.lang.String", value = "The serial number. It is sent as Base64 encoded bytes. The maximum length is 16 bytes.")
/*     */   private String ecuSerialNumber;
/*     */   
/*     */   public EcuSignRequestInput() {}
/*     */   
/*     */   public EcuSignRequestInput(String challenge, String backendSubjectKeyIdentifier, String ecuId, String ecuSerialNumber) {
/*  63 */     this.challenge = challenge;
/*  64 */     this.backendSubjectKeyIdentifier = backendSubjectKeyIdentifier;
/*  65 */     this.ecuId = ecuId;
/*  66 */     this.ecuSerialNumber = ecuSerialNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getChallenge() {
/*  75 */     return this.challenge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBackendSubjectKeyIdentifier(String backendSubjectKeyIdentifier) {
/*  84 */     this.backendSubjectKeyIdentifier = backendSubjectKeyIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBackendSubjectKeyIdentifier() {
/*  93 */     return this.backendSubjectKeyIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEcuId() {
/* 102 */     return this.ecuId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEcuSerialNumber() {
/* 111 */     return this.ecuSerialNumber;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\EcuSignRequestInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */