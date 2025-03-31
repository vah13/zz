/*    */ package com.daimler.cebas.certificates.control.exceptions;
/*    */ 
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.web.client.HttpStatusCodeException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Custom9xxHttpException
/*    */   extends HttpStatusCodeException
/*    */ {
/*    */   private int custom9xxStatusCode;
/*    */   
/*    */   public Custom9xxHttpException(int http9xxCode, String responseBody) {
/* 16 */     this(HttpStatus.CONFLICT, String.format("Http status %s with response body %s", new Object[] { Integer.valueOf(http9xxCode), responseBody }));
/* 17 */     this.custom9xxStatusCode = http9xxCode;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Custom9xxHttpException(HttpStatus statusCode, String statusText) {
/* 22 */     super(statusCode, statusText);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRawStatusCode() {
/* 28 */     return this.custom9xxStatusCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\exceptions\Custom9xxHttpException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */