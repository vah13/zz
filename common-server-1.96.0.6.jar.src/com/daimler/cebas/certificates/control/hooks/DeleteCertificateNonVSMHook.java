/*    */ package com.daimler.cebas.certificates.control.hooks;
/*    */ 
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.logging.Level;
/*    */ 
/*    */ 
/*    */ public class DeleteCertificateNonVSMHook
/*    */   implements ICertificateHooks
/*    */ {
/*    */   private Logger logger;
/*    */   
/*    */   public DeleteCertificateNonVSMHook(Logger logger) {
/* 16 */     this.logger = logger;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Consumer<Certificate>> possibleHook() {
/* 21 */     return Optional.of(createHook());
/*    */   }
/*    */   
/*    */   public Consumer<Certificate> createHook() {
/* 25 */     return this::exec;
/*    */   }
/*    */ 
/*    */   
/*    */   public void exec(Certificate cert) {
/* 30 */     Certificate backend = cert.getParent();
/* 31 */     if (cert.isNonVsmEcu() && backend != null) {
/* 32 */       this.logger.log(Level.FINE, "000623", "Backend with SKI " + backend.getSubjectKeyIdentifier() + " had the ECU Package Ts: " + backend
/* 33 */           .getEcuPackageTs() + " before reset", getClass().getSimpleName());
/* 34 */       backend.setEcuPackageTs(null);
/* 35 */       this.logger.log(Level.INFO, "000623", "Deletion of NON-VSM ECU certificate triggered ECU Package timestamp reset for backend with SKI " + backend
/* 36 */           .getSubjectKeyIdentifier(), 
/* 37 */           getClass().getSimpleName());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\hooks\DeleteCertificateNonVSMHook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */