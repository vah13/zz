/*     */ package com.daimler.cebas.configuration.entity;
/*     */ 
/*     */ import com.daimler.cebas.common.entity.AbstractEntity;
/*     */ import javax.persistence.Column;
/*     */ import javax.persistence.Entity;
/*     */ import javax.persistence.NamedQueries;
/*     */ import javax.persistence.NamedQuery;
/*     */ import javax.persistence.Table;
/*     */ import org.hibernate.annotations.DynamicUpdate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Entity
/*     */ @Table(name = "r_configuration")
/*     */ @NamedQueries({@NamedQuery(name = "Configuration_FIND_BY_CONFIG_KEY ", query = "select g from Configuration g where g.configKey = :configKey")})
/*     */ @DynamicUpdate
/*     */ public class Configuration
/*     */   extends AbstractEntity
/*     */ {
/*     */   public static final String CLASS_NAME = "Configuration";
/*     */   public static final String R_CONFIGURATION = "r_configuration";
/*     */   public static final String NAMED_QUERY_FIND_BY_CONFIG_KEY = "Configuration_FIND_BY_CONFIG_KEY ";
/*     */   public static final String NAMED_PARAMETER_CONFIG_KEY = "configKey";
/*     */   private static final long serialVersionUID = -2729085032816932250L;
/*     */   private static final String F_CONFIG_KEY = "f_config_key";
/*     */   private static final String F_CONFIG_VALUE = "F_CONFIG_VALUE";
/*     */   private static final int CONFIG_KEY_LENGTH = 50;
/*     */   private static final int CONFIG_VALUE_LENGTH = 600;
/*     */   @Column(name = "f_config_key", nullable = false, length = 50)
/*     */   private String configKey;
/*     */   @Column(name = "F_CONFIG_VALUE", length = 600)
/*     */   private String configValue;
/*     */   @Column(name = "f_description", length = 256)
/*     */   private String description;
/*     */   
/*     */   public String getConfigKey() {
/*  83 */     return this.configKey;
/*     */   }
/*     */   
/*     */   public String getConfigValue() {
/*  87 */     return this.configValue;
/*     */   }
/*     */   
/*     */   public void setConfigValue(String configValue) {
/*  91 */     this.configValue = configValue;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/*  95 */     return this.description;
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/*  99 */     this.description = description;
/*     */   }
/*     */   
/*     */   public void setConfigKey(String configKey) {
/* 103 */     this.configKey = configKey;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\entity\Configuration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */