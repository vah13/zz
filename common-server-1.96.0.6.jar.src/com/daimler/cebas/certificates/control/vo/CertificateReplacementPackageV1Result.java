/*     */ package com.daimler.cebas.certificates.control.vo;
/*     */ 
/*     */ import com.daimler.cebas.common.control.vo.CEBASResult;
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
/*     */ @ApiModel
/*     */ public class CertificateReplacementPackageV1Result
/*     */   extends CEBASResult
/*     */ {
/*     */   private ReplacementTarget target;
/*     */   @ApiModelProperty(dataType = "java.lang.String")
/*     */   private byte[] ecuCertificate;
/*     */   @ApiModelProperty(dataType = "java.lang.String")
/*     */   private byte[] backendCertificate;
/*     */   @ApiModelProperty(dataType = "java.lang.String")
/*     */   private byte[] rootCertificate;
/*     */   @ApiModelProperty(dataType = "java.lang.String")
/*     */   private byte[] linkCertificate;
/*     */   
/*     */   public CertificateReplacementPackageV1Result(String errorMessage) {
/*  50 */     super(errorMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CertificateReplacementPackageV1Result(ReplacementTarget replacementTarget, byte[] ecuCertificate) {
/*  62 */     this.target = replacementTarget;
/*  63 */     this.ecuCertificate = ecuCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReplacementTarget getTarget() {
/*  72 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getEcuCertificate() {
/*  81 */     return this.ecuCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBackendCertificate() {
/*  90 */     return this.backendCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBackendCertificate(byte[] backendCertificate) {
/* 100 */     this.backendCertificate = backendCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getRootCertificate() {
/* 109 */     return this.rootCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRootCertificate(byte[] rootCertificate) {
/* 119 */     this.rootCertificate = rootCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getLinkCertificate() {
/* 128 */     return this.linkCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLinkCertificate(byte[] linkCertificate) {
/* 138 */     this.linkCertificate = linkCertificate;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\CertificateReplacementPackageV1Result.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */