/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.CertificatesUpdateException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.certificates.control.exceptions.CertificatesUpdateException;

public class CertificatesUpdateNoSessionExeption
extends CertificatesUpdateException {
    private static final long serialVersionUID = -1874631156751941549L;
    private String pkiErrorMessage;
    private String pkiStatusCode;

    public CertificatesUpdateNoSessionExeption(String message) {
        super(message);
    }

    public CertificatesUpdateNoSessionExeption(String message, String pkiErrorMessage, String pkiStatusCode) {
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
