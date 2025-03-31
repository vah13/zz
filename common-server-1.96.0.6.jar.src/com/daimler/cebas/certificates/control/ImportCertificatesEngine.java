/*     */ package com.daimler.cebas.certificates.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*     */ import com.daimler.cebas.certificates.control.importing.CertificatesImporter;
/*     */ import com.daimler.cebas.certificates.control.vo.EncryptedPKCSImportResult;
/*     */ import com.daimler.cebas.certificates.control.vo.EncryptedPKCSPackageInput;
/*     */ import com.daimler.cebas.certificates.control.vo.ImportInput;
/*     */ import com.daimler.cebas.certificates.control.vo.ImportInputType;
/*     */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*     */ import com.daimler.cebas.certificates.control.vo.LocalImportInput;
/*     */ import com.daimler.cebas.certificates.control.vo.RemoteCertificateImportInput;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.common.control.HexUtil;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Paths;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.PrivateKey;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Base64;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ import org.springframework.web.util.HtmlUtils;
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @Transactional(propagation = Propagation.REQUIRED)
/*     */ public class ImportCertificatesEngine
/*     */ {
/*     */   private static final String EMPTY = "";
/*  58 */   private static final String CLASS_NAME = ImportCertificatesEngine.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   private static final Logger LOG = Logger.getLogger(CLASS_NAME);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String P12 = ".p12";
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
/*     */   private CertificatesImporter importer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractConfigurator configurator;
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
/*     */   protected MetadataManager i18n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public ImportCertificatesEngine(Session session, CertificatesImporter importer, AbstractConfigurator configurator, Logger logger, MetadataManager i18n) {
/* 112 */     this.session = session;
/* 113 */     this.importer = importer;
/* 114 */     this.configurator = configurator;
/* 115 */     this.logger = logger;
/* 116 */     this.i18n = i18n;
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
/*     */   @Transactional(propagation = Propagation.REQUIRED)
/*     */   public List<ImportResult> importCertificatesFromBase64SameTransaction(List<String> base64Certificates, boolean onlyFromPKI, boolean allowPrivateKeys) {
/* 132 */     return importCertificatesFromBase64(base64Certificates, onlyFromPKI, allowPrivateKeys);
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
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public List<ImportResult> importCertificatesFromBase64NewTransaction(List<String> base64Certificates, boolean onlyFromPKI, boolean allowPrivateKeys) {
/* 148 */     return importCertificatesFromBase64(base64Certificates, onlyFromPKI, allowPrivateKeys);
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
/*     */   private List<ImportResult> importCertificatesFromBase64(List<String> base64Certificates, boolean onlyFromPKI, boolean allowPrivateKeys) {
/* 164 */     List<RemoteCertificateImportInput> remoteInputs = (List<RemoteCertificateImportInput>)base64Certificates.stream().map(base64Cert -> new RemoteCertificateImportInput("", base64Cert)).collect(Collectors.toList());
/* 165 */     List<ImportResult> importRemoteCertificates = importRemoteCertificates(remoteInputs, onlyFromPKI, true);
/*     */     
/*     */     try {
/* 168 */       remoteInputs.clear();
/* 169 */       base64Certificates.clear();
/* 170 */     } catch (UnsupportedOperationException e) {
/* 171 */       this.logger.log(Level.FINEST, "000354X", e.getMessage(), CLASS_NAME);
/*     */     } 
/*     */     
/* 174 */     return importRemoteCertificates;
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
/*     */   public List<ImportResult> importRemoteCertificates(List<RemoteCertificateImportInput> remoteInputs, boolean onlyFromPKI, boolean allowPrivateKeys) {
/* 189 */     List<ImportInput> inputs = new ArrayList<>(remoteInputs.size());
/*     */     try {
/* 191 */       this.session.getSystemIntegrityCheckResult().clear();
/* 192 */       remoteInputs.forEach(remoteInput -> {
/*     */             byte[] certBytes = Base64.getDecoder().decode(remoteInput.getCertificateBytes());
/*     */             
/*     */             InputStream inputStream = new ByteArrayInputStream(certBytes);
/*     */             inputs.add(createImportInput(inputStream, certBytes, remoteInput.getFileName(), Optional.empty()));
/*     */           });
/* 198 */       List<ImportResult> importCertificates = this.importer.importCertificates(inputs, this.session.getCurrentUser(), onlyFromPKI, allowPrivateKeys);
/* 199 */       inputs.clear();
/*     */       
/* 201 */       return importCertificates;
/*     */     } finally {
/* 203 */       closeInputStreams(inputs);
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
/*     */   public List<ImportResult> importCertificatesFromLocal(List<LocalImportInput> inputs) {
/* 215 */     List<ImportInput> input = new ArrayList<>(inputs.size());
/* 216 */     inputs.forEach(entry -> {
/*     */           String pathString = entry.getFilePath();
/*     */           
/*     */           try {
/*     */             File initialFile = new File(pathString);
/*     */             InputStream inputStream = FileUtils.openInputStream(initialFile);
/*     */             byte[] bytes = FileUtils.readFileToByteArray(initialFile);
/*     */             ImportInput importInput = createImportInput(inputStream, bytes, initialFile.getName(), entry.hasPassword() ? Optional.<String>of(entry.getPassword()) : Optional.<String>empty());
/*     */             input.add(importInput);
/* 225 */           } catch (IOException e) {
/*     */             handleImportFromLocalException(pathString, e);
/*     */           } 
/*     */         });
/* 229 */     List<ImportResult> result = this.importer.importCertificates(input, this.session.getCurrentUser(), true);
/* 230 */     inputs.clear();
/* 231 */     input.clear();
/* 232 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ImportResult> importCertificates(List<MultipartFile> files, boolean allowPrivateKeys) {
/* 243 */     this.session.getSystemIntegrityCheckResult().clear();
/* 244 */     List<ImportInput> input = new ArrayList<>(files.size());
/* 245 */     for (MultipartFile file : files) {
/*     */       try {
/* 247 */         InputStream inputStream = file.getInputStream();
/* 248 */         byte[] bytes = file.getBytes();
/* 249 */         String fileName = Paths.get(file.getOriginalFilename(), new String[0]).getFileName().toString();
/* 250 */         input.add(createImportInput(inputStream, bytes, fileName, Optional.empty()));
/* 251 */       } catch (IOException e) {
/* 252 */         LOG.log(Level.FINEST, e.getMessage(), e);
/* 253 */         this.logger.logToFileOnly(CLASS_NAME, this.i18n
/* 254 */             .getEnglishMessage("cannotReadTheMultipart", new String[] {
/* 255 */                 HtmlUtils.htmlEscape(file.getOriginalFilename())
/*     */               }), e);
/* 257 */         throw new CEBASCertificateException(this.i18n.getMessage("cannotReadFile"));
/*     */       } 
/*     */     } 
/*     */     
/* 261 */     List<ImportResult> importCertificates = this.importer.importCertificates(input, this.session.getCurrentUser(), allowPrivateKeys);
/*     */     
/* 263 */     input.clear();
/* 264 */     return importCertificates;
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
/*     */   public EncryptedPKCSImportResult importEncryptedPKCSPackages(List<EncryptedPKCSPackageInput> inputPackages, PrivateKey privateKey) {
/* 279 */     String METHOD_NAME = "importEncryptedPKCSPackage";
/* 280 */     this.logger.entering(CLASS_NAME, "importEncryptedPKCSPackage");
/* 281 */     List<ImportInput> importInputs = new ArrayList<>();
/* 282 */     inputPackages.forEach(inputPackage -> extractImportInput(privateKey, importInputs, inputPackage));
/* 283 */     List<ImportResult> importResult = this.importer.importCertificates(importInputs, this.session.getCurrentUser(), true);
/* 284 */     EncryptedPKCSImportResult result = new EncryptedPKCSImportResult(getEncryptedPKCSImportResult(importResult), importResult);
/*     */     
/* 286 */     importInputs.clear();
/* 287 */     this.logger.exiting(CLASS_NAME, "importEncryptedPKCSPackage");
/* 288 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImportResult checkImportIntoChain(CertificatePrivateKeyHolder holder) {
/* 299 */     return this.importer.checkImportIntoChain(holder, this.session.getCurrentUser());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Certificate> importFromBaseStore(User currentUser) {
/* 310 */     return this.importer.importFromBaseStore(currentUser);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Certificate> restoreCertificatesFromBaseStore(User currentUser) {
/* 321 */     return this.importer.restoreCertificatesFromBaseStore(currentUser);
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
/*     */   protected ImportInput createImportInput(InputStream inputStream, byte[] bytes, String fileName, Optional<String> password) {
/* 338 */     if (fileName.endsWith(".p12")) {
/* 339 */       return password.<ImportInput>map(pass -> new ImportInput(inputStream, bytes, fileName, pass, ImportInputType.PKCS12))
/* 340 */         .orElseGet(() -> new ImportInput(inputStream, bytes, fileName, ImportInputType.PKCS12));
/*     */     }
/* 342 */     return new ImportInput(inputStream, bytes, fileName, ImportInputType.CERTIFICATE_FILE);
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
/*     */   protected void extractImportInput(PrivateKey privateKey, List<ImportInput> importInputs, EncryptedPKCSPackageInput inputPackage) {
/* 358 */     String backendSubjectKeyIdentifier = getDecodedHexBackendSubjectKeyIdentifier(inputPackage
/* 359 */         .getBackendSubjectKeyIdentifier());
/* 360 */     byte[] decodedPKCSPackage = getDecodedPKCSPackage(inputPackage.getEncryptedPKCSPackage(), backendSubjectKeyIdentifier);
/*     */     
/* 362 */     byte[] decryptedPKCSPackage = decryptPKCS12Package(decodedPKCSPackage, privateKey, backendSubjectKeyIdentifier);
/* 363 */     InputStream inputStream = new ByteArrayInputStream(decryptedPKCSPackage);
/*     */ 
/*     */     
/* 366 */     backendSubjectKeyIdentifier = backendSubjectKeyIdentifier + ".p12";
/*     */     
/* 368 */     ImportInput importInput = createImportInput(inputStream, decryptedPKCSPackage, backendSubjectKeyIdentifier, 
/* 369 */         Optional.of(this.configurator.getPKCS12PackagePassword()));
/* 370 */     importInputs.add(importInput);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void closeInputStreams(List<ImportInput> inputs) {
/* 380 */     inputs.forEach(this::closeInputStream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void closeInputStream(ImportInput input) {
/* 390 */     if (input.getStream() != null) {
/*     */       try {
/* 392 */         input.getStream().close();
/* 393 */       } catch (IOException e) {
/* 394 */         LOG.log(Level.FINEST, e.getMessage(), e);
/* 395 */         this.logger.log(Level.WARNING, "000354X", this.i18n
/* 396 */             .getEnglishMessage("cannotCloseStreamOnCreateCertificate", new String[] {
/* 397 */                 e.getMessage()
/*     */               }), CLASS_NAME);
/*     */       } 
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
/*     */   private void handleImportFromLocalException(String pathString, IOException e) {
/* 412 */     LOG.log(Level.FINEST, e.getMessage(), e);
/*     */     
/* 414 */     CEBASCertificateException exception = new CEBASCertificateException(this.i18n.getMessage("cannotReadFileFromPath", new String[] {
/* 415 */             HtmlUtils.htmlEscape(pathString)
/*     */           }), "cannotReadFileFromPath");
/* 417 */     this.logger.logWithTranslation(Level.WARNING, "000076", exception.getMessageId(), new String[] {
/* 418 */           HtmlUtils.htmlEscape(pathString) }, exception.getClass().getSimpleName());
/* 419 */     throw exception;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getDecodedHexBackendSubjectKeyIdentifier(String encodedBackendSubjectKeyIdentifier) {
/*     */     try {
/* 431 */       return HexUtil.base64ToHex(encodedBackendSubjectKeyIdentifier);
/* 432 */     } catch (Exception e) {
/* 433 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 434 */       logAndThrowZenzefiCertificateException("backendSerialNumberDecryptionFailed", "000178X");
/*     */ 
/*     */       
/* 437 */       return "";
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
/*     */   private byte[] decryptPKCS12Package(byte[] decodedPKCSPackage, PrivateKey privateKey, String backendSubjectKeyIdentifier) {
/*     */     try {
/* 454 */       return PKCS12Manager.decryptPKCS12(decodedPKCSPackage, privateKey);
/* 455 */     } catch (GeneralSecurityException e) {
/* 456 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 457 */       logAndThrowZenzefiCertificateException("decryptionOfPKCSPackageFailed", new String[] { backendSubjectKeyIdentifier }, "000175X");
/*     */ 
/*     */       
/* 460 */       return new byte[0];
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
/*     */   private byte[] getDecodedPKCSPackage(String input, String backendID) {
/*     */     try {
/* 474 */       return Base64.getDecoder().decode(input);
/* 475 */     } catch (IllegalArgumentException e) {
/* 476 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 477 */       logAndThrowZenzefiCertificateException("pkcsDecodeFailed", new String[] { backendID }, "000177X");
/*     */ 
/*     */       
/* 480 */       return new byte[0];
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
/*     */   private String getEncryptedPKCSImportResult(List<? extends ImportResult> importResults) {
/* 492 */     int successfulImports = 0;
/* 493 */     int failedImports = 0;
/* 494 */     for (ImportResult importResult : importResults) {
/* 495 */       if (importResult.isSuccess()) {
/* 496 */         successfulImports++; continue;
/*     */       } 
/* 498 */       failedImports++;
/*     */     } 
/*     */     
/* 501 */     return this.i18n.getMessage("pkcsImportResult", new String[] {
/* 502 */           Integer.toString(successfulImports), Integer.toString(failedImports)
/*     */         });
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
/*     */   private void logAndThrowZenzefiCertificateException(String messageID, String[] args, String logID) {
/* 519 */     CEBASCertificateException zenzefiCertificateException = new CEBASCertificateException(this.i18n.getMessage(messageID, args), messageID);
/* 520 */     this.logger.logWithTranslation(Level.WARNING, logID, zenzefiCertificateException.getMessageId(), args, zenzefiCertificateException
/* 521 */         .getClass().getSimpleName());
/* 522 */     throw zenzefiCertificateException;
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
/*     */   private void logAndThrowZenzefiCertificateException(String messageID, String logID) {
/* 537 */     CEBASCertificateException zenzefiCertificateException = new CEBASCertificateException(this.i18n.getMessage(messageID), messageID);
/* 538 */     this.logger.logWithTranslation(Level.WARNING, logID, zenzefiCertificateException.getMessageId(), zenzefiCertificateException
/* 539 */         .getClass().getSimpleName());
/* 540 */     throw zenzefiCertificateException;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\ImportCertificatesEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */