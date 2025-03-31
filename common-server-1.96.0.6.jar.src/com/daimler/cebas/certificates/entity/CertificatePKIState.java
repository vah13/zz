/*    */ package com.daimler.cebas.certificates.entity;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*    */ import com.fasterxml.jackson.annotation.JsonValue;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum CertificatePKIState
/*    */ {
/* 15 */   ACTIVE("active"), PREACTIVE("preactivation"), NONE("");
/*    */   
/*    */   private String value;
/*    */   
/*    */   CertificatePKIState(String value) {
/* 20 */     this.value = value;
/*    */   }
/*    */   
/*    */   @JsonValue
/*    */   public String getValue() {
/* 25 */     return this.value;
/*    */   }
/*    */   
/*    */   public static CertificatePKIState getFromValue(String value) {
/* 29 */     return (CertificatePKIState)Stream.<CertificatePKIState>of(values())
/* 30 */       .filter(pkiState -> pkiState.getValue().equals(value))
/* 31 */       .findFirst()
/* 32 */       .orElseThrow(() -> new CEBASCertificateException("Invalid certificate pki state: " + value));
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\entity\CertificatePKIState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */