/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.logs.control.Logger
 */
package com.daimler.cebas.certificates.control.validation;

import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.logs.control.Logger;
import org.apache.commons.lang3.StringUtils;

public class CertificateInStoreValidator {
    private static final String CLASS_NAME = CertificateInStoreValidator.class.getSimpleName();

    private CertificateInStoreValidator() {
    }

    public static boolean isInStore(Certificate certificate, String userId, Logger logger) {
        String METHOD_NAME = "isInStore";
        logger.entering(CLASS_NAME, "isInStore");
        logger.exiting(CLASS_NAME, "isInStore");
        return StringUtils.equals(certificate.getUser().getEntityId(), userId);
    }
}
