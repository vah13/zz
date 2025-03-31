/*    */ package com.daimler.cebas.system.control.exceptions;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.springframework.context.annotation.Lazy;
/*    */ import org.springframework.core.annotation.Order;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.http.ResponseEntity;
/*    */ import org.springframework.web.bind.annotation.ControllerAdvice;
/*    */ import org.springframework.web.bind.annotation.ExceptionHandler;
/*    */ import org.springframework.web.bind.annotation.ResponseStatus;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ControllerAdvice
/*    */ @RestController
/*    */ @Lazy
/*    */ @Order(-2147483648)
/*    */ public class SystemExceptionsHandler
/*    */ {
/*    */   @ResponseStatus(HttpStatus.UNAUTHORIZED)
/*    */   @ExceptionHandler({UnauthorizedOperationException.class})
/*    */   public ResponseEntity<HashMap<String, String>> handleUnauthorizedOperationException(UnauthorizedOperationException e) {
/* 25 */     HashMap<String, String> response = new HashMap<>();
/* 26 */     response.put("message", e.getMessage());
/* 27 */     return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\system\control\exceptions\SystemExceptionsHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */