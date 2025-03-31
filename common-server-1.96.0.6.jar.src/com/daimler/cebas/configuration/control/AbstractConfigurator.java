/*     */ package com.daimler.cebas.configuration.control;
/*     */ 
/*     */ import com.daimler.cebas.common.control.CEBASProperty;
/*     */ import com.daimler.cebas.common.control.HostUtil;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
/*     */ import com.daimler.cebas.configuration.entity.Configuration;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.system.control.startup.CeBASStartupProperty;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.core.env.Environment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractConfigurator
/*     */ {
/*  38 */   private static final String CLASS_NAME = AbstractConfigurator.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   private static Logger LOG = Logger.getLogger(AbstractConfigurator.class.getSimpleName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Environment environment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private MetadataManager i18n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Configuration copy(Configuration originalConfiguration) {
/*  71 */     String METHOD_NAME = "copy";
/*  72 */     this.logger.entering(CLASS_NAME, "copy");
/*  73 */     if (originalConfiguration == null) {
/*     */       
/*  75 */       ZenZefiConfigurationException e = new ZenZefiConfigurationException(this.i18n.getMessage("configurationMustNotBeNull"), "nullConfigForCopyFound");
/*     */       
/*  77 */       this.logger.logWithTranslation(Level.SEVERE, "000006X", e.getMessageId(), e
/*  78 */           .getClass().getSimpleName());
/*  79 */       throw e;
/*     */     } 
/*  81 */     Configuration copyConfiguration = new Configuration();
/*  82 */     copyConfiguration.setConfigKey(originalConfiguration.getConfigKey());
/*  83 */     copyConfiguration.setConfigValue(originalConfiguration.getConfigValue());
/*  84 */     copyConfiguration.setDescription(originalConfiguration.getDescription());
/*  85 */     this.logger.exiting(CLASS_NAME, "copy");
/*  86 */     return copyConfiguration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSecret() {
/*  93 */     String METHOD_NAME = "getSecret";
/*  94 */     this.logger.entering(CLASS_NAME, "getSecret");
/*  95 */     this.logger.exiting(CLASS_NAME, "getSecret");
/*  96 */     return readProperty(CeBASStartupProperty.SECRET.getProperty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPKCS12PackagePassword() {
/* 103 */     String METHOD_NAME = "getPKCS12PackagePassword";
/* 104 */     this.logger.entering(CLASS_NAME, "getPKCS12PackagePassword");
/* 105 */     this.logger.exiting(CLASS_NAME, "getPKCS12PackagePassword");
/* 106 */     return readProperty(CeBASStartupProperty.PKCS12_PACKAGE_PASSWORD.getProperty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCSRSubject() {
/* 113 */     String METHOD_NAME = "getCSRSubject";
/* 114 */     this.logger.entering(CLASS_NAME, "getCSRSubject");
/* 115 */     this.logger.exiting(CLASS_NAME, "getCSRSubject");
/* 116 */     return readProperty("csr.subject");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLoggingLevel() {
/* 125 */     String METHOD_NAME = "getLoggingLevel";
/* 126 */     this.logger.entering(CLASS_NAME, "getLoggingLevel");
/* 127 */     this.logger.exiting(CLASS_NAME, "getLoggingLevel");
/* 128 */     return readProperty(CEBASProperty.LOGGING_LEVEL.name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> readProperties(List<String> keys) {
/* 137 */     String METHOD_NAME = "readProperties";
/* 138 */     this.logger.entering(CLASS_NAME, "readProperties");
/* 139 */     this.logger.exiting(CLASS_NAME, "readProperties");
/*     */     
/* 141 */     return (Map<String, String>)keys.stream().collect(Collectors.toMap(key -> key, this::readProperty));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Configuration> getDefaultUserProperties() {
/* 148 */     String METHOD_NAME = "getDefaultUserProperties";
/* 149 */     this.logger.entering(CLASS_NAME, METHOD_NAME);
/* 150 */     List<String> keys = getDefaultUserPropertiesList();
/* 151 */     this.logger.exiting(CLASS_NAME, METHOD_NAME);
/* 152 */     return getUserProperties(keys);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<String> getDefaultUserPropertiesList() {
/* 161 */     return new ArrayList<>(Arrays.asList(new String[] { CEBASProperty.VALIDATE_CERTS.name(), CEBASProperty.DELETE_EXPIRED_CERTS
/* 162 */             .name(), CEBASProperty.DELETE_CERTS_SCHEDULE.name(), CEBASProperty.USER_ROLE
/* 163 */             .name() }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Configuration> getRegisteredUserProperties() {
/* 170 */     String METHOD_NAME = "getRegisteredUserProperties";
/* 171 */     this.logger.entering(CLASS_NAME, "getRegisteredUserProperties");
/* 172 */     List<String> keys = Arrays.asList(new String[] { CEBASProperty.VALIDATE_CERTS.name(), CEBASProperty.DELETE_EXPIRED_CERTS
/* 173 */           .name(), CEBASProperty.CERT_SELECTION.name(), CEBASProperty.USER_ROLE
/* 174 */           .name(), CEBASProperty.CERT_TABLE_DETAIL_VIEW_STATE.name(), CEBASProperty.CERT_TABLE_VISIBLE_COLUMNS
/* 175 */           .name(), CEBASProperty.CERT_TABLE_COLUMN_ORDER.name(), CEBASProperty.PAGINATION_MAX_ROWS_PER_PAGE
/* 176 */           .name(), CEBASProperty.AUTO_CERT_UPDATE.name() });
/* 177 */     this.logger.exiting(CLASS_NAME, "getRegisteredUserProperties");
/* 178 */     return getUserProperties(keys);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String readProperty(String key) {
/* 187 */     return this.environment.getProperty(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateDefaultUserConfigurations(List<Configuration> currentDefaultUserProperties) {
/* 196 */     getDefaultUserProperties().forEach(property -> {
/*     */           if (!currentDefaultUserProperties.stream().filter(()).findAny().isPresent()) {
/*     */             currentDefaultUserProperties.add(property);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateRegisteredUserConfigurations(User user) {
/* 211 */     getRegisteredUserProperties().forEach(property -> {
/*     */           if (!user.getConfigurations().stream().filter(()).findAny().isPresent()) {
/*     */             user.getConfigurations().add(property);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Configuration> getUserProperties(List<String> keys) {
/* 225 */     Map<String, String> properties = readProperties(keys);
/* 226 */     return createConfigurationList(properties);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Configuration> createConfigurationList(Map<String, String> properties) {
/* 234 */     return (List<Configuration>)properties.entrySet().stream().map(x -> createConfiguration((String)x.getKey(), (String)x.getValue()))
/* 235 */       .collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Configuration getMachineNameConfiguration() {
/* 244 */     String METHOD_NAME = "getMachineNameConfiguration";
/* 245 */     this.logger.entering(CLASS_NAME, METHOD_NAME);
/* 246 */     this.logger.exiting(CLASS_NAME, METHOD_NAME);
/* 247 */     return createConfiguration("MACHINE_NAME", HostUtil.getMachineName(this.logger, this.i18n));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Configuration createConfiguration(String key, String value) {
/* 258 */     Configuration configuration = new Configuration();
/* 259 */     configuration.setConfigKey(key);
/* 260 */     configuration.setConfigValue(value);
/* 261 */     return configuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Properties loadProperties(String resourceFileName) {
/* 272 */     Properties configuration = new Properties();
/*     */ 
/*     */     
/* 275 */     InputStream inputStream = AbstractConfigurator.class.getClassLoader().getResourceAsStream(resourceFileName);
/*     */     try {
/* 277 */       configuration.load(inputStream);
/* 278 */       if (inputStream != null) {
/* 279 */         inputStream.close();
/*     */       }
/* 281 */     } catch (Exception e) {
/* 282 */       LOG.warning("Failed to load properties from file " + resourceFileName + ": " + e.getMessage());
/*     */     } 
/* 284 */     return configuration;
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
/*     */   public static String loadProperty(String resourceFileName, String key) {
/* 297 */     Properties configuration = loadProperties(resourceFileName);
/* 298 */     return configuration.getProperty(key);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\AbstractConfigurator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */