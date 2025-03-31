/*     */ package com.daimler.cebas.configuration.control.gracefulshutdown;
/*     */ 
/*     */ import com.daimler.cebas.system.control.websocket.IWebsocketSessions;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Qualifier;
/*     */ import org.springframework.boot.web.context.WebServerInitializedEvent;
/*     */ import org.springframework.boot.web.server.WebServer;
/*     */ import org.springframework.boot.web.server.WebServerException;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.annotation.Lazy;
/*     */ import org.springframework.context.event.EventListener;
/*     */ import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ @Lazy
/*     */ public class ShutdownHook
/*     */   implements Runnable
/*     */ {
/*  40 */   protected final Log LOG = LogFactory.getLog(getClass());
/*     */   
/*  42 */   private final List<WebServer> embeddedContainers = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private ConfigurableApplicationContext applicationContext;
/*     */ 
/*     */   
/*     */   private volatile boolean alreadyExecuted = false;
/*     */ 
/*     */   
/*  52 */   private int shutdownTimeout = 60000;
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private ShutdownHttpFilter filter;
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private Optional<IWebsocketSessions> sessions;
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private Session session;
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   @Qualifier("taskScheduler")
/*     */   private ThreadPoolTaskScheduler taskScheduler;
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(ConfigurableApplicationContext applicationContext) {
/*  74 */     this.applicationContext = applicationContext;
/*  75 */     this.alreadyExecuted = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*  83 */     this.session.initMdc();
/*     */     
/*  85 */     if (this.alreadyExecuted) {
/*  86 */       this.LOG.info("Shutdown - was already executed. Skipping.");
/*     */       return;
/*     */     } 
/*  89 */     this.LOG.info("Shutdown - started. Existing HTTP sessions and scheduled jobs can finish within: " + this.shutdownTimeout + " miliseconds.");
/*     */     
/*  91 */     ExecutorService shutdownExecutor = null;
/*     */     
/*     */     try {
/*  94 */       shutdownExecutor = Executors.newSingleThreadExecutor();
/*  95 */       Future<Void> httpFuture = shutdownExecutor.submit(() -> {
/*     */             this.session.initMdc();
/*     */             
/*     */             shutdownHttpFilter();
/*     */             stopWebsocketSessions();
/*     */             drainScheduler(this.taskScheduler);
/*     */             shutdownHTTPConnector();
/*     */             shutdownScheduler(this.taskScheduler);
/*     */             return null;
/*     */           });
/*     */       try {
/* 106 */         httpFuture.get(this.shutdownTimeout, TimeUnit.MILLISECONDS);
/* 107 */       } catch (ExecutionException|java.util.concurrent.TimeoutException e) {
/* 108 */         this.LOG.warn("HTTP graceful shutdown failed", e);
/* 109 */       } catch (InterruptedException interrupted) {
/* 110 */         Thread.currentThread().interrupt();
/* 111 */         this.LOG.warn("HTTP graceful shutdown failed", interrupted);
/*     */       } 
/*     */     } finally {
/*     */       
/* 115 */       this.alreadyExecuted = true;
/* 116 */       shutdownExecutor(shutdownExecutor);
/* 117 */       shutdownApplication();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void drainScheduler(ThreadPoolTaskScheduler scheduler) {
/* 130 */     this.LOG.info("Shutdown - execute all scheduled tasks.");
/*     */     try {
/* 132 */       ScheduledThreadPoolExecutor executor = scheduler.getScheduledThreadPoolExecutor();
/*     */       
/* 134 */       executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
/* 135 */       executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
/* 136 */       executor.setRemoveOnCancelPolicy(true);
/* 137 */       executor.shutdown();
/* 138 */       executor.awaitTermination(this.shutdownTimeout, TimeUnit.MILLISECONDS);
/* 139 */     } catch (SecurityException e) {
/* 140 */       this.LOG.warn("Shutdown - drainScheduler failed", e);
/* 141 */     } catch (InterruptedException e) {
/* 142 */       this.LOG.warn("Shutdown - drainScheduler failed", e);
/* 143 */       Thread.currentThread().interrupt();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void stopWebsocketSessions() {
/* 148 */     if (this.sessions.isPresent()) {
/* 149 */       this.LOG.info("Shutdown - Websocket sessions");
/* 150 */       ((IWebsocketSessions)this.sessions.get()).close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void shutdownHttpFilter() {
/* 155 */     this.LOG.info("Shutdown - HTTP filter.");
/*     */     try {
/* 157 */       this.filter.shutdown();
/* 158 */     } catch (InterruptedException e) {
/* 159 */       this.LOG.warn("Shutdown - shutdownHttpFilter failed", e);
/* 160 */       Thread.currentThread().interrupt();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void shutdownScheduler(ThreadPoolTaskScheduler scheduler) {
/* 165 */     this.LOG.info("Shutdown - Schedulers.");
/*     */     try {
/* 167 */       scheduler.shutdown();
/* 168 */     } catch (SecurityException e) {
/* 169 */       this.LOG.warn("Shutdown - shutdown taskScheduler failed", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void shutdownHTTPConnector() {
/* 174 */     this.LOG.info("Shutdown - HTTP Connector.");
/*     */     try {
/* 176 */       for (WebServer embeddedServletContainer : this.embeddedContainers) {
/* 177 */         embeddedServletContainer.stop();
/*     */       }
/* 179 */     } catch (WebServerException e) {
/* 180 */       this.LOG.warn("Shutdown - shutdownHTTPConnector failed", (Throwable)e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void shutdownExecutor(ExecutorService shutdownExecutor) {
/* 185 */     if (shutdownExecutor != null) {
/*     */       try {
/* 187 */         shutdownExecutor.shutdownNow();
/* 188 */       } catch (SecurityException e) {
/* 189 */         this.LOG.warn("Executor shutdown failed", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void shutdownApplication() {
/* 195 */     this.LOG.info("Shutdown - ApplicationContext");
/* 196 */     this.applicationContext.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventListener
/*     */   public synchronized void onContainerInitialized(WebServerInitializedEvent event) {
/* 207 */     this.embeddedContainers.add(event.getSource());
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\gracefulshutdown\ShutdownHook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */