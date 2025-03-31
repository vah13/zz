/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import io.swagger.annotations.ApiModel;
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
/*    */ @ApiModel
/*    */ public class UpdateCertificatesRetryInfo
/*    */ {
/*    */   private String endpoint;
/*    */   private Integer maxRetries;
/*    */   private Integer currentRetry;
/*    */   private Integer nextRetryTime;
/*    */   private long nextRetryTimestamp;
/*    */   
/*    */   public UpdateCertificatesRetryInfo() {}
/*    */   
/*    */   public UpdateCertificatesRetryInfo(String endpoint, Integer maxRetries, Integer currentRetry, Integer nextRetryTime, long nexRetryTimestamp) {
/* 58 */     this.endpoint = endpoint;
/* 59 */     this.maxRetries = maxRetries;
/* 60 */     this.currentRetry = currentRetry;
/* 61 */     this.nextRetryTime = nextRetryTime;
/* 62 */     this.nextRetryTimestamp = nexRetryTimestamp;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getEndpoint() {
/* 69 */     return this.endpoint;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Integer getMaxRetries() {
/* 76 */     return this.maxRetries;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Integer getCurrentRetry() {
/* 83 */     return this.currentRetry;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Integer getNextRetryTime() {
/* 90 */     return this.nextRetryTime;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getNextRetryTimestamp() {
/* 97 */     return this.nextRetryTimestamp;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\UpdateCertificatesRetryInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */