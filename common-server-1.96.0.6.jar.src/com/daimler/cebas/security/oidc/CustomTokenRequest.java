/*    */ package com.daimler.cebas.security.oidc;
/*    */ 
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.http.RequestEntity;
/*    */ import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
/*    */ import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CustomTokenRequest
/*    */   implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>>
/*    */ {
/* 16 */   private OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
/*    */ 
/*    */ 
/*    */   
/*    */   public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
/* 21 */     return this.defaultConverter.convert(req);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\security\oidc\CustomTokenRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */