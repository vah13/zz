/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.entity.Versioned
 */
package com.daimler.cebas.common.control.vo;

import com.daimler.cebas.common.entity.Versioned;

public class CEBASResult
implements Versioned {
    protected String errorMessage;

    public CEBASResult() {
    }

    public CEBASResult(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public Versioned toVersion(int version) {
        return this;
    }
}
