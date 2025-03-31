/*    */ package com.daimler.cebas.system.control.performance;
/*    */ 
/*    */ import com.daimler.cebas.common.control.AbstractRepository;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASRepository;
/*    */ import com.daimler.cebas.system.entity.PerformanceAuditEntry;
/*    */ import java.util.Date;
/*    */ import java.util.List;
/*    */ import javax.persistence.criteria.CriteriaBuilder;
/*    */ import javax.persistence.criteria.CriteriaDelete;
/*    */ import javax.persistence.criteria.Expression;
/*    */ import javax.persistence.criteria.Root;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASRepository
/*    */ public class PerformanceAuditRepository
/*    */   extends AbstractRepository
/*    */ {
/*    */   public List<PerformanceAuditEntry> getPerformanceEntries() {
/* 25 */     return findAll(PerformanceAuditEntry.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void deleteEntriesOlderThanDate(Date maxDate) {
/* 34 */     CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
/*    */     
/* 36 */     CriteriaDelete<PerformanceAuditEntry> deleteStatement = criteriaBuilder.createCriteriaDelete(PerformanceAuditEntry.class);
/*    */     
/* 38 */     Root<PerformanceAuditEntry> root = deleteStatement.from(PerformanceAuditEntry.class);
/* 39 */     deleteStatement.where((Expression)criteriaBuilder
/* 40 */         .lessThan((Expression)root.get("createTimestamp"), maxDate));
/* 41 */     this.em.createQuery(deleteStatement).executeUpdate();
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\performance\PerformanceAuditRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */