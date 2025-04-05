/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.CertificateToolsProvider
 *  com.daimler.cebas.certificates.control.CertificateUtil
 *  com.daimler.cebas.certificates.control.config.CertificatesConfiguration
 *  com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory
 *  com.daimler.cebas.certificates.control.update.DefaultUpdateSession
 *  com.daimler.cebas.certificates.control.update.UpdateSteps
 *  com.daimler.cebas.certificates.control.update.UpdateType
 *  com.daimler.cebas.certificates.control.update.task.CreateCSRsTask
 *  com.daimler.cebas.certificates.control.validation.failure.CSRValidationFailureOutput
 *  com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput
 *  com.daimler.cebas.certificates.control.validation.failure.ValidationState
 *  com.daimler.cebas.certificates.control.vo.CertificateSignRequest
 *  com.daimler.cebas.certificates.control.vo.ImportResult
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.entity.parsing.CertificateParser
 *  com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi
 *  com.daimler.cebas.certificates.integration.vo.EnhancedRightsPermission
 *  com.daimler.cebas.certificates.integration.vo.PKIEnhancedCertificateRequest
 *  com.daimler.cebas.certificates.integration.vo.Permission
 *  com.daimler.cebas.common.ObjectIdentifier
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.StringUtils
 */
package com.daimler.cebas.certificates.control.update.task;

import com.daimler.cebas.certificates.control.CertificateToolsProvider;
import com.daimler.cebas.certificates.control.CertificateUtil;
import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
import com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory;
import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
import com.daimler.cebas.certificates.control.update.UpdateSteps;
import com.daimler.cebas.certificates.control.update.UpdateType;
import com.daimler.cebas.certificates.control.update.task.CreateCSRsTask;
import com.daimler.cebas.certificates.control.validation.failure.CSRValidationFailureOutput;
import com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput;
import com.daimler.cebas.certificates.control.validation.failure.ValidationState;
import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
import com.daimler.cebas.certificates.control.vo.ImportResult;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
import com.daimler.cebas.certificates.integration.vo.EnhancedRightsPermission;
import com.daimler.cebas.certificates.integration.vo.PKIEnhancedCertificateRequest;
import com.daimler.cebas.certificates.integration.vo.Permission;
import com.daimler.cebas.common.ObjectIdentifier;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@CEBASControl
public abstract class CreateEnhancedCSRsTask<T extends PublicKeyInfrastructureEsi>
extends CreateCSRsTask<T> {
    private static final String CLASS_NAME = CreateEnhancedCSRsTask.class.getSimpleName();
    private AbstractCertificateFactory factory;
    protected CertificatesConfiguration profileConfiguration;

    @Autowired
    public CreateEnhancedCSRsTask(CertificateToolsProvider toolsProvider, T publicKeyInfrastructureEsi, Session session, DefaultUpdateSession updateSession, Logger logger, CertificatesConfiguration profileConfiguration) {
        super(toolsProvider, publicKeyInfrastructureEsi, toolsProvider.getSearchEngine(), session, updateSession, logger);
        this.certificateSignRequestEngine = toolsProvider.getCertificateSignRequestEngine();
        this.deleteCertificatesEngine = toolsProvider.getDeleteCertificatesEngine();
        this.session = session;
        this.factory = toolsProvider.getFactory();
        this.profileConfiguration = profileConfiguration;
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public List<PKIEnhancedCertificateRequest> execute(List<? extends ImportResult> importResults, List<Permission> permissions, UpdateType updateType) {
        if (!this.updateSession.isRunning()) return Collections.emptyList();
        this.updateSession.updateStep(UpdateSteps.CREATE_ENHANCED_CSRS, "updateStartCreateEnhancedCSR", updateType);
        this.logger.log(Level.INFO, "000261", this.i18n.getEnglishMessage("updateStartCreateEnhancedCSR", new String[]{updateType.name()}), CLASS_NAME);
        List<String> bSKIs = importResults.stream().filter(result -> !StringUtils.isEmpty((Object)result.getAuthorityKeyIdentifier())).map(ImportResult::getSubjectKeyIdentifier).collect(Collectors.toList());
        List<PKIEnhancedCertificateRequest> enhCertificateRequests = this.getEnhancedRightsRequests(bSKIs, permissions, updateType);
        this.logger.log(Level.INFO, "000262", this.i18n.getEnglishMessage("updateStopCreateEnhancedCSR", new String[]{updateType.name()}), CLASS_NAME);
        this.updateSession.updateStep(UpdateSteps.CREATE_ENHANCED_CSRS, "updateStopCreateEnhancedCSR", updateType);
        this.updateSession.addStepResult(UpdateSteps.CREATE_ENHANCED_CSRS, enhCertificateRequests);
        this.updateSession.getPkiCertificateRequests().addAll(enhCertificateRequests);
        return enhCertificateRequests;
    }

    private List<PKIEnhancedCertificateRequest> getEnhancedRightsRequests(List<String> bSKIs, List<Permission> permissions, UpdateType updateType) {
        ArrayList<PKIEnhancedCertificateRequest> enhCertificateRequests = new ArrayList<PKIEnhancedCertificateRequest>();
        permissions.stream().filter(permission -> permission.getServices() != null).forEach(permission -> this.searchEngine.findDiagnosticCertificatesByBackendSKIs(this.session.getCurrentUser(), bSKIs, this.getUserRoleFromPermission(permission.getUserRole()), CertificateUtil.getSortedCommaSeparatedList((List)permission.getTargetECU()), CertificateUtil.getSortedCommaSeparatedList((List)permission.getTargetVIN())).stream().filter(diag -> this.profileConfiguration.getPkiKnownHandler().isPKIKnown(diag.getParent())).forEach(diag -> enhCertificateRequests.addAll(this.processPermissionsUnderDiag((Permission)permission, (Certificate)diag, updateType))));
        return enhCertificateRequests;
    }

    protected abstract List<PKIEnhancedCertificateRequest> processPermissionsUnderDiag(Permission var1, Certificate var2, UpdateType var3);

    protected PKIEnhancedCertificateRequest createCSRUnderDiag(Permission permission, Certificate diag, List<Certificate> certificatesUnderDiag, EnhancedRightsPermission enhPermission, boolean checkPermission, UpdateType updateType, List<String> targetECUs, List<String> targetVINs) {
        String userRole = null;
        String nonce = null;
        String uniqueECUID = null;
        String specialECU = null;
        String targetSubjKeyIdentifier = null;
        String ecu = CertificateUtil.getCommaSeparatedStringFromList(targetECUs);
        String vin = CertificateUtil.getCommaSeparatedStringFromList(targetVINs);
        String services = CertificateUtil.getServicesFromEnhRightsPermission((List)enhPermission.getServiceIds());
        CertificateSignRequest certificateSignRequest = new CertificateSignRequest("CN=" + this.session.getCurrentUser().getUserName(), CertificateType.ENHANCED_RIGHTS_CERTIFICATE.name(), userRole, ecu, vin, nonce, services, uniqueECUID, specialECU, targetSubjKeyIdentifier, CertificateParser.hexToBase64((String)diag.getAuthorityKeyIdentifier()), (Date)permission.getValidity(), diag.getEntityId(), this.session.getCurrentUser().getEntityId(), ObjectIdentifier.ALGORITHM_IDENTIFIER_OID.getOid(), "0.1", this.getProdQualifier(), "");
        Certificate createdCSR = this.certificateSignRequestEngine.createCertificateInSignRequestState(certificateSignRequest, checkPermission, (ValidationFailureOutput)new CSRValidationFailureOutput((ValidationState)enhPermission, (ValidationState)certificateSignRequest, this.logger), true);
        if (!enhPermission.isValid()) {
            return null;
        }
        if (enhPermission.getEnrollmentId() == null) {
            this.logger.log(Level.SEVERE, "000299X", "enrollmentId field is missing from Enhanced Rights permission with ecu: [" + ecu + "], vin: [" + vin + "] and services: [" + services + "]", CLASS_NAME);
        }
        if (null != updateType) {
            this.logger.log(Level.FINE, "000232", this.i18n.getEnglishMessage("updateCreatedCSRWithAKIAndPKIRole", new String[]{updateType.name(), createdCSR.getAuthorityKeyIdentifier(), createdCSR.getPKIRole()}), CLASS_NAME);
            if (UpdateType.FULL.equals((Object)updateType)) {
                this.deleteNonIdenticalCertificates(certificatesUnderDiag, createdCSR, updateType);
            }
        }
        PKIEnhancedCertificateRequest pkiEnhancedCertificateRequest = new PKIEnhancedCertificateRequest(enhPermission.getEnrollmentId(), createdCSR.getPkcs10Signature(), diag.getParent().getSubjectKeyIdentifier().replace("-", "").toLowerCase(), createdCSR.getType().name(), Base64.getEncoder().encodeToString(this.factory.getCertificateBytes(diag)));
        pkiEnhancedCertificateRequest.setInternalCSRId(createdCSR.getEntityId());
        return pkiEnhancedCertificateRequest;
    }
}
