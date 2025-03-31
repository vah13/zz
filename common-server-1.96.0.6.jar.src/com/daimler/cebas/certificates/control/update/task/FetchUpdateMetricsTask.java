/*     */ package com.daimler.cebas.certificates.control.update.task;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateDetails;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateStatus;
/*     */ import com.daimler.cebas.certificates.control.vo.UpdateCertificateMetrics;
/*     */ import com.daimler.cebas.certificates.control.vo.UpdateCertificatesRetryInfo;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
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
/*     */ public class FetchUpdateMetricsTask
/*     */ {
/*     */   private static final String EMPTY = "";
/*     */   private DefaultUpdateSession updateSession;
/*     */   protected MetadataManager i18n;
/*     */   
/*     */   @Autowired
/*     */   public FetchUpdateMetricsTask(DefaultUpdateSession updateSession, MetadataManager i18n) {
/*  46 */     this.updateSession = updateSession;
/*  47 */     this.i18n = i18n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UpdateCertificateMetrics getUpdateCertificatesMetrics() {
/*     */     UpdateCertificateMetrics updateCertificateMetrics;
/*  56 */     UpdateStatus status = this.updateSession.getStatus();
/*     */ 
/*     */     
/*  59 */     boolean isRunningOrNotRunningAndDetailsNotEmpty = (status == UpdateStatus.RUNNING || (status == UpdateStatus.NOT_RUNNING && !this.updateSession.isDetailsEmpty()));
/*     */ 
/*     */     
/*  62 */     boolean isMetricsAvailble = (isRunningOrNotRunningAndDetailsNotEmpty || (status == UpdateStatus.PAUSED && !this.updateSession.isDetailsEmpty()));
/*     */     
/*  64 */     if (isMetricsAvailble) {
/*  65 */       UpdateDetails currentDetails = this.updateSession.getCurrentDetails();
/*  66 */       if (currentDetails == null) {
/*  67 */         UpdateCertificatesRetryInfo composeUpdateCertificatesRetryInfo = composeUpdateCertificatesRetryInfo();
/*  68 */         return new UpdateCertificateMetrics(
/*  69 */             (composeUpdateCertificatesRetryInfo
/*  70 */             .getCurrentRetry().intValue() != 0), "", "", "", composeUpdateCertificatesRetryInfo, this.updateSession
/*     */             
/*  72 */             .didFailAllRetries(), this.updateSession
/*  73 */             .isRunning());
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  84 */       updateCertificateMetrics = new UpdateCertificateMetrics(true, currentDetails.isError(), this.i18n.getMessage(this.updateSession.getStatus().getText()), this.i18n.getMessage(currentDetails.getStep().getText()), this.i18n.getMessage(currentDetails.getMessage(), getMessageArguments(currentDetails.getMessageArguments())), composeUpdateCertificatesRetryInfo(), this.updateSession.didFailAllRetries(), this.updateSession.isRunning());
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  89 */       updateCertificateMetrics = new UpdateCertificateMetrics(false, "", "", "", null, this.updateSession.didFailAllRetries(), this.updateSession.isRunning());
/*     */     } 
/*  91 */     return updateCertificateMetrics;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UpdateCertificatesRetryInfo composeUpdateCertificatesRetryInfo() {
/* 101 */     return new UpdateCertificatesRetryInfo(this.updateSession.getRetryUrl(), this.updateSession
/* 102 */         .getMaxRetries(), this.updateSession.getCurrentRetry(), this.updateSession
/* 103 */         .getNextRetryTimeInterval(), this.updateSession
/* 104 */         .getNextRetryTimeStamp());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String[] getMessageArguments(String[] currentArgs) {
/* 114 */     List<String> args = new ArrayList<>();
/* 115 */     args.add(0, this.updateSession.getUpdateType().name());
/* 116 */     if (currentArgs != null) {
/* 117 */       Collections.addAll(args, currentArgs);
/*     */     }
/* 119 */     return args.<String>toArray(new String[args.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\contro\\update\task\FetchUpdateMetricsTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */