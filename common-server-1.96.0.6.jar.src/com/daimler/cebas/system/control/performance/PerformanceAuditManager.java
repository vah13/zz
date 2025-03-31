/*    */ package com.daimler.cebas.system.control.performance;
/*    */ 
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.common.entity.AbstractEntity;
/*    */ import com.daimler.cebas.system.entity.PerformanceAuditEntry;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.annotation.PostConstruct;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.beans.factory.annotation.Qualifier;
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASControl
/*    */ public class PerformanceAuditManager
/*    */ {
/*    */   @Autowired
/*    */   private PerformanceAuditRepository performanceAuditRepository;
/* 27 */   private final List<PerformanceAuditEntry> performanceAuditEntryList = Collections.synchronizedList(new ArrayList<>());
/*    */ 
/*    */ 
/*    */   
/*    */   @Value("${PERSIST_PERFORMANCE_AUDIT_ENTRY}")
/*    */   private String enablePerformanceAuditEntriesPersistance;
/*    */ 
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   @Qualifier("taskScheduler")
/*    */   private ThreadPoolTaskScheduler taskScheduler;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PostConstruct
/*    */   public void init() {
/* 45 */     if (Boolean.TRUE.toString()
/* 46 */       .equalsIgnoreCase(this.enablePerformanceAuditEntriesPersistance)) {
/* 47 */       this.taskScheduler.scheduleWithFixedDelay(this::pushAuditPerformanceEntries, 3000L);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addToPerformanceList(PerformanceAuditEntry performanceAuditEntry) {
/* 60 */     synchronized (this.performanceAuditEntryList) {
/* 61 */       this.performanceAuditEntryList.add(performanceAuditEntry);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void pushAuditPerformanceEntries() {
/* 69 */     if (Boolean.TRUE.toString()
/* 70 */       .equalsIgnoreCase(this.enablePerformanceAuditEntriesPersistance))
/* 71 */       synchronized (this.performanceAuditEntryList) {
/* 72 */         this.performanceAuditEntryList.forEach(performanceAuditEntryToAdd -> (PerformanceAuditEntry)this.performanceAuditRepository.create((AbstractEntity)performanceAuditEntryToAdd));
/*    */ 
/*    */         
/* 75 */         this.performanceAuditEntryList.clear();
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\performance\PerformanceAuditManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */