/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.CertificateToolsProvider;
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
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASControl
/*    */ public class FullUpdateCreateEnhancedCSRsTask
/*    */   extends ZenzefiCreateEnhancedCSRsTask
/*    */ {
/*    */   @Autowired
/*    */   public FullUpdateCreateEnhancedCSRsTask(CertificateToolsProvider toolsProvider, ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, Session session, UpdateSession updateSession, Logger logger, CertificatesConfiguration profileConfiguration) {
/* 49 */     super(toolsProvider, publicKeyInfrastructureEsi, session, updateSession, logger, profileConfiguration);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected List<PKIEnhancedCertificateRequest> processPermissionsUnderDiag(Permission permission, Certificate diag, UpdateType updateType) {
/* 57 */     List<Certificate> certificatesUnderDiag = this.searchEngine.findCertificatesUnderParent(this.session.getCurrentUser(), diag, Certificate.class);
/*    */ 
/*    */     
/* 60 */     return (List<PKIEnhancedCertificateRequest>)permission
/* 61 */       .getServices().stream().map(enhPermission -> createCSRUnderDiag(permission, diag, certificatesUnderDiag, enhPermission, true, updateType, enhPermission.getTargetECU(), enhPermission.getTargetVIN()))
/*    */       
/* 63 */       .filter(Objects::nonNull).collect(Collectors.toList());
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\FullUpdateCreateEnhancedCSRsTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */