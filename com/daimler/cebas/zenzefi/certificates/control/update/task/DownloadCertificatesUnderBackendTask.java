/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*     */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateSteps;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateTask;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKICertificateRequest;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKICertificateResponse;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*     */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.logging.Level;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
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
/*     */ public class DownloadCertificatesUnderBackendTask
/*     */   extends UpdateTask<ZenZefiPublicKeyInfrastructureEsi>
/*     */ {
/*  45 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadCertificatesUnderBackendTask.class.getSimpleName();
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public DownloadCertificatesUnderBackendTask(ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, UpdateSession updateSession, Logger logger, MetadataManager i18n) {
/*  51 */     super((PublicKeyInfrastructureEsi)publicKeyInfrastructureEsi, importCertificatesEngine, (DefaultUpdateSession)updateSession, logger, i18n);
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
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public void execute(List<PKICertificateRequest> pkiCertificateRequests, UpdateType updateType) {
/*  64 */     if (this.updateSession.isRunning()) {
/*  65 */       this.updateSession.updateStep(UpdateSteps.DOWNLOAD_DIAGNOSTIC_CERTIFICATES, "updateStartDownloadCertificates", updateType);
/*     */       
/*  67 */       this.logger.log(Level.INFO, "000239", this.i18n
/*  68 */           .getEnglishMessage("updateStartDownloadCertificates", new String[] {
/*  69 */               updateType.name()
/*     */             }), CLASS_NAME);
/*  71 */       processDiagnosticCertificates(pkiCertificateRequests, updateType);
/*  72 */       processOtherCertificates(pkiCertificateRequests, updateType);
/*  73 */       this.updateSession.updateStep(UpdateSteps.DOWNLOAD_OTHER_CERTIFICATES, "updateStopDownloadCertificates", updateType);
/*     */       
/*  75 */       this.logger.log(Level.INFO, "000240", this.i18n
/*  76 */           .getEnglishMessage("updateStopDownloadCertificates", new String[] {
/*  77 */               updateType.name()
/*     */             }), CLASS_NAME);
/*  79 */       this.updateSession.resetRetries();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processDiagnosticCertificates(List<PKICertificateRequest> pkiCertificateRequests, UpdateType updateType) {
/*  90 */     if (this.updateSession.isRunning()) {
/*     */ 
/*     */       
/*  93 */       List<PKICertificateRequest> diagnosticsPkiCertificateRequests = (List<PKICertificateRequest>)pkiCertificateRequests.stream().filter(r -> r.getCertificateType().equals(CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE.name())).collect(Collectors.toList());
/*  94 */       if (!diagnosticsPkiCertificateRequests.isEmpty()) {
/*  95 */         this.updateSession.updateStep(UpdateSteps.DOWNLOAD_DIAGNOSTIC_CERTIFICATES, "updateRequestingDiagnosticCert", updateType);
/*     */         
/*  97 */         this.logger.log(Level.INFO, "000247", this.i18n
/*  98 */             .getEnglishMessage("updateRequestingDiagnosticCert", new String[] {
/*  99 */                 updateType.name()
/*     */               }), CLASS_NAME);
/*     */         
/* 102 */         diagnosticsPkiCertificateRequests.forEach(request -> downloadAndImportDiagnosticCertificate(request, updateType));
/*     */         
/* 104 */         this.logger.log(Level.INFO, "000249", this.i18n.getEnglishMessage("updateReceiveDiagnosticCert", new String[] { updateType
/* 105 */                 .name() }), CLASS_NAME);
/* 106 */         this.updateSession.updateStep(UpdateSteps.DOWNLOAD_DIAGNOSTIC_CERTIFICATES, "updateReceiveDiagnosticCert", updateType);
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
/*     */   private void processOtherCertificates(List<PKICertificateRequest> pkiCertificateRequests, UpdateType updateType) {
/* 119 */     if (this.updateSession.isRunning()) {
/*     */ 
/*     */       
/* 122 */       List<PKICertificateRequest> otherPkiCertificateRequests = (List<PKICertificateRequest>)pkiCertificateRequests.stream().filter(r -> !r.getCertificateType().equals(CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE.name())).collect(Collectors.toList());
/* 123 */       if (!otherPkiCertificateRequests.isEmpty()) {
/* 124 */         this.updateSession.updateStep(UpdateSteps.DOWNLOAD_OTHER_CERTIFICATES, "updateRequestingRemainingCert", updateType);
/*     */         
/* 126 */         this.logger.log(Level.INFO, "000251", this.i18n.getEnglishMessage("updateRequestingRemainingCert", new String[] { updateType
/* 127 */                 .name() }), CLASS_NAME);
/*     */         
/* 129 */         otherPkiCertificateRequests.forEach(request -> downloadAndImportOtherCertificate(request, updateType));
/*     */         
/* 131 */         this.logger.log(Level.INFO, "000253", this.i18n.getEnglishMessage("updateReceivingRemainingCert", new String[] { updateType
/* 132 */                 .name() }), CLASS_NAME);
/* 133 */         this.updateSession.updateStep(UpdateSteps.DOWNLOAD_OTHER_CERTIFICATES, "updateStopDownloadCertificates", updateType);
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
/*     */   private void downloadAndImportDiagnosticCertificate(PKICertificateRequest diagnosticsPkiCertificateRequest, UpdateType updateType) {
/* 146 */     if (this.updateSession.isRunning()) {
/* 147 */       this.logger.log(Level.FINE, "000248", this.i18n
/* 148 */           .getEnglishMessage("updateRequestDiagForCSR", new String[] {
/* 149 */               updateType.name(), diagnosticsPkiCertificateRequest.toString()
/*     */             }), CLASS_NAME);
/*     */ 
/*     */       
/* 153 */       List<PKICertificateResponse> pkiCertificates = ((ZenZefiPublicKeyInfrastructureEsi)this.publicKeyInfrastructureEsi).downloadCertificates(Collections.singletonList(diagnosticsPkiCertificateRequest));
/*     */       
/* 155 */       this.logger.log(Level.FINE, "000250", this.i18n.getEnglishMessage("updateReceivedDiagnosticCert", new String[] { updateType
/* 156 */               .name(), pkiCertificates.toString() }), CLASS_NAME);
/*     */       
/* 158 */       this.importCertificatesEngine.importCertificatesFromBase64NewTransaction((List)pkiCertificates.stream().filter(Objects::nonNull)
/* 159 */           .map(PKICertificateResponse::getCertificate).collect(Collectors.toList()), true, true);
/* 160 */       this.updateSession.resetRetries();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void downloadAndImportOtherCertificate(PKICertificateRequest otherPkiCertificateRequest, UpdateType updateType) {
/* 171 */     if (this.updateSession.isRunning()) {
/* 172 */       this.logger.log(Level.FINE, "000252", this.i18n.getEnglishMessage("updateRequestRemainingCertsForCSR", new String[] { updateType
/* 173 */               .name(), otherPkiCertificateRequest.toString() }), CLASS_NAME);
/*     */ 
/*     */       
/* 176 */       List<PKICertificateResponse> pkiCertificates = ((ZenZefiPublicKeyInfrastructureEsi)this.publicKeyInfrastructureEsi).downloadCertificates(Collections.singletonList(otherPkiCertificateRequest));
/*     */       
/* 178 */       this.logger.log(Level.FINE, "000254", this.i18n.getEnglishMessage("updateReceivedRemainingCert", new String[] { updateType
/* 179 */               .name(), pkiCertificates.toString() }), CLASS_NAME);
/*     */       
/* 181 */       this.importCertificatesEngine.importCertificatesFromBase64NewTransaction((List)pkiCertificates
/* 182 */           .stream().map(PKICertificateResponse::getCertificate).collect(Collectors.toList()), true, true);
/*     */       
/* 184 */       this.updateSession.resetRetries();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\DownloadCertificatesUnderBackendTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */