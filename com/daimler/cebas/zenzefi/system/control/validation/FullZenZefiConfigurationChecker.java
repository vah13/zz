/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control.validation;
/*     */ 
/*     */ import com.daimler.cebas.common.control.CEBASProperty;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*     */ import com.daimler.cebas.configuration.control.CeBASGeneralPropertiesManager;
/*     */ import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.system.control.exceptions.ConfigurationCheckException;
/*     */ import com.daimler.cebas.system.control.profile.ISystemConfiguration;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.proxy.ProxyManager;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.proxy.ProxyType;
/*     */ import com.daimler.cebas.zenzefi.system.control.validation.AbstractZenZefiConfigurationChecker;
/*     */ import com.daimler.cebas.zenzefi.system.control.validation.ZenZefiPkiConfigurationChecker;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASControl
/*     */ public class FullZenZefiConfigurationChecker
/*     */   extends AbstractZenZefiConfigurationChecker
/*     */ {
/*  31 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.system.control.validation.FullZenZefiConfigurationChecker.class
/*  32 */     .getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   private static final Logger LOG = Logger.getLogger(com.daimler.cebas.zenzefi.system.control.validation.FullZenZefiConfigurationChecker.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ProxyManager proxyManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${security.disable.ssl.validation}")
/*     */   private String disableSSLValidation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${security.enable.ssl.truststore.internal}")
/*     */   private String enableInternalSSLTruststore;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ZenZefiPkiConfigurationChecker zenZefiPkiConfigurationChecker;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String SPRING_HTTP_MULTIPART_MAX_REQUEST_SIZE = "spring.http.multipart.max-request-size";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String SPRING_HTTP_MULTIPART_MAX_FILE_SIZE = "spring.http.multipart.max-file-size";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FullZenZefiConfigurationChecker(AbstractConfigurator configurator, Logger logger, MetadataManager i18n, ISystemConfiguration systemConfiguration, CeBASGeneralPropertiesManager generalPropertiesManager, ProxyManager proxyManager, ZenZefiPkiConfigurationChecker zenZefiPkiConfigurationChecker) {
/*  97 */     super(configurator, logger, i18n, systemConfiguration, generalPropertiesManager);
/*     */     
/*  99 */     this.zenZefiPkiConfigurationChecker = zenZefiPkiConfigurationChecker;
/* 100 */     this.proxyManager = proxyManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkConfiguration() {
/* 108 */     this.zenZefiPkiConfigurationChecker
/* 109 */       .checkOverridingOfPropertiesForPKIAndOAuth();
/* 110 */     super.checkConfiguration();
/*     */     
/* 112 */     if (!checkNumberOfFailedLoginAttempts()) {
/* 113 */       this.logger.log(Level.SEVERE, "000217X", this.i18n
/* 114 */           .getEnglishMessage("invalidNumberOfFailedLoginAttempts"), CLASS_NAME);
/*     */ 
/*     */       
/* 117 */       throw new ConfigurationCheckException();
/*     */     } 
/* 119 */     checkProxyHostAndPort();
/* 120 */     checkProxyType();
/* 121 */     checkSSLValidation();
/* 122 */     checkSSLTruststore();
/* 123 */     checkExcludedUserRolesForCentralAuthentication();
/* 124 */     checkAutoUpdateCertificates();
/* 125 */     checkPaginationMaxRowsPerPage();
/* 126 */     checkThreshHolds();
/* 127 */     checkLogoffNoUserAction();
/* 128 */     checkDetailsViewState();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnableSSLValidation(String enableSSLValidation) {
/* 138 */     this.disableSSLValidation = enableSSLValidation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnableInternalSSLTruststore(String enableInternalSSLTruststore) {
/* 148 */     this.enableInternalSSLTruststore = enableInternalSSLTruststore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkOverridingOfGeneralProperties() {
/* 156 */     checkOverridingOfZenZefiFullProperties();
/* 157 */     super.checkOverridingOfGeneralProperties();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkSSLValidation() {
/* 164 */     checkBooleanConfiguration(this.disableSSLValidation, "000270", "invalidSSLValidationConfig");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkSSLTruststore() {
/* 173 */     checkBooleanConfiguration(this.enableInternalSSLTruststore, "000507", "invalidSSLTrustStoreConfig");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkAutoUpdateCertificates() {
/* 182 */     checkBooleanConfiguration(this.configurator
/*     */         
/* 184 */         .readProperty(CEBASProperty.AUTO_CERT_UPDATE.name()), "000319X", "invalidAutoCertUpdateConfig");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkPaginationMaxRowsPerPage() {
/* 193 */     int paginationMaxRowsPerPage = getIntConfiguration(this.configurator
/* 194 */         .readProperty(CEBASProperty.PAGINATION_MAX_ROWS_PER_PAGE
/* 195 */           .name()), "000323X", "invalidPaginationMaxRowsPerPage");
/*     */ 
/*     */     
/* 198 */     int minValue = getIntConfiguration(this.configurator.readProperty(CEBASProperty.CHECK_PAGINATION_MAX_ROWS_PER_PAGE_MIN
/* 199 */           .name()), "000324X", "invalidPaginationMaxRowsPerPageCheckMin");
/*     */ 
/*     */     
/* 202 */     int maxValue = getIntConfiguration(this.configurator.readProperty(CEBASProperty.CHECK_PAGINATION_MAX_ROWS_PER_PAGE_MAX
/* 203 */           .name()), "000325X", "invalidPaginationMaxRowsPerPageCheckMax");
/*     */ 
/*     */ 
/*     */     
/* 207 */     if (paginationMaxRowsPerPage < minValue || paginationMaxRowsPerPage > maxValue)
/*     */     {
/* 209 */       logAndSetErrorFlag("000326X", "invalidPaginationMaxRowsPerPage");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkNumberOfFailedLoginAttempts() {
/* 220 */     String numberOfFailedLoginAttemptsStr = this.configurator.readProperty(CEBASProperty.NUMBER_OF_FAILED_LOGIN_ATTEMPTS
/* 221 */         .name());
/*     */     
/* 223 */     String checkMinNumberOfFailedLoginAttemptsStr = this.configurator.readProperty(CEBASProperty.CHECK_NUMBER_OF_FAILED_LOGIN_ATTEMPTS_MIN
/*     */         
/* 225 */         .name());
/*     */     
/* 227 */     String checkMaxNumberOfFailedLoginAttemptsStr = this.configurator.readProperty(CEBASProperty.CHECK_NUMBER_OF_FAILED_LOGIN_ATTEMPTS_MAX
/*     */         
/* 229 */         .name());
/*     */ 
/*     */     
/*     */     try {
/* 233 */       int numberOfFailedLoginAttempts = Integer.parseInt(numberOfFailedLoginAttemptsStr);
/*     */       
/* 235 */       int checkMinNumberOfFailedLoginAttempts = Integer.parseInt(checkMinNumberOfFailedLoginAttemptsStr);
/*     */       
/* 237 */       int checkMaxNumberOfFailedLoginAttempts = Integer.parseInt(checkMaxNumberOfFailedLoginAttemptsStr);
/*     */       
/* 239 */       if (checkMinNumberOfFailedLoginAttempts > numberOfFailedLoginAttempts || checkMaxNumberOfFailedLoginAttempts < numberOfFailedLoginAttempts)
/*     */       {
/* 241 */         return false;
/*     */       }
/* 243 */     } catch (NumberFormatException e) {
/* 244 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 245 */       return false;
/*     */     } 
/* 247 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkProxyType() {
/* 255 */     String proxyType = this.configurator.readProperty(CEBASProperty.HTTPS_PROXY_TYPE.name());
/* 256 */     if (proxyType == null || !ProxyManager.isProxyTypeValid(proxyType)) {
/* 257 */       this.logger.log(Level.SEVERE, "000287X", this.i18n
/* 258 */           .getEnglishMessage("invalidProxyType", new String[] {
/*     */               
/* 260 */               String.join(",", new CharSequence[] { ProxyType.DIRECT.name(), ProxyType.MANUAL
/* 261 */                   .name(), ProxyType.NATIVE
/* 262 */                   .name() })
/*     */             }), CLASS_NAME);
/* 264 */       throw new ConfigurationCheckException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkProxyHostAndPort() {
/* 273 */     String host = this.configurator.readProperty(CEBASProperty.HTTPS_PROXY_HOST.name());
/*     */     
/* 275 */     String port = this.configurator.readProperty(CEBASProperty.HTTPS_PROXY_PORT.name());
/* 276 */     if (host == null) {
/* 277 */       this.logger.log(Level.SEVERE, "000293X", this.i18n
/* 278 */           .getEnglishMessage("missingProxyHostProperty"), CLASS_NAME);
/*     */       
/* 280 */       throw new ConfigurationCheckException();
/*     */     } 
/* 282 */     if (port == null) {
/* 283 */       this.logger.log(Level.SEVERE, "000294X", this.i18n
/* 284 */           .getEnglishMessage("missingProxyPortProperty"), CLASS_NAME);
/*     */       
/* 286 */       throw new ConfigurationCheckException();
/*     */     } 
/*     */     try {
/* 289 */       this.proxyManager.validateProxyHostAndPort(host, port);
/* 290 */     } catch (ZenZefiConfigurationException e) {
/* 291 */       LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/* 292 */       throw new ConfigurationCheckException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkThreshHolds() {
/* 300 */     String greenThreshhold = this.configurator.readProperty(CEBASProperty.CERT_LIFETIME_GREEN_THRESHHOLD
/* 301 */         .name());
/* 302 */     String greenThreshholdMin = this.configurator.readProperty(CEBASProperty.CHECK_CERT_LIFETIME_GREEN_THRESHHOLD_MIN
/* 303 */         .name());
/* 304 */     String greenThreshholdMax = this.configurator.readProperty(CEBASProperty.CHECK_CERT_LIFETIME_GREEN_THRESHHOLD_MAX
/* 305 */         .name());
/* 306 */     String yellowThreshHold = this.configurator.readProperty(CEBASProperty.CERT_LIFETIME_YELLOW_THRESHHOLD
/* 307 */         .name());
/* 308 */     String yellowThreshHoldMin = this.configurator.readProperty(CEBASProperty.CHECK_CERT_LIFETIME_YELLOW_THRESHHOLD_MIN
/* 309 */         .name());
/* 310 */     String yellowThreshHoldMax = this.configurator.readProperty(CEBASProperty.CHECK_CERT_LIFETIME_YELLOW_THRESHHOLD_MAX
/* 311 */         .name());
/*     */     try {
/* 313 */       int green = Integer.parseInt(greenThreshhold);
/* 314 */       int greenMin = Integer.parseInt(greenThreshholdMin);
/* 315 */       int greenMax = Integer.parseInt(greenThreshholdMax);
/*     */       
/* 317 */       int yellow = Integer.parseInt(yellowThreshHold);
/* 318 */       int yellowMin = Integer.parseInt(yellowThreshHoldMin);
/* 319 */       int yellowMax = Integer.parseInt(yellowThreshHoldMax);
/*     */       
/* 321 */       boolean isYellowOk = (yellowMin > 0 && yellow >= yellowMin && yellow <= yellowMax);
/*     */ 
/*     */       
/* 324 */       boolean isGreenOk = (greenMin >= yellowMax && green >= greenMin && green <= greenMax);
/*     */ 
/*     */       
/* 327 */       boolean isValid = (isYellowOk && isGreenOk);
/*     */       
/* 329 */       if (!isValid) {
/* 330 */         logAndSetErrorFlag("000136X", "invalidThresholdsConfig");
/*     */       
/*     */       }
/*     */     }
/* 334 */     catch (NumberFormatException e) {
/* 335 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 336 */       logAndSetErrorFlag("000136X", "invalidThresholdsConfig");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkLogoffNoUserAction() {
/* 346 */     String logOffNoUserAction = this.configurator.readProperty(CEBASProperty.LOGOFF_NO_USER_ACTION.name());
/*     */     try {
/* 348 */       int logoffValue = Integer.parseInt(logOffNoUserAction);
/* 349 */       boolean isValid = (logoffValue >= 0);
/* 350 */       if (!isValid) {
/* 351 */         logAndSetErrorFlag("000134X", "invalidLogOffNoActionConfig");
/*     */       }
/*     */     }
/* 354 */     catch (NumberFormatException e) {
/* 355 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 356 */       logAndSetErrorFlag("000134X", "invalidLogOffNoActionConfig");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkDetailsViewState() {
/* 365 */     String certTableDetailsViewState = this.configurator.readProperty(CEBASProperty.CERT_TABLE_DETAIL_VIEW_STATE
/* 366 */         .name());
/* 367 */     String OPEN = "open";
/* 368 */     String CLOSE = "close";
/*     */ 
/*     */     
/* 371 */     boolean isValid = ("open".equals(certTableDetailsViewState) || "close".equals(certTableDetailsViewState));
/* 372 */     if (!isValid) {
/* 373 */       logAndSetErrorFlag("000133X", "invalidDetailsPaneStateConfig");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkOverridingOfZenZefiFullProperties() {
/* 382 */     checkOverridingProperty("spring.http.multipart.max-request-size", "000329X");
/*     */     
/* 384 */     checkOverridingProperty("spring.http.multipart.max-file-size", "000330X");
/*     */     
/* 386 */     checkOverridingProperty(CEBASProperty.CHECK_NUMBER_OF_FAILED_LOGIN_ATTEMPTS_MIN
/* 387 */         .name(), "000274X");
/*     */     
/* 389 */     checkOverridingProperty(CEBASProperty.CHECK_NUMBER_OF_FAILED_LOGIN_ATTEMPTS_MAX
/* 390 */         .name(), "000401X");
/*     */     
/* 392 */     checkOverridingProperty(CEBASProperty.CHECK_PAGINATION_MAX_ROWS_PER_PAGE_MIN
/* 393 */         .name(), "000410X");
/*     */     
/* 395 */     checkOverridingProperty(CEBASProperty.CHECK_PAGINATION_MAX_ROWS_PER_PAGE_MAX
/* 396 */         .name(), "000411X");
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\validation\FullZenZefiConfigurationChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */