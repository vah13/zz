/*    */ package com.daimler.cebas.certificates.control.update;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum UpdateStatus
/*    */ {
/* 10 */   RUNNING("updateCertificatesStatusRunning"),
/* 11 */   NOT_RUNNING("updateCertificatesStatusNotRunning"),
/* 12 */   PAUSED("updateCertificatesStatusPause");
/*    */   
/*    */   private String text;
/*    */   
/*    */   UpdateStatus(String text) {
/* 17 */     this.text = text;
/*    */   }
/*    */   
/*    */   public String getText() {
/* 21 */     return this.text;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\contro\\update\UpdateStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */