/*     */ package com.daimler.cebas.certificates.entity;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*     */ import com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum CertificateType
/*     */ {
/*  16 */   NO_TYPE("certType.NoType", "No Type", 100),
/*  17 */   BACKEND_CA_CERTIFICATE("certType.BackendCaCertificate", "Backend CA Certificate", 2),
/*  18 */   BACKEND_CA_LINK_CERTIFICATE("certType.BackendCaLinkCertificate", "Backend CA Link Certificate", 3),
/*  19 */   ROOT_CA_CERTIFICATE("certType.RootCaCertificate", "Root CA Certificate", 1),
/*  20 */   ROOT_CA_LINK_CERTIFICATE("certType.RootCaLinkCertificate", "Root CA Link Certificate", 2),
/*  21 */   ECU_CERTIFICATE("certType.EcuCertificate", "ECU Certificate", 3),
/*  22 */   DIAGNOSTIC_AUTHENTICATION_CERTIFICATE("certType.DiagnosticAuthenticationCertificate", "Diagnostic Authentication Certificate", 3),
/*     */   
/*  24 */   ENHANCED_RIGHTS_CERTIFICATE("certType.EnhancedRightsCertificate", "Enhanced Rights Certificate", 4),
/*  25 */   TIME_CERTIFICATE("certType.TimeCertificate", "Time Certificate", 3),
/*  26 */   VARIANT_CODE_USER_CERTIFICATE("certType.VariantCodeUserCertificate", "Variant Coding User Certificate", 3),
/*  27 */   VARIANT_CODING_DEVICE_CERTIFICATE("certType.VariantCodingDeviceCertificate", "Variant Coding Device Certificate", 3),
/*     */   
/*  29 */   SEC_OC_IS("certType.SecOcIs", "SecOcIs Certificate", 4),
/*  30 */   VIRTUAL_FOLDER("certType.virtualFolder", "Virtual folder", 101);
/*     */   
/*     */   private String text;
/*     */   private String languageProperty;
/*     */   private int level;
/*     */   
/*     */   CertificateType(String languageProperty, String text, int level) {
/*  37 */     this.text = text;
/*  38 */     this.languageProperty = languageProperty;
/*  39 */     this.level = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CertificateType valueFromString(String type) {
/*  49 */     CertificateType typeEnum = NO_TYPE;
/*  50 */     for (CertificateType certificateType : values()) {
/*  51 */       if (type.equalsIgnoreCase(certificateType.getText()) || type.equalsIgnoreCase(certificateType.name())) {
/*  52 */         typeEnum = certificateType;
/*     */       }
/*     */     } 
/*  55 */     if (typeEnum == NO_TYPE) {
/*  56 */       throw new CEBASCertificateException("Invalid certificate type");
/*     */     }
/*  58 */     return typeEnum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<CertificateType> typesWhichContains(String value) {
/*  68 */     List<CertificateType> types = new ArrayList<>();
/*  69 */     for (CertificateType certificateType : values()) {
/*  70 */       if (!StringUtils.isEmpty(value) && certificateType.getText().toUpperCase().contains(value.toUpperCase())) {
/*  71 */         types.add(certificateType);
/*     */       }
/*     */     } 
/*  74 */     return types;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getText() {
/*  83 */     return this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLanguageProperty() {
/*  92 */     return this.languageProperty;
/*     */   }
/*     */   
/*     */   public static CertificateType getTypeForLogging(Certificate certificate) {
/*     */     CertificateType certificateType;
/*  97 */     if (certificate.isSecOCISCert()) {
/*  98 */       certificateType = SEC_OC_IS;
/*     */     } else {
/* 100 */       certificateType = certificate.getType();
/* 101 */       if (certificateType == null) {
/* 102 */         certificateType = NO_TYPE;
/*     */       }
/*     */     } 
/* 105 */     return certificateType;
/*     */   }
/*     */   
/*     */   public static CertificateType getTypeForLogging(DeleteCertificatesInfo entry) {
/*     */     CertificateType certificateType;
/* 110 */     if (entry.getCertificateType().equals(ENHANCED_RIGHTS_CERTIFICATE) && 
/* 111 */       !StringUtils.isEmpty(entry.getTargetSubjectKeyIdentifier())) {
/* 112 */       certificateType = SEC_OC_IS;
/*     */     } else {
/* 114 */       certificateType = entry.getCertificateType();
/* 115 */       if (certificateType == null) {
/* 116 */         certificateType = NO_TYPE;
/*     */       }
/*     */     } 
/* 119 */     return certificateType;
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 123 */     return this.level;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\entity\CertificateType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */