/*     */ package com.daimler.cebas.certificates.control.chain;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.CertificateUtil;
/*     */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*     */ import com.daimler.cebas.certificates.control.repository.CertificateRepository;
/*     */ import com.daimler.cebas.certificates.control.validation.ValidationError;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateState;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.entity.RawData;
/*     */ import com.daimler.cebas.common.control.HexUtil;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import java.math.BigInteger;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bouncycastle.cert.AttributeCertificateHolder;
/*     */ import org.bouncycastle.cert.X509AttributeCertificateHolder;
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
/*     */ @CEBASControl
/*     */ public class EnhancedRightsChainOfTrust
/*     */   extends ChainOfTrust
/*     */ {
/*  44 */   private static final String CLASS_NAME = EnhancedRightsChainOfTrust.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public EnhancedRightsChainOfTrust(CertificateRepository repo, Session session, Logger logger, ApplicationEventPublisher publisher, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
/*  66 */     super(repo, session, logger, publisher, i18n, profileConfiguration);
/*     */   }
/*     */ 
/*     */   
/*     */   public void check(List<Certificate> userStoreRootCertificate, CertificatePrivateKeyHolder<Certificate> holder, List<ValidationError> errors, boolean onlyFromPKI, boolean add) {
/*  71 */     String METHOD_NAME = "check";
/*  72 */     this.logger.entering(CLASS_NAME, "check");
/*     */     
/*  74 */     boolean backendFound = false;
/*  75 */     Certificate certificate = holder.getCertificate();
/*  76 */     for (Certificate root : userStoreRootCertificate) {
/*  77 */       Optional<Certificate> backendCertificateValidFromStore = getBackendCertificateValidFromStore(root, certificate, onlyFromPKI);
/*  78 */       if (backendCertificateValidFromStore.isPresent()) {
/*  79 */         backendFound = true;
/*  80 */         Certificate backend = backendCertificateValidFromStore.get();
/*  81 */         RawData certificateData = certificate.getCertificateData();
/*  82 */         List<Certificate> children = this.repo.findByTypeAndParent(Certificate.class, certificate.getUser(), CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE, backend);
/*     */ 
/*     */         
/*  85 */         Optional<Certificate> diagOptional = children.stream().filter(cert -> matchingDiagCertificate(certificate, cert)).findFirst();
/*  86 */         if (!diagOptional.isPresent()) {
/*  87 */           errorDidNotFondDiagnosticCertificate(certificate, errors);
/*     */           return;
/*     */         } 
/*  90 */         if (this.repo.checkUniqueSignatureAndSPK(certificate.getUser(), certificate.getSignature(), certificate.getSubjectPublicKey())) {
/*  91 */           certificate.setParent(diagOptional.get());
/*  92 */           if (this.profileConfiguration.isImportWithoutSignatureCheck() || 
/*  93 */             verifySignature(backend.getCertificateData(), certificateData)) {
/*  94 */             if (add) {
/*  95 */               Certificate diagnosticCertificate = diagOptional.get();
/*  96 */               checkIfExistsCSRAndAddCertificate(holder, certificate, diagnosticCertificate);
/*     */             }  break;
/*     */           } 
/*  99 */           errorSignatureVerification(certificate, errors);
/*     */           break;
/*     */         } 
/* 102 */         errorCertificateAlreadyExist(certificate, errors);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 108 */     if (!backendFound) {
/* 109 */       errorDidNotFindBackendBasedOnAuthorityKey(certificate, errors);
/*     */     }
/*     */     
/* 112 */     this.logger.exiting(CLASS_NAME, "check");
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
/*     */   private void addCertificate(CertificatePrivateKeyHolder holder, Certificate certificate, Certificate diagnosticCertificate) {
/* 127 */     checkIdenticalWithDifferences(holder, certificate, diagnosticCertificate);
/* 128 */     addChildToParent(diagnosticCertificate, certificate);
/* 129 */     certificate.setBaseCertificateIDForEnh();
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
/*     */   private void checkIfExistsCSRAndAddCertificate(CertificatePrivateKeyHolder holder, Certificate certificate, Certificate diagnosticCertificate) {
/* 144 */     Optional<Certificate> csrOptional = findCSR(certificate, diagnosticCertificate);
/* 145 */     if (csrOptional.isPresent()) {
/* 146 */       Certificate csrCertificate = csrOptional.get();
/* 147 */       checkIdenticalWithDifferences(holder, certificate, diagnosticCertificate);
/* 148 */       RawData certificateData = certificate.getCertificateData();
/* 149 */       csrCertificate.updateCSR(certificateData.getAttributesCertificateHolder(), certificateData.getOriginalBytes());
/* 150 */       csrCertificate.setBaseCertificateIDForEnh();
/*     */     } else {
/* 152 */       addCertificate(holder, certificate, diagnosticCertificate);
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
/*     */   private void checkIdenticalWithDifferences(CertificatePrivateKeyHolder holder, Certificate certificate, Certificate diagnosticCertificate) {
/* 168 */     if (this.profileConfiguration.shouldReplaceDuringImport(certificate.getType())) {
/*     */       
/* 170 */       List<Certificate> identicalWithDifferences = Uniqueness.findIdenticalWithDifferences(diagnosticCertificate.getChildren(), certificate, this.logger);
/* 171 */       holder.addPossibleReplaceableCertificate(identicalWithDifferences);
/* 172 */       deleteCertificates(identicalWithDifferences);
/* 173 */       logReplacedCertificate(identicalWithDifferences, certificate);
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
/*     */   private void errorCertificateAlreadyExist(Certificate certificate, List<ValidationError> errors) {
/* 186 */     logCertificateAlreadyExist(certificate);
/* 187 */     CertificateType certificateType = CertificateType.getTypeForLogging(certificate);
/* 188 */     errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n
/* 189 */           .getMessage("enhRightsAlreadyExistsWithSignature", new String[] {
/* 190 */               this.i18n.getMessage(certificateType.getLanguageProperty()), certificate
/* 191 */               .getSignature()
/*     */             
/* 193 */             }), "enhRightsAlreadyExistsWithSignature", new String[] { this.i18n.getEnglishMessage(certificateType.getLanguageProperty()), certificate.getSignature() }));
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
/*     */   private void errorDidNotFondDiagnosticCertificate(Certificate certificate, List<ValidationError> errors) {
/* 205 */     CertificateType certificateType = CertificateType.getTypeForLogging(certificate);
/* 206 */     String className = getClass().getSimpleName();
/* 207 */     this.logger.log(Level.WARNING, "000024", this.i18n
/* 208 */         .getEnglishMessage("diagNotFoundForInputEnhRights", new String[] {
/* 209 */             this.i18n.getEnglishMessage(certificateType.getLanguageProperty()), certificate.getSignature() }), className);
/* 210 */     errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n
/* 211 */           .getMessage("diagNotFoundForInputEnhRights", new String[] {
/* 212 */               this.i18n.getMessage(certificateType.getLanguageProperty()), certificate
/* 213 */               .getSignature()
/*     */             
/* 215 */             }), "diagNotFoundForInputEnhRights", new String[] { this.i18n.getEnglishMessage(certificateType.getLanguageProperty()), certificate.getSignature() }));
/* 216 */     logHolderNotFound(certificate);
/*     */   }
/*     */   
/*     */   private void logHolderNotFound(Certificate certificate) {
/* 220 */     X509AttributeCertificateHolder attributesCertificateHolder = certificate.getCertificateData().getAttributesCertificateHolder();
/* 221 */     String issuerHolder = attributesCertificateHolder.getHolder().getIssuer()[0].toString();
/* 222 */     BigInteger serialNoHolder = attributesCertificateHolder.getHolder().getSerialNumber();
/* 223 */     String serialNoHolderHex = HexUtil.bytesToHex(serialNoHolder.toByteArray());
/* 224 */     String message = MessageFormat.format("Cannot import enhanced rights due to unknown holder {0} and issuer {1}", new Object[] { serialNoHolderHex, issuerHolder });
/* 225 */     this.logger.log(Level.WARNING, "000024", message, getClass().getSimpleName());
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
/* 237 */     this.logger.logWithTranslation(Level.WARNING, "000106X", "sigVerificationFailedForEnhRightsWithSigKey", new String[] { certificate
/* 238 */           .getSignature() }, CLASS_NAME);
/*     */     
/* 240 */     errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n
/* 241 */           .getMessage("sigVerificationFailedForEnhRightsWithSigKey", new String[] {
/* 242 */               certificate.getSignature()
/*     */             
/* 244 */             }), "sigVerificationFailedForEnhRightsWithSigKey", new String[] { certificate.getSignature() }));
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
/*     */   private void errorDidNotFindBackendBasedOnAuthorityKey(Certificate certificate, List<ValidationError> errors) {
/* 256 */     CertificateType certificateType = CertificateType.getTypeForLogging(certificate);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 262 */     ValidationError error = new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("backendNotFoundIssuedDiagCertForEnhRights", new String[] { this.i18n.getMessage(certificateType.getLanguageProperty()), certificate.getSignature() }), "backendNotFoundIssuedDiagCertForEnhRights", new String[] { this.i18n.getEnglishMessage(certificateType.getLanguageProperty()), certificate.getSignature() });
/* 263 */     errors.add(error);
/* 264 */     this.logger.log(Level.WARNING, "000107X", this.i18n
/* 265 */         .getEnglishMessage("backendNotFoundIssuedDiagCertForEnhRights", new String[] {
/* 266 */             this.i18n.getEnglishMessage(certificateType.getLanguageProperty()), certificate
/* 267 */             .getSignature() }), getClass().getSimpleName());
/* 268 */     logHolderNotFound(certificate);
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
/*     */   private boolean matchingDiagCertificate(Certificate ehhRightsCertificates, Certificate cert) {
/* 281 */     boolean match = false;
/* 282 */     if (cert.getState().equals(CertificateState.ISSUED)) {
/* 283 */       X509Certificate diagCertificate = cert.getCertificateData().getCert();
/* 284 */       if (diagCertificate == null) {
/* 285 */         this.logger.log(Level.WARNING, "000024", "Diagnostic[" + cert.getSerialNo() + "] type does not contain X509 data. It is skipped in lookup.", 
/* 286 */             getClass().getSimpleName());
/* 287 */         return false;
/*     */       } 
/* 289 */       BigInteger serialNumberDiagnostic = diagCertificate.getSerialNumber();
/* 290 */       X509AttributeCertificateHolder x509AttributesCertificateHolder = ehhRightsCertificates.getCertificateData().getAttributesCertificateHolder();
/* 291 */       AttributeCertificateHolder holder = x509AttributesCertificateHolder.getHolder();
/* 292 */       BigInteger serialNumberDiagEhh = holder.getSerialNumber();
/* 293 */       String issuerDiag = diagCertificate.getIssuerX500Principal().getName();
/* 294 */       String issuerEhh = holder.getIssuer()[0].toString();
/* 295 */       String sigAlgNameDiag = diagCertificate.getSigAlgName();
/* 296 */       String sigAlgEhh = x509AttributesCertificateHolder.getSignatureAlgorithm().getAlgorithm().toString();
/*     */ 
/*     */       
/* 299 */       match = (serialNumberDiagnostic.equals(serialNumberDiagEhh) && StringUtils.equals(issuerDiag, issuerEhh) && StringUtils.equals(sigAlgNameDiag, sigAlgEhh));
/*     */     } 
/* 301 */     return match;
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
/*     */   public Optional<Certificate> findCSR(Certificate certificateToAdd, Certificate diagnosticCertificate) {
/* 313 */     String METHOD_NAME = "findCSR";
/* 314 */     this.logger.entering(CLASS_NAME, "findCSR");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 324 */     Optional<Certificate> found = diagnosticCertificate.getChildren().stream().filter(certificate -> (certificate.getState() == CertificateState.SIGNING_REQUEST && !certificate.getCertificateData().isCertificate() && certificate.getAuthorityKeyIdentifier().equals(certificateToAdd.getAuthorityKeyIdentifier()) && certificate.getIssuerSerialNumber().equals(certificateToAdd.getIssuerSerialNumber()) && certificate.getIssuer().equals(certificateToAdd.getIssuer()) && certificate.getTargetSubjectKeyIdentifier().equals(certificateToAdd.getTargetSubjectKeyIdentifier()) && CertificateUtil.compareCommaSeparatedLists(certificate.getTargetECU(), certificateToAdd.getTargetECU()) && CertificateUtil.compareCommaSeparatedLists(certificate.getTargetVIN(), certificateToAdd.getTargetVIN()))).findFirst();
/* 325 */     this.logger.exiting(CLASS_NAME, "findCSR");
/* 326 */     return found;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\chain\EnhancedRightsChainOfTrust.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */