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
/*    */ 
/*    */ 
/*    */ public class FilterObject
/*    */ {
/*    */   private final boolean match;
/*    */   private final Object value;
/*    */   
/*    */   public FilterObject(boolean match, Object value) {
/* 28 */     this.match = match;
/* 29 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isMatch() {
/* 38 */     return this.match;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getFilterValue() {
/* 47 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\vo\FilterObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */