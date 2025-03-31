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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PKCS10GenerationException
/*    */   extends CEBASException
/*    */ {
/*    */   private static final long serialVersionUID = 7054049113637415756L;
/*    */   
/*    */   public PKCS10GenerationException(String message) {
/* 25 */     super(message);
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
/*    */   public PKCS10GenerationException(String message, String messageId) {
/* 37 */     super(message, messageId);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\exceptions\PKCS10GenerationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */