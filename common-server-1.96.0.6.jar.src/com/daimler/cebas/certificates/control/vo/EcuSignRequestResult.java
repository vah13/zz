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
/*    */ public class EcuSignRequestResult
/*    */   extends CEBASResult
/*    */ {
/*    */   private String signature;
/*    */   private String ecuCertificate;
/*    */   private Long expirationDate;
/*    */   private String serialNumber;
/*    */   
/*    */   public EcuSignRequestResult() {}
/*    */   
/*    */   public EcuSignRequestResult(String signature, String ecuCertificate, Long expirationDate, String serialNumber) {
/* 54 */     this.signature = signature;
/* 55 */     this.ecuCertificate = ecuCertificate;
/* 56 */     this.expirationDate = expirationDate;
/* 57 */     this.serialNumber = serialNumber;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSignature() {
/* 66 */     return this.signature;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getEcuCertificate() {
/* 75 */     return this.ecuCertificate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Long getExpirationDate() {
/* 84 */     return this.expirationDate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSerialNumber() {
/* 93 */     return this.serialNumber;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\EcuSignRequestResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */