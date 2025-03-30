/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*    */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*    */ import com.daimler.cebas.certificates.control.update.UpdateSteps;
/*    */ import com.daimler.cebas.certificates.control.update.UpdateTask;
/*    */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*    */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*    */ import com.daimler.cebas.certificates.integration.vo.PKICertificateResponse;
/*    */ import com.daimler.cebas.certificates.integration.vo.PKIEnhancedCertificateRequest;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*    */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.logging.Level;
/*    */ import java.util.stream.Collectors;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.transaction.annotation.Propagation;
/*    */ import org.springframework.transaction.annotation.Transactional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASControl
/*    */ public class DownloadEnhancedCertificatesTask
/*    */   extends UpdateTask<ZenZefiPublicKeyInfrastructureEsi>
/*    */ {
/* 35 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadEnhancedCertificatesTask.class.getSimpleName();
/*    */ 
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   public DownloadEnhancedCertificatesTask(ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, UpdateSession updateSession, Logger logger, MetadataManager i18n) {
/* 41 */     super((PublicKeyInfrastructureEsi)publicKeyInfrastructureEsi, importCertificatesEngine, (DefaultUpdateSession)updateSession, logger, i18n);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*    */   public void execute(List<PKIEnhancedCertificateRequest> enhCertificateRequests, UpdateType updateType) {
/* 51 */     if (this.updateSession.isRunning()) {
/* 52 */       this.updateSession.updateStep(UpdateSteps.DOWNLOAD_ENHANCED_CERTIFICATES, "updateStartDownloadEnhancedCSR", updateType);
/* 53 */       this.logger.log(Level.INFO, "000263", this.i18n
/* 54 */           .getEnglishMessage("updateStartDownloadEnhancedCSR", new String[] {
/* 55 */               updateType.name()
/*    */             }), CLASS_NAME);
/*    */       
/* 58 */       this.logger.log(Level.INFO, "000264", this.i18n.getEnglishMessage("updateRequestingEnhancedCSR", new String[] { updateType
/* 59 */               .name() }), CLASS_NAME);
/* 60 */       this.logger.log(Level.FINE, "000265", this.i18n.getEnglishMessage("updateRequestEnhancedCSR", new String[] { updateType
/* 61 */               .name(), enhCertificateRequests.toString() }), CLASS_NAME);
/*    */ 
/*    */       
/* 64 */       List<PKICertificateResponse> enhPkiCertificates = (List<PKICertificateResponse>)enhCertificateRequests.stream().map(this::downloadEhhRights).flatMap(Collection::stream).collect(Collectors.toList());
/*    */       
/* 66 */       this.logger.log(Level.INFO, "000266", this.i18n.getEnglishMessage("updateReceivingEnhancedRightsCert", new String[] { updateType
/* 67 */               .name() }), CLASS_NAME);
/* 68 */       this.logger.log(Level.FINE, "000267", this.i18n.getEnglishMessage("updateReceivedEnhancedRightsCert", new String[] { updateType
/* 69 */               .name(), enhPkiCertificates.toString() }), CLASS_NAME);
/*    */       
/* 71 */       this.importCertificatesEngine.importCertificatesFromBase64NewTransaction((List)enhPkiCertificates.stream()
/* 72 */           .map(PKICertificateResponse::getCertificate).collect(Collectors.toList()), true, true);
/*    */       
/* 74 */       this.updateSession.updateStep(UpdateSteps.DOWNLOAD_ENHANCED_CERTIFICATES, "updateStopDownloadEnhancedRightsCert", updateType);
/* 75 */       this.updateSession.addStepResult(UpdateSteps.DOWNLOAD_ENHANCED_CERTIFICATES, enhPkiCertificates);
/* 76 */       this.logger.log(Level.INFO, "000268", this.i18n
/* 77 */           .getEnglishMessage("updateStopDownloadEnhancedRightsCert", new String[] {
/* 78 */               updateType.name()
/*    */             }), CLASS_NAME);
/* 80 */       this.updateSession.resetRetries();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private List<PKICertificateResponse> downloadEhhRights(PKIEnhancedCertificateRequest request) {
/* 91 */     if (this.updateSession.isRunning()) {
/*    */       
/* 93 */       List<PKICertificateResponse> downloadEnhancedRightsCertificates = ((ZenZefiPublicKeyInfrastructureEsi)this.publicKeyInfrastructureEsi).downloadEnhancedRightsCertificates(Collections.singletonList(request), true);
/* 94 */       this.updateSession.resetRetries();
/* 95 */       return downloadEnhancedRightsCertificates;
/*    */     } 
/* 97 */     return Collections.emptyList();
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\DownloadEnhancedCertificatesTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */