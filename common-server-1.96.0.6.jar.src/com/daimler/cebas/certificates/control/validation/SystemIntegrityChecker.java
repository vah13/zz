/*     */ package com.daimler.cebas.certificates.control.validation;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.chain.ChainOfTrust;
/*     */ import com.daimler.cebas.certificates.control.repository.CertificateRepository;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateState;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.entity.RawData;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
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
/*     */ public class SystemIntegrityChecker
/*     */ {
/*  36 */   private static final String CLASS_NAME = SystemIntegrityChecker.class
/*  37 */     .getName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String HEAD_TAG_START = "<";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String HEAD_TAG_END = ">";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String TAIL_TAG_START = "</";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String TAIL_TAG_END = ">";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String HEAD_INTEGRITY_CHECK_MESSAGE = "<IntegrityCheckMessage>";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String TAIL_INTEGRITY_CHECK_MESSAGE = "</IntegrityCheckMessage>";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ENCODING_TAG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CERTIFICATES_TAG_START = "<CertificateIntegrityCheck>";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CERTIFICATES_TAG_END = "</CertificateIntegrityCheck>";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CHECK_RESULT_TAG_START = "<IntegrityCheckResult>";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CHECK_RESULT_TAG_END = "</IntegrityCheckResult>";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String TOTAL_CERTIFICATES_CHECKED_TAG_START = "<TotalCertificatesChecked>";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String TOTAL_CERTIFICATES_CHECKED_TAG_END = "</TotalCertificatesChecked>";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String TOTAL_CERTIFICATES_VALID_TAG_START = "<TotalValidCertificates>";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String TOTAL_CERTIFICATES_VALID_TAG_END = "</TotalValidCertificates>";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String TOTAL_CERTIFICATES_INVALID_TAG_START = "<TotalInvalidCertificates>";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String TOTAL_CERTIFICATES_INVALID_TAG_END = "</TotalInvalidCertificates>";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CHECK_RESULT_OK = "OK";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CHECK_RESULT_FAILED = "Failed";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOG_ROOT = "RootCertificate";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOG_ROOT_LINK = "RootLinkCertificate";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOG_BACKEND = "BackendCertificate";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOG_BACKEND_LINK = "BackendLinkCertificate";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOG_ECU = "ECUCertificate";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOG_DIAGNOSTIC = "DiagnosticAuthenticationCertificate";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOG_ENHANCED_RIGHTS = "EnhancedRightsCertificate";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOG_TIME = "TimeCertificate";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOG_VARIANT_CODE_USER = "VariantCodeUserCertificate";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOG_VARIANT_CODING_DEVICE = "VariantCodingDeviceCertificate";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Session session;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Logger logger;
/*     */ 
/*     */ 
/*     */   
/*     */   private MetadataManager i18n;
/*     */ 
/*     */ 
/*     */   
/*     */   private CertificateRepository repo;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public SystemIntegrityChecker(CertificateRepository repo, Session session, MetadataManager i18n, Logger logger) {
/* 207 */     this.session = session;
/* 208 */     this.i18n = i18n;
/* 209 */     this.logger = logger;
/* 210 */     this.repo = repo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkSystemIntegrity(User currentUser, SystemIntegrityCheckResult sessionIntegrityCheckResult) {
/* 220 */     String METHOD_NAME = "checkSystemIntegrity";
/* 221 */     this.logger.entering(CLASS_NAME, "checkSystemIntegrity");
/*     */     
/* 223 */     checkCertificatesIntegrity(currentUser.getCertificates(), sessionIntegrityCheckResult);
/*     */     
/* 225 */     this.logger.exiting(CLASS_NAME, "checkSystemIntegrity");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkCertificatesIntegrity(List<Certificate> certificatesList, SystemIntegrityCheckResult sessionIntegrityCheckResult) {
/* 235 */     StringBuilder xmlReport = new StringBuilder();
/* 236 */     AtomicInteger totalNumberOfCertificates = new AtomicInteger(0);
/* 237 */     List<SystemIntegrityCheckError> errors = new ArrayList<>();
/*     */     
/* 239 */     for (Certificate root : certificatesList) {
/* 240 */       Optional<SystemIntegrityCheckError> resultOptional = prepareValidationResult(totalNumberOfCertificates, root);
/* 241 */       resultOptional.ifPresent(errors::add);
/* 242 */       List<Certificate> flattenedRoot = (List<Certificate>)root.flattened().collect(Collectors.toList());
/* 243 */       xmlReport.append(createReport(flattenedRoot, root, errors, totalNumberOfCertificates));
/*     */     } 
/*     */     
/* 246 */     String report = xmlReport.toString();
/* 247 */     xmlReport.delete(0, xmlReport.length());
/* 248 */     xmlReport.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
/* 249 */     xmlReport.append("<CertificateIntegrityCheck>");
/*     */     
/* 251 */     appendToXML(xmlReport, "<IntegrityCheckResult>", "</IntegrityCheckResult>", 
/* 252 */         !errors.isEmpty() ? "Failed" : "OK");
/*     */     
/* 254 */     appendToXML(xmlReport, "<TotalCertificatesChecked>", "</TotalCertificatesChecked>", 
/*     */         
/* 256 */         Integer.valueOf(totalNumberOfCertificates.get()));
/*     */     
/* 258 */     appendToXML(xmlReport, "<TotalValidCertificates>", "</TotalValidCertificates>", 
/*     */         
/* 260 */         Integer.valueOf(totalNumberOfCertificates.get() - errors.size()));
/*     */     
/* 262 */     appendToXML(xmlReport, "<TotalInvalidCertificates>", "</TotalInvalidCertificates>", 
/* 263 */         Integer.valueOf(errors.size()));
/*     */     
/* 265 */     xmlReport.append(report);
/* 266 */     xmlReport.append("</CertificateIntegrityCheck>");
/* 267 */     sessionIntegrityCheckResult.updateIntegrityCheckXML(xmlReport.toString());
/* 268 */     sessionIntegrityCheckResult.updateErrosList(errors);
/* 269 */     sessionIntegrityCheckResult.updateTotalNumberOfCheckedCertificates(totalNumberOfCertificates.get());
/* 270 */     this.repo.clearContext();
/*     */   }
/*     */ 
/*     */   
/*     */   private void appendToXML(StringBuilder xmlReport, String tagStart, String tagEnd, Object value) {
/* 275 */     xmlReport.append(tagStart).append(value).append(tagEnd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Optional<SystemIntegrityCheckError> prepareValidationResult(AtomicInteger totalNumberOfCertificates, Certificate certificate) {
/* 282 */     totalNumberOfCertificates.incrementAndGet();
/* 283 */     return validateCertificate(certificate);
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
/*     */   private void checkParentSubjectKeyMatchesChildAuthKey(Certificate certificate, List<SystemIntegrityCheckError> errors) {
/* 295 */     if (certificate.getParent() != null) {
/* 296 */       if (certificate
/* 297 */         .getType() == CertificateType.ENHANCED_RIGHTS_CERTIFICATE) {
/* 298 */         Certificate parentBackend = certificate.getParent().getParent();
/* 299 */         if (parentBackend != null && 
/* 300 */           !certificate.getAuthorityKeyIdentifier().equals(parentBackend
/* 301 */             .getSubjectKeyIdentifier())) {
/* 302 */           addKeysNotMatchError(certificate, errors, "parentSubjectKeyNotMatchChildAuthKey");
/*     */         }
/*     */       }
/* 305 */       else if (!certificate.getAuthorityKeyIdentifier().equals(certificate
/* 306 */           .getParent().getSubjectKeyIdentifier())) {
/* 307 */         addKeysNotMatchError(certificate, errors, "parentSubjectKeyNotMatchChildAuthKey");
/*     */       } 
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
/*     */   private void checkRootLinkSubjectKeyIdentifier(Certificate certificate, List<SystemIntegrityCheckError> errors) {
/* 322 */     if (certificate.getParent() != null && 
/* 323 */       !certificate.getSubjectKeyIdentifier().equals(certificate
/* 324 */         .getParent().getSubjectKeyIdentifier())) {
/* 325 */       addKeysNotMatchError(certificate, errors, "parentSubjectKeyNotMatchChildSubjKey");
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
/*     */   private void addKeysNotMatchError(Certificate certificate, List<SystemIntegrityCheckError> errors, String messageId) {
/* 337 */     String errorMessage = this.i18n.getMessage(messageId);
/* 338 */     AtomicBoolean certificateIdHasErrors = new AtomicBoolean(false);
/* 339 */     errors.forEach(error -> {
/*     */           if (error.getCertificateId().equals(certificate.getEntityId())) {
/*     */             error.getErrorMessages().add(errorMessage);
/*     */             
/*     */             error.getMessageIds().add(messageId);
/*     */             certificateIdHasErrors.set(true);
/*     */           } 
/*     */         });
/* 347 */     if (!certificateIdHasErrors.get()) {
/* 348 */       errors.add(new SystemIntegrityCheckError(certificate.getEntityId(), Collections.singletonList(errorMessage), Collections.singletonList(messageId)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Optional<SystemIntegrityCheckError> validateCertificate(Certificate certificate) {
/* 359 */     List<Optional<ValidationError>> validationResult = CertificatesValidator.extendedValidation(certificate, this.logger, this.i18n);
/* 360 */     RawData issuerData = getIssuerCertificateData(certificate);
/* 361 */     RawData toBeVerifiedData = certificate.getCertificateData();
/*     */     
/* 363 */     Optional<ValidationError> signatureVerificationOptional = verifySignature(certificate, issuerData, toBeVerifiedData);
/* 364 */     if (signatureVerificationOptional.isPresent()) {
/* 365 */       validationResult.add(signatureVerificationOptional);
/*     */     }
/*     */     
/* 368 */     if (isUnderBackend(certificate) && !CertificatesValidator.checkPublicKeyMatchesTheOneGeneratedFromPrivateKey(this.session, certificate, this.logger)) {
/* 369 */       validationResult.add(Optional.of(new ValidationError(certificate
/* 370 */               .getSubjectKeyIdentifier(), this.i18n
/* 371 */               .getMessage("invalidOrMissingPrivateKey", new String[] {
/*     */ 
/*     */                   
/* 374 */                   certificate.getSubjectKeyIdentifier()
/*     */                 
/* 376 */                 }), "invalidOrMissingPrivateKey", new String[] { certificate.getSubjectKeyIdentifier() })));
/*     */     }
/*     */     
/* 379 */     return saveErrorMessage(validationResult, certificate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isUnderBackend(Certificate certificate) {
/* 389 */     List<CertificateType> underBackend = Arrays.asList(new CertificateType[] { CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE, CertificateType.ECU_CERTIFICATE, CertificateType.TIME_CERTIFICATE, CertificateType.VARIANT_CODING_DEVICE_CERTIFICATE, CertificateType.VARIANT_CODE_USER_CERTIFICATE });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 395 */     return underBackend.contains(certificate.getType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RawData getIssuerCertificateData(Certificate certificate) {
/*     */     RawData issuerData;
/* 406 */     if (certificate.getParent() == null) {
/* 407 */       issuerData = certificate.getCertificateData();
/* 408 */     } else if (certificate
/* 409 */       .getType() == CertificateType.ROOT_CA_LINK_CERTIFICATE) {
/* 410 */       List<Certificate> roots = certificate.getUser().getCertificates();
/* 411 */       Optional<Certificate> linked = ChainOfTrust.getParentOfLinkCertificate(roots, certificate);
/* 412 */       issuerData = ((Certificate)linked.orElse(certificate)).getCertificateData();
/* 413 */     } else if (certificate
/* 414 */       .getType() == CertificateType.BACKEND_CA_LINK_CERTIFICATE) {
/*     */       
/* 416 */       List<Certificate> backends = certificate.getParent().getParent().getChildren();
/* 417 */       Optional<Certificate> linked = ChainOfTrust.getParentOfLinkCertificate(backends, certificate);
/* 418 */       issuerData = ((Certificate)linked.orElse(certificate)).getCertificateData();
/*     */     }
/* 420 */     else if (certificate.getCertificateData().isCertificate()) {
/* 421 */       issuerData = certificate.getParent().getCertificateData();
/*     */     } else {
/*     */       
/* 424 */       issuerData = certificate.getParent().getParent().getCertificateData();
/*     */     } 
/* 426 */     return issuerData;
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
/*     */   private Optional<ValidationError> verifySignature(Certificate certificate, RawData issuerData, RawData toBeVerifiedData) {
/* 438 */     if (!CertificatesValidator.verifySignature(issuerData, toBeVerifiedData, this.logger)) {
/* 439 */       return Optional.of(new ValidationError(certificate
/* 440 */             .getSubjectKeyIdentifier(), this.i18n
/* 441 */             .getMessage("sigVerificationFailedSubjKey", new String[] {
/*     */ 
/*     */                 
/* 444 */                 certificate.getSubjectKeyIdentifier()
/*     */               
/* 446 */               }), "sigVerificationFailedSubjKey", new String[] { certificate.getSubjectKeyIdentifier() }));
/*     */     }
/* 448 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Optional<SystemIntegrityCheckError> saveErrorMessage(List<Optional<ValidationError>> validationResult, Certificate certificate) {
/* 458 */     List<String> errorMessages = new ArrayList<>();
/* 459 */     List<String> messageIds = new ArrayList<>();
/* 460 */     validationResult.forEach(error -> error.ifPresent(()));
/*     */ 
/*     */ 
/*     */     
/* 464 */     if (!errorMessages.isEmpty()) {
/* 465 */       return Optional.of(new SystemIntegrityCheckError(certificate.getEntityId(), errorMessages, messageIds));
/*     */     }
/* 467 */     return Optional.empty();
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
/*     */   private StringBuilder createReport(List<Certificate> certificates, Certificate root, List<SystemIntegrityCheckError> errors, AtomicInteger totalNumberOfCertificates) {
/* 485 */     StringBuilder xmlBuffer = new StringBuilder();
/* 486 */     boolean isBackendTagOpen = false;
/* 487 */     boolean isRootTagOpen = false;
/* 488 */     boolean isDiagnosticTagOpen = false;
/*     */     
/* 490 */     for (int i = 0; i < certificates.size(); i++) {
/* 491 */       Certificate certificate = certificates.get(i);
/* 492 */       getErrorsFromValidation(root, errors, totalNumberOfCertificates, certificate);
/*     */ 
/*     */       
/* 495 */       if (isDiagnosticTagOpen && shouldCloseDiagnosticTag(certificate)) {
/* 496 */         closeXMLTagIfParentChanged(CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE, xmlBuffer);
/*     */ 
/*     */         
/* 499 */         isDiagnosticTagOpen = false;
/*     */       } 
/*     */       
/* 502 */       if (isBackendTagOpen && shouldCloseBackendTag(certificate)) {
/* 503 */         closeXMLTagIfParentChanged(CertificateType.BACKEND_CA_CERTIFICATE, xmlBuffer);
/*     */         
/* 505 */         isBackendTagOpen = false;
/*     */       } 
/*     */       
/* 508 */       if (isRootTagOpen && isRootCertificate(certificate)) {
/* 509 */         closeXMLTagIfParentChanged(CertificateType.ROOT_CA_CERTIFICATE, xmlBuffer);
/*     */         
/* 511 */         isRootTagOpen = false;
/*     */       } 
/*     */       
/* 514 */       xmlBuffer.append("<");
/* 515 */       xmlBuffer.append(getCertificateTypeFromEnum(certificate.getType()));
/* 516 */       xmlBuffer.append(" id=\"").append(certificate.getEntityId())
/* 517 */         .append("\"");
/* 518 */       xmlBuffer.append(" name=\"").append(certificate.getSubject())
/* 519 */         .append("\"");
/* 520 */       xmlBuffer.append(">");
/* 521 */       if (!errors.isEmpty()) {
/* 522 */         xmlBuffer.append("<IntegrityCheckMessage>");
/* 523 */         ((List)errors.stream()
/* 524 */           .filter(error -> error.getCertificateId().equals(certificate.getEntityId()))
/*     */           
/* 526 */           .collect(Collectors.toList()))
/* 527 */           .forEach(error -> error.getErrorMessages().forEach(xmlBuffer::append));
/*     */         
/* 529 */         xmlBuffer.append("</IntegrityCheckMessage>");
/*     */       } 
/*     */       
/* 532 */       if (doesNotHaveChildrenIsNotRootOrBackendOrDiag(certificate)) {
/* 533 */         xmlBuffer.append("</").append(
/* 534 */             getCertificateTypeFromEnum(certificate.getType()))
/* 535 */           .append(">");
/*     */       }
/*     */       
/* 538 */       if (isRootCertificate(certificate)) {
/* 539 */         isRootTagOpen = true;
/* 540 */       } else if (isBackendCertificate(certificate)) {
/* 541 */         isBackendTagOpen = true;
/* 542 */       } else if (isDiagnosticCertificate(certificate)) {
/* 543 */         isDiagnosticTagOpen = true;
/*     */       } 
/*     */ 
/*     */       
/* 547 */       if (i == certificates.size() - 1) {
/* 548 */         if (isDiagnosticTagOpen) {
/* 549 */           closeXMLTagIfParentChanged(CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE, xmlBuffer);
/*     */         }
/*     */ 
/*     */         
/* 553 */         closeRootAndBackendTags(xmlBuffer, isBackendTagOpen);
/*     */       } 
/*     */     } 
/* 556 */     return xmlBuffer;
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
/*     */   private void getErrorsFromValidation(Certificate root, List<SystemIntegrityCheckError> errors, AtomicInteger totalNumberOfCertificates, Certificate certificate) {
/* 571 */     if (!certificate.getEntityId().equals(root.getEntityId()) && certificate
/* 572 */       .getState().equals(CertificateState.ISSUED)) {
/* 573 */       Optional<SystemIntegrityCheckError> resultOptional = prepareValidationResult(totalNumberOfCertificates, certificate);
/*     */       
/* 575 */       resultOptional.ifPresent(errors::add);
/* 576 */       if (certificate
/* 577 */         .getType() == CertificateType.ROOT_CA_LINK_CERTIFICATE || certificate
/*     */         
/* 579 */         .getType() == CertificateType.BACKEND_CA_LINK_CERTIFICATE) {
/* 580 */         checkRootLinkSubjectKeyIdentifier(certificate, errors);
/*     */       } else {
/* 582 */         checkParentSubjectKeyMatchesChildAuthKey(certificate, errors);
/*     */       } 
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
/*     */   private void closeXMLTagIfParentChanged(CertificateType certificateType, StringBuilder xmlBuffer) {
/* 596 */     xmlBuffer.append("</")
/* 597 */       .append(getCertificateTypeFromEnum(certificateType))
/* 598 */       .append(">");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void closeRootAndBackendTags(StringBuilder xmlBuffer, boolean isBackendTagOpen) {
/* 609 */     if (isBackendTagOpen) {
/* 610 */       xmlBuffer.append("</")
/* 611 */         .append(getCertificateTypeFromEnum(CertificateType.BACKEND_CA_CERTIFICATE))
/*     */         
/* 613 */         .append(">");
/*     */     }
/* 615 */     xmlBuffer.append("</")
/* 616 */       .append(getCertificateTypeFromEnum(CertificateType.ROOT_CA_CERTIFICATE))
/*     */       
/* 618 */       .append(">");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean shouldCloseBackendTag(Certificate nextCertificate) {
/* 628 */     return (isBackendCertificate(nextCertificate) || 
/* 629 */       isRootCertificate(nextCertificate));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean shouldCloseDiagnosticTag(Certificate nextCertificate) {
/* 640 */     return isNotEnhancedRightsCertificate(nextCertificate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isRootCertificate(Certificate certificate) {
/* 650 */     return (certificate.getType() == CertificateType.ROOT_CA_CERTIFICATE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isBackendCertificate(Certificate certificate) {
/* 660 */     return (certificate.getType() == CertificateType.BACKEND_CA_CERTIFICATE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isNotEnhancedRightsCertificate(Certificate certificate) {
/* 671 */     return 
/* 672 */       (certificate.getType() != CertificateType.ENHANCED_RIGHTS_CERTIFICATE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isDiagnosticCertificate(Certificate certificate) {
/* 683 */     return 
/* 684 */       (certificate.getType() == CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE);
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
/*     */   private boolean doesNotHaveChildrenIsNotRootOrBackendOrDiag(Certificate certificate) {
/* 696 */     return (certificate.getChildren().isEmpty() && certificate
/*     */       
/* 698 */       .getType() != CertificateType.BACKEND_CA_CERTIFICATE && certificate
/* 699 */       .getType() != CertificateType.ROOT_CA_CERTIFICATE && certificate
/*     */       
/* 701 */       .getType() != CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE);
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
/*     */   private String getCertificateTypeFromEnum(CertificateType type) {
/* 713 */     switch (type) {
/*     */       case BACKEND_CA_CERTIFICATE:
/* 715 */         return "BackendCertificate";
/*     */       
/*     */       case BACKEND_CA_LINK_CERTIFICATE:
/* 718 */         return "BackendLinkCertificate";
/*     */       
/*     */       case ROOT_CA_CERTIFICATE:
/* 721 */         return "RootCertificate";
/*     */       
/*     */       case ROOT_CA_LINK_CERTIFICATE:
/* 724 */         return "RootLinkCertificate";
/*     */       
/*     */       case DIAGNOSTIC_AUTHENTICATION_CERTIFICATE:
/* 727 */         return "DiagnosticAuthenticationCertificate";
/*     */       
/*     */       case ECU_CERTIFICATE:
/* 730 */         return "ECUCertificate";
/*     */       
/*     */       case ENHANCED_RIGHTS_CERTIFICATE:
/* 733 */         return "EnhancedRightsCertificate";
/*     */       
/*     */       case TIME_CERTIFICATE:
/* 736 */         return "TimeCertificate";
/*     */       
/*     */       case VARIANT_CODE_USER_CERTIFICATE:
/* 739 */         return "VariantCodeUserCertificate";
/*     */       
/*     */       case VARIANT_CODING_DEVICE_CERTIFICATE:
/* 742 */         return "VariantCodingDeviceCertificate";
/*     */     } 
/*     */     
/* 745 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\SystemIntegrityChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */