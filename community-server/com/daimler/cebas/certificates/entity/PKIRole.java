/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.entity.CertificateType
 */
package com.daimler.cebas.certificates.entity;

import com.daimler.cebas.certificates.entity.CertificateType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class PKIRole {
    protected static final Map<Integer, CertificateType> ROLES = PKIRole.createMap();

    private PKIRole() {
    }

    private static Map<Integer, CertificateType> createMap() {
        HashMap<Integer, CertificateType> myMap = new HashMap<Integer, CertificateType>();
        myMap.put(1, CertificateType.ROOT_CA_CERTIFICATE);
        myMap.put(2, CertificateType.BACKEND_CA_CERTIFICATE);
        myMap.put(3, CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE);
        myMap.put(4, CertificateType.ECU_CERTIFICATE);
        myMap.put(5, CertificateType.ENHANCED_RIGHTS_CERTIFICATE);
        myMap.put(7, CertificateType.VARIANT_CODING_DEVICE_CERTIFICATE);
        myMap.put(8, CertificateType.TIME_CERTIFICATE);
        myMap.put(9, CertificateType.VARIANT_CODE_USER_CERTIFICATE);
        myMap.put(10, CertificateType.ROOT_CA_LINK_CERTIFICATE);
        myMap.put(11, CertificateType.BACKEND_CA_LINK_CERTIFICATE);
        return myMap;
    }

    public static Map<Integer, CertificateType> getRoles() {
        return ROLES;
    }

    public static Integer getPKIRoleFromCertificateType(CertificateType certificateType) {
        if (certificateType == CertificateType.TIME_CERTIFICATE) {
            return 3;
        }
        Optional<Map.Entry> value = ROLES.entrySet().stream().filter(entry -> certificateType.equals(entry.getValue())).findFirst();
        return value.map(Map.Entry::getKey).orElse(null);
    }
}
