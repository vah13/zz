/*     */ package com.daimler.cebas.certificates.control.update.task;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.CertificateToolsProvider;
/*     */ import com.daimler.cebas.certificates.control.CertificateUtil;
/*     */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*     */ import com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory;
/*     */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateSteps;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*     */ import com.daimler.cebas.certificates.control.validation.failure.CSRValidationFailureOutput;
/*     */ import com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput;
/*     */ import com.daimler.cebas.certificates.control.validation.failure.ValidationState;
/*     */ import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
/*     */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
/*     */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*     */ import com.daimler.cebas.certificates.integration.vo.EnhancedRightsPermission;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKIEnhancedCertificateRequest;
/*     */ import com.daimler.cebas.certificates.integration.vo.Permission;
/*     */ import com.daimler.cebas.common.ObjectIdentifier;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Base64;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public abstract class CreateEnhancedCSRsTask<T extends PublicKeyInfrastructureEsi>
/*     */   extends CreateCSRsTask<T>
/*     */ {
/*  52 */   private static final String CLASS_NAME = CreateEnhancedCSRsTask.class.getSimpleName();
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractCertificateFactory factory;
/*     */ 
/*     */ 
/*     */   
/*     */   protected CertificatesConfiguration profileConfiguration;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public CreateEnhancedCSRsTask(CertificateToolsProvider toolsProvider, T publicKeyInfrastructureEsi, Session session, DefaultUpdateSession updateSession, Logger logger, CertificatesConfiguration profileConfiguration) {
/*  67 */     super(toolsProvider, publicKeyInfrastructureEsi, toolsProvider.getSearchEngine(), session, updateSession, logger);
/*  68 */     this.certificateSignRequestEngine = toolsProvider.getCertificateSignRequestEngine();
/*  69 */     this.deleteCertificatesEngine = toolsProvider.getDeleteCertificatesEngine();
/*  70 */     this.session = session;
/*  71 */     this.factory = toolsProvider.getFactory();
/*  72 */     this.profileConfiguration = profileConfiguration;
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
/*     */   public List<PKIEnhancedCertificateRequest> execute(List<? extends ImportResult> importResults, List<Permission> permissions, UpdateType updateType) {
/*  87 */     if (this.updateSession.isRunning()) {
/*  88 */       this.updateSession.updateStep(UpdateSteps.CREATE_ENHANCED_CSRS, "updateStartCreateEnhancedCSR", updateType);
/*  89 */       this.logger.log(Level.INFO, "000261", this.i18n
/*  90 */           .getEnglishMessage("updateStartCreateEnhancedCSR", new String[] {
/*  91 */               updateType.name()
/*     */             }), CLASS_NAME);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  97 */       List<String> bSKIs = (List<String>)importResults.stream().filter(result -> !StringUtils.isEmpty(result.getAuthorityKeyIdentifier())).map(ImportResult::getSubjectKeyIdentifier).collect(Collectors.toList());
/*  98 */       List<PKIEnhancedCertificateRequest> enhCertificateRequests = getEnhancedRightsRequests(bSKIs, permissions, updateType);
/*     */       
/* 100 */       this.logger.log(Level.INFO, "000262", this.i18n.getEnglishMessage("updateStopCreateEnhancedCSR", new String[] { updateType.name() }), CLASS_NAME);
/* 101 */       this.updateSession.updateStep(UpdateSteps.CREATE_ENHANCED_CSRS, "updateStopCreateEnhancedCSR", updateType);
/* 102 */       this.updateSession.addStepResult(UpdateSteps.CREATE_ENHANCED_CSRS, enhCertificateRequests);
/* 103 */       this.updateSession.getPkiCertificateRequests().addAll(enhCertificateRequests);
/* 104 */       return enhCertificateRequests;
/*     */     } 
/* 106 */     return Collections.emptyList();
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
/*     */   private List<PKIEnhancedCertificateRequest> getEnhancedRightsRequests(List<String> bSKIs, List<Permission> permissions, UpdateType updateType) {
/* 121 */     List<PKIEnhancedCertificateRequest> enhCertificateRequests = new ArrayList<>();
/* 122 */     permissions.stream().filter(permission -> (permission.getServices() != null))
/* 123 */       .forEach(permission -> this.searchEngine.findDiagnosticCertificatesByBackendSKIs(this.session.getCurrentUser(), bSKIs, getUserRoleFromPermission(permission.getUserRole()), CertificateUtil.getSortedCommaSeparatedList(permission.getTargetECU()), CertificateUtil.getSortedCommaSeparatedList(permission.getTargetVIN())).stream().filter(()).forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     return enhCertificateRequests;
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
/*     */   protected PKIEnhancedCertificateRequest createCSRUnderDiag(Permission permission, Certificate diag, List<Certificate> certificatesUnderDiag, EnhancedRightsPermission enhPermission, boolean checkPermission, UpdateType updateType, List<String> targetECUs, List<String> targetVINs) {
/* 160 */     String userRole = null;
/* 161 */     String nonce = null;
/* 162 */     String uniqueECUID = null;
/* 163 */     String specialECU = null;
/* 164 */     String targetSubjKeyIdentifier = null;
/* 165 */     String ecu = CertificateUtil.getCommaSeparatedStringFromList(targetECUs);
/* 166 */     String vin = CertificateUtil.getCommaSeparatedStringFromList(targetVINs);
/* 167 */     String services = CertificateUtil.getServicesFromEnhRightsPermission(enhPermission.getServiceIds());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     CertificateSignRequest certificateSignRequest = new CertificateSignRequest("CN=" + this.session.getCurrentUser().getUserName(), CertificateType.ENHANCED_RIGHTS_CERTIFICATE.name(), userRole, ecu, vin, nonce, services, uniqueECUID, specialECU, targetSubjKeyIdentifier, CertificateParser.hexToBase64(diag.getAuthorityKeyIdentifier()), permission.getValidity(), diag.getEntityId(), this.session.getCurrentUser().getEntityId(), ObjectIdentifier.ALGORITHM_IDENTIFIER_OID.getOid(), "0.1", getProdQualifier(), "");
/* 175 */     Certificate createdCSR = this.certificateSignRequestEngine.createCertificateInSignRequestState(certificateSignRequest, checkPermission, (ValidationFailureOutput)new CSRValidationFailureOutput((ValidationState)enhPermission, (ValidationState)certificateSignRequest, this.logger), true);
/*     */ 
/*     */ 
/*     */     
/* 179 */     if (!enhPermission.isValid()) {
/* 180 */       return null;
/*     */     }
/*     */     
/* 183 */     if (enhPermission.getEnrollmentId() == null) {
/* 184 */       this.logger.log(Level.SEVERE, "000299X", "enrollmentId field is missing from Enhanced Rights permission with ecu: [" + ecu + "], vin: [" + vin + "] and services: [" + services + "]", CLASS_NAME);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     if (null != updateType) {
/* 191 */       this.logger.log(Level.FINE, "000232", this.i18n.getEnglishMessage("updateCreatedCSRWithAKIAndPKIRole", new String[] { updateType
/* 192 */               .name(), createdCSR.getAuthorityKeyIdentifier(), createdCSR.getPKIRole() }), CLASS_NAME);
/* 193 */       if (UpdateType.FULL.equals(updateType)) {
/* 194 */         deleteNonIdenticalCertificates(certificatesUnderDiag, createdCSR, updateType);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     PKIEnhancedCertificateRequest pkiEnhancedCertificateRequest = new PKIEnhancedCertificateRequest(enhPermission.getEnrollmentId(), createdCSR.getPkcs10Signature(), diag.getParent().getSubjectKeyIdentifier().replace("-", "").toLowerCase(), createdCSR.getType().name(), Base64.getEncoder().encodeToString(this.factory.getCertificateBytes(diag)));
/* 202 */     pkiEnhancedCertificateRequest.setInternalCSRId(createdCSR.getEntityId());
/* 203 */     return pkiEnhancedCertificateRequest;
/*     */   }
/*     */   
/*     */   protected abstract List<PKIEnhancedCertificateRequest> processPermissionsUnderDiag(Permission paramPermission, Certificate paramCertificate, UpdateType paramUpdateType);
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\contro\\update\task\CreateEnhancedCSRsTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */