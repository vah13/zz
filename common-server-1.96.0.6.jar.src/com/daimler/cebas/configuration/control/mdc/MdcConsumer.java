/*    */ package com.daimler.cebas.configuration.control.mdc;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.function.Consumer;
/*    */ import org.slf4j.MDC;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MdcConsumer<T>
/*    */   implements Consumer<T>
/*    */ {
/*    */   private final Consumer<T> delegate;
/*    */   private final Map<String, String> mdc;
/*    */   
/*    */   public MdcConsumer(Consumer<T> delegate) {
/* 18 */     this.delegate = delegate;
/* 19 */     this.mdc = MDC.getCopyOfContextMap();
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept(T t) {
/* 24 */     if (this.mdc != null && !this.mdc.isEmpty()) {
/* 25 */       MDC.setContextMap(this.mdc);
/*    */     }
/* 27 */     this.delegate.accept(t);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\mdc\MdcConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */