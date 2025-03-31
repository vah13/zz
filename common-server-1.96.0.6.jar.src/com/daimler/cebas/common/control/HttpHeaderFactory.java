/*    */ package com.daimler.cebas.common.control;
/*    */ 
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.MediaType;
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
/*    */ public class HttpHeaderFactory
/*    */ {
/*    */   private static final String BEARER = "Bearer ";
/*    */   private static final String NO_CACHE_NO_STORE = "no-cache, no-store";
/*    */   private static final String CACHE_CONTROL = "Cache-Control";
/*    */   
/*    */   public static HttpHeaders buildHeaderCacheControl() {
/* 39 */     HttpHeaders responseHeaders = new HttpHeaders();
/* 40 */     responseHeaders.set("Cache-Control", "no-cache, no-store");
/* 41 */     return responseHeaders;
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
/*    */   
/*    */   public static HttpHeaders buildHeaderCacheControlWithPagination(long page, long pageSize, long maxRows) {
/* 54 */     HttpHeaders responseHeaders = new HttpHeaders();
/* 55 */     responseHeaders.set("Cache-Control", "no-cache, no-store");
/* 56 */     long startingIndex = page * pageSize;
/* 57 */     long endIndex = startingIndex + pageSize;
/* 58 */     responseHeaders.set("Content-Range", "items " + startingIndex + "-" + endIndex + "/" + maxRows);
/*    */     
/* 60 */     return responseHeaders;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static HttpHeaders buildDefaultPKIRequestHeader(String accessToken) {
/* 70 */     HttpHeaders headers = new HttpHeaders();
/* 71 */     headers.add("Authorization", "Bearer " + accessToken);
/* 72 */     return headers;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static HttpHeaders buildDefaultPKIRequestHeader(String accessToken, MediaType mediaType) {
/* 83 */     HttpHeaders headers = new HttpHeaders();
/* 84 */     headers.add("Authorization", "Bearer " + accessToken);
/* 85 */     headers.setContentType(mediaType);
/* 86 */     return headers;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static HttpHeaders buildDefaultPKIRequestHeaderWithNoAuthCode(MediaType mediaType) {
/* 95 */     HttpHeaders headers = new HttpHeaders();
/* 96 */     headers.setContentType(mediaType);
/* 97 */     return headers;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\HttpHeaderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */