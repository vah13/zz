/*    */ package com.daimler.cebas.security;
/*    */ 
/*    */ import com.daimler.cebas.common.control.CEBASException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.security.GeneralSecurityException;
/*    */ import java.security.InvalidKeyException;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.SecureRandom;
/*    */ import java.util.Arrays;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import javax.crypto.Cipher;
/*    */ import javax.crypto.KeyGenerator;
/*    */ import javax.crypto.NoSuchPaddingException;
/*    */ import javax.crypto.SecretKey;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EncryptedString
/*    */ {
/* 22 */   private static final Logger LOG = Logger.getLogger(EncryptedString.class.getName());
/*    */   
/*    */   private byte[] encryptedSecret;
/*    */   private static Cipher cipher;
/*    */   private KeyGenerator keyGenerator;
/*    */   private SecretKey secretKey;
/*    */   
/*    */   public void setValue(String value) {
/* 30 */     if (value == null) {
/* 31 */       destroy();
/* 32 */       this.encryptedSecret = null;
/*    */     } else {
/*    */       try {
/* 35 */         this.secretKey = getKeyGenerator().generateKey();
/* 36 */         getCipher().init(1, this.secretKey);
/* 37 */         this.encryptedSecret = getCipher().doFinal(value.getBytes(StandardCharsets.UTF_8));
/* 38 */       } catch (InvalidKeyException|NoSuchAlgorithmException|NoSuchPaddingException|javax.crypto.IllegalBlockSizeException|javax.crypto.BadPaddingException e) {
/*    */         
/* 40 */         LOG.log(Level.FINEST, e.getMessage(), e);
/* 41 */         throw new CEBASException(e.getMessage());
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 47 */     if (this.encryptedSecret == null) {
/* 48 */       return null;
/*    */     }
/*    */     
/*    */     try {
/* 52 */       getCipher().init(2, this.secretKey);
/* 53 */       byte[] decryptedByte = getCipher().doFinal(this.encryptedSecret);
/* 54 */       String decryptedText = new String(decryptedByte, StandardCharsets.UTF_8);
/* 55 */       return decryptedText;
/* 56 */     } catch (InvalidKeyException|NoSuchAlgorithmException|NoSuchPaddingException|javax.crypto.IllegalBlockSizeException|javax.crypto.BadPaddingException e) {
/*    */       
/* 58 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 59 */       throw new CEBASException(e.getMessage());
/*    */     } 
/*    */   }
/*    */   
/*    */   public void destroy() {
/* 64 */     if (this.encryptedSecret != null) {
/* 65 */       Arrays.fill(this.encryptedSecret, (byte)0);
/*    */     }
/*    */   }
/*    */   
/*    */   private synchronized KeyGenerator getKeyGenerator() throws NoSuchAlgorithmException {
/* 70 */     if (this.keyGenerator == null) {
/* 71 */       SecureRandom rand = new SecureRandom();
/* 72 */       this.keyGenerator = KeyGenerator.getInstance("AES");
/* 73 */       this.keyGenerator.init(256, rand);
/*    */     } 
/* 75 */     return this.keyGenerator;
/*    */   }
/*    */   
/*    */   private synchronized Cipher getCipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
/* 79 */     if (cipher == null) {
/* 80 */       cipher = Cipher.getInstance("AES");
/*    */     }
/* 82 */     return cipher;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\security\EncryptedString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */