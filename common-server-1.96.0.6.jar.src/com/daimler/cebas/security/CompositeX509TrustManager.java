/*     */ package com.daimler.cebas.security;
/*     */ 
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompositeX509TrustManager
/*     */   implements X509TrustManager
/*     */ {
/*     */   private final List<X509TrustManager> trustManagers;
/*     */   private final Logger logger;
/*     */   private final String CLASS_NAME;
/*     */   
/*     */   public CompositeX509TrustManager(Logger logger, X509TrustManager... trustManagers) {
/*  55 */     this.logger = logger;
/*  56 */     this.trustManagers = Arrays.asList(trustManagers);
/*  57 */     this.CLASS_NAME = getClass().getSimpleName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
/*  65 */     List<CertificateException> exceptions = new ArrayList<>();
/*  66 */     for (X509TrustManager trustManager : this.trustManagers) {
/*     */       try {
/*  68 */         trustManager.checkClientTrusted(chain, authType);
/*     */         return;
/*  70 */       } catch (CertificateException e) {
/*     */         
/*  72 */         exceptions.add(e);
/*     */       } 
/*     */     } 
/*  75 */     this.logger.log(Level.INFO, "000509X", "SSL client trust check did not succeed. Please see log for a list of all problems.", this.CLASS_NAME);
/*     */     
/*  77 */     for (CertificateException e : exceptions) {
/*  78 */       this.logger.logToFileOnly(this.CLASS_NAME, "SSL client trust check did not succeed.", e);
/*     */     }
/*  80 */     throw new CertificateException("None of the TrustManagers trust this certificate chain. See log file for details.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
/*  89 */     List<CertificateException> exceptions = new ArrayList<>();
/*  90 */     for (X509TrustManager trustManager : this.trustManagers) {
/*     */       try {
/*  92 */         trustManager.checkServerTrusted(chain, authType);
/*     */         return;
/*  94 */       } catch (CertificateException e) {
/*     */         
/*  96 */         exceptions.add(e);
/*     */       } 
/*     */     } 
/*  99 */     this.logger.log(Level.INFO, "000510X", "SSL server trust check did not succeed. Please see log for a list of all problems.", this.CLASS_NAME);
/*     */     
/* 101 */     for (CertificateException e : exceptions) {
/* 102 */       this.logger.logToFileOnly(this.CLASS_NAME, "SSL server trust check did not succeed.", e);
/*     */     }
/* 104 */     throw new CertificateException("None of the TrustManagers trust this certificate chain. See log file for details.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public X509Certificate[] getAcceptedIssuers() {
/* 113 */     X509Certificate[] certificates = new X509Certificate[0];
/* 114 */     for (X509TrustManager trustManager : this.trustManagers) {
/* 115 */       certificates = (X509Certificate[])ArrayUtils.addAll((Object[])certificates, (Object[])trustManager.getAcceptedIssuers());
/*     */     }
/* 117 */     return certificates;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\security\CompositeX509TrustManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */