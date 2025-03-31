/*    */ package com.daimler.cebas.system.control.performance;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Arrays;
/*    */ import java.util.Date;
/*    */ import java.util.HashMap;
/*    */ import org.aspectj.lang.JoinPoint;
/*    */ import org.aspectj.lang.reflect.CodeSignature;
/*    */ import org.aspectj.lang.reflect.MethodSignature;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.beans.factory.annotation.Value;
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
/*    */ public abstract class AbstractPerformanceAuditInterceptor
/*    */ {
/*    */   @Value("${PERSIST_PERFORMANCE_AUDIT_ENTRY}")
/*    */   private String enablePerformanceAuditEntriesPersistance;
/*    */   @Autowired
/*    */   private PerformanceAuditRequest performanceAuditRequest;
/*    */   
/*    */   public void start(JoinPoint joinPoint) {
/* 41 */     MethodSignature signature = (MethodSignature)joinPoint.getSignature();
/* 42 */     Method method = signature.getMethod();
/* 43 */     HashMap<String, Object> parametersMap = new HashMap<>();
/* 44 */     Object[] requestArguments = joinPoint.getArgs();
/*    */     
/* 46 */     String[] paramenterName = ((CodeSignature)joinPoint.getSignature()).getParameterNames();
/* 47 */     for (int i = 0; i < Arrays.asList((T[])requestArguments).size(); i++) {
/* 48 */       parametersMap.put(Arrays.<String>asList(paramenterName).get(i), 
/* 49 */           Arrays.<Object>asList(requestArguments).get(i));
/*    */     }
/* 51 */     this.performanceAuditRequest.setCorrelationId(
/* 52 */         String.valueOf(parametersMap.get("correlationid")));
/* 53 */     parametersMap.clear();
/* 54 */     this.performanceAuditRequest
/* 55 */       .setClassName(joinPoint.getTarget().getClass().getSimpleName());
/* 56 */     this.performanceAuditRequest.setMethodName(method.getName());
/* 57 */     this.performanceAuditRequest.setStartTime(new Date());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void end(JoinPoint joinPoint) {
/* 67 */     this.performanceAuditRequest.setEndTime(new Date());
/* 68 */     if (Boolean.TRUE.toString()
/* 69 */       .equalsIgnoreCase(this.enablePerformanceAuditEntriesPersistance))
/* 70 */       this.performanceAuditRequest.add(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\performance\AbstractPerformanceAuditInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */