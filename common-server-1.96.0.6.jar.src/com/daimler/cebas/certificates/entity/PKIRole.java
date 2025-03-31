/*    */ package com.daimler.cebas.certificates.entity;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class PKIRole
/*    */ {
/* 17 */   protected static final Map<Integer, CertificateType> ROLES = createMap();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Map<Integer, CertificateType> createMap() {
/* 28 */     Map<Integer, CertificateType> myMap = new HashMap<>();
/* 29 */     myMap.put(Integer.valueOf(1), CertificateType.ROOT_CA_CERTIFICATE);
/* 30 */     myMap.put(Integer.valueOf(2), CertificateType.BACKEND_CA_CERTIFICATE);
/* 31 */     myMap.put(Integer.valueOf(3), CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE);
/* 32 */     myMap.put(Integer.valueOf(4), CertificateType.ECU_CERTIFICATE);
/* 33 */     myMap.put(Integer.valueOf(5), CertificateType.ENHANCED_RIGHTS_CERTIFICATE);
/* 34 */     myMap.put(Integer.valueOf(7), CertificateType.VARIANT_CODING_DEVICE_CERTIFICATE);
/* 35 */     myMap.put(Integer.valueOf(8), CertificateType.TIME_CERTIFICATE);
/* 36 */     myMap.put(Integer.valueOf(9), CertificateType.VARIANT_CODE_USER_CERTIFICATE);
/* 37 */     myMap.put(Integer.valueOf(10), CertificateType.ROOT_CA_LINK_CERTIFICATE);
/* 38 */     myMap.put(Integer.valueOf(11), CertificateType.BACKEND_CA_LINK_CERTIFICATE);
/* 39 */     return myMap;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Map<Integer, CertificateType> getRoles() {
/* 48 */     return ROLES;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Integer getPKIRoleFromCertificateType(CertificateType certificateType) {
/* 59 */     if (certificateType == CertificateType.TIME_CERTIFICATE) {
/* 60 */       return Integer.valueOf(3);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 66 */     Optional<Map.Entry<Integer, CertificateType>> value = ROLES.entrySet().stream().filter(entry -> certificateType.equals(entry.getValue())).findFirst();
/* 67 */     return value.<Integer>map(Map.Entry::getKey).orElse(null);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\entity\PKIRole.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */