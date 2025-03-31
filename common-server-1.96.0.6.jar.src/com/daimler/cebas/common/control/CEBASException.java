/*    */ package com.daimler.cebas.common.control;
/*    */ 
/*    */ import org.apache.commons.lang3.StringUtils;
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
/*    */ public class CEBASException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 3182248433901239456L;
/*    */   protected String messageId;
/*    */   
/*    */   public CEBASException(String message) {
/* 28 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CEBASException(String message, String messageId) {
/* 38 */     super(message);
/* 39 */     this.messageId = messageId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessageId() {
/* 48 */     return this.messageId;
/*    */   }
/*    */   
/*    */   public void setMessageId(String messageId) {
/* 52 */     this.messageId = messageId;
/*    */   }
/*    */   
/*    */   public boolean hasMessageId() {
/* 56 */     return !StringUtils.isEmpty(this.messageId);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean hasCEBASExceptionCause(Throwable e) {
/* 66 */     return hasCause(e, CEBASException.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean hasCause(Throwable e, Class<?> clazz) {
/* 77 */     if (e == null) {
/* 78 */       return false;
/*    */     }
/* 80 */     if (clazz.isAssignableFrom(e.getClass())) {
/* 81 */       return true;
/*    */     }
/* 83 */     return hasCause(e.getCause(), clazz);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\CEBASException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */