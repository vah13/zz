/*      */ package com.daimler.cebas.certificates.control;
/*      */ 
/*      */ import com.daimler.cebas.certificates.CertificateCryptoEngine;
/*      */ import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
/*      */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*      */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*      */ import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
/*      */ import com.daimler.cebas.certificates.control.exceptions.CertificatesImportException;
/*      */ import com.daimler.cebas.certificates.control.exceptions.SignatureCheckException;
/*      */ import com.daimler.cebas.certificates.control.exceptions.SystemIntegrityEmptyReportException;
/*      */ import com.daimler.cebas.certificates.control.exceptions.ZenzefiCertificateNotFoundForExportPublicKeyFileException;
/*      */ import com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory;
/*      */ import com.daimler.cebas.certificates.control.hooks.ICertificateHooks;
/*      */ import com.daimler.cebas.certificates.control.hooks.NoHook;
/*      */ import com.daimler.cebas.certificates.control.repository.CertificateRepository;
/*      */ import com.daimler.cebas.certificates.control.validation.CertificatesFieldsValidator;
/*      */ import com.daimler.cebas.certificates.control.validation.CertificatesProcessValidation;
/*      */ import com.daimler.cebas.certificates.control.validation.SystemIntegrityCheckResult;
/*      */ import com.daimler.cebas.certificates.control.validation.SystemIntegrityChecker;
/*      */ import com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput;
/*      */ import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
/*      */ import com.daimler.cebas.certificates.control.vo.CertificateSummary;
/*      */ import com.daimler.cebas.certificates.control.vo.CertificateWithSNAndIssuerSNResult;
/*      */ import com.daimler.cebas.certificates.control.vo.CertificateWithSNResult;
/*      */ import com.daimler.cebas.certificates.control.vo.DeleteCertificateModel;
/*      */ import com.daimler.cebas.certificates.control.vo.DeleteCertificates;
/*      */ import com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo;
/*      */ import com.daimler.cebas.certificates.control.vo.ExtendedCertificateWithSNAndIssuerSNResult;
/*      */ import com.daimler.cebas.certificates.control.vo.ExtendedDeleteCertificatesResult;
/*      */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*      */ import com.daimler.cebas.certificates.control.vo.LocalImportInput;
/*      */ import com.daimler.cebas.certificates.control.vo.RemoteCertificateImportInput;
/*      */ import com.daimler.cebas.certificates.control.vo.SignatureCheckHolder;
/*      */ import com.daimler.cebas.certificates.control.zkNoSupport.ZkNoMappingResult;
/*      */ import com.daimler.cebas.certificates.control.zkNoSupport.ZkNoSupport;
/*      */ import com.daimler.cebas.certificates.entity.BackendContext;
/*      */ import com.daimler.cebas.certificates.entity.Certificate;
/*      */ import com.daimler.cebas.certificates.entity.CertificateType;
/*      */ import com.daimler.cebas.certificates.entity.UserRole;
/*      */ import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
/*      */ import com.daimler.cebas.common.control.CEBASException;
/*      */ import com.daimler.cebas.common.control.CEBASProperty;
/*      */ import com.daimler.cebas.common.control.HexUtil;
/*      */ import com.daimler.cebas.common.control.MetadataManager;
/*      */ import com.daimler.cebas.common.control.annotations.CEBASService;
/*      */ import com.daimler.cebas.common.control.vo.FilterObject;
/*      */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*      */ import com.daimler.cebas.configuration.control.ConfigurationUtil;
/*      */ import com.daimler.cebas.configuration.control.mdc.MdcDecoratorCompletableFuture;
/*      */ import com.daimler.cebas.configuration.entity.Configuration;
/*      */ import com.daimler.cebas.logs.control.Logger;
/*      */ import com.daimler.cebas.users.control.Session;
/*      */ import com.daimler.cebas.users.entity.User;
/*      */ import java.io.Closeable;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.security.GeneralSecurityException;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Base64;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import java.util.stream.Collectors;
/*      */ import javax.persistence.Tuple;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ import org.springframework.beans.factory.annotation.Autowired;
/*      */ import org.springframework.data.domain.Pageable;
/*      */ import org.springframework.util.StringUtils;
/*      */ import org.springframework.web.multipart.MultipartFile;
/*      */ import org.springframework.web.util.HtmlUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @CEBASService
/*      */ public abstract class CertificatesService
/*      */ {
/*      */   protected static final String USER_ENTITY_ID = "user";
/*   96 */   private static final String CLASS_NAME = CertificatesService.class.getSimpleName();
/*   97 */   private static final Logger LOG = Logger.getLogger(CLASS_NAME);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Logger logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CertificateRepository repository;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Session session;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected SearchEngine searchEngine;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractCertificateFactory factory;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected MetadataManager i18n;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractConfigurator configurator;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CertificatesConfiguration certificateProfileConfiguration;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CertificateSignRequestEngine certificateSignRequestEngine;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ImportCertificatesEngine importCertificatesEngine;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected DeleteCertificatesEngine deleteEngine;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ImportSession importSession;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SystemIntegrityChecker systemIntegrityCheck;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Autowired
/*      */   public CertificatesService(CertificateToolsProvider toolsProvider, CertificateRepository repository, Logger logger, Session session, AbstractConfigurator configurator, CertificatesConfiguration certificateProfileConfiguration, ImportSession importSession) {
/*  172 */     this.repository = repository;
/*  173 */     this.logger = logger;
/*  174 */     this.session = session;
/*  175 */     this.searchEngine = toolsProvider.getSearchEngine();
/*  176 */     this.systemIntegrityCheck = toolsProvider.getSystemIntegrityCheck();
/*  177 */     this.factory = toolsProvider.getFactory();
/*  178 */     this.i18n = toolsProvider.getI18n();
/*  179 */     this.configurator = configurator;
/*  180 */     this.certificateProfileConfiguration = certificateProfileConfiguration;
/*  181 */     this.certificateSignRequestEngine = toolsProvider.getCertificateSignRequestEngine();
/*  182 */     this.importCertificatesEngine = toolsProvider.getImporter();
/*  183 */     this.deleteEngine = toolsProvider.getDeleteCertificatesEngine();
/*  184 */     this.importSession = importSession;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ZkNoMappingResult resolveZkNOAndSki(ZkNoSupport input, boolean checkBackendExists) {
/*      */     String zkNo;
/*      */     String backendSubjectKeyIdentifier;
/*  208 */     if (!CertificatesFieldsValidator.isSubjectKeyIdentifier(input.getIdentifier())) {
/*  209 */       zkNo = input.getIdentifier();
/*  210 */       List<String> columns = new ArrayList<>();
/*  211 */       columns.add("subjectKeyIdentifier");
/*      */       
/*  213 */       List<Tuple> mappings = getBackendTupleForIdentifier(zkNo, columns);
/*  214 */       if (mappings.isEmpty()) {
/*      */         
/*  216 */         CertificateNotFoundException certificateNotFoundException = new CertificateNotFoundException(this.i18n.getMessage("certificateOfTypeNotFoundForIdentifier", new String[] {
/*  217 */                 this.i18n.getMessage(CertificateType.BACKEND_CA_CERTIFICATE.name()), zkNo, "" }));
/*  218 */         this.logger.logWithExceptionByInfo("000553X", (CEBASException)certificateNotFoundException);
/*  219 */         throw certificateNotFoundException;
/*      */       } 
/*  221 */       backendSubjectKeyIdentifier = (String)((Tuple)mappings.get(0)).get(0);
/*      */     } else {
/*  223 */       zkNo = "";
/*  224 */       backendSubjectKeyIdentifier = HexUtil.base64ToHex(input.getIdentifier());
/*      */     } 
/*  226 */     if (checkBackendExists) {
/*  227 */       verifyBackendExists(backendSubjectKeyIdentifier);
/*      */     }
/*  229 */     return new ZkNoMappingResult(backendSubjectKeyIdentifier, zkNo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void verifyBackendExists(String backendSubjectKeyIdentifier) {
/*  241 */     Optional<Certificate> backendCertificateOptional = this.searchEngine.findCertificate(this.session.getCurrentUser(), backendSubjectKeyIdentifier, CertificateType.BACKEND_CA_CERTIFICATE);
/*      */     
/*  243 */     if (!backendCertificateOptional.isPresent()) {
/*      */       
/*  245 */       CertificateNotFoundException certificateNotFoundException = new CertificateNotFoundException(this.i18n.getMessage("certificateOfTypeNotFound", new String[] {
/*  246 */               this.i18n.getMessage(CertificateType.BACKEND_CA_CERTIFICATE.name()) }));
/*  247 */       this.logger.logWithExceptionByInfo("000483X", (CEBASException)certificateNotFoundException);
/*  248 */       throw certificateNotFoundException;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Tuple> getBackendTupleForIdentifier(String identifier, List<String> columns) {
/*  263 */     Map<String, Object> params = new HashMap<>();
/*  264 */     params.put("type", CertificateType.BACKEND_CA_CERTIFICATE);
/*      */     
/*  266 */     if (!StringUtils.isBlank(identifier)) {
/*  267 */       CertificatesFieldsValidator.isIdentifierValid(identifier, this.i18n, this.logger);
/*      */       
/*  269 */       if (CertificatesFieldsValidator.isSubjectKeyIdentifier(identifier)) {
/*  270 */         params.put("subjectKeyIdentifier", HexUtil.base64ToHex(identifier));
/*      */       } else {
/*  272 */         params.put("zkNo", identifier);
/*      */       } 
/*      */     } 
/*  275 */     return this.searchEngine.findTuples(this.session.getCurrentUser(), params, columns, Certificate.class);
/*      */   }
/*      */   
/*      */   public List<ZkNoMappingResult> getZkNoMapping(String identifier) {
/*  279 */     List<String> columns = getColumnsForIdentifierSearch();
/*  280 */     List<Tuple> tuples = getBackendTupleForIdentifier(identifier, columns);
/*  281 */     return ZkNoSupport.getZkNoMapping(identifier, tuples, this.logger);
/*      */   }
/*      */   
/*      */   protected List<String> getColumnsForIdentifierSearch() {
/*  285 */     List<String> columns = new ArrayList<>();
/*  286 */     columns.add("subjectKeyIdentifier");
/*  287 */     columns.add("zkNo");
/*  288 */     return columns;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Number countCertificatesCurrentUser(Map<String, FilterObject> filters) {
/*  299 */     String METHOD_NAME = "countCertificatesCurrentUser";
/*  300 */     this.logger.entering(CLASS_NAME, "countCertificatesCurrentUser");
/*  301 */     filters.put("user", new FilterObject(true, this.session.getCurrentUser()));
/*  302 */     this.logger.exiting(CLASS_NAME, "countCertificatesCurrentUser");
/*  303 */     return this.repository.countFilter(filters);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Number countRootCertificatesCurrentUser() {
/*  312 */     String METHOD_NAME = "countCertificatesCurrentUser";
/*  313 */     this.logger.entering(CLASS_NAME, "countCertificatesCurrentUser");
/*  314 */     Map<String, Object> map = new HashMap<>();
/*  315 */     map.put("user", this.session.getCurrentUser());
/*  316 */     map.put("type", CertificateType.ROOT_CA_CERTIFICATE);
/*  317 */     this.logger.exiting(CLASS_NAME, "countCertificatesCurrentUser");
/*  318 */     return this.repository.countWithQuery(Certificate.class, map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Certificate> getCertificates(String parent) {
/*  328 */     String METHOD_NAME = "getCertificates";
/*  329 */     this.logger.entering(CLASS_NAME, "getCertificates");
/*  330 */     Map<String, Object> map = new HashMap<>();
/*  331 */     map.put("entityId", parent);
/*  332 */     List<Certificate> parents = this.repository.findWithNamedQuery("parent", map, 0);
/*  333 */     this.logger.exiting(CLASS_NAME, "getCertificates");
/*  334 */     return parents;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<DeleteCertificatesInfo> deleteCertificates(List<String> ids) {
/*  344 */     return this.deleteEngine.deleteCertificates(ids);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ExtendedDeleteCertificatesResult> deleteCertificatesAdditionalLogging(List<String> ids) {
/*  354 */     return this.deleteEngine.deleteCertificatesAdditionalLogging(ids);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<DeleteCertificatesInfo> deleteCertificates(List<String> ids, User currentUser) {
/*  365 */     return this.deleteEngine.deleteCertificates(ids, currentUser);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ExtendedDeleteCertificatesResult> deleteCertificates(DeleteCertificates deleteCertificate) {
/*  376 */     String METHOD_NAME = "deleteCertificates";
/*  377 */     this.logger.entering(CLASS_NAME, "deleteCertificates");
/*      */     
/*  379 */     List<ExtendedDeleteCertificatesResult> deletedCertificatesList = this.deleteEngine.deleteCertificates(deleteCertificate, getCertificatesCurrentUser(), (ICertificateHooks)new NoHook());
/*      */     
/*  381 */     this.logger.exiting(CLASS_NAME, "deleteCertificates");
/*  382 */     return deletedCertificatesList;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Certificate createCertificateInSignRequestState(CertificateSignRequest certificateSignRequest, boolean checkCurrentUser) {
/*  393 */     String METHOD_NAME = "createCertificateInSignRequestState";
/*  394 */     this.logger.entering(CLASS_NAME, "createCertificateInSignRequestState");
/*      */     
/*  396 */     Certificate result = this.certificateSignRequestEngine.createCertificateInSignRequestState(certificateSignRequest, checkCurrentUser, ValidationFailureOutput::outputFailureWithThrow, true);
/*      */     
/*  398 */     this.logger.exiting(CLASS_NAME, "createCertificateInSignRequestState");
/*  399 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Certificate getCertificateFromCSRId(String csrId) {
/*      */     Certificate certificate;
/*  409 */     String METHOD_NAME = "getCertificateFromCSRId";
/*  410 */     this.logger.entering(CLASS_NAME, "getCertificateFromCSRId");
/*      */     
/*  412 */     CertificatesProcessValidation.validateGetCertificateFromCSRId(csrId, this.i18n, this.logger);
/*  413 */     Optional<Certificate> certificateOptional = this.repository.findCertificate(csrId);
/*      */     
/*  415 */     if (certificateOptional.isPresent()) {
/*  416 */       certificate = certificateOptional.get();
/*      */     } else {
/*      */       
/*  419 */       ZenzefiCertificateNotFoundForExportPublicKeyFileException certificateNotFoundException = new ZenzefiCertificateNotFoundForExportPublicKeyFileException(this.i18n.getMessage("certificateDoesNotExistInUserStore", new String[] {
/*  420 */               HtmlUtils.htmlEscape(csrId)
/*      */             }), "certificateDoesNotExistInUserStore");
/*  422 */       this.logger.logWithTranslation(Level.WARNING, "000020X", certificateNotFoundException
/*  423 */           .getMessageId(), new String[] { HtmlUtils.htmlEscape(csrId) }, certificateNotFoundException
/*  424 */           .getClass().getSimpleName());
/*  425 */       throw certificateNotFoundException;
/*      */     } 
/*  427 */     this.logger.exiting(CLASS_NAME, "getCertificateFromCSRId");
/*  428 */     return certificate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ImportResult> importCertificatesFromLocal(List<LocalImportInput> inputs) {
/*  438 */     String METHOD_NAME = "importCertificatesFromLocal";
/*  439 */     this.logger.entering(CLASS_NAME, "importCertificatesFromLocal");
/*      */     
/*  441 */     this.importSession.run();
/*      */     try {
/*  443 */       return MdcDecoratorCompletableFuture.supplyAsync(() -> this.importCertificatesEngine.importCertificatesFromLocal(inputs)).get();
/*  444 */     } catch (ExecutionException e) {
/*  445 */       if (CEBASException.hasCEBASExceptionCause(e)) {
/*  446 */         throw (RuntimeException)e.getCause();
/*      */       }
/*  448 */       this.logger.log(Level.INFO, "000583", e.getCause().getMessage(), getClass().getSimpleName());
/*  449 */       throw new CertificatesImportException(e.getCause().getMessage());
/*      */     }
/*  451 */     catch (InterruptedException e) {
/*  452 */       Thread.currentThread().interrupt();
/*  453 */       this.logger.log(Level.INFO, "000584", e.getCause().getMessage(), getClass().getSimpleName());
/*  454 */       throw new CertificatesImportException(e.getCause().getMessage());
/*      */     } finally {
/*  456 */       this.importSession.setNotRunning();
/*  457 */       this.logger.exiting(CLASS_NAME, "importCertificatesFromLocal");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ImportResult> importCertificates(List<MultipartFile> files) {
/*  468 */     String METHOD_NAME = "importCertificates";
/*  469 */     this.logger.entering(CLASS_NAME, "importCertificates");
/*      */     
/*  471 */     this.importSession.run();
/*      */     try {
/*  473 */       return MdcDecoratorCompletableFuture.supplyAsync(() -> this.importCertificatesEngine.importCertificates(files, allowImportOfPrivateKeys())).get();
/*  474 */     } catch (ExecutionException e) {
/*  475 */       if (CEBASException.hasCEBASExceptionCause(e)) {
/*  476 */         throw (RuntimeException)e.getCause();
/*      */       }
/*  478 */       this.logger.log(Level.INFO, "000583", e.getCause().getMessage(), getClass().getSimpleName());
/*  479 */       throw new CertificatesImportException(e.getCause().getMessage());
/*      */     }
/*  481 */     catch (InterruptedException e) {
/*  482 */       Thread.currentThread().interrupt();
/*  483 */       this.logger.log(Level.INFO, "000584", e.getCause().getMessage(), getClass().getSimpleName());
/*  484 */       throw new CertificatesImportException(e.getCause().getMessage());
/*      */     } finally {
/*  486 */       this.importSession.setNotRunning();
/*  487 */       this.logger.exiting(CLASS_NAME, "importCertificates");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ImportResult> importRemoteCertificates(List<RemoteCertificateImportInput> remoteInputs, boolean onlyFromPKI) {
/*  500 */     String METHOD_NAME = "importRemoteCertificates";
/*  501 */     this.logger.entering(CLASS_NAME, "importRemoteCertificates");
/*      */     
/*  503 */     this.importSession.run();
/*      */     try {
/*  505 */       return this.importCertificatesEngine.importRemoteCertificates(remoteInputs, false, allowImportOfPrivateKeys());
/*      */     } finally {
/*  507 */       this.importSession.setNotRunning();
/*  508 */       this.logger.exiting(CLASS_NAME, "importRemoteCertificates");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SystemIntegrityCheckResult checkSystemIntegrity() {
/*  516 */     String METHOD_NAME = "checkSystemIntegrity";
/*  517 */     this.logger.entering(CLASS_NAME, "checkSystemIntegrity");
/*      */     
/*  519 */     this.systemIntegrityCheck.checkSystemIntegrity(this.session.getCurrentUser(), this.session.getSystemIntegrityCheckResult());
/*      */     
/*  521 */     this.logger.exiting(CLASS_NAME, "checkSystemIntegrity");
/*  522 */     return this.session.getSystemIntegrityCheckResult();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeSystemIntegrityReportToOutputStream(OutputStream outputStream) {
/*  531 */     String METHOD_NAME = "writeSystemIntegrityReportToOutputStream";
/*  532 */     this.logger.entering(CLASS_NAME, "writeSystemIntegrityReportToOutputStream");
/*      */     
/*  534 */     String xmlReport = this.session.getSystemIntegrityCheckResult().getIntegrityCheckXML();
/*  535 */     if (xmlReport == null || xmlReport.isEmpty()) {
/*      */       
/*  537 */       SystemIntegrityEmptyReportException reportNotFoundException = new SystemIntegrityEmptyReportException(this.i18n.getMessage("systemIntegrityReportNotFound"), "systemIntegrityReportNotFound");
/*      */       
/*  539 */       this.logger.logWithTranslation(Level.WARNING, "000064X", reportNotFoundException
/*  540 */           .getMessageId(), reportNotFoundException.getClass().getSimpleName());
/*  541 */       throw reportNotFoundException;
/*      */     } 
/*      */     try {
/*  544 */       outputStream.write(xmlReport.getBytes(StandardCharsets.UTF_8));
/*  545 */     } catch (IOException e) {
/*  546 */       LOG.log(Level.FINEST, e.getMessage(), e);
/*  547 */       this.logger.logWithTranslation(Level.WARNING, "000063X", "cannotDownloadSystemIntegrityReport", CLASS_NAME);
/*      */     } finally {
/*      */       
/*  550 */       closeStream(outputStream);
/*      */     } 
/*  552 */     this.logger.exiting(CLASS_NAME, "writeSystemIntegrityReportToOutputStream");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSystemIntegrityLogExistent() {
/*  561 */     String METHOD_NAME = "isSystemIntegrityLogExistent";
/*  562 */     this.logger.entering(CLASS_NAME, "isSystemIntegrityLogExistent");
/*  563 */     this.logger.exiting(CLASS_NAME, "isSystemIntegrityLogExistent");
/*      */     
/*  565 */     return !this.session.getSystemIntegrityCheckResult().getIntegrityCheckXML().isEmpty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CertificateWithSNResult getDiagCert(String backendCertSkid) {
/*  577 */     return getDiagCert(backendCertSkid, null, null, (byte)0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CertificateWithSNResult getDiagCert(String backendCertSkid, String vin) {
/*  590 */     return getDiagCert(backendCertSkid, vin, null, (byte)0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CertificateWithSNResult getDiagCert(String backendCertSkid, byte userRole) {
/*  603 */     return getDiagCert(backendCertSkid, null, null, userRole);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CertificateWithSNResult getDiagCert(String backendCertSkid, String vin, String ecu) {
/*  617 */     return getDiagCert(backendCertSkid, vin, ecu, (byte)0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CertificateWithSNResult getDiagCert(String backendCertSkid, String vin, String ecu, byte role) {
/*  632 */     String METHOD_NAME = "getDiagCert";
/*  633 */     this.logger.entering(CLASS_NAME, "getDiagCert");
/*  634 */     UserRole userRoleFromByte = UserRole.getUserRoleFromByte(role);
/*  635 */     String userRole = userRoleFromByte.getText();
/*  636 */     Certificate certificateResult = getDiagnosticCert(backendCertSkid, vin, ecu, userRole);
/*      */     
/*  638 */     CertificateWithSNResult diagCert = new CertificateWithSNResult(this.factory.getCertificateBytes(certificateResult), CertificateParser.hexStringToByteArray(certificateResult.getSerialNo()));
/*  639 */     this.logger.log(Level.INFO, "000047", this.i18n.getEnglishMessage("foundDiagCertWithParams", new String[] { diagCert
/*      */             
/*  641 */             .getCertificateData(), diagCert.getSerialNumber(), backendCertSkid, ecu, vin, userRole, 
/*      */             
/*  643 */             composeMessageForCurrentUser() }), CLASS_NAME);
/*      */     
/*  645 */     this.logger.exiting(CLASS_NAME, "getDiagCert");
/*  646 */     return diagCert;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean checkActiveDiagCert(String backendCertSkid, String diagCertSerialNo, String targetEcu, String targetVin, byte role) {
/*  660 */     String METHOD_NAME = "checkActiveDiagCert";
/*  661 */     this.logger.entering(CLASS_NAME, "checkActiveDiagCert");
/*      */     
/*  663 */     boolean result = false;
/*  664 */     CertificatesProcessValidation.validateCheckActiveDiagCert(backendCertSkid, diagCertSerialNo, targetEcu, targetVin, this.i18n, this.logger);
/*  665 */     Configuration certSelectionConfiguration = ConfigurationUtil.getConfigurationForUser(this.session.getCurrentUser(), CEBASProperty.CERT_SELECTION, this.logger, this.i18n);
/*  666 */     if (ConfigurationUtil.isAutomaticSelection(certSelectionConfiguration)) {
/*  667 */       UserRole userRoleFromByte = UserRole.getUserRoleFromByte(role);
/*  668 */       String userRole = userRoleFromByte.getText();
/*  669 */       Optional<Certificate> certificateOptional = this.searchEngine.findValidDiagnosticCertificate(this.session.getCurrentUser(), backendCertSkid, targetVin, targetEcu, userRole);
/*  670 */       if (certificateOptional.isPresent()) {
/*  671 */         Certificate certificate = certificateOptional.get();
/*  672 */         String certificateSerialNo = HexUtil.omitLeadingZeros(certificate.getSerialNo());
/*  673 */         String querySerialNo = HexUtil.omitLeadingZeros(diagCertSerialNo);
/*  674 */         if (certificateSerialNo.equals(querySerialNo)) {
/*  675 */           this.logger.logWithTranslation(Level.INFO, "000061", "foundDiagCertificateForCriteria", CLASS_NAME);
/*  676 */           result = true;
/*      */         } else {
/*  678 */           this.logger.logWithTranslation(Level.WARNING, "000061X", "noCertificateFoundMatchingTheFilterCriteriaDifferentSN", CLASS_NAME);
/*      */         } 
/*      */       } else {
/*  681 */         this.logger.logWithTranslation(Level.WARNING, "000061X", "noCertificateFoundMatchingTheFilterCriteriaDifferentSN", CLASS_NAME);
/*      */       } 
/*      */     } else {
/*  684 */       this.logger.logWithTranslation(Level.WARNING, "000061X", "activeDiagManualSelection", CLASS_NAME);
/*      */     } 
/*      */     
/*  687 */     this.logger.exiting(CLASS_NAME, "checkActiveDiagCert");
/*  688 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ExtendedCertificateWithSNAndIssuerSNResult> getEnhDiagCert(String backendCertSkid, String diagCertSerialNo, String vin, String ecu) {
/*      */     List<Certificate> certificatesResult;
/*  702 */     String METHOD_NAME = "getEnhDiagCert";
/*  703 */     this.logger.entering(CLASS_NAME, "getEnhDiagCert");
/*      */     
/*  705 */     CertificatesProcessValidation.validateGetEnhDiagCert(backendCertSkid, diagCertSerialNo, vin, ecu, this.i18n, this.logger);
/*      */     
/*  707 */     Configuration certSelectionConfiguration = ConfigurationUtil.getConfigurationForUser(this.session.getCurrentUser(), CEBASProperty.CERT_SELECTION, this.logger, this.i18n);
/*  708 */     if (ConfigurationUtil.isAutomaticSelection(certSelectionConfiguration)) {
/*  709 */       certificatesResult = this.searchEngine.findValidEnhancedDiagnosticCertificates(this.session.getCurrentUser(), backendCertSkid, diagCertSerialNo, vin, ecu);
/*      */     } else {
/*  711 */       certificatesResult = this.searchEngine.findEnhancedDiagnosticCertificatesActiveForTest(this.session.getCurrentUser());
/*      */     } 
/*  713 */     if (certificatesResult.isEmpty()) {
/*      */       
/*  715 */       CertificateNotFoundException ex = new CertificateNotFoundException(this.i18n.getMessage("noCertificateFoundMatchingCriteriaEnhanced", new String[] { backendCertSkid, ecu, vin, diagCertSerialNo }), "noCertificateFoundMatchingCriteriaEnhanced");
/*      */ 
/*      */       
/*  718 */       this.logger.logWithTranslation(Level.WARNING, "000123X", ex.getMessageId(), new String[] { backendCertSkid, ecu, vin, diagCertSerialNo }, ex
/*  719 */           .getClass().getSimpleName());
/*  720 */       throw ex;
/*      */     } 
/*      */     
/*  723 */     this.logger.logWithTranslation(Level.INFO, "000498", "foundEnhCertificateForCriteria", new String[] { "" + certificatesResult
/*  724 */           .size() }, CLASS_NAME);
/*      */     
/*  726 */     this.logger.exiting(CLASS_NAME, "getEnhDiagCert");
/*  727 */     return (List<ExtendedCertificateWithSNAndIssuerSNResult>)certificatesResult.stream()
/*  728 */       .map(c -> {
/*      */           CertificateWithSNAndIssuerSNResult certificateWithSNAndIssuerSNResult = new CertificateWithSNAndIssuerSNResult(this.factory.getCertificateBytes(c), CertificateParser.hexStringToByteArray(c.getSerialNo()), CertificateParser.hexStringToByteArray(c.getIssuerSerialNumber()));
/*      */           
/*      */           BackendContext backendContext = c.getBackendContext();
/*      */           
/*      */           return new ExtendedCertificateWithSNAndIssuerSNResult(certificateWithSNAndIssuerSNResult, backendContext.getZkNo());
/*  734 */         }).collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean signatureCheck(SignatureCheckHolder signatureCheck) {
/*      */     boolean response;
/*  744 */     String METHOD_NAME = "signatureCheck";
/*  745 */     this.logger.entering(CLASS_NAME, "signatureCheck");
/*      */ 
/*      */     
/*  748 */     if (StringUtils.isEmpty(signatureCheck.getMessage()) || StringUtils.isEmpty(signatureCheck.getEcuCertificate()) || 
/*  749 */       StringUtils.isEmpty(signatureCheck.getSignature())) {
/*      */       
/*  751 */       SignatureCheckException exception = new SignatureCheckException(this.i18n.getMessage("wrongInputAllParamsMandatory"), "wrongInputAllParamsMandatory");
/*      */       
/*  753 */       this.logger.logWithTranslation(Level.WARNING, "000119X", exception.getMessageId(), exception
/*  754 */           .getClass().getSimpleName());
/*  755 */       throw exception;
/*      */     } 
/*  757 */     byte[] messageRaw = Base64.getDecoder().decode(signatureCheck.getMessage());
/*  758 */     byte[] signatureRaw = Base64.getDecoder().decode(signatureCheck.getSignature());
/*  759 */     Certificate certificateToCheck = this.factory.getCertificateFromBase64(signatureCheck.getEcuCertificate());
/*  760 */     CertificatePrivateKeyHolder holder = new CertificatePrivateKeyHolder(certificateToCheck, Optional.empty());
/*  761 */     ImportResult checkImportIntoChain = this.importCertificatesEngine.checkImportIntoChain(holder);
/*  762 */     if (checkImportIntoChain.isSuccess()) {
/*      */       try {
/*  764 */         response = CertificateCryptoEngine.checkMessageSignature(certificateToCheck
/*  765 */             .getCertificateData().getCert().getPublicKey(), messageRaw, signatureRaw);
/*  766 */       } catch (NoSuchAlgorithmException|java.security.NoSuchProviderException|java.security.SignatureException|java.security.InvalidKeyException e) {
/*  767 */         LOG.log(Level.FINEST, e.getMessage(), e);
/*      */         
/*  769 */         SignatureCheckException ex = new SignatureCheckException(this.i18n.getMessage("couldNotVerifySignatureForMessage"), "couldNotVerifySignatureForMessage");
/*      */         
/*  771 */         this.logger.logWithTranslation(Level.WARNING, "000067X", ex.getMessageId(), ex
/*  772 */             .getClass().getSimpleName());
/*  773 */         throw ex;
/*      */       } 
/*      */     } else {
/*      */       
/*  777 */       this.logger.logWithTranslation(Level.WARNING, "000102X", "couldNotVerifySignature", new String[] { checkImportIntoChain
/*  778 */             .getMessage() }, CLASS_NAME);
/*      */       
/*  780 */       response = false;
/*      */     } 
/*      */     
/*  783 */     this.logger.exiting(CLASS_NAME, "signatureCheck");
/*  784 */     return response;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writePublicKeyToOutputStream(Certificate certificate, OutputStream outputStream) {
/*  794 */     String METHOD_NAME = "writePublicKeyToOutputStream";
/*  795 */     this.logger.entering(CLASS_NAME, "writePublicKeyToOutputStream");
/*      */     
/*      */     try {
/*  798 */       String publicKey = certificate.getSubjectPublicKey();
/*  799 */       byte[] hexStringToByteArray = CertificateParser.hexStringToByteArray(publicKey);
/*  800 */       outputStream.write(hexStringToByteArray);
/*  801 */     } catch (IOException e) {
/*  802 */       LOG.log(Level.FINEST, e.getMessage(), e);
/*      */       
/*  804 */       CEBASException exception = new CEBASException(this.i18n.getMessage("writingPublicKeyToFileFailed", new String[] { e.getMessage() }), "writingPublicKeyToFileFailed");
/*      */       
/*  806 */       this.logger.logWithTranslation(Level.WARNING, "000018X", exception.getMessageId(), new String[] { e
/*  807 */             .getMessage() }, exception.getClass().getSimpleName());
/*  808 */       throw exception;
/*      */     } finally {
/*  810 */       closeStream(outputStream);
/*      */     } 
/*  812 */     this.logger.exiting(CLASS_NAME, "writePublicKeyToOutputStream");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Certificate> List<? extends CertificateSummary> listCertificateSummary(Class<T> type, boolean all) {
/*  824 */     String METHOD_NAME = "listCertificateSummary";
/*  825 */     this.logger.entering(CLASS_NAME, "listCertificateSummary");
/*  826 */     List<T> userCertificates = this.searchEngine.findCertificatesByUser(type, this.session.getCurrentUser(), all, null);
/*  827 */     this.logger.log(Level.INFO, "000448", this.i18n.getEnglishMessage("returnCertificateList"), getClass().getSimpleName());
/*  828 */     this.logger.exiting(CLASS_NAME, "listCertificateSummary");
/*  829 */     return this.certificateProfileConfiguration.convertToCertificateSummary(userCertificates);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Certificate> List<? extends CertificateSummary> listPaginatedCertificateSummary(Class<T> type, boolean all, Pageable pageable) {
/*  841 */     String METHOD_NAME = "listCertificateSummary";
/*  842 */     this.logger.entering(CLASS_NAME, "listCertificateSummary");
/*      */     
/*  844 */     List<T> userCertificates = this.searchEngine.findCertificatesByUser(type, this.session.getCurrentUser(), all, pageable);
/*      */     
/*  846 */     this.logger.log(Level.INFO, "000448", this.i18n.getEnglishMessage("returnCertificateList"), getClass().getSimpleName());
/*  847 */     this.logger.exiting(CLASS_NAME, "listCertificateSummary");
/*      */     
/*  849 */     return this.certificateProfileConfiguration.convertToCertificateSummary(userCertificates);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> restoreCertificates() {
/*  858 */     String METHOD_NAME = "restoreCertificates";
/*  859 */     this.logger.entering(CLASS_NAME, "restoreCertificates");
/*  860 */     this.session.getSystemIntegrityCheckResult().clear();
/*      */     
/*  862 */     List<Certificate> flattenedCertificates = new ArrayList<>();
/*  863 */     List<Certificate> restoredStoreCertificates = new ArrayList<>();
/*      */     
/*  865 */     User currentUser = this.session.getCurrentUser();
/*  866 */     if (countRootCertificatesCurrentUser().intValue() == 0) {
/*  867 */       this.importCertificatesEngine.importFromBaseStore(currentUser);
/*  868 */       flattenedCertificates.addAll(getCertificatesCurrentUser());
/*      */     } else {
/*  870 */       restoredStoreCertificates.addAll(this.importCertificatesEngine.restoreCertificatesFromBaseStore(currentUser));
/*      */     } 
/*  872 */     restoredStoreCertificates
/*  873 */       .forEach(root -> flattenedCertificates.addAll((Collection)root.flattened().collect(Collectors.toList())));
/*      */     
/*  875 */     this.logger.log(Level.INFO, "000137", this.i18n
/*  876 */         .getEnglishMessage("restoredCertificates", new String[] {
/*  877 */             Integer.toString(flattenedCertificates.size())
/*      */           }), CLASS_NAME);
/*      */     
/*  880 */     this.logger.exiting(CLASS_NAME, "restoreCertificates");
/*  881 */     return (List<String>)flattenedCertificates.stream().map(Certificate::getSignatureBase64).collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Certificate getDiagnosticCert(String backendCertSkid, String vin, String ecu, String userRole) {
/*      */     Certificate certificateResult;
/*  896 */     CertificatesProcessValidation.validateGetDiagCert(backendCertSkid, vin, ecu, this.i18n, this.logger);
/*      */     
/*  898 */     Configuration certSelectionConfiguration = ConfigurationUtil.getConfigurationForUser(this.session.getCurrentUser(), CEBASProperty.CERT_SELECTION, this.logger, this.i18n);
/*      */     
/*  900 */     User currentUser = this.session.getCurrentUser();
/*  901 */     if (ConfigurationUtil.isAutomaticSelection(certSelectionConfiguration)) {
/*  902 */       Optional<Certificate> certificate = this.searchEngine.findValidDiagnosticCertificate(currentUser, backendCertSkid, vin, ecu, userRole);
/*      */       
/*  904 */       if (certificate.isPresent()) {
/*  905 */         certificateResult = certificate.get();
/*      */       } else {
/*      */         
/*  908 */         CertificateNotFoundException ex = new CertificateNotFoundException(this.i18n.getEnglishMessage("noCertificateFoundMatchingCriteriaBasic", new String[] {
/*      */                 
/*  910 */                 backendCertSkid, ecu, vin, userRole.equals(UserRole.NO_ROLE.getText()) ? "" : userRole }));
/*  911 */         this.logger.logWithExceptionByWarning("000046", (CEBASException)ex);
/*  912 */         throw ex;
/*      */       } 
/*      */     } else {
/*      */       
/*  916 */       certificateResult = this.searchEngine.findCertificateActiveForTesting(currentUser, CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE);
/*      */     } 
/*      */     
/*  919 */     return certificateResult;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected <T extends Certificate> T getCertByAuthKeyIdentAndSerialNo(String authorityKeyIdentifier, String serialNo, Class<T> clazz) {
/*  932 */     CertificatesProcessValidation.validateGetCertByAuthKeyIdentAndSerialNo(authorityKeyIdentifier, serialNo, this.i18n, this.logger);
/*      */     
/*  934 */     User currentUser = this.session.getCurrentUser();
/*  935 */     List<T> certificates = this.searchEngine.findCertByAuthKeyIdentAndSerialNo(currentUser, authorityKeyIdentifier, serialNo, clazz);
/*      */ 
/*      */     
/*  938 */     if (certificates.isEmpty()) {
/*      */       
/*  940 */       CertificateNotFoundException ex = new CertificateNotFoundException(this.i18n.getMessage("noCertificateFoundWithGivenAKIAndSN"), "noCertificateFoundWithGivenAKIAndSN");
/*      */       
/*  942 */       this.logger.logWithTranslation(Level.WARNING, "000059X", ex.getMessageId(), ex
/*  943 */           .getClass().getSimpleName());
/*  944 */       throw ex;
/*      */     } 
/*  946 */     return certificates.get(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void closeStream(Closeable stream) {
/*      */     try {
/*  958 */       if (stream != null) {
/*  959 */         stream.close();
/*      */       }
/*  961 */     } catch (IOException e) {
/*  962 */       LOG.log(Level.FINEST, e.getMessage(), e);
/*  963 */       this.logger.log(Level.WARNING, "000173X", "cannotCloseStreamOnCertificateOperations", CLASS_NAME);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String composeMessageForCurrentUser() {
/*      */     String currentUserName;
/*  982 */     if (this.session.isDefaultUser()) {
/*  983 */       currentUserName = this.i18n.getMessage("defaultUser");
/*      */     } else {
/*  985 */       currentUserName = this.session.getCurrentUser().getUserName();
/*      */     } 
/*  987 */     return currentUserName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Certificate getRequestedCertificate(String certificateId) {
/*  997 */     CertificatesProcessValidation.validateGetCertificate(certificateId, this.i18n, this.logger);
/*  998 */     Optional<Certificate> optional = this.repository.findCertificate(certificateId);
/*  999 */     return optional.<Throwable>orElseThrow(() -> new CEBASCertificateException(this.i18n.getMessage("certificateDoesNotExist")));
/*      */   }
/*      */   
/*      */   public DeleteCertificateModel getDeleteCertificateModel(DeleteCertificateModel model) {
/* 1003 */     String skiBase64 = model.getAuthorityKeyIdentifier();
/* 1004 */     if (!CertificatesFieldsValidator.isSubjectKeyIdentifier(skiBase64)) {
/* 1005 */       String skiHex = getHexBackendSKI(skiBase64);
/* 1006 */       skiBase64 = Base64.getEncoder().encodeToString(HexUtil.hexStringToByteArray(skiHex));
/*      */     } 
/* 1008 */     return new DeleteCertificateModel(skiBase64, model.getSerialNo());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getHexBackendSKI(String backendSKI) {
/* 1018 */     return resolveZkNOAndSki(backendSKI::toString, !CertificatesFieldsValidator.isSubjectKeyIdentifier(backendSKI)).getSki();
/*      */   }
/*      */   
/*      */   public abstract List<? extends Certificate> getCertificatesCurrentUser();
/*      */   
/*      */   protected abstract boolean allowImportOfPrivateKeys();
/*      */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\CertificatesService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */