/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*     */ import com.daimler.cebas.certificates.control.SearchEngine;
/*     */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateSteps;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateTask;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*     */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKICertificateRequest;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKIEnhancedCertificateRequest;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.CertificatesUpdaterFactory;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.HolderRequests;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*     */ import com.daimler.cebas.zenzefi.certificates.entity.ZenZefiCertificate;
/*     */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.lang3.StringUtils;
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
/*     */ public class CollectTimeAndSecocisCSRsTask
/*     */   extends UpdateTask<ZenZefiPublicKeyInfrastructureEsi>
/*     */ {
/*     */   private SearchEngine searchEngine;
/*     */   private Session session;
/*     */   
/*     */   @Autowired
/*     */   public CollectTimeAndSecocisCSRsTask(ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, UpdateSession updateSession, Logger logger, MetadataManager i18n, SearchEngine searchEngine, Session session) {
/*  76 */     super((PublicKeyInfrastructureEsi)publicKeyInfrastructureEsi, importCertificatesEngine, (DefaultUpdateSession)updateSession, logger, i18n);
/*  77 */     this.searchEngine = searchEngine;
/*  78 */     this.session = session;
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
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public HolderRequests execute(List<? extends ImportResult> rootAndBackendImportResult, UpdateType updateType) {
/*  92 */     List<PKICertificateRequest> requests = new ArrayList<>();
/*  93 */     List<PKIEnhancedCertificateRequest> ehhRequest = new ArrayList<>();
/*  94 */     if (this.updateSession.isRunning()) {
/*  95 */       this.updateSession.updateStep(UpdateSteps.COLLECTING_TIME_AND_SECOCIS_CSRS_NOT_CREATED_BASED_ON_PERMISSIONS, "collectingTimeAndSecocisCSRsNotCreatedBasedOnPermissionsStep", updateType);
/*     */       
/*  97 */       this.logger.log(Level.INFO, "000355", this.i18n
/*  98 */           .getEnglishMessage("collectingTimeAndSecocisCSRsNotCreatedBasedOnPermissionsStep", new String[] {
/*     */               
/* 100 */               updateType.name()
/* 101 */             }), com.daimler.cebas.zenzefi.certificates.control.update.task.CollectTimeAndSecocisCSRsTask.class.getSimpleName());
/*     */       
/* 103 */       List<Certificate> csrs = new ArrayList<>();
/*     */ 
/*     */       
/* 106 */       List<? extends ImportResult> backendsResult = (List<? extends ImportResult>)rootAndBackendImportResult.stream().filter(result -> !StringUtils.isEmpty(result.getAuthorityKeyIdentifier())).collect(Collectors.toList());
/*     */ 
/*     */       
/* 109 */       List<List<Certificate>> collected = (List<List<Certificate>>)backendsResult.stream().map(result -> this.searchEngine.findCSRsUnderBackend(this.session.getCurrentUser(), result.getSubjectKeyIdentifier())).collect(Collectors.toList());
/* 110 */       collected.forEach(csrs::addAll);
/* 111 */       collected.clear();
/*     */ 
/*     */       
/* 114 */       List<Certificate> timeCSRs = (List<Certificate>)csrs.stream().filter(csr -> (csr.getType() == CertificateType.TIME_CERTIFICATE && Boolean.TRUE.equals(((ZenZefiCertificate)csr.getParent()).getPkiKnown()))).collect(Collectors.toList());
/*     */ 
/*     */       
/* 117 */       List<Certificate> secocisCSR = (List<Certificate>)csrs.stream().filter(csr -> (csr.getType() == CertificateType.ENHANCED_RIGHTS_CERTIFICATE && csr.isSecOCISCert() && Boolean.TRUE.equals(((ZenZefiCertificate)csr.getParent().getParent()).getPkiKnown()))).collect(Collectors.toList());
/* 118 */       requests.addAll((Collection<? extends PKICertificateRequest>)timeCSRs.stream().map(CertificatesUpdaterFactory::getSimplePKIRequest)
/* 119 */           .collect(Collectors.toList()));
/* 120 */       ehhRequest.addAll((Collection<? extends PKIEnhancedCertificateRequest>)secocisCSR.stream()
/* 121 */           .map(csr -> CertificatesUpdaterFactory.getEhhRightSimpleRequest(csr, csr.getParent(), this.logger))
/* 122 */           .collect(Collectors.toList()));
/*     */       
/* 124 */       this.updateSession.addStepResult(UpdateSteps.COLLECTING_TIME_AND_SECOCIS_CSRS_NOT_CREATED_BASED_ON_PERMISSIONS, HolderRequests.class);
/*     */       
/* 126 */       this.updateSession.getPkiCertificateRequests().addAll(ehhRequest);
/* 127 */       this.updateSession.getPkiCertificateRequests().addAll(requests);
/*     */     } 
/* 129 */     return new HolderRequests(requests, ehhRequest);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\CollectTimeAndSecocisCSRsTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */