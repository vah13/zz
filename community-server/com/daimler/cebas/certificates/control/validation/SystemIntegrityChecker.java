/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.chain.ChainOfTrust
 *  com.daimler.cebas.certificates.control.repository.CertificateRepository
 *  com.daimler.cebas.certificates.control.validation.CertificatesValidator
 *  com.daimler.cebas.certificates.control.validation.SystemIntegrityCheckError
 *  com.daimler.cebas.certificates.control.validation.SystemIntegrityCheckResult
 *  com.daimler.cebas.certificates.control.validation.SystemIntegrityChecker$1
 *  com.daimler.cebas.certificates.control.validation.ValidationError
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateState
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.entity.RawData
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  com.daimler.cebas.users.entity.User
 *  org.springframework.beans.factory.annotation.Autowired
 */
package com.daimler.cebas.certificates.control.validation;

import com.daimler.cebas.certificates.control.chain.ChainOfTrust;
import com.daimler.cebas.certificates.control.repository.CertificateRepository;
import com.daimler.cebas.certificates.control.validation.CertificatesValidator;
import com.daimler.cebas.certificates.control.validation.SystemIntegrityCheckError;
import com.daimler.cebas.certificates.control.validation.SystemIntegrityCheckResult;
import com.daimler.cebas.certificates.control.validation.SystemIntegrityChecker;
import com.daimler.cebas.certificates.control.validation.ValidationError;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateState;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.entity.RawData;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import com.daimler.cebas.users.entity.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@CEBASControl
public class SystemIntegrityChecker {
    private static final String CLASS_NAME = SystemIntegrityChecker.class.getName();
    private static final String HEAD_TAG_START = "<";
    private static final String HEAD_TAG_END = ">";
    private static final String TAIL_TAG_START = "</";
    private static final String TAIL_TAG_END = ">";
    private static final String HEAD_INTEGRITY_CHECK_MESSAGE = "<IntegrityCheckMessage>";
    private static final String TAIL_INTEGRITY_CHECK_MESSAGE = "</IntegrityCheckMessage>";
    private static final String ENCODING_TAG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String CERTIFICATES_TAG_START = "<CertificateIntegrityCheck>";
    private static final String CERTIFICATES_TAG_END = "</CertificateIntegrityCheck>";
    private static final String CHECK_RESULT_TAG_START = "<IntegrityCheckResult>";
    private static final String CHECK_RESULT_TAG_END = "</IntegrityCheckResult>";
    private static final String TOTAL_CERTIFICATES_CHECKED_TAG_START = "<TotalCertificatesChecked>";
    private static final String TOTAL_CERTIFICATES_CHECKED_TAG_END = "</TotalCertificatesChecked>";
    private static final String TOTAL_CERTIFICATES_VALID_TAG_START = "<TotalValidCertificates>";
    private static final String TOTAL_CERTIFICATES_VALID_TAG_END = "</TotalValidCertificates>";
    private static final String TOTAL_CERTIFICATES_INVALID_TAG_START = "<TotalInvalidCertificates>";
    private static final String TOTAL_CERTIFICATES_INVALID_TAG_END = "</TotalInvalidCertificates>";
    private static final String CHECK_RESULT_OK = "OK";
    private static final String CHECK_RESULT_FAILED = "Failed";
    private static final String LOG_ROOT = "RootCertificate";
    private static final String LOG_ROOT_LINK = "RootLinkCertificate";
    private static final String LOG_BACKEND = "BackendCertificate";
    private static final String LOG_BACKEND_LINK = "BackendLinkCertificate";
    private static final String LOG_ECU = "ECUCertificate";
    private static final String LOG_DIAGNOSTIC = "DiagnosticAuthenticationCertificate";
    private static final String LOG_ENHANCED_RIGHTS = "EnhancedRightsCertificate";
    private static final String LOG_TIME = "TimeCertificate";
    private static final String LOG_VARIANT_CODE_USER = "VariantCodeUserCertificate";
    private static final String LOG_VARIANT_CODING_DEVICE = "VariantCodingDeviceCertificate";
    protected Session session;
    private Logger logger;
    private MetadataManager i18n;
    private CertificateRepository repo;

    @Autowired
    public SystemIntegrityChecker(CertificateRepository repo, Session session, MetadataManager i18n, Logger logger) {
        this.session = session;
        this.i18n = i18n;
        this.logger = logger;
        this.repo = repo;
    }

    public void checkSystemIntegrity(User currentUser, SystemIntegrityCheckResult sessionIntegrityCheckResult) {
        String METHOD_NAME = "checkSystemIntegrity";
        this.logger.entering(CLASS_NAME, "checkSystemIntegrity");
        this.checkCertificatesIntegrity(currentUser.getCertificates(), sessionIntegrityCheckResult);
        this.logger.exiting(CLASS_NAME, "checkSystemIntegrity");
    }

    private void checkCertificatesIntegrity(List<Certificate> certificatesList, SystemIntegrityCheckResult sessionIntegrityCheckResult) {
        StringBuilder xmlReport = new StringBuilder();
        AtomicInteger totalNumberOfCertificates = new AtomicInteger(0);
        ArrayList<SystemIntegrityCheckError> errors = new ArrayList<SystemIntegrityCheckError>();
        for (Certificate root : certificatesList) {
            Optional<SystemIntegrityCheckError> resultOptional = this.prepareValidationResult(totalNumberOfCertificates, root);
            resultOptional.ifPresent(errors::add);
            List<Certificate> flattenedRoot = root.flattened().collect(Collectors.toList());
            xmlReport.append((CharSequence)this.createReport(flattenedRoot, root, errors, totalNumberOfCertificates));
        }
        String report = xmlReport.toString();
        xmlReport.delete(0, xmlReport.length());
        xmlReport.append(ENCODING_TAG);
        xmlReport.append(CERTIFICATES_TAG_START);
        this.appendToXML(xmlReport, CHECK_RESULT_TAG_START, CHECK_RESULT_TAG_END, !errors.isEmpty() ? CHECK_RESULT_FAILED : CHECK_RESULT_OK);
        this.appendToXML(xmlReport, TOTAL_CERTIFICATES_CHECKED_TAG_START, TOTAL_CERTIFICATES_CHECKED_TAG_END, totalNumberOfCertificates.get());
        this.appendToXML(xmlReport, TOTAL_CERTIFICATES_VALID_TAG_START, TOTAL_CERTIFICATES_VALID_TAG_END, totalNumberOfCertificates.get() - errors.size());
        this.appendToXML(xmlReport, TOTAL_CERTIFICATES_INVALID_TAG_START, TOTAL_CERTIFICATES_INVALID_TAG_END, errors.size());
        xmlReport.append(report);
        xmlReport.append(CERTIFICATES_TAG_END);
        sessionIntegrityCheckResult.updateIntegrityCheckXML(xmlReport.toString());
        sessionIntegrityCheckResult.updateErrosList(errors);
        sessionIntegrityCheckResult.updateTotalNumberOfCheckedCertificates(totalNumberOfCertificates.get());
        this.repo.clearContext();
    }

    private void appendToXML(StringBuilder xmlReport, String tagStart, String tagEnd, Object value) {
        xmlReport.append(tagStart).append(value).append(tagEnd);
    }

    private Optional<SystemIntegrityCheckError> prepareValidationResult(AtomicInteger totalNumberOfCertificates, Certificate certificate) {
        totalNumberOfCertificates.incrementAndGet();
        return this.validateCertificate(certificate);
    }

    private void checkParentSubjectKeyMatchesChildAuthKey(Certificate certificate, List<SystemIntegrityCheckError> errors) {
        if (certificate.getParent() == null) return;
        if (certificate.getType() == CertificateType.ENHANCED_RIGHTS_CERTIFICATE) {
            Certificate parentBackend = certificate.getParent().getParent();
            if (parentBackend == null) return;
            if (certificate.getAuthorityKeyIdentifier().equals(parentBackend.getSubjectKeyIdentifier())) return;
            this.addKeysNotMatchError(certificate, errors, "parentSubjectKeyNotMatchChildAuthKey");
        } else {
            if (certificate.getAuthorityKeyIdentifier().equals(certificate.getParent().getSubjectKeyIdentifier())) return;
            this.addKeysNotMatchError(certificate, errors, "parentSubjectKeyNotMatchChildAuthKey");
        }
    }

    private void checkRootLinkSubjectKeyIdentifier(Certificate certificate, List<SystemIntegrityCheckError> errors) {
        if (certificate.getParent() == null) return;
        if (certificate.getSubjectKeyIdentifier().equals(certificate.getParent().getSubjectKeyIdentifier())) return;
        this.addKeysNotMatchError(certificate, errors, "parentSubjectKeyNotMatchChildSubjKey");
    }

    private void addKeysNotMatchError(Certificate certificate, List<SystemIntegrityCheckError> errors, String messageId) {
        String errorMessage = this.i18n.getMessage(messageId);
        AtomicBoolean certificateIdHasErrors = new AtomicBoolean(false);
        errors.forEach(error -> {
            if (!error.getCertificateId().equals(certificate.getEntityId())) return;
            error.getErrorMessages().add(errorMessage);
            error.getMessageIds().add(messageId);
            certificateIdHasErrors.set(true);
        });
        if (certificateIdHasErrors.get()) return;
        errors.add(new SystemIntegrityCheckError(certificate.getEntityId(), Collections.singletonList(errorMessage), Collections.singletonList(messageId)));
    }

    private Optional<SystemIntegrityCheckError> validateCertificate(Certificate certificate) {
        RawData toBeVerifiedData;
        List validationResult = CertificatesValidator.extendedValidation((Certificate)certificate, (Logger)this.logger, (MetadataManager)this.i18n);
        RawData issuerData = this.getIssuerCertificateData(certificate);
        Optional<ValidationError> signatureVerificationOptional = this.verifySignature(certificate, issuerData, toBeVerifiedData = certificate.getCertificateData());
        if (signatureVerificationOptional.isPresent()) {
            validationResult.add(signatureVerificationOptional);
        }
        if (!this.isUnderBackend(certificate)) return this.saveErrorMessage(validationResult, certificate);
        if (CertificatesValidator.checkPublicKeyMatchesTheOneGeneratedFromPrivateKey((Session)this.session, (Certificate)certificate, (Logger)this.logger)) return this.saveErrorMessage(validationResult, certificate);
        validationResult.add(Optional.of(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("invalidOrMissingPrivateKey", new String[]{certificate.getSubjectKeyIdentifier()}), "invalidOrMissingPrivateKey", new String[]{certificate.getSubjectKeyIdentifier()})));
        return this.saveErrorMessage(validationResult, certificate);
    }

    private boolean isUnderBackend(Certificate certificate) {
        List<CertificateType> underBackend = Arrays.asList(CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE, CertificateType.ECU_CERTIFICATE, CertificateType.TIME_CERTIFICATE, CertificateType.VARIANT_CODING_DEVICE_CERTIFICATE, CertificateType.VARIANT_CODE_USER_CERTIFICATE);
        return underBackend.contains(certificate.getType());
    }

    private RawData getIssuerCertificateData(Certificate certificate) {
        RawData issuerData;
        if (certificate.getParent() == null) {
            issuerData = certificate.getCertificateData();
        } else if (certificate.getType() == CertificateType.ROOT_CA_LINK_CERTIFICATE) {
            List roots = certificate.getUser().getCertificates();
            Optional linked = ChainOfTrust.getParentOfLinkCertificate((List)roots, (Certificate)certificate);
            issuerData = linked.orElse(certificate).getCertificateData();
        } else if (certificate.getType() == CertificateType.BACKEND_CA_LINK_CERTIFICATE) {
            List backends = certificate.getParent().getParent().getChildren();
            Optional linked = ChainOfTrust.getParentOfLinkCertificate((List)backends, (Certificate)certificate);
            issuerData = linked.orElse(certificate).getCertificateData();
        } else {
            issuerData = certificate.getCertificateData().isCertificate() ? certificate.getParent().getCertificateData() : certificate.getParent().getParent().getCertificateData();
        }
        return issuerData;
    }

    private Optional<ValidationError> verifySignature(Certificate certificate, RawData issuerData, RawData toBeVerifiedData) {
        if (CertificatesValidator.verifySignature((RawData)issuerData, (RawData)toBeVerifiedData, (Logger)this.logger)) return Optional.empty();
        return Optional.of(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("sigVerificationFailedSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}), "sigVerificationFailedSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}));
    }

    private Optional<SystemIntegrityCheckError> saveErrorMessage(List<Optional<ValidationError>> validationResult, Certificate certificate) {
        ArrayList errorMessages = new ArrayList();
        ArrayList messageIds = new ArrayList();
        validationResult.forEach(error -> error.ifPresent(e -> {
            errorMessages.add(e.getErrorMessage());
            messageIds.add(e.getMessageId());
        }));
        if (errorMessages.isEmpty()) return Optional.empty();
        return Optional.of(new SystemIntegrityCheckError(certificate.getEntityId(), errorMessages, messageIds));
    }

    private StringBuilder createReport(List<Certificate> certificates, Certificate root, List<SystemIntegrityCheckError> errors, AtomicInteger totalNumberOfCertificates) {
        StringBuilder xmlBuffer = new StringBuilder();
        boolean isBackendTagOpen = false;
        boolean isRootTagOpen = false;
        boolean isDiagnosticTagOpen = false;
        int i = 0;
        while (i < certificates.size()) {
            Certificate certificate = certificates.get(i);
            this.getErrorsFromValidation(root, errors, totalNumberOfCertificates, certificate);
            if (isDiagnosticTagOpen && this.shouldCloseDiagnosticTag(certificate)) {
                this.closeXMLTagIfParentChanged(CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE, xmlBuffer);
                isDiagnosticTagOpen = false;
            }
            if (isBackendTagOpen && this.shouldCloseBackendTag(certificate)) {
                this.closeXMLTagIfParentChanged(CertificateType.BACKEND_CA_CERTIFICATE, xmlBuffer);
                isBackendTagOpen = false;
            }
            if (isRootTagOpen && this.isRootCertificate(certificate)) {
                this.closeXMLTagIfParentChanged(CertificateType.ROOT_CA_CERTIFICATE, xmlBuffer);
                isRootTagOpen = false;
            }
            xmlBuffer.append(HEAD_TAG_START);
            xmlBuffer.append(this.getCertificateTypeFromEnum(certificate.getType()));
            xmlBuffer.append(" id=\"").append(certificate.getEntityId()).append("\"");
            xmlBuffer.append(" name=\"").append(certificate.getSubject()).append("\"");
            xmlBuffer.append(">");
            if (!errors.isEmpty()) {
                xmlBuffer.append(HEAD_INTEGRITY_CHECK_MESSAGE);
                errors.stream().filter(error -> error.getCertificateId().equals(certificate.getEntityId())).collect(Collectors.toList()).forEach(error -> error.getErrorMessages().forEach(xmlBuffer::append));
                xmlBuffer.append(TAIL_INTEGRITY_CHECK_MESSAGE);
            }
            if (this.doesNotHaveChildrenIsNotRootOrBackendOrDiag(certificate)) {
                xmlBuffer.append(TAIL_TAG_START).append(this.getCertificateTypeFromEnum(certificate.getType())).append(">");
            }
            if (this.isRootCertificate(certificate)) {
                isRootTagOpen = true;
            } else if (this.isBackendCertificate(certificate)) {
                isBackendTagOpen = true;
            } else if (this.isDiagnosticCertificate(certificate)) {
                isDiagnosticTagOpen = true;
            }
            if (i == certificates.size() - 1) {
                if (isDiagnosticTagOpen) {
                    this.closeXMLTagIfParentChanged(CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE, xmlBuffer);
                }
                this.closeRootAndBackendTags(xmlBuffer, isBackendTagOpen);
            }
            ++i;
        }
        return xmlBuffer;
    }

    private void getErrorsFromValidation(Certificate root, List<SystemIntegrityCheckError> errors, AtomicInteger totalNumberOfCertificates, Certificate certificate) {
        if (certificate.getEntityId().equals(root.getEntityId())) return;
        if (!certificate.getState().equals((Object)CertificateState.ISSUED)) return;
        Optional<SystemIntegrityCheckError> resultOptional = this.prepareValidationResult(totalNumberOfCertificates, certificate);
        resultOptional.ifPresent(errors::add);
        if (certificate.getType() == CertificateType.ROOT_CA_LINK_CERTIFICATE || certificate.getType() == CertificateType.BACKEND_CA_LINK_CERTIFICATE) {
            this.checkRootLinkSubjectKeyIdentifier(certificate, errors);
        } else {
            this.checkParentSubjectKeyMatchesChildAuthKey(certificate, errors);
        }
    }

    private void closeXMLTagIfParentChanged(CertificateType certificateType, StringBuilder xmlBuffer) {
        xmlBuffer.append(TAIL_TAG_START).append(this.getCertificateTypeFromEnum(certificateType)).append(">");
    }

    private void closeRootAndBackendTags(StringBuilder xmlBuffer, boolean isBackendTagOpen) {
        if (isBackendTagOpen) {
            xmlBuffer.append(TAIL_TAG_START).append(this.getCertificateTypeFromEnum(CertificateType.BACKEND_CA_CERTIFICATE)).append(">");
        }
        xmlBuffer.append(TAIL_TAG_START).append(this.getCertificateTypeFromEnum(CertificateType.ROOT_CA_CERTIFICATE)).append(">");
    }

    private boolean shouldCloseBackendTag(Certificate nextCertificate) {
        return this.isBackendCertificate(nextCertificate) || this.isRootCertificate(nextCertificate);
    }

    private boolean shouldCloseDiagnosticTag(Certificate nextCertificate) {
        return this.isNotEnhancedRightsCertificate(nextCertificate);
    }

    private boolean isRootCertificate(Certificate certificate) {
        return certificate.getType() == CertificateType.ROOT_CA_CERTIFICATE;
    }

    private boolean isBackendCertificate(Certificate certificate) {
        return certificate.getType() == CertificateType.BACKEND_CA_CERTIFICATE;
    }

    private boolean isNotEnhancedRightsCertificate(Certificate certificate) {
        return certificate.getType() != CertificateType.ENHANCED_RIGHTS_CERTIFICATE;
    }

    private boolean isDiagnosticCertificate(Certificate certificate) {
        return certificate.getType() == CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE;
    }

    private boolean doesNotHaveChildrenIsNotRootOrBackendOrDiag(Certificate certificate) {
        return certificate.getChildren().isEmpty() && certificate.getType() != CertificateType.BACKEND_CA_CERTIFICATE && certificate.getType() != CertificateType.ROOT_CA_CERTIFICATE && certificate.getType() != CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE;
    }

    private String getCertificateTypeFromEnum(CertificateType type) {
        switch (1.$SwitchMap$com$daimler$cebas$certificates$entity$CertificateType[type.ordinal()]) {
            case 1: {
                return LOG_BACKEND;
            }
            case 2: {
                return LOG_BACKEND_LINK;
            }
            case 3: {
                return LOG_ROOT;
            }
            case 4: {
                return LOG_ROOT_LINK;
            }
            case 5: {
                return LOG_DIAGNOSTIC;
            }
            case 6: {
                return LOG_ECU;
            }
            case 7: {
                return LOG_ENHANCED_RIGHTS;
            }
            case 8: {
                return LOG_TIME;
            }
            case 9: {
                return LOG_VARIANT_CODE_USER;
            }
            case 10: {
                return LOG_VARIANT_CODING_DEVICE;
            }
        }
        return "";
    }
}
