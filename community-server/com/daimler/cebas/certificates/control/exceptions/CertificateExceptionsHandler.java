/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.AuthKeyIdentSerialNumberPairException
 *  com.daimler.cebas.certificates.control.exceptions.BadFormatException
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateMoreResultsFoundException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundOnDownloadException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateRetrievalException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateRollbackException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateSigningException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqEnhRightsException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqInvalidTypeException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqParentTypeInvalidException
 *  com.daimler.cebas.certificates.control.exceptions.CertificatesUpdateException
 *  com.daimler.cebas.certificates.control.exceptions.CertificatesUpdateNoSessionExeption
 *  com.daimler.cebas.certificates.control.exceptions.CheckOwnershipException
 *  com.daimler.cebas.certificates.control.exceptions.EncryptedDiagnosticPackageException
 *  com.daimler.cebas.certificates.control.exceptions.InvalidInputException
 *  com.daimler.cebas.certificates.control.exceptions.InvalidInputForExportPublicKeyFileException
 *  com.daimler.cebas.certificates.control.exceptions.PkcsException
 *  com.daimler.cebas.certificates.control.exceptions.RuntimeExceptionEvent
 *  com.daimler.cebas.certificates.control.exceptions.SecOCISException
 *  com.daimler.cebas.certificates.control.exceptions.SecureVariantCodingException
 *  com.daimler.cebas.certificates.control.exceptions.SignatureCheckException
 *  com.daimler.cebas.certificates.control.exceptions.StoreModificationNotAllowed
 *  com.daimler.cebas.certificates.control.exceptions.SystemIntegrityEmptyReportException
 *  com.daimler.cebas.certificates.control.exceptions.TimeException
 *  com.daimler.cebas.certificates.control.exceptions.UpdateNotRunningException
 *  com.daimler.cebas.certificates.control.exceptions.ZenzefiCertificateNotFoundForExportPublicKeyFileException
 *  com.daimler.cebas.certificates.control.vo.CEBASPKIResult
 *  com.daimler.cebas.common.control.vo.CEBASResult
 *  javax.servlet.http.HttpServletResponse
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.context.ApplicationEvent
 *  org.springframework.context.ApplicationEventPublisher
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.core.annotation.Order
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.MethodArgumentNotValidException
 *  org.springframework.web.bind.annotation.ControllerAdvice
 *  org.springframework.web.bind.annotation.ExceptionHandler
 *  org.springframework.web.bind.annotation.ResponseStatus
 *  org.springframework.web.bind.annotation.RestController
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.certificates.control.exceptions.AuthKeyIdentSerialNumberPairException;
import com.daimler.cebas.certificates.control.exceptions.BadFormatException;
import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.control.exceptions.CertificateMoreResultsFoundException;
import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundOnDownloadException;
import com.daimler.cebas.certificates.control.exceptions.CertificateRetrievalException;
import com.daimler.cebas.certificates.control.exceptions.CertificateRollbackException;
import com.daimler.cebas.certificates.control.exceptions.CertificateSigningException;
import com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqEnhRightsException;
import com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqInvalidTypeException;
import com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqParentTypeInvalidException;
import com.daimler.cebas.certificates.control.exceptions.CertificatesUpdateException;
import com.daimler.cebas.certificates.control.exceptions.CertificatesUpdateNoSessionExeption;
import com.daimler.cebas.certificates.control.exceptions.CheckOwnershipException;
import com.daimler.cebas.certificates.control.exceptions.EncryptedDiagnosticPackageException;
import com.daimler.cebas.certificates.control.exceptions.InvalidInputException;
import com.daimler.cebas.certificates.control.exceptions.InvalidInputForExportPublicKeyFileException;
import com.daimler.cebas.certificates.control.exceptions.PkcsException;
import com.daimler.cebas.certificates.control.exceptions.RuntimeExceptionEvent;
import com.daimler.cebas.certificates.control.exceptions.SecOCISException;
import com.daimler.cebas.certificates.control.exceptions.SecureVariantCodingException;
import com.daimler.cebas.certificates.control.exceptions.SignatureCheckException;
import com.daimler.cebas.certificates.control.exceptions.StoreModificationNotAllowed;
import com.daimler.cebas.certificates.control.exceptions.SystemIntegrityEmptyReportException;
import com.daimler.cebas.certificates.control.exceptions.TimeException;
import com.daimler.cebas.certificates.control.exceptions.UpdateNotRunningException;
import com.daimler.cebas.certificates.control.exceptions.ZenzefiCertificateNotFoundForExportPublicKeyFileException;
import com.daimler.cebas.certificates.control.vo.CEBASPKIResult;
import com.daimler.cebas.common.control.vo.CEBASResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
@Lazy
@Order(value=-2147483648)
public class CertificateExceptionsHandler {
    @Autowired
    private ApplicationEventPublisher publisher;

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={CEBASCertificateException.class})
    public ResponseEntity<HashMap<String, String>> handleBaseException(CEBASCertificateException e) {
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("message", e.getMessage());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ExceptionHandler(value={ZenzefiCertificateNotFoundForExportPublicKeyFileException.class})
    public void handleZenzefiCertificateNotFoundForExportPublicKeyFileException(ZenzefiCertificateNotFoundForExportPublicKeyFileException e, HttpServletResponse response) throws IOException {
        response.resetBuffer();
        response.setStatus(404);
        response.setHeader("Content-Type", "application/json");
        response.getOutputStream().print("{\"errorMessage\":\"" + e.getMessage() + "\"}");
        response.flushBuffer();
    }

    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ExceptionHandler(value={CertificateNotFoundException.class})
    public ResponseEntity<CEBASResult> handleCertificateNotFoundException(CertificateNotFoundException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ExceptionHandler(value={CertificateNotFoundOnDownloadException.class})
    public ResponseEntity<CEBASResult> handleCertificateNotFoundOnDownloadException(CertificateNotFoundOnDownloadException e) {
        return new ResponseEntity((Object)new CEBASPKIResult(e.getMessage(), e.getPkiErrorMessage(), e.getPkiStatusCode()), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(value=HttpStatus.CONFLICT)
    @ExceptionHandler(value={CertificateMoreResultsFoundException.class})
    public ResponseEntity<CEBASResult> handleCertificateMoreResultsFoundException(CertificateMoreResultsFoundException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(value={CertificateSigningReqParentTypeInvalidException.class})
    public ResponseEntity<CEBASResult> handleCertificateSigningReqParentTypeInvalidException(CertificateSigningReqParentTypeInvalidException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ResponseStatus(value=HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(value={CertificateSigningReqEnhRightsException.class})
    public ResponseEntity<CEBASResult> handleCertificateSigningReqEnhRightsException(CertificateSigningReqEnhRightsException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.PRECONDITION_FAILED);
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={CertificateSigningException.class})
    public ResponseEntity<CEBASResult> handleCertificateSigningException(CertificateSigningException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value={CertificateRetrievalException.class})
    public ResponseEntity<CEBASResult> handleCertificateRetrievalException(CertificateRetrievalException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={SystemIntegrityEmptyReportException.class})
    public ResponseEntity<String> handleEmptySystemIntegrityReportException(SystemIntegrityEmptyReportException e) {
        return new ResponseEntity((Object)e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={AuthKeyIdentSerialNumberPairException.class})
    public ResponseEntity<CEBASResult> handleAuthKeyIdentSerialNumberPairException(AuthKeyIdentSerialNumberPairException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={SignatureCheckException.class})
    public ResponseEntity<CEBASResult> handleSignatureCheckException(SignatureCheckException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={SecureVariantCodingException.class})
    public ResponseEntity<CEBASResult> handleSecureVariantException(SecureVariantCodingException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={EncryptedDiagnosticPackageException.class})
    public ResponseEntity<CEBASResult> handleEncryptedDiagnosticPackageException(EncryptedDiagnosticPackageException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={CheckOwnershipException.class})
    public ResponseEntity<CEBASResult> handleCheckOnwnershipException(CheckOwnershipException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={SecOCISException.class})
    public ResponseEntity<CEBASResult> handleSecOCISException(SecOCISException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={TimeException.class})
    public ResponseEntity<CEBASResult> handleTimeException(TimeException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={BadFormatException.class})
    public ResponseEntity<CEBASResult> handleBadFormatException(BadFormatException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={InvalidInputException.class})
    public ResponseEntity<CEBASResult> handleInvalidInputException(InvalidInputException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={MethodArgumentNotValidException.class})
    public ResponseEntity<CEBASResult> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder errorMessages = new StringBuilder();
        List fieldErrors = e.getBindingResult().getFieldErrors();
        fieldErrors.forEach(fieldError -> errorMessages.append(fieldError.getDefaultMessage()).append(" "));
        return new ResponseEntity((Object)new CEBASResult(errorMessages.toString()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={InvalidInputForExportPublicKeyFileException.class})
    public void handleInvalidInputExportPublicKeyFileException(InvalidInputForExportPublicKeyFileException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={CertificateSigningReqInvalidTypeException.class})
    public ResponseEntity<CEBASResult> handleInvalidCertificateSignRequesteException(CertificateSigningReqInvalidTypeException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.CONFLICT)
    @ExceptionHandler(value={CertificateRollbackException.class})
    public ResponseEntity<CEBASResult> handleRollbackException(CertificateRollbackException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value={PkcsException.class})
    public ResponseEntity<CEBASResult> handlePkcsException(PkcsException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value={CertificatesUpdateException.class})
    public ResponseEntity<String> handleFullUpdateException(CertificatesUpdateException e) {
        this.publisher.publishEvent((ApplicationEvent)new RuntimeExceptionEvent((Object)e));
        return new ResponseEntity((Object)e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value={CertificatesUpdateNoSessionExeption.class})
    public ResponseEntity<CEBASPKIResult> handleUpdateExceptionNoSession(CertificatesUpdateNoSessionExeption e) {
        this.publisher.publishEvent((ApplicationEvent)new RuntimeExceptionEvent((Object)e));
        return new ResponseEntity((Object)new CEBASPKIResult(e.getMessage(), e.getPkiErrorMessage(), e.getPkiStatusCode()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(value={StoreModificationNotAllowed.class})
    public ResponseEntity<String> handleFullUpdateException(StoreModificationNotAllowed e) {
        return new ResponseEntity((Object)e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(value={UpdateNotRunningException.class})
    public ResponseEntity<HashMap<String, String>> handleBaseException(UpdateNotRunningException e) {
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("message", e.getMessage());
        return new ResponseEntity(response, HttpStatus.NOT_ACCEPTABLE);
    }
}
