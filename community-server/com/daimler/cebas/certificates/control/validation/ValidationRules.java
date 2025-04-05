/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.entity.UserRole
 *  com.daimler.cebas.certificates.entity.parsing.CertificateParser
 *  org.springframework.util.CollectionUtils
 */
package com.daimler.cebas.certificates.control.validation;

import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.entity.UserRole;
import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

public final class ValidationRules {
    public static final String CN_PREFIX = "CN=";
    private static final Logger LOG = Logger.getLogger(ValidationRules.class.getSimpleName());

    private ValidationRules() {
    }

    private static boolean isNotExpired(Certificate certificate) {
        boolean notExpired = true;
        try {
            certificate.checkValidity();
        }
        catch (CertificateExpiredException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            notExpired = false;
        }
        catch (CertificateNotYetValidException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            notExpired = true;
        }
        return notExpired;
    }

    public static Predicate<Certificate> isNotExpired() {
        return ValidationRules::isNotExpired;
    }

    public static Predicate<Certificate> validSubject() {
        return certificate -> !StringUtils.isEmpty(certificate.getSubject().replace(CN_PREFIX, "")) && certificate.getSubject().length() <= 30;
    }

    public static Predicate<Certificate> validSerialNumber() {
        return certificate -> !StringUtils.isEmpty(certificate.getSerialNo());
    }

    public static Predicate<Certificate> validVersion() {
        return certificate -> !StringUtils.isEmpty(certificate.getVersion());
    }

    public static Predicate<Certificate> validIssuer() {
        return certificate -> {
            String issuer = certificate.getIssuer().replace(CN_PREFIX, "");
            return !StringUtils.isEmpty(certificate.getIssuer()) && issuer.getBytes(StandardCharsets.UTF_8).length <= 15;
        };
    }

    public static Predicate<Certificate> validProdQualifier() {
        return certificate -> certificate.getProdQualifierRaw() != null && certificate.getProdQualifierRaw().length == 1;
    }

    public static Predicate<Certificate> validAlgorithmIdentifier() {
        return certificate -> !StringUtils.isEmpty(certificate.getAlgorithmIdentifier());
    }

    public static Predicate<Certificate> validSubjectKeyIdentifier() {
        return certificate -> certificate.getType() == CertificateType.ENHANCED_RIGHTS_CERTIFICATE || certificate.getSubjectKeyIdentifierRaw() != null && certificate.getSubjectKeyIdentifierRaw().length == 20 && certificate.getSubjectKeyIdentifier().equals(CertificateParser.getSubjectKeyIdentifierFromPublicKey((String)certificate.getSubjectPublicKey()));
    }

    public static Predicate<Certificate> validTargetSubjectKeyIdentifier() {
        return certificate -> certificate.getType() != CertificateType.ENHANCED_RIGHTS_CERTIFICATE || certificate.getTargetSubjectKeyIdentifier() == null || certificate.getTargetSubjectKeyIdentifierRaw().length == 20;
    }

    public static Predicate<Certificate> validPKIRole() {
        return certificate -> certificate.getPKIRoleRaw() != null && certificate.getPKIRoleRaw().length == 1;
    }

    public static Predicate<Certificate> validSignature() {
        return certificate -> certificate.getSignatureRaw() != null && certificate.getSignatureRaw().length == 64;
    }

    public static Predicate<Certificate> validAuthorityKeyIdentifier() {
        return certificate -> certificate.getType() == CertificateType.ROOT_CA_CERTIFICATE || certificate.getAuthorityKeyIdentifierRaw() != null && certificate.getAuthorityKeyIdentifierRaw().length == 20;
    }

    public static Predicate<Certificate> validKeyUsage() {
        return certificate -> certificate.getKeyUsage() != null && ValidationRules.validKeyUsage(certificate.getKeyUsage());
    }

    public static Predicate<Certificate> validBasicConstraints() {
        return certificate -> !StringUtils.isEmpty(certificate.getBasicConstraints());
    }

    public static boolean validKeyUsage(boolean[] keyUsages) {
        int foundUsage = 0;
        for (boolean b : keyUsages) {
            if (!b) continue;
            ++foundUsage;
        }
        return foundUsage == 1;
    }

    public static Predicate<Certificate> validUniqueEcuId() {
        return certificate -> !StringUtils.isEmpty(certificate.getUniqueECUID()) && ValidationRules.validatedUniqueEcuId(certificate.getUniqueECUID(), certificate.isVsmEcu());
    }

    public static Predicate<Certificate> validSpecialECU() {
        return certificate -> certificate.getSpecialEcuRaw() == null || certificate.getSpecialEcuRaw() != null && certificate.getSpecialEcuRaw().length == 1 && (certificate.getSpecialEcuRaw()[0] == 0 || certificate.getSpecialEcuRaw()[0] == 1);
    }

    public static Predicate<Certificate> validUserRole() {
        return certificate -> certificate.getUserRolesRaw() != null && certificate.getUserRolesRaw().length == 1 && !StringUtils.isEmpty(certificate.getUserRole()) && !StringUtils.equals(certificate.getUserRole(), UserRole.NO_ROLE.getText());
    }

    public static Predicate<Certificate> validServices() {
        return certificate -> !StringUtils.isEmpty(certificate.getServices()) && ValidationRules.validedServices(certificate.getServices());
    }

    public static Predicate<Certificate> validBaseCertificateID() {
        return certificate -> !StringUtils.isEmpty(certificate.getBaseCertificateID());
    }

    public static Predicate<Certificate> validNonce() {
        return certificate -> certificate.getNonceRaw() != null && certificate.getNonceRaw().length == 32;
    }

    public static Predicate<Certificate> validTargetVIN() {
        return certificate -> certificate.getTargetVIN() == null || certificate.getTargetVIN().isEmpty() || !StringUtils.isEmpty(certificate.getTargetVIN()) && ValidationRules.validTargetVIN(certificate.getTargetVIN());
    }

    public static Predicate<Certificate> validTargetECU() {
        return certificate -> certificate.getTargetECU() == null || certificate.getTargetECU().isEmpty() || !StringUtils.isEmpty(certificate.getTargetECU()) && ValidationRules.validatedEcus(certificate.getTargetECU());
    }

    private static boolean validatedEcus(String targetECU) {
        String[] split;
        boolean valid = true;
        String[] stringArray = split = targetECU.split(", ");
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String ecu = stringArray[n2];
            if (ValidationRules.isECULengthNotCorrect(ecu)) {
                valid = false;
                return valid;
            }
            ++n2;
        }
        return valid;
    }

    public static boolean isECULengthNotCorrect(String ecu) {
        return null == ecu || ecu.trim().getBytes(StandardCharsets.UTF_8).length > 30;
    }

    private static boolean validatedUniqueEcuId(String uniqueEcu, boolean isVsm) {
        boolean valid = true;
        String[] split = uniqueEcu.split(", ");
        int maxLength = isVsm ? 30 : 15;
        String[] stringArray = split;
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String ecu = stringArray[n2];
            if (ecu.trim().getBytes(StandardCharsets.UTF_8).length > maxLength) {
                valid = false;
                return valid;
            }
            ++n2;
        }
        return valid;
    }

    private static boolean validTargetVIN(String vins) {
        String[] split;
        boolean valid = true;
        String[] stringArray = split = vins.split(", ");
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String vin = stringArray[n2];
            if (ValidationRules.isVINLengthNotCorrect(vin)) {
                valid = false;
                return valid;
            }
            ++n2;
        }
        return valid;
    }

    public static boolean isVINLengthNotCorrect(String vin) {
        return null == vin || new String(vin.getBytes(), StandardCharsets.UTF_8).length() != 17;
    }

    private static boolean validedServices(String services) {
        String[] split;
        boolean valid = true;
        String[] stringArray = split = services.split(", ");
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String service = stringArray[n2];
            if (service.trim().getBytes(StandardCharsets.UTF_8).length > 8) {
                valid = false;
                return valid;
            }
            ++n2;
        }
        return valid;
    }

    public static Predicate<Certificate> validPublicKey() {
        return certificate -> certificate.getType() == CertificateType.ENHANCED_RIGHTS_CERTIFICATE || certificate.getSubjectPublicKeyRaw() != null && certificate.getSubjectPublicKeyRaw().length == 32;
    }

    public static Map<String, List<String>> validateTargetEcuAndTargetVIN(List<String> targetECUs, List<String> targetVINs) {
        HashMap<String, List<String>> result = new HashMap<String, List<String>>();
        if (!CollectionUtils.isEmpty(targetECUs)) {
            List invalidTargetECUs = targetECUs.stream().filter(ecu -> !StringUtils.isAllBlank(ecu) && ValidationRules.isECULengthNotCorrect(ecu)).collect(Collectors.toList());
            result.put("targetECU", invalidTargetECUs);
        }
        if (CollectionUtils.isEmpty(targetVINs)) return result;
        List invalidTargetVINs = targetVINs.stream().filter(vin -> !StringUtils.isAllBlank(vin) && ValidationRules.isVINLengthNotCorrect(vin)).collect(Collectors.toList());
        result.put("targetVIN", invalidTargetVINs);
        return result;
    }
}
