/*    */ package com.daimler.cebas.certificates.control.hooks;
/*    */ 
/*    */ import com.daimler.cebas.certificates.entity.Certificate;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ public class NoHook
/*    */   implements ICertificateHooks
/*    */ {
/*    */   public Optional<Consumer<Certificate>> possibleHook() {
/* 11 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   public void exec(Certificate cert) {}
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\hooks\NoHook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */