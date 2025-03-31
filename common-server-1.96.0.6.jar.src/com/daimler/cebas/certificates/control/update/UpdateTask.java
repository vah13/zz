/*    */ package com.daimler.cebas.certificates.control.update;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.CertificateUtil;
/*    */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
/*    */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import java.util.logging.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class UpdateTask<T extends PublicKeyInfrastructureEsi>
/*    */ {
/*    */   protected T publicKeyInfrastructureEsi;
/*    */   protected ImportCertificatesEngine importCertificatesEngine;
/*    */   protected DefaultUpdateSession updateSession;
/*    */   protected Logger logger;
/*    */   protected MetadataManager i18n;
/*    */   
/*    */   public UpdateTask(T publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, DefaultUpdateSession updateSession, Logger logger, MetadataManager i18n) {
/* 66 */     this.publicKeyInfrastructureEsi = publicKeyInfrastructureEsi;
/* 67 */     this.importCertificatesEngine = importCertificatesEngine;
/* 68 */     this.updateSession = updateSession;
/* 69 */     this.logger = logger;
/* 70 */     this.i18n = i18n;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isCertificateValidityExceeded(UpdateType updateType, Certificate certificate, String renewal, String className) {
/* 83 */     boolean result = CertificateUtil.isCertificateValidityExceedingMinimumRenewal(certificate, renewal);
/*    */     
/* 85 */     if (!result) {
/* 86 */       this.logger.log(Level.FINER, "000278", this.i18n
/* 87 */           .getEnglishMessage("updateDidNotCreateCSRCertStillValid", new String[] {
/* 88 */               updateType.name(), certificate.getAuthorityKeyIdentifier(), certificate.getSerialNo()
/*    */             }), className);
/*    */     }
/* 91 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\contro\\update\UpdateTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */