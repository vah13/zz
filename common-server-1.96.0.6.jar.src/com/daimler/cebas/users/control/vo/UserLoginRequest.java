/*    */ package com.daimler.cebas.users.control.vo;
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
/*    */ @ApiModel
/*    */ public class UserLoginRequest
/*    */ {
/*    */   @ApiModelProperty(dataType = "java.lang.String", required = true, value = "The user name. Length between 1 and 7 characters. Must be alpha numeric, non blank.")
/*    */   private String userName;
/*    */   @ApiModelProperty(dataType = "java.lang.String", required = true, value = "The user password as base64 encoded UTF-8 string. Length of the undecoded string must be between 9 and 100 characters. Must contain upper case, lower case, digit, and special characters.")
/*    */   private String userPassword;
/*    */   
/*    */   public String getUserName() {
/* 34 */     return this.userName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUserPassword() {
/* 43 */     return this.userPassword;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\ceba\\users\control\vo\UserLoginRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */