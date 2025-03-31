/*    */ package com.daimler.cebas.common.control.http;
/*    */ 
/*    */ import com.daimler.cebas.common.control.HttpHeaderFactory;
/*    */ import org.springframework.http.HttpEntity;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.util.MultiValueMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class HttpEntityFactory
/*    */ {
/*    */   public static HttpEntity buildDefaultPKIRequestEntity(String accessToken) {
/* 24 */     return new HttpEntity((MultiValueMap)HttpHeaderFactory.buildDefaultPKIRequestHeader(accessToken, MediaType.TEXT_PLAIN));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static HttpEntity buildDefaultPKIRequestEntity(String accessToken, MediaType mediaType, String body) {
/* 36 */     return new HttpEntity(body, 
/* 37 */         (MultiValueMap)HttpHeaderFactory.buildDefaultPKIRequestHeader(accessToken, mediaType));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static HttpEntity buildDefaultPKIRequestEntityWithNoToken(MediaType mediaType, String body) {
/* 48 */     return new HttpEntity(body, 
/* 49 */         (MultiValueMap)HttpHeaderFactory.buildDefaultPKIRequestHeaderWithNoAuthCode(mediaType));
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\http\HttpEntityFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */