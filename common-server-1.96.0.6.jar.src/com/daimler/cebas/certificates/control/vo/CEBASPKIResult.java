/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import com.daimler.cebas.common.control.vo.CEBASResult;
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
/*    */ public class CEBASPKIResult
/*    */   extends CEBASResult
/*    */ {
/*    */   private String pkiErrorMessage;
/*    */   private String pkiStatusCode;
/*    */   
/*    */   public CEBASPKIResult(String errorMessage, String pkiErrorMessage, String pkiStatusCode) {
/* 28 */     super(errorMessage);
/* 29 */     this.pkiErrorMessage = pkiErrorMessage;
/* 30 */     this.pkiStatusCode = pkiStatusCode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPkiErrorMessage() {
/* 39 */     return this.pkiErrorMessage;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPkiStatusCode() {
/* 47 */     return this.pkiStatusCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\CEBASPKIResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */