/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.users.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class UserLoginException
extends CEBASException {
    private static final long serialVersionUID = 1L;

    public UserLoginException(String message) {
        super(message);
    }

    public UserLoginException(String message, String messageId) {
        super(message, messageId);
    }
}
