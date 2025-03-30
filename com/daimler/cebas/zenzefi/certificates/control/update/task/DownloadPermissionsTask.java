/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*    */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*    */ import com.daimler.cebas.certificates.control.update.task.DefaultDownloadPermissionsTask;
/*    */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*    */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ 
/*    */ 
/*    */ @CEBASControl
/*    */ public class DownloadPermissionsTask
/*    */   extends DefaultDownloadPermissionsTask<ZenZefiPublicKeyInfrastructureEsi>
/*    */ {
/*    */   @Autowired
/*    */   public DownloadPermissionsTask(ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, UpdateSession updateSession, Logger logger, MetadataManager i18n) {
/* 21 */     super((PublicKeyInfrastructureEsi)publicKeyInfrastructureEsi, importCertificatesEngine, (DefaultUpdateSession)updateSession, logger, i18n);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\DownloadPermissionsTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */