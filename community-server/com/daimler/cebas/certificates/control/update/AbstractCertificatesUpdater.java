/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.EmptyPermissionsException
 *  com.daimler.cebas.certificates.control.exceptions.InvalidTokenException
 *  com.daimler.cebas.certificates.control.update.DefaultUpdateSession
 *  com.daimler.cebas.certificates.control.update.UpdateType
 *  com.daimler.cebas.certificates.control.update.task.AbstractDownloadBackendsTask
 *  com.daimler.cebas.certificates.control.update.task.UpdateRootAndBackendsResult
 *  com.daimler.cebas.certificates.control.vo.DownloadBackendsResult
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.integration.vo.Permission
 *  com.daimler.cebas.common.control.ApplicationInvalidState
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.logs.control.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.context.ApplicationEvent
 *  org.springframework.context.ApplicationEventPublisher
 */
package com.daimler.cebas.certificates.control.update;

import com.daimler.cebas.certificates.control.exceptions.EmptyPermissionsException;
import com.daimler.cebas.certificates.control.exceptions.InvalidTokenException;
import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
import com.daimler.cebas.certificates.control.update.UpdateType;
import com.daimler.cebas.certificates.control.update.task.AbstractDownloadBackendsTask;
import com.daimler.cebas.certificates.control.update.task.UpdateRootAndBackendsResult;
import com.daimler.cebas.certificates.control.vo.DownloadBackendsResult;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.integration.vo.Permission;
import com.daimler.cebas.common.control.ApplicationInvalidState;
import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.logs.control.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

public abstract class AbstractCertificatesUpdater {
    protected Logger logger;
    protected MetadataManager i18n;
    protected DefaultUpdateSession updateSession;
    protected UpdateType updateType;
    protected AbstractDownloadBackendsTask downloadBackendsTask;
    @Autowired
    protected ApplicationEventPublisher applicationEventPublisher;
    @Value(value="${pki.base.url}")
    private String pkiBaseUrl;

    public AbstractCertificatesUpdater(Logger logger, MetadataManager i18n, DefaultUpdateSession updateSession, AbstractDownloadBackendsTask downloadBackendsTask) {
        this.logger = logger;
        this.i18n = i18n;
        this.updateSession = updateSession;
        this.downloadBackendsTask = downloadBackendsTask;
    }

    public abstract void updateCertificates();

    protected void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }

    protected DownloadBackendsResult addDownloadBackendsErrorState(Throwable e) {
        String errorMessage = e.getMessage().replaceAll("[\r\n]+", "");
        if (errorMessage.contains("Invalid token")) {
            this.invalidTokenErrorCase(errorMessage);
            errorMessage = this.handleInvalidToken();
        } else {
            this.generalErrorCase(this.i18n.getMessage("certificatesDownloadBackendsError", new String[]{errorMessage}));
            errorMessage = this.i18n.getEnglishMessage("certificatesDownloadBackendsError", new String[]{errorMessage});
        }
        if (this.shouldLogStacktrace(e)) {
            this.logger.logToFileOnly(this.getClass().getSimpleName(), errorMessage, e);
        }
        this.logger.log(Level.SEVERE, "000462X", errorMessage, this.getClass().getSimpleName());
        return new DownloadBackendsResult();
    }

    protected List<Permission> addPermissionsErrorState(Throwable e) {
        String errorMessage = e.getMessage();
        if (errorMessage.contains("Invalid token")) {
            this.invalidTokenErrorCase(errorMessage);
            errorMessage = this.handleInvalidToken();
        } else {
            String message = e.getCause() != null && e.getCause() instanceof EmptyPermissionsException ? this.i18n.getMessage("noPermissionsErrorWithLink", new String[]{this.pkiBaseUrl + "/ui/"}, this.updateSession.getSessionLocale()) : this.i18n.getMessage("permissionsError");
            this.generalErrorCase(message);
        }
        if (this.shouldLogStacktrace(e)) {
            this.logger.logToFileOnly(this.getClass().getSimpleName(), errorMessage, e);
            this.logger.log(Level.SEVERE, "000289X", "Error while downloading permissions, please check the logfile for more information", this.getClass().getSimpleName());
        } else {
            this.logger.log(Level.SEVERE, "000289X", "Error while downloading permissions - " + errorMessage, this.getClass().getSimpleName());
        }
        return new ArrayList<Permission>();
    }

    protected Void addDownloadCertificatesErrorStateForEcu(Throwable e) {
        this.updateSession.resetEcuCondition(Boolean.valueOf(true));
        return this.addDownloadCertificatesErrorState(e);
    }

    protected Void addDownloadCertificatesErrorStateForLink(Throwable e) {
        this.updateSession.resetLinkCondition(Boolean.valueOf(true));
        return this.addDownloadCertificatesErrorState(e);
    }

    protected Void addDownloadCertificatesErrorState(Throwable e) {
        String errorMessage = e.getMessage();
        if (CEBASException.hasCause((Throwable)e, InvalidTokenException.class)) {
            this.invalidTokenErrorCase(errorMessage);
            errorMessage = this.handleInvalidToken();
        } else {
            this.generalErrorCase(errorMessage);
        }
        if (this.shouldLogStacktrace(e)) {
            this.logger.logToFileOnly(this.getClass().getSimpleName(), errorMessage, e);
        }
        this.logger.log(Level.SEVERE, "000461X", errorMessage, this.getClass().getSimpleName());
        return null;
    }

    protected void invalidTokenErrorCase(String errorMessage) {
        this.updateSession.handleInvalidToken(errorMessage, this.updateType);
    }

    protected String handleInvalidToken() {
        String errorMessage = null == System.getProperty("spring.profiles.active") ? "Update session failed due to invalid token. Authorization Error, user will be switched to offline mode" : "Update session failed due to invalid token. Authorization Error";
        this.generalErrorCase(errorMessage);
        this.applicationEventPublisher.publishEvent((ApplicationEvent)new ApplicationInvalidState((Object)this));
        return errorMessage;
    }

    protected void generalErrorCase(String errorMessage) {
        this.updateSession.updateStep(this.updateSession.getCurrentStep(), errorMessage, this.updateType, true);
        this.updateSession.setNotRunning();
    }

    protected Map<CertificateType, UpdateRootAndBackendsResult> addUpdateBackendsErrorState(Throwable e) {
        String errorMessage = e.getMessage();
        this.generalErrorCase(errorMessage);
        this.logger.log(Level.SEVERE, "000540X", errorMessage, this.getClass().getSimpleName());
        HashMap<CertificateType, UpdateRootAndBackendsResult> emptyResult = new HashMap<CertificateType, UpdateRootAndBackendsResult>();
        UpdateRootAndBackendsResult emptyBResult = new UpdateRootAndBackendsResult();
        emptyResult.put(CertificateType.ECU_CERTIFICATE, emptyBResult);
        emptyResult.put(CertificateType.BACKEND_CA_LINK_CERTIFICATE, emptyBResult);
        return emptyResult;
    }

    private boolean shouldLogStacktrace(Throwable e) {
        if (e == null) {
            return false;
        }
        if (e instanceof EmptyPermissionsException) return false;
        if (e.getCause() != null && e.getCause() instanceof EmptyPermissionsException) {
            return false;
        }
        if (e instanceof InvalidTokenException) return false;
        if (e.getCause() == null) return true;
        if (!(e.getCause() instanceof InvalidTokenException)) return true;
        return false;
    }
}
