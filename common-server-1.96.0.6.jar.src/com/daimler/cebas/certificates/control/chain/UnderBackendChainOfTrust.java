/*     */ package com.daimler.cebas.certificates.control.chain;
/*     */ 
/*     */ import com.daimler.cebas.certificates.CertificateCryptoEngine;
/*     */ import com.daimler.cebas.certificates.control.SearchEngine;
/*     */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*     */ import com.daimler.cebas.certificates.control.repository.CertificateRepository;
/*     */ import com.daimler.cebas.certificates.control.validation.CertificatesValidator;
/*     */ import com.daimler.cebas.certificates.control.validation.ValidationError;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.entity.RawData;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.common.entity.AbstractEntity;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.users.entity.UserKeyPair;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.ApplicationEventPublisher;
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
/*     */ public class UnderBackendChainOfTrust
/*     */   extends ChainOfTrust
/*     */ {
/*  44 */   private static final Logger LOG = Logger.getLogger(CertificatesValidator.class.getName());
/*     */ 
/*     */ 
/*     */   
/*  48 */   private static final String CLASS_NAME = UnderBackendChainOfTrust.class.getSimpleName();
/*     */ 
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public UnderBackendChainOfTrust(CertificateRepository repo, Session session, Logger logger, ApplicationEventPublisher publisher, MetadataManager i18n, CertificatesConfiguration profileConfiguration, SearchEngine searchEngine) {
/*  69 */     super(repo, session, logger, publisher, i18n, profileConfiguration);
/*  70 */     this.searchEngine = searchEngine;
/*     */   }
/*     */ 
/*     */   
/*     */   public void check(List<Certificate> userStoreRootCertificate, CertificatePrivateKeyHolder<Certificate> holder, List<ValidationError> errors, boolean onlyFromPKI, boolean add) {
/*  75 */     String METHOD_NAME = "check";
/*  76 */     this.logger.entering(CLASS_NAME, "check");
/*     */     
/*  78 */     boolean foundBackend = false;
/*  79 */     Certificate certificate = holder.getCertificate();
/*  80 */     for (Certificate root : userStoreRootCertificate) {
/*  81 */       Optional<Certificate> backendCertificateValidFromStore = getBackendCertificateValidFromStore(root, certificate, onlyFromPKI);
/*  82 */       if (backendCertificateValidFromStore.isPresent()) {
/*  83 */         foundBackend = true;
/*  84 */         Certificate backend = backendCertificateValidFromStore.get();
/*  85 */         RawData backendCertificateData = backend.getCertificateData();
/*  86 */         RawData certificateData = certificate.getCertificateData();
/*  87 */         if (backendCertificateData.isCertificate()) {
/*  88 */           if (this.profileConfiguration.isImportWithoutSignatureCheck() || verifySignature(backendCertificateData, certificateData)) {
/*  89 */             if (add)
/*  90 */               addUnderBackend(holder, errors, backend); 
/*     */             break;
/*     */           } 
/*  93 */           errorSignatureVerification(certificate, errors);
/*     */           
/*     */           break;
/*     */         } 
/*  97 */         errorBackendNotFoundAsIssuer(certificate, errors);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     if (!foundBackend) {
/* 104 */       errorBackendNotFoundBaseOnAuthorityKey(certificate, errors);
/*     */     }
/* 106 */     this.logger.exiting(CLASS_NAME, "check");
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
/*     */   private void addUnderBackend(CertificatePrivateKeyHolder<Certificate> holder, List<ValidationError> errors, Certificate backend) {
/* 118 */     Certificate certificate = holder.getCertificate();
/* 119 */     if (this.profileConfiguration.isUnique(certificate, this.logger)) {
/* 120 */       this.profileConfiguration.getAddBackendHandler(this, this.searchEngine).addUnderBackend(holder, errors, backend);
/*     */     } else {
/* 122 */       errorCertificateAlreadyExist(certificate, errors);
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
/*     */   protected boolean checkEcuCertAndCsrExists(List<ValidationError> errors, Certificate backend, Certificate certificate) {
/* 137 */     if (certificate.getType() == CertificateType.ECU_CERTIFICATE || !this.profileConfiguration.checkCSRExists()) {
/* 138 */       if (this.profileConfiguration.shouldReplaceECUCertificate() && this.profileConfiguration
/* 139 */         .shouldReplaceDuringImport(certificate.getType())) {
/* 140 */         replaceEcuCertificate(certificate);
/*     */       }
/* 142 */       add(backend, certificate);
/* 143 */       return true;
/*     */     } 
/* 145 */     errorDidNotFindCSR(certificate, errors);
/* 146 */     return false;
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
/*     */   protected boolean addCertificateWithCSR(CertificatePrivateKeyHolder holder, List<ValidationError> errors, Certificate certificate, Certificate csrCertificate, boolean extendedValidation) {
/* 161 */     if (csrCertificate.getType() != certificate.getType()) {
/* 162 */       logCsrTypeDoesNotMatchCertificateType(certificate, csrCertificate);
/*     */     }
/* 164 */     checkIdenticalWithDifferences(holder);
/* 165 */     PrivateKey privateKey = getPrivateKeyFromKeyPair(certificate.getUser(), csrCertificate);
/* 166 */     if (!extendedValidation || matchPublicKeys(certificate, privateKey, errors)) {
/* 167 */       RawData certificateData = certificate.getCertificateData();
/* 168 */       csrCertificate.updateCSR(certificateData.getCert(), certificate.getCertificateData().getOriginalBytes());
/* 169 */       holder.setInternalId(csrCertificate.getEntityId());
/* 170 */       return true;
/*     */     } 
/* 172 */     return false;
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
/*     */   protected boolean addCertificateWithPrivateKey(CertificatePrivateKeyHolder holder, List<ValidationError> errors, Certificate backend, Certificate certificate, boolean extendedValidation) {
/* 187 */     checkIdenticalWithDifferences(holder);
/* 188 */     User user = certificate.getUser();
/* 189 */     if (!extendedValidation || matchPublicKeys(certificate, holder.getPrivateKey(), errors)) {
/* 190 */       UserKeyPair userKeyPair = createUserKeyPair(certificate, holder.getPrivateKey(), user);
/* 191 */       user.getKeyPairs().add(userKeyPair);
/* 192 */       add(backend, certificate);
/* 193 */       this.repo.create((AbstractEntity)userKeyPair);
/* 194 */       return true;
/*     */     } 
/* 196 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void add(Certificate backend, Certificate certificate) {
/* 206 */     certificate.setParent(backend);
/* 207 */     this.repo.create((AbstractEntity)certificate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkIdenticalWithDifferences(CertificatePrivateKeyHolder<Certificate> holder) {
/* 217 */     Certificate certificate = holder.getCertificate();
/* 218 */     if ((certificate.getType() != CertificateType.ECU_CERTIFICATE || this.profileConfiguration
/* 219 */       .shouldReplaceECUCertificate()) && this.profileConfiguration
/* 220 */       .shouldReplaceDuringImport(certificate.getType())) {
/* 221 */       List<Certificate> identical = this.searchEngine.findIdentical(certificate);
/* 222 */       List<Certificate> identicalWithDifferences = Uniqueness.findIdenticalWithDifferences(identical, certificate, this.logger);
/* 223 */       holder.addPossibleReplaceableCertificate(identicalWithDifferences);
/* 224 */       deleteCertificates(identicalWithDifferences);
/* 225 */       logReplacedCertificate(identicalWithDifferences, certificate);
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
/*     */   private UserKeyPair createUserKeyPair(Certificate cert, PrivateKey privateKey, User user) {
/* 238 */     String publicKey = cert.getSubjectPublicKey();
/* 239 */     String encodedPrivateKey = this.session.getCryptoEngine().encodePrivateKey(this.session.getContainerKey(), privateKey.getEncoded());
/* 240 */     return new UserKeyPair(publicKey, encodedPrivateKey, user, cert);
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
/*     */   private boolean matchPublicKeys(Certificate certificate, PrivateKey privateKey, List<ValidationError> errors) {
/*     */     try {
/* 254 */       PublicKey calculatedPublicKey = CertificateCryptoEngine.calculateEDDSAFromEdDSAPrivate(privateKey);
/* 255 */       String publicKeyFromPrivate = this.session.getCryptoEngine().getPublicKeyDataHex(calculatedPublicKey);
/* 256 */       boolean pkMatch = certificate.getSubjectPublicKey().equals(publicKeyFromPrivate);
/* 257 */       if (!pkMatch) {
/* 258 */         errorPublicKeyDoesNotMatchPrivateKey(certificate, publicKeyFromPrivate, errors);
/*     */       }
/* 260 */       return pkMatch;
/* 261 */     } catch (GeneralSecurityException e) {
/* 262 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 263 */       errorPublicKeyDoesNotMatchPrivateKey(certificate, "", errors);
/* 264 */       return false;
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
/*     */   private PrivateKey getPrivateKeyFromKeyPair(User user, Certificate csrCertificate) {
/* 276 */     UserKeyPair userKeyPair = this.searchEngine.getUserKeyPairForCertificate(user, csrCertificate);
/* 277 */     byte[] decodedPrivateKey = this.session.getCryptoEngine().decodePrivateKeyToByteArray(this.session.getContainerKey(), userKeyPair
/* 278 */         .getPrivateKey());
/* 279 */     return CertificateCryptoEngine.generatePrivateKey(decodedPrivateKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void errorDidNotFindCSR(Certificate certificate, List<ValidationError> errors) {
/* 290 */     errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("csrAndPrivateKeyNotFoundForSubjKey", new String[] { certificate
/*     */ 
/*     */               
/* 293 */               .getSubjectKeyIdentifier()
/*     */             }), "csrAndPrivateKeyNotFoundForSubjKey", new String[] {
/* 295 */             certificate.getSubjectKeyIdentifier() }));
/* 296 */     this.logger.log(Level.WARNING, "000026", this.i18n
/* 297 */         .getEnglishMessage("csrAndPrivateKeyNotFoundForSubjKey", new String[] {
/* 298 */             certificate.getSubjectKeyIdentifier()
/* 299 */           }), getClass().getSimpleName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void errorHasPrivateKeyAndFoundCSR(Certificate certificate, List<ValidationError> errors) {
/* 310 */     errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("csrAndPrivateKeyFoundForSubjKey", new String[] { certificate
/*     */ 
/*     */               
/* 313 */               .getSubjectKeyIdentifier()
/*     */             }), "csrAndPrivateKeyFoundForSubjKey", new String[] {
/* 315 */             certificate.getSubjectKeyIdentifier() }));
/* 316 */     this.logger.log(Level.WARNING, "000026", this.i18n
/* 317 */         .getEnglishMessage("csrAndPrivateKeyFoundForSubjKey", new String[] {
/* 318 */             certificate.getSubjectKeyIdentifier()
/* 319 */           }), getClass().getSimpleName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void errorCertificateAlreadyExist(Certificate certificate, List<ValidationError> errors) {
/* 329 */     logCertificateAlreadyExist(certificate);
/*     */     
/* 331 */     errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("certAlreadyExistsWithSubjectKey", new String[] { certificate
/*     */ 
/*     */               
/* 334 */               .getSubjectKeyIdentifier()
/*     */             }), "certAlreadyExistsWithSubjectKey", new String[] {
/* 336 */             certificate.getSubjectKeyIdentifier()
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void errorPublicKeyDoesNotMatchPrivateKey(Certificate certificate, String publicKeyFromPrivate, List<ValidationError> errors) {
/* 348 */     this.logger.log(Level.WARNING, "000029X", this.i18n
/* 349 */         .getEnglishMessage("certPublicKeyDoesntMatchPublicKeyFromPrivateKey", new String[] {
/* 350 */             certificate.getSubjectPublicKey(), publicKeyFromPrivate, certificate
/* 351 */             .getSubjectKeyIdentifier()
/*     */           }), CLASS_NAME);
/* 353 */     errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n
/* 354 */           .getMessage("certPublicKeyDoesntMatchPublicKeyFromPrivateKey", new String[] {
/* 355 */               certificate.getSubjectPublicKey(), publicKeyFromPrivate, certificate
/* 356 */               .getSubjectKeyIdentifier()
/*     */             
/* 358 */             }), "certPublicKeyDoesntMatchPublicKeyFromPrivateKey", new String[] { certificate.getSubjectPublicKey(), publicKeyFromPrivate, certificate
/* 359 */             .getSubjectKeyIdentifier() }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void errorBackendNotFoundAsIssuer(Certificate certificate, List<ValidationError> errors) {
/* 369 */     logDidNotFindIssuer("000111X", certificate, CertificateType.BACKEND_CA_CERTIFICATE);
/*     */     
/* 371 */     errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("backendNotFoundSubjKey", new String[] { certificate
/*     */               
/* 373 */               .getSubjectKeyIdentifier()
/* 374 */             }), "backendNotFoundSubjKey", new String[] { certificate.getSubjectKeyIdentifier() }));
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
/*     */   private void errorBackendNotFoundBaseOnAuthorityKey(Certificate certificate, List<ValidationError> errors) {
/* 388 */     ValidationError error = new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("backendNotFoundIssuedSubjKey", new String[] { certificate.getSubjectKeyIdentifier() }), "backendNotFoundIssuedSubjKey", new String[] { certificate.getSubjectKeyIdentifier() });
/* 389 */     errors.add(error);
/* 390 */     logDidNotFindIssuer("000112X", certificate, CertificateType.BACKEND_CA_CERTIFICATE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void errorSignatureVerification(Certificate certificate, List<ValidationError> errors) {
/* 400 */     this.logger.logWithTranslation(Level.WARNING, "000030", "sigVerificationFailedSubjKey", new String[] { certificate
/* 401 */           .getSubjectKeyIdentifier() }, CLASS_NAME);
/*     */ 
/*     */     
/* 404 */     errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("sigVerificationFailedSubjKey", new String[] { certificate
/*     */ 
/*     */               
/* 407 */               .getSubjectKeyIdentifier()
/*     */             }), "sigVerificationFailedSubjKey", new String[] {
/* 409 */             certificate.getSubjectKeyIdentifier()
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logCsrTypeDoesNotMatchCertificateType(Certificate certificate, Certificate csrCertficate) {
/* 419 */     this.logger.log(Level.WARNING, "000053", this.i18n
/* 420 */         .getEnglishMessage("csrTypeNotMatchCertType", new String[] {
/* 421 */             csrCertficate.getType().getText(), certificate.getType().getText(), certificate
/* 422 */             .getSubjectKeyIdentifier()
/* 423 */           }), getClass().getSimpleName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void errorCertificateWithSameAuthKeyAndSerialNumber(Certificate certificate, List<ValidationError> errors) {
/* 434 */     this.logger.log(Level.WARNING, "000022X", this.i18n
/* 435 */         .getEnglishMessage("certAlreadyExistsWithAuthKeyAndSN", new String[] {
/* 436 */             certificate.getAuthorityKeyIdentifier(), certificate.getSerialNo()
/*     */           }), CLASS_NAME);
/* 438 */     errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n
/* 439 */           .getMessage("certAlreadyExistsWithAuthKeyAndSN", new String[] {
/* 440 */               certificate.getAuthorityKeyIdentifier(), certificate.getSerialNo()
/*     */             
/* 442 */             }), "certAlreadyExistsWithAuthKeyAndSN", new String[] { certificate.getAuthorityKeyIdentifier(), certificate.getSerialNo() }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void replaceEcuCertificate(Certificate certificateToBeImported) {
/* 451 */     List<Certificate> identical = this.searchEngine.findIdentical(certificateToBeImported);
/* 452 */     List<Certificate> identicalWithDifferences = Uniqueness.findIdenticalWithDifferences(identical, certificateToBeImported, this.logger);
/* 453 */     deleteCertificates(identicalWithDifferences);
/* 454 */     logReplacedCertificate(identicalWithDifferences, certificateToBeImported);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\chain\UnderBackendChainOfTrust.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */