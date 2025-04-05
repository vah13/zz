/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.vo.CEBASResult
 *  com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException
 *  com.daimler.cebas.configuration.control.exceptions.ZenZefiNotAllowedConfigurationException
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.core.annotation.Order
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.ControllerAdvice
 *  org.springframework.web.bind.annotation.ExceptionHandler
 *  org.springframework.web.bind.annotation.ResponseStatus
 *  org.springframework.web.bind.annotation.RestController
 */
package com.daimler.cebas.configuration.control.exceptions;

import com.daimler.cebas.common.control.vo.CEBASResult;
import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
import com.daimler.cebas.configuration.control.exceptions.ZenZefiNotAllowedConfigurationException;
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
public class ConfigurationExceptionHandler {
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={ZenZefiConfigurationException.class})
    public ResponseEntity<CEBASResult> handleConfigurationException(ZenZefiConfigurationException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value=HttpStatus.FORBIDDEN)
    @ExceptionHandler(value={ZenZefiNotAllowedConfigurationException.class})
    public ResponseEntity<CEBASResult> handleNotAllowedConfigurationException(ZenZefiNotAllowedConfigurationException e) {
        return new ResponseEntity((Object)new CEBASResult(e.getMessage()), HttpStatus.FORBIDDEN);
    }
}
