/*    */ package com.daimler.cebas.certificates.control.config;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.DeleteCertificateHandlerDefault;
/*    */ import com.daimler.cebas.certificates.control.DeleteCertificatesEngine;
/*    */ import com.daimler.cebas.certificates.control.config.handlers.IDeleteCertificateHandler;
/*    */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
/*    */ import com.daimler.cebas.certificates.integration.vo.Permission;
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
/*    */ public interface CertificatesDeleteConfiguration
/*    */ {
/*    */   default IDeleteCertificateHandler getDeleteCertificatesHandler(DeleteCertificatesEngine engine, Logger logger, MetadataManager i18n) {
/* 27 */     return (IDeleteCertificateHandler)new DeleteCertificateHandlerDefault(engine, logger, i18n);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean shouldDeleteDuringCSRCreation(Certificate certificate) {
/* 36 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean matchEnrollmentId(Certificate certificate, Permission permission) {
/* 47 */     return false;
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
/*    */   default void logDeletedUnauthorizedCertificate(Certificate certificate, UpdateType updateType, Logger logger, MetadataManager i18n) {
/* 59 */     logger.log(Level.INFO, "000231", i18n
/* 60 */         .getEnglishMessage("updateDeleteCertWithAKIAndSN", new String[] { updateType.name(), certificate
/* 61 */             .getAuthorityKeyIdentifier(), certificate.getSerialNo()
/* 62 */           }), getClass().getSimpleName());
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\config\CertificatesDeleteConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */