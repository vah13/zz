/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExtendedImportResult
/*    */   extends ImportResult
/*    */ {
/*    */   private String internalId;
/*    */   private String backendZkNo;
/*    */   
/*    */   public ExtendedImportResult() {}
/*    */   
/*    */   public ExtendedImportResult(String subjectKeyIdentifier, String authorityKeyIdentifier, String message, boolean isSuccess, String internalId, String backendZkNo) {
/* 42 */     super(subjectKeyIdentifier, authorityKeyIdentifier, message, isSuccess);
/* 43 */     this.internalId = internalId;
/* 44 */     this.backendZkNo = backendZkNo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getInternalId() {
/* 53 */     return this.internalId;
/*    */   }
/*    */   
/*    */   public String getBackendZkNo() {
/* 57 */     return this.backendZkNo;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\ExtendedImportResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */