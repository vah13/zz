/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control.login;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*     */ import com.daimler.cebas.common.control.CEBASProperty;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*     */ import com.daimler.cebas.configuration.control.ConfigurationUtil;
/*     */ import com.daimler.cebas.configuration.entity.Configuration;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.control.vo.UserLoginRequest;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.CertificatesUpdaterFactory;
/*     */ import com.daimler.cebas.zenzefi.users.control.login.LoginState;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
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
/*     */ public abstract class AbstractLoginStrategy
/*     */ {
/*     */   public static final String START_UPDATE_AFTER_LOGIN = "startUpdateAfterLogin";
/*     */   protected static final String AUTHENTICATED = "authenticated";
/*     */   protected static final String AUTHENTICATED_AGAINST_BACKEND = "authenticatedAgainstBackend";
/*     */   protected static final String AUTO_UPDATE_CERTIFICATES = "autoUpdateCertificates";
/*     */   protected static final String USER_NAME = "userName";
/*     */   protected Logger logger;
/*     */   protected Session session;
/*     */   protected AbstractConfigurator configurator;
/*     */   protected MetadataManager i18n;
/*     */   private CertificatesUpdaterFactory updaterFactory;
/*     */   
/*     */   public AbstractLoginStrategy(Logger logger, Session session, AbstractConfigurator configurator, MetadataManager i18n, CertificatesUpdaterFactory updaterFactory) {
/*  87 */     this.logger = logger;
/*  88 */     this.session = session;
/*  89 */     this.configurator = configurator;
/*  90 */     this.i18n = i18n;
/*  91 */     this.updaterFactory = updaterFactory;
/*     */   }
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
/*     */   public Map<String, String> createLoginResponse(LoginState loginState, boolean accountDeleted) {
/*     */     boolean autoUpdateCertificates;
/* 123 */     Map<String, String> response = new HashMap<>();
/*     */     
/* 125 */     List<Configuration> configurations = this.session.getCurrentUser().getConfigurations();
/*     */ 
/*     */     
/* 128 */     Optional<Configuration> registerdUserAutoCertConfigOptional = configurations.stream().filter(config -> config.getConfigKey().equalsIgnoreCase(CEBASProperty.AUTO_CERT_UPDATE.name())).findFirst();
/* 129 */     if (registerdUserAutoCertConfigOptional.isPresent()) {
/* 130 */       autoUpdateCertificates = Boolean.valueOf(((Configuration)registerdUserAutoCertConfigOptional.get()).getConfigValue()).booleanValue();
/*     */     } else {
/* 132 */       autoUpdateCertificates = Boolean.valueOf(this.configurator.readProperty(CEBASProperty.AUTO_CERT_UPDATE.name())).booleanValue();
/*     */     } 
/* 134 */     switch (null.$SwitchMap$com$daimler$cebas$zenzefi$users$control$login$LoginState$Status[loginState.getStatus().ordinal()]) {
/*     */       
/*     */       case 1:
/* 137 */         response.put("message", this.i18n
/* 138 */             .getMessage("loginUserDoesNotExist", new String[] { loginState.getMessage() }));
/*     */         break;
/*     */       case 2:
/* 141 */         response.put("message", this.i18n
/* 142 */             .getMessage("loginWrongPassword", new String[] { loginState.getMessage() }));
/*     */         break;
/*     */       case 3:
/* 145 */         response.put("message", this.i18n
/* 146 */             .getMessage("loginErrorOccurred", new String[] { loginState.getMessage() }));
/*     */         break;
/*     */       case 4:
/* 149 */         buildLoginResponse(response);
/*     */         break;
/*     */     } 
/*     */     
/* 153 */     response.put("accountDeleted", String.valueOf(accountDeleted));
/* 154 */     response.put("authenticated", String.valueOf(loginState.isAuthenticated()));
/* 155 */     response.put("authenticatedAgainstBackend", String.valueOf((this.session.getBackendAuthenticatedUser() != null)));
/* 156 */     response.put("autoUpdateCertificates", String.valueOf(autoUpdateCertificates));
/* 157 */     response.put("userName", this.session.getCurrentUser().getUserName());
/* 158 */     addStartAfterLoginProperty(response);
/* 159 */     return response;
/*     */   }
/*     */   
/*     */   protected void addStartAfterLoginProperty(Map<String, String> response) {
/* 163 */     if (this.session.isDefaultUser()) {
/*     */       return;
/*     */     }
/* 166 */     response.put("startUpdateAfterLogin", "" + isUpdateStartAfterLoginEnabled());
/*     */   }
/*     */   
/*     */   protected String getDisplayName(User currentUser) {
/* 170 */     return currentUser.getFirstName() + " " + currentUser.getLastName() + getUserIdForDisplay(currentUser);
/*     */   }
/*     */   
/*     */   protected String getUserIdForDisplay(User currentUser) {
/* 174 */     return "(ID: " + currentUser.getUserName() + ")";
/*     */   }
/*     */   
/*     */   protected boolean isUpdateStartAfterLoginEnabled() {
/* 178 */     User currentUser = this.session.getCurrentUser();
/* 179 */     Configuration autoUpdate = ConfigurationUtil.getConfigurationForUser(currentUser, CEBASProperty.AUTO_CERT_UPDATE, this.logger, this.i18n);
/*     */     
/* 181 */     return (Boolean.valueOf(autoUpdate.getConfigValue()).booleanValue() && !this.session.isDefaultUser() && this.session
/* 182 */       .getBackendAuthenticatedUser() != null && !this.session.isCheckLocalPassword());
/*     */   }
/*     */   
/*     */   public void startDifferentialUpdate(boolean authenticated, Map<String, String> response) {
/* 186 */     if (authenticated && response.get("startUpdateAfterLogin") != null && ((String)response.get("startUpdateAfterLogin")).equals("true"))
/* 187 */       this.updaterFactory.getUpdateCertificatesInstance(UpdateType.DIFFERENTIAL).updateCertificates(); 
/*     */   }
/*     */   
/*     */   public abstract User handleUserNotFound(UserLoginRequest paramUserLoginRequest);
/*     */   
/*     */   abstract void buildLoginResponse(Map<String, String> paramMap);
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\login\AbstractLoginStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */