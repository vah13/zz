/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.security;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.apache.commons.lang3.exception.ExceptionUtils;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.security.core.AuthenticationException;
/*    */ import org.springframework.security.oauth2.core.OAuth2Error;
/*    */ import org.springframework.security.oauth2.jwt.JwtValidationException;
/*    */ import org.springframework.security.web.DefaultRedirectStrategy;
/*    */ import org.springframework.security.web.RedirectStrategy;
/*    */ import org.springframework.security.web.authentication.AuthenticationFailureHandler;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class ZenZefiAuthenticationFailureHandler
/*    */   implements AuthenticationFailureHandler
/*    */ {
/* 23 */   private RedirectStrategy redirectStrategy = (RedirectStrategy)new DefaultRedirectStrategy();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
/* 29 */     Throwable rootCause = ExceptionUtils.getRootCause((Throwable)exception);
/* 30 */     if (rootCause.getClass().isAssignableFrom(JwtValidationException.class)) {
/*    */       
/* 32 */       Collection<OAuth2Error> errors = ((JwtValidationException)rootCause).getErrors();
/* 33 */       for (OAuth2Error oAuth2Error : errors) {
/* 34 */         if ("invavlid_clock_setting".equals(oAuth2Error.getErrorCode())) {
/* 35 */           this.redirectStrategy.sendRedirect(request, response, "/#/zenzefi/ui/error/401_clock");
/*    */           
/*    */           return;
/*    */         } 
/*    */       } 
/*    */     } 
/* 41 */     response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\security\ZenZefiAuthenticationFailureHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */