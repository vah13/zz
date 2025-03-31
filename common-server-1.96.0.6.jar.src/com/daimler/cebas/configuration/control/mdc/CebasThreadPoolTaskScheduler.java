/*    */ package com.daimler.cebas.configuration.control.mdc;
/*    */ 
/*    */ import java.util.concurrent.RejectedExecutionHandler;
/*    */ import java.util.concurrent.ScheduledExecutorService;
/*    */ import java.util.concurrent.ThreadFactory;
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
/*    */ public class CebasThreadPoolTaskScheduler
/*    */   extends ThreadPoolTaskScheduler
/*    */ {
/*    */   private static final long serialVersionUID = 7946527707921828825L;
/*    */   
/*    */   protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
/* 23 */     return new MdcDecoratorScheduledThreadPoolExecutor(poolSize, threadFactory, rejectedExecutionHandler);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\mdc\CebasThreadPoolTaskScheduler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */