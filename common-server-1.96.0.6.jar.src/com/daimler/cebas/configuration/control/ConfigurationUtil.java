/*    */ package com.daimler.cebas.configuration.control;
/*    */ 
/*    */ import com.daimler.cebas.common.control.CEBASException;
/*    */ import com.daimler.cebas.common.control.CEBASProperty;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
/*    */ import com.daimler.cebas.configuration.entity.Configuration;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.users.entity.User;
/*    */ import java.util.Optional;
/*    */ import java.util.logging.Level;
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
/*    */ public class ConfigurationUtil
/*    */ {
/* 25 */   private static final String CLASS_NAME = ConfigurationUtil.class.getSimpleName();
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
/*    */   public static Configuration getConfigurationForUser(User user, CEBASProperty zenZefiProperty, Logger logger, MetadataManager i18n) {
/* 45 */     String METHOD_NAME = "getConfigurationForUser";
/* 46 */     logger.entering(CLASS_NAME, "getConfigurationForUser");
/*    */     
/* 48 */     Optional<Configuration> configOptional = user.getConfigurations().stream().filter(config -> config.getConfigKey().equals(zenZefiProperty.name())).findFirst();
/* 49 */     Configuration configuration = configOptional.<Throwable>orElseThrow(logger.logWithTranslationSupplier(Level.SEVERE, "000045X", new String[] { zenZefiProperty
/* 50 */             .name() }, (CEBASException)new ZenZefiConfigurationException(i18n
/*    */             
/* 52 */             .getMessage("couldNotFindConfiguration", new String[] { zenZefiProperty.name() }), "couldNotFindConfiguration")));
/*    */     
/* 54 */     logger.exiting(CLASS_NAME, "getConfigurationForUser");
/* 55 */     return configuration;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isAutomaticSelection(User user, Logger logger, MetadataManager i18n) {
/* 66 */     Configuration certSelectionConfiguration = getConfigurationForUser(user, CEBASProperty.CERT_SELECTION, logger, i18n);
/*    */     
/* 68 */     if (certSelectionConfiguration == null) {
/* 69 */       throw new ZenZefiConfigurationException("Certificate selection configuration does not exist for current user");
/*    */     }
/*    */     
/* 72 */     return isAutomaticSelection(certSelectionConfiguration);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isAutomaticSelection(Configuration certSelectionConfiguration) {
/* 82 */     return "automatic".equals(certSelectionConfiguration.getConfigValue());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean hasUserExtendedValidation(User user, Logger logger, MetadataManager i18n) {
/* 94 */     Configuration extendedValidation = getConfigurationForUser(user, CEBASProperty.VALIDATE_CERTS, logger, i18n);
/*    */     
/* 96 */     return extendedValidation.getConfigValue().equalsIgnoreCase(Boolean.TRUE.toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\ConfigurationUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */