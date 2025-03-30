/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.boundary;
/*    */ 
/*    */ import com.daimler.cebas.common.control.HttpHeaderFactory;
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
/*    */ @Api(tags = {"ZenZefi Public API V3"}, description = "Exposed endpoints for external clients")
/*    */ public class UsersResourceV3
/*    */   extends AbstractUsersResource
/*    */ {
/* 38 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.users.boundary.UsersResourceV3.class.getName();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @ApiOperation(httpMethod = "POST", nickname = "loginV3", value = "Local login for an exiting user in based on the provided username and password.", tags = {"ZenZefi Public API V3"})
/*    */   @ApiResponses({@ApiResponse(code = 200, message = "Login successful."), @ApiResponse(code = 406, message = "Login error."), @ApiResponse(code = 412, message = "User already logged in."), @ApiResponse(code = 500, message = "Exception message")})
/*    */   @RequestMapping(value = {"/v3/users/login"}, method = {RequestMethod.POST})
/*    */   public ResponseEntity<Map<String, String>> loginV3(@ApiParam(value = "The user.", required = true) @RequestBody UserLoginRequest user, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 51 */     String METHOD_NAME = "loginV3";
/* 52 */     this.logger.entering(CLASS_NAME, "loginV3");
/* 53 */     this.logger.exiting(CLASS_NAME, "loginV3");
/* 54 */     return getLoginResponse(user, locale, correlationid);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @ApiOperation(httpMethod = "POST", nickname = "logoutV3", value = "Log out a user from ZenZefi.", tags = {"ZenZefi Public API V3"})
/*    */   @ApiResponse(code = 200, message = "OK")
/*    */   @RequestMapping(value = {"/v3/users/logout"}, method = {RequestMethod.POST})
/*    */   public ResponseEntity<HashMap<String, String>> logoutUserV3(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 64 */     String METHOD_NAME = "logoutUserV3";
/* 65 */     this.logger.entering(CLASS_NAME, "logoutUserV3");
/* 66 */     HashMap<String, String> logoutResponse = getLogoutResponse(locale, correlationid);
/* 67 */     this.logger.exiting(CLASS_NAME, "logoutUserV3");
/* 68 */     return new ResponseEntity(logoutResponse, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @ApiOperation(httpMethod = "GET", nickname = "currentUserV3", value = "Checks if the current user is the default user. Endpoint used in the ZenZefi UI for automated logout.", tags = {"ZenZefi Public API V3"})
/*    */   @RequestMapping(value = {"/v3/users/currentUser"}, method = {RequestMethod.GET})
/*    */   public ResponseEntity<UserDetailsWithSession> currentUserV3(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 76 */     String METHOD_NAME = "currentUserV3";
/* 77 */     this.logger.logEnteringToFile(CLASS_NAME, "currentUserV3");
/* 78 */     addRequestMetaData(locale);
/*    */     
/* 80 */     UserDetailsWithSession details = this.userService.getCurrentUserDetails();
/*    */     
/* 82 */     this.logger.logExitingToFile(CLASS_NAME, "currentUserV3");
/* 83 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(details);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\boundary\UsersResourceV3.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */