/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.configuration.boundary;
/*     */ 
/*     */ import com.daimler.cebas.common.control.HttpHeaderFactory;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.vo.Enablement;
/*     */ import com.daimler.cebas.configuration.control.BusinessDivisionMapping;
/*     */ import com.daimler.cebas.configuration.control.PkiUrlProperty;
/*     */ import com.daimler.cebas.configuration.control.vo.RolePriorityConfiguration;
/*     */ import com.daimler.cebas.configuration.entity.Configuration;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.system.control.validation.vo.BusinessEnvironment;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.ZenZefiConfigurationsService;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.proxy.ProxyInput;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.proxy.ProxyOutput;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.proxy.ProxyTypeInput;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.proxy.ProxyWithType;
/*     */ import com.daimler.cebas.zenzefi.system.control.websocket.WebsocketController;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import io.swagger.annotations.ApiParam;
/*     */ import io.swagger.annotations.ApiResponse;
/*     */ import io.swagger.annotations.ApiResponses;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import javax.validation.Valid;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.PutMapping;
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
/*     */ @Api(value = "ZenZefi Configuration API", tags = {"ZenZefi Configuration Management"})
/*     */ public class ConfigurationsResource
/*     */ {
/*  60 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.configuration.boundary.ConfigurationsResource.class.getName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CONFIGURATIONS = "/configurations";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DETAILS_PANEL_STATE = "/configurations/detailsPanelState";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CERTIFICATES_COLUMN_VISIBILITY = "/configurations/certificatesColumnVisibility";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CERTIFICATES_COLUMN_ORDER = "/configurations/certificatesColumnOrder";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String PAGINATION_MAX_ROWS = "/configurations/maxRowsPerPage";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String APPLICATION_VERSION = "/configurations/applicationVersion";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String PKI_LOGOUT_URL = "/configurations/pkiLogoutUrl";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String PROXY = "/configurations/proxy";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String PROXY_TYPE = "/configurations/proxyType";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String PROXY_WITH_TYPE = "/configurations/proxyWithType";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String PKI_SCOPE = "/configurations/pkiScope";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String PKIY_SCOPE_KEY = "scope";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ZenZefiConfigurationsService configurationService;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MetadataManager requestMetaData;
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
/*     */   private WebsocketController wsController;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public ConfigurationsResource(ZenZefiConfigurationsService service, Logger logger, MetadataManager requestMetaData) {
/* 158 */     this.configurationService = service;
/* 159 */     this.logger = logger;
/* 160 */     this.requestMetaData = requestMetaData;
/*     */   }
/*     */   
/*     */   @Autowired(required = false)
/*     */   public void setWsController(WebsocketController wsController) {
/* 165 */     this.wsController = wsController;
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
/*     */   @ApiOperation(httpMethod = "GET", produces = "JSON", value = "Returns an array of general configurations, which are attached to default user. Endpoint used in the ZenZefi UI.")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = List.class), @ApiResponse(code = 500, message = "Internal server error")})
/*     */   @RequestMapping(value = {"/configurations/general"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<List<Configuration>> getGeneralConfigurations(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 184 */     String METHOD_NAME = "getGeneralConfigurations";
/* 185 */     this.logger.entering(CLASS_NAME, "getGeneralConfigurations");
/* 186 */     this.requestMetaData.setLocale(locale);
/* 187 */     List<Configuration> currentUserConfigurations = this.configurationService.getGeneralConfigurations();
/* 188 */     this.logger.exiting(CLASS_NAME, "getGeneralConfigurations");
/* 189 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(currentUserConfigurations);
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
/*     */   @ApiOperation(httpMethod = "GET", produces = "JSON", value = "Returns an array of configurations, which are attached to current user (Default or Registered). Endpoint used in the ZenZefi UI.")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = List.class), @ApiResponse(code = 500, message = "Internal server error")})
/*     */   @RequestMapping(value = {"/configurations"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<List<Configuration>> getUserConfigurations(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 208 */     String METHOD_NAME = "getUserConfigurations";
/* 209 */     this.logger.entering(CLASS_NAME, "getUserConfigurations");
/* 210 */     this.requestMetaData.setLocale(locale);
/* 211 */     List<Configuration> currentUserConfigurations = this.configurationService.getCurrentUserConfigurations();
/* 212 */     this.logger.exiting(CLASS_NAME, "getUserConfigurations");
/* 213 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(currentUserConfigurations);
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
/*     */   @ApiOperation(httpMethod = "PUT", produces = "JSON", value = "Update configuration. Endpoint used in the ZenZefi UI.")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = Configuration.class), @ApiResponse(code = 500, message = "Internal server error"), @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")})
/*     */   @RequestMapping(value = {"/configurations/{id}"}, method = {RequestMethod.PUT})
/*     */   public ResponseEntity<Configuration> updateConfiguration(@ApiParam(value = "The id of the configuration. Must be a valid UUID.", required = true) @PathVariable String id, @ApiParam(name = "configuration", value = "The configuration.", required = true) @RequestBody Configuration configuration, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 239 */     String METHOD_NAME = "updateConfiguration";
/* 240 */     this.logger.entering(CLASS_NAME, "updateConfiguration");
/* 241 */     this.requestMetaData.setLocale(locale);
/* 242 */     Configuration updatedConfiguration = this.configurationService.updateConfiguration(configuration);
/* 243 */     this.wsController.triggerClientUpdate();
/* 244 */     this.logger.exiting(CLASS_NAME, "updateConfiguration");
/*     */     
/* 246 */     return new ResponseEntity(updatedConfiguration, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
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
/*     */   @ApiOperation(httpMethod = "GET", produces = "JSON", value = "Returns an array of user roles, attached to current user (default or registered). Endpoint used in the ZenZefi UI.")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = List.class), @ApiResponse(code = 500, message = "Internal server error"), @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")})
/*     */   @RequestMapping(value = {"/configurations/rolesPriority"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<List<RolePriorityConfiguration>> getRolePriorityConfigurationOfCurrentUser(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 266 */     String METHOD_NAME = "getRolePriorityConfigurationOfCurrentUser";
/* 267 */     this.logger.entering(CLASS_NAME, "getRolePriorityConfigurationOfCurrentUser");
/* 268 */     this.requestMetaData.setLocale(locale);
/*     */     
/* 270 */     List<RolePriorityConfiguration> rolesPriorityConfiguration = this.configurationService.getRolesPriorityConfiguration();
/* 271 */     this.logger.exiting(CLASS_NAME, "getRolePriorityConfigurationOfCurrentUser");
/* 272 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl()))
/* 273 */       .body(rolesPriorityConfiguration);
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
/*     */   @ApiOperation(httpMethod = "POST", produces = "JSON", value = "Updates user roles configuration, for current user (default or registered). Endpoint used in the ZenZefi UI.")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = Configuration.class), @ApiResponse(code = 500, message = "Internal server error")})
/*     */   @RequestMapping(value = {"/configurations/rolesPriority"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<Configuration> updateConfigurationRolesPriorityOfCurrentUser(@ApiParam(name = "configuration", value = "The configuration.", required = true) @RequestBody Configuration configuration, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 295 */     String METHOD_NAME = "updateConfigurationRolesPriorityOfCurrentUser";
/* 296 */     this.logger.entering(CLASS_NAME, "updateConfigurationRolesPriorityOfCurrentUser");
/* 297 */     this.requestMetaData.setLocale(locale);
/* 298 */     Configuration userRolesConfiguration = this.configurationService.getUserRolesConfiguration();
/* 299 */     userRolesConfiguration.setConfigValue(configuration.getConfigValue());
/* 300 */     this.configurationService.updateConfiguration(userRolesConfiguration);
/* 301 */     if (this.wsController != null) {
/* 302 */       this.wsController.triggerClientUpdate();
/*     */     }
/* 304 */     this.logger.exiting(CLASS_NAME, "updateConfigurationRolesPriorityOfCurrentUser");
/* 305 */     return new ResponseEntity(userRolesConfiguration, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
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
/*     */   @ApiOperation(httpMethod = "POST", nickname = "setDetailsPanelState", value = "Sets the details pane state of the certificates table for the current logged user. Endpoint used in the ZenZefi UI.")
/*     */   @RequestMapping(value = {"/configurations/detailsPanelState/{state}"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<HashMap<String, String>> setDetailsPanelState(@ApiParam(name = "state", value = "The details pane state. Open or closed.", required = true) @PathVariable String state, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 325 */     String METHOD_NAME = "setDetailsPanelState";
/* 326 */     this.logger.entering(CLASS_NAME, "setDetailsPanelState");
/* 327 */     this.requestMetaData.setLocale(locale);
/* 328 */     HashMap<String, String> response = new HashMap<>();
/* 329 */     this.configurationService.setDetailsPanelState(state);
/* 330 */     this.logger.exiting(CLASS_NAME, "setDetailsPanelState");
/* 331 */     return new ResponseEntity(response, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
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
/*     */   @ApiOperation(httpMethod = "GET", nickname = "getDetailsPanelState", value = "Gets the details pane state of the certificates table for the current logged user. Endpoint used in the ZenZefi UI.")
/*     */   @RequestMapping(value = {"/configurations/detailsPanelState"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<HashMap<String, String>> getDetailsPanelState(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 348 */     String METHOD_NAME = "getDetailsPanelState";
/* 349 */     this.logger.entering(CLASS_NAME, "getDetailsPanelState");
/* 350 */     this.requestMetaData.setLocale(locale);
/* 351 */     HashMap<String, String> response = new HashMap<>();
/* 352 */     String panelState = this.configurationService.getDetailsPanelState();
/* 353 */     response.put("message", panelState);
/* 354 */     this.logger.exiting(CLASS_NAME, "getDetailsPanelState");
/* 355 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(response);
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
/*     */   @ApiOperation(httpMethod = "GET", nickname = "getCertificatesColumnVisibility", value = "Gets the column visibility of the certificates table for the current logged user. Endpoint used in the ZenZefi UI.")
/*     */   @RequestMapping(value = {"/configurations/certificatesColumnVisibility"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<HashMap<String, String>> getCertificatesTableColumnVisibility(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 372 */     String METHOD_NAME = "getCertificatesTableColumnVisibility";
/* 373 */     this.logger.entering(CLASS_NAME, "getCertificatesTableColumnVisibility");
/* 374 */     this.requestMetaData.setLocale(locale);
/* 375 */     HashMap<String, String> response = new HashMap<>();
/* 376 */     String columnVisibility = this.configurationService.getCertificatesColumnVisibility();
/* 377 */     response.put("message", columnVisibility);
/* 378 */     this.logger.exiting(CLASS_NAME, "getCertificatesTableColumnVisibility");
/* 379 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(response);
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
/*     */   @ApiOperation(httpMethod = "POST", nickname = "setCertificatesColumnVisibility", value = "Sets the column visibility of the certificates table for the current logged user. Endpoint used in the ZenZefi UI.")
/*     */   @RequestMapping(value = {"/configurations/certificatesColumnVisibility"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<HashMap<String, String>> setCertificatesTableColumnVisibility(@ApiParam(name = "columnVisibilities", value = "Column visibilities.", required = true) @RequestBody Map<String, String> columnVisibilities, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 399 */     String METHOD_NAME = "setCertificatesTableColumnVisibility";
/* 400 */     this.logger.entering(CLASS_NAME, "setCertificatesTableColumnVisibility");
/* 401 */     this.requestMetaData.setLocale(locale);
/* 402 */     HashMap<String, String> response = new HashMap<>();
/* 403 */     String visibilities = columnVisibilities.get("message");
/* 404 */     this.configurationService.setCertificatesColumnVisibility(visibilities);
/* 405 */     this.logger.exiting(CLASS_NAME, "setCertificatesTableColumnVisibility");
/* 406 */     return new ResponseEntity(response, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
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
/*     */   @ApiOperation(httpMethod = "POST", nickname = "setGertificatesColumnOrder", value = "Sets the column order of the certificates table for the current logged user. Endpoint used in the ZenZefi UI.")
/*     */   @RequestMapping(value = {"/configurations/certificatesColumnOrder"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<HashMap<String, String>> setCertificatesTableColumnOrder(@ApiParam(name = "columnOrder", value = "The column order.", required = true) @RequestBody Map<String, String> columnOrder, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 426 */     String METHOD_NAME = "setCertificatesTableColumnOrder";
/* 427 */     this.logger.entering(CLASS_NAME, "setCertificatesTableColumnOrder");
/* 428 */     this.requestMetaData.setLocale(locale);
/* 429 */     HashMap<String, String> response = new HashMap<>();
/* 430 */     String order = columnOrder.get("message");
/* 431 */     this.configurationService.setCertificatesColumnOrder(order);
/* 432 */     this.logger.exiting(CLASS_NAME, "setCertificatesTableColumnOrder");
/* 433 */     return new ResponseEntity(response, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
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
/*     */   @ApiOperation(httpMethod = "GET", nickname = "getCertificatesColumnOrder", value = "Gets the column order of the certificates table for the current logged user. Endpoint used in the ZenZefi UI.")
/*     */   @RequestMapping(value = {"/configurations/certificatesColumnOrder"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<HashMap<String, String>> getCertificatesTableColumnOrder(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 450 */     String METHOD_NAME = "getCertificatesTableColumnOrder";
/* 451 */     this.logger.entering(CLASS_NAME, "getCertificatesTableColumnOrder");
/* 452 */     this.requestMetaData.setLocale(locale);
/* 453 */     HashMap<String, String> response = new HashMap<>();
/* 454 */     response.put("message", this.configurationService.getCertificatesColumnOrder());
/* 455 */     this.logger.exiting(CLASS_NAME, "getCertificatesTableColumnOrder");
/* 456 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(response);
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
/*     */   @ApiOperation(httpMethod = "GET", nickname = "getApplicationVersion", value = "Returns the application version.")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = String.class)})
/*     */   @RequestMapping(value = {"/configurations/applicationVersion"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<String> getApplicationVersion(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 474 */     String METHOD_NAME = "getApplicationVersion";
/* 475 */     this.logger.entering(CLASS_NAME, "getApplicationVersion");
/* 476 */     this.requestMetaData.setLocale(locale);
/* 477 */     String applicationVersion = this.configurationService.getApplicationVersion();
/* 478 */     String pkiEnv = this.env.getProperty(PkiUrlProperty.PKI_ENVIRONMENT.getProperty());
/* 479 */     String pkiBaseUrl = this.env.getProperty(PkiUrlProperty.PKI_BASE_URL.getProperty());
/*     */     
/* 481 */     BusinessEnvironment envValue = BusinessEnvironment.valueOf(this.env.getProperty("BUSINESS_ENVIRONMENT"));
/* 482 */     this.logger.exiting(CLASS_NAME, "getApplicationVersion");
/* 483 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl()))
/* 484 */       .body(applicationVersion + "_" + BusinessDivisionMapping.getMapping(envValue, pkiEnv, pkiBaseUrl));
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
/*     */   @ApiOperation(httpMethod = "GET", nickname = "getPKILogoutURL", value = "Returns the PKI Logout URL.")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = String.class)})
/*     */   @RequestMapping(value = {"/configurations/pkiLogoutUrl"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<String> getPKILogoutURL(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 502 */     String METHOD_NAME = "getApplicationVersion";
/* 503 */     this.logger.entering(CLASS_NAME, "getApplicationVersion");
/* 504 */     this.requestMetaData.setLocale(locale);
/* 505 */     String logoutUrl = this.configurationService.getPKILogoutUrlProperty();
/* 506 */     this.logger.exiting(CLASS_NAME, "getApplicationVersion");
/* 507 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(logoutUrl);
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
/*     */   @ApiOperation(httpMethod = "POST", nickname = "setProxy", value = "Sets the server proxy.")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = String.class), @ApiResponse(code = 400, message = "Only the default user can set the proxy", response = String.class)})
/*     */   @RequestMapping(value = {"/configurations/proxy"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<?> setProxy(@ApiParam(name = "proxy", value = "Proxy value.", required = true) @RequestBody ProxyInput proxy, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 529 */     String METHOD_NAME = "setProxy";
/* 530 */     this.logger.entering(CLASS_NAME, "setProxy");
/* 531 */     ResponseEntity<?> response = null;
/* 532 */     if (isProxySettingEnabled()) {
/* 533 */       this.requestMetaData.setLocale(locale);
/* 534 */       this.configurationService.setProxy(proxy);
/* 535 */       response = ResponseEntity.noContent().headers(HttpHeaderFactory.buildHeaderCacheControl()).build();
/*     */     } else {
/* 537 */       response = createAndLogForbiddenResponse();
/*     */     } 
/* 539 */     if (this.wsController != null) {
/* 540 */       this.wsController.triggerClientUpdate();
/*     */     }
/* 542 */     this.logger.exiting(CLASS_NAME, "setProxy");
/* 543 */     return response;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResponseEntity createAndLogForbiddenResponse() {
/* 553 */     this.logger.log(Level.WARNING, "000538", "Blocked attempt to configure proxy because it's disabled", CLASS_NAME);
/*     */     
/* 555 */     return new ResponseEntity("Proxy configuration is disabled.", HttpStatus.FORBIDDEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isProxySettingEnabled() {
/* 564 */     String isProxyEnabled = this.env.getProperty("ENABLE_PROXY_SETTINGS");
/* 565 */     return "true".equals(isProxyEnabled);
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
/*     */   @ApiOperation(httpMethod = "GET", nickname = "getProxy", value = "Gets the server proxy.")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = String.class), @ApiResponse(code = 400, message = "Only the default user can set the proxy", response = String.class)})
/*     */   @RequestMapping(value = {"/configurations/proxy"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<ProxyOutput> getProxy(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 584 */     String METHOD_NAME = "getProxy";
/* 585 */     this.logger.entering(CLASS_NAME, "getProxy");
/* 586 */     this.requestMetaData.setLocale(locale);
/* 587 */     ProxyOutput proxy = this.configurationService.getProxy();
/* 588 */     this.logger.exiting(CLASS_NAME, "getProxy");
/* 589 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(proxy);
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
/*     */   @ApiOperation(httpMethod = "GET", value = "Gets the server proxy type.")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = String.class), @ApiResponse(code = 400, message = "Only the default user can get the proxy type", response = String.class)})
/*     */   @RequestMapping(value = {"/configurations/proxyType"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<HashMap<String, String>> getProxyType(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 608 */     String METHOD_NAME = "getProxyType";
/* 609 */     this.logger.entering(CLASS_NAME, "getProxyType");
/* 610 */     this.requestMetaData.setLocale(locale);
/* 611 */     String proxyType = this.configurationService.getProxyType();
/* 612 */     HashMap<String, String> response = new HashMap<>();
/* 613 */     response.put("message", proxyType);
/* 614 */     this.logger.exiting(CLASS_NAME, "getProxyType");
/* 615 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(response);
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
/*     */   @ApiOperation(httpMethod = "POST", value = "Sets the server proxy type.")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = String.class), @ApiResponse(code = 400, message = "Only the default user can set the proxy type", response = String.class)})
/*     */   @RequestMapping(value = {"/configurations/proxyType"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<String> setProxyType(@ApiParam(name = "proxyType", value = "Proxy type value.", required = true) @RequestBody ProxyTypeInput proxy, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 638 */     String METHOD_NAME = "setProxyType";
/* 639 */     this.logger.entering(CLASS_NAME, "setProxyType");
/* 640 */     ResponseEntity<String> response = null;
/* 641 */     if (isProxySettingEnabled()) {
/* 642 */       this.requestMetaData.setLocale(locale);
/* 643 */       this.configurationService.setProxyType(proxy);
/* 644 */       response = ResponseEntity.noContent().headers(HttpHeaderFactory.buildHeaderCacheControl()).build();
/*     */     } else {
/* 646 */       response = createAndLogForbiddenResponse();
/*     */     } 
/* 648 */     if (this.wsController != null) {
/* 649 */       this.wsController.triggerClientUpdate();
/*     */     }
/* 651 */     this.logger.exiting(CLASS_NAME, "setProxyType");
/* 652 */     return response;
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
/*     */   @ApiOperation(httpMethod = "POST", value = "Sets the server proxy and the proxy type.")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = String.class), @ApiResponse(code = 400, message = "Only the default user can set the proxy", response = String.class)})
/*     */   @RequestMapping(value = {"/configurations/proxyWithType"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<String> setProxyWithType(@ApiParam(name = "proxy", value = "Proxy value.", required = true) @RequestBody ProxyWithType proxy, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 675 */     String METHOD_NAME = "setProxyWithType";
/* 676 */     this.logger.entering(CLASS_NAME, "setProxyWithType");
/* 677 */     ResponseEntity<String> response = null;
/* 678 */     if (isProxySettingEnabled()) {
/* 679 */       this.requestMetaData.setLocale(locale);
/* 680 */       this.configurationService.setWithProxyType(proxy.getProxyInput(), proxy.getProxyType());
/* 681 */       response = ResponseEntity.noContent().headers(HttpHeaderFactory.buildHeaderCacheControl()).build();
/*     */     } else {
/* 683 */       response = createAndLogForbiddenResponse();
/*     */     } 
/* 685 */     if (this.wsController != null) {
/* 686 */       this.wsController.triggerClientUpdate();
/*     */     }
/* 688 */     this.logger.exiting(CLASS_NAME, "setProxyWithType");
/* 689 */     return response;
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
/*     */   @ApiOperation(httpMethod = "GET", nickname = "getPaginationMaxRows", value = "Gets the max rows for pagination. Endpoint used in the ZenZefi UI, in the logs table.")
/*     */   @RequestMapping(value = {"/configurations/maxRowsPerPage"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<String> getMaxRowsPerPage(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 706 */     String METHOD_NAME = "getMaxRowsPerPage";
/* 707 */     this.logger.entering(CLASS_NAME, "getMaxRowsPerPage");
/* 708 */     this.requestMetaData.setLocale(locale);
/* 709 */     String maxRowsPerPage = this.configurationService.getPaginationMaxRowsPerPage();
/* 710 */     this.logger.exiting(CLASS_NAME, "getMaxRowsPerPage");
/* 711 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(maxRowsPerPage);
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
/*     */   @ApiOperation(httpMethod = "POST", nickname = "updatePaginationMaxRows", value = "Updates the max rows for pagination. Endpoint used in the ZenZefi UI, in the logs table.")
/*     */   @RequestMapping(value = {"/configurations/maxRowsPerPage"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<Void> updateMaxRowsPerPage(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam(value = "The maximum number of rows for pagination. Endpoint used in the ZenZefi UI, in the logs table. Valid values: integer values.", required = true) @RequestBody String maxRowsPerPage) {
/* 731 */     String METHOD_NAME = "updateMaxRowsPerPage";
/* 732 */     this.logger.entering(CLASS_NAME, "updateMaxRowsPerPage");
/* 733 */     this.requestMetaData.setLocale(locale);
/* 734 */     this.configurationService.updatePaginationMaxRowsPerPage(maxRowsPerPage);
/* 735 */     this.logger.exiting(CLASS_NAME, "updateMaxRowsPerPage");
/* 736 */     return ResponseEntity.noContent().headers(HttpHeaderFactory.buildHeaderCacheControl()).build();
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
/*     */   @ApiOperation(httpMethod = "GET", nickname = "activeProfiles", value = "Returns current active profiles")
/*     */   @RequestMapping(value = {"/configurations/activeProfiles"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<String[]> activeProfiles(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 753 */     String METHOD_NAME = "activeProfiles";
/* 754 */     this.logger.entering(CLASS_NAME, "activeProfiles");
/* 755 */     this.requestMetaData.setLocale(locale);
/* 756 */     this.logger.exiting(CLASS_NAME, "activeProfiles");
/* 757 */     return ResponseEntity.ok(this.env.getActiveProfiles());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "PUT", nickname = "updateAutologout", value = "Activates or deactivates automatically logout.")
/*     */   @PutMapping({"/configurations/autologout"})
/*     */   public ResponseEntity<String> updateAutologout(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @RequestBody @Valid Enablement activate) {
/* 766 */     String METHOD_NAME = "updateAutologout";
/* 767 */     this.logger.logEnteringToFile(CLASS_NAME, "updateAutologout");
/* 768 */     this.requestMetaData.setLocale(locale);
/*     */     
/* 770 */     this.configurationService.updateAutologout(activate.isEnable());
/* 771 */     this.wsController.triggerClientUpdate();
/*     */     
/* 773 */     String response = activate.isEnable() ? "Autologout is activated" : "Autologout is deactivated";
/* 774 */     this.logger.log(Level.INFO, "000617", response, CLASS_NAME);
/* 775 */     this.logger.logExitingToFile(CLASS_NAME, "updateAutologout");
/* 776 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(response);
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
/*     */   @ApiOperation(httpMethod = "GET", value = "Gets the pki scope setting.")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = String.class), @ApiResponse(code = 400, message = "Only the default user can get the pki scope", response = String.class)})
/*     */   @RequestMapping(value = {"/configurations/pkiScope"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<HashMap<String, Boolean>> getPkiScope(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 795 */     HashMap<String, Boolean> response = new HashMap<>();
/* 796 */     response.put("message", Boolean.valueOf(this.configurationService.getPkiscope()));
/* 797 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(response);
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
/*     */   @ApiOperation(httpMethod = "POST", value = "Sets the pki scope setting.")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = String.class), @ApiResponse(code = 400, message = "Only the default user can set the proxy type", response = String.class)})
/*     */   @RequestMapping(value = {"/configurations/pkiScope"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<String> setPkiScope(@ApiParam(name = "scope", value = "The scope as boolean value.", required = true, example = "{scope: true}") @RequestBody Map<String, Boolean> scope, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 819 */     this.requestMetaData.setLocale(locale);
/*     */     
/* 821 */     if (!scope.containsKey("scope") || scope.get("scope") == null) {
/* 822 */       this.logger.log(Level.WARNING, "000692", "Blocked attempt to configure proxy because it's disabled", CLASS_NAME);
/*     */       
/* 824 */       return new ResponseEntity("No scope value specified. Please provide a value in the form: {\"scope\": true}.", HttpStatus.BAD_REQUEST);
/*     */     } 
/* 826 */     this.configurationService.setPkiscope(((Boolean)scope.get("scope")).booleanValue());
/*     */     
/* 828 */     if (this.wsController != null) {
/* 829 */       this.wsController.triggerClientUpdate();
/*     */     }
/* 831 */     return ResponseEntity.noContent().headers(HttpHeaderFactory.buildHeaderCacheControl()).build();
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\configuration\boundary\ConfigurationsResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */