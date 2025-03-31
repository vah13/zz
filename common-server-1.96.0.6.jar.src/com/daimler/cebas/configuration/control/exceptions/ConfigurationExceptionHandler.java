/*    */ package com.daimler.cebas.configuration.control.exceptions;
/*    */ 
/*    */ import com.daimler.cebas.common.control.vo.CEBASResult;
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
/*    */ public class ConfigurationExceptionHandler
/*    */ {
/*    */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*    */   @ExceptionHandler({ZenZefiConfigurationException.class})
/*    */   public ResponseEntity<CEBASResult> handleConfigurationException(ZenZefiConfigurationException e) {
/* 25 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @ResponseStatus(HttpStatus.FORBIDDEN)
/*    */   @ExceptionHandler({ZenZefiNotAllowedConfigurationException.class})
/*    */   public ResponseEntity<CEBASResult> handleNotAllowedConfigurationException(ZenZefiNotAllowedConfigurationException e) {
/* 33 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.FORBIDDEN);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\exceptions\ConfigurationExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */