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
public class RootLinkChainOfTrust
extends ChainOfTrust {
    private static final String CLASS_NAME = RootLinkChainOfTrust.class.getSimpleName();

    @Autowired
    public RootLinkChainOfTrust(CertificateRepository repo, Session session, Logger logger, ApplicationEventPublisher publisher, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
        super(repo, session, logger, publisher, i18n, profileConfiguration);
    }

    public void check(List<Certificate> rootCertificates, CertificatePrivateKeyHolder holder, List<ValidationError> errors, boolean onlyFromPKI, boolean add) {
        String METHOD_NAME = "check";
        this.logger.entering(CLASS_NAME, "check");
        Certificate certificate = holder.getCertificate();
        Optional rootCertificateValidFromStore = this.getRootCertificateValidFromStore(rootCertificates, certificate);
        if (rootCertificateValidFromStore.isPresent()) {
            Certificate rootLinked = (Certificate)rootCertificateValidFromStore.get();
            if (rootLinked.getSubject().equals(certificate.getIssuer())) {
                if (this.profileConfiguration.isImportWithoutSignatureCheck() || this.verifySignature(rootLinked.getCertificateData(), certificate.getCertificateData())) {
                    if (add) {
                        this.addRootLinkCertificate(rootCertificates, certificate, errors);
                    }
                } else {
                    this.errorSignatureVerification(certificate, errors);
                }
            } else {
                this.errorDidNotFindRootIssuer(certificate, errors);
            }
        } else {
            this.errorDidNotFindRootIssuer(certificate, errors);
        }
        this.logger.exiting(CLASS_NAME, "check");
    }

    private void addRootLinkCertificate(List<Certificate> rootCertificate, Certificate certificate, List<ValidationError> errors) {
        Optional<Certificate> foundAsociatedRoot = rootCertificate.stream().filter(root -> root.getSubjectPublicKey().trim().equals(certificate.getSubjectPublicKey().trim())).findFirst();
        if (foundAsociatedRoot.isPresent()) {
            Certificate rootAsociated = foundAsociatedRoot.get();
            if (this.profileConfiguration.isUnique(certificate, this.logger)) {
                this.checkCertificateReplacement(rootAsociated, certificate);
                this.addChildToParent(rootAsociated, certificate);
            } else {
                this.errorCertificateAlreadyExist(certificate, errors);
            }
        } else {
            this.errorDidNotFindAssociatedRoot(certificate, errors);
        }
    }

    private void checkCertificateReplacement(Certificate rootAsociated, Certificate certificate) {
        if (!this.profileConfiguration.shouldReplaceDuringImport(certificate.getType())) return;
        this.replaceLinkCertificate(rootAsociated, certificate, CertificateType.ROOT_CA_LINK_CERTIFICATE);
    }

    private void errorDidNotFindRootIssuer(Certificate certificate, List<ValidationError> errors) {
        this.logDidNotFindIssuer("000109X", certificate, CertificateType.ROOT_CA_CERTIFICATE);
        ValidationError error = new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("rootNotFoundIssuedLinkSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}), "rootNotFoundIssuedLinkSubjKey", new String[]{certificate.getSubjectKeyIdentifier()});
        errors.add(error);
    }

    private void errorSignatureVerification(Certificate certificate, List<ValidationError> errors) {
        this.logger.logWithTranslation(Level.WARNING, "000110X", "sigVerificationFailedRootLinkSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}, CLASS_NAME);
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("sigVerificationFailedRootLinkSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}), "sigVerificationFailedRootLinkSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}));
    }

    private void errorDidNotFindAssociatedRoot(Certificate certificate, List<ValidationError> errors) {
        this.logger.log(Level.WARNING, "000025", this.i18n.getEnglishMessage("rootNotFoundAssociatedInputLinkSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}), ((Object)((Object)this)).getClass().getSimpleName());
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("rootNotFoundAssociatedInputLinkSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}), "rootNotFoundAssociatedInputLinkSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}));
    }

    private void errorCertificateAlreadyExist(Certificate certificate, List<ValidationError> errors) {
        this.logCertificateAlreadyExist(certificate);
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("certAlreadyExistsWithSubjectKey", new String[]{certificate.getSubjectKeyIdentifier()}), "certAlreadyExistsWithSubjectKey", new String[]{certificate.getSubjectKeyIdentifier()}));
    }
}
