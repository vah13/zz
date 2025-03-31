/*     */ package com.daimler.cebas.certificates.control.vo;
/*     */ 
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificatePKIState;
/*     */ import com.daimler.cebas.common.control.vo.CEBASResult;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CertificateSummary
/*     */   extends CEBASResult
/*     */ {
/*     */   private String id;
/*     */   private String subject;
/*     */   private String authorityKeyIdentifier;
/*     */   private String issuer;
/*     */   private String serialNo;
/*     */   private String certificateType;
/*     */   private String userRole;
/*     */   private String targetVIN;
/*     */   private Long validTo;
/*     */   private Long validFrom;
/*     */   private String linkCertTs;
/*     */   private CertificatePKIState pkiState;
/*     */   
/*     */   public CertificateSummary(Certificate certificate) {
/*  79 */     this.id = certificate.getEntityId();
/*  80 */     this.subject = certificate.getSubject();
/*  81 */     this.authorityKeyIdentifier = certificate.getAuthorityKeyIdentifier();
/*  82 */     this.issuer = certificate.getIssuer();
/*  83 */     this.serialNo = certificate.getSerialNo();
/*  84 */     this.certificateType = certificate.getPKIRole();
/*  85 */     this.userRole = certificate.getUserRole();
/*  86 */     this.targetVIN = certificate.getTargetVIN();
/*  87 */     this.validTo = Long.valueOf(certificate.getValidTo().getTime());
/*  88 */     this.validFrom = Long.valueOf(certificate.getValidFrom().getTime());
/*  89 */     this.linkCertTs = certificate.getLinkCertTs();
/*  90 */     this.pkiState = certificate.getPkiState();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  99 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubject() {
/* 108 */     return this.subject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAuthorityKeyIdentifier() {
/* 117 */     return this.authorityKeyIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIssuer() {
/* 126 */     return this.issuer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSerialNo() {
/* 135 */     return this.serialNo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCertificateType() {
/* 144 */     return this.certificateType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserRole() {
/* 153 */     return this.userRole;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTargetVIN() {
/* 162 */     return this.targetVIN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long getValidTo() {
/* 171 */     return this.validTo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long getValidFrom() {
/* 180 */     return this.validFrom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLinkCertTs() {
/* 189 */     return this.linkCertTs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CertificatePKIState getPkiState() {
/* 198 */     return this.pkiState;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\CertificateSummary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */