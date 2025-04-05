/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.InvalidInputException
 *  com.daimler.cebas.certificates.control.exceptions.InvalidInputForExportPublicKeyFileException
 *  com.daimler.cebas.certificates.control.exceptions.SecOCISException
 *  com.daimler.cebas.certificates.control.exceptions.TimeException
 *  com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput
 *  com.daimler.cebas.certificates.entity.parsing.CertificateParser
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.HexUtil
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.FacadePattern
 *  com.daimler.cebas.logs.control.Logger
 *  org.springframework.web.util.HtmlUtils
 */
package com.daimler.cebas.certificates.control.validation;

import com.daimler.cebas.certificates.control.exceptions.InvalidInputException;
import com.daimler.cebas.certificates.control.exceptions.InvalidInputForExportPublicKeyFileException;
import com.daimler.cebas.certificates.control.exceptions.SecOCISException;
import com.daimler.cebas.certificates.control.exceptions.TimeException;
import com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput;
import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.HexUtil;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.FacadePattern;
import com.daimler.cebas.logs.control.Logger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

@FacadePattern
public class CertificatesFieldsValidator {
    private static final String VALUE_SEPARATOR = ", ";
    private static final String REGEX_UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final int SERIAL_NO_MAX_SIZE = 16;
    public static final int AUTHORITY_KEY_IDENTIFIER_SIZE = 20;
    public static final int TARGET_ECU_MAX_SIZE = 30;
    public static final int SUBJECT_KEY_IDENTIFIER_SIZE = 20;
    public static final int NONCE_SIZE = 32;
    public static final int TARGET_VIN_SIZE = 17;
    public static final int SUBJECT_MAX_SIZE = 30;
    public static final int UNIQUE_ECU_ID_MAX_SIZE = 30;
    public static final int SPECIAL_ECU_SIZE = 1;
    public static final String REGEX_VIN_PRINTABLE_CHS = "[\\x21-\\x7E]+";
    public static final String DEFAULT_ZK_NO_REGEX = "^A?\\d{10}$";
    public static final int ALGORITHM_IDENTIFIER_MAX_DB_SIZE = 255;
    public static final int AUTHORITY_KEY_IDENTIFIER_MAX_DB_SIZE = 120;
    public static final int BASE_CERTIFICATE_ID_MAX_DB_SIZE = 255;
    public static final int BASIC_CONSTRAINTS_MAX_DB_SIZE = 255;
    public static final int ISSUER_MAX_DB_SIZE = 50;
    public static final int ISSUER_SERIAL_NUMBER_MAX_DB_SIZE = 100;
    public static final int NONCE_MAX_DB_SIZE = 150;
    public static final int PROD_QUALIFIER_MAX_DB_SIZE = 50;
    public static final int SERIAL_NO_MAX_DB_SIZE = 100;
    public static final int SPECIAL_ECU_MAX_DB_SIZE = 30;
    public static final int SUBJECT_MAX_DB_SIZE = 50;
    public static final int SUBJECT_KEY_IDENTIFIER_MAX_DB_SIZE = 120;
    public static final int SUBJECT_PUBLIC_KEY_MAX_DB_SIZE = 150;
    public static final int TARGET_SUBJECT_KEY_IDENTIFIER_MAX_DB_SIZE = 120;
    public static final int USER_ROLE_MAX_DB_SIZE = 255;
    public static final int VERSION_MAX_DB_SIZE = 20;
    private static String zkNoRegex = "^A?\\d{10}$";

    private CertificatesFieldsValidator() {
    }

    public static void setZkNoRegex(String zkNoRegex) {
        if (null == zkNoRegex) return;
        CertificatesFieldsValidator.zkNoRegex = zkNoRegex;
    }

    public static void isUUID(String idToBeVerify, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.isUUID(idToBeVerify, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
    }

    public static void isUUID(String idToBeVerify, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy) {
        if (idToBeVerify.matches(REGEX_UUID)) return;
        InvalidInputException invalidInputIDException = new InvalidInputException(i18n.getMessage("invalidInputForUUID", new String[]{HtmlUtils.htmlEscape((String)idToBeVerify)}), "invalidInputForUUID");
        logger.log(Level.WARNING, "000188X", i18n.getEnglishMessage(invalidInputIDException.getMessageId(), new String[]{HtmlUtils.htmlEscape((String)idToBeVerify)}), invalidInputIDException.getClass().getSimpleName());
        failureOutputStrategy.outputFailure((CEBASException)invalidInputIDException);
    }

    public static void isUUIDForExportPublicKey(String idToBeVerify, MetadataManager i18n, Logger logger) {
        if (idToBeVerify.matches(REGEX_UUID)) return;
        InvalidInputForExportPublicKeyFileException invalidInputIDException = new InvalidInputForExportPublicKeyFileException(i18n.getMessage("invalidInputForUUID", new String[]{HtmlUtils.htmlEscape((String)idToBeVerify)}), "invalidInputForUUID");
        logger.log(Level.WARNING, "000194X", i18n.getEnglishMessage(invalidInputIDException.getMessageId(), new String[]{HtmlUtils.htmlEscape((String)idToBeVerify)}), invalidInputIDException.getClass().getSimpleName());
        throw invalidInputIDException;
    }

    public static void isUUIDForExportCSRToFile(String idToBeVerify, MetadataManager i18n, Logger logger) {
        if (idToBeVerify.matches(REGEX_UUID)) return;
        InvalidInputException invalidInputIDException = new InvalidInputException(i18n.getMessage("invalidInputForUUID", new String[]{HtmlUtils.htmlEscape((String)idToBeVerify)}), "invalidInputForUUID");
        logger.log(Level.WARNING, "000194X", i18n.getEnglishMessage(invalidInputIDException.getMessageId(), new String[]{HtmlUtils.htmlEscape((String)idToBeVerify)}), invalidInputIDException.getClass().getSimpleName());
        throw invalidInputIDException;
    }

    public static void areUUID(List<String> idsToBeVerify, MetadataManager i18n, Logger logger) {
        ArrayList<String> notValidIDS = new ArrayList<String>();
        Iterator<String> iterator = idsToBeVerify.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                if (notValidIDS.isEmpty()) return;
                notValidIDS.forEach(notValidID -> CertificatesFieldsValidator.isUUID(notValidID, i18n, logger));
                return;
            }
            String entry = iterator.next();
            if (entry.matches(REGEX_UUID)) continue;
            notValidIDS.add(entry);
        }
    }

    public static void isSerialNoLengthCorrect(String serialNoToBeVerify, MetadataManager i18n, Logger logger) {
        if (serialNoToBeVerify != null && CertificateParser.hexStringToByteArray((String)serialNoToBeVerify).length <= 16) {
            if (CertificateParser.hexStringToByteArray((String)serialNoToBeVerify).length >= 1) return;
        }
        InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForSerialNumber", new String[]{CertificatesFieldsValidator.htmlEscape(serialNoToBeVerify)}), "invalidInputForSerialNumber");
        logger.log(Level.WARNING, "000190X", i18n.getEnglishMessage(invalidInputException.getMessageId(), new String[]{HtmlUtils.htmlEscape((String)CertificatesFieldsValidator.htmlEscape(serialNoToBeVerify))}), invalidInputException.getClass().getSimpleName());
        throw invalidInputException;
    }

    public static void isAuthorityKeyIdentifierLengthCorrect(String authorityKeyIdentifierToBeVerify, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect(authorityKeyIdentifierToBeVerify, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
    }

    public static void isAuthorityKeyIdentifierLengthCorrect(String authorityKeyIdentifierToBeVerify, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy) {
        if (authorityKeyIdentifierToBeVerify != null) {
            if (CertificateParser.hexStringToByteArray((String)authorityKeyIdentifierToBeVerify).length == 20) return;
        }
        InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForAuthorityKeyIdentifier", new String[]{CertificatesFieldsValidator.htmlEscape(authorityKeyIdentifierToBeVerify)}), "invalidInputForAuthorityKeyIdentifier");
        logger.log(Level.WARNING, "000191X", i18n.getEnglishMessage(invalidInputException.getMessageId(), new String[]{CertificatesFieldsValidator.htmlEscape(authorityKeyIdentifierToBeVerify)}), invalidInputException.getClass().getSimpleName());
        failureOutputStrategy.outputFailure((CEBASException)invalidInputException);
    }

    public static void isSubjectKeyIdentifierLengthCorrect(String subjectKeyIdentifierToBeVerify, MetadataManager i18n, Logger logger) {
        if (subjectKeyIdentifierToBeVerify != null) {
            if (CertificateParser.hexStringToByteArray((String)subjectKeyIdentifierToBeVerify).length == 20) return;
        }
        InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForSubjectKeyIdentifier", new String[]{CertificatesFieldsValidator.htmlEscape(subjectKeyIdentifierToBeVerify)}), "invalidInputForSubjectKeyIdentifier");
        logger.log(Level.WARNING, "000193X", i18n.getEnglishMessage(invalidInputException.getMessageId(), new String[]{CertificatesFieldsValidator.htmlEscape(subjectKeyIdentifierToBeVerify)}), invalidInputException.getClass().getSimpleName());
        throw invalidInputException;
    }

    public static boolean isSubjectKeyIdentifier(String subjectKeyIdentifierToBeVerify) {
        return subjectKeyIdentifierToBeVerify == null || CertificateParser.hexStringToByteArray((String)subjectKeyIdentifierToBeVerify).length == 20 || !CertificatesFieldsValidator.isZkNo(subjectKeyIdentifierToBeVerify);
    }

    public static boolean isZkNo(String someValue) {
        return null != someValue && someValue.matches(zkNoRegex);
    }

    public static void isIdentifierValid(String identifierToVerify, MetadataManager i18n, Logger logger) {
        if (!StringUtils.isBlank(identifierToVerify)) {
            if (CertificatesFieldsValidator.isZkNo(identifierToVerify)) return;
            if (CertificateParser.hexStringToByteArray((String)HexUtil.base64ToHex((String)identifierToVerify)).length == 20) return;
        }
        InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForIdentifier", new String[]{CertificatesFieldsValidator.htmlEscape(identifierToVerify)}), "invalidInputForIdentifier");
        logger.log(Level.WARNING, "000546X", i18n.getEnglishMessage(invalidInputException.getMessageId(), new String[]{CertificatesFieldsValidator.htmlEscape(identifierToVerify)}), invalidInputException.getClass().getSimpleName());
        throw invalidInputException;
    }

    public static void isTargetVINLengthCorrectMultiple(String targetVinToBeVerify, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.isTargetVINLengthCorrectMultiple(targetVinToBeVerify, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
    }

    public static void isTargetVINLengthCorrectMultiple(String targetVinToBeVerify, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy) {
        String[] vins;
        String[] stringArray = vins = targetVinToBeVerify.split(VALUE_SEPARATOR);
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String vin = stringArray[n2];
            CertificatesFieldsValidator.isTargetVINLengthCorrectSingle(i18n, logger, failureOutputStrategy, vin);
            ++n2;
        }
    }

    public static void isTargetVINLengthCorrectSingle(MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy, String vin) {
        if (vin.length() == 17) return;
        InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForTargetVIN", new String[]{HtmlUtils.htmlEscape((String)vin)}), "invalidInputForTargetVIN");
        logger.log(Level.WARNING, "000192X", i18n.getEnglishMessage(invalidInputException.getMessageId(), new String[]{HtmlUtils.htmlEscape((String)vin)}), invalidInputException.getClass().getSimpleName());
        failureOutputStrategy.outputFailure((CEBASException)invalidInputException);
    }

    public static void isTargetECULengthCorrectMultiple(String targetEcuToBeVerify, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.isTargetECULengthCorrectMultiple(targetEcuToBeVerify, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
    }

    public static void isTargetECULengthCorrectMultiple(String targetEcuToBeVerify, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy) {
        String[] ecus;
        String[] stringArray = ecus = targetEcuToBeVerify.split(VALUE_SEPARATOR);
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String ecu = stringArray[n2];
            CertificatesFieldsValidator.isTargetECULengthCorrentSingle(i18n, logger, failureOutputStrategy, ecu);
            ++n2;
        }
    }

    public static void isTargetECULengthCorrentSingle(MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy, String ecu) {
        if (ecu.length() <= 30) return;
        InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForTargetECU", new String[]{HtmlUtils.htmlEscape((String)ecu)}), "invalidInputForTargetECU");
        logger.log(Level.WARNING, "000180X", i18n.getEnglishMessage(invalidInputException.getMessageId(), new String[]{HtmlUtils.htmlEscape((String)ecu)}), invalidInputException.getClass().getSimpleName());
        failureOutputStrategy.outputFailure((CEBASException)invalidInputException);
    }

    public static void isEmptyInput(List<?> input, MetadataManager i18n, Logger logger) {
        if (input != null) {
            if (!input.isEmpty()) return;
        }
        InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputEmptyList"), "invalidInputEmptyList");
        logger.log(Level.WARNING, "000181X", i18n.getEnglishMessage(invalidInputException.getMessageId()), invalidInputException.getClass().getSimpleName());
        throw invalidInputException;
    }

    public static void isNonceLengthCorrect(String nonceToBeVerify, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.isNonceLengthCorrect(nonceToBeVerify, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
    }

    public static void isNonceLengthCorrect(String nonceToBeVerify, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOuputStrategy) {
        byte[] byteNonceToBeVerify = Base64.getDecoder().decode(nonceToBeVerify);
        if (Base64.getDecoder().decode(nonceToBeVerify) != null) {
            if (Base64.getDecoder().decode(nonceToBeVerify).length == 32) return;
        }
        InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForNonce", new String[]{HtmlUtils.htmlEscape((String)HexUtil.bytesToHex((byte[])byteNonceToBeVerify))}), "invalidInputForNonce");
        logger.log(Level.WARNING, "000182X", i18n.getEnglishMessage(invalidInputException.getMessageId(), new String[]{HtmlUtils.htmlEscape((String)HexUtil.bytesToHex((byte[])byteNonceToBeVerify))}), invalidInputException.getClass().getSimpleName());
        failureOuputStrategy.outputFailure((CEBASException)invalidInputException);
    }

    public static void isSubjectLengthCorrect(String subjectToBeVerify, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.isSubjectLengthCorrect(subjectToBeVerify, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
    }

    public static void isSubjectLengthCorrect(String subjectToBeVerify, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy) {
        byte[] byteSubjectToBeVerify = subjectToBeVerify.getBytes(StandardCharsets.UTF_8);
        if (byteSubjectToBeVerify.length <= 30) return;
        InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForSubject", new String[]{HtmlUtils.htmlEscape((String)subjectToBeVerify)}), "invalidInputForSubject");
        logger.log(Level.WARNING, "000183X", i18n.getEnglishMessage(invalidInputException.getMessageId(), new String[]{HtmlUtils.htmlEscape((String)subjectToBeVerify)}), invalidInputException.getClass().getSimpleName());
        failureOutputStrategy.outputFailure((CEBASException)invalidInputException);
    }

    public static void isUniqueECUIDLengthCorrectMultiple(String uniqueECUIDToBeVerify, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.isUniqueECUIDLengthCorrectMultiple(uniqueECUIDToBeVerify, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
    }

    public static void isUniqueECUIDLengthCorrectMultiple(String uniqueECUIDToBeVerify, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy) {
        String[] uniqueEcus;
        String[] stringArray = uniqueEcus = uniqueECUIDToBeVerify.split(VALUE_SEPARATOR);
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String uniqueEcu = stringArray[n2];
            CertificatesFieldsValidator.isUniqueECUIDLengthCorrectSingle(i18n, logger, failureOutputStrategy, uniqueEcu);
            ++n2;
        }
    }

    public static void isUniqueECUIDLengthCorrectSingle(MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy, String uniqueEcu) {
        if (uniqueEcu.length() <= 30) return;
        InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForUniqueECUID", new String[]{HtmlUtils.htmlEscape((String)uniqueEcu)}), "invalidInputForUniqueECUID");
        logger.log(Level.WARNING, "000185X", i18n.getEnglishMessage(invalidInputException.getMessageId(), new String[]{HtmlUtils.htmlEscape((String)uniqueEcu)}), invalidInputException.getClass().getSimpleName());
        failureOutputStrategy.outputFailure((CEBASException)invalidInputException);
    }

    public static void isSpecialECULengthCorrect(String specialECUToBeVerify, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.isSpecialECULengthCorrect(specialECUToBeVerify, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
    }

    private static boolean vinContainsOnlyPrintableChs(String targetVin) {
        return targetVin.matches(REGEX_VIN_PRINTABLE_CHS);
    }

    public static void checkPrintableChsForTimeCertVin(Logger logger, String targetVin, String CLASS_NAME) {
        if (CertificatesFieldsValidator.vinContainsOnlyPrintableChs(targetVin)) return;
        String message = "Target VIN: " + targetVin + " should contain only printable characters in Time Certificate request";
        logger.log(Level.SEVERE, "000647X", message, CLASS_NAME);
        throw new TimeException(message);
    }

    public static void checkPrintableChsForSecOCISCertVin(Logger logger, String targetVin, String CLASS_NAME) {
        String message = "Target VIN: " + targetVin + " should contain only printable characters in SecOCIS Certificate request";
        if (CertificatesFieldsValidator.vinContainsOnlyPrintableChs(targetVin)) return;
        logger.log(Level.SEVERE, "000648X", message, CLASS_NAME);
        throw new SecOCISException(message);
    }

    public static void isSpecialECULengthCorrect(String specialECUToBeVerify, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy) {
        if (specialECUToBeVerify.length() == 1) return;
        InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForSpecialECU", new String[]{HtmlUtils.htmlEscape((String)specialECUToBeVerify)}), "invalidInputForSpecialECU");
        logger.log(Level.WARNING, "000186X", i18n.getEnglishMessage(invalidInputException.getMessageId(), new String[]{HtmlUtils.htmlEscape((String)specialECUToBeVerify)}), invalidInputException.getClass().getSimpleName());
        failureOutputStrategy.outputFailure((CEBASException)invalidInputException);
    }

    private static String htmlEscape(String string) {
        return string != null ? HtmlUtils.htmlEscape((String)string) : "null";
    }
}
