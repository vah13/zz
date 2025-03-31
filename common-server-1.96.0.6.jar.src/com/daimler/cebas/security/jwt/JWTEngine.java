/*     */ package com.daimler.cebas.security.jwt;
/*     */ 
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.security.jwt.vo.JWTHeader;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.Signature;
/*     */ import java.security.SignatureException;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.PKCS8EncodedKeySpec;
/*     */ import java.util.Base64;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JWTEngine
/*     */ {
/*     */   private static final String INVALID_DER_SIGNATURE_FORMAT = "Invalid DER signature format.";
/*     */   private static final int EC_NUMBER_SIZE = 32;
/*     */   
/*     */   public static String createToken(Object content, String privateKey) throws JsonProcessingException {
/*  48 */     ObjectMapper mapper = new ObjectMapper();
/*     */     
/*  50 */     String headerJson = mapper.writeValueAsString(new JWTHeader());
/*  51 */     String payloadJson = mapper.writeValueAsString(content);
/*     */ 
/*     */     
/*  54 */     String header = Base64.getEncoder().encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));
/*     */     
/*  56 */     String payload = Base64.getEncoder().encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
/*     */     
/*  58 */     String signature = null;
/*     */     try {
/*  60 */       signature = Base64.getEncoder().encodeToString(
/*  61 */           signToken(String.join(".", new CharSequence[] { header, payload }), privateKey));
/*  62 */     } catch (NoSuchAlgorithmException|InvalidKeyException|SignatureException|NoSuchProviderException|InvalidKeySpecException e) {
/*     */ 
/*     */       
/*  65 */       throw new CEBASException(e.getMessage());
/*     */     } 
/*     */     
/*  68 */     return String.join(".", new CharSequence[] { header, payload, signature });
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] signToken(String token, String privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException, InvalidKeySpecException {
/*  95 */     PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey.getBytes(StandardCharsets.UTF_8)));
/*  96 */     KeyFactory keyFactory = KeyFactory.getInstance("EC");
/*  97 */     PrivateKey pk = keyFactory.generatePrivate(privateSpec);
/*     */     
/*  99 */     Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
/* 100 */     signature.initSign(pk);
/* 101 */     signature.update(token.getBytes(StandardCharsets.UTF_8));
/* 102 */     return DERToJOSE(signature.sign());
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] DERToJOSE(byte[] derSignature) throws SignatureException {
/* 107 */     boolean derEncoded = (derSignature[0] == 48 && derSignature.length != 64);
/*     */     
/* 109 */     if (!derEncoded) {
/* 110 */       throw new SignatureException("Invalid DER signature format.");
/*     */     }
/*     */     
/* 113 */     byte[] joseSignature = new byte[64];
/*     */ 
/*     */     
/* 116 */     int offset = 1;
/* 117 */     if (derSignature[1] == -127)
/*     */     {
/* 119 */       offset++;
/*     */     }
/*     */ 
/*     */     
/* 123 */     int encodedLength = derSignature[offset++] & 0xFF;
/* 124 */     if (encodedLength != derSignature.length - offset) {
/* 125 */       throw new SignatureException("Invalid DER signature format.");
/*     */     }
/*     */ 
/*     */     
/* 129 */     offset++;
/*     */ 
/*     */     
/* 132 */     int rLength = derSignature[offset++];
/* 133 */     if (rLength > 33) {
/* 134 */       throw new SignatureException("Invalid DER signature format.");
/*     */     }
/* 136 */     int rPadding = 32 - rLength;
/*     */     
/* 138 */     System.arraycopy(derSignature, offset + Math.max(-rPadding, 0), joseSignature, 
/* 139 */         Math.max(rPadding, 0), rLength + 
/* 140 */         Math.min(rPadding, 0));
/*     */ 
/*     */     
/* 143 */     offset += rLength + 1;
/*     */ 
/*     */     
/* 146 */     int sLength = derSignature[offset++];
/* 147 */     if (sLength > 33) {
/* 148 */       throw new SignatureException("Invalid DER signature format.");
/*     */     }
/* 150 */     int sPadding = 32 - sLength;
/*     */     
/* 152 */     System.arraycopy(derSignature, offset + Math.max(-sPadding, 0), joseSignature, 32 + 
/* 153 */         Math.max(sPadding, 0), sLength + 
/* 154 */         Math.min(sPadding, 0));
/*     */     
/* 156 */     return joseSignature;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\security\jwt\JWTEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */