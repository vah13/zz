/*    */ package com.daimler.cebas.common.control.pagination;
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
/*    */ public class PaginationHelper
/*    */ {
/*    */   private static final int SIZE_100 = 100;
/*    */   private static final int SIZE_1500 = 1500;
/*    */   private static final int SIZE_5000 = 5000;
/*    */   private static final int SIZE_25000 = 25000;
/*    */   private static final int SIZE_100000 = 100000;
/*    */   
/*    */   public static int getBatchSize(long totalCount) {
/* 22 */     if (totalCount <= 100L)
/* 23 */       return 100; 
/* 24 */     if (totalCount <= 1500L)
/* 25 */       return (int)totalCount * 20 / 100; 
/* 26 */     if (totalCount <= 5000L)
/* 27 */       return (int)totalCount * 10 / 100; 
/* 28 */     if (totalCount <= 25000L)
/* 29 */       return (int)totalCount * 3 / 100; 
/* 30 */     if (totalCount <= 100000L) {
/* 31 */       return (int)totalCount / 200;
/*    */     }
/* 33 */     return (int)totalCount / 500;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void makePagination(long totalSize, ISimplePagination consumer) {
/* 45 */     int page = 0;
/* 46 */     int batchSize = getBatchSize(totalSize);
/*    */     
/* 48 */     while ((page * batchSize) < totalSize) {
/* 49 */       consumer.accept(page, batchSize);
/* 50 */       page++;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean makeBooleanPagination(long totalSize, IBooleanPagination consumer) {
/* 62 */     int page = 0;
/* 63 */     int batchSize = getBatchSize(totalSize);
/*    */     
/* 65 */     while ((page * batchSize) < totalSize) {
/* 66 */       boolean result = consumer.accept(page, batchSize);
/* 67 */       if (!result) {
/* 68 */         return result;
/*    */       }
/* 70 */       page++;
/*    */     } 
/* 72 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isOnLastPage(int page, int pageSize, long totalCount) {
/* 84 */     return (getRemainingNumberOfElements(page, pageSize, totalCount) <= pageSize);
/*    */   }
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
/*    */   public static int getRemainingNumberOfElements(int page, int pageSize, long totalCount) {
/* 98 */     return (int)totalCount - page * pageSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\pagination\PaginationHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */