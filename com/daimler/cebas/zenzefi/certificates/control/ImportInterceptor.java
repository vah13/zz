/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.ImportSession;
/*    */ import com.daimler.cebas.common.control.CEBASException;
/*    */ import org.aspectj.lang.ProceedingJoinPoint;
/*    */ import org.aspectj.lang.annotation.Around;
/*    */ import org.aspectj.lang.annotation.Aspect;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
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
/*    */ 
/*    */ @Aspect
/*    */ @Component
/*    */ public class ImportInterceptor
/*    */ {
/*    */   @Autowired
/*    */   private ImportSession importSession;
/*    */   
/*    */   @Around("execution(* com.daimler.cebas.zenzefi.certificates.control.ZenZefiCertificatesService.importCertificates(..)) || execution(* com.daimler.cebas.zenzefi.certificates.control.ZenZefiCertificatesService.importCertificatesFromLocal(..)) || execution(* com.daimler.cebas.zenzefi.certificates.control.ZenZefiCertificatesService.importEncryptedPKCSPackages(..)) || execution(* com.daimler.cebas.zenzefi.certificates.control.ZenZefiCertificatesService.importRemoteCertificates(..))")
/*    */   public Object interceptAroundMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
/*    */     try {
/* 47 */       return joinPoint.proceed();
/* 48 */     } catch (CEBASException|javax.persistence.PersistenceException e) {
/* 49 */       throw e;
/*    */     } finally {
/* 51 */       if (this.importSession.isRunning())
/* 52 */         this.importSession.setNotRunning(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\control\ImportInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */