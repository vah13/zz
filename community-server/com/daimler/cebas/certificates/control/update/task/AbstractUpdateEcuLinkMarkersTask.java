/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.ImportCertificatesEngine
 *  com.daimler.cebas.certificates.control.SearchEngine
 *  com.daimler.cebas.certificates.control.update.DefaultUpdateSession
 *  com.daimler.cebas.certificates.control.update.UpdateTask
 *  com.daimler.cebas.certificates.control.update.UpdateType
 *  com.daimler.cebas.certificates.control.update.task.UpdateRootAndBackendsResult
 *  com.daimler.cebas.certificates.control.vo.DownloadBackendsResult
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi
 *  com.daimler.cebas.certificates.integration.vo.BackendIdentifier
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.daimler.cebas.certificates.control.update.task;

import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
import com.daimler.cebas.certificates.control.SearchEngine;
import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
import com.daimler.cebas.certificates.control.update.UpdateTask;
import com.daimler.cebas.certificates.control.update.UpdateType;
import com.daimler.cebas.certificates.control.update.task.UpdateRootAndBackendsResult;
import com.daimler.cebas.certificates.control.vo.DownloadBackendsResult;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
import com.daimler.cebas.certificates.integration.vo.BackendIdentifier;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractUpdateEcuLinkMarkersTask<T extends PublicKeyInfrastructureEsi>
extends UpdateTask<T> {
    private Session session;
    private SearchEngine searchEngine;
    private String className;

    public AbstractUpdateEcuLinkMarkersTask(T publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, DefaultUpdateSession updateSession, Logger logger, MetadataManager i18n, Session session, SearchEngine searchEngine, String className) {
        super(publicKeyInfrastructureEsi, importCertificatesEngine, updateSession, logger, i18n);
        this.session = session;
        this.searchEngine = searchEngine;
        this.className = className;
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void execute(Map<CertificateType, UpdateRootAndBackendsResult> resultMap, DownloadBackendsResult backendsResult, UpdateType updateType) {
        if (this.updateSession.ecuResetCondition().booleanValue() && this.updateSession.linkResetCondition().booleanValue()) {
            this.logger.log(Level.INFO, "000542", this.i18n.getEnglishMessage("resetMarkersOnEcuLink", new String[]{updateType.name()}), this.className);
            return;
        }
        List backendPersistentCertificates = this.searchEngine.findCertificatesByType(Certificate.class, this.session.getCurrentUser(), CertificateType.BACKEND_CA_CERTIFICATE);
        List ecuBackendsFromIdentifiers = resultMap.get(CertificateType.ECU_CERTIFICATE).getUpdatedRootAndBackends();
        List rootLinksFromIdentifiers = resultMap.get(CertificateType.ROOT_CA_LINK_CERTIFICATE).getUpdatedRootAndBackends();
        List backendLinksFromIdentifiers = resultMap.get(CertificateType.BACKEND_CA_LINK_CERTIFICATE).getUpdatedRootAndBackends();
        for (Certificate backendCertificate : backendPersistentCertificates) {
            BackendIdentifier matchingCertFromIdentifier;
            Optional matchingBackendIdentifier;
            if (this.needsToBeUpdated(backendCertificate, ecuBackendsFromIdentifiers) && !this.updateSession.ecuResetCondition().booleanValue() && (matchingBackendIdentifier = backendsResult.getBackendIdentifierBasedOnSki(backendCertificate.getSubjectKeyIdentifier())).isPresent()) {
                matchingCertFromIdentifier = (BackendIdentifier)matchingBackendIdentifier.get();
                backendCertificate.setEcuPackageTs(matchingCertFromIdentifier.getEcuPackageTs());
            }
            if (!this.needsToBeUpdated(backendCertificate, backendLinksFromIdentifiers) || this.updateSession.linkResetCondition().booleanValue() || !(matchingBackendIdentifier = backendsResult.getBackendIdentifierBasedOnSki(backendCertificate.getSubjectKeyIdentifier())).isPresent()) continue;
            matchingCertFromIdentifier = (BackendIdentifier)matchingBackendIdentifier.get();
            backendCertificate.setLinkCertTs(matchingCertFromIdentifier.getLinkCertTs());
        }
        List rootPersistentCertificates = this.searchEngine.findCertificatesByType(Certificate.class, this.session.getCurrentUser(), CertificateType.ROOT_CA_CERTIFICATE);
        Iterator iterator = rootPersistentCertificates.iterator();
        while (iterator.hasNext()) {
            Optional matchingRootIdentifier;
            Certificate rootCertificate = (Certificate)iterator.next();
            if (!this.needsToBeUpdated(rootCertificate, rootLinksFromIdentifiers) || this.updateSession.linkResetCondition().booleanValue() || !(matchingRootIdentifier = backendsResult.getBackendIdentifierBasedOnSki(rootCertificate.getSubjectKeyIdentifier())).isPresent()) continue;
            BackendIdentifier matchingCertFromIdentifier = (BackendIdentifier)matchingRootIdentifier.get();
            rootCertificate.setLinkCertTs(matchingCertFromIdentifier.getLinkCertTs());
        }
    }

    private boolean needsToBeUpdated(Certificate backendCertificate, List<Certificate> backendUpdatedCertificatesFromIdentifiers) {
        return backendUpdatedCertificatesFromIdentifiers.stream().anyMatch(bi -> bi.getSubjectKeyIdentifier().equalsIgnoreCase(backendCertificate.getSubjectKeyIdentifier()));
    }
}
