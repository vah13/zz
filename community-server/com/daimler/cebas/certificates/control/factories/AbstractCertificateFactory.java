/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.PKCS12Manager
 *  com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException
 *  com.daimler.cebas.certificates.control.factories.AbstractImportResultFactory
 *  com.daimler.cebas.certificates.control.factories.CertificateHolderFactory
 *  com.daimler.cebas.certificates.control.vo.CertificateSignRequest
 *  com.daimler.cebas.certificates.control.vo.ImportResult
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.parsing.CertificateParser
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.FactoryMethodPattern
 *  com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  com.daimler.cebas.users.entity.User
 *  org.bouncycastle.cert.X509AttributeCertificateHolder
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.core.io.ClassPathResource
 */
package com.daimler.cebas.certificates.control.factories;

import com.daimler.cebas.certificates.control.PKCS12Manager;
import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
import com.daimler.cebas.certificates.control.factories.AbstractImportResultFactory;
import com.daimler.cebas.certificates.control.factories.CertificateHolderFactory;
import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
import com.daimler.cebas.certificates.control.vo.ImportResult;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.FactoryMethodPattern;
import com.daimler.cebas.configuration.control.exceptions.ZenZefiConfigurationException;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import com.daimler.cebas.users.entity.User;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import org.bouncycastle.cert.X509AttributeCertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

@FactoryMethodPattern
public abstract class AbstractCertificateFactory {
    private static final String CREATE_CERTIFICATE_METHOD_NAME = "createCertificate";
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(AbstractCertificateFactory.class.getName());
    private static final String CLASS_NAME = AbstractCertificateFactory.class.getSimpleName();
    private CertificateHolderFactory certificateHolderFactory;
    private Logger logger;
    private MetadataManager i18n;
    public static final String CERTIFICATE_TYPE = "X.509";
    private static final String EMPTY = "";
    private AbstractImportResultFactory importResultFactory;
    private Session session;

    @Autowired
    public AbstractCertificateFactory(Logger logger, MetadataManager i18n, AbstractImportResultFactory importResultFactory, CertificateHolderFactory certificateHolderFactory, Session session) {
        this.logger = logger;
        this.i18n = i18n;
        this.importResultFactory = importResultFactory;
        this.certificateHolderFactory = certificateHolderFactory;
        this.session = session;
    }

    public Optional<Certificate> createCertificate(InputStream stream, User user) {
        String METHOD_NAME = CREATE_CERTIFICATE_METHOD_NAME;
        this.logger.entering(CLASS_NAME, CREATE_CERTIFICATE_METHOD_NAME);
        X509Certificate certificate = this.getCertificate(stream);
        this.logger.exiting(CLASS_NAME, CREATE_CERTIFICATE_METHOD_NAME);
        return certificate == null ? Optional.empty() : Optional.of(this.getCertificateInstance(certificate, user));
    }

    public Optional<Certificate> createCertificate(InputStream stream, byte[] originalBytes, User user) {
        String METHOD_NAME = CREATE_CERTIFICATE_METHOD_NAME;
        this.logger.entering(CLASS_NAME, CREATE_CERTIFICATE_METHOD_NAME);
        if (stream == null && (originalBytes == null || originalBytes.length == 0)) {
            this.logger.log(Level.INFO, "000591", this.i18n.getMessage("wrongInputCreatingCertificate"), CLASS_NAME);
            throw new CEBASCertificateException(this.i18n.getMessage("wrongInputCreatingCertificate"));
        }
        String errorMessage = null;
        X509Certificate certificate = null;
        boolean checkAttributeCertificate = false;
        try {
            certificate = this.getCertificate(stream);
        }
        catch (IllegalArgumentException e) {
            LOG.log(Level.FINE, "Exception when building X509: " + e.getMessage());
            checkAttributeCertificate = false;
            if (CertificateParser.isInvalidPublicKey((InputStream)stream)) {
                errorMessage = this.i18n.getMessage("thePublicKeyIsInvalid", new String[]{e.getMessage()});
                this.logger.logWithException("000417X", this.i18n.getEnglishMessage("thePublicKeyIsInvalid", new String[]{e.getMessage()}), new CEBASException(e.getMessage()));
            } else {
                errorMessage = e.getMessage();
            }
        }
        catch (CEBASException e) {
            checkAttributeCertificate = true;
            LOG.log(Level.FINEST, e.getMessage(), e);
            errorMessage = e.getMessage();
        }
        if (checkAttributeCertificate && originalBytes != null && originalBytes.length != 0) {
            return this.createCertificateAsAttribute(originalBytes, user);
        }
        if (certificate == null) {
            throw new CEBASCertificateException(errorMessage);
        }
        this.logger.exiting(CLASS_NAME, CREATE_CERTIFICATE_METHOD_NAME);
        return Optional.of(this.getCertificateInstance(certificate, originalBytes, user));
    }

    public List<Optional<CertificatePrivateKeyHolder>> createCertificatePrivateKeyHolder(InputStream stream, byte[] byteArrayForPossibleCerAttributeHolder, User user, boolean isPKCS12, String password, List<ImportResult> importResults, String fileName) {
        String METHOD_NAME = "createCertificatePrivateKeyHolder";
        this.logger.entering(CLASS_NAME, "createCertificatePrivateKeyHolder");
        if (isPKCS12) {
            return this.createCertificateHoldersFromPKCS(stream, user, password, importResults, fileName);
        }
        try {
            Optional<Certificate> createCertificate = this.createCertificate(stream, byteArrayForPossibleCerAttributeHolder, user);
            if (createCertificate.isPresent()) {
                return Collections.singletonList(Optional.of(new CertificatePrivateKeyHolder(fileName, createCertificate.get(), Optional.empty())));
            }
        }
        catch (Exception e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            ImportResult invalidImportResult = new ImportResult(fileName, EMPTY, EMPTY, this.i18n.getMessage("cannotReadFile") + ": " + fileName + ", " + e.getMessage(), false);
            importResults.add(invalidImportResult);
        }
        this.logger.exiting(CLASS_NAME, "createCertificatePrivateKeyHolder");
        return Collections.emptyList();
    }

    public Optional<Certificate> createCertificateAsAttribute(byte[] bytes, User user) {
        X509AttributeCertificateHolder certificate;
        String METHOD_NAME = "createCertificateAsAttribute";
        this.logger.entering(CLASS_NAME, "createCertificateAsAttribute");
        try {
            certificate = new X509AttributeCertificateHolder(bytes);
        }
        catch (IOException e) {
            this.logger.log(Level.WARNING, "000075", this.i18n.getEnglishMessage("certificateBytesCannotBeRead", new String[]{e.getMessage()}), CLASS_NAME);
            throw new CEBASCertificateException(this.i18n.getMessage("certificateBytesCannotBeRead", new String[]{e.getMessage()}));
        }
        this.logger.exiting(CLASS_NAME, "createCertificateAsAttribute");
        return Optional.of(this.getCertificateInstance(certificate, bytes, user));
    }

    public abstract Certificate getCertificateInstance();

    public abstract Certificate getCertificateInstance(X509AttributeCertificateHolder var1, User var2);

    public abstract Certificate getCertificateInstance(X509AttributeCertificateHolder var1, byte[] var2, User var3);

    public abstract Certificate getCertificateInstance(X509Certificate var1, User var2);

    public abstract Certificate getCertificateInstance(X509Certificate var1, byte[] var2, User var3);

    public abstract Certificate getCertificateInstance(String var1, CertificateSignRequest var2, User var3);

    public abstract Certificate getCertificateInstance(String var1, CertificateSignRequest var2, X509AttributeCertificateHolder var3, User var4);

    public Certificate createCertificate(String resourcePath, User user) throws CEBASCertificateException {
        String METHOD_NAME = CREATE_CERTIFICATE_METHOD_NAME;
        this.logger.entering(CLASS_NAME, CREATE_CERTIFICATE_METHOD_NAME);
        X509Certificate cer = this.getCertificate(resourcePath);
        if (cer == null) {
            throw new CertificateNotFoundException(this.i18n.getEnglishMessage("couldNotCreateCertFormBytes", new String[]{"The certificate under path " + resourcePath + " could not be created."}));
        }
        Certificate certificate = this.getCertificateInstance(cer, user);
        this.logger.entering(CLASS_NAME, CREATE_CERTIFICATE_METHOD_NAME);
        return certificate;
    }

    public ImportResult getImportResult(CertificatePrivateKeyHolder holder, String errorMessage, boolean isSuccess) {
        return this.importResultFactory.getImportResult(holder, errorMessage, isSuccess);
    }

    public byte[] getCertificateBytes(Certificate certificate) {
        try {
            if (certificate.getCertificateData() == null || certificate.getCertificateData().getOriginalBytes() == null) return certificate.getCertificateData().getCert() != null ? certificate.getCertificateData().getCert().getEncoded() : certificate.getCertificateData().getAttributesCertificateHolder().getEncoded();
            return certificate.getCertificateData().getOriginalBytes();
        }
        catch (IOException | CertificateEncodingException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            CertificateNotFoundException ex = new CertificateNotFoundException(this.i18n.getMessage("errorGettingEncodedCertificateBytes", new String[]{e.getMessage()}), "errorGettingEncodedCertificateBytes");
            this.logger.logWithTranslation(Level.WARNING, "000058X", ex.getMessageId(), new String[]{e.getMessage()}, ex.getClass().getSimpleName());
            throw ex;
        }
    }

    public Certificate getCertificateFromBase64(String certificateBase64) {
        byte[] certificateRaw = Base64.getDecoder().decode(certificateBase64);
        return this.getCertificateFromBytes(certificateRaw);
    }

    protected Certificate getCertificateFromBytes(byte[] certificateRaw) {
        ByteArrayInputStream is = new ByteArrayInputStream(certificateRaw);
        User currentUser = this.session.getCurrentUser();
        Optional<Certificate> certificateOptional = this.createCertificate(is, certificateRaw, currentUser);
        if (certificateOptional.isPresent()) return certificateOptional.orElseThrow(this.logger.logWithTranslationSupplier(Level.WARNING, "000066X", (CEBASException)new ZenZefiConfigurationException(this.i18n.getMessage("couldNotCreateCertFormBytes"), "couldNotCreateCertFormBytes")));
        Optional<Certificate> ehhRights = this.createCertificate(null, certificateRaw, currentUser);
        if (ehhRights.isPresent()) {
            CertificateNotFoundException ex = new CertificateNotFoundException(this.i18n.getMessage("couldNotCheckSignatureOfMessageForEngRights"), "couldNotCheckSignatureOfMessageForEngRights");
            this.logger.logWithTranslation(Level.WARNING, "000099X", ex.getMessageId(), ex.getClass().getSimpleName());
            throw ex;
        }
        CertificateNotFoundException ex = new CertificateNotFoundException(this.i18n.getMessage("couldNotCreateCertFormBytes"), "couldNotCreateCertFormBytes");
        this.logger.logWithTranslation(Level.WARNING, "000100X", ex.getMessageId(), ex.getClass().getSimpleName());
        throw ex;
    }

    private X509Certificate getCertificate(InputStream stream) {
        X509Certificate certificate;
        try {
            CertificateFactory cf = CertificateFactory.getInstance(CERTIFICATE_TYPE);
            certificate = (X509Certificate)cf.generateCertificate(stream);
        }
        catch (CertificateException ex) {
            this.warnCouldNotCreateX509Certificate(ex.getMessage(), ex);
            throw new CEBASCertificateException(this.i18n.getMessage("couldNotCreateCertFormBytes", new String[]{ex.getMessage()}));
        }
        finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            }
            catch (IOException e) {
                this.warnCouldNotCloseStreamOnCreateX509Certificate(e.getMessage(), e);
            }
        }
        return certificate;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private X509Certificate getCertificate(String resourcePath) {
        X509Certificate certificate = null;
        InputStream inputStream = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance(CERTIFICATE_TYPE);
            ClassPathResource resource = new ClassPathResource(resourcePath);
            if (resource.exists()) {
                certificate = (X509Certificate)cf.generateCertificate(resource.getInputStream());
            } else if (new File(resourcePath).exists()) {
                inputStream = new FileInputStream(resourcePath);
                certificate = (X509Certificate)cf.generateCertificate(inputStream);
            } else {
                this.warnCouldNotCreateX509Certificate("Resource does not exist: " + resourcePath);
            }
        }
        catch (IOException | CertificateException ex) {
            this.warnCouldNotCreateX509Certificate(ex.getMessage(), ex);
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    this.warnCouldNotCloseStreamOnCreateX509Certificate(e.getMessage(), e);
                }
            }
        }
        return certificate;
    }

    private List<Optional<CertificatePrivateKeyHolder>> createCertificateHoldersFromPKCS(InputStream stream, User user, String password, List<ImportResult> importResults, String filename) {
        return PKCS12Manager.readPKCS12((InputStream)stream, (String)password, (User)user, (Logger)this.logger, (CertificateHolderFactory)this.certificateHolderFactory, importResults, (String)filename, (MetadataManager)this.i18n);
    }

    private void warnCouldNotCreateX509Certificate(String message, Exception e) {
        LOG.log(Level.FINEST, e.getMessage(), e);
        this.warnCouldNotCreateX509Certificate(message);
    }

    private void warnCouldNotCreateX509Certificate(String message) {
        this.logger.log(Level.WARNING, "000050", this.i18n.getEnglishMessage("couldNotCreateCertFormBytes", new String[]{message}), CLASS_NAME);
    }

    private void warnCouldNotCloseStreamOnCreateX509Certificate(String message, Exception e) {
        LOG.log(Level.FINEST, e.getMessage(), e);
        this.warnCouldNotCloseStreamOnCreateX509Certificate(message);
    }

    private void warnCouldNotCloseStreamOnCreateX509Certificate(String message) {
        this.logger.log(Level.WARNING, "000172X", this.i18n.getEnglishMessage("cannotCloseStreamOnCreateCertificate", new String[]{message}), CLASS_NAME);
    }
}
