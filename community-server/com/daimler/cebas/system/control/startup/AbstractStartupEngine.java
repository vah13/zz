/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ch.qos.logback.classic.LoggerContext
 *  com.daimler.cebas.certificates.control.cronjob.CertificatesCleanUp
 *  com.daimler.cebas.certificates.control.cronjob.CertificatesCronJobs
 *  com.daimler.cebas.certificates.control.importing.CertificatesImporter
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.CEBASProperty
 *  com.daimler.cebas.common.control.ICEBASRecovery
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.configuration.control.AbstractConfigurator
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.system.control.SystemIntegrity
 *  com.daimler.cebas.system.control.performance.PerformanceAuditCronJob
 *  com.daimler.cebas.system.control.validation.AbstractConfigurationChecker
 *  com.daimler.cebas.users.control.Session
 *  com.daimler.cebas.users.control.crypto.UserCryptoEngine
 *  com.daimler.cebas.users.control.factories.UserFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Qualifier
 *  org.springframework.boot.context.event.ApplicationReadyEvent
 *  org.springframework.context.ApplicationListener
 *  org.springframework.core.env.ConfigurableEnvironment
 *  org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
 */
package com.daimler.cebas.system.control.startup;

import ch.qos.logback.classic.LoggerContext;
import com.daimler.cebas.certificates.control.cronjob.CertificatesCleanUp;
import com.daimler.cebas.certificates.control.cronjob.CertificatesCronJobs;
import com.daimler.cebas.certificates.control.importing.CertificatesImporter;
import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.CEBASProperty;
import com.daimler.cebas.common.control.ICEBASRecovery;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.configuration.control.AbstractConfigurator;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.system.control.SystemIntegrity;
import com.daimler.cebas.system.control.performance.PerformanceAuditCronJob;
import com.daimler.cebas.system.control.validation.AbstractConfigurationChecker;
import com.daimler.cebas.users.control.Session;
import com.daimler.cebas.users.control.crypto.UserCryptoEngine;
import com.daimler.cebas.users.control.factories.UserFactory;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public abstract class AbstractStartupEngine
implements ApplicationListener<ApplicationReadyEvent> {
    private static final String CLASS_NAME = AbstractStartupEngine.class.getSimpleName();
    public static final String[] LOGGERS = new String[]{"Root", "org.springframework.web", "org.hibernate", "com.daimler.cebas"};
    @Autowired
    protected Logger logger;
    @Autowired
    protected Session session;
    @Autowired
    protected CertificatesImporter certificatesImporter;
    @Autowired
    private CertificatesCronJobs certificatesCronJobs;
    @Autowired
    private PerformanceAuditCronJob performanceAuditCronJob;
    @Autowired
    protected AbstractConfigurator configurator;
    @Autowired
    private CertificatesCleanUp cleanUpCertificates;
    @Autowired
    private AbstractConfigurationChecker configurationChecker;
    @Autowired
    private ConfigurableEnvironment environment;
    @Autowired
    protected MetadataManager i18n;
    @Autowired
    private UserCryptoEngine cryptoEngine;
    @Autowired
    @Qualifier(value="taskScheduler")
    protected ThreadPoolTaskScheduler taskScheduler;
    @Autowired
    protected Optional<ICEBASRecovery> cebasRecoveryOptional;

    public void onApplicationEvent(ApplicationReadyEvent event) {
        String METHOD_NAME = "onApplicationEvent";
        this.logger.entering(CLASS_NAME, "onApplicationEvent");
        try {
            this.setLoggerLevel();
            this.configurationChecker.checkOverridingOfGeneralProperties();
            this.configurationChecker.checkConfiguration();
            UserFactory.setDefaultUsername((String)this.getDefaultUserName());
            if (this.session.userExists(UserFactory.getDefaultUsername())) {
                SystemIntegrity.checkSystemIntegrity((UserCryptoEngine)this.cryptoEngine, (AbstractConfigurator)this.configurator, (Logger)this.logger, (Session)this.session, (MetadataManager)this.i18n);
                this.configurator.updateDefaultUserConfigurations(this.session.getDefaultUser().getConfigurations());
                this.session.setDefaultUser();
                CompletableFuture.runAsync(() -> this.cleanUpCertificates.cleanUpCertificatesCurrentUser());
            } else {
                this.createDefaultUser();
                this.session.setDefaultUser();
            }
            this.certificatesCronJobs.scheduleDeleteExpiredCertificatesForAllUsers();
            this.performanceAuditCronJob.scheduleCleanUpPerformanceEntries();
            this.logger.exiting(CLASS_NAME, "onApplicationEvent");
        }
        catch (CEBASException e) {
            this.close();
            throw e;
        }
    }

    public abstract String getDefaultUserName();

    public abstract void createDefaultUser();

    private void setLoggerLevel() {
        String loggingLevel = this.configurator.readProperty(CEBASProperty.LOGGING_LEVEL.name());
        LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
        String[] stringArray = LOGGERS;
        int n = stringArray.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) {
                this.logger.setConfigLoggingLevel(loggingLevel);
                this.environment.getSystemProperties().put(CEBASProperty.LOGGING_LEVEL.name(), loggingLevel);
                return;
            }
            String loggerName = stringArray[n2];
            loggerContext.getLogger(loggerName).setLevel(Logger.getLogbackMappedLogLevel((String)loggingLevel));
            ++n2;
        }
    }

    protected void close() {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(2000L);
            }
            catch (InterruptedException e) {
                CEBASException exception = new CEBASException(e.getMessage());
                this.logger.logWithException("000313X", "Task interrupted " + e.getMessage(), exception);
                Thread.currentThread().interrupt();
                throw exception;
            }
            System.exit(0);
        });
    }

    protected void checkForBackup() {
        if (this.cebasRecoveryOptional == null) return;
        if (!this.cebasRecoveryOptional.isPresent()) return;
        this.cebasRecoveryOptional.get().doBackup();
    }
}
