/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.users.entity.User
 */
package com.daimler.cebas.users.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.users.entity.User;

public class UserValidationException
extends CEBASException {
    private static final long serialVersionUID = 7414644012725320245L;
    private final User user;

    public UserValidationException(User user, String message) {
        super(message);
        this.user = user;
    }

    public UserValidationException(User user, String message, String messageId) {
        super(message, messageId);
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
