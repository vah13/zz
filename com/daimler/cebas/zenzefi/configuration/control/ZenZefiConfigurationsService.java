/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.configuration.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.cronjob.CertificatesCleanUp;
/*     */ import com.daimler.cebas.certificates.control.cronjob.CertificatesCronJobs;
/*     */ import com.daimler.cebas.common.control.CEBASProperty;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASService;
/*     */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*     */ import com.daimler.cebas.configuration.control.ConfigurationRepository;
/*     */ import com.daimler.cebas.configuration.control.ConfigurationUtil;
/*     */ import com.daimler.cebas.configuration.control.ConfigurationsService;
/*     */ import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
/*     */ import com.daimler.cebas.configuration.control.exceptions.ZenZefiNotAllowedConfigurationException;
/*     */ import com.daimler.cebas.configuration.entity.Configuration;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.PkiScopeManager;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.proxy.ProxyInput;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.proxy.ProxyManager;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.proxy.ProxyOutput;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.proxy.ProxyTypeInput;
/*     */ import com.daimler.cebas.zenzefi.users.control.idle.UserIdleTimer;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASService
/*     */ public class ZenZefiConfigurationsService
/*     */   extends ConfigurationsService
/*     */ {
/*  42 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.configuration.control.ZenZefiConfigurationsService.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ProxyManager proxyManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UserIdleTimer userIdleTimer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PkiScopeManager pkiScopeManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public ZenZefiConfigurationsService(Session session, ConfigurationRepository repository, Logger logger, CertificatesCronJobs certificateCronJobs, AbstractConfigurator configurator, MetadataManager i18n, ProxyManager proxyManager, UserIdleTimer userIdleTimer, CertificatesCleanUp cleanUpCertificates, PkiScopeManager pkiScopeManager) {
/*  85 */     super(session, repository, logger, certificateCronJobs, configurator, i18n, cleanUpCertificates);
/*  86 */     this.proxyManager = proxyManager;
/*  87 */     this.userIdleTimer = userIdleTimer;
/*  88 */     this.pkiScopeManager = pkiScopeManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxy(ProxyInput proxy) {
/*  98 */     String METHOD_NAME = "setProxy";
/*  99 */     this.logger.entering(CLASS_NAME, "setProxy");
/* 100 */     this.proxyManager.setProxyFromInput(proxy);
/* 101 */     this.logger.exiting(CLASS_NAME, "setProxy");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPkiscope(boolean scope) {
/* 111 */     if (!this.session.isDefaultUser()) {
/* 112 */       this.logger.log(Level.INFO, "000691X", "The pki scope can only be changed on default user level.", com.daimler.cebas.zenzefi.configuration.control.ZenZefiConfigurationsService.class
/*     */           
/* 114 */           .getName());
/* 115 */       throw new ZenZefiConfigurationException(this.i18n.getMessage("invalidUserScopeForPkiScopeChange"));
/*     */     } 
/* 117 */     this.pkiScopeManager.setPkiscope(scope);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getPkiscope() {
/* 127 */     return this.pkiScopeManager.getPkiscope(this.session.getDefaultUser());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyOutput getProxy() {
/* 136 */     String METHOD_NAME = "getProxy";
/* 137 */     this.logger.entering(CLASS_NAME, "getProxy");
/* 138 */     this.logger.exiting(CLASS_NAME, "getProxy");
/* 139 */     return this.proxyManager.getProxy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getProxyType() {
/* 148 */     String METHOD_NAME = "getProxyType";
/* 149 */     this.logger.entering(CLASS_NAME, "getProxyType");
/* 150 */     this.logger.exiting(CLASS_NAME, "getProxyType");
/* 151 */     return this.proxyManager.getProxyType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPKILogoutUrlProperty() {
/* 160 */     String METHOD_NAME = "getPKILogoutUrlProperty";
/* 161 */     String PKI_LOGOUT_URL = "pki.logout.url";
/* 162 */     this.logger.exiting(CLASS_NAME, "getPKILogoutUrlProperty");
/* 163 */     return this.configurator.readProperty("pki.logout.url");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyType(ProxyTypeInput input) {
/* 174 */     String METHOD_NAME = "setProxyType";
/* 175 */     this.logger.entering(CLASS_NAME, "setProxyType");
/* 176 */     this.proxyManager.setProxyTypeFromInput(input);
/* 177 */     this.logger.exiting(CLASS_NAME, "setProxyType");
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
/*     */   public void setWithProxyType(ProxyInput proxy, ProxyTypeInput proxyType) {
/* 189 */     setProxyType(proxyType);
/* 190 */     setProxy(proxy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPaginationMaxRowsPerPage() {
/* 199 */     String METHOD_NAME = "getPaginationMaxRowsPerPage";
/* 200 */     this.logger.entering(CLASS_NAME, "getPaginationMaxRowsPerPage");
/* 201 */     this.logger.exiting(CLASS_NAME, "getPaginationMaxRowsPerPage");
/* 202 */     return ConfigurationUtil.getConfigurationForUser(this.session.getCurrentUser(), CEBASProperty.PAGINATION_MAX_ROWS_PER_PAGE, this.logger, this.i18n)
/* 203 */       .getConfigValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updatePaginationMaxRowsPerPage(String newValue) {
/* 213 */     String METHOD_NAME = "updatePaginationMaxRowsPerPage";
/* 214 */     this.logger.entering(CLASS_NAME, "updatePaginationMaxRowsPerPage");
/*     */     try {
/* 216 */       ConfigurationUtil.getConfigurationForUser(this.session.getCurrentUser(), CEBASProperty.PAGINATION_MAX_ROWS_PER_PAGE, this.logger, this.i18n)
/*     */         
/* 218 */         .setConfigValue("" + Integer.valueOf(newValue));
/* 219 */     } catch (NumberFormatException e) {
/* 220 */       throw new ZenZefiConfigurationException(this.i18n.getMessage("invalidPaginationMaxRowsPerPage"));
/*     */     } 
/* 222 */     this.logger.exiting(CLASS_NAME, "updatePaginationMaxRowsPerPage");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateAutologout(boolean activateAutologout) {
/* 229 */     String METHOD_NAME = "updateAutologout";
/* 230 */     this.logger.entering(CLASS_NAME, "updateAutologout");
/*     */     
/* 232 */     if (this.session.isDefaultUser()) {
/* 233 */       throw new ZenZefiNotAllowedConfigurationException("Only an authenticated user can activate or deactivate automatically logout.");
/*     */     }
/*     */     
/* 236 */     this.userIdleTimer.setAutologout(activateAutologout);
/* 237 */     this.userIdleTimer.reset();
/* 238 */     this.logger.exiting(CLASS_NAME, "updateAutologout");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Configuration getAutologoutConfiguration() {
/* 247 */     Configuration autologoutConfiguration = new Configuration();
/* 248 */     autologoutConfiguration.setConfigKey(CEBASProperty.AUTOLOGOUT.name());
/* 249 */     autologoutConfiguration.setConfigValue(String.valueOf(this.userIdleTimer.isAutologout()));
/* 250 */     autologoutConfiguration.setDescription("Configuration for automatically logout");
/* 251 */     return autologoutConfiguration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
/*     */   public List<Configuration> getCurrentUserConfigurations() {
/* 260 */     List<Configuration> configurations = super.getCurrentUserConfigurations();
/*     */     
/* 262 */     if (!this.session.isDefaultUser()) {
/* 263 */       Configuration autologoutConfiguration = getAutologoutConfiguration();
/* 264 */       configurations.add(autologoutConfiguration);
/*     */     } 
/*     */     
/* 267 */     return configurations;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\configuration\control\ZenZefiConfigurationsService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */