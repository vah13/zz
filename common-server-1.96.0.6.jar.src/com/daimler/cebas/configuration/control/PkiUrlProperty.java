/*     */ package com.daimler.cebas.configuration.control;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum PkiUrlProperty
/*     */ {
/*   8 */   PKI_ENVIRONMENT("pki.environment", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  13 */   PKI_BASE_URL("pki.base.url", true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  18 */   SUPPLIER_PKI_BASE_URL("supplier.pki.base.url", true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  23 */   PKI_REVOKE_URL("pki.revoke.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  28 */   PKI_LOGOUT_URL("pki.logout.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  33 */   PKI_BACKEND_IDENTIFIERS_URL("pki.backends.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   PKI_BACKEND_IDENTIFIERS_URL_V2("pki.backends.url.v2", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   PKI_CERTIFICATES_CHAIN("pki.chain.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   PKI_USER_PERMISSIONS("pki.permissions.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   PKI_CERTIFICATES_URL("pki.certificate.end-user.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   PKI_TIME_CERTIFICATES_URL("pki.time.certificate.end-user.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   PKI_SECOCIS_CERTIFICATES_URL("pki.secocis.certificate.end-user.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   PKI_ENHANCED_RIGHTS_CERTIFICATES_URL("pki.enhanced.certificate.end-user.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   PKI_LINK_CERTIFICATES_URL("pki.link.certificate.end-user.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   PKI_CERTIFICATES_VSM_URL("pki.certificate.vsm.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   PKI_IDENTIFIERS_NON_VSM_URL("pki.identifiers.non.vsm.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   PKI_CERTIFICATES_NON_VSM_URL("pki.certificate.non.vsm.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   TEST_PKI_BASE_URL("test.pki.base.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   TEST_SUPPLIER_PKI_BASE_URL("test.supplier.pki.base.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   TEST_PKI_LOGOUT_URL("test.pki.logout.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   PROD_PKI_BASE_URL("prod.pki.base.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   PROD_SUPPLIER_PKI_BASE_URL("prod.supplier.pki.base.url", false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   PROD_PKI_LOGOUT_URL("prod.pki.logout.url", false);
/*     */ 
/*     */ 
/*     */   
/*     */   private String property;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean runtimeOnly;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PkiUrlProperty(String property, boolean runtimeOnly) {
/* 132 */     this.property = property;
/* 133 */     this.runtimeOnly = runtimeOnly;
/*     */   }
/*     */   
/*     */   public String getProperty() {
/* 137 */     return this.property;
/*     */   }
/*     */   
/*     */   public boolean isRuntimeOnly() {
/* 141 */     return this.runtimeOnly;
/*     */   }
/*     */   
/*     */   public static PkiUrlProperty getFromProperty(String propertyName) {
/* 145 */     PkiUrlProperty[] v = values();
/* 146 */     for (PkiUrlProperty p : v) {
/* 147 */       if (p.getProperty().equals(propertyName)) {
/* 148 */         return p;
/*     */       }
/*     */     } 
/* 151 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\PkiUrlProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */