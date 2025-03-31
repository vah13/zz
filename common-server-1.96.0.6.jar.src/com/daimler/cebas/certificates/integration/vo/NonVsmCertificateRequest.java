/*    */ package com.daimler.cebas.certificates.integration.vo;
/*    */ 
/*    */ import java.util.List;
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
/*    */ public class NonVsmCertificateRequest
/*    */ {
/*    */   private String caId;
/*    */   private String ski;
/*    */   private String cn;
/*    */   private List<String> ecuUniqueIds;
/*    */   
/*    */   public NonVsmCertificateRequest() {}
/*    */   
/*    */   public NonVsmCertificateRequest(String caId, String ski, String cn, List<String> ecuUniqueIds) {
/* 31 */     this.caId = caId;
/* 32 */     this.ski = ski;
/* 33 */     this.cn = cn;
/* 34 */     this.ecuUniqueIds = ecuUniqueIds;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCaId(String caId) {
/* 42 */     this.caId = caId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCaId() {
/* 49 */     return this.caId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSki() {
/* 56 */     return this.ski;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSki(String ski) {
/* 64 */     this.ski = ski;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCn() {
/* 71 */     return this.cn;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCn(String cn) {
/* 79 */     this.cn = cn;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> getEcuUniqueIds() {
/* 86 */     return this.ecuUniqueIds;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setEcuUniqueIds(List<String> ecuUniqueIds) {
/* 94 */     this.ecuUniqueIds = ecuUniqueIds;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\integration\vo\NonVsmCertificateRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */