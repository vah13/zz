/*    */ package com.daimler.cebas.common.control.vo;
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
/*    */ public class ExtendedFilterObject
/*    */   extends FilterObject
/*    */ {
/*    */   private String orSomethingElse;
/*    */   
/*    */   public ExtendedFilterObject(boolean match, Object value) {
/* 26 */     super(match, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ExtendedFilterObject(boolean match, Object value, String orSomethingElse) {
/* 37 */     super(match, value);
/* 38 */     this.orSomethingElse = orSomethingElse;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getOrSomethingElse() {
/* 47 */     return this.orSomethingElse;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\vo\ExtendedFilterObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */