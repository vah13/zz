/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput
 *  com.daimler.cebas.certificates.control.vo.CertificateSignRequest
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.entity.parsing.CertificateParser
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.HexUtil
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.logs.control.Logger
 */
package com.daimler.cebas.certificates.control.validation;

import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput;
import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.HexUtil;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.logs.control.Logger;
import java.util.Base64;
import java.util.logging.Level;

public class CertificateSignRequestValidator {
    private static final String ONE_SPACE = " ";
    private static final String CLASS_NAME = CertificateSignRequestValidator.class.getSimpleName();
    private static final String PATTERN_NONCE = "^([a-zA-Z0-9]+){2}(-([a-zA-Z0-9]+){2}){31}";

    private CertificateSignRequestValidator() {
    }

    public static void validate(CertificateSignRequest certificateSignRequest, Logger logger, MetadataManager i18n) {
        CertificateSignRequestValidator.validate(certificateSignRequest, logger, i18n, ValidationFailureOutput::outputFailureWithThrow);
    }

    public static void validate(CertificateSignRequest certificateSignRequest, Logger logger, MetadataManager i18n, ValidationFailureOutput strategy) {
        String METHOD_NAME = "validate";
        logger.entering(CLASS_NAME, "validate");
        StringBuilder englishMessages = new StringBuilder();
        StringBuilder translatedMessages = new StringBuilder();
        CertificateType type = CertificateType.valueFromString((String)certificateSignRequest.getCertificateType());
        if (certificateSignRequest.getParentId() == null && certificateSignRequest.getAuthorityKeyIdentifier() == null && certificateSignRequest.getParentId().isEmpty() && certificateSignRequest.getAuthorityKeyIdentifier().isEmpty()) {
            CertificateSignRequestValidator.saveMessages(englishMessages, translatedMessages, "parentCannotBeEmpty", i18n);
        }
        if (type == CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE && (certificateSignRequest.getUserRole() == null || certificateSignRequest.getUserRole().isEmpty())) {
            CertificateSignRequestValidator.saveMessages(englishMessages, translatedMessages, "userRoleCannotBeEmpty", i18n);
        }
        if (type == CertificateType.ECU_CERTIFICATE) {
            if (certificateSignRequest.getUniqueECUID() == null || certificateSignRequest.getUniqueECUID().isEmpty() || certificateSignRequest.getUniqueECUID().length() > 30) {
                CertificateSignRequestValidator.saveMessages(englishMessages, translatedMessages, "uniqueEcuIDMaxSize", i18n);
            }
            if (certificateSignRequest.getTargetECU() != null && !certificateSignRequest.getTargetECU().isEmpty()) {
                CertificateSignRequestValidator.saveMessages(englishMessages, translatedMessages, "targetECUNotApplyToECU", i18n);
            }
            if (certificateSignRequest.getTargetVIN() != null && !certificateSignRequest.getTargetVIN().isEmpty()) {
                CertificateSignRequestValidator.saveMessages(englishMessages, translatedMessages, "targetVINNotApplyToECU", i18n);
            }
        }
        if (type == CertificateType.TIME_CERTIFICATE) {
            String base64DecodedNonce;
            String string = base64DecodedNonce = certificateSignRequest.getNonce() != null ? HexUtil.bytesToHex((byte[])Base64.getDecoder().decode(certificateSignRequest.getNonce())) : null;
            if (base64DecodedNonce == null || base64DecodedNonce.isEmpty() || !base64DecodedNonce.matches(PATTERN_NONCE) && CertificateParser.hexStringToByteArray((String)base64DecodedNonce).length != 32) {
                CertificateSignRequestValidator.saveMessages(englishMessages, translatedMessages, "nonceCannotBeEmptyMatchPattern", PATTERN_NONCE, i18n);
            }
        }
        if (type != CertificateType.TIME_CERTIFICATE && certificateSignRequest.getNonce() != null && !certificateSignRequest.getNonce().isEmpty()) {
            CertificateSignRequestValidator.saveMessages(englishMessages, translatedMessages, "nonceSetOnlyForTimeCert", i18n);
        }
        if (type != CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE && type != CertificateType.TIME_CERTIFICATE && certificateSignRequest.getUserRole() != null && !certificateSignRequest.getUserRole().isEmpty()) {
            CertificateSignRequestValidator.saveMessages(englishMessages, translatedMessages, "userRoleSetDiagCertificate", i18n);
        }
        if (type != CertificateType.ECU_CERTIFICATE && certificateSignRequest.getUniqueECUID() != null && !certificateSignRequest.getUniqueECUID().isEmpty()) {
            CertificateSignRequestValidator.saveMessages(englishMessages, translatedMessages, "uniqueECUIDOnlyForECUCert", i18n);
        }
        if (type != CertificateType.ECU_CERTIFICATE && certificateSignRequest.getSpecialECU() != null && !certificateSignRequest.getSpecialECU().isEmpty()) {
            CertificateSignRequestValidator.saveMessages(englishMessages, translatedMessages, "specialECUCanBeOnlyECUCert", i18n);
        }
        if (certificateSignRequest.getValidTo() == null) {
            CertificateSignRequestValidator.saveMessages(englishMessages, translatedMessages, "validToNotEmpty", i18n);
        }
        if (englishMessages.length() > 0) {
            CEBASCertificateException zenzefiCertificateException = new CEBASCertificateException(i18n.getMessage("certificateSignReqFailed", new String[]{translatedMessages.toString()}), "certificateSignReqFailed");
            logger.logWithTranslation(Level.WARNING, "000105X", zenzefiCertificateException.getMessageId(), new String[]{englishMessages.toString()}, zenzefiCertificateException.getClass().getSimpleName());
            strategy.outputFailure((CEBASException)zenzefiCertificateException);
        }
        logger.exiting(CLASS_NAME, "validate");
    }

    private static void saveMessages(StringBuilder englishMessages, StringBuilder translatedMessages, String messageId, String argument, MetadataManager i18n) {
        englishMessages.append(ONE_SPACE).append(i18n.getEnglishMessage(messageId, new String[]{argument}));
        translatedMessages.append(ONE_SPACE).append(i18n.getMessage(messageId, new String[]{argument}));
    }

    private static void saveMessages(StringBuilder englishMessages, StringBuilder translatedMessages, String messageId, MetadataManager i18n) {
        englishMessages.append(ONE_SPACE).append(i18n.getEnglishMessage(messageId));
        translatedMessages.append(ONE_SPACE).append(i18n.getMessage(messageId));
    }
}
