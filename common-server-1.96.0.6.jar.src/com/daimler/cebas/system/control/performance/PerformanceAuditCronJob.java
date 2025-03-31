/*    */ package com.daimler.cebas.system.control.performance;
/*    */ 
/*    */ import com.daimler.cebas.common.control.CEBASProperty;
/*    */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.beans.factory.annotation.Qualifier;
/*    */ import org.springframework.scheduling.TaskScheduler;
/*    */ import org.springframework.scheduling.Trigger;
/*    */ import org.springframework.scheduling.support.CronTrigger;
/*    */ import org.springframework.stereotype.Component;
/*    */ import org.springframework.transaction.annotation.Propagation;
/*    */ import org.springframework.transaction.annotation.Transactional;
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
/*    */ @Component
/*    */ public class PerformanceAuditCronJob
/*    */ {
/*    */   @Autowired
/*    */   private PerformanceAuditCleanUp performanceAuditCleanUp;
/*    */   @Autowired
/*    */   @Qualifier("taskScheduler")
/*    */   private TaskScheduler taskScheduler;
/*    */   @Autowired
/*    */   private AbstractConfigurator configurator;
/*    */   
/*    */   @Transactional(propagation = Propagation.REQUIRED)
/*    */   public void scheduleCleanUpPerformanceEntries() {
/* 44 */     String maxAgePerformanceEntry = this.configurator.readProperty(CEBASProperty.PERFORMANCE_AUDIT_ENTRY_MAX_AGE
/* 45 */         .name());
/* 46 */     String schedulerDeletePerformanceEntry = this.configurator.readProperty(CEBASProperty.PERFORMANCE_AUDIT_CLEANUP_JOB
/* 47 */         .name());
/* 48 */     CronTrigger cronTrigger = new CronTrigger(schedulerDeletePerformanceEntry);
/*    */     
/* 50 */     Runnable task = () -> this.performanceAuditCleanUp.deletePerformanceEntries(maxAgePerformanceEntry);
/*    */     
/* 52 */     this.taskScheduler.schedule(task, (Trigger)cronTrigger);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\performance\PerformanceAuditCronJob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */