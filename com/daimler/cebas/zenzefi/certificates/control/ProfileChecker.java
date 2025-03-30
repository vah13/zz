/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ProfileChecker
/*    */ {
/*    */   public static boolean isAfterSales(String[] profiles) {
/* 28 */     for (String profile : profiles) {
/* 29 */       if (profile.equals("AFTERSALES")) {
/* 30 */         return true;
/*    */       }
/*    */     } 
/* 33 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isNotAfterSales(String[] profiles) {
/* 44 */     return !isAfterSales(profiles);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\control\ProfileChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */