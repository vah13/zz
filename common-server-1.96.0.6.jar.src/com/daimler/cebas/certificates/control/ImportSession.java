/*    */ package com.daimler.cebas.certificates.control;
/*    */ 
/*    */ import com.daimler.cebas.certificates.control.exceptions.StoreModificationNotAllowed;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASSession;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import java.util.concurrent.locks.ReentrantLock;
/*    */ import java.util.logging.Level;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASSession
/*    */ public class ImportSession
/*    */ {
/*    */   private Logger logger;
/* 32 */   private ReentrantLock lock = new ReentrantLock();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private MetadataManager i18n;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   public ImportSession(Logger logger, MetadataManager i18n) {
/* 49 */     this.logger = logger;
/* 50 */     this.i18n = i18n;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void run() {
/* 57 */     if (isRunning()) {
/* 58 */       this.logger.log(Level.WARNING, "000367", this.i18n
/* 59 */           .getEnglishMessage("operationNotAllowedImportIsRunning"), ImportSession.class
/* 60 */           .getSimpleName());
/* 61 */       throw new StoreModificationNotAllowed(this.i18n.getMessage("operationNotAllowedImportIsRunning"));
/*    */     } 
/* 63 */     this.logger.log(Level.INFO, "000279", "Import session started", ImportSession.class
/* 64 */         .getSimpleName());
/* 65 */     this.lock.lock();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setNotRunning() {
/* 72 */     if (this.lock.getHoldCount() != 0) {
/* 73 */       this.logger.log(Level.INFO, "000279", "Import session stopped", ImportSession.class
/* 74 */           .getSimpleName());
/* 75 */       this.lock.unlock();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isRunning() {
/* 85 */     return this.lock.isLocked();
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\ImportSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */