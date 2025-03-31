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
/*    */ public class CertificatesUpdateNoSessionExeption
/*    */   extends CertificatesUpdateException
/*    */ {
/*    */   private static final long serialVersionUID = -1874631156751941549L;
/*    */   private String pkiErrorMessage;
/*    */   private String pkiStatusCode;
/*    */   
/*    */   public CertificatesUpdateNoSessionExeption(String message) {
/* 27 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CertificatesUpdateNoSessionExeption(String message, String pkiErrorMessage, String pkiStatusCode) {
/* 38 */     super(message);
/* 39 */     this.pkiErrorMessage = pkiErrorMessage;
/* 40 */     this.pkiStatusCode = pkiStatusCode;
/*    */   }
/*    */   
/*    */   public String getPkiErrorMessage() {
/* 44 */     return this.pkiErrorMessage;
/*    */   }
/*    */   
/*    */   public String getPkiStatusCode() {
/* 48 */     return this.pkiStatusCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\exceptions\CertificatesUpdateNoSessionExeption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */