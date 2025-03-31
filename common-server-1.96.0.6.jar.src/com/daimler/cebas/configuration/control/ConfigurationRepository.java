/*     */ package com.daimler.cebas.configuration.control;
/*     */ 
/*     */ import com.daimler.cebas.common.control.AbstractRepository;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASRepository;
/*     */ import com.daimler.cebas.common.entity.AbstractEntity;
/*     */ import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
/*     */ import com.daimler.cebas.configuration.entity.Configuration;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Logger;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASRepository
/*     */ public class ConfigurationRepository
/*     */   extends AbstractRepository
/*     */ {
/*  26 */   private static final Logger LOG = Logger.getLogger(ConfigurationRepository.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  31 */   private static final String CLASS_NAME = ConfigurationRepository.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MetadataManager i18n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public ConfigurationRepository(MetadataManager i18n) {
/*  45 */     this.i18n = i18n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<Configuration> findConfigurationByConfigKey(String configKey) {
/*  55 */     String METHOD_NAME = "findConfigurationByConfigKey";
/*  56 */     LOG.entering(CLASS_NAME, METHOD_NAME);
/*  57 */     Map<String, Object> parameters = new HashMap<>();
/*  58 */     parameters.put("configKey", configKey);
/*  59 */     List<Configuration> resultList = findWithNamedQuery("Configuration_FIND_BY_CONFIG_KEY ", parameters, 1);
/*     */     
/*  61 */     Configuration configuration = null;
/*  62 */     if (!resultList.isEmpty()) {
/*  63 */       configuration = resultList.get(0);
/*     */     }
/*  65 */     LOG.exiting(CLASS_NAME, METHOD_NAME);
/*  66 */     return Optional.ofNullable(configuration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Configuration addConfiguration(String configKey, String configValue) {
/*  77 */     String METHOD_NAME = "addConfiguration";
/*  78 */     LOG.entering(CLASS_NAME, METHOD_NAME);
/*  79 */     Configuration configuration = new Configuration();
/*  80 */     configuration.setConfigKey(configKey);
/*  81 */     configuration.setConfigValue(configValue);
/*  82 */     LOG.exiting(CLASS_NAME, METHOD_NAME);
/*  83 */     return (Configuration)create((AbstractEntity)configuration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Configuration updateConfiguration(String configKey, String configValue) {
/*  94 */     String METHOD_NAME = "updateConfiguration";
/*  95 */     LOG.entering(CLASS_NAME, METHOD_NAME);
/*  96 */     LOG.exiting(CLASS_NAME, METHOD_NAME);
/*  97 */     return updateConfig(configKey, configValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRES_NEW)
/*     */   public Configuration updateConfigurationInNewTransaction(String configKey, String configValue) {
/* 112 */     String METHOD_NAME = "updateConfigurationInNewTransaction";
/* 113 */     LOG.entering(CLASS_NAME, METHOD_NAME);
/* 114 */     LOG.exiting(CLASS_NAME, METHOD_NAME);
/* 115 */     return updateConfig(configKey, configValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Configuration updateConfig(String configKey, String configValue) {
/* 128 */     Optional<Configuration> configurationOptional = findConfigurationByConfigKey(configKey);
/* 129 */     Configuration configuration = configurationOptional.<Throwable>orElseThrow(() -> new ZenZefiConfigurationException(this.i18n.getMessage("couldNotUpdateConfiguration")));
/*     */     
/* 131 */     configuration.setConfigValue(configValue);
/* 132 */     return (Configuration)update((AbstractEntity)configuration);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\ConfigurationRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */