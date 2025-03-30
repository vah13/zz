/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control.login;
/*    */ 
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import com.daimler.cebas.users.control.vo.UserLoginRequest;
/*    */ import com.daimler.cebas.users.entity.User;
/*    */ import com.daimler.cebas.zenzefi.certificates.control.update.CertificatesUpdaterFactory;
/*    */ import com.daimler.cebas.zenzefi.users.control.login.AbstractLoginStrategy;
/*    */ import java.util.Map;
/*    */ import java.util.logging.Level;
/*    */ import org.springframework.context.annotation.Profile;
/*    */ import org.springframework.web.util.HtmlUtils;
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
/*    */ @Profile({"!AFTERSALES"})
/*    */ public class DefaultLoginStrategy
/*    */   extends AbstractLoginStrategy
/*    */ {
/* 32 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.users.control.login.DefaultLoginStrategy.class.getSimpleName();
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
/*    */   public DefaultLoginStrategy(Logger logger, Session session, AbstractConfigurator configurator, MetadataManager i18n, CertificatesUpdaterFactory updaterFactory) {
/* 50 */     super(logger, session, configurator, i18n, updaterFactory);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public User handleUserNotFound(UserLoginRequest someUser) {
/* 61 */     this.logger.logWithTranslation(Level.INFO, "000040", "unknownUser", new String[] {
/* 62 */           HtmlUtils.htmlEscape(someUser.getUserName()) }, CLASS_NAME);
/* 63 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void buildLoginResponse(Map<String, String> response) {
/* 74 */     User currentUser = this.session.getCurrentUser();
/* 75 */     String displayName = getDisplayName(currentUser);
/* 76 */     response.put("message", this.i18n.getMessage("loginSuccessful"));
/* 77 */     response.put("displayName", displayName);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\login\DefaultLoginStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */