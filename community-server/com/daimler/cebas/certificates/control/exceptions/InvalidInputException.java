/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class InvalidInputException
extends CEBASException {
    private static final long serialVersionUID = -7346119774762425763L;

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, String messageId) {
        super(message, messageId);
    }
}
