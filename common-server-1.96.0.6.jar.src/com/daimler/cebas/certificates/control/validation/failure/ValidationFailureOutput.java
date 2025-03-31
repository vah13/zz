/*    */ package com.daimler.cebas.certificates.control.validation.failure;
/*    */ 
/*    */ import com.daimler.cebas.common.control.CEBASException;
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
/*    */ @FunctionalInterface
/*    */ public interface ValidationFailureOutput
/*    */ {
/*    */   void outputFailure(CEBASException paramCEBASException);
/*    */   
/*    */   static void outputFailureWithThrow(CEBASException exception) {
/* 29 */     throw exception;
/*    */   }
/*    */   
/*    */   static void skipeFailureOutput(CEBASException exception) {}
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\failure\ValidationFailureOutput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */