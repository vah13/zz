/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.AbstractDeleteCertificates
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.common.entity.Versioned
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.AbstractDeleteCertificates;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.common.entity.Versioned;

public class DeleteCertificatesResult
extends AbstractDeleteCertificates {
    private boolean isSuccess;
    private String message;

    public DeleteCertificatesResult(String certificateId, boolean isCertificate, CertificateType certificateType, boolean isSuccess, String message, String serialNo, String subjectKeyIdentifier, String authKeyIdentifier) {
        super(certificateId, isCertificate, certificateType, serialNo, subjectKeyIdentifier, authKeyIdentifier);
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public DeleteCertificatesResult(String certificateId, boolean isCertificate, CertificateType certificateType, boolean isSuccess, String message, String authKeyIdentifier, String publicKey) {
        super(certificateId, isCertificate, certificateType, authKeyIdentifier, publicKey);
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public String getMessage() {
        return this.message;
    }

    public Versioned toVersion(int version) {
        return this;
    }
}
