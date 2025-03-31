/*    */ package com.daimler.cebas.system.control.startup.readers;
/*    */ 
/*    */ import com.daimler.cebas.system.control.exceptions.ConfigurationCheckException;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import javax.imageio.ImageIO;
/*    */ import org.apache.commons.lang3.RandomStringUtils;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class ReaderUtils
/*    */ {
/*    */   public static final String SECRET_VALID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?";
/*    */   public static final int SECRET_LENGTH = 32;
/* 25 */   private static final Log LOG = LogFactory.getLog(ReaderUtils.class);
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
/*    */   public static byte[] getBytesFromImage(BufferedImage image, boolean overwriteHeaders) {
/* 38 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*    */     try {
/* 40 */       ImageIO.write(image, "bmp", baos);
/* 41 */       baos.flush();
/* 42 */       byte[] imageInByte = baos.toByteArray();
/* 43 */       baos.close();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 49 */       if (overwriteHeaders) {
/* 50 */         imageInByte[0] = 0;
/* 51 */         imageInByte[1] = 1;
/*    */       } 
/*    */       
/* 54 */       return imageInByte;
/* 55 */     } catch (IOException e) {
/* 56 */       LOG.error(e);
/* 57 */       throw new ConfigurationCheckException("It was not possible to read data from the secret store: " + e
/* 58 */           .getMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String createNewRandomSecretString() {
/* 68 */     String password = RandomStringUtils.random(32, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?");
/*    */     
/* 70 */     if (!isValidPassword(password)) {
/* 71 */       password = createNewRandomSecretString();
/*    */     }
/* 73 */     return password;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isValidPassword(String password) {
/* 83 */     boolean hasUppercase = !password.equals(password.toLowerCase());
/* 84 */     boolean hasLowercase = !password.equals(password.toUpperCase());
/* 85 */     boolean hasSpecial = !password.matches("[A-Za-z0-9]*");
/* 86 */     boolean hasNumeric = password.matches(".*\\d+.*");
/*    */     
/* 88 */     if (!hasUppercase || !hasLowercase || !hasSpecial || !hasNumeric) {
/* 89 */       return false;
/*    */     }
/* 91 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\startup\readers\ReaderUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */