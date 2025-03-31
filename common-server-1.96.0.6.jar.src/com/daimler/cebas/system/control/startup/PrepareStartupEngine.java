/*     */ package com.daimler.cebas.system.control.startup;
/*     */ 
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.StartupInfoBuilder;
/*     */ import com.daimler.cebas.configuration.control.PkiUrlProperty;
/*     */ import com.daimler.cebas.system.control.exceptions.ConfigurationCheckException;
/*     */ import com.daimler.cebas.system.control.startup.readers.SecretFactory;
/*     */ import com.daimler.cebas.system.control.startup.readers.SecretMap;
/*     */ import com.daimler.cebas.system.control.startup.readers.SecretReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.boot.ansi.AnsiOutput;
/*     */ import org.springframework.context.ApplicationContextInitializer;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.MapPropertySource;
/*     */ import org.springframework.core.env.PropertiesPropertySource;
/*     */ import org.springframework.core.env.PropertySource;
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
/*     */ public class PrepareStartupEngine
/*     */   implements ApplicationContextInitializer<ConfigurableApplicationContext>
/*     */ {
/*  42 */   protected static final Log LOG = LogFactory.getLog(PrepareStartupEngine.class);
/*     */ 
/*     */   
/*     */   private Class<?> appClazz;
/*     */   
/*     */   private SecretMap secret;
/*     */   
/*     */   private Set<CeBASStartupProperty> startupProperties;
/*     */   
/*     */   private Properties fileProperties;
/*     */   
/*     */   private boolean isProdEnvironment;
/*     */ 
/*     */   
/*     */   public PrepareStartupEngine(Class<?> appClazz, String propertyFile, String generalPropertyFile, Set<CeBASStartupProperty> startupProperties, SecretReader... readers) {
/*  57 */     this.appClazz = appClazz;
/*  58 */     this.startupProperties = startupProperties;
/*  59 */     this.fileProperties = getProperties(propertyFile, generalPropertyFile);
/*     */     
/*  61 */     for (CeBASStartupProperty p : startupProperties) {
/*  62 */       if (this.fileProperties.containsKey(p.name()) || this.fileProperties.containsKey(p.getProperty())) {
/*  63 */         throw new ConfigurationCheckException("The property " + p.name() + "/" + p.getProperty() + " must not be overriden in any property files.");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  68 */     this.isProdEnvironment = isProdEnvironment(this.fileProperties);
/*     */     
/*  70 */     SecretFactory f = SecretFactory.create(this.fileProperties);
/*  71 */     for (SecretReader reader : readers) {
/*  72 */       f.run(reader);
/*     */     }
/*     */     
/*  75 */     this.secret = f.finish();
/*     */     
/*  77 */     for (CeBASStartupProperty p : startupProperties) {
/*  78 */       if (p.isLog()) {
/*  79 */         LOG.info("The property " + p.getProperty() + " is defined as: " + this.secret.get(p));
/*     */       }
/*  81 */       if (p.isRequired() && !this.secret.contains(p)) {
/*  82 */         throw new CEBASException("The property " + p.getProperty() + " is required but was not provided within the secret context.");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  87 */     setAnsiColor();
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isProdEnvironment(Properties properties) {
/*  92 */     String pkiEnvironment = properties.getProperty(PkiUrlProperty.PKI_ENVIRONMENT.getProperty());
/*  93 */     if ("PROD".equals(pkiEnvironment)) {
/*  94 */       return true;
/*     */     }
/*     */     
/*  97 */     if (StringUtils.isEmpty(pkiEnvironment) || "TEST".equals(pkiEnvironment)) {
/*  98 */       return false;
/*     */     }
/* 100 */     throw new CEBASException("The property " + PkiUrlProperty.PKI_ENVIRONMENT.getProperty() + " must either be set to " + "PROD" + " or to " + "TEST" + " but was " + pkiEnvironment + " instead.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setAnsiColor() {
/* 106 */     boolean isWindows = System.getProperty("os.name").toLowerCase(Locale.ENGLISH).startsWith("win");
/* 107 */     if (isWindows) {
/* 108 */       String shell = System.getenv().get("SHELL");
/* 109 */       if (shell != null && shell.contains("bash")) {
/* 110 */         AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
/*     */         return;
/*     */       } 
/* 113 */       String conEmu = System.getenv().get("ConEmuANSI");
/* 114 */       if (conEmu != null && conEmu.contains("ON")) {
/* 115 */         AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize(ConfigurableApplicationContext applicationContext) {
/* 125 */     LOG.info((new StartupInfoBuilder(this.appClazz)).getConfigurationsInfo());
/*     */     
/* 127 */     ConfigurableEnvironment environment = applicationContext.getEnvironment();
/* 128 */     setSslProperty(environment);
/*     */     
/* 130 */     Map<String, Object> props = new LinkedHashMap<>();
/* 131 */     for (CeBASStartupProperty p : this.startupProperties) {
/* 132 */       if (environment.containsProperty(p.getProperty()) || environment.containsProperty(p.name())) {
/* 133 */         throw new ConfigurationCheckException("The property " + p.name() + "/" + p.getProperty() + " must not be overriden with environment properties.");
/*     */       }
/*     */ 
/*     */       
/* 137 */       if (this.secret.contains(p)) {
/* 138 */         if (this.isProdEnvironment && p.isProdEnvironmentRelevant()) {
/* 139 */           props.put(p.getProperty(), this.secret.get(p)); continue;
/* 140 */         }  if (!this.isProdEnvironment && p.isTestEnvironmentRelevant()) {
/* 141 */           props.put(p.getProperty(), this.secret.get(p));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     setTestProdProperty(props, "spring.security.oauth2.client.registration.gas.provider", this.isProdEnvironment);
/* 147 */     setTestProdProperty(props, "spring.security.oauth2.client.registration.gas.redirect-uri", this.isProdEnvironment);
/* 148 */     setTestProdProperty(props, "security.revoke-token-uri", this.isProdEnvironment);
/* 149 */     setTestProdProperty(props, PkiUrlProperty.PKI_LOGOUT_URL.getProperty(), this.isProdEnvironment);
/* 150 */     setTestProdProperty(props, PkiUrlProperty.PKI_BASE_URL.getProperty(), this.isProdEnvironment);
/*     */     
/* 152 */     MapPropertySource mapPropertySource = new MapPropertySource("startupProperties", props);
/* 153 */     environment.getPropertySources().addFirst((PropertySource)mapPropertySource);
/*     */   }
/*     */   
/*     */   private void setTestProdProperty(Map<String, Object> props, String property, boolean prod) {
/* 157 */     String env = prod ? "prod." : "test.";
/* 158 */     props.put(property, this.fileProperties.get(env + property));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setSslProperty(ConfigurableEnvironment environment) {
/* 168 */     String version = System.getProperty("java.version");
/* 169 */     if (version.startsWith("1.")) {
/* 170 */       version = version.substring(2, 3);
/*     */     } else {
/* 172 */       int dot = version.indexOf(".");
/* 173 */       if (dot != -1) {
/* 174 */         version = version.substring(0, dot);
/*     */       }
/*     */     } 
/*     */     
/* 178 */     if (version.equals("11") || version.equals("12") || version.equals("13")) {
/* 179 */       Properties props = new Properties();
/* 180 */       props.put("server.ssl.enabled-protocols", "SSLv3, TLSv1, TLSv1.1, TLSv1.2");
/* 181 */       environment.getPropertySources().addFirst((PropertySource)new PropertiesPropertySource("ssl_property", props));
/* 182 */       LOG.info("Setting SSL enabled protocols due to incompatible java version: " + version);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Properties getProperties(String propertyFile, String generalPropertyFile) {
/* 188 */     Properties p = new Properties();
/* 189 */     try (InputStream stream = getClass().getResourceAsStream(propertyFile)) {
/* 190 */       p.load(stream);
/* 191 */     } catch (IOException e) {
/* 192 */       LOG.warn(e);
/* 193 */       throw new ConfigurationCheckException("It was not possible to read data from the static property file: " + e
/* 194 */           .getMessage());
/*     */     } 
/*     */     
/* 197 */     try (InputStream stream = getClass().getResourceAsStream(generalPropertyFile)) {
/*     */       
/* 199 */       if (stream != null) {
/* 200 */         p.load(stream);
/*     */       }
/*     */     }
/* 203 */     catch (IOException e) {
/* 204 */       LOG.warn(e);
/* 205 */       throw new ConfigurationCheckException("It was not possible to read data from the general property file: " + e
/* 206 */           .getMessage());
/*     */     } 
/*     */     
/* 209 */     return p;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\startup\PrepareStartupEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */