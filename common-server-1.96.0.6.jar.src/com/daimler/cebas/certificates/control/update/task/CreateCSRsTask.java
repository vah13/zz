/*     */ package com.daimler.cebas.certificates.control.update.task;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.CertificateSignRequestEngine;
/*     */ import com.daimler.cebas.certificates.control.CertificateToolsProvider;
/*     */ import com.daimler.cebas.certificates.control.DeleteCertificatesEngine;
/*     */ import com.daimler.cebas.certificates.control.SearchEngine;
/*     */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateTask;
/*     */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.UserRole;
/*     */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASControl
/*     */ public abstract class CreateCSRsTask<T extends PublicKeyInfrastructureEsi>
/*     */   extends UpdateTask<T>
/*     */ {
/*     */   protected static final String EMPTY = "";
/*     */   protected static final String SPACE = " ";
/*  33 */   private static final String CLASS_NAME = CreateCSRsTask.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SearchEngine searchEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CertificateSignRequestEngine certificateSignRequestEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DeleteCertificatesEngine deleteCertificatesEngine;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Session session;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public CreateCSRsTask(CertificateToolsProvider toolsProvider, T publicKeyInfrastructureEsi, SearchEngine searchEngine, Session session, DefaultUpdateSession updateSession, Logger logger) {
/*  59 */     super((PublicKeyInfrastructureEsi)publicKeyInfrastructureEsi, toolsProvider.getImporter(), updateSession, logger, toolsProvider.getI18n());
/*  60 */     this.searchEngine = searchEngine;
/*  61 */     this.certificateSignRequestEngine = toolsProvider.getCertificateSignRequestEngine();
/*  62 */     this.deleteCertificatesEngine = toolsProvider.getDeleteCertificatesEngine();
/*  63 */     this.session = session;
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
/*     */   protected void deleteNonIdenticalCertificates(List<Certificate> certificatesUnderParent, Certificate createdCSR, UpdateType updateType) {
/*  77 */     createdCSR.initializeTransients();
/*     */ 
/*     */ 
/*     */     
/*  81 */     List<String> certificatesIdsNotTheSame = (List<String>)certificatesUnderParent.stream().filter(cert -> !cert.identicalWithDifference(createdCSR)).map(Certificate::getEntityId).collect(Collectors.toList());
/*  82 */     if (!certificatesIdsNotTheSame.isEmpty()) {
/*  83 */       this.logger.log(Level.INFO, "000259", this.i18n
/*  84 */           .getEnglishMessage("updateStartDeletingCertsWhichAreNotReplaced", new String[] {
/*  85 */               updateType.name()
/*     */             }), CLASS_NAME);
/*  87 */       this.deleteCertificatesEngine.deleteCertificatesDifferentTransaction(certificatesIdsNotTheSame);
/*  88 */       this.logger.log(Level.INFO, "000260", this.i18n
/*  89 */           .getEnglishMessage("updateEndDeletingCertsWhichAreNotReplaced", new String[] {
/*  90 */               updateType.name()
/*     */             }), CLASS_NAME);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUserRoleFromPermission(String byteValue) {
/* 102 */     if (byteValue != null) {
/* 103 */       UserRole userRole = UserRole.getUserRoleFromByte(Byte.decode(byteValue).byteValue());
/* 104 */       return userRole.getText();
/*     */     } 
/* 106 */     return "";
/*     */   }
/*     */   
/*     */   protected abstract String getProdQualifier();
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\contro\\update\task\CreateCSRsTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */