/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.configuration.control;
/*    */ 
/*    */ import com.daimler.cebas.common.control.CEBASProperty;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.configuration.control.ConfigurationRepository;
/*    */ import com.daimler.cebas.configuration.control.ConfigurationUtil;
/*    */ import com.daimler.cebas.configuration.control.PkiUrlProperty;
/*    */ import com.daimler.cebas.configuration.entity.Configuration;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.users.entity.User;
/*    */ import com.daimler.cebas.zenzefi.configuration.control.ZenZefiConfigurationsService;
/*    */ import com.daimler.cebas.zenzefi.configuration.control.ZenZefiPkiManager;
/*    */ import java.util.logging.Level;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.core.env.ConfigurableEnvironment;
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
/*    */ @CEBASControl
/*    */ public class PkiScopeManager
/*    */ {
/*    */   private Logger logger;
/*    */   private MetadataManager i18n;
/*    */   private ConfigurationRepository repository;
/*    */   private ZenZefiPkiManager zenZefiPkiManager;
/*    */   private ConfigurableEnvironment configurableEnvironment;
/*    */   
/*    */   @Autowired
/*    */   public PkiScopeManager(Logger logger, MetadataManager i18n, ConfigurationRepository repository, ZenZefiPkiManager zenZefiPkiManager, ConfigurableEnvironment configurableEnvironment) {
/* 49 */     this.logger = logger;
/* 50 */     this.i18n = i18n;
/* 51 */     this.repository = repository;
/* 52 */     this.zenZefiPkiManager = zenZefiPkiManager;
/* 53 */     this.configurableEnvironment = configurableEnvironment;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPkiscope(boolean scope) {
/* 64 */     String stringValue = Boolean.toString(scope);
/* 65 */     this.repository.findConfigurationByConfigKey(CEBASProperty.PKISCOPE_SWITCH.name())
/* 66 */       .ifPresent(config -> config.setConfigValue(stringValue));
/*    */     
/* 68 */     this.configurableEnvironment.getSystemProperties().put(CEBASProperty.PKISCOPE_SWITCH.name(), stringValue);
/* 69 */     this.zenZefiPkiManager.setPkiEnvironmentProperties();
/*    */     
/* 71 */     this.logger.log(Level.INFO, "000693", "The pki scope (override oidc scope) was set to: " + stringValue + ". The base url is now: " + this.configurableEnvironment
/*    */         
/* 73 */         .getSystemProperties().get(PkiUrlProperty.PKI_BASE_URL.getProperty()), ZenZefiConfigurationsService.class
/* 74 */         .getName());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDbStateToSystem() {
/* 81 */     this.repository.findConfigurationByConfigKey(CEBASProperty.PKISCOPE_SWITCH.name())
/* 82 */       .ifPresent(config -> this.configurableEnvironment.getSystemProperties().put(CEBASProperty.PKISCOPE_SWITCH.name(), config.getConfigValue()));
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
/*    */   
/*    */   public boolean getPkiscope(User user) {
/* 95 */     String configValue = ConfigurationUtil.getConfigurationForUser(user, CEBASProperty.PKISCOPE_SWITCH, this.logger, this.i18n).getConfigValue();
/*    */     
/* 97 */     return Boolean.parseBoolean(configValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\configuration\control\PkiScopeManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */