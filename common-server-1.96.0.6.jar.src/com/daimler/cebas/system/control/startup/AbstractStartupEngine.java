/*     */ package com.daimler.cebas.system.control.startup;
/*     */ 
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import com.daimler.cebas.certificates.control.cronjob.CertificatesCleanUp;
/*     */ import com.daimler.cebas.certificates.control.cronjob.CertificatesCronJobs;
/*     */ import com.daimler.cebas.certificates.control.importing.CertificatesImporter;
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.CEBASProperty;
/*     */ import com.daimler.cebas.common.control.ICEBASRecovery;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.system.control.SystemIntegrity;
/*     */ import com.daimler.cebas.system.control.performance.PerformanceAuditCronJob;
/*     */ import com.daimler.cebas.system.control.validation.AbstractConfigurationChecker;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.control.crypto.UserCryptoEngine;
/*     */ import com.daimler.cebas.users.control.factories.UserFactory;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Qualifier;
/*     */ import org.springframework.boot.context.event.ApplicationReadyEvent;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
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
/*     */ public abstract class AbstractStartupEngine
/*     */   implements ApplicationListener<ApplicationReadyEvent>
/*     */ {
/*  45 */   private static final String CLASS_NAME = AbstractStartupEngine.class.getSimpleName();
/*     */   
/*  47 */   public static final String[] LOGGERS = new String[] { "Root", "org.springframework.web", "org.hibernate", "com.daimler.cebas" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   protected Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   protected Session session;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   protected CertificatesImporter certificatesImporter;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private CertificatesCronJobs certificatesCronJobs;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private PerformanceAuditCronJob performanceAuditCronJob;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   protected AbstractConfigurator configurator;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private CertificatesCleanUp cleanUpCertificates;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private AbstractConfigurationChecker configurationChecker;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private ConfigurableEnvironment environment;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   protected MetadataManager i18n;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private UserCryptoEngine cryptoEngine;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   @Qualifier("taskScheduler")
/*     */   protected ThreadPoolTaskScheduler taskScheduler;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   protected Optional<ICEBASRecovery> cebasRecoveryOptional;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onApplicationEvent(ApplicationReadyEvent event) {
/* 125 */     String METHOD_NAME = "onApplicationEvent";
/* 126 */     this.logger.entering(CLASS_NAME, "onApplicationEvent");
/*     */     
/*     */     try {
/* 129 */       setLoggerLevel();
/*     */ 
/*     */ 
/*     */       
/* 133 */       this.configurationChecker.checkOverridingOfGeneralProperties();
/* 134 */       this.configurationChecker.checkConfiguration();
/*     */       
/* 136 */       UserFactory.setDefaultUsername(getDefaultUserName());
/* 137 */       if (this.session.userExists(UserFactory.getDefaultUsername())) {
/* 138 */         SystemIntegrity.checkSystemIntegrity(this.cryptoEngine, this.configurator, this.logger, this.session, this.i18n);
/* 139 */         this.configurator.updateDefaultUserConfigurations(this.session.getDefaultUser().getConfigurations());
/* 140 */         this.session.setDefaultUser();
/* 141 */         CompletableFuture.runAsync(() -> this.cleanUpCertificates.cleanUpCertificatesCurrentUser());
/*     */       } else {
/*     */         
/* 144 */         createDefaultUser();
/* 145 */         this.session.setDefaultUser();
/*     */       } 
/* 147 */       this.certificatesCronJobs.scheduleDeleteExpiredCertificatesForAllUsers();
/* 148 */       this.performanceAuditCronJob.scheduleCleanUpPerformanceEntries();
/* 149 */       this.logger.exiting(CLASS_NAME, "onApplicationEvent");
/* 150 */     } catch (CEBASException e) {
/* 151 */       close();
/* 152 */       throw e;
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setLoggerLevel() {
/* 170 */     String loggingLevel = this.configurator.readProperty(CEBASProperty.LOGGING_LEVEL.name());
/*     */     
/* 172 */     LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
/* 173 */     for (String loggerName : LOGGERS) {
/* 174 */       loggerContext.getLogger(loggerName).setLevel(Logger.getLogbackMappedLogLevel(loggingLevel));
/*     */     }
/*     */     
/* 177 */     this.logger.setConfigLoggingLevel(loggingLevel);
/* 178 */     this.environment.getSystemProperties().put(CEBASProperty.LOGGING_LEVEL.name(), loggingLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void close() {
/* 185 */     CompletableFuture.runAsync(() -> {
/*     */           try {
/*     */             Thread.sleep(2000L);
/* 188 */           } catch (InterruptedException e) {
/*     */             CEBASException exception = new CEBASException(e.getMessage());
/*     */             this.logger.logWithException("000313X", "Task interrupted " + e.getMessage(), exception);
/*     */             Thread.currentThread().interrupt();
/*     */             throw exception;
/*     */           } 
/*     */           System.exit(0);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkForBackup() {
/* 203 */     if (this.cebasRecoveryOptional != null && this.cebasRecoveryOptional.isPresent())
/* 204 */       ((ICEBASRecovery)this.cebasRecoveryOptional.get()).doBackup(); 
/*     */   }
/*     */   
/*     */   public abstract String getDefaultUserName();
/*     */   
/*     */   public abstract void createDefaultUser();
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\startup\AbstractStartupEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */