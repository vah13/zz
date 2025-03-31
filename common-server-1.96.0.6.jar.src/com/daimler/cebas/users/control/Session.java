/*     */ package com.daimler.cebas.users.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.validation.SystemIntegrityCheckResult;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.StartupStatus;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASSession;
/*     */ import com.daimler.cebas.common.entity.AbstractEntity;
/*     */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.security.EncryptedString;
/*     */ import com.daimler.cebas.system.control.startup.CeBASStartupProperty;
/*     */ import com.daimler.cebas.users.control.crypto.UserCryptoEngine;
/*     */ import com.daimler.cebas.users.control.exceptions.ApplicationNotStartedException;
/*     */ import com.daimler.cebas.users.control.exceptions.UserException;
/*     */ import com.daimler.cebas.users.control.factories.UserFactory;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.users.entity.UserKeyPair;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Base64;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import javax.annotation.PostConstruct;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.jboss.logging.MDC;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
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
/*     */ @CEBASSession
/*     */ public class Session
/*     */ {
/*  49 */   private static final String CLASS_NAME = Session.class.getName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String MDC_USERID = "userId";
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
/*     */   public static final String NAME = "Session";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private Environment env;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String defaultUser;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String currentUser;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String currentWindowsUser;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private User backendAuthenticatedUser;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String token;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Date tokenExpirationDate;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean onlineLogout;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkLocalPassword;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String containerKey;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private UserFactory userFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private UserRepository repository;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private UserCryptoEngine cryptoEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private AbstractConfigurator configurator;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String secret;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EncryptedString refreshToken;
/*     */ 
/*     */ 
/*     */   
/*     */   private SystemIntegrityCheckResult systemIntegrityCheckResult;
/*     */ 
/*     */ 
/*     */   
/* 158 */   private static String userName = UserFactory.getDefaultUsername();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean transitionValid;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostConstruct
/*     */   private void init() {
/* 170 */     this.secret = this.configurator.readProperty(CeBASStartupProperty.SECRET.getProperty());
/* 171 */     this.systemIntegrityCheckResult = new SystemIntegrityCheckResult();
/* 172 */     this.refreshToken = new EncryptedString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.MANDATORY)
/*     */   public boolean userExists(String userName) {
/* 184 */     String METHOD_NAME = "userExists";
/* 185 */     this.logger.entering(CLASS_NAME, "userExists");
/*     */     
/* 187 */     Optional<User> optional = this.repository.findUserByName(userName);
/* 188 */     boolean exists = optional.isPresent();
/* 189 */     this.logger.exiting(CLASS_NAME, "userExists");
/* 190 */     return exists;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.MANDATORY)
/*     */   public <T extends User> T createDefaultUser(Class<T> type) {
/* 202 */     String METHOD_NAME = "createDefaultUser";
/* 203 */     this.logger.entering(CLASS_NAME, "createDefaultUser");
/* 204 */     this.logger.exiting(CLASS_NAME, "createDefaultUser");
/* 205 */     return (T)this.repository.create((AbstractEntity)this.userFactory.getDefaultUser(type));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setDefaultUser() {
/*     */     User user;
/* 212 */     String METHOD_NAME = "setDefaultUser";
/* 213 */     this.logger.entering(CLASS_NAME, "setDefaultUser");
/*     */     
/* 215 */     if (this.defaultUser == null) {
/* 216 */       Optional<User> defaultUserOptional = this.repository.findUserByName(UserFactory.getDefaultUsername());
/* 217 */       if (defaultUserOptional.isPresent()) {
/* 218 */         user = defaultUserOptional.get();
/*     */       } else {
/* 220 */         throw new CEBASException("Default user is not yet initialized");
/*     */       } 
/*     */     } else {
/* 223 */       user = this.repository.findUserById(this.defaultUser);
/*     */     } 
/* 225 */     this.currentUser = this.defaultUser;
/* 226 */     setUserId(UserFactory.getDefaultUsername());
/* 227 */     updateMdc();
/* 228 */     this.containerKey = this.cryptoEngine.generateContainerKey(this.cryptoEngine.getAESDecryptedSalt(this.secret, user), this.cryptoEngine
/* 229 */         .getAESDecryptedUserPassword(this.secret, user), user);
/* 230 */     this.logger.exiting(CLASS_NAME, "setDefaultUser");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized User getDefaultUser() {
/* 239 */     User user = this.repository.findUserByName(UserFactory.getDefaultUsername()).orElse(null);
/* 240 */     this.defaultUser = (user != null) ? user.getEntityId() : null;
/* 241 */     return user;
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
/*     */   public synchronized void setCurrentUser(User user, String passwordContainerKey) {
/* 253 */     String METHOD_NAME = "setCurrentUser";
/* 254 */     this.logger.entering(CLASS_NAME, "setCurrentUser");
/* 255 */     this.currentUser = user.getEntityId();
/* 256 */     setUserId(user.getUserName());
/* 257 */     updateMdc();
/* 258 */     this.containerKey = this.cryptoEngine.generateContainerKey(this.cryptoEngine.getAESDecryptedSalt(this.secret, user), new String(
/* 259 */           Base64.getDecoder().decode(passwordContainerKey), StandardCharsets.UTF_8), user);
/* 260 */     this.logger.exiting(CLASS_NAME, "setCurrentUser");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getContainerKey() {
/* 269 */     String METHOD_NAME = "getContainerKey";
/* 270 */     this.logger.entering(CLASS_NAME, "getContainerKey");
/* 271 */     this.logger.exiting(CLASS_NAME, "getContainerKey");
/* 272 */     return this.containerKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized User getCurrentUser() {
/*     */     User user;
/* 282 */     if (!StartupStatus.isApplicationStarted()) {
/* 283 */       throw new ApplicationNotStartedException("Application is started up yet - can not get the current user.");
/*     */     }
/*     */     
/* 286 */     if (this.currentUser == null) {
/* 287 */       Optional<User> findUserByName = this.repository.findUserByName(UserFactory.getDefaultUsername());
/* 288 */       if (findUserByName.isPresent()) {
/* 289 */         user = findUserByName.get();
/*     */       } else {
/* 291 */         throw new UserException("Error getting current user.");
/*     */       } 
/*     */     } else {
/* 294 */       user = this.repository.findUserById(this.currentUser);
/*     */     } 
/* 296 */     return user;
/*     */   }
/*     */   
/*     */   public synchronized String getCurrentWindowsUser() {
/* 300 */     return this.currentWindowsUser;
/*     */   }
/*     */   
/*     */   public synchronized void setCurrentWindowsUser(String windowsUser) {
/* 304 */     this.currentWindowsUser = windowsUser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<User> getAllUsers() {
/* 313 */     String METHOD_NAME = "getAllUsers";
/* 314 */     this.logger.entering(CLASS_NAME, "getAllUsers");
/* 315 */     this.logger.exiting(CLASS_NAME, "getAllUsers");
/* 316 */     return this.repository.findAll(User.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isDefaultUser() {
/* 325 */     String METHOD_NAME = "isDefaultUser";
/* 326 */     this.logger.entering(CLASS_NAME, "isDefaultUser");
/* 327 */     this.logger.exiting(CLASS_NAME, "isDefaultUser");
/* 328 */     return (StringUtils.equals(this.currentUser, this.defaultUser) || 
/* 329 */       StringUtils.equals(userName, UserFactory.getDefaultUsername()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserCryptoEngine getCryptoEngine() {
/* 338 */     String METHOD_NAME = "getCryptoEngine";
/* 339 */     this.logger.entering(CLASS_NAME, "getCryptoEngine");
/* 340 */     this.logger.exiting(CLASS_NAME, "getCryptoEngine");
/* 341 */     return this.cryptoEngine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SystemIntegrityCheckResult getSystemIntegrityCheckResult() {
/* 350 */     String METHOD_NAME = "getSystemIntegrityCheckResult";
/* 351 */     this.logger.entering(CLASS_NAME, "getSystemIntegrityCheckResult");
/* 352 */     this.logger.exiting(CLASS_NAME, "getSystemIntegrityCheckResult");
/* 353 */     return this.systemIntegrityCheckResult;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.REQUIRED)
/*     */   public Optional<UserKeyPair> getCorrelatedKeyPair(Certificate certificate) {
/* 365 */     Map<String, Object> map = new HashMap<>();
/* 366 */     map.put("certificate", certificate);
/* 367 */     List<UserKeyPair> findWithNamedQuery = this.repository.findWithNamedQuery("findUserKeyPairByCertificate", map, -1);
/*     */     
/* 369 */     return findWithNamedQuery.stream().findFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized User getBackendAuthenticatedUser() {
/* 378 */     return this.backendAuthenticatedUser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setBackendAuthenticatedUser(User backendAuthenticatedUser) {
/* 388 */     this.backendAuthenticatedUser = backendAuthenticatedUser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getToken() {
/* 397 */     return this.token;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setToken(String token) {
/* 407 */     this.token = token;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Date getTokenExpirationDate() {
/* 416 */     return this.tokenExpirationDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setTokenExpirationDate(Date tokenExpirationDate) {
/* 425 */     this.tokenExpirationDate = tokenExpirationDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Boolean isOnlineLogout() {
/* 434 */     return Boolean.valueOf(this.onlineLogout);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setOnlineLogout(boolean onlineLogout) {
/* 443 */     this.onlineLogout = onlineLogout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isCheckLocalPassword() {
/* 452 */     return this.checkLocalPassword;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setCheckLocalPassword(boolean checkLocalPassword) {
/* 462 */     this.checkLocalPassword = checkLocalPassword;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized String getUserId() {
/* 471 */     return userName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static synchronized void setUserId(String userId) {
/* 482 */     userName = userId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setTransitionValid(boolean transitionValid) {
/* 492 */     this.transitionValid = transitionValid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isTransitionValid() {
/* 501 */     return this.transitionValid;
/*     */   }
/*     */   
/*     */   public String getRefreshToken() {
/* 505 */     return this.refreshToken.getValue();
/*     */   }
/*     */   
/*     */   public void setRefreshToken(String refreshToken) {
/* 509 */     this.refreshToken.setValue(refreshToken);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] activeProfiles() {
/* 518 */     return this.env.getActiveProfiles();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeUserFromMDC() {
/* 525 */     MDC.remove("userId");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateMdc() {
/* 532 */     setUserToMdc(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initMdc() {
/* 539 */     setUserToMdc(true);
/*     */   }
/*     */   
/*     */   private void setUserToMdc(boolean create) {
/* 543 */     Object userId = MDC.get("userId");
/* 544 */     if (create || (userId != null && StringUtils.isNotBlank((String)userId)))
/* 545 */       if (isDefaultUser()) {
/* 546 */         MDC.put("userId", "Default User");
/*     */       } else {
/* 548 */         MDC.put("userId", getUserId());
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\ceba\\users\control\Session.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */