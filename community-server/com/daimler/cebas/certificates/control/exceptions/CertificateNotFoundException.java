/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class CertificateNotFoundException
extends CEBASException {
    private static final long serialVersionUID = -499247382468222554L;

    public CertificateNotFoundException(String message) {
        super(message);
    }

    public CertificateNotFoundException(String message, String messageId) {
        super(message, messageId);
    }
}
