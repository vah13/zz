/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class SecureVariantCodingException
extends CEBASException {
    private static final long serialVersionUID = 1L;

    public SecureVariantCodingException(String message) {
        super(message);
    }

    public SecureVariantCodingException(String message, String messageId) {
        super(message, messageId);
    }
}
