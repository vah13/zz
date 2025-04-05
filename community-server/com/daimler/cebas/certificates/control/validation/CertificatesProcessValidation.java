/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.InvalidInputException
 *  com.daimler.cebas.certificates.control.validation.CertificatesFieldsValidator
 *  com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput
 *  com.daimler.cebas.certificates.control.vo.AbstractCertificateReplacementPackageInput
 *  com.daimler.cebas.certificates.control.vo.CertificateSignRequest
 *  com.daimler.cebas.certificates.control.vo.ChainReplacementPackageInput
 *  com.daimler.cebas.certificates.control.vo.CheckOwnershipInput
 *  com.daimler.cebas.certificates.control.vo.DeleteCertificateModel
 *  com.daimler.cebas.certificates.control.vo.DeleteCertificates
 *  com.daimler.cebas.certificates.control.vo.EcuSignRequestInput
 *  com.daimler.cebas.certificates.control.vo.ISecOCIsInput
 *  com.daimler.cebas.certificates.control.vo.SecureVariantCodingInput
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.common.control.HexUtil
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.FacadePattern
 *  com.daimler.cebas.logs.control.Logger
 *  org.springframework.web.util.HtmlUtils
 */
package com.daimler.cebas.certificates.control.validation;

import com.daimler.cebas.certificates.control.exceptions.InvalidInputException;
import com.daimler.cebas.certificates.control.validation.CertificatesFieldsValidator;
import com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput;
import com.daimler.cebas.certificates.control.vo.AbstractCertificateReplacementPackageInput;
import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
import com.daimler.cebas.certificates.control.vo.ChainReplacementPackageInput;
import com.daimler.cebas.certificates.control.vo.CheckOwnershipInput;
import com.daimler.cebas.certificates.control.vo.DeleteCertificateModel;
import com.daimler.cebas.certificates.control.vo.DeleteCertificates;
import com.daimler.cebas.certificates.control.vo.EcuSignRequestInput;
import com.daimler.cebas.certificates.control.vo.ISecOCIsInput;
import com.daimler.cebas.certificates.control.vo.SecureVariantCodingInput;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.common.control.HexUtil;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.FacadePattern;
import com.daimler.cebas.logs.control.Logger;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

@FacadePattern
public class CertificatesProcessValidation {
    protected static final String REGEX_BASE64_ENCODED = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";

    protected CertificatesProcessValidation() {
    }

    public static void validateCheckActiveDiagCert(String backendCertSkid, String diagCertSerialNo, String targetEcu, String targetVin, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.isSubjectKeyIdentifierLengthCorrect((String)backendCertSkid, (MetadataManager)i18n, (Logger)logger);
        CertificatesFieldsValidator.isSerialNoLengthCorrect((String)diagCertSerialNo, (MetadataManager)i18n, (Logger)logger);
        if (targetVin != null && !targetVin.isEmpty()) {
            CertificatesFieldsValidator.isTargetVINLengthCorrectSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)targetVin);
        }
        if (targetEcu == null) return;
        CertificatesFieldsValidator.isTargetECULengthCorrentSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)targetEcu);
    }

    public static void validateGetCertByAuthKeyIdentAndSerialNo(String authorityKeyIdentifier, String serialNo, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect((String)authorityKeyIdentifier, (MetadataManager)i18n, (Logger)logger);
        CertificatesFieldsValidator.isSerialNoLengthCorrect((String)serialNo, (MetadataManager)i18n, (Logger)logger);
    }

    public static void validateGetDiagCert(String backendCertSkid, String vin, String ecu, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.isSubjectKeyIdentifierLengthCorrect((String)backendCertSkid, (MetadataManager)i18n, (Logger)logger);
        if (vin != null && !vin.isEmpty()) {
            CertificatesFieldsValidator.isTargetVINLengthCorrectSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)vin);
        }
        if (ecu == null) return;
        if (ecu.isEmpty()) return;
        CertificatesFieldsValidator.isTargetECULengthCorrentSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)ecu);
    }

    public static void validateGetEnhDiagCert(String backendCertSkid, String diagCertSerialNo, String vin, String ecu, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.isSubjectKeyIdentifierLengthCorrect((String)backendCertSkid, (MetadataManager)i18n, (Logger)logger);
        CertificatesFieldsValidator.isSerialNoLengthCorrect((String)diagCertSerialNo, (MetadataManager)i18n, (Logger)logger);
        if (vin != null && !vin.isEmpty()) {
            CertificatesFieldsValidator.isTargetVINLengthCorrectSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)vin);
        }
        if (ecu == null) return;
        if (ecu.isEmpty()) return;
        CertificatesFieldsValidator.isTargetECULengthCorrentSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)ecu);
    }

    public static void validateGetTimeCert(String backendCertSkid, String nonce, String vin, String ecu, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect((String)backendCertSkid, (MetadataManager)i18n, (Logger)logger);
        if (nonce.matches(REGEX_BASE64_ENCODED)) {
            CertificatesFieldsValidator.isNonceLengthCorrect((String)nonce, (MetadataManager)i18n, (Logger)logger);
        } else {
            CertificatesProcessValidation.throwExceptionInputNotBase64Encrypted(i18n, logger, nonce);
        }
        if (StringUtils.isNotEmpty(vin)) {
            CertificatesFieldsValidator.isTargetVINLengthCorrectSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)vin);
            CertificatesFieldsValidator.checkPrintableChsForTimeCertVin((Logger)logger, (String)vin, (String)CertificatesProcessValidation.class.getSimpleName());
            if (!StringUtils.isNotEmpty(ecu)) return;
            CertificatesFieldsValidator.isTargetECULengthCorrentSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)ecu);
            return;
        }
        InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("nullOrEmptyTargetVIN"), "nullOrEmptyTargetVIN");
        logger.log(Level.WARNING, "000192X", i18n.getEnglishMessage(invalidInputException.getMessageId()), invalidInputException.getClass().getSimpleName());
        throw invalidInputException;
    }

    public static void validateGetCertificate(String certificateId, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.isUUID((String)certificateId, (MetadataManager)i18n, (Logger)logger);
    }

    public static void validateDeleteCertificates(List<String> ids, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.areUUID(ids, (MetadataManager)i18n, (Logger)logger);
    }

    public static void validateDeleteCertificate(DeleteCertificates deleteCertificate, MetadataManager i18n, Logger logger) {
        if (deleteCertificate.isAll()) return;
        CertificatesFieldsValidator.isEmptyInput((List)deleteCertificate.getModels(), (MetadataManager)i18n, (Logger)logger);
        deleteCertificate.getModels().forEach(model -> CertificatesProcessValidation.validateDeleteCertificateModel(i18n, logger, model));
    }

    private static void validateDeleteCertificateModel(MetadataManager i18n, Logger logger, DeleteCertificateModel model) {
        if (model.getAuthorityKeyIdentifier() != null && !model.getAuthorityKeyIdentifier().isEmpty()) {
            if (model.getAuthorityKeyIdentifier().matches(REGEX_BASE64_ENCODED)) {
                CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect((String)HexUtil.bytesToHex((byte[])Base64.getDecoder().decode(model.getAuthorityKeyIdentifier())), (MetadataManager)i18n, (Logger)logger);
            } else {
                CertificatesProcessValidation.throwExceptionInputNotBase64Encrypted(i18n, logger, model.getAuthorityKeyIdentifier());
            }
        }
        String serialString = null;
        if (model.getSerialNo() != null) {
            serialString = HexUtil.bytesToHex((byte[])Base64.getDecoder().decode(model.getSerialNo()));
        }
        CertificatesFieldsValidator.isSerialNoLengthCorrect(serialString, (MetadataManager)i18n, (Logger)logger);
    }

    public static void validateCreateCertificateSignRequest(CertificateSignRequest certificateSignRequest, MetadataManager i18n, Logger logger) {
        CertificatesProcessValidation.validateCreateCertificateSignRequest(certificateSignRequest, i18n, logger, ValidationFailureOutput::outputFailureWithThrow);
    }

    public static void validateCreateCertificateSignRequest(CertificateSignRequest certificateSignRequest, MetadataManager i18n, Logger logger, ValidationFailureOutput failureOutputStrategy) {
        if (certificateSignRequest.getUserId() != null) {
            CertificatesFieldsValidator.isUUID((String)certificateSignRequest.getUserId(), (MetadataManager)i18n, (Logger)logger, (ValidationFailureOutput)failureOutputStrategy);
        }
        if (certificateSignRequest.getSubject() != null) {
            CertificatesFieldsValidator.isSubjectLengthCorrect((String)certificateSignRequest.getSubject(), (MetadataManager)i18n, (Logger)logger, (ValidationFailureOutput)failureOutputStrategy);
        }
        if (certificateSignRequest.getAuthorityKeyIdentifier() != null && !certificateSignRequest.getAuthorityKeyIdentifier().isEmpty()) {
            CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect((String)HexUtil.bytesToHex((byte[])Base64.getDecoder().decode(certificateSignRequest.getAuthorityKeyIdentifier())), (MetadataManager)i18n, (Logger)logger, (ValidationFailureOutput)failureOutputStrategy);
        }
        if (certificateSignRequest.getParentId() != null) {
            CertificatesFieldsValidator.isUUID((String)certificateSignRequest.getParentId(), (MetadataManager)i18n, (Logger)logger);
        }
        if (certificateSignRequest.getCertificateType().equals(CertificateType.TIME_CERTIFICATE.getText())) {
            if (certificateSignRequest.getNonce().matches(REGEX_BASE64_ENCODED)) {
                CertificatesFieldsValidator.isNonceLengthCorrect((String)certificateSignRequest.getNonce(), (MetadataManager)i18n, (Logger)logger, (ValidationFailureOutput)failureOutputStrategy);
            } else {
                CertificatesProcessValidation.throwExceptionInputNotBase64Encrypted(i18n, logger, certificateSignRequest.getNonce());
            }
        }
        if (certificateSignRequest.getCertificateType().equals(CertificateType.ECU_CERTIFICATE.getText())) {
            CertificatesFieldsValidator.isUniqueECUIDLengthCorrectMultiple((String)certificateSignRequest.getUniqueECUID(), (MetadataManager)i18n, (Logger)logger, (ValidationFailureOutput)failureOutputStrategy);
            CertificatesFieldsValidator.isSpecialECULengthCorrect((String)certificateSignRequest.getSpecialECU(), (MetadataManager)i18n, (Logger)logger, (ValidationFailureOutput)failureOutputStrategy);
        }
        if (certificateSignRequest.getTargetECU() != null && !certificateSignRequest.getTargetECU().isEmpty()) {
            CertificatesFieldsValidator.isTargetECULengthCorrectMultiple((String)certificateSignRequest.getTargetECU(), (MetadataManager)i18n, (Logger)logger, (ValidationFailureOutput)failureOutputStrategy);
        }
        if (certificateSignRequest.getTargetVIN() == null) return;
        if (certificateSignRequest.getTargetVIN().isEmpty()) return;
        CertificatesFieldsValidator.isTargetVINLengthCorrectMultiple((String)certificateSignRequest.getTargetVIN(), (MetadataManager)i18n, (Logger)logger, (ValidationFailureOutput)failureOutputStrategy);
    }

    public static void validateGetCertificateFromCSRId(String csrId, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.isUUIDForExportPublicKey((String)csrId, (MetadataManager)i18n, (Logger)logger);
    }

    public static void validateExportCsrToFile(String csrId, MetadataManager i18n, Logger logger) {
        CertificatesFieldsValidator.isUUIDForExportCSRToFile((String)csrId, (MetadataManager)i18n, (Logger)logger);
    }

    public static void validateCheckOwnership(CheckOwnershipInput checkOwnershipInput, MetadataManager i18n, Logger logger) {
        if (StringUtils.isEmpty(checkOwnershipInput.getBackendCertSubjKeyId()) || StringUtils.isEmpty(checkOwnershipInput.getSerialNumber()) || StringUtils.isEmpty(checkOwnershipInput.getEcuChallenge())) {
            InvalidInputException ex = new InvalidInputException(i18n.getMessage("invalidInputForCheckOwnership"), "invalidInputForCheckOwnership");
            logger.logWithTranslation(Level.WARNING, "000142X", ex.getMessageId(), ex.getClass().getSimpleName());
            throw ex;
        }
        if (checkOwnershipInput.getBackendCertSubjKeyId().matches(REGEX_BASE64_ENCODED)) {
            CertificatesFieldsValidator.isSubjectKeyIdentifierLengthCorrect((String)HexUtil.bytesToHex((byte[])Base64.getDecoder().decode(checkOwnershipInput.getBackendCertSubjKeyId())), (MetadataManager)i18n, (Logger)logger);
        } else {
            CertificatesProcessValidation.throwExceptionInputNotBase64Encrypted(i18n, logger, checkOwnershipInput.getBackendCertSubjKeyId());
        }
        if (checkOwnershipInput.getSerialNumber().matches(REGEX_BASE64_ENCODED)) {
            CertificatesFieldsValidator.isSerialNoLengthCorrect((String)HexUtil.bytesToHex((byte[])Base64.getDecoder().decode(checkOwnershipInput.getSerialNumber())), (MetadataManager)i18n, (Logger)logger);
        } else {
            CertificatesProcessValidation.throwExceptionInputNotBase64Encrypted(i18n, logger, checkOwnershipInput.getSerialNumber());
        }
    }

    public static void validateGetSecOCISCert(ISecOCIsInput secOCISInput, MetadataManager i18n, Logger logger) {
        if (secOCISInput.getBackendCertSubjKeyId().matches(REGEX_BASE64_ENCODED)) {
            CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect((String)HexUtil.bytesToHex((byte[])Base64.getDecoder().decode(secOCISInput.getBackendCertSubjKeyId())), (MetadataManager)i18n, (Logger)logger);
        } else {
            CertificatesProcessValidation.throwExceptionInputNotBase64Encrypted(i18n, logger, secOCISInput.getBackendCertSubjKeyId());
        }
        if (secOCISInput.getDiagCertSerialNumber().matches(REGEX_BASE64_ENCODED)) {
            CertificatesFieldsValidator.isSerialNoLengthCorrect((String)HexUtil.bytesToHex((byte[])Base64.getDecoder().decode(secOCISInput.getDiagCertSerialNumber())), (MetadataManager)i18n, (Logger)logger);
        } else {
            CertificatesProcessValidation.throwExceptionInputNotBase64Encrypted(i18n, logger, secOCISInput.getDiagCertSerialNumber());
        }
        if (secOCISInput.getTargetECU() != null && !secOCISInput.getTargetECU().isEmpty()) {
            CertificatesFieldsValidator.isTargetECULengthCorrentSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)secOCISInput.getTargetECU());
        }
        if (secOCISInput.getTargetVIN() == null) return;
        if (secOCISInput.getTargetVIN().isEmpty()) return;
        CertificatesFieldsValidator.isTargetVINLengthCorrectSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)secOCISInput.getTargetVIN());
        CertificatesFieldsValidator.checkPrintableChsForSecOCISCertVin((Logger)logger, (String)secOCISInput.getTargetVIN(), (String)CertificatesProcessValidation.class.getSimpleName());
    }

    public static void validateSecureVariantCoding(SecureVariantCodingInput secureVariantCodingInput, MetadataManager i18n, Logger logger) {
        if (secureVariantCodingInput.getBackendSubjectKeyIdentifier().matches(REGEX_BASE64_ENCODED)) {
            CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect((String)HexUtil.bytesToHex((byte[])Base64.getDecoder().decode(secureVariantCodingInput.getBackendSubjectKeyIdentifier())), (MetadataManager)i18n, (Logger)logger);
        } else {
            CertificatesProcessValidation.throwExceptionInputNotBase64Encrypted(i18n, logger, secureVariantCodingInput.getBackendSubjectKeyIdentifier());
        }
        if (secureVariantCodingInput.getTargetECU() != null) {
            CertificatesFieldsValidator.isTargetECULengthCorrentSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)secureVariantCodingInput.getTargetECU());
        }
        if (secureVariantCodingInput.getTargetVIN() == null) return;
        if (secureVariantCodingInput.getTargetVIN().isEmpty()) return;
        CertificatesFieldsValidator.isTargetVINLengthCorrectSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)secureVariantCodingInput.getTargetVIN());
    }

    public static void validateGetDataSignedByEcuPrivateKey(EcuSignRequestInput input, Logger logger, MetadataManager i18n) {
        if (StringUtils.isEmpty(input.getBackendSubjectKeyIdentifier())) {
            CertificatesProcessValidation.handleGetDataSignedByEcuPrivateKeyInvalidInput("backendSubjectKeyIdentifierMandatory", logger, i18n);
        }
        if (StringUtils.isEmpty(input.getChallenge())) {
            CertificatesProcessValidation.handleGetDataSignedByEcuPrivateKeyInvalidInput("challangeByteArrayMandatory", logger, i18n);
        }
        if (StringUtils.isEmpty(input.getEcuId())) {
            CertificatesProcessValidation.handleGetDataSignedByEcuPrivateKeyInvalidInput("invalidUniqueEcuId", logger, i18n);
        }
        if (input.getBackendSubjectKeyIdentifier().matches(REGEX_BASE64_ENCODED)) {
            CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect((String)HexUtil.bytesToHex((byte[])Base64.getDecoder().decode(input.getBackendSubjectKeyIdentifier())), (MetadataManager)i18n, (Logger)logger);
        } else {
            CertificatesProcessValidation.throwExceptionInputNotBase64Encrypted(i18n, logger, input.getBackendSubjectKeyIdentifier());
        }
        if (!StringUtils.isEmpty(input.getEcuSerialNumber())) {
            if (input.getEcuSerialNumber().matches(REGEX_BASE64_ENCODED)) {
                CertificatesFieldsValidator.isSerialNoLengthCorrect((String)HexUtil.bytesToHex((byte[])Base64.getDecoder().decode(input.getEcuSerialNumber())), (MetadataManager)i18n, (Logger)logger);
            } else {
                CertificatesProcessValidation.throwExceptionInputNotBase64Encrypted(i18n, logger, input.getEcuSerialNumber());
            }
        }
        if (!input.getChallenge().matches(REGEX_BASE64_ENCODED)) {
            CertificatesProcessValidation.throwExceptionInputNotBase64Encrypted(i18n, logger, input.getChallenge());
        }
        CertificatesFieldsValidator.isUniqueECUIDLengthCorrectSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)input.getEcuId());
    }

    public static void validateAutomaticReplacementPackageInput(AbstractCertificateReplacementPackageInput input, Logger logger, MetadataManager i18n) {
        CertificatesProcessValidation.validateReplacementPackageCertificate(input.getCertificate(), logger, i18n);
        CertificatesProcessValidation.validateReplacementPackageSKI(input.getTargetBackendCertSubjKeyId(), logger, i18n);
        CertificatesProcessValidation.validateAutomaticReplacementPackageUniqueEcuId(input.getUniqueEcuId(), logger, i18n);
    }

    public static void validateAutomaticChainReplacementPackageInput(ChainReplacementPackageInput input, Logger logger, MetadataManager i18n) {
        CertificatesProcessValidation.validateReplacementPackageSKI(input.getTargetBackendCertSubjKeyId(), logger, i18n);
        CertificatesProcessValidation.validateReplacementPackageSKI(input.getSourceBackendCertSubjKeyId(), logger, i18n);
    }

    private static void validateReplacementPackageSKI(String subjectKeyIdentifier, Logger logger, MetadataManager i18n) {
        if (StringUtils.isEmpty(subjectKeyIdentifier)) {
            InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputForSubjectKeyIdentifier", new String[]{subjectKeyIdentifier}), "invalidInputForSubjectKeyIdentifier");
            logger.logWithTranslation(Level.WARNING, "000300X", invalidInputException.getMessageId(), invalidInputException.getClass().getSimpleName());
            throw invalidInputException;
        }
        if (subjectKeyIdentifier.matches(REGEX_BASE64_ENCODED)) {
            CertificatesFieldsValidator.isAuthorityKeyIdentifierLengthCorrect((String)HexUtil.bytesToHex((byte[])Base64.getDecoder().decode(subjectKeyIdentifier)), (MetadataManager)i18n, (Logger)logger);
        } else {
            CertificatesProcessValidation.throwExceptionInputNotBase64Encrypted(i18n, logger, subjectKeyIdentifier);
        }
    }

    protected static void throwExceptionInputNotBase64Encrypted(MetadataManager i18n, Logger logger, String value) {
        InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidInputNotBase64Encoded", new String[]{HtmlUtils.htmlEscape((String)value)}), "invalidInputNotBase64Encoded");
        logger.log(Level.WARNING, "000195X", i18n.getEnglishMessage(invalidInputException.getMessageId(), new String[]{HtmlUtils.htmlEscape((String)value)}), invalidInputException.getClass().getSimpleName());
        throw invalidInputException;
    }

    private static void handleGetDataSignedByEcuPrivateKeyInvalidInput(String i18nMessageId, Logger logger, MetadataManager i18n) {
        InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage(i18nMessageId), i18nMessageId);
        logger.logWithTranslation(Level.WARNING, "000275X", invalidInputException.getMessageId(), invalidInputException.getClass().getSimpleName());
        throw invalidInputException;
    }

    private static void validateAutomaticReplacementPackageUniqueEcuId(String uniqueEcuId, Logger logger, MetadataManager i18n) {
        if (StringUtils.isEmpty(uniqueEcuId)) return;
        CertificatesFieldsValidator.isUniqueECUIDLengthCorrectSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)uniqueEcuId);
    }

    private static void validateReplacementPackageCertificate(String certificate, Logger logger, MetadataManager i18n) {
        if (StringUtils.isEmpty(certificate)) {
            InvalidInputException invalidInputException = new InvalidInputException(i18n.getMessage("invalidCertificateType", new String[]{CertificateType.ECU_CERTIFICATE.name()}), "invalidCertificateType");
            logger.logWithTranslation(Level.WARNING, "000301X", invalidInputException.getMessageId(), new String[]{CertificateType.ECU_CERTIFICATE.name()}, invalidInputException.getClass().getSimpleName());
            throw invalidInputException;
        }
        if (certificate.matches(REGEX_BASE64_ENCODED)) return;
        CertificatesProcessValidation.throwExceptionInputNotBase64Encrypted(i18n, logger, certificate);
    }
}
