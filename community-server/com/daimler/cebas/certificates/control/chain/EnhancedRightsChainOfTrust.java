/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.CertificateUtil
 *  com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder
 *  com.daimler.cebas.certificates.control.chain.ChainOfTrust
 *  com.daimler.cebas.certificates.control.chain.Uniqueness
 *  com.daimler.cebas.certificates.control.config.CertificatesConfiguration
 *  com.daimler.cebas.certificates.control.repository.CertificateRepository
 *  com.daimler.cebas.certificates.control.validation.ValidationError
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateState
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.entity.RawData
 *  com.daimler.cebas.common.control.HexUtil
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  org.bouncycastle.cert.AttributeCertificateHolder
 *  org.bouncycastle.cert.X509AttributeCertificateHolder
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.context.ApplicationEventPublisher
 */
package com.daimler.cebas.certificates.control.chain;

import com.daimler.cebas.certificates.control.CertificateUtil;
import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
import com.daimler.cebas.certificates.control.chain.ChainOfTrust;
import com.daimler.cebas.certificates.control.chain.Uniqueness;
import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
import com.daimler.cebas.certificates.control.repository.CertificateRepository;
import com.daimler.cebas.certificates.control.validation.ValidationError;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateState;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.entity.RawData;
import com.daimler.cebas.common.control.HexUtil;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cert.AttributeCertificateHolder;
import org.bouncycastle.cert.X509AttributeCertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@CEBASControl
public class EnhancedRightsChainOfTrust
extends ChainOfTrust {
    private static final String CLASS_NAME = EnhancedRightsChainOfTrust.class.getSimpleName();

    @Autowired
    public EnhancedRightsChainOfTrust(CertificateRepository repo, Session session, Logger logger, ApplicationEventPublisher publisher, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
        super(repo, session, logger, publisher, i18n, profileConfiguration);
    }

    public void check(List<Certificate> userStoreRootCertificate, CertificatePrivateKeyHolder holder, List<ValidationError> errors, boolean onlyFromPKI, boolean add) {
        String METHOD_NAME = "check";
        this.logger.entering(CLASS_NAME, "check");
        boolean backendFound = false;
        Certificate certificate = holder.getCertificate();
        for (Certificate root : userStoreRootCertificate) {
            Optional backendCertificateValidFromStore = this.getBackendCertificateValidFromStore(root, certificate, onlyFromPKI);
            if (!backendCertificateValidFromStore.isPresent()) continue;
            backendFound = true;
            Certificate backend = (Certificate)backendCertificateValidFromStore.get();
            RawData certificateData = certificate.getCertificateData();
            List children = this.repo.findByTypeAndParent(Certificate.class, certificate.getUser(), CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE, backend);
            Optional<Certificate> diagOptional = children.stream().filter(cert -> this.matchingDiagCertificate(certificate, (Certificate)cert)).findFirst();
            if (!diagOptional.isPresent()) {
                this.errorDidNotFondDiagnosticCertificate(certificate, errors);
                return;
            }
            if (this.repo.checkUniqueSignatureAndSPK(certificate.getUser(), certificate.getSignature(), certificate.getSubjectPublicKey())) {
                certificate.setParent(diagOptional.get());
                if (this.profileConfiguration.isImportWithoutSignatureCheck() || this.verifySignature(backend.getCertificateData(), certificateData)) {
                    if (!add) break;
                    Certificate diagnosticCertificate = diagOptional.get();
                    this.checkIfExistsCSRAndAddCertificate(holder, certificate, diagnosticCertificate);
                    break;
                }
                this.errorSignatureVerification(certificate, errors);
                break;
            }
            this.errorCertificateAlreadyExist(certificate, errors);
            break;
        }
        if (!backendFound) {
            this.errorDidNotFindBackendBasedOnAuthorityKey(certificate, errors);
        }
        this.logger.exiting(CLASS_NAME, "check");
    }

    private void addCertificate(CertificatePrivateKeyHolder holder, Certificate certificate, Certificate diagnosticCertificate) {
        this.checkIdenticalWithDifferences(holder, certificate, diagnosticCertificate);
        this.addChildToParent(diagnosticCertificate, certificate);
        certificate.setBaseCertificateIDForEnh();
    }

    private void checkIfExistsCSRAndAddCertificate(CertificatePrivateKeyHolder holder, Certificate certificate, Certificate diagnosticCertificate) {
        Optional<Certificate> csrOptional = this.findCSR(certificate, diagnosticCertificate);
        if (csrOptional.isPresent()) {
            Certificate csrCertificate = csrOptional.get();
            this.checkIdenticalWithDifferences(holder, certificate, diagnosticCertificate);
            RawData certificateData = certificate.getCertificateData();
            csrCertificate.updateCSR(certificateData.getAttributesCertificateHolder(), certificateData.getOriginalBytes());
            csrCertificate.setBaseCertificateIDForEnh();
        } else {
            this.addCertificate(holder, certificate, diagnosticCertificate);
        }
    }

    private void checkIdenticalWithDifferences(CertificatePrivateKeyHolder holder, Certificate certificate, Certificate diagnosticCertificate) {
        if (!this.profileConfiguration.shouldReplaceDuringImport(certificate.getType())) return;
        List identicalWithDifferences = Uniqueness.findIdenticalWithDifferences((List)diagnosticCertificate.getChildren(), (Certificate)certificate, (Logger)this.logger);
        holder.addPossibleReplaceableCertificate(identicalWithDifferences);
        this.deleteCertificates(identicalWithDifferences);
        this.logReplacedCertificate(identicalWithDifferences, certificate);
    }

    private void errorCertificateAlreadyExist(Certificate certificate, List<ValidationError> errors) {
        this.logCertificateAlreadyExist(certificate);
        CertificateType certificateType = CertificateType.getTypeForLogging((Certificate)certificate);
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("enhRightsAlreadyExistsWithSignature", new String[]{this.i18n.getMessage(certificateType.getLanguageProperty()), certificate.getSignature()}), "enhRightsAlreadyExistsWithSignature", new String[]{this.i18n.getEnglishMessage(certificateType.getLanguageProperty()), certificate.getSignature()}));
    }

    private void errorDidNotFondDiagnosticCertificate(Certificate certificate, List<ValidationError> errors) {
        CertificateType certificateType = CertificateType.getTypeForLogging((Certificate)certificate);
        String className = ((Object)((Object)this)).getClass().getSimpleName();
        this.logger.log(Level.WARNING, "000024", this.i18n.getEnglishMessage("diagNotFoundForInputEnhRights", new String[]{this.i18n.getEnglishMessage(certificateType.getLanguageProperty()), certificate.getSignature()}), className);
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("diagNotFoundForInputEnhRights", new String[]{this.i18n.getMessage(certificateType.getLanguageProperty()), certificate.getSignature()}), "diagNotFoundForInputEnhRights", new String[]{this.i18n.getEnglishMessage(certificateType.getLanguageProperty()), certificate.getSignature()}));
        this.logHolderNotFound(certificate);
    }

    private void logHolderNotFound(Certificate certificate) {
        X509AttributeCertificateHolder attributesCertificateHolder = certificate.getCertificateData().getAttributesCertificateHolder();
        String issuerHolder = attributesCertificateHolder.getHolder().getIssuer()[0].toString();
        BigInteger serialNoHolder = attributesCertificateHolder.getHolder().getSerialNumber();
        String serialNoHolderHex = HexUtil.bytesToHex((byte[])serialNoHolder.toByteArray());
        String message = MessageFormat.format("Cannot import enhanced rights due to unknown holder {0} and issuer {1}", serialNoHolderHex, issuerHolder);
        this.logger.log(Level.WARNING, "000024", message, ((Object)((Object)this)).getClass().getSimpleName());
    }

    private void errorSignatureVerification(Certificate certificate, List<ValidationError> errors) {
        this.logger.logWithTranslation(Level.WARNING, "000106X", "sigVerificationFailedForEnhRightsWithSigKey", new String[]{certificate.getSignature()}, CLASS_NAME);
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("sigVerificationFailedForEnhRightsWithSigKey", new String[]{certificate.getSignature()}), "sigVerificationFailedForEnhRightsWithSigKey", new String[]{certificate.getSignature()}));
    }

    private void errorDidNotFindBackendBasedOnAuthorityKey(Certificate certificate, List<ValidationError> errors) {
        CertificateType certificateType = CertificateType.getTypeForLogging((Certificate)certificate);
        ValidationError error = new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("backendNotFoundIssuedDiagCertForEnhRights", new String[]{this.i18n.getMessage(certificateType.getLanguageProperty()), certificate.getSignature()}), "backendNotFoundIssuedDiagCertForEnhRights", new String[]{this.i18n.getEnglishMessage(certificateType.getLanguageProperty()), certificate.getSignature()});
        errors.add(error);
        this.logger.log(Level.WARNING, "000107X", this.i18n.getEnglishMessage("backendNotFoundIssuedDiagCertForEnhRights", new String[]{this.i18n.getEnglishMessage(certificateType.getLanguageProperty()), certificate.getSignature()}), ((Object)((Object)this)).getClass().getSimpleName());
        this.logHolderNotFound(certificate);
    }

    private boolean matchingDiagCertificate(Certificate ehhRightsCertificates, Certificate cert) {
        boolean match = false;
        if (!cert.getState().equals((Object)CertificateState.ISSUED)) return match;
        X509Certificate diagCertificate = cert.getCertificateData().getCert();
        if (diagCertificate == null) {
            this.logger.log(Level.WARNING, "000024", "Diagnostic[" + cert.getSerialNo() + "] type does not contain X509 data. It is skipped in lookup.", ((Object)((Object)this)).getClass().getSimpleName());
            return false;
        }
        BigInteger serialNumberDiagnostic = diagCertificate.getSerialNumber();
        X509AttributeCertificateHolder x509AttributesCertificateHolder = ehhRightsCertificates.getCertificateData().getAttributesCertificateHolder();
        AttributeCertificateHolder holder = x509AttributesCertificateHolder.getHolder();
        BigInteger serialNumberDiagEhh = holder.getSerialNumber();
        String issuerDiag = diagCertificate.getIssuerX500Principal().getName();
        String issuerEhh = holder.getIssuer()[0].toString();
        String sigAlgNameDiag = diagCertificate.getSigAlgName();
        String sigAlgEhh = x509AttributesCertificateHolder.getSignatureAlgorithm().getAlgorithm().toString();
        match = serialNumberDiagnostic.equals(serialNumberDiagEhh) && StringUtils.equals(issuerDiag, issuerEhh) && StringUtils.equals(sigAlgNameDiag, sigAlgEhh);
        return match;
    }

    public Optional<Certificate> findCSR(Certificate certificateToAdd, Certificate diagnosticCertificate) {
        String METHOD_NAME = "findCSR";
        this.logger.entering(CLASS_NAME, "findCSR");
        Optional<Certificate> found = diagnosticCertificate.getChildren().stream().filter(certificate -> certificate.getState() == CertificateState.SIGNING_REQUEST && !certificate.getCertificateData().isCertificate() && certificate.getAuthorityKeyIdentifier().equals(certificateToAdd.getAuthorityKeyIdentifier()) && certificate.getIssuerSerialNumber().equals(certificateToAdd.getIssuerSerialNumber()) && certificate.getIssuer().equals(certificateToAdd.getIssuer()) && certificate.getTargetSubjectKeyIdentifier().equals(certificateToAdd.getTargetSubjectKeyIdentifier()) && CertificateUtil.compareCommaSeparatedLists((String)certificate.getTargetECU(), (String)certificateToAdd.getTargetECU()) && CertificateUtil.compareCommaSeparatedLists((String)certificate.getTargetVIN(), (String)certificateToAdd.getTargetVIN())).findFirst();
        this.logger.exiting(CLASS_NAME, "findCSR");
        return found;
    }
}
