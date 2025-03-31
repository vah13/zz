/*     */ package com.daimler.cebas.logs.entity;
/*     */ 
/*     */ import com.daimler.cebas.logs.control.LogCryptoEngine;
/*     */ import com.fasterxml.jackson.annotation.JsonView;
/*     */ import java.util.Date;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlTransient;
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
/*     */ @XmlRootElement(name = "log")
/*     */ public class CEBASLog
/*     */ {
/*     */   @XmlTransient
/*     */   @JsonView({LogsViews.Persisted.class, LogsViews.Management.class})
/*     */   private long id;
/*     */   @JsonView({LogsViews.Donwload.class})
/*     */   private String createUser;
/*     */   @JsonView({LogsViews.Donwload.class, LogsViews.Persisted.class, LogsViews.Management.class})
/*     */   private String type;
/*     */   @JsonView({LogsViews.Donwload.class, LogsViews.Persisted.class, LogsViews.Management.class})
/*     */   private String message;
/*     */   @JsonView({LogsViews.Donwload.class, LogsViews.Persisted.class})
/*     */   private String checksum;
/*     */   @JsonView({LogsViews.Donwload.class, LogsViews.Persisted.class, LogsViews.Management.class})
/*     */   private long createTimestamp;
/*     */   @JsonView({LogsViews.Donwload.class, LogsViews.Management.class})
/*     */   private boolean valid;
/*     */   
/*     */   public CEBASLog() {}
/*     */   
/*     */   public CEBASLog(String createUser, String type, String message) {
/*  74 */     this.createUser = createUser;
/*  75 */     this.type = type;
/*  76 */     this.message = message;
/*  77 */     this.createTimestamp = (new Date()).getTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/*  87 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(String type) {
/*  96 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 105 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessage(String message) {
/* 114 */     this.message = message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getChecksum() {
/* 123 */     return this.checksum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValid() {
/* 132 */     return this.valid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValid(boolean isValid) {
/* 141 */     this.valid = isValid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElement
/*     */   public long getCreateTimestamp() {
/* 151 */     return this.createTimestamp;
/*     */   }
/*     */   
/*     */   public void setId(long id) {
/* 155 */     this.id = id;
/* 156 */     this.checksum = LogCryptoEngine.hashWithSHA512((this.id + this.createTimestamp) + this.type + this.message);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 161 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreateUser() {
/* 170 */     return this.createUser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean validateLogChecksum() {
/* 179 */     return getChecksum().equals(LogCryptoEngine.hashWithSHA512((this.id + this.createTimestamp) + this.type + 
/* 180 */           getMessage()));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 185 */     return "CEBASLog [id=" + this.id + ", createUser=" + this.createUser + ", type=" + this.type + ", message=" + this.message + ", checksum=" + this.checksum + ", createTimestamp=" + this.createTimestamp + ", valid=" + this.valid + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\logs\entity\CEBASLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */