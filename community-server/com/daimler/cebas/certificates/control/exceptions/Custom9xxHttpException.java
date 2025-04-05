/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.http.HttpStatus
 *  org.springframework.web.client.HttpStatusCodeException
 */
package com.daimler.cebas.certificates.control.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class Custom9xxHttpException
extends HttpStatusCodeException {
    private int custom9xxStatusCode;

    public Custom9xxHttpException(int http9xxCode, String responseBody) {
        this(HttpStatus.CONFLICT, String.format("Http status %s with response body %s", http9xxCode, responseBody));
        this.custom9xxStatusCode = http9xxCode;
    }

    protected Custom9xxHttpException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }

    public int getRawStatusCode() {
        return this.custom9xxStatusCode;
    }
}
