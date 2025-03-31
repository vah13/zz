/*    */ package com.daimler.cebas.logs.control.exception;
/*    */ 
/*    */ import org.springframework.context.annotation.Lazy;
/*    */ import org.springframework.core.annotation.Order;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.http.ResponseEntity;
/*    */ import org.springframework.web.bind.annotation.ControllerAdvice;
/*    */ import org.springframework.web.bind.annotation.ExceptionHandler;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ControllerAdvice
/*    */ @RestController
/*    */ @Lazy
/*    */ @Order(-2147483648)
/*    */ public class LogExceptionHandler
/*    */ {
/*    */   @ExceptionHandler({MissingRangeException.class})
/*    */   public ResponseEntity<String> handleMissingRangeException(MissingRangeException exception) {
/* 27 */     return new ResponseEntity("", HttpStatus.BAD_REQUEST);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\logs\control\exception\LogExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */