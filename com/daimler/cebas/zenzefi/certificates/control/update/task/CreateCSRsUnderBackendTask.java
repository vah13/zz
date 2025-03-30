/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.CertificateToolsProvider;
/*     */ import com.daimler.cebas.certificates.control.CertificateUtil;
/*     */ import com.daimler.cebas.certificates.control.SearchEngine;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CertificatesUpdateException;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateSteps;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*     */ import com.daimler.cebas.certificates.control.validation.failure.CSRValidationFailureOutput;
/*     */ import com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput;
/*     */ import com.daimler.cebas.certificates.control.validation.failure.ValidationState;
/*     */ import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
/*     */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.entity.PKIRole;
/*     */ import com.daimler.cebas.certificates.integration.vo.EnhancedRightsPermission;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKICertificateRequest;
/*     */ import com.daimler.cebas.certificates.integration.vo.Permission;
/*     */ import com.daimler.cebas.common.ObjectIdentifier;
/*     */ import com.daimler.cebas.common.control.HexUtil;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.ZenzefiCreateCSRsTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.entity.ZenZefiCertificate;
/*     */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
/*     */ import java.security.cert.CertificateExpiredException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Base64;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ @CEBASControl
/*     */ public abstract class CreateCSRsUnderBackendTask
/*     */   extends ZenzefiCreateCSRsTask
/*     */ {
/*     */   private static final String DASH = "-";
/*  62 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.certificates.control.update.task.CreateCSRsUnderBackendTask.class.getSimpleName();
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public CreateCSRsUnderBackendTask(CertificateToolsProvider toolsProvider, ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, SearchEngine searchEngine, Session session, UpdateSession updateSession, Logger logger, MetadataManager i18n) {
/*  68 */     super(toolsProvider, publicKeyInfrastructureEsi, searchEngine, session, updateSession, logger);
/*  69 */     this.searchEngine = searchEngine;
/*  70 */     this.certificateSignRequestEngine = toolsProvider.getCertificateSignRequestEngine();
/*  71 */     this.deleteCertificatesEngine = toolsProvider.getDeleteCertificatesEngine();
/*  72 */     this.session = session;
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
/*     */   public List<PKICertificateRequest> execute(List<? extends ImportResult> importResults, List<Permission> permissions, UpdateType updateType) {
/*  84 */     if (this.updateSession.isRunning()) {
/*  85 */       this.updateSession.updateStep(UpdateSteps.CREATE_CSRS, "updateStartCreateCSRsProcess", updateType);
/*  86 */       this.logger.log(Level.INFO, "000228", this.i18n.getEnglishMessage("updateStartCreateCSRsProcess", new String[] { updateType
/*  87 */               .name() }), CLASS_NAME);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  93 */       List<PKICertificateRequest> pkiCertificateRequests = (List<PKICertificateRequest>)importResults.stream().filter(this::isBackendPresent).map(result -> processBackendUpdate(permissions, result, updateType)).flatMap(Collection::stream).collect(Collectors.toList());
/*     */       
/*  95 */       this.logger.log(Level.INFO, "000233", this.i18n.getEnglishMessage("updateStopCreateCSRsProcess", new String[] { updateType
/*  96 */               .name() }), CLASS_NAME);
/*     */       
/*  98 */       this.updateSession.addStepResult(UpdateSteps.CREATE_CSRS, pkiCertificateRequests);
/*  99 */       this.updateSession.updateStep(UpdateSteps.CREATE_CSRS, "updateStopCreateCSRsProcess", updateType);
/* 100 */       this.updateSession.getPkiCertificateRequests().addAll(pkiCertificateRequests);
/* 101 */       return pkiCertificateRequests;
/*     */     } 
/* 103 */     return Collections.emptyList();
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
/*     */   private Optional<PKICertificateRequest> createCSRAndPKICertificateRequestForBackendIfRunning(Certificate backendCertificate, Permission permission, UpdateType updateType) {
/* 115 */     if (this.updateSession.isRunning()) {
/* 116 */       return createCSRAndPKICertificateRequestForBackend(backendCertificate, permission, updateType);
/*     */     }
/* 118 */     return Optional.empty();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PKICertificateRequest createPKICertificateRequestFromCSR(Certificate backendCertificate, Permission permission, boolean deleteNonIdenticalCertificates, UpdateType updateType) {
/* 141 */     Certificate createdCSR = createCSRFromPermissionUnderBackend(backendCertificate, permission);
/* 142 */     if (!permission.isValid()) {
/* 143 */       return null;
/*     */     }
/* 145 */     this.logger.log(Level.FINE, "000232", this.i18n
/* 146 */         .getEnglishMessage("updateCreatedCSRWithAKIPKIRoleUserRole", new String[] {
/* 147 */             updateType.name(), createdCSR.getAuthorityKeyIdentifier(), createdCSR
/* 148 */             .getPKIRole(), createdCSR.getUserRole()
/*     */           }), CLASS_NAME);
/* 150 */     if (deleteNonIdenticalCertificates) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 155 */       List<Certificate> certificatesUnderBackend = (List<Certificate>)this.searchEngine.findCertificatesUnderParentByType(this.session.getCurrentUser(), backendCertificate.getSubjectKeyIdentifier(), createdCSR.getType(), Certificate.class).stream().filter(this::shouldDelete).collect(Collectors.toList());
/* 156 */       deleteNonIdenticalCertificates(certificatesUnderBackend, createdCSR, updateType);
/*     */     } 
/*     */ 
/*     */     
/* 160 */     PKICertificateRequest pkiCertificateRequest = new PKICertificateRequest(permission.getEnrollmentId(), createdCSR.getPkcs10Signature(), backendCertificate.getSubjectKeyIdentifier().replace("-", "").toLowerCase(), createdCSR.getType().name());
/* 161 */     pkiCertificateRequest.setInternalCSRId(createdCSR.getEntityId());
/* 162 */     return pkiCertificateRequest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Certificate createCSRFromPermissionUnderBackend(Certificate backendCertificate, Permission permission) {
/* 173 */     String nonce = null;
/* 174 */     String specialECU = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     CertificateSignRequest certificateSignRequest = new CertificateSignRequest("CN=" + this.session.getCurrentUser().getUserName(), getPKIRoleFromPermission(permission.getPkiRole()), getUserRoleFromPermission(permission.getUserRole()), CertificateUtil.getCommaSeparatedStringFromList(permission.getTargetECU()), CertificateUtil.getCommaSeparatedStringFromList(permission.getTargetVIN()), nonce, null, CertificateUtil.getCommaSeparatedStringFromList(permission.getUniqueECUID()), specialECU, permission.getValidity(), backendCertificate.getEntityId(), this.session.getCurrentUser().getEntityId(), ObjectIdentifier.ALGORITHM_IDENTIFIER_OID.getOid(), "0.1", getProdQualifier(), Base64.getEncoder().encodeToString(HexUtil.hexStringToByteArray(backendCertificate.getSubjectKeyIdentifier())));
/*     */     
/* 185 */     return this.certificateSignRequestEngine.createCertificateInSignRequestStateNewTransaction(certificateSignRequest, true, (ValidationFailureOutput)new CSRValidationFailureOutput((ValidationState)permission, (ValidationState)certificateSignRequest, this.logger));
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
/*     */   private List<PKICertificateRequest> processBackendUpdate(List<Permission> permissions, ImportResult backendResult, UpdateType updateType) {
/* 201 */     Certificate backendCertificate = (Certificate)this.searchEngine.findPKIKnownCertificateBySkiAkiAndType(this.session.getCurrentUser(), backendResult.getSubjectKeyIdentifier(), backendResult.getAuthorityKeyIdentifier(), CertificateType.BACKEND_CA_CERTIFICATE, ZenZefiCertificate.class).orElseThrow(() -> {
/*     */           this.logger.log(Level.INFO, "000585", this.i18n.getMessage("backendNotFound"), getClass().getSimpleName());
/*     */ 
/*     */           
/*     */           return new CertificatesUpdateException(this.i18n.getMessage("backendNotFound"), "backendNotFound");
/*     */         });
/*     */     
/* 208 */     if (this.updateSession.isRunning()) {
/* 209 */       deleteCSRsUnderBackendExceptTime(backendCertificate, updateType);
/* 210 */       deleteCSRsUnderDiagExceptSecOCIS(backendCertificate);
/* 211 */       deleteCertificatesForWhichTheUserDoesNotHavePermission(backendCertificate, permissions, updateType);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 216 */       List<PKICertificateRequest> requests = (List<PKICertificateRequest>)permissions.stream().map(permission -> createCSRAndPKICertificateRequestForBackendIfRunning(backendCertificate, permission, updateType)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
/* 217 */       this.logger.log(Level.INFO, "000229", "CSR creation report under backend " + backendCertificate
/* 218 */           .getSubject() + ": " + requests.size(), CLASS_NAME);
/*     */       
/* 220 */       return requests;
/*     */     } 
/* 222 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void deleteCSRsUnderBackendExceptTime(Certificate backendCertificate, UpdateType updateType) {
/* 232 */     this.logger.log(Level.INFO, "000229", this.i18n
/* 233 */         .getEnglishMessage("updateDeleteCSRUnderBackendWithSKIAndSN", new String[] { updateType.name(), backendCertificate
/* 234 */             .getSubjectKeyIdentifier(), backendCertificate.getSerialNo() }), CLASS_NAME);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 240 */     List<String> csrsToDelete = (List<String>)this.searchEngine.findCSRsUnderBackend(this.session.getCurrentUser(), backendCertificate).stream().filter(csr -> !csr.getType().equals(CertificateType.TIME_CERTIFICATE)).map(Certificate::getEntityId).collect(Collectors.toList());
/* 241 */     if (!csrsToDelete.isEmpty()) {
/* 242 */       this.deleteCertificatesEngine.deleteCertificatesDifferentTransaction(csrsToDelete);
/*     */     }
/*     */     
/* 245 */     this.logger.log(Level.INFO, "000229", "CSRs cleanup (Excluding Time) report under backend " + backendCertificate
/* 246 */         .getSubject() + ": " + csrsToDelete.size(), CLASS_NAME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void deleteCSRsUnderDiagExceptSecOCIS(Certificate backendCertificate) {
/* 255 */     this.searchEngine.findDiagnosticCertificates(this.session.getCurrentUser(), backendCertificate)
/* 256 */       .forEach(this::deleteCSRUnderDiagExceptSecOCIS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void deleteCSRUnderDiagExceptSecOCIS(Certificate diagCert) {
/* 267 */     List<String> csrsNotSecOcisIds = (List<String>)this.searchEngine.findCSRsUnderDiag(this.session.getCurrentUser(), diagCert.getAuthorityKeyIdentifier()).stream().filter(csr -> !csr.isSecOCISCert()).map(Certificate::getEntityId).collect(Collectors.toList());
/* 268 */     if (!csrsNotSecOcisIds.isEmpty()) {
/* 269 */       this.deleteCertificatesEngine.deleteCertificatesDifferentTransaction(csrsNotSecOcisIds);
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
/*     */   private void deleteCertificatesForWhichTheUserDoesNotHavePermission(Certificate backendCertificate, List<Permission> permissions, UpdateType updateType) {
/* 284 */     this.logger.log(Level.INFO, "000230", this.i18n
/* 285 */         .getEnglishMessage("updateDeletingCertForWhichUserDoesNotHavePermission", new String[] {
/* 286 */             updateType.name(), backendCertificate.getSubjectKeyIdentifier(), backendCertificate
/* 287 */             .getSerialNo()
/*     */           }), CLASS_NAME);
/*     */     
/* 290 */     List<String> unauthorizedCertificateIds = new ArrayList<>();
/* 291 */     this.searchEngine.findCertificatesUnderParent(this.session.getCurrentUser(), backendCertificate, ZenZefiCertificate.class)
/* 292 */       .parallelStream()
/* 293 */       .filter(this::shouldDelete)
/* 294 */       .forEach(certificate -> addToUnauthorizedCertificates(unauthorizedCertificateIds, permissions, certificate, updateType));
/*     */     
/* 296 */     if (!unauthorizedCertificateIds.isEmpty()) {
/* 297 */       this.deleteCertificatesEngine.deleteCertificatesDifferentTransaction(unauthorizedCertificateIds);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToUnauthorizedCertificates(List<String> unauthorizedCertificateIds, List<Permission> permissions, ZenZefiCertificate certificate, UpdateType updateType) {
/* 316 */     Optional<Permission> foundPermission = permissions.stream().filter(permission -> CertificateUtil.isCertificateMatchingPermission((Certificate)certificate, permission)).findAny();
/* 317 */     if (!foundPermission.isPresent()) {
/* 318 */       this.logger.log(Level.INFO, "000231", this.i18n
/* 319 */           .getEnglishMessage("updateDeleteCertWithAKIAndSN", new String[] { updateType.name(), certificate
/* 320 */               .getAuthorityKeyIdentifier(), certificate.getSerialNo() }), CLASS_NAME);
/*     */       
/* 322 */       unauthorizedCertificateIds.add(certificate.getEntityId());
/*     */     } else {
/* 324 */       unauthorizedCertificateIds.addAll(getEnhRightsCertsForWhichUserDoesNotHavePermissions(foundPermission.get(), certificate, updateType));
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
/*     */   
/*     */   private boolean shouldDelete(Certificate certificate) {
/* 340 */     CertificateType type = certificate.getType();
/* 341 */     switch (null.$SwitchMap$com$daimler$cebas$certificates$entity$CertificateType[type.ordinal()]) {
/*     */       case 1:
/* 343 */         return (hasPrivateKey(certificate) || isNotValid(certificate));
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/* 347 */         return isNotValid(certificate);
/*     */     } 
/* 349 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isNotValid(Certificate certificate) {
/*     */     try {
/* 355 */       certificate.checkValidity();
/* 356 */     } catch (CertificateExpiredException|java.security.cert.CertificateNotYetValidException e) {
/* 357 */       return true;
/*     */     } 
/* 359 */     return false;
/*     */   }
/*     */   
/*     */   private boolean hasPrivateKey(Certificate certificate) {
/* 363 */     return this.searchEngine.hasPrivateKey(certificate);
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
/*     */   private List<String> getEnhRightsCertsForWhichUserDoesNotHavePermissions(Permission foundPermission, ZenZefiCertificate diag, UpdateType updateType) {
/* 378 */     List<String> unauthorizedCertificateIds = new ArrayList<>();
/* 379 */     List<EnhancedRightsPermission> services = foundPermission.getServices();
/* 380 */     List<Certificate> enhCerts = this.searchEngine.findEnhancedRightsCertificates(this.session.getCurrentUser(), diag
/* 381 */         .getParent().getSubjectKeyIdentifier(), diag.getSerialNo());
/* 382 */     enhCerts.parallelStream()
/* 383 */       .filter(enhCert -> Boolean.TRUE.equals(((ZenZefiCertificate)enhCert.getParent().getParent()).getPkiKnown()))
/* 384 */       .forEach(enhCert -> {
/*     */           if (services == null || services.isEmpty()) {
/*     */             addEnhCertToUnauthorizedCertificates(unauthorizedCertificateIds, updateType, enhCert);
/*     */           } else {
/*     */             Optional<EnhancedRightsPermission> enhPermissionOptional = services.parallelStream().filter(()).findAny();
/*     */             
/*     */             if (!enhPermissionOptional.isPresent()) {
/*     */               addEnhCertToUnauthorizedCertificates(unauthorizedCertificateIds, updateType, enhCert);
/*     */             }
/*     */           } 
/*     */         });
/*     */     
/* 396 */     return unauthorizedCertificateIds;
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
/*     */   private void addEnhCertToUnauthorizedCertificates(List<String> unauthorizedCertificateIds, UpdateType updateType, Certificate enhCert) {
/* 410 */     this.logger.log(Level.INFO, "000231", this.i18n.getEnglishMessage("updateDeleteCertWithAKIAndSN", new String[] { updateType
/* 411 */             .name(), enhCert.getAuthorityKeyIdentifier(), enhCert.getSerialNo() }), CLASS_NAME);
/* 412 */     unauthorizedCertificateIds.add(enhCert.getEntityId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getPKIRoleFromPermission(String byteValue) {
/* 422 */     int pkiRole = Integer.decode(byteValue).intValue();
/* 423 */     return ((CertificateType)PKIRole.getRoles().get(Integer.valueOf(pkiRole))).name();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isBackendPresent(ImportResult result) {
/* 433 */     return (!StringUtils.isEmpty(result.getAuthorityKeyIdentifier()) && this.searchEngine
/* 434 */       .findPKIKnownCertificateBySkiAkiAndType(this.session.getCurrentUser(), result.getSubjectKeyIdentifier(), result
/* 435 */         .getAuthorityKeyIdentifier(), CertificateType.BACKEND_CA_CERTIFICATE, ZenZefiCertificate.class)
/* 436 */       .isPresent());
/*     */   }
/*     */   
/*     */   protected abstract Optional<PKICertificateRequest> createCSRAndPKICertificateRequestForBackend(Certificate paramCertificate, Permission paramPermission, UpdateType paramUpdateType);
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\CreateCSRsUnderBackendTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */