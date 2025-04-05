/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.CertificateUtil
 *  com.daimler.cebas.certificates.control.CertificatesService
 *  com.daimler.cebas.certificates.control.SearchEngine
 *  com.daimler.cebas.certificates.control.chain.events.CertificatesDeleteEvent
 *  com.daimler.cebas.certificates.control.config.CertificatesConfiguration
 *  com.daimler.cebas.certificates.control.config.handlers.IDeleteCertificateHandler
 *  com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException
 *  com.daimler.cebas.certificates.control.hooks.ICertificateHooks
 *  com.daimler.cebas.certificates.control.hooks.NoHook
 *  com.daimler.cebas.certificates.control.repository.CertificateRepository
 *  com.daimler.cebas.certificates.control.update.UpdateType
 *  com.daimler.cebas.certificates.control.validation.CertificatesProcessValidation
 *  com.daimler.cebas.certificates.control.vo.AbstractDeleteCertificates
 *  com.daimler.cebas.certificates.control.vo.DeleteCertificateModel
 *  com.daimler.cebas.certificates.control.vo.DeleteCertificates
 *  com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo
 *  com.daimler.cebas.certificates.control.vo.ExtendedDeleteCertificatesResult
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.integration.vo.EnhancedRightsPermission
 *  com.daimler.cebas.certificates.integration.vo.Permission
 *  com.daimler.cebas.common.control.HexUtil
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.common.entity.AbstractEntity
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  com.daimler.cebas.users.entity.User
 *  com.daimler.cebas.users.entity.UserKeyPair
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.context.event.EventListener
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.daimler.cebas.certificates.control;

import com.daimler.cebas.certificates.control.CertificateUtil;
import com.daimler.cebas.certificates.control.CertificatesService;
import com.daimler.cebas.certificates.control.SearchEngine;
import com.daimler.cebas.certificates.control.chain.events.CertificatesDeleteEvent;
import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
import com.daimler.cebas.certificates.control.config.handlers.IDeleteCertificateHandler;
import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
import com.daimler.cebas.certificates.control.hooks.ICertificateHooks;
import com.daimler.cebas.certificates.control.hooks.NoHook;
import com.daimler.cebas.certificates.control.repository.CertificateRepository;
import com.daimler.cebas.certificates.control.update.UpdateType;
import com.daimler.cebas.certificates.control.validation.CertificatesProcessValidation;
import com.daimler.cebas.certificates.control.vo.AbstractDeleteCertificates;
import com.daimler.cebas.certificates.control.vo.DeleteCertificateModel;
import com.daimler.cebas.certificates.control.vo.DeleteCertificates;
import com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo;
import com.daimler.cebas.certificates.control.vo.ExtendedDeleteCertificatesResult;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.integration.vo.EnhancedRightsPermission;
import com.daimler.cebas.certificates.integration.vo.Permission;
import com.daimler.cebas.common.control.HexUtil;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.common.entity.AbstractEntity;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import com.daimler.cebas.users.entity.User;
import com.daimler.cebas.users.entity.UserKeyPair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@CEBASControl
public class DeleteCertificatesEngine {
    private static final String CLASS_NAME = DeleteCertificatesEngine.class.getSimpleName();
    private final String METHOD_NAME = "deleteCertificates";
    private static final String SecOcIs = "SecOcIs";
    private Session session;
    private SearchEngine searchEngine;
    private CertificateRepository repository;
    private Logger logger;
    private MetadataManager i18n;
    private CertificatesConfiguration certConfig;

    @Autowired
    public DeleteCertificatesEngine(Session session, SearchEngine searchEngine, CertificateRepository repository, CertificatesConfiguration certConfig, Logger logger, MetadataManager i18n) {
        this.session = session;
        this.searchEngine = searchEngine;
        this.repository = repository;
        this.logger = logger;
        this.i18n = i18n;
        this.certConfig = certConfig;
    }

    public CertificateRepository getRepository() {
        return this.repository;
    }

    public List<ExtendedDeleteCertificatesResult> deleteCertificatesAdditionalLogging(List<String> ids) {
        return this.deleteCertificatesAdditionalLogging(ids, (ICertificateHooks)new NoHook());
    }

    public List<ExtendedDeleteCertificatesResult> deleteCertificatesAdditionalLogging(List<String> ids, ICertificateHooks hookProvider) {
        String METHOD_NAME = "deleteCertificatesAdditionalLogging";
        this.logger.entering(CLASS_NAME, "deleteCertificatesAdditionalLogging");
        IDeleteCertificateHandler delHandler = this.certConfig.getDeleteCertificatesHandler(this, this.logger, this.i18n);
        ArrayList<ExtendedDeleteCertificatesResult> deleteCertificatesByIdResultList = new ArrayList<ExtendedDeleteCertificatesResult>();
        List<DeleteCertificatesInfo> deleteCertificatesInfos = this.deleteCertificates(ids, hookProvider);
        deleteCertificatesInfos.forEach(deleteCertificatesInfo -> {
            if (deleteCertificatesInfo.isCertificate()) {
                deleteCertificatesByIdResultList.add(delHandler.createSuccessDeleteCertificateResult(deleteCertificatesInfo));
            } else {
                deleteCertificatesByIdResultList.add(delHandler.createSuccessDeleteCSRResult(deleteCertificatesInfo));
            }
        });
        List<String> failedCerts = ids.stream().filter(failedCertId -> !deleteCertificatesInfos.stream().map(AbstractDeleteCertificates::getCertificateId).collect(Collectors.toList()).contains(failedCertId)).collect(Collectors.toList());
        failedCerts.forEach(notFoundId -> deleteCertificatesByIdResultList.add(delHandler.createFailDeleteCertificateByIdResult(notFoundId)));
        deleteCertificatesByIdResultList.forEach(deleteResult -> this.logger.log(Level.INFO, "000179", this.i18n.getEnglishMessage(deleteResult.getMessage()), CLASS_NAME));
        this.logger.exiting(CLASS_NAME, "deleteCertificatesAdditionalLogging");
        return deleteCertificatesByIdResultList;
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public List<DeleteCertificatesInfo> deleteCertificates(List<String> ids) {
        this.logger.entering(CLASS_NAME, "deleteCertificates");
        List<DeleteCertificatesInfo> deletedCertificates = this.deleteInternal(ids, false, this.session.getCurrentUser(), (ICertificateHooks)new NoHook());
        this.logger.exiting(CLASS_NAME, "deleteCertificates");
        return deletedCertificates;
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public List<DeleteCertificatesInfo> deleteCertificates(List<String> ids, ICertificateHooks hookProvider) {
        this.logger.entering(CLASS_NAME, "deleteCertificates");
        List<DeleteCertificatesInfo> deletedCertificates = this.deleteInternal(ids, false, this.session.getCurrentUser(), hookProvider);
        this.logger.exiting(CLASS_NAME, "deleteCertificates");
        return deletedCertificates;
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public List<DeleteCertificatesInfo> deleteCertificatesDuringImport(List<String> ids) {
        String METHOD_NAME = "deleteCertificatesDuringImport";
        this.logger.entering(CLASS_NAME, "deleteCertificatesDuringImport");
        List<DeleteCertificatesInfo> deletedCertificates = this.deleteInternal(ids, true, this.session.getCurrentUser(), (ICertificateHooks)new NoHook());
        this.logger.exiting(CLASS_NAME, "deleteCertificatesDuringImport");
        return deletedCertificates;
    }

    public List<DeleteCertificatesInfo> deleteCertificates(List<String> ids, User user) {
        return this.deleteInternal(ids, false, user, (ICertificateHooks)new NoHook());
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public List<DeleteCertificatesInfo> deleteCertificatesDifferentTransaction(List<String> ids) {
        return this.deleteCertificates(ids);
    }

    private List<DeleteCertificatesInfo> deleteInternal(List<String> ids, boolean duringUpdateSession, User user, ICertificateHooks hookProvider) {
        CertificatesProcessValidation.validateDeleteCertificates(ids, (MetadataManager)this.i18n, (Logger)this.logger);
        this.session.getSystemIntegrityCheckResult().clear();
        HashMap<String, List<String>> param = new HashMap<String, List<String>>();
        param.put("inclList", ids);
        List certificates = this.repository.findWithNamedQuery("IN_LIST_CERTIFICATE", param, -1);
        if (certificates.isEmpty()) {
            this.logger.log(Level.INFO, "000466", this.i18n.getMessage("deleteCertificatesCalledResultNotFound"), this.getClass().getSimpleName());
            throw new CertificateNotFoundException(this.i18n.getMessage("deleteCertificatesCalledResultNotFound"));
        }
        ArrayList roots = new ArrayList();
        ArrayList<DeleteCertificatesInfo> deleteCertificatesInfo = new ArrayList<DeleteCertificatesInfo>();
        IDeleteCertificateHandler delHandler = this.certConfig.getDeleteCertificatesHandler(this, this.logger, this.i18n);
        certificates.forEach(certificate -> delHandler.deleteCertificate(ids, duringUpdateSession, user, deleteCertificatesInfo, roots, certificate));
        hookProvider.possibleHook().ifPresent(certificates::forEach);
        roots.forEach(root -> this.repository.deleteManagedEntity((AbstractEntity)root));
        return deleteCertificatesInfo;
    }

    public void deleteCertificateForUser(Certificate certificate, User currentUser) {
        this.deleteKPForChildren(certificate, currentUser);
        if (certificate.getParent() != null) {
            certificate.getParent().getChildren().remove(certificate);
            certificate.getChildren().clear();
            this.repository.update((AbstractEntity)certificate.getParent());
            this.repository.flush();
            this.repository.delete(Certificate.class, certificate.getEntityId());
        } else {
            currentUser.getCertificates().remove(certificate);
        }
    }

    public List<ExtendedDeleteCertificatesResult> deleteCertificates(DeleteCertificates deleteCertificate, List<Certificate> currentUserCertificates) {
        return this.deleteCertificates(deleteCertificate, currentUserCertificates, (ICertificateHooks)new NoHook());
    }

    public List<ExtendedDeleteCertificatesResult> deleteCertificates(DeleteCertificates deleteCertificate, List<? extends Certificate> currentUserCertificates, ICertificateHooks hookProvider) {
        List<Object> deletedCertificatesList = new ArrayList();
        IDeleteCertificateHandler delHandler = this.certConfig.getDeleteCertificatesHandler(this, this.logger, this.i18n);
        CertificatesProcessValidation.validateDeleteCertificate((DeleteCertificates)deleteCertificate, (MetadataManager)this.i18n, (Logger)this.logger);
        if (deleteCertificate.isAll()) {
            List<String> ids = currentUserCertificates.stream().map(Certificate::getEntityId).collect(Collectors.toList());
            if (ids.isEmpty()) {
                this.logger.log(Level.INFO, "000467", this.i18n.getMessage("deleteCertificatesCalledResultNotFound"), this.getClass().getSimpleName());
                throw new CertificateNotFoundException(this.i18n.getMessage("deleteCertificatesCalledResultNotFound"));
            }
            deletedCertificatesList = this.deleteCertificates(ids).stream().map(arg_0 -> ((IDeleteCertificateHandler)delHandler).createSuccessDeleteCertificateResult(arg_0)).collect(Collectors.toList());
        } else {
            List models = deleteCertificate.getModels();
            if (models != null) {
                deletedCertificatesList = this.deleteCertificatesByAuthKeyIdentifierAndSerialNumber(models, hookProvider);
            }
        }
        deletedCertificatesList.forEach(deleteResult -> this.logger.log(Level.INFO, "000171", this.i18n.getEnglishMessage(deleteResult.getMessage()), CLASS_NAME));
        return deletedCertificatesList;
    }

    @EventListener
    public void deleteCertificates(CertificatesDeleteEvent deleteEvent) {
        List ids = deleteEvent.getIds();
        this.logger.entering(CLASS_NAME, "deleteCertificates");
        this.session.getSystemIntegrityCheckResult().clear();
        User currentUser = this.session.getCurrentUser();
        HashMap<String, List> param = new HashMap<String, List>();
        param.put("inclList", ids);
        List certificates = this.repository.findWithNamedQuery("IN_LIST_CERTIFICATE", param, -1);
        certificates.forEach(certificate -> {
            this.deleteCertificateForUser((Certificate)certificate, currentUser);
            String certificateType = certificate.isSecOCISCert() ? SecOcIs : certificate.getPKIRole();
            this.logger.log(Level.INFO, "000120", this.i18n.getEnglishMessage("deleteCertificateDuringImport", new String[]{certificateType, certificate.getEntityId()}), CertificatesService.class.getSimpleName());
        });
    }

    public <T extends Certificate> List<T> searchCertificates(Class<T> clazz, CertificateType type) {
        List searchCertificates = this.searchEngine.searchCertificates(clazz, type);
        if (searchCertificates != null) return searchCertificates;
        return Collections.emptyList();
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public <T extends Certificate> void deleteCertificatesForWhichTheUserDoesNotHavePermission(Certificate backendCertificate, List<Permission> permissions, UpdateType updateType, Class<T> type) {
        this.logger.log(Level.INFO, "000230", this.i18n.getEnglishMessage("updateDeletingCertForWhichUserDoesNotHavePermission", new String[]{updateType.name(), backendCertificate.getSubjectKeyIdentifier(), backendCertificate.getSerialNo()}), CLASS_NAME);
        ArrayList<String> unauthorizedCertificateIds = new ArrayList<String>();
        List certificatesUnderParent = this.searchEngine.findCertificatesUnderParent(this.session.getCurrentUser(), backendCertificate, type);
        certificatesUnderParent.parallelStream().filter(c -> this.certConfig.shouldDeleteDuringCSRCreation(c)).forEach(certificate -> this.addToUnauthorizedCertificate((List<String>)unauthorizedCertificateIds, permissions, (Certificate)certificate, updateType));
        if (unauthorizedCertificateIds.isEmpty()) return;
        this.deleteCertificatesDifferentTransaction(unauthorizedCertificateIds);
    }

    private void addToUnauthorizedCertificate(List<String> unauthorizedCertificateIds, List<Permission> permissions, Certificate certificate, UpdateType updateType) {
        Optional<Permission> foundPermission = permissions.stream().filter(permission -> CertificateUtil.isCertificateMatchingPermission((Certificate)certificate, (Permission)permission) || this.certConfig.matchEnrollmentId(certificate, permission)).findAny();
        if (!foundPermission.isPresent()) {
            this.certConfig.logDeletedUnauthorizedCertificate(certificate, updateType, this.logger, this.i18n);
            unauthorizedCertificateIds.add(certificate.getEntityId());
        } else {
            unauthorizedCertificateIds.addAll(this.getEnhRightsCertsForWhichUserDoesNotHavePermissions(foundPermission.get(), certificate, updateType));
        }
    }

    private List<String> getEnhRightsCertsForWhichUserDoesNotHavePermissions(Permission foundPermission, Certificate diag, UpdateType updateType) {
        ArrayList<String> unauthorizedCertificateIds = new ArrayList<String>();
        List services = foundPermission.getServices();
        List enhCerts = this.searchEngine.findEnhancedRightsCertificates(this.session.getCurrentUser(), diag.getParent().getSubjectKeyIdentifier(), diag.getSerialNo());
        enhCerts.parallelStream().forEach(enhCert -> {
            if (services == null || services.isEmpty()) {
                this.addEnhCertToUnauthorizedCertificateIds((List<String>)unauthorizedCertificateIds, updateType, (Certificate)enhCert);
            } else {
                Optional<EnhancedRightsPermission> enhPermissionOptional = services.stream().filter(enhRightsPermission -> CertificateUtil.isCertificateMatchingEnhancedRightsPermission((Certificate)enhCert, (EnhancedRightsPermission)enhRightsPermission)).findAny();
                if (enhPermissionOptional.isPresent()) return;
                this.addEnhCertToUnauthorizedCertificateIds((List<String>)unauthorizedCertificateIds, updateType, (Certificate)enhCert);
            }
        });
        return unauthorizedCertificateIds;
    }

    private void addEnhCertToUnauthorizedCertificateIds(List<String> unauthorizedCertificateIds, UpdateType updateType, Certificate enhCert) {
        this.logger.log(Level.INFO, "000231", this.i18n.getEnglishMessage("updateDeleteCertWithAKIAndSN", new String[]{updateType.name(), enhCert.getAuthorityKeyIdentifier(), enhCert.getSerialNo()}), CLASS_NAME);
        unauthorizedCertificateIds.add(enhCert.getEntityId());
    }

    private List<ExtendedDeleteCertificatesResult> deleteCertificatesByAuthKeyIdentifierAndSerialNumber(List<DeleteCertificateModel> models, ICertificateHooks hookProvider) {
        IDeleteCertificateHandler delHandler = this.certConfig.getDeleteCertificatesHandler(this, this.logger, this.i18n);
        User currentUser = this.session.getCurrentUser();
        ArrayList<ExtendedDeleteCertificatesResult> deletedCertificatesByAuthKeyAndSerialNo = new ArrayList<ExtendedDeleteCertificatesResult>();
        ArrayList certsToBeDeleted = new ArrayList();
        ArrayList<ExtendedDeleteCertificatesResult> failedCerts = new ArrayList<ExtendedDeleteCertificatesResult>();
        for (DeleteCertificateModel model : models) {
            String currentAuthKeyIdentifier = "";
            String currentSerialNumber = "";
            if (!StringUtils.isEmpty(model.getAuthorityKeyIdentifier())) {
                currentAuthKeyIdentifier = HexUtil.base64ToHex((String)model.getAuthorityKeyIdentifier());
            }
            if (!StringUtils.isEmpty(model.getSerialNo())) {
                currentSerialNumber = HexUtil.base64ToHex((String)model.getSerialNo());
            }
            List certificates = this.searchEngine.findCertByAuthKeyIdentAndSerialNo(currentUser, currentAuthKeyIdentifier, currentSerialNumber, Certificate.class);
            certsToBeDeleted.addAll(certificates);
            if (!certificates.isEmpty()) continue;
            failedCerts.add(delHandler.createFailDeleteCertificateByAuthKeyAndSnResult(currentAuthKeyIdentifier, currentSerialNumber));
        }
        if (certsToBeDeleted.isEmpty()) {
            this.logger.log(Level.INFO, "000468", this.i18n.getMessage("deleteCertificatesCalledResultNotFound"), this.getClass().getSimpleName());
            throw new CertificateNotFoundException(this.i18n.getMessage("deleteCertificatesCalledResultNotFound"));
        }
        List<String> listForDeletion = certsToBeDeleted.stream().map(Certificate::getEntityId).collect(Collectors.toList());
        List<DeleteCertificatesInfo> deletedCertificates = this.deleteCertificates(listForDeletion, hookProvider);
        deletedCertificates.forEach(deletedCertificateInfo -> {
            if (deletedCertificateInfo.isCertificate()) {
                deletedCertificatesByAuthKeyAndSerialNo.add(delHandler.createSuccessDeleteCertificateResult(deletedCertificateInfo));
            } else {
                deletedCertificatesByAuthKeyAndSerialNo.add(delHandler.createSuccessDeleteCSRResult(deletedCertificateInfo));
            }
        });
        deletedCertificatesByAuthKeyAndSerialNo.addAll(failedCerts);
        return deletedCertificatesByAuthKeyAndSerialNo;
    }

    public void deleteKPForChildren(Certificate root, User user) {
        if (root.getType() != CertificateType.ROOT_CA_CERTIFICATE && root.getType() != CertificateType.ROOT_CA_LINK_CERTIFICATE && root.getType() != CertificateType.BACKEND_CA_CERTIFICATE && root.getType() != CertificateType.ENHANCED_RIGHTS_CERTIFICATE && root.getType() != CertificateType.BACKEND_CA_LINK_CERTIFICATE) {
            this.removeKeyPairValuesFromCSR(user, root.getEntityId());
        } else {
            root.getChildren().forEach(certificate -> this.deleteKPForChildren((Certificate)certificate, user));
        }
    }

    private void removeKeyPairValuesFromCSR(User currentUser, String csrId) {
        Optional<UserKeyPair> filteredKeyPair = currentUser.getKeyPairs().stream().filter(keyPair -> keyPair.getCertificate() != null && csrId.equals(keyPair.getCertificate().getEntityId())).findFirst();
        if (!filteredKeyPair.isPresent()) return;
        UserKeyPair keyPair2 = filteredKeyPair.get();
        keyPair2.setCertificate(null);
        currentUser.getKeyPairs().remove(keyPair2);
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void deleteCertificatesWithNewTransaction(Set<String> ids, Session session) {
        this.deleteInternal(new ArrayList<String>(ids), false, session.getCurrentUser(), (ICertificateHooks)new NoHook());
    }
}
