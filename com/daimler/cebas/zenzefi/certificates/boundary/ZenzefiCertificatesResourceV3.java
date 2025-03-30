/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.boundary;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*     */ import com.daimler.cebas.certificates.control.vo.RootOrBackend;
/*     */ import com.daimler.cebas.certificates.control.vo.RootOrBackendResult;
/*     */ import com.daimler.cebas.certificates.control.vo.UpdateCertificateMetrics;
/*     */ import com.daimler.cebas.certificates.control.zkNoSupport.ZkNoMappingResult;
/*     */ import com.daimler.cebas.common.control.HttpHeaderFactory;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.vo.Enablement;
/*     */ import com.daimler.cebas.common.entity.Versioned;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV3;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.AbstractZenZefiCertificatesService;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.ZenZefiCertificatesService;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import io.swagger.annotations.ApiParam;
/*     */ import io.swagger.annotations.ApiResponse;
/*     */ import io.swagger.annotations.ApiResponses;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ @RestController
/*     */ @Api(tags = {"ZenZefi Public API V3"}, description = "Exposed endpoints for external clients")
/*     */ public class ZenzefiCertificatesResourceV3
/*     */   extends AbstractZenZefiCertificatesResourceV3<ZenZefiCertificatesService>
/*     */ {
/*  57 */   private static final Logger LOG = Logger.getLogger(com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV3.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV3.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String VERSION = "/v3";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public ZenzefiCertificatesResourceV3(ZenZefiCertificatesService certificateService, Logger logger, CertificatesConfiguration certificateProfileConfiguration, MetadataManager requestMetaData) {
/*  83 */     super((AbstractZenZefiCertificatesService)certificateService, logger, certificateProfileConfiguration, requestMetaData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", value = "Returns the system integrity check XML report. Endpoint used only in the ZenZefi UI.", nickname = "getIntegrityCheckLogFileV3", produces = "application/xml", response = File.class, tags = {"ZenZefi Public API V3"})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "System integrity check xml report not found"), @ApiResponse(code = 500, message = "Internal server error")})
/*     */   @RequestMapping(value = {"/v3/certificates/checkSystemIntegrityLog"}, method = {RequestMethod.GET}, produces = {"application/xml"})
/*     */   public ResponseEntity<String> getIntegrityCheckLogFileV3(HttpServletResponse response, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/*  96 */     String METHOD_NAME = "getIntegrityCheckLogFileV3";
/*  97 */     this.logger.entering(CLASS_NAME, "getIntegrityCheckLogFileV3");
/*  98 */     addRequestMetaData(locale);
/*  99 */     String INTEGRITY_CHECK_FILE_NAME = "system_integrity_check_report.xml";
/*     */     
/* 101 */     response.setStatus(200);
/* 102 */     response.addHeader("Content-Disposition", "attachment; filename=\"system_integrity_check_report.xml\"");
/* 103 */     response.addHeader("Cache-Control", "no-cache, no-store");
/*     */     try {
/* 105 */       ((ZenZefiCertificatesService)this.certificateService).writeSystemIntegrityReportToOutputStream((OutputStream)response.getOutputStream());
/* 106 */     } catch (IOException e) {
/* 107 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 108 */       this.logger.logWithTranslation(Level.SEVERE, "000062X", "integrityCheckReportError", new String[] { e
/* 109 */             .getMessage() }, CLASS_NAME);
/* 110 */       new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
/*     */     } 
/* 112 */     this.logger.log(Level.INFO, "000447", this.requestMetaData
/* 113 */         .getEnglishMessage("checkSystemIntegrityLog"), 
/* 114 */         getClass().getSimpleName());
/* 115 */     this.logger.exiting(CLASS_NAME, "getIntegrityCheckLogFileV3");
/* 116 */     return new ResponseEntity((MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "POST", value = "Perform full update of the certificates. All certificates the user has rights for are requested independently of the certificate renewal time.", nickname = "updateFullCertificatesV3", tags = {"ZenZefi Public API V3"})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Registered user needs to be logged in."), @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")})
/*     */   @RequestMapping(value = {"/v3/certificates/update/full"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<Map<String, String>> updateFullCertificatesV3(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam(value = "Client id which identifies the client that makes a request to the server.", required = false) @RequestHeader(value = "X-Client-ID", required = false) String clientId) {
/* 129 */     String METHOD_NAME = "updateFullCertificatesV3";
/* 130 */     this.logger.entering(CLASS_NAME, "updateFullCertificatesV3");
/* 131 */     addRequestMetaData(locale);
/* 132 */     boolean hasToken = ((ZenZefiCertificatesService)this.certificateService).fullCertificatesUpdate(clientId);
/* 133 */     Map<String, String> response = new HashMap<>();
/* 134 */     if (hasToken) {
/* 135 */       response.put("message", "full update started");
/* 136 */       response.put("monitoring", "/certificates/update/metrics");
/*     */     } else {
/* 138 */       this.logger.log(Level.INFO, "000523", "Full update not started: UNAUTHORIZED", 
/* 139 */           getClass().getSimpleName());
/* 140 */       response.put("message", "full update not started: UNAUTHORIZED");
/*     */     } 
/* 142 */     this.logger.exiting(CLASS_NAME, "updateFullCertificatesV3");
/* 143 */     return new ResponseEntity(response, hasToken ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "POST", value = "Perform differential update of the certificates. Only those certificates in the user-specific certificate store.", nickname = "updateDifferentialCertificatesV3", tags = {"ZenZefi Public API V3"})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Registered user needs to be logged in."), @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")})
/*     */   @RequestMapping(value = {"/v3/certificates/update/differential"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<Map<String, String>> updateDifferentialCertificatesV3(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam(value = "Client id which identifies the client that makes a request to the server.", required = false) @RequestHeader(value = "X-Client-ID", required = false) String clientId) {
/* 156 */     String METHOD_NAME = "updateDifferentialCertificatesV3";
/* 157 */     this.logger.entering(CLASS_NAME, "updateDifferentialCertificatesV3");
/* 158 */     addRequestMetaData(locale);
/* 159 */     boolean hasToken = ((ZenZefiCertificatesService)this.certificateService).differentialCertificatesUpdate(clientId);
/* 160 */     Map<String, String> response = new HashMap<>();
/* 161 */     if (hasToken) {
/* 162 */       response.put("message", "differential update started");
/* 163 */       response.put("monitoring", "/certificates/update/metrics");
/*     */     } else {
/* 165 */       this.logger.log(Level.INFO, "000524", "Differential update not started: UNAUTHORIZED", 
/* 166 */           getClass().getSimpleName());
/* 167 */       response.put("message", "differential update not started: UNAUTHORIZED");
/*     */     } 
/* 169 */     this.logger.exiting(CLASS_NAME, "updateDifferentialCertificatesV3");
/* 170 */     return new ResponseEntity(response, hasToken ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", value = "Returns the metrics during the certificates update process", nickname = "updateCertificatesMetricsV3", tags = {"ZenZefi Public API V3"})
/*     */   @RequestMapping(value = {"/v3/certificates/update/metrics"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<UpdateCertificateMetrics> updateCertificatesMetricsV3(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam(value = "Client id which identifies the client that makes a request to the server.", required = false) @RequestHeader(value = "X-Client-ID", required = false) String clientId, HttpServletResponse httpServletResponse) {
/* 182 */     String METHOD_NAME = "updateCertificatesMetricsV3";
/* 183 */     this.logger.entering(CLASS_NAME, "updateCertificatesMetricsV3");
/* 184 */     addRequestMetaData(locale);
/*     */     
/* 186 */     UpdateCertificateMetrics result = ((ZenZefiCertificatesService)this.certificateService).getUpdateCertificatesMetrics();
/*     */     
/* 188 */     this.logger.exiting(CLASS_NAME, "updateCertificatesMetricsV3");
/* 189 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "POST", value = "Enable/Disable extended validation", nickname = "enableExtendedValidationV3", tags = {"ZenZefi Public API V3"})
/*     */   @RequestMapping(value = {"/v3/certificates/extendedValidation"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<String> enableExtendedValidationV3(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam(value = "Client id which identifies the client that makes a request to the server.", required = false) @RequestHeader(value = "X-Client-ID", required = false) String clientId, @RequestBody Enablement enablement) {
/* 200 */     String METHOD_NAME = "enableExtendedValidationV3";
/* 201 */     this.logger.entering(CLASS_NAME, "enableExtendedValidationV3");
/* 202 */     addRequestMetaData(locale);
/* 203 */     String state = ((ZenZefiCertificatesService)this.certificateService).enableExtendedValidation(enablement);
/* 204 */     this.logger.exiting(CLASS_NAME, "enableExtendedValidationV3");
/* 205 */     return ResponseEntity.of(Optional.of("Extended validation set to: " + state));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", value = "Returns the root and backend certificates for the currently logged in user.", nickname = "getRootAndBackendsV3", tags = {"ZenZefi Public API V3"})
/*     */   @RequestMapping(value = {"/v3/certificates/rootsAndBackends"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<List<RootOrBackendResult>> getRootAndBackendsV3(@ApiParam(value = "Which certificates should be retrieved: BACKEND, ROOT. Parameter is optional, if none is provided all are returned.", required = false) @RequestParam(required = false) RootOrBackend rootOrBackend, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam(value = "Client id which identifies the client that makes a request to the server.", required = false) @RequestHeader(value = "X-Client-ID", required = false) String clientId, HttpServletResponse httpServletResponse) {
/* 217 */     String METHOD_NAME = "getRootAndBackendsV3";
/* 218 */     this.logger.entering(CLASS_NAME, "getRootAndBackendsV3");
/*     */ 
/*     */     
/* 221 */     List<RootOrBackendResult> result = ((ZenZefiCertificatesService)this.certificateService).getCertificatesByTypeCurrentUser(RootOrBackend.toType(rootOrBackend));
/*     */     
/* 223 */     this.logger.exiting(CLASS_NAME, "getRootAndBackendsV3");
/* 224 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(result);
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
/*     */   @ApiOperation(httpMethod = "GET", value = "Returns for a given ZK number the corresponding BSKI, and vice versa", nickname = "zkNoMappingV3", tags = {"ZenZefi Public API V3"})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = ZkNoMappingResult.class, responseContainer = "List"), @ApiResponse(code = 400, message = "Invalid format for the given identifier | The backend found with the identifier: XYZ does not have a ZK Number"), @ApiResponse(code = 404, message = " No mapping entry found for Zk number / bski"), @ApiResponse(code = 500, message = "Internal error message")})
/*     */   @RequestMapping(value = {"/v3/certificates/zknumbermapping"}, method = {RequestMethod.GET}, produces = {"application/json;charset=UTF-8", "application/asc"})
/*     */   public ResponseEntity<List<? extends Versioned>> zkNoMappingV3(@ApiParam("The ZK Number or the BSKI. Type is determined from the value, therefore must either be a valid BSKI or a valid ZK number.") @RequestParam(required = false) String identifier, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam("Client id which identifies the client that makes a request to the server.") @RequestHeader(value = "X-Client-ID", required = false) String clientId) {
/* 241 */     String METHOD_NAME = "zkNoMappingV3";
/* 242 */     this.logger.entering(CLASS_NAME, "zkNoMappingV3");
/* 243 */     this.requestMetaData.setLocale(locale);
/* 244 */     List<ZkNoMappingResult> zkNoMapping = ((ZenZefiCertificatesService)this.certificateService).getZkNoMapping(identifier);
/* 245 */     this.logger.exiting(CLASS_NAME, "zkNoMappingV3");
/* 246 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(zkNoMapping);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "POST", value = "Create signature for input byte array", nickname = "signPayloadV3", tags = {"ZenZefi Public API V3"})
/*     */   @RequestMapping(value = {"/v3/signpayload"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<String> signPayload(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam(value = "Client id which identifies the client that makes a request to the server.", required = false) @RequestHeader(value = "X-Client-ID", required = false) String clientId, @RequestBody String payload) {
/* 257 */     String METHOD_NAME = "signPayloadV3";
/* 258 */     this.logger.entering(CLASS_NAME, "signPayloadV3");
/* 259 */     addRequestMetaData(locale);
/* 260 */     String signedPayload = ((ZenZefiCertificatesService)this.certificateService).signPayload(payload);
/* 261 */     this.logger.exiting(CLASS_NAME, "signPayloadV3");
/* 262 */     return ResponseEntity.of(Optional.of(signedPayload));
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\boundary\ZenzefiCertificatesResourceV3.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */