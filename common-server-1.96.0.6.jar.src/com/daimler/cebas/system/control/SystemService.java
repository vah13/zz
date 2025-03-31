/*    */ package com.daimler.cebas.system.control;
/*    */ 
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASService;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.system.control.performance.PerformanceAuditRepository;
/*    */ import com.daimler.cebas.system.control.vo.PerformanceAuditSummary;
/*    */ import com.daimler.cebas.system.entity.PerformanceAuditEntry;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import java.util.List;
/*    */ import java.util.logging.Level;
/*    */ import java.util.stream.Collectors;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASService
/*    */ public class SystemService
/*    */ {
/* 25 */   private static final String CLASS_NAME = SystemService.class.getSimpleName();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Logger logger;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected MetadataManager i18n;
/*    */ 
/*    */ 
/*    */   
/*    */   protected Session session;
/*    */ 
/*    */ 
/*    */   
/*    */   private PerformanceAuditRepository performanceAuditRepository;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   public SystemService(Logger logger, MetadataManager i18n, Session session, PerformanceAuditRepository performanceAuditRepository) {
/* 50 */     this.logger = logger;
/* 51 */     this.i18n = i18n;
/* 52 */     this.session = session;
/* 53 */     this.performanceAuditRepository = performanceAuditRepository;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<PerformanceAuditSummary> getPerformanceAuditEntrySummaries() {
/* 62 */     String METHOD_NAME = "getDatabasePerformanceEntries";
/* 63 */     this.logger.entering(CLASS_NAME, "getDatabasePerformanceEntries");
/* 64 */     this.logger.exiting(CLASS_NAME, "getDatabasePerformanceEntries");
/* 65 */     this.logger.log(Level.INFO, "000530", "Loading existing performance audit entries.", CLASS_NAME);
/*    */     
/* 67 */     return (List<PerformanceAuditSummary>)getDatabasePerformanceEntries().stream().map(p -> new PerformanceAuditSummary(p))
/* 68 */       .collect(Collectors.toList());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private List<PerformanceAuditEntry> getDatabasePerformanceEntries() {
/* 77 */     String METHOD_NAME = "getDatabasePerformanceEntries";
/* 78 */     this.logger.entering(CLASS_NAME, "getDatabasePerformanceEntries");
/* 79 */     this.logger.exiting(CLASS_NAME, "getDatabasePerformanceEntries");
/* 80 */     return this.performanceAuditRepository.getPerformanceEntries();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String composeMessageForCurrentUser() {
/*    */     String currentUserName;
/* 90 */     if (this.session.isDefaultUser()) {
/* 91 */       currentUserName = this.i18n.getMessage("defaultUser");
/*    */     } else {
/* 93 */       currentUserName = this.session.getCurrentUser().getUserName();
/*    */     } 
/* 95 */     return currentUserName;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\SystemService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */