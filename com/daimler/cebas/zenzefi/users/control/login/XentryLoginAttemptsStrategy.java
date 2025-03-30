/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control.login;
/*     */ 
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.zenzefi.users.control.DeleteUserAccountsEngine;
/*     */ import com.daimler.cebas.zenzefi.users.control.UserRegistrationEngine;
/*     */ import com.daimler.cebas.zenzefi.users.control.login.AbstractLoginAttemptsStrategy;
/*     */ import com.daimler.cebas.zenzefi.users.entity.ZenZefiUser;
/*     */ import java.util.logging.Level;
/*     */ import org.springframework.context.annotation.Profile;
/*     */ import org.springframework.web.context.annotation.RequestScope;
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
/*     */ @CEBASControl
/*     */ @Profile({"AFTERSALES"})
/*     */ @RequestScope
/*     */ public class XentryLoginAttemptsStrategy
/*     */   extends AbstractLoginAttemptsStrategy
/*     */ {
/*  34 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.users.control.login.XentryLoginAttemptsStrategy.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UserRegistrationEngine userRegistrationEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean userRecreatedDueToUsername = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ZenZefiUser userRecreated;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XentryLoginAttemptsStrategy(Logger logger, MetadataManager i18n, AbstractConfigurator configurator, Session session, DeleteUserAccountsEngine deleteUserAccountsEngine, UserRegistrationEngine userRegistrationEngine) {
/*  70 */     super(logger, i18n, configurator, session, deleteUserAccountsEngine);
/*  71 */     this.userRegistrationEngine = userRegistrationEngine;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleInvalidLoginAttempt(ZenZefiUser user, String inputPassword) {
/*  76 */     this.logger.logWithTranslation(Level.WARNING, "000215X", "loginFailedAccountDeleted", CLASS_NAME);
/*     */     
/*  78 */     this.deleteUserAccountsEngine.deleteAccNewTransc(user.getEntityId());
/*  79 */     this.userRecreated = createUser((User)user, inputPassword);
/*  80 */     this.userRegistrationEngine.register(this.userRecreated);
/*  81 */     this.logger.logWithTranslation(Level.INFO, "000036", "loginUserStoreCreated", new String[] { user
/*  82 */           .getUserName() }, CLASS_NAME);
/*  83 */     this.userRecreatedDueToUsername = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRecreatedUserIntoSession() {
/*  90 */     this.session.setCurrentUser((User)this.userRecreated, this.userRecreated.getUserPassword());
/*  91 */     this.session.setTransitionValid(true);
/*  92 */     this.session.setCheckLocalPassword(false);
/*  93 */     this.session.setBackendAuthenticatedUser(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ZenZefiUser createUser(User oldUser, String newPassword) {
/* 103 */     ZenZefiUser user = new ZenZefiUser();
/* 104 */     user.setUserName(oldUser.getUserName());
/* 105 */     user.setUserPassword(newPassword);
/* 106 */     user.setFirstName(oldUser.getFirstName());
/* 107 */     user.setLastName(oldUser.getLastName());
/* 108 */     user.setOrganisation(oldUser.getOrganisation());
/* 109 */     return user;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUserRecreatedDueToUsername() {
/* 118 */     return this.userRecreatedDueToUsername;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\login\XentryLoginAttemptsStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */