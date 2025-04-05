/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.PKCS12Manager
 *  com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.control.importing.CertificatesImporter
 *  com.daimler.cebas.certificates.control.vo.EncryptedPKCSImportResult
 *  com.daimler.cebas.certificates.control.vo.EncryptedPKCSPackageInput
 *  com.daimler.cebas.certificates.control.vo.ImportInput
 *  com.daimler.cebas.certificates.control.vo.ImportInputType
 *  com.daimler.cebas.certificates.control.vo.ImportResult
 *  com.daimler.cebas.certificates.control.vo.LocalImportInput
 *  com.daimler.cebas.certificates.control.vo.RemoteCertificateImportInput
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.common.control.HexUtil
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.configuration.control.AbstractConfigurator
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  com.daimler.cebas.users.entity.User
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.web.multipart.MultipartFile
 *  org.springframework.web.util.HtmlUtils
 */
package com.daimler.cebas.certificates.control;

import com.daimler.cebas.certificates.control.PKCS12Manager;
import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.control.importing.CertificatesImporter;
import com.daimler.cebas.certificates.control.vo.EncryptedPKCSImportResult;
import com.daimler.cebas.certificates.control.vo.EncryptedPKCSPackageInput;
import com.daimler.cebas.certificates.control.vo.ImportInput;
import com.daimler.cebas.certificates.control.vo.ImportInputType;
import com.daimler.cebas.certificates.control.vo.ImportResult;
import com.daimler.cebas.certificates.control.vo.LocalImportInput;
import com.daimler.cebas.certificates.control.vo.RemoteCertificateImportInput;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.common.control.HexUtil;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.configuration.control.AbstractConfigurator;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import com.daimler.cebas.users.entity.User;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

@CEBASControl
@Transactional(propagation=Propagation.REQUIRED)
public class ImportCertificatesEngine {
    private static final String EMPTY = "";
    private static final String CLASS_NAME = ImportCertificatesEngine.class.getSimpleName();
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(CLASS_NAME);
    private static final String P12 = ".p12";
    protected Session session;
    private CertificatesImporter importer;
    protected AbstractConfigurator configurator;
    protected Logger logger;
    protected MetadataManager i18n;

    @Autowired
    public ImportCertificatesEngine(Session session, CertificatesImporter importer, AbstractConfigurator configurator, Logger logger, MetadataManager i18n) {
        this.session = session;
        this.importer = importer;
        this.configurator = configurator;
        this.logger = logger;
        this.i18n = i18n;
    }

    @Transactional(propagation=Propagation.REQUIRED)
    public List<ImportResult> importCertificatesFromBase64SameTransaction(List<String> base64Certificates, boolean onlyFromPKI, boolean allowPrivateKeys) {
        return this.importCertificatesFromBase64(base64Certificates, onlyFromPKI, allowPrivateKeys);
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public List<ImportResult> importCertificatesFromBase64NewTransaction(List<String> base64Certificates, boolean onlyFromPKI, boolean allowPrivateKeys) {
        return this.importCertificatesFromBase64(base64Certificates, onlyFromPKI, allowPrivateKeys);
    }

    private List<ImportResult> importCertificatesFromBase64(List<String> base64Certificates, boolean onlyFromPKI, boolean allowPrivateKeys) {
        List<RemoteCertificateImportInput> remoteInputs = base64Certificates.stream().map(base64Cert -> new RemoteCertificateImportInput(EMPTY, base64Cert)).collect(Collectors.toList());
        List<ImportResult> importRemoteCertificates = this.importRemoteCertificates(remoteInputs, onlyFromPKI, true);
        try {
            remoteInputs.clear();
            base64Certificates.clear();
        }
        catch (UnsupportedOperationException e) {
            this.logger.log(Level.FINEST, "000354X", e.getMessage(), CLASS_NAME);
        }
        return importRemoteCertificates;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<ImportResult> importRemoteCertificates(List<RemoteCertificateImportInput> remoteInputs, boolean onlyFromPKI, boolean allowPrivateKeys) {
        ArrayList<ImportInput> inputs = new ArrayList<ImportInput>(remoteInputs.size());
        try {
            this.session.getSystemIntegrityCheckResult().clear();
            remoteInputs.forEach(remoteInput -> {
                byte[] certBytes = Base64.getDecoder().decode(remoteInput.getCertificateBytes());
                ByteArrayInputStream inputStream = new ByteArrayInputStream(certBytes);
                inputs.add(this.createImportInput(inputStream, certBytes, remoteInput.getFileName(), Optional.<String>empty()));
            });
            List importCertificates = this.importer.importCertificates(inputs, this.session.getCurrentUser(), onlyFromPKI, allowPrivateKeys);
            inputs.clear();
            List list = importCertificates;
            return list;
        }
        finally {
            this.closeInputStreams(inputs);
        }
    }

    public List<ImportResult> importCertificatesFromLocal(List<LocalImportInput> inputs) {
        ArrayList input = new ArrayList(inputs.size());
        inputs.forEach(entry -> {
            String pathString = entry.getFilePath();
            try {
                File initialFile = new File(pathString);
                FileInputStream inputStream = FileUtils.openInputStream(initialFile);
                byte[] bytes = FileUtils.readFileToByteArray(initialFile);
                ImportInput importInput = this.createImportInput(inputStream, bytes, initialFile.getName(), entry.hasPassword() ? Optional.of(entry.getPassword()) : Optional.empty());
                input.add(importInput);
            }
            catch (IOException e) {
                this.handleImportFromLocalException(pathString, e);
            }
        });
        List result = this.importer.importCertificates(input, this.session.getCurrentUser(), true);
        inputs.clear();
        input.clear();
        return result;
    }

    public List<ImportResult> importCertificates(List<MultipartFile> files, boolean allowPrivateKeys) {
        this.session.getSystemIntegrityCheckResult().clear();
        ArrayList<ImportInput> input = new ArrayList<ImportInput>(files.size());
        Iterator<MultipartFile> iterator = files.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                List importCertificates = this.importer.importCertificates(input, this.session.getCurrentUser(), allowPrivateKeys);
                input.clear();
                return importCertificates;
            }
            MultipartFile file = iterator.next();
            try {
                InputStream inputStream = file.getInputStream();
                byte[] bytes = file.getBytes();
                String fileName = Paths.get(file.getOriginalFilename(), new String[0]).getFileName().toString();
                input.add(this.createImportInput(inputStream, bytes, fileName, Optional.<String>empty()));
            }
            catch (IOException e) {
                LOG.log(Level.FINEST, e.getMessage(), e);
                this.logger.logToFileOnly(CLASS_NAME, this.i18n.getEnglishMessage("cannotReadTheMultipart", new String[]{HtmlUtils.htmlEscape((String)file.getOriginalFilename())}), (Throwable)e);
                throw new CEBASCertificateException(this.i18n.getMessage("cannotReadFile"));
            }
        }
    }

    public EncryptedPKCSImportResult importEncryptedPKCSPackages(List<EncryptedPKCSPackageInput> inputPackages, PrivateKey privateKey) {
        String METHOD_NAME = "importEncryptedPKCSPackage";
        this.logger.entering(CLASS_NAME, "importEncryptedPKCSPackage");
        ArrayList importInputs = new ArrayList();
        inputPackages.forEach(inputPackage -> this.extractImportInput(privateKey, importInputs, (EncryptedPKCSPackageInput)inputPackage));
        List importResult = this.importer.importCertificates(importInputs, this.session.getCurrentUser(), true);
        EncryptedPKCSImportResult result = new EncryptedPKCSImportResult(this.getEncryptedPKCSImportResult(importResult), importResult);
        importInputs.clear();
        this.logger.exiting(CLASS_NAME, "importEncryptedPKCSPackage");
        return result;
    }

    public ImportResult checkImportIntoChain(CertificatePrivateKeyHolder holder) {
        return this.importer.checkImportIntoChain(holder, this.session.getCurrentUser());
    }

    public List<Certificate> importFromBaseStore(User currentUser) {
        return this.importer.importFromBaseStore(currentUser);
    }

    public List<Certificate> restoreCertificatesFromBaseStore(User currentUser) {
        return this.importer.restoreCertificatesFromBaseStore(currentUser);
    }

    protected ImportInput createImportInput(InputStream inputStream, byte[] bytes, String fileName, Optional<String> password) {
        if (!fileName.endsWith(P12)) return new ImportInput(inputStream, bytes, fileName, ImportInputType.CERTIFICATE_FILE);
        return password.map(pass -> new ImportInput(inputStream, bytes, fileName, pass, ImportInputType.PKCS12)).orElseGet(() -> new ImportInput(inputStream, bytes, fileName, ImportInputType.PKCS12));
    }

    protected void extractImportInput(PrivateKey privateKey, List<ImportInput> importInputs, EncryptedPKCSPackageInput inputPackage) {
        String backendSubjectKeyIdentifier = this.getDecodedHexBackendSubjectKeyIdentifier(inputPackage.getBackendSubjectKeyIdentifier());
        byte[] decodedPKCSPackage = this.getDecodedPKCSPackage(inputPackage.getEncryptedPKCSPackage(), backendSubjectKeyIdentifier);
        byte[] decryptedPKCSPackage = this.decryptPKCS12Package(decodedPKCSPackage, privateKey, backendSubjectKeyIdentifier);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decryptedPKCSPackage);
        backendSubjectKeyIdentifier = backendSubjectKeyIdentifier + P12;
        ImportInput importInput = this.createImportInput(inputStream, decryptedPKCSPackage, backendSubjectKeyIdentifier, Optional.of(this.configurator.getPKCS12PackagePassword()));
        importInputs.add(importInput);
    }

    private void closeInputStreams(List<ImportInput> inputs) {
        inputs.forEach(this::closeInputStream);
    }

    private void closeInputStream(ImportInput input) {
        if (input.getStream() == null) return;
        try {
            input.getStream().close();
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            this.logger.log(Level.WARNING, "000354X", this.i18n.getEnglishMessage("cannotCloseStreamOnCreateCertificate", new String[]{e.getMessage()}), CLASS_NAME);
        }
    }

    private void handleImportFromLocalException(String pathString, IOException e) {
        LOG.log(Level.FINEST, e.getMessage(), e);
        CEBASCertificateException exception = new CEBASCertificateException(this.i18n.getMessage("cannotReadFileFromPath", new String[]{HtmlUtils.htmlEscape((String)pathString)}), "cannotReadFileFromPath");
        this.logger.logWithTranslation(Level.WARNING, "000076", exception.getMessageId(), new String[]{HtmlUtils.htmlEscape((String)pathString)}, exception.getClass().getSimpleName());
        throw exception;
    }

    private String getDecodedHexBackendSubjectKeyIdentifier(String encodedBackendSubjectKeyIdentifier) {
        try {
            return HexUtil.base64ToHex((String)encodedBackendSubjectKeyIdentifier);
        }
        catch (Exception e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            this.logAndThrowZenzefiCertificateException("backendSerialNumberDecryptionFailed", "000178X");
            return EMPTY;
        }
    }

    private byte[] decryptPKCS12Package(byte[] decodedPKCSPackage, PrivateKey privateKey, String backendSubjectKeyIdentifier) {
        try {
            return PKCS12Manager.decryptPKCS12((byte[])decodedPKCSPackage, (PrivateKey)privateKey);
        }
        catch (GeneralSecurityException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            this.logAndThrowZenzefiCertificateException("decryptionOfPKCSPackageFailed", new String[]{backendSubjectKeyIdentifier}, "000175X");
            return new byte[0];
        }
    }

    private byte[] getDecodedPKCSPackage(String input, String backendID) {
        try {
            return Base64.getDecoder().decode(input);
        }
        catch (IllegalArgumentException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            this.logAndThrowZenzefiCertificateException("pkcsDecodeFailed", new String[]{backendID}, "000177X");
            return new byte[0];
        }
    }

    private String getEncryptedPKCSImportResult(List<? extends ImportResult> importResults) {
        int successfulImports = 0;
        int failedImports = 0;
        Iterator<? extends ImportResult> iterator = importResults.iterator();
        while (iterator.hasNext()) {
            ImportResult importResult = iterator.next();
            if (importResult.isSuccess()) {
                ++successfulImports;
                continue;
            }
            ++failedImports;
        }
        return this.i18n.getMessage("pkcsImportResult", new String[]{Integer.toString(successfulImports), Integer.toString(failedImports)});
    }

    private void logAndThrowZenzefiCertificateException(String messageID, String[] args, String logID) {
        CEBASCertificateException zenzefiCertificateException = new CEBASCertificateException(this.i18n.getMessage(messageID, args), messageID);
        this.logger.logWithTranslation(Level.WARNING, logID, zenzefiCertificateException.getMessageId(), args, zenzefiCertificateException.getClass().getSimpleName());
        throw zenzefiCertificateException;
    }

    private void logAndThrowZenzefiCertificateException(String messageID, String logID) {
        CEBASCertificateException zenzefiCertificateException = new CEBASCertificateException(this.i18n.getMessage(messageID), messageID);
        this.logger.logWithTranslation(Level.WARNING, logID, zenzefiCertificateException.getMessageId(), zenzefiCertificateException.getClass().getSimpleName());
        throw zenzefiCertificateException;
    }
}
