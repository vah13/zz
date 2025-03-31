/*    */ package com.daimler.cebas.system.control.exceptions;
/*    */ 
/*    */ import com.daimler.cebas.common.control.CEBASException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnauthorizedOperationException
/*    */   extends CEBASException
/*    */ {
/*    */   private static final long serialVersionUID = -4652974986645179154L;
/*    */   
/*    */   public UnauthorizedOperationException(String message) {
/* 13 */     super(message);
/*    */   }
/*    */   
/*    */   public UnauthorizedOperationException(String message, String messageId) {
/* 17 */     super(message, messageId);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\exceptions\UnauthorizedOperationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */