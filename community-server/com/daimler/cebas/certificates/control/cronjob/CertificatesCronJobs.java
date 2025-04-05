/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.cronjob.CertificatesCleanUp
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.control.repository.CertificateRepository
 *  com.daimler.cebas.common.control.CEBASProperty
 *  com.daimler.cebas.common.control.RequestMetadata
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.configuration.control.CeBASGeneralPropertiesManager
 *  com.daimler.cebas.configuration.entity.Configuration
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  com.daimler.cebas.users.entity.User
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Qualifier
 *  org.springframework.scheduling.TaskScheduler
 *  org.springframework.scheduling.Trigger
 *  org.springframework.scheduling.support.CronTrigger
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.StringUtils
 */
package com.daimler.cebas.certificates.control.cronjob;

import com.daimler.cebas.certificates.control.cronjob.CertificatesCleanUp;
import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.control.repository.CertificateRepository;
import com.daimler.cebas.common.control.CEBASProperty;
import com.daimler.cebas.common.control.RequestMetadata;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.configuration.control.CeBASGeneralPropertiesManager;
import com.daimler.cebas.configuration.entity.Configuration;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import com.daimler.cebas.users.entity.User;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@CEBASControl
public class CertificatesCronJobs {
    private static final String CLASS_NAME = CertificatesCronJobs.class.getSimpleName();
    private Logger logger;
    private CertificateRepository certificateRepository;
    private CeBASGeneralPropertiesManager generalPropertiesManager;
    @Autowired
    @Qualifier(value="taskScheduler")
    private TaskScheduler taskScheduler;
    private Session session;
    private ScheduledFuture<?> schedule;
    private final CertificatesCleanUp cleanUpCertificates;
    private RequestMetadata i18n;

    @Autowired
    public CertificatesCronJobs(CertificateRepository certificateRepository, Logger logger, Session session, CertificatesCleanUp cleanUpCertificates, RequestMetadata i18n, @Qualifier(value="General") CeBASGeneralPropertiesManager generalPropertiesManager) {
        this.certificateRepository = certificateRepository;
        this.logger = logger;
        this.session = session;
        this.cleanUpCertificates = cleanUpCertificates;
        this.i18n = i18n;
        this.generalPropertiesManager = generalPropertiesManager;
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public void scheduleDeleteExpiredCertificatesForAllUsers() {
        String METHOD_NAME = "scheduleDeleteExpiredCertificatesForAllUsers";
        this.logger.entering(CLASS_NAME, "scheduleDeleteExpiredCertificatesForAllUsers");
        User defaultUser = this.session.getDefaultUser();
        String deleteCertsSchedule = this.getDeleteExpiredCertsSchedule(defaultUser);
        if (this.schedule != null) {
            this.schedule.cancel(true);
        }
        CronTrigger cronTrigger = new CronTrigger(deleteCertsSchedule);
        Runnable task = () -> ((CertificatesCleanUp)this.cleanUpCertificates).executeCronJobTask();
        this.schedule = this.taskScheduler.schedule(task, (Trigger)cronTrigger);
        this.logger.log(Level.INFO, "000157", "Current automatic delete Cron Job set to: " + cronTrigger.getExpression(), CLASS_NAME);
        this.logger.exiting(CLASS_NAME, "scheduleDeleteExpiredCertificatesForAllUsers");
    }

    private String getDeleteExpiredCertsSchedule(User defaultUser) {
        String deleteCertsScheduleGeneralProperties = this.generalPropertiesManager.getProperty(CEBASProperty.DELETE_CERTS_SCHEDULE.name());
        if (!StringUtils.isEmpty((Object)deleteCertsScheduleGeneralProperties)) return deleteCertsScheduleGeneralProperties;
        Optional<Configuration> deleteExpiredCertScheduleConfig = defaultUser.getConfigurations().stream().filter(config -> config.getConfigKey().equals(CEBASProperty.DELETE_CERTS_SCHEDULE.name())).findFirst();
        Configuration deleteExpiredCertsSchedule = deleteExpiredCertScheduleConfig.orElseThrow(() -> new CEBASCertificateException("000001X, " + this.i18n.getMessage("configNotFoundForDeleting")));
        return deleteExpiredCertsSchedule.getConfigValue();
    }
}
