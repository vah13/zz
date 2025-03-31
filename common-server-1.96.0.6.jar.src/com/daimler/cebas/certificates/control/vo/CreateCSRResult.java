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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CreateCSRResult
/*    */   extends CEBASResult
/*    */ {
/*    */   private String internalID;
/*    */   private String subjectPublicKey;
/*    */   
/*    */   public CreateCSRResult() {}
/*    */   
/*    */   public CreateCSRResult(String internalID, String subjectPublicKey) {
/* 36 */     this.internalID = internalID;
/* 37 */     this.subjectPublicKey = subjectPublicKey;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getInternalID() {
/* 46 */     return this.internalID;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSubjectPublicKey() {
/* 55 */     return this.subjectPublicKey;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\CreateCSRResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */