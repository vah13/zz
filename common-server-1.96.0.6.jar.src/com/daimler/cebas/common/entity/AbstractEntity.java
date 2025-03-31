/*     */ package com.daimler.cebas.common.entity;
/*     */ 
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import com.fasterxml.jackson.annotation.JsonProperty;
/*     */ import io.swagger.annotations.ApiModelProperty;
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ import java.util.UUID;
/*     */ import javax.persistence.Column;
/*     */ import javax.persistence.Id;
/*     */ import javax.persistence.MappedSuperclass;
/*     */ import javax.persistence.PrePersist;
/*     */ import javax.persistence.PreUpdate;
/*     */ import javax.persistence.Temporal;
/*     */ import javax.persistence.TemporalType;
/*     */ import org.apache.commons.lang3.builder.EqualsBuilder;
/*     */ import org.apache.commons.lang3.builder.HashCodeBuilder;
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
/*     */ @MappedSuperclass
/*     */ public abstract class AbstractEntity
/*     */   implements Serializable
/*     */ {
/*     */   public static final String F_ID = "f_id";
/*     */   public static final String F_CREATE_USER = "f_create_user";
/*     */   public static final String F_UPDATE_USER = "f_update_user";
/*     */   public static final String F_CREATE_TIMESTAMP = "f_create_timestamp";
/*     */   public static final String F_UPDATE_TIMESTAMP = "f_update_timestamp";
/*     */   public static final String ID = "id";
/*     */   public static final String ENTITY_ID = "entityId";
/*     */   public static final String CREATE_USER = "createUser";
/*     */   public static final String UPDATE_USER = "updateUser";
/*     */   public static final String CREATE_TIMESTAMP = "createTimestamp";
/*     */   public static final String UPDATE_TIMESTAMP = "updateTimestamp";
/*     */   public static final int ID_LENGTH = 36;
/*     */   public static final int USER_LENGTH = 50;
/*     */   protected static final String PACKAGE_NAME = "com.daimler.cebas.zenzefi.common";
/*     */   private static final long serialVersionUID = -2274627895272084434L;
/*     */   @Id
/*     */   @Column(name = "f_id", unique = true, length = 36)
/*     */   private String entityId;
/*     */   @Column(name = "f_create_user", length = 50)
/*     */   @JsonIgnore
/*     */   private String createUser;
/*     */   @Column(name = "f_create_timestamp")
/*     */   @Temporal(TemporalType.TIMESTAMP)
/*     */   @ApiModelProperty(dataType = "java.lang.String")
/*     */   private Date createTimestamp;
/*     */   @Column(name = "f_update_user", length = 50)
/*     */   @JsonIgnore
/*     */   private String updateUser;
/*     */   @Column(name = "f_update_timestamp")
/*     */   @Temporal(TemporalType.TIMESTAMP)
/*     */   @JsonIgnore
/*     */   private Date updateTimestamp;
/*     */   
/*     */   @PrePersist
/*     */   public void onPrePersist() {
/* 146 */     this.entityId = UUID.randomUUID().toString();
/* 147 */     this.createTimestamp = new Date();
/* 148 */     this.createUser = Session.getUserId();
/*     */   }
/*     */   
/*     */   @PreUpdate
/*     */   public void onPreUpdate() {
/* 153 */     this.updateTimestamp = new Date();
/* 154 */     this.updateUser = Session.getUserId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @JsonProperty("id")
/*     */   public String getEntityId() {
/* 164 */     return this.entityId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreateUser() {
/* 173 */     return this.createUser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreateUser(String createUser) {
/* 183 */     this.createUser = createUser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUpdateUser() {
/* 192 */     return this.updateUser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUpdateUser(String updateUser) {
/* 202 */     this.updateUser = updateUser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getUpdateTimestamp() {
/* 211 */     return this.updateTimestamp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCreateTimestamp() {
/* 220 */     return this.createTimestamp;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 226 */     return (new HashCodeBuilder(17, 37)).append(getEntityId()).toHashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 232 */     boolean result = (obj != null && obj.getClass() == getClass() && obj == this);
/*     */     
/* 234 */     return (result && (new EqualsBuilder()).append(this.entityId, ((AbstractEntity)obj).getEntityId()).isEquals());
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\entity\AbstractEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */