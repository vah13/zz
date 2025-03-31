/*    */ package com.daimler.cebas.certificates.entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BackendContext
/*    */ {
/*    */   private final String zkNo;
/*    */   private final String ecuPackageTs;
/*    */   private String linkCertTs;
/*    */   
/*    */   public BackendContext(String zkNo, String ecuPackageTs, String linkCertTs) {
/* 12 */     this.zkNo = zkNo;
/* 13 */     this.ecuPackageTs = ecuPackageTs;
/* 14 */     this.linkCertTs = linkCertTs;
/*    */   }
/*    */   
/*    */   public String getZkNo() {
/* 18 */     return this.zkNo;
/*    */   }
/*    */   
/*    */   public String getEcuPackageTs() {
/* 22 */     return this.ecuPackageTs;
/*    */   }
/*    */   public String getLinkCertTs() {
/* 25 */     return this.linkCertTs;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\entity\BackendContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */