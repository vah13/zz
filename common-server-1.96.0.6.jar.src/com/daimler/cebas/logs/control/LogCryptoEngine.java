/*    */ package com.daimler.cebas.logs.control;
/*    */ 
/*    */ import com.daimler.cebas.common.CryptoTools;
/*    */ import com.daimler.cebas.common.control.CEBASException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.security.MessageDigest;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Base64;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
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
/*    */ public class LogCryptoEngine
/*    */ {
/* 25 */   private static final Logger LOG = Logger.getLogger(LogCryptoEngine.class.getName());
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
/*    */   public static String hashWithSHA512(String logMessage) {
/*    */     try {
/* 43 */       MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
/* 44 */       byte[] hashedString = messageDigest.digest(logMessage.getBytes());
/* 45 */       return Base64.getEncoder().encodeToString(hashedString);
/* 46 */     } catch (NoSuchAlgorithmException e) {
/* 47 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 48 */       throw new CEBASException(e.getMessage());
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
/*    */   public static String encryptAES(byte[] encryptionKey, byte[] text) {
/*    */     try {
/* 61 */       return Base64.getEncoder().encodeToString(
/* 62 */           CryptoTools.encryptAES(encryptionKey, text));
/* 63 */     } catch (Exception e) {
/* 64 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 65 */       throw new CEBASException(e.getMessage());
/*    */     } finally {
/* 67 */       Arrays.fill(text, (byte)0);
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
/*    */   public static String decryptAES(byte[] decryptionKey, byte[] cipherText) {
/*    */     try {
/* 80 */       byte[] decryptAES = CryptoTools.decryptAES(decryptionKey, cipherText);
/*    */       
/* 82 */       return new String(decryptAES, StandardCharsets.UTF_8);
/* 83 */     } catch (Exception e) {
/* 84 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 85 */       throw new CEBASException(e.getMessage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\logs\control\LogCryptoEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */