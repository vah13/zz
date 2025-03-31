/*     */ package com.daimler.cebas.certificates.control.vo;
/*     */ 
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.common.entity.Versioned;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeleteCertificatesInfo
/*     */   extends AbstractDeleteCertificates
/*     */ {
/*     */   private String targetSubjectKeyIdentifier;
/*     */   private String zkNo;
/*     */   private String ecuPackageTs;
/*     */   
/*     */   public DeleteCertificatesInfo(String certificateId, boolean isCertificate, CertificateType certificateType, String serialNo, String subjectKeyIdentifier, String authKeyIdentifier) {
/*  41 */     super(certificateId, isCertificate, certificateType, serialNo, subjectKeyIdentifier, authKeyIdentifier);
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
/*     */   public DeleteCertificatesInfo(String certificateId, boolean isCertificate, CertificateType certificateType, String serialNo, String subjectKeyIdentifier, String authKeyIdentifier, String targetSubjectKeyIdentifier) {
/*  67 */     super(certificateId, isCertificate, certificateType, serialNo, subjectKeyIdentifier, authKeyIdentifier);
/*     */     
/*  69 */     this.targetSubjectKeyIdentifier = targetSubjectKeyIdentifier;
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
/*     */   public DeleteCertificatesInfo(String certificateId, boolean isCertificate, CertificateType certificateType, String serialNo, String subjectKeyIdentifier, String authKeyIdentifier, String targetSubjectKeyIdentifier, String zkNo, String ecuPackageTs) {
/*  94 */     super(certificateId, isCertificate, certificateType, serialNo, subjectKeyIdentifier, authKeyIdentifier);
/*     */     
/*  96 */     this.targetSubjectKeyIdentifier = targetSubjectKeyIdentifier;
/*  97 */     this.zkNo = zkNo;
/*  98 */     this.ecuPackageTs = ecuPackageTs;
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
/*     */   public DeleteCertificatesInfo(String certificateId, boolean isCertificate, CertificateType certificateType, String authKeyIdentifier, String publicKey) {
/* 118 */     super(certificateId, isCertificate, certificateType, authKeyIdentifier, publicKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTargetSubjectKeyIdentifier() {
/* 129 */     return this.targetSubjectKeyIdentifier;
/*     */   }
/*     */   
/*     */   public String getZkNo() {
/* 133 */     return this.zkNo;
/*     */   }
/*     */   
/*     */   public String getEcuPackageTs() {
/* 137 */     return this.ecuPackageTs;
/*     */   }
/*     */ 
/*     */   
/*     */   public Versioned toVersion(int version) {
/* 142 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\DeleteCertificatesInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */