/*     */ package com.daimler.cebas.certificates.integration.vo;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JsonIgnoreProperties(ignoreUnknown = true, value = {"certificateType"})
/*     */ public class PKICertificateRequest
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 7012187663518573217L;
/*     */   @JsonIgnore
/*     */   private String internalCSRId;
/*     */   private String enrollmentId;
/*     */   protected String csr;
/*     */   protected String caIdentifier;
/*     */   protected String certificateType;
/*     */   @JsonIgnore
/*     */   protected boolean notBasedOnPermission;
/*     */   
/*     */   public PKICertificateRequest() {}
/*     */   
/*     */   public PKICertificateRequest(String csr, String caIdentifier, String certificateType) {
/*  44 */     this.csr = csr;
/*  45 */     this.caIdentifier = caIdentifier;
/*  46 */     this.certificateType = certificateType;
/*     */   }
/*     */   
/*     */   public PKICertificateRequest(String enrollmentId, String csr, String caIdentifier, String certificateType) {
/*  50 */     this.enrollmentId = enrollmentId;
/*  51 */     this.csr = csr;
/*  52 */     this.caIdentifier = caIdentifier;
/*  53 */     this.certificateType = certificateType;
/*     */   }
/*     */   
/*     */   public String getEnrollmentId() {
/*  57 */     return this.enrollmentId;
/*     */   }
/*     */   
/*     */   public String getCsr() {
/*  61 */     return this.csr;
/*     */   }
/*     */   
/*     */   public String getCaIdentifier() {
/*  65 */     return this.caIdentifier;
/*     */   }
/*     */   
/*     */   public String getCertificateType() {
/*  69 */     return this.certificateType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  74 */     return "PKICertificateRequest{enrollmentId='" + this.enrollmentId + '\'' + ", csr='" + this.csr + '\'' + ", caIdentifier='" + this.caIdentifier + '\'' + ", certificateType='" + this.certificateType + '\'' + '}';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNotBasedOnPermission(boolean notBasedOnPermission) {
/*  84 */     this.notBasedOnPermission = notBasedOnPermission;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNotBasedOnPermission() {
/*  91 */     return this.notBasedOnPermission;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInternalCSRId(String internalCSRId) {
/*  99 */     this.internalCSRId = internalCSRId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInternalCSRId() {
/* 107 */     return this.internalCSRId;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\integration\vo\PKICertificateRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */