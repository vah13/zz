/*     */ package com.daimler.cebas.certificates.control.exceptions;
/*     */ 
/*     */ import com.daimler.cebas.certificates.control.vo.CEBASPKIResult;
/*     */ import com.daimler.cebas.common.control.vo.CEBASResult;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.ApplicationEventPublisher;
/*     */ import org.springframework.context.annotation.Lazy;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.validation.FieldError;
/*     */ import org.springframework.web.bind.MethodArgumentNotValidException;
/*     */ import org.springframework.web.bind.annotation.ControllerAdvice;
/*     */ import org.springframework.web.bind.annotation.ExceptionHandler;
/*     */ import org.springframework.web.bind.annotation.ResponseStatus;
/*     */ import org.springframework.web.bind.annotation.RestController;
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
/*     */ @Order(-2147483648)
/*     */ public class CertificateExceptionsHandler
/*     */ {
/*     */   @Autowired
/*     */   private ApplicationEventPublisher publisher;
/*     */   
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   @ExceptionHandler({CEBASCertificateException.class})
/*     */   public ResponseEntity<HashMap<String, String>> handleBaseException(CEBASCertificateException e) {
/*  51 */     HashMap<String, String> response = new HashMap<>();
/*  52 */     response.put("message", e.getMessage());
/*  53 */     return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
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
/*     */   
/*     */   @ResponseStatus(HttpStatus.NOT_FOUND)
/*     */   @ExceptionHandler({ZenzefiCertificateNotFoundForExportPublicKeyFileException.class})
/*     */   public void handleZenzefiCertificateNotFoundForExportPublicKeyFileException(ZenzefiCertificateNotFoundForExportPublicKeyFileException e, HttpServletResponse response) throws IOException {
/*  71 */     response.resetBuffer();
/*  72 */     response.setStatus(404);
/*  73 */     response.setHeader("Content-Type", "application/json");
/*  74 */     response.getOutputStream().print("{\"errorMessage\":\"" + e.getMessage() + "\"}");
/*  75 */     response.flushBuffer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ResponseStatus(HttpStatus.NOT_FOUND)
/*     */   @ExceptionHandler({CertificateNotFoundException.class})
/*     */   public ResponseEntity<CEBASResult> handleCertificateNotFoundException(CertificateNotFoundException e) {
/*  88 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.NOT_FOUND);
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
/*     */   @ResponseStatus(HttpStatus.NOT_FOUND)
/*     */   @ExceptionHandler({CertificateNotFoundOnDownloadException.class})
/*     */   public ResponseEntity<CEBASResult> handleCertificateNotFoundOnDownloadException(CertificateNotFoundOnDownloadException e) {
/* 102 */     return new ResponseEntity(new CEBASPKIResult(e.getMessage(), e.getPkiErrorMessage(), e.getPkiStatusCode()), HttpStatus.NOT_FOUND);
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
/*     */   @ResponseStatus(HttpStatus.CONFLICT)
/*     */   @ExceptionHandler({CertificateMoreResultsFoundException.class})
/*     */   public ResponseEntity<CEBASResult> handleCertificateMoreResultsFoundException(CertificateMoreResultsFoundException e) {
/* 117 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.CONFLICT);
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
/*     */   @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
/*     */   @ExceptionHandler({CertificateSigningReqParentTypeInvalidException.class})
/*     */   public ResponseEntity<CEBASResult> handleCertificateSigningReqParentTypeInvalidException(CertificateSigningReqParentTypeInvalidException e) {
/* 131 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
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
/*     */   @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
/*     */   @ExceptionHandler({CertificateSigningReqEnhRightsException.class})
/*     */   public ResponseEntity<CEBASResult> handleCertificateSigningReqEnhRightsException(CertificateSigningReqEnhRightsException e) {
/* 145 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.PRECONDITION_FAILED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   @ExceptionHandler({CertificateSigningException.class})
/*     */   public ResponseEntity<CEBASResult> handleCertificateSigningException(CertificateSigningException e) {
/* 158 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
/*     */   @ExceptionHandler({CertificateRetrievalException.class})
/*     */   public ResponseEntity<CEBASResult> handleCertificateRetrievalException(CertificateRetrievalException e) {
/* 171 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   @ExceptionHandler({SystemIntegrityEmptyReportException.class})
/*     */   public ResponseEntity<String> handleEmptySystemIntegrityReportException(SystemIntegrityEmptyReportException e) {
/* 184 */     return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
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
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   @ExceptionHandler({AuthKeyIdentSerialNumberPairException.class})
/*     */   public ResponseEntity<CEBASResult> handleAuthKeyIdentSerialNumberPairException(AuthKeyIdentSerialNumberPairException e) {
/* 198 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   @ExceptionHandler({SignatureCheckException.class})
/*     */   public ResponseEntity<CEBASResult> handleSignatureCheckException(SignatureCheckException e) {
/* 211 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   @ExceptionHandler({SecureVariantCodingException.class})
/*     */   public ResponseEntity<CEBASResult> handleSecureVariantException(SecureVariantCodingException e) {
/* 224 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   @ExceptionHandler({EncryptedDiagnosticPackageException.class})
/*     */   public ResponseEntity<CEBASResult> handleEncryptedDiagnosticPackageException(EncryptedDiagnosticPackageException e) {
/* 237 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   @ExceptionHandler({CheckOwnershipException.class})
/*     */   public ResponseEntity<CEBASResult> handleCheckOnwnershipException(CheckOwnershipException e) {
/* 250 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   @ExceptionHandler({SecOCISException.class})
/*     */   public ResponseEntity<CEBASResult> handleSecOCISException(SecOCISException e) {
/* 263 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   @ExceptionHandler({TimeException.class})
/*     */   public ResponseEntity<CEBASResult> handleTimeException(TimeException e) {
/* 276 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   @ExceptionHandler({BadFormatException.class})
/*     */   public ResponseEntity<CEBASResult> handleBadFormatException(BadFormatException e) {
/* 289 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
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
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   @ExceptionHandler({InvalidInputException.class})
/*     */   public ResponseEntity<CEBASResult> handleInvalidInputException(InvalidInputException e) {
/* 304 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   @ExceptionHandler({MethodArgumentNotValidException.class})
/*     */   public ResponseEntity<CEBASResult> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
/* 317 */     StringBuilder errorMessages = new StringBuilder();
/* 318 */     List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
/* 319 */     fieldErrors.forEach(fieldError -> errorMessages.append(fieldError.getDefaultMessage()).append(" "));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 324 */     return new ResponseEntity(new CEBASResult(errorMessages.toString()), HttpStatus.BAD_REQUEST);
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
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   @ExceptionHandler({InvalidInputForExportPublicKeyFileException.class})
/*     */   public void handleInvalidInputExportPublicKeyFileException(InvalidInputForExportPublicKeyFileException e, HttpServletResponse response) throws IOException {
/* 341 */     response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
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
/*     */   @ResponseStatus(HttpStatus.BAD_REQUEST)
/*     */   @ExceptionHandler({CertificateSigningReqInvalidTypeException.class})
/*     */   public ResponseEntity<CEBASResult> handleInvalidCertificateSignRequesteException(CertificateSigningReqInvalidTypeException e) {
/* 355 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ResponseStatus(HttpStatus.CONFLICT)
/*     */   @ExceptionHandler({CertificateRollbackException.class})
/*     */   public ResponseEntity<CEBASResult> handleRollbackException(CertificateRollbackException e) {
/* 368 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.CONFLICT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
/*     */   @ExceptionHandler({PkcsException.class})
/*     */   public ResponseEntity<CEBASResult> handlePkcsException(PkcsException e) {
/* 381 */     return new ResponseEntity(new CEBASResult(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
/*     */   @ExceptionHandler({CertificatesUpdateException.class})
/*     */   public ResponseEntity<String> handleFullUpdateException(CertificatesUpdateException e) {
/* 394 */     this.publisher.publishEvent(new RuntimeExceptionEvent(e));
/* 395 */     return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
/*     */   @ExceptionHandler({CertificatesUpdateNoSessionExeption.class})
/*     */   public ResponseEntity<CEBASPKIResult> handleUpdateExceptionNoSession(CertificatesUpdateNoSessionExeption e) {
/* 408 */     this.publisher.publishEvent(new RuntimeExceptionEvent(e));
/* 409 */     return new ResponseEntity(new CEBASPKIResult(e.getMessage(), e.getPkiErrorMessage(), e.getPkiStatusCode()), HttpStatus.INTERNAL_SERVER_ERROR);
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
/*     */   @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
/*     */   @ExceptionHandler({StoreModificationNotAllowed.class})
/*     */   public ResponseEntity<String> handleFullUpdateException(StoreModificationNotAllowed e) {
/* 423 */     return new ResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
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
/*     */   @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
/*     */   @ExceptionHandler({UpdateNotRunningException.class})
/*     */   public ResponseEntity<HashMap<String, String>> handleBaseException(UpdateNotRunningException e) {
/* 437 */     HashMap<String, String> response = new HashMap<>();
/* 438 */     response.put("message", e.getMessage());
/* 439 */     return new ResponseEntity(response, HttpStatus.NOT_ACCEPTABLE);
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\exceptions\CertificateExceptionsHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */