/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.SearchEngine
 *  com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder
 *  com.daimler.cebas.certificates.control.chain.ChainIdentifier
 *  com.daimler.cebas.certificates.control.chain.ChainOfTrustManager
 *  com.daimler.cebas.certificates.control.config.CertificatesConfiguration
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException
 *  com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory
 *  com.daimler.cebas.certificates.control.repository.CertificateRepository
 *  com.daimler.cebas.certificates.control.validation.BaseStoreUtil
 *  com.daimler.cebas.certificates.control.validation.CertificatesValidator
 *  com.daimler.cebas.certificates.control.validation.ValidationError
 *  com.daimler.cebas.certificates.control.vo.ImportInput
 *  com.daimler.cebas.certificates.control.vo.ImportResult
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.common.CryptoTools
 *  com.daimler.cebas.common.control.CEBASProperty
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.common.entity.AbstractEntity
 *  com.daimler.cebas.configuration.control.AbstractConfigurator
 *  com.daimler.cebas.configuration.entity.Configuration
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.entity.User
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.web.util.HtmlUtils
 */
package com.daimler.cebas.certificates.control.importing;

import com.daimler.cebas.certificates.control.SearchEngine;
import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
import com.daimler.cebas.certificates.control.chain.ChainIdentifier;
import com.daimler.cebas.certificates.control.chain.ChainOfTrustManager;
import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
import com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory;
import com.daimler.cebas.certificates.control.repository.CertificateRepository;
import com.daimler.cebas.certificates.control.validation.BaseStoreUtil;
import com.daimler.cebas.certificates.control.validation.CertificatesValidator;
import com.daimler.cebas.certificates.control.validation.ValidationError;
import com.daimler.cebas.certificates.control.vo.ImportInput;
import com.daimler.cebas.certificates.control.vo.ImportResult;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.common.CryptoTools;
import com.daimler.cebas.common.control.CEBASProperty;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.common.entity.AbstractEntity;
import com.daimler.cebas.configuration.control.AbstractConfigurator;
import com.daimler.cebas.configuration.entity.Configuration;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.entity.User;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

@CEBASControl
public class CertificatesImporter {
    private static final String SPACE = " ";
    private static final String CLASS_NAME = CertificatesImporter.class.getSimpleName();
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(CLASS_NAME);
    private static final String MESSAGE_SEPARATOR = ": ";
    private static final String COMMNA_AND_SPACE = ", ";
    private static final String EMPTY = "";
    private Logger logger;
    private AbstractCertificateFactory factory;
    private CertificateRepository repository;
    private AbstractConfigurator configurator;
    private ChainOfTrustManager chainOfTrustManager;
    private SearchEngine searchEngine;
    private MetadataManager i18n;
    private CertificatesConfiguration profileConfiguration;
    private ReentrantLock lockBaseStore = new ReentrantLock();
    @Value(value="${PKCS12_DEFAULT_PASSWORD}")
    private String pkcs12DefaultPassword;

    @Autowired
    public CertificatesImporter(AbstractCertificateFactory factory, CertificateRepository repo, AbstractConfigurator configurator, ChainOfTrustManager chainOfTrustManager, Logger logger, SearchEngine searchEngine, MetadataManager i18n, CertificatesConfiguration profileConfiguration) {
        this.factory = factory;
        this.repository = repo;
        this.configurator = configurator;
        this.chainOfTrustManager = chainOfTrustManager;
        this.logger = logger;
        this.searchEngine = searchEngine;
        this.i18n = i18n;
        this.profileConfiguration = profileConfiguration;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Transactional(propagation=Propagation.MANDATORY)
    public List<Certificate> importFromBaseStore(Map<Path, List<Path>> mappedPaths, User user) {
        String METHOD_NAME = "importFromBaseStore";
        this.logger.entering(CLASS_NAME, "importFromBaseStore");
        ArrayList<Certificate> defaultUserCertificates = new ArrayList<Certificate>();
        try {
            mappedPaths.entrySet().forEach(entry -> this.importFromPath(user, (List<Certificate>)defaultUserCertificates, (Map.Entry<Path, List<Path>>)entry));
        }
        catch (CertificateNotFoundException e) {
            this.logger.logToFileOnly(CLASS_NAME, "Certificate could not be accessed.", (Throwable)e);
        }
        finally {
            BaseStoreUtil.closeFileSystem();
        }
        this.logger.exiting(CLASS_NAME, "importFromBaseStore");
        return defaultUserCertificates;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Transactional(propagation=Propagation.MANDATORY)
    public List<Certificate> restoreCertificatesFromBaseStore(Map<Path, List<Path>> mappedPaths, User user) {
        String METHOD_NAME = "restoreCertificatesFromBaseStore";
        this.logger.entering(CLASS_NAME, "restoreCertificatesFromBaseStore");
        ArrayList<Certificate> restoredUserCertificates = new ArrayList<Certificate>();
        try {
            Set<Map.Entry<Path, List<Path>>> entrySet = mappedPaths.entrySet();
            entrySet.forEach(entry -> this.restoreFromPath(user, (List<Certificate>)restoredUserCertificates, (Map.Entry<Path, List<Path>>)entry));
        }
        finally {
            BaseStoreUtil.closeFileSystem();
        }
        this.logger.exiting(CLASS_NAME, "restoreCertificatesFromBaseStore");
        return restoredUserCertificates;
    }

    public Comparator<CertificatePrivateKeyHolder> getSortComparator() {
        return Comparator.comparingInt(CertificatePrivateKeyHolder::getLevel);
    }

    @Transactional(propagation=Propagation.MANDATORY)
    public List<Certificate> importFromBaseStore(User user) {
        String METHOD_NAME = "importFromBaseStore";
        this.logger.entering(CLASS_NAME, "importFromBaseStore");
        Map<Path, List<Path>> mappedPaths = this.getPathForBaseStoreCertificates();
        this.logger.exiting(CLASS_NAME, "importFromBaseStore");
        return this.importFromBaseStore(mappedPaths, user);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Transactional(propagation=Propagation.MANDATORY)
    public List<Certificate> restoreCertificatesFromBaseStore(User user) {
        String METHOD_NAME = "restoreCertificatesFromBaseStore";
        this.logger.entering(CLASS_NAME, "restoreCertificatesFromBaseStore");
        try {
            if (this.lockBaseStore.isLocked()) {
                throw new CEBASCertificateException("Restoring from base store already in progress");
            }
            this.lockBaseStore.lock();
            Map<Path, List<Path>> mappedPaths = this.getPathForBaseStoreCertificates();
            this.logger.exiting(CLASS_NAME, "restoreCertificatesFromBaseStore");
            List<Certificate> list = this.restoreCertificatesFromBaseStore(mappedPaths, user);
            return list;
        }
        finally {
            if (this.lockBaseStore.getHoldCount() != 0) {
                this.lockBaseStore.unlock();
            }
        }
    }

    public ImportResult checkImportIntoChain(CertificatePrivateKeyHolder holder, User user) {
        ImportResult result;
        String METHOD_NAME = "checkImportIntoChain";
        this.logger.entering(CLASS_NAME, "checkImportIntoChain");
        Certificate certificate = holder.getCertificate();
        if (CertificatesValidator.isUnknownType((Certificate)certificate)) {
            result = new ImportResult(certificate.getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier(), this.i18n.getMessage("unknownCertificateType"), false);
        } else {
            List validationResult = CertificatesValidator.extendedValidation((Certificate)certificate, (Logger)this.logger, (MetadataManager)this.i18n);
            Optional<Optional> errorResult = validationResult.stream().filter(Optional::isPresent).findFirst();
            if (errorResult.isPresent()) {
                StringBuilder buffer = new StringBuilder();
                validationResult.forEach(error -> error.ifPresent(e -> buffer.append(e.getErrorMessage()).append("\n")));
                result = new ImportResult(certificate.getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier(), buffer.toString(), false);
                LOG.log(Level.INFO, result.getMessage());
            } else {
                List chainOfTrustErorrs = this.chainOfTrustManager.checkChainOfTrust(user.getCertificates(), holder);
                StringBuilder errorMessage = new StringBuilder();
                if (chainOfTrustErorrs.isEmpty()) {
                    LOG.log(Level.INFO, this.i18n.getEnglishMessage("certificateValidatesSuccessfully", new String[]{certificate.getPKIRole(), certificate.getSubjectKeyIdentifier()}));
                    result = new ImportResult(certificate.getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier(), this.i18n.getMessage("certificateValidatesSuccessfully", new String[]{certificate.getPKIRole(), certificate.getSubjectKeyIdentifier()}), true);
                } else {
                    chainOfTrustErorrs.forEach(error -> errorMessage.append(error.getErrorMessage()).append(SPACE));
                    LOG.log(Level.WARNING, this.i18n.getEnglishMessage("chainOfTrustValidationFailed", new String[]{errorMessage.toString()}));
                    result = new ImportResult(certificate.getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier(), errorMessage.toString(), false);
                }
            }
        }
        this.logger.exiting(CLASS_NAME, "checkImportIntoChain");
        return result;
    }

    public List<ImportResult> importCertificates(List<ImportInput> inputList, User user, boolean onlyFromPKI, boolean allowPrivateKeys) {
        return onlyFromPKI ? this.importCertificatesUnderPKIChain(user, inputList, allowPrivateKeys) : this.importCertificates(inputList, user, allowPrivateKeys);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<ImportResult> importCertificates(List<ImportInput> inputList, User user, boolean allowPrivateKeys) {
        String METHOD_NAME = "importCertificates";
        this.logger.entering(CLASS_NAME, "importCertificates");
        ArrayList<ImportResult> result = new ArrayList<ImportResult>();
        List<CertificatePrivateKeyHolder> certificates = this.getCertificatesToImport(user, inputList, result, allowPrivateKeys);
        try {
            certificates.sort(this.getSortComparator());
            certificates.forEach(holder -> this.importCertificate(user, (List<ImportResult>)result, (CertificatePrivateKeyHolder)holder, false));
        }
        finally {
            certificates.forEach(holder -> CryptoTools.destroyPrivateKey((PrivateKey)holder.getPrivateKey()));
        }
        this.logger.exiting(CLASS_NAME, "importCertificates");
        return result;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<ImportResult> importCertificatesUnderPKIChain(User user, List<ImportInput> inputList, boolean allowPrivateKeys) {
        String METHOD_NAME = "importCertificates";
        this.logger.entering(CLASS_NAME, "importCertificates");
        ArrayList<ImportResult> result = new ArrayList<ImportResult>();
        List<CertificatePrivateKeyHolder> certificates = this.getCertificatesToImport(user, inputList, result, allowPrivateKeys);
        try {
            certificates.sort(this.getSortComparator());
            certificates.forEach(holder -> this.importCertificate(user, (List<ImportResult>)result, (CertificatePrivateKeyHolder)holder, true));
        }
        finally {
            certificates.forEach(holder -> CryptoTools.destroyPrivateKey((PrivateKey)holder.getPrivateKey()));
        }
        this.logger.exiting(CLASS_NAME, "importCertificates");
        return result;
    }

    private List<CertificatePrivateKeyHolder> getCertificatesToImport(User user, List<ImportInput> inputList, List<ImportResult> result, boolean allowPrivateKeys) {
        ArrayList optionals = new ArrayList();
        inputList.forEach(input -> optionals.addAll(this.getCertificatePrivateKeyOptional(user, (ImportInput)input, result)));
        List<CertificatePrivateKeyHolder> certificates = optionals.stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        List<CertificatePrivateKeyHolder> notAllowedPrivateKeys = certificates.stream().filter(c -> c.hasPrivateKey() && !allowPrivateKeys).collect(Collectors.toList());
        notAllowedPrivateKeys.forEach(c -> this.errorMesgPKNotAllowed(result, (CertificatePrivateKeyHolder)c));
        certificates.removeAll(notAllowedPrivateKeys);
        return certificates;
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void importCertificate(User user, List<ImportResult> result, CertificatePrivateKeyHolder holder, boolean onlyFromPKI) {
        Certificate certificate = holder.getCertificate();
        Optional rolledBackCertificate = this.profileConfiguration.getMatchingRolledBackCertificate(certificate);
        if (CertificatesValidator.isUnknownType((Certificate)certificate)) {
            result.add(this.factory.getImportResult(holder, this.i18n.getMessage("unknownCertificateType"), false));
        } else if (this.profileConfiguration.certificateImportNotAllowedInvalidFields(certificate.getType(), certificate.getTargetVIN(), certificate.getTargetECU()).length != 0) {
            this.addErrorResultAndLogImportNotAllowed(result, holder, certificate, String.join((CharSequence)SPACE, this.profileConfiguration.certificateImportNotAllowedInvalidFields(certificate.getType(), certificate.getTargetVIN(), certificate.getTargetECU())), new String[0]);
        } else if (this.profileConfiguration.isCertificateImportNotAllowed(certificate)) {
            this.addErrorResultAndLogImportNotAllowed(result, holder, certificate, "invalidCertificateType", new String[]{certificate.getType().name()});
        } else if (!this.profileConfiguration.isSubjectValid(certificate)) {
            this.addErrorResult(result, holder, "certSubjDoesNotMatchConfigSubj", new String[]{certificate.getSubject(), certificate.getSerialNo(), certificate.getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier()});
            this.logSubjectNotTheSame(certificate);
        } else if (rolledBackCertificate.isPresent()) {
            this.addErrorResultAndLogImportNotAllowed(result, holder, certificate, "certificateImportNotPossibleRollbackActive", new String[]{((Certificate)rolledBackCertificate.get()).getSubjectKeyIdentifier()});
        } else if (!this.profileConfiguration.isUserRoleValid(certificate)) {
            this.addErrorResult(result, holder, "importInvalidUserRole", new String[]{certificate.getUserRole()});
        } else {
            this.processImport(user, result, holder, certificate, onlyFromPKI);
        }
    }

    protected void processImport(User user, List<ImportResult> result, CertificatePrivateKeyHolder holder, Certificate certificate, boolean onlyFromPKI) {
        Optional<Configuration> deleteExpiredCertsConfiguration;
        List validationResult = this.profileConfiguration.shouldDoExtendedValidation() ? CertificatesValidator.extendedValidation((Certificate)certificate, (Logger)this.logger, (MetadataManager)this.i18n) : CertificatesValidator.basicValidation((Certificate)certificate, ((deleteExpiredCertsConfiguration = this.getDeleteExpiredCertsConfiguration(user)).isPresent() && Boolean.valueOf(deleteExpiredCertsConfiguration.get().getConfigValue()) != false ? 1 : 0) != 0, (Logger)this.logger, (MetadataManager)this.i18n);
        CertificateType certificateType = this.determinateAttributeCertificateTypeForLogging(certificate);
        Optional<Optional> errorResult = validationResult.stream().filter(Optional::isPresent).findFirst();
        if (errorResult.isPresent()) {
            this.extractErrorResults(result, holder, validationResult, certificateType);
        } else {
            this.addCertificateToUserStore(user, result, holder, certificateType, onlyFromPKI);
        }
    }

    protected void addCertificateToUserStore(User user, List<ImportResult> result, CertificatePrivateKeyHolder holder, CertificateType certificateType, boolean onlyFromPKI) {
        Certificate certificate = holder.getCertificate();
        List chainOfTrustErrors = this.chainOfTrustManager.addCertificateToUserStore(user.getCertificates(), holder, onlyFromPKI);
        if (chainOfTrustErrors.isEmpty()) {
            this.repository.update((AbstractEntity)user);
            this.logger.log(Level.INFO, "000117", this.i18n.getEnglishMessage("certificateImportedSuccessfullyDetailMessage", new String[]{this.i18n.getEnglishMessage(certificateType.getLanguageProperty()), certificate.getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier(), certificate.getSerialNo()}), this.getClass().getSimpleName());
            ImportResult importResult = this.factory.getImportResult(holder, this.i18n.getMessage("certificateImportedSuccessfully", new String[]{this.i18n.getMessage(certificateType.getLanguageProperty()), certificate.getSubjectKeyIdentifier()}), true);
            List possibleReplacedCertificates = holder.getPossibleReplacedCertificates();
            if (!possibleReplacedCertificates.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                for (ChainIdentifier chainIdentifier : possibleReplacedCertificates) {
                    builder.append(SPACE).append(this.i18n.getMessage("warningRemovedDuringImport", new String[]{chainIdentifier.getAuthorityKeyIdentifier(), chainIdentifier.getSubjectKeyIdentifier(), chainIdentifier.getSerialNo()}));
                }
                importResult.setMessage(importResult.getMessage() + builder.toString());
            }
            result.add(importResult);
        } else {
            StringBuilder errorMessage = new StringBuilder();
            StringBuilder englishMessages = new StringBuilder();
            chainOfTrustErrors.forEach(error -> {
                errorMessage.append(error.getErrorMessage()).append(SPACE);
                englishMessages.append(this.i18n.getEnglishMessage(error.getMessageId(), error.getMessageArgs()));
            });
            this.logger.log(Level.WARNING, "000118X", this.i18n.getEnglishMessage("chainOfTrustImportingFailed", new String[]{englishMessages.toString()}), this.getClass().getSimpleName());
            result.add(this.factory.getImportResult(holder, errorMessage.toString(), false));
            chainOfTrustErrors.clear();
        }
        this.repository.flush();
    }

    private void errorMesgPKNotAllowed(List<ImportResult> result, CertificatePrivateKeyHolder c) {
        ImportResult invalidImportResult = new ImportResult(c.getFileName(), EMPTY, EMPTY, this.i18n.getMessage("notAllowedToImportPrivateKey", new String[]{c.getCertificate().getSubjectPublicKey(), c.getCertificate().getSubjectKeyIdentifier()}), false);
        result.add(invalidImportResult);
    }

    private void importFromPath(User user, List<Certificate> defaultUserCertificates, Map.Entry<Path, List<Path>> entry) {
        Certificate rootCertificate = this.createCertificateFromResourcePath(user, entry);
        if (this.shouldCheckExpired() && !CertificatesValidator.isExpired((Certificate)rootCertificate, (Logger)this.logger, (MetadataManager)this.i18n)) {
            this.addCheckedBackCertToRoot(user, entry, rootCertificate);
        } else {
            this.addUnCheckedBackCertToRoot(user, entry, rootCertificate);
        }
        defaultUserCertificates.add(rootCertificate);
        user.getCertificates().add(rootCertificate);
        this.repository.create((AbstractEntity)rootCertificate);
    }

    private void addErrorResultAndLogImportNotAllowed(List<ImportResult> result, CertificatePrivateKeyHolder holder, Certificate certificate, String messageId, String[] messageArgs) {
        this.addErrorResult(result, holder, messageId, messageArgs);
        this.profileConfiguration.logCertificateImportNotAllowed(certificate);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     * Enabled unnecessary exception pruning
     */
    private Map<Path, List<Path>> getPathForBaseStoreCertificates() {
        Path topDir = BaseStoreUtil.getBaseStoreTopPath(this.getClass(), (Logger)this.logger);
        HashMap<Path, List<Path>> mappedPaths = new HashMap<Path, List<Path>>();
        DirectoryStream topDirectories = null;
        topDirectories = BaseStoreUtil.getDirectoryStream((Path)topDir, BaseStoreUtil::getRootDirectoriesFilter, (Logger)this.logger);
        topDirectories.forEach(pathParent -> {
            ArrayList<Path> rootCertificatesPaths = new ArrayList<Path>();
            ArrayList<Path> backendFolderCertificatesPaths = new ArrayList<Path>();
            ArrayList backendCertificatesPaths = new ArrayList();
            this.processDirectoryStream((Path)pathParent, (List<Path>)rootCertificatesPaths, BaseStoreUtil::getRootFilesFilter, this.logger);
            if (rootCertificatesPaths.isEmpty()) return;
            Path rootPath = (Path)rootCertificatesPaths.get(0);
            this.processDirectoryStream((Path)pathParent, (List<Path>)backendFolderCertificatesPaths, BaseStoreUtil::getRootDirectoriesFilter, this.logger);
            backendFolderCertificatesPaths.forEach(backendPath -> this.processDirectoryStream((Path)backendPath, backendCertificatesPaths, BaseStoreUtil::getRootFilesFilter, this.logger));
            mappedPaths.put(rootPath, backendCertificatesPaths);
        });
        if (topDirectories == null) return mappedPaths;
        try {
            topDirectories.close();
            return mappedPaths;
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            this.logger.log(Level.WARNING, "000168X", this.i18n.getEnglishMessage("failedToGetDirectoryStream", new String[]{topDir.toString()}), CLASS_NAME);
            return mappedPaths;
        }
        catch (IOException e) {
            try {
                LOG.log(Level.FINEST, e.getMessage(), e);
                this.logger.log(Level.WARNING, "000168X", this.i18n.getEnglishMessage("failedToGetDirectoryStream", new String[]{topDir.toString()}), CLASS_NAME);
                if (topDirectories == null) return mappedPaths;
            }
            catch (Throwable throwable) {
                if (topDirectories == null) throw throwable;
                try {
                    topDirectories.close();
                    throw throwable;
                }
                catch (IOException e2) {
                    LOG.log(Level.FINEST, e2.getMessage(), e2);
                    this.logger.log(Level.WARNING, "000168X", this.i18n.getEnglishMessage("failedToGetDirectoryStream", new String[]{topDir.toString()}), CLASS_NAME);
                }
                throw throwable;
            }
            try {
                topDirectories.close();
                return mappedPaths;
            }
            catch (IOException e3) {
                LOG.log(Level.FINEST, e3.getMessage(), e3);
                this.logger.log(Level.WARNING, "000168X", this.i18n.getEnglishMessage("failedToGetDirectoryStream", new String[]{topDir.toString()}), CLASS_NAME);
                return mappedPaths;
            }
        }
    }

    private void addErrorResult(List<ImportResult> result, CertificatePrivateKeyHolder holder, String messageId, String[] messageArgs) {
        result.add(this.factory.getImportResult(holder, this.i18n.getMessage(messageId, messageArgs), false));
    }

    private void logSubjectNotTheSame(Certificate certificate) {
        this.logger.log(Level.WARNING, "000414X", this.i18n.getEnglishMessage("certSubjDoesNotMatchConfigSubj", new String[]{certificate.getSubject(), certificate.getSerialNo(), certificate.getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier()}), this.getClass().getSimpleName());
    }

    private void extractErrorResults(List<ImportResult> result, CertificatePrivateKeyHolder holder, List<Optional<ValidationError>> validationResult, CertificateType certificateType) {
        String ERROR_SEPARATOR = COMMNA_AND_SPACE;
        StringBuilder buffer = new StringBuilder();
        StringBuilder englishBuffer = new StringBuilder();
        Certificate certificate = holder.getCertificate();
        validationResult.forEach(error -> error.ifPresent(e -> {
            englishBuffer.append(this.i18n.getEnglishMessage(e.getMessageId(), new String[]{e.getSubjectKeyIdentifier()})).append(COMMNA_AND_SPACE);
            buffer.append(e.getErrorMessage().replace("\n", COMMNA_AND_SPACE));
        }));
        String resultErrorMessage = this.i18n.getMessage("certificateStructureValidationFailed", new String[]{this.i18n.getMessage(certificateType.getLanguageProperty()), certificate.getSerialNo(), certificate.getAuthorityKeyIdentifier()}) + buffer.toString().substring(0, buffer.toString().length() - COMMNA_AND_SPACE.length());
        result.add(this.factory.getImportResult(holder, resultErrorMessage, false));
        this.logger.log(Level.WARNING, "000114X", this.i18n.getEnglishMessage("certificateStructureValidationFailed", new String[]{certificate.getType().name(), certificate.getSerialNo(), certificate.getAuthorityKeyIdentifier()}) + englishBuffer.toString().substring(0, englishBuffer.toString().length() - COMMNA_AND_SPACE.length()), this.getClass().getSimpleName());
        validationResult.clear();
    }

    private List<Optional<CertificatePrivateKeyHolder>> getCertificatePrivateKeyOptional(User user, ImportInput input, List<ImportResult> importResults) {
        try {
            List createCertificateList = this.factory.createCertificatePrivateKeyHolder(input.getStream(), input.getBytes(), user, input.isPKCS12(), input.hasPassword() ? input.getPassword() : this.pkcs12DefaultPassword, importResults, input.getFileName());
            if (!createCertificateList.isEmpty()) return createCertificateList;
            if (importResults.isEmpty()) return createCertificateList;
            if (!input.isPKCS12()) return createCertificateList;
            ImportResult invalidItem = importResults.get(importResults.size() - 1);
            String originalMessage = invalidItem.getMessage();
            invalidItem.setMessage(this.i18n.getMessage("cannotReadFile") + MESSAGE_SEPARATOR + HtmlUtils.htmlEscape((String)input.getFileName()) + COMMNA_AND_SPACE + originalMessage);
            return createCertificateList;
        }
        catch (Exception e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            ImportResult invalidImportResult = new ImportResult(input.getFileName(), EMPTY, EMPTY, this.i18n.getMessage("cannotReadFile") + MESSAGE_SEPARATOR + HtmlUtils.htmlEscape((String)input.getFileName()) + ". " + e.getMessage(), false);
            importResults.add(invalidImportResult);
            return Collections.emptyList();
        }
    }

    private Optional<Configuration> getDeleteExpiredCertsConfiguration(User user) {
        return user.getConfigurations().stream().filter(config -> config.getConfigKey().equals(CEBASProperty.DELETE_EXPIRED_CERTS.name())).findFirst();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void processDirectoryStream(Path path, List<Path> pathList, Predicate<Path> predicate, Logger logger) {
        Closeable directoryStream = null;
        try {
            directoryStream = BaseStoreUtil.getDirectoryStream((Path)path, predicate::test, (Logger)logger);
            directoryStream.forEach(pathList::add);
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            logger.log(Level.WARNING, "000169X", this.i18n.getEnglishMessage("failedToProcessDirectoryStream", new String[]{path.toString()}), CLASS_NAME);
        }
        finally {
            try {
                if (directoryStream != null) {
                    directoryStream.close();
                }
            }
            catch (IOException e) {
                LOG.log(Level.FINEST, e.getMessage(), e);
                logger.log(Level.WARNING, "000167X", "cannotCloseDirectoryStreamAfterProcessingImport", CLASS_NAME);
            }
        }
    }

    private CertificateType determinateAttributeCertificateTypeForLogging(Certificate certificate) {
        CertificateType certificateType;
        if (certificate.isSecOCISCert()) {
            certificateType = CertificateType.SEC_OC_IS;
        } else {
            certificateType = certificate.getType();
            if (certificateType != null) return certificateType;
            certificateType = CertificateType.NO_TYPE;
        }
        return certificateType;
    }

    private void restoreFromPath(User user, List<Certificate> restoredUserCertificates, Map.Entry<Path, List<Path>> entry) {
        Certificate rootCertificate = this.createCertificateFromResourcePath(user, entry);
        Optional optional = this.searchEngine.findCertByUserAndSignature(user, rootCertificate.getSignature());
        if (!optional.isPresent()) {
            if (this.shouldCheckExpired() && !CertificatesValidator.isExpired((Certificate)rootCertificate, (Logger)this.logger, (MetadataManager)this.i18n)) {
                this.addCheckedBackCertToRoot(user, entry, rootCertificate);
            } else {
                this.addUnCheckedBackCertToRoot(user, entry, rootCertificate);
            }
            this.addNewRootWithBackCert(user, restoredUserCertificates, rootCertificate);
        } else {
            Certificate userRootCertificate = (Certificate)optional.get();
            List<Certificate> restoredBackCertificates = this.shouldCheckExpired() && !CertificatesValidator.isExpired((Certificate)userRootCertificate, (Logger)this.logger, (MetadataManager)this.i18n) ? this.getCheckedBackCert(user, entry, userRootCertificate) : this.getUnCheckedBackCert(entry, userRootCertificate, user);
            this.addRestoredBackCertForExistingRoot(user, restoredUserCertificates, userRootCertificate, restoredBackCertificates);
        }
    }

    private void addUnCheckedBackCertToRoot(User user, Map.Entry<Path, List<Path>> entry, Certificate rootCertificate) {
        List<Certificate> backendCertificates = this.getUnCheckedBackCert(entry, rootCertificate, user);
        rootCertificate.getChildren().addAll(backendCertificates);
    }

    private void addCheckedBackCertToRoot(User user, Map.Entry<Path, List<Path>> entry, Certificate rootCertificate) {
        List<Certificate> unExpiredBackCertificates = this.getCheckedBackCert(user, entry, rootCertificate);
        rootCertificate.getChildren().addAll(unExpiredBackCertificates);
    }

    private void addRestoredBackCertForExistingRoot(User user, List<Certificate> restoredUserCertificates, Certificate userRootCertificate, List<Certificate> unExpiredBackCertificates) {
        List toBeAdded = unExpiredBackCertificates.stream().filter(certificate -> !this.searchEngine.findCertByUserAndSignature(user, certificate.getSignature()).isPresent()).collect(Collectors.toList());
        userRootCertificate.getChildren().addAll(toBeAdded);
        restoredUserCertificates.addAll(toBeAdded);
        this.repository.update((AbstractEntity)userRootCertificate);
    }

    private List<Certificate> getCheckedBackCert(User user, Map.Entry<Path, List<Path>> entry, Certificate userRootCertificate) {
        List<Certificate> backendCertificates = this.getUnCheckedBackCert(entry, userRootCertificate, user);
        return backendCertificates.stream().filter(certificate -> !CertificatesValidator.isExpired((Certificate)certificate, (Logger)this.logger, (MetadataManager)this.i18n)).collect(Collectors.toList());
    }

    private List<Certificate> getUnCheckedBackCert(Map.Entry<Path, List<Path>> entry, Certificate rootCertificate, User user) {
        return entry.getValue().stream().map(path -> this.pathToBackend(rootCertificate, user, (Path)path)).collect(Collectors.toList());
    }

    private Certificate pathToBackend(Certificate rootCertificate, User user, Path path) {
        String resource = path.toAbsolutePath().toString();
        if (!"file".equals(path.toUri().getScheme())) {
            resource = resource.replaceAll("/BOOT-INF/classes/", EMPTY);
        }
        Certificate backendCertificate = this.factory.createCertificate(resource, user);
        backendCertificate.setParent(rootCertificate);
        return this.profileConfiguration.getPkiKnownHandler().updateBackendPkiKnown(backendCertificate);
    }

    private Certificate createCertificateFromResourcePath(User user, Map.Entry<Path, List<Path>> entry) {
        String resourcePath = entry.getKey().toAbsolutePath().toString();
        if (!"file".equals(entry.getKey().toUri().getScheme())) {
            resourcePath = resourcePath.replaceAll("/BOOT-INF/classes/", EMPTY);
        }
        Certificate rootCertificate = this.factory.createCertificate(resourcePath, user);
        rootCertificate.setParent(null);
        return rootCertificate;
    }

    private void addNewRootWithBackCert(User user, List<Certificate> restoredUserCertificates, Certificate rootCertificate) {
        user.getCertificates().add(rootCertificate);
        this.repository.create((AbstractEntity)rootCertificate);
        restoredUserCertificates.add(rootCertificate);
    }

    private boolean shouldCheckExpired() {
        String METHOD_NAME = "shouldCheckExpired";
        this.logger.entering(CLASS_NAME, "shouldCheckExpired");
        this.logger.exiting(CLASS_NAME, "shouldCheckExpired");
        return Boolean.valueOf(this.configurator.readProperty(CEBASProperty.DELETE_EXPIRED_CERTS.name()));
    }
}
