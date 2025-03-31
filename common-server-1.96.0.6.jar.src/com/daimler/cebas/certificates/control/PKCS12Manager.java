/*     */ package com.daimler.cebas.certificates.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.CertificateCryptoEngine;
/*     */ import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.PkcsException;
/*     */ import com.daimler.cebas.certificates.control.factories.CertificateHolderFactory;
/*     */ import com.daimler.cebas.certificates.control.validation.CertificatesFieldsValidator;
/*     */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateState;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.common.CryptoParser;
/*     */ import com.daimler.cebas.common.PublicKeyCryptoAlgorithms;
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.secunet.provider.pkcs12.AttributeCertificate;
/*     */ import com.secunet.provider.pkcs12.Pkcs12Object;
/*     */ import java.io.InputStream;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.naming.InvalidNameException;
/*     */ import org.bouncycastle.cert.X509AttributeCertificateHolder;
/*     */ import org.hibernate.boot.model.naming.IllegalIdentifierException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PKCS12Manager
/*     */ {
/*  50 */   private static final Logger LOG = Logger.getLogger(PKCS12Manager.class.getSimpleName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   private static final String CLASS_NAME = PKCS12Manager.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] generatePKCS12(List<CertificatePrivateKeyHolder> certPrivateKeyHolders, Logger logger, String password, MetadataManager i18n) {
/*  83 */     String METHOD_NAME = "generatePKCS12";
/*  84 */     logger.entering(CLASS_NAME, "generatePKCS12");
/*  85 */     Pkcs12Object p12Object = new Pkcs12Object(password.toCharArray());
/*  86 */     certPrivateKeyHolders.forEach(holder -> addEntryToPKCS(p12Object, holder, i18n, logger));
/*  87 */     return p12Object.getEncoded();
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
/*     */   public static byte[] generateDiagnosticPKCS12(List<CertificatePrivateKeyHolder> certPrivateKeyHolders, Logger logger, String password, MetadataManager i18n) {
/* 105 */     String METHOD_NAME = "generateDiagnosticPKCS12";
/* 106 */     logger.entering(CLASS_NAME, "generateDiagnosticPKCS12");
/* 107 */     Pkcs12Object p12Object = new Pkcs12Object(password.toCharArray());
/* 108 */     certPrivateKeyHolders.forEach(holder -> addEntryWithEnhancedCertsToPKCS(p12Object, holder, i18n, logger));
/* 109 */     return p12Object.getEncoded();
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
/*     */   public static byte[] generateEncryptedPKCS12(List<CertificatePrivateKeyHolder> certificatePrivateKeyHolders, Logger logger, String password, PublicKey publicKey, MetadataManager i18n) throws GeneralSecurityException {
/* 130 */     byte[] generatedPKCS12 = generatePKCS12(certificatePrivateKeyHolders, logger, password, i18n);
/* 131 */     return CertificateCryptoEngine.encryptECIES(generatedPKCS12, publicKey);
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
/*     */   public static byte[] generateEncryptedDiagnosticPKCS12(List<CertificatePrivateKeyHolder> certificatePrivateKeyHolders, Logger logger, String password, PublicKey publicKey, MetadataManager i18n) throws GeneralSecurityException {
/* 154 */     byte[] generatedPKCS12 = generateDiagnosticPKCS12(certificatePrivateKeyHolders, logger, password, i18n);
/* 155 */     return CertificateCryptoEngine.encryptECIES(generatedPKCS12, publicKey);
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
/*     */   public static byte[] decryptPKCS12(byte[] pkcs12Data, PrivateKey privateKey) throws GeneralSecurityException {
/* 169 */     return CertificateCryptoEngine.decryptECIES(pkcs12Data, privateKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PublicKey getPublicKey(byte[] bytes) {
/*     */     try {
/* 181 */       X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
/* 182 */       KeyFactory factory = KeyFactory.getInstance(PublicKeyCryptoAlgorithms.ECDH.name());
/* 183 */       return factory.generatePublic(spec);
/* 184 */     } catch (NoSuchAlgorithmException|java.security.spec.InvalidKeySpecException e) {
/* 185 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 186 */       throw new CEBASCertificateException("Cannot create public key from bytes! " + e.getMessage());
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
/*     */ 
/*     */   
/*     */   public static List<Optional<CertificatePrivateKeyHolder>> readPKCS12(InputStream inputStream, String password, User user, Logger logger, CertificateHolderFactory certificateHolderFactory, List<ImportResult> importResults, String fileName, MetadataManager i18n) {
/*     */     Pkcs12Object p12input;
/* 206 */     String METHOD_NAME = "readPKCS12";
/* 207 */     logger.entering(CLASS_NAME, "readPKCS12");
/*     */     
/*     */     try {
/* 210 */       p12input = new Pkcs12Object(inputStream, password.toCharArray());
/* 211 */     } catch (CertificateEncodingException|java.io.IOException e) {
/* 212 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 213 */       importResults.add(new ImportResult(fileName, "", "", i18n
/* 214 */             .getMessage("couldNotReadPrivateKey", new String[] { e.getMessage() }), false));
/* 215 */       logger.logWithTranslation(Level.SEVERE, "000121X", "errorGettingEncodedCertificateBytes", new String[] { e
/* 216 */             .getMessage() }, CLASS_NAME);
/* 217 */       return Collections.emptyList();
/*     */     } 
/* 219 */     List<Optional<CertificatePrivateKeyHolder>> pkcsEntries = new ArrayList<>();
/* 220 */     String friendlyName = getFriendlyNameFromKeyBag(p12input, i18n, logger);
/*     */     
/* 222 */     Arrays.<byte[]>stream(p12input.getLocalKeyIds(Pkcs12Object.BagType.KeyBag)).forEach(keyId -> extractCertificate(user, logger, certificateHolderFactory, importResults, fileName, i18n, p12input, pkcsEntries, keyId, friendlyName));
/*     */ 
/*     */     
/* 225 */     Arrays.<Certificate>stream(p12input.getCertificates()).forEach(c -> {
/*     */           if (c instanceof X509Certificate) {
/*     */             checkPublicKey(i18n, logger, (X509Certificate)c);
/*     */             pkcsEntries.add(Optional.of(certificateHolderFactory.createCertificateHolder(fileName, (X509Certificate)c, friendlyName, user)));
/*     */           } else if (c instanceof AttributeCertificate) {
/*     */             pkcsEntries.add(Optional.of(certificateHolderFactory.createCertificateHolder(fileName, (AttributeCertificate)c, user)));
/*     */           } 
/*     */         });
/* 233 */     logger.exiting(CLASS_NAME, "readPKCS12");
/* 234 */     return pkcsEntries;
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
/*     */   private static void extractCertificate(User user, Logger logger, CertificateHolderFactory certificateHolderFactory, List<ImportResult> importResults, String fileName, MetadataManager i18n, Pkcs12Object p12input, List<Optional<CertificatePrivateKeyHolder>> pkcsEntries, byte[] keyId, String friendlyName) {
/*     */     try {
/* 255 */       X509Certificate pCert = (X509Certificate)p12input.getCertificate(keyId);
/* 256 */       checkPublicKey(i18n, logger, pCert);
/* 257 */       PrivateKey pKey = p12input.getPrivateKey(keyId);
/* 258 */       pkcsEntries.add(Optional.of(certificateHolderFactory.createCertificateHolder(fileName, pCert, pKey, friendlyName, user)));
/* 259 */       p12input.remove(keyId);
/* 260 */     } catch (KeyStoreException e) {
/* 261 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 262 */       importResults.add(new ImportResult(fileName, "", "", i18n
/* 263 */             .getMessage("couldNotReadPrivateKey", new String[] { e.getMessage() }), false));
/* 264 */       logger.logWithTranslation(Level.SEVERE, "000144X", "couldNotReadPrivateKey", new String[] { e
/* 265 */             .getMessage() }, CLASS_NAME);
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
/*     */   
/*     */   private static void addEntryToPKCS(Pkcs12Object p12Object, CertificatePrivateKeyHolder holder, MetadataManager i18n, Logger logger) {
/*     */     try {
/* 284 */       if (holder.hasPrivateKey()) {
/* 285 */         p12Object.add(holder.getPrivateKey(), holder.getCertificate().getCertificateData().getCert());
/*     */       } else {
/* 287 */         p12Object.add(holder.getCertificate().getCertificateData().getCert());
/*     */       } 
/* 289 */     } catch (InvalidNameException|CertificateEncodingException e) {
/* 290 */       logAndThrowPkcsException(i18n, logger, e);
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
/*     */   private static void logAndThrowPkcsException(MetadataManager i18n, Logger logger, Exception originalException) {
/* 305 */     LOG.log(Level.FINEST, originalException.getMessage(), originalException);
/* 306 */     PkcsException exception = new PkcsException(i18n.getMessage("errorAddingPKCS12Entry"), "errorAddingPKCS12Entry");
/*     */     
/* 308 */     logger.logWithTranslation(Level.SEVERE, "000204X", exception.getMessageId(), exception
/* 309 */         .getClass().getSimpleName());
/* 310 */     throw exception;
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
/*     */   private static void addEntryWithEnhancedCertsToPKCS(Pkcs12Object p12Object, CertificatePrivateKeyHolder holder, MetadataManager i18n, Logger logger) {
/*     */     try {
/* 329 */       X509Certificate cert = holder.getCertificate().getCertificateData().getCert();
/* 330 */       if (holder.hasPrivateKey()) {
/* 331 */         p12Object.add(holder.getPrivateKey(), cert);
/*     */       } else {
/* 333 */         p12Object.add(cert);
/*     */       } 
/*     */ 
/*     */       
/* 337 */       List<Certificate> children = holder.getCertificate().getChildren();
/* 338 */       if (children != null && !children.isEmpty()) {
/* 339 */         children.stream().filter(child -> (child.getType() == CertificateType.ENHANCED_RIGHTS_CERTIFICATE && child.getState().equals(CertificateState.ISSUED)))
/* 340 */           .forEach(child -> addEnhancedRights(p12Object, i18n, logger, cert, child));
/*     */       }
/* 342 */     } catch (InvalidNameException|CertificateEncodingException e) {
/* 343 */       logAndThrowPkcsException(i18n, logger, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addEnhancedRights(Pkcs12Object p12Object, MetadataManager i18n, Logger logger, X509Certificate parentHolder, Certificate child) {
/*     */     try {
/* 351 */       X509AttributeCertificateHolder attributeCertHolder = child.getCertificateData().getAttributesCertificateHolder();
/* 352 */       p12Object.add((Certificate)new AttributeCertificate(attributeCertHolder), parentHolder);
/* 353 */     } catch (CertificateEncodingException|InvalidNameException e) {
/* 354 */       logAndThrowPkcsException(i18n, logger, e);
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
/*     */   private static void checkPublicKey(MetadataManager i18n, Logger logger, X509Certificate certificate) {
/*     */     try {
/* 367 */       CryptoParser.validatePublicKey(certificate.getEncoded());
/* 368 */     } catch (IllegalIdentifierException|IllegalArgumentException e) {
/* 369 */       logger.log(Level.WARNING, "000417X", i18n
/* 370 */           .getEnglishMessage("thePublicKeyIsInvalid", new String[] { e.getMessage() }), CLASS_NAME);
/*     */       
/* 372 */       throw new CEBASException(i18n
/* 373 */           .getMessage("thePublicKeyIsInvalid", new String[] { e.getMessage() }));
/* 374 */     } catch (CertificateEncodingException|java.io.IOException e) {
/* 375 */       LOG.log(Level.FINEST, e.getMessage(), e);
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
/*     */   private static String getFriendlyNameFromKeyBag(Pkcs12Object p12input, MetadataManager i18n, Logger logger) {
/* 392 */     Set<String> zkNOs = (Set<String>)Arrays.<String>stream(p12input.getFriendlyNames()).filter(CertificatesFieldsValidator::isZkNo).collect(Collectors.toSet());
/*     */     
/* 394 */     if (zkNOs.size() == 1)
/* 395 */       return zkNOs.iterator().next(); 
/* 396 */     if (zkNOs.size() == 0) {
/* 397 */       logger.log(Level.WARNING, "000704X", i18n
/* 398 */           .getEnglishMessage("friendlyNameFailsRegexCheck"), CLASS_NAME);
/*     */     } else {
/*     */       
/* 401 */       logger.log(Level.WARNING, "000703X", i18n
/* 402 */           .getEnglishMessage("friendlyNameIsInconsistent"), CLASS_NAME);
/*     */     } 
/*     */     
/* 405 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\PKCS12Manager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */