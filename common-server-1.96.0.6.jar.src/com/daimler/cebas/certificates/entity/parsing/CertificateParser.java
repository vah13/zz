/*     */ package com.daimler.cebas.certificates.entity.parsing;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.entity.PKIRole;
/*     */ import com.daimler.cebas.certificates.entity.UserRole;
/*     */ import com.daimler.cebas.common.CryptoParser;
/*     */ import com.daimler.cebas.common.ObjectIdentifier;
/*     */ import com.daimler.cebas.common.control.HexUtil;
/*     */ import com.daimler.cebas.exceptions.CryptoException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.PublicKey;
/*     */ import java.util.Base64;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.util.StreamUtils;
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
/*     */ public class CertificateParser
/*     */ {
/*  34 */   private static final Logger LOG = Logger.getLogger(CertificateParser.class.getName());
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
/*     */   public static String getSignature(Object cert) throws CEBASCertificateException {
/*     */     try {
/*  54 */       return HexUtil.bytesToHex(CryptoParser.getSignature(cert));
/*  55 */     } catch (CryptoException e) {
/*  56 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   public static byte[] getSignatureRaw(Object cert) throws CEBASCertificateException {
/*     */     try {
/*  71 */       return CryptoParser.getSignature(cert);
/*  72 */     } catch (CryptoException e) {
/*  73 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   public static String getVersion(Object cert) throws CEBASCertificateException {
/*     */     try {
/*  88 */       return CryptoParser.getVersion(cert);
/*  89 */     } catch (CryptoException e) {
/*  90 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   public static String getSerialNumber(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 105 */       return HexUtil.omitLeadingZeros(HexUtil.bytesToHex(CryptoParser.getSerialNumber(cert)));
/* 106 */     } catch (CryptoException e) {
/* 107 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   public static String getIssuerSerialNumber(Object holder) {
/*     */     try {
/* 120 */       return HexUtil.bytesToHex(CryptoParser.getIssuerSerialNumber(holder));
/* 121 */     } catch (CryptoException e) {
/* 122 */       LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/* 123 */       return "";
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
/*     */   public static String getSignAlgorithm(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 138 */       return CryptoParser.getSignAlgorithm(cert);
/* 139 */     } catch (CryptoException e) {
/* 140 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   public static CertificateType getPKIRole(Object cert) throws CEBASCertificateException {
/* 154 */     CertificateType type = CertificateType.NO_TYPE;
/*     */     try {
/* 156 */       Optional<byte[]> pkiRoleOptional = CryptoParser.getExtensionPrimitive(cert, ObjectIdentifier.PKI_ROLE_OID.getOid());
/* 157 */       if (pkiRoleOptional.isPresent()) {
/* 158 */         byte[] pkiRole = pkiRoleOptional.get();
/* 159 */         type = (CertificateType)PKIRole.getRoles().get(Integer.valueOf(pkiRole[0]));
/*     */       } 
/* 161 */       return type;
/* 162 */     } catch (IOException e) {
/* 163 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 164 */       return CertificateType.NO_TYPE;
/* 165 */     } catch (CryptoException e) {
/* 166 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   
/*     */   public static byte[] getPKIRoleRaw(Object cert) throws CEBASCertificateException {
/* 181 */     byte[] pkiRole = null;
/*     */     try {
/* 183 */       Optional<byte[]> pkiRoleOptional = CryptoParser.getExtensionPrimitive(cert, ObjectIdentifier.PKI_ROLE_OID.getOid());
/* 184 */       if (pkiRoleOptional.isPresent()) {
/* 185 */         pkiRole = pkiRoleOptional.get();
/*     */       }
/* 187 */       return pkiRole;
/* 188 */     } catch (IOException e) {
/* 189 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 190 */       throw new CEBASCertificateException(e.getMessage());
/* 191 */     } catch (CryptoException e) {
/* 192 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   
/*     */   public static String getUserRole(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 208 */       Optional<byte[]> userRole = CryptoParser.getExtensionPrimitive(cert, ObjectIdentifier.USER_ROLE_OID.getOid());
/* 209 */       StringBuilder buffer = new StringBuilder();
/* 210 */       userRole.ifPresent(roles -> {
/*     */             if (roles.length > 0) {
/*     */               buffer.append(roles[0]);
/*     */             }
/*     */           });
/* 215 */       String role = buffer.toString();
/* 216 */       return role.equals("") ? role : UserRole.getUserRoleFromByte((new Byte(role)).byteValue()).getText();
/* 217 */     } catch (IOException e) {
/* 218 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 219 */       throw new CEBASCertificateException(e.getMessage());
/* 220 */     } catch (CryptoException e) {
/* 221 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   
/*     */   public static byte[] getUserRoleRaw(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 237 */       byte[] userRoleByte = null;
/* 238 */       Optional<byte[]> userRole = CryptoParser.getExtensionPrimitive(cert, ObjectIdentifier.USER_ROLE_OID.getOid());
/* 239 */       if (userRole.isPresent()) {
/* 240 */         userRoleByte = userRole.get();
/*     */       }
/* 242 */       return userRoleByte;
/* 243 */     } catch (IOException e) {
/* 244 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 245 */       throw new CEBASCertificateException(e.getMessage());
/* 246 */     } catch (CryptoException e) {
/* 247 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   
/*     */   public static String getTargetEcus(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 263 */       return CryptoParser.getExtensionPrimitives(cert, ObjectIdentifier.TARGET_ECU_OID.getOid());
/* 264 */     } catch (IOException e) {
/* 265 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 266 */       throw new CEBASCertificateException(e.getMessage());
/* 267 */     } catch (CryptoException e) {
/* 268 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   
/*     */   public static String getTargetVin(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 284 */       return CryptoParser.getExtensionPrimitives(cert, ObjectIdentifier.TARGET_VIN_OID.getOid());
/* 285 */     } catch (IOException e) {
/* 286 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 287 */       throw new CEBASCertificateException(e.getMessage());
/* 288 */     } catch (CryptoException e) {
/* 289 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   
/*     */   public static String getProdQualifier(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 305 */       StringBuilder result = new StringBuilder();
/* 306 */       Optional<byte[]> prodQualifier = CryptoParser.getExtensionPrimitive(cert, ObjectIdentifier.PROD_QUALIFIER_OID.getOid());
/* 307 */       prodQualifier.ifPresent(qualifier -> result.append(qualifier[0]));
/* 308 */       return result.toString();
/* 309 */     } catch (IOException e) {
/* 310 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 311 */       throw new CEBASCertificateException("Could not get prod qualifier");
/* 312 */     } catch (CryptoException e) {
/* 313 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   
/*     */   public static byte[] getProdQualifierRaw(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 329 */       byte[] qualifier = null;
/* 330 */       Optional<byte[]> prodQualifier = CryptoParser.getExtensionPrimitive(cert, ObjectIdentifier.PROD_QUALIFIER_OID.getOid());
/* 331 */       if (prodQualifier.isPresent()) {
/* 332 */         qualifier = prodQualifier.get();
/*     */       }
/* 334 */       return qualifier;
/* 335 */     } catch (IOException e) {
/* 336 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 337 */       throw new CEBASCertificateException("Could not get prod qualifier");
/* 338 */     } catch (CryptoException e) {
/* 339 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   
/*     */   public static String getUniqueEcuIds(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 355 */       return CryptoParser.getExtensionPrimitives(cert, ObjectIdentifier.UNIQUE_ECU_OID.getOid());
/* 356 */     } catch (IOException e) {
/* 357 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 358 */       throw new CEBASCertificateException(e.getMessage());
/* 359 */     } catch (CryptoException e) {
/* 360 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   public static String getSpecialEcu(Object cert) throws CEBASCertificateException {
/* 374 */     StringBuilder buffer = new StringBuilder();
/*     */     try {
/* 376 */       Optional<byte[]> optional = CryptoParser.getExtensionPrimitive(cert, ObjectIdentifier.SPECIAL_ECU_OID.getOid());
/* 377 */       if (optional.isPresent()) {
/* 378 */         byte[] result = optional.get();
/* 379 */         if (result.length != 0) {
/* 380 */           buffer.append(result[0]);
/*     */         } else {
/* 382 */           LOG.warning("000208X The special ECU property is not available.");
/*     */         } 
/*     */       } 
/* 385 */     } catch (IOException e) {
/* 386 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 387 */       LOG.warning("000208X The special ECU property is not available. Reason: " + e.getMessage());
/* 388 */     } catch (CryptoException e) {
/* 389 */       throw new CEBASCertificateException(e.getMessage());
/*     */     } 
/* 391 */     return buffer.toString();
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
/*     */   public static byte[] getSpecialEcuRaw(Object cert) throws CEBASCertificateException {
/* 404 */     byte[] specialEcu = null;
/*     */     try {
/* 406 */       Optional<byte[]> optional = CryptoParser.getExtensionPrimitive(cert, ObjectIdentifier.SPECIAL_ECU_OID.getOid());
/* 407 */       if (optional.isPresent()) {
/* 408 */         specialEcu = optional.get();
/*     */       }
/* 410 */     } catch (IOException e) {
/* 411 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 412 */       LOG.warning("000206X SpecialECU data was not extracted. Reason: " + e.getMessage());
/* 413 */     } catch (CryptoException e) {
/* 414 */       throw new CEBASCertificateException(e.getMessage());
/*     */     } 
/* 416 */     return specialEcu;
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
/*     */   public static String getTargetSubjectKeyIdentifier(Object cert) throws CEBASCertificateException {
/* 429 */     String result = null;
/*     */     try {
/* 431 */       Optional<byte[]> optional = CryptoParser.getExtensionPrimitive(cert, ObjectIdentifier.TARGET_SUBJECT_KEY_IDENTIFIER_OID.getOid());
/* 432 */       if (optional.isPresent()) {
/* 433 */         result = HexUtil.bytesToHex(optional.get());
/*     */       }
/* 435 */     } catch (IOException e) {
/* 436 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 437 */       LOG.warning("000210X Target Subject Key Identifier is not available. Reason: " + e.getMessage());
/* 438 */     } catch (CryptoException e) {
/* 439 */       throw new CEBASCertificateException(e.getMessage());
/*     */     } 
/* 441 */     return (result != null) ? result.trim() : "";
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
/*     */   public static boolean containsTargetSubjectKeyIdentifier(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 455 */       Optional<byte[]> optional = CryptoParser.getExtensionPrimitive(cert, ObjectIdentifier.TARGET_SUBJECT_KEY_IDENTIFIER_OID.getOid());
/* 456 */       return optional.isPresent();
/* 457 */     } catch (IOException e) {
/* 458 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 459 */       return false;
/* 460 */     } catch (CryptoException e) {
/* 461 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   public static String getSubjectPublicKey(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 476 */       return HexUtil.bytesToHex(CryptoParser.getSubjectPublicKey(cert));
/* 477 */     } catch (CryptoException e) {
/* 478 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   public static byte[] getSubjectPublicKeyBytes(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 493 */       return CryptoParser.getSubjectPublicKey(cert);
/* 494 */     } catch (CryptoException e) {
/* 495 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   public static boolean[] getKeyUsage(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 510 */       return CryptoParser.getKeyUsage(cert);
/* 511 */     } catch (CryptoException e) {
/* 512 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   public static String getBasicConstraints(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 527 */       return CryptoParser.getBasicConstraints(cert);
/* 528 */     } catch (CryptoException e) {
/* 529 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   
/*     */   public static String getSubjectKeyIdentifier(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 545 */       StringBuilder buffer = new StringBuilder();
/* 546 */       Optional<byte[]> subjectKeyIdentifier = CryptoParser.getExtensionPrimitive(cert, ObjectIdentifier.SUBJECT_KEY_IDENTIFIER_OID.getOid());
/* 547 */       subjectKeyIdentifier.ifPresent(ski -> buffer.append(HexUtil.bytesToHex(ski)));
/* 548 */       return buffer.toString().trim();
/* 549 */     } catch (IOException e) {
/* 550 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 551 */       throw new CEBASCertificateException(e.getMessage());
/* 552 */     } catch (CryptoException e) {
/* 553 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   
/*     */   public static byte[] getSubjectKeyIdentifierRaw(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 569 */       Optional<byte[]> subjectKeyIdentifier = CryptoParser.getExtensionPrimitive(cert, ObjectIdentifier.SUBJECT_KEY_IDENTIFIER_OID.getOid());
/* 570 */       return subjectKeyIdentifier.orElseGet(() -> new byte[0]);
/* 571 */     } catch (IOException e) {
/* 572 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 573 */       throw new CEBASCertificateException(e.getMessage());
/* 574 */     } catch (CryptoException e) {
/* 575 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   
/*     */   public static String getServices(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 591 */       return CryptoParser.getAttributePrimitives(cert, ObjectIdentifier.SERVICES_OID.getOid());
/* 592 */     } catch (IOException e) {
/* 593 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 594 */       throw new CEBASCertificateException(e.getMessage());
/* 595 */     } catch (CryptoException e) {
/* 596 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   public static String getAuthorityKeyIdentifier(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 611 */       return HexUtil.bytesToHex(CryptoParser.getAuthorityKeyIdentifier(cert));
/* 612 */     } catch (IOException e) {
/* 613 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 614 */       return "";
/* 615 */     } catch (CryptoException e) {
/* 616 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   public static byte[] getAuthorityKeyIdentifierRaw(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 631 */       return CryptoParser.getAuthorityKeyIdentifier(cert);
/* 632 */     } catch (IOException e) {
/* 633 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 634 */       return new byte[0];
/* 635 */     } catch (CryptoException e) {
/* 636 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   public static String getNonce(Object cert) throws CEBASCertificateException {
/* 650 */     String str = null;
/*     */     try {
/* 652 */       Optional<byte[]> optional = CryptoParser.getExtensionPrimitive(cert, ObjectIdentifier.NONCE_OID.getOid());
/* 653 */       if (optional.isPresent()) {
/* 654 */         str = HexUtil.bytesToHex(optional.get());
/*     */       }
/* 656 */     } catch (IOException e) {
/* 657 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 658 */       LOG.warning("000207X Nonce is not available. Reason: " + e.getMessage());
/* 659 */     } catch (CryptoException e) {
/* 660 */       throw new CEBASCertificateException(e.getMessage());
/*     */     } 
/* 662 */     return (str != null) ? str.trim() : "";
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
/*     */   public static byte[] getNonceRaw(Object cert) throws CEBASCertificateException {
/* 675 */     byte[] nonce = null;
/*     */     try {
/* 677 */       Optional<byte[]> optional = CryptoParser.getExtensionPrimitive(cert, ObjectIdentifier.NONCE_OID.getOid());
/* 678 */       if (optional.isPresent()) {
/* 679 */         nonce = optional.get();
/*     */       }
/* 681 */     } catch (IOException e) {
/* 682 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 683 */       LOG.warning("000209X Nonce data was not extracted. Reason: " + e.getMessage());
/* 684 */     } catch (CryptoException e) {
/* 685 */       throw new CEBASCertificateException(e.getMessage());
/*     */     } 
/* 687 */     return nonce;
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
/*     */   public static boolean hasNonce(Object cert) throws CEBASCertificateException {
/*     */     try {
/* 701 */       Optional<byte[]> optional = CryptoParser.getExtensionPrimitive(cert, ObjectIdentifier.NONCE_OID.getOid());
/* 702 */       return optional.isPresent();
/* 703 */     } catch (IOException e) {
/* 704 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 705 */       LOG.warning("000207X Nonce is not available. Reason: " + e.getMessage());
/* 706 */       return false;
/* 707 */     } catch (CryptoException e) {
/* 708 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getValidityStrength(long currentMillis, long validFromMillis, long validToMillis) {
/* 728 */     if (validToMillis < currentMillis) {
/* 729 */       return 0;
/*     */     }
/* 731 */     double result = ((currentMillis - validFromMillis) * 100L) / (validToMillis - validFromMillis);
/* 732 */     return 100 - (int)result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isInvalidPublicKey(InputStream certificateInput) {
/*     */     try {
/* 744 */       byte[] encoded = StreamUtils.copyToByteArray(certificateInput);
/* 745 */       return !CryptoParser.validatePublicKey(encoded);
/* 746 */     } catch (IOException|org.hibernate.boot.model.naming.IllegalIdentifierException|IllegalArgumentException e) {
/* 747 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 748 */       return true;
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
/*     */   public static String getSubjectKeyIdentifierFromPublicKey(String publicKeyKex) throws CEBASCertificateException {
/*     */     try {
/* 763 */       byte[] publicKeyBytes = hexStringToByteArray(publicKeyKex.trim());
/* 764 */       return HexUtil.bytesToHex(CryptoParser.getSubjectKeyIdentifierFromPublicKey(publicKeyBytes));
/* 765 */     } catch (CryptoException|IllegalArgumentException e) {
/* 766 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 767 */       throw new CEBASCertificateException(e.getMessage());
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
/*     */   public static String getSubjectKeyIdentifierFromPublicKey(PublicKey publicKey) throws CEBASCertificateException {
/*     */     try {
/* 782 */       return HexUtil.bytesToHex(CryptoParser.getSubjectKeyIdentifierFromPublicKey(publicKey));
/* 783 */     } catch (CryptoException e) {
/* 784 */       LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/* 785 */       throw new CEBASCertificateException(e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] hexStringToByteArray(String value) {
/* 796 */     if (value == null) {
/* 797 */       return null;
/*     */     }
/* 799 */     return HexUtil.hexStringToByteArray(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String hexToBase64(String s) {
/* 809 */     if (s == null) {
/* 810 */       return null;
/*     */     }
/* 812 */     if (StringUtils.isBlank(s)) {
/* 813 */       return "";
/*     */     }
/* 815 */     return Base64.getEncoder().encodeToString(hexStringToByteArray(s));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decodeBase64(String s) {
/* 825 */     if (s == null) {
/* 826 */       return null;
/*     */     }
/* 828 */     return Base64.getDecoder().decode(s);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\entity\parsing\CertificateParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */