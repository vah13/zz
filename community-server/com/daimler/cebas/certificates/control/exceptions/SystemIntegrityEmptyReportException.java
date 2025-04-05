/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class SystemIntegrityEmptyReportException
extends CEBASException {
    private static final long serialVersionUID = 371088829476216773L;

    public SystemIntegrityEmptyReportException(String message) {
        super(message);
    }

    public SystemIntegrityEmptyReportException(String message, String messageId) {
        super(message, messageId);
    }
}
