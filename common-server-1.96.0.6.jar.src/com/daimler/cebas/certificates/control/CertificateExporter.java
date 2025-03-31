/*    */ package com.daimler.cebas.certificates.control;
/*    */ 
/*    */ import java.util.Formatter;
/*    */ import org.apache.commons.lang3.StringUtils;
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
/*    */ public class CertificateExporter
/*    */ {
/*    */   public static String exportToConfigurator5Format(byte[] cert) {
/* 29 */     Formatter formatter = new Formatter();
/* 30 */     for (byte b : cert) {
/* 31 */       formatter.format("%02x", new Object[] { Byte.valueOf(b) });
/*    */     } 
/* 33 */     String hexFormat = formatter.toString();
/* 34 */     formatter.close();
/* 35 */     return hexFormat;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String exportToCArrayFormat(byte[] cert) {
/* 46 */     StringBuilder stringBuilder = new StringBuilder();
/* 47 */     stringBuilder.append("unsigned char DATA[" + cert.length + "] = " + 
/* 48 */         System.lineSeparator() + "{");
/* 49 */     for (int k = 0; k < cert.length - 1; k++) {
/*    */       
/* 51 */       String value = ((k % 22 == 0) ? System.lineSeparator() : "") + "0x" + String.format("%02X", new Object[] { Byte.valueOf(cert[k]) }) + ", ";
/* 52 */       stringBuilder.append(value);
/*    */     } 
/* 54 */     stringBuilder.append((((cert.length - 1) % 22 == 0) ? 
/* 55 */         System.lineSeparator() : "") + "0x" + 
/* 56 */         String.format("%02X ", new Object[] { Byte.valueOf(cert[cert.length - 1])
/* 57 */           }) + System.lineSeparator() + "};");
/* 58 */     return stringBuilder.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String exportToCANoeFormat(byte[] cert) {
/* 69 */     StringBuilder stringBuilder = new StringBuilder();
/* 70 */     for (byte certByte : cert) {
/* 71 */       stringBuilder.append("0x").append(String.format("%02X", new Object[] { Byte.valueOf(certByte) })).append(" ");
/*    */     } 
/* 73 */     return StringUtils.chop(stringBuilder.toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\CertificateExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */