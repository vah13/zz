/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class CertificateSigningReqEnhRightsException
extends CEBASException {
    private static final long serialVersionUID = 1520231950064354263L;

    public CertificateSigningReqEnhRightsException(String message) {
        super(message);
    }

    public CertificateSigningReqEnhRightsException(String message, String messageId) {
        super(message, messageId);
    }
}
