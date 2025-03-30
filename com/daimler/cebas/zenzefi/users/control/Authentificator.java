/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control;
/*     */ 
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.crypto.UserCryptoEngine;
/*     */ import com.daimler.cebas.users.control.vo.UserLoginRequest;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.zenzefi.users.control.login.LoginState;
/*     */ import com.daimler.cebas.zenzefi.users.entity.ZenZefiUser;
/*     */ import com.daimler.cebas.zenzefi.users.integration.AuthenticationServiceEsi;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.security.core.Authentication;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class Authentificator
/*     */ {
/*  31 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.users.control.Authentificator.class.getName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UserCryptoEngine cryptoEngine;
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractConfigurator configurator;
/*     */ 
/*     */ 
/*     */   
/*     */   private AuthenticationServiceEsi authenticationServiceEsi;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public Authentificator(UserCryptoEngine userCryptoEngine, AbstractConfigurator configurator, AuthenticationServiceEsi authenticationServiceEsi) {
/*  57 */     this.cryptoEngine = userCryptoEngine;
/*  58 */     this.configurator = configurator;
/*  59 */     this.authenticationServiceEsi = authenticationServiceEsi;
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
/*     */   public LoginState authentify(User findUser, UserLoginRequest someUser) {
/*  71 */     String METHOD_NAME = "authentify";
/*  72 */     this.logger.entering(CLASS_NAME, "authentify");
/*  73 */     this.logger.exiting(CLASS_NAME, "authentify");
/*  74 */     if (findUser.getUserName().equalsIgnoreCase(someUser.getUserName()) && 
/*  75 */       checkPasswordValidity(findUser, someUser)) {
/*  76 */       return LoginState.authenticated();
/*     */     }
/*  78 */     return LoginState.wrongPassword(someUser.getUserName());
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
/*     */   public ZenZefiUser backendLogin(HttpServletRequest request) {
/*  90 */     return this.authenticationServiceEsi.backendLogin(request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void revokeToken(Authentication authentication) {
/*  99 */     this.authenticationServiceEsi.revokeToken(authentication);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void revokeToken(String accessToken) {
/* 108 */     if (!StringUtils.isEmpty(accessToken)) {
/* 109 */       this.authenticationServiceEsi.revokeToken(accessToken);
/*     */     }
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
/*     */   private boolean checkPasswordValidity(User dbUser, UserLoginRequest someUserLoginRequest) {
/* 124 */     User userToCheck = new User();
/* 125 */     userToCheck.setUserName(someUserLoginRequest.getUserName());
/* 126 */     userToCheck.setUserPassword(someUserLoginRequest.getUserPassword());
/* 127 */     String secret = this.configurator.getSecret();
/* 128 */     String machineName = this.configurator.getMachineNameConfiguration().getConfigValue();
/* 129 */     userToCheck.setSalt(dbUser.getSalt());
/* 130 */     String encryptedMachineNameUsingPassHash = this.cryptoEngine.encryptMachineNameUsingPasswordHash(secret, machineName, userToCheck);
/*     */     
/* 132 */     return encryptedMachineNameUsingPassHash.equals(dbUser.getUserPassword());
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\Authentificator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */