/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.CertificateToolsProvider;
/*    */ import com.daimler.cebas.certificates.control.CertificateUtil;
/*    */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*    */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
/*    */ import com.daimler.cebas.certificates.integration.vo.EnhancedRightsPermission;
/*    */ import com.daimler.cebas.certificates.integration.vo.PKIEnhancedCertificateRequest;
/*    */ import com.daimler.cebas.certificates.integration.vo.Permission;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.ZenzefiCreateEnhancedCSRsTask;
/*    */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASControl
/*    */ public class DifferentialUpdateCreateEnhancedCSRsTask
/*    */   extends ZenzefiCreateEnhancedCSRsTask
/*    */ {
/*    */   @Autowired
/*    */   public DifferentialUpdateCreateEnhancedCSRsTask(CertificateToolsProvider toolsProvider, ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, Session session, UpdateSession updateSession, Logger logger, CertificatesConfiguration profileConfiguration) {
/* 47 */     super(toolsProvider, publicKeyInfrastructureEsi, session, updateSession, logger, profileConfiguration);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected List<PKIEnhancedCertificateRequest> processPermissionsUnderDiag(Permission permission, Certificate diag, UpdateType updateType) {
/* 55 */     List<PKIEnhancedCertificateRequest> enhRequests = new ArrayList<>();
/* 56 */     List<Certificate> certificatesUnderDiag = this.searchEngine.findEnhancedRightsCertificates(this.session.getCurrentUser(), diag
/* 57 */         .getParent().getSubjectKeyIdentifier(), diag.getSerialNo());
/* 58 */     permission.getServices().forEach(enhPermission -> {
/*    */           Optional<Certificate> foundCertificateOptional = certificatesUnderDiag.stream().filter(()).findAny();
/*    */           
/*    */           if (!foundCertificateOptional.isPresent() || isCertificateValidityExceeded(updateType, foundCertificateOptional.get(), permission.getRenewal(), com.daimler.cebas.zenzefi.certificates.control.update.task.DifferentialUpdateCreateEnhancedCSRsTask.class.getSimpleName())) {
/*    */             PKIEnhancedCertificateRequest pkiEnhRequest = createCSRUnderDiag(permission, diag, certificatesUnderDiag, enhPermission, true, updateType, enhPermission.getTargetECU(), enhPermission.getTargetVIN());
/*    */             if (pkiEnhRequest != null) {
/*    */               enhRequests.add(pkiEnhRequest);
/*    */             }
/*    */           } 
/*    */         });
/* 68 */     return enhRequests;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\DifferentialUpdateCreateEnhancedCSRsTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */