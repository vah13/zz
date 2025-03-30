/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.CertificateToolsProvider;
/*     */ import com.daimler.cebas.certificates.control.DeleteCertificatesEngine;
/*     */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*     */ import com.daimler.cebas.certificates.control.SearchEngine;
/*     */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateSteps;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateTask;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*     */ import com.daimler.cebas.certificates.control.update.task.UpdateRootAndBackendsResult;
/*     */ import com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo;
/*     */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*     */ import com.daimler.cebas.certificates.integration.vo.NonVsmCertificateRequest;
/*     */ import com.daimler.cebas.certificates.integration.vo.NonVsmIdentifier;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKIEcuCertificateResponse;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.zenzefi.certificates.entity.ZenZefiCertificate;
/*     */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASControl
/*     */ public class DownloadNonVsmECUCertificateTask
/*     */   extends UpdateTask<ZenZefiPublicKeyInfrastructureEsi>
/*     */ {
/*     */   @Value("${non_vsm.chunk.limit}")
/*     */   private Integer chunkLimit;
/*  51 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadNonVsmECUCertificateTask.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SearchEngine searchEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DeleteCertificatesEngine deleteCertificatesEngine;
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
/*     */   @Autowired
/*     */   public DownloadNonVsmECUCertificateTask(ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, DefaultUpdateSession updateSession, Logger logger, MetadataManager i18n, CertificateToolsProvider toolsProvider, Session session) {
/*  74 */     super((PublicKeyInfrastructureEsi)publicKeyInfrastructureEsi, importCertificatesEngine, updateSession, logger, i18n);
/*  75 */     this.session = session;
/*  76 */     this.searchEngine = toolsProvider.getSearchEngine();
/*  77 */     this.deleteCertificatesEngine = toolsProvider.getDeleteCertificatesEngine();
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
/*     */   public void execute(UpdateRootAndBackendsResult updatedBackends, UpdateType updateType) {
/*  89 */     this.updateSession.updateStep(UpdateSteps.UPDATE_NON_VSM_CERTIFICATES, "startNonVsmCertificatesDownload", updateType);
/*     */     
/*  91 */     this.logger.log(Level.INFO, "000630", this.i18n.getEnglishMessage("startNonVsmCertificatesDownload", new String[] { updateType
/*  92 */             .name() }), CLASS_NAME);
/*  93 */     this.logger.log(Level.INFO, "000631", this.i18n.getEnglishMessage("updatingNonVsmCertificatesForBackend", new String[] { updateType
/*  94 */             .name(), String.valueOf(updatedBackends.getUpdatedRootAndBackends().size()) }), CLASS_NAME);
/*     */     
/*  96 */     List<String> importedCertificates = new ArrayList<>();
/*  97 */     updatedBackends.getUpdatedRootAndBackends().stream()
/*  98 */       .filter(backend -> StringUtils.isNotEmpty(backend.getAuthorityKeyIdentifier()))
/*  99 */       .forEach(backend -> handleBackend(updateType, importedCertificates, backend));
/*     */     
/* 101 */     this.updateSession.updateStep(UpdateSteps.UPDATE_NON_VSM_CERTIFICATES, "stopNonVsmCertificatesDownload", updateType);
/* 102 */     this.updateSession.addStepResult(UpdateSteps.UPDATE_NON_VSM_CERTIFICATES, importedCertificates);
/*     */     
/* 104 */     this.logger.log(Level.INFO, "000639", this.i18n
/* 105 */         .getEnglishMessage("stopNonVsmCertificatesDownload", new String[] { updateType.name() }), CLASS_NAME);
/*     */     
/* 107 */     this.updateSession.resetRetries();
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
/*     */   private void handleBackend(UpdateType updateType, List<String> importedCertificates, Certificate backendCert) {
/* 121 */     String backendSki = backendCert.getSubjectKeyIdentifier();
/* 122 */     List<NonVsmIdentifier> nonVsmIdentifiers = downloadNonVsmIdentifiers(backendSki.replaceAll("-", ""));
/* 123 */     logNonVsmIdentifiersDownloadStep(nonVsmIdentifiers, backendSki, updateType);
/* 124 */     List<ZenZefiCertificate> nonVSMsUnderParent = this.searchEngine.findVsmOrNonVsmCertificatesUnderParent(this.session.getCurrentUser(), backendSki, false, ZenZefiCertificate.class);
/*     */     
/* 126 */     List<String> notReceived = filterNotReceivedIdentifiers(nonVSMsUnderParent, nonVsmIdentifiers);
/* 127 */     checkAndDeleteUnreceivedNonVsmCertificates(notReceived, updateType);
/*     */     
/* 129 */     if (nonVsmIdentifiers.isEmpty()) {
/* 130 */       this.logger.log(Level.INFO, "000636", this.i18n.getEnglishMessage("noNonVsmIdentifiersZenzefi", new String[] { updateType
/* 131 */               .name(), backendSki }), CLASS_NAME);
/*     */     } else {
/* 133 */       int chunks = getNumberOfIterations(this.chunkLimit.intValue(), nonVsmIdentifiers);
/* 134 */       for (int chunkIndex = 0; chunkIndex < chunks; chunkIndex++) {
/* 135 */         List<NonVsmIdentifier> nonVsmIdentifierListChunk = getNextChunk(nonVsmIdentifiers, this.chunkLimit, chunkIndex);
/* 136 */         downloadCertificatesInChunks(updateType, backendSki, nonVsmIdentifierListChunk, nonVSMsUnderParent, importedCertificates);
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
/*     */   private List<NonVsmIdentifier> downloadNonVsmIdentifiers(String backendSubjectKeyIdentifier) {
/* 149 */     if (this.updateSession.isRunning()) {
/* 150 */       List<NonVsmIdentifier> nonVsmIdentifiers = ((ZenZefiPublicKeyInfrastructureEsi)this.publicKeyInfrastructureEsi).getNonVsmIdentifiers(backendSubjectKeyIdentifier);
/* 151 */       this.updateSession.resetRetries();
/* 152 */       return nonVsmIdentifiers;
/*     */     } 
/* 154 */     return Collections.emptyList();
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
/*     */   private void logNonVsmIdentifiersDownloadStep(List<NonVsmIdentifier> nonVsmIdentifiers, String backendSKI, UpdateType updateType) {
/* 169 */     if (nonVsmIdentifiers == null || nonVsmIdentifiers.isEmpty()) {
/* 170 */       this.logger.log(Level.INFO, "000632", this.i18n.getEnglishMessage("noVsmIdentifiersRetrieved", new String[] { updateType
/* 171 */               .name(), backendSKI }), CLASS_NAME);
/*     */     } else {
/* 173 */       this.logger.log(Level.FINER, "000633", this.i18n
/* 174 */           .getEnglishMessage("vsmIdentifiersRetrievedForBackend", new String[] {
/* 175 */               updateType.name(), backendSKI, listToString(nonVsmIdentifiers)
/*     */             }), CLASS_NAME);
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
/*     */   private List<String> filterNotReceivedIdentifiers(List<ZenZefiCertificate> nonVSMsUnderParent, List<NonVsmIdentifier> nonVsmIdentifiers) {
/* 192 */     return (List<String>)nonVSMsUnderParent.stream()
/* 193 */       .filter(ecu -> (Boolean.TRUE.equals(((ZenZefiCertificate)ecu.getParent()).getPkiKnown()) && nonVsmIdentifiers.stream().noneMatch(())))
/*     */       
/* 195 */       .map(Certificate::getEntityId).collect(Collectors.toList());
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
/*     */   private void checkAndDeleteUnreceivedNonVsmCertificates(List<String> notReceived, UpdateType updateType) {
/* 208 */     if (!notReceived.isEmpty()) {
/* 209 */       List<DeleteCertificatesInfo> deleteCertificatesInfos = this.deleteCertificatesEngine.deleteCertificatesDuringImport(notReceived);
/* 210 */       if (deleteCertificatesInfos.size() > 0) {
/* 211 */         deleteCertificatesInfos.forEach(deletedNonVsmCert -> this.logger.log(Level.INFO, "000634", this.i18n.getEnglishMessage("deletedNonVsmCertificateZenzefi", new String[] { updateType.name(), deletedNonVsmCert.getSubjectKeyIdentifier() }), CLASS_NAME));
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 216 */         this.logger.log(Level.INFO, "000635", this.i18n.getEnglishMessage("noDeletedNonVsmCertificateZenzefi", new String[] { updateType
/* 217 */                 .name() }), CLASS_NAME);
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
/*     */ 
/*     */   
/*     */   private int getNumberOfIterations(int chunk, List<?> list) {
/* 232 */     if (list.size() <= chunk) {
/* 233 */       return 1;
/*     */     }
/* 235 */     int division = list.size() / chunk;
/* 236 */     return (list.size() % chunk == 0) ? division : (division + 1);
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
/*     */   private List<NonVsmIdentifier> getNextChunk(List<NonVsmIdentifier> nonVsmIdentifiers, Integer chunkLimit, int chunkIndex) {
/* 251 */     int start = chunkIndex * chunkLimit.intValue();
/* 252 */     int end = Math.min(start + chunkLimit.intValue(), nonVsmIdentifiers.size());
/* 253 */     return new ArrayList<>(nonVsmIdentifiers.subList(start, end));
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
/*     */ 
/*     */   
/*     */   private void downloadCertificatesInChunks(UpdateType updateType, String ski, List<NonVsmIdentifier> nonVsmIdentifiers, List<ZenZefiCertificate> nonVSMsUnderParent, List<String> importedCertificates) {
/* 273 */     List<String> nonVsmCertificates = new ArrayList<>();
/* 274 */     for (NonVsmIdentifier nonVsmIdentifier : nonVsmIdentifiers) {
/* 275 */       NonVsmCertificateRequest nonVsmCertificateRequest = new NonVsmCertificateRequest();
/* 276 */       nonVsmCertificateRequest.setCaId(ski.replaceAll("-", ""));
/* 277 */       nonVsmCertificateRequest.setSki(nonVsmIdentifier.getSubjectKeyIdentifier().replaceAll("-", ""));
/* 278 */       nonVsmCertificateRequest.setCn(nonVsmIdentifier.getSubject());
/* 279 */       nonVsmCertificateRequest.setEcuUniqueIds(nonVsmIdentifier.getEcuUniqueIds());
/* 280 */       if (nonVSMsUnderParent.stream().noneMatch(zenzefiCertificate -> ecuEquals(nonVsmIdentifier, zenzefiCertificate))) {
/* 281 */         nonVsmCertificates.addAll(downloadNonVsmCertificates(nonVsmCertificateRequest));
/*     */       }
/*     */     } 
/*     */     
/* 285 */     if (!nonVsmCertificates.isEmpty()) {
/* 286 */       List<ImportResult> result = this.importCertificatesEngine.importCertificatesFromBase64NewTransaction(nonVsmCertificates, true, false);
/* 287 */       importedCertificates
/* 288 */         .addAll((Collection<? extends String>)result.stream().map(c -> c.getSubjectKeyIdentifier() + c.getAuthorityKeyIdentifier())
/* 289 */           .collect(Collectors.toList()));
/* 290 */       this.logger.log(Level.FINEST, "000637", this.i18n
/* 291 */           .getEnglishMessage("contentListOfNonVsmCertificates", new String[] {
/* 292 */               updateType.name(), ski, listToString(result)
/*     */             }), CLASS_NAME);
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
/*     */   private List<String> downloadNonVsmCertificates(NonVsmCertificateRequest request) {
/* 305 */     if (this.updateSession.isRunning()) {
/* 306 */       List<PKIEcuCertificateResponse> nonVsmCertificates = ((ZenZefiPublicKeyInfrastructureEsi)this.publicKeyInfrastructureEsi).downloadNonVsmCertificates(Collections.singletonList(request), true);
/* 307 */       this.updateSession.resetRetries();
/* 308 */       return (List<String>)nonVsmCertificates.stream().map(PKIEcuCertificateResponse::getEcuCert).collect(Collectors.toList());
/*     */     } 
/* 310 */     return Collections.emptyList();
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
/*     */   private String listToString(List<?> result) {
/* 322 */     ObjectMapper objectMapper = new ObjectMapper();
/*     */     try {
/* 324 */       return objectMapper.writeValueAsString(result);
/* 325 */     } catch (JsonProcessingException e) {
/* 326 */       this.logger.log(Level.WARNING, "000643X", e.getMessage(), CLASS_NAME);
/*     */       
/* 328 */       return "";
/*     */     } 
/*     */   }
/*     */   private static boolean ecuEquals(NonVsmIdentifier nonVsmIdentifier, ZenZefiCertificate certificate) {
/* 332 */     return (skiEquals(nonVsmIdentifier, certificate) && subjectEquals(nonVsmIdentifier, certificate) && uniqueEcuIdEquals(nonVsmIdentifier, certificate));
/*     */   }
/*     */   
/*     */   private static boolean skiEquals(NonVsmIdentifier nonVsmIdentifier, ZenZefiCertificate certificate) {
/* 336 */     return certificate.getSubjectKeyIdentifier().replace("-", "").equalsIgnoreCase(nonVsmIdentifier.getSubjectKeyIdentifier());
/*     */   }
/*     */   
/*     */   private static boolean subjectEquals(NonVsmIdentifier nonVsmIdentifier, ZenZefiCertificate certificate) {
/* 340 */     return certificate.getSubject().replace("CN=", "").equals(nonVsmIdentifier.getSubject());
/*     */   }
/*     */   
/*     */   private static boolean uniqueEcuIdEquals(NonVsmIdentifier nonVsmIdentifier, ZenZefiCertificate certificate) {
/* 344 */     List<String> listA = Arrays.asList(certificate.getUniqueECUID().replace(" ", "").split(","));
/* 345 */     Collections.sort(listA);
/*     */     
/* 347 */     List<String> listB = nonVsmIdentifier.getEcuUniqueIds();
/* 348 */     Collections.sort(listB);
/*     */     
/* 350 */     return listA.equals(listB);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\DownloadNonVsmECUCertificateTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */