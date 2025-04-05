/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class EmptyPermissionsException
extends CEBASException {
    private static final long serialVersionUID = 5754067865813931069L;

    public EmptyPermissionsException() {
        super("EmptyPermissionsException");
    }

    public String toString() {
        return EmptyPermissionsException.class.getSimpleName() + ": No permissions retrieved from PKI.";
    }
}
