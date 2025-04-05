/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class StoreModificationNotAllowed
extends CEBASException {
    private static final long serialVersionUID = -7066166013354798655L;

    public StoreModificationNotAllowed(String message) {
        super(message);
    }
}
