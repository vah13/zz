/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*     */ import com.daimler.cebas.certificates.control.SearchEngine;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
/*     */ import com.daimler.cebas.certificates.control.validation.CertificatesValidator;
/*     */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKICertificateRequest;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKICertificateResponse;
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.configuration.control.ConfigurationUtil;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASControl
/*     */ public class DownloadTimeCertificateTask
/*     */ {
/*     */   private static final String REASON = "Reason: ";
/*     */   private static final String IMPORT_RESULT_IS_CORRUPTED_IT_SHOULD_NOT_HAVE_BEEN_EMPTY = "Import result is corrupted, it should not have been empty";
/*  56 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadTimeCertificateTask.class
/*  57 */     .getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImportCertificatesEngine importCertificatesEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Logger logger;
/*     */ 
/*     */ 
/*     */   
/*     */   private Session session;
/*     */ 
/*     */ 
/*     */   
/*     */   private SearchEngine searchEngine;
/*     */ 
/*     */ 
/*     */   
/*     */   private MetadataManager i18n;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public DownloadTimeCertificateTask(ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, Logger logger, Session session, SearchEngine searchEngine, MetadataManager i18n) {
/*  91 */     this.publicKeyInfrastructureEsi = publicKeyInfrastructureEsi;
/*  92 */     this.importCertificatesEngine = importCertificatesEngine;
/*  93 */     this.logger = logger;
/*  94 */     this.session = session;
/*  95 */     this.searchEngine = searchEngine;
/*  96 */     this.i18n = i18n;
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
/*     */   public ImportResult executeWithoutRetry(PKICertificateRequest pkiCertRequest) {
/* 109 */     logStart(pkiCertRequest);
/*     */     
/* 111 */     List<PKICertificateResponse> pkiCertificates = this.publicKeyInfrastructureEsi.downloadTimeCertificatesWithoutRetryNoSession(
/* 112 */         Collections.singletonList(pkiCertRequest));
/* 113 */     logEnd(pkiCertificates);
/*     */     
/* 115 */     List<ImportResult> importCertificatesFromBase64 = this.importCertificatesEngine.importCertificatesFromBase64NewTransaction((List)pkiCertificates.stream()
/* 116 */         .map(PKICertificateResponse::getCertificate)
/* 117 */         .collect(Collectors.toList()), true, true);
/* 118 */     if (importCertificatesFromBase64.isEmpty()) {
/* 119 */       throw new CEBASCertificateException("Import result is corrupted, it should not have been empty");
/*     */     }
/*     */     
/* 122 */     return importCertificatesFromBase64.get(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public Optional<ImportResult> executeWithRetry(PKICertificateRequest pkiCertRequest) {
/* 134 */     logStart(pkiCertRequest);
/*     */     
/* 136 */     List<PKICertificateResponse> pkiCertificates = this.publicKeyInfrastructureEsi.downloadTimeCertificatesWithRetry(Collections.singletonList(pkiCertRequest));
/* 137 */     logEnd(pkiCertificates);
/* 138 */     if (pkiCertificates.isEmpty()) {
/* 139 */       return Optional.empty();
/*     */     }
/*     */     
/* 142 */     List<ImportResult> importCertificatesFromBase64 = this.importCertificatesEngine.importCertificatesFromBase64NewTransaction((List)pkiCertificates.stream().map(PKICertificateResponse::getCertificate)
/* 143 */         .collect(Collectors.toList()), true, true);
/* 144 */     if (importCertificatesFromBase64.isEmpty()) {
/* 145 */       throw new CEBASCertificateException("Import result is corrupted, it should not have been empty");
/*     */     }
/* 147 */     return Optional.of(importCertificatesFromBase64.get(0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional
/*     */   public Certificate findDownloadedTime(Certificate timeCsr) {
/* 159 */     User user = this.session.getCurrentUser();
/* 160 */     Optional<Certificate> timeOptional = this.searchEngine.findCertificateById(user, timeCsr
/* 161 */         .getEntityId());
/* 162 */     if (timeOptional.isPresent()) {
/* 163 */       Certificate certificate = timeOptional.get();
/* 164 */       if (ConfigurationUtil.hasUserExtendedValidation(user, this.logger, this.i18n)) {
/* 165 */         if (CertificatesValidator.isValidInChain(certificate, this.i18n, this.logger)) {
/* 166 */           return certificate;
/*     */         }
/*     */         
/* 169 */         CertificateNotFoundException certificateNotFoundException1 = new CertificateNotFoundException(this.i18n.getMessage("noTimeCertFoundMatchingFilterCriteria") + "Reason: " + "Already Time downloaded and imported certificate does not pass extended validation");
/*     */         
/* 171 */         this.logger.logWithException("000425X", certificateNotFoundException1.getMessage(), (CEBASException)certificateNotFoundException1);
/*     */         
/* 173 */         throw certificateNotFoundException1;
/*     */       } 
/*     */       
/* 176 */       return certificate;
/*     */     } 
/*     */ 
/*     */     
/* 180 */     CertificateNotFoundException certificateNotFoundException = new CertificateNotFoundException(this.i18n.getMessage("noTimeCertFoundMatchingFilterCriteria") + "Reason: " + "Already downloaded TIME certificate does not exist anymore in user's store");
/*     */     
/* 182 */     this.logger.logWithException("000425X", certificateNotFoundException.getMessage(), (CEBASException)certificateNotFoundException);
/*     */     
/* 184 */     throw certificateNotFoundException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logEnd(List<PKICertificateResponse> pkiCertificates) {
/* 195 */     this.logger.log(Level.FINE, "000281", "Received Time certificate from PKI:" + pkiCertificates
/*     */         
/* 197 */         .toString(), CLASS_NAME);
/*     */     
/* 199 */     this.logger.log(Level.INFO, "000062", "Stop Download Time Certificate from PKI.", CLASS_NAME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logStart(PKICertificateRequest pkiCertRequest) {
/* 210 */     this.logger.log(Level.FINE, "000280", "Request Time certificate from PKI for following CSR:" + pkiCertRequest
/*     */         
/* 212 */         .toString(), CLASS_NAME);
/*     */     
/* 214 */     this.logger.log(Level.INFO, "000060", "Start Download Time Certificate from PKI.", CLASS_NAME);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\DownloadTimeCertificateTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */