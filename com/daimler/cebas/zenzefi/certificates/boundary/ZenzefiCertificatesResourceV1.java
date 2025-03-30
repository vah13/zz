/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.boundary;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*     */ import com.daimler.cebas.certificates.control.vo.UpdateCertificateMetrics;
/*     */ import com.daimler.cebas.common.control.HttpHeaderFactory;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV1;
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
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ @RestController
/*     */ @Api(tags = {"ZenZefi Public API V1"}, description = "Exposed endpoints for external clients")
/*     */ @Deprecated
/*     */ public class ZenzefiCertificatesResourceV1
/*     */   extends AbstractZenZefiCertificatesResourceV1<ZenZefiCertificatesService>
/*     */ {
/*  48 */   private static final Logger LOG = Logger.getLogger(com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV1.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV1.class.getSimpleName();
/*     */   
/*     */   @Autowired
/*     */   public ZenzefiCertificatesResourceV1(ZenZefiCertificatesService certificateService, Logger logger, CertificatesConfiguration certificateProfileConfiguration, MetadataManager requestMetaData) {
/*  57 */     super((AbstractZenZefiCertificatesService)certificateService, logger, certificateProfileConfiguration, requestMetaData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String VERSION = "/v1";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @ApiOperation(httpMethod = "GET", value = "Returns the system integrity check XML report. Endpoint used only in the ZenZefi UI.", nickname = "getIntegrityCheckLogFileV1", produces = "application/xml", response = File.class, tags = {"ZenZefi Public API V1"})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "System integrity check xml report not found"), @ApiResponse(code = 500, message = "Internal server error")})
/*     */   @RequestMapping(value = {"/v1/certificates/checkSystemIntegrityLog"}, method = {RequestMethod.GET}, produces = {"application/xml"})
/*     */   public ResponseEntity<String> getIntegrityCheckLogFileV1(HttpServletResponse response, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/*  76 */     String METHOD_NAME = "getIntegrityCheckLogFileV1";
/*  77 */     this.logger.entering(CLASS_NAME, "getIntegrityCheckLogFileV1");
/*  78 */     addRequestMetaData(locale);
/*  79 */     String INTEGRITY_CHECK_FILE_NAME = "system_integrity_check_report.xml";
/*     */     
/*  81 */     response.setStatus(200);
/*  82 */     response.addHeader("Content-Disposition", "attachment; filename=\"system_integrity_check_report.xml\"");
/*  83 */     response.addHeader("Cache-Control", "no-cache, no-store");
/*     */     try {
/*  85 */       ((ZenZefiCertificatesService)this.certificateService).writeSystemIntegrityReportToOutputStream((OutputStream)response.getOutputStream());
/*  86 */     } catch (IOException e) {
/*  87 */       LOG.log(Level.FINEST, e.getMessage(), e);
/*  88 */       this.logger.logWithTranslation(Level.SEVERE, "000062X", "integrityCheckReportError", new String[] { e
/*  89 */             .getMessage() }, CLASS_NAME);
/*  90 */       new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
/*     */     } 
/*  92 */     this.logger.exiting(CLASS_NAME, "getIntegrityCheckLogFileV1");
/*  93 */     return new ResponseEntity((MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @ApiOperation(httpMethod = "POST", value = "Perform full update of the certificates. All certificates the user has rights for are requested independently of the certificate renewal time.", nickname = "updateFullCertificatesV1", tags = {"ZenZefi Public API V1"})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Registered user needs to be logged in."), @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")})
/*     */   @RequestMapping(value = {"/v1/certificates/update/full"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<Map<String, String>> updateFullCertificatesV1(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam(value = "Client id which identifies the client that makes a request to the server.", required = false) @RequestHeader(value = "X-Client-ID", required = false) String clientId) {
/* 107 */     String METHOD_NAME = "updateFullCertificatesV1";
/* 108 */     this.logger.entering(CLASS_NAME, "updateFullCertificatesV1");
/* 109 */     addRequestMetaData(locale);
/* 110 */     boolean hasToken = ((ZenZefiCertificatesService)this.certificateService).fullCertificatesUpdate(clientId);
/* 111 */     Map<String, String> response = new HashMap<>();
/* 112 */     if (hasToken) {
/* 113 */       response.put("message", "full update started");
/* 114 */       response.put("monitoring", "/certificates/update/metrics");
/*     */     } else {
/* 116 */       this.logger.log(Level.INFO, "000523", "Full update not started: UNAUTHORIZED", 
/* 117 */           getClass().getSimpleName());
/* 118 */       response.put("message", "full update not started: UNAUTHORIZED");
/*     */     } 
/* 120 */     this.logger.exiting(CLASS_NAME, "updateFullCertificatesV1");
/* 121 */     return new ResponseEntity(response, hasToken ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @ApiOperation(httpMethod = "POST", value = "Perform differential update of the certificates. Only those certificates in the user-specific certificate store.", nickname = "updateDifferentialCertificatesV1", tags = {"ZenZefi Public API V1"})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Registered user needs to be logged in."), @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")})
/*     */   @RequestMapping(value = {"/v1/certificates/update/differential"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<Map<String, String>> updateDifferentialCertificatesV1(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam(value = "Client id which identifies the client that makes a request to the server.", required = false) @RequestHeader(value = "X-Client-ID", required = false) String clientId) {
/* 135 */     String METHOD_NAME = "updateDifferentialCertificatesV1";
/* 136 */     this.logger.entering(CLASS_NAME, "updateDifferentialCertificatesV1");
/* 137 */     addRequestMetaData(locale);
/* 138 */     boolean hasToken = ((ZenZefiCertificatesService)this.certificateService).differentialCertificatesUpdate(clientId);
/* 139 */     Map<String, String> response = new HashMap<>();
/* 140 */     if (hasToken) {
/* 141 */       response.put("message", "differential update started");
/* 142 */       response.put("monitoring", "/certificates/update/metrics");
/*     */     } else {
/* 144 */       this.logger.log(Level.INFO, "000523", "Differential update not started: UNAUTHORIZED", 
/* 145 */           getClass().getSimpleName());
/* 146 */       response.put("message", "differential update not started: UNAUTHORIZED");
/*     */     } 
/* 148 */     this.logger.exiting(CLASS_NAME, "updateDifferentialCertificatesV1");
/* 149 */     return new ResponseEntity(response, hasToken ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @ApiOperation(httpMethod = "GET", value = "Returns the metrics during the certificates update process", nickname = "updateCertificatesMetricsV1", tags = {"ZenZefi Public API V1"})
/*     */   @RequestMapping(value = {"/v1/certificates/update/metrics"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<UpdateCertificateMetrics> updateCertificatesMetricsV1(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam(value = "Client id which identifies the client that makes a request to the server.", required = false) @RequestHeader(value = "X-Client-ID", required = false) String clientId, HttpServletResponse httpServletResponse) {
/* 162 */     String METHOD_NAME = "updateCertificatesMetricsV1";
/* 163 */     this.logger.entering(CLASS_NAME, "updateCertificatesMetricsV1");
/* 164 */     addRequestMetaData(locale);
/* 165 */     UpdateCertificateMetrics result = ((ZenZefiCertificatesService)this.certificateService).getUpdateCertificatesMetrics();
/* 166 */     this.logger.exiting(CLASS_NAME, "updateCertificatesMetricsV1");
/* 167 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(result);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\boundary\ZenzefiCertificatesResourceV1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */