/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.CertificateCryptoEngine
 *  com.daimler.cebas.certificates.control.SearchEngine
 *  com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder
 *  com.daimler.cebas.certificates.control.chain.ChainOfTrust
 *  com.daimler.cebas.certificates.control.chain.Uniqueness
 *  com.daimler.cebas.certificates.control.config.CertificatesConfiguration
 *  com.daimler.cebas.certificates.control.repository.CertificateRepository
 *  com.daimler.cebas.certificates.control.validation.CertificatesValidator
 *  com.daimler.cebas.certificates.control.validation.ValidationError
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.entity.RawData
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.common.entity.AbstractEntity
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  com.daimler.cebas.users.entity.User
 *  com.daimler.cebas.users.entity.UserKeyPair
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.context.ApplicationEventPublisher
 */
package com.daimler.cebas.certificates.control.chain;

import com.daimler.cebas.certificates.CertificateCryptoEngine;
import com.daimler.cebas.certificates.control.SearchEngine;
import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
import com.daimler.cebas.certificates.control.chain.ChainOfTrust;
import com.daimler.cebas.certificates.control.chain.Uniqueness;
import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
import com.daimler.cebas.certificates.control.repository.CertificateRepository;
import com.daimler.cebas.certificates.control.validation.CertificatesValidator;
import com.daimler.cebas.certificates.control.validation.ValidationError;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.entity.RawData;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.common.entity.AbstractEntity;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import com.daimler.cebas.users.entity.User;
import com.daimler.cebas.users.entity.UserKeyPair;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@CEBASControl
public class UnderBackendChainOfTrust
extends ChainOfTrust {
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(CertificatesValidator.class.getName());
    private static final String CLASS_NAME = UnderBackendChainOfTrust.class.getSimpleName();
    private SearchEngine searchEngine;

    @Autowired
    public UnderBackendChainOfTrust(CertificateRepository repo, Session session, Logger logger, ApplicationEventPublisher publisher, MetadataManager i18n, CertificatesConfiguration profileConfiguration, SearchEngine searchEngine) {
        super(repo, session, logger, publisher, i18n, profileConfiguration);
        this.searchEngine = searchEngine;
    }

    public void check(List<Certificate> userStoreRootCertificate, CertificatePrivateKeyHolder holder, List<ValidationError> errors, boolean onlyFromPKI, boolean add) {
        String METHOD_NAME = "check";
        this.logger.entering(CLASS_NAME, "check");
        boolean foundBackend = false;
        Certificate certificate = holder.getCertificate();
        for (Certificate root : userStoreRootCertificate) {
            Optional backendCertificateValidFromStore = this.getBackendCertificateValidFromStore(root, certificate, onlyFromPKI);
            if (!backendCertificateValidFromStore.isPresent()) continue;
            foundBackend = true;
            Certificate backend = (Certificate)backendCertificateValidFromStore.get();
            RawData backendCertificateData = backend.getCertificateData();
            RawData certificateData = certificate.getCertificateData();
            if (backendCertificateData.isCertificate()) {
                if (this.profileConfiguration.isImportWithoutSignatureCheck() || this.verifySignature(backendCertificateData, certificateData)) {
                    if (!add) break;
                    this.addUnderBackend(holder, errors, backend);
                    break;
                }
                this.errorSignatureVerification(certificate, errors);
                break;
            }
            this.errorBackendNotFoundAsIssuer(certificate, errors);
            break;
        }
        if (!foundBackend) {
            this.errorBackendNotFoundBaseOnAuthorityKey(certificate, errors);
        }
        this.logger.exiting(CLASS_NAME, "check");
    }

    private void addUnderBackend(CertificatePrivateKeyHolder holder, List<ValidationError> errors, Certificate backend) {
        Certificate certificate = holder.getCertificate();
        if (this.profileConfiguration.isUnique(certificate, this.logger)) {
            this.profileConfiguration.getAddBackendHandler(this, this.searchEngine).addUnderBackend(holder, errors, backend);
        } else {
            this.errorCertificateAlreadyExist(certificate, errors);
        }
    }

    protected boolean checkEcuCertAndCsrExists(List<ValidationError> errors, Certificate backend, Certificate certificate) {
        if (certificate.getType() != CertificateType.ECU_CERTIFICATE && this.profileConfiguration.checkCSRExists()) {
            this.errorDidNotFindCSR(certificate, errors);
            return false;
        }
        if (this.profileConfiguration.shouldReplaceECUCertificate() && this.profileConfiguration.shouldReplaceDuringImport(certificate.getType())) {
            this.replaceEcuCertificate(certificate);
        }
        this.add(backend, certificate);
        return true;
    }

    protected boolean addCertificateWithCSR(CertificatePrivateKeyHolder holder, List<ValidationError> errors, Certificate certificate, Certificate csrCertificate, boolean extendedValidation) {
        if (csrCertificate.getType() != certificate.getType()) {
            this.logCsrTypeDoesNotMatchCertificateType(certificate, csrCertificate);
        }
        this.checkIdenticalWithDifferences(holder);
        PrivateKey privateKey = this.getPrivateKeyFromKeyPair(certificate.getUser(), csrCertificate);
        if (extendedValidation) {
            if (!this.matchPublicKeys(certificate, privateKey, errors)) return false;
        }
        RawData certificateData = certificate.getCertificateData();
        csrCertificate.updateCSR(certificateData.getCert(), certificate.getCertificateData().getOriginalBytes());
        holder.setInternalId(csrCertificate.getEntityId());
        return true;
    }

    protected boolean addCertificateWithPrivateKey(CertificatePrivateKeyHolder holder, List<ValidationError> errors, Certificate backend, Certificate certificate, boolean extendedValidation) {
        this.checkIdenticalWithDifferences(holder);
        User user = certificate.getUser();
        if (extendedValidation) {
            if (!this.matchPublicKeys(certificate, holder.getPrivateKey(), errors)) return false;
        }
        UserKeyPair userKeyPair = this.createUserKeyPair(certificate, holder.getPrivateKey(), user);
        user.getKeyPairs().add(userKeyPair);
        this.add(backend, certificate);
        this.repo.create((AbstractEntity)userKeyPair);
        return true;
    }

    private void add(Certificate backend, Certificate certificate) {
        certificate.setParent(backend);
        this.repo.create((AbstractEntity)certificate);
    }

    private void checkIdenticalWithDifferences(CertificatePrivateKeyHolder holder) {
        Certificate certificate = holder.getCertificate();
        if (certificate.getType() == CertificateType.ECU_CERTIFICATE) {
            if (!this.profileConfiguration.shouldReplaceECUCertificate()) return;
        }
        if (!this.profileConfiguration.shouldReplaceDuringImport(certificate.getType())) return;
        List identical = this.searchEngine.findIdentical(certificate);
        List identicalWithDifferences = Uniqueness.findIdenticalWithDifferences((List)identical, (Certificate)certificate, (Logger)this.logger);
        holder.addPossibleReplaceableCertificate(identicalWithDifferences);
        this.deleteCertificates(identicalWithDifferences);
        this.logReplacedCertificate(identicalWithDifferences, certificate);
    }

    private UserKeyPair createUserKeyPair(Certificate cert, PrivateKey privateKey, User user) {
        String publicKey = cert.getSubjectPublicKey();
        String encodedPrivateKey = this.session.getCryptoEngine().encodePrivateKey(this.session.getContainerKey(), privateKey.getEncoded());
        return new UserKeyPair(publicKey, encodedPrivateKey, user, cert);
    }

    private boolean matchPublicKeys(Certificate certificate, PrivateKey privateKey, List<ValidationError> errors) {
        try {
            PublicKey calculatedPublicKey = CertificateCryptoEngine.calculateEDDSAFromEdDSAPrivate((PrivateKey)privateKey);
            String publicKeyFromPrivate = this.session.getCryptoEngine().getPublicKeyDataHex(calculatedPublicKey);
            boolean pkMatch = certificate.getSubjectPublicKey().equals(publicKeyFromPrivate);
            if (pkMatch) return pkMatch;
            this.errorPublicKeyDoesNotMatchPrivateKey(certificate, publicKeyFromPrivate, errors);
            return pkMatch;
        }
        catch (GeneralSecurityException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            this.errorPublicKeyDoesNotMatchPrivateKey(certificate, "", errors);
            return false;
        }
    }

    private PrivateKey getPrivateKeyFromKeyPair(User user, Certificate csrCertificate) {
        UserKeyPair userKeyPair = this.searchEngine.getUserKeyPairForCertificate(user, csrCertificate);
        byte[] decodedPrivateKey = this.session.getCryptoEngine().decodePrivateKeyToByteArray(this.session.getContainerKey(), userKeyPair.getPrivateKey());
        return CertificateCryptoEngine.generatePrivateKey((byte[])decodedPrivateKey);
    }

    private void errorDidNotFindCSR(Certificate certificate, List<ValidationError> errors) {
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("csrAndPrivateKeyNotFoundForSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}), "csrAndPrivateKeyNotFoundForSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}));
        this.logger.log(Level.WARNING, "000026", this.i18n.getEnglishMessage("csrAndPrivateKeyNotFoundForSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}), ((Object)((Object)this)).getClass().getSimpleName());
    }

    protected void errorHasPrivateKeyAndFoundCSR(Certificate certificate, List<ValidationError> errors) {
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("csrAndPrivateKeyFoundForSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}), "csrAndPrivateKeyFoundForSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}));
        this.logger.log(Level.WARNING, "000026", this.i18n.getEnglishMessage("csrAndPrivateKeyFoundForSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}), ((Object)((Object)this)).getClass().getSimpleName());
    }

    private void errorCertificateAlreadyExist(Certificate certificate, List<ValidationError> errors) {
        this.logCertificateAlreadyExist(certificate);
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("certAlreadyExistsWithSubjectKey", new String[]{certificate.getSubjectKeyIdentifier()}), "certAlreadyExistsWithSubjectKey", new String[]{certificate.getSubjectKeyIdentifier()}));
    }

    private void errorPublicKeyDoesNotMatchPrivateKey(Certificate certificate, String publicKeyFromPrivate, List<ValidationError> errors) {
        this.logger.log(Level.WARNING, "000029X", this.i18n.getEnglishMessage("certPublicKeyDoesntMatchPublicKeyFromPrivateKey", new String[]{certificate.getSubjectPublicKey(), publicKeyFromPrivate, certificate.getSubjectKeyIdentifier()}), CLASS_NAME);
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("certPublicKeyDoesntMatchPublicKeyFromPrivateKey", new String[]{certificate.getSubjectPublicKey(), publicKeyFromPrivate, certificate.getSubjectKeyIdentifier()}), "certPublicKeyDoesntMatchPublicKeyFromPrivateKey", new String[]{certificate.getSubjectPublicKey(), publicKeyFromPrivate, certificate.getSubjectKeyIdentifier()}));
    }

    private void errorBackendNotFoundAsIssuer(Certificate certificate, List<ValidationError> errors) {
        this.logDidNotFindIssuer("000111X", certificate, CertificateType.BACKEND_CA_CERTIFICATE);
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("backendNotFoundSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}), "backendNotFoundSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}));
    }

    private void errorBackendNotFoundBaseOnAuthorityKey(Certificate certificate, List<ValidationError> errors) {
        ValidationError error = new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("backendNotFoundIssuedSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}), "backendNotFoundIssuedSubjKey", new String[]{certificate.getSubjectKeyIdentifier()});
        errors.add(error);
        this.logDidNotFindIssuer("000112X", certificate, CertificateType.BACKEND_CA_CERTIFICATE);
    }

    private void errorSignatureVerification(Certificate certificate, List<ValidationError> errors) {
        this.logger.logWithTranslation(Level.WARNING, "000030", "sigVerificationFailedSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}, CLASS_NAME);
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("sigVerificationFailedSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}), "sigVerificationFailedSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}));
    }

    private void logCsrTypeDoesNotMatchCertificateType(Certificate certificate, Certificate csrCertficate) {
        this.logger.log(Level.WARNING, "000053", this.i18n.getEnglishMessage("csrTypeNotMatchCertType", new String[]{csrCertficate.getType().getText(), certificate.getType().getText(), certificate.getSubjectKeyIdentifier()}), ((Object)((Object)this)).getClass().getSimpleName());
    }

    protected void errorCertificateWithSameAuthKeyAndSerialNumber(Certificate certificate, List<ValidationError> errors) {
        this.logger.log(Level.WARNING, "000022X", this.i18n.getEnglishMessage("certAlreadyExistsWithAuthKeyAndSN", new String[]{certificate.getAuthorityKeyIdentifier(), certificate.getSerialNo()}), CLASS_NAME);
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("certAlreadyExistsWithAuthKeyAndSN", new String[]{certificate.getAuthorityKeyIdentifier(), certificate.getSerialNo()}), "certAlreadyExistsWithAuthKeyAndSN", new String[]{certificate.getAuthorityKeyIdentifier(), certificate.getSerialNo()}));
    }

    private void replaceEcuCertificate(Certificate certificateToBeImported) {
        List identical = this.searchEngine.findIdentical(certificateToBeImported);
        List identicalWithDifferences = Uniqueness.findIdenticalWithDifferences((List)identical, (Certificate)certificateToBeImported, (Logger)this.logger);
        this.deleteCertificates(identicalWithDifferences);
        this.logReplacedCertificate(identicalWithDifferences, certificateToBeImported);
    }
}
