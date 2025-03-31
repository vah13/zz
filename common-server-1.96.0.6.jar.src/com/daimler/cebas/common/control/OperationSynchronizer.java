/*     */ package com.daimler.cebas.common.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.StoreModificationNotAllowed;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.function.Supplier;
/*     */ import org.springframework.http.ResponseEntity;
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
/*     */ public interface OperationSynchronizer
/*     */ {
/*     */   public static final String MODIFICATIONS_NOT_ALLOWED = "Modifications not allowed";
/*  27 */   public static final Semaphore lock = new Semaphore(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default <T> ResponseEntity<T> synchronizeWithResponseEntity(Supplier<ResponseEntity<T>> execution) {
/*     */     try {
/*  39 */       lock.acquire();
/*  40 */       return execution.get();
/*  41 */     } catch (InterruptedException e) {
/*  42 */       Thread.currentThread().interrupt();
/*  43 */       throw new StoreModificationNotAllowed("Modifications not allowed");
/*     */     } finally {
/*  45 */       lock.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default <T> List<T> synchronizeWithList(Supplier<List<T>> execution) {
/*     */     try {
/*  57 */       lock.acquire();
/*  58 */       return execution.get();
/*  59 */     } catch (InterruptedException e) {
/*  60 */       Thread.currentThread().interrupt();
/*  61 */       throw new StoreModificationNotAllowed("Modifications not allowed");
/*     */     } finally {
/*  63 */       lock.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default void synchronize(Runnable execution) {
/*     */     try {
/*  75 */       lock.acquire();
/*  76 */       execution.run();
/*  77 */     } catch (InterruptedException e) {
/*  78 */       Thread.currentThread().interrupt();
/*  79 */       throw new StoreModificationNotAllowed("Modifications not allowed");
/*     */     } finally {
/*  81 */       lock.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default <T> T synchronize(Supplier<T> execution, T type) {
/*     */     try {
/*  94 */       lock.acquire();
/*  95 */       return execution.get();
/*  96 */     } catch (InterruptedException e) {
/*  97 */       Thread.currentThread().interrupt();
/*  98 */       throw new StoreModificationNotAllowed("Modifications not allowed");
/*     */     } finally {
/* 100 */       lock.release();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\OperationSynchronizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */