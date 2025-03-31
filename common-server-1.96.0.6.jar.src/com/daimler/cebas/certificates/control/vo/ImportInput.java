/*     */ package com.daimler.cebas.certificates.control.vo;
/*     */ 
/*     */ import java.io.InputStream;
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
/*     */ public class ImportInput
/*     */ {
/*     */   private InputStream stream;
/*     */   private byte[] bytes;
/*     */   private String fileName;
/*     */   private ImportInputType type;
/*     */   private String password;
/*     */   
/*     */   public ImportInput(InputStream stream, byte[] bytes, String fileName) {
/*  49 */     this(stream, bytes, fileName, null, ImportInputType.CERTIFICATE_FILE);
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
/*     */   public ImportInput(InputStream stream, byte[] bytes, String fileName, ImportInputType type) {
/*  67 */     this.stream = stream;
/*  68 */     this.bytes = bytes;
/*  69 */     this.fileName = fileName;
/*  70 */     this.type = type;
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
/*     */   public ImportInput(InputStream stream, byte[] bytes, String fileName, String password, ImportInputType type) {
/*  90 */     this.stream = stream;
/*  91 */     this.bytes = bytes;
/*  92 */     this.fileName = fileName;
/*  93 */     this.password = password;
/*  94 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getStream() {
/* 103 */     return this.stream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBytes() {
/* 112 */     return this.bytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPKCS12() {
/* 121 */     return (this.type == ImportInputType.PKCS12);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImportInputType getType() {
/* 130 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 139 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 148 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPassword() {
/* 157 */     return (this.password != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\ImportInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */