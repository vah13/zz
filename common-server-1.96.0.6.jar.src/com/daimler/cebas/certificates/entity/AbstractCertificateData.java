/*    */ package com.daimler.cebas.certificates.entity;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.security.cert.X509Certificate;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import org.bouncycastle.cert.X509AttributeCertificateHolder;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractCertificateData
/*    */ {
/*    */   protected transient X509Certificate cert;
/*    */   protected transient X509AttributeCertificateHolder attributesCertificateHolder;
/*    */   
/*    */   protected abstract Logger getLogger();
/*    */   
/*    */   protected void closeStream(InputStream stream) {
/*    */     try {
/* 44 */       if (stream != null) {
/* 45 */         stream.close();
/*    */       }
/* 47 */     } catch (IOException e) {
/* 48 */       getLogger().log(Level.FINEST, "Failed to close stream", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\entity\AbstractCertificateData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */