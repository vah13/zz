/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control.performance;
/*    */ 
/*    */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*    */ import com.daimler.cebas.system.control.performance.PerformanceAuditManager;
/*    */ import com.daimler.cebas.system.control.performance.PerformanceAuditRequest;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import com.daimler.cebas.zenzefi.configuration.control.ZenZefiConfigurator;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Arrays;
/*    */ import java.util.Date;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.aspectj.lang.JoinPoint;
/*    */ import org.aspectj.lang.annotation.After;
/*    */ import org.aspectj.lang.annotation.Aspect;
/*    */ import org.aspectj.lang.annotation.Before;
/*    */ import org.aspectj.lang.reflect.CodeSignature;
/*    */ import org.aspectj.lang.reflect.MethodSignature;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.beans.factory.annotation.Value;
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
/*    */ @Aspect
/*    */ @Component
/*    */ public class PerformanceAuditBackgroundJobInterceptor
/*    */ {
/*    */   @Autowired
/*    */   private Session session;
/*    */   @Autowired
/*    */   private ZenZefiConfigurator configurator;
/*    */   @Autowired
/*    */   private PerformanceAuditManager performanceAuditManager;
/*    */   @Value("${PERSIST_PERFORMANCE_AUDIT_ENTRY}")
/*    */   private String enablePerformanceAuditEntriesPersistance;
/* 50 */   private Map<Long, PerformanceAuditRequest> auditRequest = new ConcurrentHashMap<>();
/*    */ 
/*    */   
/*    */   @Before("execution(* com.daimler.cebas.zenzefi.certificates.control.update.AbstractZenzefiCertificatesUpdater.updateCertificates(..))")
/*    */   public void startAction(JoinPoint joinPoint) {
/* 55 */     PerformanceAuditRequest performanceAuditRequest = new PerformanceAuditRequest(this.session, (AbstractConfigurator)this.configurator, this.performanceAuditManager);
/* 56 */     performanceAuditRequest.init();
/* 57 */     MethodSignature signature = (MethodSignature)joinPoint.getSignature();
/* 58 */     Method method = signature.getMethod();
/* 59 */     HashMap<String, Object> parametersMap = new HashMap<>();
/* 60 */     Object[] requestArguments = joinPoint.getArgs();
/*    */     
/* 62 */     String[] paramenterName = ((CodeSignature)joinPoint.getSignature()).getParameterNames();
/* 63 */     for (int i = 0; i < Arrays.asList((T[])requestArguments).size(); i++) {
/* 64 */       parametersMap.put(Arrays.<String>asList(paramenterName).get(i), 
/* 65 */           Arrays.<Object>asList(requestArguments).get(i));
/*    */     }
/* 67 */     parametersMap.clear();
/* 68 */     performanceAuditRequest
/* 69 */       .setClassName(joinPoint.getTarget().getClass().getSimpleName());
/* 70 */     performanceAuditRequest.setMethodName(method.getName());
/* 71 */     performanceAuditRequest.setStartTime(new Date());
/* 72 */     this.auditRequest.put(Long.valueOf(Thread.currentThread().getId()), performanceAuditRequest);
/*    */   }
/*    */   
/*    */   @After("execution(* com.daimler.cebas.zenzefi.certificates.control.update.AbstractZenzefiCertificatesUpdater.updateCertificates(..))")
/*    */   public void endAction(JoinPoint joinPoint) {
/* 77 */     PerformanceAuditRequest performanceAuditRequest = this.auditRequest.get(Long.valueOf(Thread.currentThread().getId()));
/* 78 */     performanceAuditRequest.setEndTime(new Date());
/* 79 */     if (Boolean.TRUE.toString()
/* 80 */       .equalsIgnoreCase(this.enablePerformanceAuditEntriesPersistance)) {
/* 81 */       performanceAuditRequest.add();
/*    */     }
/* 83 */     this.auditRequest.remove(Long.valueOf(Thread.currentThread().getId()));
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\performance\PerformanceAuditBackgroundJobInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */