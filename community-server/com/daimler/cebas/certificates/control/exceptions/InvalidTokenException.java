/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class InvalidTokenException
extends CEBASException {
    private static final long serialVersionUID = 5038711495676222131L;

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, String messageId) {
        super(message, messageId);
    }
}
