/*     */ package com.daimler.cebas.certificates.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.CertificateCryptoEngine;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CertificateSigningException;
/*     */ import com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.entity.UserKeyPair;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.util.Arrays;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SignCodingDataEngine
/*     */ {
/*  30 */   private static final Logger LOG = Logger.getLogger(SignCodingDataEngine.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SearchEngine searchEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractCertificateFactory factory;
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
/*     */   protected Logger logger;
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
/*     */   public SignCodingDataEngine(SearchEngine searchEngine, AbstractCertificateFactory factory, Session session, Logger logger, MetadataManager i18n) {
/*  73 */     this.searchEngine = searchEngine;
/*  74 */     this.factory = factory;
/*  75 */     this.session = session;
/*  76 */     this.logger = logger;
/*  77 */     this.i18n = i18n;
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
/*     */   protected byte[] signDataWithCertificatePrivateKey(Certificate certificate, byte[] codingDataRaw) {
/*  90 */     UserKeyPair userKeyPair = this.searchEngine.getUserKeyPairForCertificate(this.session.getCurrentUser(), certificate);
/*  91 */     return signDataWithPrivateKey(userKeyPair, codingDataRaw);
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
/*     */   public byte[] signDataWithPrivateKey(UserKeyPair userKeyPair, byte[] codingDataRaw) {
/* 104 */     byte[] decodedPrivateKey = new byte[0];
/*     */     try {
/* 106 */       decodedPrivateKey = this.session.getCryptoEngine().decodePrivateKeyToByteArray(this.session.getContainerKey(), userKeyPair
/* 107 */           .getPrivateKey());
/*     */       
/* 109 */       return CertificateCryptoEngine.signDataWithPrivateKey(decodedPrivateKey, codingDataRaw);
/* 110 */     } catch (GeneralSecurityException e) {
/* 111 */       LOG.log(Level.FINEST, e.getMessage(), e);
/*     */       
/* 113 */       CertificateSigningException ex = new CertificateSigningException(this.i18n.getMessage("signChallangeWithPrivateKeyFailed"), "signChallangeWithPrivateKeyFailed");
/*     */       
/* 115 */       this.logger.logWithTranslation(Level.WARNING, "000074X", ex.getMessageId(), new String[] { e
/* 116 */             .getMessage() }, ex.getClass().getSimpleName());
/* 117 */       throw ex;
/*     */     } finally {
/* 119 */       Arrays.fill(decodedPrivateKey, (byte)0);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\SignCodingDataEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */