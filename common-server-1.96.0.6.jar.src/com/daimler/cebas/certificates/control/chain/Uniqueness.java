/*     */ package com.daimler.cebas.certificates.control.chain;
/*     */ 
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
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
/*     */ public final class Uniqueness
/*     */ {
/*  22 */   private static final String CLASS_NAME = Uniqueness.class.getSimpleName();
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
/*     */   
/*     */   public static boolean hasSignatureDifferentButSamePublicKey(List<Certificate> store, Certificate certificate, Logger logger) {
/*  45 */     String METHOD_NAME = "hasSignatureDifferentButSamePublicKey";
/*  46 */     logger.entering(CLASS_NAME, "hasSignatureDifferentButSamePublicKey");
/*  47 */     logger.exiting(CLASS_NAME, "hasSignatureDifferentButSamePublicKey");
/*  48 */     return store.stream().anyMatch(cert -> 
/*  49 */         (!cert.getSignature().equals(certificate.getSignature()) && cert.getSubjectPublicKey().equals(certificate.getSubjectPublicKey())));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Certificate> List<T> findIdenticalWithDifferences(List<T> store, Certificate certificate, Logger logger) {
/*  67 */     String METHOD_NAME = "findIdenticalWithDifferences";
/*  68 */     logger.entering(CLASS_NAME, "findIdenticalWithDifferences");
/*  69 */     certificate.initializeTransients();
/*  70 */     logger.exiting(CLASS_NAME, "findIdenticalWithDifferences");
/*  71 */     return (List<T>)store.stream()
/*  72 */       .filter(cert -> cert.identicalWithDifference(certificate))
/*  73 */       .collect(Collectors.toList());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Certificate> findIdenticalWithDifferences(List<Certificate> store, Certificate certificate, Set<String> handledCerts, Logger logger) {
/*  91 */     String METHOD_NAME = "findIdenticalWithDifferences";
/*  92 */     logger.entering(CLASS_NAME, "findIdenticalWithDifferences");
/*  93 */     certificate.initializeTransients();
/*  94 */     List<Certificate> resultList = new ArrayList<>();
/*  95 */     for (Certificate certInStore : store) {
/*  96 */       boolean isIdentical = certInStore.identicalWithDifference(certificate);
/*  97 */       if (isIdentical) {
/*  98 */         handledCerts.add(certInStore.getEntityId());
/*  99 */         resultList.add(certInStore);
/*     */       } 
/*     */     } 
/* 102 */     logger.exiting(CLASS_NAME, "findIdenticalWithDifferences");
/* 103 */     return resultList;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\chain\Uniqueness.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */