/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*     */ import com.daimler.cebas.certificates.control.SearchEngine;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
/*     */ import com.daimler.cebas.certificates.control.validation.CertificatesValidator;
/*     */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKICertificateResponse;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKIEnhancedCertificateRequest;
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
/*     */ @CEBASControl
/*     */ public class DownloadSecOCISCertificateTask
/*     */ {
/*     */   private static final String REASON = "Reason: ";
/*  46 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadSecOCISCertificateTask.class.getSimpleName();
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
/*     */   public DownloadSecOCISCertificateTask(ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, Logger logger, Session session, SearchEngine searchEngine, MetadataManager i18n) {
/*  80 */     this.publicKeyInfrastructureEsi = publicKeyInfrastructureEsi;
/*  81 */     this.importCertificatesEngine = importCertificatesEngine;
/*  82 */     this.logger = logger;
/*  83 */     this.session = session;
/*  84 */     this.searchEngine = searchEngine;
/*  85 */     this.i18n = i18n;
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
/*     */   public ImportResult executeWithoutRetry(PKIEnhancedCertificateRequest pkiCertRequest) {
/*  97 */     logStart(pkiCertRequest);
/*     */     
/*  99 */     List<PKICertificateResponse> pkiCertificates = this.publicKeyInfrastructureEsi.downloadSecOCISCertificatesWithoutRetryNoSession(Collections.singletonList(pkiCertRequest));
/* 100 */     logStop(pkiCertificates);
/* 101 */     List<ImportResult> result = this.importCertificatesEngine.importCertificatesFromBase64NewTransaction((List)pkiCertificates
/* 102 */         .stream().map(PKICertificateResponse::getCertificate).collect(Collectors.toList()), true, true);
/*     */     
/* 104 */     if (result.isEmpty()) {
/* 105 */       throw new CEBASCertificateException("Import result is corrupted, it should not have been empty");
/*     */     }
/* 107 */     return result.get(0);
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
/*     */   public Optional<ImportResult> executeWithRetry(PKIEnhancedCertificateRequest pkiCertRequest) {
/* 119 */     logStart(pkiCertRequest);
/*     */     
/* 121 */     List<PKICertificateResponse> pkiCertificates = this.publicKeyInfrastructureEsi.downloadSecOCISCertificatesWithRetry(Collections.singletonList(pkiCertRequest));
/* 122 */     logStop(pkiCertificates);
/* 123 */     if (pkiCertificates.isEmpty()) {
/* 124 */       return Optional.empty();
/*     */     }
/* 126 */     List<ImportResult> result = this.importCertificatesEngine.importCertificatesFromBase64NewTransaction((List)pkiCertificates
/* 127 */         .stream().map(PKICertificateResponse::getCertificate).collect(Collectors.toList()), true, true);
/*     */     
/* 129 */     return Optional.of(result.get(0));
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
/*     */   public Certificate findDownloadedSecocis(Certificate secOCISCsr) {
/* 141 */     User user = this.session.getCurrentUser();
/* 142 */     Optional<Certificate> optionalSECOCIS = this.searchEngine.findCertificateById(user, secOCISCsr
/* 143 */         .getEntityId());
/* 144 */     if (optionalSECOCIS.isPresent()) {
/* 145 */       Certificate certificate = optionalSECOCIS.get();
/* 146 */       if (ConfigurationUtil.hasUserExtendedValidation(user, this.logger, this.i18n)) {
/* 147 */         if (CertificatesValidator.isValidInChain(certificate, this.i18n, this.logger)) {
/* 148 */           return certificate;
/*     */         }
/*     */         
/* 151 */         CertificateNotFoundException certificateNotFoundException1 = new CertificateNotFoundException(this.i18n.getMessage("noSecOcisFoundMatchingFilterCriteria") + "Reason: " + "Already downloaded and imported SECOCIS certificate does not pass extended validation");
/*     */         
/* 153 */         this.logger.logWithException("000425X", certificateNotFoundException1.getMessage(), (CEBASException)certificateNotFoundException1);
/*     */         
/* 155 */         throw certificateNotFoundException1;
/*     */       } 
/*     */       
/* 158 */       return certificate;
/*     */     } 
/*     */ 
/*     */     
/* 162 */     CertificateNotFoundException certificateNotFoundException = new CertificateNotFoundException(this.i18n.getMessage("noSecOcisFoundMatchingFilterCriteria") + "Reason: " + "Already downloaded SECOCIS certificate does not exist anymore in user's store");
/*     */     
/* 164 */     this.logger.logWithException("000425X", certificateNotFoundException.getMessage(), (CEBASException)certificateNotFoundException);
/*     */     
/* 166 */     throw certificateNotFoundException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logStop(List<PKICertificateResponse> pkiCertificates) {
/* 177 */     this.logger.log(Level.FINE, "000359", "Received SecOCIS Certificate from PKI:" + pkiCertificates
/* 178 */         .toString(), CLASS_NAME);
/* 179 */     this.logger.log(Level.INFO, "000359", "Stop Download SecOCIS Certificate from PKI.", CLASS_NAME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logStart(PKIEnhancedCertificateRequest pkiCertRequest) {
/* 190 */     this.logger.log(Level.FINE, "000359", "Request SecOCIS Certificate from PKI for following CSR:" + pkiCertRequest
/* 191 */         .toString(), CLASS_NAME);
/* 192 */     this.logger.log(Level.INFO, "000359", "Start Download SecOCIS Certificate from PKI.", CLASS_NAME);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\DownloadSecOCISCertificateTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */