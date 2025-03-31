/*     */ package com.daimler.cebas.common.control;
/*     */ 
/*     */ import com.daimler.cebas.common.control.vo.BadRequestResult;
/*     */ import com.daimler.cebas.common.control.vo.CEBASResult;
/*     */ import com.daimler.cebas.logs.control.Logger;
/*     */ import com.daimler.cebas.users.control.exceptions.ApplicationNotStartedException;
/*     */ import java.util.HashMap;
/*     */ import java.util.logging.Level;
/*     */ import javax.persistence.PersistenceException;
/*     */ import javax.persistence.PessimisticLockException;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.annotation.Lazy;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.dao.PessimisticLockingFailureException;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.orm.ObjectOptimisticLockingFailureException;
/*     */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*     */ import org.springframework.web.bind.MissingServletRequestParameterException;
/*     */ import org.springframework.web.bind.annotation.ControllerAdvice;
/*     */ import org.springframework.web.bind.annotation.ExceptionHandler;
/*     */ import org.springframework.web.bind.annotation.ResponseStatus;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ControllerAdvice
/*     */ @RestController
/*     */ @Lazy
/*     */ @Order(2147483647)
/*     */ public class GenericExceptionHandler
/*     */ {
/*     */   public static final String ERROR_MESSAGE_PROPERTY_KEY = "message";
/*     */   public static final String DEFAULT_PAYLOAD_LANGUAGE = "en";
/*     */   @Autowired
/*     */   private Logger logger;
/*     */   @Autowired
/*     */   private MetadataManager i18n;
/*     */   
/*     */   @ExceptionHandler({IllegalArgumentException.class})
/*     */   public ResponseEntity<CEBASResult> handleIllegalArgumentException(IllegalArgumentException e, WebRequest r) {
/*  70 */     this.logger.log(Level.INFO, "000465X", this.i18n
/*  71 */         .getMessage("genericExceptionOccurred", new String[] { getUriDescription(r)
/*  72 */           }), getClass().getSimpleName());
/*  73 */     this.logger.logToFileOnly(getClass().getSimpleName(), "Illegal Argument.", e);
/*  74 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ExceptionHandler({MissingServletRequestParameterException.class})
/*     */   public void handleMissingMandatoryRequestParam(MissingServletRequestParameterException e, WebRequest r) throws MissingServletRequestParameterException {
/*  90 */     String message = this.i18n.getMessage("mandatoryParamMissing", new String[] { getUriDescription(r) });
/*  91 */     this.logger.log(Level.INFO, "000471X", message, getClass().getSimpleName());
/*  92 */     throw e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
/*     */   public void handleHttpMethodNoSupported(HttpRequestMethodNotSupportedException e, WebRequest r) throws HttpRequestMethodNotSupportedException {
/* 108 */     String httpMethod = e.getMethod();
/* 109 */     String message = this.i18n.getMessage("httpMethodNotSupported", new String[] { httpMethod, 
/* 110 */           getUriDescription(r) });
/* 111 */     this.logger.log(Level.INFO, "000470X", message, getClass().getSimpleName());
/* 112 */     throw e;
/*     */   }
/*     */   
/*     */   private String getUriDescription(WebRequest request) {
/* 116 */     String description = "n/a";
/* 117 */     if (request != null) {
/* 118 */       description = request.getDescription(false);
/*     */     }
/*     */     
/* 121 */     return description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ExceptionHandler({PessimisticLockException.class})
/*     */   public ResponseEntity<String> handlePessimisticLockException(PessimisticLockException e) {
/* 133 */     return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ExceptionHandler({PersistenceException.class})
/*     */   public ResponseEntity<HashMap<String, String>> handleBaseException(PersistenceException e) {
/* 145 */     HashMap<String, String> response = new HashMap<>();
/* 146 */     response.put("message", e.getMessage());
/* 147 */     this.logger.log(Level.WARNING, "000422X", e.getMessage(), getClass().getSimpleName());
/* 148 */     HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
/* 149 */     if (e.getCause() instanceof org.hibernate.StaleObjectStateException) {
/* 150 */       status = HttpStatus.PRECONDITION_FAILED;
/*     */     }
/* 152 */     return new ResponseEntity(response, status);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ExceptionHandler({PessimisticLockingFailureException.class})
/*     */   public ResponseEntity<String> handlePessimisticLockException(PessimisticLockingFailureException e) {
/* 164 */     String message = "Current processed table is locked at database level, other transaction got lock timeout";
/* 165 */     this.logger.log(Level.SEVERE, "000368X", message, getClass().getSimpleName());
/* 166 */     return new ResponseEntity(message, HttpStatus.NOT_ACCEPTABLE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ExceptionHandler({ObjectOptimisticLockingFailureException.class})
/*     */   public ResponseEntity<String> handleOptimistickException(ObjectOptimisticLockingFailureException e) {
/* 178 */     this.logger.logToFileOnly(getClass().getSimpleName(), "Optimistic locking exception.", (Throwable)e);
/* 179 */     this.logger.log(Level.SEVERE, "000368X", e.getMessage(), getClass().getSimpleName());
/* 180 */     return new ResponseEntity("Optimistic locking exception.", HttpStatus.NOT_ACCEPTABLE);
/*     */   }
/*     */   
/*     */   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
/*     */   @ExceptionHandler({ApplicationNotStartedException.class})
/*     */   public ResponseEntity<String> handleNotStartedException(ApplicationNotStartedException e, WebRequest r) {
/* 186 */     this.logger.log(Level.INFO, "000673X", e.getMessage(), getClass().getSimpleName());
/* 187 */     return new ResponseEntity("Application is starting up and is not accessible yet", HttpStatus.SERVICE_UNAVAILABLE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ExceptionHandler({Exception.class})
/*     */   public ResponseEntity<String> handleGeneric(Exception e, WebRequest r) {
/* 202 */     if (!StartupStatus.isApplicationStarted()) {
/* 203 */       this.logger.log(Level.INFO, "000677X", e.getMessage(), getClass().getSimpleName());
/* 204 */       return new ResponseEntity("Application is starting up and is not accessible yet", HttpStatus.SERVICE_UNAVAILABLE);
/*     */     } 
/*     */ 
/*     */     
/* 208 */     this.logger.log(Level.INFO, "000463X", this.i18n
/* 209 */         .getMessage("genericExceptionOccurred", new String[] { getUriDescription(r)
/* 210 */           }), getClass().getSimpleName());
/*     */     
/* 212 */     String englishMessage = this.i18n.getEnglishMessage("genericExceptionOccurred", new String[] {
/* 213 */           getUriDescription(r) });
/* 214 */     this.logger.logToFileOnly(getClass().getSimpleName(), englishMessage, e);
/* 215 */     return new ResponseEntity(englishMessage, HttpStatus.BAD_REQUEST);
/*     */   }
/*     */   
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   @ExceptionHandler({HttpMessageNotReadableException.class})
/*     */   public BadRequestResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e, WebRequest r) {
/* 221 */     String jsonInputReadErrorMessage = this.i18n.getMessage("jsonInputParseError", new String[] { getUriDescription(r) }, getLocaleFromRequestHeader(r));
/* 222 */     BadRequestResult badRequestResult = new BadRequestResult(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), jsonInputReadErrorMessage, getUriDescription(r));
/* 223 */     String englishMessage = this.i18n.getEnglishMessage("genericExceptionOccurred", new String[] { getUriDescription(r) });
/* 224 */     this.logger.log(Level.INFO, "000626X", englishMessage, getClass().getSimpleName());
/* 225 */     this.logger.logToFileOnly(getClass().getSimpleName(), englishMessage, (Throwable)e);
/* 226 */     return badRequestResult;
/*     */   }
/*     */   
/*     */   private String getLocaleFromRequestHeader(WebRequest request) {
/* 230 */     String locale = request.getHeader("Locale");
/* 231 */     return (locale != null) ? locale : "en";
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\GenericExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */