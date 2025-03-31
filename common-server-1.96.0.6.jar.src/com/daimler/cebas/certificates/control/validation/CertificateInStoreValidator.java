/*    */ package com.daimler.cebas.certificates.control.validation;
/*    */ 
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import org.apache.commons.lang3.StringUtils;
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
/*    */ public class CertificateInStoreValidator
/*    */ {
/* 19 */   private static final String CLASS_NAME = CertificateInStoreValidator.class
/* 20 */     .getSimpleName();
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
/*    */   public static boolean isInStore(Certificate certificate, String userId, Logger logger) {
/* 42 */     String METHOD_NAME = "isInStore";
/* 43 */     logger.entering(CLASS_NAME, "isInStore");
/* 44 */     logger.exiting(CLASS_NAME, "isInStore");
/* 45 */     return StringUtils.equals(certificate.getUser().getEntityId(), userId);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\CertificateInStoreValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */