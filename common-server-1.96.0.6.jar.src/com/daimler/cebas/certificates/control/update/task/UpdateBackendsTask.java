/*     */ package com.daimler.cebas.certificates.control.update.task;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.CertificateToolsProvider;
/*     */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*     */ import com.daimler.cebas.certificates.control.SearchEngine;
/*     */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*     */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateSteps;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateTask;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificatePKIState;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*     */ import com.daimler.cebas.certificates.integration.vo.BackendIdentifier;
/*     */ import com.daimler.cebas.common.control.HexUtil;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASControl
/*     */ public class UpdateBackendsTask<T extends PublicKeyInfrastructureEsi>
/*     */   extends UpdateTask<T>
/*     */ {
/*  38 */   private static final String CLASS_NAME = UpdateBackendsTask.class.getSimpleName();
/*     */ 
/*     */   
/*     */   private SearchEngine searchEngine;
/*     */ 
/*     */   
/*     */   private Session session;
/*     */ 
/*     */   
/*     */   private CertificatesConfiguration profileConfiguration;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public UpdateBackendsTask(CertificateToolsProvider toolsProvider, T publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, DefaultUpdateSession updateSession, Session session, Logger logger, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
/*  53 */     super((PublicKeyInfrastructureEsi)publicKeyInfrastructureEsi, importCertificatesEngine, updateSession, logger, i18n);
/*  54 */     this.searchEngine = toolsProvider.getSearchEngine();
/*  55 */     this.session = session;
/*  56 */     this.profileConfiguration = profileConfiguration;
/*     */   }
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public Map<CertificateType, UpdateRootAndBackendsResult> execute(UpdateType updateType, List<BackendIdentifier> backendIdentifiers) {
/*  61 */     if (this.updateSession.isNotRunning()) {
/*  62 */       Map<CertificateType, UpdateRootAndBackendsResult> emptyResult = new HashMap<>();
/*  63 */       UpdateRootAndBackendsResult emptyBResult = new UpdateRootAndBackendsResult();
/*  64 */       emptyResult.put(CertificateType.ECU_CERTIFICATE, emptyBResult);
/*  65 */       emptyResult.put(CertificateType.ROOT_CA_LINK_CERTIFICATE, emptyBResult);
/*  66 */       emptyResult.put(CertificateType.BACKEND_CA_LINK_CERTIFICATE, emptyBResult);
/*  67 */       emptyResult.put(CertificateType.NO_TYPE, emptyBResult);
/*  68 */       return emptyResult;
/*     */     } 
/*     */     
/*  71 */     this.updateSession.updateStep(UpdateSteps.UPDATE_BACKENDS, "updateStartUpdateBackendIdentifiers", updateType);
/*  72 */     this.logger.log(Level.INFO, "000542", this.i18n.getEnglishMessage("updateStartUpdateBackendIdentifiers", new String[] { updateType.name() }), CLASS_NAME);
/*     */     
/*  74 */     Map<CertificateType, UpdateRootAndBackendsResult> updateBackendsResult = updateBackends(updateType, backendIdentifiers);
/*     */     
/*  76 */     this.updateSession.updateStep(UpdateSteps.UPDATE_BACKENDS, "updateStopUpdateBackendIdentifiers", updateType);
/*  77 */     this.updateSession.addStepResult(UpdateSteps.UPDATE_BACKENDS, backendIdentifiers);
/*  78 */     this.logger.log(Level.INFO, "000545", this.i18n.getEnglishMessage("updateStopUpdateBackendIdentifiers", new String[] { updateType.name() }), CLASS_NAME);
/*  79 */     this.updateSession.resetRetries();
/*     */     
/*  81 */     return updateBackendsResult;
/*     */   }
/*     */   
/*     */   public Map<CertificateType, UpdateRootAndBackendsResult> updateBackends(UpdateType updateType, List<BackendIdentifier> backendIdentifiers) {
/*  85 */     Map<CertificateType, UpdateRootAndBackendsResult> resultMap = new HashMap<>();
/*  86 */     UpdateRootAndBackendsResult ecuUpdateBackendResults = new UpdateRootAndBackendsResult();
/*  87 */     UpdateRootAndBackendsResult rootLinkUpdateBackendResults = new UpdateRootAndBackendsResult();
/*  88 */     UpdateRootAndBackendsResult backendLinkUpdateBackendResults = new UpdateRootAndBackendsResult();
/*  89 */     UpdateRootAndBackendsResult allBackendResults = new UpdateRootAndBackendsResult();
/*     */     
/*  91 */     for (BackendIdentifier bi : backendIdentifiers) {
/*  92 */       String asEntityHexString = HexUtil.hexStringToSeparatedHexString(bi.getSubjectKeyIdentifier());
/*  93 */       CertificateType certificateType = "SUB_CA".equalsIgnoreCase(bi.getType()) ? CertificateType.BACKEND_CA_CERTIFICATE : CertificateType.ROOT_CA_CERTIFICATE;
/*  94 */       List<Certificate> findCertificatesBySkiAndType = this.searchEngine.findCertificatesBySkiAndType(this.session
/*  95 */           .getCurrentUser(), asEntityHexString, certificateType, Certificate.class);
/*     */       
/*  97 */       for (Certificate certificate : findCertificatesBySkiAndType) {
/*  98 */         if (CertificateType.BACKEND_CA_CERTIFICATE.equals(certificateType) && !this.profileConfiguration.getPkiKnownHandler().isPKIKnown(certificate)) {
/*     */           continue;
/*     */         }
/*     */         
/* 102 */         if (!StringUtils.equals(certificate.getPkiState().getValue(), bi.getPkiState())) {
/* 103 */           certificate.setPkiState(CertificatePKIState.getFromValue(bi.getPkiState()));
/*     */         }
/* 105 */         if (CertificateType.BACKEND_CA_CERTIFICATE.equals(certificateType)) {
/* 106 */           addBackendResultsForEcuUpdate(updateType, certificate, bi, ecuUpdateBackendResults);
/* 107 */           addBackendResultsForLinkUpdate(updateType, certificate, bi, backendLinkUpdateBackendResults);
/*     */         } else {
/* 109 */           addBackendResultsForLinkUpdate(updateType, certificate, bi, rootLinkUpdateBackendResults);
/*     */         } 
/* 111 */         allBackendResults.addUpdatedBackend(certificate);
/*     */       } 
/*     */     } 
/*     */     
/* 115 */     resultMap.put(CertificateType.ECU_CERTIFICATE, ecuUpdateBackendResults);
/* 116 */     resultMap.put(CertificateType.ROOT_CA_LINK_CERTIFICATE, rootLinkUpdateBackendResults);
/* 117 */     resultMap.put(CertificateType.BACKEND_CA_LINK_CERTIFICATE, backendLinkUpdateBackendResults);
/* 118 */     resultMap.put(CertificateType.NO_TYPE, allBackendResults);
/*     */     
/* 120 */     return resultMap;
/*     */   }
/*     */   private void addBackendResultsForEcuUpdate(UpdateType updateType, Certificate certificate, BackendIdentifier bi, UpdateRootAndBackendsResult result) {
/* 123 */     if (StringUtils.equals(certificate.getZkNo(), bi.getZkNo()) && 
/* 124 */       StringUtils.equals(certificate.getEcuPackageTs(), bi.getEcuPackageTs())) {
/* 125 */       this.logger.log(Level.INFO, "000543", this.i18n
/* 126 */           .getEnglishMessage("updateEcuBackendIdentifiersEquals", new String[] {
/* 127 */               updateType.name(), certificate.getSubjectKeyIdentifier(), certificate
/* 128 */               .getZkNo(), certificate.getEcuPackageTs() }), CLASS_NAME);
/*     */     } else {
/* 130 */       this.logger.log(Level.INFO, "000544", this.i18n
/* 131 */           .getEnglishMessage("updateEcuBackendIdentifiersChanged", new String[] {
/* 132 */               updateType.name(), certificate.getSubjectKeyIdentifier(), certificate
/* 133 */               .getZkNo(), bi.getZkNo(), certificate.getEcuPackageTs(), bi
/* 134 */               .getEcuPackageTs() }), CLASS_NAME);
/* 135 */       certificate.setZkNo(bi.getZkNo());
/* 136 */       result.addUpdatedBackend(certificate);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addBackendResultsForLinkUpdate(UpdateType updateType, Certificate certificate, BackendIdentifier bi, UpdateRootAndBackendsResult result) {
/* 141 */     if (StringUtils.equals(certificate.getLinkCertTs(), bi.getLinkCertTs())) {
/* 142 */       this.logger.log(Level.INFO, "000682", this.i18n
/* 143 */           .getEnglishMessage("updateLinkBackendIdentifiersEquals", new String[] {
/* 144 */               updateType.name(), certificate.getSubjectKeyIdentifier(), certificate
/* 145 */               .getLinkCertTs() }), CLASS_NAME);
/*     */     } else {
/* 147 */       this.logger.log(Level.INFO, "000683", this.i18n
/* 148 */           .getEnglishMessage("updateLinkBackendIdentifiersChanged", new String[] {
/* 149 */               updateType.name(), certificate.getSubjectKeyIdentifier(), certificate
/* 150 */               .getLinkCertTs(), bi.getLinkCertTs() }), CLASS_NAME);
/* 151 */       result.addUpdatedBackend(certificate);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\contro\\update\task\UpdateBackendsTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */