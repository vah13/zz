/*     */ package com.daimler.cebas.certificates.control.validation;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*     */ import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Collections;
/*     */ import java.util.Objects;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BaseStoreUtil
/*     */ {
/*  28 */   private static final String CLASS_NAME = StoreValidator.class
/*  29 */     .getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  35 */   private static final Logger LOG = Logger.getLogger(CertificateParser.class.getName());
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
/*     */   private static Path baseStorePath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static FileSystem fileSystem;
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
/*     */   public static DirectoryStream<Path> getDirectoryStream(Path dir, DirectoryStream.Filter<Path> filter, Logger logger) throws IOException {
/*  68 */     String METHOD_NAME = "getDirectoryStream";
/*  69 */     logger.entering(CLASS_NAME, "getDirectoryStream");
/*  70 */     logger.exiting(CLASS_NAME, "getDirectoryStream");
/*  71 */     return Files.newDirectoryStream(dir, filter);
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
/*     */   public static Path getBaseStoreTopPath(Class classs, Logger logger) {
/*  85 */     String METHOD_NAME = "getBaseStoreTopPath";
/*  86 */     logger.entering(CLASS_NAME, "getBaseStoreTopPath");
/*     */     
/*     */     try {
/*  89 */       URL resource = classs.getClassLoader().getResource("base_certificate_store");
/*  90 */       Objects.requireNonNull(resource, "Resource URL cannot be null");
/*  91 */       URI uri = resource.toURI();
/*     */       
/*  93 */       String scheme = uri.getScheme();
/*  94 */       if ("file".equals(scheme)) {
/*  95 */         return Paths.get(uri);
/*     */       }
/*  97 */       if (!"jar".equals(scheme)) {
/*  98 */         throw new IllegalArgumentException("Cannot convert to Path: " + uri);
/*     */       }
/*     */       
/* 101 */       String s = uri.toString();
/* 102 */       int separator = s.indexOf("!/");
/* 103 */       URI fileURI = URI.create(s.substring(0, separator));
/* 104 */       fileSystem = FileSystems.newFileSystem(fileURI, 
/* 105 */           Collections.emptyMap());
/*     */       
/* 107 */       baseStorePath = fileSystem.getPath("/BOOT-INF/classes/base_certificate_store", new String[0]);
/* 108 */     } catch (IOException|java.net.URISyntaxException e) {
/* 109 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 110 */       throw new CEBASCertificateException("Cannot create path from jar!");
/*     */     } 
/*     */     
/* 113 */     logger.exiting(CLASS_NAME, "getBaseStoreTopPath");
/* 114 */     return baseStorePath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeFileSystem() {
/* 121 */     if (fileSystem != null) {
/*     */       try {
/* 123 */         fileSystem.close();
/* 124 */         fileSystem = null;
/* 125 */         baseStorePath = null;
/* 126 */       } catch (IOException e) {
/* 127 */         LOG.log(Level.FINEST, e.getMessage(), e);
/* 128 */         throw new CEBASCertificateException("Cannot close base store file system.");
/*     */       } 
/*     */     }
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
/*     */   public static boolean getRootDirectoriesFilter(Path path) {
/* 142 */     return Files.isDirectory(path, new java.nio.file.LinkOption[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getRootFilesFilter(Path path) {
/* 153 */     return !Files.isDirectory(path, new java.nio.file.LinkOption[0]);
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
/*     */   public static String getRootCertificatePathId(Path rootPath) {
/* 165 */     String[] split = rootPath.getFileName().toString().split("_");
/* 166 */     String temp = split[split.length - 1];
/* 167 */     return temp.split("\\.")[0];
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
/*     */   public static String getBackendCertificateRootCertificatePathId(Path backendPath) {
/* 179 */     String[] split = backendPath.getFileName().toString().split("_");
/* 180 */     return split[split.length - 2];
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\BaseStoreUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */