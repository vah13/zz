/*     */ package com.daimler.cebas.system.control.validation;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.validation.CertificatesValidator;
/*     */ import com.daimler.cebas.certificates.entity.UserRole;
/*     */ import com.daimler.cebas.common.control.CEBASProperty;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*     */ import com.daimler.cebas.configuration.control.CeBASGeneralPropertiesManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.system.control.exceptions.ConfigurationCheckException;
/*     */ import com.daimler.cebas.system.control.profile.ISystemConfiguration;
/*     */ import com.daimler.cebas.system.control.startup.CeBASStartupProperty;
/*     */ import com.daimler.cebas.system.control.validation.vo.BusinessEnvironment;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.crypto.Cipher;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.scheduling.support.CronSequenceGenerator;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public abstract class AbstractConfigurationChecker
/*     */ {
/*  36 */   private static final Logger LOG = Logger.getLogger(CertificatesValidator.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOCALHOST = "localhost";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   private static final String CLASS_NAME = AbstractConfigurationChecker.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${server.logback.filename}.log")
/*     */   private String logFileName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${server.logback.filename}-%d{yyyy-MM-dd}.%i.log.zip")
/*     */   private String rotatedLogsFilePattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractConfigurator configurator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MetadataManager i18n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CeBASGeneralPropertiesManager generalPropertiesManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ISystemConfiguration systemConfiguration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractConfigurationChecker(AbstractConfigurator configurator, Logger logger, MetadataManager i18n, ISystemConfiguration systemConfiguration, CeBASGeneralPropertiesManager generalPropertiesManager) {
/* 102 */     this.configurator = configurator;
/* 103 */     this.logger = logger;
/* 104 */     this.i18n = i18n;
/* 105 */     this.systemConfiguration = systemConfiguration;
/* 106 */     this.generalPropertiesManager = generalPropertiesManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogFileName(String logFileName) {
/* 116 */     this.logFileName = logFileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRotatedLogsFilePattern(String rotatedLogsFilePattern) {
/* 126 */     this.rotatedLogsFilePattern = rotatedLogsFilePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkConfiguration() {
/* 133 */     String METHOD_NAME = "checkConfiguration";
/* 134 */     this.logger.entering(CLASS_NAME, "checkConfiguration");
/* 135 */     checkUnlimitedCryptographyStrengthExtension();
/* 136 */     checkBusinessEnvironment();
/* 137 */     checkValidateCerts();
/* 138 */     checkDeleteExpiredCerts();
/* 139 */     checkDeleteCertsSchedule();
/* 140 */     checkUserRole();
/* 141 */     checkSecret();
/* 142 */     checkLoggingLevel();
/* 143 */     checkLogbackFile();
/* 144 */     checkPersistenceAuditEntries();
/* 145 */     checkLogbackArchivesPattern();
/* 146 */     checkBackupTimeConfiguration();
/* 147 */     checkValidityCheckTimeToleranceConfiguration();
/* 148 */     if (this.systemConfiguration.onlyLocalhostAddress()) {
/* 149 */       checkLocalhostAddress();
/*     */     }
/* 151 */     this.logger.exiting(CLASS_NAME, "checkConfiguration");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkOverridingOfGeneralProperties() {
/* 158 */     checkOverridingProperty(CEBASProperty.MAX_AGE_LOGENTRY.name(), "000388X");
/* 159 */     checkOverridingProperty(CEBASProperty.CHECK_MAX_AGE_LOGENTRY_MIN.name(), "000389X");
/* 160 */     checkOverridingProperty(CEBASProperty.CHECK_MAX_AGE_LOGENTRY_MAX.name(), "000390X");
/* 161 */     checkOverridingProperty(CEBASProperty.USER_ROLE.name(), "000391X");
/* 162 */     checkOverridingProperty(CEBASProperty.DELETE_CERTS_SCHEDULE.name(), "000392X");
/* 163 */     checkOverridingProperty(CEBASProperty.DELETE_EXPIRED_CERTS.name(), "000393X");
/* 164 */     checkOverridingProperty(CEBASProperty.VALIDATE_CERTS.name(), "000394X");
/* 165 */     checkOverridingProperty(CEBASProperty.MAX_SIZE_LOGENTRY.name(), "000678X");
/* 166 */     checkOverridingProperty(CEBASProperty.VALIDITY_CHECK_TIME_TOLERANCE.name(), "000684X");
/* 167 */     checkOverridingProperty(CEBASProperty.PKISCOPE_SWITCH.name(), "000690X");
/*     */     
/* 169 */     for (CeBASStartupProperty p : CeBASStartupProperty.values()) {
/* 170 */       checkOverridingProperty(p.getProperty(), "000408X");
/* 171 */       checkOverridingProperty(p.name(), "000408X");
/*     */     } 
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
/*     */   protected void checkOverridingProperty(String propertyKey, String logId) {
/* 184 */     if (this.generalPropertiesManager.getProperty(propertyKey) != null) {
/* 185 */       String message = this.i18n.getEnglishMessage("propertyNotAllowedToBeOverridenInGeneralProperties", new String[] { propertyKey });
/*     */       
/* 187 */       this.logger.log(Level.SEVERE, logId, message, CLASS_NAME);
/* 188 */       throw new ConfigurationCheckException(message);
/*     */     } 
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
/*     */   protected void logAndSetErrorFlag(String logId, String messageId) {
/* 201 */     this.logger.log(Level.SEVERE, logId, this.i18n.getEnglishMessage(messageId), CLASS_NAME);
/* 202 */     throw new ConfigurationCheckException();
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
/*     */   
/*     */   protected void checkBooleanConfiguration(String configuration, String logId, String logMessage) {
/* 216 */     if (configuration == null) {
/* 217 */       logAndSetErrorFlag(logId, logMessage);
/*     */       
/*     */       return;
/*     */     } 
/* 221 */     boolean isValid = ("true".equals(configuration) || "false".equals(configuration));
/* 222 */     if (!isValid) {
/* 223 */       logAndSetErrorFlag(logId, logMessage);
/*     */     }
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
/*     */   
/*     */   protected void checkNumericConfiguration(String configuration, String logId, String logMessage) {
/* 238 */     if (configuration == null) {
/* 239 */       logAndSetErrorFlag(logId, logMessage);
/*     */       
/*     */       return;
/*     */     } 
/* 243 */     if (!StringUtils.isNumeric(configuration)) {
/* 244 */       logAndSetErrorFlag(logId, logMessage);
/*     */     }
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
/*     */   
/*     */   protected int getIntConfiguration(String configuration, String logId, String logMessage) {
/* 259 */     if (configuration == null) {
/* 260 */       logAndSetErrorFlag(logId, logMessage);
/* 261 */       return 0;
/*     */     } 
/*     */     try {
/* 264 */       return Integer.valueOf(configuration).intValue();
/* 265 */     } catch (NumberFormatException e) {
/* 266 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 267 */       logAndSetErrorFlag(logId, logMessage);
/* 268 */       return 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkBackupTimeConfiguration() {
/* 273 */     String backupTime = this.configurator.readProperty("system_data_backup_time");
/* 274 */     if (!StringUtils.isNumeric(backupTime)) {
/* 275 */       throw new ConfigurationCheckException("Wrong configuration for backup time. It must be in hours and non zero");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkValidityCheckTimeToleranceConfiguration() {
/* 283 */     String timeTolerance = this.configurator.readProperty(CEBASProperty.VALIDITY_CHECK_TIME_TOLERANCE.name());
/* 284 */     checkNumericConfiguration(timeTolerance, "000685X", "invalidValidityCheckTimeToleranceConfig");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkUnlimitedCryptographyStrengthExtension() {
/*     */     try {
/* 292 */       int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
/* 293 */       if (maxKeyLen < Integer.MAX_VALUE) {
/* 294 */         logAndSetErrorFlag("000008X", "limitedCryptographyStrength");
/*     */       }
/* 296 */     } catch (NoSuchAlgorithmException e) {
/* 297 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 298 */       throw new ConfigurationCheckException(e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkBusinessEnvironment() {
/* 306 */     String businessEnvironment = this.configurator.readProperty(CEBASProperty.BUSINESS_ENVIRONMENT.name());
/* 307 */     boolean isValid = false;
/* 308 */     for (BusinessEnvironment businessEnum : BusinessEnvironment.values()) {
/* 309 */       if (businessEnum.name().equals(businessEnvironment)) {
/* 310 */         isValid = true;
/* 311 */         this.logger.log(Level.INFO, "000321", this.i18n
/* 312 */             .getEnglishMessage("businessEnvConfig", new String[] { businessEnvironment
/* 313 */               }), getClass().getSimpleName());
/*     */         break;
/*     */       } 
/*     */     } 
/* 317 */     if (!isValid) {
/* 318 */       logAndSetErrorFlag("000125X", "invalidBusinessEnvConfig");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkValidateCerts() {
/* 326 */     String validateCerts = this.configurator.readProperty(CEBASProperty.VALIDATE_CERTS.name());
/* 327 */     checkBooleanConfiguration(validateCerts, "000126X", "invalidValidateCertsConfig");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkDeleteExpiredCerts() {
/* 335 */     String deleteExpiredCerts = this.configurator.readProperty(CEBASProperty.DELETE_EXPIRED_CERTS.name());
/* 336 */     checkBooleanConfiguration(deleteExpiredCerts, "000127X", "invalidDeleteExpiredCertsConfig");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkDeleteCertsSchedule() {
/* 354 */     String deleteCertsSchedule = this.configurator.readProperty(CEBASProperty.DELETE_CERTS_SCHEDULE.name());
/* 355 */     if (!isDeletCertsScheduleCronExpressionValid(deleteCertsSchedule)) {
/* 356 */       logAndSetErrorFlag("000128X", "invalidDeleteCertsScheduleConfig");
/*     */       
/*     */       return;
/*     */     } 
/* 360 */     String deleteCertsScheduleGeneralProperty = this.generalPropertiesManager.getProperty(CEBASProperty.DELETE_CERTS_SCHEDULE.name());
/* 361 */     if (!StringUtils.isEmpty(deleteCertsScheduleGeneralProperty) && 
/* 362 */       !isDeletCertsScheduleCronExpressionValid(deleteCertsScheduleGeneralProperty)) {
/* 363 */       logAndSetErrorFlag("000128X", "invalidDeleteCertsScheduleConfig");
/*     */     }
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
/*     */   private boolean isDeletCertsScheduleCronExpressionValid(String value) {
/* 377 */     if (value == null || StringUtils.isEmpty(value)) {
/* 378 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 382 */     if (!CronSequenceGenerator.isValidExpression(value)) {
/* 383 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 388 */       CronSequenceGenerator csg = new CronSequenceGenerator(value);
/* 389 */       Date nextRun = csg.next(new Date());
/* 390 */       Date secondRun = csg.next(nextRun);
/*     */ 
/*     */       
/* 393 */       long diff = secondRun.getTime() - nextRun.getTime();
/*     */       
/* 395 */       long convert = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
/*     */ 
/*     */       
/* 398 */       if (convert < 10L) {
/* 399 */         return false;
/*     */       }
/* 401 */     } catch (IllegalArgumentException e) {
/* 402 */       LOG.log(Level.FINEST, e.getMessage(), e);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 409 */       return false;
/*     */     } 
/* 411 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkUserRole() {
/* 419 */     String userRole = this.configurator.readProperty(CEBASProperty.USER_ROLE.name());
/* 420 */     if (userRole == null || userRole.isEmpty()) {
/* 421 */       logAndSetErrorFlag("000130X", "invalidUserRoleConfig");
/*     */       return;
/*     */     } 
/* 424 */     String SEPARATOR = ",";
/* 425 */     String[] userRoles = userRole.split(",");
/*     */ 
/*     */     
/* 428 */     if (userRoles.length != (UserRole.values()).length - 1) {
/* 429 */       logAndSetErrorFlag("000130X", "invalidUserRoleConfig");
/*     */       
/*     */       return;
/*     */     } 
/* 433 */     for (String current : userRoles) {
/* 434 */       boolean isValid = false;
/*     */       
/* 436 */       for (UserRole userRoleEnum : UserRole.values()) {
/*     */         
/* 438 */         if (!userRoleEnum.getText().equals(UserRole.NO_ROLE.getText()) && userRoleEnum
/* 439 */           .getText().equals(current)) {
/* 440 */           isValid = true;
/*     */         }
/*     */       } 
/* 443 */       if (!isValid) {
/* 444 */         logAndSetErrorFlag("000130X", "invalidUserRoleConfig");
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkSecret() {
/* 454 */     String secret = this.configurator.readProperty(CeBASStartupProperty.SECRET.getProperty());
/* 455 */     if (secret == null || secret.length() != 32 || secret.contains(" ")) {
/* 456 */       logAndSetErrorFlag("000131X", "invalidSecretConfig");
/*     */       
/*     */       return;
/*     */     } 
/* 460 */     boolean hasUpper = false;
/* 461 */     boolean hasLower = false;
/* 462 */     boolean hasDigit = false;
/* 463 */     boolean hasSpecialChar = false;
/* 464 */     boolean hasWhiteSpace = false;
/* 465 */     for (int i = 0; i < secret.length(); i++) {
/* 466 */       if (Character.isLowerCase(secret.charAt(i))) {
/* 467 */         hasLower = true;
/*     */       }
/* 469 */       if (Character.isUpperCase(secret.charAt(i))) {
/* 470 */         hasUpper = true;
/*     */       }
/* 472 */       if (Character.isDigit(secret.charAt(i))) {
/* 473 */         hasDigit = true;
/*     */       }
/* 475 */       if (!Character.isLetterOrDigit(secret.charAt(i))) {
/* 476 */         hasSpecialChar = true;
/*     */       }
/* 478 */       if (Character.isWhitespace(secret.charAt(i))) {
/* 479 */         hasWhiteSpace = true;
/*     */       }
/*     */     } 
/* 482 */     if (!hasUpper || !hasLower || !hasDigit || !hasSpecialChar || hasWhiteSpace) {
/* 483 */       logAndSetErrorFlag("000131X", "invalidSecretConfig");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkLoggingLevel() {
/* 492 */     String loggingLevel = this.configurator.readProperty(CEBASProperty.LOGGING_LEVEL.name());
/*     */     
/* 494 */     boolean isValid = (Level.FINE.getName().equals(loggingLevel) || Level.FINER.getName().equals(loggingLevel) || Level.FINEST.getName().equals(loggingLevel) || Level.INFO.getName().equals(loggingLevel));
/* 495 */     if (!isValid) {
/* 496 */       logAndSetErrorFlag("000135X", "invalidLogLevelConfig");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkLogbackFile() {
/* 504 */     String logbackFile = this.configurator.readProperty(CEBASProperty.SERVER_LOGBACK_FILE.name());
/* 505 */     if (!logbackFile.contains(this.logFileName)) {
/* 506 */       logAndSetErrorFlag("000138X", "invalidLogbackFile");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkLogbackArchivesPattern() {
/* 514 */     String logbackFileNamePattern = this.configurator.readProperty(CEBASProperty.SERVER_LOGBACK_FILENAME_PATTERN.name());
/* 515 */     if (!logbackFileNamePattern.contains(this.rotatedLogsFilePattern)) {
/* 516 */       logAndSetErrorFlag("000139X", "invalidLogbackArchivePattern");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkLocalhostAddress() {
/* 524 */     if (!this.systemConfiguration.getServerAddress().equals("localhost")) {
/* 525 */       logAndSetErrorFlag("000153X", "wrongServerAddress");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkPersistenceAuditEntries() {
/* 534 */     String enablePersistencePerformanceAuditEntries = this.configurator.readProperty(CEBASProperty.PERSIST_PERFORMANCE_AUDIT_ENTRY.name());
/* 535 */     checkBooleanConfiguration(enablePersistencePerformanceAuditEntries, "000273X", "invalidPersistenceAuditEntriesValidationConfig");
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\validation\AbstractConfigurationChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */