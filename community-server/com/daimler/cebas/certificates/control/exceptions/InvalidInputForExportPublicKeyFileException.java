/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class InvalidInputForExportPublicKeyFileException
extends CEBASException {
    private static final long serialVersionUID = -4265352014823759783L;

    public InvalidInputForExportPublicKeyFileException(String message) {
        super(message);
    }

    public InvalidInputForExportPublicKeyFileException(String message, String messageId) {
        super(message, messageId);
    }
}
