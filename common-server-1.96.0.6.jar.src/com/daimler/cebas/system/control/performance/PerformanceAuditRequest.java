/*     */ package com.daimler.cebas.system.control.performance;
/*     */ 
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*     */ import com.daimler.cebas.system.entity.PerformanceAuditEntry;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import java.util.Date;
/*     */ import javax.annotation.PostConstruct;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.web.context.annotation.RequestScope;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASControl
/*     */ @RequestScope
/*     */ public class PerformanceAuditRequest
/*     */ {
/*     */   private Session session;
/*     */   private AbstractConfigurator configurator;
/*     */   private PerformanceAuditManager performanceAuditManager;
/*     */   private PerformanceAuditEntry performanceAuditEntry;
/*     */   
/*     */   @Autowired
/*     */   public PerformanceAuditRequest(Session session, AbstractConfigurator configurator, PerformanceAuditManager performanceAuditManager) {
/*  34 */     this.session = session;
/*  35 */     this.configurator = configurator;
/*  36 */     this.performanceAuditManager = performanceAuditManager;
/*     */   }
/*     */   
/*     */   @PostConstruct
/*     */   public void init() {
/*  41 */     this.performanceAuditEntry = new PerformanceAuditEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartTime(Date startTimeDate) {
/*  50 */     this.performanceAuditEntry.setStartDate(startTimeDate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndTime(Date endTimeDate) {
/*  59 */     this.performanceAuditEntry.setEndDate(endTimeDate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassName(String className) {
/*  68 */     this.performanceAuditEntry.setClassName(className);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMethodName(String methodName) {
/*  77 */     this.performanceAuditEntry.setMethodName(methodName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCorrelationId(String correlationID) {
/*  86 */     this.performanceAuditEntry.setCorrelation_id(correlationID);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add() {
/*  93 */     this.performanceAuditEntry
/*  94 */       .setUserName(this.session.getCurrentUser().getUserName());
/*  95 */     this.performanceAuditEntry.setProjectVersion(this.configurator
/*  96 */         .readProperty("info.build.version"));
/*  97 */     this.performanceAuditEntry.setProjectRevision(this.configurator
/*  98 */         .readProperty("info.build.svn.revision"));
/*  99 */     this.performanceAuditEntry
/* 100 */       .setDuration(Long.valueOf(this.performanceAuditEntry.getEndDate().getTime() - this.performanceAuditEntry
/* 101 */           .getStartDate().getTime()));
/* 102 */     this.performanceAuditManager.addToPerformanceList(this.performanceAuditEntry);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\performance\PerformanceAuditRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */