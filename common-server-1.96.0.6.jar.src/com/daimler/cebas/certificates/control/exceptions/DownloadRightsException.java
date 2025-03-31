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
/*    */ 
/*    */ public class DownloadRightsException
/*    */   extends CEBASException
/*    */ {
/*    */   private static final long serialVersionUID = 7112543455849045254L;
/*    */   private final int pkiErrorCode;
/*    */   
/*    */   public DownloadRightsException() {
/* 27 */     super("");
/* 28 */     this.pkiErrorCode = 0;
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
/*    */   public DownloadRightsException(String message, int pkiErrorCode) {
/* 40 */     super(message);
/* 41 */     this.pkiErrorCode = pkiErrorCode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getPkiErrorCode() {
/* 50 */     return this.pkiErrorCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\exceptions\DownloadRightsException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */