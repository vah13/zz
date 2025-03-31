/*     */ package com.daimler.cebas.certificates.control.vo;
/*     */ 
/*     */ import com.daimler.cebas.common.control.vo.CEBASResult;
/*     */ import com.daimler.cebas.common.entity.Versioned;
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
/*     */ @ApiModel
/*     */ public class CertificateReplacementPackageExtendedResult
/*     */   extends CEBASResult
/*     */ {
/*     */   private ReplacementTarget target;
/*     */   @ApiModelProperty
/*     */   private ExtendedCertificateWithSnSkiResult ecuCertificate;
/*     */   @ApiModelProperty
/*     */   private ExtendedCertificateWithSnSkiResult backendCertificate;
/*     */   @ApiModelProperty
/*     */   private ExtendedCertificateWithSnSkiResult rootCertificate;
/*     */   @ApiModelProperty
/*     */   private ExtendedCertificateWithSnSkiResult linkCertificate;
/*     */   
/*     */   public CertificateReplacementPackageExtendedResult() {}
/*     */   
/*     */   public CertificateReplacementPackageExtendedResult(String errorMessage) {
/*  58 */     super(errorMessage);
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
/*     */   public CertificateReplacementPackageExtendedResult(ReplacementTarget replacementTarget, ExtendedCertificateWithSnSkiResult ecuCertificate) {
/*  70 */     this.target = replacementTarget;
/*  71 */     this.ecuCertificate = ecuCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReplacementTarget getTarget() {
/*  80 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedCertificateWithSnSkiResult getEcuCertificate() {
/*  89 */     return this.ecuCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEcuCertificate(ExtendedCertificateWithSnSkiResult ecuCertificate) {
/*  99 */     this.ecuCertificate = ecuCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedCertificateWithSnSkiResult getBackendCertificate() {
/* 108 */     return this.backendCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBackendCertificate(ExtendedCertificateWithSnSkiResult backendCertificate) {
/* 118 */     this.backendCertificate = backendCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedCertificateWithSnSkiResult getRootCertificate() {
/* 127 */     return this.rootCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRootCertificate(ExtendedCertificateWithSnSkiResult rootCertificate) {
/* 137 */     this.rootCertificate = rootCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedCertificateWithSnSkiResult getLinkCertificate() {
/* 146 */     return this.linkCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLinkCertificate(ExtendedCertificateWithSnSkiResult linkCertificate) {
/* 156 */     this.linkCertificate = linkCertificate;
/*     */   }
/*     */ 
/*     */   
/*     */   public Versioned toVersion(int version) {
/* 161 */     if (version == 2) {
/* 162 */       return (Versioned)this;
/*     */     }
/* 164 */     return super.toVersion(version);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\CertificateReplacementPackageExtendedResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */