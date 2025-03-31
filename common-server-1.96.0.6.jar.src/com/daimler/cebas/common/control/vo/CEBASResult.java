/*    */ package com.daimler.cebas.common.control.vo;
/*    */ 
/*    */ import com.daimler.cebas.common.entity.Versioned;
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
/*    */ public class CEBASResult
/*    */   implements Versioned
/*    */ {
/*    */   protected String errorMessage;
/*    */   
/*    */   public CEBASResult() {}
/*    */   
/*    */   public CEBASResult(String errorMessage) {
/* 23 */     this.errorMessage = errorMessage;
/*    */   }
/*    */   
/*    */   public String getErrorMessage() {
/* 27 */     return this.errorMessage;
/*    */   }
/*    */ 
/*    */   
/*    */   public Versioned toVersion(int version) {
/* 32 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\vo\CEBASResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */