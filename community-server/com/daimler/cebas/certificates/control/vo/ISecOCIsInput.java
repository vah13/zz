/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnore
 */
package com.daimler.cebas.certificates.control.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ISecOCIsInput {
    public String getEcuCertificate();

    public String getBackendCertSubjKeyId();

    public String getDiagCertSerialNumber();

    public String getTargetECU();

    public String getTargetVIN();

    @JsonIgnore
    public boolean isInvalid();
}
