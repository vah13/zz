/*     */ package com.daimler.cebas.certificates.control.validation;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.InvalidInputException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.InvalidInputForExportPublicKeyFileException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.SecOCISException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.TimeException;
/*     */ import com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput;
/*     */ import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.HexUtil;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.FacadePattern;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class CertificatesFieldsValidator
/*     */ {
/*     */   private static final String VALUE_SEPARATOR = ", ";
/*     */   private static final String REGEX_UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
/*     */   public static final int SERIAL_NO_MAX_SIZE = 16;
/*     */   public static final int AUTHORITY_KEY_IDENTIFIER_SIZE = 20;
/*     */   public static final int TARGET_ECU_MAX_SIZE = 30;
/*     */   public static final int SUBJECT_KEY_IDENTIFIER_SIZE = 20;
/*     */   public static final int NONCE_SIZE = 32;
/*     */   public static final int TARGET_VIN_SIZE = 17;
/*     */   public static final int SUBJECT_MAX_SIZE = 30;
/*     */   public static final int UNIQUE_ECU_ID_MAX_SIZE = 30;
/*     */   public static final int SPECIAL_ECU_SIZE = 1;
/*     */   public static final String REGEX_VIN_PRINTABLE_CHS = "[\\x21-\\x7E]+";
/*     */   public static final String DEFAULT_ZK_NO_REGEX = "^A?\\d{10}$";
/*     */   public static final int ALGORITHM_IDENTIFIER_MAX_DB_SIZE = 255;
/*     */   public static final int AUTHORITY_KEY_IDENTIFIER_MAX_DB_SIZE = 120;
/*     */   public static final int BASE_CERTIFICATE_ID_MAX_DB_SIZE = 255;
/*     */   public static final int BASIC_CONSTRAINTS_MAX_DB_SIZE = 255;
/*     */   public static final int ISSUER_MAX_DB_SIZE = 50;
/*     */   public static final int ISSUER_SERIAL_NUMBER_MAX_DB_SIZE = 100;
/*     */   public static final int NONCE_MAX_DB_SIZE = 150;
/*     */   public static final int PROD_QUALIFIER_MAX_DB_SIZE = 50;
/*     */   public static final int SERIAL_NO_MAX_DB_SIZE = 100;
/*     */   public static final int SPECIAL_ECU_MAX_DB_SIZE = 30;
/*     */   public static final int SUBJECT_MAX_DB_SIZE = 50;
/*     */   public static final int SUBJECT_KEY_IDENTIFIER_MAX_DB_SIZE = 120;
/*     */   public static final int SUBJECT_PUBLIC_KEY_MAX_DB_SIZE = 150;
/*     */   public static final int TARGET_SUBJECT_KEY_IDENTIFIER_MAX_DB_SIZE = 120;
/*     */   public static final int USER_ROLE_MAX_DB_SIZE = 255;
/*     */   public static final int VERSION_MAX_DB_SIZE = 20;
/* 112 */   private static String zkNoRegex = "^A?\\d{10}$";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setZkNoRegex(String zkNoRegex) {
/* 122 */     if (null != zkNoRegex) {
/* 123 */       CertificatesFieldsValidator.zkNoRegex = zkNoRegex;
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
/*     */   public static void isUUID(String idToBeVerify, MetadataManager i18n, Logger logger) {
/* 138 */     isUUID(idToBeVerify, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
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
/*     */   public static void isUUID(String idToBeVerify, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy) {
/* 155 */     if (!idToBeVerify.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
/*     */       
/* 157 */       InvalidInputException invalidInputIDException = new InvalidInputException(i18n.getMessage("invalidInputForUUID", new String[] {
/* 158 */               HtmlUtils.htmlEscape(idToBeVerify)
/*     */             }), "invalidInputForUUID");
/* 160 */       logger.log(Level.WARNING, "000188X", i18n
/* 161 */           .getEnglishMessage(invalidInputIDException.getMessageId(), new String[] {
/* 162 */               HtmlUtils.htmlEscape(idToBeVerify)
/* 163 */             }), invalidInputIDException.getClass().getSimpleName());
/* 164 */       failureOutputStrategy.outputFailure((CEBASException)invalidInputIDException);
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
/*     */   public static void isUUIDForExportPublicKey(String idToBeVerify, MetadataManager i18n, Logger logger) {
/* 179 */     if (!idToBeVerify.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
/*     */       
/* 181 */       InvalidInputForExportPublicKeyFileException invalidInputIDException = new InvalidInputForExportPublicKeyFileException(i18n.getMessage("invalidInputForUUID", new String[] {
/* 182 */               HtmlUtils.htmlEscape(idToBeVerify)
/*     */             }), "invalidInputForUUID");
/* 184 */       logger.log(Level.WARNING, "000194X", i18n
/* 185 */           .getEnglishMessage(invalidInputIDException.getMessageId(), new String[] {
/* 186 */               HtmlUtils.htmlEscape(idToBeVerify)
/* 187 */             }), invalidInputIDException.getClass().getSimpleName());
/* 188 */       throw invalidInputIDException;
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
/*     */   public static void isUUIDForExportCSRToFile(String idToBeVerify, MetadataManager i18n, Logger logger) {
/* 203 */     if (!idToBeVerify.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
/*     */       
/* 205 */       InvalidInputException invalidInputIDException = new InvalidInputException(i18n.getMessage("invalidInputForUUID", new String[] {
/* 206 */               HtmlUtils.htmlEscape(idToBeVerify)
/*     */             }), "invalidInputForUUID");
/* 208 */       logger.log(Level.WARNING, "000194X", i18n
/* 209 */           .getEnglishMessage(invalidInputIDException.getMessageId(), new String[] {
/* 210 */               HtmlUtils.htmlEscape(idToBeVerify)
/* 211 */             }), invalidInputIDException.getClass().getSimpleName());
/* 212 */       throw invalidInputIDException;
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
/*     */   public static void areUUID(List<String> idsToBeVerify, MetadataManager i18n, Logger logger) {
/* 227 */     List<String> notValidIDS = new ArrayList<>();
/* 228 */     for (String entry : idsToBeVerify) {
/* 229 */       if (!entry.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
/* 230 */         notValidIDS.add(entry);
/*     */       }
/*     */     } 
/* 233 */     if (!notValidIDS.isEmpty()) {
/* 234 */       notValidIDS.forEach(notValidID -> isUUID(notValidID, i18n, logger));
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
/*     */   public static void isSerialNoLengthCorrect(String serialNoToBeVerify, MetadataManager i18n, Logger logger) {
/* 249 */     if (serialNoToBeVerify == null || (
/* 250 */       CertificateParser.hexStringToByteArray(serialNoToBeVerify)).length > 16 || (
/* 251 */       CertificateParser.hexStringToByteArray(serialNoToBeVerify)).length < 1) {
/*     */       
/* 253 */       InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForSerialNumber", new String[] {
/* 254 */               htmlEscape(serialNoToBeVerify)
/*     */             }), "invalidInputForSerialNumber");
/* 256 */       logger.log(Level.WARNING, "000190X", i18n
/* 257 */           .getEnglishMessage(invalidInputException.getMessageId(), new String[] {
/* 258 */               HtmlUtils.htmlEscape(htmlEscape(serialNoToBeVerify))
/* 259 */             }), invalidInputException.getClass().getSimpleName());
/* 260 */       throw invalidInputException;
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
/*     */   public static void isAuthorityKeyIdentifierLengthCorrect(String authorityKeyIdentifierToBeVerify, MetadataManager i18n, Logger logger) {
/* 276 */     isAuthorityKeyIdentifierLengthCorrect(authorityKeyIdentifierToBeVerify, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
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
/*     */   public static void isAuthorityKeyIdentifierLengthCorrect(String authorityKeyIdentifierToBeVerify, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy) {
/* 294 */     if (authorityKeyIdentifierToBeVerify == null || (
/* 295 */       CertificateParser.hexStringToByteArray(authorityKeyIdentifierToBeVerify)).length != 20) {
/*     */       
/* 297 */       InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForAuthorityKeyIdentifier", new String[] {
/* 298 */               htmlEscape(authorityKeyIdentifierToBeVerify)
/*     */             }), "invalidInputForAuthorityKeyIdentifier");
/* 300 */       logger.log(Level.WARNING, "000191X", i18n
/* 301 */           .getEnglishMessage(invalidInputException.getMessageId(), new String[] {
/* 302 */               htmlEscape(authorityKeyIdentifierToBeVerify)
/* 303 */             }), invalidInputException.getClass().getSimpleName());
/* 304 */       failureOutputStrategy.outputFailure((CEBASException)invalidInputException);
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
/*     */   public static void isSubjectKeyIdentifierLengthCorrect(String subjectKeyIdentifierToBeVerify, MetadataManager i18n, Logger logger) {
/* 319 */     if (subjectKeyIdentifierToBeVerify == null || (
/* 320 */       CertificateParser.hexStringToByteArray(subjectKeyIdentifierToBeVerify)).length != 20) {
/*     */       
/* 322 */       InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForSubjectKeyIdentifier", new String[] {
/* 323 */               htmlEscape(subjectKeyIdentifierToBeVerify)
/*     */             }), "invalidInputForSubjectKeyIdentifier");
/* 325 */       logger.log(Level.WARNING, "000193X", i18n
/* 326 */           .getEnglishMessage(invalidInputException.getMessageId(), new String[] {
/* 327 */               htmlEscape(subjectKeyIdentifierToBeVerify)
/* 328 */             }), invalidInputException.getClass().getSimpleName());
/* 329 */       throw invalidInputException;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSubjectKeyIdentifier(String subjectKeyIdentifierToBeVerify) {
/* 340 */     return (subjectKeyIdentifierToBeVerify == null || (CertificateParser.hexStringToByteArray(subjectKeyIdentifierToBeVerify)).length == 20 || 
/* 341 */       !isZkNo(subjectKeyIdentifierToBeVerify));
/*     */   }
/*     */   
/*     */   public static boolean isZkNo(String someValue) {
/* 345 */     return (null != someValue && someValue.matches(zkNoRegex));
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
/*     */   public static void isIdentifierValid(String identifierToVerify, MetadataManager i18n, Logger logger) {
/* 359 */     if (StringUtils.isBlank(identifierToVerify) || (
/* 360 */       !isZkNo(identifierToVerify) && (CertificateParser.hexStringToByteArray(HexUtil.base64ToHex(identifierToVerify))).length != 20)) {
/*     */       
/* 362 */       InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForIdentifier", new String[] {
/* 363 */               htmlEscape(identifierToVerify)
/*     */             }), "invalidInputForIdentifier");
/* 365 */       logger.log(Level.WARNING, "000546X", i18n
/* 366 */           .getEnglishMessage(invalidInputException.getMessageId(), new String[] {
/* 367 */               htmlEscape(identifierToVerify)
/* 368 */             }), invalidInputException.getClass().getSimpleName());
/* 369 */       throw invalidInputException;
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
/*     */   public static void isTargetVINLengthCorrectMultiple(String targetVinToBeVerify, MetadataManager i18n, Logger logger) {
/* 385 */     isTargetVINLengthCorrectMultiple(targetVinToBeVerify, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
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
/*     */   public static void isTargetVINLengthCorrectMultiple(String targetVinToBeVerify, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy) {
/* 403 */     String[] vins = targetVinToBeVerify.split(", ");
/* 404 */     for (String vin : vins) {
/* 405 */       isTargetVINLengthCorrectSingle(i18n, logger, failureOutputStrategy, vin);
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
/*     */   public static void isTargetVINLengthCorrectSingle(MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy, String vin) {
/* 419 */     if (vin.length() != 17) {
/*     */       
/* 421 */       InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForTargetVIN", new String[] {
/* 422 */               HtmlUtils.htmlEscape(vin)
/*     */             }), "invalidInputForTargetVIN");
/* 424 */       logger.log(Level.WARNING, "000192X", i18n
/* 425 */           .getEnglishMessage(invalidInputException.getMessageId(), new String[] {
/* 426 */               HtmlUtils.htmlEscape(vin)
/* 427 */             }), invalidInputException.getClass().getSimpleName());
/* 428 */       failureOutputStrategy.outputFailure((CEBASException)invalidInputException);
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
/*     */   public static void isTargetECULengthCorrectMultiple(String targetEcuToBeVerify, MetadataManager i18n, Logger logger) {
/* 444 */     isTargetECULengthCorrectMultiple(targetEcuToBeVerify, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
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
/*     */   public static void isTargetECULengthCorrectMultiple(String targetEcuToBeVerify, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy) {
/* 462 */     String[] ecus = targetEcuToBeVerify.split(", ");
/* 463 */     for (String ecu : ecus) {
/* 464 */       isTargetECULengthCorrentSingle(i18n, logger, failureOutputStrategy, ecu);
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
/*     */   public static void isTargetECULengthCorrentSingle(MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy, String ecu) {
/* 478 */     if (ecu.length() > 30) {
/*     */       
/* 480 */       InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForTargetECU", new String[] {
/* 481 */               HtmlUtils.htmlEscape(ecu)
/*     */             }), "invalidInputForTargetECU");
/* 483 */       logger.log(Level.WARNING, "000180X", i18n
/* 484 */           .getEnglishMessage(invalidInputException.getMessageId(), new String[] {
/* 485 */               HtmlUtils.htmlEscape(ecu)
/* 486 */             }), invalidInputException.getClass().getSimpleName());
/* 487 */       failureOutputStrategy.outputFailure((CEBASException)invalidInputException);
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
/*     */   public static void isEmptyInput(List<?> input, MetadataManager i18n, Logger logger) {
/* 502 */     if (input == null || input.isEmpty()) {
/*     */       
/* 504 */       InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputEmptyList"), "invalidInputEmptyList");
/*     */       
/* 506 */       logger.log(Level.WARNING, "000181X", i18n
/* 507 */           .getEnglishMessage(invalidInputException.getMessageId()), invalidInputException
/* 508 */           .getClass().getSimpleName());
/* 509 */       throw invalidInputException;
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
/*     */   public static void isNonceLengthCorrect(String nonceToBeVerify, MetadataManager i18n, Logger logger) {
/* 524 */     isNonceLengthCorrect(nonceToBeVerify, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
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
/*     */   public static void isNonceLengthCorrect(String nonceToBeVerify, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOuputStrategy) {
/* 541 */     byte[] byteNonceToBeVerify = Base64.getDecoder().decode(nonceToBeVerify);
/* 542 */     if (Base64.getDecoder().decode(nonceToBeVerify) == null || (
/* 543 */       Base64.getDecoder().decode(nonceToBeVerify)).length != 32) {
/*     */       
/* 545 */       InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForNonce", new String[] {
/* 546 */               HtmlUtils.htmlEscape(HexUtil.bytesToHex(byteNonceToBeVerify))
/*     */             }), "invalidInputForNonce");
/* 548 */       logger.log(Level.WARNING, "000182X", i18n
/* 549 */           .getEnglishMessage(invalidInputException.getMessageId(), new String[] {
/* 550 */               HtmlUtils.htmlEscape(HexUtil.bytesToHex(byteNonceToBeVerify))
/* 551 */             }), invalidInputException.getClass().getSimpleName());
/* 552 */       failureOuputStrategy.outputFailure((CEBASException)invalidInputException);
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
/*     */   public static void isSubjectLengthCorrect(String subjectToBeVerify, MetadataManager i18n, Logger logger) {
/* 567 */     isSubjectLengthCorrect(subjectToBeVerify, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
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
/*     */   public static void isSubjectLengthCorrect(String subjectToBeVerify, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy) {
/* 584 */     byte[] byteSubjectToBeVerify = subjectToBeVerify.getBytes(StandardCharsets.UTF_8);
/* 585 */     if (byteSubjectToBeVerify.length > 30) {
/*     */       
/* 587 */       InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForSubject", new String[] {
/* 588 */               HtmlUtils.htmlEscape(subjectToBeVerify)
/*     */             }), "invalidInputForSubject");
/* 590 */       logger.log(Level.WARNING, "000183X", i18n
/* 591 */           .getEnglishMessage(invalidInputException.getMessageId(), new String[] {
/* 592 */               HtmlUtils.htmlEscape(subjectToBeVerify)
/* 593 */             }), invalidInputException.getClass().getSimpleName());
/* 594 */       failureOutputStrategy.outputFailure((CEBASException)invalidInputException);
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
/*     */   public static void isUniqueECUIDLengthCorrectMultiple(String uniqueECUIDToBeVerify, MetadataManager i18n, Logger logger) {
/* 610 */     isUniqueECUIDLengthCorrectMultiple(uniqueECUIDToBeVerify, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
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
/*     */   public static void isUniqueECUIDLengthCorrectMultiple(String uniqueECUIDToBeVerify, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy) {
/* 628 */     String[] uniqueEcus = uniqueECUIDToBeVerify.split(", ");
/* 629 */     for (String uniqueEcu : uniqueEcus) {
/* 630 */       isUniqueECUIDLengthCorrectSingle(i18n, logger, failureOutputStrategy, uniqueEcu);
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
/*     */   public static void isUniqueECUIDLengthCorrectSingle(MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy, String uniqueEcu) {
/* 645 */     if (uniqueEcu.length() > 30) {
/*     */       
/* 647 */       InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForUniqueECUID", new String[] {
/* 648 */               HtmlUtils.htmlEscape(uniqueEcu)
/*     */             }), "invalidInputForUniqueECUID");
/* 650 */       logger.log(Level.WARNING, "000185X", i18n
/* 651 */           .getEnglishMessage(invalidInputException.getMessageId(), new String[] {
/* 652 */               HtmlUtils.htmlEscape(uniqueEcu)
/* 653 */             }), invalidInputException.getClass().getSimpleName());
/* 654 */       failureOutputStrategy.outputFailure((CEBASException)invalidInputException);
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
/*     */   public static void isSpecialECULengthCorrect(String specialECUToBeVerify, MetadataManager i18n, Logger logger) {
/* 669 */     isSpecialECULengthCorrect(specialECUToBeVerify, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean vinContainsOnlyPrintableChs(String targetVin) {
/* 680 */     return targetVin.matches("[\\x21-\\x7E]+");
/*     */   }
/*     */   
/*     */   public static void checkPrintableChsForTimeCertVin(Logger logger, String targetVin, String CLASS_NAME) {
/* 684 */     if (!vinContainsOnlyPrintableChs(targetVin)) {
/* 685 */       String message = "Target VIN: " + targetVin + " should contain only printable characters in Time Certificate request";
/* 686 */       logger.log(Level.SEVERE, "000647X", message, CLASS_NAME);
/* 687 */       throw new TimeException(message);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void checkPrintableChsForSecOCISCertVin(Logger logger, String targetVin, String CLASS_NAME) {
/* 692 */     String message = "Target VIN: " + targetVin + " should contain only printable characters in SecOCIS Certificate request";
/* 693 */     if (!vinContainsOnlyPrintableChs(targetVin)) {
/* 694 */       logger.log(Level.SEVERE, "000648X", message, CLASS_NAME);
/* 695 */       throw new SecOCISException(message);
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
/*     */   public static void isSpecialECULengthCorrect(String specialECUToBeVerify, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy) {
/* 712 */     if (specialECUToBeVerify.length() != 1) {
/*     */       
/* 714 */       InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForSpecialECU", new String[] {
/* 715 */               HtmlUtils.htmlEscape(specialECUToBeVerify)
/*     */             }), "invalidInputForSpecialECU");
/* 717 */       logger.log(Level.WARNING, "000186X", i18n
/* 718 */           .getEnglishMessage(invalidInputException.getMessageId(), new String[] {
/* 719 */               HtmlUtils.htmlEscape(specialECUToBeVerify)
/* 720 */             }), invalidInputException.getClass().getSimpleName());
/* 721 */       failureOutputStrategy.outputFailure((CEBASException)invalidInputException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String htmlEscape(String string) {
/* 732 */     return (string != null) ? HtmlUtils.htmlEscape(string) : "null";
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\CertificatesFieldsValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */