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
/*     */ public class RootLinkChainOfTrust
/*     */   extends ChainOfTrust
/*     */ {
/*  35 */   private static final String CLASS_NAME = RootLinkChainOfTrust.class
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
/*     */   public RootLinkChainOfTrust(CertificateRepository repo, Session session, Logger logger, ApplicationEventPublisher publisher, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
/*  59 */     super(repo, session, logger, publisher, i18n, profileConfiguration);
/*     */   }
/*     */ 
/*     */   
/*     */   public void check(List<Certificate> rootCertificates, CertificatePrivateKeyHolder<Certificate> holder, List<ValidationError> errors, boolean onlyFromPKI, boolean add) {
/*  64 */     String METHOD_NAME = "check";
/*  65 */     this.logger.entering(CLASS_NAME, "check");
/*     */     
/*  67 */     Certificate certificate = holder.getCertificate();
/*  68 */     Optional<Certificate> rootCertificateValidFromStore = getRootCertificateValidFromStore(rootCertificates, certificate);
/*  69 */     if (rootCertificateValidFromStore.isPresent()) {
/*  70 */       Certificate rootLinked = rootCertificateValidFromStore.get();
/*  71 */       if (rootLinked.getSubject().equals(certificate.getIssuer())) {
/*  72 */         if (this.profileConfiguration.isImportWithoutSignatureCheck() || 
/*  73 */           verifySignature(rootLinked.getCertificateData(), certificate.getCertificateData())) {
/*  74 */           if (add) {
/*  75 */             addRootLinkCertificate(rootCertificates, certificate, errors);
/*     */           }
/*     */         } else {
/*  78 */           errorSignatureVerification(certificate, errors);
/*     */         } 
/*     */       } else {
/*  81 */         errorDidNotFindRootIssuer(certificate, errors);
/*     */       } 
/*     */     } else {
/*  84 */       errorDidNotFindRootIssuer(certificate, errors);
/*     */     } 
/*     */     
/*  87 */     this.logger.exiting(CLASS_NAME, "check");
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
/*     */   private void addRootLinkCertificate(List<Certificate> rootCertificate, Certificate certificate, List<ValidationError> errors) {
/* 105 */     Optional<Certificate> foundAsociatedRoot = rootCertificate.stream().filter(root -> root.getSubjectPublicKey().trim().equals(certificate.getSubjectPublicKey().trim())).findFirst();
/* 106 */     if (foundAsociatedRoot.isPresent()) {
/* 107 */       Certificate rootAsociated = foundAsociatedRoot.get();
/* 108 */       if (this.profileConfiguration.isUnique(certificate, this.logger)) {
/* 109 */         checkCertificateReplacement(rootAsociated, certificate);
/* 110 */         addChildToParent(rootAsociated, certificate);
/*     */       } else {
/* 112 */         errorCertificateAlreadyExist(certificate, errors);
/*     */       } 
/*     */     } else {
/* 115 */       errorDidNotFindAssociatedRoot(certificate, errors);
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
/*     */   private void checkCertificateReplacement(Certificate rootAsociated, Certificate certificate) {
/* 130 */     if (this.profileConfiguration
/* 131 */       .shouldReplaceDuringImport(certificate.getType())) {
/* 132 */       replaceLinkCertificate(rootAsociated, certificate, CertificateType.ROOT_CA_LINK_CERTIFICATE);
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
/*     */   private void errorDidNotFindRootIssuer(Certificate certificate, List<ValidationError> errors) {
/* 147 */     logDidNotFindIssuer("000109X", certificate, CertificateType.ROOT_CA_CERTIFICATE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     ValidationError error = new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("rootNotFoundIssuedLinkSubjKey", new String[] { certificate.getSubjectKeyIdentifier() }), "rootNotFoundIssuedLinkSubjKey", new String[] { certificate.getSubjectKeyIdentifier() });
/* 156 */     errors.add(error);
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
/*     */   private void errorSignatureVerification(Certificate certificate, List<ValidationError> errors) {
/* 169 */     this.logger.logWithTranslation(Level.WARNING, "000110X", "sigVerificationFailedRootLinkSubjKey", new String[] { certificate
/*     */           
/* 171 */           .getSubjectKeyIdentifier() }, CLASS_NAME);
/*     */     
/* 173 */     errors.add(new ValidationError(certificate
/*     */           
/* 175 */           .getSubjectKeyIdentifier(), this.i18n
/* 176 */           .getMessage("sigVerificationFailedRootLinkSubjKey", new String[] {
/*     */ 
/*     */               
/* 179 */               certificate.getSubjectKeyIdentifier()
/*     */             
/*     */             }), "sigVerificationFailedRootLinkSubjKey", new String[] {
/* 182 */             certificate.getSubjectKeyIdentifier()
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
/*     */   private void errorDidNotFindAssociatedRoot(Certificate certificate, List<ValidationError> errors) {
/* 195 */     this.logger.log(Level.WARNING, "000025", this.i18n
/* 196 */         .getEnglishMessage("rootNotFoundAssociatedInputLinkSubjKey", new String[] {
/*     */             
/* 198 */             certificate.getSubjectKeyIdentifier()
/* 199 */           }), getClass().getSimpleName());
/* 200 */     errors.add(new ValidationError(certificate
/*     */           
/* 202 */           .getSubjectKeyIdentifier(), this.i18n
/* 203 */           .getMessage("rootNotFoundAssociatedInputLinkSubjKey", new String[] {
/*     */ 
/*     */               
/* 206 */               certificate.getSubjectKeyIdentifier()
/*     */             
/*     */             }), "rootNotFoundAssociatedInputLinkSubjKey", new String[] {
/* 209 */             certificate.getSubjectKeyIdentifier()
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
/* 222 */     logCertificateAlreadyExist(certificate);
/* 223 */     errors.add(new ValidationError(certificate
/*     */           
/* 225 */           .getSubjectKeyIdentifier(), this.i18n
/* 226 */           .getMessage("certAlreadyExistsWithSubjectKey", new String[] {
/*     */ 
/*     */               
/* 229 */               certificate.getSubjectKeyIdentifier()
/*     */             
/*     */             }), "certAlreadyExistsWithSubjectKey", new String[] {
/* 232 */             certificate.getSubjectKeyIdentifier()
/*     */           }));
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\chain\RootLinkChainOfTrust.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */