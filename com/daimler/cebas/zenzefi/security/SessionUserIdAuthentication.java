/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.security;
/*    */ 
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import java.util.Collection;
/*    */ import org.springframework.security.core.Authentication;
/*    */ import org.springframework.security.core.GrantedAuthority;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SessionUserIdAuthentication
/*    */   implements Authentication
/*    */ {
/*    */   private static final long serialVersionUID = 5589985233476109712L;
/*    */   
/*    */   public String getName() {
/* 22 */     return Session.getUserId();
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<? extends GrantedAuthority> getAuthorities() {
/* 27 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getCredentials() {
/* 32 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getDetails() {
/* 37 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getPrincipal() {
/* 42 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAuthenticated() {
/* 47 */     return false;
/*    */   }
/*    */   
/*    */   public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {}
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\security\SessionUserIdAuthentication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */