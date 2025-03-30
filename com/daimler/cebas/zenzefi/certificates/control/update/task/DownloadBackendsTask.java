/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*    */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*    */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*    */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*    */ import com.daimler.cebas.certificates.control.update.task.AbstractDownloadBackendsTask;
/*    */ import com.daimler.cebas.certificates.control.vo.DownloadBackendsResult;
/*    */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*    */ import com.daimler.cebas.certificates.integration.vo.Permission;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*    */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.transaction.annotation.Propagation;
/*    */ import org.springframework.transaction.annotation.Transactional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASControl
/*    */ public class DownloadBackendsTask
/*    */   extends AbstractDownloadBackendsTask
/*    */ {
/* 31 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadBackendsTask.class.getSimpleName();
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
/*    */   @Autowired
/*    */   public DownloadBackendsTask(ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, UpdateSession updateSession, Logger logger, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
/* 53 */     super((PublicKeyInfrastructureEsi)publicKeyInfrastructureEsi, importCertificatesEngine, (DefaultUpdateSession)updateSession, logger, i18n, CLASS_NAME, profileConfiguration);
/*    */   }
/*    */ 
/*    */   
/*    */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*    */   public DownloadBackendsResult execute(UpdateType updateType, boolean featureToggle, List<Permission> permissions) {
/* 59 */     return super.execute(updateType, featureToggle, permissions);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean allowImportOfPrivateKeys() {
/* 64 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\DownloadBackendsTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */