/*    */ package com.daimler.cebas.certificates.control.update.task;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*    */ import com.daimler.cebas.certificates.control.SearchEngine;
/*    */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*    */ import com.daimler.cebas.certificates.control.update.UpdateTask;
/*    */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*    */ import com.daimler.cebas.certificates.control.vo.DownloadBackendsResult;
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
/*    */ import com.daimler.cebas.certificates.entity.CertificateType;
/*    */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*    */ import com.daimler.cebas.certificates.integration.vo.BackendIdentifier;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.logging.Level;
/*    */ import org.springframework.transaction.annotation.Propagation;
/*    */ import org.springframework.transaction.annotation.Transactional;
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
/*    */ public abstract class AbstractUpdateEcuLinkMarkersTask<T extends PublicKeyInfrastructureEsi>
/*    */   extends UpdateTask<T>
/*    */ {
/*    */   private Session session;
/*    */   private SearchEngine searchEngine;
/*    */   private String className;
/*    */   
/*    */   public AbstractUpdateEcuLinkMarkersTask(T publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, DefaultUpdateSession updateSession, Logger logger, MetadataManager i18n, Session session, SearchEngine searchEngine, String className) {
/* 43 */     super((PublicKeyInfrastructureEsi)publicKeyInfrastructureEsi, importCertificatesEngine, updateSession, logger, i18n);
/* 44 */     this.session = session;
/* 45 */     this.searchEngine = searchEngine;
/* 46 */     this.className = className;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*    */   public void execute(Map<CertificateType, UpdateRootAndBackendsResult> resultMap, DownloadBackendsResult backendsResult, UpdateType updateType) {
/* 58 */     if (this.updateSession.ecuResetCondition().booleanValue() && this.updateSession.linkResetCondition().booleanValue()) {
/* 59 */       this.logger.log(Level.INFO, "000542", this.i18n.getEnglishMessage("resetMarkersOnEcuLink", new String[] { updateType.name() }), this.className);
/*    */       
/*    */       return;
/*    */     } 
/* 63 */     List<Certificate> backendPersistentCertificates = this.searchEngine.findCertificatesByType(Certificate.class, this.session.getCurrentUser(), CertificateType.BACKEND_CA_CERTIFICATE);
/* 64 */     List<Certificate> ecuBackendsFromIdentifiers = ((UpdateRootAndBackendsResult)resultMap.get(CertificateType.ECU_CERTIFICATE)).getUpdatedRootAndBackends();
/* 65 */     List<Certificate> rootLinksFromIdentifiers = ((UpdateRootAndBackendsResult)resultMap.get(CertificateType.ROOT_CA_LINK_CERTIFICATE)).getUpdatedRootAndBackends();
/* 66 */     List<Certificate> backendLinksFromIdentifiers = ((UpdateRootAndBackendsResult)resultMap.get(CertificateType.BACKEND_CA_LINK_CERTIFICATE)).getUpdatedRootAndBackends();
/*    */     
/* 68 */     for (Certificate backendCertificate : backendPersistentCertificates) {
/* 69 */       if (needsToBeUpdated(backendCertificate, ecuBackendsFromIdentifiers) && !this.updateSession.ecuResetCondition().booleanValue()) {
/* 70 */         Optional<BackendIdentifier> matchingBackendIdentifier = backendsResult.getBackendIdentifierBasedOnSki(backendCertificate.getSubjectKeyIdentifier());
/* 71 */         if (matchingBackendIdentifier.isPresent()) {
/* 72 */           BackendIdentifier matchingCertFromIdentifier = matchingBackendIdentifier.get();
/* 73 */           backendCertificate.setEcuPackageTs(matchingCertFromIdentifier.getEcuPackageTs());
/*    */         } 
/*    */       } 
/* 76 */       if (needsToBeUpdated(backendCertificate, backendLinksFromIdentifiers) && !this.updateSession.linkResetCondition().booleanValue()) {
/* 77 */         Optional<BackendIdentifier> matchingBackendIdentifier = backendsResult.getBackendIdentifierBasedOnSki(backendCertificate.getSubjectKeyIdentifier());
/* 78 */         if (matchingBackendIdentifier.isPresent()) {
/* 79 */           BackendIdentifier matchingCertFromIdentifier = matchingBackendIdentifier.get();
/* 80 */           backendCertificate.setLinkCertTs(matchingCertFromIdentifier.getLinkCertTs());
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 85 */     List<Certificate> rootPersistentCertificates = this.searchEngine.findCertificatesByType(Certificate.class, this.session.getCurrentUser(), CertificateType.ROOT_CA_CERTIFICATE);
/* 86 */     for (Certificate rootCertificate : rootPersistentCertificates) {
/* 87 */       if (needsToBeUpdated(rootCertificate, rootLinksFromIdentifiers) && !this.updateSession.linkResetCondition().booleanValue()) {
/* 88 */         Optional<BackendIdentifier> matchingRootIdentifier = backendsResult.getBackendIdentifierBasedOnSki(rootCertificate.getSubjectKeyIdentifier());
/* 89 */         if (matchingRootIdentifier.isPresent()) {
/* 90 */           BackendIdentifier matchingCertFromIdentifier = matchingRootIdentifier.get();
/* 91 */           rootCertificate.setLinkCertTs(matchingCertFromIdentifier.getLinkCertTs());
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean needsToBeUpdated(Certificate backendCertificate, List<Certificate> backendUpdatedCertificatesFromIdentifiers) {
/* 98 */     return backendUpdatedCertificatesFromIdentifiers.stream().anyMatch(bi -> bi.getSubjectKeyIdentifier().equalsIgnoreCase(backendCertificate.getSubjectKeyIdentifier()));
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\contro\\update\task\AbstractUpdateEcuLinkMarkersTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */