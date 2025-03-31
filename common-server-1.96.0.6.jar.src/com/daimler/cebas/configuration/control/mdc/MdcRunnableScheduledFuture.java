/*    */ package com.daimler.cebas.configuration.control.mdc;
/*    */ 
/*    */ import com.daimler.cebas.common.control.StartupStatus;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.Delayed;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.RunnableScheduledFuture;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.TimeoutException;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import org.slf4j.MDC;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MdcRunnableScheduledFuture<V>
/*    */   implements RunnableScheduledFuture<V>
/*    */ {
/* 22 */   private static final Logger LOG = Logger.getLogger(MdcRunnableScheduledFuture.class.getName());
/*    */   
/*    */   RunnableScheduledFuture<V> delegate;
/*    */   private final Map<String, String> mdc;
/*    */   
/*    */   public MdcRunnableScheduledFuture(RunnableScheduledFuture<V> delegate) {
/* 28 */     this.delegate = delegate;
/* 29 */     this.mdc = MDC.getCopyOfContextMap();
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 34 */     if (!StartupStatus.isApplicationStarted()) {
/* 35 */       LOG.log(Level.INFO, "Application startup not finished yet - scheduled task execution will be skipped for now.");
/*    */     } else {
/*    */       
/* 38 */       if (this.mdc != null && !this.mdc.isEmpty()) {
/* 39 */         MDC.setContextMap(this.mdc);
/*    */       }
/* 41 */       this.delegate.run();
/*    */     } 
/*    */   }
/*    */   
/*    */   public long getDelay(TimeUnit unit) {
/* 46 */     return this.delegate.getDelay(unit);
/*    */   }
/*    */   
/*    */   public boolean isPeriodic() {
/* 50 */     return this.delegate.isPeriodic();
/*    */   }
/*    */   
/*    */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 54 */     return this.delegate.cancel(mayInterruptIfRunning);
/*    */   }
/*    */   
/*    */   public int compareTo(Delayed o) {
/* 58 */     return this.delegate.compareTo(o);
/*    */   }
/*    */   
/*    */   public boolean isCancelled() {
/* 62 */     return this.delegate.isCancelled();
/*    */   }
/*    */   
/*    */   public boolean isDone() {
/* 66 */     return this.delegate.isDone();
/*    */   }
/*    */   
/*    */   public V get() throws InterruptedException, ExecutionException {
/* 70 */     return this.delegate.get();
/*    */   }
/*    */   
/*    */   public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 74 */     return this.delegate.get(timeout, unit);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\mdc\MdcRunnableScheduledFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */