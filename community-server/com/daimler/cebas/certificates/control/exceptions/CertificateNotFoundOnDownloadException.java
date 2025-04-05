/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;

public class CertificateNotFoundOnDownloadException
extends CertificateNotFoundException {
    private static final long serialVersionUID = 2106145295141804887L;
    private String pkiErrorMessage;
    private String pkiStatusCode;

    public CertificateNotFoundOnDownloadException(String message, String messageId) {
        super(message, messageId);
    }

    public CertificateNotFoundOnDownloadException(String message, String messageId, String pkiErrorMessage, String pkiStatusCode) {
        super(message, messageId);
        this.pkiErrorMessage = pkiErrorMessage;
        this.pkiStatusCode = pkiStatusCode;
    }

    public CertificateNotFoundOnDownloadException(String message, String pkiErrorMessage, String pkiStatusCode) {
        super(message);
        this.pkiErrorMessage = pkiErrorMessage;
        this.pkiStatusCode = pkiStatusCode;
    }

    public String getPkiErrorMessage() {
        return this.pkiErrorMessage;
    }

    public String getPkiStatusCode() {
        return this.pkiStatusCode;
    }
}
