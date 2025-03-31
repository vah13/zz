/*    */ package com.daimler.cebas.certificates.control.exceptions;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CertificateNotFoundOnDownloadException
/*    */   extends CertificateNotFoundException
/*    */ {
/*    */   private static final long serialVersionUID = 2106145295141804887L;
/*    */   private String pkiErrorMessage;
/*    */   private String pkiStatusCode;
/*    */   
/*    */   public CertificateNotFoundOnDownloadException(String message, String messageId) {
/* 33 */     super(message, messageId);
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
/*    */   
/*    */   public CertificateNotFoundOnDownloadException(String message, String messageId, String pkiErrorMessage, String pkiStatusCode) {
/* 46 */     super(message, messageId);
/* 47 */     this.pkiErrorMessage = pkiErrorMessage;
/* 48 */     this.pkiStatusCode = pkiStatusCode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CertificateNotFoundOnDownloadException(String message, String pkiErrorMessage, String pkiStatusCode) {
/* 59 */     super(message);
/* 60 */     this.pkiErrorMessage = pkiErrorMessage;
/* 61 */     this.pkiStatusCode = pkiStatusCode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPkiErrorMessage() {
/* 70 */     return this.pkiErrorMessage;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPkiStatusCode() {
/* 79 */     return this.pkiStatusCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\exceptions\CertificateNotFoundOnDownloadException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */