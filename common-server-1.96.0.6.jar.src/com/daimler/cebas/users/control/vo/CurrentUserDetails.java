/*     */ package com.daimler.cebas.users.control.vo;
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
/*     */ public class CurrentUserDetails
/*     */ {
/*     */   private String firstName;
/*     */   private String lastName;
/*     */   private String userName;
/*     */   private boolean isDefaultUser;
/*     */   private boolean isNewUser;
/*     */   private boolean checkLocalPassword;
/*     */   private boolean isAuthenticationAgainstBackend;
/*     */   private boolean isTransitionValid;
/*     */   
/*     */   public CurrentUserDetails(String firstName, String lastName, String userName, boolean isDefaultUser, boolean isNewUser, boolean checkLocalPassword, boolean isAuthenticationAgainstBackend, boolean isTransitionValid) {
/*  43 */     this.firstName = firstName;
/*  44 */     this.lastName = lastName;
/*  45 */     this.userName = userName;
/*  46 */     this.isDefaultUser = isDefaultUser;
/*  47 */     this.isNewUser = isNewUser;
/*  48 */     this.checkLocalPassword = checkLocalPassword;
/*  49 */     this.isAuthenticationAgainstBackend = isAuthenticationAgainstBackend;
/*  50 */     this.isTransitionValid = isTransitionValid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFirstName() {
/*  59 */     return this.firstName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLastName() {
/*  68 */     return this.lastName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserName() {
/*  77 */     return this.userName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDefaultUser() {
/*  86 */     return this.isDefaultUser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNewUser() {
/*  95 */     return this.isNewUser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCheckLocalPassword() {
/* 104 */     return this.checkLocalPassword;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAuthenticationAgainstBackend() {
/* 113 */     return this.isAuthenticationAgainstBackend;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTransitionValid() {
/* 122 */     return this.isTransitionValid;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\ceba\\users\control\vo\CurrentUserDetails.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */