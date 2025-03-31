/*     */ package com.daimler.cebas.certificates.control.validation;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.DurationParser;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.entity.RawData;
/*     */ import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
/*     */ import com.daimler.cebas.common.control.CEBASProperty;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.SecurityProviders;
/*     */ import com.daimler.cebas.common.control.annotations.FacadePattern;
/*     */ import com.daimler.cebas.configuration.entity.Configuration;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.users.entity.UserKeyPair;
/*     */ import java.io.IOException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.Signature;
/*     */ import java.security.SignatureException;
/*     */ import java.security.cert.CertificateExpiredException;
/*     */ import java.security.cert.CertificateNotYetValidException;
/*     */ import java.sql.Timestamp;
/*     */ import java.time.LocalDateTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bouncycastle.cert.X509AttributeCertificateHolder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @FacadePattern
/*     */ public class CertificatesValidator
/*     */ {
/*  46 */   private static final Logger LOG = Logger.getLogger(CertificatesValidator.class.getName());
/*     */ 
/*     */   
/*     */   private static final String TYPE = " type: ";
/*     */ 
/*     */   
/*     */   private static final String SERIAL_NO = " Serial No: ";
/*     */ 
/*     */   
/*     */   private static final String SKI = " SKI: ";
/*     */ 
/*     */   
/*  58 */   private static final String CLASS_NAME = CertificatesValidator.class.getSimpleName();
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
/*     */   public static List<Optional<ValidationError>> basicValidation(Certificate certificate, boolean checkExpired, Logger logger, MetadataManager i18n) {
/*  76 */     String METHOD_NAME = "basicValidation";
/*  77 */     logger.entering(CLASS_NAME, "basicValidation");
/*  78 */     List<Optional<ValidationError>> validationErrosOptionals = new ArrayList<>();
/*  79 */     validationErrosOptionals.add(ValidationRuleChecker.check(cert -> (cert.getType() != CertificateType.NO_TYPE), certificate, i18n
/*  80 */           .getMessage("invalidCertificateType"), "invalidCertificateType"));
/*  81 */     validationErrosOptionals.addAll(checkCertificateDBFieldsLength(certificate, i18n));
/*  82 */     if (checkExpired) {
/*  83 */       validationErrosOptionals
/*  84 */         .add(ValidationRuleChecker.check(ValidationRules.isNotExpired(), certificate, i18n
/*  85 */             .getMessage("certificateExpiredInvalidDates", new String[] {
/*  86 */                 certificate.getSubjectKeyIdentifier()
/*     */               }), "certificateExpiredInvalidDates"));
/*     */     }
/*  89 */     logger.exiting(CLASS_NAME, "basicValidation");
/*  90 */     return validationErrosOptionals;
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
/*     */   public static List<Optional<ValidationError>> extendedValidation(Certificate certificate, Logger logger, MetadataManager i18n) {
/* 105 */     String METHOD_NAME = "extendedValidation";
/* 106 */     logger.entering(CLASS_NAME, "extendedValidation");
/*     */     
/* 108 */     List<Optional<ValidationError>> validationErrorsOptionals = new ArrayList<>();
/* 109 */     Optional<ValidationError> daimlerTypeValidationErrorOptional = ValidationRuleChecker.check(cert -> (cert.getType() != CertificateType.NO_TYPE), certificate, i18n
/*     */         
/* 111 */         .getMessage("invalidCertificateType"), "invalidCertificateType");
/* 112 */     validationErrorsOptionals.add(daimlerTypeValidationErrorOptional);
/*     */     
/* 114 */     if (!daimlerTypeValidationErrorOptional.isPresent()) {
/* 115 */       validationErrorsOptionals.add(ValidationRuleChecker.check(ValidationRules.validPublicKey(), certificate, i18n
/* 116 */             .getMessage("invalidPublicKey"), "invalidPublicKey"));
/* 117 */       validationErrorsOptionals.add(ValidationRuleChecker.check(ValidationRules.validAuthorityKeyIdentifier(), certificate, i18n
/* 118 */             .getMessage("invalidAuthKeyIdentifier"), "invalidAuthKeyIdentifier"));
/*     */       
/* 120 */       validationErrorsOptionals.add(ValidationRuleChecker.check(ValidationRules.validSubjectKeyIdentifier(), certificate, i18n
/* 121 */             .getMessage("invalidSubjKeyIdentifier"), "invalidSubjKeyIdentifier"));
/*     */       
/* 123 */       validationErrorsOptionals.add(ValidationRuleChecker.check(ValidationRules.validAlgorithmIdentifier(), certificate, i18n
/* 124 */             .getMessage("invalidAlgIdentifier"), "invalidAlgIdentifier"));
/* 125 */       validationErrorsOptionals.add(ValidationRuleChecker.check(ValidationRules.validSignature(), certificate, i18n
/* 126 */             .getMessage("invalidSignature"), "invalidSignature"));
/* 127 */       validationErrorsOptionals.add(ValidationRuleChecker.check(ValidationRules.validProdQualifier(), certificate, i18n
/* 128 */             .getMessage("invalidProdQualifier"), "invalidProdQualifier"));
/* 129 */       validationErrorsOptionals.add(ValidationRuleChecker.check(ValidationRules.validPKIRole(), certificate, i18n
/* 130 */             .getMessage("invalidPKIRole"), "invalidPKIRole"));
/* 131 */       validationErrorsOptionals.add(ValidationRuleChecker.check(ValidationRules.validIssuer(), certificate, i18n
/* 132 */             .getMessage("invalidIssuer"), "invalidIssuer"));
/* 133 */       validationErrorsOptionals.add(ValidationRuleChecker.check(ValidationRules.validVersion(), certificate, i18n
/* 134 */             .getMessage("invalidVersion"), "invalidVersion"));
/* 135 */       validationErrorsOptionals.add(ValidationRuleChecker.check(ValidationRules.validSerialNumber(), certificate, i18n
/* 136 */             .getMessage("invalidSerialNumber"), "invalidSerialNumber"));
/* 137 */       validationErrorsOptionals
/* 138 */         .add(ValidationRuleChecker.check(ValidationRules.isNotExpired(), certificate, i18n
/* 139 */             .getMessage("certificateExpiredInvalidDates", new String[] {
/* 140 */                 certificate.getSubjectKeyIdentifier()
/*     */               }), "certificateExpiredInvalidDates"));
/* 142 */       validationErrorsOptionals.addAll(getSpecificValidation(certificate, i18n));
/*     */     } 
/*     */     
/* 145 */     logger.exiting(CLASS_NAME, "extendedValidation");
/* 146 */     return validationErrorsOptionals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValidInChain(Certificate certificate, MetadataManager i18n, Logger logger) {
/* 156 */     if (certificateStructureValid(certificate)) {
/* 157 */       List<Optional<ValidationError>> specificValidation = getSpecificValidation(certificate, i18n);
/*     */       
/* 159 */       Optional<Optional<ValidationError>> errorFound = specificValidation.stream().filter(Optional::isPresent).findFirst();
/* 160 */       if (errorFound.isPresent()) {
/* 161 */         return false;
/*     */       }
/* 163 */       switch (certificate.getType()) {
/*     */         case ROOT_CA_CERTIFICATE:
/* 165 */           return true;
/*     */         
/*     */         case ENHANCED_RIGHTS_CERTIFICATE:
/* 168 */           return enhancedRightsSigVerification(certificate, logger);
/*     */       } 
/*     */       
/* 171 */       return regularCertificate(certificate, logger);
/*     */     } 
/*     */     
/* 174 */     return false;
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
/*     */   public static boolean isValidLinkCertInChain(Certificate issuer, Certificate linkCertificate, MetadataManager i18n, Logger logger) {
/* 186 */     if (certificateStructureValid(linkCertificate)) {
/* 187 */       List<Optional<ValidationError>> specificValidation = getSpecificValidation(linkCertificate, i18n);
/*     */       
/* 189 */       Optional<Optional<ValidationError>> errorFound = specificValidation.stream().filter(Optional::isPresent).findFirst();
/* 190 */       if (errorFound.isPresent()) {
/* 191 */         return false;
/*     */       }
/* 193 */       return verifySignature(issuer.getCertificateData(), linkCertificate.getCertificateData(), logger);
/*     */     } 
/*     */     
/* 196 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Optional<Configuration> getValidCertsConfiguration(User user) {
/* 207 */     return user.getConfigurations().stream()
/* 208 */       .filter(config -> config.getConfigKey().equals(CEBASProperty.VALIDATE_CERTS.name())).findFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isBasicValidation(User user) {
/* 218 */     Optional<Configuration> validateCerts = getValidCertsConfiguration(user);
/* 219 */     return (validateCerts.isPresent() && !Boolean.valueOf(((Configuration)validateCerts.get()).getConfigValue()).booleanValue());
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
/*     */   public static boolean verifySignature(RawData parentCertificateData, RawData certificateData, Logger logger) {
/* 234 */     boolean verify = true;
/*     */     try {
/* 236 */       if (certificateData.isCertificate()) {
/* 237 */         certificateData.getCert().verify(parentCertificateData.getCert().getPublicKey(), SecurityProviders.SNED25519
/* 238 */             .name());
/*     */       } else {
/* 240 */         PublicKey publicKey = parentCertificateData.getCert().getPublicKey();
/*     */         
/* 242 */         X509AttributeCertificateHolder attributesCertificateHolder = certificateData.getAttributesCertificateHolder();
/* 243 */         verify = verifySignatureAttributeHolder(publicKey, attributesCertificateHolder);
/*     */       } 
/* 245 */     } catch (NoSuchAlgorithmException|InvalidKeyException|NoSuchProviderException|SignatureException|java.security.cert.CertificateException|IOException e) {
/*     */       String authorityKeyIdentifier, userRole, serialNo;
/* 247 */       LOG.log(Level.FINEST, e.getMessage(), e);
/*     */ 
/*     */ 
/*     */       
/* 251 */       if (certificateData.isCertificate()) {
/* 252 */         authorityKeyIdentifier = CertificateParser.getAuthorityKeyIdentifier(certificateData.getCert());
/* 253 */         userRole = CertificateParser.getPKIRole(certificateData.getCert()).getText();
/* 254 */         serialNo = CertificateParser.getSerialNumber(certificateData.getCert());
/*     */       } else {
/*     */         
/* 257 */         authorityKeyIdentifier = CertificateParser.getAuthorityKeyIdentifier(certificateData.getAttributesCertificateHolder());
/* 258 */         userRole = CertificateParser.getPKIRole(certificateData.getCert()).getText();
/* 259 */         serialNo = CertificateParser.getSerialNumber(certificateData.getAttributesCertificateHolder());
/*     */       } 
/* 261 */       logger.logWithTranslation(Level.WARNING, "000015", "signatureVerificationFailed", new String[] { authorityKeyIdentifier, serialNo, userRole }, CLASS_NAME);
/*     */ 
/*     */       
/* 264 */       verify = false;
/*     */     } 
/* 266 */     return verify;
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
/*     */   public static boolean checkPublicKeyMatchesTheOneGeneratedFromPrivateKey(Session session, Certificate certificate, Logger logger) {
/* 284 */     Optional<UserKeyPair> userKeyPairOptional = session.getCorrelatedKeyPair(certificate);
/* 285 */     if (CertificateType.ECU_CERTIFICATE == certificate.getType() && !userKeyPairOptional.isPresent()) {
/* 286 */       return true;
/*     */     }
/* 288 */     if (userKeyPairOptional.isPresent()) {
/* 289 */       boolean matchPublicKeys = session.getCryptoEngine().matchPublicKeys(session.getContainerKey(), certificate, userKeyPairOptional
/* 290 */           .get());
/* 291 */       if (!matchPublicKeys) {
/* 292 */         logger.log(Level.WARNING, "000381", "Public key generated from private key does not match the one in certificate with AKI:" + certificate
/*     */             
/* 294 */             .getAuthorityKeyIdentifier() + " SKI: " + certificate.getSubjectKeyIdentifier() + " Serial No: " + certificate
/* 295 */             .getSerialNo() + " type: " + certificate.getType(), CertificatesValidator.class
/* 296 */             .getSimpleName());
/*     */       }
/* 298 */       return matchPublicKeys;
/*     */     } 
/* 300 */     logger.log(Level.WARNING, "000382", "Private key was not found for certificate with AKI: " + certificate
/* 301 */         .getAuthorityKeyIdentifier() + " SKI: " + certificate
/* 302 */         .getSubjectKeyIdentifier() + " Serial No: " + certificate.getSerialNo() + " type: " + certificate
/* 303 */         .getType(), CertificatesValidator.class
/* 304 */         .getSimpleName());
/* 305 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isExpired(Certificate certificate, Logger logger, MetadataManager i18n) {
/* 315 */     String METHOD_NAME = "isExpired";
/* 316 */     logger.entering(CLASS_NAME, "isExpired");
/* 317 */     boolean expired = false;
/*     */     try {
/* 319 */       certificate.checkValidity();
/* 320 */     } catch (CertificateExpiredException e) {
/* 321 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 322 */       expired = true;
/* 323 */     } catch (CertificateNotYetValidException e) {
/* 324 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 325 */       logger.log(Level.WARNING, "000154X", i18n
/* 326 */           .getMessage("validationFailed", new String[] {
/* 327 */               "Validity " + e.getMessage() + " for certificate with serial number: " + certificate
/* 328 */               .getSerialNo() + " and subject key identifier: " + certificate
/* 329 */               .getSubjectKeyIdentifier()
/*     */             }), CLASS_NAME);
/*     */     } 
/* 332 */     logger.exiting(CLASS_NAME, "isExpired");
/* 333 */     return expired;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isUnknownType(Certificate certificate) {
/* 343 */     return (certificate.getType() == CertificateType.NO_TYPE);
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
/*     */   private static List<Optional<ValidationError>> checkCertificateDBFieldsLength(Certificate certificate, MetadataManager i18n) {
/* 357 */     List<Optional<ValidationError>> validationErrors = new ArrayList<>();
/* 358 */     validationErrors.add(ValidationRuleChecker.check(cert -> (StringUtils.defaultString(cert.getAlgorithmIdentifier()).length() <= 255), certificate, i18n
/*     */ 
/*     */           
/* 361 */           .getMessage("invalidAlgIdentifier"), "invalidAlgIdentifier"));
/* 362 */     validationErrors.add(ValidationRuleChecker.check(cert -> (StringUtils.defaultString(cert.getAuthorityKeyIdentifier()).length() <= 120), certificate, i18n
/*     */ 
/*     */           
/* 365 */           .getMessage("invalidAuthKeyIdentifier"), "invalidAuthKeyIdentifier"));
/*     */     
/* 367 */     validationErrors.add(ValidationRuleChecker.check(cert -> (StringUtils.defaultString(cert.getBaseCertificateID()).length() <= 255), certificate, i18n
/*     */ 
/*     */           
/* 370 */           .getMessage("invalidBaseCertificateId"), "invalidBaseCertificateId"));
/*     */     
/* 372 */     validationErrors.add(ValidationRuleChecker.check(cert -> (StringUtils.defaultString(cert.getBasicConstraints()).length() <= 255), certificate, i18n
/*     */ 
/*     */           
/* 375 */           .getMessage("invalidBasicConstraints"), "invalidBasicConstraints"));
/*     */     
/* 377 */     validationErrors.add(ValidationRuleChecker.check(cert -> (StringUtils.defaultString(cert.getIssuer()).length() <= 50), certificate, i18n
/*     */ 
/*     */           
/* 380 */           .getMessage("invalidIssuer"), "invalidIssuer"));
/* 381 */     validationErrors.add(ValidationRuleChecker.check(cert -> (StringUtils.defaultString(cert.getIssuerSerialNumber()).length() <= 100), certificate, i18n
/*     */ 
/*     */           
/* 384 */           .getMessage("invalidIssuerSerialNumber"), "invalidIssuerSerialNumber"));
/*     */     
/* 386 */     validationErrors.add(ValidationRuleChecker.check(cert -> (StringUtils.defaultString(cert.getNonce()).length() <= 150), certificate, i18n
/*     */ 
/*     */           
/* 389 */           .getMessage("invalidNonce"), "invalidNonce"));
/* 390 */     validationErrors.add(ValidationRuleChecker.check(cert -> (StringUtils.defaultString(cert.getProdQualifier()).length() <= 50), certificate, i18n
/*     */ 
/*     */           
/* 393 */           .getMessage("invalidProdQualifier"), "invalidProdQualifier"));
/* 394 */     validationErrors.add(ValidationRuleChecker.check(cert -> (StringUtils.defaultString(cert.getSerialNo()).length() <= 100), certificate, i18n
/*     */ 
/*     */           
/* 397 */           .getMessage("invalidSerialNumber"), "invalidSerialNumber"));
/* 398 */     validationErrors.add(ValidationRuleChecker.check(cert -> (StringUtils.defaultString(cert.getSpecialECU()).length() <= 30), certificate, i18n
/*     */ 
/*     */           
/* 401 */           .getMessage("invalidSpecialEcu"), "invalidSpecialEcu"));
/* 402 */     validationErrors.add(ValidationRuleChecker.check(cert -> (StringUtils.defaultString(cert.getSubject()).length() <= 50), certificate, i18n
/*     */ 
/*     */           
/* 405 */           .getMessage("invalidSubject"), "invalidSubject"));
/* 406 */     validationErrors.add(ValidationRuleChecker.check(cert -> (StringUtils.defaultString(cert.getSubjectKeyIdentifier()).length() <= 120), certificate, i18n
/*     */ 
/*     */           
/* 409 */           .getMessage("invalidSubjKeyIdentifier"), "invalidSubjKeyIdentifier"));
/*     */     
/* 411 */     validationErrors.add(ValidationRuleChecker.check(cert -> (StringUtils.defaultString(cert.getSubjectPublicKey()).length() <= 150), certificate, i18n
/*     */ 
/*     */           
/* 414 */           .getMessage("invalidPublicKey"), "invalidPublicKey"));
/* 415 */     validationErrors.add(ValidationRuleChecker.check(cert -> (StringUtils.defaultString(cert.getTargetSubjectKeyIdentifier()).length() <= 120), certificate, i18n
/*     */ 
/*     */           
/* 418 */           .getMessage("invalidTargetSubjectKeyIdentifier"), "invalidTargetSubjectKeyIdentifier"));
/*     */     
/* 420 */     validationErrors.add(ValidationRuleChecker.check(cert -> (StringUtils.defaultString(cert.getUserRole()).length() <= 255), certificate, i18n
/*     */ 
/*     */           
/* 423 */           .getMessage("invalidUserRole"), "invalidUserRole"));
/* 424 */     validationErrors.add(ValidationRuleChecker.check(cert -> (StringUtils.defaultString(cert.getVersion()).length() <= 20), certificate, i18n
/*     */ 
/*     */           
/* 427 */           .getMessage("invalidVersion"), "invalidVersion"));
/* 428 */     return validationErrors;
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean verifySignatureAttributeHolder(PublicKey publicKey, X509AttributeCertificateHolder attributesCertificateHolder) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, IOException {
/* 454 */     Signature signature = Signature.getInstance("SHA512withEdDSA", SecurityProviders.SNED25519.name());
/* 455 */     signature.initVerify(publicKey);
/* 456 */     signature.update(attributesCertificateHolder.toASN1Structure().getAcinfo().getEncoded());
/* 457 */     boolean verify = signature.verify(attributesCertificateHolder.getSignature());
/* 458 */     return verify;
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
/*     */   private static List<Optional<ValidationError>> validEnhRightsCertificateStructure(Certificate certificate, MetadataManager i18n) {
/* 471 */     List<Optional<ValidationError>> enhRightsValidationErrorsOptional = new ArrayList<>();
/* 472 */     enhRightsValidationErrorsOptional.add(ValidationRuleChecker.check(ValidationRules.validTargetECU(), certificate, i18n
/* 473 */           .getMessage("invalidTargetEcu"), "invalidTargetEcu"));
/* 474 */     enhRightsValidationErrorsOptional.add(ValidationRuleChecker.check(ValidationRules.validTargetVIN(), certificate, i18n
/* 475 */           .getMessage("invalidTargetVin"), "invalidTargetVin"));
/* 476 */     enhRightsValidationErrorsOptional.add(ValidationRuleChecker.check(ValidationRules.validServices(), certificate, i18n
/* 477 */           .getMessage("invalidServices"), "invalidServices"));
/* 478 */     enhRightsValidationErrorsOptional
/* 479 */       .add(ValidationRuleChecker.check(ValidationRules.validBaseCertificateID(), certificate, i18n
/* 480 */           .getMessage("invalidBaseCertificateId"), "invalidBaseCertificateId"));
/*     */     
/* 482 */     if (certificate.isSecOCISCert()) {
/* 483 */       enhRightsValidationErrorsOptional
/* 484 */         .add(ValidationRuleChecker.check(ValidationRules.validTargetSubjectKeyIdentifier(), certificate, i18n
/* 485 */             .getMessage("invalidTargetSubjectKeyIdentifier"), "invalidTargetSubjectKeyIdentifier"));
/*     */     }
/*     */ 
/*     */     
/* 489 */     return enhRightsValidationErrorsOptional;
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
/*     */   private static List<Optional<ValidationError>> validTimeCertificateStructure(Certificate certificate, MetadataManager i18n) {
/* 502 */     List<Optional<ValidationError>> timeValidationErrorsOptional = validDiagCertificateStructure(certificate, i18n);
/* 503 */     timeValidationErrorsOptional.add(ValidationRuleChecker.check(ValidationRules.validNonce(), certificate, i18n
/* 504 */           .getMessage("invalidNonce"), "invalidNonce"));
/*     */     
/* 506 */     return timeValidationErrorsOptional;
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
/*     */   private static List<Optional<ValidationError>> validDiagCertificateStructure(Certificate certificate, MetadataManager i18n) {
/* 519 */     List<Optional<ValidationError>> diagValidationErrorsOptional = new ArrayList<>();
/* 520 */     diagValidationErrorsOptional.add(ValidationRuleChecker.check(ValidationRules.validUserRole(), certificate, i18n
/* 521 */           .getMessage("invalidUserRole"), "invalidUserRole"));
/* 522 */     diagValidationErrorsOptional.add(ValidationRuleChecker.check(ValidationRules.validTargetECU(), certificate, i18n
/* 523 */           .getMessage("invalidTargetEcu"), "invalidTargetEcu"));
/* 524 */     diagValidationErrorsOptional.add(ValidationRuleChecker.check(ValidationRules.validTargetVIN(), certificate, i18n
/* 525 */           .getMessage("invalidTargetVin"), "invalidTargetVin"));
/*     */     
/* 527 */     return diagValidationErrorsOptional;
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
/*     */   private static List<Optional<ValidationError>> validVariantCodingUserCertificateStructure(Certificate certificate, MetadataManager i18n) {
/* 540 */     List<Optional<ValidationError>> varCodingUsrValidationErrorsOptional = new ArrayList<>();
/* 541 */     varCodingUsrValidationErrorsOptional.add(ValidationRuleChecker.check(ValidationRules.validTargetECU(), certificate, i18n
/* 542 */           .getMessage("invalidTargetEcu"), "invalidTargetEcu"));
/* 543 */     varCodingUsrValidationErrorsOptional.add(ValidationRuleChecker.check(ValidationRules.validTargetVIN(), certificate, i18n
/* 544 */           .getMessage("invalidTargetVin"), "invalidTargetVin"));
/*     */     
/* 546 */     return varCodingUsrValidationErrorsOptional;
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
/*     */   private static List<Optional<ValidationError>> validEcuCertificateStructure(Certificate certificate, MetadataManager i18n) {
/* 559 */     List<Optional<ValidationError>> ecuValidationErrorsOptional = new ArrayList<>();
/* 560 */     ecuValidationErrorsOptional.add(ValidationRuleChecker.check(ValidationRules.validUniqueEcuId(), certificate, i18n
/* 561 */           .getMessage("invalidUniqueEcuId"), "invalidUniqueEcuId"));
/* 562 */     ecuValidationErrorsOptional.add(ValidationRuleChecker.check(ValidationRules.validSpecialECU(), certificate, i18n
/* 563 */           .getMessage("invalidSpecialEcu"), "invalidSpecialEcu"));
/*     */     
/* 565 */     return ecuValidationErrorsOptional;
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
/*     */   private static List<Optional<ValidationError>> validStandardStructure(Certificate certificate, MetadataManager i18n) {
/* 578 */     List<Optional<ValidationError>> validationErrorsOptional = new ArrayList<>();
/* 579 */     validationErrorsOptional.add(ValidationRuleChecker.check(ValidationRules.validKeyUsage(), certificate, i18n
/* 580 */           .getMessage("invalidKeyUsage"), "invalidKeyUsage"));
/* 581 */     validationErrorsOptional.add(ValidationRuleChecker.check(ValidationRules.validBasicConstraints(), certificate, i18n
/* 582 */           .getMessage("invalidBasicConstraints"), "invalidBasicConstraints"));
/*     */     
/* 584 */     return validationErrorsOptional;
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
/*     */   private static List<Optional<ValidationError>> getSpecificValidation(Certificate certificate, MetadataManager i18n) {
/* 597 */     List<Optional<ValidationError>> validationErrorsOptionals = new ArrayList<>();
/* 598 */     validationErrorsOptionals.add(ValidationRuleChecker.check(ValidationRules.validSubject(), certificate, i18n
/* 599 */           .getMessage("invalidSubject"), "invalidSubject"));
/*     */     
/* 601 */     switch (certificate.getType()) {
/*     */       case ECU_CERTIFICATE:
/* 603 */         validationErrorsOptionals.addAll(validEcuCertificateStructure(certificate, i18n));
/* 604 */         return validationErrorsOptionals;
/*     */       case VARIANT_CODE_USER_CERTIFICATE:
/* 606 */         validationErrorsOptionals.addAll(validVariantCodingUserCertificateStructure(certificate, i18n));
/* 607 */         return validationErrorsOptionals;
/*     */       case VARIANT_CODING_DEVICE_CERTIFICATE:
/* 609 */         return validationErrorsOptionals;
/*     */       case DIAGNOSTIC_AUTHENTICATION_CERTIFICATE:
/* 611 */         validationErrorsOptionals.addAll(validDiagCertificateStructure(certificate, i18n));
/* 612 */         return validationErrorsOptionals;
/*     */       case TIME_CERTIFICATE:
/* 614 */         validationErrorsOptionals.addAll(validTimeCertificateStructure(certificate, i18n));
/* 615 */         return validationErrorsOptionals;
/*     */       case ENHANCED_RIGHTS_CERTIFICATE:
/* 617 */         return validEnhRightsCertificateStructure(certificate, i18n);
/*     */     } 
/* 619 */     validationErrorsOptionals.addAll(validStandardStructure(certificate, i18n));
/* 620 */     return validationErrorsOptionals;
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
/*     */   private static boolean regularCertificate(Certificate certificate, Logger logger) {
/* 635 */     RawData certificateToCheck = certificate.getCertificateData();
/* 636 */     Certificate parent = certificate.getParent();
/* 637 */     if (parent == null) {
/* 638 */       return false;
/*     */     }
/* 640 */     RawData certificateParent = parent.getCertificateData();
/* 641 */     return verifySignature(certificateParent, certificateToCheck, logger);
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
/*     */   private static boolean certificateStructureValid(Certificate certificate) {
/* 654 */     boolean validPublicKeyAKIandSKI = (ValidationRules.validPublicKey().test(certificate) && ValidationRules.validAuthorityKeyIdentifier().test(certificate) && ValidationRules.validSubjectKeyIdentifier().test(certificate));
/* 655 */     return (validPublicKeyAKIandSKI && ValidationRules.validAlgorithmIdentifier().test(certificate) && 
/* 656 */       ValidationRules.validSignature().test(certificate) && 
/* 657 */       ValidationRules.validProdQualifier().test(certificate) && 
/* 658 */       ValidationRules.validPKIRole().test(certificate) && ValidationRules.validIssuer().test(certificate) && 
/* 659 */       ValidationRules.validVersion().test(certificate) && 
/* 660 */       ValidationRules.validSerialNumber().test(certificate) && 
/* 661 */       ValidationRules.isNotExpired().test(certificate));
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
/*     */   private static boolean enhancedRightsSigVerification(Certificate certificate, Logger logger) {
/* 676 */     RawData certificateToCheck = certificate.getCertificateData();
/* 677 */     Certificate parentEnhanced = certificate.getParent();
/* 678 */     if (parentEnhanced == null) {
/* 679 */       return false;
/*     */     }
/* 681 */     RawData certificateParent = parentEnhanced.getParent().getCertificateData();
/* 682 */     return verifySignature(certificateParent, certificateToCheck, logger);
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
/*     */   public static boolean exceedMinRemainingValidity(Certificate certificate, String minRemainingValidity) {
/* 695 */     if (null == minRemainingValidity) {
/* 696 */       return true;
/*     */     }
/* 698 */     LocalDateTime minValidity = DurationParser.parse(minRemainingValidity).plusTo(LocalDateTime.now());
/* 699 */     return (certificate.getValidTo().compareTo(Timestamp.valueOf(minValidity)) > 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\CertificatesValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */