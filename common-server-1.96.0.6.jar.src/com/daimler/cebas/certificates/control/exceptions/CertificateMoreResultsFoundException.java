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
/*    */ public class CertificateMoreResultsFoundException
/*    */   extends CEBASException
/*    */ {
/*    */   private static final long serialVersionUID = -3322497479668575687L;
/*    */   
/*    */   public CertificateMoreResultsFoundException(String message) {
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
/*    */   public CertificateMoreResultsFoundException(String message, String messageId) {
/* 34 */     super(message, messageId);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\exceptions\CertificateMoreResultsFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */