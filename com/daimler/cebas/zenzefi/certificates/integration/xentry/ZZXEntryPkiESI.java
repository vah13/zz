/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.integration.xentry;
/*     */ 
/*     */ import com.daimler.cebas.common.control.MetadataManager;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.configuration.control.PkiUrlProperty;
/*     */ import com.daimler.cebas.configuration.control.util.PkiPropertiesManager;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*     */ import com.daimler.cebas.zenzefi.certificates.integration.StatusChanger;
/*     */ import com.daimler.cebas.zenzefi.certificates.integration.ZenZefiPublicKeyInfrastructureEsi;
/*     */ import com.daimler.cebas.zenzefi.certificates.integration.xentry.XentryClient;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.util.ZenZefiPkiAndOidcPropertiesManager;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.springframework.context.annotation.Profile;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CEBASControl
/*     */ @Profile({"AFTERSALES"})
/*     */ public class ZZXEntryPkiESI
/*     */   extends ZenZefiPublicKeyInfrastructureEsi
/*     */ {
/*     */   private static final String XENTRY_DESTINATION_SEND_PROPERTY = "xentry_destination_send";
/*     */   private static final String XENTRY_PORT_PROPERTY = "xentry_port";
/*     */   private static final String XENTRY_ADDRESS_PROPERTY = "xentry_address";
/*     */   private final int xentryPort;
/*     */   private final String xentryAddress;
/*     */   private final String destination;
/*     */   private ZenZefiPkiAndOidcPropertiesManager pkiAndOAuthPropertiesManager;
/*     */   private Environment env;
/*     */   private XentryClient xentryClient;
/*     */   private String pkiBaseURL;
/*     */   
/*     */   public ZZXEntryPkiESI(RestTemplate restTemplate, Logger logger, MetadataManager i18n, Session session, UpdateSession updateSession, ZenZefiPkiAndOidcPropertiesManager pkiAndOAuthPropertiesManager, Environment env, StatusChanger statusChanger) {
/* 111 */     super(restTemplate, logger, i18n, session, updateSession, pkiAndOAuthPropertiesManager, statusChanger);
/* 112 */     this.env = env;
/* 113 */     this.pkiAndOAuthPropertiesManager = pkiAndOAuthPropertiesManager;
/* 114 */     this.xentryClient = new XentryClient(logger);
/* 115 */     this.xentryAddress = env.getProperty("xentry_address");
/* 116 */     this.xentryPort = Integer.valueOf(Objects.<String>requireNonNull(env.getProperty("xentry_port"))).intValue();
/* 117 */     this.destination = env.getProperty("xentry_destination_send");
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getToken() {
/* 122 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   protected PkiPropertiesManager getPkiPropertiesManager() {
/* 127 */     return (PkiPropertiesManager)this.pkiAndOAuthPropertiesManager;
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> List<T> makeRequest(String url, HttpMethod method, Class<T[]> setType) {
/* 132 */     return makeRequest(url, method, setType, "");
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> List<T> makeRequest(String url, HttpMethod method, Class<T[]> setType, String requestJson) {
/* 137 */     if (this.updateSession.isRunning()) {
/* 138 */       return makeRequestWithoutSessionRunning(url, method, setType, requestJson);
/*     */     }
/* 140 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> List<T> makeRequest(String url, HttpMethod method, Class<T[]> setType, String requestJson, MediaType mediaType) {
/* 145 */     if (this.updateSession.isRunning()) {
/* 146 */       return makeRequestWithoutSessionRunning(url, method, setType, requestJson, mediaType);
/*     */     }
/* 148 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> List<T> makeRequest(String url, HttpMethod methodHttp, Class<T[]> setType, MultiValueMap<String, String> queryParams) {
/* 154 */     if (this.updateSession.isRunning()) {
/* 155 */       String method = methodHttp.name();
/* 156 */       String response = this.xentryClient.sendMessage(this.xentryAddress, this.xentryPort, this.destination, method, getBaseURL(), this.xentryClient
/* 157 */           .extractResourceWithQuery(url, queryParams), "");
/* 158 */       return Arrays.asList((T[])this.xentryClient.readObject(response, setType));
/*     */     } 
/* 160 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> List<T> makeRequestWithoutSessionRunning(String url, HttpMethod method, Class<T[]> setType) {
/* 166 */     return makeRequestWithoutSessionRunning(url, method, setType, "");
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> List<T> makeRequestWithoutSessionRunning(String url, HttpMethod method, Class<T[]> setType, String requestJson) {
/* 171 */     return makeRequestWithoutSessionRunning(url, method, setType, requestJson, null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> List<T> makeRequestWithoutSessionRunning(String url, HttpMethod method, Class<T[]> setType, String requestJson, MediaType mediaType) {
/* 176 */     String extractPath = this.xentryClient.extractResource(url);
/* 177 */     String methodName = method.name();
/* 178 */     String response = this.xentryClient.sendMessage(this.xentryAddress, this.xentryPort, this.destination, methodName, getBaseURL(), extractPath, requestJson);
/* 179 */     return Arrays.asList((T[])this.xentryClient.readObject(response, setType));
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> List<T> makeRequestWithoutSessionRunning(String url, HttpMethod method, Class<T[]> setType, MultiValueMap<String, String> queryParams) {
/* 184 */     String extractPath = this.xentryClient.extractResource(url);
/* 185 */     UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParams(queryParams);
/* 186 */     String methodName = method.name();
/* 187 */     String response = this.xentryClient.sendMessage(this.xentryAddress, this.xentryPort, this.destination, methodName, getBaseURL(), extractPath, builder.build().encode().toString());
/* 188 */     return Arrays.asList((T[])this.xentryClient.readObject(response, setType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getBaseURL() {
/* 197 */     if (this.pkiBaseURL == null) {
/* 198 */       this.pkiBaseURL = this.env.getProperty(PkiUrlProperty.PKI_BASE_URL.getProperty());
/*     */     }
/* 200 */     return this.pkiBaseURL;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\integration\xentry\ZZXEntryPkiESI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */