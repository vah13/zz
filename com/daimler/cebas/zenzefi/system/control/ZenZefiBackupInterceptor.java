/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control;
/*    */ 
/*    */ import com.daimler.cebas.common.control.ICEBASRecovery;
/*    */ import org.aspectj.lang.ProceedingJoinPoint;
/*    */ import org.aspectj.lang.annotation.Around;
/*    */ import org.aspectj.lang.annotation.Aspect;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.stereotype.Component;
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
/*    */ 
/*    */ 
/*    */ @Aspect
/*    */ @Component
/*    */ @ConditionalOnProperty(name = {"system_data_recovery_enabled"}, havingValue = "true")
/*    */ public class ZenZefiBackupInterceptor
/*    */ {
/*    */   @Autowired
/*    */   private ICEBASRecovery systemRecovery;
/*    */   
/*    */   @Around("execution(* com.daimler.cebas.zenzefi.certificates.control.ZenZefiCertificatesService.importCertificates(..)) || execution(* com.daimler.cebas.zenzefi.certificates.control.ZenZefiCertificatesService.importCertificatesFromLocal(..)) || execution(* com.daimler.cebas.zenzefi.certificates.control.ZenZefiCertificatesService.importEncryptedPKCSPackages(..)) || execution(* com.daimler.cebas.zenzefi.certificates.control.ZenZefiCertificatesService.importRemoteCertificates(..)) ||execution(* com.daimler.cebas.zenzefi.certificates.control.ZenZefiCertificatesService.getTimeCert(..)) || execution(* com.daimler.cebas.zenzefi.certificates.control.ZenZefiCertificatesService.getSecOCISCert(..)) || execution(* com.daimler.cebas.zenzefi.users.control.UserService.deleteAccounts(..)) || execution(* com.daimler.cebas.zenzefi.users.control.UserService.deleteAccount(..)) || execution(* com.daimler.cebas.zenzefi.users.control.UserRegistrationEngine.register(..)) || execution(* com.daimler.cebas.certificates.control.update.DefaultUpdateSession.setNotRunning(..))")
/*    */   public Object interceptAroundMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
/* 46 */     Object o = joinPoint.proceed();
/* 47 */     this.systemRecovery.scheduleBackup();
/* 48 */     return o;
/*    */   }
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
/*    */   @Around("execution(* com.daimler.cebas.zenzefi.system.control.ZenZefiSystemService.generateKeyPair(..))")
/*    */   public Object interceptAroundMethodExecutionCoupling(ProceedingJoinPoint joinPoint) throws Throwable {
/* 63 */     Object o = joinPoint.proceed();
/* 64 */     this.systemRecovery.scheduleCouplingBackup();
/* 65 */     return o;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\ZenZefiBackupInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */