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
/*    */ public class CertificateSigningException
/*    */   extends CEBASException
/*    */ {
/*    */   private static final long serialVersionUID = -5455723753606172337L;
/*    */   
/*    */   public CertificateSigningException(String message) {
/* 21 */     super(message);
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
/*    */   public CertificateSigningException(String message, String messageId) {
/* 33 */     super(message, messageId);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\exceptions\CertificateSigningException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */