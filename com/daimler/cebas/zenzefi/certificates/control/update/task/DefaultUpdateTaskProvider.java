/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.update.task;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.update.task.AbstractUpdateEcuLinkMarkersTask;
/*     */ import com.daimler.cebas.certificates.control.update.task.UpdateBackendsTask;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.CollectTimeAndSecocisCSRsTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.CreateCSRsUnderBackendTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadBackendsTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadCertificatesUnderBackendTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadEnhancedCertificatesTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadLinkCertificatesTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadNonVsmECUCertificateTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadPermissionsTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadTimeAndSecocisTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.IUpdateTaskProvider;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.UpdateRenewalPeriodTask;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.task.ZenzefiCreateEnhancedCSRsTask;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASControl
/*     */ public class DefaultUpdateTaskProvider
/*     */   implements IUpdateTaskProvider
/*     */ {
/*     */   private DownloadBackendsTask downloadBackendsTask;
/*     */   private DownloadPermissionsTask downloadPermissionsTask;
/*     */   private CreateCSRsUnderBackendTask createCSRsUnderBackendTask;
/*     */   private DownloadCertificatesUnderBackendTask downloadCertificatesTask;
/*     */   private ZenzefiCreateEnhancedCSRsTask createEnhancedCSRsTask;
/*     */   private DownloadEnhancedCertificatesTask downloadEnhancedCertificatesTask;
/*     */   private CollectTimeAndSecocisCSRsTask collectTimeAndSecocisCSRTask;
/*     */   private DownloadTimeAndSecocisTask downloadTimeAndSecocisTask;
/*     */   private UpdateBackendsTask updateBackendsTask;
/*     */   private UpdateRenewalPeriodTask updateRenewalPeriodTask;
/*     */   private DownloadLinkCertificatesTask downloadLinkCertificatesTask;
/*     */   private DownloadNonVsmECUCertificateTask downloadNonVsmECUCertificateTask;
/*     */   private AbstractUpdateEcuLinkMarkersTask updateEcuLinkMarkersTask;
/*     */   
/*     */   public DefaultUpdateTaskProvider(DownloadBackendsTask downloadBackendsTask, DownloadPermissionsTask downloadPermissionsTask, CreateCSRsUnderBackendTask createCSRsUnderBackendTask, DownloadCertificatesUnderBackendTask downloadCertificatesTask, ZenzefiCreateEnhancedCSRsTask createEnhancedCSRsTask, DownloadEnhancedCertificatesTask downloadEnhancedCertificatesTask, CollectTimeAndSecocisCSRsTask collectTimeAndSecocisCSRTask, AbstractUpdateEcuLinkMarkersTask updateEcuLinkMarkersTask, DownloadTimeAndSecocisTask downloadTimeAndSecocisTask, UpdateBackendsTask updateBackendsTask, UpdateRenewalPeriodTask updateRenewalPeriodTask, DownloadLinkCertificatesTask downloadLinkCertificatesTask, DownloadNonVsmECUCertificateTask downloadNonVsmECUCertificateTask) {
/* 108 */     this.downloadBackendsTask = downloadBackendsTask;
/* 109 */     this.downloadPermissionsTask = downloadPermissionsTask;
/* 110 */     this.createCSRsUnderBackendTask = createCSRsUnderBackendTask;
/* 111 */     this.downloadCertificatesTask = downloadCertificatesTask;
/* 112 */     this.createEnhancedCSRsTask = createEnhancedCSRsTask;
/* 113 */     this.downloadEnhancedCertificatesTask = downloadEnhancedCertificatesTask;
/* 114 */     this.collectTimeAndSecocisCSRTask = collectTimeAndSecocisCSRTask;
/* 115 */     this.updateEcuLinkMarkersTask = updateEcuLinkMarkersTask;
/* 116 */     this.downloadTimeAndSecocisTask = downloadTimeAndSecocisTask;
/* 117 */     this.updateBackendsTask = updateBackendsTask;
/* 118 */     this.updateRenewalPeriodTask = updateRenewalPeriodTask;
/* 119 */     this.downloadLinkCertificatesTask = downloadLinkCertificatesTask;
/* 120 */     this.downloadNonVsmECUCertificateTask = downloadNonVsmECUCertificateTask;
/*     */   }
/*     */   
/*     */   public CreateCSRsUnderBackendTask getCreateCSRsUnderBackendTask() {
/* 124 */     return this.createCSRsUnderBackendTask;
/*     */   }
/*     */   
/*     */   public ZenzefiCreateEnhancedCSRsTask getCreateEnhancedCSRsTask() {
/* 128 */     return this.createEnhancedCSRsTask;
/*     */   }
/*     */   
/*     */   public DownloadBackendsTask getDownloadBackendsTask() {
/* 132 */     return this.downloadBackendsTask;
/*     */   }
/*     */   
/*     */   public DownloadCertificatesUnderBackendTask getDownloadCertificatesTask() {
/* 136 */     return this.downloadCertificatesTask;
/*     */   }
/*     */   
/*     */   public DownloadEnhancedCertificatesTask getDownloadEnhancedCertificatesTask() {
/* 140 */     return this.downloadEnhancedCertificatesTask;
/*     */   }
/*     */   
/*     */   public DownloadPermissionsTask getDownloadPermissionsTask() {
/* 144 */     return this.downloadPermissionsTask;
/*     */   }
/*     */ 
/*     */   
/*     */   public CollectTimeAndSecocisCSRsTask getCollectTimeAndSecocisCSR() {
/* 149 */     return this.collectTimeAndSecocisCSRTask;
/*     */   }
/*     */ 
/*     */   
/*     */   public DownloadTimeAndSecocisTask getDownloadTimeAndSecocisTask() {
/* 154 */     return this.downloadTimeAndSecocisTask;
/*     */   }
/*     */ 
/*     */   
/*     */   public UpdateBackendsTask getUpdateBackendTask() {
/* 159 */     return this.updateBackendsTask;
/*     */   }
/*     */ 
/*     */   
/*     */   public UpdateRenewalPeriodTask getUpdateRenewalPeriodTask() {
/* 164 */     return this.updateRenewalPeriodTask;
/*     */   }
/*     */ 
/*     */   
/*     */   public DownloadLinkCertificatesTask getDownloadLinkCertificatesTask() {
/* 169 */     return this.downloadLinkCertificatesTask;
/*     */   }
/*     */ 
/*     */   
/*     */   public DownloadNonVsmECUCertificateTask getDownloadNonVsmEcuCertificateTask() {
/* 174 */     return this.downloadNonVsmECUCertificateTask;
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractUpdateEcuLinkMarkersTask getAbstractUpdateEcuLinkMarkersTask() {
/* 179 */     return this.updateEcuLinkMarkersTask;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\contro\\update\task\DefaultUpdateTaskProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */