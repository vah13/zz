/*     */ package com.daimler.cebas.users.control.crypto;
/*     */ 
/*     */ import com.daimler.cebas.certificates.CertificateCryptoEngine;
/*     */ import com.daimler.cebas.certificates.control.crypto.CustomPKCS8EncodedKeySpec;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.common.CryptoTools;
/*     */ import com.daimler.cebas.common.PublicKeyCryptoAlgorithms;
/*     */ import com.daimler.cebas.common.control.HexUtil;
/*     */ import com.daimler.cebas.common.control.SecurityProviders;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.exceptions.UserCryptoException;
/*     */ import com.daimler.cebas.users.control.factories.UserFactory;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.users.entity.UserKeyPair;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.KeyPair;
/*     */ import java.security.KeyPairGenerator;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.KeySpec;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ import java.util.Arrays;
/*     */ import java.util.Base64;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.crypto.SecretKeyFactory;
/*     */ import javax.crypto.spec.PBEKeySpec;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.asn1.x9.X9ECParameters;
/*     */ import org.bouncycastle.crypto.BlockCipher;
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.ec.CustomNamedCurves;
/*     */ import org.bouncycastle.crypto.engines.AESEngine;
/*     */ import org.bouncycastle.crypto.modes.CBCBlockCipher;
/*     */ import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
/*     */ import org.bouncycastle.crypto.params.KeyParameter;
/*     */ import org.bouncycastle.jce.spec.ECParameterSpec;
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASControl
/*     */ public class UserCryptoEngine
/*     */ {
/*  52 */   private static final Logger LOG = Logger.getLogger(UserCryptoEngine.class.getSimpleName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private static final String ED_DSA = PublicKeyCryptoAlgorithms.EdDSA.name();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   private static final String ECDH = PublicKeyCryptoAlgorithms.ECDH.name();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private static final String CLASS_NAME = UserCryptoEngine.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserCryptoEngine(Logger logger) {
/*  80 */     this.logger = logger;
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
/*     */   public String encryptMachineNameUsingPasswordHash(String secret, String machineName, User user) {
/*  93 */     String METHOD_NAME = "encryptMachineNameUsingPasswordHash";
/*  94 */     String decryptedSalt = getAESDecryptedSalt(secret, user);
/*  95 */     String decodedPassword = getBase64Decoded(user.getUserPassword().getBytes(StandardCharsets.UTF_8));
/*  96 */     String passwordHash = hashPassword(decodedPassword, decryptedSalt, getHashIterationCount(user));
/*  97 */     return encryptMachineName(passwordHash, machineName);
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
/*     */   public boolean matchPublicKeys(String containerKey, Certificate certificate, UserKeyPair userKeyPair) {
/* 112 */     byte[] decodedPrivateKey = decodePrivateKeyToByteArray(containerKey, userKeyPair.getPrivateKey());
/* 113 */     PrivateKey privateKey = CertificateCryptoEngine.generatePrivateKey(decodedPrivateKey);
/*     */     try {
/* 115 */       PublicKey calculatedPublicKey = CertificateCryptoEngine.calculateEDDSAFromEdDSAPrivate(privateKey);
/* 116 */       String publicKeyFromPrivate = getPublicKeyDataHex(calculatedPublicKey);
/* 117 */       return certificate.getSubjectPublicKey().equals(publicKeyFromPrivate);
/* 118 */     } catch (GeneralSecurityException e) {
/* 119 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 120 */       return false;
/*     */     } finally {
/* 122 */       CryptoTools.destroyPrivateKey(privateKey);
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
/*     */ 
/*     */   
/*     */   public byte[] encryptAES(String encryptionKey, byte[] text) {
/* 136 */     String METHOD_NAME = "encryptAES";
/*     */     try {
/* 138 */       int textLength = text.length;
/*     */       
/* 140 */       PaddedBufferedBlockCipher encryptCipher = new PaddedBufferedBlockCipher((BlockCipher)new CBCBlockCipher((BlockCipher)new AESEngine()));
/*     */       
/* 142 */       encryptCipher.init(true, (CipherParameters)new KeyParameter(encryptionKey.getBytes(StandardCharsets.UTF_8)));
/* 143 */       byte[] buffer = new byte[encryptCipher.getOutputSize(textLength)];
/* 144 */       int outputLength = encryptCipher.processBytes(text, 0, textLength, buffer, 0);
/* 145 */       outputLength += encryptCipher.doFinal(buffer, outputLength);
/* 146 */       if (outputLength < buffer.length) {
/* 147 */         return Arrays.copyOfRange(buffer, 0, outputLength);
/*     */       }
/* 149 */       return buffer;
/* 150 */     } catch (Exception e) {
/* 151 */       LOG.log(Level.WARNING, e.getMessage(), e);
/* 152 */       throw new UserCryptoException(e.getMessage());
/*     */     } finally {
/* 154 */       Arrays.fill(text, (byte)0);
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
/*     */   
/*     */   public byte[] decryptAES(String decryptionKey, byte[] cipherText) {
/* 167 */     String METHOD_NAME = "decryptAES";
/*     */     try {
/* 169 */       int cipherTextLength = cipherText.length;
/* 170 */       PaddedBufferedBlockCipher encryptCipher = new PaddedBufferedBlockCipher((BlockCipher)new CBCBlockCipher((BlockCipher)new AESEngine()));
/*     */       
/* 172 */       encryptCipher.init(false, (CipherParameters)new KeyParameter(decryptionKey.getBytes(StandardCharsets.UTF_8)));
/* 173 */       byte[] buffer = new byte[encryptCipher.getOutputSize(cipherTextLength)];
/* 174 */       int outputLength = encryptCipher.processBytes(cipherText, 0, cipherTextLength, buffer, 0);
/* 175 */       outputLength += encryptCipher.doFinal(buffer, outputLength);
/* 176 */       if (outputLength < buffer.length) {
/* 177 */         return Arrays.copyOfRange(buffer, 0, outputLength);
/*     */       }
/* 179 */       return buffer;
/* 180 */     } catch (Exception e) {
/* 181 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 182 */       throw new UserCryptoException(e.getMessage());
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
/*     */   public String getAESEncryptedUserPassword(String secret, String password) {
/* 194 */     String METHOD_NAME = "getAESEncryptedUserPassword";
/* 195 */     byte[] encPassword = encryptAES(secret, password.getBytes(StandardCharsets.UTF_8));
/* 196 */     String encPasswordString = Base64.getEncoder().encodeToString(encPassword);
/* 197 */     return encPasswordString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAESEncryptedSalt(String secret, String salt) {
/* 208 */     String METHOD_NAME = "getAESEncryptedSalt";
/* 209 */     byte[] encSalt = encryptAES(secret, salt.getBytes(StandardCharsets.UTF_8));
/* 210 */     String encSaltString = Base64.getEncoder().encodeToString(encSalt);
/* 211 */     return encSaltString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAESDecryptedUserPassword(String secret, User user) {
/* 222 */     String METHOD_NAME = "getAESDecryptedUserPassword";
/* 223 */     String encryptedPassword = user.getUserPassword();
/* 224 */     byte[] decryptedPassword = decryptAES(secret, Base64.getDecoder().decode(encryptedPassword));
/* 225 */     return new String(decryptedPassword, StandardCharsets.UTF_8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAESDecryptedSalt(String secret, User user) {
/* 236 */     String METHOD_NAME = "getAESDecryptedSalt";
/* 237 */     String encryptedSalt = user.getSalt();
/* 238 */     byte[] decryptedSalt = decryptAES(secret, Base64.getDecoder().decode(encryptedSalt));
/* 239 */     return new String(decryptedSalt, StandardCharsets.UTF_8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String generateContainerKey(String secret, User user) {
/* 250 */     String METHOD_NAME = "generateContainerKey";
/*     */     try {
/* 252 */       String decryptedPassword = getAESDecryptedUserPassword(secret, user);
/* 253 */       String decryptedSalt = getAESDecryptedSalt(secret, user);
/* 254 */       return hashPassword(decryptedPassword, decryptedSalt, getHashIterationCount(user));
/* 255 */     } catch (Exception e) {
/* 256 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 257 */       throw new UserCryptoException(e.getMessage());
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
/*     */   
/*     */   public String hashPassword(String decryptedPassword, String decryptedSalt, int hashIterationCount) {
/* 270 */     int keyLength = 128;
/*     */     
/* 272 */     KeySpec spec = new PBEKeySpec(decryptedPassword.toCharArray(), decryptedSalt.getBytes(StandardCharsets.UTF_8), hashIterationCount, keyLength);
/*     */     
/* 274 */     byte[] encodedKey = null;
/*     */     try {
/* 276 */       SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
/* 277 */       encodedKey = factory.generateSecret(spec).getEncoded();
/* 278 */       return Base64.getEncoder().encodeToString(encodedKey);
/* 279 */     } catch (InvalidKeySpecException|NoSuchAlgorithmException e) {
/* 280 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 281 */       throw new UserCryptoException(e.getMessage());
/*     */     } finally {
/* 283 */       Arrays.fill(encodedKey, (byte)0);
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
/*     */   private int getHashIterationCount(User user) {
/* 295 */     if (isDefaultUser(user)) {
/* 296 */       return 10000;
/*     */     }
/* 298 */     return 100000;
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
/*     */   private boolean isDefaultUser(User user) {
/* 311 */     if (user == null) {
/* 312 */       return false;
/*     */     }
/* 314 */     if (user.getUserName() == null) {
/* 315 */       return false;
/*     */     }
/* 317 */     return user.getUserName().equals(UserFactory.getDefaultUsername());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String encryptMachineName(String encryptionKey, String machineName) {
/* 328 */     String METHOD_NAME = "encryptMachineName";
/* 329 */     return Base64.getEncoder().encodeToString(encryptAES(encryptionKey, machineName.getBytes(StandardCharsets.UTF_8)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String decryptMachineName(String containerKey, String machineNameEncrypted) {
/* 340 */     String METHOD_NAME = "decryptMachineName";
/* 341 */     return new String(decryptAES(containerKey, Base64.getDecoder().decode(machineNameEncrypted)), StandardCharsets.UTF_8);
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
/*     */   public String generateContainerKey(String salt, String password, User user) {
/* 353 */     String METHOD_NAME = "generateContainerKey";
/* 354 */     return hashPassword(password, salt, getHashIterationCount(user));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getBase64Decoded(byte[] encoded) {
/* 365 */     return new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeyPair generateKeyPair() {
/* 374 */     String METHOD_NAME = "generateKeyPair";
/*     */     try {
/* 376 */       KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ED_DSA, SecurityProviders.SNED25519.name());
/* 377 */       KeyPair keyPair = keyGen.generateKeyPair();
/* 378 */       return keyPair;
/* 379 */     } catch (Exception e) {
/* 380 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 381 */       throw new UserCryptoException(e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeyPair generateCommunicationKeyPair() {
/* 391 */     String METHOD_NAME = "generateCommunicationKeyPair";
/*     */     try {
/* 393 */       KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ECDH, SecurityProviders.BC.name());
/* 394 */       X9ECParameters ecP = CustomNamedCurves.getByName("curve25519");
/*     */       
/* 396 */       ECParameterSpec ecSpec = new ECParameterSpec(ecP.getCurve(), ecP.getG(), ecP.getN(), ecP.getH(), ecP.getSeed());
/* 397 */       keyGen.initialize((AlgorithmParameterSpec)ecSpec, new SecureRandom());
/* 398 */       KeyPair keyPair = keyGen.generateKeyPair();
/* 399 */       return keyPair;
/* 400 */     } catch (Exception e) {
/* 401 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 402 */       throw new UserCryptoException(e.getMessage());
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
/*     */   
/*     */   public String getPublicKeyDataHex(PublicKey key) {
/* 415 */     String METHOD_NAME = "getPublicKeyHex";
/* 416 */     X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(key.getEncoded());
/* 417 */     ASN1Sequence asn1seq = ASN1Sequence.getInstance(pubKeySpec.getEncoded());
/* 418 */     SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(asn1seq);
/* 419 */     byte[] pubBytes = subjectPublicKeyInfo.getPublicKeyData().getBytes();
/* 420 */     return HexUtil.bytesToHex(pubBytes).trim();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPublicKeyEncoded(PublicKey key) {
/* 430 */     return HexUtil.bytesToHex(key.getEncoded()).trim();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrivateKey getCommunicationPrivateKeyFromBytes(byte[] privateKeyBytes) {
/* 440 */     CustomPKCS8EncodedKeySpec spec = null;
/* 441 */     PrivateKey privateKey = null;
/*     */     try {
/* 443 */       spec = new CustomPKCS8EncodedKeySpec(privateKeyBytes);
/* 444 */       KeyFactory factory = KeyFactory.getInstance(PublicKeyCryptoAlgorithms.ECDH.name());
/* 445 */       privateKey = factory.generatePrivate((KeySpec)spec);
/* 446 */       return privateKey;
/* 447 */     } catch (NoSuchAlgorithmException|InvalidKeySpecException e) {
/* 448 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 449 */       throw new UserCryptoException(e.getMessage());
/*     */     } finally {
/* 451 */       if (spec != null) {
/* 452 */         spec.destroy();
/*     */       }
/* 454 */       CryptoTools.destroyPrivateKey(privateKey);
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
/*     */   public String encodePrivateKey(String containerKey, byte[] privateKey) {
/* 466 */     String METHOD_NAME = "encodePrivateKey";
/* 467 */     return Base64.getEncoder().encodeToString(encryptAES(containerKey, privateKey));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String decodePrivateKey(String containerKey, String encodedPrivateKey) {
/* 478 */     String METHOD_NAME = "decodePrivateKey";
/* 479 */     return new String(decryptAES(containerKey, Base64.getDecoder().decode(encodedPrivateKey)), StandardCharsets.UTF_8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] decodePrivateKeyToByteArray(String containerKey, String encodedPrivateKey) {
/* 490 */     String METHOD_NAME = "decodePrivateKeyToByteArray";
/* 491 */     return decryptAES(containerKey, Base64.getDecoder().decode(encodedPrivateKey));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserKeyPair generateUserKeyPair(String containerKey, User user) {
/* 502 */     KeyPair generateKeyPair = generateKeyPair();
/*     */     try {
/* 504 */       String publicKey = getPublicKeyDataHex(generateKeyPair.getPublic());
/* 505 */       String encodedPrivateKey = encodePrivateKey(containerKey, generateKeyPair.getPrivate().getEncoded());
/* 506 */       return new UserKeyPair(publicKey, encodedPrivateKey, user);
/*     */     } finally {
/* 508 */       CryptoTools.destroyPrivateKey(generateKeyPair.getPrivate());
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
/*     */   public UserKeyPair generateUserKeyPairCommunication(String containerKey, User user) {
/* 520 */     KeyPair generateKeyPair = generateCommunicationKeyPair();
/*     */     try {
/* 522 */       String publicKey = getPublicKeyEncoded(generateKeyPair.getPublic());
/* 523 */       String encodedPrivateKey = encodePrivateKey(containerKey, generateKeyPair.getPrivate().getEncoded());
/* 524 */       return new UserKeyPair(publicKey, encodedPrivateKey, user);
/*     */     } finally {
/* 526 */       CryptoTools.destroyPrivateKey(generateKeyPair.getPrivate());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\ceba\\users\control\crypto\UserCryptoEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */