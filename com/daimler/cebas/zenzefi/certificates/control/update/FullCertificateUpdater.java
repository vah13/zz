/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.AbstractZenzefiCertificatesUpdater;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.IUpdateTaskProvider;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.providers.FullUpdateTaskProvider;
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
/*    */ public class FullCertificateUpdater
/*    */   extends AbstractZenzefiCertificatesUpdater
/*    */ {
/*    */   @Autowired
/*    */   public FullCertificateUpdater(Logger logger, MetadataManager i18n, UpdateSession updateSession, FullUpdateTaskProvider taskProvider) {
/* 28 */     super(logger, i18n, updateSession, (IUpdateTaskProvider)taskProvider);
/* 29 */     setUpdateType(UpdateType.FULL);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\FullCertificateUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */