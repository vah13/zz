/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.configuration.control.util;
/*     */ 
/*     */ import com.daimler.cebas.configuration.control.util.PkiPropertiesManager;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.context.annotation.Configuration;
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
/*     */ @Configuration
/*     */ public class ZenZefiPkiAndOidcPropertiesManager
/*     */   extends PkiPropertiesManager
/*     */ {
/*     */   @Value("${pki.environment}")
/*     */   private String pkiEnvironment;
/*     */   @Value("${security.oidc.max-time-skew}")
/*     */   private int oidcMaxTimeSkew;
/*     */   @Value("${test.pki.base.url}")
/*     */   private String testPkiBaseUrl;
/*     */   @Value("${test.supplier.pki.base.url}")
/*     */   private String testSupplierPkiBaseUrl;
/*     */   @Value("${test.pki.logout.url}")
/*     */   private String testPkiLogoutUrl;
/*     */   @Value("${spring.security.oauth2.client.provider.TEST.token-uri}")
/*     */   private String testOidcClientAccessToken;
/*     */   @Value("${spring.security.oauth2.client.provider.TEST.authorization-uri}")
/*     */   private String testOidcClientAuthorization;
/*     */   @Value("${spring.security.oauth2.client.provider.TEST.user-info-uri}")
/*     */   private String testOidcClientUserInfo;
/*     */   @Value("${test.spring.security.oauth2.client.registration.gas.redirect-uri}")
/*     */   private String testOidcRedirectUri;
/*     */   @Value("${prod.pki.base.url}")
/*     */   private String prodPkiBaseUrl;
/*     */   @Value("${prod.supplier.pki.base.url}")
/*     */   private String prodSupplierPkiBaseUrl;
/*     */   @Value("${prod.pki.logout.url}")
/*     */   private String prodPkiLogoutUrl;
/*     */   @Value("${prod.spring.security.oauth2.client.registration.gas.redirect-uri}")
/*     */   private String prodOidcRedirectUri;
/*     */   @Value("${spring.security.oauth2.client.provider.PROD.token-uri}")
/*     */   private String prodOidcClientAccessToken;
/*     */   @Value("${spring.security.oauth2.client.provider.PROD.authorization-uri}")
/*     */   private String prodOidcClientAuthorization;
/*     */   @Value("${spring.security.oauth2.client.provider.PROD.user-info-uri}")
/*     */   private String prodOidcClientUserInfo;
/*     */   @Value("${pki.logout.url}")
/*     */   private String pkiLogoutUrl;
/*     */   @Value("${security.revoke-token-uri}")
/*     */   private String oidcRevokeTokenUri;
/*     */   @Value("${test.security.revoke-token-uri}")
/*     */   private String testOidcRevokeTokenUri;
/*     */   @Value("${prod.security.revoke-token-uri}")
/*     */   private String prodOidcRevokeTokenUri;
/*     */   private String pkiCertificateVsmUrl;
/*     */   private String pkiIdentifiersNonVsmUrl;
/*     */   private String pkiCertificateNonVsmUrl;
/*     */   
/*     */   public void setPkiCertificateVsmUrl(String pkiCertificateVsmUrl) {
/* 150 */     this.pkiCertificateVsmUrl = pkiCertificateVsmUrl;
/*     */   }
/*     */   
/*     */   public String getPkiCertificateVsmUrl() {
/* 154 */     return this.pkiCertificateVsmUrl;
/*     */   }
/*     */   
/*     */   public void setPkiIdentifiersNonVsmUrl(String pkiIdentifiersNonVsmUrl) {
/* 158 */     this.pkiIdentifiersNonVsmUrl = pkiIdentifiersNonVsmUrl;
/*     */   }
/*     */   
/*     */   public String getPkiIdentifiersNonVsmUrl() {
/* 162 */     return this.pkiIdentifiersNonVsmUrl;
/*     */   }
/*     */   
/*     */   public void setPkiCertificateNonVsmUrl(String pkiCertificateNonVsmUrl) {
/* 166 */     this.pkiCertificateNonVsmUrl = pkiCertificateNonVsmUrl;
/*     */   }
/*     */   
/*     */   public String getPkiCertificateNonVsmUrl() {
/* 170 */     return this.pkiCertificateNonVsmUrl;
/*     */   }
/*     */   
/*     */   public String getPkiEnvironment() {
/* 174 */     return this.pkiEnvironment;
/*     */   }
/*     */   
/*     */   public void setPkiLogoutUrl(String pkiLogoutUrl) {
/* 178 */     this.pkiLogoutUrl = pkiLogoutUrl;
/*     */   }
/*     */   
/*     */   public String getTestPkiBaseUrl() {
/* 182 */     return this.testPkiBaseUrl;
/*     */   }
/*     */   
/*     */   public String getSupplierTestPkiBaseUrl() {
/* 186 */     return this.testSupplierPkiBaseUrl;
/*     */   }
/*     */   
/*     */   public String getTestOidcClientAccessToken() {
/* 190 */     return this.testOidcClientAccessToken;
/*     */   }
/*     */   
/*     */   public String getTestOidcClientAuthorization() {
/* 194 */     return this.testOidcClientAuthorization;
/*     */   }
/*     */   
/*     */   public String getTestOidcClientUserInfo() {
/* 198 */     return this.testOidcClientUserInfo;
/*     */   }
/*     */   
/*     */   public String getTestPkiLogoutUrl() {
/* 202 */     return this.testPkiLogoutUrl;
/*     */   }
/*     */   
/*     */   public String getProdPkiBaseUrl() {
/* 206 */     return this.prodPkiBaseUrl;
/*     */   }
/*     */   
/*     */   public String getSupplierProdPkiBaseUrl() {
/* 210 */     return this.prodSupplierPkiBaseUrl;
/*     */   }
/*     */   
/*     */   public String getProdPkiLogoutUrl() {
/* 214 */     return this.prodPkiLogoutUrl;
/*     */   }
/*     */   
/*     */   public String getProdOidcClientAccessToken() {
/* 218 */     return this.prodOidcClientAccessToken;
/*     */   }
/*     */   
/*     */   public String getProdOidcClientAuthorization() {
/* 222 */     return this.prodOidcClientAuthorization;
/*     */   }
/*     */   
/*     */   public String getProdOidcClientUserInfo() {
/* 226 */     return this.prodOidcClientUserInfo;
/*     */   }
/*     */   
/*     */   public String getTestOidcRedirectUri() {
/* 230 */     return this.testOidcRedirectUri;
/*     */   }
/*     */   
/*     */   public String getProdOidcRedirectUri() {
/* 234 */     return this.prodOidcRedirectUri;
/*     */   }
/*     */   
/*     */   public String getOidcRevokeTokenUri() {
/* 238 */     return this.oidcRevokeTokenUri;
/*     */   }
/*     */   
/*     */   public String getTestOidcRevokeTokenUri() {
/* 242 */     return this.testOidcRevokeTokenUri;
/*     */   }
/*     */   
/*     */   public String getProdOidcRevokeTokenUri() {
/* 246 */     return this.prodOidcRevokeTokenUri;
/*     */   }
/*     */   
/*     */   public void setOidcRevokeTokenUri(String oidcRevokeTokenUri) {
/* 250 */     this.oidcRevokeTokenUri = oidcRevokeTokenUri;
/*     */   }
/*     */   
/*     */   public String getPkiLogoutUrl() {
/* 254 */     return this.pkiLogoutUrl;
/*     */   }
/*     */   
/*     */   public int getOidcMaxTimeSkew() {
/* 258 */     return this.oidcMaxTimeSkew;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\configuration\contro\\util\ZenZefiPkiAndOidcPropertiesManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */