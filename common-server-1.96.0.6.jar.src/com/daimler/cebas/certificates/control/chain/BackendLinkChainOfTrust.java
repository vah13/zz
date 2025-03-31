/*     */ package com.daimler.cebas.certificates.control.chain;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*     */ import com.daimler.cebas.certificates.control.repository.CertificateRepository;
/*     */ import com.daimler.cebas.certificates.control.validation.ValidationError;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.ApplicationEventPublisher;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class BackendLinkChainOfTrust
/*     */   extends ChainOfTrust
/*     */ {
/*  35 */   private static final String CLASS_NAME = BackendLinkChainOfTrust.class
/*  36 */     .getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public BackendLinkChainOfTrust(CertificateRepository repo, Session session, Logger logger, ApplicationEventPublisher publisher, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
/*  59 */     super(repo, session, logger, publisher, i18n, profileConfiguration);
/*     */   }
/*     */ 
/*     */   
/*     */   public void check(List<Certificate> rootCertificates, CertificatePrivateKeyHolder<Certificate> holder, List<ValidationError> errors, boolean onlyFromPKI, boolean add) {
/*  64 */     String METHOD_NAME = "check";
/*  65 */     this.logger.entering(CLASS_NAME, "check");
/*     */     
/*  67 */     boolean foundBackend = false;
/*  68 */     Certificate certificate = holder.getCertificate();
/*  69 */     for (Certificate root : rootCertificates) {
/*  70 */       Optional<Certificate> backendCertificateValidFromStore = getBackendCertificateValidFromStore(root, certificate, onlyFromPKI);
/*  71 */       if (backendCertificateValidFromStore.isPresent()) {
/*  72 */         foundBackend = true;
/*  73 */         Certificate backend = backendCertificateValidFromStore.get();
/*  74 */         if (this.profileConfiguration.isImportWithoutSignatureCheck() || 
/*  75 */           verifySignature(backend.getCertificateData(), certificate.getCertificateData())) {
/*  76 */           if (add)
/*  77 */             addBackendLinkCertificate(certificate, errors, root); 
/*     */           break;
/*     */         } 
/*  80 */         errorSignatureVerification(certificate, errors);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/*  86 */     if (!foundBackend) {
/*  87 */       errorDidNotFoundBackendBasedOnAuthorityKey(certificate, errors);
/*     */     }
/*     */     
/*  90 */     this.logger.exiting(CLASS_NAME, "check");
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
/*     */   private void addBackendLinkCertificate(Certificate certificate, List<ValidationError> errors, Certificate root) {
/* 106 */     Optional<Certificate> foundAssociatedBacked = root.getChildren().stream().filter(backendCert -> backendCert.getSubjectPublicKey().trim().equals(certificate.getSubjectPublicKey().trim())).findFirst();
/* 107 */     if (foundAssociatedBacked.isPresent()) {
/* 108 */       Certificate associatedBackend = foundAssociatedBacked.get();
/* 109 */       if (this.repo.checkUniqueSignatureAndSPK(certificate.getUser(), certificate.getSignature(), certificate.getSubjectPublicKey())) {
/* 110 */         checkCertificateReplacement(associatedBackend, certificate);
/* 111 */         addChildToParent(associatedBackend, certificate);
/*     */       } else {
/* 113 */         errorCertificateAlreadyExist(certificate, errors);
/*     */       } 
/*     */     } else {
/* 116 */       errorDidNotFindAssociatedBackend(certificate, errors);
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
/*     */   
/*     */   private void checkCertificateReplacement(Certificate assosciatedBackend, Certificate certificate) {
/* 131 */     if (this.profileConfiguration
/* 132 */       .shouldReplaceDuringImport(certificate.getType())) {
/* 133 */       replaceLinkCertificate(assosciatedBackend, certificate, CertificateType.BACKEND_CA_LINK_CERTIFICATE);
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
/*     */   
/*     */   private void errorDidNotFindAssociatedBackend(Certificate certificate, List<ValidationError> errors) {
/* 148 */     this.logger.log(Level.WARNING, "000031", this.i18n
/* 149 */         .getEnglishMessage("backendNotFoundAssociatedInputLink", new String[] {
/*     */             
/* 151 */             certificate.getSubjectKeyIdentifier()
/* 152 */           }), getClass().getSimpleName());
/* 153 */     errors.add(new ValidationError(certificate
/*     */           
/* 155 */           .getSubjectKeyIdentifier(), this.i18n
/* 156 */           .getMessage("backendNotFoundAssociatedInputLink", new String[] {
/*     */ 
/*     */               
/* 159 */               certificate.getSubjectKeyIdentifier()
/*     */             
/*     */             }), "backendNotFoundAssociatedInputLink", new String[] {
/* 162 */             certificate.getSubjectKeyIdentifier()
/*     */           }));
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
/*     */   private void errorCertificateAlreadyExist(Certificate certificate, List<ValidationError> errors) {
/* 175 */     logCertificateAlreadyExist(certificate);
/* 176 */     errors.add(new ValidationError(certificate
/*     */           
/* 178 */           .getSubjectKeyIdentifier(), this.i18n
/* 179 */           .getMessage("certAlreadyExistsWithSubjectKey", new String[] {
/*     */ 
/*     */               
/* 182 */               certificate.getSubjectKeyIdentifier()
/*     */             
/*     */             }), "certAlreadyExistsWithSubjectKey", new String[] {
/* 185 */             certificate.getSubjectKeyIdentifier()
/*     */           }));
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
/*     */   private void errorSignatureVerification(Certificate certificate, List<ValidationError> errors) {
/* 198 */     this.logger.logWithTranslation(Level.WARNING, "000108X", "sigVerificationFailedInputLinkSubjKey", new String[] { certificate
/*     */           
/* 200 */           .getSubjectKeyIdentifier() }, CLASS_NAME);
/*     */     
/* 202 */     errors.add(new ValidationError(certificate
/*     */           
/* 204 */           .getSubjectKeyIdentifier(), this.i18n
/* 205 */           .getMessage("sigVerificationFailedInputLinkSubjKey", new String[] {
/*     */ 
/*     */               
/* 208 */               certificate.getSubjectKeyIdentifier()
/*     */             
/*     */             }), "sigVerificationFailedInputLinkSubjKey", new String[] {
/* 211 */             certificate.getSubjectKeyIdentifier()
/*     */           }));
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
/*     */   private void errorDidNotFoundBackendBasedOnAuthorityKey(Certificate certificate, List<ValidationError> errors) {
/* 230 */     ValidationError e = new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("backendNotFoundIssuedLinkSubjKey", new String[] { certificate.getSubjectKeyIdentifier() }), "backendNotFoundIssuedLinkSubjKey", new String[] { certificate.getSubjectKeyIdentifier() });
/* 231 */     logDidNotFindIssuer("000035", certificate, CertificateType.BACKEND_CA_CERTIFICATE);
/*     */     
/* 233 */     errors.add(e);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\chain\BackendLinkChainOfTrust.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */