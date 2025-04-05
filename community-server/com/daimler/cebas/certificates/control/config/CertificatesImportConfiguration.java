/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 */
package com.daimler.cebas.certificates.control.config;

import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import java.util.List;

public interface CertificatesImportConfiguration {
    public boolean isCertificateImportNotAllowed(Certificate var1);

    public String[] certificateImportNotAllowedInvalidFields(CertificateType var1, String var2, String var3);

    public boolean shouldReplaceDuringImport(CertificateType var1);

    public boolean isImportWithoutSignatureCheck();

    public void logCertificateImportNotAllowed(Certificate var1);

    public List<CertificateType> availableCertificateTypesForImport();
}
