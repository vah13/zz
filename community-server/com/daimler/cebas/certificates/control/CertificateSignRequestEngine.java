/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.CertificateCryptoEngine
 *  com.daimler.cebas.certificates.control.SearchEngine
 *  com.daimler.cebas.certificates.control.config.CertificatesConfiguration
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqEnhRightsException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqInvalidTypeException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqParentNotExistsException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqParentTypeInvalidException
 *  com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory
 *  com.daimler.cebas.certificates.control.factories.AttributeCertificateHolderGenerator
 *  com.daimler.cebas.certificates.control.repository.CertificateRepository
 *  com.daimler.cebas.certificates.control.validation.CertificateInStoreValidator
 *  com.daimler.cebas.certificates.control.validation.CertificateSignRequestValidator
 *  com.daimler.cebas.certificates.control.validation.CertificatesProcessValidation
 *  com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput
 *  com.daimler.cebas.certificates.control.vo.CertificateSignRequest
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.entity.PKIRole
 *  com.daimler.cebas.certificates.entity.UserRole
 *  com.daimler.cebas.common.CryptoTools
 *  com.daimler.cebas.common.control.HexUtil
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.common.entity.AbstractEntity
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  com.daimler.cebas.users.control.crypto.UserCryptoEngine
 *  com.daimler.cebas.users.entity.User
 *  com.daimler.cebas.users.entity.UserKeyPair
 *  org.bouncycastle.cert.X509AttributeCertificateHolder
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.StringUtils
 */
package com.daimler.cebas.certificates.control;

import com.daimler.cebas.certificates.CertificateCryptoEngine;
import com.daimler.cebas.certificates.control.SearchEngine;
import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
import com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqEnhRightsException;
import com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqInvalidTypeException;
import com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqParentNotExistsException;
import com.daimler.cebas.certificates.control.exceptions.CertificateSigningReqParentTypeInvalidException;
import com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory;
import com.daimler.cebas.certificates.control.factories.AttributeCertificateHolderGenerator;
import com.daimler.cebas.certificates.control.repository.CertificateRepository;
import com.daimler.cebas.certificates.control.validation.CertificateInStoreValidator;
import com.daimler.cebas.certificates.control.validation.CertificateSignRequestValidator;
import com.daimler.cebas.certificates.control.validation.CertificatesProcessValidation;
import com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput;
import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.entity.PKIRole;
import com.daimler.cebas.certificates.entity.UserRole;
import com.daimler.cebas.common.CryptoTools;
import com.daimler.cebas.common.control.HexUtil;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.common.entity.AbstractEntity;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import com.daimler.cebas.users.control.crypto.UserCryptoEngine;
import com.daimler.cebas.users.entity.User;
import com.daimler.cebas.users.entity.UserKeyPair;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Level;
import org.bouncycastle.cert.X509AttributeCertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@CEBASControl
public class CertificateSignRequestEngine {
    private static final String HEX_SEPARATOR = "-";
    private static final String EMPTY = "";
    private Session session;
    protected SearchEngine searchEngine;
    protected CertificateRepository repository;
    protected CertificatesConfiguration certificateProfileConfiguration;
    private AbstractCertificateFactory factory;
    private Logger logger;
    private MetadataManager i18n;

    @Autowired
    public CertificateSignRequestEngine(Session session, SearchEngine searchEngine, CertificateRepository repository, CertificatesConfiguration certificateProfileConfiguration, AbstractCertificateFactory factory, Logger logger, MetadataManager i18n) {
        this.session = session;
        this.searchEngine = searchEngine;
        this.repository = repository;
        this.certificateProfileConfiguration = certificateProfileConfiguration;
        this.factory = factory;
        this.logger = logger;
        this.i18n = i18n;
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public Certificate createCertificateInSignRequestState(CertificateSignRequest certificateSignRequest, boolean checkCurrentUser, ValidationFailureOutput failureOutputStrategy, boolean persistCsr) {
        CertificatesProcessValidation.validateCreateCertificateSignRequest((CertificateSignRequest)certificateSignRequest, (MetadataManager)this.i18n, (Logger)this.logger);
        this.session.getSystemIntegrityCheckResult().clear();
        Certificate certificate = null;
        User currentUser = this.session.getCurrentUser();
        if (!certificateSignRequest.isValid()) throw this.getInvalidCertificateTypeException(certificateSignRequest);
        if (!this.isValidCertificateType(CertificateType.valueFromString((String)certificateSignRequest.getCertificateType()))) throw this.getInvalidCertificateTypeException(certificateSignRequest);
        if (certificateSignRequest.getValidTo() == null) {
            certificateSignRequest.setValidTo(this.certificateProfileConfiguration.getCSRValidTo(this.logger, this.i18n));
        }
        CertificateSignRequestValidator.validate((CertificateSignRequest)certificateSignRequest, (Logger)this.logger, (MetadataManager)this.i18n, (ValidationFailureOutput)failureOutputStrategy);
        if (!certificateSignRequest.isValid()) return certificate;
        certificate = this.createCertificateInSignRequestState(certificateSignRequest, checkCurrentUser, currentUser, persistCsr);
        return certificate;
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public Certificate createCertificateInSignRequestStateNewTransaction(CertificateSignRequest certificateSignRequest, boolean checkCurrentUser, ValidationFailureOutput failureOutputStrategy) {
        return this.createCertificateInSignRequestState(certificateSignRequest, checkCurrentUser, failureOutputStrategy, true);
    }

    protected CEBASCertificateException getUnauthorizedUserException() {
        CEBASCertificateException zenzefiCertificateException = new CEBASCertificateException(this.i18n.getMessage("registeredUserNeedsToBeLoggedIn"), "registeredUserNeedsToBeLoggedIn");
        this.logger.logWithTranslation(Level.WARNING, "000010X", zenzefiCertificateException.getMessageId(), zenzefiCertificateException.getClass().getSimpleName());
        return zenzefiCertificateException;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Certificate createCertificateInSignRequestState(CertificateSignRequest certificateSignRequest, boolean checkCurrentUser, User currentUser, boolean persistCsr) {
        Certificate certificate;
        certificateSignRequest.setUserId(currentUser.getUserName());
        this.validateRights(checkCurrentUser);
        UserCryptoEngine cryptoEngine = this.session.getCryptoEngine();
        Certificate parent = this.getParent(certificateSignRequest, currentUser);
        this.repository.refresh((AbstractEntity)parent);
        CertificateType csrType = CertificateType.valueFromString((String)certificateSignRequest.getCertificateType());
        this.validateCSRUnderDiagnostic(parent, csrType);
        String publicKey = EMPTY;
        if (csrType == CertificateType.ENHANCED_RIGHTS_CERTIFICATE) {
            if (parent.getType() != CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE) {
                CertificateSigningReqEnhRightsException zenzefiCertificateException = new CertificateSigningReqEnhRightsException(this.i18n.getMessage("enhRightsExistsOnlyInContextOfDiagAuth"), "enhRightsExistsOnlyInContextOfDiagAuth");
                this.logger.logWithTranslation(Level.WARNING, "000098X", zenzefiCertificateException.getMessageId(), zenzefiCertificateException.getClass().getSimpleName());
                throw zenzefiCertificateException;
            }
            X509AttributeCertificateHolder attributeHolder = AttributeCertificateHolderGenerator.generate((Certificate)parent, (CertificateSignRequest)certificateSignRequest, (Logger)this.logger);
            KeyPair keyPair = this.getKeyPairForCertificate(parent);
            try {
                certificateSignRequest.setSignature(this.createPKCS10SignRequest(certificateSignRequest, keyPair));
                certificate = this.factory.getCertificateInstance(EMPTY, certificateSignRequest, attributeHolder, currentUser);
                certificate.setParent(parent);
                parent.getChildren().add(certificate);
                this.repository.create((AbstractEntity)certificate);
            }
            finally {
                CryptoTools.destroyPrivateKey((PrivateKey)keyPair.getPrivate());
            }
        }
        KeyPair generateKeyPair = cryptoEngine.generateKeyPair();
        try {
            publicKey = cryptoEngine.getPublicKeyDataHex(generateKeyPair.getPublic());
            if (persistCsr) {
                String encodedPrivateKey = cryptoEngine.encodePrivateKey(this.session.getContainerKey(), generateKeyPair.getPrivate().getEncoded());
                certificateSignRequest.setSignature(this.createPKCS10SignRequest(certificateSignRequest, generateKeyPair));
                certificate = this.factory.getCertificateInstance(publicKey, certificateSignRequest, currentUser);
                UserKeyPair userKeyPair = new UserKeyPair(publicKey, encodedPrivateKey, currentUser, certificate);
                currentUser.getKeyPairs().add(userKeyPair);
                certificate.setParent(parent);
                parent.getChildren().add(certificate);
                this.repository.create((AbstractEntity)certificate);
                this.repository.create((AbstractEntity)userKeyPair);
            } else {
                certificateSignRequest.setSignature(this.createPKCS10SignRequest(certificateSignRequest, generateKeyPair));
                certificate = this.factory.getCertificateInstance(publicKey, certificateSignRequest, currentUser);
            }
        }
        finally {
            CryptoTools.destroyPrivateKey((PrivateKey)generateKeyPair.getPrivate());
        }
        this.logger.log(Level.INFO, "000049", this.i18n.getEnglishMessage("csrCreatedType", new String[]{certificateSignRequest.getCertificateType(), parent.getSubjectKeyIdentifier(), publicKey}), this.getClass().getSimpleName());
        return certificate;
    }

    private void validateRights(boolean checkCurrentUser) {
        if (!checkCurrentUser) return;
        if (!this.session.isDefaultUser()) return;
        throw this.getUnauthorizedUserException();
    }

    private boolean isValidCertificateType(CertificateType type) {
        return this.certificateProfileConfiguration.availableCertificateTypesForCSRCreation().contains(type);
    }

    private Optional<Certificate> getParentCertificateOptional(CertificateSignRequest certificateSignRequest, User currentUser) {
        return certificateSignRequest.getParentId() != null ? this.repository.findCertificate(certificateSignRequest.getParentId()) : this.searchEngine.findCertificate(currentUser, HexUtil.bytesToHex((byte[])Base64.getDecoder().decode(certificateSignRequest.getAuthorityKeyIdentifier())));
    }

    private KeyPair getKeyPairForCertificate(Certificate certificate) {
        UserKeyPair userKeyPair = this.searchEngine.getUserKeyPairForCertificate(this.session.getCurrentUser(), certificate);
        byte[] decodedPrivateKey = this.session.getCryptoEngine().decodePrivateKeyToByteArray(this.session.getContainerKey(), userKeyPair.getPrivateKey());
        PublicKey publicKey = certificate.getCertificateData().getCert().getPublicKey();
        PrivateKey privateKey = CertificateCryptoEngine.generatePrivateKey((byte[])decodedPrivateKey);
        return new KeyPair(publicKey, privateKey);
    }

    private String createPKCS10SignRequest(CertificateSignRequest certificateSignRequest, KeyPair keyPair) {
        String subject = certificateSignRequest.getSubject();
        CertificateType certificateType = CertificateType.valueFromString((String)certificateSignRequest.getCertificateType());
        Optional<Byte> pkiRole = Optional.ofNullable(PKIRole.getPKIRoleFromCertificateType((CertificateType)certificateType).byteValue());
        Optional<Byte> userRole = Optional.ofNullable(certificateType.equals((Object)CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE) || certificateType.equals((Object)CertificateType.TIME_CERTIFICATE) ? Byte.valueOf(UserRole.getByteFromText((String)certificateSignRequest.getUserRole())) : null);
        Optional<String> vin = Optional.ofNullable(certificateSignRequest.getTargetVIN());
        Optional<String> ecu = Optional.ofNullable(certificateSignRequest.getTargetECU());
        Optional<Object> nonce = Optional.ofNullable(!StringUtils.isEmpty((Object)certificateSignRequest.getNonce()) ? HexUtil.bytesToHex((byte[])Base64.getDecoder().decode(certificateSignRequest.getNonce())).replace(HEX_SEPARATOR, EMPTY).toLowerCase() : null);
        Optional<String> uniqueECUId = Optional.ofNullable(certificateSignRequest.getUniqueECUID());
        Optional<String> specialECU = Optional.ofNullable(certificateSignRequest.getSpecialECU());
        Optional<String> services = Optional.ofNullable(certificateSignRequest.getServices());
        Optional<Object> targetSubjectKeyIdentifier = Optional.ofNullable(!StringUtils.isEmpty((Object)certificateSignRequest.getTargetSubjectKeyIdentifier()) ? certificateSignRequest.getTargetSubjectKeyIdentifier().replace(HEX_SEPARATOR, EMPTY).toLowerCase() : null);
        Optional<Byte> prodQualifier = Optional.ofNullable(certificateSignRequest.retrieveProdQualifierForCSR(certificateSignRequest).byteValue());
        byte[] pkcs10Bytes = CertificateCryptoEngine.createCertificateSignRequestAsPKCS10((String)subject, (KeyPair)keyPair, pkiRole, userRole, vin, ecu, nonce, uniqueECUId, specialECU, services, targetSubjectKeyIdentifier, prodQualifier, (String)certificateSignRequest.getCertificateType());
        return CertificateCryptoEngine.convertSignedCSRToBase64EncodedPemFormat((byte[])pkcs10Bytes);
    }

    private void validateParentType(Certificate parent) {
        if (parent.getType() == CertificateType.BACKEND_CA_CERTIFICATE) return;
        if (parent.getType() == CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE) return;
        CertificateSigningReqParentTypeInvalidException zenzefiCertificateException = new CertificateSigningReqParentTypeInvalidException(this.i18n.getMessage("parentCertInvalidBackendAndDiagSupportedForEnhRights", new String[]{parent.getType().getText()}), "parentCertInvalidBackendAndDiagSupportedForEnhRights");
        this.logger.logWithTranslation(Level.WARNING, "000095X", zenzefiCertificateException.getMessageId(), zenzefiCertificateException.getClass().getSimpleName());
        throw zenzefiCertificateException;
    }

    private void validateParentCurrentUserStore(User currentUser, Certificate parent) {
        if (CertificateInStoreValidator.isInStore((Certificate)parent, (String)currentUser.getEntityId(), (Logger)this.logger)) return;
        CertificateSigningReqParentNotExistsException zenzefiCertificateException = new CertificateSigningReqParentNotExistsException(this.i18n.getMessage("providedCertParentNotExist"), "providedCertParentNotExist");
        this.logger.logWithTranslation(Level.WARNING, "000096X", zenzefiCertificateException.getMessageId(), zenzefiCertificateException.getClass().getSimpleName());
        throw zenzefiCertificateException;
    }

    private void validateCSRUnderDiagnostic(Certificate parent, CertificateType csrType) {
        if (parent.getType() != CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE) return;
        if (csrType == CertificateType.ENHANCED_RIGHTS_CERTIFICATE) return;
        CertificateSigningReqEnhRightsException zenzefiCertificateException = new CertificateSigningReqEnhRightsException(this.i18n.getMessage("diagCertOnlyEnhRightsCSRUnderIt"), "diagCertOnlyEnhRightsCSRUnderIt");
        this.logger.logWithTranslation(Level.WARNING, "000097X", zenzefiCertificateException.getMessageId(), zenzefiCertificateException.getClass().getSimpleName());
        throw zenzefiCertificateException;
    }

    private CertificateSigningReqInvalidTypeException getInvalidCertificateTypeException(CertificateSignRequest certificateSignRequest) {
        CertificateSigningReqInvalidTypeException zenzefiCertificateException = new CertificateSigningReqInvalidTypeException(this.i18n.getMessage("cannotCreateCSR", new String[]{certificateSignRequest.getCertificateType()}), "cannotCreateCSR");
        this.logger.logWithTranslation(Level.WARNING, "000152X", zenzefiCertificateException.getMessageId(), new String[]{certificateSignRequest.getCertificateType()}, zenzefiCertificateException.getClass().getSimpleName());
        return zenzefiCertificateException;
    }

    private CertificateNotFoundException parentDoesNotExistException() {
        CertificateNotFoundException parentDoesNotExist = new CertificateNotFoundException(this.i18n.getMessage("backendNotFound"), "backendNotFound");
        this.logger.logWithTranslation(Level.WARNING, "000094X", parentDoesNotExist.getMessageId(), parentDoesNotExist.getClass().getSimpleName());
        return parentDoesNotExist;
    }

    private Certificate getParent(CertificateSignRequest certificateSignRequest, User currentUser) {
        Optional<Certificate> parentCertificateOptional = this.getParentCertificateOptional(certificateSignRequest, currentUser);
        Certificate parent = parentCertificateOptional.orElseThrow(this::parentDoesNotExistException);
        if (certificateSignRequest.getParentId() == null || certificateSignRequest.getParentId().isEmpty()) {
            certificateSignRequest.setParentId(parent.getEntityId());
        }
        this.validateParentType(parent);
        this.validateParentCurrentUserStore(currentUser, parent);
        return parent;
    }
}
