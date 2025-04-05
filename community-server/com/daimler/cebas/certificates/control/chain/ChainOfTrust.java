/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.chain.IChainOfTrust
 *  com.daimler.cebas.certificates.control.chain.Uniqueness
 *  com.daimler.cebas.certificates.control.chain.events.CertificatesDeleteEvent
 *  com.daimler.cebas.certificates.control.config.CertificatesConfiguration
 *  com.daimler.cebas.certificates.control.repository.CertificateRepository
 *  com.daimler.cebas.certificates.control.validation.CertificatesValidator
 *  com.daimler.cebas.certificates.control.validation.ValidationError
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.entity.RawData
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.entity.AbstractEntity
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  org.springframework.context.ApplicationEvent
 *  org.springframework.context.ApplicationEventPublisher
 */
package com.daimler.cebas.certificates.control.chain;

import com.daimler.cebas.certificates.control.chain.IChainOfTrust;
import com.daimler.cebas.certificates.control.chain.Uniqueness;
import com.daimler.cebas.certificates.control.chain.events.CertificatesDeleteEvent;
import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
import com.daimler.cebas.certificates.control.repository.CertificateRepository;
import com.daimler.cebas.certificates.control.validation.CertificatesValidator;
import com.daimler.cebas.certificates.control.validation.ValidationError;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.entity.RawData;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.entity.AbstractEntity;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

public abstract class ChainOfTrust
implements IChainOfTrust {
    private static final String CLASS_NAME = ChainOfTrust.class.getSimpleName();
    protected Logger logger;
    protected ApplicationEventPublisher publisher;
    protected CertificateRepository repo;
    protected Session session;
    protected MetadataManager i18n;
    protected CertificatesConfiguration profileConfiguration;

    public ChainOfTrust(CertificateRepository repo, Session session, Logger logger, ApplicationEventPublisher publisher, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
        this.repo = repo;
        this.session = session;
        this.logger = logger;
        this.publisher = publisher;
        this.i18n = i18n;
        this.profileConfiguration = profileConfiguration;
    }

    public static Optional<Certificate> getParentOfLinkCertificate(List<Certificate> possibleParents, Certificate rootLink) {
        return possibleParents.stream().filter(cert -> cert.getSubjectKeyIdentifier().equals(rootLink.getAuthorityKeyIdentifier())).findFirst();
    }

    protected boolean verifySignature(RawData parentCertificateData, RawData certificateData) {
        String METHOD_NAME = "verifySignature";
        this.logger.entering(CLASS_NAME, "verifySignature");
        boolean verify = CertificatesValidator.verifySignature((RawData)parentCertificateData, (RawData)certificateData, (Logger)this.logger);
        this.logger.exiting(CLASS_NAME, "verifySignature");
        return verify;
    }

    protected Optional<Certificate> getBackendCertificateValidFromStore(Certificate rootCertificate, Certificate certificateToAdd, boolean onlyFromPKI) {
        String METHOD_NAME = "getBackendCertificateValidFromStore";
        this.logger.entering(CLASS_NAME, "getBackendCertificateValidFromStore");
        List backendCertificates = rootCertificate.getChildren();
        Optional<Certificate> backendCertificateOptional = backendCertificates.stream().filter(backend -> backend.getSubjectKeyIdentifier().equals(certificateToAdd.getAuthorityKeyIdentifier()) && (!onlyFromPKI || this.profileConfiguration.getPkiKnownHandler().isPKIKnown(backend))).findFirst();
        this.logger.exiting(CLASS_NAME, "getBackendCertificateValidFromStore");
        return backendCertificateOptional;
    }

    protected Optional<Certificate> getRootCertificateValidFromStore(List<Certificate> rootCertificate, Certificate certificateToAdd) {
        String METHOD_NAME = "getRootCertificateValidFromStore";
        this.logger.entering(CLASS_NAME, "getRootCertificateValidFromStore");
        Optional<Certificate> rootCertificateOptional = rootCertificate.stream().filter(backend -> backend.getSubjectKeyIdentifier().equals(certificateToAdd.getAuthorityKeyIdentifier())).findFirst();
        this.logger.exiting(CLASS_NAME, "getRootCertificateValidFromStore");
        return rootCertificateOptional;
    }

    protected void addChildToParent(Certificate parent, Certificate child) {
        String METHOD_NAME = "addChildToParent";
        this.logger.entering(CLASS_NAME, "addChildToParent");
        parent.getChildren().add(child);
        child.setParent(parent);
        this.repo.create((AbstractEntity)child);
        this.logger.exiting(CLASS_NAME, "addChildToParent");
    }

    protected void deleteCertificates(List<Certificate> certificates) {
        String METHOD_NAME = "deleteCertificates";
        this.logger.entering(CLASS_NAME, "deleteCertificates");
        List ids = certificates.stream().map(Certificate::getEntityId).collect(Collectors.toList());
        this.publisher.publishEvent((ApplicationEvent)new CertificatesDeleteEvent((Object)this, ids));
        this.logger.exiting(CLASS_NAME, "deleteCertificates");
    }

    protected void replaceLinkCertificate(Certificate parent, Certificate linkToBeImported, CertificateType certificateType) {
        List links = parent.getChildren().parallelStream().filter(link -> link.getType() == certificateType && link.getAuthorityKeyIdentifier().equals(linkToBeImported.getAuthorityKeyIdentifier())).collect(Collectors.toList());
        List identicalWithDifferences = Uniqueness.findIdenticalWithDifferences(links, (Certificate)linkToBeImported, (Logger)this.logger);
        this.deleteCertificates(identicalWithDifferences);
        this.logReplacedCertificate(identicalWithDifferences, linkToBeImported);
    }

    protected void logReplacedCertificate(List<Certificate> certificates, Certificate newCertificate) {
        String METHOD_NAME = "logReplacedCertificate";
        this.logger.entering(CLASS_NAME, "logReplacedCertificate");
        if (certificates.size() > 1) {
            this.logger.log(Level.WARNING, "000052", this.i18n.getEnglishMessage("foundMultipleCertificatesToBeReplacedBySignatureAndPK", new String[]{newCertificate.getSignature(), newCertificate.getSubjectPublicKey()}), CLASS_NAME);
        }
        certificates.forEach(cert -> this.logger.log(Level.INFO, "000051", this.i18n.getEnglishMessage("certificateWithSignatureAndPKReplaced", new String[]{cert.getSignature(), cert.getSubjectPublicKey(), newCertificate.getSignature(), newCertificate.getSubjectPublicKey()}), CLASS_NAME));
        this.logger.exiting(CLASS_NAME, "logReplacedCertificate");
    }

    protected void logCertificateAlreadyExist(Certificate certificate) {
        this.logger.log(Level.INFO, "000016", this.i18n.getEnglishMessage("certAlreadyExistsWithSubjectKey", new String[]{certificate.getSubjectKeyIdentifier()}), CLASS_NAME);
    }

    protected void logDidNotFindIssuer(String loggingConstant, Certificate certificate, CertificateType issuerType) {
        this.logger.log(Level.WARNING, loggingConstant, this.i18n.getEnglishMessage("issuerNotFound", new String[]{issuerType.getText(), certificate.getPKIRole(), certificate.getSubjectKeyIdentifier()}), CLASS_NAME);
    }

    protected void logFoundMulipleCertificateWithDifferentSignatureButSamePublicKey() {
        this.logger.log(Level.WARNING, "000054", this.i18n.getEnglishMessage("foundMultipleCertificatesWithDiffSigButSamePublicKey"), CLASS_NAME);
    }

    protected void addErrorFoundMultipleCeritficateWithDifferentSignatureButSamePublicKey(Certificate certificate, List<ValidationError> errors) {
        errors.add(new ValidationError(certificate.getSubjectKeyIdentifier(), this.i18n.getMessage("multipleCertificatesDifSig"), "multipleCertificatesDifSig", null));
    }
}
