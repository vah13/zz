/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control.idle;
/*    */ 
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import com.daimler.cebas.zenzefi.users.control.idle.UserIdleTimer;
/*    */ import org.aspectj.lang.JoinPoint;
/*    */ import org.aspectj.lang.annotation.After;
/*    */ import org.aspectj.lang.annotation.Aspect;
/*    */ import org.aspectj.lang.annotation.Before;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Aspect
/*    */ @Component
/*    */ public class AspectUserInteraction
/*    */ {
/* 23 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.users.control.idle.AspectUserInteraction.class.getSimpleName();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   private UserIdleTimer userIdleTimer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   private Logger logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   private Session session;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Before("execution(* com.daimler.cebas.zenzefi.configuration.boundary.ConfigurationsResource.*(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResource.*(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV1.*(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV2.*(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV3.*(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenzefiCertificatesResource.*(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV1.*(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV2.*(..)) || execution(* com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV3.*(..)) || execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResource.resetSessionTimeout(..)) || execution(* com.daimler.cebas.zenzefi.logs.boundary.LogsResource.*(..)) || execution(* com.daimler.cebas.zenzefi.logs.boundary.LogsResourceV1.*(..)) || execution(* com.daimler.cebas.zenzefi.logs.boundary.LogsResourceV2.*(..)) || execution(* com.daimler.cebas.zenzefi.logs.boundary.LogsResourceV3.*(..))")
/*    */   public void resetTimerOnUserInteraction(JoinPoint joinPoint) {
/* 64 */     String METHOD_NAME = "resetTimerOnUserInteraction";
/* 65 */     this.logger.entering(CLASS_NAME, METHOD_NAME);
/* 66 */     if (this.userIdleTimer.isLoggedIn()) {
/* 67 */       this.userIdleTimer.reset();
/*    */     }
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
/*    */   @After("execution(* com.daimler.cebas.zenzefi.users.control.UserService.login(..)) ||execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResource.login(..)) || execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResource.oauthRegister(..)) || execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResourceV1.loginV1(..)) || execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResourceV2.loginV2(..)) || execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResourceV3.loginV3(..))")
/*    */   public void setLoginState() {
/* 81 */     String METHOD_NAME = "resetTimerAfterLogin";
/* 82 */     this.logger.entering(CLASS_NAME, "resetTimerAfterLogin");
/* 83 */     this.userIdleTimer.setLoggedIn(!this.session.isDefaultUser());
/* 84 */     this.logger.exiting(CLASS_NAME, "resetTimerAfterLogin");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @After("execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResource.logoutUser(..)) || execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResourceV1.logoutUserV1(..)) || execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResourceV2.logoutUserV2(..)) || execution(* com.daimler.cebas.zenzefi.users.boundary.UsersResourceV3.logoutUserV3(..)) || execution(* com.daimler.cebas.zenzefi.users.integration.ZenZefiLogoutHandler.logout(..))")
/*    */   public void setLogoffState() {
/* 96 */     String METHOD_NAME = "stopTimerAfterLogout";
/* 97 */     this.logger.entering(CLASS_NAME, "stopTimerAfterLogout");
/* 98 */     this.userIdleTimer.setLoggedIn(false);
/* 99 */     this.logger.exiting(CLASS_NAME, "stopTimerAfterLogout");
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\idle\AspectUserInteraction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */