/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.RollbackCertificate$RollbackMode
 *  com.daimler.cebas.common.control.vo.CEBASResult
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.RollbackCertificate;
import com.daimler.cebas.common.control.vo.CEBASResult;

public class RollbackCertificate
extends CEBASResult {
    private String backendKeyIdentifier;
    private String serialNumber;
    private RollbackMode rollbackMode;

    public RollbackCertificate() {
    }

    public RollbackCertificate(String backendKeyIdentifier, String serialNumber, RollbackMode rollbackMode) {
        this.backendKeyIdentifier = backendKeyIdentifier;
        this.serialNumber = serialNumber;
        this.rollbackMode = rollbackMode;
    }

    public String getBackendKeyIdentifier() {
        return this.backendKeyIdentifier;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public RollbackMode getRollbackMode() {
        return this.rollbackMode;
    }
}
