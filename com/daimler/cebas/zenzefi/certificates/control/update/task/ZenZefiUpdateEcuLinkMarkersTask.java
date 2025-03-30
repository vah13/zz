/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
/*    */ import com.daimler.cebas.certificates.control.SearchEngine;
/*    */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*    */ import com.daimler.cebas.certificates.control.update.task.AbstractUpdateEcuLinkMarkersTask;
/*    */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
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
/*    */ @CEBASControl
/*    */ public class ZenZefiUpdateEcuLinkMarkersTask
/*    */   extends AbstractUpdateEcuLinkMarkersTask<ZenZefiPublicKeyInfrastructureEsi>
/*    */ {
/*    */   @Autowired
/*    */   public ZenZefiUpdateEcuLinkMarkersTask(ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, DefaultUpdateSession updateSession, Logger logger, MetadataManager i18n, Session session, SearchEngine searchEngine) {
/* 30 */     super((PublicKeyInfrastructureEsi)publicKeyInfrastructureEsi, importCertificatesEngine, updateSession, logger, i18n, session, searchEngine, com.daimler.cebas.zenzefi.certificates.control.update.task.ZenZefiUpdateEcuLinkMarkersTask.class.getSimpleName());
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\ZenZefiUpdateEcuLinkMarkersTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */