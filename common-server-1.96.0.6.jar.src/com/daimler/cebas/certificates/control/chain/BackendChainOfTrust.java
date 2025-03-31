/*     */ package com.daimler.cebas.certificates.control.chain;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*     */ import com.daimler.cebas.certificates.control.repository.CertificateRepository;
/*     */ import com.daimler.cebas.certificates.control.validation.ValidationError;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.entity.RawData;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import org.apache.commons.lang3.StringUtils;
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
/*     */ @CEBASControl
/*     */ public class BackendChainOfTrust
/*     */   extends ChainOfTrust
/*     */ {
/*  34 */   private static final String CLASS_NAME = BackendChainOfTrust.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public BackendChainOfTrust(CertificateRepository repo, Session session, Logger logger, ApplicationEventPublisher publisher, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
/*  55 */     super(repo, session, logger, publisher, i18n, profileConfiguration);
/*     */   }
/*     */ 
/*     */   
/*     */   public void check(List<Certificate> rootCertificate, CertificatePrivateKeyHolder<Certificate> holder, List<ValidationError> errors, boolean onlyFromPKI, boolean add) {
/*  60 */     String METHOD_NAME = "check";
/*  61 */     this.logger.entering(CLASS_NAME, "check");
/*     */     
/*  63 */     boolean foundRoot = false;
/*  64 */     Certificate certificate = holder.getCertificate();
/*  65 */     for (Certificate root : rootCertificate) {
/*  66 */       if (root.getSubjectKeyIdentifier().equals(certificate.getAuthorityKeyIdentifier())) {
/*  67 */         foundRoot = true;
/*  68 */         RawData rootCertificateData = root.getCertificateData();
/*  69 */         RawData certificateData = certificate.getCertificateData();
/*  70 */         if (this.profileConfiguration.isImportWithoutSignatureCheck() || 
/*  71 */           verifySignature(rootCertificateData, certificateData)) {
/*  72 */           if (add)
/*  73 */             addBackendCertificate(certificate, errors, root); 
/*     */           break;
/*     */         } 
/*  76 */         errorSignatureVerification(certificate, errors);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  81 */     if (!foundRoot) {
/*  82 */       errorDidNotfindRootIssuerBasedOnAuthorityKey(certificate, errors);
/*     */     }
/*  84 */     this.logger.exiting(CLASS_NAME, "check");
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
/*     */   private void addBackendCertificate(Certificate certificate, List<ValidationError> errors, Certificate root) {
/*  98 */     if (this.repo.checkUniqueSignatureAndSPK(certificate.getUser(), certificate.getSignature(), certificate.getSubjectPublicKey())) {
/*  99 */       certificate = this.profileConfiguration.getPkiKnownHandler().updateBackendPkiKnown(certificate);
/* 100 */       addChildToParent(root, certificate);
/*     */     }
/* 102 */     else if (!this.profileConfiguration.getPkiKnownHandler().updateBackendPkiKnownOnIdentical(certificate)) {
/* 103 */       errorCertificateAlreadyExist(certificate, errors);
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
/*     */   private void errorCertificateAlreadyExist(Certificate certificate, List<ValidationError> errors) {
/* 117 */     if (StringUtils.isEmpty(certificate.getZkNo())) {
/* 118 */       logCertificateAlreadyExist(certificate);
/* 119 */       errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n
/* 120 */             .getMessage("certAlreadyExistsWithSubjectKey", new String[] {
/* 121 */                 certificate.getSubjectKeyIdentifier()
/*     */               
/* 123 */               }), "certAlreadyExistsWithSubjectKey", new String[] { certificate.getSubjectKeyIdentifier() }));
/*     */     } else {
/* 125 */       this.logger.log(Level.INFO, "000016", this.i18n
/* 126 */           .getEnglishMessage("certAlreadyExistsWithSubjectKeyAndZkNo", new String[] {
/*     */               
/* 128 */               certificate.getSubjectKeyIdentifier(), certificate.getZkNo()
/*     */             }), CLASS_NAME);
/* 130 */       errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n
/* 131 */             .getMessage("certAlreadyExistsWithSubjectKeyAndZkNo", new String[] {
/* 132 */                 certificate.getSubjectKeyIdentifier(), certificate.getZkNo()
/*     */               
/* 134 */               }), "certAlreadyExistsWithSubjectKeyAndZkNo", new String[] { certificate.getSubjectKeyIdentifier(), certificate.getZkNo() }));
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
/*     */ 
/*     */   
/*     */   private void errorDidNotfindRootIssuerBasedOnAuthorityKey(Certificate certificate, List<ValidationError> errors) {
/* 151 */     ValidationError error = new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("rootNotFoundWhichCouldHaveIssuedBackend", new String[] { certificate.getSubjectKeyIdentifier() }), "rootNotFoundWhichCouldHaveIssuedBackend", new String[] { certificate.getSubjectKeyIdentifier() });
/* 152 */     errors.add(error);
/* 153 */     logDidNotFindIssuer("000106", certificate, CertificateType.ROOT_CA_CERTIFICATE);
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
/* 165 */     this.logger.logWithTranslation(Level.WARNING, "000027", "sigVerificationFailedBackendSubjKey", new String[] { certificate
/*     */           
/* 167 */           .getSubjectKeyIdentifier() }, CLASS_NAME);
/* 168 */     errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n
/* 169 */           .getMessage("sigVerificationFailedBackendSubjKey", new String[] {
/* 170 */               certificate.getSubjectKeyIdentifier()
/*     */             
/* 172 */             }), "sigVerificationFailedBackendSubjKey", new String[] { certificate.getSubjectKeyIdentifier() }));
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\chain\BackendChainOfTrust.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */