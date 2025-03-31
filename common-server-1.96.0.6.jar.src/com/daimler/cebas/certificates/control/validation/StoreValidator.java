/*     */ package com.daimler.cebas.certificates.control.validation;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public final class StoreValidator
/*     */ {
/*  29 */   private static final String CLASS_NAME = StoreValidator.class
/*  30 */     .getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  36 */   private static final Logger LOG = Logger.getLogger(StoreValidator.class.getSimpleName());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void validateBaseStore(Class classs, MetadataManager i18n, Logger logger) {
/*  58 */     String METHOD_NAME = "validateBaseStore";
/*  59 */     logger.entering(CLASS_NAME, METHOD_NAME);
/*  60 */     Path topDir = BaseStoreUtil.getBaseStoreTopPath(classs, logger);
/*  61 */     DirectoryStream<Path> topDirectories = null;
/*     */     try {
/*  63 */       topDirectories = BaseStoreUtil.getDirectoryStream(topDir, BaseStoreUtil::getRootDirectoriesFilter, logger);
/*     */       
/*  65 */       List<Path> rootCertificatesPaths = new ArrayList<>();
/*  66 */       List<Path> rootCertificatesFolders = new ArrayList<>();
/*  67 */       topDirectories.forEach(pathParent -> {
/*     */             rootCertificatesFolders.add(pathParent);
/*     */             
/*     */             rootCertificatesPaths.clear();
/*     */             
/*     */             extractDirectoryStream(pathParent, rootCertificatesPaths, BaseStoreUtil::getRootFilesFilter, logger);
/*     */             
/*     */             if (rootCertificatesPaths.isEmpty()) {
/*     */               logger.log(Level.INFO, "000588X", "foundRootFolderEmpty", CLASS_NAME);
/*     */               throw new CEBASCertificateException(i18n.getMessage("foundRootFolderEmpty"));
/*     */             } 
/*     */           });
/*  79 */       if (rootCertificatesFolders.isEmpty()) {
/*  80 */         logger.log(Level.INFO, "000589X", "noRootFoldersFound", CLASS_NAME);
/*     */         
/*  82 */         throw new CEBASCertificateException(i18n
/*  83 */             .getMessage("noRootFoldersFound"));
/*     */       } 
/*  85 */       rootCertificatesPaths.clear();
/*  86 */       rootCertificatesFolders.clear();
/*  87 */     } catch (IOException e) {
/*  88 */       LOG.log(Level.FINEST, e.getMessage(), e);
/*  89 */       logger.log(Level.WARNING, "000170X", "failedToGetDirectoryStream", CLASS_NAME);
/*     */     } finally {
/*     */       
/*  92 */       if (topDirectories != null) {
/*     */         try {
/*  94 */           topDirectories.close();
/*  95 */         } catch (IOException e) {
/*  96 */           LOG.log(Level.FINEST, e.getMessage(), e);
/*  97 */           logger.log(Level.WARNING, "000168X", i18n
/*  98 */               .getEnglishMessage("failedToGetDirectoryStream", new String[] {
/*     */                   
/* 100 */                   topDir.toString()
/*     */                 }), CLASS_NAME);
/*     */         } 
/*     */       }
/* 104 */       BaseStoreUtil.closeFileSystem();
/*     */     } 
/* 106 */     logger.exiting(CLASS_NAME, METHOD_NAME);
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
/*     */   private static void extractDirectoryStream(Path path, List<Path> pathList, Predicate<Path> predicate, Logger logger) {
/* 119 */     DirectoryStream<Path> directoryStream = null;
/*     */     try {
/* 121 */       directoryStream = BaseStoreUtil.getDirectoryStream(path, predicate::test, logger);
/*     */       
/* 123 */       directoryStream.forEach(pathList::add);
/* 124 */     } catch (IOException e) {
/* 125 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 126 */       logger.log(Level.WARNING, "000166X", "failedToProcessDirectoryStream", CLASS_NAME);
/*     */     } finally {
/*     */       
/*     */       try {
/* 130 */         if (directoryStream != null) {
/* 131 */           directoryStream.close();
/*     */         }
/* 133 */       } catch (IOException e) {
/* 134 */         LOG.log(Level.FINEST, e.getMessage(), e);
/* 135 */         logger.log(Level.WARNING, "000165X", "cannotCloseDirectoryStreamAfterProcessingValidation", CLASS_NAME);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\validation\StoreValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */