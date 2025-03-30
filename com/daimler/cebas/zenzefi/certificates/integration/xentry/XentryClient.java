/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.integration.xentry;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.exceptions.Custom9xxHttpException;
/*     */ import com.daimler.cebas.common.control.CEBASException;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.zenzefi.certificates.integration.xentry.XentryRequest;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.nimbusds.jose.util.StandardCharset;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.ConnectException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.client.HttpClientErrorException;
/*     */ import org.springframework.web.client.HttpServerErrorException;
/*     */ import org.springframework.web.client.ResourceAccessException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XentryClient
/*     */ {
/*     */   private static final String SEPARATOR = ":";
/*     */   private static final String COMMUNICATION_WITH_XENTRY_FAILED = "Communication with Xentry failed: ";
/*     */   private static final String MESSAGE = "message";
/*     */   private static final String XENTRY_RESPONSE_IS_NULL = "Xentry response is null";
/*     */   private static final String CANNOT_START_CONNECTION_WITH_XENTRY = "Cannot start connection with Xentry";
/*     */   private static final String TIMEOUT_FROM_XENTRY_TO_PKI = "Timeout from Xentry to PKI";
/*     */   private static final String CANNOT_EXTRACT_PATH_FROM_URL = "Cannot extract path from URL: ";
/*     */   private static final String CLOSING_XENTRY_CONNECTION_FAILED = "Closing Xentry connection failed";
/*     */   private static final String CANNOT_PARSE_RESPONSE = "Cannot parse response: ";
/*     */   private static final String TIMEOUT = "timeout";
/*     */   private static final String HTTPSTATE = "httpstate";
/*     */   protected Socket clientSocket;
/*     */   protected PrintWriter out;
/*     */   protected BufferedReader in;
/* 105 */   private static final Logger LOG = Logger.getLogger(com.daimler.cebas.zenzefi.certificates.integration.xentry.XentryClient.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   private ObjectMapper objectMapper = new ObjectMapper();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String NO_ERROR = "RETURN:0:";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String UNKNOWN_ERROR = "RETURN:1:";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String BACKEND_TIMEOUT = "RETURN:2:";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String AUTHORIZATION_OF_XD_CLIENT_DENIED = "RETURN:3:";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String AUTHORIZATION_OF_USER_DENIED = "RETURN:4:";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String NO_DATA = "RETURN:5:";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String HTTP_ERROR = "RETURN:6:";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String RESERVED_MARKER = "RETURN:7:";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Logger cebasLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XentryClient(Logger cebasLogger) {
/* 163 */     this.cebasLogger = cebasLogger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startConnection(String ip, int port) {
/*     */     try {
/* 174 */       this.clientSocket = new Socket(ip, port);
/* 175 */       this.clientSocket.setSoTimeout(30000);
/* 176 */       this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
/* 177 */       this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
/* 178 */     } catch (SocketTimeoutException e) {
/* 179 */       LOG.log(Level.FINER, e.getMessage(), e);
/* 180 */       throw new ResourceAccessException("Timeout from Xentry to PKI:" + e.getMessage());
/* 181 */     } catch (ConnectException e) {
/* 182 */       LOG.log(Level.FINER, e.getMessage(), e);
/* 183 */       throw new ResourceAccessException("Cannot start connection with Xentry");
/* 184 */     } catch (IOException e) {
/* 185 */       LOG.log(Level.FINER, e.getMessage(), e);
/* 186 */       throw new ResourceAccessException("Cannot start connection with Xentry:" + e.getMessage());
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
/*     */   public String sendMessage(String ip, int port, String sendDestination, String method, String endpoint, String resource, String payloadJson) {
/*     */     try {
/* 199 */       startConnection(ip, port);
/* 200 */       XentryRequest xentryRequest = new XentryRequest(sendDestination, method, endpoint, resource, payloadJson);
/* 201 */       return xentryCommunication(xentryRequest);
/* 202 */     } catch (SocketTimeoutException e) {
/* 203 */       LOG.log(Level.FINER, e.getMessage(), e);
/* 204 */       throw new ResourceAccessException("Timeout from Xentry to PKI:" + e.getMessage());
/* 205 */     } catch (IOException e) {
/* 206 */       LOG.log(Level.FINER, e.getMessage(), e);
/* 207 */       throw new CEBASException("Communication with Xentry failed: " + e.getMessage());
/*     */     } finally {
/* 209 */       stopConnection();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stopConnection() {
/*     */     try {
/* 218 */       if (this.in != null) {
/* 219 */         this.in.close();
/*     */       }
/* 221 */       if (this.out != null) {
/* 222 */         this.out.close();
/*     */       }
/* 224 */       if (this.clientSocket != null) {
/* 225 */         this.clientSocket.close();
/*     */       }
/* 227 */     } catch (IOException e) {
/* 228 */       LOG.log(Level.WARNING, e.getMessage(), e);
/* 229 */       throw new CEBASException("Closing Xentry connection failed");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String extractResource(String url) {
/*     */     try {
/* 241 */       URL urlResource = new URL(url);
/* 242 */       String path = urlResource.getPath();
/* 243 */       LOG.log(Level.FINE, "Extracted resource: " + path + " from URL: " + urlResource.toExternalForm());
/* 244 */       return path;
/* 245 */     } catch (MalformedURLException e) {
/* 246 */       LOG.log(Level.FINER, e.getMessage(), e);
/* 247 */       throw new CEBASException("Cannot extract path from URL: " + url);
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
/*     */   public String extractResourceWithQuery(String url, MultiValueMap<String, String> queryParams) {
/* 259 */     UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParams(queryParams);
/* 260 */     URI uri = builder.build().encode().toUri();
/* 261 */     if (StringUtils.isBlank(uri.getQuery())) {
/* 262 */       return uri.getPath();
/*     */     }
/* 264 */     return uri.getPath() + "?" + uri.getQuery();
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
/*     */   public <T> T readObject(String response, Class<T> type) {
/*     */     try {
/* 277 */       return (T)this.objectMapper.readValue(response, type);
/* 278 */     } catch (JsonMappingException e) {
/* 279 */       LOG.log(Level.WARNING, e.getMessage(), (Throwable)e);
/* 280 */       throw new CEBASException("Cannot parse response: " + response + " as: " + type.getName());
/* 281 */     } catch (IOException e) {
/* 282 */       LOG.log(Level.FINER, e.getMessage(), e);
/* 283 */       throw new CEBASException("Cannot parse response: " + response);
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
/*     */   protected String xentryCommunication(XentryRequest xentryRequest) throws IOException {
/* 295 */     this.out.println(xentryRequest.toRawData());
/* 296 */     String response = this.in.readLine();
/* 297 */     String originalResponse = response;
/* 298 */     if (response == null) {
/* 299 */       HttpServerErrorException httpServerErrorException = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Xentry response is null");
/*     */       
/* 301 */       LOG.log(Level.WARNING, httpServerErrorException.getMessage(), (Throwable)httpServerErrorException);
/* 302 */       throw httpServerErrorException;
/*     */     } 
/* 304 */     response = errorCheck(response);
/* 305 */     JsonNode actualObj = this.objectMapper.readTree(response);
/* 306 */     if (actualObj != null) {
/* 307 */       JsonNode jsonHttpCode = actualObj.get("httpstate");
/* 308 */       JsonNode timeout = actualObj.get("timeout");
/* 309 */       JsonNode message = actualObj.get("message");
/* 310 */       if (timeout != null && timeout.asBoolean()) {
/* 311 */         throw new ResourceAccessException("Timeout from Xentry to PKI:" + message.asText());
/*     */       }
/* 313 */       if (jsonHttpCode != null) {
/* 314 */         int httpCode = jsonHttpCode.asInt();
/* 315 */         HttpStatus status = HttpStatus.resolve(httpCode);
/* 316 */         if (is9xxStatusCode(httpCode))
/* 317 */           throw new Custom9xxHttpException(httpCode, message.asText()); 
/* 318 */         if (status.is4xxClientError()) {
/* 319 */           throw new HttpClientErrorException(status, message.asText());
/*     */         }
/* 321 */         throw new HttpServerErrorException(status, message.asText());
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 326 */       String errorNoContent = "No data from xetry. Original response: " + originalResponse + " for Xentry Request: " + xentryRequest.toRawData();
/* 327 */       this.cebasLogger.log(Level.WARNING, "000423X", errorNoContent, 
/* 328 */           getClass().getSimpleName());
/* 329 */       throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "No content from xentry", errorNoContent
/* 330 */           .getBytes(StandardCharset.UTF_8), StandardCharset.UTF_8);
/*     */     } 
/*     */     
/* 333 */     return response;
/*     */   }
/*     */   
/*     */   private boolean is9xxStatusCode(int statusCodeValue) {
/* 337 */     return (statusCodeValue >= 900);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String errorCheck(String response) {
/* 347 */     if (response.startsWith("RETURN:0:")) {
/* 348 */       LOG.log(Level.INFO, "Reponse ok from Xentry");
/* 349 */     } else if (response.startsWith("RETURN:1:")) {
/* 350 */       logUnknownErrorFromXentry(response);
/* 351 */     } else if (response.startsWith("RETURN:2:")) {
/* 352 */       LOG.log(Level.WARNING, "Xentry error: Backend timeout");
/* 353 */       this.cebasLogger.log(Level.WARNING, "000423X", "Xentry error: Backend timeout", 
/* 354 */           getClass().getSimpleName());
/* 355 */     } else if (response.startsWith("RETURN:3:")) {
/* 356 */       LOG.log(Level.WARNING, "Xentry error: Authorization of XD client denied");
/* 357 */       this.cebasLogger.log(Level.WARNING, "000423X", "Xentry error: Authorization of XD client denied", 
/* 358 */           getClass().getSimpleName());
/* 359 */     } else if (response.startsWith("RETURN:4:")) {
/* 360 */       LOG.log(Level.WARNING, "Xentry error: Authorization of user denied");
/* 361 */       this.cebasLogger.log(Level.WARNING, "000423X", "Xentry error: Authorization of user denied", 
/* 362 */           getClass().getSimpleName());
/* 363 */     } else if (response.startsWith("RETURN:5:")) {
/* 364 */       LOG.log(Level.WARNING, "Xentry error: No data returned");
/* 365 */       this.cebasLogger.log(Level.WARNING, "000423X", "Xentry error: No data returned", 
/* 366 */           getClass().getSimpleName());
/* 367 */     } else if (response.startsWith("RETURN:6:")) {
/* 368 */       LOG.log(Level.WARNING, "Xentry, HTTP Error");
/* 369 */       this.cebasLogger.log(Level.WARNING, "000423X", "Xentry, HTTP Error", 
/* 370 */           getClass().getSimpleName());
/* 371 */     } else if (response.startsWith("RETURN:7:")) {
/* 372 */       LOG.log(Level.WARNING, "Xentry Error: Reserved");
/* 373 */       this.cebasLogger.log(Level.WARNING, "000423X", "Xentry Error: Reserved", 
/* 374 */           getClass().getSimpleName());
/*     */     } else {
/* 376 */       logUnknownErrorFromXentry(response);
/*     */     } 
/* 378 */     return removeReturnCodeInfo(response);
/*     */   }
/*     */   
/*     */   private void logUnknownErrorFromXentry(String error) {
/* 382 */     LOG.log(Level.WARNING, "Unknown error from Xentry: " + error);
/* 383 */     this.cebasLogger.log(Level.WARNING, "000423X", "Unknown error from Xentry: " + error, 
/* 384 */         getClass().getSimpleName());
/*     */   }
/*     */ 
/*     */   
/*     */   private String removeReturnCodeInfo(String cleanResponse) {
/* 389 */     boolean noCodeAvailable = (StringUtils.startsWith(cleanResponse, "RETURN:[") || StringUtils.startsWith(cleanResponse, "RETURN:{"));
/* 390 */     if (noCodeAvailable) {
/* 391 */       return StringUtils.stripStart(cleanResponse, "RETURN:");
/*     */     }
/*     */     
/* 394 */     String[] splitResponse = cleanResponse.split("RETURN:");
/* 395 */     if (splitResponse.length == 1) {
/* 396 */       return cleanResponse;
/*     */     }
/* 398 */     int offSet = splitResponse[1].indexOf(":");
/* 399 */     if (offSet == -1) {
/* 400 */       return "";
/*     */     }
/* 402 */     return splitResponse[1].substring(offSet + 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\integration\xentry\XentryClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */