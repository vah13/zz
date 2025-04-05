/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class CertificatesUpdateException
extends CEBASException {
    private static final long serialVersionUID = 1702361823739585575L;

    public CertificatesUpdateException(String message) {
        super(message);
    }

    public CertificatesUpdateException(String message, String messageId) {
        super(message, messageId);
    }
}
