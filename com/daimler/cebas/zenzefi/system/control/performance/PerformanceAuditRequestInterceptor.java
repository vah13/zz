/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control.performance;
/*    */ 
/*    */ import com.daimler.cebas.system.control.performance.AbstractPerformanceAuditInterceptor;
/*    */ import org.aspectj.lang.JoinPoint;
/*    */ import org.aspectj.lang.annotation.After;
/*    */ import org.aspectj.lang.annotation.Aspect;
/*    */ import org.aspectj.lang.annotation.Before;
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
/*    */ @Aspect
/*    */ @Component
/*    */ public class PerformanceAuditRequestInterceptor
/*    */   extends AbstractPerformanceAuditInterceptor
/*    */ {
/*    */   @Before("execution(* com.daimler.cebas.zenzefi.configuration.boundary.ConfigurationsResource.*(..)) || execution(* com.daimler.cebas.zenzefi.logs.boundary.LogsResource.*(..)) || execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResource.*(..)) ||execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResource.*(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV1.*(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV2.*(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV3.*(..)) || execution(* com.daimler.cebas.zenzefi.system.boundary.ZenZefiSystemResource.*(..))")
/*    */   public void startAction(JoinPoint joinPoint) {
/* 34 */     start(joinPoint);
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
/*    */ 
/*    */ 
/*    */   
/*    */   @After("execution(* com.daimler.cebas.zenzefi.configuration.boundary.ConfigurationsResource.*(..)) || execution(* com.daimler.cebas.zenzefi.logs.boundary.LogsResource.*(..)) || execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResource.*(..)) ||execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResource.*(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV1.*(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV2.*(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV3.*(..)) || execution(* com.daimler.cebas.zenzefi.system.boundary.ZenZefiSystemResource.*(..))")
/*    */   public void endAction(JoinPoint joinPoint) {
/* 52 */     end(joinPoint);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\performance\PerformanceAuditRequestInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */