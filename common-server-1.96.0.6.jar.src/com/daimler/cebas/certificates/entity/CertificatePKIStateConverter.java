/*    */ package com.daimler.cebas.certificates.entity;
/*    */ 
/*    */ import javax.persistence.AttributeConverter;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CertificatePKIStateConverter
/*    */   implements AttributeConverter<CertificatePKIState, String>
/*    */ {
/*    */   public String convertToDatabaseColumn(CertificatePKIState pkiState) {
/* 16 */     if (pkiState == null) {
/* 17 */       return CertificatePKIState.NONE.getValue();
/*    */     }
/* 19 */     return pkiState.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public CertificatePKIState convertToEntityAttribute(String value) {
/* 24 */     if (StringUtils.isEmpty(value)) {
/* 25 */       return CertificatePKIState.NONE;
/*    */     }
/* 27 */     return CertificatePKIState.getFromValue(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\entity\CertificatePKIStateConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */