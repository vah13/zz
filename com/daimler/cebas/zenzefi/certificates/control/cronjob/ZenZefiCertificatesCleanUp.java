/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control.cronjob;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.DeleteCertificatesEngine;
/*     */ import com.daimler.cebas.certificates.control.DurationParser;
/*     */ import com.daimler.cebas.certificates.control.ImportSession;
/*     */ import com.daimler.cebas.certificates.control.cronjob.CertificatesCleanUp;
/*     */ import com.daimler.cebas.certificates.control.hooks.HookProviderType;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.control.UserRepository;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*     */ import java.time.Period;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Value;
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
/*     */ @CEBASControl
/*     */ public class ZenZefiCertificatesCleanUp
/*     */   extends CertificatesCleanUp
/*     */ {
/*  35 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.certificates.control.cronjob.ZenZefiCertificatesCleanUp.class.getSimpleName();
/*     */   
/*     */   @Value("${MAX_AGE_VSM_CERTS:#{null}}")
/*     */   private String maxAgeCerts;
/*     */   
/*  40 */   private String defaultMAxAgeCerts = "P90D";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Period maxAgeCertsPeriod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UpdateSession updateSession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImportSession importSession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public ZenZefiCertificatesCleanUp(DeleteCertificatesEngine deleteCertEngine, UserRepository userRepository, Logger logger, MetadataManager i18n, Session session, UpdateSession updateSession, ImportSession importSession) {
/*  72 */     super(deleteCertEngine, userRepository, logger, i18n, session);
/*  73 */     this.updateSession = updateSession;
/*  74 */     this.importSession = importSession;
/*     */   }
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public void executeCronJobTask() {
/*  80 */     if (this.updateSession.isRunning() || this.updateSession.isPaused() || this.importSession.isRunning()) {
/*     */       return;
/*     */     }
/*  83 */     super.executeCronJobTask();
/*  84 */     deleteOlderVSMZZCertificates();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void deleteOlderVSMCertificates() {
/*  89 */     this.logger.log(Level.INFO, "000161", this.i18n.getEnglishMessage("deleteEcuCertsStart"), CLASS_NAME);
/*  90 */     List<Certificate> certsToRemove = this.deleteCertificatesEngine.getRepository().getVSMCertIdsCreatedBefore(this.session.getCurrentUser(), deleteVSMCleanUpDate(), true);
/*  91 */     removeCertificatesOnCronJob(certsToRemove, HookProviderType.ECU_CERTS);
/*  92 */     this.logger.log(Level.INFO, "000161", this.i18n.getEnglishMessage("deleteEcuCertsEnd"), CLASS_NAME);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Period getExpiryPeriod() {
/*  98 */     this.maxAgeCertsPeriod = getExpiryPeriod(this.maxAgeCerts, this.defaultMAxAgeCerts, this.maxAgeCertsPeriod);
/*  99 */     return this.maxAgeCertsPeriod;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DurationParser getExpirationPeriodForAdHocCerts() {
/* 104 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void deleteOlderVariantCodingCertificates() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void deleteOlderDiagnosticCertificates() {}
/*     */ 
/*     */ 
/*     */   
/*     */   private void deleteOlderVSMZZCertificates() {
/* 118 */     if (isDeleteExpiredCertsSetOnTrue(this.session.getCurrentUser()))
/* 119 */       deleteOlderVSMCertificates(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\control\cronjob\ZenZefiCertificatesCleanUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */