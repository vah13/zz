/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class CertificateRetrievalException
extends CEBASException {
    private static final long serialVersionUID = -3284182196681513135L;

    public CertificateRetrievalException(String message) {
        super(message);
    }
}
