/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.validation.CertificatesValidator
 *  com.daimler.cebas.certificates.entity.UserRole
 *  com.daimler.cebas.common.control.CEBASProperty
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.configuration.control.AbstractConfigurator
 *  com.daimler.cebas.configuration.control.CeBASGeneralPropertiesManager
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.system.control.exceptions.ConfigurationCheckException
 *  com.daimler.cebas.system.control.profile.ISystemConfiguration
 *  com.daimler.cebas.system.control.startup.CeBASStartupProperty
 *  com.daimler.cebas.system.control.validation.vo.BusinessEnvironment
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.scheduling.support.CronSequenceGenerator
 *  org.springframework.util.StringUtils
 */
package com.daimler.cebas.system.control.validation;

import com.daimler.cebas.certificates.control.validation.CertificatesValidator;
import com.daimler.cebas.certificates.entity.UserRole;
import com.daimler.cebas.common.control.CEBASProperty;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.configuration.control.AbstractConfigurator;
import com.daimler.cebas.configuration.control.CeBASGeneralPropertiesManager;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.system.control.exceptions.ConfigurationCheckException;
import com.daimler.cebas.system.control.profile.ISystemConfiguration;
import com.daimler.cebas.system.control.startup.CeBASStartupProperty;
import com.daimler.cebas.system.control.validation.vo.BusinessEnvironment;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.crypto.Cipher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.util.StringUtils;

public abstract class AbstractConfigurationChecker {
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(CertificatesValidator.class.getName());
    private static final String LOCALHOST = "localhost";
    private static final String CLASS_NAME = AbstractConfigurationChecker.class.getSimpleName();
    @Value(value="${server.logback.filename}.log")
    private String logFileName;
    @Value(value="${server.logback.filename}-%d{yyyy-MM-dd}.%i.log.zip")
    private String rotatedLogsFilePattern;
    protected AbstractConfigurator configurator;
    protected Logger logger;
    protected MetadataManager i18n;
    protected CeBASGeneralPropertiesManager generalPropertiesManager;
    private ISystemConfiguration systemConfiguration;

    public AbstractConfigurationChecker(AbstractConfigurator configurator, Logger logger, MetadataManager i18n, ISystemConfiguration systemConfiguration, CeBASGeneralPropertiesManager generalPropertiesManager) {
        this.configurator = configurator;
        this.logger = logger;
        this.i18n = i18n;
        this.systemConfiguration = systemConfiguration;
        this.generalPropertiesManager = generalPropertiesManager;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public void setRotatedLogsFilePattern(String rotatedLogsFilePattern) {
        this.rotatedLogsFilePattern = rotatedLogsFilePattern;
    }

    public void checkConfiguration() {
        String METHOD_NAME = "checkConfiguration";
        this.logger.entering(CLASS_NAME, "checkConfiguration");
        this.checkUnlimitedCryptographyStrengthExtension();
        this.checkBusinessEnvironment();
        this.checkValidateCerts();
        this.checkDeleteExpiredCerts();
        this.checkDeleteCertsSchedule();
        this.checkUserRole();
        this.checkSecret();
        this.checkLoggingLevel();
        this.checkLogbackFile();
        this.checkPersistenceAuditEntries();
        this.checkLogbackArchivesPattern();
        this.checkBackupTimeConfiguration();
        this.checkValidityCheckTimeToleranceConfiguration();
        if (this.systemConfiguration.onlyLocalhostAddress()) {
            this.checkLocalhostAddress();
        }
        this.logger.exiting(CLASS_NAME, "checkConfiguration");
    }

    public void checkOverridingOfGeneralProperties() {
        this.checkOverridingProperty(CEBASProperty.MAX_AGE_LOGENTRY.name(), "000388X");
        this.checkOverridingProperty(CEBASProperty.CHECK_MAX_AGE_LOGENTRY_MIN.name(), "000389X");
        this.checkOverridingProperty(CEBASProperty.CHECK_MAX_AGE_LOGENTRY_MAX.name(), "000390X");
        this.checkOverridingProperty(CEBASProperty.USER_ROLE.name(), "000391X");
        this.checkOverridingProperty(CEBASProperty.DELETE_CERTS_SCHEDULE.name(), "000392X");
        this.checkOverridingProperty(CEBASProperty.DELETE_EXPIRED_CERTS.name(), "000393X");
        this.checkOverridingProperty(CEBASProperty.VALIDATE_CERTS.name(), "000394X");
        this.checkOverridingProperty(CEBASProperty.MAX_SIZE_LOGENTRY.name(), "000678X");
        this.checkOverridingProperty(CEBASProperty.VALIDITY_CHECK_TIME_TOLERANCE.name(), "000684X");
        this.checkOverridingProperty(CEBASProperty.PKISCOPE_SWITCH.name(), "000690X");
        CeBASStartupProperty[] ceBASStartupPropertyArray = CeBASStartupProperty.values();
        int n = ceBASStartupPropertyArray.length;
        int n2 = 0;
        while (n2 < n) {
            CeBASStartupProperty p = ceBASStartupPropertyArray[n2];
            this.checkOverridingProperty(p.getProperty(), "000408X");
            this.checkOverridingProperty(p.name(), "000408X");
            ++n2;
        }
    }

    protected void checkOverridingProperty(String propertyKey, String logId) {
        if (this.generalPropertiesManager.getProperty(propertyKey) == null) return;
        String message = this.i18n.getEnglishMessage("propertyNotAllowedToBeOverridenInGeneralProperties", new String[]{propertyKey});
        this.logger.log(Level.SEVERE, logId, message, CLASS_NAME);
        throw new ConfigurationCheckException(message);
    }

    protected void logAndSetErrorFlag(String logId, String messageId) {
        this.logger.log(Level.SEVERE, logId, this.i18n.getEnglishMessage(messageId), CLASS_NAME);
        throw new ConfigurationCheckException();
    }

    protected void checkBooleanConfiguration(String configuration, String logId, String logMessage) {
        if (configuration == null) {
            this.logAndSetErrorFlag(logId, logMessage);
            return;
        }
        boolean isValid = "true".equals(configuration) || "false".equals(configuration);
        if (isValid) return;
        this.logAndSetErrorFlag(logId, logMessage);
    }

    protected void checkNumericConfiguration(String configuration, String logId, String logMessage) {
        if (configuration == null) {
            this.logAndSetErrorFlag(logId, logMessage);
            return;
        }
        if (org.apache.commons.lang3.StringUtils.isNumeric(configuration)) return;
        this.logAndSetErrorFlag(logId, logMessage);
    }

    protected int getIntConfiguration(String configuration, String logId, String logMessage) {
        if (configuration == null) {
            this.logAndSetErrorFlag(logId, logMessage);
            return 0;
        }
        try {
            return Integer.valueOf(configuration);
        }
        catch (NumberFormatException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            this.logAndSetErrorFlag(logId, logMessage);
            return 0;
        }
    }

    private void checkBackupTimeConfiguration() {
        String backupTime = this.configurator.readProperty("system_data_backup_time");
        if (org.apache.commons.lang3.StringUtils.isNumeric(backupTime)) return;
        throw new ConfigurationCheckException("Wrong configuration for backup time. It must be in hours and non zero");
    }

    private void checkValidityCheckTimeToleranceConfiguration() {
        String timeTolerance = this.configurator.readProperty(CEBASProperty.VALIDITY_CHECK_TIME_TOLERANCE.name());
        this.checkNumericConfiguration(timeTolerance, "000685X", "invalidValidityCheckTimeToleranceConfig");
    }

    private void checkUnlimitedCryptographyStrengthExtension() {
        try {
            int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
            if (maxKeyLen >= Integer.MAX_VALUE) return;
            this.logAndSetErrorFlag("000008X", "limitedCryptographyStrength");
        }
        catch (NoSuchAlgorithmException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new ConfigurationCheckException(e.getMessage());
        }
    }

    private void checkBusinessEnvironment() {
        String businessEnvironment = this.configurator.readProperty(CEBASProperty.BUSINESS_ENVIRONMENT.name());
        boolean isValid = false;
        for (BusinessEnvironment businessEnum : BusinessEnvironment.values()) {
            if (!businessEnum.name().equals(businessEnvironment)) continue;
            isValid = true;
            this.logger.log(Level.INFO, "000321", this.i18n.getEnglishMessage("businessEnvConfig", new String[]{businessEnvironment}), this.getClass().getSimpleName());
            break;
        }
        if (isValid) return;
        this.logAndSetErrorFlag("000125X", "invalidBusinessEnvConfig");
    }

    private void checkValidateCerts() {
        String validateCerts = this.configurator.readProperty(CEBASProperty.VALIDATE_CERTS.name());
        this.checkBooleanConfiguration(validateCerts, "000126X", "invalidValidateCertsConfig");
    }

    private void checkDeleteExpiredCerts() {
        String deleteExpiredCerts = this.configurator.readProperty(CEBASProperty.DELETE_EXPIRED_CERTS.name());
        this.checkBooleanConfiguration(deleteExpiredCerts, "000127X", "invalidDeleteExpiredCertsConfig");
    }

    private void checkDeleteCertsSchedule() {
        String deleteCertsSchedule = this.configurator.readProperty(CEBASProperty.DELETE_CERTS_SCHEDULE.name());
        if (!this.isDeletCertsScheduleCronExpressionValid(deleteCertsSchedule)) {
            this.logAndSetErrorFlag("000128X", "invalidDeleteCertsScheduleConfig");
            return;
        }
        String deleteCertsScheduleGeneralProperty = this.generalPropertiesManager.getProperty(CEBASProperty.DELETE_CERTS_SCHEDULE.name());
        if (StringUtils.isEmpty((Object)deleteCertsScheduleGeneralProperty)) return;
        if (this.isDeletCertsScheduleCronExpressionValid(deleteCertsScheduleGeneralProperty)) return;
        this.logAndSetErrorFlag("000128X", "invalidDeleteCertsScheduleConfig");
    }

    private boolean isDeletCertsScheduleCronExpressionValid(String value) {
        if (value == null) return false;
        if (StringUtils.isEmpty((Object)value)) {
            return false;
        }
        if (!CronSequenceGenerator.isValidExpression((String)value)) {
            return false;
        }
        try {
            CronSequenceGenerator csg = new CronSequenceGenerator(value);
            Date nextRun = csg.next(new Date());
            Date secondRun = csg.next(nextRun);
            long diff = secondRun.getTime() - nextRun.getTime();
            long convert = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
            if (convert >= 10L) return true;
            return false;
        }
        catch (IllegalArgumentException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            return false;
        }
    }

    private void checkUserRole() {
        String userRole = this.configurator.readProperty(CEBASProperty.USER_ROLE.name());
        if (userRole == null || userRole.isEmpty()) {
            this.logAndSetErrorFlag("000130X", "invalidUserRoleConfig");
            return;
        }
        String SEPARATOR = ",";
        String[] userRoles = userRole.split(",");
        if (userRoles.length != UserRole.values().length - 1) {
            this.logAndSetErrorFlag("000130X", "invalidUserRoleConfig");
            return;
        }
        String[] stringArray = userRoles;
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String current = stringArray[n2];
            boolean isValid = false;
            for (UserRole userRoleEnum : UserRole.values()) {
                if (userRoleEnum.getText().equals(UserRole.NO_ROLE.getText()) || !userRoleEnum.getText().equals(current)) continue;
                isValid = true;
            }
            if (!isValid) {
                this.logAndSetErrorFlag("000130X", "invalidUserRoleConfig");
                return;
            }
            ++n2;
        }
    }

    private void checkSecret() {
        String secret = this.configurator.readProperty(CeBASStartupProperty.SECRET.getProperty());
        if (secret == null || secret.length() != 32 || secret.contains(" ")) {
            this.logAndSetErrorFlag("000131X", "invalidSecretConfig");
            return;
        }
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        boolean hasWhiteSpace = false;
        for (int i = 0; i < secret.length(); ++i) {
            if (Character.isLowerCase(secret.charAt(i))) {
                hasLower = true;
            }
            if (Character.isUpperCase(secret.charAt(i))) {
                hasUpper = true;
            }
            if (Character.isDigit(secret.charAt(i))) {
                hasDigit = true;
            }
            if (!Character.isLetterOrDigit(secret.charAt(i))) {
                hasSpecialChar = true;
            }
            if (!Character.isWhitespace(secret.charAt(i))) continue;
            hasWhiteSpace = true;
        }
        if (hasUpper && hasLower && hasDigit && hasSpecialChar) {
            if (!hasWhiteSpace) return;
        }
        this.logAndSetErrorFlag("000131X", "invalidSecretConfig");
    }

    private void checkLoggingLevel() {
        String loggingLevel = this.configurator.readProperty(CEBASProperty.LOGGING_LEVEL.name());
        boolean isValid = Level.FINE.getName().equals(loggingLevel) || Level.FINER.getName().equals(loggingLevel) || Level.FINEST.getName().equals(loggingLevel) || Level.INFO.getName().equals(loggingLevel);
        if (isValid) return;
        this.logAndSetErrorFlag("000135X", "invalidLogLevelConfig");
    }

    private void checkLogbackFile() {
        String logbackFile = this.configurator.readProperty(CEBASProperty.SERVER_LOGBACK_FILE.name());
        if (logbackFile.contains(this.logFileName)) return;
        this.logAndSetErrorFlag("000138X", "invalidLogbackFile");
    }

    private void checkLogbackArchivesPattern() {
        String logbackFileNamePattern = this.configurator.readProperty(CEBASProperty.SERVER_LOGBACK_FILENAME_PATTERN.name());
        if (logbackFileNamePattern.contains(this.rotatedLogsFilePattern)) return;
        this.logAndSetErrorFlag("000139X", "invalidLogbackArchivePattern");
    }

    private void checkLocalhostAddress() {
        if (this.systemConfiguration.getServerAddress().equals(LOCALHOST)) return;
        this.logAndSetErrorFlag("000153X", "wrongServerAddress");
    }

    private void checkPersistenceAuditEntries() {
        String enablePersistencePerformanceAuditEntries = this.configurator.readProperty(CEBASProperty.PERSIST_PERFORMANCE_AUDIT_ENTRY.name());
        this.checkBooleanConfiguration(enablePersistencePerformanceAuditEntries, "000273X", "invalidPersistenceAuditEntriesValidationConfig");
    }
}
