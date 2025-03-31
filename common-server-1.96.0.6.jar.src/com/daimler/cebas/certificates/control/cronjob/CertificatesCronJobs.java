/*     */ package com.daimler.cebas.certificates.control.cronjob;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*     */ import com.daimler.cebas.certificates.control.repository.CertificateRepository;
/*     */ import com.daimler.cebas.common.control.CEBASProperty;
/*     */ import com.daimler.cebas.common.control.RequestMetadata;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.configuration.control.CeBASGeneralPropertiesManager;
/*     */ import com.daimler.cebas.configuration.entity.Configuration;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.logging.Level;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Qualifier;
/*     */ import org.springframework.scheduling.TaskScheduler;
/*     */ import org.springframework.scheduling.Trigger;
/*     */ import org.springframework.scheduling.support.CronTrigger;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class CertificatesCronJobs
/*     */ {
/*  39 */   private static final String CLASS_NAME = CertificatesCronJobs.class
/*  40 */     .getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CertificateRepository certificateRepository;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CeBASGeneralPropertiesManager generalPropertiesManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   @Qualifier("taskScheduler")
/*     */   private TaskScheduler taskScheduler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Session session;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ScheduledFuture<?> schedule;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final CertificatesCleanUp cleanUpCertificates;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RequestMetadata i18n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public CertificatesCronJobs(CertificateRepository certificateRepository, Logger logger, Session session, CertificatesCleanUp cleanUpCertificates, RequestMetadata i18n, @Qualifier("General") CeBASGeneralPropertiesManager generalPropertiesManager) {
/* 106 */     this.certificateRepository = certificateRepository;
/* 107 */     this.logger = logger;
/* 108 */     this.session = session;
/* 109 */     this.cleanUpCertificates = cleanUpCertificates;
/* 110 */     this.i18n = i18n;
/* 111 */     this.generalPropertiesManager = generalPropertiesManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRED)
/*     */   public void scheduleDeleteExpiredCertificatesForAllUsers() {
/* 119 */     String METHOD_NAME = "scheduleDeleteExpiredCertificatesForAllUsers";
/* 120 */     this.logger.entering(CLASS_NAME, "scheduleDeleteExpiredCertificatesForAllUsers");
/* 121 */     User defaultUser = this.session.getDefaultUser();
/* 122 */     String deleteCertsSchedule = getDeleteExpiredCertsSchedule(defaultUser);
/*     */     
/* 124 */     if (this.schedule != null) {
/* 125 */       this.schedule.cancel(true);
/*     */     }
/* 127 */     CronTrigger cronTrigger = new CronTrigger(deleteCertsSchedule);
/* 128 */     Runnable task = this.cleanUpCertificates::executeCronJobTask;
/* 129 */     this.schedule = this.taskScheduler.schedule(task, (Trigger)cronTrigger);
/* 130 */     this.logger.log(Level.INFO, "000157", "Current automatic delete Cron Job set to: " + cronTrigger
/*     */         
/* 132 */         .getExpression(), CLASS_NAME);
/*     */     
/* 134 */     this.logger.exiting(CLASS_NAME, "scheduleDeleteExpiredCertificatesForAllUsers");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getDeleteExpiredCertsSchedule(User defaultUser) {
/* 146 */     String deleteCertsScheduleGeneralProperties = this.generalPropertiesManager.getProperty(CEBASProperty.DELETE_CERTS_SCHEDULE.name());
/* 147 */     if (StringUtils.isEmpty(deleteCertsScheduleGeneralProperties)) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 152 */       Optional<Configuration> deleteExpiredCertScheduleConfig = defaultUser.getConfigurations().stream().filter(config -> config.getConfigKey().equals(CEBASProperty.DELETE_CERTS_SCHEDULE.name())).findFirst();
/*     */       
/* 154 */       Configuration deleteExpiredCertsSchedule = deleteExpiredCertScheduleConfig.<Throwable>orElseThrow(() -> new CEBASCertificateException("000001X, " + this.i18n.getMessage("configNotFoundForDeleting")));
/*     */ 
/*     */ 
/*     */       
/* 158 */       return deleteExpiredCertsSchedule.getConfigValue();
/*     */     } 
/* 160 */     return deleteCertsScheduleGeneralProperties;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\cronjob\CertificatesCronJobs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */