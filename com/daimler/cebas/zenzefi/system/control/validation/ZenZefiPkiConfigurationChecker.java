/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control.validation;
/*     */ 
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.configuration.control.CeBASGeneralPropertiesManager;
/*     */ import com.daimler.cebas.configuration.control.OidcConstants;
/*     */ import com.daimler.cebas.configuration.control.PkiUrlProperty;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.system.control.exceptions.ConfigurationCheckException;
/*     */ import java.util.logging.Level;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
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
/*     */ @CEBASControl
/*     */ public class ZenZefiPkiConfigurationChecker
/*     */ {
/*  30 */   private final String CLASS_NAME = com.daimler.cebas.zenzefi.system.control.validation.ZenZefiPkiConfigurationChecker.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CeBASGeneralPropertiesManager generalPropertiesManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MetadataManager i18n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public ZenZefiPkiConfigurationChecker(CeBASGeneralPropertiesManager generalPropertiesManager, Logger logger, MetadataManager i18n) {
/*  57 */     this.generalPropertiesManager = generalPropertiesManager;
/*  58 */     this.logger = logger;
/*  59 */     this.i18n = i18n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkOverridingOfPropertiesForPKIAndOAuth() {
/*  66 */     checkOverridingOfTestPkiAndOAuthProperties();
/*  67 */     checkOverridingOfProdPkiAndOAuthProperties();
/*  68 */     checkOverridingOfPkiAndOAuthProperties();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkOverridingOfPkiAndOAuthProperties() {
/*  75 */     checkOverridingProperty(PkiUrlProperty.PKI_LOGOUT_URL.getProperty(), "000331X");
/*  76 */     checkOverridingProperty(PkiUrlProperty.PKI_REVOKE_URL.getProperty(), "000332X");
/*  77 */     checkOverridingProperty(PkiUrlProperty.PKI_BACKEND_IDENTIFIERS_URL.getProperty(), "000333X");
/*  78 */     checkOverridingProperty(PkiUrlProperty.PKI_BACKEND_IDENTIFIERS_URL_V2.getProperty(), "000680X");
/*  79 */     checkOverridingProperty(PkiUrlProperty.PKI_CERTIFICATES_CHAIN.getProperty(), "000334X");
/*  80 */     checkOverridingProperty(PkiUrlProperty.PKI_USER_PERMISSIONS.getProperty(), "000335X");
/*  81 */     checkOverridingProperty(PkiUrlProperty.PKI_CERTIFICATES_URL.getProperty(), "000336X");
/*  82 */     checkOverridingProperty(PkiUrlProperty.PKI_TIME_CERTIFICATES_URL.getProperty(), "000337X");
/*  83 */     checkOverridingProperty(PkiUrlProperty.PKI_ENHANCED_RIGHTS_CERTIFICATES_URL.getProperty(), "000338X");
/*  84 */     checkOverridingProperty(PkiUrlProperty.PKI_LINK_CERTIFICATES_URL.getProperty(), "000625X");
/*  85 */     checkOverridingProperty(PkiUrlProperty.PKI_CERTIFICATES_VSM_URL.getProperty(), "000645X");
/*  86 */     checkOverridingProperty(PkiUrlProperty.PKI_IDENTIFIERS_NON_VSM_URL.getProperty(), "000644X");
/*  87 */     checkOverridingProperty(PkiUrlProperty.PKI_CERTIFICATES_NON_VSM_URL.getProperty(), "000645X");
/*  88 */     checkOverridingProperty(PkiUrlProperty.PKI_SECOCIS_CERTIFICATES_URL.getProperty(), "000339X");
/*  89 */     checkOverridingProperty(OidcConstants.CLIENT_ID, "000361X");
/*  90 */     checkOverridingProperty("security.revoke-token-uri", "000659X");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkOverridingOfTestPkiAndOAuthProperties() {
/*  97 */     checkOverridingProperty(PkiUrlProperty.TEST_PKI_BASE_URL.getProperty(), "000340X");
/*  98 */     checkOverridingProperty(PkiUrlProperty.TEST_PKI_LOGOUT_URL.getProperty(), "000341X");
/*  99 */     checkOverridingProperty(OidcConstants.TEST_CLIENT_ID, "000342X");
/* 100 */     checkOverridingProperty("spring.security.oauth2.client.provider.TEST.token-uri", "000344X");
/* 101 */     checkOverridingProperty("spring.security.oauth2.client.provider.TEST.authorization-uri", "000345X");
/* 102 */     checkOverridingProperty("spring.security.oauth2.client.provider.TEST.user-info-uri", "000346X");
/* 103 */     checkOverridingProperty("spring.security.oauth2.client.provider.TEST.jwk-set-uri", "000365X");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkOverridingOfProdPkiAndOAuthProperties() {
/* 110 */     checkOverridingProperty(PkiUrlProperty.PROD_PKI_BASE_URL.getProperty(), "000347X");
/* 111 */     checkOverridingProperty(PkiUrlProperty.PROD_PKI_LOGOUT_URL.getProperty(), "000348X");
/* 112 */     checkOverridingProperty(OidcConstants.PROD_CLIENT_ID, "000349X");
/* 113 */     checkOverridingProperty("spring.security.oauth2.client.provider.PROD.token-uri", "000351X");
/* 114 */     checkOverridingProperty("spring.security.oauth2.client.provider.PROD.authorization-uri", "000352X");
/* 115 */     checkOverridingProperty("spring.security.oauth2.client.provider.PROD.user-info-uri", "000353X");
/* 116 */     checkOverridingProperty("spring.security.oauth2.client.provider.PROD.jwk-set-uri", "000364X");
/* 117 */     checkOverridingProperty("prod.security.revoke-token-uri", "000661X");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkOverridingProperty(String propertyValue, String logId) {
/* 127 */     if (this.generalPropertiesManager.getProperty(propertyValue) != null) {
/* 128 */       String message = this.i18n.getEnglishMessage("propertyNotAllowedToBeOverridenInGeneralProperties", new String[] { propertyValue });
/*     */       
/* 130 */       this.logger.log(Level.SEVERE, logId, message, this.CLASS_NAME);
/* 131 */       throw new ConfigurationCheckException(message);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\validation\ZenZefiPkiConfigurationChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */