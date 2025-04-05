/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.entity.CertificateType
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.entity.CertificateType;

public enum RootOrBackend {
    ROOT(CertificateType.ROOT_CA_CERTIFICATE),
    BACKEND(CertificateType.BACKEND_CA_CERTIFICATE);

    private CertificateType type;

    private RootOrBackend(CertificateType type) {
        this.type = type;
    }

    public static CertificateType[] toType(RootOrBackend o) {
        if (o == null) return new CertificateType[]{RootOrBackend.ROOT.type, RootOrBackend.BACKEND.type};
        return new CertificateType[]{o.type};
    }

    public static RootOrBackend fromType(CertificateType o) {
        if (o != RootOrBackend.ROOT.type) return BACKEND;
        return ROOT;
    }
}
