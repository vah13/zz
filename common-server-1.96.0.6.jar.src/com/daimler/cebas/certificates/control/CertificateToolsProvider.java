/*     */ package com.daimler.cebas.certificates.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory;
/*     */ import com.daimler.cebas.certificates.control.validation.SystemIntegrityChecker;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
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
/*     */ @CEBASControl
/*     */ public class CertificateToolsProvider
/*     */ {
/*     */   private ImportCertificatesEngine importer;
/*     */   private SearchEngine searchEngine;
/*     */   private SystemIntegrityChecker systemIntegrityCheck;
/*     */   private AbstractCertificateFactory factory;
/*     */   private MetadataManager i18n;
/*     */   private DeleteCertificatesEngine deleteCertificatesEngine;
/*     */   private CertificateSignRequestEngine certificateSignRequestEngine;
/*     */   
/*     */   @Autowired
/*     */   public CertificateToolsProvider(ImportCertificatesEngine importer, SearchEngine searchEngine, SystemIntegrityChecker systemIntegrityCheck, AbstractCertificateFactory factory, MetadataManager i18n, DeleteCertificatesEngine deleteCertificatesEngine, CertificateSignRequestEngine certificateSignRequestEngine) {
/*  78 */     this.importer = importer;
/*  79 */     this.systemIntegrityCheck = systemIntegrityCheck;
/*  80 */     this.factory = factory;
/*  81 */     this.i18n = i18n;
/*  82 */     this.deleteCertificatesEngine = deleteCertificatesEngine;
/*  83 */     this.certificateSignRequestEngine = certificateSignRequestEngine;
/*  84 */     this.searchEngine = searchEngine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImportCertificatesEngine getImporter() {
/*  91 */     return this.importer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SearchEngine getSearchEngine() {
/*  98 */     return this.searchEngine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SystemIntegrityChecker getSystemIntegrityCheck() {
/* 105 */     return this.systemIntegrityCheck;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractCertificateFactory getFactory() {
/* 112 */     return this.factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MetadataManager getI18n() {
/* 119 */     return this.i18n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeleteCertificatesEngine getDeleteCertificatesEngine() {
/* 128 */     return this.deleteCertificatesEngine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CertificateSignRequestEngine getCertificateSignRequestEngine() {
/* 137 */     return this.certificateSignRequestEngine;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\CertificateToolsProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */