/*    */ package com.daimler.cebas.system.control.alerts;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum AlertMessageId
/*    */ {
/*  8 */   SYSTEM_CLOCK_IS_NOT_SYNCRONOUS_WITH_PKI("system_clock_is_not_synchronous_with_pki");
/*    */   
/*    */   private final String value;
/*    */   
/*    */   AlertMessageId(String value) {
/* 13 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 17 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\alerts\AlertMessageId.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */