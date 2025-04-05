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

public class DeleteCertificatesInfo
extends AbstractDeleteCertificates {
    private String targetSubjectKeyIdentifier;
    private String zkNo;
    private String ecuPackageTs;

    public DeleteCertificatesInfo(String certificateId, boolean isCertificate, CertificateType certificateType, String serialNo, String subjectKeyIdentifier, String authKeyIdentifier) {
        super(certificateId, isCertificate, certificateType, serialNo, subjectKeyIdentifier, authKeyIdentifier);
    }

    public DeleteCertificatesInfo(String certificateId, boolean isCertificate, CertificateType certificateType, String serialNo, String subjectKeyIdentifier, String authKeyIdentifier, String targetSubjectKeyIdentifier) {
        super(certificateId, isCertificate, certificateType, serialNo, subjectKeyIdentifier, authKeyIdentifier);
        this.targetSubjectKeyIdentifier = targetSubjectKeyIdentifier;
    }

    public DeleteCertificatesInfo(String certificateId, boolean isCertificate, CertificateType certificateType, String serialNo, String subjectKeyIdentifier, String authKeyIdentifier, String targetSubjectKeyIdentifier, String zkNo, String ecuPackageTs) {
        super(certificateId, isCertificate, certificateType, serialNo, subjectKeyIdentifier, authKeyIdentifier);
        this.targetSubjectKeyIdentifier = targetSubjectKeyIdentifier;
        this.zkNo = zkNo;
        this.ecuPackageTs = ecuPackageTs;
    }

    public DeleteCertificatesInfo(String certificateId, boolean isCertificate, CertificateType certificateType, String authKeyIdentifier, String publicKey) {
        super(certificateId, isCertificate, certificateType, authKeyIdentifier, publicKey);
    }

    public String getTargetSubjectKeyIdentifier() {
        return this.targetSubjectKeyIdentifier;
    }

    public String getZkNo() {
        return this.zkNo;
    }

    public String getEcuPackageTs() {
        return this.ecuPackageTs;
    }

    public Versioned toVersion(int version) {
        return this;
    }
}
