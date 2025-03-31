/*     */ package com.daimler.cebas.common.control;
/*     */ 
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.apache.commons.io.input.BoundedInputStream;
/*     */ import org.apache.commons.lang3.StringUtils;
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
/*     */ public final class HostUtil
/*     */ {
/*     */   private static final String EMPTY = "";
/*  29 */   private static final String CLASS_NAME = HostUtil.class.getSimpleName();
/*     */   
/*  31 */   private static final Logger LOG = Logger.getLogger(CLASS_NAME);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String HOSTNAME = "hostname";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String OS_NAME = "os.name";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String COMPUTERNAME = "COMPUTERNAME";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String WIN = "win";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String NUX = "nux";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String NIX = "nix";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getMacAddress(Logger logger) {
/*  76 */     String METHOD_NAME = "getMacAddress";
/*  77 */     logger.entering(CLASS_NAME, "getMacAddress");
/*     */     try {
/*  79 */       InetAddress ip = InetAddress.getLocalHost();
/*  80 */       NetworkInterface network = NetworkInterface.getByInetAddress(ip);
/*     */       
/*  82 */       if (network == null) {
/*  83 */         logger.exiting(CLASS_NAME, "getMacAddress");
/*  84 */         return "00-00-00-00-00-00";
/*     */       } 
/*  86 */       byte[] mac = network.getHardwareAddress();
/*  87 */       if (mac == null) {
/*  88 */         logger.exiting(CLASS_NAME, "getMacAddress");
/*  89 */         return "00-00-00-00-00-00";
/*     */       } 
/*  91 */       StringBuilder sb = new StringBuilder();
/*  92 */       for (int i = 0; i < mac.length; i++) {
/*  93 */         sb.append(String.format("%02X%s", new Object[] { Byte.valueOf(mac[i]), (i < mac.length - 1) ? "-" : "" }));
/*     */       } 
/*  95 */       logger.exiting(CLASS_NAME, "getMacAddress");
/*  96 */       return sb.toString();
/*     */     }
/*  98 */     catch (Exception e) {
/*  99 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 100 */       throw new CEBASException(e.getMessage());
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
/*     */   public static String getMachineName(Logger logger, MetadataManager i18n) {
/* 115 */     String METHOD_NAME = "getMachineName";
/* 116 */     logger.entering(CLASS_NAME, "getMachineName");
/* 117 */     logger.exiting(CLASS_NAME, "getMachineName");
/* 118 */     return getHostName(logger, i18n);
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
/*     */   private static String getHostName(Logger logger, MetadataManager i18n) {
/*     */     try {
/* 134 */       InetAddress addr = InetAddress.getLocalHost();
/* 135 */       String hostName = addr.getHostName();
/* 136 */       if (!StringUtils.isEmpty(hostName)) {
/* 137 */         logger.log(Level.INFO, "000218", i18n
/* 138 */             .getEnglishMessage("hostNameTakenFromInetAddr"), CLASS_NAME);
/* 139 */         return hostName;
/*     */       } 
/* 141 */       return getHostNameFromSystemVariables(logger, i18n);
/*     */     }
/* 143 */     catch (Exception e) {
/* 144 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 145 */       return getHostNameFromSystemVariables(logger, i18n);
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
/*     */   public static String getHostNameFromSystemVariables(Logger logger, MetadataManager i18n) {
/* 158 */     String hostName = "";
/*     */     
/*     */     try {
/* 161 */       String os = System.getProperty("os.name").toLowerCase();
/* 162 */       if (os.contains("win")) {
/* 163 */         hostName = System.getenv("COMPUTERNAME");
/* 164 */       } else if (os.contains("nux") || os.contains("nix")) {
/* 165 */         hostName = System.getenv("hostname".toUpperCase());
/*     */       } 
/* 167 */     } catch (SecurityException e) {
/* 168 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 169 */       return getHostFromNativeCommand(logger, i18n);
/*     */     } 
/*     */     
/* 172 */     if (!StringUtils.isEmpty(hostName)) {
/* 173 */       logger.log(Level.INFO, "000219", i18n
/* 174 */           .getEnglishMessage("hostNameTakenFromEnvVariables"), CLASS_NAME);
/* 175 */       return hostName;
/*     */     } 
/* 177 */     return getHostFromNativeCommand(logger, i18n);
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
/*     */   public static String getHostFromNativeCommand(Logger logger, MetadataManager i18n) {
/* 190 */     BoundedInputStream inputStream = null;
/* 191 */     BufferedReader reader = null;
/* 192 */     String hostName = "";
/*     */     try {
/* 194 */       Process process = Runtime.getRuntime().exec("hostname");
/* 195 */       inputStream = new BoundedInputStream(process.getInputStream(), 2000L);
/* 196 */       reader = new BufferedReader(new InputStreamReader((InputStream)inputStream));
/* 197 */       StringBuilder result = new StringBuilder();
/*     */       String line;
/* 199 */       while ((line = reader.readLine()) != null) {
/* 200 */         result.append(line);
/* 201 */         result.append(" ");
/*     */       } 
/* 203 */       hostName = result.toString().trim();
/* 204 */     } catch (IOException e) {
/* 205 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 206 */       logger.log(Level.WARNING, "000221X", i18n
/* 207 */           .getEnglishMessage("unableToRetrieveHostName", new String[] { e.getMessage() }), CLASS_NAME);
/*     */     } finally {
/*     */       
/* 210 */       if (reader != null) {
/*     */         try {
/* 212 */           reader.close();
/* 213 */         } catch (IOException e) {
/* 214 */           LOG.log(Level.FINEST, e.getMessage(), e);
/* 215 */           logger.log(Level.WARNING, "000221X", i18n
/* 216 */               .getEnglishMessage("unableToRetrieveHostName", new String[] { e.getMessage() }), CLASS_NAME);
/*     */         } 
/*     */       }
/*     */       
/* 220 */       if (inputStream != null) {
/*     */         try {
/* 222 */           inputStream.close();
/* 223 */         } catch (IOException e) {
/* 224 */           LOG.log(Level.FINEST, e.getMessage(), e);
/* 225 */           logger.log(Level.WARNING, "000221X", i18n
/* 226 */               .getEnglishMessage("unableToRetrieveHostName", new String[] { e.getMessage() }), CLASS_NAME);
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 232 */     if (!StringUtils.isEmpty(hostName)) {
/* 233 */       logger.log(Level.INFO, "000220", i18n
/* 234 */           .getEnglishMessage("hostNameTakenFromNativeCommand"), CLASS_NAME);
/*     */     } else {
/* 236 */       logger.log(Level.WARNING, "000221X", i18n
/* 237 */           .getEnglishMessage("unableToRetrieveHostName", new String[] { "" }), CLASS_NAME);
/*     */     } 
/*     */     
/* 240 */     return hostName;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\HostUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */