/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class CheckOwnershipException
extends CEBASException {
    private static final long serialVersionUID = 3982782936021119375L;

    public CheckOwnershipException(String message) {
        super(message);
    }

    public CheckOwnershipException(String message, String messageId) {
        super(message, messageId);
    }
}
