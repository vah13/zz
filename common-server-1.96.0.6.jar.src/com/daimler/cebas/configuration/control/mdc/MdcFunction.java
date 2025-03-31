/*    */ package com.daimler.cebas.configuration.control.mdc;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import org.slf4j.MDC;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MdcFunction<T, R>
/*    */   implements Function<T, R>
/*    */ {
/*    */   private final Function<T, R> delegate;
/*    */   private final Map<String, String> mdc;
/*    */   
/*    */   public MdcFunction(Function<T, R> delegate) {
/* 18 */     this.delegate = delegate;
/* 19 */     this.mdc = MDC.getCopyOfContextMap();
/*    */   }
/*    */ 
/*    */   
/*    */   public R apply(T t) {
/* 24 */     if (this.mdc != null && !this.mdc.isEmpty()) {
/* 25 */       MDC.setContextMap(this.mdc);
/*    */     }
/* 27 */     return this.delegate.apply(t);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\mdc\MdcFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */