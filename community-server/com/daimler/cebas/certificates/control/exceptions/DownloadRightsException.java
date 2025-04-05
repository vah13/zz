/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class DownloadRightsException
extends CEBASException {
    private static final long serialVersionUID = 7112543455849045254L;
    private final int pkiErrorCode;

    public DownloadRightsException() {
        super("");
        this.pkiErrorCode = 0;
    }

    public DownloadRightsException(String message, int pkiErrorCode) {
        super(message);
        this.pkiErrorCode = pkiErrorCode;
    }

    public int getPkiErrorCode() {
        return this.pkiErrorCode;
    }
}
