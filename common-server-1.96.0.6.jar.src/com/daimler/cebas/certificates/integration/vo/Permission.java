/*     */ package com.daimler.cebas.certificates.integration.vo;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.validation.failure.ValidationState;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import java.sql.Timestamp;
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
/*     */ public class Permission
/*     */   extends ValidationState
/*     */ {
/*     */   private String enrollmentId;
/*     */   private List<String> validCAs;
/*     */   private List<String> targetECU;
/*     */   private List<String> targetVIN;
/*     */   private String renewal;
/*     */   private String pkiRole;
/*     */   private String userRole;
/*     */   private String requester;
/*     */   private String assignee;
/*     */   private String assigner;
/*     */   private Timestamp validity;
/*     */   private String lifetime;
/*     */   private String review;
/*     */   private List<String> uniqueECUID;
/*     */   private String specialECU;
/*     */   private List<EnhancedRightsPermission> services;
/*     */   @JsonIgnore
/*     */   private boolean valid = true;
/*     */   
/*     */   public Permission() {}
/*     */   
/*     */   public Permission(String enrollmentId, List<String> validCAs, List<String> targetECU, List<String> targetVIN, String renewal, String pkiRole, String userRole, String requester, String assignee, String assigner, Timestamp validity, String lifetime, String review, List<String> uniqueECUID, String specialECU, List<EnhancedRightsPermission> services) {
/* 132 */     this.enrollmentId = enrollmentId;
/* 133 */     this.validCAs = validCAs;
/* 134 */     this.targetECU = targetECU;
/* 135 */     this.targetVIN = targetVIN;
/* 136 */     this.renewal = renewal;
/* 137 */     this.pkiRole = pkiRole;
/* 138 */     this.userRole = userRole;
/* 139 */     this.requester = requester;
/* 140 */     this.assignee = assignee;
/* 141 */     this.assigner = assigner;
/* 142 */     this.validity = validity;
/* 143 */     this.lifetime = lifetime;
/* 144 */     this.review = review;
/* 145 */     this.services = services;
/* 146 */     this.uniqueECUID = uniqueECUID;
/* 147 */     this.specialECU = specialECU;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEnrollmentId() {
/* 156 */     return this.enrollmentId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRequester() {
/* 165 */     return this.requester;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAssignee() {
/* 174 */     return this.assignee;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAssigner() {
/* 183 */     return this.assigner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserRole() {
/* 192 */     return this.userRole;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPkiRole() {
/* 201 */     return this.pkiRole;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Timestamp getValidity() {
/* 210 */     return this.validity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getTargetECU() {
/* 219 */     return this.targetECU;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getTargetVIN() {
/* 228 */     return this.targetVIN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLifetime() {
/* 237 */     return this.lifetime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRenewal() {
/* 246 */     return this.renewal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReview() {
/* 255 */     return this.review;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<EnhancedRightsPermission> getServices() {
/* 264 */     return this.services;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getUniqueECUID() {
/* 273 */     return this.uniqueECUID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSpecialECU() {
/* 282 */     return this.specialECU;
/*     */   }
/*     */   
/*     */   public List<String> getValidCAs() {
/* 286 */     return this.validCAs;
/*     */   }
/*     */   
/*     */   public void setValidCAs(List<String> validCAs) {
/* 290 */     this.validCAs = validCAs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValid(boolean valid) {
/* 298 */     this.valid = valid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValid() {
/* 306 */     return this.valid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 316 */     return "Permission{enrollmentId='" + this.enrollmentId + '\'' + ", requester='" + this.requester + '\'' + ", validCAs='" + this.validCAs + '\'' + ", assignee='" + this.assignee + '\'' + ", assigner='" + this.assigner + '\'' + ", userRole='" + this.userRole + '\'' + ", pkiRole='" + this.pkiRole + '\'' + ", validity=" + this.validity + ", targetECU=" + this.targetECU + ", targetVIN=" + this.targetVIN + ", lifetime='" + this.lifetime + '\'' + ", renewal='" + this.renewal + '\'' + ", review='" + this.review + '\'' + ", services=" + this.services + ", uniqueECUID='" + this.uniqueECUID + "', specialECU='" + this.specialECU + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\integration\vo\Permission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */