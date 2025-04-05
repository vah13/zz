/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.DurationParser
 *  com.daimler.cebas.certificates.control.validation.CertificatesValidator$1
 *  com.daimler.cebas.certificates.control.validation.ValidationError
 *  com.daimler.cebas.certificates.control.validation.ValidationRuleChecker
 *  com.daimler.cebas.certificates.control.validation.ValidationRules
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.entity.RawData
 *  com.daimler.cebas.certificates.entity.parsing.CertificateParser
 *  com.daimler.cebas.common.control.CEBASProperty
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.SecurityProviders
 *  com.daimler.cebas.common.control.annotations.FacadePattern
 *  com.daimler.cebas.configuration.entity.Configuration
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  com.daimler.cebas.users.entity.User
 *  com.daimler.cebas.users.entity.UserKeyPair
 *  org.bouncycastle.cert.X509AttributeCertificateHolder
 */
package com.daimler.cebas.certificates.control.validation;

import com.daimler.cebas.certificates.control.DurationParser;
import com.daimler.cebas.certificates.control.validation.CertificatesValidator;
import com.daimler.cebas.certificates.control.validation.ValidationError;
import com.daimler.cebas.certificates.control.validation.ValidationRuleChecker;
import com.daimler.cebas.certificates.control.validation.ValidationRules;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.entity.RawData;
import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
import com.daimler.cebas.common.control.CEBASProperty;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.SecurityProviders;
import com.daimler.cebas.common.control.annotations.FacadePattern;
import com.daimler.cebas.configuration.entity.Configuration;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import com.daimler.cebas.users.entity.User;
import com.daimler.cebas.users.entity.UserKeyPair;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cert.X509AttributeCertificateHolder;

@FacadePattern
public class CertificatesValidator {
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(CertificatesValidator.class.getName());
    private static final String TYPE = " type: ";
    private static final String SERIAL_NO = " Serial No: ";
    private static final String SKI = " SKI: ";
    private static final String CLASS_NAME = CertificatesValidator.class.getSimpleName();

    private CertificatesValidator() {
    }

    public static List<Optional<ValidationError>> basicValidation(Certificate certificate, boolean checkExpired, Logger logger, MetadataManager i18n) {
        String METHOD_NAME = "basicValidation";
        logger.entering(CLASS_NAME, "basicValidation");
        ArrayList<Optional<ValidationError>> validationErrosOptionals = new ArrayList<Optional<ValidationError>>();
        validationErrosOptionals.add(ValidationRuleChecker.check(cert -> cert.getType() != CertificateType.NO_TYPE, (Certificate)certificate, (String)i18n.getMessage("invalidCertificateType"), (String)"invalidCertificateType"));
        validationErrosOptionals.addAll(CertificatesValidator.checkCertificateDBFieldsLength(certificate, i18n));
        if (checkExpired) {
            validationErrosOptionals.add(ValidationRuleChecker.check((Predicate)ValidationRules.isNotExpired(), (Certificate)certificate, (String)i18n.getMessage("certificateExpiredInvalidDates", new String[]{certificate.getSubjectKeyIdentifier()}), (String)"certificateExpiredInvalidDates"));
        }
        logger.exiting(CLASS_NAME, "basicValidation");
        return validationErrosOptionals;
    }

    public static List<Optional<ValidationError>> extendedValidation(Certificate certificate, Logger logger, MetadataManager i18n) {
        String METHOD_NAME = "extendedValidation";
        logger.entering(CLASS_NAME, "extendedValidation");
        ArrayList<Optional<ValidationError>> validationErrorsOptionals = new ArrayList<Optional<ValidationError>>();
        Optional daimlerTypeValidationErrorOptional = ValidationRuleChecker.check(cert -> cert.getType() != CertificateType.NO_TYPE, (Certificate)certificate, (String)i18n.getMessage("invalidCertificateType"), (String)"invalidCertificateType");
        validationErrorsOptionals.add(daimlerTypeValidationErrorOptional);
        if (!daimlerTypeValidationErrorOptional.isPresent()) {
            validationErrorsOptionals.add(ValidationRuleChecker.check((Predicate)ValidationRules.validPublicKey(), (Certificate)certificate, (String)i18n.getMessage("invalidPublicKey"), (String)"invalidPublicKey"));
            validationErrorsOptionals.add(ValidationRuleChecker.check((Predicate)ValidationRules.validAuthorityKeyIdentifier(), (Certificate)certificate, (String)i18n.getMessage("invalidAuthKeyIdentifier"), (String)"invalidAuthKeyIdentifier"));
            validationErrorsOptionals.add(ValidationRuleChecker.check((Predicate)ValidationRules.validSubjectKeyIdentifier(), (Certificate)certificate, (String)i18n.getMessage("invalidSubjKeyIdentifier"), (String)"invalidSubjKeyIdentifier"));
            validationErrorsOptionals.add(ValidationRuleChecker.check((Predicate)ValidationRules.validAlgorithmIdentifier(), (Certificate)certificate, (String)i18n.getMessage("invalidAlgIdentifier"), (String)"invalidAlgIdentifier"));
            validationErrorsOptionals.add(ValidationRuleChecker.check((Predicate)ValidationRules.validSignature(), (Certificate)certificate, (String)i18n.getMessage("invalidSignature"), (String)"invalidSignature"));
            validationErrorsOptionals.add(ValidationRuleChecker.check((Predicate)ValidationRules.validProdQualifier(), (Certificate)certificate, (String)i18n.getMessage("invalidProdQualifier"), (String)"invalidProdQualifier"));
            validationErrorsOptionals.add(ValidationRuleChecker.check((Predicate)ValidationRules.validPKIRole(), (Certificate)certificate, (String)i18n.getMessage("invalidPKIRole"), (String)"invalidPKIRole"));
            validationErrorsOptionals.add(ValidationRuleChecker.check((Predicate)ValidationRules.validIssuer(), (Certificate)certificate, (String)i18n.getMessage("invalidIssuer"), (String)"invalidIssuer"));
            validationErrorsOptionals.add(ValidationRuleChecker.check((Predicate)ValidationRules.validVersion(), (Certificate)certificate, (String)i18n.getMessage("invalidVersion"), (String)"invalidVersion"));
            validationErrorsOptionals.add(ValidationRuleChecker.check((Predicate)ValidationRules.validSerialNumber(), (Certificate)certificate, (String)i18n.getMessage("invalidSerialNumber"), (String)"invalidSerialNumber"));
            validationErrorsOptionals.add(ValidationRuleChecker.check((Predicate)ValidationRules.isNotExpired(), (Certificate)certificate, (String)i18n.getMessage("certificateExpiredInvalidDates", new String[]{certificate.getSubjectKeyIdentifier()}), (String)"certificateExpiredInvalidDates"));
            validationErrorsOptionals.addAll(CertificatesValidator.getSpecificValidation(certificate, i18n));
        }
        logger.exiting(CLASS_NAME, "extendedValidation");
        return validationErrorsOptionals;
    }

    public static boolean isValidInChain(Certificate certificate, MetadataManager i18n, Logger logger) {
        if (!CertificatesValidator.certificateStructureValid(certificate)) return false;
        List<Optional<ValidationError>> specificValidation = CertificatesValidator.getSpecificValidation(certificate, i18n);
        Optional<Optional> errorFound = specificValidation.stream().filter(Optional::isPresent).findFirst();
        if (errorFound.isPresent()) {
            return false;
        }
        switch (1.$SwitchMap$com$daimler$cebas$certificates$entity$CertificateType[certificate.getType().ordinal()]) {
            case 1: {
                return true;
            }
            case 2: {
                return CertificatesValidator.enhancedRightsSigVerification(certificate, logger);
            }
        }
        return CertificatesValidator.regularCertificate(certificate, logger);
    }

    public static boolean isValidLinkCertInChain(Certificate issuer, Certificate linkCertificate, MetadataManager i18n, Logger logger) {
        if (!CertificatesValidator.certificateStructureValid(linkCertificate)) return false;
        List<Optional<ValidationError>> specificValidation = CertificatesValidator.getSpecificValidation(linkCertificate, i18n);
        Optional<Optional> errorFound = specificValidation.stream().filter(Optional::isPresent).findFirst();
        if (!errorFound.isPresent()) return CertificatesValidator.verifySignature(issuer.getCertificateData(), linkCertificate.getCertificateData(), logger);
        return false;
    }

    public static Optional<Configuration> getValidCertsConfiguration(User user) {
        return user.getConfigurations().stream().filter(config -> config.getConfigKey().equals(CEBASProperty.VALIDATE_CERTS.name())).findFirst();
    }

    public static boolean isBasicValidation(User user) {
        Optional<Configuration> validateCerts = CertificatesValidator.getValidCertsConfiguration(user);
        return validateCerts.isPresent() && Boolean.valueOf(validateCerts.get().getConfigValue()) == false;
    }

    public static boolean verifySignature(RawData parentCertificateData, RawData certificateData, Logger logger) {
        boolean verify = true;
        try {
            if (certificateData.isCertificate()) {
                certificateData.getCert().verify(parentCertificateData.getCert().getPublicKey(), SecurityProviders.SNED25519.name());
            } else {
                PublicKey publicKey = parentCertificateData.getCert().getPublicKey();
                X509AttributeCertificateHolder attributesCertificateHolder = certificateData.getAttributesCertificateHolder();
                verify = CertificatesValidator.verifySignatureAttributeHolder(publicKey, attributesCertificateHolder);
            }
        }
        catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException | CertificateException e) {
            String serialNo;
            String userRole;
            String authorityKeyIdentifier;
            LOG.log(Level.FINEST, e.getMessage(), e);
            if (certificateData.isCertificate()) {
                authorityKeyIdentifier = CertificateParser.getAuthorityKeyIdentifier((Object)certificateData.getCert());
                userRole = CertificateParser.getPKIRole((Object)certificateData.getCert()).getText();
                serialNo = CertificateParser.getSerialNumber((Object)certificateData.getCert());
            } else {
                authorityKeyIdentifier = CertificateParser.getAuthorityKeyIdentifier((Object)certificateData.getAttributesCertificateHolder());
                userRole = CertificateParser.getPKIRole((Object)certificateData.getCert()).getText();
                serialNo = CertificateParser.getSerialNumber((Object)certificateData.getAttributesCertificateHolder());
            }
            logger.logWithTranslation(Level.WARNING, "000015", "signatureVerificationFailed", new String[]{authorityKeyIdentifier, serialNo, userRole}, CLASS_NAME);
            verify = false;
        }
        return verify;
    }

    public static boolean checkPublicKeyMatchesTheOneGeneratedFromPrivateKey(Session session, Certificate certificate, Logger logger) {
        Optional userKeyPairOptional = session.getCorrelatedKeyPair(certificate);
        if (CertificateType.ECU_CERTIFICATE == certificate.getType() && !userKeyPairOptional.isPresent()) {
            return true;
        }
        if (userKeyPairOptional.isPresent()) {
            boolean matchPublicKeys = session.getCryptoEngine().matchPublicKeys(session.getContainerKey(), certificate, (UserKeyPair)userKeyPairOptional.get());
            if (matchPublicKeys) return matchPublicKeys;
            logger.log(Level.WARNING, "000381", "Public key generated from private key does not match the one in certificate with AKI:" + certificate.getAuthorityKeyIdentifier() + SKI + certificate.getSubjectKeyIdentifier() + SERIAL_NO + certificate.getSerialNo() + TYPE + certificate.getType(), CertificatesValidator.class.getSimpleName());
            return matchPublicKeys;
        }
        logger.log(Level.WARNING, "000382", "Private key was not found for certificate with AKI: " + certificate.getAuthorityKeyIdentifier() + SKI + certificate.getSubjectKeyIdentifier() + SERIAL_NO + certificate.getSerialNo() + TYPE + certificate.getType(), CertificatesValidator.class.getSimpleName());
        return false;
    }

    public static boolean isExpired(Certificate certificate, Logger logger, MetadataManager i18n) {
        String METHOD_NAME = "isExpired";
        logger.entering(CLASS_NAME, "isExpired");
        boolean expired = false;
        try {
            certificate.checkValidity();
        }
        catch (CertificateExpiredException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            expired = true;
        }
        catch (CertificateNotYetValidException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            logger.log(Level.WARNING, "000154X", i18n.getMessage("validationFailed", new String[]{"Validity " + e.getMessage() + " for certificate with serial number: " + certificate.getSerialNo() + " and subject key identifier: " + certificate.getSubjectKeyIdentifier()}), CLASS_NAME);
        }
        logger.exiting(CLASS_NAME, "isExpired");
        return expired;
    }

    public static boolean isUnknownType(Certificate certificate) {
        return certificate.getType() == CertificateType.NO_TYPE;
    }

    private static List<Optional<ValidationError>> checkCertificateDBFieldsLength(Certificate certificate, MetadataManager i18n) {
        ArrayList<Optional<ValidationError>> validationErrors = new ArrayList<Optional<ValidationError>>();
        validationErrors.add(ValidationRuleChecker.check(cert -> StringUtils.defaultString(cert.getAlgorithmIdentifier()).length() <= 255, (Certificate)certificate, (String)i18n.getMessage("invalidAlgIdentifier"), (String)"invalidAlgIdentifier"));
        validationErrors.add(ValidationRuleChecker.check(cert -> StringUtils.defaultString(cert.getAuthorityKeyIdentifier()).length() <= 120, (Certificate)certificate, (String)i18n.getMessage("invalidAuthKeyIdentifier"), (String)"invalidAuthKeyIdentifier"));
        validationErrors.add(ValidationRuleChecker.check(cert -> StringUtils.defaultString(cert.getBaseCertificateID()).length() <= 255, (Certificate)certificate, (String)i18n.getMessage("invalidBaseCertificateId"), (String)"invalidBaseCertificateId"));
        validationErrors.add(ValidationRuleChecker.check(cert -> StringUtils.defaultString(cert.getBasicConstraints()).length() <= 255, (Certificate)certificate, (String)i18n.getMessage("invalidBasicConstraints"), (String)"invalidBasicConstraints"));
        validationErrors.add(ValidationRuleChecker.check(cert -> StringUtils.defaultString(cert.getIssuer()).length() <= 50, (Certificate)certificate, (String)i18n.getMessage("invalidIssuer"), (String)"invalidIssuer"));
        validationErrors.add(ValidationRuleChecker.check(cert -> StringUtils.defaultString(cert.getIssuerSerialNumber()).length() <= 100, (Certificate)certificate, (String)i18n.getMessage("invalidIssuerSerialNumber"), (String)"invalidIssuerSerialNumber"));
        validationErrors.add(ValidationRuleChecker.check(cert -> StringUtils.defaultString(cert.getNonce()).length() <= 150, (Certificate)certificate, (String)i18n.getMessage("invalidNonce"), (String)"invalidNonce"));
        validationErrors.add(ValidationRuleChecker.check(cert -> StringUtils.defaultString(cert.getProdQualifier()).length() <= 50, (Certificate)certificate, (String)i18n.getMessage("invalidProdQualifier"), (String)"invalidProdQualifier"));
        validationErrors.add(ValidationRuleChecker.check(cert -> StringUtils.defaultString(cert.getSerialNo()).length() <= 100, (Certificate)certificate, (String)i18n.getMessage("invalidSerialNumber"), (String)"invalidSerialNumber"));
        validationErrors.add(ValidationRuleChecker.check(cert -> StringUtils.defaultString(cert.getSpecialECU()).length() <= 30, (Certificate)certificate, (String)i18n.getMessage("invalidSpecialEcu"), (String)"invalidSpecialEcu"));
        validationErrors.add(ValidationRuleChecker.check(cert -> StringUtils.defaultString(cert.getSubject()).length() <= 50, (Certificate)certificate, (String)i18n.getMessage("invalidSubject"), (String)"invalidSubject"));
        validationErrors.add(ValidationRuleChecker.check(cert -> StringUtils.defaultString(cert.getSubjectKeyIdentifier()).length() <= 120, (Certificate)certificate, (String)i18n.getMessage("invalidSubjKeyIdentifier"), (String)"invalidSubjKeyIdentifier"));
        validationErrors.add(ValidationRuleChecker.check(cert -> StringUtils.defaultString(cert.getSubjectPublicKey()).length() <= 150, (Certificate)certificate, (String)i18n.getMessage("invalidPublicKey"), (String)"invalidPublicKey"));
        validationErrors.add(ValidationRuleChecker.check(cert -> StringUtils.defaultString(cert.getTargetSubjectKeyIdentifier()).length() <= 120, (Certificate)certificate, (String)i18n.getMessage("invalidTargetSubjectKeyIdentifier"), (String)"invalidTargetSubjectKeyIdentifier"));
        validationErrors.add(ValidationRuleChecker.check(cert -> StringUtils.defaultString(cert.getUserRole()).length() <= 255, (Certificate)certificate, (String)i18n.getMessage("invalidUserRole"), (String)"invalidUserRole"));
        validationErrors.add(ValidationRuleChecker.check(cert -> StringUtils.defaultString(cert.getVersion()).length() <= 20, (Certificate)certificate, (String)i18n.getMessage("invalidVersion"), (String)"invalidVersion"));
        return validationErrors;
    }

    private static boolean verifySignatureAttributeHolder(PublicKey publicKey, X509AttributeCertificateHolder attributesCertificateHolder) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, IOException {
        Signature signature = Signature.getInstance("SHA512withEdDSA", SecurityProviders.SNED25519.name());
        signature.initVerify(publicKey);
        signature.update(attributesCertificateHolder.toASN1Structure().getAcinfo().getEncoded());
        boolean verify = signature.verify(attributesCertificateHolder.getSignature());
        return verify;
    }

    private static List<Optional<ValidationError>> validEnhRightsCertificateStructure(Certificate certificate, MetadataManager i18n) {
        ArrayList<Optional<ValidationError>> enhRightsValidationErrorsOptional = new ArrayList<Optional<ValidationError>>();
        enhRightsValidationErrorsOptional.add(ValidationRuleChecker.check((Predicate)ValidationRules.validTargetECU(), (Certificate)certificate, (String)i18n.getMessage("invalidTargetEcu"), (String)"invalidTargetEcu"));
        enhRightsValidationErrorsOptional.add(ValidationRuleChecker.check((Predicate)ValidationRules.validTargetVIN(), (Certificate)certificate, (String)i18n.getMessage("invalidTargetVin"), (String)"invalidTargetVin"));
        enhRightsValidationErrorsOptional.add(ValidationRuleChecker.check((Predicate)ValidationRules.validServices(), (Certificate)certificate, (String)i18n.getMessage("invalidServices"), (String)"invalidServices"));
        enhRightsValidationErrorsOptional.add(ValidationRuleChecker.check((Predicate)ValidationRules.validBaseCertificateID(), (Certificate)certificate, (String)i18n.getMessage("invalidBaseCertificateId"), (String)"invalidBaseCertificateId"));
        if (!certificate.isSecOCISCert()) return enhRightsValidationErrorsOptional;
        enhRightsValidationErrorsOptional.add(ValidationRuleChecker.check((Predicate)ValidationRules.validTargetSubjectKeyIdentifier(), (Certificate)certificate, (String)i18n.getMessage("invalidTargetSubjectKeyIdentifier"), (String)"invalidTargetSubjectKeyIdentifier"));
        return enhRightsValidationErrorsOptional;
    }

    private static List<Optional<ValidationError>> validTimeCertificateStructure(Certificate certificate, MetadataManager i18n) {
        List<Optional<ValidationError>> timeValidationErrorsOptional = CertificatesValidator.validDiagCertificateStructure(certificate, i18n);
        timeValidationErrorsOptional.add(ValidationRuleChecker.check((Predicate)ValidationRules.validNonce(), (Certificate)certificate, (String)i18n.getMessage("invalidNonce"), (String)"invalidNonce"));
        return timeValidationErrorsOptional;
    }

    private static List<Optional<ValidationError>> validDiagCertificateStructure(Certificate certificate, MetadataManager i18n) {
        ArrayList<Optional<ValidationError>> diagValidationErrorsOptional = new ArrayList<Optional<ValidationError>>();
        diagValidationErrorsOptional.add(ValidationRuleChecker.check((Predicate)ValidationRules.validUserRole(), (Certificate)certificate, (String)i18n.getMessage("invalidUserRole"), (String)"invalidUserRole"));
        diagValidationErrorsOptional.add(ValidationRuleChecker.check((Predicate)ValidationRules.validTargetECU(), (Certificate)certificate, (String)i18n.getMessage("invalidTargetEcu"), (String)"invalidTargetEcu"));
        diagValidationErrorsOptional.add(ValidationRuleChecker.check((Predicate)ValidationRules.validTargetVIN(), (Certificate)certificate, (String)i18n.getMessage("invalidTargetVin"), (String)"invalidTargetVin"));
        return diagValidationErrorsOptional;
    }

    private static List<Optional<ValidationError>> validVariantCodingUserCertificateStructure(Certificate certificate, MetadataManager i18n) {
        ArrayList<Optional<ValidationError>> varCodingUsrValidationErrorsOptional = new ArrayList<Optional<ValidationError>>();
        varCodingUsrValidationErrorsOptional.add(ValidationRuleChecker.check((Predicate)ValidationRules.validTargetECU(), (Certificate)certificate, (String)i18n.getMessage("invalidTargetEcu"), (String)"invalidTargetEcu"));
        varCodingUsrValidationErrorsOptional.add(ValidationRuleChecker.check((Predicate)ValidationRules.validTargetVIN(), (Certificate)certificate, (String)i18n.getMessage("invalidTargetVin"), (String)"invalidTargetVin"));
        return varCodingUsrValidationErrorsOptional;
    }

    private static List<Optional<ValidationError>> validEcuCertificateStructure(Certificate certificate, MetadataManager i18n) {
        ArrayList<Optional<ValidationError>> ecuValidationErrorsOptional = new ArrayList<Optional<ValidationError>>();
        ecuValidationErrorsOptional.add(ValidationRuleChecker.check((Predicate)ValidationRules.validUniqueEcuId(), (Certificate)certificate, (String)i18n.getMessage("invalidUniqueEcuId"), (String)"invalidUniqueEcuId"));
        ecuValidationErrorsOptional.add(ValidationRuleChecker.check((Predicate)ValidationRules.validSpecialECU(), (Certificate)certificate, (String)i18n.getMessage("invalidSpecialEcu"), (String)"invalidSpecialEcu"));
        return ecuValidationErrorsOptional;
    }

    private static List<Optional<ValidationError>> validStandardStructure(Certificate certificate, MetadataManager i18n) {
        ArrayList<Optional<ValidationError>> validationErrorsOptional = new ArrayList<Optional<ValidationError>>();
        validationErrorsOptional.add(ValidationRuleChecker.check((Predicate)ValidationRules.validKeyUsage(), (Certificate)certificate, (String)i18n.getMessage("invalidKeyUsage"), (String)"invalidKeyUsage"));
        validationErrorsOptional.add(ValidationRuleChecker.check((Predicate)ValidationRules.validBasicConstraints(), (Certificate)certificate, (String)i18n.getMessage("invalidBasicConstraints"), (String)"invalidBasicConstraints"));
        return validationErrorsOptional;
    }

    private static List<Optional<ValidationError>> getSpecificValidation(Certificate certificate, MetadataManager i18n) {
        ArrayList<Optional<ValidationError>> validationErrorsOptionals = new ArrayList<Optional<ValidationError>>();
        validationErrorsOptionals.add(ValidationRuleChecker.check((Predicate)ValidationRules.validSubject(), (Certificate)certificate, (String)i18n.getMessage("invalidSubject"), (String)"invalidSubject"));
        switch (1.$SwitchMap$com$daimler$cebas$certificates$entity$CertificateType[certificate.getType().ordinal()]) {
            case 3: {
                validationErrorsOptionals.addAll(CertificatesValidator.validEcuCertificateStructure(certificate, i18n));
                return validationErrorsOptionals;
            }
            case 4: {
                validationErrorsOptionals.addAll(CertificatesValidator.validVariantCodingUserCertificateStructure(certificate, i18n));
                return validationErrorsOptionals;
            }
            case 5: {
                return validationErrorsOptionals;
            }
            case 6: {
                validationErrorsOptionals.addAll(CertificatesValidator.validDiagCertificateStructure(certificate, i18n));
                return validationErrorsOptionals;
            }
            case 7: {
                validationErrorsOptionals.addAll(CertificatesValidator.validTimeCertificateStructure(certificate, i18n));
                return validationErrorsOptionals;
            }
            case 2: {
                return CertificatesValidator.validEnhRightsCertificateStructure(certificate, i18n);
            }
        }
        validationErrorsOptionals.addAll(CertificatesValidator.validStandardStructure(certificate, i18n));
        return validationErrorsOptionals;
    }

    private static boolean regularCertificate(Certificate certificate, Logger logger) {
        RawData certificateToCheck = certificate.getCertificateData();
        Certificate parent = certificate.getParent();
        if (parent == null) {
            return false;
        }
        RawData certificateParent = parent.getCertificateData();
        return CertificatesValidator.verifySignature(certificateParent, certificateToCheck, logger);
    }

    private static boolean certificateStructureValid(Certificate certificate) {
        boolean validPublicKeyAKIandSKI = ValidationRules.validPublicKey().test(certificate) && ValidationRules.validAuthorityKeyIdentifier().test(certificate) && ValidationRules.validSubjectKeyIdentifier().test(certificate);
        return validPublicKeyAKIandSKI && ValidationRules.validAlgorithmIdentifier().test(certificate) && ValidationRules.validSignature().test(certificate) && ValidationRules.validProdQualifier().test(certificate) && ValidationRules.validPKIRole().test(certificate) && ValidationRules.validIssuer().test(certificate) && ValidationRules.validVersion().test(certificate) && ValidationRules.validSerialNumber().test(certificate) && ValidationRules.isNotExpired().test(certificate);
    }

    private static boolean enhancedRightsSigVerification(Certificate certificate, Logger logger) {
        RawData certificateToCheck = certificate.getCertificateData();
        Certificate parentEnhanced = certificate.getParent();
        if (parentEnhanced == null) {
            return false;
        }
        RawData certificateParent = parentEnhanced.getParent().getCertificateData();
        return CertificatesValidator.verifySignature(certificateParent, certificateToCheck, logger);
    }

    public static boolean exceedMinRemainingValidity(Certificate certificate, String minRemainingValidity) {
        if (null == minRemainingValidity) {
            return true;
        }
        LocalDateTime minValidity = DurationParser.parse((String)minRemainingValidity).plusTo(LocalDateTime.now());
        return certificate.getValidTo().compareTo(Timestamp.valueOf(minValidity)) > 0;
    }
}
