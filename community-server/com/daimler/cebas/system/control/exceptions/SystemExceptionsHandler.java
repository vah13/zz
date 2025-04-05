/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.system.control.exceptions.UnauthorizedOperationException
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.core.annotation.Order
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.ControllerAdvice
 *  org.springframework.web.bind.annotation.ExceptionHandler
 *  org.springframework.web.bind.annotation.ResponseStatus
 *  org.springframework.web.bind.annotation.RestController
 */
package com.daimler.cebas.system.control.exceptions;

import com.daimler.cebas.system.control.exceptions.UnauthorizedOperationException;
import java.util.HashMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
@Lazy
@Order(value=-2147483648)
public class SystemExceptionsHandler {
    @ResponseStatus(value=HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value={UnauthorizedOperationException.class})
    public ResponseEntity<HashMap<String, String>> handleUnauthorizedOperationException(UnauthorizedOperationException e) {
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("message", e.getMessage());
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }
}
