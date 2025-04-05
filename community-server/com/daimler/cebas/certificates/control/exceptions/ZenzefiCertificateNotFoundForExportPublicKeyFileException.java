/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class ZenzefiCertificateNotFoundForExportPublicKeyFileException
extends CEBASException {
    private static final long serialVersionUID = 6204868899197643314L;

    public ZenzefiCertificateNotFoundForExportPublicKeyFileException(String message) {
        super(message);
    }

    public ZenzefiCertificateNotFoundForExportPublicKeyFileException(String message, String messageId) {
        super(message, messageId);
    }
}
