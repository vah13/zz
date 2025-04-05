/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class CertificateMoreResultsFoundException
extends CEBASException {
    private static final long serialVersionUID = -3322497479668575687L;

    public CertificateMoreResultsFoundException(String message) {
        super(message);
    }

    public CertificateMoreResultsFoundException(String message, String messageId) {
        super(message, messageId);
    }
}
