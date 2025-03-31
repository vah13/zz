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
/*     */ @ApiModel
/*     */ public class ChainReplacementPackageResult
/*     */   extends CEBASResult
/*     */ {
/*     */   @ApiModelProperty
/*     */   private ExtendedCertificateWithSnSkiResult rootCertificate;
/*     */   @ApiModelProperty
/*     */   private ExtendedCertificateWithSnSkiResult backendCertificate;
/*     */   @ApiModelProperty
/*     */   private ExtendedCertificateWithSnSkiResult linkCertificate;
/*     */   
/*     */   public ChainReplacementPackageResult() {}
/*     */   
/*     */   public ChainReplacementPackageResult(String errorMessage) {
/*  47 */     super(errorMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedCertificateWithSnSkiResult getRootCertificate() {
/*  56 */     return this.rootCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRootCertificate(ExtendedCertificateWithSnSkiResult rootCertificate) {
/*  66 */     this.rootCertificate = rootCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedCertificateWithSnSkiResult getBackendCertificate() {
/*  75 */     return this.backendCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBackendCertificate(ExtendedCertificateWithSnSkiResult backendCertificate) {
/*  85 */     this.backendCertificate = backendCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedCertificateWithSnSkiResult getLinkCertificate() {
/*  94 */     return this.linkCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLinkCertificate(ExtendedCertificateWithSnSkiResult linkCertificate) {
/* 104 */     this.linkCertificate = linkCertificate;
/*     */   }
/*     */ 
/*     */   
/*     */   public Versioned toVersion(int version) {
/* 109 */     if (version == 2) {
/* 110 */       return (Versioned)this;
/*     */     }
/* 112 */     return super.toVersion(version);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\ChainReplacementPackageResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */