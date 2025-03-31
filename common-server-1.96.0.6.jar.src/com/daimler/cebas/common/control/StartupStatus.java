/*    */ package com.daimler.cebas.common.control;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StartupStatus
/*    */ {
/*    */   private static boolean started = false;
/*    */   
/*    */   public static boolean isApplicationStarted() {
/* 12 */     return started;
/*    */   }
/*    */   
/*    */   public static void setStarted() {
/* 16 */     started = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\StartupStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */