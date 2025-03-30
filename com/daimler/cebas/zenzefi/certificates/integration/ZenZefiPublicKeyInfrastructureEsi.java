/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.integration;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CertificatesUpdateException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CertificatesUpdateNoSessionExeption;
/*     */ import com.daimler.cebas.certificates.control.exceptions.DownloadRightsException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.InvalidTokenException;
/*     */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*     */ import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
/*     */ import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
/*     */ import com.daimler.cebas.certificates.integration.QueryParamFactory;
/*     */ import com.daimler.cebas.certificates.integration.vo.NonVsmCertificateRequest;
/*     */ import com.daimler.cebas.certificates.integration.vo.NonVsmIdentifier;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKICertificateRequest;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKICertificateResponse;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKIEcuCertificateResponse;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKIEnhancedCertificateRequest;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKILinkCertificateResponse;
/*     */ import com.daimler.cebas.certificates.integration.vo.VsmCertificateRequest;
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASService;
/*     */ import com.daimler.cebas.common.control.http.HttpEntityFactory;
/*     */ import com.daimler.cebas.configuration.control.util.PkiPropertiesManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*     */ import com.daimler.cebas.zenzefi.certificates.integration.StatusChanger;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.util.ZenZefiPkiAndOidcPropertiesManager;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.annotation.Profile;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.web.client.HttpStatusCodeException;
/*     */ import org.springframework.web.client.ResourceAccessException;
/*     */ import org.springframework.web.client.RestTemplate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASService
/*     */ @Profile({"!AFTERSALES"})
/*     */ public class ZenZefiPublicKeyInfrastructureEsi
/*     */   extends PublicKeyInfrastructureEsi
/*     */ {
/*     */   private static final String DOWNLOAD_FROM_PKI_FAILED = "Download from PKI failed.The CSR is still available in user's specific store.";
/*     */   private static final String SECOCIS = "SECOCIS";
/*     */   private static final String TIME = "TIME";
/*     */   private static final String COULD_NOT_CREATE_REQUEST_FOR_DOWNLOADING_USER_CERTIFICATES_REASON = "Could not create request for downloading user certificates. Reason: ";
/*     */   private static final String USER_DOES_NOT_HAVE_PERMISSIONS_FOR_DOWLOADING_CERTIFICATE = "User does not have permissions for downloading  certificate of type: ";
/*     */   private static final String TIME_SECOCIS_CERTS_HTTP_ERROR = "Http error in requesting %s certificates with status: %s";
/*  81 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   private static final Logger LOG = Logger.getLogger(CertificateParser.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Session session;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ZenZefiPkiAndOidcPropertiesManager pkiAndOAuthPropertiesManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StatusChanger statusChanger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public ZenZefiPublicKeyInfrastructureEsi(RestTemplate restTemplate, Logger logger, MetadataManager i18n, Session session, UpdateSession updateSession, ZenZefiPkiAndOidcPropertiesManager pkiAndOAuthPropertiesManager, StatusChanger statusChanger) {
/* 123 */     super(restTemplate, logger, i18n, (DefaultUpdateSession)updateSession);
/* 124 */     this.session = session;
/* 125 */     this.pkiAndOAuthPropertiesManager = pkiAndOAuthPropertiesManager;
/* 126 */     this.statusChanger = statusChanger;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PKICertificateResponse> downloadCertificates(List<PKICertificateRequest> certificateRequests) {
/* 131 */     String pkiCertificatesEndUserUrl = this.pkiAndOAuthPropertiesManager.getPkiCertificatesEndUserUrl();
/* 132 */     String payload = "";
/*     */     try {
/* 134 */       payload = (new ObjectMapper()).writeValueAsString(certificateRequests);
/* 135 */       List<PKICertificateResponse> response = makeRequest(pkiCertificatesEndUserUrl, HttpMethod.POST, PKICertificateResponse[].class, payload);
/*     */       
/* 137 */       markAsProcessed(certificateRequests);
/* 138 */       return response;
/* 139 */     } catch (IOException e) {
/* 140 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 141 */       this.logger.log(Level.WARNING, "000257X", "Could not create request for downloading user certificates. Reason: " + e
/* 142 */           .getMessage(), CLASS_NAME);
/* 143 */     } catch (HttpStatusCodeException httpEx) {
/* 144 */       LOG.log(Level.FINEST, httpEx.getMessage(), (Throwable)httpEx);
/* 145 */       handleHttpErrorCodeLogger(pkiCertificatesEndUserUrl, 
/* 146 */           HttpEntityFactory.buildDefaultPKIRequestEntityWithNoToken(MediaType.APPLICATION_JSON, payload), httpEx
/* 147 */           .getRawStatusCode(), httpEx.getResponseBodyAsString());
/* 148 */       downloadFailedInSession(certificateRequests, httpEx);
/* 149 */       stateChange(certificateRequests, httpEx);
/* 150 */       checkNotAuthorized(httpEx);
/* 151 */     } catch (ResourceAccessException e) {
/* 152 */       LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/* 153 */       logCannotAccess(e, pkiCertificatesEndUserUrl);
/* 154 */       if (this.updateSession.isRunning()) {
/* 155 */         return executeRetry(certificateRequests, e, this::downloadCertificates, pkiCertificatesEndUserUrl);
/*     */       }
/* 157 */       return Collections.emptyList();
/*     */     } 
/*     */     
/* 160 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<PKILinkCertificateResponse> downloadLinkCertificatesInActiveState(boolean isUpdateRunning) {
/* 171 */     return downloadLinkCertificates(null, null, isUpdateRunning);
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
/*     */   public List<PKILinkCertificateResponse> downloadLinkCertificatesForActiveSKI(String targetCa, boolean isUpdateRunning) {
/* 184 */     return downloadLinkCertificates(null, targetCa, isUpdateRunning);
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
/*     */   public List<PKILinkCertificateResponse> downloadLinkCertificatesForActiveAKI(String sourceCa, boolean isUpdateRunning) {
/* 197 */     return downloadLinkCertificates(sourceCa, null, isUpdateRunning);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public List<PKILinkCertificateResponse> downloadLinkCertificates(String sourceCa, String targetCa, boolean isUpdateRunning) {
/* 226 */     String truncatedSourceCa = (sourceCa == null) ? null : sourceCa.replaceAll("-", "");
/* 227 */     String truncatedTargetCa = (targetCa == null) ? null : targetCa.replaceAll("-", "");
/* 228 */     String pkiLinkCertUrl = getPkiPropertiesManager().getPkiLinkCertificateUrl();
/*     */     try {
/* 230 */       if (isUpdateRunning) {
/* 231 */         return makeRequest(pkiLinkCertUrl, HttpMethod.GET, PKILinkCertificateResponse[].class, QueryParamFactory.createLinkCertParam(truncatedSourceCa, truncatedTargetCa));
/*     */       }
/* 233 */       return makeRequestWithoutSessionRunning(pkiLinkCertUrl, HttpMethod.GET, PKILinkCertificateResponse[].class, QueryParamFactory.createLinkCertParam(truncatedSourceCa, truncatedTargetCa));
/*     */     }
/* 235 */     catch (HttpStatusCodeException httpEx) {
/* 236 */       this.updateSession.resetLinkCondition(Boolean.valueOf(true));
/* 237 */       LOG.log(Level.FINEST, httpEx.getMessage(), (Throwable)httpEx);
/* 238 */       String payload = "{\"sourceCa\": \"" + truncatedSourceCa + "\", \"targetCa\": \"" + truncatedTargetCa + "\"}";
/* 239 */       handleHttpErrorCodeLogger(pkiLinkCertUrl, 
/* 240 */           HttpEntityFactory.buildDefaultPKIRequestEntityWithNoToken(MediaType.APPLICATION_JSON, payload), httpEx
/* 241 */           .getRawStatusCode(), httpEx.getResponseBodyAsString());
/* 242 */       checkNotAuthorized(httpEx);
/* 243 */     } catch (ResourceAccessException e) {
/* 244 */       this.updateSession.resetLinkCondition(Boolean.valueOf(true));
/* 245 */       LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/* 246 */       logCannotAccess(e, pkiLinkCertUrl);
/*     */     } 
/*     */     
/* 249 */     return new ArrayList<>();
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
/*     */   public List<PKICertificateResponse> downloadTimeCertificatesWithoutRetryNoSession(List<PKICertificateRequest> certificateRequests) {
/* 261 */     String pkiTimeCertificatesUrl = this.pkiAndOAuthPropertiesManager.getPkiTimeCertificatesUrl();
/* 262 */     String payload = "";
/*     */     try {
/* 264 */       payload = (new ObjectMapper()).writeValueAsString(certificateRequests);
/* 265 */       return makeRequestWithoutSessionRunning(pkiTimeCertificatesUrl, HttpMethod.POST, PKICertificateResponse[].class, payload);
/*     */     }
/* 267 */     catch (IOException e) {
/* 268 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 269 */       this.logger.log(Level.WARNING, "000257X", "Could not create request for downloading user certificates. Reason: " + e
/* 270 */           .getMessage(), CLASS_NAME);
/* 271 */     } catch (HttpStatusCodeException httpEx) {
/* 272 */       LOG.log(Level.FINEST, httpEx.getMessage(), (Throwable)httpEx);
/* 273 */       handleHttpErrorCodeLogger(pkiTimeCertificatesUrl, 
/* 274 */           HttpEntityFactory.buildDefaultPKIRequestEntityWithNoToken(MediaType.APPLICATION_JSON, payload), httpEx
/* 275 */           .getRawStatusCode(), httpEx.getResponseBodyAsString());
/* 276 */       stateChange(certificateRequests, httpEx);
/* 277 */       if (httpEx.getStatusCode() == HttpStatus.FORBIDDEN || httpEx.getStatusCode() == HttpStatus.UNAUTHORIZED) {
/* 278 */         this.logger.log(Level.WARNING, "000320X", "User does not have permissions for downloading  certificate of type: TIME", CLASS_NAME);
/*     */         
/* 280 */         throw new DownloadRightsException(this.statusChanger.getPKIErrorMessage(httpEx.getResponseBodyAsString()), httpEx
/* 281 */             .getRawStatusCode());
/*     */       } 
/* 283 */       String errorBody = httpEx.getResponseBodyAsString();
/* 284 */       String errorPayload = StringUtils.isEmpty(errorBody) ? String.format("Http error in requesting %s certificates with status: %s", new Object[] { "TIME", httpEx.getStatusCode() }) : errorBody;
/* 285 */       this.logger.log(Level.SEVERE, "000234X", errorPayload, CLASS_NAME);
/* 286 */       throw new CertificatesUpdateNoSessionExeption("Download from PKI failed.The CSR is still available in user's specific store.", this.statusChanger
/* 287 */           .getPKIErrorMessage(httpEx.getResponseBodyAsString()), 
/* 288 */           Integer.toString(httpEx.getRawStatusCode()));
/*     */     }
/* 290 */     catch (ResourceAccessException e) {
/* 291 */       LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/*     */       
/* 293 */       List<String> ids = (List<String>)certificateRequests.stream().map(PKICertificateRequest::getInternalCSRId).collect(Collectors.toList());
/* 294 */       this.statusChanger.moveAllCSRToPKITimeoutState(ids);
/* 295 */       logCannotAccess(e, pkiTimeCertificatesUrl);
/* 296 */       throw new CertificateNotFoundException(this.i18n
/* 297 */           .getMessage("noTimeCertFoundMatchingFilter") + " Reason: " + e
/* 298 */           .getMessage());
/*     */     } 
/* 300 */     return Collections.emptyList();
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
/*     */   public List<PKICertificateResponse> downloadTimeCertificatesWithRetry(List<PKICertificateRequest> certificateRequests) {
/* 312 */     String pkiTimeCertificatesUrl = this.pkiAndOAuthPropertiesManager.getPkiTimeCertificatesUrl();
/* 313 */     String payload = "";
/*     */     try {
/* 315 */       payload = (new ObjectMapper()).writeValueAsString(certificateRequests);
/* 316 */       List<PKICertificateResponse> response = makeRequest(pkiTimeCertificatesUrl, HttpMethod.POST, PKICertificateResponse[].class, payload);
/*     */       
/* 318 */       markAsProcessed(certificateRequests);
/* 319 */       return response;
/* 320 */     } catch (IOException e) {
/* 321 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 322 */       this.logger.log(Level.WARNING, "000257X", "Could not create request for downloading user certificates. Reason: " + e
/* 323 */           .getMessage(), CLASS_NAME);
/* 324 */     } catch (HttpStatusCodeException httpEx) {
/* 325 */       LOG.log(Level.FINEST, httpEx.getMessage(), (Throwable)httpEx);
/* 326 */       handleHttpErrorCodeLogger(pkiTimeCertificatesUrl, 
/* 327 */           HttpEntityFactory.buildDefaultPKIRequestEntityWithNoToken(MediaType.APPLICATION_JSON, payload), httpEx
/* 328 */           .getRawStatusCode(), httpEx.getResponseBodyAsString());
/* 329 */       downloadFailedInSession(certificateRequests, httpEx);
/* 330 */       stateChange(certificateRequests, httpEx);
/* 331 */       checkNotAuthorized(httpEx);
/*     */       
/* 333 */       if (httpEx.getStatusCode().is4xxClientError()) {
/* 334 */         this.logger.log(Level.WARNING, "000320X", "User does not have permissions for downloading  certificate of type: TIME", CLASS_NAME);
/*     */       }
/*     */     }
/* 337 */     catch (ResourceAccessException e) {
/* 338 */       LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/* 339 */       logCannotAccess(e, pkiTimeCertificatesUrl);
/* 340 */       if (this.updateSession.isRunning()) {
/* 341 */         return executeRetry(certificateRequests, e, this::downloadTimeCertificatesWithRetry, pkiTimeCertificatesUrl);
/*     */       }
/*     */       
/* 344 */       return Collections.emptyList();
/*     */     } 
/*     */     
/* 347 */     return Collections.emptyList();
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
/*     */   public List<PKICertificateResponse> downloadEnhancedRightsCertificates(List<PKIEnhancedCertificateRequest> certificateRequests, boolean isUpdateRunning) {
/* 359 */     String pkiEnhancedRightsCertificatesUrl = this.pkiAndOAuthPropertiesManager.getPkiEnhancedRightsCertificatesUrl();
/* 360 */     String payload = ""; try {
/*     */       List<PKICertificateResponse> response;
/* 362 */       payload = (new ObjectMapper()).writeValueAsString(certificateRequests);
/*     */       
/* 364 */       if (isUpdateRunning) {
/* 365 */         response = makeRequest(pkiEnhancedRightsCertificatesUrl, HttpMethod.POST, PKICertificateResponse[].class, payload);
/* 366 */         markAsProcessed((List)certificateRequests);
/*     */       } else {
/* 368 */         response = makeRequestWithoutSessionRunning(pkiEnhancedRightsCertificatesUrl, HttpMethod.POST, PKICertificateResponse[].class, payload);
/*     */       } 
/* 370 */       return response;
/* 371 */     } catch (IOException e) {
/* 372 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 373 */       this.logger.log(Level.WARNING, "000257X", "Could not create request for downloading user certificates. Reason: " + e
/* 374 */           .getMessage(), CLASS_NAME);
/* 375 */     } catch (HttpStatusCodeException httpEx) {
/* 376 */       LOG.log(Level.FINEST, httpEx.getMessage(), (Throwable)httpEx);
/* 377 */       handleHttpErrorCodeLogger(pkiEnhancedRightsCertificatesUrl, 
/* 378 */           HttpEntityFactory.buildDefaultPKIRequestEntityWithNoToken(MediaType.APPLICATION_JSON, payload), httpEx
/* 379 */           .getRawStatusCode(), httpEx.getResponseBodyAsString());
/* 380 */       downloadFailedInSession((List)certificateRequests, httpEx);
/* 381 */       stateChangeEnhanced(certificateRequests, httpEx);
/* 382 */       checkNotAuthorized(httpEx);
/* 383 */     } catch (ResourceAccessException e) {
/* 384 */       LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/* 385 */       logCannotAccess(e, pkiEnhancedRightsCertificatesUrl);
/* 386 */       if (isUpdateRunning && this.updateSession.isRunning()) {
/* 387 */         return executeRetry(certificateRequests, e, requests -> downloadEnhancedRightsCertificates(requests, isUpdateRunning), pkiEnhancedRightsCertificatesUrl);
/*     */       }
/* 389 */       return Collections.emptyList();
/*     */     } 
/*     */     
/* 392 */     return Collections.emptyList();
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
/*     */   public List<PKICertificateResponse> downloadSecOCISCertificatesWithoutRetryNoSession(List<PKIEnhancedCertificateRequest> certificateRequests) {
/* 404 */     String pkiSecOCISCertificatesUrl = this.pkiAndOAuthPropertiesManager.getPkiSecOCISCertificatesUrl();
/* 405 */     String payload = "";
/*     */     try {
/* 407 */       payload = (new ObjectMapper()).writeValueAsString(certificateRequests);
/* 408 */       return makeRequestWithoutSessionRunning(pkiSecOCISCertificatesUrl, HttpMethod.POST, PKICertificateResponse[].class, payload);
/*     */     }
/* 410 */     catch (IOException e) {
/* 411 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 412 */       this.logger.log(Level.WARNING, "000257X", "Could not create request for downloading user certificates. Reason: " + e
/* 413 */           .getMessage(), CLASS_NAME);
/* 414 */     } catch (HttpStatusCodeException e) {
/* 415 */       LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/* 416 */       handleHttpErrorCodeLogger(pkiSecOCISCertificatesUrl, 
/* 417 */           HttpEntityFactory.buildDefaultPKIRequestEntityWithNoToken(MediaType.APPLICATION_JSON, payload), e
/* 418 */           .getRawStatusCode(), e.getResponseBodyAsString());
/* 419 */       stateChangeEnhanced(certificateRequests, e);
/* 420 */       if (e.getStatusCode() == HttpStatus.FORBIDDEN || e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
/* 421 */         this.logger.log(Level.WARNING, "000320X", "User does not have permissions for downloading  certificate of type: SECOCIS", CLASS_NAME);
/*     */         
/* 423 */         throw new DownloadRightsException(this.statusChanger.getPKIErrorMessage(e.getResponseBodyAsString()), e
/* 424 */             .getRawStatusCode());
/*     */       } 
/* 426 */       String errorBody = e.getResponseBodyAsString();
/* 427 */       String errorPayload = StringUtils.isEmpty(errorBody) ? String.format("Http error in requesting %s certificates with status: %s", new Object[] { "SECOCIS", e.getStatusCode() }) : errorBody;
/* 428 */       this.logger.log(Level.SEVERE, "000234X", errorPayload, CLASS_NAME);
/* 429 */       throw new CertificatesUpdateNoSessionExeption("Download from PKI failed.The CSR is still available in user's specific store.", this.statusChanger
/* 430 */           .getPKIErrorMessage(e.getMessage()), 
/* 431 */           Integer.toString(e.getRawStatusCode()));
/*     */     }
/* 433 */     catch (ResourceAccessException e) {
/* 434 */       LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/*     */       
/* 436 */       List<String> ids = (List<String>)certificateRequests.stream().map(PKICertificateRequest::getInternalCSRId).collect(Collectors.toList());
/* 437 */       this.statusChanger.moveAllCSRToPKITimeoutState(ids);
/* 438 */       logCannotAccess(e, pkiSecOCISCertificatesUrl);
/* 439 */       throw new CertificateNotFoundException(e.getMessage());
/*     */     } 
/*     */     
/* 442 */     return Collections.emptyList();
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
/*     */   public List<PKICertificateResponse> downloadSecOCISCertificatesWithRetry(List<PKIEnhancedCertificateRequest> certificateRequests) {
/* 454 */     String pkiSecOCISCertificatesUrl = this.pkiAndOAuthPropertiesManager.getPkiSecOCISCertificatesUrl();
/* 455 */     String payload = "";
/*     */     try {
/* 457 */       payload = (new ObjectMapper()).writeValueAsString(certificateRequests);
/* 458 */       List<PKICertificateResponse> response = makeRequest(pkiSecOCISCertificatesUrl, HttpMethod.POST, PKICertificateResponse[].class, payload);
/*     */       
/* 460 */       markAsProcessed((List)certificateRequests);
/* 461 */       return response;
/* 462 */     } catch (IOException e) {
/* 463 */       LOG.log(Level.FINEST, e.getMessage(), e);
/* 464 */       this.logger.log(Level.WARNING, "000257X", "Could not create request for downloading user certificates. Reason: " + e
/* 465 */           .getMessage(), CLASS_NAME);
/* 466 */     } catch (HttpStatusCodeException e) {
/* 467 */       LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/* 468 */       handleHttpErrorCodeLogger(pkiSecOCISCertificatesUrl, 
/* 469 */           HttpEntityFactory.buildDefaultPKIRequestEntityWithNoToken(MediaType.APPLICATION_JSON, payload), e
/* 470 */           .getRawStatusCode(), e.getResponseBodyAsString());
/* 471 */       downloadFailedInSession((List)certificateRequests, e);
/* 472 */       stateChangeEnhanced(certificateRequests, e);
/* 473 */       checkNotAuthorized(e);
/*     */       
/* 475 */       if (e.getStatusCode().is4xxClientError()) {
/* 476 */         this.logger.log(Level.WARNING, "000320X", "User does not have permissions for downloading  certificate of type: SECOCIS", CLASS_NAME);
/*     */       }
/*     */     }
/* 479 */     catch (ResourceAccessException e) {
/* 480 */       LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/* 481 */       logCannotAccess(e, pkiSecOCISCertificatesUrl);
/* 482 */       if (this.updateSession.isRunning()) {
/* 483 */         return executeRetry(certificateRequests, e, this::downloadSecOCISCertificatesWithRetry, pkiSecOCISCertificatesUrl);
/*     */       }
/*     */       
/* 486 */       return Collections.emptyList();
/*     */     } 
/*     */     
/* 489 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<NonVsmIdentifier> getNonVsmIdentifiers(String backendSubjectKeyIdentifier) {
/* 500 */     String url = this.pkiAndOAuthPropertiesManager.getPkiIdentifiersNonVsmUrl();
/*     */     try {
/* 502 */       return makeRequest(url, HttpMethod.GET, NonVsmIdentifier[].class, QueryParamFactory.createNonVsmIdentifierParam(backendSubjectKeyIdentifier));
/* 503 */     } catch (HttpStatusCodeException e) {
/* 504 */       this.updateSession.resetEcuCondition(Boolean.valueOf(true));
/* 505 */       String payload = "{\"caId\": \"" + backendSubjectKeyIdentifier + "\"}";
/* 506 */       handleHttpErrorCodeLogger(url, 
/* 507 */           HttpEntityFactory.buildDefaultPKIRequestEntityWithNoToken(MediaType.APPLICATION_JSON, payload), e
/* 508 */           .getRawStatusCode(), e.getResponseBodyAsString());
/* 509 */     } catch (ResourceAccessException e) {
/* 510 */       this.updateSession.resetEcuCondition(Boolean.valueOf(true));
/* 511 */       logCannotAccess(e, url);
/* 512 */       this.logger.log(Level.WARNING, "000639X", e.getMessage(), getClass().getSimpleName());
/*     */     } 
/* 514 */     return Collections.emptyList();
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
/*     */   public List<PKIEcuCertificateResponse> downloadNonVsmCertificates(List<NonVsmCertificateRequest> request, boolean isUpdateRunning) {
/* 528 */     String url = this.pkiAndOAuthPropertiesManager.getPkiCertificateNonVsmUrl();
/* 529 */     String requestPayload = "";
/*     */     try {
/* 531 */       ObjectMapper objectMapper = new ObjectMapper();
/* 532 */       requestPayload = objectMapper.writeValueAsString(request);
/* 533 */       if (isUpdateRunning) {
/* 534 */         return makeRequest(url, HttpMethod.POST, PKIEcuCertificateResponse[].class, requestPayload, MediaType.APPLICATION_JSON);
/*     */       }
/* 536 */       return makeRequestWithoutSessionRunning(url, HttpMethod.POST, PKIEcuCertificateResponse[].class, requestPayload, MediaType.APPLICATION_JSON);
/* 537 */     } catch (HttpStatusCodeException e) {
/* 538 */       this.updateSession.resetEcuCondition(Boolean.valueOf(true));
/* 539 */       handleHttpErrorCodeLogger(url, HttpEntityFactory.buildDefaultPKIRequestEntityWithNoToken(MediaType.APPLICATION_JSON, requestPayload), e
/* 540 */           .getRawStatusCode(), e.getResponseBodyAsString());
/* 541 */     } catch (ResourceAccessException e) {
/* 542 */       this.updateSession.resetEcuCondition(Boolean.valueOf(true));
/* 543 */       logCannotAccess(e, url);
/* 544 */       this.logger.log(Level.WARNING, "000641X", e.getMessage(), getClass().getSimpleName());
/* 545 */     } catch (JsonProcessingException e) {
/* 546 */       this.updateSession.resetEcuCondition(Boolean.valueOf(true));
/* 547 */       this.logger.logToFileOnly(getClass().getSimpleName(), e.getMessage(), (Throwable)e);
/* 548 */       this.logger.log(Level.WARNING, "000642X", e.getMessage(), getClass().getSimpleName());
/*     */     } 
/* 550 */     return Collections.emptyList();
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
/*     */   public List<PKIEcuCertificateResponse> downloadVsmCertificates(List<VsmCertificateRequest> request, boolean isUpdateRunning) {
/* 563 */     String url = this.pkiAndOAuthPropertiesManager.getPkiCertificateVsmUrl();
/* 564 */     String requestPayload = "";
/*     */     try {
/* 566 */       ObjectMapper objectMapper = new ObjectMapper();
/* 567 */       requestPayload = objectMapper.writeValueAsString(request);
/* 568 */       if (isUpdateRunning) {
/* 569 */         return makeRequest(url, HttpMethod.POST, PKIEcuCertificateResponse[].class, requestPayload, MediaType.APPLICATION_JSON);
/*     */       }
/* 571 */       return makeRequestWithoutSessionRunning(url, HttpMethod.POST, PKIEcuCertificateResponse[].class, requestPayload, MediaType.APPLICATION_JSON);
/* 572 */     } catch (HttpStatusCodeException e) {
/* 573 */       handleHttpErrorCodeLogger(url, HttpEntityFactory.buildDefaultPKIRequestEntityWithNoToken(MediaType.APPLICATION_JSON, requestPayload), e
/* 574 */           .getRawStatusCode(), e.getResponseBodyAsString());
/* 575 */     } catch (ResourceAccessException e) {
/* 576 */       logCannotAccess(e, url);
/* 577 */       this.logger.log(Level.WARNING, "000641X", e.getMessage(), getClass().getSimpleName());
/* 578 */     } catch (JsonProcessingException e) {
/* 579 */       this.logger.logToFileOnly(getClass().getSimpleName(), e.getMessage(), (Throwable)e);
/* 580 */       this.logger.log(Level.WARNING, "000642X", e.getMessage(), getClass().getSimpleName());
/*     */     } 
/* 582 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   private void checkNotAuthorized(HttpStatusCodeException e) {
/* 586 */     if (e.getRawStatusCode() == 401 || e.getResponseBodyAsString().contains("Invalid token") || e
/* 587 */       .getResponseBodyAsString().contains("Invalid Authorization token ")) {
/* 588 */       throw new InvalidTokenException("Invalid token");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getToken() {
/* 598 */     return this.session.getToken();
/*     */   }
/*     */ 
/*     */   
/*     */   protected PkiPropertiesManager getPkiPropertiesManager() {
/* 603 */     return (PkiPropertiesManager)this.pkiAndOAuthPropertiesManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void stateChange(List<? extends PKICertificateRequest> certificateRequests, HttpStatusCodeException httpEx) {
/* 614 */     if (httpEx.getStatusCode().is5xxServerError() || (httpEx
/* 615 */       .getStatusCode().is4xxClientError() && httpEx.getStatusCode() != HttpStatus.UNAUTHORIZED && httpEx
/* 616 */       .getStatusCode() != HttpStatus.FORBIDDEN)) {
/* 617 */       certificateRequests
/* 618 */         .forEach(request -> this.statusChanger.moveCSRToPKIErrorCase(httpEx, request.getInternalCSRId()));
/*     */     }
/* 620 */     if (httpEx.getStatusCode() == HttpStatus.FORBIDDEN || httpEx.getStatusCode() == HttpStatus.UNAUTHORIZED) {
/* 621 */       certificateRequests
/* 622 */         .forEach(request -> this.statusChanger.moveCSRToNoPermissionsCase(request.getInternalCSRId()));
/*     */     }
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
/*     */   private void stateChangeEnhanced(List<PKIEnhancedCertificateRequest> certificateRequests, HttpStatusCodeException httpEx) {
/* 636 */     if (httpEx.getStatusCode().is5xxServerError() || (httpEx
/* 637 */       .getStatusCode().is4xxClientError() && httpEx.getRawStatusCode() != 401)) {
/* 638 */       certificateRequests
/* 639 */         .forEach(request -> this.statusChanger.moveCSRToPKIErrorCase(httpEx, request.getInternalCSRId()));
/*     */     }
/* 641 */     if (httpEx.getStatusCode() == HttpStatus.FORBIDDEN || httpEx.getStatusCode() == HttpStatus.UNAUTHORIZED) {
/* 642 */       certificateRequests
/* 643 */         .forEach(request -> this.statusChanger.moveCSRToNoPermissionsCase(request.getInternalCSRId()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void downloadFailedInSession(List<? extends PKICertificateRequest> certificateRequests, HttpStatusCodeException httpEx) {
/* 649 */     certificateRequests.forEach(request -> this.logger.logWithException("000311X", "PKI Request failed. Payload: " + request.toString() + " " + httpEx.getResponseBodyAsString(), new CEBASException(httpEx.getMessage())));
/*     */ 
/*     */     
/* 652 */     markAsProcessed(certificateRequests);
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends PKICertificateRequest> List<PKICertificateResponse> executeRetry(List<T> certificateRequests, ResourceAccessException e, Function<List<T>, List<PKICertificateResponse>> callback, String url) {
/* 657 */     if (this.updateSession.getCurrentRetry().intValue() < this.updateSession.getMaxRetries().intValue()) {
/* 658 */       waitForNextRetry(url);
/* 659 */       return callback.apply(certificateRequests);
/*     */     } 
/* 661 */     this.updateSession.setDidFailAllRetries(true);
/* 662 */     moveCSRTToTimeout(certificateRequests);
/* 663 */     this.logger.log(Level.INFO, "000586", e.getMessage(), getClass().getSimpleName());
/* 664 */     throw new CertificatesUpdateException(e.getMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <T extends PKICertificateRequest> void moveCSRTToTimeout(List<T> certificateRequests) {
/* 671 */     List<String> ids = (List<String>)certificateRequests.stream().map(PKICertificateRequest::getInternalCSRId).collect(Collectors.toList());
/* 672 */     ids.addAll((Collection<? extends String>)this.updateSession.getPkiCertificateRequests().stream().map(PKICertificateRequest::getInternalCSRId)
/* 673 */         .collect(Collectors.toList()));
/*     */     
/* 675 */     this.statusChanger.moveAllCSRToPKITimeoutState(ids);
/*     */   }
/*     */   
/*     */   private void markAsProcessed(List<? extends PKICertificateRequest> processedRequests) {
/* 679 */     this.updateSession.getPkiCertificateRequests().removeAll(processedRequests);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\integration\ZenZefiPublicKeyInfrastructureEsi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */