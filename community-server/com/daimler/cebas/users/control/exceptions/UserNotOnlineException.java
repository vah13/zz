/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.users.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class UserNotOnlineException
extends CEBASException {
    private static final long serialVersionUID = 8579017317585550254L;

    public UserNotOnlineException(String message, String messageId) {
        super(message, messageId);
    }
}
