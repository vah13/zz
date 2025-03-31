/*    */ package com.daimler.cebas.common.entity;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ 
/*    */ public class InPredicate
/*    */ {
/*    */   private List<?> predicates;
/*    */   
/*    */   public InPredicate(Object... predicates) {
/* 11 */     this.predicates = Arrays.asList(predicates);
/*    */   }
/*    */   
/*    */   public List<?> getPredicates() {
/* 15 */     return this.predicates;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\entity\InPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */