/*    */ package com.daimler.cebas.users.control.exceptions;
/*    */ 
/*    */ import com.daimler.cebas.common.control.CEBASException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserLoginException
/*    */   extends CEBASException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public UserLoginException(String message) {
/* 16 */     super(message);
/*    */   }
/*    */   
/*    */   public UserLoginException(String message, String messageId) {
/* 20 */     super(message, messageId);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\ceba\\users\control\exceptions\UserLoginException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */