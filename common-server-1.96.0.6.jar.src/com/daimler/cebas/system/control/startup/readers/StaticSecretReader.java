/*    */ package com.daimler.cebas.system.control.startup.readers;
/*    */ 
/*    */ import java.util.Properties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StaticSecretReader
/*    */   extends SecretReader
/*    */ {
/*    */   public static final int MAXIMUM_SECRET_BYTES = 2500;
/*    */   
/*    */   protected boolean overwriteHeader() {
/* 16 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int maximumSecretSize() {
/* 21 */     return 2500;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SecretMap getSecret() {
/* 26 */     return getSecret(SecretReader.class.getClassLoader().getResourceAsStream("daimler_logo.bmp"));
/*    */   }
/*    */   
/*    */   protected void prepare(Properties properties, SecretMap secret) {}
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\startup\readers\StaticSecretReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */