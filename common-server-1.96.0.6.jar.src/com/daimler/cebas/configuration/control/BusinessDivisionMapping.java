/*     */ package com.daimler.cebas.configuration.control;
/*     */ 
/*     */ import com.daimler.cebas.system.control.validation.vo.BusinessEnvironment;
/*     */ import java.net.InetAddress;
/*     */ import java.net.URL;
/*     */ import java.net.UnknownHostException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum BusinessDivisionMapping
/*     */ {
/*  19 */   RD("E"), PRODUCTION("P"), SUPPLIER("S"), AFTER_SALES("D");
/*     */   
/*     */   private String mapping;
/*     */   
/*     */   BusinessDivisionMapping(String mapping) {
/*  24 */     this.mapping = mapping;
/*     */   }
/*     */   
/*     */   public String getMapping() {
/*  28 */     return this.mapping;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getMapping(BusinessEnvironment businessEnv) {
/*  38 */     switch (businessEnv) {
/*     */       case DEVELOPMENT_INTERNAL:
/*  40 */         return RD.getMapping();
/*     */       case AFTERSALES:
/*  42 */         return AFTER_SALES.getMapping();
/*     */       case DEVELOPMENT_SUPPLIER:
/*  44 */         return SUPPLIER.getMapping();
/*     */       case PRODUCTION:
/*  46 */         return PRODUCTION.getMapping();
/*     */     } 
/*  48 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getMapping(BusinessEnvironment businessEnv, String pkiEnv, String pkiBaseUrl) {
/*  61 */     String pki = getPKIMapping(pkiEnv, pkiBaseUrl);
/*  62 */     return getMapping(businessEnv) + pki;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getPKIMapping(String pkiEnv, String pkiBaseUrl) {
/*  73 */     if (isLocalhostUrl(pkiBaseUrl)) {
/*  74 */       return "L";
/*     */     }
/*     */     
/*  77 */     return pkiEnv.equals("PROD") ? "P" : "I";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLocalhostUrl(String url) {
/*  86 */     if (url == null) {
/*  87 */       return false;
/*     */     }
/*     */     
/*     */     try {
/*  91 */       String host = (new URL(url)).getHost();
/*  92 */       InetAddress addr = InetAddress.getByName(host);
/*  93 */       if (addr.isAnyLocalAddress() || addr.isLoopbackAddress()) {
/*  94 */         return true;
/*     */       }
/*     */     }
/*  97 */     catch (UnknownHostException|java.net.MalformedURLException e) {
/*  98 */       return false;
/*     */     } 
/*     */     
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getPKIMappingFull(String pkiEnv, String pkiBaseUrl) {
/* 111 */     if ("-".equals(pkiEnv)) {
/* 112 */       return "-";
/*     */     }
/* 114 */     if (isLocalhostUrl(pkiBaseUrl)) {
/* 115 */       return "LOCAL";
/*     */     }
/* 117 */     return pkiEnv.equals("PROD") ? "PROD" : "INT";
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\BusinessDivisionMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */