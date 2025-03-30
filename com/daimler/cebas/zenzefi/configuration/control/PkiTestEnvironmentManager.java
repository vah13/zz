/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.configuration.control;
/*    */ 
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.configuration.control.PkiConstants;
/*    */ import com.daimler.cebas.configuration.control.PkiUrlProperty;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import com.daimler.cebas.zenzefi.configuration.control.PkiEnvironmentAbstract;
/*    */ import com.daimler.cebas.zenzefi.configuration.control.util.ZenZefiPkiAndOidcPropertiesManager;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.core.env.ConfigurableEnvironment;
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
/*    */ @CEBASControl
/*    */ @Configuration
/*    */ public class PkiTestEnvironmentManager
/*    */   extends PkiEnvironmentAbstract
/*    */ {
/*    */   @Autowired
/*    */   public PkiTestEnvironmentManager(ConfigurableEnvironment environment, ZenZefiPkiAndOidcPropertiesManager pkiAndOAuthPropertiesManager, Session session) {
/* 35 */     super(environment, pkiAndOAuthPropertiesManager, session);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleEnvironment() {
/* 43 */     setTestEnvironmentProperties();
/* 44 */     recomposeUrlsBasedOnPkiEnvironmentTest();
/* 45 */     handleDefaultUrlsAfterSetOfPkiEnvironment();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void recomposeUrlsBasedOnPkiEnvironmentTest() {
/* 52 */     String baseUrl = isSupplierContextActive() ? this.pkiAndOAuthPropertiesManager.getSupplierTestPkiBaseUrl() : this.pkiAndOAuthPropertiesManager.getTestPkiBaseUrl();
/*    */     
/* 54 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_REVOKE_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 55 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_BACKEND_IDENTIFIERS_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 56 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_BACKEND_IDENTIFIERS_URL_V2.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 57 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_CERTIFICATES_CHAIN.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 58 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_USER_PERMISSIONS.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 59 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_CERTIFICATES_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 60 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_TIME_CERTIFICATES_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 61 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_SECOCIS_CERTIFICATES_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 62 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_ENHANCED_RIGHTS_CERTIFICATES_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 63 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_LINK_CERTIFICATES_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 64 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_CERTIFICATES_VSM_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 65 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_IDENTIFIERS_NON_VSM_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 66 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_CERTIFICATES_NON_VSM_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void setTestEnvironmentProperties() {
/* 74 */     this.environment.getSystemProperties().put(PkiUrlProperty.PKI_ENVIRONMENT.getProperty(), "TEST");
/*    */     
/* 76 */     String baseUrl = isSupplierContextActive() ? this.pkiAndOAuthPropertiesManager.getSupplierTestPkiBaseUrl() : this.pkiAndOAuthPropertiesManager.getTestPkiBaseUrl();
/* 77 */     this.environment.getSystemProperties().put(PkiUrlProperty.PKI_BASE_URL.getProperty(), baseUrl);
/* 78 */     this.environment.getSystemProperties().put(PkiUrlProperty.PKI_LOGOUT_URL.getProperty(), this.pkiAndOAuthPropertiesManager.getTestPkiLogoutUrl());
/* 79 */     this.environment.getSystemProperties().put("spring.security.oauth2.client.registration.gas.redirect-uri", this.pkiAndOAuthPropertiesManager.getTestOidcRedirectUri());
/* 80 */     this.environment.getSystemProperties().put("security.revoke-token-uri", this.pkiAndOAuthPropertiesManager.getTestOidcRevokeTokenUri());
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\configuration\control\PkiTestEnvironmentManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */