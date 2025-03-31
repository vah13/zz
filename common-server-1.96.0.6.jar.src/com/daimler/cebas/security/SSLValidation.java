/*     */ package com.daimler.cebas.security;
/*     */ 
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class SSLValidation
/*     */ {
/*  35 */   private static final String CLASS_NAME = SSLValidation.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   private static final Logger LOG = Logger.getLogger(CLASS_NAME);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${security.disable.ssl.validation}")
/*     */   private String disableSSLValidation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${security.enable.ssl.truststore.internal}")
/*     */   private String enableInternatlSSLTruststore;
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
/*     */   @Autowired
/*     */   public SSLValidation(Logger logger) {
/*  67 */     this.logger = logger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void configure() {
/*  74 */     if (Boolean.parseBoolean(this.disableSSLValidation)) {
/*  75 */       disableSSLValidation();
/*  76 */     } else if (Boolean.parseBoolean(this.enableInternatlSSLTruststore)) {
/*  77 */       enableInternalSSLTruststore();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void enableInternalSSLTruststore() {
/*  82 */     this.logger.log(Level.INFO, "000505", "Enabling internal SSL truststore.", CLASS_NAME);
/*     */     try {
/*  84 */       SSLContext sslContext = (new SSLClassPathTrustStoreLoader(this.logger)).provideSSLContext();
/*  85 */       HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
/*  86 */     } catch (KeyManagementException|java.security.UnrecoverableKeyException|NoSuchAlgorithmException|java.security.KeyStoreException|java.security.cert.CertificateException|java.io.IOException e) {
/*     */       
/*  88 */       this.logger.logToFileOnly(CLASS_NAME, "Enabling internal SSL truststore failed", e);
/*  89 */       this.logger.log(Level.WARNING, "000506X", "Enabling internal SSL truststore failed - further SSLHandshake Exceptions could result due to this problem: " + e
/*     */           
/*  91 */           .getMessage(), CLASS_NAME);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void disableSSLValidation() {
/* 102 */     TrustManager[] trustAllCerts = { new X509TrustManager() {
/*     */           public X509Certificate[] getAcceptedIssuers() {
/* 104 */             return new X509Certificate[0];
/*     */           }
/*     */           
/*     */           public void checkClientTrusted(X509Certificate[] certs, String authType) {
/* 108 */             SSLValidation.this.logger.log(Level.INFO, "000689", "No check for server so that validation passes", SSLValidation.CLASS_NAME);
/*     */           }
/*     */ 
/*     */           
/*     */           public void checkServerTrusted(X509Certificate[] certs, String authType) {
/* 113 */             SSLValidation.this.logger.log(Level.INFO, "000689", "No check for server so that validation passes", SSLValidation.CLASS_NAME);
/*     */           }
/*     */         } };
/*     */ 
/*     */     
/*     */     try {
/* 119 */       SSLContext sc = SSLContext.getInstance("SSL");
/* 120 */       sc.init(null, trustAllCerts, new SecureRandom());
/* 121 */       HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
/*     */ 
/*     */       
/* 124 */       HostnameVerifier allHostsValid = (hostname, session) -> true;
/*     */       
/* 126 */       HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
/*     */     }
/* 128 */     catch (NoSuchAlgorithmException|KeyManagementException e) {
/* 129 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 130 */       this.logger.log(Level.WARNING, "000258X", "Could not ignore SSL certificates.", CLASS_NAME);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\security\SSLValidation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */