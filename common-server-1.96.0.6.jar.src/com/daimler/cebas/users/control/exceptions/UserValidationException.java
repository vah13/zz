/*    */ package com.daimler.cebas.users.control.exceptions;
/*    */ 
/*    */ import com.daimler.cebas.common.control.CEBASException;
/*    */ import com.daimler.cebas.users.entity.User;
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
/*    */ public class UserValidationException
/*    */   extends CEBASException
/*    */ {
/*    */   private static final long serialVersionUID = 7414644012725320245L;
/*    */   private final User user;
/*    */   
/*    */   public UserValidationException(User user, String message) {
/* 31 */     super(message);
/* 32 */     this.user = user;
/*    */   }
/*    */ 
/*    */   
/*    */   public UserValidationException(User user, String message, String messageId) {
/* 37 */     super(message, messageId);
/* 38 */     this.user = user;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public User getUser() {
/* 45 */     return this.user;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\ceba\\users\control\exceptions\UserValidationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */