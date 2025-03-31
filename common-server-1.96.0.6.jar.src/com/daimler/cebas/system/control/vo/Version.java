/*    */ package com.daimler.cebas.system.control.vo;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Version
/*    */ {
/*    */   private String serverVersion;
/*    */   private String apiVersion;
/*    */   private String build;
/*    */   private String system;
/*    */   
/*    */   public Version() {}
/*    */   
/*    */   public Version(String serverVersion, String apiVersion, String build, String system) {
/* 46 */     this.serverVersion = serverVersion;
/* 47 */     this.apiVersion = apiVersion;
/* 48 */     this.build = build;
/* 49 */     this.system = system;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getServerVersion() {
/* 58 */     return this.serverVersion;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getApiVersion() {
/* 67 */     return this.apiVersion;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getBuild() {
/* 76 */     return this.build;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSystem() {
/* 85 */     return this.system;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\vo\Version.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */