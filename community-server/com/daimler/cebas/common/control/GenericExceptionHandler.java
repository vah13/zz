/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.StartupStatus
 *  com.daimler.cebas.common.control.vo.BadRequestResult
 *  com.daimler.cebas.common.control.vo.CEBASResult
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.exceptions.ApplicationNotStartedException
 *  javax.persistence.PersistenceException
 *  javax.persistence.PessimisticLockException
 *  org.hibernate.StaleObjectStateException
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.core.annotation.Order
 *  org.springframework.dao.PessimisticLockingFailureException
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.ResponseEntity
 *  org.springframework.http.converter.HttpMessageNotReadableException
 *  org.springframework.orm.ObjectOptimisticLockingFailureException
 *  org.springframework.web.HttpRequestMethodNotSupportedException
 *  org.springframework.web.bind.MissingServletRequestParameterException
 *  org.springframework.web.bind.annotation.ControllerAdvice
 *  org.springframework.web.bind.annotation.ExceptionHandler
 *  org.springframework.web.bind.annotation.ResponseStatus
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.context.request.WebRequest
 */
package com.daimler.cebas.common.control;

import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.StartupStatus;
import com.daimler.cebas.common.control.vo.BadRequestResult;
import com.daimler.cebas.common.control.vo.CEBASResult;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.exceptions.ApplicationNotStartedException;
import java.util.HashMap;
import java.util.logging.Level;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RestController
@Lazy
@Order(value=0x7FFFFFFF)
public class GenericExceptionHandler {
    public static final String ERROR_MESSAGE_PROPERTY_KEY = "message";
    public static final String DEFAULT_PAYLOAD_LANGUAGE = "en";
    @Autowired
    private Logger logger;
    @Autowired
    private MetadataManager i18n;

    @ExceptionHandler(value={IllegalArgumentException.class})
    public ResponseEntity<CEBASResult> handleIllegalArgumentException(IllegalArgumentException e, WebRequest r) {
        this.logger.log(Level.INFO, "000465X", this.i18n.getMessage("genericExceptionOccurred", new String[]{this.getUriDescription(r)}), this.getClass().getSimpleName());
        this.logger.logToFileOnly(this.getClass().getSimpleName(), "Illegal Argument.", (Throwable)e);
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value={MissingServletRequestParameterException.class})
    public void handleMissingMandatoryRequestParam(MissingServletRequestParameterException e, WebRequest r) throws MissingServletRequestParameterException {
        String message = this.i18n.getMessage("mandatoryParamMissing", new String[]{this.getUriDescription(r)});
        this.logger.log(Level.INFO, "000471X", message, this.getClass().getSimpleName());
        throw e;
    }

    @ExceptionHandler(value={HttpRequestMethodNotSupportedException.class})
    public void handleHttpMethodNoSupported(HttpRequestMethodNotSupportedException e, WebRequest r) throws HttpRequestMethodNotSupportedException {
        String httpMethod = e.getMethod();
        String message = this.i18n.getMessage("httpMethodNotSupported", new String[]{httpMethod, this.getUriDescription(r)});
        this.logger.log(Level.INFO, "000470X", message, this.getClass().getSimpleName());
        throw e;
    }

    private String getUriDescription(WebRequest request) {
        String description = "n/a";
        if (request == null) return description;
        description = request.getDescription(false);
        return description;
    }

    @ExceptionHandler(value={PessimisticLockException.class})
    public ResponseEntity<String> handlePessimisticLockException(PessimisticLockException e) {
        return new ResponseEntity((Object)e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value={PersistenceException.class})
    public ResponseEntity<HashMap<String, String>> handleBaseException(PersistenceException e) {
        HashMap<String, String> response = new HashMap<String, String>();
        response.put(ERROR_MESSAGE_PROPERTY_KEY, e.getMessage());
        this.logger.log(Level.WARNING, "000422X", e.getMessage(), this.getClass().getSimpleName());
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (!(e.getCause() instanceof StaleObjectStateException)) return new ResponseEntity(response, status);
        status = HttpStatus.PRECONDITION_FAILED;
        return new ResponseEntity(response, status);
    }

    @ExceptionHandler(value={PessimisticLockingFailureException.class})
    public ResponseEntity<String> handlePessimisticLockException(PessimisticLockingFailureException e) {
        String message = "Current processed table is locked at database level, other transaction got lock timeout";
        this.logger.log(Level.SEVERE, "000368X", message, this.getClass().getSimpleName());
        return new ResponseEntity((Object)message, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value={ObjectOptimisticLockingFailureException.class})
    public ResponseEntity<String> handleOptimistickException(ObjectOptimisticLockingFailureException e) {
        this.logger.logToFileOnly(this.getClass().getSimpleName(), "Optimistic locking exception.", (Throwable)e);
        this.logger.log(Level.SEVERE, "000368X", e.getMessage(), this.getClass().getSimpleName());
        return new ResponseEntity((Object)"Optimistic locking exception.", HttpStatus.NOT_ACCEPTABLE);
    }

    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value={ApplicationNotStartedException.class})
    public ResponseEntity<String> handleNotStartedException(ApplicationNotStartedException e, WebRequest r) {
        this.logger.log(Level.INFO, "000673X", e.getMessage(), this.getClass().getSimpleName());
        return new ResponseEntity((Object)"Application is starting up and is not accessible yet", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(value={Exception.class})
    public ResponseEntity<String> handleGeneric(Exception e, WebRequest r) {
        if (!StartupStatus.isApplicationStarted()) {
            this.logger.log(Level.INFO, "000677X", e.getMessage(), this.getClass().getSimpleName());
            return new ResponseEntity((Object)"Application is starting up and is not accessible yet", HttpStatus.SERVICE_UNAVAILABLE);
        }
        this.logger.log(Level.INFO, "000463X", this.i18n.getMessage("genericExceptionOccurred", new String[]{this.getUriDescription(r)}), this.getClass().getSimpleName());
        String englishMessage = this.i18n.getEnglishMessage("genericExceptionOccurred", new String[]{this.getUriDescription(r)});
        this.logger.logToFileOnly(this.getClass().getSimpleName(), englishMessage, (Throwable)e);
        return new ResponseEntity((Object)englishMessage, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={HttpMessageNotReadableException.class})
    public BadRequestResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e, WebRequest r) {
        String jsonInputReadErrorMessage = this.i18n.getMessage("jsonInputParseError", new String[]{this.getUriDescription(r)}, this.getLocaleFromRequestHeader(r));
        BadRequestResult badRequestResult = new BadRequestResult(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), jsonInputReadErrorMessage, this.getUriDescription(r));
        String englishMessage = this.i18n.getEnglishMessage("genericExceptionOccurred", new String[]{this.getUriDescription(r)});
        this.logger.log(Level.INFO, "000626X", englishMessage, this.getClass().getSimpleName());
        this.logger.logToFileOnly(this.getClass().getSimpleName(), englishMessage, (Throwable)e);
        return badRequestResult;
    }

    private String getLocaleFromRequestHeader(WebRequest request) {
        String locale = request.getHeader("Locale");
        return locale != null ? locale : DEFAULT_PAYLOAD_LANGUAGE;
    }
}
