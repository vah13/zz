/*    */ package com.daimler.cebas.certificates.control.crypto;
/*    */ 
/*    */ import java.security.spec.PKCS8EncodedKeySpec;
/*    */ import java.util.Arrays;
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
/*    */ public class CustomPKCS8EncodedKeySpec
/*    */   extends PKCS8EncodedKeySpec
/*    */ {
/*    */   private byte[] encodedKey;
/*    */   
/*    */   public CustomPKCS8EncodedKeySpec(byte[] encodedKey) {
/* 29 */     super(new byte[0]);
/* 30 */     this.encodedKey = encodedKey;
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
/*    */   public byte[] getEncoded() {
/* 42 */     return this.encodedKey;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void destroy() {
/* 51 */     Arrays.fill(this.encodedKey, (byte)0);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\crypto\CustomPKCS8EncodedKeySpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */