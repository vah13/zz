/*     */ package com.daimler.cebas.certificates.control.vo;
/*     */ 
/*     */ import com.daimler.cebas.common.entity.Versioned;
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
/*     */ 
/*     */ 
/*     */ public class ImportResult
/*     */   implements Versioned
/*     */ {
/*     */   private String fileName;
/*     */   private String subjectKeyIdentifier;
/*     */   private String authorityKeyIdentifier;
/*     */   private String message;
/*     */   private boolean isSuccess;
/*     */   
/*     */   public ImportResult() {}
/*     */   
/*     */   public ImportResult(String fileName, String subjectKeyIdentifier, String authorityKeyIdentifier, String message, boolean isSuccess) {
/*  61 */     this.fileName = fileName;
/*  62 */     this.subjectKeyIdentifier = subjectKeyIdentifier;
/*  63 */     this.authorityKeyIdentifier = authorityKeyIdentifier;
/*  64 */     this.message = message;
/*  65 */     this.isSuccess = isSuccess;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImportResult(String subjectKeyIdentifier, String authorityKeyIdentifier, String message, boolean isSuccess) {
/*  70 */     this.subjectKeyIdentifier = subjectKeyIdentifier;
/*  71 */     this.authorityKeyIdentifier = authorityKeyIdentifier;
/*  72 */     this.message = message;
/*  73 */     this.isSuccess = isSuccess;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/*  82 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAuthorityKeyIdentifier() {
/*  91 */     return this.authorityKeyIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubjectKeyIdentifier() {
/* 100 */     return this.subjectKeyIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 109 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessage(String message) {
/* 119 */     this.message = message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSuccess() {
/* 128 */     return this.isSuccess;
/*     */   }
/*     */ 
/*     */   
/*     */   public Versioned toVersion(int version) {
/* 133 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 138 */     return "ImportResult{fileName='" + this.fileName + '\'' + ", subjectKeyIdentifier='" + this.subjectKeyIdentifier + '\'' + ", authorityKeyIdentifier='" + this.authorityKeyIdentifier + '\'' + ", message='" + this.message + '\'' + ", isSuccess=" + this.isSuccess + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\ImportResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */