/*     */ package com.daimler.cebas.certificates.control.vo;
/*     */ 
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.common.entity.Versioned;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import com.fasterxml.jackson.annotation.JsonProperty;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDeleteCertificates
/*     */   implements Versioned
/*     */ {
/*     */   private String certificateId;
/*     */   private boolean isCertificate;
/*     */   private CertificateType certificateType;
/*     */   private String serialNo;
/*     */   private String subjectKeyIdentifier;
/*     */   private String authKeyIdentifier;
/*     */   protected String publicKey;
/*     */   
/*     */   public AbstractDeleteCertificates(String certificateId, boolean isCertificate, CertificateType certificateType, String serialNo, String subjectKeyIdentifier, String authKeyIdentifier) {
/*  69 */     this.certificateId = certificateId;
/*  70 */     this.isCertificate = isCertificate;
/*  71 */     this.certificateType = certificateType;
/*  72 */     this.serialNo = serialNo;
/*  73 */     this.subjectKeyIdentifier = subjectKeyIdentifier;
/*  74 */     this.authKeyIdentifier = authKeyIdentifier;
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
/*     */   public AbstractDeleteCertificates(String certificateId, boolean isCertificate, CertificateType certificateType, String authKeyIdentifier, String publicKey) {
/*  93 */     this.certificateId = certificateId;
/*  94 */     this.isCertificate = isCertificate;
/*  95 */     this.certificateType = certificateType;
/*  96 */     this.authKeyIdentifier = authKeyIdentifier;
/*  97 */     this.publicKey = publicKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCertificateId() {
/* 106 */     return this.certificateId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCertificate() {
/* 115 */     return this.isCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @JsonIgnore
/*     */   public CertificateType getCertificateType() {
/* 125 */     return this.certificateType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @JsonProperty("certificateType")
/*     */   public String getCertificateTypeForJson() {
/* 135 */     return this.certificateType.getText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSerialNo() {
/* 144 */     return this.serialNo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubjectKeyIdentifier() {
/* 153 */     return this.subjectKeyIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAuthKeyIdentifier() {
/* 162 */     return this.authKeyIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPublicKey() {
/* 171 */     return this.publicKey;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\AbstractDeleteCertificates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */