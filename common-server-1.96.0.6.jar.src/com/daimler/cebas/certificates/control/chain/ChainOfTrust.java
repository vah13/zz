/*     */ package com.daimler.cebas.certificates.control.chain;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.chain.events.CertificatesDeleteEvent;
/*     */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*     */ import com.daimler.cebas.certificates.control.repository.CertificateRepository;
/*     */ import com.daimler.cebas.certificates.control.validation.CertificatesValidator;
/*     */ import com.daimler.cebas.certificates.control.validation.ValidationError;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.entity.RawData;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.entity.AbstractEntity;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.context.ApplicationEvent;
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
/*     */ public abstract class ChainOfTrust
/*     */   implements IChainOfTrust
/*     */ {
/*  34 */   private static final String CLASS_NAME = ChainOfTrust.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ApplicationEventPublisher publisher;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CertificateRepository repo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Session session;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MetadataManager i18n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CertificatesConfiguration profileConfiguration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChainOfTrust(CertificateRepository repo, Session session, Logger logger, ApplicationEventPublisher publisher, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
/*  86 */     this.repo = repo;
/*  87 */     this.session = session;
/*  88 */     this.logger = logger;
/*  89 */     this.publisher = publisher;
/*  90 */     this.i18n = i18n;
/*  91 */     this.profileConfiguration = profileConfiguration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Optional<Certificate> getParentOfLinkCertificate(List<Certificate> possibleParents, Certificate rootLink) {
/* 102 */     return possibleParents.stream()
/* 103 */       .filter(cert -> cert.getSubjectKeyIdentifier().equals(rootLink.getAuthorityKeyIdentifier()))
/*     */       
/* 105 */       .findFirst();
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
/*     */   protected boolean verifySignature(RawData parentCertificateData, RawData certificateData) {
/* 118 */     String METHOD_NAME = "verifySignature";
/* 119 */     this.logger.entering(CLASS_NAME, "verifySignature");
/*     */     
/* 121 */     boolean verify = CertificatesValidator.verifySignature(parentCertificateData, certificateData, this.logger);
/*     */     
/* 123 */     this.logger.exiting(CLASS_NAME, "verifySignature");
/* 124 */     return verify;
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
/*     */   protected Optional<Certificate> getBackendCertificateValidFromStore(Certificate rootCertificate, Certificate certificateToAdd, boolean onlyFromPKI) {
/* 139 */     String METHOD_NAME = "getBackendCertificateValidFromStore";
/* 140 */     this.logger.entering(CLASS_NAME, "getBackendCertificateValidFromStore");
/*     */     
/* 142 */     List<Certificate> backendCertificates = rootCertificate.getChildren();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     Optional<Certificate> backendCertificateOptional = backendCertificates.stream().filter(backend -> (backend.getSubjectKeyIdentifier().equals(certificateToAdd.getAuthorityKeyIdentifier()) && (!onlyFromPKI || this.profileConfiguration.getPkiKnownHandler().isPKIKnown(backend)))).findFirst();
/* 148 */     this.logger.exiting(CLASS_NAME, "getBackendCertificateValidFromStore");
/* 149 */     return backendCertificateOptional;
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
/*     */   protected Optional<Certificate> getRootCertificateValidFromStore(List<Certificate> rootCertificate, Certificate certificateToAdd) {
/* 163 */     String METHOD_NAME = "getRootCertificateValidFromStore";
/* 164 */     this.logger.entering(CLASS_NAME, "getRootCertificateValidFromStore");
/*     */ 
/*     */ 
/*     */     
/* 168 */     Optional<Certificate> rootCertificateOptional = rootCertificate.stream().filter(backend -> backend.getSubjectKeyIdentifier().equals(certificateToAdd.getAuthorityKeyIdentifier())).findFirst();
/*     */     
/* 170 */     this.logger.exiting(CLASS_NAME, "getRootCertificateValidFromStore");
/* 171 */     return rootCertificateOptional;
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
/*     */   protected void addChildToParent(Certificate parent, Certificate child) {
/* 183 */     String METHOD_NAME = "addChildToParent";
/* 184 */     this.logger.entering(CLASS_NAME, "addChildToParent");
/*     */     
/* 186 */     parent.getChildren().add(child);
/* 187 */     child.setParent(parent);
/* 188 */     this.repo.create((AbstractEntity)child);
/*     */     
/* 190 */     this.logger.exiting(CLASS_NAME, "addChildToParent");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void deleteCertificates(List<Certificate> certificates) {
/* 200 */     String METHOD_NAME = "deleteCertificates";
/* 201 */     this.logger.entering(CLASS_NAME, "deleteCertificates");
/*     */ 
/*     */     
/* 204 */     List<String> ids = (List<String>)certificates.stream().map(Certificate::getEntityId).collect(Collectors.toList());
/* 205 */     this.publisher.publishEvent((ApplicationEvent)new CertificatesDeleteEvent(this, ids));
/* 206 */     this.logger.exiting(CLASS_NAME, "deleteCertificates");
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
/*     */   protected void replaceLinkCertificate(Certificate parent, Certificate linkToBeImported, CertificateType certificateType) {
/* 226 */     List<Certificate> links = (List<Certificate>)parent.getChildren().parallelStream().filter(link -> (link.getType() == certificateType && link.getAuthorityKeyIdentifier().equals(linkToBeImported.getAuthorityKeyIdentifier()))).collect(Collectors.toList());
/*     */     
/* 228 */     List<Certificate> identicalWithDifferences = Uniqueness.findIdenticalWithDifferences(links, linkToBeImported, this.logger);
/* 229 */     deleteCertificates(identicalWithDifferences);
/* 230 */     logReplacedCertificate(identicalWithDifferences, linkToBeImported);
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
/*     */   protected void logReplacedCertificate(List<Certificate> certificates, Certificate newCertificate) {
/* 243 */     String METHOD_NAME = "logReplacedCertificate";
/* 244 */     this.logger.entering(CLASS_NAME, "logReplacedCertificate");
/* 245 */     if (certificates.size() > 1) {
/* 246 */       this.logger.log(Level.WARNING, "000052", this.i18n
/* 247 */           .getEnglishMessage("foundMultipleCertificatesToBeReplacedBySignatureAndPK", new String[] {
/*     */               
/* 249 */               newCertificate.getSignature(), newCertificate
/* 250 */               .getSubjectPublicKey()
/*     */             }), CLASS_NAME);
/*     */     }
/*     */     
/* 254 */     certificates.forEach(cert -> this.logger.log(Level.INFO, "000051", this.i18n.getEnglishMessage("certificateWithSignatureAndPKReplaced", new String[] { cert.getSignature(), cert.getSubjectPublicKey(), newCertificate.getSignature(), newCertificate.getSubjectPublicKey() }), CLASS_NAME));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 263 */     this.logger.exiting(CLASS_NAME, "logReplacedCertificate");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void logCertificateAlreadyExist(Certificate certificate) {
/* 273 */     this.logger.log(Level.INFO, "000016", this.i18n
/* 274 */         .getEnglishMessage("certAlreadyExistsWithSubjectKey", new String[] {
/*     */             
/* 276 */             certificate.getSubjectKeyIdentifier()
/*     */           }), CLASS_NAME);
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
/*     */   protected void logDidNotFindIssuer(String loggingConstant, Certificate certificate, CertificateType issuerType) {
/* 292 */     this.logger.log(Level.WARNING, loggingConstant, this.i18n
/* 293 */         .getEnglishMessage("issuerNotFound", new String[] {
/* 294 */             issuerType.getText(), certificate
/* 295 */             .getPKIRole(), certificate
/* 296 */             .getSubjectKeyIdentifier()
/*     */           }), CLASS_NAME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void logFoundMulipleCertificateWithDifferentSignatureButSamePublicKey() {
/* 306 */     this.logger.log(Level.WARNING, "000054", this.i18n
/* 307 */         .getEnglishMessage("foundMultipleCertificatesWithDiffSigButSamePublicKey"), CLASS_NAME);
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
/*     */   protected void addErrorFoundMultipleCeritficateWithDifferentSignatureButSamePublicKey(Certificate certificate, List<ValidationError> errors) {
/* 322 */     errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n
/* 323 */           .getMessage("multipleCertificatesDifSig"), "multipleCertificatesDifSig", null));
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\chain\ChainOfTrust.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */