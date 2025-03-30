/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task.providers;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.update.task.AbstractUpdateEcuLinkMarkersTask;
/*    */ import com.daimler.cebas.certificates.control.update.task.UpdateBackendsTask;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.CollectTimeAndSecocisCSRsTask;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.CreateCSRsUnderBackendTask;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DefaultUpdateTaskProvider;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DifferentialUpdateCreateCSRsUnderBackendTask;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DifferentialUpdateCreateEnhancedCSRsTask;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadBackendsTask;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadCertificatesUnderBackendTask;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadEnhancedCertificatesTask;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadLinkCertificatesTask;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadNonVsmECUCertificateTask;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadPermissionsTask;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadTimeAndSecocisTask;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.UpdateRenewalPeriodTask;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.task.ZenzefiCreateEnhancedCSRsTask;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASControl
/*    */ public class DifferentialUpdateTaskProvider
/*    */   extends DefaultUpdateTaskProvider
/*    */ {
/*    */   public DifferentialUpdateTaskProvider(DownloadBackendsTask downloadBackendsTask, DownloadPermissionsTask downloadPermissionsTask, DifferentialUpdateCreateCSRsUnderBackendTask createCSRsUnderBackendTask, DownloadCertificatesUnderBackendTask downloadCertificatesTask, DifferentialUpdateCreateEnhancedCSRsTask createEnhancedCSRsTask, DownloadEnhancedCertificatesTask downloadEnhancedCertificatesTask, CollectTimeAndSecocisCSRsTask collectTimeAndSecocisCSRTask, DownloadTimeAndSecocisTask downloadTimeAndSecocisTask, UpdateBackendsTask updateBackendsTask, UpdateRenewalPeriodTask updateRenewalPeriodTask, DownloadLinkCertificatesTask downloadLinkCertificatesTask, DownloadNonVsmECUCertificateTask downloadNonVsmECUCertificateTask, AbstractUpdateEcuLinkMarkersTask updateEcuLinkMarkersTask) {
/* 33 */     super(downloadBackendsTask, downloadPermissionsTask, (CreateCSRsUnderBackendTask)createCSRsUnderBackendTask, downloadCertificatesTask, (ZenzefiCreateEnhancedCSRsTask)createEnhancedCSRsTask, downloadEnhancedCertificatesTask, collectTimeAndSecocisCSRTask, updateEcuLinkMarkersTask, downloadTimeAndSecocisTask, updateBackendsTask, updateRenewalPeriodTask, downloadLinkCertificatesTask, downloadNonVsmECUCertificateTask);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\providers\DifferentialUpdateTaskProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */