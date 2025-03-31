/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import com.daimler.cebas.common.control.vo.CEBASResult;
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
/*    */ 
/*    */ public class RollbackCertificate
/*    */   extends CEBASResult
/*    */ {
/*    */   private String backendKeyIdentifier;
/*    */   private String serialNumber;
/*    */   private RollbackMode rollbackMode;
/*    */   
/*    */   public RollbackCertificate() {}
/*    */   
/*    */   public RollbackCertificate(String backendKeyIdentifier, String serialNumber, RollbackMode rollbackMode) {
/* 46 */     this.backendKeyIdentifier = backendKeyIdentifier;
/* 47 */     this.serialNumber = serialNumber;
/* 48 */     this.rollbackMode = rollbackMode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getBackendKeyIdentifier() {
/* 58 */     return this.backendKeyIdentifier;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSerialNumber() {
/* 67 */     return this.serialNumber;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RollbackMode getRollbackMode() {
/* 76 */     return this.rollbackMode;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public enum RollbackMode
/*    */   {
/* 83 */     ROLLBACK_ON, ROLLBACK_OFF;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\RollbackCertificate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */