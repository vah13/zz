/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.CEBASProperty
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException
 *  com.daimler.cebas.configuration.entity.Configuration
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.entity.User
 */
package com.daimler.cebas.configuration.control;

import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.CEBASProperty;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
import com.daimler.cebas.configuration.entity.Configuration;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.entity.User;
import java.util.Optional;
import java.util.logging.Level;

public class ConfigurationUtil {
    private static final String CLASS_NAME = ConfigurationUtil.class.getSimpleName();

    private ConfigurationUtil() {
    }

    public static Configuration getConfigurationForUser(User user, CEBASProperty zenZefiProperty, Logger logger, MetadataManager i18n) {
        String METHOD_NAME = "getConfigurationForUser";
        logger.entering(CLASS_NAME, "getConfigurationForUser");
        Optional<Configuration> configOptional = user.getConfigurations().stream().filter(config -> config.getConfigKey().equals(zenZefiProperty.name())).findFirst();
        Configuration configuration = configOptional.orElseThrow(logger.logWithTranslationSupplier(Level.SEVERE, "000045X", new String[]{zenZefiProperty.name()}, (CEBASException)new ZenZefiConfigurationException(i18n.getMessage("couldNotFindConfiguration", new String[]{zenZefiProperty.name()}), "couldNotFindConfiguration")));
        logger.exiting(CLASS_NAME, "getConfigurationForUser");
        return configuration;
    }

    public static boolean isAutomaticSelection(User user, Logger logger, MetadataManager i18n) {
        Configuration certSelectionConfiguration = ConfigurationUtil.getConfigurationForUser(user, CEBASProperty.CERT_SELECTION, logger, i18n);
        if (certSelectionConfiguration != null) return ConfigurationUtil.isAutomaticSelection(certSelectionConfiguration);
        throw new ZenZefiConfigurationException("Certificate selection configuration does not exist for current user");
    }

    public static boolean isAutomaticSelection(Configuration certSelectionConfiguration) {
        return "automatic".equals(certSelectionConfiguration.getConfigValue());
    }

    public static boolean hasUserExtendedValidation(User user, Logger logger, MetadataManager i18n) {
        Configuration extendedValidation = ConfigurationUtil.getConfigurationForUser(user, CEBASProperty.VALIDATE_CERTS, logger, i18n);
        return extendedValidation.getConfigValue().equalsIgnoreCase(Boolean.TRUE.toString());
    }
}
