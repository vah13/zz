/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class CertificateSigningReqParentNotExistsException
extends CEBASException {
    private static final long serialVersionUID = 3792705200438640009L;

    public CertificateSigningReqParentNotExistsException(String message) {
        super(message);
    }

    public CertificateSigningReqParentNotExistsException(String message, String messageId) {
        super(message, messageId);
    }
}
