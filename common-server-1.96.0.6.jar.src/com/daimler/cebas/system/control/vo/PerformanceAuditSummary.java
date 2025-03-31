/*     */ package com.daimler.cebas.system.control.vo;
/*     */ 
/*     */ import com.daimler.cebas.system.entity.PerformanceAuditEntry;
/*     */ import io.swagger.annotations.ApiModelProperty;
/*     */ import java.util.Date;
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
/*     */ public class PerformanceAuditSummary
/*     */ {
/*     */   private String id;
/*     */   private String createUser;
/*     */   private Long createTimestamp;
/*     */   private String updateUser;
/*     */   @ApiModelProperty(dataType = "java.lang.String")
/*     */   private Date updateTimestamp;
/*     */   private String userName;
/*     */   private String className;
/*     */   private String methodName;
/*     */   private Long duration;
/*     */   private String projectVersion;
/*     */   private String projectRevision;
/*     */   private String correlation_id;
/*     */   
/*     */   public PerformanceAuditSummary(PerformanceAuditEntry performanceAuditEntry) {
/*  73 */     this.id = performanceAuditEntry.getEntityId();
/*  74 */     this.createUser = performanceAuditEntry.getCreateUser();
/*  75 */     this.createTimestamp = Long.valueOf(performanceAuditEntry.getCreateTimestamp()
/*  76 */         .getTime());
/*  77 */     this.updateUser = performanceAuditEntry.getUpdateUser();
/*  78 */     this.updateTimestamp = performanceAuditEntry.getUpdateTimestamp();
/*  79 */     this.userName = performanceAuditEntry.getUserName();
/*  80 */     this.className = performanceAuditEntry.getClassName();
/*  81 */     this.methodName = performanceAuditEntry.getMethodName();
/*  82 */     this.duration = performanceAuditEntry.getDuration();
/*  83 */     this.projectVersion = performanceAuditEntry.getProjectVersion();
/*  84 */     this.projectRevision = performanceAuditEntry.getProjectRevision();
/*  85 */     this.correlation_id = performanceAuditEntry.getCorrelation_id();
/*     */   }
/*     */   
/*     */   public String getId() {
/*  89 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getCreateUser() {
/*  93 */     return this.createUser;
/*     */   }
/*     */   
/*     */   public Long getCreateTimestamp() {
/*  97 */     return this.createTimestamp;
/*     */   }
/*     */   
/*     */   public String getUpdateUser() {
/* 101 */     return this.updateUser;
/*     */   }
/*     */   
/*     */   public Date getUpdateTimestamp() {
/* 105 */     return this.updateTimestamp;
/*     */   }
/*     */   
/*     */   public String getUserName() {
/* 109 */     return this.userName;
/*     */   }
/*     */   
/*     */   public String getClassName() {
/* 113 */     return this.className;
/*     */   }
/*     */   
/*     */   public String getMethodName() {
/* 117 */     return this.methodName;
/*     */   }
/*     */   
/*     */   public Long getDuration() {
/* 121 */     return this.duration;
/*     */   }
/*     */   
/*     */   public String getProjectVersion() {
/* 125 */     return this.projectVersion;
/*     */   }
/*     */   
/*     */   public String getProjectRevision() {
/* 129 */     return this.projectRevision;
/*     */   }
/*     */   
/*     */   public String getCorrelation_id() {
/* 133 */     return this.correlation_id;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\vo\PerformanceAuditSummary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */