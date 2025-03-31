/*     */ package com.daimler.cebas.security;
/*     */ 
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509KeyManager;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SSLClassPathTrustStoreLoader
/*     */ {
/*     */   private static final String DUMMY = "njqnA3dv%JodGR\"al&$C.NQD'xV44d`?";
/*     */   private static final String CERT_PATH = "sslcert/**";
/*     */   private static final String CERT_EXTENSION = ".cer";
/*     */   private Logger logger;
/*     */   
/*     */   public SSLClassPathTrustStoreLoader(Logger logger) {
/*  71 */     this.logger = logger;
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
/*     */   public SSLContext provideSSLContext() throws NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, CertificateException, IOException {
/*  93 */     String defaultAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
/*  94 */     X509KeyManager jvmKeyManager = getKeyManager(defaultAlgorithm, null, null);
/*  95 */     X509TrustManager customTrustManager = getTrustManager("SunX509", createCustomKeyStore());
/*  96 */     X509TrustManager jvmTrustManager = getTrustManager(defaultAlgorithm, null);
/*     */     
/*  98 */     KeyManager[] keyManagers = { jvmKeyManager };
/*     */ 
/*     */     
/* 101 */     TrustManager[] trustManagers = { new CompositeX509TrustManager(this.logger, new X509TrustManager[] { customTrustManager, jvmTrustManager }) };
/*     */     
/* 103 */     SSLContext context = SSLContext.getInstance("SSL");
/* 104 */     context.init(keyManagers, trustManagers, null);
/* 105 */     return context;
/*     */   }
/*     */ 
/*     */   
/*     */   private X509KeyManager getKeyManager(String algorithm, KeyStore keystore, char[] password) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
/* 110 */     KeyManagerFactory factory = KeyManagerFactory.getInstance(algorithm);
/* 111 */     factory.init(keystore, password);
/* 112 */     KeyManager[] keyManagers = factory.getKeyManagers();
/* 113 */     for (KeyManager keyManager : keyManagers) {
/* 114 */       if (keyManager instanceof X509KeyManager) {
/* 115 */         return (X509KeyManager)keyManager;
/*     */       }
/*     */     } 
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private X509TrustManager getTrustManager(String algorithm, KeyStore keystore) throws KeyStoreException, NoSuchAlgorithmException {
/* 123 */     TrustManagerFactory factory = TrustManagerFactory.getInstance(algorithm);
/* 124 */     factory.init(keystore);
/* 125 */     TrustManager[] trustManagers = factory.getTrustManagers();
/* 126 */     for (TrustManager trustManager : trustManagers) {
/* 127 */       if (trustManager instanceof X509TrustManager) {
/* 128 */         return (X509TrustManager)trustManager;
/*     */       }
/*     */     } 
/* 131 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private KeyStore createCustomKeyStore() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
/* 139 */     KeyStore myTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
/* 140 */     myTrustStore.load(null, "njqnA3dv%JodGR\"al&$C.NQD'xV44d`?".toCharArray());
/* 141 */     TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/* 142 */     tmf.init(myTrustStore);
/*     */     
/* 144 */     List<Certificate> certs = loadCertificates();
/* 145 */     int i = 1;
/* 146 */     for (Certificate certificate : certs) {
/* 147 */       if (certificate instanceof X509Certificate) {
/* 148 */         X509Certificate x509 = (X509Certificate)certificate;
/* 149 */         myTrustStore.setCertificateEntry(x509.getSubjectDN().getName(), certificate);
/*     */         
/* 151 */         this.logger.log(Level.INFO, "000511", "Successfully imported SSL certificate " + x509
/* 152 */             .getSubjectDN().getName(), 
/* 153 */             getClass().getSimpleName());
/*     */         continue;
/*     */       } 
/* 156 */       myTrustStore.setCertificateEntry("cebas" + i++, certificate);
/*     */     } 
/*     */     
/* 159 */     return myTrustStore;
/*     */   }
/*     */   
/*     */   private List<Certificate> loadCertificates() throws CertificateException, IOException {
/* 163 */     List<Certificate> result = new ArrayList<>();
/* 164 */     CertificateFactory cf = CertificateFactory.getInstance("X.509");
/*     */     
/* 166 */     List<Resource> certFiles = getCertFiles("sslcert/**", ".cer");
/* 167 */     for (Resource resource : certFiles) {
/* 168 */       InputStream fis = resource.getInputStream();
/* 169 */       BufferedInputStream bis = new BufferedInputStream(fis);
/*     */       
/* 171 */       while (bis.available() > 0) {
/*     */         try {
/* 173 */           Certificate cert = cf.generateCertificate(bis);
/* 174 */           result.add(cert);
/* 175 */         } catch (Exception e) {
/* 176 */           this.logger.log(Level.WARNING, "000512X", "Can not read and import SSL certificate " + resource
/* 177 */               .getFilename() + " because it may be possibly broken or malformed", 
/*     */               
/* 179 */               getClass().getSimpleName());
/*     */         } 
/*     */       } 
/* 182 */       bis.close();
/* 183 */       fis.close();
/*     */     } 
/* 185 */     return result;
/*     */   }
/*     */   
/*     */   protected List<Resource> getCertFiles(String resourcePath, String fileExtension) throws IOException {
/* 189 */     List<Resource> propertyFiles = new ArrayList<>();
/*     */     
/*     */     try {
/* 192 */       PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
/* 193 */       Resource[] resources = resolver.getResources(resourcePath);
/*     */       
/* 195 */       for (Resource resource : resources) {
/* 196 */         if (resource.getFilename() != null && resource.getFilename().endsWith(fileExtension)) {
/* 197 */           this.logger.log(Level.INFO, "000508", "Reading custom trust certificate: " + resource
/* 198 */               .getFilename(), 
/* 199 */               getClass().getSimpleName());
/* 200 */           propertyFiles.add(resource);
/*     */         } 
/*     */       } 
/* 203 */     } catch (FileNotFoundException e) {
/* 204 */       this.logger.log(Level.INFO, "000521", "There are no custom certificates available, SSL handshake will be based on the default Java trust store only.", 
/*     */           
/* 206 */           getClass().getSimpleName());
/*     */     } 
/*     */     
/* 209 */     return propertyFiles;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\security\SSLClassPathTrustStoreLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */