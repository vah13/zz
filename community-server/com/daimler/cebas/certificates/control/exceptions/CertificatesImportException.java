/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class CertificatesImportException
extends CEBASException {
    private static final long serialVersionUID = -4617187495026550945L;

    public CertificatesImportException(String message) {
        super(message);
    }

    public CertificatesImportException(String message, String messageId) {
        super(message, messageId);
    }
}
