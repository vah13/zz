/*     */ package com.daimler.cebas.certificates.control.factories;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.PKCS12Manager;
/*     */ import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
/*     */ import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
/*     */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.FactoryMethodPattern;
/*     */ import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Base64;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.bouncycastle.cert.X509AttributeCertificateHolder;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @FactoryMethodPattern
/*     */ public abstract class AbstractCertificateFactory
/*     */ {
/*     */   private static final String CREATE_CERTIFICATE_METHOD_NAME = "createCertificate";
/*  59 */   private static final Logger LOG = Logger.getLogger(AbstractCertificateFactory.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   private static final String CLASS_NAME = AbstractCertificateFactory.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CertificateHolderFactory certificateHolderFactory;
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
/*     */   private MetadataManager i18n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String CERTIFICATE_TYPE = "X.509";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String EMPTY = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractImportResultFactory importResultFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Session session;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public AbstractCertificateFactory(Logger logger, MetadataManager i18n, AbstractImportResultFactory importResultFactory, CertificateHolderFactory certificateHolderFactory, Session session) {
/* 119 */     this.logger = logger;
/* 120 */     this.i18n = i18n;
/* 121 */     this.importResultFactory = importResultFactory;
/* 122 */     this.certificateHolderFactory = certificateHolderFactory;
/* 123 */     this.session = session;
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
/*     */   public Optional<Certificate> createCertificate(InputStream stream, User user) {
/* 137 */     String METHOD_NAME = "createCertificate";
/* 138 */     this.logger.entering(CLASS_NAME, "createCertificate");
/* 139 */     X509Certificate certificate = getCertificate(stream);
/* 140 */     this.logger.exiting(CLASS_NAME, "createCertificate");
/* 141 */     return (certificate == null) ? Optional.<Certificate>empty() : Optional.<Certificate>of(getCertificateInstance(certificate, user));
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
/*     */   public Optional<Certificate> createCertificate(InputStream stream, byte[] originalBytes, User user) {
/* 156 */     String METHOD_NAME = "createCertificate";
/* 157 */     this.logger.entering(CLASS_NAME, "createCertificate");
/* 158 */     if (stream == null && (originalBytes == null || originalBytes.length == 0)) {
/* 159 */       this.logger.log(Level.INFO, "000591", this.i18n
/* 160 */           .getMessage("wrongInputCreatingCertificate"), CLASS_NAME);
/* 161 */       throw new CEBASCertificateException(this.i18n.getMessage("wrongInputCreatingCertificate"));
/*     */     } 
/* 163 */     String errorMessage = null;
/* 164 */     X509Certificate certificate = null;
/* 165 */     boolean checkAttributeCertificate = false;
/*     */     try {
/* 167 */       certificate = getCertificate(stream);
/* 168 */     } catch (IllegalArgumentException e) {
/* 169 */       LOG.log(Level.FINE, "Exception when building X509: " + e.getMessage());
/* 170 */       checkAttributeCertificate = false;
/* 171 */       if (CertificateParser.isInvalidPublicKey(stream)) {
/* 172 */         errorMessage = this.i18n.getMessage("thePublicKeyIsInvalid", new String[] { e.getMessage() });
/* 173 */         this.logger.logWithException("000417X", this.i18n
/* 174 */             .getEnglishMessage("thePublicKeyIsInvalid", new String[] { e.getMessage() }), new CEBASException(e
/* 175 */               .getMessage()));
/*     */       } else {
/* 177 */         errorMessage = e.getMessage();
/*     */       } 
/* 179 */     } catch (CEBASException e) {
/* 180 */       checkAttributeCertificate = true;
/* 181 */       LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/* 182 */       errorMessage = e.getMessage();
/*     */     } 
/* 184 */     if (checkAttributeCertificate && originalBytes != null && originalBytes.length != 0) {
/* 185 */       return createCertificateAsAttribute(originalBytes, user);
/*     */     }
/* 187 */     if (certificate == null) {
/* 188 */       throw new CEBASCertificateException(errorMessage);
/*     */     }
/* 190 */     this.logger.exiting(CLASS_NAME, "createCertificate");
/* 191 */     return Optional.of(getCertificateInstance(certificate, originalBytes, user));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Optional<CertificatePrivateKeyHolder>> createCertificatePrivateKeyHolder(InputStream stream, byte[] byteArrayForPossibleCerAttributeHolder, User user, boolean isPKCS12, String password, List<ImportResult> importResults, String fileName) {
/* 217 */     String METHOD_NAME = "createCertificatePrivateKeyHolder";
/* 218 */     this.logger.entering(CLASS_NAME, "createCertificatePrivateKeyHolder");
/* 219 */     if (isPKCS12) {
/* 220 */       return createCertificateHoldersFromPKCS(stream, user, password, importResults, fileName);
/*     */     }
/*     */     try {
/* 223 */       Optional<Certificate> createCertificate = createCertificate(stream, byteArrayForPossibleCerAttributeHolder, user);
/* 224 */       if (createCertificate.isPresent()) {
/* 225 */         return Collections.singletonList(
/* 226 */             Optional.of(new CertificatePrivateKeyHolder(fileName, createCertificate.get(), Optional.empty())));
/*     */       }
/* 228 */     } catch (Exception e) {
/* 229 */       LOG.log(Level.FINEST, e.getMessage(), e);
/*     */       
/* 231 */       ImportResult invalidImportResult = new ImportResult(fileName, "", "", this.i18n.getMessage("cannotReadFile") + ": " + fileName + ", " + e.getMessage(), false);
/*     */       
/* 233 */       importResults.add(invalidImportResult);
/*     */     } 
/* 235 */     this.logger.exiting(CLASS_NAME, "createCertificatePrivateKeyHolder");
/* 236 */     return Collections.emptyList();
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
/*     */   public Optional<Certificate> createCertificateAsAttribute(byte[] bytes, User user) {
/*     */     X509AttributeCertificateHolder certificate;
/* 250 */     String METHOD_NAME = "createCertificateAsAttribute";
/* 251 */     this.logger.entering(CLASS_NAME, "createCertificateAsAttribute");
/*     */     
/*     */     try {
/* 254 */       certificate = new X509AttributeCertificateHolder(bytes);
/* 255 */     } catch (IOException e) {
/* 256 */       this.logger.log(Level.WARNING, "000075", this.i18n.getEnglishMessage("certificateBytesCannotBeRead", new String[] { e
/* 257 */               .getMessage() }), CLASS_NAME);
/* 258 */       throw new CEBASCertificateException(this.i18n
/* 259 */           .getMessage("certificateBytesCannotBeRead", new String[] { e.getMessage() }));
/*     */     } 
/* 261 */     this.logger.exiting(CLASS_NAME, "createCertificateAsAttribute");
/* 262 */     return Optional.of(getCertificateInstance(certificate, bytes, user));
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
/*     */   public abstract Certificate getCertificateInstance();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Certificate getCertificateInstance(X509AttributeCertificateHolder paramX509AttributeCertificateHolder, User paramUser);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Certificate getCertificateInstance(X509AttributeCertificateHolder paramX509AttributeCertificateHolder, byte[] paramArrayOfbyte, User paramUser);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Certificate getCertificateInstance(X509Certificate paramX509Certificate, User paramUser);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Certificate getCertificateInstance(X509Certificate paramX509Certificate, byte[] paramArrayOfbyte, User paramUser);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Certificate getCertificateInstance(String paramString, CertificateSignRequest paramCertificateSignRequest, User paramUser);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Certificate getCertificateInstance(String paramString, CertificateSignRequest paramCertificateSignRequest, X509AttributeCertificateHolder paramX509AttributeCertificateHolder, User paramUser);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Certificate createCertificate(String resourcePath, User user) throws CEBASCertificateException {
/* 361 */     String METHOD_NAME = "createCertificate";
/* 362 */     this.logger.entering(CLASS_NAME, "createCertificate");
/* 363 */     X509Certificate cer = getCertificate(resourcePath);
/* 364 */     if (cer == null) {
/* 365 */       throw new CertificateNotFoundException(this.i18n
/* 366 */           .getEnglishMessage("couldNotCreateCertFormBytes", new String[] { "The certificate under path " + resourcePath + " could not be created." }));
/*     */     }
/*     */     
/* 369 */     Certificate certificate = getCertificateInstance(cer, user);
/* 370 */     this.logger.entering(CLASS_NAME, "createCertificate");
/* 371 */     return certificate;
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
/*     */   public ImportResult getImportResult(CertificatePrivateKeyHolder holder, String errorMessage, boolean isSuccess) {
/* 386 */     return this.importResultFactory.getImportResult(holder, errorMessage, isSuccess);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getCertificateBytes(Certificate certificate) {
/*     */     try {
/* 398 */       if (certificate.getCertificateData() != null && certificate
/* 399 */         .getCertificateData().getOriginalBytes() != null) {
/* 400 */         return certificate.getCertificateData().getOriginalBytes();
/*     */       }
/* 402 */       return (certificate.getCertificateData().getCert() != null) ? certificate
/* 403 */         .getCertificateData().getCert().getEncoded() : certificate
/* 404 */         .getCertificateData().getAttributesCertificateHolder().getEncoded();
/* 405 */     } catch (CertificateEncodingException|IOException e) {
/* 406 */       LOG.log(Level.FINEST, e.getMessage(), e);
/*     */       
/* 408 */       CertificateNotFoundException ex = new CertificateNotFoundException(this.i18n.getMessage("errorGettingEncodedCertificateBytes", new String[] { e.getMessage() }), "errorGettingEncodedCertificateBytes");
/*     */       
/* 410 */       this.logger.logWithTranslation(Level.WARNING, "000058X", ex.getMessageId(), new String[] { e
/* 411 */             .getMessage() }, ex.getClass().getSimpleName());
/* 412 */       throw ex;
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
/*     */   public Certificate getCertificateFromBase64(String certificateBase64) {
/* 424 */     byte[] certificateRaw = Base64.getDecoder().decode(certificateBase64);
/* 425 */     return getCertificateFromBytes(certificateRaw);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Certificate getCertificateFromBytes(byte[] certificateRaw) {
/* 436 */     User currentUser = this.session.getCurrentUser();
/* 437 */     InputStream is = new ByteArrayInputStream(certificateRaw);
/* 438 */     Optional<Certificate> certificateOptional = createCertificate(is, certificateRaw, currentUser);
/* 439 */     if (!certificateOptional.isPresent()) {
/* 440 */       Optional<Certificate> ehhRights = createCertificate(null, certificateRaw, currentUser);
/* 441 */       if (ehhRights.isPresent()) {
/*     */         
/* 443 */         CertificateNotFoundException certificateNotFoundException = new CertificateNotFoundException(this.i18n.getMessage("couldNotCheckSignatureOfMessageForEngRights"), "couldNotCheckSignatureOfMessageForEngRights");
/*     */         
/* 445 */         this.logger.logWithTranslation(Level.WARNING, "000099X", certificateNotFoundException.getMessageId(), certificateNotFoundException
/* 446 */             .getClass().getSimpleName());
/* 447 */         throw certificateNotFoundException;
/*     */       } 
/*     */       
/* 450 */       CertificateNotFoundException ex = new CertificateNotFoundException(this.i18n.getMessage("couldNotCreateCertFormBytes"), "couldNotCreateCertFormBytes");
/*     */       
/* 452 */       this.logger.logWithTranslation(Level.WARNING, "000100X", ex.getMessageId(), ex
/* 453 */           .getClass().getSimpleName());
/* 454 */       throw ex;
/*     */     } 
/*     */     
/* 457 */     return certificateOptional
/* 458 */       .<Throwable>orElseThrow(this.logger.logWithTranslationSupplier(Level.WARNING, "000066X", (CEBASException)new ZenZefiConfigurationException(this.i18n
/*     */             
/* 460 */             .getMessage("couldNotCreateCertFormBytes"), "couldNotCreateCertFormBytes")));
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
/*     */   private X509Certificate getCertificate(InputStream stream) {
/*     */     X509Certificate certificate;
/*     */     try {
/* 474 */       CertificateFactory cf = CertificateFactory.getInstance("X.509");
/* 475 */       certificate = (X509Certificate)cf.generateCertificate(stream);
/* 476 */     } catch (CertificateException ex) {
/* 477 */       warnCouldNotCreateX509Certificate(ex.getMessage(), ex);
/* 478 */       throw new CEBASCertificateException(this.i18n.getMessage("couldNotCreateCertFormBytes", new String[] { ex
/* 479 */               .getMessage() }));
/*     */     } finally {
/*     */       try {
/* 482 */         if (stream != null) {
/* 483 */           stream.close();
/*     */         }
/* 485 */       } catch (IOException e) {
/* 486 */         warnCouldNotCloseStreamOnCreateX509Certificate(e.getMessage(), e);
/*     */       } 
/*     */     } 
/* 489 */     return certificate;
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
/*     */   private X509Certificate getCertificate(String resourcePath) {
/* 502 */     X509Certificate certificate = null;
/* 503 */     InputStream inputStream = null;
/*     */     try {
/* 505 */       CertificateFactory cf = CertificateFactory.getInstance("X.509");
/* 506 */       ClassPathResource classPathResource = new ClassPathResource(resourcePath);
/* 507 */       if (classPathResource.exists()) {
/* 508 */         certificate = (X509Certificate)cf.generateCertificate(classPathResource.getInputStream());
/* 509 */       } else if ((new File(resourcePath)).exists()) {
/* 510 */         inputStream = new FileInputStream(resourcePath);
/* 511 */         certificate = (X509Certificate)cf.generateCertificate(inputStream);
/*     */       } else {
/* 513 */         warnCouldNotCreateX509Certificate("Resource does not exist: " + resourcePath);
/*     */       } 
/* 515 */     } catch (CertificateException|IOException ex) {
/* 516 */       warnCouldNotCreateX509Certificate(ex.getMessage(), ex);
/*     */     } finally {
/* 518 */       if (inputStream != null) {
/*     */         try {
/* 520 */           inputStream.close();
/* 521 */         } catch (IOException e) {
/* 522 */           warnCouldNotCloseStreamOnCreateX509Certificate(e.getMessage(), e);
/*     */         } 
/*     */       }
/*     */     } 
/* 526 */     return certificate;
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
/*     */   private List<Optional<CertificatePrivateKeyHolder>> createCertificateHoldersFromPKCS(InputStream stream, User user, String password, List<ImportResult> importResults, String filename) {
/* 544 */     return PKCS12Manager.readPKCS12(stream, password, user, this.logger, this.certificateHolderFactory, importResults, filename, this.i18n);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void warnCouldNotCreateX509Certificate(String message, Exception e) {
/* 555 */     LOG.log(Level.FINEST, e.getMessage(), e);
/* 556 */     warnCouldNotCreateX509Certificate(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void warnCouldNotCreateX509Certificate(String message) {
/* 566 */     this.logger.log(Level.WARNING, "000050", this.i18n
/* 567 */         .getEnglishMessage("couldNotCreateCertFormBytes", new String[] { message }), CLASS_NAME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void warnCouldNotCloseStreamOnCreateX509Certificate(String message, Exception e) {
/* 578 */     LOG.log(Level.FINEST, e.getMessage(), e);
/* 579 */     warnCouldNotCloseStreamOnCreateX509Certificate(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void warnCouldNotCloseStreamOnCreateX509Certificate(String message) {
/* 589 */     this.logger.log(Level.WARNING, "000172X", this.i18n
/* 590 */         .getEnglishMessage("cannotCloseStreamOnCreateCertificate", new String[] { message }), CLASS_NAME);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\factories\AbstractCertificateFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */