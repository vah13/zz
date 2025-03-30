/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.boundary;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*     */ import com.daimler.cebas.certificates.control.validation.SystemIntegrityCheckResult;
/*     */ import com.daimler.cebas.certificates.control.validation.SystemIntegrityCheckResultWithoutReport;
/*     */ import com.daimler.cebas.certificates.control.vo.ExtendedDeleteCertificatesResult;
/*     */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*     */ import com.daimler.cebas.certificates.control.vo.RootOrBackend;
/*     */ import com.daimler.cebas.certificates.control.vo.RootOrBackendResult;
/*     */ import com.daimler.cebas.certificates.control.vo.UpdateCertificateMetrics;
/*     */ import com.daimler.cebas.certificates.control.zkNoSupport.ZkNoMappingResult;
/*     */ import com.daimler.cebas.certificates.entity.Certificate;
/*     */ import com.daimler.cebas.certificates.entity.CertificateType;
/*     */ import com.daimler.cebas.certificates.entity.CertificatesView;
/*     */ import com.daimler.cebas.common.control.Filter;
/*     */ import com.daimler.cebas.common.control.HttpHeaderFactory;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.vo.CEBASResult;
/*     */ import com.daimler.cebas.common.control.vo.Enablement;
/*     */ import com.daimler.cebas.common.entity.Versioned;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV3;
/*     */ import com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenzefiCertificatesResource;
/*     */ import com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV3;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.AbstractZenZefiCertificatesService;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.ZenZefiCertificatesService;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.vo.ActiveForTestingCertificates;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.vo.CertificateHexFormatOutput;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.vo.CertificatesUseCaseHolder;
/*     */ import com.daimler.cebas.zenzefi.certificates.entity.TestingUseCaseType;
/*     */ import com.daimler.cebas.zenzefi.certificates.entity.VirtualCertificate;
/*     */ import com.daimler.cebas.zenzefi.certificates.entity.ZenZefiCertificate;
/*     */ import com.fasterxml.jackson.annotation.JsonView;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import io.swagger.annotations.ApiParam;
/*     */ import io.swagger.annotations.ApiResponse;
/*     */ import io.swagger.annotations.ApiResponses;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.bind.annotation.GetMapping;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestHeader;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @Api(value = "ZenZefi Certificates API", tags = {"ZenZefi Certificate Management"})
/*     */ public class ZenzefiCertificatesResource
/*     */   extends AbstractZenzefiCertificatesResource<ZenZefiCertificatesService>
/*     */ {
/*     */   private static final String QUERY_PARAM = "q";
/*  74 */   private static final String CLASS_NAME = AbstractZenzefiCertificatesResource.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String VERSION = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ZenzefiCertificatesResourceV3 zenzefiCertificatesResourceV3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public ZenzefiCertificatesResource(ZenZefiCertificatesService certificatesService, ZenzefiCertificatesResourceV3 zenzefiCertificatesResourceV3, Logger logger, CertificatesConfiguration certificateProfileConfiguration, MetadataManager requestMetaData) {
/*  99 */     super((AbstractZenZefiCertificatesService)certificatesService, (AbstractZenZefiCertificatesResourceV3)zenzefiCertificatesResourceV3, logger, certificateProfileConfiguration, requestMetaData);
/* 100 */     this.zenzefiCertificatesResourceV3 = zenzefiCertificatesResourceV3;
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
/*     */   @ApiOperation(httpMethod = "GET", value = "Returns the system integrity check XML report. Endpoint used only in the ZenZefi UI.", nickname = "getIntegrityCheckLogFile", produces = "application/xml", response = File.class, tags = {"ZenZefi Public API"})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "System integrity check xml report not found"), @ApiResponse(code = 500, message = "Internal server error")})
/*     */   @RequestMapping(value = {"/certificates/checkSystemIntegrityLog"}, method = {RequestMethod.GET}, produces = {"application/xml"})
/*     */   public ResponseEntity<String> getIntegrityCheckLogFile(HttpServletResponse response, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 124 */     return this.zenzefiCertificatesResourceV3.getIntegrityCheckLogFileV3(response, locale, correlationid);
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
/*     */   @ApiOperation(httpMethod = "POST", value = "Perform full update of the certificates. All certificates the user has rights for are requested independently of the certificate renewal time.", nickname = "updateFullCertificates", tags = {"ZenZefi Public API"})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Registered user needs to be logged in."), @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")})
/*     */   @RequestMapping(value = {"/certificates/update/full"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<Map<String, String>> updateFullCertificates(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam("Client id which identifies the client that makes a request to the server.") @RequestHeader(value = "X-Client-ID", required = false) String clientId) {
/* 148 */     return this.zenzefiCertificatesResourceV3.updateFullCertificatesV3(locale, correlationid, clientId);
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
/*     */   @ApiOperation(httpMethod = "POST", value = "Perform differential update of the certificates. Only those certificates in the user-specific certificate store.", nickname = "updateDifferentialCertificates", tags = {"ZenZefi Public API"})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Registered user needs to be logged in."), @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")})
/*     */   @RequestMapping(value = {"/certificates/update/differential"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<Map<String, String>> updateDifferentialCertificates(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam("Client id which identifies the client that makes a request to the server.") @RequestHeader(value = "X-Client-ID", required = false) String clientId) {
/* 172 */     return this.zenzefiCertificatesResourceV3.updateDifferentialCertificatesV3(locale, correlationid, clientId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", value = "Returns the root and backend certificates for the currently logged in user.", nickname = "getRootAndBackends", tags = {"ZenZefi Public API"})
/*     */   @RequestMapping(value = {"/certificates/rootsAndBackends"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<List<RootOrBackendResult>> getRootAndBackends(@ApiParam("Which certificates should be retrieved: BACKEND, ROOT. Parameter is optional, if none is provided all are returned.") @RequestParam(required = false) RootOrBackend rootOrBackend, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam("Client id which identifies the client that makes a request to the server.") @RequestHeader(value = "X-Client-ID", required = false) String clientId, HttpServletResponse httpServletResponse) {
/* 184 */     return this.zenzefiCertificatesResourceV3.getRootAndBackendsV3(rootOrBackend, locale, correlationid, clientId, httpServletResponse);
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
/*     */   @ApiOperation(httpMethod = "GET", value = "Returns the metrics during the certificates update process", nickname = "updateCertificatesMetrics")
/*     */   @RequestMapping(value = {"/certificates/update/metrics"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<UpdateCertificateMetrics> updateCertificatesMetrics(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam("Client id which identifies the client that makes a request to the server.") @RequestHeader(value = "X-Client-ID", required = false) String clientId, HttpServletResponse httpServletResponse) {
/* 209 */     return this.zenzefiCertificatesResourceV3.updateCertificatesMetricsV3(locale, correlationid, clientId, httpServletResponse);
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
/*     */   @ApiOperation(httpMethod = "GET", produces = "JSON", value = "Returns a json representation of required certificate by id. The id must be a valid UUID.")
/*     */   @RequestMapping(value = {"/certificates/{id}"}, method = {RequestMethod.GET})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = Certificate.class), @ApiResponse(code = 400, message = "Certificate does not exist | Invalid format for the given ID."), @ApiResponse(code = 500, message = "Internal server error")})
/*     */   public ResponseEntity<Certificate> getCertificate(@ApiParam(value = "The id of the certificate. Must be valid UUID.", required = true) @PathVariable String id, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 232 */     String METHOD_NAME = "getCertificate";
/* 233 */     this.logger.entering(CLASS_NAME, "getCertificate");
/* 234 */     addRequestMetaData(locale);
/* 235 */     Certificate certificate = ((ZenZefiCertificatesService)this.certificateService).getCertificate(id);
/* 236 */     this.logger.exiting(CLASS_NAME, "getCertificate");
/* 237 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(certificate);
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
/*     */   @ApiOperation(httpMethod = "POST", nickname = "Import Files", value = "Not working from Swagger UI and Swagger Generated API. Imports certificates, .crt or .p12 files into current logged in user-specific certificate store. Operation used in the ZenZefi UI for importing.")
/*     */   @RequestMapping(value = {"/certificates/import/files"}, method = {RequestMethod.POST}, produces = {"application/json"}, consumes = {"multipart/form-data"})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = ImportResult.class, responseContainer = "List"), @ApiResponse(code = 400, message = "Cannot read the multipart data from input")})
/*     */   public ResponseEntity<List<ImportResult>> importCertificates(@RequestParam("files") List<MultipartFile> files, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 260 */     String METHOD_NAME = "importCertificates";
/* 261 */     this.logger.entering(CLASS_NAME, "importCertificates");
/* 262 */     addRequestMetaData(locale);
/* 263 */     List<ImportResult> result = ((ZenZefiCertificatesService)this.certificateService).importCertificates(files);
/* 264 */     this.logger.exiting(CLASS_NAME, "importCertificates");
/* 265 */     return new ResponseEntity(result, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
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
/*     */   @ApiOperation(httpMethod = "GET", value = "Check system integrity without downloading the XML report. Endpoint used only in the ZenZefi UI.", nickname = "checkSystemIntegrityWithoutReport")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "")})
/*     */   @RequestMapping(value = {"/certificates/checkSystemIntegrityReport"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<SystemIntegrityCheckResultWithoutReport> checkSystemIntegrityWithoutReport(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 283 */     String METHOD_NAME = "checkSystemIntegrityWithoutReport";
/* 284 */     this.logger.entering(CLASS_NAME, "checkSystemIntegrityWithoutReport");
/* 285 */     addRequestMetaData(locale);
/*     */     
/* 287 */     SystemIntegrityCheckResult result = ((ZenZefiCertificatesService)this.certificateService).checkSystemIntegrity();
/* 288 */     SystemIntegrityCheckResultWithoutReport resultWithoutReport = new SystemIntegrityCheckResultWithoutReport(result.getTotalNumberOfCheckedCertificates(), result.getIntegrityCheckErrors());
/*     */     
/* 290 */     this.logger.exiting(CLASS_NAME, "checkSystemIntegrityWithoutReport");
/* 291 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(resultWithoutReport);
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
/*     */   @ApiOperation(httpMethod = "GET", value = "Checks whether the system integrity report is still existent. Endpoint used only in the ZenZefi UI.", nickname = "isIntegrityCheckLogXMLExistent")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "")})
/*     */   @RequestMapping(value = {"/certificates/checkSystemIntegrityLogExistance"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<String> isIntegrityCheckLogXMLExistent(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 309 */     String METHOD_NAME = "isIntegrityCheckLogXMLExistent";
/* 310 */     this.logger.entering(CLASS_NAME, "isIntegrityCheckLogXMLExistent");
/* 311 */     addRequestMetaData(locale);
/* 312 */     boolean reportStillExists = ((ZenZefiCertificatesService)this.certificateService).isSystemIntegrityLogExistent();
/* 313 */     this.logger.exiting(CLASS_NAME, "isIntegrityCheckLogXMLExistent");
/* 314 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body("" + reportStillExists);
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
/*     */   @ApiOperation(httpMethod = "PUT", value = "Switches the state of active for testing  based on certificate's id and type. The type should be known by ZZ server.", nickname = "updateActiveForTesting")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Not found new certificate active for testing. | Not found old certificate active for testing. | Cannot switch active for testing."), @ApiResponse(code = 500, message = "Invalid configuration."), @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")})
/*     */   @RequestMapping(value = {"/certificates/activeForTesting/{id}"}, method = {RequestMethod.PUT})
/*     */   public ResponseEntity<String> updateActiveForTesting(@ApiParam(value = "Internal ID in ZenZefi", required = true) @PathVariable String id, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationId) {
/* 337 */     String METHOD_NAME = "updateActiveForTesting";
/* 338 */     this.logger.entering(CLASS_NAME, "updateActiveForTesting");
/* 339 */     addRequestMetaData(locale);
/*     */     
/* 341 */     ((ZenZefiCertificatesService)this.certificateService).updateActiveForTesting(id);
/*     */     
/* 343 */     this.logger.exiting(CLASS_NAME, "updateActiveForTesting");
/* 344 */     return new ResponseEntity((MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
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
/*     */   @ApiOperation(httpMethod = "PUT", value = "Activating certificate to be used in testing mechanism. The path variable 'id' is ZenZefi internal id of the certificate.", nickname = "activateForTesting")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Not found new certificate active for testing. | Not found old certificate active for testing. | Cannot switch active for testing."), @ApiResponse(code = 500, message = "Invalid configuration."), @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")})
/*     */   @RequestMapping(value = {"/certificates/activeForTesting/activate/{id}"}, method = {RequestMethod.PUT})
/*     */   public ResponseEntity<String> activateForTesting(@ApiParam(value = "Internal ID in ZenZefi", required = true) @PathVariable String id, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 367 */     String METHOD_NAME = "updateActiveForTesting";
/* 368 */     this.logger.entering(CLASS_NAME, "updateActiveForTesting");
/* 369 */     addRequestMetaData(locale);
/* 370 */     ((ZenZefiCertificatesService)this.certificateService).setActiveForTesting(id);
/* 371 */     this.logger.exiting(CLASS_NAME, "updateActiveForTesting");
/* 372 */     return new ResponseEntity((MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
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
/*     */   @ApiOperation(httpMethod = "PUT", value = "Activating certificate to be used in testing mechanism. The use case can be: G, VSM, RP. G=General use case, VSM= VSM use cases, RP = Replacement Package use case. The path variable 'id' is ZenZefi internal id of the certificate.", nickname = "activateForTestingBasedOnUseCase")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Not found new certificate active for testing. | Not found old certificate active for testing. | Cannot switch active for testing."), @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")})
/*     */   @RequestMapping(value = {"/certificates/activeForTesting/activate/{id}/{useCase}"}, method = {RequestMethod.PUT})
/*     */   public ResponseEntity<String> activateForTestingBasedOnUseCase(@ApiParam("Internal ID in ZenZefi") @PathVariable String id, @ApiParam("G=General use case, VSM= VSM use cases, RP = Replacement Package use case") @PathVariable String useCase, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 395 */     String METHOD_NAME = "updateActiveForTesting";
/* 396 */     this.logger.entering(CLASS_NAME, "updateActiveForTesting");
/* 397 */     addRequestMetaData(locale);
/* 398 */     ((ZenZefiCertificatesService)this.certificateService).setActiveForTesting(id, TestingUseCaseType.valueOf(useCase));
/* 399 */     this.logger.exiting(CLASS_NAME, "updateActiveForTesting");
/* 400 */     return new ResponseEntity((MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
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
/*     */   @ApiOperation(httpMethod = "PUT", value = "Deactivating certificate usage in testing mechanism. The path variable 'id' is Zenzefi internal id of the certificate.", nickname = "deactivateUpdateForTesting")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Not found new certificate active for testing. | Not found old certificate active for testing. | Cannot switch active for testing."), @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")})
/*     */   @RequestMapping(value = {"/certificates/activeForTesting/deactivate/{id}"}, method = {RequestMethod.PUT})
/*     */   public ResponseEntity<String> deactivateForTesting(@ApiParam(value = "Internal ID in ZenZefi", required = true) @PathVariable String id, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 422 */     String METHOD_NAME = "updateActiveForTesting";
/* 423 */     this.logger.entering(CLASS_NAME, "updateActiveForTesting");
/* 424 */     addRequestMetaData(locale);
/* 425 */     ((ZenZefiCertificatesService)this.certificateService).unsetActiveForTesting(id);
/* 426 */     this.logger.exiting(CLASS_NAME, "updateActiveForTesting");
/* 427 */     return new ResponseEntity((MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
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
/*     */   @ApiOperation(httpMethod = "PUT", value = "Deactivating certificate usage in testing mechanism. The use case can be: G, VSM, RP. G=General use case, VSM= VSM use cases, RP = Replacement Package use case. The path variable 'id' is ZenZefi internal id of the certificate.", nickname = "deactivateUpdateForTestingBasedOnUseCase")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Not found new certificate active for testing. | Not found old certificate active for testing. | Cannot switch active for testing."), @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")})
/*     */   @RequestMapping(value = {"/certificates/activeForTesting/deactivate/{id}/{useCase}"}, method = {RequestMethod.PUT})
/*     */   public ResponseEntity<String> deactivateForTestingBasedOnUseCase(@ApiParam("Internal ID in ZenZefi") @PathVariable String id, @ApiParam("G=General use case, VSM= VSM use cases, RP = Replacement Package use case") @PathVariable String useCase, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 450 */     String METHOD_NAME = "updateActiveForTesting";
/* 451 */     this.logger.entering(CLASS_NAME, "updateActiveForTesting");
/* 452 */     addRequestMetaData(locale);
/* 453 */     ((ZenZefiCertificatesService)this.certificateService).unsetActiveForTesting(id, TestingUseCaseType.valueOf(useCase));
/* 454 */     this.logger.exiting(CLASS_NAME, "updateActiveForTesting");
/* 455 */     return new ResponseEntity((MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", value = "Checking if active for testing is enabled", nickname = "activeForTestingEnabled")
/*     */   @GetMapping(path = {"/certificates/activeForTesting/enabled"})
/*     */   public ResponseEntity<Boolean> isActiveForTestingActive() {
/* 466 */     return new ResponseEntity(Boolean.valueOf(((ZenZefiCertificatesService)this.certificateService).isActiveUserManualTesting()), HttpStatus.OK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", value = "Getting the certificates base on type", nickname = "certificatesByType")
/*     */   @GetMapping(path = {"/certificates/activeForTesting/options/{type}"})
/*     */   @JsonView({CertificatesView.SelectionView.class})
/*     */   public ResponseEntity<List<ZenZefiCertificate>> getActiveForTestingOptions(@PathVariable String type, @RequestParam Map<String, String> queryMap) {
/* 479 */     CertificateType realType = CertificateType.valueOf(type);
/* 480 */     List<ZenZefiCertificate> certificates = ((ZenZefiCertificatesService)this.certificateService).getActiveForTestingOptions(realType);
/* 481 */     if (queryMap != null && queryMap.containsKey("q")) {
/* 482 */       String queryString = queryMap.get("q");
/* 483 */       if (StringUtils.isNotEmpty(queryString)) {
/*     */ 
/*     */         
/* 486 */         List<ZenZefiCertificate> collect = (List<ZenZefiCertificate>)certificates.stream().filter(Filter.getFilterPredicatedBasedOnType(realType, queryString)).collect(Collectors.toList());
/* 487 */         return new ResponseEntity(collect, HttpStatus.OK);
/*     */       } 
/*     */     } 
/* 490 */     return new ResponseEntity(certificates, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", value = "Getting the active for testing certificates", nickname = "activeForTestingCertificates")
/*     */   @GetMapping(path = {"/certificates/activeForTesting"})
/*     */   public ResponseEntity<ActiveForTestingCertificates> getActiveForTestingCertificates() {
/* 502 */     ActiveForTestingCertificates activeForTestingCertificates = ((ZenZefiCertificatesService)this.certificateService).getActiveForTestingCertificates();
/* 503 */     return new ResponseEntity(activeForTestingCertificates, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", value = "Getting the active for testing certificates", nickname = "activeForTestingCertificatesBasedOnUseCase")
/*     */   @GetMapping(path = {"/certificates/activeForTesting/usecases/{type}"})
/*     */   public ResponseEntity<CertificatesUseCaseHolder> getActiveForTestingBasedOnUseCasesCertificates(@PathVariable String type) {
/* 516 */     CertificateType realType = CertificateType.valueOf(type);
/*     */     
/* 518 */     CertificatesUseCaseHolder activeForTestingBasedOnUseCase = ((ZenZefiCertificatesService)this.certificateService).getActiveForTestingBasedOnUseCase(realType);
/* 519 */     return new ResponseEntity(activeForTestingBasedOnUseCase, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", value = "Getting the active for testing enhanced certificates", nickname = "activeForTestingCertificatesEnhanced")
/*     */   @GetMapping(path = {"/certificates/activeForTesting/enhanced"})
/*     */   @JsonView({CertificatesView.SelectionEnhancedRightsView.class})
/*     */   public ResponseEntity<List<Certificate>> getActiveForTestingEnhancedCertificates() {
/* 532 */     List<Certificate> enhanced = ((ZenZefiCertificatesService)this.certificateService).getActiveForTestingEnhanced();
/* 533 */     return new ResponseEntity(enhanced, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
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
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", produces = "application/json", nickname = "filter", value = "Returns an array of filtered certificates belonging to the current logged in user. This endpoint is used in the ZenZefi UI, for filtering the certificates table.")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = Certificate.class, responseContainer = "List"), @ApiResponse(code = 500, message = "Internal server error")})
/*     */   @RequestMapping(value = {"/certificates/filter"}, method = {RequestMethod.GET})
/*     */   @JsonView({CertificatesView.Management.class})
/*     */   public ResponseEntity<List<Certificate>> filtered(@ApiParam("The filtering parameters, which also includes the limit for the query.") @RequestParam(required = false) Map<String, String> allRequestParams, @ApiParam("Valid from date. The date format is yyyy-MM-dd.") @RequestParam(required = false) String[] validFrom, @ApiParam("Valid to date. The date format is yyyy-MM-dd.") @RequestParam(required = false) String[] validTo, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 563 */     String METHOD_NAME = "filtered";
/* 564 */     this.logger.entering(CLASS_NAME, "filtered");
/* 565 */     addRequestMetaData(locale);
/*     */     
/* 567 */     List<Certificate> certificates = ((ZenZefiCertificatesService)this.certificateService).getFilteredCertificates(allRequestParams, validFrom, validTo);
/* 568 */     if (noFilterApplied(allRequestParams, validFrom, validTo)) {
/* 569 */       List<Certificate> certsWithFolders = addEcuFoldersTo(certificates);
/* 570 */       return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(certsWithFolders);
/*     */     } 
/*     */     
/* 573 */     this.logger.exiting(CLASS_NAME, "filtered");
/* 574 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(certificates);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean noFilterApplied(@RequestParam(required = false) @ApiParam("The filtering parameters, which also includes the limit for the query.") Map<String, String> allRequestParams, @RequestParam(required = false) @ApiParam("Valid from date. The date format is yyyy-MM-dd.") String[] validFrom, @RequestParam(required = false) @ApiParam("Valid to date. The date format is yyyy-MM-dd.") String[] validTo) {
/* 581 */     return (allRequestParams.isEmpty() && validFrom == null && validTo == null);
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
/*     */   private List<Certificate> addEcuFoldersTo(List<Certificate> certificates) {
/* 594 */     List<Certificate> certsWithFolders = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 599 */     List<String> backendIdsWithEcus = (List<String>)certificates.stream().filter(c -> (CertificateType.ECU_CERTIFICATE == c.getType())).map(Certificate::getParentId).distinct().collect(Collectors.toList());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 605 */     Map<String, VirtualCertificate> backendId2Folder = (Map<String, VirtualCertificate>)certificates.stream().filter(c -> backendIdsWithEcus.contains(c.getEntityId())).map(c -> new VirtualCertificate("ECU Certificate folder", c)).collect(Collectors.toMap(Certificate::getParentId, c -> c));
/*     */     
/* 607 */     List<Certificate> ecus = new ArrayList<>();
/*     */     
/* 609 */     certificates.forEach(c -> {
/*     */           if (CertificateType.ECU_CERTIFICATE == c.getType()) {
/*     */             ecus.add(c);
/*     */           } else if (CertificateType.BACKEND_CA_CERTIFICATE == c.getType()) {
/*     */             certsWithFolders.add(c);
/*     */ 
/*     */             
/*     */             VirtualCertificate v = (VirtualCertificate)backendId2Folder.get(c.getEntityId());
/*     */ 
/*     */             
/*     */             if (v != null) {
/*     */               certsWithFolders.add(v);
/*     */             }
/*     */           } else {
/*     */             certsWithFolders.add(c);
/*     */           } 
/*     */         });
/*     */     
/* 627 */     ecus.stream().sorted(Comparator.comparing(Certificate::getSubject)).forEach(c -> {
/*     */           certsWithFolders.add(c);
/*     */           
/*     */           VirtualCertificate v = (VirtualCertificate)backendId2Folder.get(c.getParentId());
/*     */           c.setParentId(v.getEntityId());
/*     */           v.addChild(c);
/*     */         });
/* 634 */     return certsWithFolders;
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
/*     */   @ApiOperation(httpMethod = "GET", produces = "application/json", nickname = "filterOptions", value = "Filter options")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = String.class, responseContainer = "List"), @ApiResponse(code = 500, message = "Internal server error")})
/*     */   @RequestMapping(value = {"/certificates/filter/options"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<Set<String>> filterOptions(@ApiParam("column for which the options are needed") @RequestParam(required = false) String columnName, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 653 */     Set<String> columnFilterOptions = ((ZenZefiCertificatesService)this.certificateService).getColumnFilterOptions(columnName);
/* 654 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(columnFilterOptions);
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
/*     */   @ApiOperation(httpMethod = "GET", value = "endpoint export certificate", nickname = "exportCertificate")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "Export Certificate successfully executed.", response = CertificateHexFormatOutput.class)})
/*     */   @RequestMapping(value = {"/certificates/export/{id}"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<CertificateHexFormatOutput> exportCertificate(@ApiParam(value = "The id of the certificate. Must be valid UUID.", required = true) @PathVariable String id, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 676 */     String METHOD_NAME = "exportCertificate";
/* 677 */     this.logger.entering(CLASS_NAME, "exportCertificate");
/* 678 */     this.requestMetaData.setLocale(locale);
/* 679 */     CertificateHexFormatOutput result = ((ZenZefiCertificatesService)this.certificateService).exportInMultipleFormat(id);
/* 680 */     this.logger.exiting(CLASS_NAME, "exportCertificate");
/* 681 */     return new ResponseEntity(result, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
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
/*     */   @ApiOperation(httpMethod = "GET", value = "Returns whether or not the update operation is active.", nickname = "isUpdateActive")
/*     */   @RequestMapping(value = {"/certificates/update/active"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<Boolean> isUpdateSessionActive(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam("Client id which identifies the client that makes a request to the server.") @RequestHeader(value = "X-Client-ID", required = false) String clientId) {
/* 701 */     String METHOD_NAME = "isUpdateSessionActive";
/* 702 */     this.logger.entering(CLASS_NAME, "isUpdateSessionActive");
/* 703 */     addRequestMetaData(locale);
/* 704 */     boolean result = ((ZenZefiCertificatesService)this.certificateService).isUpdateSessionActive();
/* 705 */     this.logger.exiting(CLASS_NAME, "isUpdateSessionActive");
/* 706 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(Boolean.valueOf(result));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "POST", value = "Update process cancellation", nickname = "cancelUpdate")
/*     */   @ApiResponses({@ApiResponse(code = 406, message = "Update session cannot be cancelled since is not running", response = CEBASResult.class)})
/*     */   @RequestMapping(value = {"/certificates/update/cancel"}, method = {RequestMethod.POST})
/*     */   public void cancelUpdate() {
/* 717 */     ((ZenZefiCertificatesService)this.certificateService).cancelUpdateSession();
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
/*     */   @ApiOperation(httpMethod = "GET", produces = "JSON", value = "Returns a json representation of required certificate by id. The id must be a valid UUID.")
/*     */   @RequestMapping(value = {"/certificates/details/{id}"}, method = {RequestMethod.GET})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = ZenZefiCertificate.class), @ApiResponse(code = 400, message = "Certificate does not exist | Invalid format for the given ID."), @ApiResponse(code = 500, message = "Internal server error")})
/*     */   @JsonView({CertificatesView.Detail.class})
/*     */   public ResponseEntity<ZenZefiCertificate> getCertificateForDetailsPanel(@ApiParam(value = "The id of the certificate. Must be valid UUID.", required = true) @PathVariable String id, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/* 742 */     String METHOD_NAME = "getCertificateForDetailsPane";
/* 743 */     this.logger.entering(CLASS_NAME, "getCertificateForDetailsPane");
/* 744 */     addRequestMetaData(locale);
/*     */     
/* 746 */     ZenZefiCertificate certificate = ((ZenZefiCertificatesService)this.certificateService).getCertificateForDetailPanel(id);
/*     */     
/* 748 */     this.logger.exiting(CLASS_NAME, "getCertificateForDetailsPane");
/* 749 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(certificate);
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
/*     */   @ApiOperation(httpMethod = "GET", value = "Returns for a given ZK number the corresponding BSKI, and vice versa", nickname = "zkNoMapping", tags = {"ZenZefi Public API"})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "", response = ZkNoMappingResult.class, responseContainer = "List"), @ApiResponse(code = 400, message = "Invalid format for the given identifier | The backend found with the identifier: XYZ does not have a ZK Number"), @ApiResponse(code = 404, message = " No mapping entry found for ZK Number / bski"), @ApiResponse(code = 500, message = "Internal error message")})
/*     */   @RequestMapping(value = {"/certificates/zknumbermapping"}, method = {RequestMethod.GET}, produces = {"application/json;charset=UTF-8", "application/asc"})
/*     */   public ResponseEntity<List<? extends Versioned>> zkNoMapping(@ApiParam("The ZK Number or the BSKI. Type is determined from the value, therefore must either be a valid BSKI or a valid ZK number.") @RequestParam(required = false) String identifier, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam("Client id which identifies the client that makes a request to the server.") @RequestHeader(value = "X-Client-ID", required = false) String clientId) {
/* 766 */     String METHOD_NAME = "zkNoMapping";
/* 767 */     this.logger.entering(CLASS_NAME, "zkNoMapping");
/* 768 */     this.requestMetaData.setLocale(locale);
/* 769 */     List<ZkNoMappingResult> zkNoMapping = ((ZenZefiCertificatesService)this.certificateService).getZkNoMapping(identifier);
/* 770 */     this.logger.exiting(CLASS_NAME, "zkNoMapping");
/* 771 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(zkNoMapping);
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
/*     */   @ApiOperation(httpMethod = "POST", produces = "JSON", value = "Remove certificates via POST json body instead of url ids. Enables deletion of long lists of ids that are not allowed anymore due to http url length restrictions.")
/*     */   @ApiResponses({@ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "Invalid format for the given ID. | Delete certificates called but no match was found. | The certificate with id does not exist in current user-specific certificate store."), @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")})
/*     */   @RequestMapping(value = {"/certificates/remove"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<List<ExtendedDeleteCertificatesResult>> deleteCertificatesUsingBody(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam(name = "idsToDelete", value = "Json Array of ids that should be deleted.", required = true) @RequestBody String[] idsToDelete) {
/* 796 */     String METHOD_NAME = "deleteCertificatesUsingBody";
/* 797 */     this.logger.entering(CLASS_NAME, "deleteCertificatesUsingBody");
/* 798 */     addRequestMetaData(locale);
/*     */     
/* 800 */     List<ExtendedDeleteCertificatesResult> deletedCertificatesList = ((ZenZefiCertificatesService)this.certificateService).deleteCertificatesAdditionalLogging(Arrays.asList(idsToDelete));
/*     */     
/* 802 */     this.logger.exiting(CLASS_NAME, "deleteCertificatesUsingBody");
/* 803 */     return new ResponseEntity(deletedCertificatesList, (MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "POST", value = "Enable/Disable extended validation", nickname = "enableExtendedValidationV3", tags = {"ZenZefi Public API"})
/*     */   @RequestMapping(value = {"/certificates/extendedValidation"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<String> enableExtendedValidationV3(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en") @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam("Business id which identifies the request and business case.") @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam("Client id which identifies the client that makes a request to the server.") @RequestHeader(value = "X-Client-ID", required = false) String clientId, @RequestBody Enablement enablement) {
/* 814 */     return this.zenzefiCertificatesResourceV3.enableExtendedValidationV3(locale, correlationid, clientId, enablement);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "POST", value = "Create signature for input byte array", nickname = "signPayloadV3", tags = {"ZenZefi Public API V3"})
/*     */   @RequestMapping(value = {"/signpayload"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<String> signPayload(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam(value = "Client id which identifies the client that makes a request to the server.", required = false) @RequestHeader(value = "X-Client-ID", required = false) String clientId, @RequestBody String payload) {
/* 825 */     String METHOD_NAME = "signPayloadV3";
/* 826 */     this.logger.entering(CLASS_NAME, "signPayloadV3");
/* 827 */     addRequestMetaData(locale);
/* 828 */     String signedPayload = ((ZenZefiCertificatesService)this.certificateService).signPayload(payload);
/* 829 */     this.logger.exiting(CLASS_NAME, "signPayloadV3");
/* 830 */     return ResponseEntity.of(Optional.of(signedPayload));
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\boundary\ZenzefiCertificatesResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */