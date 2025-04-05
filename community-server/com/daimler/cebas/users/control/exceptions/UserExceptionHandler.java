/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException
 *  com.daimler.cebas.users.control.exceptions.UserException
 *  com.daimler.cebas.users.control.exceptions.UserLoginException
 *  com.daimler.cebas.users.control.exceptions.UserValidationException
 *  com.daimler.cebas.users.entity.User
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.core.annotation.Order
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.ControllerAdvice
 *  org.springframework.web.bind.annotation.ExceptionHandler
 *  org.springframework.web.bind.annotation.ResponseStatus
 *  org.springframework.web.bind.annotation.RestController
 */
package com.daimler.cebas.users.control.exceptions;

import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
import com.daimler.cebas.users.control.exceptions.UserException;
import com.daimler.cebas.users.control.exceptions.UserLoginException;
import com.daimler.cebas.users.control.exceptions.UserValidationException;
import com.daimler.cebas.users.entity.User;
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
public class UserExceptionHandler {
    @ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(value={UserValidationException.class})
    public User handleException(UserValidationException exception) {
        User user = exception.getUser();
        if ("Invalid".equals(user.getUserPassword())) return user;
        user.setUserPassword("");
        return user;
    }

    @ExceptionHandler(value={UserException.class})
    public ResponseEntity<HashMap<String, String>> handleException(UserException exception) {
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("message", exception.getMessage());
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value={UserLoginException.class})
    public ResponseEntity<HashMap<String, String>> handleException(UserLoginException exception) {
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("message", exception.getMessage());
        return new ResponseEntity(response, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(value={ZenZefiConfigurationException.class})
    public ResponseEntity<HashMap<String, String>> handleException(ZenZefiConfigurationException exception) {
        HashMap<String, String> response = new HashMap<String, String>();
        response.put("message", exception.getMessage());
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
