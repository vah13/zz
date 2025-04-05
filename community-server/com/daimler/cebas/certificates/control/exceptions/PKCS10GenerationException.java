/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class PKCS10GenerationException
extends CEBASException {
    private static final long serialVersionUID = 7054049113637415756L;

    public PKCS10GenerationException(String message) {
        super(message);
    }

    public PKCS10GenerationException(String message, String messageId) {
        super(message, messageId);
    }
}
