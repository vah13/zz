/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.vo.CEBASResult
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.common.control.vo.CEBASResult;

public class CEBASPKIResult
extends CEBASResult {
    private String pkiErrorMessage;
    private String pkiStatusCode;

    public CEBASPKIResult(String errorMessage, String pkiErrorMessage, String pkiStatusCode) {
        super(errorMessage);
        this.pkiErrorMessage = pkiErrorMessage;
        this.pkiStatusCode = pkiStatusCode;
    }

    public String getPkiErrorMessage() {
        return this.pkiErrorMessage;
    }

    public String getPkiStatusCode() {
        return this.pkiStatusCode;
    }
}
