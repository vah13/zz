/*     */ package com.daimler.cebas.certificates.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.CertificateCryptoEngine;
/*     */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqEnhRightsException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqInvalidTypeException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqParentNotExistsException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqParentTypeInvalidException;
/*     */ import com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory;
/*     */ import com.daimler.cebas.certificates.control.factories.AttributeCertificateHolderGenerator;
/*     */ import com.daimler.cebas.certificates.control.repository.CertificateRepository;
/*     */ import com.daimler.cebas.certificates.control.validation.CertificateInStoreValidator;
/*     */ import com.daimler.cebas.certificates.control.validation.CertificateSignRequestValidator;
/*     */ import com.daimler.cebas.certificates.control.validation.CertificatesProcessValidation;
/*     */ import com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput;
/*     */ import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.entity.PKIRole;
/*     */ import com.daimler.cebas.certificates.entity.UserRole;
/*     */ import com.daimler.cebas.common.CryptoTools;
/*     */ import com.daimler.cebas.common.control.HexUtil;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.common.entity.AbstractEntity;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.control.crypto.UserCryptoEngine;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.users.entity.UserKeyPair;
/*     */ import java.security.KeyPair;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.util.Base64;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import org.bouncycastle.cert.X509AttributeCertificateHolder;
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
/*     */ public class CertificateSignRequestEngine
/*     */ {
/*     */   private static final String HEX_SEPARATOR = "-";
/*     */   private static final String EMPTY = "";
/*     */   private Session session;
/*     */   protected SearchEngine searchEngine;
/*     */   protected CertificateRepository repository;
/*     */   protected CertificatesConfiguration certificateProfileConfiguration;
/*     */   private AbstractCertificateFactory factory;
/*     */   private Logger logger;
/*     */   private MetadataManager i18n;
/*     */   
/*     */   @Autowired
/*     */   public CertificateSignRequestEngine(Session session, SearchEngine searchEngine, CertificateRepository repository, CertificatesConfiguration certificateProfileConfiguration, AbstractCertificateFactory factory, Logger logger, MetadataManager i18n) {
/* 126 */     this.session = session;
/* 127 */     this.searchEngine = searchEngine;
/* 128 */     this.repository = repository;
/* 129 */     this.certificateProfileConfiguration = certificateProfileConfiguration;
/* 130 */     this.factory = factory;
/* 131 */     this.logger = logger;
/* 132 */     this.i18n = i18n;
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
/*     */   @Transactional(propagation = Propagation.REQUIRED)
/*     */   public Certificate createCertificateInSignRequestState(CertificateSignRequest certificateSignRequest, boolean checkCurrentUser, ValidationFailureOutput failureOutputStrategy, boolean persistCsr) {
/* 151 */     CertificatesProcessValidation.validateCreateCertificateSignRequest(certificateSignRequest, this.i18n, this.logger);
/* 152 */     this.session.getSystemIntegrityCheckResult().clear();
/* 153 */     Certificate certificate = null;
/* 154 */     User currentUser = this.session.getCurrentUser();
/* 155 */     if (certificateSignRequest.isValid() && isValidCertificateType(
/* 156 */         CertificateType.valueFromString(certificateSignRequest.getCertificateType()))) {
/* 157 */       if (certificateSignRequest.getValidTo() == null) {
/* 158 */         certificateSignRequest.setValidTo(this.certificateProfileConfiguration.getCSRValidTo(this.logger, this.i18n));
/*     */       }
/* 160 */       CertificateSignRequestValidator.validate(certificateSignRequest, this.logger, this.i18n, failureOutputStrategy);
/* 161 */       if (certificateSignRequest.isValid()) {
/* 162 */         certificate = createCertificateInSignRequestState(certificateSignRequest, checkCurrentUser, currentUser, persistCsr);
/*     */       }
/*     */     } else {
/*     */       
/* 166 */       throw getInvalidCertificateTypeException(certificateSignRequest);
/*     */     } 
/* 168 */     return certificate;
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
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public Certificate createCertificateInSignRequestStateNewTransaction(CertificateSignRequest certificateSignRequest, boolean checkCurrentUser, ValidationFailureOutput failureOutputStrategy) {
/* 185 */     return createCertificateInSignRequestState(certificateSignRequest, checkCurrentUser, failureOutputStrategy, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CEBASCertificateException getUnauthorizedUserException() {
/* 196 */     CEBASCertificateException zenzefiCertificateException = new CEBASCertificateException(this.i18n.getMessage("registeredUserNeedsToBeLoggedIn"), "registeredUserNeedsToBeLoggedIn");
/*     */     
/* 198 */     this.logger.logWithTranslation(Level.WARNING, "000010X", zenzefiCertificateException
/* 199 */         .getMessageId(), zenzefiCertificateException.getClass().getSimpleName());
/* 200 */     return zenzefiCertificateException;
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
/*     */   private Certificate createCertificateInSignRequestState(CertificateSignRequest certificateSignRequest, boolean checkCurrentUser, User currentUser, boolean persistCsr) {
/*     */     Certificate certificate;
/* 216 */     certificateSignRequest.setUserId(currentUser.getUserName());
/* 217 */     validateRights(checkCurrentUser);
/* 218 */     UserCryptoEngine cryptoEngine = this.session.getCryptoEngine();
/* 219 */     Certificate parent = getParent(certificateSignRequest, currentUser);
/* 220 */     this.repository.refresh((AbstractEntity)parent);
/* 221 */     CertificateType csrType = CertificateType.valueFromString(certificateSignRequest.getCertificateType());
/* 222 */     validateCSRUnderDiagnostic(parent, csrType);
/* 223 */     String publicKey = "";
/*     */     
/* 225 */     if (csrType == CertificateType.ENHANCED_RIGHTS_CERTIFICATE) {
/* 226 */       if (parent.getType() == CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE) {
/* 227 */         X509AttributeCertificateHolder attributeHolder = AttributeCertificateHolderGenerator.generate(parent, certificateSignRequest, this.logger);
/*     */         
/* 229 */         KeyPair keyPair = getKeyPairForCertificate(parent);
/*     */         try {
/* 231 */           certificateSignRequest.setSignature(createPKCS10SignRequest(certificateSignRequest, keyPair));
/* 232 */           certificate = this.factory.getCertificateInstance("", certificateSignRequest, attributeHolder, currentUser);
/*     */           
/* 234 */           certificate.setParent(parent);
/* 235 */           parent.getChildren().add(certificate);
/* 236 */           this.repository.create((AbstractEntity)certificate);
/*     */         } finally {
/* 238 */           CryptoTools.destroyPrivateKey(keyPair.getPrivate());
/*     */         } 
/*     */       } else {
/*     */         
/* 242 */         CertificateSigningReqEnhRightsException zenzefiCertificateException = new CertificateSigningReqEnhRightsException(this.i18n.getMessage("enhRightsExistsOnlyInContextOfDiagAuth"), "enhRightsExistsOnlyInContextOfDiagAuth");
/*     */         
/* 244 */         this.logger.logWithTranslation(Level.WARNING, "000098X", zenzefiCertificateException
/* 245 */             .getMessageId(), zenzefiCertificateException
/* 246 */             .getClass().getSimpleName());
/* 247 */         throw zenzefiCertificateException;
/*     */       } 
/*     */     } else {
/* 250 */       KeyPair generateKeyPair = cryptoEngine.generateKeyPair();
/*     */       try {
/* 252 */         publicKey = cryptoEngine.getPublicKeyDataHex(generateKeyPair.getPublic());
/* 253 */         if (persistCsr) {
/* 254 */           String encodedPrivateKey = cryptoEngine.encodePrivateKey(this.session.getContainerKey(), generateKeyPair
/* 255 */               .getPrivate().getEncoded());
/* 256 */           certificateSignRequest
/* 257 */             .setSignature(createPKCS10SignRequest(certificateSignRequest, generateKeyPair));
/* 258 */           certificate = this.factory.getCertificateInstance(publicKey, certificateSignRequest, currentUser);
/* 259 */           UserKeyPair userKeyPair = new UserKeyPair(publicKey, encodedPrivateKey, currentUser, certificate);
/* 260 */           currentUser.getKeyPairs().add(userKeyPair);
/* 261 */           certificate.setParent(parent);
/* 262 */           parent.getChildren().add(certificate);
/* 263 */           this.repository.create((AbstractEntity)certificate);
/* 264 */           this.repository.create((AbstractEntity)userKeyPair);
/*     */         } else {
/* 266 */           certificateSignRequest
/* 267 */             .setSignature(createPKCS10SignRequest(certificateSignRequest, generateKeyPair));
/* 268 */           certificate = this.factory.getCertificateInstance(publicKey, certificateSignRequest, currentUser);
/*     */         } 
/*     */       } finally {
/*     */         
/* 272 */         CryptoTools.destroyPrivateKey(generateKeyPair.getPrivate());
/*     */       } 
/*     */     } 
/* 275 */     this.logger.log(Level.INFO, "000049", this.i18n
/* 276 */         .getEnglishMessage("csrCreatedType", new String[] {
/* 277 */             certificateSignRequest.getCertificateType(), parent.getSubjectKeyIdentifier(), publicKey
/* 278 */           }), getClass().getSimpleName());
/* 279 */     return certificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateRights(boolean checkCurrentUser) {
/* 287 */     if (checkCurrentUser && this.session.isDefaultUser()) {
/* 288 */       throw getUnauthorizedUserException();
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
/*     */   private boolean isValidCertificateType(CertificateType type) {
/* 300 */     return this.certificateProfileConfiguration.availableCertificateTypesForCSRCreation().contains(type);
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
/*     */   private Optional<Certificate> getParentCertificateOptional(CertificateSignRequest certificateSignRequest, User currentUser) {
/* 314 */     return (certificateSignRequest.getParentId() != null) ? this.repository
/* 315 */       .findCertificate(certificateSignRequest.getParentId()) : this.searchEngine
/* 316 */       .findCertificate(currentUser, 
/* 317 */         HexUtil.bytesToHex(Base64.getDecoder().decode(certificateSignRequest.getAuthorityKeyIdentifier())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private KeyPair getKeyPairForCertificate(Certificate certificate) {
/* 328 */     UserKeyPair userKeyPair = this.searchEngine.getUserKeyPairForCertificate(this.session.getCurrentUser(), certificate);
/* 329 */     byte[] decodedPrivateKey = this.session.getCryptoEngine().decodePrivateKeyToByteArray(this.session.getContainerKey(), userKeyPair
/* 330 */         .getPrivateKey());
/* 331 */     PublicKey publicKey = certificate.getCertificateData().getCert().getPublicKey();
/* 332 */     PrivateKey privateKey = CertificateCryptoEngine.generatePrivateKey(decodedPrivateKey);
/* 333 */     return new KeyPair(publicKey, privateKey);
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
/*     */   private String createPKCS10SignRequest(CertificateSignRequest certificateSignRequest, KeyPair keyPair) {
/* 346 */     String subject = certificateSignRequest.getSubject();
/* 347 */     CertificateType certificateType = CertificateType.valueFromString(certificateSignRequest.getCertificateType());
/*     */     
/* 349 */     Optional<Byte> pkiRole = Optional.ofNullable(Byte.valueOf(PKIRole.getPKIRoleFromCertificateType(certificateType).byteValue()));
/*     */     
/* 351 */     Optional<Byte> userRole = Optional.ofNullable((certificateType.equals(CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE) || certificateType
/* 352 */         .equals(CertificateType.TIME_CERTIFICATE)) ? 
/* 353 */         Byte.valueOf(UserRole.getByteFromText(certificateSignRequest.getUserRole())) : null);
/*     */     
/* 355 */     Optional<String> vin = Optional.ofNullable(certificateSignRequest.getTargetVIN());
/* 356 */     Optional<String> ecu = Optional.ofNullable(certificateSignRequest.getTargetECU());
/* 357 */     Optional<String> nonce = Optional.ofNullable(!StringUtils.isEmpty(certificateSignRequest.getNonce()) ? 
/* 358 */         HexUtil.bytesToHex(Base64.getDecoder().decode(certificateSignRequest.getNonce()))
/* 359 */         .replace("-", "").toLowerCase() : null);
/*     */     
/* 361 */     Optional<String> uniqueECUId = Optional.ofNullable(certificateSignRequest.getUniqueECUID());
/* 362 */     Optional<String> specialECU = Optional.ofNullable(certificateSignRequest.getSpecialECU());
/* 363 */     Optional<String> services = Optional.ofNullable(certificateSignRequest.getServices());
/*     */     
/* 365 */     Optional<String> targetSubjectKeyIdentifier = Optional.ofNullable(!StringUtils.isEmpty(certificateSignRequest.getTargetSubjectKeyIdentifier()) ? certificateSignRequest
/* 366 */         .getTargetSubjectKeyIdentifier().replace("-", "")
/* 367 */         .toLowerCase() : null);
/*     */ 
/*     */     
/* 370 */     Optional<Byte> prodQualifier = Optional.ofNullable(Byte.valueOf(certificateSignRequest.retrieveProdQualifierForCSR(certificateSignRequest).byteValue()));
/* 371 */     byte[] pkcs10Bytes = CertificateCryptoEngine.createCertificateSignRequestAsPKCS10(subject, keyPair, pkiRole, userRole, vin, ecu, nonce, uniqueECUId, specialECU, services, targetSubjectKeyIdentifier, prodQualifier, certificateSignRequest
/*     */         
/* 373 */         .getCertificateType());
/* 374 */     return CertificateCryptoEngine.convertSignedCSRToBase64EncodedPemFormat(pkcs10Bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateParentType(Certificate parent) {
/* 384 */     if (parent.getType() != CertificateType.BACKEND_CA_CERTIFICATE && parent
/* 385 */       .getType() != CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE) {
/*     */       
/* 387 */       CertificateSigningReqParentTypeInvalidException zenzefiCertificateException = new CertificateSigningReqParentTypeInvalidException(this.i18n.getMessage("parentCertInvalidBackendAndDiagSupportedForEnhRights", new String[] {
/* 388 */               parent.getType().getText()
/*     */             }), "parentCertInvalidBackendAndDiagSupportedForEnhRights");
/* 390 */       this.logger.logWithTranslation(Level.WARNING, "000095X", zenzefiCertificateException
/* 391 */           .getMessageId(), zenzefiCertificateException.getClass().getSimpleName());
/* 392 */       throw zenzefiCertificateException;
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
/*     */   private void validateParentCurrentUserStore(User currentUser, Certificate parent) {
/* 405 */     if (!CertificateInStoreValidator.isInStore(parent, currentUser.getEntityId(), this.logger)) {
/*     */       
/* 407 */       CertificateSigningReqParentNotExistsException zenzefiCertificateException = new CertificateSigningReqParentNotExistsException(this.i18n.getMessage("providedCertParentNotExist"), "providedCertParentNotExist");
/*     */       
/* 409 */       this.logger.logWithTranslation(Level.WARNING, "000096X", zenzefiCertificateException
/* 410 */           .getMessageId(), zenzefiCertificateException.getClass().getSimpleName());
/* 411 */       throw zenzefiCertificateException;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateCSRUnderDiagnostic(Certificate parent, CertificateType csrType) {
/* 421 */     if (parent.getType() == CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE && csrType != CertificateType.ENHANCED_RIGHTS_CERTIFICATE) {
/*     */ 
/*     */       
/* 424 */       CertificateSigningReqEnhRightsException zenzefiCertificateException = new CertificateSigningReqEnhRightsException(this.i18n.getMessage("diagCertOnlyEnhRightsCSRUnderIt"), "diagCertOnlyEnhRightsCSRUnderIt");
/* 425 */       this.logger.logWithTranslation(Level.WARNING, "000097X", zenzefiCertificateException
/* 426 */           .getMessageId(), zenzefiCertificateException.getClass().getSimpleName());
/* 427 */       throw zenzefiCertificateException;
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
/*     */   private CertificateSigningReqInvalidTypeException getInvalidCertificateTypeException(CertificateSignRequest certificateSignRequest) {
/* 442 */     CertificateSigningReqInvalidTypeException zenzefiCertificateException = new CertificateSigningReqInvalidTypeException(this.i18n.getMessage("cannotCreateCSR", new String[] {
/* 443 */             certificateSignRequest.getCertificateType()
/*     */           }), "cannotCreateCSR");
/* 445 */     this.logger.logWithTranslation(Level.WARNING, "000152X", zenzefiCertificateException
/* 446 */         .getMessageId(), new String[] { certificateSignRequest
/* 447 */           .getCertificateType() }, zenzefiCertificateException
/* 448 */         .getClass().getSimpleName());
/* 449 */     return zenzefiCertificateException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CertificateNotFoundException parentDoesNotExistException() {
/* 459 */     CertificateNotFoundException parentDoesNotExist = new CertificateNotFoundException(this.i18n.getMessage("backendNotFound"), "backendNotFound");
/* 460 */     this.logger.logWithTranslation(Level.WARNING, "000094X", parentDoesNotExist.getMessageId(), parentDoesNotExist
/* 461 */         .getClass().getSimpleName());
/* 462 */     return parentDoesNotExist;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Certificate getParent(CertificateSignRequest certificateSignRequest, User currentUser) {
/* 473 */     Optional<Certificate> parentCertificateOptional = getParentCertificateOptional(certificateSignRequest, currentUser);
/*     */     
/* 475 */     Certificate parent = parentCertificateOptional.<Throwable>orElseThrow(this::parentDoesNotExistException);
/* 476 */     if (certificateSignRequest.getParentId() == null || certificateSignRequest.getParentId().isEmpty()) {
/* 477 */       certificateSignRequest.setParentId(parent.getEntityId());
/*     */     }
/* 479 */     validateParentType(parent);
/* 480 */     validateParentCurrentUserStore(currentUser, parent);
/* 481 */     return parent;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\CertificateSignRequestEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */