/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class PkcsException
extends CEBASException {
    private static final long serialVersionUID = -2013773866727859229L;

    public PkcsException(String message, String messageId) {
        super(message, messageId);
    }
}
