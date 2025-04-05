/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.client.HttpStatusCodeException
 */
package com.daimler.cebas.certificates.control.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

public class PkiPayloadParsingException
extends HttpStatusCodeException {
    public PkiPayloadParsingException(HttpStatus statusCode, ResponseEntity<String> pkiResponse) {
        super(statusCode, "Http status: " + statusCode + ". Invalid error payload received: " + (String)pkiResponse.getBody());
    }
}
