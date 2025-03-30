/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control.login;
/*    */ 
/*    */ import com.daimler.cebas.common.control.CEBASProperty;
/*    */ import com.daimler.cebas.common.control.MetadataManager;
/*    */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*    */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import com.daimler.cebas.users.control.exceptions.UserException;
/*    */ import com.daimler.cebas.zenzefi.users.control.DeleteUserAccountsEngine;
/*    */ import com.daimler.cebas.zenzefi.users.control.login.AbstractLoginAttemptsStrategy;
/*    */ import com.daimler.cebas.zenzefi.users.entity.ZenZefiUser;
/*    */ import java.util.logging.Level;
/*    */ import org.springframework.context.annotation.Profile;
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
/*    */ @Profile({"!AFTERSALES"})
/*    */ public class DefaultLoginAttemptsStrategy
/*    */   extends AbstractLoginAttemptsStrategy
/*    */ {
/* 34 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.users.control.login.DefaultLoginAttemptsStrategy.class
/* 35 */     .getSimpleName();
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
/*    */   public DefaultLoginAttemptsStrategy(Logger logger, MetadataManager i18n, AbstractConfigurator configurator, Session session, DeleteUserAccountsEngine deleteUserAccountsEngine) {
/* 54 */     super(logger, i18n, configurator, session, deleteUserAccountsEngine);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleInvalidLoginAttempt(ZenZefiUser user, String inputPassword) {
/* 60 */     this.logger.log(Level.INFO, "000212X", this.i18n
/* 61 */         .getEnglishMessage("userLoginPasswordsNotMatch", new String[] {
/*    */             
/* 63 */             user.getUserName()
/*    */           }), CLASS_NAME);
/* 65 */     int numberOfFailedLogins = Integer.parseInt(this.configurator.readProperty(CEBASProperty.NUMBER_OF_FAILED_LOGIN_ATTEMPTS
/* 66 */           .name()));
/*    */ 
/*    */     
/* 69 */     int failedUserLoginAttempts = (user.getNumberOfFailedLoginAttempts() == null) ? 0 : user.getNumberOfFailedLoginAttempts().intValue();
/* 70 */     failedUserLoginAttempts++;
/* 71 */     if (failedUserLoginAttempts >= numberOfFailedLogins) {
/* 72 */       this.session.setBackendAuthenticatedUser(null);
/* 73 */       this.session.setToken(null);
/* 74 */       this.deleteUserAccountsEngine.deleteAccount(user.getEntityId());
/* 75 */       this.logger.logWithTranslation(Level.WARNING, "000215X", "loginFailedAccountDeleted", CLASS_NAME);
/*    */ 
/*    */ 
/*    */       
/* 79 */       this.session.setTransitionValid(true);
/* 80 */       throw new UserException(this.i18n.getMessage("loginFailedAccountDeleted"));
/*    */     } 
/*    */     
/* 83 */     this.session.setTransitionValid(true);
/* 84 */     user.setNumberOfFailedLoginAttempts(Integer.valueOf(failedUserLoginAttempts));
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\login\DefaultLoginAttemptsStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */