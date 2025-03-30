/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control;
/*     */ 
/*     */ import com.daimler.cebas.common.control.HttpHeaderFactory;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.util.ZenZefiPkiAndOidcPropertiesManager;
/*     */ import com.daimler.cebas.zenzefi.security.SessionUserIdAuthentication;
/*     */ import com.daimler.cebas.zenzefi.security.ZenZefiSecurityConfiguration;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Base64;
/*     */ import java.util.Date;
/*     */ import java.util.logging.Level;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.annotation.Profile;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.security.core.Authentication;
/*     */ import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
/*     */ import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
/*     */ import org.springframework.security.oauth2.core.OAuth2AccessToken;
/*     */ import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ @CEBASControl
/*     */ @Profile({"!AFTERSALES"})
/*     */ public class RefreshTokenClient
/*     */ {
/*     */   private static final String OIDC_KEY_TOKEN = "token";
/*     */   private static final String OIDC_KEY_TOKEN_TYPE_HINT = "token_type_hint";
/*     */   private static final String HEADER_BASIC_AUTH = "Basic ";
/*     */   private static final String HEADER_AUTH_DIVIDER = ":";
/*     */   private static final long OIDC_DEFAULT_EXPIRATION = 1800L;
/*     */   @Autowired
/*     */   private ZenZefiSecurityConfiguration securityConfig;
/*     */   @Autowired
/*     */   private Logger logger;
/*     */   @Autowired
/*     */   private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
/*     */   @Autowired
/*     */   private ZenZefiPkiAndOidcPropertiesManager pkiAndOAuthPropertiesManager;
/*     */   @Autowired
/*     */   private Session session;
/*     */   
/*     */   public boolean isTokenExpired() {
/*  65 */     if (this.session.isDefaultUser()) {
/*  66 */       return false;
/*     */     }
/*  68 */     if (this.session.getTokenExpirationDate() == null) {
/*  69 */       return true;
/*     */     }
/*  71 */     return (new Date()).after(this.session.getTokenExpirationDate());
/*     */   } public OAuth2AuthorizedClient requestNewTokensFromIdp(RestTemplate restTemplate) {
/*     */     String accessToken, refreshToken;
/*     */     long expires;
/*  75 */     OAuth2AuthorizedClient client = this.oAuth2AuthorizedClientService.loadAuthorizedClient("gas", Session.getUserId());
/*     */     
/*  77 */     String tokenUri = client.getClientRegistration().getProviderDetails().getTokenUri();
/*  78 */     ResponseEntity<String> response = restTemplate.exchange(tokenUri, HttpMethod.POST, createRefreshRequest(client), String.class, new Object[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  84 */       ObjectMapper mapper = new ObjectMapper();
/*  85 */       JsonNode jsonObj = mapper.readTree((String)response.getBody());
/*  86 */       accessToken = getValueFromJson(jsonObj, "access_token");
/*  87 */       refreshToken = getValueFromJson(jsonObj, "refresh_token");
/*  88 */       expires = getLongValueFrom(jsonObj, "expires_in", 1800L);
/*     */     }
/*  90 */     catch (IOException parseException) {
/*  91 */       this.logger.logToFileOnly(getClass().getSimpleName(), "Could not parse the response received from the IDP", parseException);
/*     */       
/*  93 */       this.logger.log(Level.INFO, "000663X", "Could not parse the response received from the IDP: " + parseException
/*  94 */           .getMessage(), 
/*  95 */           getClass().getSimpleName());
/*  96 */       return null;
/*     */     } 
/*     */     
/*  99 */     OAuth2AuthorizedClient authorizedClient = createAuthorizedClient(client, accessToken, null, expires);
/* 100 */     saveTokens(authorizedClient, refreshToken);
/*     */     
/* 102 */     return authorizedClient;
/*     */   }
/*     */   
/*     */   public void performRevokeTokenRequest(RestTemplate restTemplate) {
/* 106 */     OAuth2AuthorizedClient client = this.oAuth2AuthorizedClientService.loadAuthorizedClient("gas", Session.getUserId());
/* 107 */     String accessToken = client.getAccessToken().getTokenValue();
/*     */     
/*     */     try {
/* 110 */       HttpHeaders headers = HttpHeaderFactory.buildDefaultPKIRequestHeader(accessToken);
/* 111 */       headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
/*     */       
/* 113 */       HttpEntity<MultiValueMap<String, String>> request = createRevokeRequest(client);
/*     */       
/* 115 */       this.logger.log(Level.INFO, "000657", "Token revocation request will be executed against: " + this.pkiAndOAuthPropertiesManager
/* 116 */           .getOidcRevokeTokenUri(), getClass().getSimpleName());
/* 117 */       restTemplate.exchange(this.pkiAndOAuthPropertiesManager.getOidcRevokeTokenUri(), HttpMethod.POST, request, String.class, new Object[0]);
/*     */       
/* 119 */       this.logger.log(Level.INFO, "000658", "Token revocation executed successfully", 
/* 120 */           getClass().getSimpleName());
/* 121 */     } catch (Exception e) {
/* 122 */       String msg = "Error while invalidating token with Idp server. Reason: " + e.getMessage();
/* 123 */       this.logger.logToFileOnly(getClass().getSimpleName(), msg, e);
/* 124 */       this.logger.log(Level.WARNING, "000655X", msg, getClass().getSimpleName());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private HttpEntity<MultiValueMap<String, String>> createRevokeRequest(OAuth2AuthorizedClient client) throws IOException {
/* 130 */     HttpHeaders headers = new HttpHeaders();
/* 131 */     headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
/*     */     
/* 133 */     String clientId = client.getClientRegistration().getClientId() + ":";
/*     */     
/* 135 */     headers.add("Authorization", "Basic " + 
/* 136 */         Base64.getEncoder().encodeToString(clientId.getBytes(StandardCharsets.UTF_8)));
/*     */     
/* 138 */     OAuth2AuthorizedClient oAuthClient = this.securityConfig.getOAuthClient(Session.getUserId());
/*     */     
/* 140 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/*     */     
/* 142 */     if (oAuthClient.getAccessToken() == null) {
/* 143 */       throw new IOException("There is no access token available: a revoke request can not be created.");
/*     */     }
/*     */     
/* 146 */     linkedMultiValueMap.add("token", oAuthClient.getAccessToken().getTokenValue());
/* 147 */     linkedMultiValueMap.add("token_type_hint", "access_token");
/*     */     
/* 149 */     return new HttpEntity(linkedMultiValueMap, (MultiValueMap)headers);
/*     */   }
/*     */ 
/*     */   
/*     */   private HttpEntity<MultiValueMap<String, String>> createRefreshRequest(OAuth2AuthorizedClient client) {
/* 154 */     HttpHeaders headers = new HttpHeaders();
/* 155 */     headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
/*     */     
/* 157 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 158 */     linkedMultiValueMap.add("grant_type", "refresh_token");
/* 159 */     linkedMultiValueMap.add("client_id", client.getClientRegistration().getClientId());
/* 160 */     linkedMultiValueMap.add("refresh_token", this.session.getRefreshToken());
/*     */     
/* 162 */     return new HttpEntity(linkedMultiValueMap, (MultiValueMap)headers);
/*     */   }
/*     */ 
/*     */   
/*     */   private String getValueFromJson(JsonNode jsonObj, String key) throws IOException {
/* 167 */     String value = jsonObj.get(key).textValue();
/*     */     
/* 169 */     if (value == null) {
/* 170 */       throw new IOException("Could not parse the required key: " + key + " from the IDP response.");
/*     */     }
/* 172 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   private long getLongValueFrom(JsonNode jsonObj, String key, long defaultValue) {
/* 177 */     long value = jsonObj.get(key).asLong();
/* 178 */     if (value == 0L) {
/* 179 */       this.logger.log(Level.WARNING, "000667", "Could not parse the required numeric key: " + key + " from the IDP response - wrong format: " + jsonObj
/*     */           
/* 181 */           .get(key) + ". Will return default value: " + defaultValue, 
/* 182 */           getClass().getSimpleName());
/* 183 */       return defaultValue;
/*     */     } 
/* 185 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private OAuth2AuthorizedClient createAuthorizedClient(OAuth2AuthorizedClient client, String accessToken, String refreshToken, long expires) {
/* 191 */     OAuth2AccessTokenResponse tokenResponse = OAuth2AccessTokenResponse.withToken(accessToken).tokenType(OAuth2AccessToken.TokenType.BEARER).expiresIn(expires).build();
/* 192 */     return new OAuth2AuthorizedClient(client.getClientRegistration(), Session.getUserId(), tokenResponse
/* 193 */         .getAccessToken(), null);
/*     */   }
/*     */   
/*     */   private void saveTokens(OAuth2AuthorizedClient authorizedClient, String refreshToken) {
/* 197 */     this.oAuth2AuthorizedClientService.saveAuthorizedClient(authorizedClient, (Authentication)new SessionUserIdAuthentication());
/*     */     
/* 199 */     this.session.setToken(authorizedClient.getAccessToken().getTokenValue());
/* 200 */     this.session.setRefreshToken(refreshToken);
/* 201 */     this.session.setTokenExpirationDate(Date.from(authorizedClient.getAccessToken().getExpiresAt()));
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\RefreshTokenClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */