/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.configuration.control;
/*     */ 
/*     */ import com.daimler.cebas.common.control.CEBASProperty;
/*     */ import com.daimler.cebas.configuration.control.PkiUrlProperty;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.util.ZenZefiPkiAndOidcPropertiesManager;
/*     */ import com.daimler.cebas.zenzefi.users.entity.UserOrganisation;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PkiEnvironmentAbstract
/*     */ {
/*     */   protected static final String ENVIRONMENT_PKI_BACKUP_PREFIX = "backup.zz.";
/*     */   protected ConfigurableEnvironment environment;
/*     */   protected ZenZefiPkiAndOidcPropertiesManager pkiAndOAuthPropertiesManager;
/*     */   protected Session session;
/*     */   
/*     */   public PkiEnvironmentAbstract(ConfigurableEnvironment environment, ZenZefiPkiAndOidcPropertiesManager pkiAndOAuthPropertiesManager, Session session) {
/*  44 */     this.environment = environment;
/*  45 */     this.pkiAndOAuthPropertiesManager = pkiAndOAuthPropertiesManager;
/*  46 */     this.session = session;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void handleEnvironment();
/*     */ 
/*     */   
/*     */   public void storeOriginalPkiValues() {
/*  55 */     PkiUrlProperty[] values = PkiUrlProperty.values();
/*  56 */     for (PkiUrlProperty p : values) {
/*  57 */       if (!p.isRuntimeOnly()) {
/*  58 */         String value = this.environment.getRequiredProperty(p.getProperty());
/*  59 */         this.environment.getSystemProperties().put("backup.zz." + p.getProperty(), value);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleDefaultUrlsAfterSetOfPkiEnvironment() {
/*  69 */     this.pkiAndOAuthPropertiesManager.setPkiRevokeUrl(
/*  70 */         String.valueOf(this.environment.getRequiredProperty(PkiUrlProperty.PKI_REVOKE_URL.getProperty())));
/*  71 */     this.pkiAndOAuthPropertiesManager.setPkiLogoutUrl(
/*  72 */         String.valueOf(this.environment.getRequiredProperty(PkiUrlProperty.PKI_LOGOUT_URL.getProperty())));
/*  73 */     this.pkiAndOAuthPropertiesManager.setPkiBackendIdentifiersUrl(this.environment
/*  74 */         .getRequiredProperty(PkiUrlProperty.PKI_BACKEND_IDENTIFIERS_URL.getProperty()));
/*  75 */     this.pkiAndOAuthPropertiesManager.setPkiBackendIdentifiersUrlV2(this.environment
/*  76 */         .getRequiredProperty(PkiUrlProperty.PKI_BACKEND_IDENTIFIERS_URL_V2.getProperty()));
/*  77 */     this.pkiAndOAuthPropertiesManager.setPkiCertificatesChainUrl(
/*  78 */         String.valueOf(this.environment.getRequiredProperty(PkiUrlProperty.PKI_CERTIFICATES_CHAIN.getProperty())));
/*  79 */     this.pkiAndOAuthPropertiesManager.setPkiPermissionsUrl(
/*  80 */         String.valueOf(this.environment.getRequiredProperty(PkiUrlProperty.PKI_USER_PERMISSIONS.getProperty())));
/*  81 */     this.pkiAndOAuthPropertiesManager.setPkiCertificatesEndUserUrl(
/*  82 */         String.valueOf(this.environment.getRequiredProperty(PkiUrlProperty.PKI_CERTIFICATES_URL.getProperty())));
/*  83 */     this.pkiAndOAuthPropertiesManager.setPkiTimeCertificatesUrl(
/*  84 */         String.valueOf(this.environment.getRequiredProperty(PkiUrlProperty.PKI_TIME_CERTIFICATES_URL.getProperty())));
/*  85 */     this.pkiAndOAuthPropertiesManager.setPkiEnhancedRightsCertificatesUrl(String.valueOf(this.environment
/*  86 */           .getRequiredProperty(PkiUrlProperty.PKI_ENHANCED_RIGHTS_CERTIFICATES_URL.getProperty())));
/*  87 */     this.pkiAndOAuthPropertiesManager.setPkiLinkCertificateUrl(
/*  88 */         String.valueOf(this.environment.getRequiredProperty(PkiUrlProperty.PKI_LINK_CERTIFICATES_URL.getProperty())));
/*  89 */     this.pkiAndOAuthPropertiesManager.setPkiCertificateVsmUrl(
/*  90 */         String.valueOf(this.environment.getRequiredProperty(PkiUrlProperty.PKI_CERTIFICATES_VSM_URL.getProperty())));
/*  91 */     this.pkiAndOAuthPropertiesManager.setPkiIdentifiersNonVsmUrl(
/*  92 */         String.valueOf(this.environment.getRequiredProperty(PkiUrlProperty.PKI_IDENTIFIERS_NON_VSM_URL.getProperty())));
/*  93 */     this.pkiAndOAuthPropertiesManager.setPkiCertificateNonVsmUrl(
/*  94 */         String.valueOf(this.environment.getRequiredProperty(PkiUrlProperty.PKI_CERTIFICATES_NON_VSM_URL.getProperty())));
/*  95 */     this.pkiAndOAuthPropertiesManager.setPkiSecOCISCertificatesUrl(
/*  96 */         String.valueOf(this.environment.getRequiredProperty(PkiUrlProperty.PKI_SECOCIS_CERTIFICATES_URL.getProperty())));
/*     */     
/*  98 */     this.pkiAndOAuthPropertiesManager
/*  99 */       .setOidcRevokeTokenUri(this.environment.getRequiredProperty("security.revoke-token-uri"));
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
/*     */   protected void composeUrlsForPkiEnvironment(String url, String regex, String baseUrl) {
/* 111 */     if (PkiUrlProperty.getFromProperty(url).isRuntimeOnly()) {
/* 112 */       this.environment.getSystemProperties().put(url, this.environment.getRequiredProperty(url).replace(regex, baseUrl));
/*     */     } else {
/* 114 */       this.environment.getSystemProperties().put(url, this.environment
/* 115 */           .getRequiredProperty("backup.zz." + url).replace(regex, baseUrl));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSupplierContextActive() {
/* 122 */     if (Boolean.getBoolean(CEBASProperty.PKISCOPE_SWITCH.name())) {
/* 123 */       return true;
/*     */     }
/*     */     
/* 126 */     if (this.session.getBackendAuthenticatedUser() == null) {
/* 127 */       return false;
/*     */     }
/* 129 */     return UserOrganisation.SUPPLIER.name().equals(this.session.getBackendAuthenticatedUser().getOrganisation());
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\configuration\control\PkiEnvironmentAbstract.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */