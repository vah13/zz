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
 *  com.daimler.cebas.certificates.entity.RawData
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
import com.daimler.cebas.certificates.entity.RawData;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@CEBASControl
public class BackendChainOfTrust
extends ChainOfTrust {
    private static final String CLASS_NAME = BackendChainOfTrust.class.getSimpleName();

    @Autowired
    public BackendChainOfTrust(CertificateRepository repo, Session session, Logger logger, ApplicationEventPublisher publisher, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
        super(repo, session, logger, publisher, i18n, profileConfiguration);
    }

    public void check(List<Certificate> rootCertificate, CertificatePrivateKeyHolder holder, List<ValidationError> errors, boolean onlyFromPKI, boolean add) {
        String METHOD_NAME = "check";
        this.logger.entering(CLASS_NAME, "check");
        boolean foundRoot = false;
        Certificate certificate = holder.getCertificate();
        for (Certificate root : rootCertificate) {
            if (!root.getSubjectKeyIdentifier().equals(certificate.getAuthorityKeyIdentifier())) continue;
            foundRoot = true;
            RawData rootCertificateData = root.getCertificateData();
            RawData certificateData = certificate.getCertificateData();
            if (this.profileConfiguration.isImportWithoutSignatureCheck() || this.verifySignature(rootCertificateData, certificateData)) {
                if (!add) break;
                this.addBackendCertificate(certificate, errors, root);
                break;
            }
            this.errorSignatureVerification(certificate, errors);
            break;
        }
        if (!foundRoot) {
            this.errorDidNotfindRootIssuerBasedOnAuthorityKey(certificate, errors);
        }
        this.logger.exiting(CLASS_NAME, "check");
    }

    private void addBackendCertificate(Certificate certificate, List<ValidationError> errors, Certificate root) {
        if (this.repo.checkUniqueSignatureAndSPK(certificate.getUser(), certificate.getSignature(), certificate.getSubjectPublicKey())) {
            certificate = this.profileConfiguration.getPkiKnownHandler().updateBackendPkiKnown(certificate);
            this.addChildToParent(root, certificate);
        } else {
            if (this.profileConfiguration.getPkiKnownHandler().updateBackendPkiKnownOnIdentical(certificate)) return;
            this.errorCertificateAlreadyExist(certificate, errors);
        }
    }

    private void errorCertificateAlreadyExist(Certificate certificate, List<ValidationError> errors) {
        if (StringUtils.isEmpty(certificate.getZkNo())) {
            this.logCertificateAlreadyExist(certificate);
            errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("certAlreadyExistsWithSubjectKey", new String[]{certificate.getSubjectKeyIdentifier()}), "certAlreadyExistsWithSubjectKey", new String[]{certificate.getSubjectKeyIdentifier()}));
        } else {
            this.logger.log(Level.INFO, "000016", this.i18n.getEnglishMessage("certAlreadyExistsWithSubjectKeyAndZkNo", new String[]{certificate.getSubjectKeyIdentifier(), certificate.getZkNo()}), CLASS_NAME);
            errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("certAlreadyExistsWithSubjectKeyAndZkNo", new String[]{certificate.getSubjectKeyIdentifier(), certificate.getZkNo()}), "certAlreadyExistsWithSubjectKeyAndZkNo", new String[]{certificate.getSubjectKeyIdentifier(), certificate.getZkNo()}));
        }
    }

    private void errorDidNotfindRootIssuerBasedOnAuthorityKey(Certificate certificate, List<ValidationError> errors) {
        ValidationError error = new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("rootNotFoundWhichCouldHaveIssuedBackend", new String[]{certificate.getSubjectKeyIdentifier()}), "rootNotFoundWhichCouldHaveIssuedBackend", new String[]{certificate.getSubjectKeyIdentifier()});
        errors.add(error);
        this.logDidNotFindIssuer("000106", certificate, CertificateType.ROOT_CA_CERTIFICATE);
    }

    private void errorSignatureVerification(Certificate certificate, List<ValidationError> errors) {
        this.logger.logWithTranslation(Level.WARNING, "000027", "sigVerificationFailedBackendSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}, CLASS_NAME);
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("sigVerificationFailedBackendSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}), "sigVerificationFailedBackendSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}));
    }
}
