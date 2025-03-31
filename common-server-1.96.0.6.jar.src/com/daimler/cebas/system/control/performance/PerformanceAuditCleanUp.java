/*    */ package com.daimler.cebas.system.control.performance;
/*    */ 
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import java.util.Date;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
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
/*    */ @CEBASControl
/*    */ public class PerformanceAuditCleanUp
/*    */ {
/*    */   private PerformanceAuditRepository performanceAuditRepository;
/*    */   
/*    */   @Autowired
/*    */   public PerformanceAuditCleanUp(PerformanceAuditRepository performanceAuditRepository) {
/* 25 */     this.performanceAuditRepository = performanceAuditRepository;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*    */   public void deletePerformanceEntries(String maxAgeLogEntry) {
/* 36 */     Date maxDate = new Date(System.currentTimeMillis() - Integer.valueOf(maxAgeLogEntry).intValue());
/* 37 */     this.performanceAuditRepository.deleteEntriesOlderThanDate(maxDate);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\performance\PerformanceAuditCleanUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */