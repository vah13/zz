/*    */ package com.daimler.cebas.certificates.control.chain;
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
/*    */ public class ChainIdentifier
/*    */ {
/*    */   private String authorityKeyIdentifier;
/*    */   private String subjectKeyIdentifier;
/*    */   private String serialNo;
/*    */   private String subjectPublicKey;
/*    */   
/*    */   public ChainIdentifier(String authorityKeyIdentifier, String subjectKeyIdentifier, String serialNo, String subjectPublicKey) {
/* 47 */     this.authorityKeyIdentifier = authorityKeyIdentifier;
/* 48 */     this.subjectKeyIdentifier = subjectKeyIdentifier;
/* 49 */     this.serialNo = serialNo;
/* 50 */     this.subjectPublicKey = subjectPublicKey;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAuthorityKeyIdentifier() {
/* 59 */     return this.authorityKeyIdentifier;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSubjectKeyIdentifier() {
/* 68 */     return this.subjectKeyIdentifier;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSerialNo() {
/* 77 */     return this.serialNo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSubjectPublicKey() {
/* 86 */     return this.subjectPublicKey;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\chain\ChainIdentifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */