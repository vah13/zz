/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import java.util.Date;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DateFilter
/*    */ {
/*    */   private Date dateStart;
/*    */   private Date dateEnd;
/*    */   
/*    */   public DateFilter(Date dateStart, Date dateEnd) {
/* 33 */     this.dateStart = dateStart;
/* 34 */     this.dateEnd = dateEnd;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Date getStart() {
/* 43 */     return this.dateStart;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Date getEnd() {
/* 52 */     return this.dateEnd;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\DateFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */