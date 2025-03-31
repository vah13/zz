/*     */ package com.daimler.cebas.certificates.control.validation;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.DurationParser;
/*     */ import com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput;
/*     */ import com.daimler.cebas.certificates.entity.PKIRole;
/*     */ import com.daimler.cebas.certificates.entity.UserRole;
/*     */ import com.daimler.cebas.certificates.integration.vo.Permission;
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.FacadePattern;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.time.format.DateTimeParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
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
/*     */ public final class PermissionsValidator
/*     */ {
/*  31 */   private static final Logger LOG = Logger.getLogger(PermissionsValidator.class.getSimpleName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean validate(Permission permission, Logger logger, MetadataManager i18n) {
/*  52 */     List<String> errorMessages = new ArrayList<>();
/*  53 */     validatePKIRole(permission.getPkiRole(), errorMessages, i18n);
/*  54 */     validateUserRole(permission.getUserRole(), errorMessages, i18n);
/*  55 */     validateTargetECU(permission.getTargetECU(), errorMessages, logger, i18n);
/*  56 */     validateTargetVIN(permission.getTargetVIN(), errorMessages, logger, i18n);
/*  57 */     validateRenewal(permission.getRenewal(), errorMessages, i18n);
/*  58 */     validateUniqueECUId(permission.getUniqueECUID(), errorMessages, logger, i18n);
/*  59 */     StringBuilder errorMessagesHolder = new StringBuilder();
/*  60 */     if (!errorMessages.isEmpty()) {
/*  61 */       errorMessages.forEach(errorMessagesHolder::append);
/*  62 */       logger.log(Level.WARNING, "000317X", "Ignoring invalid permission: " + permission
/*  63 */           .toString() + " Reason: " + errorMessagesHolder, PermissionsValidator.class
/*  64 */           .getSimpleName());
/*  65 */       permission.setValid(false);
/*  66 */       return false;
/*     */     } 
/*  68 */     permission.setValid(true);
/*  69 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void validatePKIRole(String role, List<String> errorMessagesIds, MetadataManager i18n) {
/*  80 */     if (StringUtils.isEmpty(role)) {
/*  81 */       errorMessagesIds.add(i18n.getEnglishMessage("invalidPKIRole"));
/*     */       return;
/*     */     } 
/*     */     try {
/*  85 */       int pkiRole = Integer.decode(role).intValue();
/*  86 */       if (PKIRole.getRoles().get(Integer.valueOf(pkiRole)) == null) {
/*  87 */         errorMessagesIds.add(i18n.getEnglishMessage("invalidPKIRole"));
/*     */       }
/*  89 */     } catch (NumberFormatException e) {
/*  90 */       errorMessagesIds.add(i18n.getEnglishMessage("invalidPKIRole"));
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
/*     */   private static void validateUserRole(String role, List<String> errorMessagesIds, MetadataManager i18n) {
/* 102 */     if (StringUtils.isEmpty(role)) {
/*     */       return;
/*     */     }
/*     */     try {
/* 106 */       UserRole userRole = UserRole.getUserRoleFromByte(Byte.decode(role).byteValue());
/* 107 */       if (userRole == UserRole.NO_ROLE) {
/* 108 */         errorMessagesIds.add(i18n.getEnglishMessage("invalidUserRole"));
/*     */       }
/* 110 */     } catch (NumberFormatException e) {
/* 111 */       errorMessagesIds.add(i18n.getEnglishMessage("invalidUserRole"));
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
/*     */   private static void validateTargetECU(List<String> targetECU, List<String> errorMessagesIds, Logger logger, MetadataManager i18n) {
/* 125 */     if (targetECU == null) {
/*     */       return;
/*     */     }
/* 128 */     targetECU.forEach(ecu -> {
/*     */           
/*     */           try {
/*     */             CertificatesFieldsValidator.isTargetECULengthCorrentSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, ecu);
/* 132 */           } catch (CEBASException e) {
/*     */             LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/*     */             errorMessagesIds.add(i18n.getEnglishMessage("invalidInputForTargetECU", new String[] { ecu }));
/*     */           } 
/*     */         });
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
/*     */   private static void validateUniqueECUId(List<String> uniqueEcuID, List<String> errorMessagesIds, Logger logger, MetadataManager i18n) {
/* 150 */     if (uniqueEcuID == null) {
/*     */       return;
/*     */     }
/* 153 */     uniqueEcuID.forEach(ecu -> {
/*     */           
/*     */           try {
/*     */             CertificatesFieldsValidator.isUniqueECUIDLengthCorrectSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, ecu);
/* 157 */           } catch (CEBASException e) {
/*     */             LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/*     */             errorMessagesIds.add(i18n.getEnglishMessage("invalidInputForUniqueECUID", new String[] { ecu }));
/*     */           } 
/*     */         });
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
/*     */   private static void validateTargetVIN(List<String> targetVIN, List<String> errorMessagesIds, Logger logger, MetadataManager i18n) {
/* 175 */     if (targetVIN == null) {
/*     */       return;
/*     */     }
/* 178 */     targetVIN.forEach(vin -> {
/*     */           
/*     */           try {
/*     */             CertificatesFieldsValidator.isTargetVINLengthCorrectSingle(i18n, logger, ValidationFailureOutput::outputFailureWithThrow, vin);
/* 182 */           } catch (CEBASException e) {
/*     */             LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/*     */             errorMessagesIds.add(i18n.getEnglishMessage("invalidInputForTargetVIN", new String[] { vin }));
/*     */           } 
/*     */         });
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
/*     */   private static void validateRenewal(String renewal, List<String> errorMessagesIds, MetadataManager i18n) {
/* 198 */     if (StringUtils.isEmpty(renewal)) {
/* 199 */       errorMessagesIds.add(i18n.getEnglishMessage("invalidRenewal"));
/*     */       return;
/*     */     } 
/*     */     try {
/* 203 */       DurationParser.parse(renewal);
/* 204 */     } catch (DateTimeParseException e) {
/* 205 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 206 */       errorMessagesIds.add(i18n.getEnglishMessage("invalidRenewal"));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\PermissionsValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */