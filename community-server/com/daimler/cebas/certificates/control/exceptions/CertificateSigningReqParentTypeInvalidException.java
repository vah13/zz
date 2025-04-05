/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class CertificateSigningReqParentTypeInvalidException
extends CEBASException {
    private static final long serialVersionUID = -3486455418954786352L;

    public CertificateSigningReqParentTypeInvalidException(String message) {
        super(message);
    }

    public CertificateSigningReqParentTypeInvalidException(String message, String messageId) {
        super(message, messageId);
    }
}
