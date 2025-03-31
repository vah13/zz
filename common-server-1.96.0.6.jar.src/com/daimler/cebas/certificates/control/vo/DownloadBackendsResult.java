/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import com.daimler.cebas.certificates.integration.vo.BackendIdentifier;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DownloadBackendsResult
/*    */ {
/*    */   private List<ImportResult> importResult;
/*    */   private List<BackendIdentifier> backendIdentifiers;
/*    */   
/*    */   public DownloadBackendsResult() {
/* 17 */     this.importResult = Collections.emptyList();
/* 18 */     this.backendIdentifiers = Collections.emptyList();
/*    */   }
/*    */ 
/*    */   
/*    */   public DownloadBackendsResult(List<ImportResult> importResult, List<BackendIdentifier> backendIdentifiers) {
/* 23 */     this.importResult = importResult;
/* 24 */     this.backendIdentifiers = backendIdentifiers;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<ImportResult> getImportResult() {
/* 31 */     return this.importResult;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setImportResult(List<ImportResult> importResult) {
/* 38 */     this.importResult = importResult;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<BackendIdentifier> getBackendIdentifiers() {
/* 45 */     return this.backendIdentifiers;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBackendIdentifiers(List<BackendIdentifier> backendIdentifiers) {
/* 52 */     this.backendIdentifiers = backendIdentifiers;
/*    */   }
/*    */   
/*    */   public boolean importExecuted() {
/* 56 */     return (this.importResult.size() > 0);
/*    */   }
/*    */   
/*    */   public Optional<BackendIdentifier> getBackendIdentifierBasedOnSki(String ski) {
/* 60 */     return this.backendIdentifiers.stream().filter(bi -> ski.replaceAll("-", "").equalsIgnoreCase(bi.getSubjectKeyIdentifier())).findFirst();
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\DownloadBackendsResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */