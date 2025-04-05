/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.logs.control.exception.MissingRangeException
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.core.annotation.Order
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.ControllerAdvice
 *  org.springframework.web.bind.annotation.ExceptionHandler
 *  org.springframework.web.bind.annotation.RestController
 */
package com.daimler.cebas.logs.control.exception;

import com.daimler.cebas.logs.control.exception.MissingRangeException;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
@Lazy
@Order(value=-2147483648)
public class LogExceptionHandler {
    @ExceptionHandler(value={MissingRangeException.class})
    public ResponseEntity<String> handleMissingRangeException(MissingRangeException exception) {
        return new ResponseEntity((Object)"", HttpStatus.BAD_REQUEST);
    }
}
