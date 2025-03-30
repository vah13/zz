/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.control;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.cronjob.CertificatesCleanUp;
/*     */ import com.daimler.cebas.common.control.ApplicationInvalidState;
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASService;
/*     */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.users.control.UserRepository;
/*     */ import com.daimler.cebas.users.control.exceptions.UserException;
/*     */ import com.daimler.cebas.users.control.exceptions.UserLoginException;
/*     */ import com.daimler.cebas.users.control.factories.UserFactory;
/*     */ import com.daimler.cebas.users.control.vo.UserData;
/*     */ import com.daimler.cebas.users.control.vo.UserDetailsWithSession;
/*     */ import com.daimler.cebas.users.control.vo.UserLoginRequest;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.ZenZefiPkiManager;
/*     */ import com.daimler.cebas.zenzefi.security.ZenzefiSessionHolder;
/*     */ import com.daimler.cebas.zenzefi.system.control.websocket.WebsocketController;
/*     */ import com.daimler.cebas.zenzefi.users.control.Authentificator;
/*     */ import com.daimler.cebas.zenzefi.users.control.DeleteUserAccountsEngine;
/*     */ import com.daimler.cebas.zenzefi.users.control.UserContext;
/*     */ import com.daimler.cebas.zenzefi.users.control.UserRegistrationEngine;
/*     */ import com.daimler.cebas.zenzefi.users.control.idle.UserIdleTimer;
/*     */ import com.daimler.cebas.zenzefi.users.control.idle.event.IdleEvent;
/*     */ import com.daimler.cebas.zenzefi.users.control.login.AbstractLoginAttemptsStrategy;
/*     */ import com.daimler.cebas.zenzefi.users.control.login.AbstractLoginStrategy;
/*     */ import com.daimler.cebas.zenzefi.users.control.login.LoginState;
/*     */ import com.daimler.cebas.zenzefi.users.control.validation.IUserValidator;
/*     */ import com.daimler.cebas.zenzefi.users.entity.ZenZefiUser;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.event.EventListener;
/*     */ import org.springframework.scheduling.annotation.Async;
/*     */ import org.springframework.security.core.Authentication;
/*     */ import org.springframework.security.core.context.SecurityContextHolder;
/*     */ import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
/*     */ import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
/*     */ import org.springframework.security.oauth2.core.OAuth2AccessToken;
/*     */ import org.springframework.transaction.annotation.Propagation;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ import org.springframework.web.client.RestTemplate;
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
/*     */ @CEBASService
/*     */ public class UserService
/*     */ {
/*     */   private static final String REVOKE_TOKEN_TRANSITION_NOT_POSSIBLE = "Since transition is not possible the retrieved token will be revoked";
/*     */   private static final String USER_DOES_NOT_EXIST = "User does not exist in DB. Registration will be performed next.";
/*     */   private static final String TRANSITION_NOT_POSSIBLE = "Transition from offline to online cannot be performed because there is a different user logged in with Daimler backend";
/*     */   private static final String WAS_AUTHENTICATED_SUCCESSFULLY_AGAINST_THE_BACKEND_AND_AN_ACCESS_TOKEN_WAS_GENERATED_WITH_VALIDITY_PERIOD = " was authenticated successfully against the backend and an access token was generated with validity period: ";
/*  77 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.users.control.UserService.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String AUTHENTICATED = "authenticated";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int SECURE_RANDOM_BYTE_LENGTH = 36;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UserRepository repository;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Authentificator auth;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Session session;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UserIdleTimer userIdleTimer;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractConfigurator configurator;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CertificatesCleanUp certificatesCleanUp;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MetadataManager i18nRequestMetadata;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private OAuth2AuthorizedClientService authClientService;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ZenzefiSessionHolder zenzefiSessionHolder;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final AbstractLoginStrategy loginStrategy;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final AbstractLoginAttemptsStrategy loginAttemptsStrategy;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final DeleteUserAccountsEngine deleteUserAccountsEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UserRegistrationEngine userRegistrationEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   private final Object userContextChangeLock = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private WebsocketController wsController;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RestTemplate restTemplate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ZenZefiPkiManager zenZefiPkiManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public UserService(UserRepository repository, Authentificator auth, UserContext userContext, AbstractConfigurator configurator, IUserValidator userValidator, CertificatesCleanUp certificatesCleanUp, ZenzefiSessionHolder zenzefiSessionHolder, UserRegistrationEngine userRegistrationEngine, UserIdleTimer userIdleTimer, WebsocketController wsController, RestTemplate restTemplate, ZenZefiPkiManager zenZefiPkiManager) {
/* 200 */     this.repository = repository;
/* 201 */     this.auth = auth;
/* 202 */     this.session = userContext.getSession();
/* 203 */     this.configurator = configurator;
/* 204 */     this.certificatesCleanUp = certificatesCleanUp;
/* 205 */     this.logger = userContext.getLogger();
/* 206 */     this.i18nRequestMetadata = userContext.getMetadataManager();
/* 207 */     this.authClientService = userContext.getAuthorizedClientService();
/* 208 */     this.zenzefiSessionHolder = zenzefiSessionHolder;
/* 209 */     this.loginStrategy = userContext.getLoginStrategy();
/* 210 */     this.deleteUserAccountsEngine = userContext.getDeleteUserAccountsEngine();
/* 211 */     this.loginAttemptsStrategy = userContext.getLoginAttemptsStrategy();
/* 212 */     this.userRegistrationEngine = userRegistrationEngine;
/* 213 */     this.userIdleTimer = userIdleTimer;
/* 214 */     this.wsController = wsController;
/* 215 */     this.restTemplate = restTemplate;
/* 216 */     this.zenZefiPkiManager = zenZefiPkiManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void oauthLogin(HttpServletRequest request) {
/* 227 */     String METHOD_NAME = "oauthLogin";
/* 228 */     this.logger.entering(CLASS_NAME, "oauthLogin");
/* 229 */     ZenZefiUser user = this.auth.backendLogin(request);
/*     */     
/* 231 */     synchronized (this.userContextChangeLock) {
/* 232 */       String onlineUser = user.getUserName();
/* 233 */       String sessionUser = this.session.getCurrentUser().getUserName();
/*     */       
/* 235 */       boolean differentUserAuthRequest = (!this.session.isDefaultUser() && !sessionUser.equalsIgnoreCase(onlineUser));
/*     */       
/* 237 */       OAuth2AccessToken accessToken = getOAuthClient(user.getUserName()).getAccessToken();
/*     */       
/* 239 */       if (differentUserAuthRequest) {
/* 240 */         this.logger.log(Level.WARNING, "000360", "Transition from offline to online cannot be performed because there is a different user logged in with Daimler backend", 
/* 241 */             getClass().getSimpleName());
/* 242 */         this.logger.log(Level.WARNING, "000360", "Since transition is not possible the retrieved token will be revoked", 
/* 243 */             getClass().getSimpleName());
/*     */         
/* 245 */         logoutBackend(accessToken.getTokenValue());
/* 246 */         this.session.setTransitionValid(false);
/* 247 */         UserContext.addLogingErrorMessage(this.i18nRequestMetadata
/* 248 */             .getMessage("transitionNotPossible", new String[] { sessionUser, onlineUser }));
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 253 */       if (findUser(user.getUserName()) == null) {
/* 254 */         this.logger.log(Level.INFO, "000038", "User does not exist in DB. Registration will be performed next.", CLASS_NAME);
/*     */       }
/* 256 */       this.session.setTransitionValid(true);
/* 257 */       this.session.setBackendAuthenticatedUser((User)user);
/* 258 */       this.session.setToken(accessToken.getTokenValue());
/* 259 */       this.session.setTokenExpirationDate(Date.from(accessToken.getExpiresAt()));
/*     */       
/* 261 */       if (this.session.isDefaultUser()) {
/* 262 */         this.session.setCheckLocalPassword(true);
/*     */       }
/*     */       
/* 265 */       this.zenZefiPkiManager.setPkiEnvironmentProperties();
/*     */       
/* 267 */       this.logger.log(Level.FINE, "000041", "The user: " + user.getUserName() + " was authenticated successfully against the backend and an access token was generated with validity period: " + accessToken
/*     */           
/* 269 */           .getExpiresAt(), CLASS_NAME);
/*     */     } 
/*     */     
/* 272 */     this.logger.exiting(CLASS_NAME, "oauthLogin");
/*     */   }
/*     */   
/*     */   private OAuth2AuthorizedClient getOAuthClient(String userName) {
/* 276 */     return this.authClientService.loadAuthorizedClient("gas", userName);
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
/*     */   private Map<String, String> sendLoginResponseViaWebsockets(boolean isUserAuthenticated, Map<String, String> response) {
/* 288 */     if (isUserAuthenticated) {
/*     */       
/* 290 */       this.userIdleTimer.setLoggedIn(true);
/* 291 */       this.userIdleTimer.reset();
/* 292 */       this.userIdleTimer.setAutologout(true);
/* 293 */       long remainingSeconds = getRemainingSessionSeconds();
/* 294 */       response.put("remainingSessionSeconds", String.valueOf(remainingSeconds));
/*     */ 
/*     */       
/* 297 */       this.wsController.login(response);
/*     */     } 
/* 299 */     return response;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.NEVER)
/*     */   public boolean isTransitionValid() {
/* 309 */     return this.session.isTransitionValid();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.NEVER)
/*     */   public void oauthLogout(Authentication authentication) {
/* 317 */     String METHOD_NAME = "oauthLogout";
/* 318 */     this.logger.entering(CLASS_NAME, "oauthLogout");
/* 319 */     synchronized (this.userContextChangeLock) {
/* 320 */       boolean shouldRevokeOnSimpleLogout = true;
/* 321 */       if (authentication != null) {
/* 322 */         this.auth.revokeToken(authentication);
/* 323 */         shouldRevokeOnSimpleLogout = false;
/*     */       } 
/* 325 */       if (!this.session.isDefaultUser() || (this.session
/* 326 */         .getBackendAuthenticatedUser() != null && 
/* 327 */         !this.session.getBackendAuthenticatedUser().getUserName().equalsIgnoreCase(this.session.getDefaultUser().getUserName()))) {
/* 328 */         logout(shouldRevokeOnSimpleLogout);
/*     */       }
/*     */     } 
/* 331 */     this.logger.exiting(CLASS_NAME, "oauthLogout");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> oauthRegister(UserLoginRequest someUser) {
/* 342 */     String METHOD_NAME = "oauthRegister";
/* 343 */     this.logger.entering(CLASS_NAME, "oauthRegister");
/* 344 */     synchronized (this.userContextChangeLock) {
/* 345 */       LoginState loginState; ZenZefiUser backendAuthenticatedUser = (ZenZefiUser)this.session.getBackendAuthenticatedUser();
/* 346 */       ZenZefiUser copyBackendAuthenticatedUser = new ZenZefiUser();
/* 347 */       copyKnownBackendAuthenticatedUserInfo(backendAuthenticatedUser, copyBackendAuthenticatedUser);
/*     */       
/* 349 */       if (StringUtils.equalsIgnoreCase(someUser.getUserName(), copyBackendAuthenticatedUser.getUserName())) {
/* 350 */         copyBackendAuthenticatedUser.setUserPassword(someUser.getUserPassword());
/* 351 */         this.userRegistrationEngine.register(copyBackendAuthenticatedUser);
/* 352 */         this.session.setCurrentUser((User)copyBackendAuthenticatedUser, someUser.getUserPassword());
/* 353 */         this.session.setCheckLocalPassword(false);
/* 354 */         loginState = LoginState.authenticated();
/*     */       } else {
/* 356 */         loginState = LoginState.wrongPassword(someUser.getUserName());
/*     */       } 
/* 358 */       Map<String, String> response = this.loginStrategy.createLoginResponse(loginState, false);
/* 359 */       response = sendLoginResponseViaWebsockets(loginState.isAuthenticated(), response);
/* 360 */       this.loginStrategy.startDifferentialUpdate(loginState.isAuthenticated(), response);
/* 361 */       this.logger.exiting(CLASS_NAME, "oauthRegister");
/* 362 */       return response;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> login(UserLoginRequest user) {
/* 373 */     synchronized (this.userContextChangeLock) {
/* 374 */       this.session.getSystemIntegrityCheckResult().clear();
/* 375 */       handleExistingLogin(user);
/*     */       try {
/* 377 */         LoginState loginState = authenticateUser(user);
/* 378 */         Map<String, String> response = this.loginStrategy.createLoginResponse(loginState, false);
/* 379 */         response = sendLoginResponseViaWebsockets(loginState.isAuthenticated(), response);
/* 380 */         this.loginStrategy.startDifferentialUpdate(loginState.isAuthenticated(), response);
/* 381 */         return response;
/* 382 */       } catch (UserException e) {
/* 383 */         Map<String, String> response = this.loginStrategy.createLoginResponse(LoginState.error(e.getMessage()), true);
/*     */         
/* 385 */         this.loginStrategy.startDifferentialUpdate(true, response);
/* 386 */         return response;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private LoginState authenticateUser(UserLoginRequest loginRequest) {
/*     */     LoginState loginState;
/* 393 */     ZenZefiUser user = (ZenZefiUser)findUser(loginRequest);
/* 394 */     if (user == null) {
/* 395 */       loginState = LoginState.userDoesNotExist(loginRequest.getUserName());
/* 396 */       this.session.setTransitionValid(false);
/* 397 */       this.session.setCheckLocalPassword(false);
/*     */     } else {
/* 399 */       loginState = this.auth.authentify((User)user, loginRequest);
/* 400 */       if (loginState.isAuthenticated()) {
/* 401 */         this.configurator.updateRegisteredUserConfigurations((User)user);
/* 402 */         user.setNumberOfFailedLoginAttempts(Integer.valueOf(0));
/* 403 */         this.repository.flush();
/* 404 */         this.session.setCurrentUser((User)user, loginRequest.getUserPassword());
/* 405 */         this.certificatesCleanUp.cleanUpCertificates((User)user);
/* 406 */         this.session.setTransitionValid(true);
/* 407 */         this.logger.log(Level.INFO, "000037", this.i18nRequestMetadata
/* 408 */             .getEnglishMessage("userLoggedIn", new String[] { user.getUserName() }), CLASS_NAME);
/* 409 */         this.session.setCheckLocalPassword(false);
/*     */       } else {
/* 411 */         this.loginAttemptsStrategy.handleInvalidLoginAttempt(user, loginRequest.getUserPassword());
/* 412 */         this.session.setCheckLocalPassword(true);
/*     */       } 
/*     */     } 
/* 415 */     return loginState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(propagation = Propagation.NEVER)
/*     */   public ZenZefiUser register(ZenZefiUser user) {
/* 427 */     String METHOD_NAME = "register";
/* 428 */     this.logger.entering(CLASS_NAME, "register");
/* 429 */     this.logger.exiting(CLASS_NAME, "register");
/* 430 */     synchronized (this.userContextChangeLock) {
/* 431 */       return this.userRegistrationEngine.register(user);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Transactional(readOnly = true)
/*     */   public UserDetailsWithSession getCurrentUserDetails() {
/*     */     UserDetailsWithSession response;
/* 442 */     String METHOD_NAME = "getCurrentUserDetails";
/* 443 */     this.logger.logEnteringToFile(CLASS_NAME, "getCurrentUserDetails");
/*     */     
/* 445 */     synchronized (this.userContextChangeLock) {
/* 446 */       User defaultUser = this.session.getDefaultUser();
/* 447 */       User backendAuthenticatedUser = this.session.getBackendAuthenticatedUser();
/* 448 */       User currentUser = getCurrentUser();
/* 449 */       if (backendAuthenticatedUser != null && currentUser
/* 450 */         .getUserName().equals(backendAuthenticatedUser.getUserName()) && 
/* 451 */         !currentUser.getUserName().equals(defaultUser.getUserName())) {
/*     */ 
/*     */ 
/*     */         
/* 455 */         response = new UserDetailsWithSession(currentUser.getFirstName(), currentUser.getLastName(), currentUser.getUserName(), false, false, this.session.isCheckLocalPassword(), true, this.session.isTransitionValid());
/* 456 */       } else if (backendAuthenticatedUser != null && currentUser
/* 457 */         .getUserName().equals(defaultUser.getUserName())) {
/*     */         
/* 459 */         boolean isNewUser = isNewUser(backendAuthenticatedUser);
/*     */ 
/*     */         
/* 462 */         response = new UserDetailsWithSession(backendAuthenticatedUser.getFirstName(), backendAuthenticatedUser.getLastName(), backendAuthenticatedUser.getUserName(), false, isNewUser, this.session.isCheckLocalPassword(), true, this.session.isTransitionValid());
/* 463 */       } else if (backendAuthenticatedUser == null && 
/* 464 */         !currentUser.getUserName().equals(defaultUser.getUserName())) {
/*     */ 
/*     */ 
/*     */         
/* 468 */         response = new UserDetailsWithSession(StringUtils.defaultString(currentUser.getFirstName()), StringUtils.defaultString(currentUser.getLastName()), currentUser.getUserName(), false, false, this.session.isCheckLocalPassword(), false, this.session.isTransitionValid());
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 473 */         response = new UserDetailsWithSession(StringUtils.defaultString(defaultUser.getFirstName()), StringUtils.defaultString(defaultUser.getLastName()), defaultUser.getUserName(), true, false, this.session.isCheckLocalPassword(), false, this.session.isTransitionValid());
/*     */       } 
/*     */     } 
/*     */     
/* 477 */     long remainingSeconds = getRemainingSessionSeconds();
/* 478 */     response.setRemainingSessionSeconds(remainingSeconds);
/*     */     
/* 480 */     this.logger.log(Level.FINE, "000534", this.i18nRequestMetadata.getEnglishMessage("getCurrentUserDetails", new String[] { response
/* 481 */             .getUserName() }), CLASS_NAME);
/* 482 */     this.logger.logExitingToFile(CLASS_NAME, "getCurrentUserDetails");
/* 483 */     return response;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long getRemainingSessionSeconds() {
/* 492 */     long expirationTimestamp = this.userIdleTimer.getExpirationTimestamp();
/* 493 */     if (expirationTimestamp == Long.MAX_VALUE) {
/* 494 */       return 0L;
/*     */     }
/* 496 */     Instant expiration = Instant.ofEpochMilli(expirationTimestamp);
/* 497 */     Instant now = Instant.now();
/* 498 */     if (now.isAfter(expiration) || this.session.isDefaultUser()) {
/* 499 */       return 0L;
/*     */     }
/* 501 */     Duration remainingTime = Duration.between(Instant.now(), expiration);
/* 502 */     return remainingTime.getSeconds();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public User getCurrentUser() {
/* 512 */     String METHOD_NAME = "getCurrentUser";
/* 513 */     this.logger.entering(CLASS_NAME, "getCurrentUser");
/* 514 */     this.logger.exiting(CLASS_NAME, "getCurrentUser");
/* 515 */     return this.session.getCurrentUser();
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
/*     */   public boolean updateWindowsUser(String windowsUser) {
/* 527 */     if (windowsUser == null) {
/* 528 */       return false;
/*     */     }
/*     */     
/* 531 */     String currentWindowsUser = this.session.getCurrentWindowsUser();
/* 532 */     if (currentWindowsUser == null) {
/* 533 */       this.session.setCurrentWindowsUser(windowsUser);
/* 534 */       return false;
/*     */     } 
/* 536 */     if (!windowsUser.equals(currentWindowsUser)) {
/* 537 */       this.session.setCurrentWindowsUser(windowsUser);
/* 538 */       return true;
/*     */     } 
/* 540 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Async
/*     */   @EventListener
/*     */   public void automatedLogout(IdleEvent idleEvent) {
/* 552 */     String METHOD_NAME = "automatedLogout";
/* 553 */     this.logger.entering(CLASS_NAME, "automatedLogout");
/* 554 */     if (!this.session.getCurrentUser().getUserName().equals(UserFactory.getDefaultUsername())) {
/* 555 */       oauthLogout(SecurityContextHolder.getContext().getAuthentication());
/* 556 */       idleEvent.setLoggedOff();
/* 557 */       this.logger.log(Level.INFO, "000009", this.i18nRequestMetadata
/* 558 */           .getEnglishMessage("automatedLogOutUserIs"), CLASS_NAME);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logout() {
/* 566 */     String METHOD_NAME = "logout";
/* 567 */     this.logger.entering(CLASS_NAME, "logout");
/* 568 */     synchronized (this.userContextChangeLock) {
/* 569 */       logout(true);
/* 570 */       this.session.setTransitionValid(true);
/*     */     } 
/* 572 */     this.logger.exiting(CLASS_NAME, "logout");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<UserData> getUserAccounts() {
/* 581 */     String METHOD_NAME = "getUserAccounts";
/* 582 */     this.logger.entering(CLASS_NAME, "getUserAccounts");
/*     */ 
/*     */     
/* 585 */     List<UserData> usersAccounts = (List<UserData>)this.session.getAllUsers().stream().filter(user -> !user.getUserName().equals(UserFactory.getDefaultUsername())).map(user -> new UserData(user)).collect(Collectors.toList());
/* 586 */     this.logger.exiting(CLASS_NAME, "getUserAccounts");
/* 587 */     return usersAccounts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<UserData> getUserAccountByUsername(String username) {
/* 598 */     String METHOD_NAME = "getUserAccountByUsername";
/* 599 */     this.logger.entering(CLASS_NAME, "getUserAccountByUsername");
/*     */     
/* 601 */     Optional<UserData> account = this.session.getAllUsers().stream().filter(user -> user.getUserName().equals(username)).findFirst().map(user -> new UserData(user));
/* 602 */     this.logger.exiting(CLASS_NAME, "getUserAccountByUsername");
/* 603 */     return account;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOfflineLoggedIn() {
/* 612 */     return (!this.session.isDefaultUser() && this.session.getBackendAuthenticatedUser() == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteAccounts(List<String> ids) {
/* 621 */     String METHOD_NAME = "deleteAccounts";
/* 622 */     this.logger.entering(CLASS_NAME, "deleteAccounts");
/* 623 */     ids.forEach(id -> deleteAccount(id));
/* 624 */     this.wsController.triggerClientUpdate();
/* 625 */     this.logger.exiting(CLASS_NAME, "deleteAccounts");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteAccount(String id) {
/* 635 */     String METHOD_NAME = "deleteAccount";
/* 636 */     this.logger.entering(CLASS_NAME, "deleteAccount");
/* 637 */     if (!this.session.isDefaultUser()) {
/* 638 */       User transitiveUser = this.session.getCurrentUser();
/* 639 */       User persistedUser = findUser(transitiveUser.getUserName());
/* 640 */       if (persistedUser != null && persistedUser.getEntityId().equals(id)) {
/* 641 */         logout();
/*     */       }
/*     */     } 
/* 644 */     this.deleteUserAccountsEngine.deleteAccount(id);
/* 645 */     this.logger.exiting(CLASS_NAME, "deleteAccount");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void logout(boolean revokeToken) {
/* 654 */     UserDetailsWithSession currentUserDetails = getCurrentUserDetails();
/* 655 */     this.session.getSystemIntegrityCheckResult().clear();
/* 656 */     this.zenzefiSessionHolder.destroySessions();
/* 657 */     if (revokeToken) {
/* 658 */       this.auth.revokeToken(this.session.getToken());
/*     */     }
/* 660 */     if (null != this.session.getBackendAuthenticatedUser()) {
/* 661 */       this.session.setOnlineLogout(true);
/*     */     } else {
/* 663 */       this.session.setOnlineLogout(false);
/*     */     } 
/* 665 */     this.session.setBackendAuthenticatedUser(null);
/* 666 */     this.session.setToken(null);
/* 667 */     this.session.setTokenExpirationDate(null);
/* 668 */     this.session.setCheckLocalPassword(false);
/* 669 */     this.session.setDefaultUser();
/* 670 */     this.session.setRefreshToken(null);
/* 671 */     SecurityContextHolder.clearContext();
/* 672 */     this.wsController.logout(currentUserDetails);
/* 673 */     this.logger.log(Level.INFO, "000009", this.i18nRequestMetadata
/* 674 */         .getEnglishMessage("logOutUserIs"), CLASS_NAME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void logoutBackend(String token) {
/* 683 */     this.zenzefiSessionHolder.destroySessions();
/* 684 */     this.auth.revokeToken(token);
/* 685 */     this.session.setBackendAuthenticatedUser(null);
/* 686 */     this.session.setToken(null);
/* 687 */     this.session.setTokenExpirationDate(null);
/* 688 */     this.session.setCheckLocalPassword(false);
/* 689 */     this.session.setRefreshToken(null);
/* 690 */     this.session.setDefaultUser();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventListener
/*     */   protected void applicationInvalidState(ApplicationInvalidState event) {
/* 699 */     this.auth.revokeToken(this.session.getToken());
/* 700 */     this.session.setToken(null);
/* 701 */     this.session.setTokenExpirationDate(null);
/* 702 */     this.session.setBackendAuthenticatedUser(null);
/* 703 */     this.zenzefiSessionHolder.destroySessions();
/* 704 */     SecurityContextHolder.clearContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void copyKnownBackendAuthenticatedUserInfo(ZenZefiUser backendAuthenticatedUser, ZenZefiUser copyBackendAuthenticatedUser) {
/* 715 */     String userNameFromBackend = backendAuthenticatedUser.getUserName();
/* 716 */     if (StringUtils.isEmpty(userNameFromBackend)) {
/* 717 */       throw new CEBASException("Error: Found user with user name");
/*     */     }
/* 719 */     if (StringUtils.equals(userNameFromBackend, UserFactory.getDefaultUsername())) {
/* 720 */       throw new CEBASException("Error: Backend user's id cannot be the same as default user: " + userNameFromBackend);
/*     */     }
/*     */     
/* 723 */     copyBackendAuthenticatedUser.setUserName(userNameFromBackend);
/* 724 */     copyBackendAuthenticatedUser.setFirstName(backendAuthenticatedUser.getFirstName());
/* 725 */     copyBackendAuthenticatedUser.setLastName(backendAuthenticatedUser.getLastName());
/* 726 */     copyBackendAuthenticatedUser.setOrganisation(backendAuthenticatedUser.getOrganisation());
/* 727 */     copyBackendAuthenticatedUser.setRole(backendAuthenticatedUser.getRole());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleExistingLogin(UserLoginRequest someUser) {
/* 736 */     User backendAuthenticatedUser = this.session.getBackendAuthenticatedUser();
/* 737 */     if (this.session.isCheckLocalPassword() && backendAuthenticatedUser != null && 
/* 738 */       !backendAuthenticatedUser.getUserName().equals(someUser.getUserName())) {
/* 739 */       throw new UserLoginException("The user id: " + someUser.getUserName() + " is not authenticated against backend. Another user is in process of authentication");
/*     */     }
/*     */     
/* 742 */     if (!UserFactory.getDefaultUsername().equalsIgnoreCase(this.session.getCurrentUser().getUserName())) {
/* 743 */       this.session.setTransitionValid(false);
/*     */       
/* 745 */       UserLoginException userLoginException = new UserLoginException(this.i18nRequestMetadata.getMessage("userAlreadyLoggedIn"), "userAlreadyLoggedIn");
/* 746 */       this.logger.logWithTranslation(Level.WARNING, "000039", userLoginException.getMessageId(), userLoginException
/* 747 */           .getClass().getSimpleName());
/* 748 */       throw userLoginException;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isNewUser(User backendAuthenticatedUser) {
/* 759 */     return (findUser(backendAuthenticatedUser.getUserName()) == null);
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
/*     */   private User findUser(UserLoginRequest loginRequest) {
/* 771 */     User user = findUser(loginRequest.getUserName());
/* 772 */     return (user != null) ? user : this.loginStrategy.handleUserNotFound(loginRequest);
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
/*     */   private User findUser(String userName) {
/* 784 */     Optional<User> optional = this.repository.findUserByName(userName);
/* 785 */     return optional.orElse(null);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\control\UserService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */