/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.DeleteCertificatesEngine
 *  com.daimler.cebas.certificates.control.DurationParser
 *  com.daimler.cebas.certificates.control.hooks.DeleteCertificateNonVSMHook
 *  com.daimler.cebas.certificates.control.hooks.HookProviderType
 *  com.daimler.cebas.certificates.control.hooks.ICertificateHooks
 *  com.daimler.cebas.certificates.control.hooks.NoHook
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.common.control.CEBASProperty
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.common.entity.AbstractEntity
 *  com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException
 *  com.daimler.cebas.configuration.entity.Configuration
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  com.daimler.cebas.users.control.UserRepository
 *  com.daimler.cebas.users.control.factories.UserFactory
 *  com.daimler.cebas.users.entity.User
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.daimler.cebas.certificates.control.cronjob;

import com.daimler.cebas.certificates.control.DeleteCertificatesEngine;
import com.daimler.cebas.certificates.control.DurationParser;
import com.daimler.cebas.certificates.control.hooks.DeleteCertificateNonVSMHook;
import com.daimler.cebas.certificates.control.hooks.HookProviderType;
import com.daimler.cebas.certificates.control.hooks.ICertificateHooks;
import com.daimler.cebas.certificates.control.hooks.NoHook;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.common.control.CEBASProperty;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.common.entity.AbstractEntity;
import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
import com.daimler.cebas.configuration.entity.Configuration;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import com.daimler.cebas.users.control.UserRepository;
import com.daimler.cebas.users.control.factories.UserFactory;
import com.daimler.cebas.users.entity.User;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@CEBASControl
public abstract class CertificatesCleanUp {
    private static final String CLASS_NAME = CertificatesCleanUp.class.getSimpleName();
    protected DeleteCertificatesEngine deleteCertificatesEngine;
    private UserRepository userRepository;
    protected Logger logger;
    protected MetadataManager i18n;
    protected Session session;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    public CertificatesCleanUp(DeleteCertificatesEngine deleteCertificatesEngine, UserRepository userRepository, Logger logger, MetadataManager i18n, Session session) {
        this.deleteCertificatesEngine = deleteCertificatesEngine;
        this.userRepository = userRepository;
        this.logger = logger;
        this.i18n = i18n;
        this.session = session;
    }

    public void cleanUpCertificates(User user) {
        String METHOD_NAME = "cleanUpCertificates";
        this.logger.entering(CLASS_NAME, "cleanUpCertificates");
        if (this.isDeleteExpiredCertsSetOnTrue(user)) {
            this.deleteExpiredCertificates(user);
        } else {
            this.logger.log(Level.INFO, "000650", this.i18n.getMessage("vsmCertAutoDeleteSkipped"), CLASS_NAME);
        }
        this.logger.exiting(CLASS_NAME, "cleanUpCertificates");
    }

    protected boolean isDeleteExpiredCertsSetOnTrue(User user) {
        Optional<Configuration> optional = user.getConfigurations().stream().filter(c -> c.getConfigKey().equals(CEBASProperty.DELETE_EXPIRED_CERTS.name())).findFirst();
        Configuration deleteExpiredCertsConfig = optional.orElseThrow(() -> new ZenZefiConfigurationException(this.i18n.getMessage("configurationNotFound", new String[]{CEBASProperty.DELETE_EXPIRED_CERTS.name()})));
        return Boolean.TRUE.equals(Boolean.valueOf(deleteExpiredCertsConfig.getConfigValue()));
    }

    @Transactional
    public void cleanUpCertificatesCurrentUser() {
        User currentUser = this.session.getCurrentUser();
        this.cleanUpCertificates(currentUser);
    }

    private void deleteExpiredCertificates(User user) {
        String METHOD_NAME = "deleteExpiredCertificates";
        this.logger.entering(CLASS_NAME, "deleteExpiredCertificates");
        user = this.userRepository.findUserById(user.getEntityId());
        this.logger.log(Level.INFO, "000004", this.i18n.getEnglishMessage("deletingExpiredCertificatesForUser", new String[]{this.composeMessageForCurrentUser(user)}), CLASS_NAME);
        this.removeExpiredECUCertificates(user);
        List ids = this.deleteCertificatesEngine.getRepository().getExpiredNonECUCertificates(user);
        if (!ids.isEmpty()) {
            List deletedCertificates = this.deleteCertificatesEngine.deleteCertificates(ids, user);
            deletedCertificates.forEach(deleteCertificatesInfo -> this.logger.log(Level.INFO, "000122", this.i18n.getEnglishMessage("deleteExpiredCertificateCronTrigger", new String[]{this.i18n.getEnglishMessage(deleteCertificatesInfo.getCertificateType().getLanguageProperty()), deleteCertificatesInfo.getCertificateId(), deleteCertificatesInfo.getSerialNo(), deleteCertificatesInfo.getSubjectKeyIdentifier(), deleteCertificatesInfo.getAuthKeyIdentifier()}), CLASS_NAME));
        }
        this.userRepository.detach((AbstractEntity)user);
        this.logger.exiting(CLASS_NAME, "deleteExpiredCertificates");
    }

    private void removeExpiredECUCertificates(User user) {
        List certsToRemove = this.deleteCertificatesEngine.getRepository().getExpiredECUCertificates(user);
        this.removeCertificatesOnCronJob(certsToRemove, HookProviderType.ECU_CERTS);
    }

    protected void removeCertificatesOnCronJob(List<Certificate> certsToRemove, HookProviderType hookProviderType) {
        if (certsToRemove.isEmpty()) return;
        this.session.getSystemIntegrityCheckResult().clear();
        DeleteCertificateNonVSMHook hookProvider = HookProviderType.ECU_CERTS.equals((Object)hookProviderType) ? new DeleteCertificateNonVSMHook(this.logger) : new NoHook();
        certsToRemove.forEach(arg_0 -> this.lambda$removeCertificatesOnCronJob$3((ICertificateHooks)hookProvider, arg_0));
        this.deleteCertificatesEngine.getRepository().flush();
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void executeCronJobTask() {
        this.deleteExpiredCertificates();
    }

    protected Date deleteVSMCleanUpDate() {
        LocalDateTime expiredTime = LocalDateTime.now().minus(this.getExpiryPeriod());
        return Date.from(expiredTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    protected Date deleteAdHocCertificatesCleanUpDate() {
        LocalDateTime expiredTime = this.getExpirationPeriodForAdHocCerts().plusTo(LocalDateTime.now());
        return new Date(Timestamp.valueOf(expiredTime).getTime());
    }

    protected abstract void deleteOlderVSMCertificates();

    protected abstract void deleteOlderVariantCodingCertificates();

    protected abstract void deleteOlderDiagnosticCertificates();

    protected abstract Period getExpiryPeriod();

    protected abstract DurationParser getExpirationPeriodForAdHocCerts();

    protected Period getExpiryPeriod(String maxAgeCerts, String defaultMaxAgeCerts, Period maxAgeCertsPeriod) {
        if (maxAgeCertsPeriod != null) return maxAgeCertsPeriod;
        try {
            maxAgeCertsPeriod = Period.parse(maxAgeCerts);
        }
        catch (Exception e) {
            this.logger.logToFileOnly(CLASS_NAME, "Parse exception for certificate max age: " + maxAgeCerts, (Throwable)e);
            this.logger.log(Level.WARNING, "000557X", "The certificate max age param could not be parsed. Please check the configuration. Stack trace can be checked in the log file. The param is set as: " + maxAgeCerts, CLASS_NAME);
            maxAgeCertsPeriod = Period.parse(defaultMaxAgeCerts);
        }
        return maxAgeCertsPeriod;
    }

    private void deleteExpiredCertificates() {
        String METHOD_NAME = "deleteExpiredCertificates";
        this.logger.entering(CLASS_NAME, "deleteExpiredCertificates");
        this.logger.log(Level.INFO, "000001", "System trying to delete expired certificates, time: " + this.dateFormatter.format(new Date()), CLASS_NAME);
        List users = this.userRepository.findUsersWithDeleteExpiredCertsTrue();
        users.forEach(this::deleteExpiredCertificates);
        this.logger.exiting(CLASS_NAME, "deleteExpiredCertificates");
    }

    private String composeMessageForCurrentUser(User user) {
        String currentUserName = user.getUserName().equals(UserFactory.getDefaultUsername()) ? this.i18n.getMessage("defaultUser") : user.getUserName();
        return currentUserName;
    }

    private /* synthetic */ void lambda$removeCertificatesOnCronJob$3(ICertificateHooks hookProvider, Certificate certificate) {
        hookProvider.exec(certificate);
        this.deleteCertificatesEngine.deleteKPForChildren(certificate, this.session.getCurrentUser());
        this.deleteCertificatesEngine.getRepository().deleteManagedEntity((AbstractEntity)certificate);
        this.logger.log(Level.INFO, "000122", this.i18n.getEnglishMessage("deleteExpiredCertificateCronTrigger", new String[]{this.i18n.getEnglishMessage(certificate.getType().getLanguageProperty()), certificate.getEntityId(), certificate.getSerialNo(), certificate.getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier()}), CLASS_NAME);
    }
}
