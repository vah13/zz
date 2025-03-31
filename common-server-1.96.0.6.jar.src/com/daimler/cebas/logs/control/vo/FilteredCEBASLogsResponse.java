/*    */ package com.daimler.cebas.logs.control.vo;
/*    */ 
/*    */ import com.daimler.cebas.logs.entity.CEBASLog;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FilteredCEBASLogsResponse
/*    */ {
/*    */   private List<CEBASLog> filteredLogs;
/*    */   private int totalSize;
/*    */   
/*    */   public FilteredCEBASLogsResponse(List<CEBASLog> filteredLogs, int totalSize) {
/* 14 */     this.filteredLogs = filteredLogs;
/* 15 */     this.totalSize = totalSize;
/*    */   }
/*    */   
/*    */   public List<CEBASLog> getFilteredLogs() {
/* 19 */     return this.filteredLogs;
/*    */   }
/*    */   
/*    */   public int getTotalSize() {
/* 23 */     return this.totalSize;
/*    */   }
/*    */   
/*    */   public void setFilteredLogs(List<CEBASLog> filteredLogs) {
/* 27 */     this.filteredLogs = filteredLogs;
/*    */   }
/*    */   
/*    */   public void setTotalSize(int totalSize) {
/* 31 */     this.totalSize = totalSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\logs\control\vo\FilteredCEBASLogsResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */