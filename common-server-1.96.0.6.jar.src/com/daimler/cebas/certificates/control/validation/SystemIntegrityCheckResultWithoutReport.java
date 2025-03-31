/*    */ package com.daimler.cebas.certificates.control.validation;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
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
/*    */ public class SystemIntegrityCheckResultWithoutReport
/*    */ {
/*    */   private int totalNumberOfCheckedCertificates;
/*    */   private Map<String, List<String>> integrityCheckErrorMap;
/*    */   
/*    */   public SystemIntegrityCheckResultWithoutReport(int totalNumberOfCheckedCertificates, List<SystemIntegrityCheckError> integrityCheckErrors) {
/* 26 */     this.totalNumberOfCheckedCertificates = totalNumberOfCheckedCertificates;
/* 27 */     this
/* 28 */       .integrityCheckErrorMap = (Map<String, List<String>>)integrityCheckErrors.stream().collect(Collectors.toMap(SystemIntegrityCheckError::getCertificateId, SystemIntegrityCheckError::getMessageIds));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getTotalNumberOfCheckedCertificates() {
/* 37 */     return this.totalNumberOfCheckedCertificates;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getTotalNumberOfFailedChecks() {
/* 46 */     return this.integrityCheckErrorMap.size();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, List<String>> getIntegrityCheckErrorMap() {
/* 55 */     return this.integrityCheckErrorMap;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\SystemIntegrityCheckResultWithoutReport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */