/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo
 *  com.daimler.cebas.certificates.entity.Certificate
 *  org.springframework.util.StringUtils
 */
package com.daimler.cebas.certificates.entity;

import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo;
import com.daimler.cebas.certificates.entity.Certificate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

public enum CertificateType {
    NO_TYPE("certType.NoType", "No Type", 100),
    BACKEND_CA_CERTIFICATE("certType.BackendCaCertificate", "Backend CA Certificate", 2),
    BACKEND_CA_LINK_CERTIFICATE("certType.BackendCaLinkCertificate", "Backend CA Link Certificate", 3),
    ROOT_CA_CERTIFICATE("certType.RootCaCertificate", "Root CA Certificate", 1),
    ROOT_CA_LINK_CERTIFICATE("certType.RootCaLinkCertificate", "Root CA Link Certificate", 2),
    ECU_CERTIFICATE("certType.EcuCertificate", "ECU Certificate", 3),
    DIAGNOSTIC_AUTHENTICATION_CERTIFICATE("certType.DiagnosticAuthenticationCertificate", "Diagnostic Authentication Certificate", 3),
    ENHANCED_RIGHTS_CERTIFICATE("certType.EnhancedRightsCertificate", "Enhanced Rights Certificate", 4),
    TIME_CERTIFICATE("certType.TimeCertificate", "Time Certificate", 3),
    VARIANT_CODE_USER_CERTIFICATE("certType.VariantCodeUserCertificate", "Variant Coding User Certificate", 3),
    VARIANT_CODING_DEVICE_CERTIFICATE("certType.VariantCodingDeviceCertificate", "Variant Coding Device Certificate", 3),
    SEC_OC_IS("certType.SecOcIs", "SecOcIs Certificate", 4),
    VIRTUAL_FOLDER("certType.virtualFolder", "Virtual folder", 101);

    private String text;
    private String languageProperty;
    private int level;

    private CertificateType(String languageProperty, String text, int level) {
        this.text = text;
        this.languageProperty = languageProperty;
        this.level = level;
    }

    public static CertificateType valueFromString(String type) {
        CertificateType typeEnum = NO_TYPE;
        CertificateType[] certificateTypeArray = CertificateType.values();
        int n = certificateTypeArray.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) {
                if (typeEnum != NO_TYPE) return typeEnum;
                throw new CEBASCertificateException("Invalid certificate type");
            }
            CertificateType certificateType = certificateTypeArray[n2];
            if (type.equalsIgnoreCase(certificateType.getText()) || type.equalsIgnoreCase(certificateType.name())) {
                typeEnum = certificateType;
            }
            ++n2;
        }
    }

    public static List<CertificateType> typesWhichContains(String value) {
        ArrayList<CertificateType> types = new ArrayList<CertificateType>();
        CertificateType[] certificateTypeArray = CertificateType.values();
        int n = certificateTypeArray.length;
        int n2 = 0;
        while (n2 < n) {
            CertificateType certificateType = certificateTypeArray[n2];
            if (!StringUtils.isEmpty((Object)value) && certificateType.getText().toUpperCase().contains(value.toUpperCase())) {
                types.add(certificateType);
            }
            ++n2;
        }
        return types;
    }

    public String getText() {
        return this.text;
    }

    public String getLanguageProperty() {
        return this.languageProperty;
    }

    public static CertificateType getTypeForLogging(Certificate certificate) {
        CertificateType certificateType;
        if (certificate.isSecOCISCert()) {
            certificateType = SEC_OC_IS;
        } else {
            certificateType = certificate.getType();
            if (certificateType != null) return certificateType;
            certificateType = NO_TYPE;
        }
        return certificateType;
    }

    public static CertificateType getTypeForLogging(DeleteCertificatesInfo entry) {
        CertificateType certificateType;
        if (entry.getCertificateType().equals((Object)ENHANCED_RIGHTS_CERTIFICATE) && !StringUtils.isEmpty((Object)entry.getTargetSubjectKeyIdentifier())) {
            certificateType = SEC_OC_IS;
        } else {
            certificateType = entry.getCertificateType();
            if (certificateType != null) return certificateType;
            certificateType = NO_TYPE;
        }
        return certificateType;
    }

    public int getLevel() {
        return this.level;
    }
}
