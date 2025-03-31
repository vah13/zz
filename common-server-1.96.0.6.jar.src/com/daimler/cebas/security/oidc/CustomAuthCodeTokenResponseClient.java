/*    */ package com.daimler.cebas.security.oidc;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.http.RequestEntity;
/*    */ import org.springframework.http.ResponseEntity;
/*    */ import org.springframework.http.converter.FormHttpMessageConverter;
/*    */ import org.springframework.http.converter.HttpMessageConverter;
/*    */ import org.springframework.security.oauth2.client.endpoint.AbstractOAuth2AuthorizationGrantRequest;
/*    */ import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
/*    */ import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
/*    */ import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
/*    */ import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
/*    */ import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
/*    */ import org.springframework.security.oauth2.core.OAuth2Error;
/*    */ import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
/*    */ import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
/*    */ import org.springframework.stereotype.Component;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.CollectionUtils;
/*    */ import org.springframework.web.client.ResponseErrorHandler;
/*    */ import org.springframework.web.client.RestOperations;
/*    */ import org.springframework.web.client.RestTemplate;
/*    */ 
/*    */ @Component
/*    */ public class CustomAuthCodeTokenResponseClient
/*    */   implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>
/*    */ {
/* 29 */   private Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> requestEntityConverter = (Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>>)new OAuth2AuthorizationCodeGrantRequestEntityConverter();
/*    */   private RestOperations restOperations;
/*    */   
/*    */   public CustomAuthCodeTokenResponseClient() {
/* 33 */     RestTemplate restTemplate = new RestTemplate(Arrays.asList(new HttpMessageConverter[] { (HttpMessageConverter)new FormHttpMessageConverter(), (HttpMessageConverter)new OAuth2AccessTokenResponseHttpMessageConverter() }));
/* 34 */     restTemplate.setErrorHandler((ResponseErrorHandler)new OAuth2ErrorResponseErrorHandler());
/* 35 */     this.restOperations = (RestOperations)restTemplate;
/*    */   }
/*    */   
/*    */   public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
/*    */     ResponseEntity<OAuth2AccessTokenResponse> response;
/* 40 */     Assert.notNull(authorizationCodeGrantRequest, "authorizationCodeGrantRequest cannot be null");
/* 41 */     RequestEntity<?> request = (RequestEntity)this.requestEntityConverter.convert(authorizationCodeGrantRequest);
/*    */     
/*    */     try {
/* 44 */       response = this.restOperations.exchange(request, OAuth2AccessTokenResponse.class);
/* 45 */     } catch (Exception ex) {
/*    */ 
/*    */       
/* 48 */       OAuth2Error oauth2Error = new OAuth2Error("invalid_token_response", "An error occurred while attempting to retrieve the OAuth 2.0 Access Token Response: " + ex.getMessage(), null);
/* 49 */       throw new OAuth2AuthorizationException(oauth2Error, ex);
/*    */     } 
/*    */     
/* 52 */     OAuth2AccessTokenResponse tokenResponse = (OAuth2AccessTokenResponse)response.getBody();
/* 53 */     if (CollectionUtils.isEmpty(tokenResponse.getAccessToken().getScopes()))
/*    */     {
/* 55 */       tokenResponse = OAuth2AccessTokenResponse.withResponse(tokenResponse).scopes(authorizationCodeGrantRequest.getClientRegistration().getScopes()).build();
/*    */     }
/*    */ 
/*    */     
/* 59 */     return tokenResponse;
/*    */   }
/*    */   
/*    */   public void setRequestEntityConverter(Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> requestEntityConverter) {
/* 63 */     Assert.notNull(requestEntityConverter, "requestEntityConverter cannot be null");
/* 64 */     this.requestEntityConverter = requestEntityConverter;
/*    */   }
/*    */   
/*    */   public void setRestOperations(RestOperations restOperations) {
/* 68 */     Assert.notNull(restOperations, "restOperations cannot be null");
/* 69 */     this.restOperations = restOperations;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\security\oidc\CustomAuthCodeTokenResponseClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */