/*    */ package com.daimler.cebas.system.control.startup.readers;
/*    */ 
/*    */ import java.util.Properties;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SecretFactory
/*    */ {
/* 15 */   protected static final Log LOG = LogFactory.getLog(SecretFactory.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Properties properties;
/*    */ 
/*    */ 
/*    */   
/*    */   private SecretMap secret;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static SecretFactory create(Properties properties) {
/* 30 */     SecretFactory f = new SecretFactory();
/* 31 */     f.properties = properties;
/*    */     
/* 33 */     return f;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SecretFactory run(SecretReader s) {
/* 43 */     s.prepare(this.properties, this.secret);
/* 44 */     SecretMap newSecret = s.getSecret();
/* 45 */     join(newSecret);
/*    */     
/* 47 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SecretMap finish() {
/* 55 */     return this.secret;
/*    */   }
/*    */   
/*    */   private void join(SecretMap newSecret) {
/* 59 */     if (this.secret == null) {
/* 60 */       this.secret = newSecret;
/*    */     } else {
/* 62 */       this.secret.join(newSecret);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\startup\readers\SecretFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */