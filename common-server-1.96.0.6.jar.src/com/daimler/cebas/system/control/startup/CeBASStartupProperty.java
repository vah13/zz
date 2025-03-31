/*     */ package com.daimler.cebas.system.control.startup;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum CeBASStartupProperty
/*     */ {
/*  14 */   SECRET("SECRET", true, true, true, true, false, false),
/*     */   
/*  16 */   PKCS12_DEFAULT_PASSWORD("PKCS12_DEFAULT_PASSWORD", true, true, true, true, false, false),
/*     */   
/*  18 */   PKCS12_PACKAGE_PASSWORD("PKCS12_PACKAGE_PASSWORD", true, true, true, true, false, false),
/*     */   
/*  20 */   SPRING_DATASOURCE_PASSWORD("spring.datasource.password", true, true, true, true, false, false),
/*     */   
/*  22 */   SPRING_DATASOURCE_USERNAME("spring.datasource.username", true, true, true, true, false, false),
/*     */   
/*  24 */   SECURITY_OAUTH2_CLIENT_CLIENT_ID("spring.security.oauth2.client.registration.gas.client-id", false, true, true, true, false, false),
/*     */   
/*  26 */   SECURITY_OAUTH2_CLIENT_SECRET("spring.security.oauth2.client.registration.gas.client-secret", false, true, true, true, false, false),
/*     */   
/*  28 */   TEST_OIDC_CLIENT_ID("spring.security.oauth2.client.registration.gas.client-id", true, false, true, false, false, false),
/*     */   
/*  30 */   PROD_OIDC_CLIENT_ID("spring.security.oauth2.client.registration.gas.client-id", true, false, false, true, false, false),
/*     */   
/*  32 */   JWT_KEY("jwt.private.key", false, true, true, true, false, false),
/*     */   
/*  34 */   CSR_ENROLLMENT_ID("csr.enrollmentId", false, true, true, true, false, false),
/*     */   
/*  36 */   CSR_SUBJECT("csr.subject", false, true, true, true, true, true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String property;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean required;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean log;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean sigModul;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean zenzefi;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean test;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean prod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CeBASStartupProperty(String property, boolean zenzefi, boolean sigModul, boolean test, boolean prod, boolean required, boolean log) {
/*  94 */     this.property = property;
/*  95 */     this.sigModul = sigModul;
/*  96 */     this.zenzefi = zenzefi;
/*  97 */     this.test = test;
/*  98 */     this.prod = prod;
/*  99 */     this.required = required;
/* 100 */     this.log = log;
/*     */   }
/*     */   
/*     */   public String getProperty() {
/* 104 */     return this.property;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRequired() {
/* 113 */     return this.required;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLog() {
/* 122 */     return this.log;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTestEnvironmentRelevant() {
/* 131 */     return this.test;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProdEnvironmentRelevant() {
/* 140 */     return this.prod;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Constants
/*     */   {
/*     */     public static final String PKCS12_DEFAULT = "PKCS12_DEFAULT_PASSWORD";
/*     */ 
/*     */     
/*     */     public static final String OIDC_CLIENT_ID = "spring.security.oauth2.client.registration.gas.client-id";
/*     */ 
/*     */     
/*     */     public static final String OIDC_CLIENT_SECRET = "spring.security.oauth2.client.registration.gas.client-secret";
/*     */ 
/*     */     
/*     */     public static final String JWT_PRIVATE_KEY = "jwt.private.key";
/*     */ 
/*     */     
/*     */     public static final String CSR_ENROLLMENT_ID = "csr.enrollmentId";
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<CeBASStartupProperty> getSigModul() {
/* 164 */     Set<CeBASStartupProperty> result = new HashSet<>();
/* 165 */     for (CeBASStartupProperty p : values()) {
/* 166 */       if (p.sigModul) {
/* 167 */         result.add(p);
/*     */       }
/*     */     } 
/* 170 */     return result;
/*     */   }
/*     */   
/*     */   public static Set<CeBASStartupProperty> getZenzefi() {
/* 174 */     Set<CeBASStartupProperty> result = new HashSet<>();
/* 175 */     for (CeBASStartupProperty p : values()) {
/* 176 */       if (p.zenzefi) {
/* 177 */         result.add(p);
/*     */       }
/*     */     } 
/* 180 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\startup\CeBASStartupProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */