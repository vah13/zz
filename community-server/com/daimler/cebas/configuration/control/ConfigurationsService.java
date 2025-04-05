/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.cronjob.CertificatesCleanUp
 *  com.daimler.cebas.certificates.control.cronjob.CertificatesCronJobs
 *  com.daimler.cebas.common.control.CEBASProperty
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASService
 *  com.daimler.cebas.common.entity.AbstractEntity
 *  com.daimler.cebas.configuration.control.AbstractConfigurator
 *  com.daimler.cebas.configuration.control.ConfigurationRepository
 *  com.daimler.cebas.configuration.control.ConfigurationUtil
 *  com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException
 *  com.daimler.cebas.configuration.control.vo.RolePriorityConfiguration
 *  com.daimler.cebas.configuration.entity.Configuration
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  com.daimler.cebas.users.entity.User
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.daimler.cebas.configuration.control;

import com.daimler.cebas.certificates.control.cronjob.CertificatesCleanUp;
import com.daimler.cebas.certificates.control.cronjob.CertificatesCronJobs;
import com.daimler.cebas.common.control.CEBASProperty;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASService;
import com.daimler.cebas.common.entity.AbstractEntity;
import com.daimler.cebas.configuration.control.AbstractConfigurator;
import com.daimler.cebas.configuration.control.ConfigurationRepository;
import com.daimler.cebas.configuration.control.ConfigurationUtil;
import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
import com.daimler.cebas.configuration.control.vo.RolePriorityConfiguration;
import com.daimler.cebas.configuration.entity.Configuration;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import com.daimler.cebas.users.entity.User;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@CEBASService
public class ConfigurationsService {
    private static final String DEFAULT = "default";
    private static final String CLASS_NAME = ConfigurationsService.class.getSimpleName();
    protected Logger logger;
    protected Session session;
    protected ConfigurationRepository repository;
    private CertificatesCronJobs certificateCronJobs;
    protected AbstractConfigurator configurator;
    protected MetadataManager i18n;
    private CertificatesCleanUp cleanUpCertificates;
    private final Predicate<? super Configuration> userConfigurationsPredicate = configuration -> configuration.getConfigKey().equals(CEBASProperty.CERT_SELECTION.name()) || configuration.getConfigKey().equals(CEBASProperty.USER_ROLE.name()) || configuration.getConfigKey().equals(CEBASProperty.VALIDATE_CERTS.name()) || configuration.getConfigKey().equals(CEBASProperty.DELETE_EXPIRED_CERTS.name()) || configuration.getConfigKey().equals(CEBASProperty.AUTO_CERT_UPDATE.name());
    private final Predicate<? super Configuration> generalConfigurationsPredicate = configuration -> configuration.getConfigKey().equals(CEBASProperty.DELETE_CERTS_SCHEDULE.name()) || configuration.getConfigKey().equals(CEBASProperty.PKISCOPE_SWITCH.name());

    @Autowired
    public ConfigurationsService(Session session, ConfigurationRepository repository, Logger logger, CertificatesCronJobs certificateCronJobs, AbstractConfigurator configurator, MetadataManager i18n, CertificatesCleanUp cleanUpCertificates) {
        this.session = session;
        this.repository = repository;
        this.logger = logger;
        this.certificateCronJobs = certificateCronJobs;
        this.configurator = configurator;
        this.i18n = i18n;
        this.cleanUpCertificates = cleanUpCertificates;
    }

    @Transactional(readOnly=true, propagation=Propagation.REQUIRED)
    public List<Configuration> getCurrentUserConfigurations() {
        String METHOD_NAME = "getCurrentUserConfigurations";
        this.logger.entering(CLASS_NAME, "getCurrentUserConfigurations");
        User currentUser = this.session.getCurrentUser();
        List<Configuration> configurations = currentUser.getConfigurations().stream().filter(this.userConfigurationsPredicate).collect(Collectors.toList());
        this.logger.exiting(CLASS_NAME, "getCurrentUserConfigurations");
        return configurations;
    }

    @Transactional(readOnly=true, propagation=Propagation.REQUIRED)
    public List<Configuration> getGeneralConfigurations() {
        String METHOD_NAME = "getGeneralConfigurations";
        this.logger.entering(CLASS_NAME, "getGeneralConfigurations");
        User defaultUser = this.session.getDefaultUser();
        List<Configuration> configurations = defaultUser.getConfigurations().stream().filter(this.generalConfigurationsPredicate).collect(Collectors.toList());
        this.logger.exiting(CLASS_NAME, "getGeneralConfigurations");
        return configurations;
    }

    public Configuration updateConfiguration(Configuration configuration) {
        String METHOD_NAME = "updateConfiguration";
        this.logger.entering(CLASS_NAME, "updateConfiguration");
        Optional currentUserConfigOptional = this.repository.find(Configuration.class, configuration.getEntityId());
        if (!currentUserConfigOptional.isPresent()) throw new ZenZefiConfigurationException("Configuration not found.");
        Configuration currentUserConfig = (Configuration)currentUserConfigOptional.get();
        currentUserConfig.setConfigValue(configuration.getConfigValue());
        Configuration config = (Configuration)this.repository.update((AbstractEntity)currentUserConfig);
        this.repository.flush();
        if (config.getConfigKey().equals(CEBASProperty.DELETE_CERTS_SCHEDULE.name())) {
            this.certificateCronJobs.scheduleDeleteExpiredCertificatesForAllUsers();
        }
        if (config.getConfigKey().equals(CEBASProperty.VALIDATE_CERTS.name())) {
            String state = config.getConfigValue().equals("true") ? "ON" : "OFF";
            this.logger.log(Level.INFO, "000618", "Certificate validation set to " + state, CLASS_NAME);
        }
        if (config.getConfigKey().equals(CEBASProperty.DELETE_EXPIRED_CERTS.name()) && config.getConfigValue().equals("true")) {
            this.cleanUpCertificates.cleanUpCertificatesCurrentUser();
        }
        this.logger.log(Level.INFO, "000089", this.i18n.getEnglishMessage("configWithIdUpdated", new String[]{config.getEntityId()}), CLASS_NAME);
        this.logger.exiting(CLASS_NAME, "updateConfiguration");
        return config;
    }

    @Transactional(readOnly=true, propagation=Propagation.REQUIRED)
    public List<RolePriorityConfiguration> getRolesPriorityConfiguration() {
        String METHOD_NAME = "getRolesPriorityConfiguration";
        this.logger.entering(CLASS_NAME, "getRolesPriorityConfiguration");
        ArrayList<RolePriorityConfiguration> rolePriorityConfigList = new ArrayList<RolePriorityConfiguration>();
        Configuration configuration = this.getUserRolesConfiguration();
        String[] split = configuration.getConfigValue().split(",");
        int i = 0;
        while (true) {
            if (i >= split.length) {
                this.logger.exiting(CLASS_NAME, "getRolesPriorityConfiguration");
                return rolePriorityConfigList;
            }
            rolePriorityConfigList.add(new RolePriorityConfiguration(Integer.toString(i + 1), split[i]));
            ++i;
        }
    }

    @Transactional(readOnly=true, propagation=Propagation.REQUIRED)
    public Configuration getUserRolesConfiguration() {
        String METHOD_NAME = "getUserRolesConfiguration";
        this.logger.entering(CLASS_NAME, "getUserRolesConfiguration");
        this.logger.exiting(CLASS_NAME, "getUserRolesConfiguration");
        return ConfigurationUtil.getConfigurationForUser((User)this.session.getCurrentUser(), (CEBASProperty)CEBASProperty.USER_ROLE, (Logger)this.logger, (MetadataManager)this.i18n);
    }

    public void setDetailsPanelState(String state) {
        String METHOD_NAME = "setDetailsPanelState";
        this.logger.entering(CLASS_NAME, "setDetailsPanelState");
        User currentUser = this.session.getCurrentUser();
        currentUser.getConfigurations().forEach(configuration -> {
            if (!configuration.getConfigKey().equals(CEBASProperty.CERT_TABLE_DETAIL_VIEW_STATE.name())) return;
            configuration.setConfigValue(state);
        });
        this.logger.exiting(CLASS_NAME, "setDetailsPanelState");
    }

    public String getDetailsPanelState() {
        Configuration configuration;
        String METHOD_NAME = "getDetailsPanelState";
        this.logger.entering(CLASS_NAME, "getDetailsPanelState");
        User currentUser = this.session.getCurrentUser();
        Iterator iterator = currentUser.getConfigurations().iterator();
        do {
            if (iterator.hasNext()) continue;
            this.logger.exiting(CLASS_NAME, "getDetailsPanelState");
            return "";
        } while (!(configuration = (Configuration)iterator.next()).getConfigKey().equals(CEBASProperty.CERT_TABLE_DETAIL_VIEW_STATE.name()));
        return configuration.getConfigValue();
    }

    @Transactional(readOnly=true, propagation=Propagation.REQUIRED)
    public String getCertificatesColumnVisibility() {
        String METHOD_NAME = "getCertificatesColumnVisibility";
        this.logger.entering(CLASS_NAME, "getCertificatesColumnVisibility");
        String configValue = "";
        User currentUser = this.session.getCurrentUser();
        List configurations = currentUser.getConfigurations();
        for (Configuration configuration : configurations) {
            if (!configuration.getConfigKey().equals(CEBASProperty.CERT_TABLE_VISIBLE_COLUMNS.name())) continue;
            configValue = configuration.getConfigValue();
            if (!DEFAULT.equals(configValue)) break;
            configValue = this.defaultColumnVisibility();
            break;
        }
        this.logger.exiting(CLASS_NAME, "getCertificatesColumnVisibility");
        return configValue;
    }

    public void setCertificatesColumnVisibility(String columnVisibilities) {
        String METHOD_NAME = "setCertificatesColumnVisibility";
        this.logger.entering(CLASS_NAME, "setCertificatesColumnVisibility");
        User currentUser = this.session.getCurrentUser();
        currentUser.getConfigurations().forEach(configuration -> {
            if (!configuration.getConfigKey().equals(CEBASProperty.CERT_TABLE_VISIBLE_COLUMNS.name())) return;
            configuration.setConfigValue(columnVisibilities);
        });
        this.logger.exiting(CLASS_NAME, "setCertificatesColumnVisibility");
    }

    public void setCertificatesColumnOrder(String columnOrder) {
        String METHOD_NAME = "setCertificatesColumnOrder";
        this.logger.entering(CLASS_NAME, "setCertificatesColumnOrder");
        User currentUser = this.session.getCurrentUser();
        currentUser.getConfigurations().forEach(configuration -> {
            if (!configuration.getConfigKey().equals(CEBASProperty.CERT_TABLE_COLUMN_ORDER.name())) return;
            configuration.setConfigValue(columnOrder);
        });
        this.logger.exiting(CLASS_NAME, "setCertificatesColumnOrder");
    }

    public String getCertificatesColumnOrder() {
        String METHOD_NAME = "getCertificatesColumnOrder";
        this.logger.entering(CLASS_NAME, "getCertificatesColumnOrder");
        String configValue = "";
        User currentUser = this.session.getCurrentUser();
        for (Configuration configuration : currentUser.getConfigurations()) {
            if (!configuration.getConfigKey().equals(CEBASProperty.CERT_TABLE_COLUMN_ORDER.name())) continue;
            configValue = configuration.getConfigValue();
            if (!DEFAULT.equals(configValue)) break;
            configValue = this.defaultColumnOrder();
            break;
        }
        this.logger.exiting(CLASS_NAME, "getCertificatesColumnOrder");
        return configValue;
    }

    public String getApplicationVersion() {
        String APPLICATION_VERSION_PROPERTY = "info.build.version";
        String METHOD_NAME = "getApplicationVersion";
        this.logger.entering(CLASS_NAME, "getApplicationVersion");
        this.logger.exiting(CLASS_NAME, "getApplicationVersion");
        return this.configurator.readProperty("info.build.version");
    }

    private String defaultColumnVisibility() {
        return "{\"subject\":true,\"pkirole\":true,\"userRole\":true,\"issuer\":true,\"serialNo\":true,\"targetECU\":true,\"targetVIN\":true,\"status\":true,\"validTo\":true,\"validFrom\":false,\"validityStrengthColor\":true,\"uniqueECUID\":false,\"specialECU\":false,\"subjectPublicKey\":false,\"baseCertificateID\":false,\"issuerSerialNumber\":false,\"subjectKeyIdentifier\":false,\"authorityKeyIdentifier\":false,\"services\":false,\"nonce\":false,\"signature\":false,\"targetSubjectKeyIdentifier\":false,\"zkNo\":false,\"ecuPackageTs\":false,\"linkCertTs\":false,\"pkiKnown\":false,\"pkiState\":false}";
    }

    private String defaultColumnOrder() {
        return "subject,pkirole,userRole,issuer,serialNo,targetECU,targetVIN,status,validTo,validFrom,validityStrengthColor,uniqueECUID,specialECU,subjectPublicKey,baseCertificateID,issuerSerialNumber,subjectKeyIdentifier,authorityKeyIdentifier,services,nonce,signature,targetSubjectKeyIdentifier,zkNo,ecuPackageTs,linkCertTs,pkiKnown,pkiState";
    }
}
