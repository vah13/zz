/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control.login;
/*     */ 
/*     */ import com.daimler.cebas.common.control.CEBASProperty;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*     */ import com.daimler.cebas.configuration.control.ConfigurationUtil;
/*     */ import com.daimler.cebas.configuration.entity.Configuration;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.control.vo.UserLoginRequest;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.CertificatesUpdaterFactory;
/*     */ import com.daimler.cebas.zenzefi.users.control.UserRegistrationEngine;
/*     */ import com.daimler.cebas.zenzefi.users.control.login.AbstractLoginStrategy;
/*     */ import com.daimler.cebas.zenzefi.users.control.login.LoginState;
/*     */ import com.daimler.cebas.zenzefi.users.control.login.XentryLoginAttemptsStrategy;
/*     */ import com.daimler.cebas.zenzefi.users.entity.ZenZefiUser;
/*     */ import java.util.Map;
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
/*     */ public class XentryLoginStrategy
/*     */   extends AbstractLoginStrategy
/*     */ {
/*     */   private static final String TRUE = "true";
/*     */   private UserRegistrationEngine userRegistrationEngine;
/*     */   private boolean userCreated;
/*     */   private XentryLoginAttemptsStrategy loginAttemptsStrategy;
/*     */   
/*     */   public XentryLoginStrategy(UserRegistrationEngine userRegistrationEngine, Logger logger, Session session, AbstractConfigurator configurator, MetadataManager i18n, CertificatesUpdaterFactory updaterFactory, XentryLoginAttemptsStrategy loginAttemptsStrategy) {
/*  69 */     super(logger, session, configurator, i18n, updaterFactory);
/*  70 */     this.userRegistrationEngine = userRegistrationEngine;
/*  71 */     this.loginAttemptsStrategy = loginAttemptsStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public User handleUserNotFound(UserLoginRequest someUser) {
/*  81 */     ZenZefiUser user = new ZenZefiUser();
/*  82 */     user.setUserPassword(someUser.getUserPassword());
/*  83 */     user.setUserName(someUser.getUserName());
/*  84 */     this.userCreated = true;
/*  85 */     return (User)this.userRegistrationEngine.register(user);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> createLoginResponse(LoginState loginState, boolean accountDeleted) {
/*  90 */     Map<String, String> createLoginResponse = super.createLoginResponse(loginState, accountDeleted);
/*  91 */     if (createLoginResponse.get("authenticated") != null && 
/*  92 */       this.loginAttemptsStrategy.isUserRecreatedDueToUsername()) {
/*  93 */       this.userCreated = true;
/*  94 */       this.loginAttemptsStrategy.setRecreatedUserIntoSession();
/*  95 */       createLoginResponse.put("authenticated", "true");
/*  96 */       buildLoginResponse(createLoginResponse);
/*     */     } 
/*     */ 
/*     */     
/* 100 */     return createLoginResponse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void buildLoginResponse(Map<String, String> response) {
/* 108 */     User currentUser = this.session.getCurrentUser();
/* 109 */     response.put("displayName", getUserIdForDisplay(currentUser));
/* 110 */     response.put("message", this.userCreated ? this.i18n.getMessage("loginUserStoreCreated", new String[] { currentUser.getUserName() }) : this.i18n
/* 111 */         .getMessage("loginStoreOpened", new String[] { currentUser.getUserName() }));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUpdateStartAfterLoginEnabled() {
/* 116 */     User currentUser = this.session.getCurrentUser();
/* 117 */     Configuration autoUpdate = ConfigurationUtil.getConfigurationForUser(currentUser, CEBASProperty.AUTO_CERT_UPDATE, this.logger, this.i18n);
/* 118 */     return (Boolean.valueOf(autoUpdate.getConfigValue()).booleanValue() && !this.session.isDefaultUser());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addStartAfterLoginProperty(Map<String, String> response) {
/* 123 */     response.put("startUpdateAfterLogin", "true");
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\login\XentryLoginStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */