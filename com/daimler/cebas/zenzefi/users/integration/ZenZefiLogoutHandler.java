/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.integration;
/*    */ 
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.users.control.exceptions.UserException;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*    */ import com.daimler.cebas.zenzefi.system.control.validation.FullZenZefiConfigurationChecker;
/*    */ import com.daimler.cebas.zenzefi.users.control.UserService;
/*    */ import java.io.IOException;
/*    */ import java.util.Date;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import javax.servlet.http.Cookie;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.security.core.Authentication;
/*    */ import org.springframework.security.core.context.SecurityContextHolder;
/*    */ import org.springframework.security.web.authentication.logout.LogoutHandler;
/*    */ import org.springframework.web.context.annotation.RequestScope;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CEBASControl
/*    */ @RequestScope
/*    */ public class ZenZefiLogoutHandler
/*    */   implements LogoutHandler
/*    */ {
/* 35 */   private static final Logger LOG = Logger.getLogger(FullZenZefiConfigurationChecker.class.getName());
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   protected Logger logger;
/*    */ 
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   private UpdateSession updateSession;
/*    */ 
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   private UserService userService;
/*    */ 
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   protected MetadataManager i18n;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
/* 62 */     long currentTimeStamp = (new Date()).getTime();
/* 63 */     if (this.updateSession.isRunning() || this.updateSession.isPaused() || (currentTimeStamp - this.updateSession
/* 64 */       .getLastStopTimestamp()) / 1000L < 3L) {
/* 65 */       this.logger.log(Level.WARNING, "000282", this.i18n
/* 66 */           .getEnglishMessage("updateOperationNotAllowed"), 
/* 67 */           getClass().getSimpleName());
/* 68 */       updateRunningResponse(httpServletResponse);
/*    */     } else {
/*    */       
/* 71 */       SecurityContextHolder.clearContext();
/* 72 */       Cookie[] cookies = httpServletRequest.getCookies();
/* 73 */       if (cookies != null) {
/* 74 */         for (Cookie cookie : cookies) {
/* 75 */           cookie.setMaxAge(0);
/*    */         }
/*    */       }
/*    */       
/* 79 */       this.userService.oauthLogout(authentication);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void updateRunningResponse(HttpServletResponse httpServletResponse) {
/*    */     try {
/* 91 */       httpServletResponse.setStatus(406);
/* 92 */       httpServletResponse.getWriter()
/* 93 */         .write(this.i18n.getEnglishMessage("updateOperationNotAllowed"));
/* 94 */       httpServletResponse.getWriter().flush();
/* 95 */     } catch (IOException e) {
/* 96 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 97 */       throw new UserException(e.getMessage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\integration\ZenZefiLogoutHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */