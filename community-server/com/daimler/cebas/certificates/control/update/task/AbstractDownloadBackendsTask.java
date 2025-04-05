/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.ImportCertificatesEngine
 *  com.daimler.cebas.certificates.control.config.CertificatesConfiguration
 *  com.daimler.cebas.certificates.control.update.DefaultUpdateSession
 *  com.daimler.cebas.certificates.control.update.UpdateSteps
 *  com.daimler.cebas.certificates.control.update.UpdateTask
 *  com.daimler.cebas.certificates.control.update.UpdateType
 *  com.daimler.cebas.certificates.control.vo.DownloadBackendsResult
 *  com.daimler.cebas.certificates.control.vo.ImportResult
 *  com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi
 *  com.daimler.cebas.certificates.integration.vo.BackendIdentifier
 *  com.daimler.cebas.certificates.integration.vo.Permission
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.logs.control.Logger
 *  org.springframework.util.CollectionUtils
 */
package com.daimler.cebas.certificates.control.update.task;

import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
import com.daimler.cebas.certificates.control.update.UpdateSteps;
import com.daimler.cebas.certificates.control.update.UpdateTask;
import com.daimler.cebas.certificates.control.update.UpdateType;
import com.daimler.cebas.certificates.control.vo.DownloadBackendsResult;
import com.daimler.cebas.certificates.control.vo.ImportResult;
import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
import com.daimler.cebas.certificates.integration.vo.BackendIdentifier;
import com.daimler.cebas.certificates.integration.vo.Permission;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.logs.control.Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

public abstract class AbstractDownloadBackendsTask
extends UpdateTask<PublicKeyInfrastructureEsi> {
    private String className;
    private CertificatesConfiguration profileConfiguration;

    public AbstractDownloadBackendsTask(PublicKeyInfrastructureEsi publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, DefaultUpdateSession updateSession, Logger logger, MetadataManager i18n, String className, CertificatesConfiguration profileConfiguration) {
        super(publicKeyInfrastructureEsi, importCertificatesEngine, updateSession, logger, i18n);
        this.className = className;
        this.profileConfiguration = profileConfiguration;
    }

    public DownloadBackendsResult execute(UpdateType updateType, boolean useV2, List<Permission> permissions) {
        List<BackendIdentifier> validBackendIdentifiers;
        if (this.updateSession.isNotRunning()) {
            return new DownloadBackendsResult();
        }
        this.updateSession.updateStep(UpdateSteps.RETRIEVE_BACKEND_IDENTIFIERS, "updateRequestBackendIdentifiers", updateType);
        this.logBeforeRetrieveBackendIdentifiers(updateType);
        List<BackendIdentifier> rootAndBackendIdentifiers = new ArrayList<BackendIdentifier>();
        rootAndBackendIdentifiers.addAll(this.publicKeyInfrastructureEsi.getBackendIdentifiers());
        rootAndBackendIdentifiers.addAll(this.downloadPreactiveIdentifiers(updateType, permissions));
        this.updateSession.updateStep(UpdateSteps.RETRIEVE_BACKEND_IDENTIFIERS, "updateReceiveBackendIdentifiers", updateType);
        this.logAfterRetrievalOfBackendIdentifiers(rootAndBackendIdentifiers, updateType);
        this.updateSession.updateStep(UpdateSteps.RETRIEVE_BACKENDS, "updateRequestingBackendCertChain", updateType);
        if (useV2) {
            List<String> rootSKIs = this.getRootSKIsFromIdentifiers(rootAndBackendIdentifiers);
            Map<Boolean, List<BackendIdentifier>> backendIdentifiers = this.getBackendIdentifiers(rootAndBackendIdentifiers, rootSKIs);
            rootAndBackendIdentifiers = this.excludeInvalidBackendIdentifiers(updateType, backendIdentifiers.get(Boolean.FALSE), rootAndBackendIdentifiers);
            validBackendIdentifiers = backendIdentifiers.get(Boolean.TRUE);
        } else {
            validBackendIdentifiers = rootAndBackendIdentifiers;
        }
        Set base64Certificates = this.publicKeyInfrastructureEsi.getCertificatesChain(validBackendIdentifiers);
        this.logAfterBackendsDownload(base64Certificates, updateType);
        List importResults = this.importCertificatesEngine.importCertificatesFromBase64NewTransaction(new ArrayList(base64Certificates), true, this.allowImportOfPrivateKeys());
        this.logAfterBackendsImport(importResults, updateType);
        this.profileConfiguration.getPkiKnownHandler().updatePkiKnownForAllUnknownBackends(importResults);
        this.updateSession.updateStep(UpdateSteps.RETRIEVE_BACKENDS, "updateDownloadRootBackendStop", updateType);
        this.updateSession.addStepResult(UpdateSteps.RETRIEVE_BACKEND_IDENTIFIERS, (Object)importResults);
        this.updateSession.resetRetries();
        return new DownloadBackendsResult(importResults, rootAndBackendIdentifiers);
    }

    public List<BackendIdentifier> downloadPreactiveIdentifiers(List<Permission> permissions) {
        return this.downloadPreactiveIdentifiers(null, permissions);
    }

    private List<BackendIdentifier> downloadPreactiveIdentifiers(UpdateType updateType, List<Permission> permissions) {
        if (CollectionUtils.isEmpty(permissions)) {
            return new ArrayList<BackendIdentifier>();
        }
        ArrayList<BackendIdentifier> preactiveIdentifiers = new ArrayList<BackendIdentifier>();
        Set preactiveCaIds = permissions.stream().filter(permission -> !CollectionUtils.isEmpty((Collection)permission.getValidCAs())).map(Permission::getValidCAs).flatMap(Collection::stream).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
        Iterator iterator = preactiveCaIds.iterator();
        while (iterator.hasNext()) {
            List identifiers;
            String preactiveCaId = (String)iterator.next();
            if (this.updateSession.isRunning()) {
                identifiers = this.publicKeyInfrastructureEsi.getPreactiveBackendIdentifiers(true, preactiveCaId);
                if (CollectionUtils.isEmpty((Collection)identifiers)) {
                    this.logger.log(Level.INFO, "000550", this.i18n.getEnglishMessage("updateNotDownloadedPreactiveCa", new String[]{updateType.name(), preactiveCaId}), this.className);
                    continue;
                }
                preactiveIdentifiers.addAll(identifiers);
                continue;
            }
            identifiers = this.publicKeyInfrastructureEsi.getPreactiveBackendIdentifiers(false, preactiveCaId);
            if (CollectionUtils.isEmpty((Collection)identifiers)) continue;
            preactiveIdentifiers.addAll(identifiers);
        }
        return preactiveIdentifiers;
    }

    private List<String> getRootSKIsFromIdentifiers(List<BackendIdentifier> rootAndBackendIdentifiers) {
        return rootAndBackendIdentifiers.stream().filter(identifier -> "ROOT_CA".equalsIgnoreCase(identifier.getType())).map(BackendIdentifier::getSubjectKeyIdentifier).collect(Collectors.toList());
    }

    private Map<Boolean, List<BackendIdentifier>> getBackendIdentifiers(List<BackendIdentifier> rootAndBackendIdentifiers, List<String> rootSKIs) {
        return rootAndBackendIdentifiers.stream().filter(identifier -> "SUB_CA".equalsIgnoreCase(identifier.getType())).collect(Collectors.partitioningBy(identifier -> rootSKIs.contains(identifier.getAuthorityKeyIdentifier())));
    }

    protected abstract boolean allowImportOfPrivateKeys();

    private void logBeforeRetrieveBackendIdentifiers(UpdateType updateType) {
        this.logger.log(Level.FINER, "000241", this.i18n.getEnglishMessage("updateDownloadRootBackendStart", new String[]{updateType.name()}), this.className);
        this.logger.log(Level.FINER, "000242", this.i18n.getEnglishMessage("updateRequestBackendIdentifiers", new String[]{updateType.name()}), this.className);
    }

    private void logAfterRetrievalOfBackendIdentifiers(List<BackendIdentifier> backendIdentifiers, UpdateType updateType) {
        this.logger.log(Level.FINER, "000243", this.i18n.getEnglishMessage("updateReceiveBackendIdentifiers", new String[]{updateType.name()}), this.className);
        this.logger.log(Level.FINE, "000223", this.i18n.getEnglishMessage("updateReceivedBackendIdentifiers", new String[]{updateType.name(), backendIdentifiers.toString()}), this.className);
        this.logger.log(Level.FINER, "000244", this.i18n.getEnglishMessage("updateRequestingBackendCertChain", new String[]{updateType.name()}), this.className);
    }

    private List<BackendIdentifier> excludeInvalidBackendIdentifiers(UpdateType updateType, List<BackendIdentifier> invalidBackendIdentifiers, List<BackendIdentifier> rootAndBackendIdentifiers) {
        if (CollectionUtils.isEmpty(invalidBackendIdentifiers)) return rootAndBackendIdentifiers;
        invalidBackendIdentifiers.forEach(identifier -> this.logger.log(Level.INFO, "000550", this.i18n.getEnglishMessage("updateInvalidBackendIdentifiers", new String[]{updateType.name(), identifier.getAuthorityKeyIdentifier(), identifier.getSubjectKeyIdentifier()}), this.className));
        rootAndBackendIdentifiers = rootAndBackendIdentifiers.stream().filter(identifier -> !invalidBackendIdentifiers.contains(identifier)).collect(Collectors.toList());
        return rootAndBackendIdentifiers;
    }

    private void logAfterBackendsDownload(Set<String> base64Certificates, UpdateType updateType) {
        this.logger.log(Level.FINER, "000245", this.i18n.getEnglishMessage("updateReceivingBackendCertChain", new String[]{updateType.name()}), this.className);
        this.logger.log(Level.FINER, "000224", this.i18n.getEnglishMessage("updateReceivedBackendCertChain", new String[]{updateType.name(), base64Certificates.toString()}), this.className);
        if (!base64Certificates.isEmpty()) return;
        this.logger.log(Level.WARNING, "000550", this.i18n.getEnglishMessage("updateProcessCannotUpdate"), this.className);
    }

    private void logAfterBackendsImport(List<? extends ImportResult> importResults, UpdateType updateType) {
        if (importResults.isEmpty()) {
            this.logger.log(Level.INFO, "000225", updateType + " Update - Root and Backends, No certificates returned by PKI", this.className);
        }
        importResults.stream().filter(result -> !result.isSuccess()).forEach(result -> this.logger.log(Level.WARNING, "000225", updateType + " Update - Root and Backends, PKI  certificate with AKI: " + result.getAuthorityKeyIdentifier() + " and SKI: " + result.getSubjectKeyIdentifier() + " could have not been imported: " + result.getMessage(), this.className));
        this.logger.log(Level.FINER, "000246", this.i18n.getEnglishMessage("updateDownloadRootBackendStop", new String[]{updateType.name()}), this.className);
    }
}
