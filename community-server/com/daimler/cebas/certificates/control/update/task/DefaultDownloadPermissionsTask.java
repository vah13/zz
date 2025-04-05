/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.ImportCertificatesEngine
 *  com.daimler.cebas.certificates.control.update.DefaultUpdateSession
 *  com.daimler.cebas.certificates.control.update.UpdateSteps
 *  com.daimler.cebas.certificates.control.update.UpdateTask
 *  com.daimler.cebas.certificates.control.update.UpdateType
 *  com.daimler.cebas.certificates.control.validation.PermissionsValidator
 *  com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi
 *  com.daimler.cebas.certificates.integration.vo.Permission
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.logs.control.Logger
 */
package com.daimler.cebas.certificates.control.update.task;

import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
import com.daimler.cebas.certificates.control.update.UpdateSteps;
import com.daimler.cebas.certificates.control.update.UpdateTask;
import com.daimler.cebas.certificates.control.update.UpdateType;
import com.daimler.cebas.certificates.control.validation.PermissionsValidator;
import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
import com.daimler.cebas.certificates.integration.vo.Permission;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.logs.control.Logger;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

@CEBASControl
public abstract class DefaultDownloadPermissionsTask<T extends PublicKeyInfrastructureEsi>
extends UpdateTask<T> {
    private static final String CLASS_NAME = DefaultDownloadPermissionsTask.class.getSimpleName();

    public DefaultDownloadPermissionsTask(T publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, DefaultUpdateSession updateSession, Logger logger, MetadataManager i18n) {
        super(publicKeyInfrastructureEsi, importCertificatesEngine, updateSession, logger, i18n);
    }

    public List<Permission> execute(UpdateType updateType) {
        if (!this.updateSession.isRunning()) return Collections.emptyList();
        this.updateSession.updateStep(UpdateSteps.RETRIEVE_PERMISSIONS, "updateStartDownloadUserPermissions", updateType);
        this.logBeforeRetrievePermissions(updateType);
        List permissions = this.publicKeyInfrastructureEsi.getPermissions();
        this.logAfterRetrievePermissions(updateType, permissions);
        this.updateSession.updateStep(UpdateSteps.RETRIEVE_PERMISSIONS, "updateStopDownloadUserPermissions", updateType);
        this.updateSession.addStepResult(UpdateSteps.RETRIEVE_PERMISSIONS, (Object)permissions);
        this.updateSession.resetRetries();
        return permissions.stream().filter(permission -> PermissionsValidator.validate((Permission)permission, (Logger)this.logger, (MetadataManager)this.i18n)).collect(Collectors.toList());
    }

    private void logAfterRetrievePermissions(UpdateType updateType, List<Permission> permissions) {
        this.logger.log(Level.INFO, "000237", this.i18n.getEnglishMessage("updateReceivingUserPermissions", new String[]{updateType.name()}), CLASS_NAME);
        this.logger.log(Level.INFO, "000226", this.i18n.getEnglishMessage("updateReceivedUserPermissions", new String[]{updateType.name(), this.createPermissionsLoggingString(permissions)}), CLASS_NAME);
        this.logger.log(Level.INFO, "000238", this.i18n.getEnglishMessage("updateStopDownloadUserPermissions", new String[]{updateType.name()}), CLASS_NAME);
    }

    private void logBeforeRetrievePermissions(UpdateType updateType) {
        this.logger.log(Level.INFO, "000235", this.i18n.getEnglishMessage("updateStartDownloadUserPermissions", new String[]{updateType.name()}), CLASS_NAME);
        this.logger.log(Level.INFO, "000236", this.i18n.getEnglishMessage("updateRequestingUserPermissions", new String[]{updateType.name()}), CLASS_NAME);
    }

    private String createPermissionsLoggingString(List<Permission> permissions) {
        return permissions.isEmpty() ? "[]" : permissions.stream().map(Permission::toString).collect(Collectors.joining(","));
    }
}
