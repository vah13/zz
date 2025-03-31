/*    */ package com.daimler.cebas.common.control;
/*    */ 
/*    */ import java.security.SecureRandom;
/*    */ import java.util.Base64;
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
/*    */ public final class GenerateSecureRandom
/*    */ {
/*    */   public static String generateSecureNumber(int length) {
/* 26 */     SecureRandom secureRandom = new SecureRandom();
/* 27 */     byte[] bytes = new byte[length];
/* 28 */     secureRandom.nextBytes(bytes);
/* 29 */     return Base64.getEncoder().encodeToString(bytes);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\GenerateSecureRandom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */