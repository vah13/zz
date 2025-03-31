/*     */ package com.daimler.cebas.certificates.control.validation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class SystemIntegrityCheckResult
/*     */ {
/*  30 */   private int totalNumberOfCheckedCertificates = 0;
/*  31 */   private List<SystemIntegrityCheckError> integrityCheckErrors = new ArrayList<>();
/*  32 */   private String integrityCheckXML = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int getTotalNumberOfCheckedCertificates() {
/*  41 */     return this.totalNumberOfCheckedCertificates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int getTotalNumberOfFailedChecks() {
/*  50 */     return this.integrityCheckErrors.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized List<SystemIntegrityCheckError> getIntegrityCheckErrors() {
/*  59 */     return this.integrityCheckErrors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getIntegrityCheckXML() {
/*  68 */     return this.integrityCheckXML;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void updateErrosList(List<SystemIntegrityCheckError> newErrorsList) {
/*  78 */     if (this.integrityCheckErrors != null) {
/*  79 */       this.integrityCheckErrors.clear();
/*  80 */       this.integrityCheckErrors.addAll(newErrorsList);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void updateTotalNumberOfCheckedCertificates(int newTotalOfCheckedCertificates) {
/*  91 */     this.totalNumberOfCheckedCertificates = newTotalOfCheckedCertificates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void updateIntegrityCheckXML(String newXML) {
/* 101 */     this.integrityCheckXML = newXML;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void clear() {
/* 109 */     if (this.integrityCheckErrors != null) {
/* 110 */       this.integrityCheckErrors.clear();
/*     */     }
/* 112 */     this.totalNumberOfCheckedCertificates = 0;
/* 113 */     this.integrityCheckXML = "";
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 118 */     return "SystemIntegrityCheckResult [totalNumberOfCheckedCertificates=" + this.totalNumberOfCheckedCertificates + ", totalNumberOfFailedChecks=" + this.integrityCheckErrors
/* 119 */       .size() + ", integrityCheckErrors=" + this.integrityCheckErrors + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\SystemIntegrityCheckResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */