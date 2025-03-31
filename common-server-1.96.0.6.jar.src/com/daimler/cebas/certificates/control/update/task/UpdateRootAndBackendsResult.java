/*    */ package com.daimler.cebas.certificates.control.update.task;
/*    */ 
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UpdateRootAndBackendsResult
/*    */ {
/* 13 */   private List<Certificate> updatedRootAndBackends = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public void addUpdatedBackend(Certificate be) {
/* 17 */     this.updatedRootAndBackends.add(be);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Certificate> getUpdatedRootAndBackends() {
/* 24 */     return this.updatedRootAndBackends;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\contro\\update\task\UpdateRootAndBackendsResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */