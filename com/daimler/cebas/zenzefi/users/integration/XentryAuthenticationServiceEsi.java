/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.integration;
/*    */ 
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.zenzefi.configuration.control.util.ZenZefiPkiAndOidcPropertiesManager;
/*    */ import com.daimler.cebas.zenzefi.users.entity.ZenZefiUser;
/*    */ import com.daimler.cebas.zenzefi.users.integration.AuthenticationServiceEsi;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.context.annotation.Profile;
/*    */ import org.springframework.security.core.Authentication;
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
/*    */ @CEBASControl
/*    */ @Profile({"AFTERSALES"})
/*    */ public class XentryAuthenticationServiceEsi
/*    */   implements AuthenticationServiceEsi
/*    */ {
/*    */   @Autowired
/*    */   public XentryAuthenticationServiceEsi(Logger logger, ZenZefiPkiAndOidcPropertiesManager pkiAndOAuthPropertiesManager) {}
/*    */   
/*    */   public ZenZefiUser backendLogin(HttpServletRequest request) {
/* 44 */     throw new IllegalStateException("Illegal method for xentry.");
/*    */   }
/*    */   
/*    */   public void revokeToken(Authentication authentication) {}
/*    */   
/*    */   public void revokeToken(String accessToken) {}
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\integration\XentryAuthenticationServiceEsi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */