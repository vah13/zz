/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.DeleteCertificatesEngine;
/*     */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*     */ import com.daimler.cebas.certificates.control.exceptions.DownloadRightsException;
/*     */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateSteps;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateTask;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*     */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKICertificateRequest;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKIEnhancedCertificateRequest;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.CollectTimeAndSecocisCSRsTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadSecOCISCertificateTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadTimeCertificateTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASControl
/*     */ public class DownloadTimeAndSecocisTask
/*     */   extends UpdateTask<ZenZefiPublicKeyInfrastructureEsi>
/*     */ {
/*     */   private static final String WAS_DELETED_BECAUSE_THE_USER_DOES_NOT_HAVE_THE_RIGHTS_TO_DOWNLOAD_CERTIFICATE_BASED_ON_THE_SECOCIS_REQUEST = " was deleted because the  user does not have the rights to download certificate based on the secocis request: ";
/*     */   private DownloadTimeCertificateTask downloadTimeTask;
/*     */   private DownloadSecOCISCertificateTask downloadSecocisTask;
/*     */   private DeleteCertificatesEngine deleteEngine;
/*     */   
/*     */   public DownloadTimeAndSecocisTask(ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, UpdateSession updateSession, Logger logger, MetadataManager i18n, DownloadTimeCertificateTask downloadTimeTask, DownloadSecOCISCertificateTask downloadSecocsiTask, DeleteCertificatesEngine deleteEngine) {
/*  73 */     super((PublicKeyInfrastructureEsi)publicKeyInfrastructureEsi, importCertificatesEngine, (DefaultUpdateSession)updateSession, logger, i18n);
/*  74 */     this.downloadTimeTask = downloadTimeTask;
/*  75 */     this.downloadSecocisTask = downloadSecocsiTask;
/*  76 */     this.deleteEngine = deleteEngine;
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
/*     */   public void execute(List<PKICertificateRequest> timeRequests, List<PKIEnhancedCertificateRequest> pkiSecocisRequests, UpdateType updateType) {
/*  91 */     if (this.updateSession.isRunning()) {
/*  92 */       if (!timeRequests.isEmpty()) {
/*  93 */         this.updateSession.updateStep(UpdateSteps.DOWNLOAD_TIME_AND_SECOCSI_CERTIFICATE_USING_CSRS_NOT_CREATED_BASED_ON_PERMISSIONS, "dowloadingTimeCertificateUsingCSRsNotCreateBasedOnPermissionsStep", updateType);
/*     */ 
/*     */ 
/*     */         
/*  97 */         this.logger.log(Level.INFO, "000355", this.i18n.getEnglishMessage("dowloadingTimeCertificateUsingCSRsNotCreateBasedOnPermissionsStep", new String[] { updateType
/*     */                 
/*  99 */                 .name() }), CollectTimeAndSecocisCSRsTask.class.getSimpleName());
/* 100 */         timeRequests.forEach(this::downloadTime);
/*     */       } 
/* 102 */       if (!pkiSecocisRequests.isEmpty()) {
/* 103 */         this.updateSession.updateStep(UpdateSteps.DOWNLOAD_TIME_AND_SECOCSI_CERTIFICATE_USING_CSRS_NOT_CREATED_BASED_ON_PERMISSIONS, "dowloadingSecocisCertificateUsingCSRsNotCreateBasedOnPermissionsStep", updateType);
/*     */ 
/*     */ 
/*     */         
/* 107 */         this.logger.log(Level.INFO, "000355", this.i18n.getEnglishMessage("dowloadingSecocisCertificateUsingCSRsNotCreateBasedOnPermissionsStep", new String[] { updateType
/*     */                 
/* 109 */                 .name() }), CollectTimeAndSecocisCSRsTask.class.getSimpleName());
/* 110 */         pkiSecocisRequests.forEach(this::downloadSecocis);
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
/*     */   private void downloadSecocis(PKIEnhancedCertificateRequest request) {
/*     */     try {
/* 123 */       this.downloadSecocisTask.executeWithRetry(request);
/* 124 */     } catch (DownloadRightsException e) {
/* 125 */       this.logger.log(Level.WARNING, "000357", "CSR with id:" + request.getInternalCSRId() + " was deleted because the  user does not have the rights to download certificate based on the secocis request: " + request
/*     */           
/* 127 */           .toString(), CollectTimeAndSecocisCSRsTask.class.getSimpleName());
/* 128 */       this.deleteEngine.deleteCertificates(Arrays.asList(new String[] { request.getInternalCSRId() }));
/*     */     } 
/* 130 */     this.updateSession.resetRetries();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void downloadTime(PKICertificateRequest request) {
/*     */     try {
/* 141 */       this.downloadTimeTask.executeWithRetry(request);
/* 142 */     } catch (DownloadRightsException e) {
/* 143 */       this.logger.log(Level.WARNING, "000357", "CSR with id:" + request.getInternalCSRId() + " was deleted because the  user does not have the rights to download certificate based on the secocis request: " + request
/*     */           
/* 145 */           .toString(), CollectTimeAndSecocisCSRsTask.class.getSimpleName());
/* 146 */       this.deleteEngine.deleteCertificates(Arrays.asList(new String[] { request.getInternalCSRId() }));
/*     */     } 
/* 148 */     this.updateSession.resetRetries();
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\DownloadTimeAndSecocisTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */