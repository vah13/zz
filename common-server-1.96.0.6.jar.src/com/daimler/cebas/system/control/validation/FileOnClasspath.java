/*    */ package com.daimler.cebas.system.control.validation;
/*    */ 
/*    */ import ch.qos.logback.core.PropertyDefinerBase;
/*    */ 
/*    */ public class FileOnClasspath
/*    */   extends PropertyDefinerBase {
/*    */   String path;
/*    */   
/*    */   public String getPath() {
/* 10 */     return this.path;
/*    */   }
/*    */   
/*    */   public void setPath(String path) {
/* 14 */     this.path = path;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPropertyValue() {
/* 19 */     if (resourceExists(FileOnClasspath.class.getClassLoader())) {
/* 20 */       return "true";
/*    */     }
/* 22 */     return "false";
/*    */   }
/*    */ 
/*    */   
/*    */   private boolean resourceExists(ClassLoader loader) {
/* 27 */     if (loader == null || this.path == null) {
/* 28 */       return false;
/*    */     }
/*    */     
/* 31 */     if (loader.getResource(this.path) != null) {
/* 32 */       return true;
/*    */     }
/*    */     
/* 35 */     return resourceExists(loader.getParent());
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\validation\FileOnClasspath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */