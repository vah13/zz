/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.CertificateToolsProvider;
/*    */ import com.daimler.cebas.certificates.control.SearchEngine;
/*    */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*    */ import com.daimler.cebas.certificates.control.update.task.CreateCSRsTask;
/*    */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*    */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASControl
/*    */ public abstract class ZenzefiCreateCSRsTask
/*    */   extends CreateCSRsTask<ZenZefiPublicKeyInfrastructureEsi>
/*    */ {
/*    */   @Value("${prod.qualifier}")
/*    */   protected String prodQualifierValue;
/*    */   
/*    */   @Autowired
/*    */   public ZenzefiCreateCSRsTask(CertificateToolsProvider toolsProvider, ZenZefiPublicKeyInfrastructureEsi publicKeyInfrastructureEsi, SearchEngine searchEngine, Session session, UpdateSession updateSession, Logger logger) {
/* 28 */     super(toolsProvider, (PublicKeyInfrastructureEsi)publicKeyInfrastructureEsi, searchEngine, session, (DefaultUpdateSession)updateSession, logger);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getProdQualifier() {
/* 33 */     return this.prodQualifierValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\ZenzefiCreateCSRsTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */