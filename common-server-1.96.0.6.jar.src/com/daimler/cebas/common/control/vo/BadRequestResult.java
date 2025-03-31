/*    */ package com.daimler.cebas.common.control.vo;
/*    */ 
/*    */ public class BadRequestResult {
/*    */   private final long timeStamp;
/*    */   private final int status;
/*    */   private final String error;
/*    */   private final String message;
/*    */   private final String path;
/*    */   
/*    */   public BadRequestResult(long timeStamp, int status, String error, String message, String path) {
/* 11 */     this.timeStamp = timeStamp;
/* 12 */     this.status = status;
/* 13 */     this.error = error;
/* 14 */     this.message = message;
/* 15 */     this.path = path;
/*    */   }
/*    */   
/*    */   public long getTimeStamp() {
/* 19 */     return this.timeStamp;
/*    */   }
/*    */   
/*    */   public int getStatus() {
/* 23 */     return this.status;
/*    */   }
/*    */   
/*    */   public String getError() {
/* 27 */     return this.error;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 31 */     return this.message;
/*    */   }
/*    */   
/*    */   public String getPath() {
/* 35 */     return this.path;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\vo\BadRequestResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */