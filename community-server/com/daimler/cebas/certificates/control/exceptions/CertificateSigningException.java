/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class CertificateSigningException
extends CEBASException {
    private static final long serialVersionUID = -5455723753606172337L;

    public CertificateSigningException(String message) {
        super(message);
    }

    public CertificateSigningException(String message, String messageId) {
        super(message, messageId);
    }
}
