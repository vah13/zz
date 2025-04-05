/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class CertificateSigningReqInvalidTypeException
extends CEBASException {
    private static final long serialVersionUID = -5573036408174436254L;

    public CertificateSigningReqInvalidTypeException(String message) {
        super(message);
    }

    public CertificateSigningReqInvalidTypeException(String message, String messageId) {
        super(message, messageId);
    }
}
