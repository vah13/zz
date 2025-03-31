/*    */ package com.daimler.cebas.system.control.validation.vo;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum CertificateTableColumns
/*    */ {
/*  7 */   DEFAULT("default"), ACTIVE_FOR_TESTING("activeForTesting"), SUBJECT("subject"), PKI_ROLE("pkirole"), USER_ROLE("userRole"),
/*  8 */   ISSUER("issuer"), SERIAL_NO("serialNo"), TARGET_ECU("targetECU"), TARGET_VIN("targetVIN"),
/*  9 */   VALID_TO("validTo"), VALIDITY_STRENGTHCOLOR("validityStrengthColor"), VALID_FROM("validFrom"),
/* 10 */   UNIQUE_ECU_ID("uniqueECUID"), SPECIAL_ECU("specialECU"), SUBJECT_PUBLIC_KEY("subjectPublicKey"),
/* 11 */   BASE_CERTIFICATE_ID("baseCertificateID"), ISSUER_SERIAL_NUMBER("issuerSerialNumber"),
/* 12 */   sUBJECT_KEY_IDENTIFIER("subjectKeyIdentifier"),
/* 13 */   AUTHORITY_KEY_IDENTIFIER("authorityKeyIdentifier"),
/* 14 */   SERVICES("services"), NONCE("nonce"),
/* 15 */   SIGNATURE("signature"),
/* 16 */   TARGET_SUBJECT_KEY_IDENTIFIER("targetSubjectKeyIdentifier");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private String text;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   CertificateTableColumns(String text) {
/* 30 */     this.text = text;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getText() {
/* 39 */     return this.text;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean valueFromString(String column) {
/* 50 */     boolean isColumnValid = false;
/* 51 */     for (CertificateTableColumns certificateTableColumns : values()) {
/* 52 */       if (certificateTableColumns.getText().contains(column)) {
/* 53 */         isColumnValid = true;
/*    */         break;
/*    */       } 
/* 56 */       isColumnValid = false;
/*    */     } 
/*    */     
/* 59 */     return isColumnValid;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\validation\vo\CertificateTableColumns.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */