/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.AbstractRepository
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASRepository
 *  com.daimler.cebas.common.entity.AbstractEntity
 *  com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException
 *  com.daimler.cebas.configuration.entity.Configuration
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.daimler.cebas.configuration.control;

import com.daimler.cebas.common.control.AbstractRepository;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASRepository;
import com.daimler.cebas.common.entity.AbstractEntity;
import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
import com.daimler.cebas.configuration.entity.Configuration;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@CEBASRepository
public class ConfigurationRepository
extends AbstractRepository {
    private static final Logger LOG = Logger.getLogger(ConfigurationRepository.class.getName());
    private static final String CLASS_NAME = ConfigurationRepository.class.getSimpleName();
    private MetadataManager i18n;

    @Autowired
    public ConfigurationRepository(MetadataManager i18n) {
        this.i18n = i18n;
    }

    public Optional<Configuration> findConfigurationByConfigKey(String configKey) {
        String METHOD_NAME = "findConfigurationByConfigKey";
        LOG.entering(CLASS_NAME, METHOD_NAME);
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("configKey", configKey);
        List resultList = this.findWithNamedQuery("Configuration_FIND_BY_CONFIG_KEY ", parameters, 1);
        Configuration configuration = null;
        if (!resultList.isEmpty()) {
            configuration = (Configuration)resultList.get(0);
        }
        LOG.exiting(CLASS_NAME, METHOD_NAME);
        return Optional.ofNullable(configuration);
    }

    public Configuration addConfiguration(String configKey, String configValue) {
        String METHOD_NAME = "addConfiguration";
        LOG.entering(CLASS_NAME, METHOD_NAME);
        Configuration configuration = new Configuration();
        configuration.setConfigKey(configKey);
        configuration.setConfigValue(configValue);
        LOG.exiting(CLASS_NAME, METHOD_NAME);
        return (Configuration)this.create((AbstractEntity)configuration);
    }

    public Configuration updateConfiguration(String configKey, String configValue) {
        String METHOD_NAME = "updateConfiguration";
        LOG.entering(CLASS_NAME, METHOD_NAME);
        LOG.exiting(CLASS_NAME, METHOD_NAME);
        return this.updateConfig(configKey, configValue);
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public Configuration updateConfigurationInNewTransaction(String configKey, String configValue) {
        String METHOD_NAME = "updateConfigurationInNewTransaction";
        LOG.entering(CLASS_NAME, METHOD_NAME);
        LOG.exiting(CLASS_NAME, METHOD_NAME);
        return this.updateConfig(configKey, configValue);
    }

    private Configuration updateConfig(String configKey, String configValue) {
        Optional<Configuration> configurationOptional = this.findConfigurationByConfigKey(configKey);
        Configuration configuration = configurationOptional.orElseThrow(() -> new ZenZefiConfigurationException(this.i18n.getMessage("couldNotUpdateConfiguration")));
        configuration.setConfigValue(configValue);
        return (Configuration)this.update((AbstractEntity)configuration);
    }
}
