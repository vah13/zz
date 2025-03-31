/*     */ package com.daimler.cebas.system.control.startup.readers;
/*     */ 
/*     */ import com.daimler.cebas.system.control.exceptions.ConfigurationCheckException;
/*     */ import com.daimler.cebas.system.control.startup.ImageConversion;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Properties;
/*     */ import javax.imageio.ImageIO;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SecretReader
/*     */ {
/*  27 */   protected static final Log LOG = LogFactory.getLog(SecretReader.class);
/*     */ 
/*     */   
/*     */   protected abstract void prepare(Properties paramProperties, SecretMap paramSecretMap);
/*     */   
/*     */   protected abstract SecretMap getSecret();
/*     */   
/*     */   protected abstract boolean overwriteHeader();
/*     */   
/*     */   protected abstract int maximumSecretSize();
/*     */   
/*     */   protected SecretMap getSecret(File file) {
/*     */     try {
/*  40 */       byte[] bytes = FileUtils.readFileToByteArray(file);
/*  41 */       return getSecret(bytes);
/*  42 */     } catch (IOException e) {
/*  43 */       LOG.error(e);
/*  44 */       throw new ConfigurationCheckException("Invalid DB cache file. Please delete: " + file + " and retry. " + e
/*  45 */           .getMessage());
/*  46 */     } catch (NullPointerException e) {
/*  47 */       LOG.error(e);
/*  48 */       throw new ConfigurationCheckException("Invalid DB cache file. Please delete: " + file + " and retry. ");
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SecretMap getSecret(InputStream stream) {
/*     */     try {
/*  54 */       byte[] bytes = IOUtils.toByteArray(stream);
/*  55 */       return getSecret(bytes);
/*  56 */     } catch (IOException e) {
/*  57 */       LOG.error(e);
/*  58 */       throw new ConfigurationCheckException("Invalid configuration. Please try to reinstall. " + e.getMessage());
/*     */     } finally {
/*     */       try {
/*  61 */         stream.close();
/*  62 */       } catch (IOException e) {
/*  63 */         LOG.debug("Secret stream was not closed properly");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private SecretMap getSecret(byte[] bytes) {
/*  69 */     BufferedImage image = getImageFromBytes(bytes);
/*  70 */     if (image == null || image.getWidth() == 0 || image.getHeight() == 0) {
/*  71 */       throw new ConfigurationCheckException("Invalid DB cache file. Please delete, try to reinstall and retry. ");
/*     */     }
/*  73 */     String read = ImageConversion.read(image, maximumSecretSize());
/*  74 */     return SecretMap.fromString(read);
/*     */   }
/*     */   
/*     */   protected final boolean fileExists(File file) {
/*  78 */     return (!file.isDirectory() && file.exists());
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void createFile(File file, byte[] secret) {
/*     */     try {
/*  84 */       FileUtils.writeByteArrayToFile(file, secret);
/*  85 */     } catch (IOException e) {
/*  86 */       LOG.error(e);
/*  87 */       throw new ConfigurationCheckException("Could not create the file: " + file + " - reason: " + e
/*  88 */           .getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final byte[] hideSecret(SecretMap secret, BufferedImage image) {
/*  94 */     String secretText = secret.createString(maximumSecretSize());
/*     */     
/*  96 */     BufferedImage secretImage = ImageConversion.write(image, secretText);
/*  97 */     return ReaderUtils.getBytesFromImage(secretImage, overwriteHeader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final BufferedImage getImageFromBytes(byte[] imageInByte) {
/*     */     try {
/* 106 */       if (overwriteHeader()) {
/* 107 */         imageInByte[0] = 66;
/* 108 */         imageInByte[1] = 77;
/*     */       } 
/*     */       
/* 111 */       InputStream in = new ByteArrayInputStream(imageInByte);
/* 112 */       return ImageIO.read(in);
/*     */     }
/* 114 */     catch (IOException e) {
/* 115 */       LOG.error(e);
/* 116 */       throw new ConfigurationCheckException("It was not possible to write data to the property store: " + e
/* 117 */           .getMessage());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\startup\readers\SecretReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */