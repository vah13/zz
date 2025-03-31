/*     */ package com.daimler.cebas.configuration.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.cronjob.CertificatesCleanUp;
/*     */ import com.daimler.cebas.certificates.control.cronjob.CertificatesCronJobs;
/*     */ import com.daimler.cebas.common.control.CEBASProperty;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASService;
/*     */ import com.daimler.cebas.common.entity.AbstractEntity;
/*     */ import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
/*     */ import com.daimler.cebas.configuration.control.vo.RolePriorityConfiguration;
/*     */ import com.daimler.cebas.configuration.entity.Configuration;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.logging.Level;
/*     */ import java.util.stream.Collectors;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASService
/*     */ public class ConfigurationsService
/*     */ {
/*     */   private static final String DEFAULT = "default";
/*  43 */   private static final String CLASS_NAME = ConfigurationsService.class.getSimpleName();
/*     */ 
/*     */ 
/*     */   
/*     */   protected Logger logger;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Session session;
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurationRepository repository;
/*     */ 
/*     */ 
/*     */   
/*     */   private CertificatesCronJobs certificateCronJobs;
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractConfigurator configurator;
/*     */ 
/*     */ 
/*     */   
/*     */   protected MetadataManager i18n;
/*     */ 
/*     */ 
/*     */   
/*     */   private CertificatesCleanUp cleanUpCertificates;
/*     */ 
/*     */   
/*     */   private final Predicate<? super Configuration> userConfigurationsPredicate;
/*     */ 
/*     */   
/*     */   private final Predicate<? super Configuration> generalConfigurationsPredicate;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public ConfigurationsService(Session session, ConfigurationRepository repository, Logger logger, CertificatesCronJobs certificateCronJobs, AbstractConfigurator configurator, MetadataManager i18n, CertificatesCleanUp cleanUpCertificates) {
/*  83 */     this.userConfigurationsPredicate = (configuration -> (configuration.getConfigKey().equals(CEBASProperty.CERT_SELECTION.name()) || configuration.getConfigKey().equals(CEBASProperty.USER_ROLE.name()) || configuration.getConfigKey().equals(CEBASProperty.VALIDATE_CERTS.name()) || configuration.getConfigKey().equals(CEBASProperty.DELETE_EXPIRED_CERTS.name()) || configuration.getConfigKey().equals(CEBASProperty.AUTO_CERT_UPDATE.name())));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     this.generalConfigurationsPredicate = (configuration -> (configuration.getConfigKey().equals(CEBASProperty.DELETE_CERTS_SCHEDULE.name()) || configuration.getConfigKey().equals(CEBASProperty.PKISCOPE_SWITCH.name())));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     this.session = session;
/* 112 */     this.repository = repository;
/* 113 */     this.logger = logger;
/* 114 */     this.certificateCronJobs = certificateCronJobs;
/* 115 */     this.configurator = configurator;
/* 116 */     this.i18n = i18n;
/* 117 */     this.cleanUpCertificates = cleanUpCertificates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
/*     */   public List<Configuration> getCurrentUserConfigurations() {
/* 126 */     String METHOD_NAME = "getCurrentUserConfigurations";
/* 127 */     this.logger.entering(CLASS_NAME, "getCurrentUserConfigurations");
/*     */     
/* 129 */     User currentUser = this.session.getCurrentUser();
/*     */ 
/*     */     
/* 132 */     List<Configuration> configurations = (List<Configuration>)currentUser.getConfigurations().stream().filter(this.userConfigurationsPredicate).collect(Collectors.toList());
/*     */     
/* 134 */     this.logger.exiting(CLASS_NAME, "getCurrentUserConfigurations");
/* 135 */     return configurations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
/*     */   public List<Configuration> getGeneralConfigurations() {
/* 144 */     String METHOD_NAME = "getGeneralConfigurations";
/* 145 */     this.logger.entering(CLASS_NAME, "getGeneralConfigurations");
/* 146 */     User defaultUser = this.session.getDefaultUser();
/*     */ 
/*     */     
/* 149 */     List<Configuration> configurations = (List<Configuration>)defaultUser.getConfigurations().stream().filter(this.generalConfigurationsPredicate).collect(Collectors.toList());
/* 150 */     this.logger.exiting(CLASS_NAME, "getGeneralConfigurations");
/* 151 */     return configurations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Configuration updateConfiguration(Configuration configuration) {
/* 161 */     String METHOD_NAME = "updateConfiguration";
/* 162 */     this.logger.entering(CLASS_NAME, "updateConfiguration");
/*     */     
/* 164 */     Optional<Configuration> currentUserConfigOptional = this.repository.find(Configuration.class, configuration.getEntityId());
/* 165 */     if (currentUserConfigOptional.isPresent()) {
/* 166 */       Configuration currentUserConfig = currentUserConfigOptional.get();
/* 167 */       currentUserConfig.setConfigValue(configuration.getConfigValue());
/* 168 */       Configuration config = (Configuration)this.repository.update((AbstractEntity)currentUserConfig);
/* 169 */       this.repository.flush();
/* 170 */       if (config.getConfigKey().equals(CEBASProperty.DELETE_CERTS_SCHEDULE.name())) {
/* 171 */         this.certificateCronJobs.scheduleDeleteExpiredCertificatesForAllUsers();
/*     */       }
/* 173 */       if (config.getConfigKey().equals(CEBASProperty.VALIDATE_CERTS.name())) {
/* 174 */         String state = config.getConfigValue().equals("true") ? "ON" : "OFF";
/* 175 */         this.logger.log(Level.INFO, "000618", "Certificate validation set to " + state, CLASS_NAME);
/*     */       } 
/* 177 */       if (config.getConfigKey().equals(CEBASProperty.DELETE_EXPIRED_CERTS.name()) && config.getConfigValue().equals("true")) {
/* 178 */         this.cleanUpCertificates.cleanUpCertificatesCurrentUser();
/*     */       }
/* 180 */       this.logger.log(Level.INFO, "000089", this.i18n
/* 181 */           .getEnglishMessage("configWithIdUpdated", new String[] { config.getEntityId() }), CLASS_NAME);
/* 182 */       this.logger.exiting(CLASS_NAME, "updateConfiguration");
/* 183 */       return config;
/*     */     } 
/* 185 */     throw new ZenZefiConfigurationException("Configuration not found.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
/*     */   public List<RolePriorityConfiguration> getRolesPriorityConfiguration() {
/* 196 */     String METHOD_NAME = "getRolesPriorityConfiguration";
/* 197 */     this.logger.entering(CLASS_NAME, "getRolesPriorityConfiguration");
/* 198 */     List<RolePriorityConfiguration> rolePriorityConfigList = new ArrayList<>();
/* 199 */     Configuration configuration = getUserRolesConfiguration();
/* 200 */     String[] split = configuration.getConfigValue().split(",");
/* 201 */     for (int i = 0; i < split.length; i++) {
/* 202 */       rolePriorityConfigList.add(new RolePriorityConfiguration(
/* 203 */             Integer.toString(i + 1), split[i]));
/*     */     }
/* 205 */     this.logger.exiting(CLASS_NAME, "getRolesPriorityConfiguration");
/* 206 */     return rolePriorityConfigList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
/*     */   public Configuration getUserRolesConfiguration() {
/* 217 */     String METHOD_NAME = "getUserRolesConfiguration";
/* 218 */     this.logger.entering(CLASS_NAME, "getUserRolesConfiguration");
/* 219 */     this.logger.exiting(CLASS_NAME, "getUserRolesConfiguration");
/* 220 */     return ConfigurationUtil.getConfigurationForUser(this.session
/* 221 */         .getCurrentUser(), CEBASProperty.USER_ROLE, this.logger, this.i18n);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDetailsPanelState(String state) {
/* 232 */     String METHOD_NAME = "setDetailsPanelState";
/* 233 */     this.logger.entering(CLASS_NAME, "setDetailsPanelState");
/* 234 */     User currentUser = this.session.getCurrentUser();
/* 235 */     currentUser.getConfigurations().forEach(configuration -> {
/*     */           if (configuration.getConfigKey().equals(CEBASProperty.CERT_TABLE_DETAIL_VIEW_STATE.name())) {
/*     */             configuration.setConfigValue(state);
/*     */           }
/*     */         });
/*     */     
/* 241 */     this.logger.exiting(CLASS_NAME, "setDetailsPanelState");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDetailsPanelState() {
/* 250 */     String METHOD_NAME = "getDetailsPanelState";
/* 251 */     this.logger.entering(CLASS_NAME, "getDetailsPanelState");
/* 252 */     User currentUser = this.session.getCurrentUser();
/* 253 */     for (Configuration configuration : currentUser.getConfigurations()) {
/* 254 */       if (configuration.getConfigKey().equals(CEBASProperty.CERT_TABLE_DETAIL_VIEW_STATE
/* 255 */           .name())) {
/* 256 */         return configuration.getConfigValue();
/*     */       }
/*     */     } 
/* 259 */     this.logger.exiting(CLASS_NAME, "getDetailsPanelState");
/* 260 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
/*     */   public String getCertificatesColumnVisibility() {
/* 270 */     String METHOD_NAME = "getCertificatesColumnVisibility";
/* 271 */     this.logger.entering(CLASS_NAME, "getCertificatesColumnVisibility");
/*     */     
/* 273 */     String configValue = "";
/* 274 */     User currentUser = this.session.getCurrentUser();
/* 275 */     List<Configuration> configurations = currentUser.getConfigurations();
/* 276 */     for (Configuration configuration : configurations) {
/* 277 */       if (configuration.getConfigKey().equals(CEBASProperty.CERT_TABLE_VISIBLE_COLUMNS.name())) {
/* 278 */         configValue = configuration.getConfigValue();
/* 279 */         if ("default".equals(configValue)) {
/* 280 */           configValue = defaultColumnVisibility();
/*     */         }
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 286 */     this.logger.exiting(CLASS_NAME, "getCertificatesColumnVisibility");
/* 287 */     return configValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCertificatesColumnVisibility(String columnVisibilities) {
/* 297 */     String METHOD_NAME = "setCertificatesColumnVisibility";
/* 298 */     this.logger.entering(CLASS_NAME, "setCertificatesColumnVisibility");
/*     */     
/* 300 */     User currentUser = this.session.getCurrentUser();
/* 301 */     currentUser.getConfigurations().forEach(configuration -> {
/*     */           if (configuration.getConfigKey().equals(CEBASProperty.CERT_TABLE_VISIBLE_COLUMNS.name())) {
/*     */             configuration.setConfigValue(columnVisibilities);
/*     */           }
/*     */         });
/*     */     
/* 307 */     this.logger.exiting(CLASS_NAME, "setCertificatesColumnVisibility");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCertificatesColumnOrder(String columnOrder) {
/* 317 */     String METHOD_NAME = "setCertificatesColumnOrder";
/* 318 */     this.logger.entering(CLASS_NAME, "setCertificatesColumnOrder");
/*     */     
/* 320 */     User currentUser = this.session.getCurrentUser();
/* 321 */     currentUser.getConfigurations().forEach(configuration -> {
/*     */           if (configuration.getConfigKey().equals(CEBASProperty.CERT_TABLE_COLUMN_ORDER.name())) {
/*     */             configuration.setConfigValue(columnOrder);
/*     */           }
/*     */         });
/*     */     
/* 327 */     this.logger.exiting(CLASS_NAME, "setCertificatesColumnOrder");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCertificatesColumnOrder() {
/* 336 */     String METHOD_NAME = "getCertificatesColumnOrder";
/* 337 */     this.logger.entering(CLASS_NAME, "getCertificatesColumnOrder");
/*     */     
/* 339 */     String configValue = "";
/* 340 */     User currentUser = this.session.getCurrentUser();
/* 341 */     for (Configuration configuration : currentUser.getConfigurations()) {
/* 342 */       if (configuration.getConfigKey().equals(CEBASProperty.CERT_TABLE_COLUMN_ORDER.name())) {
/* 343 */         configValue = configuration.getConfigValue();
/* 344 */         if ("default".equals(configValue)) {
/* 345 */           configValue = defaultColumnOrder();
/*     */         }
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 351 */     this.logger.exiting(CLASS_NAME, "getCertificatesColumnOrder");
/* 352 */     return configValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getApplicationVersion() {
/* 361 */     String APPLICATION_VERSION_PROPERTY = "info.build.version";
/* 362 */     String METHOD_NAME = "getApplicationVersion";
/* 363 */     this.logger.entering(CLASS_NAME, "getApplicationVersion");
/* 364 */     this.logger.exiting(CLASS_NAME, "getApplicationVersion");
/* 365 */     return this.configurator.readProperty("info.build.version");
/*     */   }
/*     */   
/*     */   private String defaultColumnVisibility() {
/* 369 */     return "{\"subject\":true,\"pkirole\":true,\"userRole\":true,\"issuer\":true,\"serialNo\":true,\"targetECU\":true,\"targetVIN\":true,\"status\":true,\"validTo\":true,\"validFrom\":false,\"validityStrengthColor\":true,\"uniqueECUID\":false,\"specialECU\":false,\"subjectPublicKey\":false,\"baseCertificateID\":false,\"issuerSerialNumber\":false,\"subjectKeyIdentifier\":false,\"authorityKeyIdentifier\":false,\"services\":false,\"nonce\":false,\"signature\":false,\"targetSubjectKeyIdentifier\":false,\"zkNo\":false,\"ecuPackageTs\":false,\"linkCertTs\":false,\"pkiKnown\":false,\"pkiState\":false}";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String defaultColumnOrder() {
/* 380 */     return "subject,pkirole,userRole,issuer,serialNo,targetECU,targetVIN,status,validTo,validFrom,validityStrengthColor,uniqueECUID,specialECU,subjectPublicKey,baseCertificateID,issuerSerialNumber,subjectKeyIdentifier,authorityKeyIdentifier,services,nonce,signature,targetSubjectKeyIdentifier,zkNo,ecuPackageTs,linkCertTs,pkiKnown,pkiState";
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\ConfigurationsService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */