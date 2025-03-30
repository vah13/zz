/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.security;
/*    */ 
/*    */ import com.daimler.cebas.zenzefi.configuration.control.util.ZenZefiPkiAndOidcPropertiesManager;
/*    */ import com.daimler.cebas.zenzefi.users.control.UserContext;
/*    */ import java.io.IOException;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.security.core.Authentication;
/*    */ import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
/*    */ import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class ZenZefiLogoutSuccessHandler
/*    */   extends AbstractAuthenticationTargetUrlRequestHandler
/*    */   implements LogoutSuccessHandler
/*    */ {
/*    */   @Autowired
/*    */   private ZenZefiPkiAndOidcPropertiesManager pkiAndOAuthPropertiesManager;
/*    */   @Autowired
/*    */   private UserContext userContext;
/*    */   
/*    */   public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
/* 29 */     if (this.userContext.getSession().isOnlineLogout().booleanValue()) {
/* 30 */       handle(request, response, authentication);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
/* 36 */     return this.pkiAndOAuthPropertiesManager.getPkiLogoutUrl();
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\security\ZenZefiLogoutSuccessHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */