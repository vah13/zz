/*     */ package com.daimler.cebas.certificates.integration.vo;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.validation.failure.ValidationState;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.annotation.JsonProperty;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JsonIgnoreProperties(ignoreUnknown = true)
/*     */ public class EnhancedRightsPermission
/*     */   extends ValidationState
/*     */ {
/*     */   private String enrollmentId;
/*     */   private List<String> targetECU;
/*     */   private List<String> targetVIN;
/*     */   @JsonProperty("sid")
/*     */   private List<String> serviceIds;
/*     */   private String renewal;
/*     */   private String lifeTime;
/*     */   
/*     */   public EnhancedRightsPermission() {}
/*     */   
/*     */   public EnhancedRightsPermission(String enrollmentId, List<String> targetECU, List<String> targetVIN, List<String> serviceIds, String renewal, String lifeTime) {
/*  66 */     this.enrollmentId = enrollmentId;
/*  67 */     this.targetECU = targetECU;
/*  68 */     this.targetVIN = targetVIN;
/*  69 */     this.serviceIds = serviceIds;
/*  70 */     this.renewal = renewal;
/*  71 */     this.lifeTime = lifeTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEnrollmentId() {
/*  80 */     return this.enrollmentId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getTargetECU() {
/*  89 */     return this.targetECU;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getTargetVIN() {
/*  98 */     return this.targetVIN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getServiceIds() {
/* 107 */     return this.serviceIds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRenewal() {
/* 116 */     return this.renewal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLifeTime() {
/* 125 */     return this.lifeTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 133 */     return "EnhancedRightsPermission{enrollmentId='" + this.enrollmentId + '\'' + ", targetECU=" + this.targetECU + ", targetVIN=" + this.targetVIN + ", serviceIds=" + this.serviceIds + ", renewal='" + this.renewal + '\'' + ", lifeTime='" + this.lifeTime + '\'' + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\integration\vo\EnhancedRightsPermission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */