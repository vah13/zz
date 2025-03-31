/*     */ package com.daimler.cebas.certificates.control.chain;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*     */ import com.daimler.cebas.certificates.control.repository.CertificateRepository;
/*     */ import com.daimler.cebas.certificates.control.validation.ValidationError;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.SecurityProviders;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.common.entity.AbstractEntity;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ 
/*     */ @CEBASControl
/*     */ public class RootChainOfTrust
/*     */   extends ChainOfTrust
/*     */ {
/*  41 */   private static final String CLASS_NAME = RootChainOfTrust.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   private static final Logger LOG = Logger.getLogger(CertificateParser.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public RootChainOfTrust(CertificateRepository repo, Session session, Logger logger, ApplicationEventPublisher publisher, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
/*  69 */     super(repo, session, logger, publisher, i18n, profileConfiguration);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void check(List<Certificate> userStoreRootCertificate, CertificatePrivateKeyHolder<Certificate> holder, List<ValidationError> errors, boolean onlyFromPKI, boolean add) {
/*  75 */     String METHOD_NAME = "check";
/*  76 */     this.logger.entering(CLASS_NAME, "check");
/*  77 */     Certificate certificate = holder.getCertificate();
/*  78 */     if (this.profileConfiguration.isImportWithoutSignatureCheck() && add) {
/*  79 */       addCertificate(userStoreRootCertificate, errors, certificate);
/*     */     } else {
/*     */       try {
/*  82 */         X509Certificate cert = certificate.getCertificateData().getCert();
/*  83 */         cert.verify(cert.getPublicKey(), SecurityProviders.SNED25519.name());
/*  84 */         if (add) {
/*  85 */           addCertificate(userStoreRootCertificate, errors, certificate);
/*     */         }
/*  87 */       } catch (NoSuchAlgorithmException|java.security.SignatureException|java.security.NoSuchProviderException|java.security.InvalidKeyException|java.security.cert.CertificateException e) {
/*     */         
/*  89 */         LOG.log(Level.FINEST, e.getMessage(), e);
/*  90 */         errorSignatureVerification(certificate, errors);
/*     */       } 
/*     */     } 
/*  93 */     this.logger.exiting(CLASS_NAME, "check");
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
/*     */   private void addCertificate(List<Certificate> userStoreRootCertificate, List<ValidationError> errors, Certificate certificate) {
/* 107 */     if (this.repo.checkUniqueSignatureAndSPK(certificate.getUser(), certificate.getSignature(), certificate.getSubjectPublicKey())) {
/* 108 */       userStoreRootCertificate.add(certificate);
/* 109 */       this.repo.create((AbstractEntity)certificate);
/*     */     } else {
/* 111 */       errorCertificateAlreadyExist(certificate, errors);
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
/*     */   private void errorSignatureVerification(Certificate certificate, List<ValidationError> errors) {
/* 124 */     this.logger.logWithTranslation(Level.WARNING, "000028", "sigVerificationFailedRootSubjKey", new String[] { certificate
/* 125 */           .getSubjectKeyIdentifier() }, CLASS_NAME);
/*     */     
/* 127 */     errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n
/* 128 */           .getMessage("sigVerificationFailedRootSubjKey", new String[] {
/* 129 */               certificate.getSubjectKeyIdentifier()
/*     */             
/* 131 */             }), "sigVerificationFailedRootSubjKey", new String[] { certificate.getSubjectKeyIdentifier() }));
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
/* 143 */     logCertificateAlreadyExist(certificate);
/* 144 */     errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n
/* 145 */           .getMessage("certAlreadyExistsWithSubjectKey", new String[] {
/* 146 */               certificate.getSubjectKeyIdentifier()
/*     */             
/* 148 */             }), "certAlreadyExistsWithSubjectKey", new String[] { certificate.getSubjectKeyIdentifier() }));
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\chain\RootChainOfTrust.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */