/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.users.boundary;
/*    */ 
/*    */ import com.daimler.cebas.common.control.HttpHeaderFactory;
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
/*    */ @Deprecated
/*    */ @RestController
/*    */ @Api(tags = {"ZenZefi Public API V1"}, description = "Exposed endpoints for external clients")
/*    */ public class UsersResourceV1
/*    */   extends AbstractUsersResource
/*    */ {
/* 38 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.users.boundary.UsersResourceV1.class.getName();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   @ApiOperation(httpMethod = "POST", nickname = "loginV1", value = "Local login for an exiting user in based on the provided username and password.", tags = {"ZenZefi Public API V1"})
/*    */   @ApiResponses({@ApiResponse(code = 200, message = "Login successful."), @ApiResponse(code = 406, message = "Login error."), @ApiResponse(code = 412, message = "User already logged in."), @ApiResponse(code = 500, message = "Exception message")})
/*    */   @RequestMapping(value = {"/v1/users/login"}, method = {RequestMethod.POST})
/*    */   public ResponseEntity<Map<String, String>> loginV1(@ApiParam(value = "The user.", required = true) @RequestBody UserLoginRequest user, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 52 */     String METHOD_NAME = "loginV1";
/* 53 */     this.logger.entering(CLASS_NAME, "loginV1");
/* 54 */     this.logger.exiting(CLASS_NAME, "loginV1");
/* 55 */     return getLoginResponse(user, locale, correlationid);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   @ApiOperation(httpMethod = "POST", nickname = "logoutV1", value = "Log out a user from ZenZefi.", tags = {"ZenZefi Public API V1"})
/*    */   @ApiResponse(code = 200, message = "OK")
/*    */   @RequestMapping(value = {"/v1/users/logout"}, method = {RequestMethod.POST})
/*    */   public ResponseEntity<HashMap<String, String>> logoutUserV1(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 66 */     String METHOD_NAME = "logoutUserV1";
/* 67 */     this.logger.entering(CLASS_NAME, "logoutUserV1");
/* 68 */     HashMap<String, String> logoutResponse = getLogoutResponse(locale, correlationid);
/* 69 */     this.logger.exiting(CLASS_NAME, "logoutUserV1");
/* 70 */     return new ResponseEntity(logoutResponse, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzef\\users\boundary\UsersResourceV1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */