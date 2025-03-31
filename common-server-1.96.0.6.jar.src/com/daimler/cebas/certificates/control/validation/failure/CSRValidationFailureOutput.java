/*    */ package com.daimler.cebas.certificates.control.validation.failure;
/*    */ 
/*    */ import com.daimler.cebas.common.control.CEBASException;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import java.util.logging.Level;
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
/*    */ public class CSRValidationFailureOutput
/*    */   implements ValidationFailureOutput
/*    */ {
/*    */   private ValidationState csr;
/*    */   private ValidationState permission;
/*    */   private Logger logger;
/*    */   
/*    */   public CSRValidationFailureOutput(ValidationState permission, ValidationState csr, Logger logger) {
/* 43 */     this.csr = csr;
/* 44 */     this.logger = logger;
/* 45 */     this.permission = permission;
/*    */   }
/*    */ 
/*    */   
/*    */   public void outputFailure(CEBASException exception) {
/* 50 */     this.permission.setValid(false);
/* 51 */     this.csr.setValid(false);
/* 52 */     this.logger.log(Level.WARNING, "000317X", "Skipping CSR generation from permission: " + this.permission
/*    */         
/* 54 */         .toString() + " Reason: " + exception
/* 55 */         .getMessage(), 
/* 56 */         getClass().getSimpleName());
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\failure\CSRValidationFailureOutput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */