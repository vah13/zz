/*     */ package com.daimler.cebas.certificates.control.validation;
/*     */ 
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.entity.UserRole;
/*     */ import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.cert.CertificateExpiredException;
/*     */ import java.security.cert.CertificateNotYetValidException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ValidationRules
/*     */ {
/*     */   public static final String CN_PREFIX = "CN=";
/*  33 */   private static final Logger LOG = Logger.getLogger(ValidationRules.class.getSimpleName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isNotExpired(Certificate certificate) {
/*  50 */     boolean notExpired = true;
/*     */     try {
/*  52 */       certificate.checkValidity();
/*  53 */     } catch (CertificateExpiredException e) {
/*  54 */       LOG.log(Level.FINEST, e.getMessage(), e);
/*  55 */       notExpired = false;
/*  56 */     } catch (CertificateNotYetValidException e) {
/*  57 */       LOG.log(Level.FINEST, e.getMessage(), e);
/*  58 */       notExpired = true;
/*     */     } 
/*  60 */     return notExpired;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> isNotExpired() {
/*  69 */     return ValidationRules::isNotExpired;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validSubject() {
/*  78 */     return certificate -> (!StringUtils.isEmpty(certificate.getSubject().replace("CN=", "")) && certificate.getSubject().length() <= 30);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validSerialNumber() {
/*  87 */     return certificate -> !StringUtils.isEmpty(certificate.getSerialNo());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validVersion() {
/*  96 */     return certificate -> !StringUtils.isEmpty(certificate.getVersion());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validIssuer() {
/* 105 */     return certificate -> {
/*     */         String issuer = certificate.getIssuer().replace("CN=", "");
/* 107 */         return (!StringUtils.isEmpty(certificate.getIssuer()) && (issuer.getBytes(StandardCharsets.UTF_8)).length <= 15);
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validProdQualifier() {
/* 117 */     return certificate -> (certificate.getProdQualifierRaw() != null && (certificate.getProdQualifierRaw()).length == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validAlgorithmIdentifier() {
/* 126 */     return certificate -> !StringUtils.isEmpty(certificate.getAlgorithmIdentifier());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validSubjectKeyIdentifier() {
/* 135 */     return certificate -> (certificate.getType() == CertificateType.ENHANCED_RIGHTS_CERTIFICATE || (certificate.getSubjectKeyIdentifierRaw() != null && (certificate.getSubjectKeyIdentifierRaw()).length == 20 && certificate.getSubjectKeyIdentifier().equals(CertificateParser.getSubjectKeyIdentifierFromPublicKey(certificate.getSubjectPublicKey()))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validTargetSubjectKeyIdentifier() {
/* 144 */     return certificate -> (certificate.getType() != CertificateType.ENHANCED_RIGHTS_CERTIFICATE || certificate.getTargetSubjectKeyIdentifier() == null || (certificate.getTargetSubjectKeyIdentifierRaw()).length == 20);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validPKIRole() {
/* 153 */     return certificate -> (certificate.getPKIRoleRaw() != null && (certificate.getPKIRoleRaw()).length == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validSignature() {
/* 162 */     return certificate -> (certificate.getSignatureRaw() != null && (certificate.getSignatureRaw()).length == 64);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validAuthorityKeyIdentifier() {
/* 171 */     return certificate -> (certificate.getType() == CertificateType.ROOT_CA_CERTIFICATE || (certificate.getAuthorityKeyIdentifierRaw() != null && (certificate.getAuthorityKeyIdentifierRaw()).length == 20));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validKeyUsage() {
/* 180 */     return certificate -> (certificate.getKeyUsage() != null && validKeyUsage(certificate.getKeyUsage()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validBasicConstraints() {
/* 189 */     return certificate -> !StringUtils.isEmpty(certificate.getBasicConstraints());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean validKeyUsage(boolean[] keyUsages) {
/* 200 */     int foundUsage = 0;
/* 201 */     for (boolean b : keyUsages) {
/* 202 */       if (b) {
/* 203 */         foundUsage++;
/*     */       }
/*     */     } 
/* 206 */     return (foundUsage == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validUniqueEcuId() {
/* 215 */     return certificate -> (!StringUtils.isEmpty(certificate.getUniqueECUID()) && validatedUniqueEcuId(certificate.getUniqueECUID(), certificate.isVsmEcu()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validSpecialECU() {
/* 224 */     return certificate -> (certificate.getSpecialEcuRaw() == null || (certificate.getSpecialEcuRaw() != null && (certificate.getSpecialEcuRaw()).length == 1 && (certificate.getSpecialEcuRaw()[0] == 0 || certificate.getSpecialEcuRaw()[0] == 1)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validUserRole() {
/* 235 */     return certificate -> (certificate.getUserRolesRaw() != null && (certificate.getUserRolesRaw()).length == 1 && !StringUtils.isEmpty(certificate.getUserRole()) && !StringUtils.equals(certificate.getUserRole(), UserRole.NO_ROLE.getText()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validServices() {
/* 244 */     return certificate -> (!StringUtils.isEmpty(certificate.getServices()) && validedServices(certificate.getServices()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validBaseCertificateID() {
/* 253 */     return certificate -> !StringUtils.isEmpty(certificate.getBaseCertificateID());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validNonce() {
/* 262 */     return certificate -> (certificate.getNonceRaw() != null && (certificate.getNonceRaw()).length == 32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validTargetVIN() {
/* 271 */     return certificate -> (certificate.getTargetVIN() == null || certificate.getTargetVIN().isEmpty() || (!StringUtils.isEmpty(certificate.getTargetVIN()) && validTargetVIN(certificate.getTargetVIN())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validTargetECU() {
/* 280 */     return certificate -> (certificate.getTargetECU() == null || certificate.getTargetECU().isEmpty() || (!StringUtils.isEmpty(certificate.getTargetECU()) && validatedEcus(certificate.getTargetECU())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validatedEcus(String targetECU) {
/* 291 */     boolean valid = true;
/* 292 */     String[] split = targetECU.split(", ");
/* 293 */     for (String ecu : split) {
/* 294 */       if (isECULengthNotCorrect(ecu)) {
/* 295 */         valid = false;
/*     */         break;
/*     */       } 
/*     */     } 
/* 299 */     return valid;
/*     */   }
/*     */   
/*     */   public static boolean isECULengthNotCorrect(String ecu) {
/* 303 */     return (null == ecu || (ecu.trim().getBytes(StandardCharsets.UTF_8)).length > 30);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validatedUniqueEcuId(String uniqueEcu, boolean isVsm) {
/* 314 */     boolean valid = true;
/* 315 */     String[] split = uniqueEcu.split(", ");
/* 316 */     int maxLength = isVsm ? 30 : 15;
/* 317 */     for (String ecu : split) {
/* 318 */       if ((ecu.trim().getBytes(StandardCharsets.UTF_8)).length > maxLength) {
/* 319 */         valid = false;
/*     */         break;
/*     */       } 
/*     */     } 
/* 323 */     return valid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validTargetVIN(String vins) {
/* 334 */     boolean valid = true;
/* 335 */     String[] split = vins.split(", ");
/* 336 */     for (String vin : split) {
/* 337 */       if (isVINLengthNotCorrect(vin)) {
/* 338 */         valid = false;
/*     */         break;
/*     */       } 
/*     */     } 
/* 342 */     return valid;
/*     */   }
/*     */   
/*     */   public static boolean isVINLengthNotCorrect(String vin) {
/* 346 */     return (null == vin || (new String(vin.getBytes(), StandardCharsets.UTF_8)).length() != 17);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validedServices(String services) {
/* 357 */     boolean valid = true;
/* 358 */     String[] split = services.split(", ");
/* 359 */     for (String service : split) {
/* 360 */       if ((service.trim().getBytes(StandardCharsets.UTF_8)).length > 8) {
/*     */         
/* 362 */         valid = false;
/*     */         break;
/*     */       } 
/*     */     } 
/* 366 */     return valid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Certificate> validPublicKey() {
/* 375 */     return certificate -> (certificate.getType() == CertificateType.ENHANCED_RIGHTS_CERTIFICATE || (certificate.getSubjectPublicKeyRaw() != null && (certificate.getSubjectPublicKeyRaw()).length == 32));
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
/*     */   public static Map<String, List<String>> validateTargetEcuAndTargetVIN(List<String> targetECUs, List<String> targetVINs) {
/* 391 */     Map<String, List<String>> result = new HashMap<>();
/* 392 */     if (!CollectionUtils.isEmpty(targetECUs)) {
/*     */ 
/*     */ 
/*     */       
/* 396 */       List<String> invalidTargetECUs = (List<String>)targetECUs.stream().filter(ecu -> (!StringUtils.isAllBlank(new CharSequence[] { ecu }) && isECULengthNotCorrect(ecu))).collect(Collectors.toList());
/* 397 */       result.put("targetECU", invalidTargetECUs);
/*     */     } 
/* 399 */     if (!CollectionUtils.isEmpty(targetVINs)) {
/*     */ 
/*     */ 
/*     */       
/* 403 */       List<String> invalidTargetVINs = (List<String>)targetVINs.stream().filter(vin -> (!StringUtils.isAllBlank(new CharSequence[] { vin }) && isVINLengthNotCorrect(vin))).collect(Collectors.toList());
/* 404 */       result.put("targetVIN", invalidTargetVINs);
/*     */     } 
/* 406 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\ValidationRules.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */