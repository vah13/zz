/*     */ package com.daimler.cebas.certificates.control.config;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.vo.CertificateSummary;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface CertificatesConfiguration
/*     */   extends CertificatesDeleteConfiguration, CertificatesImportConfiguration, ChainOfTrustConfiguration
/*     */ {
/*     */   public static final String YYYY_MM_DD = "yyyy/MM/dd";
/*     */   
/*     */   Date getCSRValidTo(Logger paramLogger, MetadataManager paramMetadataManager);
/*     */   
/*     */   Optional<Certificate> getMatchingRolledBackCertificate(Certificate paramCertificate);
/*     */   
/*     */   List<CertificateType> availableCertificateTypesForCSRCreation();
/*     */   
/*     */   List<CertificateType> availableCertificateTypesForRollbackEnabling();
/*     */   
/*     */   boolean checkCSRExists();
/*     */   
/*     */   boolean isSubjectValid(Certificate paramCertificate);
/*     */   
/*     */   List<? extends CertificateSummary> convertToCertificateSummary(List<? extends Certificate> paramList);
/*     */   
/*     */   static String fileNameToHexString(byte[] fileNameProposal) {
/*  91 */     StringBuilder hexString = new StringBuilder();
/*  92 */     for (byte b : fileNameProposal) {
/*  93 */       String hex = Integer.toHexString(0xFF & b);
/*  94 */       if (hex.length() == 1) {
/*  95 */         hexString.append('0');
/*     */       }
/*  97 */       hexString.append(hex);
/*     */     } 
/*  99 */     return hexString.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isUserRoleValid(Certificate paramCertificate);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean isAutomaticMarkRollbackChildren() {
/* 117 */     return false;
/*     */   }
/*     */   
/*     */   boolean isUnique(Certificate paramCertificate, Logger paramLogger);
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\config\CertificatesConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */