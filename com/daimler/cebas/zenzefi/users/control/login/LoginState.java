/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control.login;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LoginState
/*    */ {
/*    */   private Status status;
/*    */   private String message;
/*    */   
/*    */   private LoginState(Status status) {
/* 15 */     this.status = status;
/*    */   }
/*    */   
/*    */   public static com.daimler.cebas.zenzefi.users.control.login.LoginState authenticated() {
/* 19 */     return new com.daimler.cebas.zenzefi.users.control.login.LoginState(Status.AUTHENTICATED);
/*    */   }
/*    */   
/*    */   public static com.daimler.cebas.zenzefi.users.control.login.LoginState userDoesNotExist(String userName) {
/* 23 */     com.daimler.cebas.zenzefi.users.control.login.LoginState r = new com.daimler.cebas.zenzefi.users.control.login.LoginState(Status.USER_DOES_NOT_EXIST);
/* 24 */     r.message = userName;
/* 25 */     return r;
/*    */   }
/*    */   
/*    */   public static com.daimler.cebas.zenzefi.users.control.login.LoginState wrongPassword(String userName) {
/* 29 */     com.daimler.cebas.zenzefi.users.control.login.LoginState r = new com.daimler.cebas.zenzefi.users.control.login.LoginState(Status.WRONG_PASSWORD);
/* 30 */     r.message = userName;
/* 31 */     return r;
/*    */   }
/*    */   
/*    */   public static com.daimler.cebas.zenzefi.users.control.login.LoginState error(String errorMessage) {
/* 35 */     com.daimler.cebas.zenzefi.users.control.login.LoginState r = new com.daimler.cebas.zenzefi.users.control.login.LoginState(Status.ERROR);
/* 36 */     r.message = errorMessage;
/* 37 */     return r;
/*    */   }
/*    */   
/*    */   public boolean isAuthenticated() {
/* 41 */     return (this.status == Status.AUTHENTICATED);
/*    */   }
/*    */   
/*    */   public Status getStatus() {
/* 45 */     return this.status;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 49 */     return this.message;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\login\LoginState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */