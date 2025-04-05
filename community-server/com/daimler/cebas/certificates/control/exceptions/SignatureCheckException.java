/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class SignatureCheckException
extends CEBASException {
    private static final long serialVersionUID = -8287762923184326497L;

    public SignatureCheckException(String message) {
        super(message);
    }

    public SignatureCheckException(String message, String messageId) {
        super(message, messageId);
    }
}
