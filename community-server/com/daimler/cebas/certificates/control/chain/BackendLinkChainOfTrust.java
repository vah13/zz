/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder
 *  com.daimler.cebas.certificates.control.chain.ChainOfTrust
 *  com.daimler.cebas.certificates.control.config.CertificatesConfiguration
 *  com.daimler.cebas.certificates.control.repository.CertificateRepository
 *  com.daimler.cebas.certificates.control.validation.ValidationError
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.context.ApplicationEventPublisher
 */
package com.daimler.cebas.certificates.control.chain;

import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
import com.daimler.cebas.certificates.control.chain.ChainOfTrust;
import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
import com.daimler.cebas.certificates.control.repository.CertificateRepository;
import com.daimler.cebas.certificates.control.validation.ValidationError;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@CEBASControl
public class BackendLinkChainOfTrust
extends ChainOfTrust {
    private static final String CLASS_NAME = BackendLinkChainOfTrust.class.getSimpleName();

    @Autowired
    public BackendLinkChainOfTrust(CertificateRepository repo, Session session, Logger logger, ApplicationEventPublisher publisher, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
        super(repo, session, logger, publisher, i18n, profileConfiguration);
    }

    public void check(List<Certificate> rootCertificates, CertificatePrivateKeyHolder holder, List<ValidationError> errors, boolean onlyFromPKI, boolean add) {
        String METHOD_NAME = "check";
        this.logger.entering(CLASS_NAME, "check");
        boolean foundBackend = false;
        Certificate certificate = holder.getCertificate();
        for (Certificate root : rootCertificates) {
            Optional backendCertificateValidFromStore = this.getBackendCertificateValidFromStore(root, certificate, onlyFromPKI);
            if (!backendCertificateValidFromStore.isPresent()) continue;
            foundBackend = true;
            Certificate backend = (Certificate)backendCertificateValidFromStore.get();
            if (this.profileConfiguration.isImportWithoutSignatureCheck() || this.verifySignature(backend.getCertificateData(), certificate.getCertificateData())) {
                if (!add) break;
                this.addBackendLinkCertificate(certificate, errors, root);
                break;
            }
            this.errorSignatureVerification(certificate, errors);
            break;
        }
        if (!foundBackend) {
            this.errorDidNotFoundBackendBasedOnAuthorityKey(certificate, errors);
        }
        this.logger.exiting(CLASS_NAME, "check");
    }

    private void addBackendLinkCertificate(Certificate certificate, List<ValidationError> errors, Certificate root) {
        Optional<Certificate> foundAssociatedBacked = root.getChildren().stream().filter(backendCert -> backendCert.getSubjectPublicKey().trim().equals(certificate.getSubjectPublicKey().trim())).findFirst();
        if (foundAssociatedBacked.isPresent()) {
            Certificate associatedBackend = foundAssociatedBacked.get();
            if (this.repo.checkUniqueSignatureAndSPK(certificate.getUser(), certificate.getSignature(), certificate.getSubjectPublicKey())) {
                this.checkCertificateReplacement(associatedBackend, certificate);
                this.addChildToParent(associatedBackend, certificate);
            } else {
                this.errorCertificateAlreadyExist(certificate, errors);
            }
        } else {
            this.errorDidNotFindAssociatedBackend(certificate, errors);
        }
    }

    private void checkCertificateReplacement(Certificate assosciatedBackend, Certificate certificate) {
        if (!this.profileConfiguration.shouldReplaceDuringImport(certificate.getType())) return;
        this.replaceLinkCertificate(assosciatedBackend, certificate, CertificateType.BACKEND_CA_LINK_CERTIFICATE);
    }

    private void errorDidNotFindAssociatedBackend(Certificate certificate, List<ValidationError> errors) {
        this.logger.log(Level.WARNING, "000031", this.i18n.getEnglishMessage("backendNotFoundAssociatedInputLink", new String[]{certificate.getSubjectKeyIdentifier()}), ((Object)((Object)this)).getClass().getSimpleName());
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("backendNotFoundAssociatedInputLink", new String[]{certificate.getSubjectKeyIdentifier()}), "backendNotFoundAssociatedInputLink", new String[]{certificate.getSubjectKeyIdentifier()}));
    }

    private void errorCertificateAlreadyExist(Certificate certificate, List<ValidationError> errors) {
        this.logCertificateAlreadyExist(certificate);
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("certAlreadyExistsWithSubjectKey", new String[]{certificate.getSubjectKeyIdentifier()}), "certAlreadyExistsWithSubjectKey", new String[]{certificate.getSubjectKeyIdentifier()}));
    }

    private void errorSignatureVerification(Certificate certificate, List<ValidationError> errors) {
        this.logger.logWithTranslation(Level.WARNING, "000108X", "sigVerificationFailedInputLinkSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}, CLASS_NAME);
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("sigVerificationFailedInputLinkSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}), "sigVerificationFailedInputLinkSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}));
    }

    private void errorDidNotFoundBackendBasedOnAuthorityKey(Certificate certificate, List<ValidationError> errors) {
        ValidationError e = new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("backendNotFoundIssuedLinkSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}), "backendNotFoundIssuedLinkSubjKey", new String[]{certificate.getSubjectKeyIdentifier()});
        this.logDidNotFindIssuer("000035", certificate, CertificateType.BACKEND_CA_CERTIFICATE);
        errors.add(e);
    }
}
