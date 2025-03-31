/*     */ package com.daimler.cebas.certificates.control.validation;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*     */ import com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput;
/*     */ import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.HexUtil;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.util.Base64;
/*     */ import java.util.logging.Level;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CertificateSignRequestValidator
/*     */ {
/*     */   private static final String ONE_SPACE = " ";
/*  31 */   private static final String CLASS_NAME = CertificateSignRequestValidator.class
/*  32 */     .getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String PATTERN_NONCE = "^([a-zA-Z0-9]+){2}(-([a-zA-Z0-9]+){2}){31}";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void validate(CertificateSignRequest certificateSignRequest, Logger logger, MetadataManager i18n) {
/*  58 */     validate(certificateSignRequest, logger, i18n, ValidationFailureOutput::outputFailureWithThrow);
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
/*     */   public static void validate(CertificateSignRequest certificateSignRequest, Logger logger, MetadataManager i18n, ValidationFailureOutput strategy) {
/*  78 */     String METHOD_NAME = "validate";
/*  79 */     logger.entering(CLASS_NAME, "validate");
/*  80 */     StringBuilder englishMessages = new StringBuilder();
/*  81 */     StringBuilder translatedMessages = new StringBuilder();
/*     */     
/*  83 */     CertificateType type = CertificateType.valueFromString(certificateSignRequest.getCertificateType());
/*     */     
/*  85 */     if (certificateSignRequest.getParentId() == null && certificateSignRequest
/*  86 */       .getAuthorityKeyIdentifier() == null && certificateSignRequest
/*  87 */       .getParentId().isEmpty() && certificateSignRequest
/*  88 */       .getAuthorityKeyIdentifier()
/*  89 */       .isEmpty()) {
/*  90 */       saveMessages(englishMessages, translatedMessages, "parentCannotBeEmpty", i18n);
/*     */     }
/*     */ 
/*     */     
/*  94 */     if (type == CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE && (certificateSignRequest
/*  95 */       .getUserRole() == null || certificateSignRequest
/*  96 */       .getUserRole().isEmpty())) {
/*  97 */       saveMessages(englishMessages, translatedMessages, "userRoleCannotBeEmpty", i18n);
/*     */     }
/*     */ 
/*     */     
/* 101 */     if (type == CertificateType.ECU_CERTIFICATE) {
/* 102 */       if (certificateSignRequest.getUniqueECUID() == null || certificateSignRequest
/* 103 */         .getUniqueECUID().isEmpty() || certificateSignRequest
/* 104 */         .getUniqueECUID().length() > 30) {
/* 105 */         saveMessages(englishMessages, translatedMessages, "uniqueEcuIDMaxSize", i18n);
/*     */       }
/*     */ 
/*     */       
/* 109 */       if (certificateSignRequest.getTargetECU() != null && 
/* 110 */         !certificateSignRequest.getTargetECU().isEmpty()) {
/* 111 */         saveMessages(englishMessages, translatedMessages, "targetECUNotApplyToECU", i18n);
/*     */       }
/*     */ 
/*     */       
/* 115 */       if (certificateSignRequest.getTargetVIN() != null && 
/* 116 */         !certificateSignRequest.getTargetVIN().isEmpty()) {
/* 117 */         saveMessages(englishMessages, translatedMessages, "targetVINNotApplyToECU", i18n);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 122 */     if (type == CertificateType.TIME_CERTIFICATE) {
/*     */ 
/*     */       
/* 125 */       String base64DecodedNonce = (certificateSignRequest.getNonce() != null) ? HexUtil.bytesToHex(Base64.getDecoder()
/* 126 */           .decode(certificateSignRequest.getNonce())) : null;
/*     */       
/* 128 */       if (base64DecodedNonce == null || base64DecodedNonce.isEmpty() || (
/* 129 */         !base64DecodedNonce.matches("^([a-zA-Z0-9]+){2}(-([a-zA-Z0-9]+){2}){31}") && (
/* 130 */         CertificateParser.hexStringToByteArray(base64DecodedNonce)).length != 32))
/*     */       {
/* 132 */         saveMessages(englishMessages, translatedMessages, "nonceCannotBeEmptyMatchPattern", "^([a-zA-Z0-9]+){2}(-([a-zA-Z0-9]+){2}){31}", i18n);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 138 */     if (type != CertificateType.TIME_CERTIFICATE && certificateSignRequest
/* 139 */       .getNonce() != null && 
/* 140 */       !certificateSignRequest.getNonce().isEmpty()) {
/* 141 */       saveMessages(englishMessages, translatedMessages, "nonceSetOnlyForTimeCert", i18n);
/*     */     }
/*     */ 
/*     */     
/* 145 */     if (type != CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE && type != CertificateType.TIME_CERTIFICATE && certificateSignRequest
/*     */       
/* 147 */       .getUserRole() != null && 
/* 148 */       !certificateSignRequest.getUserRole().isEmpty()) {
/* 149 */       saveMessages(englishMessages, translatedMessages, "userRoleSetDiagCertificate", i18n);
/*     */     }
/*     */ 
/*     */     
/* 153 */     if (type != CertificateType.ECU_CERTIFICATE && certificateSignRequest
/* 154 */       .getUniqueECUID() != null && 
/* 155 */       !certificateSignRequest.getUniqueECUID().isEmpty()) {
/* 156 */       saveMessages(englishMessages, translatedMessages, "uniqueECUIDOnlyForECUCert", i18n);
/*     */     }
/*     */ 
/*     */     
/* 160 */     if (type != CertificateType.ECU_CERTIFICATE && certificateSignRequest
/* 161 */       .getSpecialECU() != null && 
/* 162 */       !certificateSignRequest.getSpecialECU().isEmpty()) {
/* 163 */       saveMessages(englishMessages, translatedMessages, "specialECUCanBeOnlyECUCert", i18n);
/*     */     }
/*     */ 
/*     */     
/* 167 */     if (certificateSignRequest.getValidTo() == null) {
/* 168 */       saveMessages(englishMessages, translatedMessages, "validToNotEmpty", i18n);
/*     */     }
/*     */ 
/*     */     
/* 172 */     if (englishMessages.length() > 0) {
/*     */       
/* 174 */       CEBASCertificateException zenzefiCertificateException = new CEBASCertificateException(i18n.getMessage("certificateSignReqFailed", new String[] {
/* 175 */               translatedMessages.toString()
/*     */             }), "certificateSignReqFailed");
/* 177 */       logger.logWithTranslation(Level.WARNING, "000105X", zenzefiCertificateException
/*     */           
/* 179 */           .getMessageId(), new String[] { englishMessages
/* 180 */             .toString() }, zenzefiCertificateException
/* 181 */           .getClass().getSimpleName());
/* 182 */       strategy.outputFailure((CEBASException)zenzefiCertificateException);
/*     */     } 
/* 184 */     logger.exiting(CLASS_NAME, "validate");
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
/*     */   private static void saveMessages(StringBuilder englishMessages, StringBuilder translatedMessages, String messageId, String argument, MetadataManager i18n) {
/* 204 */     englishMessages.append(" ").append(i18n
/* 205 */         .getEnglishMessage(messageId, new String[] { argument }));
/* 206 */     translatedMessages.append(" ")
/* 207 */       .append(i18n.getMessage(messageId, new String[] { argument }));
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
/*     */   private static void saveMessages(StringBuilder englishMessages, StringBuilder translatedMessages, String messageId, MetadataManager i18n) {
/* 225 */     englishMessages.append(" ")
/* 226 */       .append(i18n.getEnglishMessage(messageId));
/* 227 */     translatedMessages.append(" ").append(i18n.getMessage(messageId));
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\CertificateSignRequestValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */