/*    */ package com.daimler.cebas.configuration.control.mdc;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import org.slf4j.MDC;
/*    */ import org.springframework.web.context.request.RequestAttributes;
/*    */ import org.springframework.web.context.request.RequestContextHolder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MdcSupplier<T>
/*    */   implements Supplier<T>
/*    */ {
/*    */   private final Supplier<T> delegate;
/*    */   private final Map<String, String> mdc;
/*    */   private final RequestAttributes requestAttributes;
/*    */   
/*    */   public MdcSupplier(Supplier<T> delegate) {
/* 21 */     this.delegate = delegate;
/* 22 */     this.mdc = MDC.getCopyOfContextMap();
/* 23 */     this.requestAttributes = RequestContextHolder.getRequestAttributes();
/*    */   }
/*    */ 
/*    */   
/*    */   public T get() {
/* 28 */     if (this.mdc != null && !this.mdc.isEmpty()) {
/* 29 */       MDC.setContextMap(this.mdc);
/*    */     }
/* 31 */     RequestContextHolder.setRequestAttributes(this.requestAttributes);
/* 32 */     return this.delegate.get();
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\mdc\MdcSupplier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */