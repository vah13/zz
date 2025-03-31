/*    */ package com.daimler.cebas.configuration.control.mdc;
/*    */ 
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.RejectedExecutionHandler;
/*    */ import java.util.concurrent.RunnableScheduledFuture;
/*    */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MdcDecoratorScheduledThreadPoolExecutor
/*    */   extends ScheduledThreadPoolExecutor
/*    */ {
/* 17 */   private final String CEBAS_PACKAGE = "com.daimler.cebas";
/*    */ 
/*    */   
/*    */   public MdcDecoratorScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
/* 21 */     super(corePoolSize, threadFactory, handler);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
/* 27 */     if (runnable.toString().contains("com.daimler.cebas")) {
/* 28 */       return new MdcRunnableScheduledFuture<>(task);
/*    */     }
/* 30 */     return task;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> callable, RunnableScheduledFuture<V> task) {
/* 38 */     if (callable.toString().contains("com.daimler.cebas")) {
/* 39 */       return new MdcRunnableScheduledFuture<>(task);
/*    */     }
/* 41 */     return task;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\mdc\MdcDecoratorScheduledThreadPoolExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */