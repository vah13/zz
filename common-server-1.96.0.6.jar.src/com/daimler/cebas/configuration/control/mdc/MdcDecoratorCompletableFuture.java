/*    */ package com.daimler.cebas.configuration.control.mdc;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionStage;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MdcDecoratorCompletableFuture<T>
/*    */   extends CompletableFuture<T>
/*    */ {
/*    */   public <U> CompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> fn) {
/* 21 */     Function<? super T, ? extends U> f = new MdcFunction<>(fn);
/* 22 */     return super.thenApplyAsync(f);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <U> CompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> fn, Executor executor) {
/* 28 */     Function<? super T, ? extends U> f = new MdcFunction<>(fn);
/* 29 */     return super.thenApplyAsync(f, executor);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> a) {
/* 35 */     Consumer<? super T> f = new MdcConsumer<>(a);
/* 36 */     return super.thenAcceptAsync(f);
/*    */   }
/*    */   
/*    */   public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
/* 40 */     return CompletableFuture.supplyAsync(new MdcSupplier<>(supplier));
/*    */   }
/*    */   
/*    */   public static CompletableFuture<Void> runAsync(Runnable runnable) {
/* 44 */     return CompletableFuture.runAsync(new MdcRunnable(runnable));
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\mdc\MdcDecoratorCompletableFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */