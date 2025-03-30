/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.boundary;
/*    */ 
/*    */ import com.daimler.cebas.common.control.HttpHeaderFactory;
/*    */ import com.daimler.cebas.users.control.vo.CurrentUserDetails;
/*    */ import com.daimler.cebas.users.control.vo.UserDetailsWithSession;
/*    */ import com.daimler.cebas.users.control.vo.UserLoginRequest;
/*    */ import com.daimler.cebas.zenzefi.users.boundary.AbstractUsersResource;
/*    */ import io.swagger.annotations.Api;
/*    */ import io.swagger.annotations.ApiOperation;
/*    */ import io.swagger.annotations.ApiParam;
/*    */ import io.swagger.annotations.ApiResponse;
/*    */ import io.swagger.annotations.ApiResponses;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.http.ResponseEntity;
/*    */ import org.springframework.util.MultiValueMap;
/*    */ import org.springframework.web.bind.annotation.RequestBody;
/*    */ import org.springframework.web.bind.annotation.RequestHeader;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RequestMethod;
/*    */ import org.springframework.web.bind.annotation.RestController;
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
/*    */ @RestController
/*    */ @Api(tags = {"ZenZefi Public API V2"}, description = "Exposed endpoints for external clients")
/*    */ public class UsersResourceV2
/*    */   extends AbstractUsersResource
/*    */ {
/* 39 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.users.boundary.UsersResourceV2.class.getName();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @ApiOperation(httpMethod = "POST", nickname = "loginV2", value = "Local login for an exiting user in based on the provided username and password.", tags = {"ZenZefi Public API V2"})
/*    */   @ApiResponses({@ApiResponse(code = 200, message = "Login successful."), @ApiResponse(code = 406, message = "Login error."), @ApiResponse(code = 412, message = "User already logged in."), @ApiResponse(code = 500, message = "Exception message")})
/*    */   @RequestMapping(value = {"/v2/users/login"}, method = {RequestMethod.POST})
/*    */   public ResponseEntity<Map<String, String>> loginV2(@ApiParam(value = "The user.", required = true) @RequestBody UserLoginRequest user, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 52 */     String METHOD_NAME = "loginV2";
/* 53 */     this.logger.entering(CLASS_NAME, "loginV2");
/* 54 */     this.logger.exiting(CLASS_NAME, "loginV2");
/* 55 */     return getLoginResponse(user, locale, correlationid);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @ApiOperation(httpMethod = "POST", nickname = "logoutV2", value = "Log out a user from ZenZefi.", tags = {"ZenZefi Public API V2"})
/*    */   @ApiResponse(code = 200, message = "OK")
/*    */   @RequestMapping(value = {"/v2/users/logout"}, method = {RequestMethod.POST})
/*    */   public ResponseEntity<HashMap<String, String>> logoutUserV2(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 65 */     String METHOD_NAME = "logoutUserV2";
/* 66 */     this.logger.entering(CLASS_NAME, "logoutUserV2");
/* 67 */     HashMap<String, String> logoutResponse = getLogoutResponse(locale, correlationid);
/* 68 */     this.logger.exiting(CLASS_NAME, "logoutUserV2");
/* 69 */     return new ResponseEntity(logoutResponse, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @ApiOperation(httpMethod = "GET", nickname = "currentUserV2", value = "Checks if the current user is the default user. Endpoint used in the ZenZefi UI for automated logout.", tags = {"ZenZefi Public API V2"})
/*    */   @RequestMapping(value = {"/v2/users/currentUser"}, method = {RequestMethod.GET})
/*    */   public ResponseEntity<CurrentUserDetails> currentUserV2(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 78 */     String METHOD_NAME = "currentUserV2";
/* 79 */     this.logger.logEnteringToFile(CLASS_NAME, "currentUserV2");
/* 80 */     addRequestMetaData(locale);
/* 81 */     UserDetailsWithSession userDetailsWithSession = this.userService.getCurrentUserDetails();
/* 82 */     this.logger.logExitingToFile(CLASS_NAME, "currentUserV2");
/* 83 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(userDetailsWithSession);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\boundary\UsersResourceV2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */