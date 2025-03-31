/*     */ package com.daimler.cebas.certificates.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.chain.events.CertificatesDeleteEvent;
/*     */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*     */ import com.daimler.cebas.certificates.control.config.handlers.IDeleteCertificateHandler;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
/*     */ import com.daimler.cebas.certificates.control.hooks.ICertificateHooks;
/*     */ import com.daimler.cebas.certificates.control.hooks.NoHook;
/*     */ import com.daimler.cebas.certificates.control.repository.CertificateRepository;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*     */ import com.daimler.cebas.certificates.control.validation.CertificatesProcessValidation;
/*     */ import com.daimler.cebas.certificates.control.vo.AbstractDeleteCertificates;
/*     */ import com.daimler.cebas.certificates.control.vo.DeleteCertificateModel;
/*     */ import com.daimler.cebas.certificates.control.vo.DeleteCertificates;
/*     */ import com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo;
/*     */ import com.daimler.cebas.certificates.control.vo.ExtendedDeleteCertificatesResult;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.integration.vo.EnhancedRightsPermission;
/*     */ import com.daimler.cebas.certificates.integration.vo.Permission;
/*     */ import com.daimler.cebas.common.control.HexUtil;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.common.entity.AbstractEntity;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.users.entity.UserKeyPair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.event.EventListener;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASControl
/*     */ public class DeleteCertificatesEngine
/*     */ {
/*  52 */   private static final String CLASS_NAME = DeleteCertificatesEngine.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private final String METHOD_NAME = "deleteCertificates";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String SecOcIs = "SecOcIs";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Session session;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SearchEngine searchEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CertificateRepository repository;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MetadataManager i18n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CertificatesConfiguration certConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public DeleteCertificatesEngine(Session session, SearchEngine searchEngine, CertificateRepository repository, CertificatesConfiguration certConfig, Logger logger, MetadataManager i18n) {
/* 111 */     this.session = session;
/* 112 */     this.searchEngine = searchEngine;
/* 113 */     this.repository = repository;
/* 114 */     this.logger = logger;
/* 115 */     this.i18n = i18n;
/* 116 */     this.certConfig = certConfig;
/*     */   }
/*     */   
/*     */   public CertificateRepository getRepository() {
/* 120 */     return this.repository;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ExtendedDeleteCertificatesResult> deleteCertificatesAdditionalLogging(List<String> ids) {
/* 131 */     return deleteCertificatesAdditionalLogging(ids, (ICertificateHooks)new NoHook());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ExtendedDeleteCertificatesResult> deleteCertificatesAdditionalLogging(List<String> ids, ICertificateHooks hookProvider) {
/* 142 */     String METHOD_NAME = "deleteCertificatesAdditionalLogging";
/* 143 */     this.logger.entering(CLASS_NAME, "deleteCertificatesAdditionalLogging");
/*     */     
/* 145 */     IDeleteCertificateHandler delHandler = this.certConfig.getDeleteCertificatesHandler(this, this.logger, this.i18n);
/* 146 */     List<ExtendedDeleteCertificatesResult> deleteCertificatesByIdResultList = new ArrayList<>();
/* 147 */     List<DeleteCertificatesInfo> deleteCertificatesInfos = deleteCertificates(ids, hookProvider);
/* 148 */     deleteCertificatesInfos.forEach(deleteCertificatesInfo -> {
/*     */           if (deleteCertificatesInfo.isCertificate()) {
/*     */             deleteCertificatesByIdResultList.add(delHandler.createSuccessDeleteCertificateResult(deleteCertificatesInfo));
/*     */           } else {
/*     */             deleteCertificatesByIdResultList.add(delHandler.createSuccessDeleteCSRResult(deleteCertificatesInfo));
/*     */           } 
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     List<String> failedCerts = (List<String>)ids.stream().filter(failedCertId -> !((List)deleteCertificatesInfos.stream().map(AbstractDeleteCertificates::getCertificateId).collect(Collectors.toList())).contains(failedCertId)).collect(Collectors.toList());
/* 160 */     failedCerts.forEach(notFoundId -> deleteCertificatesByIdResultList.add(delHandler.createFailDeleteCertificateByIdResult(notFoundId)));
/*     */     
/* 162 */     deleteCertificatesByIdResultList.forEach(deleteResult -> this.logger.log(Level.INFO, "000179", this.i18n.getEnglishMessage(deleteResult.getMessage()), CLASS_NAME));
/*     */     
/* 164 */     this.logger.exiting(CLASS_NAME, "deleteCertificatesAdditionalLogging");
/* 165 */     return deleteCertificatesByIdResultList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRED)
/*     */   public List<DeleteCertificatesInfo> deleteCertificates(List<String> ids) {
/* 177 */     this.logger.entering(CLASS_NAME, "deleteCertificates");
/*     */     
/* 179 */     List<DeleteCertificatesInfo> deletedCertificates = deleteInternal(ids, false, this.session.getCurrentUser(), (ICertificateHooks)new NoHook());
/*     */     
/* 181 */     this.logger.exiting(CLASS_NAME, "deleteCertificates");
/* 182 */     return deletedCertificates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRED)
/*     */   public List<DeleteCertificatesInfo> deleteCertificates(List<String> ids, ICertificateHooks hookProvider) {
/* 194 */     this.logger.entering(CLASS_NAME, "deleteCertificates");
/*     */     
/* 196 */     List<DeleteCertificatesInfo> deletedCertificates = deleteInternal(ids, false, this.session.getCurrentUser(), hookProvider);
/*     */     
/* 198 */     this.logger.exiting(CLASS_NAME, "deleteCertificates");
/* 199 */     return deletedCertificates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRED)
/*     */   public List<DeleteCertificatesInfo> deleteCertificatesDuringImport(List<String> ids) {
/* 211 */     String METHOD_NAME = "deleteCertificatesDuringImport";
/* 212 */     this.logger.entering(CLASS_NAME, "deleteCertificatesDuringImport");
/* 213 */     List<DeleteCertificatesInfo> deletedCertificates = deleteInternal(ids, true, this.session.getCurrentUser(), (ICertificateHooks)new NoHook());
/* 214 */     this.logger.exiting(CLASS_NAME, "deleteCertificatesDuringImport");
/* 215 */     return deletedCertificates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<DeleteCertificatesInfo> deleteCertificates(List<String> ids, User user) {
/* 228 */     return deleteInternal(ids, false, user, (ICertificateHooks)new NoHook());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public List<DeleteCertificatesInfo> deleteCertificatesDifferentTransaction(List<String> ids) {
/* 240 */     return deleteCertificates(ids);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<DeleteCertificatesInfo> deleteInternal(List<String> ids, boolean duringUpdateSession, User user, ICertificateHooks hookProvider) {
/* 252 */     CertificatesProcessValidation.validateDeleteCertificates(ids, this.i18n, this.logger);
/* 253 */     this.session.getSystemIntegrityCheckResult().clear();
/*     */     
/* 255 */     Map<String, Object> param = new HashMap<>();
/* 256 */     param.put("inclList", ids);
/* 257 */     List<Certificate> certificates = this.repository.findWithNamedQuery("IN_LIST_CERTIFICATE", param, -1);
/* 258 */     if (certificates.isEmpty()) {
/* 259 */       this.logger.log(Level.INFO, "000466", this.i18n
/* 260 */           .getMessage("deleteCertificatesCalledResultNotFound"), 
/* 261 */           getClass().getSimpleName());
/* 262 */       throw new CertificateNotFoundException(this.i18n.getMessage("deleteCertificatesCalledResultNotFound"));
/*     */     } 
/*     */     
/* 265 */     List<Certificate> roots = new ArrayList<>();
/* 266 */     List<DeleteCertificatesInfo> deleteCertificatesInfo = new ArrayList<>();
/* 267 */     IDeleteCertificateHandler delHandler = this.certConfig.getDeleteCertificatesHandler(this, this.logger, this.i18n);
/* 268 */     certificates.forEach(certificate -> delHandler.deleteCertificate(ids, duringUpdateSession, user, deleteCertificatesInfo, roots, certificate));
/* 269 */     hookProvider.possibleHook().ifPresent(certificates::forEach);
/* 270 */     roots.forEach(root -> this.repository.deleteManagedEntity((AbstractEntity)root));
/* 271 */     return deleteCertificatesInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteCertificateForUser(Certificate certificate, User currentUser) {
/* 283 */     deleteKPForChildren(certificate, currentUser);
/* 284 */     if (certificate.getParent() != null) {
/* 285 */       certificate.getParent().getChildren().remove(certificate);
/* 286 */       certificate.getChildren().clear();
/* 287 */       this.repository.update((AbstractEntity)certificate.getParent());
/* 288 */       this.repository.flush();
/* 289 */       this.repository.delete(Certificate.class, certificate.getEntityId());
/*     */     } else {
/* 291 */       currentUser.getCertificates().remove(certificate);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ExtendedDeleteCertificatesResult> deleteCertificates(DeleteCertificates deleteCertificate, List<Certificate> currentUserCertificates) {
/* 306 */     return deleteCertificates(deleteCertificate, currentUserCertificates, (ICertificateHooks)new NoHook());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ExtendedDeleteCertificatesResult> deleteCertificates(DeleteCertificates deleteCertificate, List<? extends Certificate> currentUserCertificates, ICertificateHooks hookProvider) {
/* 319 */     List<ExtendedDeleteCertificatesResult> deletedCertificatesList = new ArrayList<>();
/* 320 */     IDeleteCertificateHandler delHandler = this.certConfig.getDeleteCertificatesHandler(this, this.logger, this.i18n);
/* 321 */     CertificatesProcessValidation.validateDeleteCertificate(deleteCertificate, this.i18n, this.logger);
/* 322 */     if (deleteCertificate.isAll()) {
/*     */ 
/*     */       
/* 325 */       List<String> ids = (List<String>)currentUserCertificates.stream().map(Certificate::getEntityId).collect(Collectors.toList());
/* 326 */       if (ids.isEmpty()) {
/* 327 */         this.logger.log(Level.INFO, "000467", this.i18n
/* 328 */             .getMessage("deleteCertificatesCalledResultNotFound"), 
/* 329 */             getClass().getSimpleName());
/* 330 */         throw new CertificateNotFoundException(this.i18n
/* 331 */             .getMessage("deleteCertificatesCalledResultNotFound"));
/*     */       } 
/*     */ 
/*     */       
/* 335 */       deletedCertificatesList = (List<ExtendedDeleteCertificatesResult>)deleteCertificates(ids).stream().map(delHandler::createSuccessDeleteCertificateResult).collect(Collectors.toList());
/*     */     } else {
/* 337 */       List<DeleteCertificateModel> models = deleteCertificate.getModels();
/* 338 */       if (models != null) {
/* 339 */         deletedCertificatesList = deleteCertificatesByAuthKeyIdentifierAndSerialNumber(models, hookProvider);
/*     */       }
/*     */     } 
/* 342 */     deletedCertificatesList.forEach(deleteResult -> this.logger.log(Level.INFO, "000171", this.i18n.getEnglishMessage(deleteResult.getMessage()), CLASS_NAME));
/*     */     
/* 344 */     return deletedCertificatesList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventListener
/*     */   public void deleteCertificates(CertificatesDeleteEvent deleteEvent) {
/* 355 */     List<String> ids = deleteEvent.getIds();
/* 356 */     this.logger.entering(CLASS_NAME, "deleteCertificates");
/* 357 */     this.session.getSystemIntegrityCheckResult().clear();
/* 358 */     User currentUser = this.session.getCurrentUser();
/* 359 */     Map<String, Object> param = new HashMap<>();
/* 360 */     param.put("inclList", ids);
/* 361 */     List<Certificate> certificates = this.repository.findWithNamedQuery("IN_LIST_CERTIFICATE", param, -1);
/* 362 */     certificates.forEach(certificate -> {
/*     */           String certificateType;
/*     */           deleteCertificateForUser(certificate, currentUser);
/*     */           if (certificate.isSecOCISCert()) {
/*     */             certificateType = "SecOcIs";
/*     */           } else {
/*     */             certificateType = certificate.getPKIRole();
/*     */           } 
/*     */           this.logger.log(Level.INFO, "000120", this.i18n.getEnglishMessage("deleteCertificateDuringImport", new String[] { certificateType, certificate.getEntityId() }), CertificatesService.class.getSimpleName());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Certificate> List<T> searchCertificates(Class<T> clazz, CertificateType type) {
/* 389 */     List<T> searchCertificates = this.searchEngine.searchCertificates(clazz, type);
/* 390 */     if (searchCertificates == null) {
/* 391 */       return Collections.emptyList();
/*     */     }
/* 393 */     return searchCertificates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public <T extends Certificate> void deleteCertificatesForWhichTheUserDoesNotHavePermission(Certificate backendCertificate, List<Permission> permissions, UpdateType updateType, Class<T> type) {
/* 408 */     this.logger.log(Level.INFO, "000230", this.i18n
/* 409 */         .getEnglishMessage("updateDeletingCertForWhichUserDoesNotHavePermission", new String[] {
/* 410 */             updateType.name(), backendCertificate.getSubjectKeyIdentifier(), backendCertificate
/* 411 */             .getSerialNo()
/*     */           }), CLASS_NAME);
/* 413 */     List<String> unauthorizedCertificateIds = new ArrayList<>();
/* 414 */     List<T> certificatesUnderParent = this.searchEngine.findCertificatesUnderParent(this.session.getCurrentUser(), backendCertificate, type);
/* 415 */     certificatesUnderParent.parallelStream()
/* 416 */       .filter(c -> this.certConfig.shouldDeleteDuringCSRCreation(c))
/* 417 */       .forEach(certificate -> addToUnauthorizedCertificate(unauthorizedCertificateIds, permissions, certificate, updateType));
/*     */     
/* 419 */     if (!unauthorizedCertificateIds.isEmpty()) {
/* 420 */       deleteCertificatesDifferentTransaction(unauthorizedCertificateIds);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToUnauthorizedCertificate(List<String> unauthorizedCertificateIds, List<Permission> permissions, Certificate certificate, UpdateType updateType) {
/* 435 */     Optional<Permission> foundPermission = permissions.stream().filter(permission -> (CertificateUtil.isCertificateMatchingPermission(certificate, permission) || this.certConfig.matchEnrollmentId(certificate, permission))).findAny();
/* 436 */     if (!foundPermission.isPresent()) {
/* 437 */       this.certConfig.logDeletedUnauthorizedCertificate(certificate, updateType, this.logger, this.i18n);
/* 438 */       unauthorizedCertificateIds.add(certificate.getEntityId());
/*     */     } else {
/* 440 */       unauthorizedCertificateIds.addAll(getEnhRightsCertsForWhichUserDoesNotHavePermissions(foundPermission.get(), certificate, updateType));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> getEnhRightsCertsForWhichUserDoesNotHavePermissions(Permission foundPermission, Certificate diag, UpdateType updateType) {
/* 452 */     List<String> unauthorizedCertificateIds = new ArrayList<>();
/* 453 */     List<EnhancedRightsPermission> services = foundPermission.getServices();
/* 454 */     List<Certificate> enhCerts = this.searchEngine.findEnhancedRightsCertificates(this.session.getCurrentUser(), diag
/* 455 */         .getParent().getSubjectKeyIdentifier(), diag.getSerialNo());
/* 456 */     enhCerts.parallelStream().forEach(enhCert -> {
/*     */           if (services == null || services.isEmpty()) {
/*     */             addEnhCertToUnauthorizedCertificateIds(unauthorizedCertificateIds, updateType, enhCert);
/*     */           } else {
/*     */             Optional<EnhancedRightsPermission> enhPermissionOptional = services.stream().filter(()).findAny();
/*     */             
/*     */             if (!enhPermissionOptional.isPresent()) {
/*     */               addEnhCertToUnauthorizedCertificateIds(unauthorizedCertificateIds, updateType, enhCert);
/*     */             }
/*     */           } 
/*     */         });
/* 467 */     return unauthorizedCertificateIds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addEnhCertToUnauthorizedCertificateIds(List<String> unauthorizedCertificateIds, UpdateType updateType, Certificate enhCert) {
/* 481 */     this.logger.log(Level.INFO, "000231", this.i18n.getEnglishMessage("updateDeleteCertWithAKIAndSN", new String[] { updateType
/* 482 */             .name(), enhCert.getAuthorityKeyIdentifier(), enhCert.getSerialNo() }), CLASS_NAME);
/* 483 */     unauthorizedCertificateIds.add(enhCert.getEntityId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<ExtendedDeleteCertificatesResult> deleteCertificatesByAuthKeyIdentifierAndSerialNumber(List<DeleteCertificateModel> models, ICertificateHooks hookProvider) {
/* 496 */     IDeleteCertificateHandler delHandler = this.certConfig.getDeleteCertificatesHandler(this, this.logger, this.i18n);
/* 497 */     User currentUser = this.session.getCurrentUser();
/* 498 */     List<ExtendedDeleteCertificatesResult> deletedCertificatesByAuthKeyAndSerialNo = new ArrayList<>();
/* 499 */     List<Certificate> certsToBeDeleted = new ArrayList<>();
/* 500 */     List<ExtendedDeleteCertificatesResult> failedCerts = new ArrayList<>();
/* 501 */     for (DeleteCertificateModel model : models) {
/* 502 */       String currentAuthKeyIdentifier = "";
/* 503 */       String currentSerialNumber = "";
/* 504 */       if (!StringUtils.isEmpty(model.getAuthorityKeyIdentifier())) {
/* 505 */         currentAuthKeyIdentifier = HexUtil.base64ToHex(model.getAuthorityKeyIdentifier());
/*     */       }
/* 507 */       if (!StringUtils.isEmpty(model.getSerialNo())) {
/* 508 */         currentSerialNumber = HexUtil.base64ToHex(model.getSerialNo());
/*     */       }
/* 510 */       List<Certificate> certificates = this.searchEngine.findCertByAuthKeyIdentAndSerialNo(currentUser, currentAuthKeyIdentifier, currentSerialNumber, Certificate.class);
/*     */       
/* 512 */       certsToBeDeleted.addAll(certificates);
/* 513 */       if (certificates.isEmpty()) {
/* 514 */         failedCerts.add(delHandler.createFailDeleteCertificateByAuthKeyAndSnResult(currentAuthKeyIdentifier, currentSerialNumber));
/*     */       }
/*     */     } 
/* 517 */     if (certsToBeDeleted.isEmpty()) {
/* 518 */       this.logger.log(Level.INFO, "000468", this.i18n
/* 519 */           .getMessage("deleteCertificatesCalledResultNotFound"), 
/* 520 */           getClass().getSimpleName());
/* 521 */       throw new CertificateNotFoundException(this.i18n
/* 522 */           .getMessage("deleteCertificatesCalledResultNotFound"));
/*     */     } 
/* 524 */     List<String> listForDeletion = (List<String>)certsToBeDeleted.stream().map(Certificate::getEntityId).collect(Collectors.toList());
/* 525 */     List<DeleteCertificatesInfo> deletedCertificates = deleteCertificates(listForDeletion, hookProvider);
/* 526 */     deletedCertificates.forEach(deletedCertificateInfo -> {
/*     */           if (deletedCertificateInfo.isCertificate()) {
/*     */             deletedCertificatesByAuthKeyAndSerialNo.add(delHandler.createSuccessDeleteCertificateResult(deletedCertificateInfo));
/*     */           } else {
/*     */             deletedCertificatesByAuthKeyAndSerialNo.add(delHandler.createSuccessDeleteCSRResult(deletedCertificateInfo));
/*     */           } 
/*     */         });
/* 533 */     deletedCertificatesByAuthKeyAndSerialNo.addAll(failedCerts);
/* 534 */     return deletedCertificatesByAuthKeyAndSerialNo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteKPForChildren(Certificate root, User user) {
/* 547 */     if (root.getType() != CertificateType.ROOT_CA_CERTIFICATE && root
/* 548 */       .getType() != CertificateType.ROOT_CA_LINK_CERTIFICATE && root
/* 549 */       .getType() != CertificateType.BACKEND_CA_CERTIFICATE && root
/* 550 */       .getType() != CertificateType.ENHANCED_RIGHTS_CERTIFICATE && root
/* 551 */       .getType() != CertificateType.BACKEND_CA_LINK_CERTIFICATE) {
/* 552 */       removeKeyPairValuesFromCSR(user, root.getEntityId());
/*     */     } else {
/* 554 */       root.getChildren().forEach(certificate -> deleteKPForChildren(certificate, user));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeKeyPairValuesFromCSR(User currentUser, String csrId) {
/* 569 */     Optional<UserKeyPair> filteredKeyPair = currentUser.getKeyPairs().stream().filter(keyPair -> (keyPair.getCertificate() != null && csrId.equals(keyPair.getCertificate().getEntityId()))).findFirst();
/* 570 */     if (filteredKeyPair.isPresent()) {
/* 571 */       UserKeyPair keyPair = filteredKeyPair.get();
/* 572 */       keyPair.setCertificate(null);
/* 573 */       currentUser.getKeyPairs().remove(keyPair);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public void deleteCertificatesWithNewTransaction(Set<String> ids, Session session) {
/* 579 */     deleteInternal(new ArrayList<>(ids), false, session.getCurrentUser(), (ICertificateHooks)new NoHook());
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\DeleteCertificatesEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */