/*     */ package com.daimler.cebas.certificates.control.validation;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.InvalidInputException;
/*     */ import com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput;
/*     */ import com.daimler.cebas.certificates.control.vo.AbstractCertificateReplacementPackageInput;
/*     */ import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
/*     */ import com.daimler.cebas.certificates.control.vo.ChainReplacementPackageInput;
/*     */ import com.daimler.cebas.certificates.control.vo.CheckOwnershipInput;
/*     */ import com.daimler.cebas.certificates.control.vo.DeleteCertificateModel;
/*     */ import com.daimler.cebas.certificates.control.vo.DeleteCertificates;
/*     */ import com.daimler.cebas.certificates.control.vo.EcuSignRequestInput;
/*     */ import com.daimler.cebas.certificates.control.vo.ISecOCIsInput;
/*     */ import com.daimler.cebas.certificates.control.vo.SecureVariantCodingInput;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.common.control.HexUtil;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.FacadePattern;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.util.Base64;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.web.util.HtmlUtils;
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
/*     */ @FacadePattern
/*     */ public class CertificatesProcessValidation
/*     */ {
/*     */   protected static final String REGEX_BASE64_ENCODED = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
/*     */   
/*     */   public static void validateCheckActiveDiagCert(String backendCertSkid, String diagCertSerialNo, String targetEcu, String targetVin, MetadataManager i18n, Logger logger) {
/*  62 */     CertificatesFieldsValidator.isSubjectKeyIdentifierLengthCorrect(backendCertSkid, i18n, logger);
/*  63 */     CertificatesFieldsValidator.isSerialNoLengthCorrect(diagCertSerialNo, i18n, logger);
/*  64 */     if (targetVin != null && !targetVin.isEmpty()) {
/*  65 */       CertificatesFieldsValidator.isTargetVINLengthCorrectSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, targetVin);
/*     */     }
/*     */     
/*  68 */     if (targetEcu != null) {
/*  69 */       CertificatesFieldsValidator.isTargetECULengthCorrentSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, targetEcu);
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
/*     */   public static void validateGetCertByAuthKeyIdentAndSerialNo(String authorityKeyIdentifier, String serialNo, MetadataManager i18n, Logger logger) {
/*  88 */     CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect(authorityKeyIdentifier, i18n, logger);
/*  89 */     CertificatesFieldsValidator.isSerialNoLengthCorrect(serialNo, i18n, logger);
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
/*     */   public static void validateGetDiagCert(String backendCertSkid, String vin, String ecu, MetadataManager i18n, Logger logger) {
/* 108 */     CertificatesFieldsValidator.isSubjectKeyIdentifierLengthCorrect(backendCertSkid, i18n, logger);
/* 109 */     if (vin != null && !vin.isEmpty()) {
/* 110 */       CertificatesFieldsValidator.isTargetVINLengthCorrectSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, vin);
/*     */     }
/*     */     
/* 113 */     if (ecu != null && !ecu.isEmpty()) {
/* 114 */       CertificatesFieldsValidator.isTargetECULengthCorrentSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, ecu);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void validateGetEnhDiagCert(String backendCertSkid, String diagCertSerialNo, String vin, String ecu, MetadataManager i18n, Logger logger) {
/* 137 */     CertificatesFieldsValidator.isSubjectKeyIdentifierLengthCorrect(backendCertSkid, i18n, logger);
/* 138 */     CertificatesFieldsValidator.isSerialNoLengthCorrect(diagCertSerialNo, i18n, logger);
/* 139 */     if (vin != null && !vin.isEmpty()) {
/* 140 */       CertificatesFieldsValidator.isTargetVINLengthCorrectSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, vin);
/*     */     }
/*     */     
/* 143 */     if (ecu != null && !ecu.isEmpty()) {
/* 144 */       CertificatesFieldsValidator.isTargetECULengthCorrentSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, ecu);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static void validateGetTimeCert(String backendCertSkid, String nonce, String vin, String ecu, MetadataManager i18n, Logger logger) {
/* 166 */     CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect(backendCertSkid, i18n, logger);
/* 167 */     if (nonce.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$")) {
/* 168 */       CertificatesFieldsValidator.isNonceLengthCorrect(nonce, i18n, logger);
/*     */     } else {
/* 170 */       throwExceptionInputNotBase64Encrypted(i18n, logger, nonce);
/*     */     } 
/* 172 */     if (StringUtils.isNotEmpty(vin)) {
/* 173 */       CertificatesFieldsValidator.isTargetVINLengthCorrectSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, vin);
/* 174 */       CertificatesFieldsValidator.checkPrintableChsForTimeCertVin(logger, vin, CertificatesProcessValidation.class.getSimpleName());
/*     */     } else {
/*     */       
/* 177 */       InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("nullOrEmptyTargetVIN"), "nullOrEmptyTargetVIN");
/* 178 */       logger.log(Level.WARNING, "000192X", i18n
/* 179 */           .getEnglishMessage(invalidInputException.getMessageId()), invalidInputException.getClass().getSimpleName());
/* 180 */       throw invalidInputException;
/*     */     } 
/* 182 */     if (StringUtils.isNotEmpty(ecu)) {
/* 183 */       CertificatesFieldsValidator.isTargetECULengthCorrentSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, ecu);
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
/*     */   public static void validateGetCertificate(String certificateId, MetadataManager i18n, Logger logger) {
/* 198 */     CertificatesFieldsValidator.isUUID(certificateId, i18n, logger);
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
/*     */   public static void validateDeleteCertificates(List<String> ids, MetadataManager i18n, Logger logger) {
/* 212 */     CertificatesFieldsValidator.areUUID(ids, i18n, logger);
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
/*     */   public static void validateDeleteCertificate(DeleteCertificates deleteCertificate, MetadataManager i18n, Logger logger) {
/* 227 */     if (!deleteCertificate.isAll()) {
/* 228 */       CertificatesFieldsValidator.isEmptyInput(deleteCertificate.getModels(), i18n, logger);
/* 229 */       deleteCertificate.getModels().forEach(model -> validateDeleteCertificateModel(i18n, logger, model));
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
/*     */   private static void validateDeleteCertificateModel(MetadataManager i18n, Logger logger, DeleteCertificateModel model) {
/* 247 */     if (model.getAuthorityKeyIdentifier() != null && !model.getAuthorityKeyIdentifier().isEmpty()) {
/* 248 */       if (model.getAuthorityKeyIdentifier().matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$")) {
/* 249 */         CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect(
/* 250 */             HexUtil.bytesToHex(Base64.getDecoder().decode(model.getAuthorityKeyIdentifier())), i18n, logger);
/*     */       } else {
/*     */         
/* 253 */         throwExceptionInputNotBase64Encrypted(i18n, logger, model.getAuthorityKeyIdentifier());
/*     */       } 
/*     */     }
/*     */     
/* 257 */     String serialString = null;
/* 258 */     if (model.getSerialNo() != null) {
/* 259 */       serialString = HexUtil.bytesToHex(Base64.getDecoder().decode(model.getSerialNo()));
/*     */     }
/*     */     
/* 262 */     CertificatesFieldsValidator.isSerialNoLengthCorrect(serialString, i18n, logger);
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
/*     */   public static void validateCreateCertificateSignRequest(CertificateSignRequest certificateSignRequest, MetadataManager i18n, Logger logger) {
/* 278 */     validateCreateCertificateSignRequest(certificateSignRequest, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
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
/*     */   public static void validateCreateCertificateSignRequest(CertificateSignRequest certificateSignRequest, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy) {
/* 296 */     if (certificateSignRequest.getUserId() != null) {
/* 297 */       CertificatesFieldsValidator.isUUID(certificateSignRequest.getUserId(), i18n, logger, failureOutputStrategy);
/*     */     }
/* 299 */     if (certificateSignRequest.getSubject() != null) {
/* 300 */       CertificatesFieldsValidator.isSubjectLengthCorrect(certificateSignRequest.getSubject(), i18n, logger, failureOutputStrategy);
/*     */     }
/*     */     
/* 303 */     if (certificateSignRequest.getAuthorityKeyIdentifier() != null && 
/* 304 */       !certificateSignRequest.getAuthorityKeyIdentifier().isEmpty()) {
/* 305 */       CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect(
/* 306 */           HexUtil.bytesToHex(Base64.getDecoder().decode(certificateSignRequest.getAuthorityKeyIdentifier())), i18n, logger, failureOutputStrategy);
/*     */     }
/*     */     
/* 309 */     if (certificateSignRequest.getParentId() != null) {
/* 310 */       CertificatesFieldsValidator.isUUID(certificateSignRequest.getParentId(), i18n, logger);
/*     */     }
/* 312 */     if (certificateSignRequest.getCertificateType().equals(CertificateType.TIME_CERTIFICATE.getText())) {
/* 313 */       if (certificateSignRequest.getNonce().matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$")) {
/* 314 */         CertificatesFieldsValidator.isNonceLengthCorrect(certificateSignRequest.getNonce(), i18n, logger, failureOutputStrategy);
/*     */       } else {
/*     */         
/* 317 */         throwExceptionInputNotBase64Encrypted(i18n, logger, certificateSignRequest.getNonce());
/*     */       } 
/*     */     }
/* 320 */     if (certificateSignRequest.getCertificateType().equals(CertificateType.ECU_CERTIFICATE.getText())) {
/* 321 */       CertificatesFieldsValidator.isUniqueECUIDLengthCorrectMultiple(certificateSignRequest.getUniqueECUID(), i18n, logger, failureOutputStrategy);
/*     */       
/* 323 */       CertificatesFieldsValidator.isSpecialECULengthCorrect(certificateSignRequest.getSpecialECU(), i18n, logger, failureOutputStrategy);
/*     */     } 
/*     */     
/* 326 */     if (certificateSignRequest.getTargetECU() != null && !certificateSignRequest.getTargetECU().isEmpty()) {
/* 327 */       CertificatesFieldsValidator.isTargetECULengthCorrectMultiple(certificateSignRequest.getTargetECU(), i18n, logger, failureOutputStrategy);
/*     */     }
/*     */     
/* 330 */     if (certificateSignRequest.getTargetVIN() != null && !certificateSignRequest.getTargetVIN().isEmpty()) {
/* 331 */       CertificatesFieldsValidator.isTargetVINLengthCorrectMultiple(certificateSignRequest.getTargetVIN(), i18n, logger, failureOutputStrategy);
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
/*     */   public static void validateGetCertificateFromCSRId(String csrId, MetadataManager i18n, Logger logger) {
/* 347 */     CertificatesFieldsValidator.isUUIDForExportPublicKey(csrId, i18n, logger);
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
/*     */   public static void validateExportCsrToFile(String csrId, MetadataManager i18n, Logger logger) {
/* 361 */     CertificatesFieldsValidator.isUUIDForExportCSRToFile(csrId, i18n, logger);
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
/*     */   public static void validateCheckOwnership(CheckOwnershipInput checkOwnershipInput, MetadataManager i18n, Logger logger) {
/* 376 */     if (StringUtils.isEmpty(checkOwnershipInput.getBackendCertSubjKeyId()) || 
/* 377 */       StringUtils.isEmpty(checkOwnershipInput.getSerialNumber()) || 
/* 378 */       StringUtils.isEmpty(checkOwnershipInput.getEcuChallenge())) {
/*     */       
/* 380 */       InvalidInputException ex = new InvalidInputException(i18n.getMessage("invalidInputForCheckOwnership"), "invalidInputForCheckOwnership");
/*     */       
/* 382 */       logger.logWithTranslation(Level.WARNING, "000142X", ex.getMessageId(), ex
/* 383 */           .getClass().getSimpleName());
/* 384 */       throw ex;
/*     */     } 
/* 386 */     if (checkOwnershipInput.getBackendCertSubjKeyId().matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$")) {
/* 387 */       CertificatesFieldsValidator.isSubjectKeyIdentifierLengthCorrect(HexUtil.bytesToHex(
/* 388 */             Base64.getDecoder().decode(checkOwnershipInput.getBackendCertSubjKeyId())), i18n, logger);
/*     */     } else {
/* 390 */       throwExceptionInputNotBase64Encrypted(i18n, logger, checkOwnershipInput.getBackendCertSubjKeyId());
/*     */     } 
/*     */     
/* 393 */     if (checkOwnershipInput.getSerialNumber().matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$")) {
/* 394 */       CertificatesFieldsValidator.isSerialNoLengthCorrect(
/* 395 */           HexUtil.bytesToHex(Base64.getDecoder().decode(checkOwnershipInput.getSerialNumber())), i18n, logger);
/*     */     } else {
/*     */       
/* 398 */       throwExceptionInputNotBase64Encrypted(i18n, logger, checkOwnershipInput.getSerialNumber());
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
/*     */   public static void validateGetSecOCISCert(ISecOCIsInput secOCISInput, MetadataManager i18n, Logger logger) {
/* 413 */     if (secOCISInput.getBackendCertSubjKeyId().matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$")) {
/* 414 */       CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect(
/* 415 */           HexUtil.bytesToHex(Base64.getDecoder().decode(secOCISInput.getBackendCertSubjKeyId())), i18n, logger);
/*     */     } else {
/*     */       
/* 418 */       throwExceptionInputNotBase64Encrypted(i18n, logger, secOCISInput.getBackendCertSubjKeyId());
/*     */     } 
/*     */     
/* 421 */     if (secOCISInput.getDiagCertSerialNumber().matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$")) {
/* 422 */       CertificatesFieldsValidator.isSerialNoLengthCorrect(HexUtil.bytesToHex(Base64.getDecoder().decode(secOCISInput.getDiagCertSerialNumber())), i18n, logger);
/*     */     } else {
/* 424 */       throwExceptionInputNotBase64Encrypted(i18n, logger, secOCISInput.getDiagCertSerialNumber());
/*     */     } 
/* 426 */     if (secOCISInput.getTargetECU() != null && !secOCISInput.getTargetECU().isEmpty()) {
/* 427 */       CertificatesFieldsValidator.isTargetECULengthCorrentSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, secOCISInput.getTargetECU());
/*     */     }
/* 429 */     if (secOCISInput.getTargetVIN() != null && !secOCISInput.getTargetVIN().isEmpty()) {
/* 430 */       CertificatesFieldsValidator.isTargetVINLengthCorrectSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, secOCISInput.getTargetVIN());
/* 431 */       CertificatesFieldsValidator.checkPrintableChsForSecOCISCertVin(logger, secOCISInput.getTargetVIN(), CertificatesProcessValidation.class.getSimpleName());
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
/*     */   public static void validateSecureVariantCoding(SecureVariantCodingInput secureVariantCodingInput, MetadataManager i18n, Logger logger) {
/* 447 */     if (secureVariantCodingInput.getBackendSubjectKeyIdentifier().matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$")) {
/* 448 */       CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect(
/* 449 */           HexUtil.bytesToHex(
/* 450 */             Base64.getDecoder().decode(secureVariantCodingInput.getBackendSubjectKeyIdentifier())), i18n, logger);
/*     */     } else {
/*     */       
/* 453 */       throwExceptionInputNotBase64Encrypted(i18n, logger, secureVariantCodingInput
/* 454 */           .getBackendSubjectKeyIdentifier());
/*     */     } 
/* 456 */     if (secureVariantCodingInput.getTargetECU() != null) {
/* 457 */       CertificatesFieldsValidator.isTargetECULengthCorrentSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, secureVariantCodingInput
/* 458 */           .getTargetECU());
/*     */     }
/* 460 */     if (secureVariantCodingInput.getTargetVIN() != null && !secureVariantCodingInput.getTargetVIN().isEmpty()) {
/* 461 */       CertificatesFieldsValidator.isTargetVINLengthCorrectSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, secureVariantCodingInput
/* 462 */           .getTargetVIN());
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
/*     */   public static void validateGetDataSignedByEcuPrivateKey(EcuSignRequestInput input, Logger logger, MetadataManager i18n) {
/* 478 */     if (StringUtils.isEmpty(input.getBackendSubjectKeyIdentifier())) {
/* 479 */       handleGetDataSignedByEcuPrivateKeyInvalidInput("backendSubjectKeyIdentifierMandatory", logger, i18n);
/*     */     }
/*     */     
/* 482 */     if (StringUtils.isEmpty(input.getChallenge())) {
/* 483 */       handleGetDataSignedByEcuPrivateKeyInvalidInput("challangeByteArrayMandatory", logger, i18n);
/*     */     }
/*     */     
/* 486 */     if (StringUtils.isEmpty(input.getEcuId())) {
/* 487 */       handleGetDataSignedByEcuPrivateKeyInvalidInput("invalidUniqueEcuId", logger, i18n);
/*     */     }
/*     */     
/* 490 */     if (input.getBackendSubjectKeyIdentifier().matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$")) {
/* 491 */       CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect(
/* 492 */           HexUtil.bytesToHex(Base64.getDecoder().decode(input.getBackendSubjectKeyIdentifier())), i18n, logger);
/*     */     } else {
/*     */       
/* 495 */       throwExceptionInputNotBase64Encrypted(i18n, logger, input.getBackendSubjectKeyIdentifier());
/*     */     } 
/*     */     
/* 498 */     if (!StringUtils.isEmpty(input.getEcuSerialNumber())) {
/* 499 */       if (input.getEcuSerialNumber().matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$")) {
/* 500 */         CertificatesFieldsValidator.isSerialNoLengthCorrect(
/* 501 */             HexUtil.bytesToHex(Base64.getDecoder().decode(input.getEcuSerialNumber())), i18n, logger);
/*     */       } else {
/*     */         
/* 504 */         throwExceptionInputNotBase64Encrypted(i18n, logger, input.getEcuSerialNumber());
/*     */       } 
/*     */     }
/*     */     
/* 508 */     if (!input.getChallenge().matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$")) {
/* 509 */       throwExceptionInputNotBase64Encrypted(i18n, logger, input.getChallenge());
/*     */     }
/*     */     
/* 512 */     CertificatesFieldsValidator.isUniqueECUIDLengthCorrectSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, input
/* 513 */         .getEcuId());
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
/*     */   public static void validateAutomaticReplacementPackageInput(AbstractCertificateReplacementPackageInput input, Logger logger, MetadataManager i18n) {
/* 527 */     validateReplacementPackageCertificate(input.getCertificate(), logger, i18n);
/* 528 */     validateReplacementPackageSKI(input.getTargetBackendCertSubjKeyId(), logger, i18n);
/* 529 */     validateAutomaticReplacementPackageUniqueEcuId(input.getUniqueEcuId(), logger, i18n);
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
/*     */   public static void validateAutomaticChainReplacementPackageInput(ChainReplacementPackageInput input, Logger logger, MetadataManager i18n) {
/* 543 */     validateReplacementPackageSKI(input.getTargetBackendCertSubjKeyId(), logger, i18n);
/* 544 */     validateReplacementPackageSKI(input.getSourceBackendCertSubjKeyId(), logger, i18n);
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
/*     */   private static void validateReplacementPackageSKI(String subjectKeyIdentifier, Logger logger, MetadataManager i18n) {
/* 558 */     if (StringUtils.isEmpty(subjectKeyIdentifier)) {
/*     */       
/* 560 */       InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForSubjectKeyIdentifier", new String[] { subjectKeyIdentifier }), "invalidInputForSubjectKeyIdentifier");
/*     */       
/* 562 */       logger.logWithTranslation(Level.WARNING, "000300X", invalidInputException
/* 563 */           .getMessageId(), invalidInputException.getClass().getSimpleName());
/* 564 */       throw invalidInputException;
/*     */     } 
/*     */     
/* 567 */     if (subjectKeyIdentifier.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$")) {
/* 568 */       CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect(
/* 569 */           HexUtil.bytesToHex(Base64.getDecoder().decode(subjectKeyIdentifier)), i18n, logger);
/*     */     } else {
/* 571 */       throwExceptionInputNotBase64Encrypted(i18n, logger, subjectKeyIdentifier);
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
/*     */   protected static void throwExceptionInputNotBase64Encrypted(MetadataManager i18n, Logger logger, String value) {
/* 587 */     InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputNotBase64Encoded", new String[] {
/* 588 */             HtmlUtils.htmlEscape(value)
/*     */           }), "invalidInputNotBase64Encoded");
/* 590 */     logger.log(Level.WARNING, "000195X", i18n
/* 591 */         .getEnglishMessage(invalidInputException.getMessageId(), new String[] {
/* 592 */             HtmlUtils.htmlEscape(value)
/* 593 */           }), invalidInputException.getClass().getSimpleName());
/* 594 */     throw invalidInputException;
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
/*     */   private static void handleGetDataSignedByEcuPrivateKeyInvalidInput(String i18nMessageId, Logger logger, MetadataManager i18n) {
/* 609 */     InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage(i18nMessageId), i18nMessageId);
/* 610 */     logger.logWithTranslation(Level.WARNING, "000275X", invalidInputException.getMessageId(), invalidInputException
/* 611 */         .getClass().getSimpleName());
/* 612 */     throw invalidInputException;
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
/*     */   private static void validateAutomaticReplacementPackageUniqueEcuId(String uniqueEcuId, Logger logger, MetadataManager i18n) {
/* 627 */     if (!StringUtils.isEmpty(uniqueEcuId)) {
/* 628 */       CertificatesFieldsValidator.isUniqueECUIDLengthCorrectSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, uniqueEcuId);
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
/*     */   private static void validateReplacementPackageCertificate(String certificate, Logger logger, MetadataManager i18n) {
/* 644 */     if (StringUtils.isEmpty(certificate)) {
/*     */       
/* 646 */       InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidCertificateType", new String[] {
/* 647 */               CertificateType.ECU_CERTIFICATE.name()
/*     */             }), "invalidCertificateType");
/* 649 */       logger.logWithTranslation(Level.WARNING, "000301X", invalidInputException
/* 650 */           .getMessageId(), new String[] { CertificateType.ECU_CERTIFICATE.name() }, invalidInputException
/* 651 */           .getClass().getSimpleName());
/* 652 */       throw invalidInputException;
/*     */     } 
/*     */     
/* 655 */     if (!certificate.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$"))
/* 656 */       throwExceptionInputNotBase64Encrypted(i18n, logger, certificate); 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\CertificatesProcessValidation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */