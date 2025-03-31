/*    */ package com.daimler.cebas.configuration.control.mdc;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.slf4j.MDC;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MdcRunnable
/*    */   implements Runnable
/*    */ {
/*    */   private final Runnable delegate;
/*    */   private final Map<String, String> mdc;
/*    */   
/*    */   public MdcRunnable(Runnable delegate) {
/* 17 */     this.delegate = delegate;
/* 18 */     this.mdc = MDC.getCopyOfContextMap();
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 23 */     if (this.mdc != null && !this.mdc.isEmpty()) {
/* 24 */       MDC.setContextMap(this.mdc);
/*    */     }
/* 26 */     this.delegate.run();
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\mdc\MdcRunnable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */