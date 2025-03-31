/*     */ package com.daimler.cebas.common.control;
/*     */ 
/*     */ import java.util.Base64;
/*     */ import java.util.Locale;
/*     */ import org.apache.commons.lang3.RegExUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HexUtil
/*     */ {
/*     */   public static final String GROUP_SEPARATOR = "-";
/*     */   private static final String _00_GROUP = "00";
/*  18 */   protected static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
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
/*     */   public static String bytesToHex(byte[] bytes) {
/*  36 */     char[] hexChars = new char[bytes.length * 3];
/*  37 */     for (int j = 0; j < bytes.length; j++) {
/*  38 */       int v = bytes[j] & 0xFF;
/*  39 */       hexChars[j * 3] = HEX_ARRAY[v >>> 4];
/*  40 */       hexChars[j * 3 + 1] = HEX_ARRAY[v & 0xF];
/*  41 */       if (j < bytes.length - 1)
/*  42 */         hexChars[j * 3 + 2] = '-'; 
/*     */     } 
/*  44 */     return (new String(hexChars)).trim();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String base64ToHex(String base64) {
/*  53 */     byte[] decode = Base64.getDecoder().decode(base64);
/*  54 */     return bytesToHex(decode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] hexStringToByteArray(String s) {
/*  64 */     s = s.replace("-", "");
/*  65 */     int len = s.length();
/*  66 */     if (len % 2 != 0) {
/*  67 */       return new byte[0];
/*     */     }
/*     */     
/*  70 */     byte[] data = new byte[len / 2];
/*  71 */     for (int i = 0; i < len; i += 2) {
/*  72 */       data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
/*     */     }
/*  74 */     return data;
/*     */   }
/*     */   
/*     */   public static String hexStringToSeparatedHexString(String value) {
/*  78 */     if (StringUtils.isBlank(value)) {
/*  79 */       return "";
/*     */     }
/*     */     
/*  82 */     value = RegExUtils.removeAll(value, "[-\\s]");
/*  83 */     value = value.toUpperCase(Locale.ENGLISH);
/*     */     
/*  85 */     boolean isHex = value.matches("[0-9A-F]+");
/*  86 */     boolean isEven = (value.length() % 2 == 0);
/*  87 */     if (!isHex || !isEven) {
/*  88 */       throw new CEBASException("The string " + value + " is not a valid hex string and can not be converted.", "000541X");
/*     */     }
/*     */ 
/*     */     
/*  92 */     value = RegExUtils.replaceAll(value, "(.{2})", "$1-");
/*  93 */     value = StringUtils.stripEnd(value, "-");
/*     */     
/*  95 */     return value;
/*     */   }
/*     */   
/*     */   public boolean hexStringsEqual(String a, String b) {
/*  99 */     if (a == null && b == null) {
/* 100 */       return true;
/*     */     }
/* 102 */     if (a == null || b == null) {
/* 103 */       return false;
/*     */     }
/* 105 */     a = RegExUtils.removeAll(a, "[-\\s]");
/* 106 */     b = RegExUtils.removeAll(b, "[-\\s]");
/*     */     
/* 108 */     return a.equalsIgnoreCase(b);
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
/*     */   public static String omitLeadingZeros(String hexString) {
/* 120 */     if (hexString == null) {
/* 121 */       return null;
/*     */     }
/* 123 */     if ("00".equals(hexString)) {
/* 124 */       return hexString;
/*     */     }
/* 126 */     String[] split = hexString.split("-");
/* 127 */     boolean foundNonZero = false;
/* 128 */     if (split[0].equals("00")) {
/* 129 */       StringBuilder buffer = new StringBuilder();
/* 130 */       for (int i = 1; i < split.length; i++) {
/* 131 */         if (!split[i].equals("00") || !split[i - 1].equals("00") || foundNonZero) {
/*     */ 
/*     */           
/* 134 */           foundNonZero = true;
/* 135 */           if (i == split.length - 1) {
/* 136 */             buffer.append(split[i]);
/*     */           } else {
/* 138 */             buffer.append(split[i] + "-");
/*     */           } 
/*     */         } 
/*     */       } 
/* 142 */       return buffer.toString();
/*     */     } 
/* 144 */     return hexString;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String hexToBase64(String someValue) {
/* 149 */     return Base64.getEncoder().encodeToString(hexStringToByteArray(someValue));
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\HexUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */