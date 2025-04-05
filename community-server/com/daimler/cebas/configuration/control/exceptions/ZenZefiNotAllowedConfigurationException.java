/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.configuration.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class ZenZefiNotAllowedConfigurationException
extends CEBASException {
    private static final long serialVersionUID = -3771784286142500127L;

    public ZenZefiNotAllowedConfigurationException(String message) {
        super(message);
    }

    public ZenZefiNotAllowedConfigurationException(String message, String messageId) {
        super(message, messageId);
    }
}
