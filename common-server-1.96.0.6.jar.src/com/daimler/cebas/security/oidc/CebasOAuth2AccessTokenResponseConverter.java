/*    */ package com.daimler.cebas.security.oidc;
/*    */ 
/*    */ import com.daimler.cebas.users.control.Session;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.logging.Logger;
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.security.oauth2.core.OAuth2AccessToken;
/*    */ import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
/*    */ import org.springframework.util.StringUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CebasOAuth2AccessTokenResponseConverter
/*    */   implements Converter<Map<String, String>, OAuth2AccessTokenResponse>
/*    */ {
/* 22 */   private static final Logger LOGGER = Logger.getLogger(CebasOAuth2AccessTokenResponseConverter.class.getName());
/*    */   
/* 24 */   private static final Set<String> TOKEN_RESPONSE_PARAMETER_NAMES = new HashSet<>(
/* 25 */       Arrays.asList(new String[] { "access_token", "token_type", "expires_in", "refresh_token", "scope" }));
/*    */   
/*    */   private Session session;
/*    */ 
/*    */   
/*    */   public CebasOAuth2AccessTokenResponseConverter(Session session) {
/* 31 */     this.session = session;
/*    */   }
/*    */ 
/*    */   
/*    */   public OAuth2AccessTokenResponse convert(Map<String, String> tokenResponseParameters) {
/* 36 */     String accessToken = tokenResponseParameters.get("access_token");
/*    */     
/* 38 */     OAuth2AccessToken.TokenType accessTokenType = null;
/* 39 */     if (OAuth2AccessToken.TokenType.BEARER.getValue()
/* 40 */       .equalsIgnoreCase(tokenResponseParameters.get("token_type"))) {
/* 41 */       accessTokenType = OAuth2AccessToken.TokenType.BEARER;
/*    */     }
/*    */     
/* 44 */     long expiresIn = 0L;
/* 45 */     if (tokenResponseParameters.containsKey("expires_in")) {
/*    */       try {
/* 47 */         expiresIn = Long.parseLong(tokenResponseParameters.get("expires_in"));
/* 48 */       } catch (NumberFormatException ex) {
/* 49 */         LOGGER.warning("000001X - Parsing failed. Reason: " + ex.getMessage());
/*    */       } 
/*    */     }
/*    */     
/* 53 */     Set<String> scopes = Collections.emptySet();
/* 54 */     if (tokenResponseParameters.containsKey("scope")) {
/* 55 */       String scope = tokenResponseParameters.get("scope");
/* 56 */       scopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
/*    */     } 
/*    */     
/* 59 */     String refreshToken = tokenResponseParameters.get("refresh_token");
/* 60 */     this.session.setRefreshToken(refreshToken);
/*    */     
/* 62 */     Map<String, Object> additionalParameters = new LinkedHashMap<>();
/* 63 */     for (Map.Entry<String, String> entry : tokenResponseParameters.entrySet()) {
/* 64 */       if (!TOKEN_RESPONSE_PARAMETER_NAMES.contains(entry.getKey())) {
/* 65 */         additionalParameters.put(entry.getKey(), entry.getValue());
/*    */       }
/*    */     } 
/*    */     
/* 69 */     return 
/* 70 */       OAuth2AccessTokenResponse.withToken(accessToken)
/* 71 */       .tokenType(accessTokenType)
/* 72 */       .expiresIn(expiresIn)
/* 73 */       .scopes(scopes)
/* 74 */       .additionalParameters(additionalParameters)
/* 75 */       .build();
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\security\oidc\CebasOAuth2AccessTokenResponseConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */