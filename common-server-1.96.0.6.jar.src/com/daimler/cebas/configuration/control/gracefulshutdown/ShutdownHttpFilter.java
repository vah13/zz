/*     */ package com.daimler.cebas.configuration.control.gracefulshutdown;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicLongFieldUpdater;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.FilterConfig;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.boot.web.servlet.FilterRegistrationBean;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Lazy;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ @Lazy
/*     */ public class ShutdownHttpFilter
/*     */   implements Filter
/*     */ {
/*  33 */   private static final AtomicLongFieldUpdater<ShutdownHttpFilter> activeRequestsUpdater = AtomicLongFieldUpdater.newUpdater(ShutdownHttpFilter.class, "activeRequests");
/*     */   
/*     */   private volatile long activeRequests;
/*     */   
/*     */   private volatile boolean shutdown;
/*     */   private CountDownLatch latch;
/*  39 */   protected final Log LOG = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${cebas.shutdown.timeout.http}")
/*     */   private int shutdownTimeout;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
/*  50 */     if (this.shutdown) {
/*  51 */       HttpServletResponse resp = (HttpServletResponse)response;
/*  52 */       resp.sendError(503, "SpringBoot shutting down");
/*     */     }
/*     */     else {
/*     */       
/*  56 */       activeRequestsUpdater.incrementAndGet(this);
/*     */       try {
/*  58 */         chain.doFilter(request, response);
/*     */       } finally {
/*  60 */         if (this.shutdown) {
/*  61 */           getCountDownLatch().countDown();
/*     */         }
/*  63 */         activeRequestsUpdater.decrementAndGet(this);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized CountDownLatch getCountDownLatch() {
/*  74 */     if (this.latch == null) {
/*     */       try {
/*  76 */         this
/*  77 */           .latch = new CountDownLatch((int)activeRequestsUpdater.get(this));
/*  78 */       } catch (IllegalArgumentException e) {
/*  79 */         this.LOG.debug(e.getMessage());
/*  80 */         this.latch = new CountDownLatch(0);
/*     */       } 
/*     */     }
/*  83 */     return this.latch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() throws InterruptedException {
/*  94 */     this.shutdown = true;
/*  95 */     boolean allFinished = getCountDownLatch().await(this.shutdownTimeout, TimeUnit.MILLISECONDS);
/*     */     
/*  97 */     if (!allFinished)
/*     */     {
/*     */       
/* 100 */       this.LOG.info("Shutdown - Not all http threads finished during timeout. They will be canceled during shutdown.");
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
/*     */   @Bean
/*     */   public FilterRegistrationBean myFilterBean() {
/* 114 */     FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
/* 115 */     filterRegBean.setFilter(this);
/* 116 */     filterRegBean.addUrlPatterns(new String[] { "/*" });
/* 117 */     filterRegBean.setEnabled(Boolean.TRUE.booleanValue());
/* 118 */     filterRegBean.setName("Graceful HTTP Filter");
/* 119 */     filterRegBean.setOrder(-2147483648);
/* 120 */     filterRegBean.setAsyncSupported(Boolean.TRUE.booleanValue());
/* 121 */     return filterRegBean;
/*     */   }
/*     */   
/*     */   public void init(FilterConfig filterConfig) throws ServletException {}
/*     */   
/*     */   public void destroy() {}
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\gracefulshutdown\ShutdownHttpFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */