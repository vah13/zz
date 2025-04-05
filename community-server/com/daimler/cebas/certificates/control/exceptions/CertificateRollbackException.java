/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class CertificateRollbackException
extends CEBASException {
    private static final long serialVersionUID = -8988131869451867820L;

    public CertificateRollbackException(String message) {
        super(message);
    }

    public CertificateRollbackException(String message, String messageId) {
        super(message, messageId);
    }
}
