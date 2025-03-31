/*    */ package com.daimler.cebas.common.entity;
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
/*    */ public class EmptyPredicate
/*    */ {
/*    */   private final boolean canBeEmpty;
/*    */   
/*    */   public EmptyPredicate(boolean canBeEmpty) {
/* 18 */     this.canBeEmpty = canBeEmpty;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canBeEmpty() {
/* 28 */     return this.canBeEmpty;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\entity\EmptyPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */