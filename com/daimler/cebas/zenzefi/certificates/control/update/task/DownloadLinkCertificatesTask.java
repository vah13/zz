/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.DeleteCertificatesEngine;
/*     */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*     */ import com.daimler.cebas.certificates.control.SearchEngine;
/*     */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateSteps;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateTask;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*     */ import com.daimler.cebas.certificates.control.update.task.UpdateRootAndBackendsResult;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificatePKIState;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKILinkCertificateResponse;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.zenzefi.certificates.entity.ZenZefiCertificate;
/*     */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ @CEBASControl
/*     */ public class DownloadLinkCertificatesTask
/*     */   extends UpdateTask<ZenZefiPublicKeyInfrastructureEsi>
/*     */ {
/*  46 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadLinkCertificatesTask.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Session session;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SearchEngine searchEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DeleteCertificatesEngine deleteCertificatesEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public DownloadLinkCertificatesTask(ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, DefaultUpdateSession updateSession, Logger logger, MetadataManager i18n, Session session, SearchEngine searchEngine, DeleteCertificatesEngine deleteCertificatesEngine) {
/*  78 */     super((PublicKeyInfrastructureEsi)publicKeyInfrastructureEsi, importCertificatesEngine, updateSession, logger, i18n);
/*  79 */     this.session = session;
/*  80 */     this.searchEngine = searchEngine;
/*  81 */     this.deleteCertificatesEngine = deleteCertificatesEngine;
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
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public void execute(UpdateType updateType, UpdateRootAndBackendsResult updatedRoots, UpdateRootAndBackendsResult updatedBackends) {
/*  96 */     if (this.updateSession.isRunning()) {
/*  97 */       this.updateSession.updateStep(UpdateSteps.DOWNLOAD_LINK_CERTIFICATES, "updateStartDownloadLinkCertificates", updateType);
/*  98 */       this.logger.log(Level.INFO, "000558", this.i18n.getEnglishMessage("updateStartDownloadLinkCertificates", new String[] { updateType
/*  99 */               .name() }), CLASS_NAME);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 104 */       List<String> activeRootSKIs = (List<String>)updatedRoots.getUpdatedRootAndBackends().stream().filter(root -> CertificatePKIState.ACTIVE.equals(root.getPkiState())).map(Certificate::getSubjectKeyIdentifier).collect(Collectors.toList());
/*     */ 
/*     */ 
/*     */       
/* 108 */       List<String> activeBackendSKIs = (List<String>)updatedBackends.getUpdatedRootAndBackends().stream().filter(backend -> CertificatePKIState.ACTIVE.equals(backend.getPkiState())).map(Certificate::getSubjectKeyIdentifier).collect(Collectors.toList());
/* 109 */       deleteLinkCertificates(activeBackendSKIs, CertificateType.BACKEND_CA_LINK_CERTIFICATE);
/* 110 */       deleteLinkCertificates(activeRootSKIs, CertificateType.ROOT_CA_LINK_CERTIFICATE);
/*     */       
/* 112 */       List<String> linkCertificates = new ArrayList<>();
/* 113 */       activeRootSKIs.forEach(ski -> linkCertificates.addAll(downloadLinkCertificates(ski)));
/* 114 */       activeBackendSKIs.forEach(ski -> linkCertificates.addAll(downloadLinkCertificates(ski)));
/* 115 */       if (!linkCertificates.isEmpty()) {
/* 116 */         this.importCertificatesEngine.importCertificatesFromBase64NewTransaction(linkCertificates, true, false);
/*     */       } else {
/* 118 */         this.logger.log(Level.INFO, "000564", this.i18n.getEnglishMessage("noLinkCertsFoundZenZefi", new String[] { updateType
/* 119 */                 .name() }), CLASS_NAME);
/*     */       } 
/*     */       
/* 122 */       this.updateSession.updateStep(UpdateSteps.DOWNLOAD_LINK_CERTIFICATES, "updateStopDownloadLinkCertificates", updateType);
/*     */       
/* 124 */       this.logger.log(Level.INFO, "000560", this.i18n.getEnglishMessage("updateStopDownloadLinkCertificates", new String[] { updateType
/* 125 */               .name() }), CLASS_NAME);
/* 126 */       this.updateSession.resetRetries();
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
/*     */   private void deleteLinkCertificates(List<String> subjectKeyIds, CertificateType certificateType) {
/* 138 */     List<ZenZefiCertificate> links = this.searchEngine.findCertificatesByType(ZenZefiCertificate.class, this.session.getCurrentUser(), certificateType);
/*     */ 
/*     */     
/* 141 */     Set<String> linkCertsToDelete = new HashSet<>();
/* 142 */     subjectKeyIds.forEach(sKId -> linkCertsToDelete.addAll((Collection)links.stream().filter(()).map(Certificate::getEntityId).collect(Collectors.toList())));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     if (!linkCertsToDelete.isEmpty()) {
/* 151 */       this.deleteCertificatesEngine.deleteCertificatesWithNewTransaction(linkCertsToDelete, this.session);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> downloadLinkCertificates(String activeCa) {
/* 162 */     if (this.updateSession.isRunning()) {
/* 163 */       List<PKILinkCertificateResponse> result = new ArrayList<>();
/*     */       
/* 165 */       result.addAll(((ZenZefiPublicKeyInfrastructureEsi)this.publicKeyInfrastructureEsi).downloadLinkCertificatesForActiveSKI(activeCa, this.updateSession.isRunning()));
/* 166 */       result.addAll(((ZenZefiPublicKeyInfrastructureEsi)this.publicKeyInfrastructureEsi).downloadLinkCertificatesForActiveAKI(activeCa, this.updateSession.isRunning()));
/*     */       
/* 168 */       this.updateSession.resetRetries();
/* 169 */       return (List<String>)result.stream().map(PKILinkCertificateResponse::getLinkCert).collect(Collectors.toList());
/*     */     } 
/* 171 */     return Collections.emptyList();
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\DownloadLinkCertificatesTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */