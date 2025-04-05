/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class CEBASCertificateException
extends CEBASException {
    private static final long serialVersionUID = 1L;

    public CEBASCertificateException(String message) {
        super(message);
    }

    public CEBASCertificateException(String message, String messageId) {
        super(message, messageId);
    }
}
