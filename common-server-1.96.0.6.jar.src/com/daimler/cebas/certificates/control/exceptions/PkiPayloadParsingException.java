/*    */ package com.daimler.cebas.certificates.control.exceptions;
/*    */ 
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.http.ResponseEntity;
/*    */ import org.springframework.web.client.HttpStatusCodeException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PkiPayloadParsingException
/*    */   extends HttpStatusCodeException
/*    */ {
/*    */   public PkiPayloadParsingException(HttpStatus statusCode, ResponseEntity<String> pkiResponse) {
/* 15 */     super(statusCode, "Http status: " + statusCode + ". Invalid error payload received: " + (String)pkiResponse.getBody());
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\exceptions\PkiPayloadParsingException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */