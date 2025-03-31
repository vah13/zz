/*     */ package com.daimler.cebas.system.control.startup.readers;
/*     */ 
/*     */ import com.daimler.cebas.system.control.exceptions.ConfigurationCheckException;
/*     */ import com.daimler.cebas.system.control.startup.CeBASStartupProperty;
/*     */ import com.daimler.cebas.system.control.startup.ImageConversion;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DynamicSecretReader
/*     */   extends SecretReader
/*     */ {
/*  21 */   protected static final Log LOG = LogFactory.getLog(DynamicSecretReader.class);
/*     */ 
/*     */   
/*     */   public static final int MAXIMUM_SECRET_BYTES = 1500;
/*     */   
/*     */   protected static final String PROPERTY_DATA_PATH = "program.data.path";
/*     */   
/*     */   protected static final String PROPERTY_DB_NAME = "datasource.name";
/*     */   
/*     */   protected static final String DYNAMIC_FILE_EXTENSION = ".cache";
/*     */   
/*     */   protected static final String DB_EXTENSION = ".mv.db";
/*     */   
/*     */   private Properties properties;
/*     */   
/*     */   private String staticSecret;
/*     */ 
/*     */   
/*     */   protected void prepare(Properties properties, SecretMap secret) {
/*  40 */     this.properties = properties;
/*  41 */     this.staticSecret = secret.get(CeBASStartupProperty.SECRET);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SecretMap getSecret() {
/*  47 */     File file = getSecretFileLocation();
/*     */     
/*  49 */     if (!fileExists(file)) {
/*     */       byte[] fileData;
/*  51 */       if (existingInstallation()) {
/*  52 */         fileData = createFileDataFromStaticSecret();
/*     */       } else {
/*  54 */         fileData = createFileDataFromDynamicSecret();
/*     */       } 
/*     */       
/*  57 */       createFile(file, fileData);
/*     */     } 
/*     */     
/*  60 */     return getSecret(file);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean overwriteHeader() {
/*  65 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int maximumSecretSize() {
/*  70 */     return 1500;
/*     */   }
/*     */   
/*     */   private File getSecretFileLocation() {
/*  74 */     String dataPath = this.properties.getProperty("program.data.path");
/*  75 */     String dbName = this.properties.getProperty("datasource.name");
/*     */     
/*  77 */     if (dataPath == null || dbName == null) {
/*  78 */       throw new ConfigurationCheckException("The configuration is not valid. At least one of the following required properties is not set: program.data.path / datasource.name");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  83 */     return new File(dataPath, dbName + ".cache");
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean existingInstallation() {
/*  88 */     String dataPath = this.properties.getProperty("program.data.path");
/*  89 */     String dbName = this.properties.getProperty("datasource.name");
/*     */     
/*  91 */     if (dataPath == null) {
/*  92 */       return false;
/*     */     }
/*     */     
/*  95 */     File folder = new File(dataPath);
/*  96 */     if (!folder.exists()) {
/*  97 */       return false;
/*     */     }
/*  99 */     File[] files = folder.listFiles();
/* 100 */     if (files == null) {
/* 101 */       return false;
/*     */     }
/* 103 */     for (File file : files) {
/* 104 */       if (file.isFile() && (dbName + ".mv.db").equals(file.getName())) {
/* 105 */         return true;
/*     */       }
/*     */     } 
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private byte[] createFileDataFromDynamicSecret() {
/* 113 */     BufferedImage randomImage = ImageConversion.createRandomImage();
/*     */     
/* 115 */     String randomSecretString = ReaderUtils.createNewRandomSecretString();
/* 116 */     SecretMap s = new SecretMap();
/* 117 */     s.put(CeBASStartupProperty.SECRET, randomSecretString);
/*     */     
/* 119 */     return hideSecret(s, randomImage);
/*     */   }
/*     */ 
/*     */   
/*     */   private byte[] createFileDataFromStaticSecret() {
/* 124 */     BufferedImage randomImage = ImageConversion.createRandomImage();
/*     */     
/* 126 */     SecretMap s = new SecretMap();
/* 127 */     s.put(CeBASStartupProperty.SECRET, this.staticSecret);
/*     */     
/* 129 */     return hideSecret(s, randomImage);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\startup\readers\DynamicSecretReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */