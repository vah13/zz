/*      */ package com.daimler.cebas.certificates.control.importing;
/*      */ 
/*      */ import com.daimler.cebas.certificates.control.SearchEngine;
/*      */ import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
/*      */ import com.daimler.cebas.certificates.control.chain.ChainIdentifier;
/*      */ import com.daimler.cebas.certificates.control.chain.ChainOfTrustManager;
/*      */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*      */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*      */ import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
/*      */ import com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory;
/*      */ import com.daimler.cebas.certificates.control.repository.CertificateRepository;
/*      */ import com.daimler.cebas.certificates.control.validation.BaseStoreUtil;
/*      */ import com.daimler.cebas.certificates.control.validation.CertificatesValidator;
/*      */ import com.daimler.cebas.certificates.control.validation.ValidationError;
/*      */ import com.daimler.cebas.certificates.control.vo.ImportInput;
/*      */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*      */ import com.daimler.cebas.certificates.entity.Certificate;
/*      */ import com.daimler.cebas.certificates.entity.CertificateType;
/*      */ import com.daimler.cebas.common.CryptoTools;
/*      */ import com.daimler.cebas.common.control.CEBASProperty;
/*      */ import com.daimler.cebas.common.control.MetadataManager;
/*      */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*      */ import com.daimler.cebas.common.entity.AbstractEntity;
/*      */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*      */ import com.daimler.cebas.configuration.entity.Configuration;
/*      */ import com.daimler.cebas.logs.control.Logger;
/*      */ import com.daimler.cebas.users.entity.User;
/*      */ import java.io.IOException;
/*      */ import java.nio.file.DirectoryStream;
/*      */ import java.nio.file.Path;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import java.util.stream.Collectors;
/*      */ import org.springframework.beans.factory.annotation.Autowired;
/*      */ import org.springframework.beans.factory.annotation.Value;
/*      */ import org.springframework.transaction.annotation.Propagation;
/*      */ import org.springframework.transaction.annotation.Transactional;
/*      */ import org.springframework.web.util.HtmlUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @CEBASControl
/*      */ public class CertificatesImporter
/*      */ {
/*      */   private static final String SPACE = " ";
/*   69 */   private static final String CLASS_NAME = CertificatesImporter.class.getSimpleName();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   74 */   private static final Logger LOG = Logger.getLogger(CLASS_NAME);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String MESSAGE_SEPARATOR = ": ";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String COMMNA_AND_SPACE = ", ";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String EMPTY = "";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Logger logger;
/*      */ 
/*      */ 
/*      */   
/*      */   private AbstractCertificateFactory factory;
/*      */ 
/*      */ 
/*      */   
/*      */   private CertificateRepository repository;
/*      */ 
/*      */ 
/*      */   
/*      */   private AbstractConfigurator configurator;
/*      */ 
/*      */ 
/*      */   
/*      */   private ChainOfTrustManager chainOfTrustManager;
/*      */ 
/*      */ 
/*      */   
/*      */   private SearchEngine searchEngine;
/*      */ 
/*      */ 
/*      */   
/*      */   private MetadataManager i18n;
/*      */ 
/*      */ 
/*      */   
/*      */   private CertificatesConfiguration profileConfiguration;
/*      */ 
/*      */ 
/*      */   
/*  126 */   private ReentrantLock lockBaseStore = new ReentrantLock();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Value("${PKCS12_DEFAULT_PASSWORD}")
/*      */   private String pkcs12DefaultPassword;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Autowired
/*      */   public CertificatesImporter(AbstractCertificateFactory factory, CertificateRepository repo, AbstractConfigurator configurator, ChainOfTrustManager chainOfTrustManager, Logger logger, SearchEngine searchEngine, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
/*  158 */     this.factory = factory;
/*  159 */     this.repository = repo;
/*  160 */     this.configurator = configurator;
/*  161 */     this.chainOfTrustManager = chainOfTrustManager;
/*  162 */     this.logger = logger;
/*  163 */     this.searchEngine = searchEngine;
/*  164 */     this.i18n = i18n;
/*  165 */     this.profileConfiguration = profileConfiguration;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Transactional(propagation = Propagation.MANDATORY)
/*      */   public List<Certificate> importFromBaseStore(Map<Path, List<Path>> mappedPaths, User user) {
/*  179 */     String METHOD_NAME = "importFromBaseStore";
/*  180 */     this.logger.entering(CLASS_NAME, "importFromBaseStore");
/*  181 */     List<Certificate> defaultUserCertificates = new ArrayList<>();
/*      */     try {
/*  183 */       mappedPaths.entrySet().forEach(entry -> importFromPath(user, defaultUserCertificates, entry));
/*  184 */     } catch (CertificateNotFoundException e) {
/*  185 */       this.logger.logToFileOnly(CLASS_NAME, "Certificate could not be accessed.", (Throwable)e);
/*      */     } finally {
/*  187 */       BaseStoreUtil.closeFileSystem();
/*      */     } 
/*  189 */     this.logger.exiting(CLASS_NAME, "importFromBaseStore");
/*  190 */     return defaultUserCertificates;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Transactional(propagation = Propagation.MANDATORY)
/*      */   public List<Certificate> restoreCertificatesFromBaseStore(Map<Path, List<Path>> mappedPaths, User user) {
/*  204 */     String METHOD_NAME = "restoreCertificatesFromBaseStore";
/*  205 */     this.logger.entering(CLASS_NAME, "restoreCertificatesFromBaseStore");
/*  206 */     List<Certificate> restoredUserCertificates = new ArrayList<>();
/*      */     try {
/*  208 */       Set<Map.Entry<Path, List<Path>>> entrySet = mappedPaths.entrySet();
/*  209 */       entrySet.forEach(entry -> restoreFromPath(user, restoredUserCertificates, entry));
/*      */     } finally {
/*  211 */       BaseStoreUtil.closeFileSystem();
/*      */     } 
/*  213 */     this.logger.exiting(CLASS_NAME, "restoreCertificatesFromBaseStore");
/*  214 */     return restoredUserCertificates;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Comparator<CertificatePrivateKeyHolder> getSortComparator() {
/*  223 */     return Comparator.comparingInt(CertificatePrivateKeyHolder::getLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Transactional(propagation = Propagation.MANDATORY)
/*      */   public List<Certificate> importFromBaseStore(User user) {
/*  231 */     String METHOD_NAME = "importFromBaseStore";
/*  232 */     this.logger.entering(CLASS_NAME, "importFromBaseStore");
/*  233 */     Map<Path, List<Path>> mappedPaths = getPathForBaseStoreCertificates();
/*  234 */     this.logger.exiting(CLASS_NAME, "importFromBaseStore");
/*  235 */     return importFromBaseStore(mappedPaths, user);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Transactional(propagation = Propagation.MANDATORY)
/*      */   public List<Certificate> restoreCertificatesFromBaseStore(User user) {
/*  247 */     String METHOD_NAME = "restoreCertificatesFromBaseStore";
/*  248 */     this.logger.entering(CLASS_NAME, "restoreCertificatesFromBaseStore");
/*      */     try {
/*  250 */       if (this.lockBaseStore.isLocked()) {
/*  251 */         throw new CEBASCertificateException("Restoring from base store already in progress");
/*      */       }
/*  253 */       this.lockBaseStore.lock();
/*  254 */       Map<Path, List<Path>> mappedPaths = getPathForBaseStoreCertificates();
/*  255 */       this.logger.exiting(CLASS_NAME, "restoreCertificatesFromBaseStore");
/*  256 */       return restoreCertificatesFromBaseStore(mappedPaths, user);
/*      */     } finally {
/*  258 */       if (this.lockBaseStore.getHoldCount() != 0) {
/*  259 */         this.lockBaseStore.unlock();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ImportResult checkImportIntoChain(CertificatePrivateKeyHolder holder, User user) {
/*      */     ImportResult result;
/*  275 */     String METHOD_NAME = "checkImportIntoChain";
/*  276 */     this.logger.entering(CLASS_NAME, "checkImportIntoChain");
/*      */     
/*  278 */     Certificate certificate = holder.getCertificate();
/*  279 */     if (CertificatesValidator.isUnknownType(certificate)) {
/*      */       
/*  281 */       result = new ImportResult(certificate.getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier(), this.i18n.getMessage("unknownCertificateType"), false);
/*      */     }
/*      */     else {
/*      */       
/*  285 */       List<Optional<ValidationError>> validationResult = CertificatesValidator.extendedValidation(certificate, this.logger, this.i18n);
/*      */       
/*  287 */       Optional<Optional<ValidationError>> errorResult = validationResult.stream().filter(Optional::isPresent).findFirst();
/*  288 */       if (errorResult.isPresent()) {
/*  289 */         StringBuilder buffer = new StringBuilder();
/*  290 */         validationResult
/*  291 */           .forEach(error -> error.ifPresent(()));
/*      */         
/*  293 */         result = new ImportResult(certificate.getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier(), buffer.toString(), false);
/*  294 */         LOG.log(Level.INFO, result.getMessage());
/*      */       } else {
/*  296 */         List<ValidationError> chainOfTrustErorrs = this.chainOfTrustManager.checkChainOfTrust(user.getCertificates(), holder);
/*      */         
/*  298 */         StringBuilder errorMessage = new StringBuilder();
/*  299 */         if (chainOfTrustErorrs.isEmpty()) {
/*  300 */           LOG.log(Level.INFO, this.i18n
/*  301 */               .getEnglishMessage("certificateValidatesSuccessfully", new String[] {
/*  302 */                   certificate.getPKIRole(), certificate.getSubjectKeyIdentifier()
/*      */                 }));
/*      */           
/*  305 */           result = new ImportResult(certificate.getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier(), this.i18n.getMessage("certificateValidatesSuccessfully", new String[] {
/*  306 */                   certificate.getPKIRole(), certificate.getSubjectKeyIdentifier()
/*      */                 }), true);
/*      */         } else {
/*  309 */           chainOfTrustErorrs.forEach(error -> errorMessage.append(error.getErrorMessage()).append(" "));
/*  310 */           LOG.log(Level.WARNING, this.i18n
/*  311 */               .getEnglishMessage("chainOfTrustValidationFailed", new String[] {
/*  312 */                   errorMessage.toString()
/*      */                 }));
/*  314 */           result = new ImportResult(certificate.getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier(), errorMessage.toString(), false);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  319 */     this.logger.exiting(CLASS_NAME, "checkImportIntoChain");
/*  320 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ImportResult> importCertificates(List<ImportInput> inputList, User user, boolean onlyFromPKI, boolean allowPrivateKeys) {
/*  336 */     return onlyFromPKI ? importCertificatesUnderPKIChain(user, inputList, allowPrivateKeys) : importCertificates(inputList, user, allowPrivateKeys);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ImportResult> importCertificates(List<ImportInput> inputList, User user, boolean allowPrivateKeys) {
/*  350 */     String METHOD_NAME = "importCertificates";
/*  351 */     this.logger.entering(CLASS_NAME, "importCertificates");
/*      */     
/*  353 */     List<ImportResult> result = new ArrayList<>();
/*  354 */     List<CertificatePrivateKeyHolder> certificates = getCertificatesToImport(user, inputList, result, allowPrivateKeys);
/*      */     try {
/*  356 */       certificates.sort(getSortComparator());
/*  357 */       certificates.forEach(holder -> importCertificate(user, result, holder, false));
/*      */     } finally {
/*  359 */       certificates.forEach(holder -> CryptoTools.destroyPrivateKey(holder.getPrivateKey()));
/*      */     } 
/*      */     
/*  362 */     this.logger.exiting(CLASS_NAME, "importCertificates");
/*  363 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ImportResult> importCertificatesUnderPKIChain(User user, List<ImportInput> inputList, boolean allowPrivateKeys) {
/*  377 */     String METHOD_NAME = "importCertificates";
/*  378 */     this.logger.entering(CLASS_NAME, "importCertificates");
/*      */     
/*  380 */     List<ImportResult> result = new ArrayList<>();
/*  381 */     List<CertificatePrivateKeyHolder> certificates = getCertificatesToImport(user, inputList, result, allowPrivateKeys);
/*      */     try {
/*  383 */       certificates.sort(getSortComparator());
/*  384 */       certificates.forEach(holder -> importCertificate(user, result, holder, true));
/*      */     } finally {
/*  386 */       certificates.forEach(holder -> CryptoTools.destroyPrivateKey(holder.getPrivateKey()));
/*      */     } 
/*      */     
/*  389 */     this.logger.exiting(CLASS_NAME, "importCertificates");
/*  390 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<CertificatePrivateKeyHolder> getCertificatesToImport(User user, List<ImportInput> inputList, List<ImportResult> result, boolean allowPrivateKeys) {
/*  406 */     List<Optional<CertificatePrivateKeyHolder>> optionals = new ArrayList<>();
/*  407 */     inputList.forEach(input -> optionals.addAll(getCertificatePrivateKeyOptional(user, input, result)));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  412 */     List<CertificatePrivateKeyHolder> certificates = (List<CertificatePrivateKeyHolder>)optionals.stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
/*      */ 
/*      */ 
/*      */     
/*  416 */     List<CertificatePrivateKeyHolder> notAllowedPrivateKeys = (List<CertificatePrivateKeyHolder>)certificates.stream().filter(c -> (c.hasPrivateKey() && !allowPrivateKeys)).collect(Collectors.toList());
/*      */     
/*  418 */     notAllowedPrivateKeys.forEach(c -> errorMesgPKNotAllowed(result, c));
/*  419 */     certificates.removeAll(notAllowedPrivateKeys);
/*      */     
/*  421 */     return certificates;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*      */   public void importCertificate(User user, List<ImportResult> result, CertificatePrivateKeyHolder holder, boolean onlyFromPKI) {
/*  438 */     Certificate certificate = holder.getCertificate();
/*  439 */     Optional<Certificate> rolledBackCertificate = this.profileConfiguration.getMatchingRolledBackCertificate(certificate);
/*  440 */     if (CertificatesValidator.isUnknownType(certificate)) {
/*  441 */       result.add(this.factory.getImportResult(holder, this.i18n.getMessage("unknownCertificateType"), false));
/*  442 */     } else if ((this.profileConfiguration.certificateImportNotAllowedInvalidFields(certificate.getType(), certificate
/*  443 */         .getTargetVIN(), certificate.getTargetECU())).length != 0) {
/*  444 */       addErrorResultAndLogImportNotAllowed(result, holder, certificate, 
/*  445 */           String.join(" ", (CharSequence[])this.profileConfiguration.certificateImportNotAllowedInvalidFields(certificate
/*  446 */               .getType(), certificate.getTargetVIN(), certificate.getTargetECU())), new String[0]);
/*      */     }
/*  448 */     else if (this.profileConfiguration.isCertificateImportNotAllowed(certificate)) {
/*  449 */       addErrorResultAndLogImportNotAllowed(result, holder, certificate, "invalidCertificateType", new String[] { certificate
/*  450 */             .getType().name() });
/*  451 */     } else if (!this.profileConfiguration.isSubjectValid(certificate)) {
/*  452 */       addErrorResult(result, holder, "certSubjDoesNotMatchConfigSubj", new String[] { certificate
/*  453 */             .getSubject(), certificate.getSerialNo(), certificate
/*  454 */             .getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier() });
/*  455 */       logSubjectNotTheSame(certificate);
/*  456 */     } else if (rolledBackCertificate.isPresent()) {
/*  457 */       addErrorResultAndLogImportNotAllowed(result, holder, certificate, "certificateImportNotPossibleRollbackActive", new String[] { ((Certificate)rolledBackCertificate
/*      */             
/*  459 */             .get()).getSubjectKeyIdentifier() });
/*  460 */     } else if (!this.profileConfiguration.isUserRoleValid(certificate)) {
/*  461 */       addErrorResult(result, holder, "importInvalidUserRole", new String[] { certificate
/*  462 */             .getUserRole() });
/*      */     } else {
/*  464 */       processImport(user, result, holder, certificate, onlyFromPKI);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void processImport(User user, List<ImportResult> result, CertificatePrivateKeyHolder holder, Certificate certificate, boolean onlyFromPKI) {
/*      */     List<Optional<ValidationError>> validationResult;
/*  478 */     if (this.profileConfiguration.shouldDoExtendedValidation()) {
/*  479 */       validationResult = CertificatesValidator.extendedValidation(certificate, this.logger, this.i18n);
/*      */     } else {
/*  481 */       Optional<Configuration> deleteExpiredCertsConfiguration = getDeleteExpiredCertsConfiguration(user);
/*      */       
/*  483 */       validationResult = CertificatesValidator.basicValidation(certificate, (deleteExpiredCertsConfiguration
/*  484 */           .isPresent() && 
/*  485 */           Boolean.valueOf(((Configuration)deleteExpiredCertsConfiguration.get()).getConfigValue()).booleanValue()), this.logger, this.i18n);
/*      */     } 
/*      */     
/*  488 */     CertificateType certificateType = determinateAttributeCertificateTypeForLogging(certificate);
/*      */     
/*  490 */     Optional<Optional<ValidationError>> errorResult = validationResult.stream().filter(Optional::isPresent).findFirst();
/*  491 */     if (errorResult.isPresent()) {
/*  492 */       extractErrorResults(result, holder, validationResult, certificateType);
/*      */     } else {
/*  494 */       addCertificateToUserStore(user, result, holder, certificateType, onlyFromPKI);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addCertificateToUserStore(User user, List<ImportResult> result, CertificatePrivateKeyHolder holder, CertificateType certificateType, boolean onlyFromPKI) {
/*  514 */     Certificate certificate = holder.getCertificate();
/*  515 */     List<ValidationError> chainOfTrustErrors = this.chainOfTrustManager.addCertificateToUserStore(user.getCertificates(), holder, onlyFromPKI);
/*  516 */     if (chainOfTrustErrors.isEmpty()) {
/*  517 */       this.repository.update((AbstractEntity)user);
/*  518 */       this.logger.log(Level.INFO, "000117", this.i18n
/*  519 */           .getEnglishMessage("certificateImportedSuccessfullyDetailMessage", new String[] {
/*  520 */               this.i18n.getEnglishMessage(certificateType.getLanguageProperty()), certificate
/*  521 */               .getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier(), certificate
/*  522 */               .getSerialNo()
/*  523 */             }), getClass().getSimpleName());
/*  524 */       ImportResult importResult = this.factory.getImportResult(holder, this.i18n
/*  525 */           .getMessage("certificateImportedSuccessfully", new String[] {
/*  526 */               this.i18n.getMessage(certificateType.getLanguageProperty()), certificate
/*  527 */               .getSubjectKeyIdentifier()
/*      */             }), true);
/*  529 */       List<ChainIdentifier> possibleReplacedCertificates = holder.getPossibleReplacedCertificates();
/*  530 */       if (!possibleReplacedCertificates.isEmpty()) {
/*  531 */         StringBuilder builder = new StringBuilder();
/*  532 */         for (ChainIdentifier chainIdentifier : possibleReplacedCertificates) {
/*  533 */           builder.append(" ")
/*  534 */             .append(this.i18n.getMessage("warningRemovedDuringImport", new String[] {
/*  535 */                   chainIdentifier.getAuthorityKeyIdentifier(), chainIdentifier
/*  536 */                   .getSubjectKeyIdentifier(), chainIdentifier
/*  537 */                   .getSerialNo() }));
/*      */         } 
/*  539 */         importResult.setMessage(importResult.getMessage() + builder.toString());
/*      */       } 
/*  541 */       result.add(importResult);
/*      */     } else {
/*      */       
/*  544 */       StringBuilder errorMessage = new StringBuilder();
/*  545 */       StringBuilder englishMessages = new StringBuilder();
/*  546 */       chainOfTrustErrors.forEach(error -> {
/*      */             errorMessage.append(error.getErrorMessage()).append(" ");
/*      */             englishMessages.append(this.i18n.getEnglishMessage(error.getMessageId(), error.getMessageArgs()));
/*      */           });
/*  550 */       this.logger.log(Level.WARNING, "000118X", this.i18n
/*  551 */           .getEnglishMessage("chainOfTrustImportingFailed", new String[] {
/*  552 */               englishMessages.toString()
/*  553 */             }), getClass().getSimpleName());
/*  554 */       result.add(this.factory.getImportResult(holder, errorMessage.toString(), false));
/*  555 */       chainOfTrustErrors.clear();
/*      */     } 
/*  557 */     this.repository.flush();
/*      */   }
/*      */ 
/*      */   
/*      */   private void errorMesgPKNotAllowed(List<ImportResult> result, CertificatePrivateKeyHolder c) {
/*  562 */     ImportResult invalidImportResult = new ImportResult(c.getFileName(), "", "", this.i18n.getMessage("notAllowedToImportPrivateKey", new String[] {
/*  563 */             c.getCertificate().getSubjectPublicKey(), c.getCertificate().getSubjectKeyIdentifier()
/*      */           }), false);
/*  565 */     result.add(invalidImportResult);
/*      */   }
/*      */   
/*      */   private void importFromPath(User user, List<Certificate> defaultUserCertificates, Map.Entry<Path, List<Path>> entry) {
/*  569 */     Certificate rootCertificate = createCertificateFromResourcePath(user, entry);
/*  570 */     if (shouldCheckExpired() && !CertificatesValidator.isExpired(rootCertificate, this.logger, this.i18n)) {
/*  571 */       addCheckedBackCertToRoot(user, entry, rootCertificate);
/*      */     } else {
/*  573 */       addUnCheckedBackCertToRoot(user, entry, rootCertificate);
/*      */     } 
/*  575 */     defaultUserCertificates.add(rootCertificate);
/*  576 */     user.getCertificates().add(rootCertificate);
/*  577 */     this.repository.create((AbstractEntity)rootCertificate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addErrorResultAndLogImportNotAllowed(List<ImportResult> result, CertificatePrivateKeyHolder holder, Certificate certificate, String messageId, String[] messageArgs) {
/*  596 */     addErrorResult(result, holder, messageId, messageArgs);
/*  597 */     this.profileConfiguration.logCertificateImportNotAllowed(certificate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<Path, List<Path>> getPathForBaseStoreCertificates() {
/*  606 */     Path topDir = BaseStoreUtil.getBaseStoreTopPath(getClass(), this.logger);
/*  607 */     Map<Path, List<Path>> mappedPaths = new HashMap<>();
/*  608 */     DirectoryStream<Path> topDirectories = null;
/*      */     try {
/*  610 */       topDirectories = BaseStoreUtil.getDirectoryStream(topDir, BaseStoreUtil::getRootDirectoriesFilter, this.logger);
/*  611 */       topDirectories.forEach(pathParent -> {
/*      */             List<Path> rootCertificatesPaths = new ArrayList<>();
/*      */             
/*      */             List<Path> backendFolderCertificatesPaths = new ArrayList<>();
/*      */             
/*      */             List<Path> backendCertificatesPaths = new ArrayList<>();
/*      */             
/*      */             processDirectoryStream(pathParent, rootCertificatesPaths, BaseStoreUtil::getRootFilesFilter, this.logger);
/*      */             if (!rootCertificatesPaths.isEmpty()) {
/*      */               Path rootPath = rootCertificatesPaths.get(0);
/*      */               processDirectoryStream(pathParent, backendFolderCertificatesPaths, BaseStoreUtil::getRootDirectoriesFilter, this.logger);
/*      */               backendFolderCertificatesPaths.forEach(());
/*      */               mappedPaths.put(rootPath, backendCertificatesPaths);
/*      */             } 
/*      */           });
/*  626 */     } catch (IOException e) {
/*  627 */       LOG.log(Level.FINEST, e.getMessage(), e);
/*  628 */       this.logger.log(Level.WARNING, "000168X", this.i18n.getEnglishMessage("failedToGetDirectoryStream", new String[] { topDir
/*  629 */               .toString() }), CLASS_NAME);
/*      */     } finally {
/*  631 */       if (topDirectories != null) {
/*      */         try {
/*  633 */           topDirectories.close();
/*  634 */         } catch (IOException e) {
/*  635 */           LOG.log(Level.FINEST, e.getMessage(), e);
/*  636 */           this.logger.log(Level.WARNING, "000168X", this.i18n
/*  637 */               .getEnglishMessage("failedToGetDirectoryStream", new String[] {
/*  638 */                   topDir.toString()
/*      */                 }), CLASS_NAME);
/*      */         } 
/*      */       }
/*      */     } 
/*  643 */     return mappedPaths;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addErrorResult(List<ImportResult> result, CertificatePrivateKeyHolder holder, String messageId, String[] messageArgs) {
/*  660 */     result.add(this.factory.getImportResult(holder, this.i18n.getMessage(messageId, messageArgs), false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void logSubjectNotTheSame(Certificate certificate) {
/*  670 */     this.logger.log(Level.WARNING, "000414X", this.i18n
/*  671 */         .getEnglishMessage("certSubjDoesNotMatchConfigSubj", new String[] {
/*  672 */             certificate.getSubject(), certificate.getSerialNo(), certificate
/*  673 */             .getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier()
/*  674 */           }), getClass().getSimpleName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void extractErrorResults(List<ImportResult> result, CertificatePrivateKeyHolder holder, List<Optional<ValidationError>> validationResult, CertificateType certificateType) {
/*  691 */     String ERROR_SEPARATOR = ", ";
/*  692 */     StringBuilder buffer = new StringBuilder();
/*  693 */     StringBuilder englishBuffer = new StringBuilder();
/*  694 */     Certificate certificate = holder.getCertificate();
/*  695 */     validationResult.forEach(error -> error.ifPresent(()));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  703 */     String resultErrorMessage = this.i18n.getMessage("certificateStructureValidationFailed", new String[] { this.i18n.getMessage(certificateType.getLanguageProperty()), certificate.getSerialNo(), certificate.getAuthorityKeyIdentifier() }) + buffer.toString().substring(0, buffer.toString().length() - ", ".length());
/*  704 */     result.add(this.factory.getImportResult(holder, resultErrorMessage, false));
/*  705 */     this.logger.log(Level.WARNING, "000114X", this.i18n
/*  706 */         .getEnglishMessage("certificateStructureValidationFailed", new String[] {
/*      */             
/*  708 */             certificate.getType().name(), certificate.getSerialNo(), certificate
/*  709 */             .getAuthorityKeyIdentifier()
/*  710 */           }) + englishBuffer.toString().substring(0, englishBuffer
/*  711 */           .toString().length() - ", ".length()), 
/*  712 */         getClass().getSimpleName());
/*  713 */     validationResult.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<Optional<CertificatePrivateKeyHolder>> getCertificatePrivateKeyOptional(User user, ImportInput input, List<ImportResult> importResults) {
/*      */     try {
/*  731 */       List<Optional<CertificatePrivateKeyHolder>> createCertificateList = this.factory.createCertificatePrivateKeyHolder(input.getStream(), input.getBytes(), user, input.isPKCS12(), 
/*  732 */           input.hasPassword() ? input.getPassword() : this.pkcs12DefaultPassword, importResults, input
/*  733 */           .getFileName());
/*  734 */       if (createCertificateList.isEmpty() && !importResults.isEmpty())
/*      */       {
/*  736 */         if (input.isPKCS12()) {
/*  737 */           ImportResult invalidItem = importResults.get(importResults.size() - 1);
/*  738 */           String originalMessage = invalidItem.getMessage();
/*  739 */           invalidItem.setMessage(this.i18n.getMessage("cannotReadFile") + ": " + 
/*  740 */               HtmlUtils.htmlEscape(input.getFileName()) + ", " + originalMessage);
/*      */         } 
/*      */       }
/*  743 */       return createCertificateList;
/*  744 */     } catch (Exception e) {
/*  745 */       LOG.log(Level.FINEST, e.getMessage(), e);
/*      */ 
/*      */       
/*  748 */       ImportResult invalidImportResult = new ImportResult(input.getFileName(), "", "", this.i18n.getMessage("cannotReadFile") + ": " + HtmlUtils.htmlEscape(input.getFileName()) + ". " + e.getMessage(), false);
/*      */       
/*  750 */       importResults.add(invalidImportResult);
/*  751 */       return Collections.emptyList();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Optional<Configuration> getDeleteExpiredCertsConfiguration(User user) {
/*  763 */     return user.getConfigurations().stream()
/*  764 */       .filter(config -> config.getConfigKey().equals(CEBASProperty.DELETE_EXPIRED_CERTS.name())).findFirst();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processDirectoryStream(Path path, List<Path> pathList, Predicate<Path> predicate, Logger logger) {
/*  780 */     DirectoryStream<Path> directoryStream = null;
/*      */     try {
/*  782 */       directoryStream = BaseStoreUtil.getDirectoryStream(path, predicate::test, logger);
/*  783 */       directoryStream.forEach(pathList::add);
/*  784 */     } catch (IOException e) {
/*  785 */       LOG.log(Level.FINEST, e.getMessage(), e);
/*  786 */       logger.log(Level.WARNING, "000169X", this.i18n.getEnglishMessage("failedToProcessDirectoryStream", new String[] { path
/*  787 */               .toString() }), CLASS_NAME);
/*      */     } finally {
/*      */       try {
/*  790 */         if (directoryStream != null) {
/*  791 */           directoryStream.close();
/*      */         }
/*  793 */       } catch (IOException e) {
/*  794 */         LOG.log(Level.FINEST, e.getMessage(), e);
/*  795 */         logger.log(Level.WARNING, "000167X", "cannotCloseDirectoryStreamAfterProcessingImport", CLASS_NAME);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CertificateType determinateAttributeCertificateTypeForLogging(Certificate certificate) {
/*      */     CertificateType certificateType;
/*  810 */     if (certificate.isSecOCISCert()) {
/*  811 */       certificateType = CertificateType.SEC_OC_IS;
/*      */     } else {
/*  813 */       certificateType = certificate.getType();
/*  814 */       if (certificateType == null) {
/*  815 */         certificateType = CertificateType.NO_TYPE;
/*      */       }
/*      */     } 
/*  818 */     return certificateType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void restoreFromPath(User user, List<Certificate> restoredUserCertificates, Map.Entry<Path, List<Path>> entry) {
/*  832 */     Certificate rootCertificate = createCertificateFromResourcePath(user, entry);
/*  833 */     Optional<Certificate> optional = this.searchEngine.findCertByUserAndSignature(user, rootCertificate.getSignature());
/*  834 */     if (!optional.isPresent()) {
/*  835 */       if (shouldCheckExpired() && !CertificatesValidator.isExpired(rootCertificate, this.logger, this.i18n)) {
/*  836 */         addCheckedBackCertToRoot(user, entry, rootCertificate);
/*      */       } else {
/*  838 */         addUnCheckedBackCertToRoot(user, entry, rootCertificate);
/*      */       } 
/*  840 */       addNewRootWithBackCert(user, restoredUserCertificates, rootCertificate);
/*      */     } else {
/*  842 */       List<Certificate> restoredBackCertificates; Certificate userRootCertificate = optional.get();
/*      */       
/*  844 */       if (shouldCheckExpired() && !CertificatesValidator.isExpired(userRootCertificate, this.logger, this.i18n)) {
/*  845 */         restoredBackCertificates = getCheckedBackCert(user, entry, userRootCertificate);
/*      */       } else {
/*  847 */         restoredBackCertificates = getUnCheckedBackCert(entry, userRootCertificate, user);
/*      */       } 
/*  849 */       addRestoredBackCertForExistingRoot(user, restoredUserCertificates, userRootCertificate, restoredBackCertificates);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addUnCheckedBackCertToRoot(User user, Map.Entry<Path, List<Path>> entry, Certificate rootCertificate) {
/*  864 */     List<Certificate> backendCertificates = getUnCheckedBackCert(entry, rootCertificate, user);
/*  865 */     rootCertificate.getChildren().addAll(backendCertificates);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addCheckedBackCertToRoot(User user, Map.Entry<Path, List<Path>> entry, Certificate rootCertificate) {
/*  879 */     List<Certificate> unExpiredBackCertificates = getCheckedBackCert(user, entry, rootCertificate);
/*  880 */     rootCertificate.getChildren().addAll(unExpiredBackCertificates);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addRestoredBackCertForExistingRoot(User user, List<Certificate> restoredUserCertificates, Certificate userRootCertificate, List<Certificate> unExpiredBackCertificates) {
/*  899 */     List<Certificate> toBeAdded = (List<Certificate>)unExpiredBackCertificates.stream().filter(certificate -> !this.searchEngine.findCertByUserAndSignature(user, certificate.getSignature()).isPresent()).collect(Collectors.toList());
/*  900 */     userRootCertificate.getChildren().addAll(toBeAdded);
/*  901 */     restoredUserCertificates.addAll(toBeAdded);
/*  902 */     this.repository.update((AbstractEntity)userRootCertificate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<Certificate> getCheckedBackCert(User user, Map.Entry<Path, List<Path>> entry, Certificate userRootCertificate) {
/*  917 */     List<Certificate> backendCertificates = getUnCheckedBackCert(entry, userRootCertificate, user);
/*  918 */     return (List<Certificate>)backendCertificates.stream()
/*  919 */       .filter(certificate -> !CertificatesValidator.isExpired(certificate, this.logger, this.i18n))
/*  920 */       .collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<Certificate> getUnCheckedBackCert(Map.Entry<Path, List<Path>> entry, Certificate rootCertificate, User user) {
/*  933 */     return (List<Certificate>)((List)entry.getValue()).stream().map(path -> pathToBackend(rootCertificate, user, path))
/*  934 */       .collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Certificate pathToBackend(Certificate rootCertificate, User user, Path path) {
/*  949 */     String resource = path.toAbsolutePath().toString();
/*  950 */     if (!"file".equals(path.toUri().getScheme())) {
/*  951 */       resource = resource.replaceAll("/BOOT-INF/classes/", "");
/*      */     }
/*  953 */     Certificate backendCertificate = this.factory.createCertificate(resource, user);
/*  954 */     backendCertificate.setParent(rootCertificate);
/*  955 */     return this.profileConfiguration.getPkiKnownHandler().updateBackendPkiKnown(backendCertificate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Certificate createCertificateFromResourcePath(User user, Map.Entry<Path, List<Path>> entry) {
/*  968 */     String resourcePath = ((Path)entry.getKey()).toAbsolutePath().toString();
/*  969 */     if (!"file".equals(((Path)entry.getKey()).toUri().getScheme())) {
/*  970 */       resourcePath = resourcePath.replaceAll("/BOOT-INF/classes/", "");
/*      */     }
/*  972 */     Certificate rootCertificate = this.factory.createCertificate(resourcePath, user);
/*  973 */     rootCertificate.setParent(null);
/*  974 */     return rootCertificate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addNewRootWithBackCert(User user, List<Certificate> restoredUserCertificates, Certificate rootCertificate) {
/*  988 */     user.getCertificates().add(rootCertificate);
/*  989 */     this.repository.create((AbstractEntity)rootCertificate);
/*  990 */     restoredUserCertificates.add(rootCertificate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean shouldCheckExpired() {
/*  999 */     String METHOD_NAME = "shouldCheckExpired";
/* 1000 */     this.logger.entering(CLASS_NAME, "shouldCheckExpired");
/* 1001 */     this.logger.exiting(CLASS_NAME, "shouldCheckExpired");
/* 1002 */     return Boolean.valueOf(this.configurator.readProperty(CEBASProperty.DELETE_EXPIRED_CERTS.name())).booleanValue();
/*      */   }
/*      */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\importing\CertificatesImporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */