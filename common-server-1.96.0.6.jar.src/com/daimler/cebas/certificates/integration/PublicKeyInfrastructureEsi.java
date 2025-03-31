/*     */ package com.daimler.cebas.certificates.integration;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.PKCS12Manager;
/*     */ import com.daimler.cebas.certificates.control.exceptions.CertificatesUpdateException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.Custom9xxHttpException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.InvalidTokenException;
/*     */ import com.daimler.cebas.certificates.control.exceptions.PkiPayloadParsingException;
/*     */ import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
/*     */ import com.daimler.cebas.certificates.integration.vo.BackendIdentifier;
/*     */ import com.daimler.cebas.certificates.integration.vo.NonVsmCertificateRequest;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKICertificateRequest;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKICertificateResponse;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKIEcuCertificateResponse;
/*     */ import com.daimler.cebas.certificates.integration.vo.PKIEnhancedCertificateRequest;
/*     */ import com.daimler.cebas.certificates.integration.vo.Permission;
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASService;
/*     */ import com.daimler.cebas.common.control.annotations.ObjectAdapterPattern;
/*     */ import com.daimler.cebas.common.control.http.HttpEntityFactory;
/*     */ import com.daimler.cebas.configuration.control.util.PkiPropertiesManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.client.HttpStatusCodeException;
/*     */ import org.springframework.web.client.ResourceAccessException;
/*     */ import org.springframework.web.client.RestTemplate;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @ObjectAdapterPattern
/*     */ public abstract class PublicKeyInfrastructureEsi
/*     */ {
/*     */   @Value("${feature.certificate.chain.exchange:false}")
/*     */   private boolean certificateChainFeatureEnabled;
/*  71 */   protected static final Logger LOG = Logger.getLogger(PKCS12Manager.class.getSimpleName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   private static final String CLASS_NAME = PublicKeyInfrastructureEsi.class.getName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String SPACE = " ";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String PKI_REQUEST_FAILED_PAYLOAD = "PKI Request failed. Payload: ";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String INVALID_AUTHORIZATION_ERROR = "Invalid Authorization token ";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String INVALID_TOKEN_ERROR = "Invalid token";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAXIMUM_REQ_RESP_LENGHT = 2000;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String EMPTY_PAYLOAD = "Empty payload";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RestTemplate restTemplate;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MetadataManager i18n;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DefaultUpdateSession updateSession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   public PublicKeyInfrastructureEsi(RestTemplate restTemplate, Logger logger, MetadataManager i18n, DefaultUpdateSession updateSession) {
/* 136 */     this.restTemplate = restTemplate;
/* 137 */     this.logger = logger;
/* 138 */     this.i18n = i18n;
/* 139 */     this.updateSession = updateSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BackendIdentifier> getBackendIdentifiers() {
/* 148 */     return getBackendIdentifiersInternal(true, null);
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
/*     */   public List<BackendIdentifier> getPreactiveBackendIdentifiers(boolean onlyDuringUpdateSession, String preactiveCaId) {
/* 161 */     return getBackendIdentifiersInternal(onlyDuringUpdateSession, preactiveCaId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BackendIdentifier> getBackendIdentifiersWithoutSession() {
/* 170 */     return getBackendIdentifiersInternal(false, null);
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
/*     */   private List<BackendIdentifier> getBackendIdentifiersInternal(boolean onlyDuringUpdateSession, String preactiveCaId) {
/* 183 */     String pkiBackendIdentifiersUrl = getPkiBackendIdentifiersUrl();
/*     */     
/*     */     try {
/* 186 */       if (onlyDuringUpdateSession) {
/* 187 */         return makeRequest(pkiBackendIdentifiersUrl, HttpMethod.GET, BackendIdentifier[].class, 
/* 188 */             QueryParamFactory.createRootAndBackendIdentifierParam(preactiveCaId));
/*     */       }
/* 190 */       return makeRequestWithoutSessionRunning(pkiBackendIdentifiersUrl, HttpMethod.GET, BackendIdentifier[].class, 
/* 191 */           QueryParamFactory.createRootAndBackendIdentifierParam(preactiveCaId));
/*     */     }
/* 193 */     catch (HttpStatusCodeException httpEx) {
/* 194 */       LOG.log(Level.FINEST, httpEx.getMessage(), (Throwable)httpEx);
/* 195 */       String message = getErrorMessageFromHttpExceptionBody(getExceptionMessage(httpEx));
/* 196 */       handleHttpErrorCodeLogger(pkiBackendIdentifiersUrl, null, httpEx.getRawStatusCode(), httpEx.getResponseBodyAsString());
/* 197 */       if (isUnauthorized(httpEx)) {
/* 198 */         throw new InvalidTokenException("Invalid token");
/*     */       }
/* 200 */       this.logger.log(Level.INFO, "000575", message, getClass().getSimpleName());
/*     */       
/* 202 */       throw new CertificatesUpdateException(message);
/* 203 */     } catch (ResourceAccessException e) {
/* 204 */       LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/* 205 */       logCannotAccess(e, pkiBackendIdentifiersUrl);
/* 206 */       if (this.updateSession.isRunning()) {
/* 207 */         if (this.updateSession.getCurrentRetry().intValue() < this.updateSession.getMaxRetries().intValue()) {
/* 208 */           waitForNextRetry(pkiBackendIdentifiersUrl);
/* 209 */           return getBackendIdentifiersInternal(onlyDuringUpdateSession, preactiveCaId);
/*     */         } 
/* 211 */         this.updateSession.setDidFailAllRetries(true);
/* 212 */         String errorPayload = e.getMessage();
/* 213 */         this.logger.log(Level.INFO, "000576", errorPayload, getClass().getSimpleName());
/* 214 */         throw new CertificatesUpdateException(errorPayload);
/*     */       } 
/*     */       
/* 217 */       return Collections.emptyList();
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
/*     */   public Set<String> getCertificatesChain(List<BackendIdentifier> backendIdentifiers) {
/* 230 */     String pkiCertificatesChainUrl = getPkiPropertiesManager().getPkiCertificatesChainUrl();
/*     */     try {
/* 232 */       return (Set<String>)backendIdentifiers.stream()
/* 233 */         .map(backendIdentifier -> makeRequest(pkiCertificatesChainUrl, HttpMethod.GET, String[].class, QueryParamFactory.createCertChainParam(backendIdentifier.getSubjectKeyIdentifier())))
/*     */         
/* 235 */         .flatMap(Collection::stream).collect(Collectors.toSet());
/* 236 */     } catch (HttpStatusCodeException httpEx) {
/* 237 */       LOG.log(Level.FINEST, httpEx.getMessage(), (Throwable)httpEx);
/* 238 */       handleHttpErrorCodeLogger(pkiCertificatesChainUrl, null, httpEx.getRawStatusCode(), httpEx.getResponseBodyAsString());
/* 239 */       if (isUnauthorized(httpEx)) {
/* 240 */         throw new InvalidTokenException("Invalid token");
/*     */       }
/* 242 */       String message = getErrorMessageFromHttpExceptionBody(getExceptionMessage(httpEx));
/* 243 */       this.logger.log(Level.INFO, "000577", message, getClass().getSimpleName());
/* 244 */       throw new CertificatesUpdateException(message);
/* 245 */     } catch (ResourceAccessException e) {
/* 246 */       LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/* 247 */       logCannotAccess(e, pkiCertificatesChainUrl);
/* 248 */       if (this.updateSession.isRunning()) {
/* 249 */         if (this.updateSession.getCurrentRetry().intValue() < this.updateSession.getMaxRetries().intValue()) {
/* 250 */           waitForNextRetry(pkiCertificatesChainUrl);
/* 251 */           return getCertificatesChain(backendIdentifiers);
/*     */         } 
/* 253 */         this.updateSession.setDidFailAllRetries(true);
/* 254 */         throw new CertificatesUpdateException(e.getMessage());
/*     */       } 
/*     */       
/* 257 */       return Collections.emptySet();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Permission> getPermissions() {
/* 266 */     String pkiPermissionsUrl = getPkiPropertiesManager().getPkiPermissionsUrl();
/*     */     try {
/* 268 */       return makeRequest(pkiPermissionsUrl, HttpMethod.GET, Permission[].class);
/* 269 */     } catch (HttpStatusCodeException httpEx) {
/* 270 */       LOG.log(Level.FINEST, httpEx.getMessage(), (Throwable)httpEx);
/* 271 */       handleHttpErrorCodeLogger(pkiPermissionsUrl, null, httpEx.getRawStatusCode(), httpEx
/* 272 */           .getResponseBodyAsString());
/* 273 */       if (isUnauthorized(httpEx)) {
/* 274 */         throw new InvalidTokenException("Invalid token");
/*     */       }
/* 276 */       String message = getErrorMessageFromHttpExceptionBody(getExceptionMessage(httpEx));
/* 277 */       throw new CertificatesUpdateException(message);
/* 278 */     } catch (ResourceAccessException e) {
/* 279 */       LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/* 280 */       logCannotAccess(e, pkiPermissionsUrl);
/* 281 */       if (this.updateSession.isRunning()) {
/* 282 */         if (this.updateSession.getCurrentRetry().intValue() < this.updateSession.getMaxRetries().intValue()) {
/* 283 */           waitForNextRetry(pkiPermissionsUrl);
/* 284 */           return getPermissions();
/*     */         } 
/* 286 */         this.updateSession.setDidFailAllRetries(true);
/* 287 */         throw new CertificatesUpdateException(e.getMessage());
/*     */       } 
/*     */       
/* 290 */       return Collections.emptyList();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> List<T> makeRequest(String url, HttpMethod method, Class<T[]> setType) {
/* 339 */     if (this.updateSession.isRunning()) {
/* 340 */       ResponseEntity<String> pkiResponse = this.restTemplate.exchange(url, method, HttpEntityFactory.buildDefaultPKIRequestEntity(getToken()), String.class, new Object[0]);
/* 341 */       return deserializePKIResponse(pkiResponse, setType);
/*     */     } 
/* 343 */     return Collections.emptyList();
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
/*     */   protected <T> List<T> makeRequest(String url, HttpMethod method, Class<T[]> setType, MultiValueMap<String, String> queryParams) {
/* 362 */     if (this.updateSession.isRunning()) {
/* 363 */       UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParams(queryParams);
/* 364 */       ResponseEntity<String> pkiResponse = this.restTemplate.exchange(builder.build().encode().toUri(), method, HttpEntityFactory.buildDefaultPKIRequestEntity(getToken()), String.class);
/*     */       
/* 366 */       return deserializePKIResponse(pkiResponse, setType);
/*     */     } 
/* 368 */     return Collections.emptyList();
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
/*     */   protected <T> List<T> makeRequest(String url, HttpMethod method, Class<T[]> setType, String requestJson) {
/* 387 */     return makeRequest(url, method, setType, requestJson, MediaType.APPLICATION_JSON);
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
/*     */   protected <T> List<T> makeRequest(String url, HttpMethod method, Class<T[]> setType, String requestJson, MediaType mediaType) {
/* 409 */     if (this.updateSession.isRunning()) {
/* 410 */       HttpEntity httpEntity = HttpEntityFactory.buildDefaultPKIRequestEntity(getToken(), mediaType, requestJson);
/* 411 */       ResponseEntity<String> pkiResponse = this.restTemplate.exchange(url, method, httpEntity, String.class, new Object[0]);
/* 412 */       return deserializePKIResponse(pkiResponse, setType);
/*     */     } 
/* 414 */     return Collections.emptyList();
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
/*     */   protected <T> List<T> makeRequestWithoutSessionRunning(String url, HttpMethod method, Class<T[]> setType) {
/* 431 */     ResponseEntity<String> pkiResponse = this.restTemplate.exchange(url, method, HttpEntityFactory.buildDefaultPKIRequestEntity(getToken()), String.class, new Object[0]);
/* 432 */     return deserializePKIResponse(pkiResponse, setType);
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
/*     */   protected <T> List<T> makeRequestWithoutSessionRunning(String url, HttpMethod method, Class<T[]> setType, String requestJson) {
/* 451 */     return makeRequestWithoutSessionRunning(url, method, setType, requestJson, MediaType.APPLICATION_JSON);
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
/*     */   protected <T> List<T> makeRequestWithoutSessionRunning(String url, HttpMethod method, Class<T[]> setType, String requestJson, MediaType mediaType) {
/* 473 */     HttpEntity httpEntity = HttpEntityFactory.buildDefaultPKIRequestEntity(getToken(), mediaType, requestJson);
/* 474 */     ResponseEntity<String> pkiResponse = this.restTemplate.exchange(url, method, httpEntity, String.class, new Object[0]);
/*     */     
/* 476 */     return deserializePKIResponse(pkiResponse, setType);
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
/*     */   protected <T> List<T> makeRequestWithoutSessionRunning(String url, HttpMethod method, Class<T[]> setType, MultiValueMap<String, String> queryParams) {
/* 495 */     UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParams(queryParams);
/* 496 */     ResponseEntity<String> pkiResponse = this.restTemplate.exchange(builder.build().encode().toUri(), method, HttpEntityFactory.buildDefaultPKIRequestEntity(getToken()), String.class);
/*     */     
/* 498 */     return deserializePKIResponse(pkiResponse, setType);
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
/*     */   private <T> List<T> deserializePKIResponse(ResponseEntity<String> pkiResponse, Class<T[]> setType) throws Custom9xxHttpException, PkiPayloadParsingException {
/* 515 */     if (is9xxStatusCode(pkiResponse.getStatusCodeValue())) {
/* 516 */       throw new Custom9xxHttpException(pkiResponse.getStatusCodeValue(), (String)pkiResponse.getBody());
/*     */     }
/*     */     
/* 519 */     if (null != pkiResponse.getBody()) {
/*     */       try {
/* 521 */         ObjectMapper objectMapper = new ObjectMapper();
/* 522 */         T[] body = (T[])objectMapper.readValue((String)pkiResponse.getBody(), setType);
/* 523 */         return new ArrayList<>(Arrays.asList(body));
/* 524 */       } catch (JsonProcessingException e) {
/* 525 */         throw new PkiPayloadParsingException(pkiResponse.getStatusCode(), pkiResponse);
/*     */       } 
/*     */     }
/* 528 */     return new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void waitForNextRetry(String url) {
/* 537 */     if (this.updateSession.isRunning()) {
/* 538 */       Integer currentRetry = this.updateSession.retry(url);
/* 539 */       this.updateSession.setNextRetryTimeInterval();
/* 540 */       Integer nextRetryIntervalTime = this.updateSession.getNextRetryTimeInterval();
/* 541 */       this.logger.log(Level.INFO, "000234", "Retry no. " + currentRetry + " will be performed in " + nextRetryIntervalTime + " seconds for endpoint: " + url, CLASS_NAME);
/*     */       
/*     */       try {
/* 544 */         TimeUnit.MILLISECONDS.sleep(nextRetryIntervalTime.intValue() * 1000L);
/* 545 */       } catch (InterruptedException e) {
/* 546 */         CertificatesUpdateException exception = new CertificatesUpdateException(e.getMessage());
/* 547 */         this.logger.logWithException("000313X", "Task interrupted " + e.getMessage(), (CEBASException)exception);
/*     */         
/* 549 */         Thread.currentThread().interrupt();
/* 550 */         throw exception;
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleHttpErrorCodeLogger(String url, HttpEntity<?> httpEntity, int httpStatus, String errorResponseMessage) {
/* 569 */     String requestLogMessage = "Error in PKI request on url: " + url + ((httpEntity != null) ? (" : with payload: " + getTrunkOfMaximumChs(httpEntity.toString())) : "");
/* 570 */     String responseLogMessage = "Error PKI response message: " + getTrunkOfMaximumChs(errorResponseMessage) + " with HttpStatus: " + httpStatus;
/*     */ 
/*     */     
/* 573 */     this.logger.log(Level.INFO, "000628X", requestLogMessage, getClass().getSimpleName());
/* 574 */     this.logger.log(Level.INFO, "000646X", responseLogMessage, getClass().getSimpleName());
/*     */   }
/*     */   
/*     */   private String getTrunkOfMaximumChs(String message) {
/* 578 */     if (StringUtils.isEmpty(message)) {
/* 579 */       return "Empty payload";
/*     */     }
/*     */     
/* 582 */     String msg = (message.length() > 2000) ? (message.substring(0, 2000) + "[...]") : message;
/*     */     
/* 584 */     return msg.replace("\n", "").replace("\r", "");
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
/*     */   protected void logCannotAccess(ResourceAccessException e, String url) {
/* 608 */     String trimmedMessage = (e.getCause() != null) ? (" " + e.getCause().getMessage()) : "";
/* 609 */     this.logger.logWithException("000312X", "Exception while accessing PKI URI " + url + trimmedMessage, (CEBASException)new CertificatesUpdateException(e
/*     */           
/* 611 */           .getMessage()));
/* 612 */     this.logger.logToFileOnly(getClass().getSimpleName(), e.getMessage(), (Throwable)e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isUnauthorized(HttpStatusCodeException httpEx) {
/* 619 */     return (httpEx.getRawStatusCode() == 401 || httpEx.getResponseBodyAsString().contains("Invalid token") || httpEx
/* 620 */       .getResponseBodyAsString().contains("Invalid Authorization token "));
/*     */   }
/*     */   
/*     */   private boolean is9xxStatusCode(int statusCodeValue) {
/* 624 */     return (statusCodeValue >= 900);
/*     */   }
/*     */   
/*     */   private String getErrorMessageFromHttpExceptionBody(String jsonText) {
/* 628 */     if (StringUtils.isEmpty(jsonText)) {
/* 629 */       return jsonText;
/*     */     }
/*     */     
/*     */     try {
/* 633 */       ObjectMapper mapper = new ObjectMapper();
/* 634 */       JsonNode jsonObj = mapper.readTree(jsonText);
/* 635 */       JsonNode messageNode = jsonObj.get("message");
/* 636 */       if (messageNode == null) {
/* 637 */         return "Invalid error payload received: " + jsonText.replace("\n", "").replace("\r", "");
/*     */       }
/* 639 */       return messageNode.textValue();
/* 640 */     } catch (IOException parseException) {
/*     */       
/* 642 */       return jsonText;
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getExceptionMessage(HttpStatusCodeException httpEx) {
/* 647 */     String exceptionMessage = httpEx.getResponseBodyAsString();
/* 648 */     if (StringUtils.isEmpty(exceptionMessage)) {
/* 649 */       if (StringUtils.isEmpty(httpEx.getStatusText())) {
/* 650 */         return "";
/*     */       }
/* 652 */       exceptionMessage = httpEx.getStatusText();
/*     */     } 
/* 654 */     return exceptionMessage;
/*     */   }
/*     */   
/*     */   private String getPkiBackendIdentifiersUrl() {
/* 658 */     if (this.certificateChainFeatureEnabled) {
/* 659 */       return getPkiPropertiesManager().getPkiBackendIdentifiersUrlV2();
/*     */     }
/* 661 */     return getPkiPropertiesManager().getPkiBackendIdentifiersUrl();
/*     */   }
/*     */   
/*     */   public abstract List<PKICertificateResponse> downloadCertificates(List<PKICertificateRequest> paramList);
/*     */   
/*     */   public abstract List<PKIEcuCertificateResponse> downloadNonVsmCertificates(List<NonVsmCertificateRequest> paramList, boolean paramBoolean);
/*     */   
/*     */   public abstract List<PKICertificateResponse> downloadEnhancedRightsCertificates(List<PKIEnhancedCertificateRequest> paramList, boolean paramBoolean);
/*     */   
/*     */   protected abstract String getToken();
/*     */   
/*     */   protected abstract PkiPropertiesManager getPkiPropertiesManager();
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\integration\PublicKeyInfrastructureEsi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */