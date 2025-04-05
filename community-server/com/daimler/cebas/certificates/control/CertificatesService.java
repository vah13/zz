/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.CertificateCryptoEngine
 *  com.daimler.cebas.certificates.control.CertificateSignRequestEngine
 *  com.daimler.cebas.certificates.control.CertificateToolsProvider
 *  com.daimler.cebas.certificates.control.DeleteCertificatesEngine
 *  com.daimler.cebas.certificates.control.ImportCertificatesEngine
 *  com.daimler.cebas.certificates.control.ImportSession
 *  com.daimler.cebas.certificates.control.SearchEngine
 *  com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder
 *  com.daimler.cebas.certificates.control.config.CertificatesConfiguration
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException
 *  com.daimler.cebas.certificates.control.exceptions.CertificatesImportException
 *  com.daimler.cebas.certificates.control.exceptions.SignatureCheckException
 *  com.daimler.cebas.certificates.control.exceptions.SystemIntegrityEmptyReportException
 *  com.daimler.cebas.certificates.control.exceptions.ZenzefiCertificateNotFoundForExportPublicKeyFileException
 *  com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory
 *  com.daimler.cebas.certificates.control.hooks.ICertificateHooks
 *  com.daimler.cebas.certificates.control.hooks.NoHook
 *  com.daimler.cebas.certificates.control.repository.CertificateRepository
 *  com.daimler.cebas.certificates.control.validation.CertificatesFieldsValidator
 *  com.daimler.cebas.certificates.control.validation.CertificatesProcessValidation
 *  com.daimler.cebas.certificates.control.validation.SystemIntegrityCheckResult
 *  com.daimler.cebas.certificates.control.validation.SystemIntegrityChecker
 *  com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput
 *  com.daimler.cebas.certificates.control.vo.CertificateSignRequest
 *  com.daimler.cebas.certificates.control.vo.CertificateSummary
 *  com.daimler.cebas.certificates.control.vo.CertificateWithSNAndIssuerSNResult
 *  com.daimler.cebas.certificates.control.vo.CertificateWithSNResult
 *  com.daimler.cebas.certificates.control.vo.DeleteCertificateModel
 *  com.daimler.cebas.certificates.control.vo.DeleteCertificates
 *  com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo
 *  com.daimler.cebas.certificates.control.vo.ExtendedCertificateWithSNAndIssuerSNResult
 *  com.daimler.cebas.certificates.control.vo.ExtendedDeleteCertificatesResult
 *  com.daimler.cebas.certificates.control.vo.ImportResult
 *  com.daimler.cebas.certificates.control.vo.LocalImportInput
 *  com.daimler.cebas.certificates.control.vo.RemoteCertificateImportInput
 *  com.daimler.cebas.certificates.control.vo.SignatureCheckHolder
 *  com.daimler.cebas.certificates.control.zkNoSupport.ZkNoMappingResult
 *  com.daimler.cebas.certificates.control.zkNoSupport.ZkNoSupport
 *  com.daimler.cebas.certificates.entity.BackendContext
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.entity.UserRole
 *  com.daimler.cebas.certificates.entity.parsing.CertificateParser
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.CEBASProperty
 *  com.daimler.cebas.common.control.HexUtil
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASService
 *  com.daimler.cebas.common.control.vo.FilterObject
 *  com.daimler.cebas.configuration.control.AbstractConfigurator
 *  com.daimler.cebas.configuration.control.ConfigurationUtil
 *  com.daimler.cebas.configuration.control.mdc.MdcDecoratorCompletableFuture
 *  com.daimler.cebas.configuration.entity.Configuration
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  com.daimler.cebas.users.entity.User
 *  javax.persistence.Tuple
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.data.domain.Pageable
 *  org.springframework.util.StringUtils
 *  org.springframework.web.multipart.MultipartFile
 *  org.springframework.web.util.HtmlUtils
 */
package com.daimler.cebas.certificates.control;

import com.daimler.cebas.certificates.CertificateCryptoEngine;
import com.daimler.cebas.certificates.control.CertificateSignRequestEngine;
import com.daimler.cebas.certificates.control.CertificateToolsProvider;
import com.daimler.cebas.certificates.control.DeleteCertificatesEngine;
import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
import com.daimler.cebas.certificates.control.ImportSession;
import com.daimler.cebas.certificates.control.SearchEngine;
import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
import com.daimler.cebas.certificates.control.exceptions.CertificatesImportException;
import com.daimler.cebas.certificates.control.exceptions.SignatureCheckException;
import com.daimler.cebas.certificates.control.exceptions.SystemIntegrityEmptyReportException;
import com.daimler.cebas.certificates.control.exceptions.ZenzefiCertificateNotFoundForExportPublicKeyFileException;
import com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory;
import com.daimler.cebas.certificates.control.hooks.ICertificateHooks;
import com.daimler.cebas.certificates.control.hooks.NoHook;
import com.daimler.cebas.certificates.control.repository.CertificateRepository;
import com.daimler.cebas.certificates.control.validation.CertificatesFieldsValidator;
import com.daimler.cebas.certificates.control.validation.CertificatesProcessValidation;
import com.daimler.cebas.certificates.control.validation.SystemIntegrityCheckResult;
import com.daimler.cebas.certificates.control.validation.SystemIntegrityChecker;
import com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput;
import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
import com.daimler.cebas.certificates.control.vo.CertificateSummary;
import com.daimler.cebas.certificates.control.vo.CertificateWithSNAndIssuerSNResult;
import com.daimler.cebas.certificates.control.vo.CertificateWithSNResult;
import com.daimler.cebas.certificates.control.vo.DeleteCertificateModel;
import com.daimler.cebas.certificates.control.vo.DeleteCertificates;
import com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo;
import com.daimler.cebas.certificates.control.vo.ExtendedCertificateWithSNAndIssuerSNResult;
import com.daimler.cebas.certificates.control.vo.ExtendedDeleteCertificatesResult;
import com.daimler.cebas.certificates.control.vo.ImportResult;
import com.daimler.cebas.certificates.control.vo.LocalImportInput;
import com.daimler.cebas.certificates.control.vo.RemoteCertificateImportInput;
import com.daimler.cebas.certificates.control.vo.SignatureCheckHolder;
import com.daimler.cebas.certificates.control.zkNoSupport.ZkNoMappingResult;
import com.daimler.cebas.certificates.control.zkNoSupport.ZkNoSupport;
import com.daimler.cebas.certificates.entity.BackendContext;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.entity.UserRole;
import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.CEBASProperty;
import com.daimler.cebas.common.control.HexUtil;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASService;
import com.daimler.cebas.common.control.vo.FilterObject;
import com.daimler.cebas.configuration.control.AbstractConfigurator;
import com.daimler.cebas.configuration.control.ConfigurationUtil;
import com.daimler.cebas.configuration.control.mdc.MdcDecoratorCompletableFuture;
import com.daimler.cebas.configuration.entity.Configuration;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import com.daimler.cebas.users.entity.User;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

@CEBASService
public abstract class CertificatesService {
    protected static final String USER_ENTITY_ID = "user";
    private static final String CLASS_NAME = CertificatesService.class.getSimpleName();
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(CLASS_NAME);
    protected Logger logger;
    protected CertificateRepository repository;
    protected Session session;
    protected SearchEngine searchEngine;
    protected AbstractCertificateFactory factory;
    protected MetadataManager i18n;
    protected AbstractConfigurator configurator;
    protected CertificatesConfiguration certificateProfileConfiguration;
    protected CertificateSignRequestEngine certificateSignRequestEngine;
    protected ImportCertificatesEngine importCertificatesEngine;
    protected DeleteCertificatesEngine deleteEngine;
    protected ImportSession importSession;
    private SystemIntegrityChecker systemIntegrityCheck;

    @Autowired
    public CertificatesService(CertificateToolsProvider toolsProvider, CertificateRepository repository, Logger logger, Session session, AbstractConfigurator configurator, CertificatesConfiguration certificateProfileConfiguration, ImportSession importSession) {
        this.repository = repository;
        this.logger = logger;
        this.session = session;
        this.searchEngine = toolsProvider.getSearchEngine();
        this.systemIntegrityCheck = toolsProvider.getSystemIntegrityCheck();
        this.factory = toolsProvider.getFactory();
        this.i18n = toolsProvider.getI18n();
        this.configurator = configurator;
        this.certificateProfileConfiguration = certificateProfileConfiguration;
        this.certificateSignRequestEngine = toolsProvider.getCertificateSignRequestEngine();
        this.importCertificatesEngine = toolsProvider.getImporter();
        this.deleteEngine = toolsProvider.getDeleteCertificatesEngine();
        this.importSession = importSession;
    }

    public abstract List<? extends Certificate> getCertificatesCurrentUser();

    protected ZkNoMappingResult resolveZkNOAndSki(ZkNoSupport input, boolean checkBackendExists) {
        String backendSubjectKeyIdentifier;
        String zkNo;
        if (!CertificatesFieldsValidator.isSubjectKeyIdentifier((String)input.getIdentifier())) {
            zkNo = input.getIdentifier();
            ArrayList<String> columns = new ArrayList<String>();
            columns.add("subjectKeyIdentifier");
            List<Tuple> mappings = this.getBackendTupleForIdentifier(zkNo, columns);
            if (mappings.isEmpty()) {
                CertificateNotFoundException exc = new CertificateNotFoundException(this.i18n.getMessage("certificateOfTypeNotFoundForIdentifier", new String[]{this.i18n.getMessage(CertificateType.BACKEND_CA_CERTIFICATE.name()), zkNo, ""}));
                this.logger.logWithExceptionByInfo("000553X", (CEBASException)exc);
                throw exc;
            }
            backendSubjectKeyIdentifier = (String)mappings.get(0).get(0);
        } else {
            zkNo = "";
            backendSubjectKeyIdentifier = HexUtil.base64ToHex((String)input.getIdentifier());
        }
        if (!checkBackendExists) return new ZkNoMappingResult(backendSubjectKeyIdentifier, zkNo);
        this.verifyBackendExists(backendSubjectKeyIdentifier);
        return new ZkNoMappingResult(backendSubjectKeyIdentifier, zkNo);
    }

    private void verifyBackendExists(String backendSubjectKeyIdentifier) {
        Optional backendCertificateOptional = this.searchEngine.findCertificate(this.session.getCurrentUser(), backendSubjectKeyIdentifier, CertificateType.BACKEND_CA_CERTIFICATE);
        if (backendCertificateOptional.isPresent()) return;
        CertificateNotFoundException exc = new CertificateNotFoundException(this.i18n.getMessage("certificateOfTypeNotFound", new String[]{this.i18n.getMessage(CertificateType.BACKEND_CA_CERTIFICATE.name())}));
        this.logger.logWithExceptionByInfo("000483X", (CEBASException)exc);
        throw exc;
    }

    public List<Tuple> getBackendTupleForIdentifier(String identifier, List<String> columns) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("type", CertificateType.BACKEND_CA_CERTIFICATE);
        if (org.apache.commons.lang3.StringUtils.isBlank(identifier)) return this.searchEngine.findTuples(this.session.getCurrentUser(), params, columns, Certificate.class);
        CertificatesFieldsValidator.isIdentifierValid((String)identifier, (MetadataManager)this.i18n, (Logger)this.logger);
        if (CertificatesFieldsValidator.isSubjectKeyIdentifier((String)identifier)) {
            params.put("subjectKeyIdentifier", HexUtil.base64ToHex((String)identifier));
        } else {
            params.put("zkNo", identifier);
        }
        return this.searchEngine.findTuples(this.session.getCurrentUser(), params, columns, Certificate.class);
    }

    public List<ZkNoMappingResult> getZkNoMapping(String identifier) {
        List<String> columns = this.getColumnsForIdentifierSearch();
        List<Tuple> tuples = this.getBackendTupleForIdentifier(identifier, columns);
        return ZkNoSupport.getZkNoMapping((String)identifier, tuples, (Logger)this.logger);
    }

    protected List<String> getColumnsForIdentifierSearch() {
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("subjectKeyIdentifier");
        columns.add("zkNo");
        return columns;
    }

    public Number countCertificatesCurrentUser(Map<String, FilterObject> filters) {
        String METHOD_NAME = "countCertificatesCurrentUser";
        this.logger.entering(CLASS_NAME, "countCertificatesCurrentUser");
        filters.put(USER_ENTITY_ID, new FilterObject(true, (Object)this.session.getCurrentUser()));
        this.logger.exiting(CLASS_NAME, "countCertificatesCurrentUser");
        return this.repository.countFilter(filters);
    }

    public Number countRootCertificatesCurrentUser() {
        String METHOD_NAME = "countCertificatesCurrentUser";
        this.logger.entering(CLASS_NAME, "countCertificatesCurrentUser");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(USER_ENTITY_ID, this.session.getCurrentUser());
        map.put("type", CertificateType.ROOT_CA_CERTIFICATE);
        this.logger.exiting(CLASS_NAME, "countCertificatesCurrentUser");
        return this.repository.countWithQuery(Certificate.class, map);
    }

    public List<Certificate> getCertificates(String parent) {
        String METHOD_NAME = "getCertificates";
        this.logger.entering(CLASS_NAME, "getCertificates");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("entityId", parent);
        List parents = this.repository.findWithNamedQuery("parent", map, 0);
        this.logger.exiting(CLASS_NAME, "getCertificates");
        return parents;
    }

    public List<DeleteCertificatesInfo> deleteCertificates(List<String> ids) {
        return this.deleteEngine.deleteCertificates(ids);
    }

    public List<ExtendedDeleteCertificatesResult> deleteCertificatesAdditionalLogging(List<String> ids) {
        return this.deleteEngine.deleteCertificatesAdditionalLogging(ids);
    }

    public List<DeleteCertificatesInfo> deleteCertificates(List<String> ids, User currentUser) {
        return this.deleteEngine.deleteCertificates(ids, currentUser);
    }

    public List<ExtendedDeleteCertificatesResult> deleteCertificates(DeleteCertificates deleteCertificate) {
        String METHOD_NAME = "deleteCertificates";
        this.logger.entering(CLASS_NAME, "deleteCertificates");
        List deletedCertificatesList = this.deleteEngine.deleteCertificates(deleteCertificate, this.getCertificatesCurrentUser(), (ICertificateHooks)new NoHook());
        this.logger.exiting(CLASS_NAME, "deleteCertificates");
        return deletedCertificatesList;
    }

    public Certificate createCertificateInSignRequestState(CertificateSignRequest certificateSignRequest, boolean checkCurrentUser) {
        String METHOD_NAME = "createCertificateInSignRequestState";
        this.logger.entering(CLASS_NAME, "createCertificateInSignRequestState");
        Certificate result = this.certificateSignRequestEngine.createCertificateInSignRequestState(certificateSignRequest, checkCurrentUser, ValidationFailureOutput::outputFailureWithThrow, true);
        this.logger.exiting(CLASS_NAME, "createCertificateInSignRequestState");
        return result;
    }

    public Certificate getCertificateFromCSRId(String csrId) {
        String METHOD_NAME = "getCertificateFromCSRId";
        this.logger.entering(CLASS_NAME, "getCertificateFromCSRId");
        CertificatesProcessValidation.validateGetCertificateFromCSRId((String)csrId, (MetadataManager)this.i18n, (Logger)this.logger);
        Optional certificateOptional = this.repository.findCertificate(csrId);
        if (certificateOptional.isPresent()) {
            Certificate certificate = (Certificate)certificateOptional.get();
            this.logger.exiting(CLASS_NAME, "getCertificateFromCSRId");
            return certificate;
        }
        ZenzefiCertificateNotFoundForExportPublicKeyFileException certificateNotFoundException = new ZenzefiCertificateNotFoundForExportPublicKeyFileException(this.i18n.getMessage("certificateDoesNotExistInUserStore", new String[]{HtmlUtils.htmlEscape((String)csrId)}), "certificateDoesNotExistInUserStore");
        this.logger.logWithTranslation(Level.WARNING, "000020X", certificateNotFoundException.getMessageId(), new String[]{HtmlUtils.htmlEscape((String)csrId)}, certificateNotFoundException.getClass().getSimpleName());
        throw certificateNotFoundException;
    }

    public List<ImportResult> importCertificatesFromLocal(List<LocalImportInput> inputs) {
        String METHOD_NAME = "importCertificatesFromLocal";
        this.logger.entering(CLASS_NAME, "importCertificatesFromLocal");
        this.importSession.run();
        try {
            List list = (List)MdcDecoratorCompletableFuture.supplyAsync(() -> this.importCertificatesEngine.importCertificatesFromLocal(inputs)).get();
            return list;
        }
        catch (ExecutionException e) {
            if (CEBASException.hasCEBASExceptionCause((Throwable)e)) {
                throw (RuntimeException)e.getCause();
            }
            this.logger.log(Level.INFO, "000583", e.getCause().getMessage(), this.getClass().getSimpleName());
            throw new CertificatesImportException(e.getCause().getMessage());
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            this.logger.log(Level.INFO, "000584", e.getCause().getMessage(), this.getClass().getSimpleName());
            throw new CertificatesImportException(e.getCause().getMessage());
        }
        finally {
            this.importSession.setNotRunning();
            this.logger.exiting(CLASS_NAME, "importCertificatesFromLocal");
        }
    }

    public List<ImportResult> importCertificates(List<MultipartFile> files) {
        String METHOD_NAME = "importCertificates";
        this.logger.entering(CLASS_NAME, "importCertificates");
        this.importSession.run();
        try {
            List list = (List)MdcDecoratorCompletableFuture.supplyAsync(() -> this.importCertificatesEngine.importCertificates(files, this.allowImportOfPrivateKeys())).get();
            return list;
        }
        catch (ExecutionException e) {
            if (CEBASException.hasCEBASExceptionCause((Throwable)e)) {
                throw (RuntimeException)e.getCause();
            }
            this.logger.log(Level.INFO, "000583", e.getCause().getMessage(), this.getClass().getSimpleName());
            throw new CertificatesImportException(e.getCause().getMessage());
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            this.logger.log(Level.INFO, "000584", e.getCause().getMessage(), this.getClass().getSimpleName());
            throw new CertificatesImportException(e.getCause().getMessage());
        }
        finally {
            this.importSession.setNotRunning();
            this.logger.exiting(CLASS_NAME, "importCertificates");
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<ImportResult> importRemoteCertificates(List<RemoteCertificateImportInput> remoteInputs, boolean onlyFromPKI) {
        String METHOD_NAME = "importRemoteCertificates";
        this.logger.entering(CLASS_NAME, "importRemoteCertificates");
        this.importSession.run();
        try {
            List list = this.importCertificatesEngine.importRemoteCertificates(remoteInputs, false, this.allowImportOfPrivateKeys());
            return list;
        }
        finally {
            this.importSession.setNotRunning();
            this.logger.exiting(CLASS_NAME, "importRemoteCertificates");
        }
    }

    public SystemIntegrityCheckResult checkSystemIntegrity() {
        String METHOD_NAME = "checkSystemIntegrity";
        this.logger.entering(CLASS_NAME, "checkSystemIntegrity");
        this.systemIntegrityCheck.checkSystemIntegrity(this.session.getCurrentUser(), this.session.getSystemIntegrityCheckResult());
        this.logger.exiting(CLASS_NAME, "checkSystemIntegrity");
        return this.session.getSystemIntegrityCheckResult();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void writeSystemIntegrityReportToOutputStream(OutputStream outputStream) {
        String METHOD_NAME = "writeSystemIntegrityReportToOutputStream";
        this.logger.entering(CLASS_NAME, "writeSystemIntegrityReportToOutputStream");
        String xmlReport = this.session.getSystemIntegrityCheckResult().getIntegrityCheckXML();
        if (xmlReport == null || xmlReport.isEmpty()) {
            SystemIntegrityEmptyReportException reportNotFoundException = new SystemIntegrityEmptyReportException(this.i18n.getMessage("systemIntegrityReportNotFound"), "systemIntegrityReportNotFound");
            this.logger.logWithTranslation(Level.WARNING, "000064X", reportNotFoundException.getMessageId(), reportNotFoundException.getClass().getSimpleName());
            throw reportNotFoundException;
        }
        try {
            outputStream.write(xmlReport.getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            this.logger.logWithTranslation(Level.WARNING, "000063X", "cannotDownloadSystemIntegrityReport", CLASS_NAME);
        }
        finally {
            this.closeStream(outputStream);
        }
        this.logger.exiting(CLASS_NAME, "writeSystemIntegrityReportToOutputStream");
    }

    public boolean isSystemIntegrityLogExistent() {
        String METHOD_NAME = "isSystemIntegrityLogExistent";
        this.logger.entering(CLASS_NAME, "isSystemIntegrityLogExistent");
        this.logger.exiting(CLASS_NAME, "isSystemIntegrityLogExistent");
        return !this.session.getSystemIntegrityCheckResult().getIntegrityCheckXML().isEmpty();
    }

    public CertificateWithSNResult getDiagCert(String backendCertSkid) {
        return this.getDiagCert(backendCertSkid, null, null, (byte)0);
    }

    public CertificateWithSNResult getDiagCert(String backendCertSkid, String vin) {
        return this.getDiagCert(backendCertSkid, vin, null, (byte)0);
    }

    public CertificateWithSNResult getDiagCert(String backendCertSkid, byte userRole) {
        return this.getDiagCert(backendCertSkid, null, null, userRole);
    }

    public CertificateWithSNResult getDiagCert(String backendCertSkid, String vin, String ecu) {
        return this.getDiagCert(backendCertSkid, vin, ecu, (byte)0);
    }

    public CertificateWithSNResult getDiagCert(String backendCertSkid, String vin, String ecu, byte role) {
        String METHOD_NAME = "getDiagCert";
        this.logger.entering(CLASS_NAME, "getDiagCert");
        UserRole userRoleFromByte = UserRole.getUserRoleFromByte((byte)role);
        String userRole = userRoleFromByte.getText();
        Certificate certificateResult = this.getDiagnosticCert(backendCertSkid, vin, ecu, userRole);
        CertificateWithSNResult diagCert = new CertificateWithSNResult(this.factory.getCertificateBytes(certificateResult), CertificateParser.hexStringToByteArray((String)certificateResult.getSerialNo()));
        this.logger.log(Level.INFO, "000047", this.i18n.getEnglishMessage("foundDiagCertWithParams", new String[]{diagCert.getCertificateData(), diagCert.getSerialNumber(), backendCertSkid, ecu, vin, userRole, this.composeMessageForCurrentUser()}), CLASS_NAME);
        this.logger.exiting(CLASS_NAME, "getDiagCert");
        return diagCert;
    }

    public boolean checkActiveDiagCert(String backendCertSkid, String diagCertSerialNo, String targetEcu, String targetVin, byte role) {
        String METHOD_NAME = "checkActiveDiagCert";
        this.logger.entering(CLASS_NAME, "checkActiveDiagCert");
        boolean result = false;
        CertificatesProcessValidation.validateCheckActiveDiagCert((String)backendCertSkid, (String)diagCertSerialNo, (String)targetEcu, (String)targetVin, (MetadataManager)this.i18n, (Logger)this.logger);
        Configuration certSelectionConfiguration = ConfigurationUtil.getConfigurationForUser((User)this.session.getCurrentUser(), (CEBASProperty)CEBASProperty.CERT_SELECTION, (Logger)this.logger, (MetadataManager)this.i18n);
        if (ConfigurationUtil.isAutomaticSelection((Configuration)certSelectionConfiguration)) {
            UserRole userRoleFromByte = UserRole.getUserRoleFromByte((byte)role);
            String userRole = userRoleFromByte.getText();
            Optional certificateOptional = this.searchEngine.findValidDiagnosticCertificate(this.session.getCurrentUser(), backendCertSkid, targetVin, targetEcu, userRole);
            if (certificateOptional.isPresent()) {
                String querySerialNo;
                Certificate certificate = (Certificate)certificateOptional.get();
                String certificateSerialNo = HexUtil.omitLeadingZeros((String)certificate.getSerialNo());
                if (certificateSerialNo.equals(querySerialNo = HexUtil.omitLeadingZeros((String)diagCertSerialNo))) {
                    this.logger.logWithTranslation(Level.INFO, "000061", "foundDiagCertificateForCriteria", CLASS_NAME);
                    result = true;
                } else {
                    this.logger.logWithTranslation(Level.WARNING, "000061X", "noCertificateFoundMatchingTheFilterCriteriaDifferentSN", CLASS_NAME);
                }
            } else {
                this.logger.logWithTranslation(Level.WARNING, "000061X", "noCertificateFoundMatchingTheFilterCriteriaDifferentSN", CLASS_NAME);
            }
        } else {
            this.logger.logWithTranslation(Level.WARNING, "000061X", "activeDiagManualSelection", CLASS_NAME);
        }
        this.logger.exiting(CLASS_NAME, "checkActiveDiagCert");
        return result;
    }

    public List<ExtendedCertificateWithSNAndIssuerSNResult> getEnhDiagCert(String backendCertSkid, String diagCertSerialNo, String vin, String ecu) {
        String METHOD_NAME = "getEnhDiagCert";
        this.logger.entering(CLASS_NAME, "getEnhDiagCert");
        CertificatesProcessValidation.validateGetEnhDiagCert((String)backendCertSkid, (String)diagCertSerialNo, (String)vin, (String)ecu, (MetadataManager)this.i18n, (Logger)this.logger);
        Configuration certSelectionConfiguration = ConfigurationUtil.getConfigurationForUser((User)this.session.getCurrentUser(), (CEBASProperty)CEBASProperty.CERT_SELECTION, (Logger)this.logger, (MetadataManager)this.i18n);
        List certificatesResult = ConfigurationUtil.isAutomaticSelection((Configuration)certSelectionConfiguration) ? this.searchEngine.findValidEnhancedDiagnosticCertificates(this.session.getCurrentUser(), backendCertSkid, diagCertSerialNo, vin, ecu) : this.searchEngine.findEnhancedDiagnosticCertificatesActiveForTest(this.session.getCurrentUser());
        if (certificatesResult.isEmpty()) {
            CertificateNotFoundException ex = new CertificateNotFoundException(this.i18n.getMessage("noCertificateFoundMatchingCriteriaEnhanced", new String[]{backendCertSkid, ecu, vin, diagCertSerialNo}), "noCertificateFoundMatchingCriteriaEnhanced");
            this.logger.logWithTranslation(Level.WARNING, "000123X", ex.getMessageId(), new String[]{backendCertSkid, ecu, vin, diagCertSerialNo}, ex.getClass().getSimpleName());
            throw ex;
        }
        this.logger.logWithTranslation(Level.INFO, "000498", "foundEnhCertificateForCriteria", new String[]{"" + certificatesResult.size()}, CLASS_NAME);
        this.logger.exiting(CLASS_NAME, "getEnhDiagCert");
        return certificatesResult.stream().map(c -> {
            CertificateWithSNAndIssuerSNResult certificateWithSNAndIssuerSNResult = new CertificateWithSNAndIssuerSNResult(this.factory.getCertificateBytes(c), CertificateParser.hexStringToByteArray((String)c.getSerialNo()), CertificateParser.hexStringToByteArray((String)c.getIssuerSerialNumber()));
            BackendContext backendContext = c.getBackendContext();
            return new ExtendedCertificateWithSNAndIssuerSNResult(certificateWithSNAndIssuerSNResult, backendContext.getZkNo());
        }).collect(Collectors.toList());
    }

    public boolean signatureCheck(SignatureCheckHolder signatureCheck) {
        boolean response;
        String METHOD_NAME = "signatureCheck";
        this.logger.entering(CLASS_NAME, "signatureCheck");
        if (StringUtils.isEmpty((Object)signatureCheck.getMessage()) || StringUtils.isEmpty((Object)signatureCheck.getEcuCertificate()) || StringUtils.isEmpty((Object)signatureCheck.getSignature())) {
            SignatureCheckException exception = new SignatureCheckException(this.i18n.getMessage("wrongInputAllParamsMandatory"), "wrongInputAllParamsMandatory");
            this.logger.logWithTranslation(Level.WARNING, "000119X", exception.getMessageId(), exception.getClass().getSimpleName());
            throw exception;
        }
        byte[] messageRaw = Base64.getDecoder().decode(signatureCheck.getMessage());
        byte[] signatureRaw = Base64.getDecoder().decode(signatureCheck.getSignature());
        Certificate certificateToCheck = this.factory.getCertificateFromBase64(signatureCheck.getEcuCertificate());
        CertificatePrivateKeyHolder holder = new CertificatePrivateKeyHolder(certificateToCheck, Optional.empty());
        ImportResult checkImportIntoChain = this.importCertificatesEngine.checkImportIntoChain(holder);
        if (checkImportIntoChain.isSuccess()) {
            try {
                response = CertificateCryptoEngine.checkMessageSignature((PublicKey)certificateToCheck.getCertificateData().getCert().getPublicKey(), (byte[])messageRaw, (byte[])signatureRaw);
            }
            catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException e) {
                LOG.log(Level.FINEST, e.getMessage(), e);
                SignatureCheckException ex = new SignatureCheckException(this.i18n.getMessage("couldNotVerifySignatureForMessage"), "couldNotVerifySignatureForMessage");
                this.logger.logWithTranslation(Level.WARNING, "000067X", ex.getMessageId(), ex.getClass().getSimpleName());
                throw ex;
            }
        } else {
            this.logger.logWithTranslation(Level.WARNING, "000102X", "couldNotVerifySignature", new String[]{checkImportIntoChain.getMessage()}, CLASS_NAME);
            response = false;
        }
        this.logger.exiting(CLASS_NAME, "signatureCheck");
        return response;
    }

    public void writePublicKeyToOutputStream(Certificate certificate, OutputStream outputStream) {
        String METHOD_NAME = "writePublicKeyToOutputStream";
        this.logger.entering(CLASS_NAME, "writePublicKeyToOutputStream");
        try {
            String publicKey = certificate.getSubjectPublicKey();
            byte[] hexStringToByteArray = CertificateParser.hexStringToByteArray((String)publicKey);
            outputStream.write(hexStringToByteArray);
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            CEBASException exception = new CEBASException(this.i18n.getMessage("writingPublicKeyToFileFailed", new String[]{e.getMessage()}), "writingPublicKeyToFileFailed");
            this.logger.logWithTranslation(Level.WARNING, "000018X", exception.getMessageId(), new String[]{e.getMessage()}, exception.getClass().getSimpleName());
            throw exception;
        }
        finally {
            this.closeStream(outputStream);
        }
        this.logger.exiting(CLASS_NAME, "writePublicKeyToOutputStream");
    }

    public <T extends Certificate> List<? extends CertificateSummary> listCertificateSummary(Class<T> type, boolean all) {
        String METHOD_NAME = "listCertificateSummary";
        this.logger.entering(CLASS_NAME, "listCertificateSummary");
        List userCertificates = this.searchEngine.findCertificatesByUser(type, this.session.getCurrentUser(), all, null);
        this.logger.log(Level.INFO, "000448", this.i18n.getEnglishMessage("returnCertificateList"), this.getClass().getSimpleName());
        this.logger.exiting(CLASS_NAME, "listCertificateSummary");
        return this.certificateProfileConfiguration.convertToCertificateSummary(userCertificates);
    }

    public <T extends Certificate> List<? extends CertificateSummary> listPaginatedCertificateSummary(Class<T> type, boolean all, Pageable pageable) {
        String METHOD_NAME = "listCertificateSummary";
        this.logger.entering(CLASS_NAME, "listCertificateSummary");
        List userCertificates = this.searchEngine.findCertificatesByUser(type, this.session.getCurrentUser(), all, pageable);
        this.logger.log(Level.INFO, "000448", this.i18n.getEnglishMessage("returnCertificateList"), this.getClass().getSimpleName());
        this.logger.exiting(CLASS_NAME, "listCertificateSummary");
        return this.certificateProfileConfiguration.convertToCertificateSummary(userCertificates);
    }

    public List<String> restoreCertificates() {
        String METHOD_NAME = "restoreCertificates";
        this.logger.entering(CLASS_NAME, "restoreCertificates");
        this.session.getSystemIntegrityCheckResult().clear();
        ArrayList<? extends Certificate> flattenedCertificates = new ArrayList<Certificate>();
        ArrayList restoredStoreCertificates = new ArrayList();
        User currentUser = this.session.getCurrentUser();
        if (this.countRootCertificatesCurrentUser().intValue() == 0) {
            this.importCertificatesEngine.importFromBaseStore(currentUser);
            flattenedCertificates.addAll(this.getCertificatesCurrentUser());
        } else {
            restoredStoreCertificates.addAll(this.importCertificatesEngine.restoreCertificatesFromBaseStore(currentUser));
        }
        restoredStoreCertificates.forEach(root -> flattenedCertificates.addAll(root.flattened().collect(Collectors.toList())));
        this.logger.log(Level.INFO, "000137", this.i18n.getEnglishMessage("restoredCertificates", new String[]{Integer.toString(flattenedCertificates.size())}), CLASS_NAME);
        this.logger.exiting(CLASS_NAME, "restoreCertificates");
        return flattenedCertificates.stream().map(Certificate::getSignatureBase64).collect(Collectors.toList());
    }

    /*
     * Enabled force condition propagation
     */
    protected Certificate getDiagnosticCert(String backendCertSkid, String vin, String ecu, String userRole) {
        CertificatesProcessValidation.validateGetDiagCert((String)backendCertSkid, (String)vin, (String)ecu, (MetadataManager)this.i18n, (Logger)this.logger);
        Configuration certSelectionConfiguration = ConfigurationUtil.getConfigurationForUser((User)this.session.getCurrentUser(), (CEBASProperty)CEBASProperty.CERT_SELECTION, (Logger)this.logger, (MetadataManager)this.i18n);
        User currentUser = this.session.getCurrentUser();
        if (!ConfigurationUtil.isAutomaticSelection((Configuration)certSelectionConfiguration)) {
            return this.searchEngine.findCertificateActiveForTesting(currentUser, CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE);
        }
        Optional certificate = this.searchEngine.findValidDiagnosticCertificate(currentUser, backendCertSkid, vin, ecu, userRole);
        if (certificate.isPresent()) {
            return (Certificate)certificate.get();
        }
        CertificateNotFoundException ex = new CertificateNotFoundException(this.i18n.getEnglishMessage("noCertificateFoundMatchingCriteriaBasic", new String[]{backendCertSkid, ecu, vin, userRole.equals(UserRole.NO_ROLE.getText()) ? "" : userRole}));
        this.logger.logWithExceptionByWarning("000046", (CEBASException)ex);
        throw ex;
    }

    protected <T extends Certificate> T getCertByAuthKeyIdentAndSerialNo(String authorityKeyIdentifier, String serialNo, Class<T> clazz) {
        CertificatesProcessValidation.validateGetCertByAuthKeyIdentAndSerialNo((String)authorityKeyIdentifier, (String)serialNo, (MetadataManager)this.i18n, (Logger)this.logger);
        User currentUser = this.session.getCurrentUser();
        List certificates = this.searchEngine.findCertByAuthKeyIdentAndSerialNo(currentUser, authorityKeyIdentifier, serialNo, clazz);
        if (!certificates.isEmpty()) return (T)((Certificate)certificates.get(0));
        CertificateNotFoundException ex = new CertificateNotFoundException(this.i18n.getMessage("noCertificateFoundWithGivenAKIAndSN"), "noCertificateFoundWithGivenAKIAndSN");
        this.logger.logWithTranslation(Level.WARNING, "000059X", ex.getMessageId(), ex.getClass().getSimpleName());
        throw ex;
    }

    protected void closeStream(Closeable stream) {
        try {
            if (stream == null) return;
            stream.close();
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            this.logger.log(Level.WARNING, "000173X", "cannotCloseStreamOnCertificateOperations", CLASS_NAME);
        }
    }

    protected abstract boolean allowImportOfPrivateKeys();

    public String composeMessageForCurrentUser() {
        String currentUserName = this.session.isDefaultUser() ? this.i18n.getMessage("defaultUser") : this.session.getCurrentUser().getUserName();
        return currentUserName;
    }

    protected Certificate getRequestedCertificate(String certificateId) {
        CertificatesProcessValidation.validateGetCertificate((String)certificateId, (MetadataManager)this.i18n, (Logger)this.logger);
        Optional optional = this.repository.findCertificate(certificateId);
        return (Certificate)optional.orElseThrow(() -> new CEBASCertificateException(this.i18n.getMessage("certificateDoesNotExist")));
    }

    public DeleteCertificateModel getDeleteCertificateModel(DeleteCertificateModel model) {
        String skiBase64 = model.getAuthorityKeyIdentifier();
        if (CertificatesFieldsValidator.isSubjectKeyIdentifier((String)skiBase64)) return new DeleteCertificateModel(skiBase64, model.getSerialNo());
        String skiHex = this.getHexBackendSKI(skiBase64);
        skiBase64 = Base64.getEncoder().encodeToString(HexUtil.hexStringToByteArray((String)skiHex));
        return new DeleteCertificateModel(skiBase64, model.getSerialNo());
    }

    public String getHexBackendSKI(String backendSKI) {
        return this.resolveZkNOAndSki(backendSKI::toString, !CertificatesFieldsValidator.isSubjectKeyIdentifier((String)backendSKI)).getSki();
    }
}
