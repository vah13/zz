/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class BadFormatException
extends CEBASException {
    private static final long serialVersionUID = 8224623747883105438L;

    public BadFormatException(String message) {
        super(message);
    }

    public BadFormatException(String message, String messageId) {
        super(message, messageId);
    }
}
