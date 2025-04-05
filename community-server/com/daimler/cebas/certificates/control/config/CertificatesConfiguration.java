/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.config.CertificatesDeleteConfiguration
 *  com.daimler.cebas.certificates.control.config.CertificatesImportConfiguration
 *  com.daimler.cebas.certificates.control.config.ChainOfTrustConfiguration
 *  com.daimler.cebas.certificates.control.vo.CertificateSummary
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.logs.control.Logger
 */
package com.daimler.cebas.certificates.control.config;

import com.daimler.cebas.certificates.control.config.CertificatesDeleteConfiguration;
import com.daimler.cebas.certificates.control.config.CertificatesImportConfiguration;
import com.daimler.cebas.certificates.control.config.ChainOfTrustConfiguration;
import com.daimler.cebas.certificates.control.vo.CertificateSummary;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.logs.control.Logger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CertificatesConfiguration
extends CertificatesDeleteConfiguration,
CertificatesImportConfiguration,
ChainOfTrustConfiguration {
    public static final String YYYY_MM_DD = "yyyy/MM/dd";

    public Date getCSRValidTo(Logger var1, MetadataManager var2);

    public Optional<Certificate> getMatchingRolledBackCertificate(Certificate var1);

    public List<CertificateType> availableCertificateTypesForCSRCreation();

    public List<CertificateType> availableCertificateTypesForRollbackEnabling();

    public boolean checkCSRExists();

    public boolean isSubjectValid(Certificate var1);

    public List<? extends CertificateSummary> convertToCertificateSummary(List<? extends Certificate> var1);

    public static String fileNameToHexString(byte[] fileNameProposal) {
        StringBuilder hexString = new StringBuilder();
        byte[] byArray = fileNameProposal;
        int n = byArray.length;
        int n2 = 0;
        while (n2 < n) {
            byte b = byArray[n2];
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
            ++n2;
        }
        return hexString.toString();
    }

    public boolean isUserRoleValid(Certificate var1);

    default public boolean isAutomaticMarkRollbackChildren() {
        return false;
    }

    public boolean isUnique(Certificate var1, Logger var2);
}
