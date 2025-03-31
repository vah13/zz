/*    */ package com.daimler.cebas.configuration.control;
/*    */ 
/*    */ import com.daimler.cebas.configuration.control.mdc.CebasThreadPoolTaskScheduler;
/*    */ import org.springframework.beans.factory.annotation.Qualifier;
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Primary;
/*    */ import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
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
/*    */ @Configuration
/*    */ public class TaskExecutorConfiguration
/*    */ {
/*    */   @Value("${cebas.scheduler.tasks.poolsize}")
/*    */   private int taskPoolSize;
/*    */   
/*    */   @Bean
/*    */   @Primary
/*    */   @Qualifier("taskScheduler")
/*    */   public ThreadPoolTaskScheduler taskScheduler() {
/* 31 */     CebasThreadPoolTaskScheduler cebasThreadPoolTaskScheduler = new CebasThreadPoolTaskScheduler();
/* 32 */     cebasThreadPoolTaskScheduler.setPoolSize(this.taskPoolSize);
/* 33 */     cebasThreadPoolTaskScheduler.setThreadNamePrefix("CeBAS-Scheduler-");
/* 34 */     return (ThreadPoolTaskScheduler)cebasThreadPoolTaskScheduler;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\TaskExecutorConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */