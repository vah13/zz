/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASProperty
 *  com.daimler.cebas.common.control.HostUtil
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException
 *  com.daimler.cebas.configuration.entity.Configuration
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.system.control.startup.CeBASStartupProperty
 *  com.daimler.cebas.users.entity.User
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.core.env.Environment
 */
package com.daimler.cebas.configuration.control;

import com.daimler.cebas.common.control.CEBASProperty;
import com.daimler.cebas.common.control.HostUtil;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
import com.daimler.cebas.configuration.entity.Configuration;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.system.control.startup.CeBASStartupProperty;
import com.daimler.cebas.users.entity.User;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public abstract class AbstractConfigurator {
    private static final String CLASS_NAME = AbstractConfigurator.class.getSimpleName();
    private static java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(AbstractConfigurator.class.getSimpleName());
    protected Logger logger;
    protected Environment environment;
    @Autowired
    private MetadataManager i18n;

    public Configuration copy(Configuration originalConfiguration) {
        String METHOD_NAME = "copy";
        this.logger.entering(CLASS_NAME, "copy");
        if (originalConfiguration == null) {
            ZenZefiConfigurationException e = new ZenZefiConfigurationException(this.i18n.getMessage("configurationMustNotBeNull"), "nullConfigForCopyFound");
            this.logger.logWithTranslation(Level.SEVERE, "000006X", e.getMessageId(), e.getClass().getSimpleName());
            throw e;
        }
        Configuration copyConfiguration = new Configuration();
        copyConfiguration.setConfigKey(originalConfiguration.getConfigKey());
        copyConfiguration.setConfigValue(originalConfiguration.getConfigValue());
        copyConfiguration.setDescription(originalConfiguration.getDescription());
        this.logger.exiting(CLASS_NAME, "copy");
        return copyConfiguration;
    }

    public String getSecret() {
        String METHOD_NAME = "getSecret";
        this.logger.entering(CLASS_NAME, "getSecret");
        this.logger.exiting(CLASS_NAME, "getSecret");
        return this.readProperty(CeBASStartupProperty.SECRET.getProperty());
    }

    public String getPKCS12PackagePassword() {
        String METHOD_NAME = "getPKCS12PackagePassword";
        this.logger.entering(CLASS_NAME, "getPKCS12PackagePassword");
        this.logger.exiting(CLASS_NAME, "getPKCS12PackagePassword");
        return this.readProperty(CeBASStartupProperty.PKCS12_PACKAGE_PASSWORD.getProperty());
    }

    public String getCSRSubject() {
        String METHOD_NAME = "getCSRSubject";
        this.logger.entering(CLASS_NAME, "getCSRSubject");
        this.logger.exiting(CLASS_NAME, "getCSRSubject");
        return this.readProperty("csr.subject");
    }

    public String getLoggingLevel() {
        String METHOD_NAME = "getLoggingLevel";
        this.logger.entering(CLASS_NAME, "getLoggingLevel");
        this.logger.exiting(CLASS_NAME, "getLoggingLevel");
        return this.readProperty(CEBASProperty.LOGGING_LEVEL.name());
    }

    public Map<String, String> readProperties(List<String> keys) {
        String METHOD_NAME = "readProperties";
        this.logger.entering(CLASS_NAME, "readProperties");
        this.logger.exiting(CLASS_NAME, "readProperties");
        return keys.stream().collect(Collectors.toMap(key -> key, this::readProperty));
    }

    public List<Configuration> getDefaultUserProperties() {
        String METHOD_NAME = "getDefaultUserProperties";
        this.logger.entering(CLASS_NAME, METHOD_NAME);
        List<String> keys = this.getDefaultUserPropertiesList();
        this.logger.exiting(CLASS_NAME, METHOD_NAME);
        return this.getUserProperties(keys);
    }

    protected List<String> getDefaultUserPropertiesList() {
        return new ArrayList<String>(Arrays.asList(CEBASProperty.VALIDATE_CERTS.name(), CEBASProperty.DELETE_EXPIRED_CERTS.name(), CEBASProperty.DELETE_CERTS_SCHEDULE.name(), CEBASProperty.USER_ROLE.name()));
    }

    public List<Configuration> getRegisteredUserProperties() {
        String METHOD_NAME = "getRegisteredUserProperties";
        this.logger.entering(CLASS_NAME, "getRegisteredUserProperties");
        List<String> keys = Arrays.asList(CEBASProperty.VALIDATE_CERTS.name(), CEBASProperty.DELETE_EXPIRED_CERTS.name(), CEBASProperty.CERT_SELECTION.name(), CEBASProperty.USER_ROLE.name(), CEBASProperty.CERT_TABLE_DETAIL_VIEW_STATE.name(), CEBASProperty.CERT_TABLE_VISIBLE_COLUMNS.name(), CEBASProperty.CERT_TABLE_COLUMN_ORDER.name(), CEBASProperty.PAGINATION_MAX_ROWS_PER_PAGE.name(), CEBASProperty.AUTO_CERT_UPDATE.name());
        this.logger.exiting(CLASS_NAME, "getRegisteredUserProperties");
        return this.getUserProperties(keys);
    }

    public String readProperty(String key) {
        return this.environment.getProperty(key);
    }

    public void updateDefaultUserConfigurations(List<Configuration> currentDefaultUserProperties) {
        this.getDefaultUserProperties().forEach(property -> {
            if (currentDefaultUserProperties.stream().filter(currentProperty -> property.getConfigKey().equals(currentProperty.getConfigKey())).findAny().isPresent()) return;
            currentDefaultUserProperties.add((Configuration)property);
        });
    }

    public void updateRegisteredUserConfigurations(User user) {
        this.getRegisteredUserProperties().forEach(property -> {
            if (user.getConfigurations().stream().filter(currentProperty -> property.getConfigKey().equals(currentProperty.getConfigKey())).findAny().isPresent()) return;
            user.getConfigurations().add(property);
        });
    }

    private List<Configuration> getUserProperties(List<String> keys) {
        Map<String, String> properties = this.readProperties(keys);
        return this.createConfigurationList(properties);
    }

    private List<Configuration> createConfigurationList(Map<String, String> properties) {
        return properties.entrySet().stream().map(x -> this.createConfiguration((String)x.getKey(), (String)x.getValue())).collect(Collectors.toList());
    }

    public Configuration getMachineNameConfiguration() {
        String METHOD_NAME = "getMachineNameConfiguration";
        this.logger.entering(CLASS_NAME, METHOD_NAME);
        this.logger.exiting(CLASS_NAME, METHOD_NAME);
        return this.createConfiguration("MACHINE_NAME", HostUtil.getMachineName((Logger)this.logger, (MetadataManager)this.i18n));
    }

    private Configuration createConfiguration(String key, String value) {
        Configuration configuration = new Configuration();
        configuration.setConfigKey(key);
        configuration.setConfigValue(value);
        return configuration;
    }

    public static Properties loadProperties(String resourceFileName) {
        Properties configuration = new Properties();
        InputStream inputStream = AbstractConfigurator.class.getClassLoader().getResourceAsStream(resourceFileName);
        try {
            configuration.load(inputStream);
            if (inputStream == null) return configuration;
            inputStream.close();
        }
        catch (Exception e) {
            LOG.warning("Failed to load properties from file " + resourceFileName + ": " + e.getMessage());
        }
        return configuration;
    }

    public static String loadProperty(String resourceFileName, String key) {
        Properties configuration = AbstractConfigurator.loadProperties(resourceFileName);
        return configuration.getProperty(key);
    }
}
