/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.logs.control.Logger
 */
package com.daimler.cebas.certificates.control.chain;

import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.logs.control.Logger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class Uniqueness {
    private static final String CLASS_NAME = Uniqueness.class.getSimpleName();

    private Uniqueness() {
    }

    public static boolean hasSignatureDifferentButSamePublicKey(List<Certificate> store, Certificate certificate, Logger logger) {
        String METHOD_NAME = "hasSignatureDifferentButSamePublicKey";
        logger.entering(CLASS_NAME, "hasSignatureDifferentButSamePublicKey");
        logger.exiting(CLASS_NAME, "hasSignatureDifferentButSamePublicKey");
        return store.stream().anyMatch(cert -> !cert.getSignature().equals(certificate.getSignature()) && cert.getSubjectPublicKey().equals(certificate.getSubjectPublicKey()));
    }

    public static <T extends Certificate> List<T> findIdenticalWithDifferences(List<T> store, Certificate certificate, Logger logger) {
        String METHOD_NAME = "findIdenticalWithDifferences";
        logger.entering(CLASS_NAME, "findIdenticalWithDifferences");
        certificate.initializeTransients();
        logger.exiting(CLASS_NAME, "findIdenticalWithDifferences");
        return store.stream().filter(cert -> cert.identicalWithDifference(certificate)).collect(Collectors.toList());
    }

    public static List<Certificate> findIdenticalWithDifferences(List<Certificate> store, Certificate certificate, Set<String> handledCerts, Logger logger) {
        String METHOD_NAME = "findIdenticalWithDifferences";
        logger.entering(CLASS_NAME, "findIdenticalWithDifferences");
        certificate.initializeTransients();
        ArrayList<Certificate> resultList = new ArrayList<Certificate>();
        Iterator<Certificate> iterator = store.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                logger.exiting(CLASS_NAME, "findIdenticalWithDifferences");
                return resultList;
            }
            Certificate certInStore = iterator.next();
            boolean isIdentical = certInStore.identicalWithDifference(certificate);
            if (!isIdentical) continue;
            handledCerts.add(certInStore.getEntityId());
            resultList.add(certInStore);
        }
    }
}
