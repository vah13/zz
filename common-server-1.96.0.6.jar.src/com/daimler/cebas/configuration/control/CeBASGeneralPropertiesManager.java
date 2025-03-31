/*     */ package com.daimler.cebas.configuration.control;
/*     */ 
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
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
/*     */ public abstract class CeBASGeneralPropertiesManager
/*     */ {
/*  30 */   private final String CLASS_NAME = "CeBASGeneralPropertiesManager";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResourceLoader resourceLoader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Logger logger;
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
/*     */   private Properties properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CeBASGeneralPropertiesManager(Logger logger, MetadataManager i18n, ResourceLoader resourceLoader, String fileName) {
/*  62 */     this.logger = logger;
/*  63 */     this.i18n = i18n;
/*  64 */     this.resourceLoader = resourceLoader;
/*  65 */     initializeGeneralProperties(fileName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getProperty(String key) {
/*  75 */     return (this.properties != null) ? this.properties.getProperty(key) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initializeGeneralProperties(String propertiesFileName) {
/*  84 */     InputStream fileInputStream = null;
/*     */     
/*     */     try {
/*  87 */       Resource resource = this.resourceLoader.getResource("classpath:" + propertiesFileName);
/*  88 */       if (resource == null || !resource.exists()) {
/*     */         return;
/*     */       }
/*  91 */       fileInputStream = resource.getInputStream();
/*     */       
/*  93 */       this.properties = new Properties();
/*  94 */       this.properties.load(fileInputStream);
/*  95 */     } catch (IOException e) {
/*  96 */       logCannotAccessGeneralResources(e);
/*     */     } finally {
/*  98 */       if (fileInputStream != null) {
/*     */         try {
/* 100 */           fileInputStream.close();
/* 101 */         } catch (IOException e) {
/* 102 */           logCannotAccessGeneralResources(e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<Object, Object>> getGeneralPropertiesEntries() {
/* 114 */     return (this.properties != null) ? this.properties.entrySet() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties getProperties() {
/* 122 */     return this.properties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logCannotAccessGeneralResources(IOException e) {
/* 131 */     this.logger.log(Level.WARNING, "000216X", this.i18n
/* 132 */         .getEnglishMessage("errorReadingZenZefiGeneralProperties", new String[] {
/*     */             
/* 134 */             e.getMessage()
/*     */           }), "CeBASGeneralPropertiesManager");
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\CeBASGeneralPropertiesManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */