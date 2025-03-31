/*    */ package com.daimler.cebas.users.control.vo;
/*    */ 
/*    */ public class UserDetailsWithSession
/*    */   extends CurrentUserDetails
/*    */ {
/*    */   private long remainingSessionSeconds;
/*    */   
/*    */   public UserDetailsWithSession(String firstName, String lastName, String userName, boolean isDefaultUser, boolean isNewUser, boolean checkLocalPassword, boolean isAuthenticationAgainstBackend, boolean isTransitionValid) {
/*  9 */     super(firstName, lastName, userName, isDefaultUser, isNewUser, checkLocalPassword, isAuthenticationAgainstBackend, isTransitionValid);
/*    */   }
/*    */ 
/*    */   
/*    */   public UserDetailsWithSession(String firstName, String lastName, String userName, boolean isDefaultUser, boolean isNewUser, boolean checkLocalPassword, boolean isAuthenticationAgainstBackend, boolean isTransitionValid, long remainingSessionSeconds) {
/* 14 */     super(firstName, lastName, userName, isDefaultUser, isNewUser, checkLocalPassword, isAuthenticationAgainstBackend, isTransitionValid);
/* 15 */     this.remainingSessionSeconds = remainingSessionSeconds;
/*    */   }
/*    */   
/*    */   public long getRemainingSessionSeconds() {
/* 19 */     return this.remainingSessionSeconds;
/*    */   }
/*    */   
/*    */   public void setRemainingSessionSeconds(long remainingSessionSeconds) {
/* 23 */     this.remainingSessionSeconds = remainingSessionSeconds;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\ceba\\users\control\vo\UserDetailsWithSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */