/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.configuration.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class ZenZefiConfigurationException
extends CEBASException {
    private static final long serialVersionUID = -3771784286142500127L;

    public ZenZefiConfigurationException(String message) {
        super(message);
    }

    public ZenZefiConfigurationException(String message, String messageId) {
        super(message, messageId);
    }
}
