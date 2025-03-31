/*     */ package com.daimler.cebas.certificates.control.chain;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.validation.ValidationError;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASControl
/*     */ public final class ChainOfTrustManager
/*     */ {
/*  24 */   private static final String CLASS_NAME = ChainOfTrustManager.class
/*  25 */     .getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final RootChainOfTrust rootChainOfTrust;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final RootLinkChainOfTrust rootLinkChainOfTrust;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final BackendChainOfTrust backendChainOfTrust;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final BackendLinkChainOfTrust backendLinkChainOfTrust;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final UnderBackendChainOfTrust underBackendChainOfTrust;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final EnhancedRightsChainOfTrust enhancedRightsChainOfTrust;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public ChainOfTrustManager(RootChainOfTrust rootChainOfTrust, RootLinkChainOfTrust rootLinkChainOfTrust, BackendChainOfTrust backendChainOfTrust, BackendLinkChainOfTrust backendLinkChainOfTrust, UnderBackendChainOfTrust underBackendChainOfTrust, EnhancedRightsChainOfTrust enhancedRightsChainOfTrust, Logger logger) {
/*  88 */     this.rootChainOfTrust = rootChainOfTrust;
/*  89 */     this.rootLinkChainOfTrust = rootLinkChainOfTrust;
/*  90 */     this.backendChainOfTrust = backendChainOfTrust;
/*  91 */     this.backendLinkChainOfTrust = backendLinkChainOfTrust;
/*  92 */     this.underBackendChainOfTrust = underBackendChainOfTrust;
/*  93 */     this.enhancedRightsChainOfTrust = enhancedRightsChainOfTrust;
/*  94 */     this.logger = logger;
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
/*     */   public final List<ValidationError> checkChainOfTrust(List<Certificate> userStoreRootCertificate, CertificatePrivateKeyHolder holder) {
/* 107 */     String METHOD_NAME = "checkChainOfTrust";
/* 108 */     this.logger.entering(CLASS_NAME, "checkChainOfTrust");
/*     */     
/* 110 */     List<ValidationError> errors = new ArrayList<>();
/* 111 */     doChain(userStoreRootCertificate, holder, errors, false, false);
/*     */     
/* 113 */     this.logger.exiting(CLASS_NAME, "checkChainOfTrust");
/* 114 */     return errors;
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
/*     */   public final List<ValidationError> addCertificateToUserStore(List<Certificate> userStoreRootCertificate, CertificatePrivateKeyHolder holder, boolean onlyFromPKI) {
/* 130 */     String METHOD_NAME = "addCertificateToUserStore";
/* 131 */     this.logger.entering(CLASS_NAME, "addCertificateToUserStore");
/*     */     
/* 133 */     List<ValidationError> errors = new ArrayList<>();
/* 134 */     doChain(userStoreRootCertificate, holder, errors, onlyFromPKI, true);
/*     */     
/* 136 */     this.logger.exiting(CLASS_NAME, "addCertificateToUserStore");
/* 137 */     return errors;
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
/*     */   private void doChain(List<Certificate> userStoreRootCertificate, CertificatePrivateKeyHolder holder, List<ValidationError> errors, boolean onlyFromPKI, boolean add) {
/* 156 */     switch (holder.getType()) {
/*     */       case ROOT_CA_CERTIFICATE:
/* 158 */         this.rootChainOfTrust.check(userStoreRootCertificate, holder, errors, onlyFromPKI, add);
/*     */         break;
/*     */       
/*     */       case ROOT_CA_LINK_CERTIFICATE:
/* 162 */         this.rootLinkChainOfTrust.check(userStoreRootCertificate, holder, errors, onlyFromPKI, add);
/*     */         break;
/*     */       
/*     */       case BACKEND_CA_CERTIFICATE:
/* 166 */         this.backendChainOfTrust.check(userStoreRootCertificate, holder, errors, onlyFromPKI, add);
/*     */         break;
/*     */       
/*     */       case BACKEND_CA_LINK_CERTIFICATE:
/* 170 */         this.backendLinkChainOfTrust.check(userStoreRootCertificate, holder, errors, onlyFromPKI, add);
/*     */         break;
/*     */       
/*     */       case ECU_CERTIFICATE:
/*     */       case DIAGNOSTIC_AUTHENTICATION_CERTIFICATE:
/*     */       case VARIANT_CODING_DEVICE_CERTIFICATE:
/*     */       case VARIANT_CODE_USER_CERTIFICATE:
/*     */       case TIME_CERTIFICATE:
/* 178 */         this.underBackendChainOfTrust.check(userStoreRootCertificate, holder, errors, onlyFromPKI, add);
/*     */         break;
/*     */       
/*     */       case ENHANCED_RIGHTS_CERTIFICATE:
/* 182 */         this.enhancedRightsChainOfTrust.check(userStoreRootCertificate, holder, errors, onlyFromPKI, add);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\chain\ChainOfTrustManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */