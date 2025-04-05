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
 *  com.daimler.cebas.certificates.entity.parsing.CertificateParser
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.SecurityProviders
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.common.entity.AbstractEntity
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
import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.SecurityProviders;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.common.entity.AbstractEntity;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@CEBASControl
public class RootChainOfTrust
extends ChainOfTrust {
    private static final String CLASS_NAME = RootChainOfTrust.class.getSimpleName();
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(CertificateParser.class.getName());

    @Autowired
    public RootChainOfTrust(CertificateRepository repo, Session session, Logger logger, ApplicationEventPublisher publisher, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
        super(repo, session, logger, publisher, i18n, profileConfiguration);
    }

    public void check(List<Certificate> userStoreRootCertificate, CertificatePrivateKeyHolder holder, List<ValidationError> errors, boolean onlyFromPKI, boolean add) {
        String METHOD_NAME = "check";
        this.logger.entering(CLASS_NAME, "check");
        Certificate certificate = holder.getCertificate();
        if (this.profileConfiguration.isImportWithoutSignatureCheck() && add) {
            this.addCertificate(userStoreRootCertificate, errors, certificate);
        } else {
            try {
                X509Certificate cert = certificate.getCertificateData().getCert();
                cert.verify(cert.getPublicKey(), SecurityProviders.SNED25519.name());
                if (add) {
                    this.addCertificate(userStoreRootCertificate, errors, certificate);
                }
            }
            catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException | CertificateException e) {
                LOG.log(Level.FINEST, e.getMessage(), e);
                this.errorSignatureVerification(certificate, errors);
            }
        }
        this.logger.exiting(CLASS_NAME, "check");
    }

    private void addCertificate(List<Certificate> userStoreRootCertificate, List<ValidationError> errors, Certificate certificate) {
        if (this.repo.checkUniqueSignatureAndSPK(certificate.getUser(), certificate.getSignature(), certificate.getSubjectPublicKey())) {
            userStoreRootCertificate.add(certificate);
            this.repo.create((AbstractEntity)certificate);
        } else {
            this.errorCertificateAlreadyExist(certificate, errors);
        }
    }

    private void errorSignatureVerification(Certificate certificate, List<ValidationError> errors) {
        this.logger.logWithTranslation(Level.WARNING, "000028", "sigVerificationFailedRootSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}, CLASS_NAME);
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("sigVerificationFailedRootSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}), "sigVerificationFailedRootSubjKey", new String[]{certificate.getSubjectKeyIdentifier()}));
    }

    private void errorCertificateAlreadyExist(Certificate certificate, List<ValidationError> errors) {
        this.logCertificateAlreadyExist(certificate);
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("certAlreadyExistsWithSubjectKey", new String[]{certificate.getSubjectKeyIdentifier()}), "certAlreadyExistsWithSubjectKey", new String[]{certificate.getSubjectKeyIdentifier()}));
    }
}
