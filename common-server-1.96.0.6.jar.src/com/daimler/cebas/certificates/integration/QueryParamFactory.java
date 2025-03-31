/*    */ package com.daimler.cebas.certificates.integration;
/*    */ 
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.util.LinkedMultiValueMap;
/*    */ import org.springframework.util.MultiValueMap;
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
/*    */ public final class QueryParamFactory
/*    */ {
/*    */   private static final String SKI = "ski";
/*    */   private static final String CA_IDENTIFIER = "caId";
/*    */   private static final String SOURCE_CA = "sourceCa";
/*    */   private static final String TARGET_CA = "targetCa";
/*    */   
/*    */   public static MultiValueMap<String, String> createRootAndBackendIdentifierParam(String preactiveCaId) {
/* 28 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 29 */     if (StringUtils.isNotEmpty(preactiveCaId)) {
/* 30 */       linkedMultiValueMap.add("caId", preactiveCaId);
/*    */     }
/* 32 */     return (MultiValueMap<String, String>)linkedMultiValueMap;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static MultiValueMap<String, String> createCertChainParam(String subjectKeyIdentifier) {
/* 42 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 43 */     linkedMultiValueMap.add("ski", subjectKeyIdentifier);
/* 44 */     return (MultiValueMap<String, String>)linkedMultiValueMap;
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
/*    */   
/*    */   public static MultiValueMap<String, String> createLinkCertParam(String sourceCa, String targetCa) {
/* 57 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 58 */     if (StringUtils.isNotEmpty(sourceCa)) {
/* 59 */       linkedMultiValueMap.add("sourceCa", sourceCa);
/*    */     }
/* 61 */     if (StringUtils.isNotEmpty(targetCa)) {
/* 62 */       linkedMultiValueMap.add("targetCa", targetCa);
/*    */     }
/* 64 */     return (MultiValueMap<String, String>)linkedMultiValueMap;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static MultiValueMap<String, String> createNonVsmIdentifierParam(String backendSKI) {
/* 74 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 75 */     linkedMultiValueMap.add("caId", backendSKI);
/* 76 */     return (MultiValueMap<String, String>)linkedMultiValueMap;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\integration\QueryParamFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */