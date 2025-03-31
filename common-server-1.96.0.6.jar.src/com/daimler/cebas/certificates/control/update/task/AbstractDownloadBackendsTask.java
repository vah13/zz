/*     */ package com.daimler.cebas.certificates.control.update.task;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*     */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*     */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateSteps;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateTask;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*     */ import com.daimler.cebas.certificates.control.vo.DownloadBackendsResult;
/*     */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*     */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*     */ import com.daimler.cebas.certificates.integration.vo.BackendIdentifier;
/*     */ import com.daimler.cebas.certificates.integration.vo.Permission;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ public abstract class AbstractDownloadBackendsTask
/*     */   extends UpdateTask<PublicKeyInfrastructureEsi>
/*     */ {
/*     */   private String className;
/*     */   private CertificatesConfiguration profileConfiguration;
/*     */   
/*     */   public AbstractDownloadBackendsTask(PublicKeyInfrastructureEsi publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, DefaultUpdateSession updateSession, Logger logger, MetadataManager i18n, String className, CertificatesConfiguration profileConfiguration) {
/*  69 */     super(publicKeyInfrastructureEsi, importCertificatesEngine, updateSession, logger, i18n);
/*  70 */     this.className = className;
/*  71 */     this.profileConfiguration = profileConfiguration;
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
/*     */   public DownloadBackendsResult execute(UpdateType updateType, boolean useV2, List<Permission> permissions) {
/*     */     List<BackendIdentifier> validBackendIdentifiers;
/*  86 */     if (this.updateSession.isNotRunning()) {
/*  87 */       return new DownloadBackendsResult();
/*     */     }
/*     */     
/*  90 */     this.updateSession.updateStep(UpdateSteps.RETRIEVE_BACKEND_IDENTIFIERS, "updateRequestBackendIdentifiers", updateType);
/*  91 */     logBeforeRetrieveBackendIdentifiers(updateType);
/*  92 */     List<BackendIdentifier> rootAndBackendIdentifiers = new ArrayList<>();
/*  93 */     rootAndBackendIdentifiers.addAll(this.publicKeyInfrastructureEsi.getBackendIdentifiers());
/*  94 */     rootAndBackendIdentifiers.addAll(downloadPreactiveIdentifiers(updateType, permissions));
/*     */     
/*  96 */     this.updateSession.updateStep(UpdateSteps.RETRIEVE_BACKEND_IDENTIFIERS, "updateReceiveBackendIdentifiers", updateType);
/*  97 */     logAfterRetrievalOfBackendIdentifiers(rootAndBackendIdentifiers, updateType);
/*     */     
/*  99 */     this.updateSession.updateStep(UpdateSteps.RETRIEVE_BACKENDS, "updateRequestingBackendCertChain", updateType);
/*     */     
/* 101 */     if (useV2) {
/* 102 */       List<String> rootSKIs = getRootSKIsFromIdentifiers(rootAndBackendIdentifiers);
/*     */       
/* 104 */       Map<Boolean, List<BackendIdentifier>> backendIdentifiers = getBackendIdentifiers(rootAndBackendIdentifiers, rootSKIs);
/*     */       
/* 106 */       rootAndBackendIdentifiers = excludeInvalidBackendIdentifiers(updateType, backendIdentifiers.get(Boolean.FALSE), rootAndBackendIdentifiers);
/*     */ 
/*     */       
/* 109 */       validBackendIdentifiers = backendIdentifiers.get(Boolean.TRUE);
/*     */     } else {
/* 111 */       validBackendIdentifiers = rootAndBackendIdentifiers;
/*     */     } 
/*     */     
/* 114 */     Set<String> base64Certificates = this.publicKeyInfrastructureEsi.getCertificatesChain(validBackendIdentifiers);
/* 115 */     logAfterBackendsDownload(base64Certificates, updateType);
/*     */     
/* 117 */     List<ImportResult> importResults = this.importCertificatesEngine.importCertificatesFromBase64NewTransaction(new ArrayList<>(base64Certificates), true, allowImportOfPrivateKeys());
/* 118 */     logAfterBackendsImport(importResults, updateType);
/*     */     
/* 120 */     this.profileConfiguration.getPkiKnownHandler().updatePkiKnownForAllUnknownBackends(importResults);
/*     */     
/* 122 */     this.updateSession.updateStep(UpdateSteps.RETRIEVE_BACKENDS, "updateDownloadRootBackendStop", updateType);
/* 123 */     this.updateSession.addStepResult(UpdateSteps.RETRIEVE_BACKEND_IDENTIFIERS, importResults);
/* 124 */     this.updateSession.resetRetries();
/*     */     
/* 126 */     return new DownloadBackendsResult(importResults, rootAndBackendIdentifiers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BackendIdentifier> downloadPreactiveIdentifiers(List<Permission> permissions) {
/* 137 */     return downloadPreactiveIdentifiers((UpdateType)null, permissions);
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
/*     */   private List<BackendIdentifier> downloadPreactiveIdentifiers(UpdateType updateType, List<Permission> permissions) {
/* 150 */     if (CollectionUtils.isEmpty(permissions)) {
/* 151 */       return new ArrayList<>();
/*     */     }
/*     */     
/* 154 */     List<BackendIdentifier> preactiveIdentifiers = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     Set<String> preactiveCaIds = (Set<String>)permissions.stream().filter(permission -> !CollectionUtils.isEmpty(permission.getValidCAs())).map(Permission::getValidCAs).flatMap(Collection::stream).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
/*     */     
/* 162 */     for (String preactiveCaId : preactiveCaIds) {
/* 163 */       if (this.updateSession.isRunning()) {
/* 164 */         List<BackendIdentifier> list = this.publicKeyInfrastructureEsi.getPreactiveBackendIdentifiers(true, preactiveCaId);
/* 165 */         if (CollectionUtils.isEmpty(list)) {
/* 166 */           this.logger.log(Level.INFO, "000550", this.i18n
/* 167 */               .getEnglishMessage("updateNotDownloadedPreactiveCa", new String[] { updateType.name(), preactiveCaId }), this.className);
/*     */           continue;
/*     */         } 
/* 170 */         preactiveIdentifiers.addAll(list);
/*     */         continue;
/*     */       } 
/* 173 */       List<BackendIdentifier> identifiers = this.publicKeyInfrastructureEsi.getPreactiveBackendIdentifiers(false, preactiveCaId);
/* 174 */       if (!CollectionUtils.isEmpty(identifiers)) {
/* 175 */         preactiveIdentifiers.addAll(identifiers);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 180 */     return preactiveIdentifiers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> getRootSKIsFromIdentifiers(List<BackendIdentifier> rootAndBackendIdentifiers) {
/* 191 */     return (List<String>)rootAndBackendIdentifiers.stream()
/* 192 */       .filter(identifier -> "ROOT_CA".equalsIgnoreCase(identifier.getType()))
/* 193 */       .map(BackendIdentifier::getSubjectKeyIdentifier)
/* 194 */       .collect(Collectors.toList());
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
/*     */   private Map<Boolean, List<BackendIdentifier>> getBackendIdentifiers(List<BackendIdentifier> rootAndBackendIdentifiers, List<String> rootSKIs) {
/* 209 */     return (Map<Boolean, List<BackendIdentifier>>)rootAndBackendIdentifiers.stream()
/* 210 */       .filter(identifier -> "SUB_CA".equalsIgnoreCase(identifier.getType()))
/* 211 */       .collect(Collectors.partitioningBy(identifier -> rootSKIs.contains(identifier.getAuthorityKeyIdentifier())));
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
/*     */ 
/*     */   
/*     */   private void logBeforeRetrieveBackendIdentifiers(UpdateType updateType) {
/* 228 */     this.logger.log(Level.FINER, "000241", this.i18n.getEnglishMessage("updateDownloadRootBackendStart", new String[] { updateType
/* 229 */             .name() }), this.className);
/* 230 */     this.logger.log(Level.FINER, "000242", this.i18n.getEnglishMessage("updateRequestBackendIdentifiers", new String[] { updateType
/* 231 */             .name() }), this.className);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logAfterRetrievalOfBackendIdentifiers(List<BackendIdentifier> backendIdentifiers, UpdateType updateType) {
/* 241 */     this.logger.log(Level.FINER, "000243", this.i18n.getEnglishMessage("updateReceiveBackendIdentifiers", new String[] { updateType
/* 242 */             .name() }), this.className);
/* 243 */     this.logger.log(Level.FINE, "000223", this.i18n.getEnglishMessage("updateReceivedBackendIdentifiers", new String[] { updateType
/* 244 */             .name(), backendIdentifiers.toString() }), this.className);
/* 245 */     this.logger.log(Level.FINER, "000244", this.i18n.getEnglishMessage("updateRequestingBackendCertChain", new String[] { updateType
/* 246 */             .name() }), this.className);
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
/*     */   private List<BackendIdentifier> excludeInvalidBackendIdentifiers(UpdateType updateType, List<BackendIdentifier> invalidBackendIdentifiers, List<BackendIdentifier> rootAndBackendIdentifiers) {
/* 261 */     if (!CollectionUtils.isEmpty(invalidBackendIdentifiers)) {
/* 262 */       invalidBackendIdentifiers.forEach(identifier -> this.logger.log(Level.INFO, "000550", this.i18n.getEnglishMessage("updateInvalidBackendIdentifiers", new String[] { updateType.name(), identifier.getAuthorityKeyIdentifier(), identifier.getSubjectKeyIdentifier() }), this.className));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 268 */       rootAndBackendIdentifiers = (List<BackendIdentifier>)rootAndBackendIdentifiers.stream().filter(identifier -> !invalidBackendIdentifiers.contains(identifier)).collect(Collectors.toList());
/*     */     } 
/* 270 */     return rootAndBackendIdentifiers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logAfterBackendsDownload(Set<String> base64Certificates, UpdateType updateType) {
/* 281 */     this.logger.log(Level.FINER, "000245", this.i18n.getEnglishMessage("updateReceivingBackendCertChain", new String[] { updateType
/* 282 */             .name() }), this.className);
/* 283 */     this.logger.log(Level.FINER, "000224", this.i18n.getEnglishMessage("updateReceivedBackendCertChain", new String[] { updateType
/* 284 */             .name(), base64Certificates.toString() }), this.className);
/* 285 */     if (base64Certificates.isEmpty()) {
/* 286 */       this.logger.log(Level.WARNING, "000550", this.i18n.getEnglishMessage("updateProcessCannotUpdate"), this.className);
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
/*     */   private void logAfterBackendsImport(List<? extends ImportResult> importResults, UpdateType updateType) {
/* 298 */     if (importResults.isEmpty()) {
/* 299 */       this.logger.log(Level.INFO, "000225", updateType + " Update - Root and Backends, No certificates returned by PKI", this.className);
/*     */     }
/*     */     
/* 302 */     importResults.stream().filter(result -> !result.isSuccess())
/* 303 */       .forEach(result -> this.logger.log(Level.WARNING, "000225", updateType + " Update - Root and Backends, PKI  certificate with AKI: " + result.getAuthorityKeyIdentifier() + " and SKI: " + result.getSubjectKeyIdentifier() + " could have not been imported: " + result.getMessage(), this.className));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 308 */     this.logger.log(Level.FINER, "000246", this.i18n
/* 309 */         .getEnglishMessage("updateDownloadRootBackendStop", new String[] { updateType.name() }), this.className);
/*     */   }
/*     */   
/*     */   protected abstract boolean allowImportOfPrivateKeys();
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\contro\\update\task\AbstractDownloadBackendsTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */