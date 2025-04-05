/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class TimeException
extends CEBASException {
    private static final long serialVersionUID = -7629102030855671328L;

    public TimeException(String message) {
        super(message);
    }

    public TimeException(String message, String messageId) {
        super(message, messageId);
    }
}
