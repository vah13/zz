/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.boundary;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*     */ import com.daimler.cebas.certificates.control.vo.UpdateCertificateMetrics;
/*     */ import com.daimler.cebas.common.control.HttpHeaderFactory;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.zenzefi.certificates.boundary.AbstractZenZefiCertificatesResourceV2;
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
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @Api(tags = {"ZenZefi Public API V2"}, description = "Exposed endpoints for external clients")
/*     */ public class ZenzefiCertificatesResourceV2
/*     */   extends AbstractZenZefiCertificatesResourceV2<ZenZefiCertificatesService>
/*     */ {
/*  49 */   private static final Logger LOG = Logger.getLogger(com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV2.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.certificates.boundary.ZenzefiCertificatesResourceV2.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String VERSION = "/v2";
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
/*     */   public ZenzefiCertificatesResourceV2(ZenZefiCertificatesService certificateService, Logger logger, CertificatesConfiguration certificateProfileConfiguration, MetadataManager requestMetaData) {
/*  75 */     super((AbstractZenZefiCertificatesService)certificateService, logger, certificateProfileConfiguration, requestMetaData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", value = "Returns the system integrity check XML report. Endpoint used only in the ZenZefi UI.", nickname = "getIntegrityCheckLogFileV2", produces = "application/xml", response = File.class, tags = {"ZenZefi Public API V2"})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = ""), @ApiResponse(code = 400, message = "System integrity check xml report not found"), @ApiResponse(code = 500, message = "Internal server error")})
/*     */   @RequestMapping(value = {"/v2/certificates/checkSystemIntegrityLog"}, method = {RequestMethod.GET}, produces = {"application/xml"})
/*     */   public ResponseEntity<String> getIntegrityCheckLogFileV2(HttpServletResponse response, @ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid) {
/*  88 */     String METHOD_NAME = "getIntegrityCheckLogFileV2";
/*  89 */     this.logger.entering(CLASS_NAME, "getIntegrityCheckLogFileV2");
/*  90 */     addRequestMetaData(locale);
/*  91 */     String INTEGRITY_CHECK_FILE_NAME = "system_integrity_check_report.xml";
/*     */     
/*  93 */     response.setStatus(200);
/*  94 */     response.addHeader("Content-Disposition", "attachment; filename=\"system_integrity_check_report.xml\"");
/*  95 */     response.addHeader("Cache-Control", "no-cache, no-store");
/*     */     try {
/*  97 */       ((ZenZefiCertificatesService)this.certificateService).writeSystemIntegrityReportToOutputStream((OutputStream)response.getOutputStream());
/*  98 */     } catch (IOException e) {
/*  99 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 100 */       this.logger.logWithTranslation(Level.SEVERE, "000062X", "integrityCheckReportError", new String[] { e
/* 101 */             .getMessage() }, CLASS_NAME);
/* 102 */       new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
/*     */     } 
/* 104 */     this.logger.log(Level.INFO, "000447", this.requestMetaData
/* 105 */         .getEnglishMessage("checkSystemIntegrityLog"), 
/* 106 */         getClass().getSimpleName());
/* 107 */     this.logger.exiting(CLASS_NAME, "getIntegrityCheckLogFileV2");
/* 108 */     return new ResponseEntity((MultiValueMap)HttpHeaderFactory.buildHeaderCacheControl(), HttpStatus.OK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "POST", value = "Perform full update of the certificates. All certificates the user has rights for are requested independently of the certificate renewal time.", nickname = "updateFullCertificatesV2", tags = {"ZenZefi Public API V2"})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Registered user needs to be logged in."), @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")})
/*     */   @RequestMapping(value = {"/v2/certificates/update/full"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<Map<String, String>> updateFullCertificatesV2(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam(value = "Client id which identifies the client that makes a request to the server.", required = false) @RequestHeader(value = "X-Client-ID", required = false) String clientId) {
/* 121 */     String METHOD_NAME = "updateFullCertificatesV2";
/* 122 */     this.logger.entering(CLASS_NAME, "updateFullCertificatesV2");
/* 123 */     addRequestMetaData(locale);
/* 124 */     boolean hasToken = ((ZenZefiCertificatesService)this.certificateService).fullCertificatesUpdate(clientId);
/* 125 */     Map<String, String> response = new HashMap<>();
/* 126 */     if (hasToken) {
/* 127 */       response.put("message", "full update started");
/* 128 */       response.put("monitoring", "/certificates/update/metrics");
/*     */     } else {
/* 130 */       this.logger.log(Level.INFO, "000523", "Full update not started: UNAUTHORIZED", 
/* 131 */           getClass().getSimpleName());
/* 132 */       response.put("message", "full update not started: UNAUTHORIZED");
/*     */     } 
/* 134 */     this.logger.exiting(CLASS_NAME, "updateFullCertificatesV2");
/* 135 */     return new ResponseEntity(response, hasToken ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "POST", value = "Perform differential update of the certificates. Only those certificates in the user-specific certificate store.", nickname = "updateDifferentialCertificatesV2", tags = {"ZenZefi Public API V2"})
/*     */   @ApiResponses({@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "Registered user needs to be logged in."), @ApiResponse(code = 406, message = "Operation not allowed while certificates update process is active.")})
/*     */   @RequestMapping(value = {"/v2/certificates/update/differential"}, method = {RequestMethod.POST})
/*     */   public ResponseEntity<Map<String, String>> updateDifferentialCertificatesV2(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam(value = "Client id which identifies the client that makes a request to the server.", required = false) @RequestHeader(value = "X-Client-ID", required = false) String clientId) {
/* 148 */     String METHOD_NAME = "updateDifferentialCertificatesV2";
/* 149 */     this.logger.entering(CLASS_NAME, "updateDifferentialCertificatesV2");
/* 150 */     addRequestMetaData(locale);
/* 151 */     boolean hasToken = ((ZenZefiCertificatesService)this.certificateService).differentialCertificatesUpdate(clientId);
/* 152 */     Map<String, String> response = new HashMap<>();
/* 153 */     if (hasToken) {
/* 154 */       response.put("message", "differential update started");
/* 155 */       response.put("monitoring", "/certificates/update/metrics");
/*     */     } else {
/* 157 */       this.logger.log(Level.INFO, "000523", "Differential update not started: UNAUTHORIZED", 
/* 158 */           getClass().getSimpleName());
/* 159 */       response.put("message", "differential update not started: UNAUTHORIZED");
/*     */     } 
/* 161 */     this.logger.exiting(CLASS_NAME, "updateDifferentialCertificatesV2");
/* 162 */     return new ResponseEntity(response, hasToken ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiOperation(httpMethod = "GET", value = "Returns the metrics during the certificates update process", nickname = "updateCertificatesMetricsV2", tags = {"ZenZefi Public API V2"})
/*     */   @RequestMapping(value = {"/v2/certificates/update/metrics"}, method = {RequestMethod.GET})
/*     */   public ResponseEntity<UpdateCertificateMetrics> updateCertificatesMetricsV2(@ApiParam(value = "The language for the operation. E.g. en, de", defaultValue = "en", required = false) @RequestHeader(value = "Locale", defaultValue = "en") String locale, @ApiParam(value = "Business id which identifies the request and business case.", required = false) @RequestHeader(value = "X-Correlation-ID", required = false) String correlationid, @ApiParam(value = "Client id which identifies the client that makes a request to the server.", required = false) @RequestHeader(value = "X-Client-ID", required = false) String clientId, HttpServletResponse httpServletResponse) {
/* 174 */     String METHOD_NAME = "updateCertificatesMetricsV2";
/* 175 */     this.logger.entering(CLASS_NAME, "updateCertificatesMetricsV2");
/* 176 */     addRequestMetaData(locale);
/* 177 */     UpdateCertificateMetrics result = ((ZenZefiCertificatesService)this.certificateService).getUpdateCertificatesMetrics();
/* 178 */     this.logger.exiting(CLASS_NAME, "updateCertificatesMetricsV2");
/* 179 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(HttpHeaderFactory.buildHeaderCacheControl())).body(result);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\boundary\ZenzefiCertificatesResourceV2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */