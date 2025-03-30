/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.boundary;
/*     */ 
/*     */ import com.daimler.cebas.common.control.HttpHeaderFactory;
/*     */ import com.daimler.cebas.users.control.vo.UserData;
/*     */ import com.daimler.cebas.users.control.vo.UserDetailsWithSession;
/*     */ import com.daimler.cebas.users.control.vo.UserLoginRequest;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.ZenZefiCertificatesService;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*     */ import com.daimler.cebas.zenzefi.system.control.NodeServer;
/*     */ import com.daimler.cebas.zenzefi.users.boundary.AbstractUsersResource;
/*     */ import com.daimler.cebas.zenzefi.users.control.UserContext;
/*     */ import com.daimler.cebas.zenzefi.users.entity.ZenZefiUser;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import io.swagger.annotations.ApiParam;
/*     */ import io.swagger.annotations.ApiResponse;
/*     */ import io.swagger.annotations.ApiResponses;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.validation.Valid;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.context.annotation.Profile;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestHeader;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.RestController;
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
/*     */ @RestController
/*     */ @Api(value = "Users API", tags = {"ZenZefi User Management"})
/*     */ @Profile({"!AFTERSALES"})
/*     */ public class UsersResource
/*     */   extends AbstractUsersResource
/*     */ {
/*     */   private static final String REDIRECT_LOCATION = "Location";
/*     */   @Autowired
/*     */   protected ZenZefiCertificatesService certificateService;
/*  68 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.users.boundary.UsersResource.class.getName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${spring.profiles.active:Unknown}")
/*     */   private String activeProfile;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String USERS = "/users";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String REGISTRATION = "/users/register";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOGIN = "/users/login";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOGOUT = "/users/logout";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOGOUT_SWITCH = "/users/logoutOnUserSwitch";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CURRENT_USER = "/users/currentUser";
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private UpdateSession updateSession;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", nickname = "allUsers", value = "Returns a list of users.")
/*     */   @RequestMapping(value = {"/users"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<List<UserData>> getUsers(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 114 */     String METHOD_NAME = "getUsers";
/* 115 */     return getUsers(locale, correlationid, "getUsers", CLASS_NAME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "DELETE", nickname = "deleteUsers", value = "Deletes user accounts by id. The ids must valid UUIDs.")
/*     */   @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")
/*     */   @RequestMapping(value = {"/users/{ids}"}, method = {RequestMethod.DELETE})
/*     */   public ResponseEntity<String> deleteUsers(@ApiParam(name = "ids", value = "Users ids, must be valid UUIDs, comma separated.", required = true) @PathVariable String[] ids, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 125 */     String METHOD_NAME = "deleteUsers";
/* 126 */     deleteAccounts(ids, locale, correlationid, "deleteUsers", CLASS_NAME);
/* 127 */     return new ResponseEntity((MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "DELETE", nickname = "deleteUserByUsername", value = "Deletes user account by username.")
/*     */   @ApiResponse(code = 404, message = "No account with that username found.")
/*     */   @RequestMapping(value = {"/users"}, method = {RequestMethod.DELETE})
/*     */   public ResponseEntity<String> deleteUserByUsername(@ApiParam(name = "username", value = "The user name. Length between 1 and 7 characters. Must be alpha numeric, non blank.", required = true) @RequestParam String username, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 137 */     String METHOD_NAME = "deleteUserByUsername";
/* 138 */     boolean result = deleteAccountByUsername(username, locale, "deleteUserByUsername", CLASS_NAME);
/* 139 */     return new ResponseEntity((MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), result ? HttpStatus.OK : HttpStatus.NOT_FOUND);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "POST", nickname = "registration", value = "Registers a new user in ZenZefi.")
/*     */   @ApiResponses({@ApiResponse(code = 201, message = "Registration successful."), @ApiResponse(code = 406, message = "User validation failed for a field."), @ApiResponse(code = 500, message = "Exception message")})
/*     */   @RequestMapping(value = {"/users/register"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<HashMap<String, String>> register(@ApiParam(value = "The user.", required = true) @Valid @RequestBody ZenZefiUser user, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 152 */     String METHOD_NAME = "register";
/* 153 */     this.logger.entering(CLASS_NAME, "register");
/* 154 */     addRequestMetaData(locale);
/* 155 */     HashMap<String, String> response = new HashMap<>();
/* 156 */     response.put("message", this.requestMetaData
/* 157 */         .getMessage("userWasRegistered", new String[] { user.getUserName() }));
/* 158 */     this.userService.register(user);
/* 159 */     this.logger.exiting(CLASS_NAME, "register");
/* 160 */     return new ResponseEntity(response, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.CREATED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"/users/login/oauth"}, method = {RequestMethod.GET})
/*     */   public void oauthLogin(HttpServletRequest request, HttpServletResponse httpServletResponse, @RequestParam(required = false) boolean fullUpdate, @RequestParam(required = false) boolean differentialUpdate, @RequestHeader(value = "Accept-Language", required = false) String locale) {
/* 168 */     String METHOD_NAME = "oauthLogin";
/* 169 */     this.logger.entering(CLASS_NAME, "oauthLogin");
/*     */     
/* 171 */     String location = NodeServer.getClientHomepage(request);
/* 172 */     String localSplit = (locale != null) ? locale.split(",")[0] : "en";
/* 173 */     if (!localSplit.equals("de") && !localSplit.equals("en")) {
/* 174 */       localSplit = "en";
/*     */     }
/* 176 */     addRequestMetaData(localSplit);
/* 177 */     boolean offlineLoggedIn = this.userService.isOfflineLoggedIn();
/* 178 */     this.userService.oauthLogin(request);
/* 179 */     if (this.updateSession.isPaused() || (offlineLoggedIn && this.userService.isTransitionValid()))
/*     */     {
/* 181 */       if (differentialUpdate) {
/* 182 */         this.certificateService.differentialCertificatesUpdate("ZenZefiClient");
/* 183 */       } else if (fullUpdate) {
/* 184 */         this.certificateService.fullCertificatesUpdate("ZenZefiClient");
/*     */       } 
/*     */     }
/* 187 */     httpServletResponse.setHeader("Location", location);
/* 188 */     httpServletResponse.setStatus(302);
/*     */     
/* 190 */     this.logger.exiting(CLASS_NAME, "oauthLogin");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"/users/register/oauth"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<Map<String, String>> oauthRegister(@ApiParam(value = "The user.", required = true) @Valid @RequestBody UserLoginRequest user, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 198 */     String METHOD_NAME = "oauthRegister";
/* 199 */     this.logger.entering(CLASS_NAME, "oauthRegister");
/* 200 */     Map<String, String> response = this.userService.oauthRegister(user);
/* 201 */     HttpStatus httpStatus = ((String)response.get("authenticated")).equalsIgnoreCase(Boolean.TRUE.toString()) ? HttpStatus.OK : HttpStatus.NOT_ACCEPTABLE;
/*     */ 
/*     */     
/* 204 */     this.logger.exiting(CLASS_NAME, "oauthRegister");
/* 205 */     return new ResponseEntity(response, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), httpStatus);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "POST", nickname = "login", value = "Local login for an exiting user in based on the provided username and password.", tags = {"ZenZefi Public API"})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "Login success."), @ApiResponse(code = 406, message = "Login error."), @ApiResponse(code = 412, message = "User already logged in."), @ApiResponse(code = 500, message = "Exception message")})
/*     */   @RequestMapping(value = {"/users/login"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<Map<String, String>> login(@ApiParam(value = "The user.", required = true) @RequestBody UserLoginRequest user, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 219 */     String METHOD_NAME = "login";
/* 220 */     this.logger.entering(CLASS_NAME, "login");
/* 221 */     addRequestMetaData(locale);
/* 222 */     Map<String, String> response = this.userService.login(user);
/* 223 */     HttpStatus httpStatus = getLoginResponseHttpStatus(response);
/* 224 */     this.logger.exiting(CLASS_NAME, "login");
/* 225 */     return new ResponseEntity(response, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), httpStatus);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "POST", nickname = "logout", value = "Log out a user from ZenZefi.", tags = {"ZenZefi Public API"})
/*     */   @ApiResponse(code = 200, message = "OK")
/*     */   @RequestMapping(value = {"/users/logout"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<HashMap<String, String>> logoutUser(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 235 */     String METHOD_NAME = "logoutUser";
/* 236 */     this.logger.entering(CLASS_NAME, "logoutUser");
/* 237 */     HashMap<String, String> logoutResponse = getLogoutResponse(locale, correlationid);
/* 238 */     this.logger.exiting(CLASS_NAME, "logoutUser");
/* 239 */     return new ResponseEntity(logoutResponse, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", nickname = "logoutUserSwitch", value = "Log out a user from ZenZefi if the username changes.")
/*     */   @ApiResponse(code = 200, message = "OK")
/*     */   @RequestMapping(value = {"/users/logoutOnUserSwitch"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<HashMap<String, String>> logoutOnUserSwitch(@ApiParam("The user.") @RequestParam(required = true) String userId, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 249 */     String METHOD_NAME = "logoutOnUserSwitch";
/* 250 */     this.logger.entering(CLASS_NAME, "logoutOnUserSwitch");
/* 251 */     HashMap<String, String> response = getLogoutOnUserSwitchResponse(userId, locale, correlationid);
/* 252 */     this.logger.exiting(CLASS_NAME, "logoutOnUserSwitch");
/*     */     
/* 254 */     return new ResponseEntity(response, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", nickname = "currentUser", value = "Checks if the current user is the default user. Endpoint used in the ZenZefi UI for automated logout.", tags = {"ZenZefi Public API"})
/*     */   @RequestMapping(value = {"/users/currentUser"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<UserDetailsWithSession> currentUser(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 263 */     String METHOD_NAME = "currentUser";
/* 264 */     this.logger.logEnteringToFile(CLASS_NAME, "currentUser");
/* 265 */     addRequestMetaData(locale);
/*     */     
/* 267 */     UserDetailsWithSession details = this.userService.getCurrentUserDetails();
/*     */     
/* 269 */     this.logger.logExitingToFile(CLASS_NAME, "currentUser");
/* 270 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(details);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", nickname = "currentLoginErrorMessages", value = "Login errors")
/*     */   @RequestMapping(value = {"/users/login/errors"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<List<String>> currenLoginMessages(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 278 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl()))
/* 279 */       .body(UserContext.getLogingErrorMessages());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "POST", nickname = "resetSessionTimeout", value = "Resets the session timeout for the current user session and returns the user details.")
/*     */   @RequestMapping(value = {"/users/currentUser/timer/reset"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<UserDetailsWithSession> resetSessionTimeout(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 287 */     String METHOD_NAME = "resetSessionTimeout";
/* 288 */     this.logger.logEnteringToFile(CLASS_NAME, "resetSessionTimeout");
/* 289 */     addRequestMetaData(locale);
/*     */ 
/*     */ 
/*     */     
/* 293 */     UserDetailsWithSession details = this.userService.getCurrentUserDetails();
/*     */     
/* 295 */     this.logger.log(Level.INFO, "000616", "User session has been reset", CLASS_NAME);
/* 296 */     this.logger.logExitingToFile(CLASS_NAME, "resetSessionTimeout");
/* 297 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(details);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\boundary\UsersResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */