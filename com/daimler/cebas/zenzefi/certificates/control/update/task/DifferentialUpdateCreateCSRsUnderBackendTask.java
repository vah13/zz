/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.CertificateToolsProvider;
/*    */ import com.daimler.cebas.certificates.control.CertificateUtil;
/*    */ import com.daimler.cebas.certificates.control.SearchEngine;
/*    */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
/*    */ import com.daimler.cebas.certificates.entity.CertificateType;
/*    */ import com.daimler.cebas.certificates.entity.PKIRole;
/*    */ import com.daimler.cebas.certificates.integration.vo.PKICertificateRequest;
/*    */ import com.daimler.cebas.certificates.integration.vo.Permission;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.CreateCSRsUnderBackendTask;
/*    */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
/*    */ import java.util.Optional;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ 
/*    */ 
/*    */ @CEBASControl
/*    */ public class DifferentialUpdateCreateCSRsUnderBackendTask
/*    */   extends CreateCSRsUnderBackendTask
/*    */ {
/*    */   @Autowired
/*    */   public DifferentialUpdateCreateCSRsUnderBackendTask(CertificateToolsProvider toolsProvider, ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, SearchEngine searchEngine, Session session, UpdateSession updateSession, Logger logger, MetadataManager i18n) {
/* 29 */     super(toolsProvider, publicKeyInfrastructureEsi, searchEngine, session, updateSession, logger, i18n);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Optional<PKICertificateRequest> createCSRAndPKICertificateRequestForBackend(Certificate backendCertificate, Permission permission, UpdateType updateType) {
/* 35 */     CertificateType certificateType = (CertificateType)PKIRole.getRoles().get(Integer.decode(permission.getPkiRole()));
/*    */ 
/*    */ 
/*    */     
/* 39 */     Optional<Certificate> foundCertificateOptional = this.searchEngine.findCertificatesUnderParentByType(this.session.getCurrentUser(), backendCertificate.getSubjectKeyIdentifier(), certificateType, Certificate.class).stream().filter(certificate -> CertificateUtil.isCertificateMatchingPermission(certificate, permission)).findAny();
/* 40 */     if (!foundCertificateOptional.isPresent() || isCertificateValidityExceeded(updateType, foundCertificateOptional.get(), permission.getRenewal(), com.daimler.cebas.zenzefi.certificates.control.update.task.DifferentialUpdateCreateCSRsUnderBackendTask.class
/* 41 */         .getSimpleName())) {
/* 42 */       return 
/* 43 */         Optional.ofNullable(createPKICertificateRequestFromCSR(backendCertificate, permission, false, updateType));
/*    */     }
/* 45 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\DifferentialUpdateCreateCSRsUnderBackendTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */