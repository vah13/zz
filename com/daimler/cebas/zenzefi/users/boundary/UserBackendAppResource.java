/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.boundary;
/*     */ 
/*     */ import com.daimler.cebas.common.control.HttpHeaderFactory;
/*     */ import com.daimler.cebas.users.control.vo.CurrentUserDetails;
/*     */ import com.daimler.cebas.users.control.vo.UserData;
/*     */ import com.daimler.cebas.users.control.vo.UserDetailsWithSession;
/*     */ import com.daimler.cebas.users.control.vo.UserLoginRequest;
/*     */ import com.daimler.cebas.zenzefi.users.boundary.AbstractUsersResource;
/*     */ import com.daimler.cebas.zenzefi.users.boundary.UsersResource;
/*     */ import com.daimler.cebas.zenzefi.users.control.UserContext;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import io.swagger.annotations.ApiParam;
/*     */ import io.swagger.annotations.ApiResponse;
/*     */ import io.swagger.annotations.ApiResponses;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.context.annotation.Profile;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestHeader;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
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
/*     */ @RestController
/*     */ @Api(value = "Users API", tags = {"ZenZefi User Management"})
/*     */ @Profile({"AFTERSALES"})
/*     */ public class UserBackendAppResource
/*     */   extends AbstractUsersResource
/*     */ {
/*     */   private static final String APP_TRIGGERED_LOGIN = "/users/appTriggeredLogin";
/*  52 */   private static final String CLASS_NAME = UsersResource.class.getName();
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
/*     */   @ApiOperation(httpMethod = "POST", nickname = "appTriggeredlogin", value = "Application triggered login based on the provided username and password")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "Login successful."), @ApiResponse(code = 412, message = "User already logged in."), @ApiResponse(code = 500, message = "Exception message")})
/*     */   @RequestMapping(value = {"/users/appTriggeredLogin"}, method = {RequestMethod.POST})
/*     */   @Profile({"AFTERSALES"})
/*     */   public ResponseEntity<Map<String, String>> appTriggeredLogin(@ApiParam(value = "The user.", required = true) @RequestBody UserLoginRequest user, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/*  75 */     String METHOD_NAME = "appTriggeredLogin";
/*  76 */     this.logger.entering(CLASS_NAME, "appTriggeredLogin");
/*  77 */     addRequestMetaData(locale);
/*  78 */     Map<String, String> response = this.userService.login(user);
/*  79 */     HttpStatus httpStatus = getLoginResponseHttpStatus(response);
/*  80 */     this.logger.exiting(CLASS_NAME, "appTriggeredLogin");
/*  81 */     return new ResponseEntity(response, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), httpStatus);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", nickname = "allUsers", value = "Returns a list of users.")
/*     */   @RequestMapping(value = {"/users"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<List<UserData>> getUsers(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/*  89 */     String METHOD_NAME = "getUsers";
/*  90 */     return getUsers(locale, correlationid, "getUsers", CLASS_NAME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "DELETE", nickname = "deleteUsers", value = "Deletes user accounts by id. The ids must valid UUIDs.")
/*     */   @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")
/*     */   @RequestMapping(value = {"/users/{ids}"}, method = {RequestMethod.DELETE})
/*     */   public ResponseEntity<String> deleteUsers(@ApiParam(name = "ids", value = "Users ids, must be valid UUIDs, comma separated.", required = true) @PathVariable String[] ids, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 100 */     String METHOD_NAME = "deleteUsers";
/* 101 */     deleteAccounts(ids, locale, correlationid, "deleteUsers", CLASS_NAME);
/* 102 */     return new ResponseEntity((MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "POST", nickname = "logout", value = "Log out a user from ZenZefi.", tags = {"ZenZefi Public API"})
/*     */   @ApiResponse(code = 200, message = "OK")
/*     */   @RequestMapping(value = {"/users/logout"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<HashMap<String, String>> logoutUser(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 112 */     String METHOD_NAME = "logoutUser";
/* 113 */     this.logger.entering(CLASS_NAME, "logoutUser");
/* 114 */     HashMap<String, String> logoutResponse = getLogoutResponse(locale, correlationid);
/* 115 */     this.logger.exiting(CLASS_NAME, "logoutUser");
/* 116 */     return new ResponseEntity(logoutResponse, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", nickname = "currentUser", value = "Checks if the current user is the default user. Endpoint used in the ZenZefi UI for automated logout.")
/*     */   @RequestMapping(value = {"/users/currentUser"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<CurrentUserDetails> currentUser(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 124 */     String METHOD_NAME = "currentUser";
/* 125 */     this.logger.logEnteringToFile(CLASS_NAME, "currentUser");
/*     */     
/* 127 */     addRequestMetaData(locale);
/* 128 */     UserDetailsWithSession userDetailsWithSession = this.userService.getCurrentUserDetails();
/* 129 */     this.logger.logExitingToFile(CLASS_NAME, "currentUser");
/* 130 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(userDetailsWithSession);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", nickname = "currentLoginErrorMessages", value = "Login errors")
/*     */   @RequestMapping(value = {"/users/login/errors"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<List<String>> currenLoginMessages(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 138 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl()))
/* 139 */       .body(UserContext.getLogingErrorMessages());
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\boundary\UserBackendAppResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */