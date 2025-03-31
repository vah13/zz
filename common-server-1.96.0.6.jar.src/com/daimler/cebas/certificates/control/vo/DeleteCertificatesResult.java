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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeleteCertificatesResult
/*     */   extends AbstractDeleteCertificates
/*     */ {
/*     */   private boolean isSuccess;
/*     */   private String message;
/*     */   
/*     */   public DeleteCertificatesResult(String certificateId, boolean isCertificate, CertificateType certificateType, boolean isSuccess, String message, String serialNo, String subjectKeyIdentifier, String authKeyIdentifier) {
/*  48 */     super(certificateId, isCertificate, certificateType, serialNo, subjectKeyIdentifier, authKeyIdentifier);
/*     */     
/*  50 */     this.isSuccess = isSuccess;
/*  51 */     this.message = message;
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
/*     */   public DeleteCertificatesResult(String certificateId, boolean isCertificate, CertificateType certificateType, boolean isSuccess, String message, String authKeyIdentifier, String publicKey) {
/*  75 */     super(certificateId, isCertificate, certificateType, authKeyIdentifier, publicKey);
/*     */     
/*  77 */     this.isSuccess = isSuccess;
/*  78 */     this.message = message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSuccess() {
/*  87 */     return this.isSuccess;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/*  96 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Versioned toVersion(int version) {
/* 104 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\DeleteCertificatesResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */