/*    */ package com.daimler.cebas.certificates.control.vo;
/*    */ 
/*    */ import io.swagger.annotations.ApiModel;
/*    */ import io.swagger.annotations.ApiModelProperty;
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
/*    */ @ApiModel
/*    */ public class LocalImportInput
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", required = true, value = "Path of the file to be imported. The file path has to provided with single slashes as separator, e.g. c:/folder/folder.ext - mind: single backslashes can not be used.")
/*    */   private String filePath;
/*    */   @ApiModelProperty(dataType = "java.lang.String", value = "The certificate password, if needed.")
/*    */   private String password;
/*    */   
/*    */   public LocalImportInput() {}
/*    */   
/*    */   public LocalImportInput(String filePath) {
/* 39 */     this.filePath = filePath;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LocalImportInput(String filePath, String password) {
/* 51 */     this.filePath = filePath;
/* 52 */     this.password = password;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getFilePath() {
/* 61 */     return this.filePath;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPassword() {
/* 70 */     return this.password;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasPassword() {
/* 79 */     return (this.password != null);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\vo\LocalImportInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */