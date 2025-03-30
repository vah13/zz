/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.AbstractZenzefiCertificatesUpdater;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.IUpdateTaskProvider;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.providers.DifferentialUpdateTaskProvider;
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
/*    */ @CEBASControl
/*    */ public class DifferentialCertificateUpdater
/*    */   extends AbstractZenzefiCertificatesUpdater
/*    */ {
/*    */   @Autowired
/*    */   public DifferentialCertificateUpdater(Logger logger, MetadataManager i18n, UpdateSession updateSession, DifferentialUpdateTaskProvider taskProvider) {
/* 30 */     super(logger, i18n, updateSession, (IUpdateTaskProvider)taskProvider);
/* 31 */     setUpdateType(UpdateType.DIFFERENTIAL);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\DifferentialCertificateUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */