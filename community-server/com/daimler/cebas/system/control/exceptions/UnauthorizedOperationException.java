/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.system.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class UnauthorizedOperationException
extends CEBASException {
    private static final long serialVersionUID = -4652974986645179154L;

    public UnauthorizedOperationException(String message) {
        super(message);
    }

    public UnauthorizedOperationException(String message, String messageId) {
        super(message, messageId);
    }
}
