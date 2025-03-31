/*     */ package com.daimler.cebas.certificates.control.cronjob;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.DeleteCertificatesEngine;
/*     */ import com.daimler.cebas.certificates.control.DurationParser;
/*     */ import com.daimler.cebas.certificates.control.hooks.DeleteCertificateNonVSMHook;
/*     */ import com.daimler.cebas.certificates.control.hooks.HookProviderType;
/*     */ import com.daimler.cebas.certificates.control.hooks.ICertificateHooks;
/*     */ import com.daimler.cebas.certificates.control.hooks.NoHook;
/*     */ import com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.common.control.CEBASProperty;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.common.entity.AbstractEntity;
/*     */ import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
/*     */ import com.daimler.cebas.configuration.entity.Configuration;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.control.UserRepository;
/*     */ import com.daimler.cebas.users.control.factories.UserFactory;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.Period;
/*     */ import java.time.ZoneId;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
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
/*     */ public abstract class CertificatesCleanUp
/*     */ {
/*  51 */   private static final String CLASS_NAME = CertificatesCleanUp.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DeleteCertificatesEngine deleteCertificatesEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UserRepository userRepository;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MetadataManager i18n;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Session session;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   private final SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
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
/*     */   @Autowired
/*     */   public CertificatesCleanUp(DeleteCertificatesEngine deleteCertificatesEngine, UserRepository userRepository, Logger logger, MetadataManager i18n, Session session) {
/* 100 */     this.deleteCertificatesEngine = deleteCertificatesEngine;
/* 101 */     this.userRepository = userRepository;
/* 102 */     this.logger = logger;
/* 103 */     this.i18n = i18n;
/* 104 */     this.session = session;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cleanUpCertificates(User user) {
/* 114 */     String METHOD_NAME = "cleanUpCertificates";
/* 115 */     this.logger.entering(CLASS_NAME, "cleanUpCertificates");
/* 116 */     if (isDeleteExpiredCertsSetOnTrue(user)) {
/* 117 */       deleteExpiredCertificates(user);
/*     */     } else {
/* 119 */       this.logger.log(Level.INFO, "000650", this.i18n.getMessage("vsmCertAutoDeleteSkipped"), CLASS_NAME);
/*     */     } 
/* 121 */     this.logger.exiting(CLASS_NAME, "cleanUpCertificates");
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isDeleteExpiredCertsSetOnTrue(User user) {
/* 126 */     Optional<Configuration> optional = user.getConfigurations().stream().filter(c -> c.getConfigKey().equals(CEBASProperty.DELETE_EXPIRED_CERTS.name())).findFirst();
/*     */     
/* 128 */     Configuration deleteExpiredCertsConfig = optional.<Throwable>orElseThrow(() -> new ZenZefiConfigurationException(this.i18n.getMessage("configurationNotFound", new String[] { CEBASProperty.DELETE_EXPIRED_CERTS.name() })));
/*     */     
/* 130 */     return Boolean.TRUE.equals(Boolean.valueOf(deleteExpiredCertsConfig.getConfigValue()));
/*     */   }
/*     */   
/*     */   @Transactional
/*     */   public void cleanUpCertificatesCurrentUser() {
/* 135 */     User currentUser = this.session.getCurrentUser();
/* 136 */     cleanUpCertificates(currentUser);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void deleteExpiredCertificates(User user) {
/* 146 */     String METHOD_NAME = "deleteExpiredCertificates";
/* 147 */     this.logger.entering(CLASS_NAME, "deleteExpiredCertificates");
/*     */     
/* 149 */     user = this.userRepository.findUserById(user.getEntityId());
/* 150 */     this.logger.log(Level.INFO, "000004", this.i18n
/* 151 */         .getEnglishMessage("deletingExpiredCertificatesForUser", new String[] {
/* 152 */             composeMessageForCurrentUser(user)
/*     */           }), CLASS_NAME);
/*     */     
/* 155 */     removeExpiredECUCertificates(user);
/*     */     
/* 157 */     List<String> ids = this.deleteCertificatesEngine.getRepository().getExpiredNonECUCertificates(user);
/* 158 */     if (!ids.isEmpty()) {
/* 159 */       List<DeleteCertificatesInfo> deletedCertificates = this.deleteCertificatesEngine.deleteCertificates(ids, user);
/* 160 */       deletedCertificates.forEach(deleteCertificatesInfo -> this.logger.log(Level.INFO, "000122", this.i18n.getEnglishMessage("deleteExpiredCertificateCronTrigger", new String[] { this.i18n.getEnglishMessage(deleteCertificatesInfo.getCertificateType().getLanguageProperty()), deleteCertificatesInfo.getCertificateId(), deleteCertificatesInfo.getSerialNo(), deleteCertificatesInfo.getSubjectKeyIdentifier(), deleteCertificatesInfo.getAuthKeyIdentifier() }), CLASS_NAME));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     this.userRepository.detach((AbstractEntity)user);
/*     */     
/* 172 */     this.logger.exiting(CLASS_NAME, "deleteExpiredCertificates");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeExpiredECUCertificates(User user) {
/* 182 */     List<Certificate> certsToRemove = this.deleteCertificatesEngine.getRepository().getExpiredECUCertificates(user);
/* 183 */     removeCertificatesOnCronJob(certsToRemove, HookProviderType.ECU_CERTS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeCertificatesOnCronJob(List<Certificate> certsToRemove, HookProviderType hookProviderType) {
/* 193 */     if (!certsToRemove.isEmpty()) {
/* 194 */       this.session.getSystemIntegrityCheckResult().clear();
/*     */       
/* 196 */       ICertificateHooks hookProvider = HookProviderType.ECU_CERTS.equals(hookProviderType) ? (ICertificateHooks)new DeleteCertificateNonVSMHook(this.logger) : (ICertificateHooks)new NoHook();
/* 197 */       certsToRemove.forEach(certificate -> {
/*     */             hookProvider.exec(certificate);
/*     */ 
/*     */             
/*     */             this.deleteCertificatesEngine.deleteKPForChildren(certificate, this.session.getCurrentUser());
/*     */ 
/*     */             
/*     */             this.deleteCertificatesEngine.getRepository().deleteManagedEntity((AbstractEntity)certificate);
/*     */             
/*     */             this.logger.log(Level.INFO, "000122", this.i18n.getEnglishMessage("deleteExpiredCertificateCronTrigger", new String[] { this.i18n.getEnglishMessage(certificate.getType().getLanguageProperty()), certificate.getEntityId(), certificate.getSerialNo(), certificate.getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier() }), CLASS_NAME);
/*     */           });
/*     */       
/* 209 */       this.deleteCertificatesEngine.getRepository().flush();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public void executeCronJobTask() {
/* 218 */     deleteExpiredCertificates();
/*     */   }
/*     */   
/*     */   protected Date deleteVSMCleanUpDate() {
/* 222 */     LocalDateTime expiredTime = LocalDateTime.now().minus(getExpiryPeriod());
/* 223 */     return Date.from(expiredTime.atZone(ZoneId.systemDefault()).toInstant());
/*     */   }
/*     */   
/*     */   protected Date deleteAdHocCertificatesCleanUpDate() {
/* 227 */     LocalDateTime expiredTime = getExpirationPeriodForAdHocCerts().plusTo(LocalDateTime.now());
/* 228 */     return new Date(Timestamp.valueOf(expiredTime).getTime());
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
/*     */   protected Period getExpiryPeriod(String maxAgeCerts, String defaultMaxAgeCerts, Period maxAgeCertsPeriod) {
/* 263 */     if (maxAgeCertsPeriod == null) {
/*     */       try {
/* 265 */         maxAgeCertsPeriod = Period.parse(maxAgeCerts);
/* 266 */       } catch (Exception e) {
/* 267 */         this.logger.logToFileOnly(CLASS_NAME, "Parse exception for certificate max age: " + maxAgeCerts, e);
/* 268 */         this.logger.log(Level.WARNING, "000557X", "The certificate max age param could not be parsed. Please check the configuration. Stack trace can be checked in the log file. The param is set as: " + maxAgeCerts, CLASS_NAME);
/*     */ 
/*     */         
/* 271 */         maxAgeCertsPeriod = Period.parse(defaultMaxAgeCerts);
/*     */       } 
/*     */     }
/* 274 */     return maxAgeCertsPeriod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void deleteExpiredCertificates() {
/* 282 */     String METHOD_NAME = "deleteExpiredCertificates";
/* 283 */     this.logger.entering(CLASS_NAME, "deleteExpiredCertificates");
/* 284 */     this.logger.log(Level.INFO, "000001", "System trying to delete expired certificates, time: " + this.dateFormatter
/* 285 */         .format(new Date()), CLASS_NAME);
/* 286 */     List<User> users = this.userRepository.findUsersWithDeleteExpiredCertsTrue();
/* 287 */     users.forEach(this::deleteExpiredCertificates);
/* 288 */     this.logger.exiting(CLASS_NAME, "deleteExpiredCertificates");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String composeMessageForCurrentUser(User user) {
/*     */     String currentUserName;
/* 300 */     if (user.getUserName().equals(UserFactory.getDefaultUsername())) {
/* 301 */       currentUserName = this.i18n.getMessage("defaultUser");
/*     */     } else {
/* 303 */       currentUserName = user.getUserName();
/*     */     } 
/* 305 */     return currentUserName;
/*     */   }
/*     */   
/*     */   protected abstract void deleteOlderVSMCertificates();
/*     */   
/*     */   protected abstract void deleteOlderVariantCodingCertificates();
/*     */   
/*     */   protected abstract void deleteOlderDiagnosticCertificates();
/*     */   
/*     */   protected abstract Period getExpiryPeriod();
/*     */   
/*     */   protected abstract DurationParser getExpirationPeriodForAdHocCerts();
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\cronjob\CertificatesCleanUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */