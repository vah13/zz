/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.users.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class ApplicationNotStartedException
extends CEBASException {
    private static final long serialVersionUID = -7875150027350497428L;

    public ApplicationNotStartedException(String message) {
        super(message);
    }
}
