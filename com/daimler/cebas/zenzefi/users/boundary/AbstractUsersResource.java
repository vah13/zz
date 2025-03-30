/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.boundary;
/*     */ 
/*     */ import com.daimler.cebas.common.control.HttpHeaderFactory;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.factories.UserFactory;
/*     */ import com.daimler.cebas.users.control.vo.UserData;
/*     */ import com.daimler.cebas.users.control.vo.UserLoginRequest;
/*     */ import com.daimler.cebas.users.entity.User;
/*     */ import com.daimler.cebas.zenzefi.users.control.UserService;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ 
/*     */ 
/*     */ public abstract class AbstractUsersResource
/*     */ {
/*     */   protected static final String USERS = "/users";
/*     */   protected static final String CURRENT_USER = "/users/currentUser";
/*     */   protected static final String USERS_LOGIN_ERRROR_MESSAGES = "/users/login/errors";
/*     */   protected static final String LOGIN = "/users/login";
/*     */   protected static final String LOGOUT = "/users/logout";
/*     */   @Autowired
/*     */   protected UserService userService;
/*     */   @Autowired
/*     */   protected MetadataManager requestMetaData;
/*     */   @Autowired
/*     */   protected Logger logger;
/*     */   @Autowired
/*     */   private MetadataManager i18nRequestMetadata;
/*     */   
/*     */   protected void deleteAccounts(String[] ids, String locale, String correlationid, String METHOD_NAME, String CLASS_NAME) {
/*  94 */     this.logger.entering(CLASS_NAME, METHOD_NAME);
/*  95 */     addRequestMetaData(locale);
/*  96 */     this.userService.deleteAccounts(Arrays.asList(ids));
/*  97 */     this.logger.exiting(CLASS_NAME, METHOD_NAME);
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
/*     */   protected boolean deleteAccountByUsername(String username, String locale, String METHOD_NAME, String CLASS_NAME) {
/* 115 */     this.logger.entering(CLASS_NAME, METHOD_NAME);
/* 116 */     addRequestMetaData(locale);
/* 117 */     Optional<UserData> account = this.userService.getUserAccountByUsername(username);
/* 118 */     if (!account.isPresent()) {
/* 119 */       this.logger.log(Level.WARNING, "000615", "No user found with username " + username + ". Deletion of account aborted.", CLASS_NAME);
/*     */       
/* 121 */       return false;
/*     */     } 
/* 123 */     this.userService.deleteAccount(((UserData)account.get()).getId());
/* 124 */     this.logger.exiting(CLASS_NAME, METHOD_NAME);
/* 125 */     return true;
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
/*     */   protected ResponseEntity<List<UserData>> getUsers(String locale, String correlationid, String METHOD_NAME, String CLASS_NAME) {
/* 139 */     this.logger.entering(CLASS_NAME, METHOD_NAME);
/* 140 */     addRequestMetaData(locale);
/* 141 */     List<UserData> userAccounts = this.userService.getUserAccounts();
/*     */     
/* 143 */     ResponseEntity<List<UserData>> response = ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(userAccounts);
/* 144 */     this.logger.exiting(CLASS_NAME, METHOD_NAME);
/* 145 */     return response;
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
/*     */   protected ResponseEntity<Map<String, String>> getLoginResponse(UserLoginRequest user, String locale, String correlationid) {
/* 158 */     addRequestMetaData(locale);
/* 159 */     Map<String, String> response = this.userService.login(user);
/* 160 */     HttpStatus httpStatus = getLoginResponseHttpStatus(response);
/* 161 */     return new ResponseEntity(response, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), httpStatus);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HashMap<String, String> getLogoutResponse(String locale, String correlationid) {
/* 172 */     addRequestMetaData(locale);
/* 173 */     HashMap<String, String> logoutResponse = new HashMap<>();
/* 174 */     User currentUser = this.userService.getCurrentUser();
/* 175 */     if (!currentUser.getUserName().equals(UserFactory.getDefaultUsername())) {
/* 176 */       this.userService.logout();
/* 177 */       logoutResponse.put("message", this.requestMetaData.getMessage("logoutSuccessful"));
/*     */     } else {
/* 179 */       this.logger.log(Level.INFO, "000513", this.i18nRequestMetadata
/* 180 */           .getEnglishMessage("logOutUserIs"), getClass().getSimpleName());
/* 181 */       logoutResponse.put("message", this.requestMetaData
/* 182 */           .getMessage("logoutNoUserLoggedIn"));
/*     */     } 
/* 184 */     return logoutResponse;
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
/*     */   protected HashMap<String, String> getLogoutOnUserSwitchResponse(String userId, String locale, String correlationid) {
/* 197 */     addRequestMetaData(locale);
/*     */     
/* 199 */     HashMap<String, String> logoutResponse = new HashMap<>();
/* 200 */     if (this.userService.updateWindowsUser(userId)) {
/* 201 */       this.logger.log(Level.INFO, "000686", "Logout on user switch - user changed to: " + userId, 
/* 202 */           getClass().getSimpleName());
/* 203 */       logoutResponse = getLogoutResponse(locale, correlationid);
/*     */     } else {
/* 205 */       logoutResponse.put("message", this.requestMetaData.getMessage("logOutUserSwitchUserSame"));
/*     */     } 
/* 207 */     return logoutResponse;
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
/*     */   protected HttpStatus getLoginResponseHttpStatus(Map<String, String> response) {
/*     */     HttpStatus httpStatus;
/* 220 */     if (Boolean.TRUE.toString().equalsIgnoreCase(response.get("authenticated"))) {
/* 221 */       httpStatus = HttpStatus.OK;
/* 222 */     } else if (Boolean.TRUE.toString().equalsIgnoreCase(response.get("accountDeleted"))) {
/* 223 */       httpStatus = HttpStatus.FORBIDDEN;
/*     */     } else {
/* 225 */       httpStatus = HttpStatus.NOT_ACCEPTABLE;
/*     */     } 
/*     */     
/* 228 */     response.remove("accountDeleted");
/*     */     
/* 230 */     return httpStatus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addRequestMetaData(String locale) {
/* 239 */     this.requestMetaData.setLocale(locale);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\boundary\AbstractUsersResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */