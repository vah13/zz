/*     */ package com.daimler.cebas.certificates.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.config.handlers.IDeleteCertificateHandler;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*     */ import com.daimler.cebas.certificates.control.validation.CertificateInStoreValidator;
/*     */ import com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo;
/*     */ import com.daimler.cebas.certificates.control.vo.DeleteCertificatesResult;
/*     */ import com.daimler.cebas.certificates.control.vo.ExtendedDeleteCertificatesResult;
/*     */ import com.daimler.cebas.certificates.entity.BackendContext;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateState;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeleteCertificateHandlerDefault
/*     */   implements IDeleteCertificateHandler
/*     */ {
/*     */   private DeleteCertificatesEngine engine;
/*     */   protected Logger logger;
/*     */   protected MetadataManager i18n;
/*     */   
/*     */   public DeleteCertificateHandlerDefault(DeleteCertificatesEngine engine, Logger logger, MetadataManager i18n) {
/*  32 */     this.engine = engine;
/*  33 */     this.logger = logger;
/*  34 */     this.i18n = i18n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteCertificate(List<String> ids, boolean duringUpdateSession, User currentUser, List<DeleteCertificatesInfo> deleteCertificatesInfo, List<Certificate> roots, Certificate certificate) {
/*  41 */     if (certificate.getState().equals(CertificateState.ISSUED)) {
/*  42 */       deleteCertificatesInfo.add(createDeleteCertificatesInfoForCertificate(certificate));
/*     */     } else {
/*  44 */       deleteCertificatesInfo.add(createDeleteCertificatesInfoForCSR(certificate));
/*     */     } 
/*  46 */     if (CertificateInStoreValidator.isInStore(certificate, currentUser.getEntityId(), this.logger)) {
/*  47 */       boolean parentExistInList = checkIfParentShouldBeDeleted(certificate, ids);
/*  48 */       if (!parentExistInList) {
/*  49 */         this.engine.deleteCertificateForUser(certificate, currentUser);
/*  50 */         if (certificate.getParent() == null) {
/*  51 */           roots.add(certificate);
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/*  56 */       CEBASCertificateException zenzefiCertificateException = new CEBASCertificateException(this.i18n.getMessage("certificateDoesNotExistInUserStore", new String[] {
/*  57 */               certificate.getEntityId()
/*     */             }), "certificateDoesNotExistInUserStore");
/*  59 */       this.logger.log(Level.WARNING, "000104X", this.i18n
/*  60 */           .getEnglishMessage("certificateDoesNotExistInUserStore", new String[] {
/*  61 */               certificate.getEntityId()
/*  62 */             }), getClass().getSimpleName());
/*  63 */       throw zenzefiCertificateException;
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
/*     */   public ExtendedDeleteCertificatesResult createSuccessDeleteCertificateResult(DeleteCertificatesInfo deleteCertificatesInfo) {
/*  80 */     DeleteCertificatesResult deleteCertificatesResult = new DeleteCertificatesResult(deleteCertificatesInfo.getCertificateId(), deleteCertificatesInfo.isCertificate(), deleteCertificatesInfo.getCertificateType(), true, createDeleteResultMessage(deleteCertificatesInfo), deleteCertificatesInfo.getSerialNo(), deleteCertificatesInfo.getSubjectKeyIdentifier(), deleteCertificatesInfo.getAuthKeyIdentifier());
/*     */     
/*  82 */     return new ExtendedDeleteCertificatesResult(deleteCertificatesResult, deleteCertificatesInfo.getZkNo(), deleteCertificatesInfo.getEcuPackageTs());
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
/*     */   public String createDeleteResultMessage(DeleteCertificatesInfo entry) {
/*  94 */     CertificateType certificateType = CertificateType.getTypeForLogging(entry);
/*  95 */     if (StringUtils.isEmpty(entry.getZkNo())) {
/*  96 */       return this.i18n.getEnglishMessage("deleteCertificateUserAction", new String[] { this.i18n
/*  97 */             .getEnglishMessage(certificateType.getLanguageProperty()), entry.getCertificateId(), entry
/*  98 */             .getSerialNo(), entry.getSubjectKeyIdentifier(), entry.getAuthKeyIdentifier() });
/*     */     }
/* 100 */     return this.i18n.getEnglishMessage("deleteCertificateUserActionWithPN", new String[] { this.i18n
/* 101 */           .getEnglishMessage(certificateType.getLanguageProperty()), entry.getCertificateId(), entry
/* 102 */           .getSerialNo(), entry.getSubjectKeyIdentifier(), entry.getAuthKeyIdentifier(), entry.getZkNo() });
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
/*     */   public String createDeleteCSRResultMessage(DeleteCertificatesInfo entry) {
/* 115 */     return this.i18n.getEnglishMessage("deleteCSRUserAction", new String[] { this.i18n
/* 116 */           .getEnglishMessage(entry.getCertificateType().getLanguageProperty()), entry
/* 117 */           .getCertificateId(), entry.getAuthKeyIdentifier(), entry.getPublicKey() });
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
/*     */   public ExtendedDeleteCertificatesResult createFailDeleteCertificateByIdResult(String notFoundId) {
/* 130 */     DeleteCertificatesResult deleteCertificatesResult = new DeleteCertificatesResult(notFoundId, false, null, false, this.i18n.getEnglishMessage("certificateDoesNotExistInUserStore", new String[] { notFoundId }), null, null, null);
/*     */ 
/*     */     
/* 133 */     return new ExtendedDeleteCertificatesResult(deleteCertificatesResult, null, null);
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
/*     */   public ExtendedDeleteCertificatesResult createFailDeleteCertificateByAuthKeyAndSnResult(String currentAuthKeyIdentifier, String currentSerialNumber) {
/* 149 */     DeleteCertificatesResult deleteCertificatesResult = new DeleteCertificatesResult(null, false, null, false, this.i18n.getEnglishMessage("searchedCertificateDoesNotExistInUserStore", new String[] { currentAuthKeyIdentifier, currentSerialNumber }), currentSerialNumber, currentAuthKeyIdentifier, null);
/*     */ 
/*     */     
/* 152 */     return new ExtendedDeleteCertificatesResult(deleteCertificatesResult, null, null);
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
/*     */   public ExtendedDeleteCertificatesResult createSuccessDeleteCSRResult(DeleteCertificatesInfo deleteCertificatesInfo) {
/* 167 */     DeleteCertificatesResult deleteCertificatesResult = new DeleteCertificatesResult(deleteCertificatesInfo.getCertificateId(), deleteCertificatesInfo.isCertificate(), deleteCertificatesInfo.getCertificateType(), true, createDeleteCSRResultMessage(deleteCertificatesInfo), deleteCertificatesInfo.getAuthKeyIdentifier(), deleteCertificatesInfo.getPublicKey());
/*     */     
/* 169 */     return new ExtendedDeleteCertificatesResult(deleteCertificatesResult, deleteCertificatesInfo.getZkNo(), deleteCertificatesInfo.getEcuPackageTs());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DeleteCertificatesInfo createDeleteCertificatesInfoForCertificate(Certificate certificate) {
/* 180 */     BackendContext backendContext = certificate.getBackendContext();
/* 181 */     return new DeleteCertificatesInfo(certificate.getEntityId(), true, certificate.getType(), certificate
/* 182 */         .getSerialNo(), certificate.getSubjectKeyIdentifier(), certificate
/* 183 */         .getAuthorityKeyIdentifier(), certificate.getTargetSubjectKeyIdentifier(), backendContext
/* 184 */         .getZkNo(), backendContext.getEcuPackageTs());
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
/*     */   private boolean checkIfParentShouldBeDeleted(Certificate certificate, List<String> ids) {
/* 197 */     Certificate crtCertificate = certificate;
/* 198 */     while (crtCertificate.getParent() != null) {
/* 199 */       if (ids.contains(crtCertificate.getParent().getEntityId())) {
/* 200 */         return true;
/*     */       }
/* 202 */       crtCertificate = crtCertificate.getParent();
/*     */     } 
/* 204 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DeleteCertificatesInfo createDeleteCertificatesInfoForCSR(Certificate certificate) {
/* 215 */     return new DeleteCertificatesInfo(certificate.getEntityId(), false, certificate.getType(), certificate
/* 216 */         .getParent().getSubjectKeyIdentifier(), certificate.getSubjectPublicKey());
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\DeleteCertificateHandlerDefault.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */