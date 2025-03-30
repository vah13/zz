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
/*    */ public class PkiProdEnvironmentManager
/*    */   extends PkiEnvironmentAbstract
/*    */ {
/*    */   @Autowired
/*    */   public PkiProdEnvironmentManager(ConfigurableEnvironment environment, ZenZefiPkiAndOidcPropertiesManager pkiAndOAuthPropertiesManager, Session session) {
/* 35 */     super(environment, pkiAndOAuthPropertiesManager, session);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleEnvironment() {
/* 43 */     setProdEnvironmentProperties();
/* 44 */     recomposeUrlsBasedOnPkiEnvironmentProd();
/* 45 */     handleDefaultUrlsAfterSetOfPkiEnvironment();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void recomposeUrlsBasedOnPkiEnvironmentProd() {
/* 53 */     String baseUrl = isSupplierContextActive() ? this.pkiAndOAuthPropertiesManager.getSupplierProdPkiBaseUrl() : this.pkiAndOAuthPropertiesManager.getProdPkiBaseUrl();
/*    */     
/* 55 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_REVOKE_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 56 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_BACKEND_IDENTIFIERS_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 57 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_BACKEND_IDENTIFIERS_URL_V2.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 58 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_CERTIFICATES_CHAIN.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 59 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_USER_PERMISSIONS.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 60 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_CERTIFICATES_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 61 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_TIME_CERTIFICATES_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 62 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_SECOCIS_CERTIFICATES_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 63 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_ENHANCED_RIGHTS_CERTIFICATES_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 64 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_LINK_CERTIFICATES_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 65 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_CERTIFICATES_VSM_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 66 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_IDENTIFIERS_NON_VSM_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/* 67 */     composeUrlsForPkiEnvironment(PkiUrlProperty.PKI_CERTIFICATES_NON_VSM_URL.getProperty(), PkiConstants.PKI_BASE_URL_REGEX, baseUrl);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void setProdEnvironmentProperties() {
/* 76 */     this.environment.getSystemProperties().put(PkiUrlProperty.PKI_ENVIRONMENT.getProperty(), "PROD");
/* 77 */     this.environment.getSystemProperties().put(PkiUrlProperty.PKI_BASE_URL.getProperty(), this.pkiAndOAuthPropertiesManager.getProdPkiBaseUrl());
/* 78 */     this.environment.getSystemProperties().put(PkiUrlProperty.SUPPLIER_PKI_BASE_URL.getProperty(), this.pkiAndOAuthPropertiesManager.getSupplierProdPkiBaseUrl());
/* 79 */     this.environment.getSystemProperties().put(PkiUrlProperty.PKI_LOGOUT_URL.getProperty(), this.pkiAndOAuthPropertiesManager.getProdPkiLogoutUrl());
/* 80 */     this.environment.getSystemProperties().put("spring.security.oauth2.client.registration.gas.redirect-uri", this.pkiAndOAuthPropertiesManager.getProdOidcRedirectUri());
/* 81 */     this.environment.getSystemProperties().put("security.revoke-token-uri", this.pkiAndOAuthPropertiesManager.getProdOidcRevokeTokenUri());
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\configuration\control\PkiProdEnvironmentManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */