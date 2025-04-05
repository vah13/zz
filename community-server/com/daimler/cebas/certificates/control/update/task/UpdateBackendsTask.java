/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.CertificateToolsProvider
 *  com.daimler.cebas.certificates.control.ImportCertificatesEngine
 *  com.daimler.cebas.certificates.control.SearchEngine
 *  com.daimler.cebas.certificates.control.config.CertificatesConfiguration
 *  com.daimler.cebas.certificates.control.update.DefaultUpdateSession
 *  com.daimler.cebas.certificates.control.update.UpdateSteps
 *  com.daimler.cebas.certificates.control.update.UpdateTask
 *  com.daimler.cebas.certificates.control.update.UpdateType
 *  com.daimler.cebas.certificates.control.update.task.UpdateRootAndBackendsResult
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificatePKIState
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi
 *  com.daimler.cebas.certificates.integration.vo.BackendIdentifier
 *  com.daimler.cebas.common.control.HexUtil
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.daimler.cebas.certificates.control.update.task;

import com.daimler.cebas.certificates.control.CertificateToolsProvider;
import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
import com.daimler.cebas.certificates.control.SearchEngine;
import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
import com.daimler.cebas.certificates.control.update.UpdateSteps;
import com.daimler.cebas.certificates.control.update.UpdateTask;
import com.daimler.cebas.certificates.control.update.UpdateType;
import com.daimler.cebas.certificates.control.update.task.UpdateRootAndBackendsResult;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificatePKIState;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
import com.daimler.cebas.certificates.integration.vo.BackendIdentifier;
import com.daimler.cebas.common.control.HexUtil;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@CEBASControl
public class UpdateBackendsTask<T extends PublicKeyInfrastructureEsi>
extends UpdateTask<T> {
    private static final String CLASS_NAME = UpdateBackendsTask.class.getSimpleName();
    private SearchEngine searchEngine;
    private Session session;
    private CertificatesConfiguration profileConfiguration;

    @Autowired
    public UpdateBackendsTask(CertificateToolsProvider toolsProvider, T publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, DefaultUpdateSession updateSession, Session session, Logger logger, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
        super(publicKeyInfrastructureEsi, importCertificatesEngine, updateSession, logger, i18n);
        this.searchEngine = toolsProvider.getSearchEngine();
        this.session = session;
        this.profileConfiguration = profileConfiguration;
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public Map<CertificateType, UpdateRootAndBackendsResult> execute(UpdateType updateType, List<BackendIdentifier> backendIdentifiers) {
        if (this.updateSession.isNotRunning()) {
            HashMap<CertificateType, UpdateRootAndBackendsResult> emptyResult = new HashMap<CertificateType, UpdateRootAndBackendsResult>();
            UpdateRootAndBackendsResult emptyBResult = new UpdateRootAndBackendsResult();
            emptyResult.put(CertificateType.ECU_CERTIFICATE, emptyBResult);
            emptyResult.put(CertificateType.ROOT_CA_LINK_CERTIFICATE, emptyBResult);
            emptyResult.put(CertificateType.BACKEND_CA_LINK_CERTIFICATE, emptyBResult);
            emptyResult.put(CertificateType.NO_TYPE, emptyBResult);
            return emptyResult;
        }
        this.updateSession.updateStep(UpdateSteps.UPDATE_BACKENDS, "updateStartUpdateBackendIdentifiers", updateType);
        this.logger.log(Level.INFO, "000542", this.i18n.getEnglishMessage("updateStartUpdateBackendIdentifiers", new String[]{updateType.name()}), CLASS_NAME);
        Map<CertificateType, UpdateRootAndBackendsResult> updateBackendsResult = this.updateBackends(updateType, backendIdentifiers);
        this.updateSession.updateStep(UpdateSteps.UPDATE_BACKENDS, "updateStopUpdateBackendIdentifiers", updateType);
        this.updateSession.addStepResult(UpdateSteps.UPDATE_BACKENDS, backendIdentifiers);
        this.logger.log(Level.INFO, "000545", this.i18n.getEnglishMessage("updateStopUpdateBackendIdentifiers", new String[]{updateType.name()}), CLASS_NAME);
        this.updateSession.resetRetries();
        return updateBackendsResult;
    }

    public Map<CertificateType, UpdateRootAndBackendsResult> updateBackends(UpdateType updateType, List<BackendIdentifier> backendIdentifiers) {
        HashMap<CertificateType, UpdateRootAndBackendsResult> resultMap = new HashMap<CertificateType, UpdateRootAndBackendsResult>();
        UpdateRootAndBackendsResult ecuUpdateBackendResults = new UpdateRootAndBackendsResult();
        UpdateRootAndBackendsResult rootLinkUpdateBackendResults = new UpdateRootAndBackendsResult();
        UpdateRootAndBackendsResult backendLinkUpdateBackendResults = new UpdateRootAndBackendsResult();
        UpdateRootAndBackendsResult allBackendResults = new UpdateRootAndBackendsResult();
        Iterator<BackendIdentifier> iterator = backendIdentifiers.iterator();
        block0: while (true) {
            if (!iterator.hasNext()) {
                resultMap.put(CertificateType.ECU_CERTIFICATE, ecuUpdateBackendResults);
                resultMap.put(CertificateType.ROOT_CA_LINK_CERTIFICATE, rootLinkUpdateBackendResults);
                resultMap.put(CertificateType.BACKEND_CA_LINK_CERTIFICATE, backendLinkUpdateBackendResults);
                resultMap.put(CertificateType.NO_TYPE, allBackendResults);
                return resultMap;
            }
            BackendIdentifier bi = iterator.next();
            String asEntityHexString = HexUtil.hexStringToSeparatedHexString((String)bi.getSubjectKeyIdentifier());
            CertificateType certificateType = "SUB_CA".equalsIgnoreCase(bi.getType()) ? CertificateType.BACKEND_CA_CERTIFICATE : CertificateType.ROOT_CA_CERTIFICATE;
            List findCertificatesBySkiAndType = this.searchEngine.findCertificatesBySkiAndType(this.session.getCurrentUser(), asEntityHexString, certificateType, Certificate.class);
            Iterator iterator2 = findCertificatesBySkiAndType.iterator();
            while (true) {
                if (!iterator2.hasNext()) continue block0;
                Certificate certificate = (Certificate)iterator2.next();
                if (CertificateType.BACKEND_CA_CERTIFICATE.equals((Object)certificateType) && !this.profileConfiguration.getPkiKnownHandler().isPKIKnown(certificate)) continue;
                if (!StringUtils.equals(certificate.getPkiState().getValue(), bi.getPkiState())) {
                    certificate.setPkiState(CertificatePKIState.getFromValue((String)bi.getPkiState()));
                }
                if (CertificateType.BACKEND_CA_CERTIFICATE.equals((Object)certificateType)) {
                    this.addBackendResultsForEcuUpdate(updateType, certificate, bi, ecuUpdateBackendResults);
                    this.addBackendResultsForLinkUpdate(updateType, certificate, bi, backendLinkUpdateBackendResults);
                } else {
                    this.addBackendResultsForLinkUpdate(updateType, certificate, bi, rootLinkUpdateBackendResults);
                }
                allBackendResults.addUpdatedBackend(certificate);
            }
            break;
        }
    }

    private void addBackendResultsForEcuUpdate(UpdateType updateType, Certificate certificate, BackendIdentifier bi, UpdateRootAndBackendsResult result) {
        if (StringUtils.equals(certificate.getZkNo(), bi.getZkNo()) && StringUtils.equals(certificate.getEcuPackageTs(), bi.getEcuPackageTs())) {
            this.logger.log(Level.INFO, "000543", this.i18n.getEnglishMessage("updateEcuBackendIdentifiersEquals", new String[]{updateType.name(), certificate.getSubjectKeyIdentifier(), certificate.getZkNo(), certificate.getEcuPackageTs()}), CLASS_NAME);
        } else {
            this.logger.log(Level.INFO, "000544", this.i18n.getEnglishMessage("updateEcuBackendIdentifiersChanged", new String[]{updateType.name(), certificate.getSubjectKeyIdentifier(), certificate.getZkNo(), bi.getZkNo(), certificate.getEcuPackageTs(), bi.getEcuPackageTs()}), CLASS_NAME);
            certificate.setZkNo(bi.getZkNo());
            result.addUpdatedBackend(certificate);
        }
    }

    private void addBackendResultsForLinkUpdate(UpdateType updateType, Certificate certificate, BackendIdentifier bi, UpdateRootAndBackendsResult result) {
        if (StringUtils.equals(certificate.getLinkCertTs(), bi.getLinkCertTs())) {
            this.logger.log(Level.INFO, "000682", this.i18n.getEnglishMessage("updateLinkBackendIdentifiersEquals", new String[]{updateType.name(), certificate.getSubjectKeyIdentifier(), certificate.getLinkCertTs()}), CLASS_NAME);
        } else {
            this.logger.log(Level.INFO, "000683", this.i18n.getEnglishMessage("updateLinkBackendIdentifiersChanged", new String[]{updateType.name(), certificate.getSubjectKeyIdentifier(), certificate.getLinkCertTs(), bi.getLinkCertTs()}), CLASS_NAME);
            result.addUpdatedBackend(certificate);
        }
    }
}
