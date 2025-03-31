/*    */ package com.daimler.cebas.users.control.exceptions;
/*    */ 
/*    */ import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
/*    */ import com.daimler.cebas.users.entity.User;
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
/*    */ public class UserExceptionHandler
/*    */ {
/*    */   @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
/*    */   @ExceptionHandler({UserValidationException.class})
/*    */   public User handleException(UserValidationException exception) {
/* 36 */     User user = exception.getUser();
/* 37 */     if (!"Invalid".equals(user.getUserPassword())) {
/* 38 */       user.setUserPassword("");
/*    */     }
/* 40 */     return user;
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
/*    */   @ExceptionHandler({UserException.class})
/*    */   public ResponseEntity<HashMap<String, String>> handleException(UserException exception) {
/* 53 */     HashMap<String, String> response = new HashMap<>();
/* 54 */     response.put("message", exception.getMessage());
/* 55 */     return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
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
/*    */   @ExceptionHandler({UserLoginException.class})
/*    */   public ResponseEntity<HashMap<String, String>> handleException(UserLoginException exception) {
/* 68 */     HashMap<String, String> response = new HashMap<>();
/* 69 */     response.put("message", exception.getMessage());
/* 70 */     return new ResponseEntity(response, HttpStatus.PRECONDITION_FAILED);
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
/*    */   @ExceptionHandler({ZenZefiConfigurationException.class})
/*    */   public ResponseEntity<HashMap<String, String>> handleException(ZenZefiConfigurationException exception) {
/* 83 */     HashMap<String, String> response = new HashMap<>();
/* 84 */     response.put("message", exception.getMessage());
/* 85 */     return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\ceba\\users\control\exceptions\UserExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */