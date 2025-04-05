/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.DeleteCertificatesResult
 *  com.fasterxml.jackson.annotation.JsonIgnore
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.vo.DeleteCertificatesResult;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ExtendedDeleteCertificatesResult
extends DeleteCertificatesResult {
    private String zkNo;
    private String backendEcuPackageTs;
    @JsonIgnore
    private DeleteCertificatesResult deleteCertificatesResult;

    public ExtendedDeleteCertificatesResult(DeleteCertificatesResult result, String backendZkNo, String backendEcuPackageTs) {
        super(result.getCertificateId(), result.isCertificate(), result.getCertificateType(), result.isSuccess(), result.getMessage(), result.getSerialNo(), result.getSubjectKeyIdentifier(), result.getAuthKeyIdentifier());
        this.zkNo = backendZkNo;
        this.publicKey = result.getPublicKey();
        this.backendEcuPackageTs = backendEcuPackageTs;
        this.deleteCertificatesResult = result;
    }

    public String getZkNo() {
        return this.zkNo;
    }

    public String getBackendEcuPackageTs() {
        return this.backendEcuPackageTs;
    }

    public DeleteCertificatesResult getDeleteCertificatesResult() {
        return this.deleteCertificatesResult;
    }
}
