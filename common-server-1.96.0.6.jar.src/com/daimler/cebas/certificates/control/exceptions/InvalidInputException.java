/*    */ package com.daimler.cebas.certificates.control.exceptions;
/*    */ 
/*    */ import com.daimler.cebas.common.control.CEBASException;
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
/*    */ public class InvalidInputException
/*    */   extends CEBASException
/*    */ {
/*    */   private static final long serialVersionUID = -7346119774762425763L;
/*    */   
/*    */   public InvalidInputException(String message) {
/* 22 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InvalidInputException(String message, String messageId) {
/* 34 */     super(message, messageId);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\exceptions\InvalidInputException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */