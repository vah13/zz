/*    */ package com.daimler.cebas.users.control.vo;
/*    */ 
/*    */ import com.daimler.cebas.users.entity.User;
/*    */ import org.apache.commons.lang3.StringUtils;
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
/*    */ public class UserData
/*    */ {
/*    */   private final String id;
/*    */   private final String userName;
/*    */   private final String name;
/*    */   
/*    */   public UserData(User user) {
/* 36 */     this.userName = user.getUserName();
/* 37 */     this.id = user.getEntityId();
/* 38 */     this
/* 39 */       .name = StringUtils.defaultString(user.getFirstName()) + " " + StringUtils.defaultString(user.getLastName());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getId() {
/* 49 */     return this.id;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 58 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUserName() {
/* 67 */     return this.userName;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\ceba\\users\control\vo\UserData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */