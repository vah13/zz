/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.chain.events.DeleteCertsFromKnownBackends;
/*     */ import com.daimler.cebas.certificates.control.exceptions.EmptyPermissionsException;
/*     */ import com.daimler.cebas.certificates.control.update.AbstractCertificatesUpdater;
/*     */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateSteps;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*     */ import com.daimler.cebas.certificates.control.update.task.AbstractDownloadBackendsTask;
/*     */ import com.daimler.cebas.certificates.control.update.task.AbstractUpdateEcuLinkMarkersTask;
/*     */ import com.daimler.cebas.certificates.control.update.task.UpdateBackendsTask;
/*     */ import com.daimler.cebas.certificates.control.update.task.UpdateRootAndBackendsResult;
/*     */ import com.daimler.cebas.certificates.control.vo.DownloadBackendsResult;
/*     */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKICertificateRequest;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKIEnhancedCertificateRequest;
/*     */ import com.daimler.cebas.certificates.integration.vo.Permission;
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.configuration.control.mdc.MdcDecoratorCompletableFuture;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.HolderRequests;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.CollectTimeAndSecocisCSRsTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.CreateCSRsUnderBackendTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadCertificatesUnderBackendTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadEnhancedCertificatesTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadLinkCertificatesTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadNonVsmECUCertificateTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadPermissionsTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadTimeAndSecocisTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.IUpdateTaskProvider;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.UpdateRenewalPeriodTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.ZenzefiCreateEnhancedCSRsTask;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.scheduling.annotation.Async;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractZenzefiCertificatesUpdater
/*     */   extends AbstractCertificatesUpdater
/*     */ {
/*  60 */   private static final Logger LOG = Logger.getLogger(com.daimler.cebas.zenzefi.certificates.control.update.AbstractZenzefiCertificatesUpdater.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.certificates.control.update.AbstractZenzefiCertificatesUpdater.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${feature.certificate.chain.exchange:false}")
/*     */   private boolean certificateChainFeatureEnabled;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected UpdateSteps updateSteps;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DownloadPermissionsTask downloadPermissionsTask;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CreateCSRsUnderBackendTask createCSRsUnderBackendTask;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DownloadCertificatesUnderBackendTask downloadCertificatesTask;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ZenzefiCreateEnhancedCSRsTask createEnhancedCSRsTask;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DownloadEnhancedCertificatesTask downloadEnhancedCertificatesTask;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CollectTimeAndSecocisCSRsTask collectTimeAndSecocisCSRsTask;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DownloadTimeAndSecocisTask downloadTimeAndSecocisTask;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UpdateBackendsTask updateBackendsTask;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UpdateRenewalPeriodTask updateRenewalPeriodTask;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DownloadLinkCertificatesTask downloadLinkCertificatesTask;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DownloadNonVsmECUCertificateTask downloadNonVsmECUCertificateTask;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractUpdateEcuLinkMarkersTask updateEcuLinkMarkersTask;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractZenzefiCertificatesUpdater(Logger logger, MetadataManager i18n, UpdateSession updateSession, IUpdateTaskProvider taskProvider) {
/* 144 */     super(logger, i18n, (DefaultUpdateSession)updateSession, (AbstractDownloadBackendsTask)taskProvider.getDownloadBackendsTask());
/* 145 */     this.downloadPermissionsTask = taskProvider.getDownloadPermissionsTask();
/* 146 */     this.createCSRsUnderBackendTask = taskProvider.getCreateCSRsUnderBackendTask();
/* 147 */     this.downloadCertificatesTask = taskProvider.getDownloadCertificatesTask();
/* 148 */     this.createEnhancedCSRsTask = taskProvider.getCreateEnhancedCSRsTask();
/* 149 */     this.downloadEnhancedCertificatesTask = taskProvider.getDownloadEnhancedCertificatesTask();
/* 150 */     this.collectTimeAndSecocisCSRsTask = taskProvider.getCollectTimeAndSecocisCSR();
/* 151 */     this.downloadTimeAndSecocisTask = taskProvider.getDownloadTimeAndSecocisTask();
/* 152 */     this.updateBackendsTask = taskProvider.getUpdateBackendTask();
/* 153 */     this.updateRenewalPeriodTask = taskProvider.getUpdateRenewalPeriodTask();
/* 154 */     this.downloadLinkCertificatesTask = taskProvider.getDownloadLinkCertificatesTask();
/* 155 */     this.downloadNonVsmECUCertificateTask = taskProvider.getDownloadNonVsmEcuCertificateTask();
/* 156 */     this.updateEcuLinkMarkersTask = taskProvider.getAbstractUpdateEcuLinkMarkersTask();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Async
/*     */   public void updateCertificates() {
/* 165 */     String METHOD_NAME = "updateCertificates";
/*     */     
/* 167 */     this.updateSession.setUpLanguage();
/*     */     
/* 169 */     this.logger.log(Level.INFO, "000222", this.i18n.getEnglishMessage("updateProcessStart", new String[] { this.updateType.name() }), CLASS_NAME);
/* 170 */     this.updateSession.run();
/*     */     try {
/* 172 */       List<Permission> permissions = downloadPermissions();
/* 173 */       DownloadBackendsResult rootBackendResults = downloadBackends(permissions);
/* 174 */       if (rootBackendResults.importExecuted()) {
/* 175 */         List<ImportResult> importResult = rootBackendResults.getImportResult();
/* 176 */         handleEmptyPermissions(permissions, importResult);
/* 177 */         Map<CertificateType, UpdateRootAndBackendsResult> updateRootAndBackendsResultMap = updateBackendMappings(rootBackendResults);
/* 178 */         updateNonVsmECUCertificates(updateRootAndBackendsResultMap.get(CertificateType.ECU_CERTIFICATE));
/* 179 */         updateLinkCertificates(updateRootAndBackendsResultMap.get(CertificateType.ROOT_CA_LINK_CERTIFICATE), updateRootAndBackendsResultMap.get(CertificateType.BACKEND_CA_LINK_CERTIFICATE), updateRootAndBackendsResultMap.get(CertificateType.NO_TYPE));
/* 180 */         updateEcuLinkMarkers(updateRootAndBackendsResultMap, rootBackendResults);
/* 181 */         List<PKICertificateRequest> stdRequests = createCSRsUnderBackend(importResult, permissions);
/* 182 */         downloadCertificatesUnderBackend(stdRequests);
/* 183 */         CompletableFuture<List<PKIEnhancedCertificateRequest>> ehhRightsFutureRequests = createEnhancedRightsCSRs(importResult, permissions);
/* 184 */         downloadEnhancedRights(ehhRightsFutureRequests);
/* 185 */         processTimeAndSecocis(importResult);
/* 186 */         updateRenewalPeriod(permissions);
/* 187 */         this.logger.log(Level.INFO, "000227", this.i18n
/* 188 */             .getEnglishMessage("updateProcessEnd", new String[] { this.updateType.name() }), CLASS_NAME);
/*     */       }
/*     */     
/* 191 */     } catch (ExecutionException e) {
/* 192 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 193 */       this.logger.log(Level.SEVERE, "000284X", "Error while performing " + this.updateType
/* 194 */           .name() + " Certificates Update. Reason: " + e.getMessage(), CLASS_NAME);
/*     */     }
/* 196 */     catch (InterruptedException e) {
/* 197 */       Thread.currentThread().interrupt();
/* 198 */       this.logger.log(Level.SEVERE, "000284X", "Error while performing " + this.updateType.name() + " Update process manager was interrupted:  " + e
/* 199 */           .getMessage(), CLASS_NAME);
/*     */     } finally {
/* 201 */       if (this.updateSession.isRunning()) {
/* 202 */         this.updateSession.setNotRunning();
/*     */       }
/*     */     } 
/* 205 */     this.logger.exiting(CLASS_NAME, "updateCertificates");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<CertificateType, UpdateRootAndBackendsResult> updateBackendMappings(DownloadBackendsResult rootAndBackendResults) throws ExecutionException, InterruptedException {
/* 211 */     return MdcDecoratorCompletableFuture.supplyAsync(() -> this.updateBackendsTask.execute(this.updateType, rootAndBackendResults.getBackendIdentifiers()))
/*     */       
/* 213 */       .exceptionally(this::addUpdateBackendsErrorState).get();
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
/*     */   private void updateNonVsmECUCertificates(UpdateRootAndBackendsResult updatedBackends) throws ExecutionException, InterruptedException {
/* 227 */     if (this.updateSession.isRunning() && this.certificateChainFeatureEnabled) {
/* 228 */       CompletableFuture.runAsync(() -> this.downloadNonVsmECUCertificateTask.execute(updatedBackends, this.updateType)).get();
/*     */     } else {
/* 230 */       this.logger.log(Level.INFO, "000643", this.i18n.getEnglishMessage("skipNonVsmFeatureDisabled", new String[] { this.updateType
/* 231 */               .name() }), getClass().getSimpleName());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateLinkCertificates(UpdateRootAndBackendsResult updatedRoots, UpdateRootAndBackendsResult updatedBackends, UpdateRootAndBackendsResult allRootsAndBackends) throws InterruptedException, ExecutionException {
/* 250 */     if (this.updateSession.isRunning() && this.certificateChainFeatureEnabled) {
/* 251 */       if (UpdateType.DIFFERENTIAL.equals(this.updateType)) {
/* 252 */         CompletableFuture.runAsync(() -> this.downloadLinkCertificatesTask.execute(this.updateType, updatedRoots, updatedBackends)).get();
/*     */       } else {
/* 254 */         UpdateRootAndBackendsResult allRoots = new UpdateRootAndBackendsResult();
/* 255 */         allRoots.getUpdatedRootAndBackends().addAll((Collection)allRootsAndBackends.getUpdatedRootAndBackends().stream()
/* 256 */             .filter(cert -> CertificateType.ROOT_CA_CERTIFICATE.equals(cert.getType()))
/* 257 */             .collect(Collectors.toList()));
/* 258 */         UpdateRootAndBackendsResult allBackends = new UpdateRootAndBackendsResult();
/* 259 */         allBackends.getUpdatedRootAndBackends().addAll((Collection)allRootsAndBackends.getUpdatedRootAndBackends().stream()
/* 260 */             .filter(cert -> CertificateType.BACKEND_CA_CERTIFICATE.equals(cert.getType()))
/* 261 */             .collect(Collectors.toList()));
/* 262 */         CompletableFuture.runAsync(() -> this.downloadLinkCertificatesTask.execute(this.updateType, allRoots, allBackends)).get();
/*     */       } 
/*     */     } else {
/* 265 */       this.logger.log(Level.INFO, "000643", this.i18n.getEnglishMessage("skipLinkFeatureDisabled", new String[] { this.updateType
/* 266 */               .name() }), CLASS_NAME);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateEcuLinkMarkers(Map<CertificateType, UpdateRootAndBackendsResult> updatedBackends, DownloadBackendsResult rootBackendResults) throws InterruptedException, ExecutionException {
/* 283 */     if (this.updateSession.isRunning() && this.certificateChainFeatureEnabled) {
/* 284 */       CompletableFuture.runAsync(() -> this.updateEcuLinkMarkersTask.execute(updatedBackends, rootBackendResults, this.updateType)).get();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected String handleInvalidToken() {
/* 290 */     String message = super.handleInvalidToken();
/* 291 */     this.updateSession.setNotRunning();
/* 292 */     return message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateRenewalPeriod(List<Permission> permissions) throws InterruptedException, ExecutionException {
/* 303 */     if (this.updateSession.isRunning()) {
/* 304 */       CompletableFuture.runAsync(() -> this.updateRenewalPeriodTask.execute(permissions)).get();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processTimeAndSecocis(List<? extends ImportResult> rootAndBackendResults) throws InterruptedException, ExecutionException {
/* 321 */     HolderRequests holderRequests = CompletableFuture.<HolderRequests>supplyAsync(() -> this.collectTimeAndSecocisCSRsTask.execute(rootAndBackendResults, this.updateType)).exceptionally(this::addErrorStateCollectingTimeAndSecocisCSR).get();
/* 322 */     if (this.updateSession.isRunning()) {
/* 323 */       List<PKICertificateRequest> underBackendRequests = holderRequests.getUnderBackendRequests();
/* 324 */       List<PKIEnhancedCertificateRequest> underDiagRequests = holderRequests.getUnderDiagRequests();
/* 325 */       CompletableFuture.runAsync(() -> this.downloadTimeAndSecocisTask.execute(underBackendRequests, underDiagRequests, this.updateType))
/* 326 */         .get();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<PKICertificateRequest> addPKICertificatesRequestErrorState(Throwable e) {
/* 337 */     UpdateSteps currentStep = this.updateSession.getCurrentStep();
/* 338 */     this.updateSession.updateStep(currentStep, e.getMessage(), this.updateType, true);
/* 339 */     this.updateSession.setNotRunning();
/* 340 */     this.logger.logWithException("000290X", e.getMessage(), new CEBASException(e.getMessage()));
/* 341 */     return new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<PKIEnhancedCertificateRequest> addPKIEnhhCertificatesRequestErrorState(Throwable e) {
/* 351 */     UpdateSteps currentStep = this.updateSession.getCurrentStep();
/* 352 */     this.updateSession.updateStep(currentStep, e.getMessage(), this.updateType, true);
/* 353 */     this.updateSession.setNotRunning();
/* 354 */     this.logger.logWithException("000292X", e.getMessage(), new CEBASException(e.getMessage()));
/* 355 */     return new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HolderRequests addErrorStateCollectingTimeAndSecocisCSR(Throwable e) {
/* 365 */     String errorMessage = e.getMessage();
/* 366 */     generalErrorCase("Errror collecting time and secocis CSRS");
/* 367 */     this.logger.logWithException("000288X", errorMessage, new CEBASException(errorMessage));
/* 368 */     return new HolderRequests(new ArrayList(), new ArrayList());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void downloadEnhancedRights(CompletableFuture<List<PKIEnhancedCertificateRequest>> pkiEnhancedCertificateRequestsFuture) throws InterruptedException, ExecutionException {
/* 374 */     if (this.updateSession.isRunning()) {
/* 375 */       pkiEnhancedCertificateRequestsFuture
/* 376 */         .thenAcceptAsync(pkiEnhancedCertificateRequests -> this.downloadEnhancedCertificatesTask.execute(pkiEnhancedCertificateRequests, this.updateType))
/*     */         
/* 378 */         .exceptionally(this::addDownloadCertificatesErrorState).get();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private CompletableFuture<List<PKIEnhancedCertificateRequest>> createEnhancedRightsCSRs(List<ImportResult> rootAndBackendResults, List<Permission> permissions) {
/* 384 */     return 
/* 385 */       MdcDecoratorCompletableFuture.supplyAsync(() -> this.createEnhancedCSRsTask.execute(rootAndBackendResults, permissions, this.updateType))
/* 386 */       .exceptionally(this::addPKIEnhhCertificatesRequestErrorState);
/*     */   }
/*     */ 
/*     */   
/*     */   private void downloadCertificatesUnderBackend(List<PKICertificateRequest> pkiCertificateRequests) throws InterruptedException, ExecutionException {
/* 391 */     if (this.updateSession.isRunning())
/*     */     {
/* 393 */       MdcDecoratorCompletableFuture.runAsync(() -> this.downloadCertificatesTask.execute(pkiCertificateRequests, this.updateType))
/* 394 */         .exceptionally(this::addDownloadCertificatesErrorState).get();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private List<PKICertificateRequest> createCSRsUnderBackend(List<ImportResult> rootAndBackendResults, List<Permission> permissions) throws InterruptedException, ExecutionException {
/* 400 */     return 
/* 401 */       MdcDecoratorCompletableFuture.supplyAsync(() -> this.createCSRsUnderBackendTask.execute(rootAndBackendResults, permissions, this.updateType))
/* 402 */       .exceptionally(this::addPKICertificatesRequestErrorState).get();
/*     */   }
/*     */   
/*     */   private List<Permission> downloadPermissions() throws InterruptedException, ExecutionException {
/* 406 */     return MdcDecoratorCompletableFuture.supplyAsync(() -> this.downloadPermissionsTask.execute(this.updateType))
/* 407 */       .exceptionally(this::addPermissionsErrorState).get();
/*     */   }
/*     */   
/*     */   private void handleEmptyPermissions(List<Permission> permissions, List<ImportResult> rootAdnBackendsResult) {
/* 411 */     if (permissions.isEmpty()) {
/* 412 */       this.applicationEventPublisher.publishEvent((ApplicationEvent)new DeleteCertsFromKnownBackends(rootAdnBackendsResult, this));
/* 413 */       throw new EmptyPermissionsException();
/*     */     } 
/*     */   }
/*     */   
/*     */   private DownloadBackendsResult downloadBackends(List<Permission> permissions) throws InterruptedException, ExecutionException {
/* 418 */     return MdcDecoratorCompletableFuture.supplyAsync(() -> this.downloadBackendsTask.execute(this.updateType, this.certificateChainFeatureEnabled, permissions))
/* 419 */       .exceptionally(this::addDownloadBackendsErrorState).get();
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\AbstractZenzefiCertificatesUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */