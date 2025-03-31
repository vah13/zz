/*     */ package com.daimler.cebas.system.control.startup;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.util.Random;
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
/*     */ public class ImageConversion
/*     */ {
/*     */   private static final int MASK = 1;
/*     */   private static final int ANTI_MASK = -2;
/*  23 */   private static final Random ra = new Random();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BufferedImage createRandomImage() {
/*  32 */     int width = 120;
/*  33 */     int height = 120;
/*  34 */     BufferedImage img = new BufferedImage(width, height, 1);
/*     */     
/*  36 */     for (int y = 0; y < height; y++) {
/*  37 */       for (int x = 0; x < width; x++) {
/*     */         
/*  39 */         int r = (int)(ra.nextDouble() * 256.0D);
/*  40 */         int g = (int)(ra.nextDouble() * 256.0D);
/*  41 */         int b = (int)(ra.nextDouble() * 256.0D);
/*     */         
/*  43 */         int rgb = 0xFF000000 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
/*     */         
/*  45 */         img.setRGB(x, y, rgb);
/*     */       } 
/*     */     } 
/*     */     
/*  49 */     return img;
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
/*     */   public static BufferedImage write(BufferedImage image, String t) {
/*  63 */     int x = 0;
/*  64 */     int y = 0;
/*  65 */     for (int i = 0; i < t.length(); i++) {
/*  66 */       int b = t.charAt(i);
/*  67 */       for (int j = 0; j < 8; j++) {
/*     */         
/*  69 */         int flag = b & 0x1;
/*  70 */         if (flag == 1) {
/*  71 */           if (x < image.getWidth()) {
/*  72 */             image.setRGB(x, y, image.getRGB(x, y) | 0x1);
/*  73 */             x++;
/*     */           } else {
/*  75 */             y++;
/*  76 */             x = 0;
/*  77 */             image.setRGB(x, y, image.getRGB(x, y) | 0x1);
/*  78 */             x++;
/*     */           }
/*     */         
/*  81 */         } else if (x < image.getWidth()) {
/*  82 */           image.setRGB(x, y, image.getRGB(x, y) & 0xFFFFFFFE);
/*     */           
/*  84 */           x++;
/*     */         } else {
/*  86 */           y++;
/*  87 */           x = 0;
/*  88 */           image.setRGB(x, y, image.getRGB(x, y) & 0xFFFFFFFE);
/*  89 */           x++;
/*     */         } 
/*     */         
/*  92 */         b >>= 1;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  97 */     return image;
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
/*     */   public static String read(BufferedImage image, int length) {
/* 110 */     int x = 0;
/* 111 */     int y = 0;
/*     */     
/* 113 */     char[] c = new char[length];
/* 114 */     for (int i = 0; i < length; i++) {
/* 115 */       int b = 0;
/*     */       
/* 117 */       for (int j = 0; j < 8; j++) {
/* 118 */         int flag; if (x < image.getWidth()) {
/* 119 */           flag = image.getRGB(x, y) & 0x1;
/* 120 */           x++;
/*     */         } else {
/* 122 */           x = 0;
/* 123 */           y++;
/* 124 */           flag = image.getRGB(0, y) & 0x1;
/* 125 */           x++;
/*     */         } 
/* 127 */         if (flag == 1) {
/* 128 */           b >>= 1;
/* 129 */           b |= 0x80;
/*     */         } else {
/* 131 */           b >>= 1;
/*     */         } 
/*     */       } 
/* 134 */       c[i] = (char)b;
/*     */     } 
/* 136 */     return new String(c);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\startup\ImageConversion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */