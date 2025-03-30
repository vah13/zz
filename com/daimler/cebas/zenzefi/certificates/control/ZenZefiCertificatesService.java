/*      */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.certificates.control;
/*      */ 
/*      */ import com.daimler.cebas.certificates.control.CertificateExporter;
/*      */ import com.daimler.cebas.certificates.control.CertificateToolsProvider;
/*      */ import com.daimler.cebas.certificates.control.CertificatesService;
/*      */ import com.daimler.cebas.certificates.control.ImportSession;
/*      */ import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
/*      */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*      */ import com.daimler.cebas.certificates.control.exceptions.CertificateMoreResultsFoundException;
/*      */ import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
/*      */ import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundOnDownloadException;
/*      */ import com.daimler.cebas.certificates.control.exceptions.CertificatesUpdateException;
/*      */ import com.daimler.cebas.certificates.control.exceptions.DownloadRightsException;
/*      */ import com.daimler.cebas.certificates.control.exceptions.SecOCISException;
/*      */ import com.daimler.cebas.certificates.control.exceptions.TimeException;
/*      */ import com.daimler.cebas.certificates.control.hooks.DeleteCertificateNonVSMHook;
/*      */ import com.daimler.cebas.certificates.control.hooks.ICertificateHooks;
/*      */ import com.daimler.cebas.certificates.control.repository.CertificateRepository;
/*      */ import com.daimler.cebas.certificates.control.update.UpdateType;
/*      */ import com.daimler.cebas.certificates.control.update.task.FetchUpdateMetricsTask;
/*      */ import com.daimler.cebas.certificates.control.validation.CertificatesFieldsValidator;
/*      */ import com.daimler.cebas.certificates.control.validation.CertificatesProcessValidation;
/*      */ import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
/*      */ import com.daimler.cebas.certificates.control.vo.CertificateWithSNResult;
/*      */ import com.daimler.cebas.certificates.control.vo.DeleteCertificates;
/*      */ import com.daimler.cebas.certificates.control.vo.ExtendedCertificateWithSNResult;
/*      */ import com.daimler.cebas.certificates.control.vo.ExtendedDeleteCertificatesResult;
/*      */ import com.daimler.cebas.certificates.control.vo.ISecOCIsInput;
/*      */ import com.daimler.cebas.certificates.control.vo.ImportResult;
/*      */ import com.daimler.cebas.certificates.control.vo.UpdateCertificateMetrics;
/*      */ import com.daimler.cebas.certificates.control.zkNoSupport.MappingsUpdater;
/*      */ import com.daimler.cebas.certificates.control.zkNoSupport.ZkNoMappingResult;
/*      */ import com.daimler.cebas.certificates.control.zkNoSupport.ZkNoSupport;
/*      */ import com.daimler.cebas.certificates.entity.BackendContext;
/*      */ import com.daimler.cebas.certificates.entity.Certificate;
/*      */ import com.daimler.cebas.certificates.entity.CertificateState;
/*      */ import com.daimler.cebas.certificates.entity.CertificateType;
/*      */ import com.daimler.cebas.certificates.entity.UserRole;
/*      */ import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
/*      */ import com.daimler.cebas.certificates.integration.vo.PKICertificateRequest;
/*      */ import com.daimler.cebas.certificates.integration.vo.PKIEnhancedCertificateRequest;
/*      */ import com.daimler.cebas.common.ObjectIdentifier;
/*      */ import com.daimler.cebas.common.control.CEBASException;
/*      */ import com.daimler.cebas.common.control.CEBASProperty;
/*      */ import com.daimler.cebas.common.control.HexUtil;
/*      */ import com.daimler.cebas.common.control.annotations.CEBASService;
/*      */ import com.daimler.cebas.common.control.vo.Enablement;
/*      */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*      */ import com.daimler.cebas.configuration.control.ConfigurationUtil;
/*      */ import com.daimler.cebas.configuration.control.mdc.MdcDecoratorCompletableFuture;
/*      */ import com.daimler.cebas.configuration.entity.Configuration;
/*      */ import com.daimler.cebas.logs.control.Logger;
/*      */ import com.daimler.cebas.users.control.Session;
/*      */ import com.daimler.cebas.users.entity.User;
/*      */ import com.daimler.cebas.zenzefi.certificates.control.AbstractZenZefiCertificatesService;
/*      */ import com.daimler.cebas.zenzefi.certificates.control.ProfileChecker;
/*      */ import com.daimler.cebas.zenzefi.certificates.control.ZenZefiSignCodingDataEngine;
/*      */ import com.daimler.cebas.zenzefi.certificates.control.replacement.ReplacementPackageFactory;
/*      */ import com.daimler.cebas.zenzefi.certificates.control.replacement.ReplacementTargetDetermination;
/*      */ import com.daimler.cebas.zenzefi.certificates.control.update.CertificatesUpdaterFactory;
/*      */ import com.daimler.cebas.zenzefi.certificates.control.update.UpdateSession;
/*      */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadSecOCISCertificateTask;
/*      */ import com.daimler.cebas.zenzefi.certificates.control.update.task.DownloadTimeCertificateTask;
/*      */ import com.daimler.cebas.zenzefi.certificates.control.vo.ActiveForTestingCertificates;
/*      */ import com.daimler.cebas.zenzefi.certificates.control.vo.CertificateHexFormatOutput;
/*      */ import com.daimler.cebas.zenzefi.certificates.control.vo.CertificatesUseCaseHolder;
/*      */ import com.daimler.cebas.zenzefi.certificates.entity.TestingUseCaseType;
/*      */ import com.daimler.cebas.zenzefi.certificates.entity.ZenZefiCertificate;
/*      */ import com.daimler.cebas.zenzefi.common.control.constants.ZenZefiConstants;
/*      */ import com.daimler.cebas.zenzefi.system.control.coupling.SystemCouplingManager;
/*      */ import com.daimler.cebas.zenzefi.users.control.backend.IBackendLoginCheck;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Base64;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import java.util.Optional;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import java.util.stream.Collectors;
/*      */ import javax.persistence.Tuple;
/*      */ import org.springframework.beans.factory.annotation.Autowired;
/*      */ import org.springframework.beans.factory.annotation.Value;
/*      */ import org.springframework.util.StringUtils;
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
/*      */ public class ZenZefiCertificatesService
/*      */   extends AbstractZenZefiCertificatesService
/*      */ {
/*  101 */   private static final Logger LOG = Logger.getLogger(CertificateParser.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  106 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.certificates.control.ZenZefiCertificatesService.class.getSimpleName();
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String SERIAL_NUMBER = " serial number ";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String RETRIEVED_TIME_CERTIFICATE_WITH_AUTHORITY_KEY_IDENTIFIER = "Retrieved from user-specific certificate store Time Certificate with authority key identifier ";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String DEFAULT_USER_CANNOT_START_CERTIFICATES_UPDATE = "Default user cannot start certificates update";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String WRONG_INPUT_PLEASE_CHECK_THE_MANDATORY_FIELDS = "Wrong input! Please check the mandatory fields. They cannot be empty!";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String TIME_CSR_ALREADY_EXISTS = "Time CSR already exists: ";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String BACKEND_SKI = " Backend SKI:";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String TARGET_VIN = " Target VIN: ";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String TARGET_ECU = " Target ECU: ";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String NONCE = " Nonce: ";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String INTERNAL_ID = " Internal Id: ";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String RETRIEVED_SECOCIS_CERTIFICATE_WITH_AUTHORITY_KEY_IDENTIFIER = "Retrieved from user-specific certificate store Secocis Certificate with authority key identifier ";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String SECOCIS_CSR_ALREADY_EXISTS = "Secocis CSR already exists: ";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String DIAGNOSTIC_SKI = " Diagnostic SKI:";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String REASON = "Reason: ";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String START_CREATE_TIME_CSR_PROCESS = "Start Create Time CSR process.";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String STOP_CREATE_TIME_CSR_PROCESS = "Stop Create Time CSR process.";
/*      */ 
/*      */   
/*      */   private static final String DASH = "-";
/*      */ 
/*      */   
/*      */   private static final String EMPTY = "";
/*      */ 
/*      */   
/*      */   @Value("${prod.qualifier}")
/*      */   private String prodQualifierValue;
/*      */ 
/*      */   
/*      */   private UpdateSession updateSession;
/*      */ 
/*      */   
/*      */   private CertificatesUpdaterFactory updaterFactory;
/*      */ 
/*      */   
/*      */   private DownloadTimeCertificateTask downloadTimeCertificateTask;
/*      */ 
/*      */   
/*      */   private DownloadSecOCISCertificateTask downloadSecOCISCertificateTask;
/*      */ 
/*      */   
/*      */   @Value("#{'${EXCLUDED_CENTRAL_AUTH_ROLES}'.split(',')}")
/*      */   private List<String> excludedRolesForCentralAuth;
/*      */ 
/*      */   
/*      */   private FetchUpdateMetricsTask fetchUpdateMetricsTask;
/*      */ 
/*      */   
/*      */   private IBackendLoginCheck backendLogin;
/*      */ 
/*      */   
/*      */   private MappingsUpdater mappingsUpdater;
/*      */ 
/*      */ 
/*      */   
/*      */   @Autowired
/*      */   public ZenZefiCertificatesService(CertificateToolsProvider toolsProvider, CertificateRepository repository, Logger logger, Session session, AbstractConfigurator configurator, CertificatesConfiguration certificateProfileConfiguration, CertificatesUpdaterFactory updaterFactory, SystemCouplingManager systemCouplingManager, DownloadTimeCertificateTask downloadTimeCertificateTask, DownloadSecOCISCertificateTask downloadSecOCISCertificateTask, ReplacementPackageFactory replacementPackageFactory, ZenZefiSignCodingDataEngine signCodingDataEngine, UpdateSession updateSession, ReplacementTargetDetermination replacementTargetDetermination, FetchUpdateMetricsTask fetchUpdateMetricsTask, IBackendLoginCheck backendLogin, ImportSession importSession, MappingsUpdater mappingsUpdater, @Value("${zk.no.regexp}") String zkNoRegex) {
/*  211 */     super(toolsProvider, repository, logger, session, configurator, certificateProfileConfiguration, replacementPackageFactory, signCodingDataEngine, systemCouplingManager, importSession, replacementTargetDetermination);
/*      */ 
/*      */     
/*  214 */     this.updaterFactory = updaterFactory;
/*  215 */     this.downloadTimeCertificateTask = downloadTimeCertificateTask;
/*  216 */     this.downloadSecOCISCertificateTask = downloadSecOCISCertificateTask;
/*  217 */     this.updateSession = updateSession;
/*  218 */     this.fetchUpdateMetricsTask = fetchUpdateMetricsTask;
/*  219 */     this.backendLogin = backendLogin;
/*  220 */     this.mappingsUpdater = mappingsUpdater;
/*  221 */     CertificatesFieldsValidator.setZkNoRegex((null == zkNoRegex) ? "^A?\\d{10}$" : zkNoRegex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean differentialCertificatesUpdate(String clientId) {
/*  231 */     if (!validateUpdateCertificatesClientId(clientId)) {
/*  232 */       return false;
/*      */     }
/*  234 */     this.session.getSystemIntegrityCheckResult().clear();
/*  235 */     this.updaterFactory.getUpdateCertificatesInstance(UpdateType.DIFFERENTIAL).updateCertificates();
/*  236 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<ExtendedDeleteCertificatesResult> deleteCertificatesAdditionalLogging(List<String> ids) {
/*  241 */     return this.deleteEngine.deleteCertificatesAdditionalLogging(ids, (ICertificateHooks)new DeleteCertificateNonVSMHook(this.logger));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<ExtendedDeleteCertificatesResult> deleteCertificates(DeleteCertificates deleteCertificate) {
/*  246 */     return super.deleteCertificates(deleteCertificate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateActiveForTesting(String id) {
/*  255 */     checkSelectionType();
/*  256 */     Optional<ZenZefiCertificate> activeForTestingCandidateOptional = this.repository.find(ZenZefiCertificate.class, id);
/*  257 */     if (activeForTestingCandidateOptional.isPresent()) {
/*  258 */       ZenZefiCertificate zenZefiCertificate = activeForTestingCandidateOptional.get();
/*  259 */       CertificateType type = zenZefiCertificate.getType();
/*  260 */       if (type == CertificateType.ENHANCED_RIGHTS_CERTIFICATE && !zenZefiCertificate.isSecOCISCert()) {
/*  261 */         zenZefiCertificate.setActiveForTesting(!zenZefiCertificate.isActiveForTesting());
/*      */       } else {
/*      */         
/*  264 */         List<ZenZefiCertificate> existingActiveForTestingList = this.repository.findActiveForTesting(ZenZefiCertificate.class, this.session.getCurrentUser(), type);
/*  265 */         if (zenZefiCertificate.isSecOCISCert())
/*      */         {
/*  267 */           existingActiveForTestingList = (List<ZenZefiCertificate>)existingActiveForTestingList.stream().filter(cert -> cert.isSecOCISCert()).collect(Collectors.toList());
/*      */         }
/*  269 */         clearOthers(zenZefiCertificate, existingActiveForTestingList);
/*  270 */         zenZefiCertificate.setActiveForTesting(!zenZefiCertificate.isActiveForTesting());
/*  271 */         zenZefiCertificate.setTestingType(TestingUseCaseType.G);
/*      */       } 
/*      */     } else {
/*      */       
/*  275 */       CertificateNotFoundException certificateNotFoundException = new CertificateNotFoundException(this.i18n.getMessage("certificateNotFound"));
/*  276 */       this.logger.logWithExceptionByInfo("000497X", (CEBASException)certificateNotFoundException);
/*  277 */       throw certificateNotFoundException;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public List<ZkNoMappingResult> getZkNoMapping(String identifier) {
/*  283 */     if (!StringUtils.isEmpty(identifier)) {
/*  284 */       List<ZkNoMappingResult> zkNoMapping = getZkNoMappingResultsForIdentifier(identifier, true);
/*      */       
/*  286 */       if (zkNoMapping.isEmpty()) {
/*  287 */         throw new CertificateNotFoundException(this.i18n
/*  288 */             .getMessage("entireMappingNothingFound", new String[] { identifier }));
/*      */       }
/*      */       
/*  291 */       Optional<ZkNoMappingResult> noZkNoFound = zkNoMapping.stream().filter(mapping -> (mapping.getZkNo() == null)).findFirst();
/*  292 */       if (noZkNoFound.isPresent()) {
/*  293 */         throw new CEBASCertificateException(this.i18n
/*  294 */             .getMessage("backendWithoutZkNo", new String[] { identifier }));
/*      */       }
/*  296 */       return zkNoMapping;
/*      */     } 
/*  298 */     List<Certificate> allBackends = this.searchEngine.findAllBackends(this.session.getCurrentUser());
/*      */     
/*  300 */     List<List<ZkNoMappingResult>> listOfLists = (List<List<ZkNoMappingResult>>)allBackends.stream().map(cert -> getZkNOForCert(cert)).collect(Collectors.toList());
/*  301 */     return (List<ZkNoMappingResult>)listOfLists.stream().flatMap(Collection::stream).collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */   
/*      */   protected List<ZkNoMappingResult> getZkNoMappingResultsForIdentifier(String identifier, boolean download) {
/*  306 */     List<ZkNoMappingResult> zkNoMapping = super.getZkNoMapping(identifier);
/*      */     
/*  308 */     Optional<ZkNoMappingResult> foundWithNoZkNumber = zkNoMapping.stream().filter(mapping -> (mapping.getZkNo() == null)).findFirst();
/*  309 */     if ((zkNoMapping.isEmpty() || foundWithNoZkNumber.isPresent()) && download) {
/*  310 */       this.mappingsUpdater.updateMappings();
/*  311 */       return getZkNoMappingResultsForIdentifier(identifier, false);
/*      */     } 
/*  313 */     return zkNoMapping;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<ZkNoMappingResult> getZkNOForCert(Certificate certificate) {
/*  323 */     String aki = HexUtil.hexToBase64(certificate.getSubjectKeyIdentifier());
/*  324 */     List<Tuple> backendTupleForIdentifier = getBackendTupleForIdentifier(aki, getColumnsForIdentifierSearch());
/*  325 */     return ZkNoSupport.getZkNoMapping(backendTupleForIdentifier);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setActiveForTesting(String id) {
/*  334 */     checkSelectionType();
/*  335 */     Optional<ZenZefiCertificate> activeForTestingCandidateOptional = this.repository.find(ZenZefiCertificate.class, id);
/*  336 */     if (activeForTestingCandidateOptional.isPresent()) {
/*  337 */       ZenZefiCertificate zenZefiCertificate = activeForTestingCandidateOptional.get();
/*  338 */       CertificateType type = zenZefiCertificate.getType();
/*  339 */       if (zenZefiCertificate.isActiveForTesting()) {
/*      */         return;
/*      */       }
/*  342 */       if (type == CertificateType.ENHANCED_RIGHTS_CERTIFICATE && !zenZefiCertificate.isSecOCISCert()) {
/*  343 */         zenZefiCertificate.setActiveForTesting(!zenZefiCertificate.isActiveForTesting());
/*      */         
/*      */         return;
/*      */       } 
/*  347 */       List<ZenZefiCertificate> existingActiveForTestingList = this.repository.findActiveForTesting(ZenZefiCertificate.class, this.session.getCurrentUser(), type);
/*  348 */       if (zenZefiCertificate.isSecOCISCert())
/*      */       {
/*  350 */         existingActiveForTestingList = (List<ZenZefiCertificate>)existingActiveForTestingList.stream().filter(cert -> cert.isSecOCISCert()).collect(Collectors.toList());
/*      */       }
/*  352 */       clearOthers(zenZefiCertificate, existingActiveForTestingList);
/*  353 */       zenZefiCertificate.setActiveForTesting(true);
/*      */     }
/*      */     else {
/*      */       
/*  357 */       CertificateNotFoundException exc = new CertificateNotFoundException(this.i18n.getMessage("certificateNotFound"));
/*  358 */       this.logger.logWithExceptionByInfo("000494X", (CEBASException)exc);
/*  359 */       throw exc;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void unsetActiveForTesting(String id) {
/*  369 */     checkSelectionType();
/*  370 */     Optional<ZenZefiCertificate> activeForTestingCandidateOptional = this.repository.find(ZenZefiCertificate.class, id);
/*  371 */     if (activeForTestingCandidateOptional.isPresent()) {
/*  372 */       ZenZefiCertificate zenZefiCertificate = activeForTestingCandidateOptional.get();
/*  373 */       if (zenZefiCertificate.isActiveForTesting()) {
/*  374 */         zenZefiCertificate.setActiveForTesting(false);
/*      */       }
/*      */     } else {
/*      */       
/*  378 */       CertificateNotFoundException exc = new CertificateNotFoundException(this.i18n.getMessage("certificateNotFound"));
/*  379 */       this.logger.logWithExceptionByInfo("000496X", (CEBASException)exc);
/*  380 */       throw exc;
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
/*      */   public void setActiveForTesting(String id, TestingUseCaseType testingUseCase) {
/*  392 */     Optional<ZenZefiCertificate> activeForTestingCandidateOptional = this.repository.find(ZenZefiCertificate.class, id);
/*  393 */     if (activeForTestingCandidateOptional.isPresent()) {
/*  394 */       ZenZefiCertificate zenZefiCertificate = activeForTestingCandidateOptional.get();
/*  395 */       CertificateType type = zenZefiCertificate.getType();
/*  396 */       if (type != CertificateType.ECU_CERTIFICATE) {
/*  397 */         throw new CEBASCertificateException("Type enhanced rights is not supported to be set based on use case");
/*      */       }
/*      */       
/*  400 */       List<ZenZefiCertificate> repositoryData = this.repository.findActiveForTesting(ZenZefiCertificate.class, this.session
/*  401 */           .getCurrentUser(), type);
/*  402 */       if (zenZefiCertificate.isActiveForTesting()) {
/*  403 */         if (zenZefiCertificate.getTestingType() != testingUseCase) {
/*  404 */           zenZefiCertificate.setTestingType(TestingUseCaseType.G);
/*  405 */           clearOthers(zenZefiCertificate, repositoryData);
/*      */         } 
/*      */       } else {
/*      */         
/*  409 */         List<ZenZefiCertificate> existingActiveForTestingList = (List<ZenZefiCertificate>)repositoryData.stream().filter(cert -> (cert.getTestingType() == testingUseCase)).collect(Collectors.toList());
/*      */         
/*  411 */         Optional<ZenZefiCertificate> existingGeneral = repositoryData.stream().filter(cert -> (cert.getTestingType() == TestingUseCaseType.G)).findFirst();
/*  412 */         if (existingGeneral.isPresent()) {
/*  413 */           ZenZefiCertificate existingGeneralCertificate = existingGeneral.get();
/*  414 */           existingGeneralCertificate
/*  415 */             .setTestingType(existingGeneralCertificate.getOppositeTestingType(testingUseCase));
/*      */         } 
/*  417 */         clearOthers(zenZefiCertificate, existingActiveForTestingList);
/*  418 */         zenZefiCertificate.setActiveForTesting(true);
/*  419 */         zenZefiCertificate.setTestingType(testingUseCase);
/*      */       } 
/*      */     } else {
/*      */       
/*  423 */       CertificateNotFoundException exc = new CertificateNotFoundException(this.i18n.getMessage("certificateNotFound"));
/*  424 */       this.logger.logWithExceptionByInfo("000493X", (CEBASException)exc);
/*  425 */       throw exc;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void unsetActiveForTesting(String id, TestingUseCaseType testingUseCase) {
/*  435 */     Optional<ZenZefiCertificate> activeForTestingCandidateOptional = this.repository.find(ZenZefiCertificate.class, id);
/*  436 */     if (activeForTestingCandidateOptional.isPresent()) {
/*  437 */       ZenZefiCertificate zenZefiCertificate = activeForTestingCandidateOptional.get();
/*  438 */       if (zenZefiCertificate.isActiveForTesting()) {
/*  439 */         if (zenZefiCertificate.getTestingType() == TestingUseCaseType.G) {
/*  440 */           zenZefiCertificate.setTestingType(zenZefiCertificate.getOppositeTestingType(testingUseCase));
/*      */         } else {
/*  442 */           if (zenZefiCertificate.getTestingType() != testingUseCase) {
/*  443 */             throw new CEBASCertificateException("Wrong testing use case: " + testingUseCase + ". The certificate is active for the use case: " + zenZefiCertificate
/*      */                 
/*  445 */                 .getTestingType());
/*      */           }
/*  447 */           zenZefiCertificate.setActiveForTesting(false);
/*  448 */           zenZefiCertificate.setTestingType(TestingUseCaseType.G);
/*      */         } 
/*      */       }
/*      */     } else {
/*      */       
/*  453 */       CertificateNotFoundException certificateNotFoundException = new CertificateNotFoundException(this.i18n.getMessage("certificateNotFound"));
/*  454 */       this.logger.logWithExceptionByInfo("000495X", (CEBASException)certificateNotFoundException);
/*  455 */       throw certificateNotFoundException;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ZenZefiCertificate> getActiveForTestingOptions(CertificateType type) {
/*  466 */     if (CertificateType.SEC_OC_IS == type) {
/*  467 */       type = CertificateType.ENHANCED_RIGHTS_CERTIFICATE;
/*  468 */       List<ZenZefiCertificate> allEnhanced = this.searchEngine.findCertificatesByType(ZenZefiCertificate.class, this.session
/*  469 */           .getCurrentUser(), type);
/*      */       
/*  471 */       List<ZenZefiCertificate> collect = (List<ZenZefiCertificate>)allEnhanced.stream().filter(cert -> cert.isSecOCISCert()).collect(Collectors.toList());
/*  472 */       collect.sort(Comparator.<ZenZefiCertificate, Comparable>comparing(ZenZefiCertificate::isActiveForTesting).reversed());
/*  473 */       return collect;
/*      */     } 
/*  475 */     if (CertificateType.ENHANCED_RIGHTS_CERTIFICATE == type) {
/*  476 */       return (List<ZenZefiCertificate>)this.repository.findNotActiveForTesting(ZenZefiCertificate.class, this.session.getCurrentUser(), type).stream()
/*  477 */         .filter(cert -> !cert.isSecOCISCert()).collect(Collectors.toList());
/*      */     }
/*  479 */     List<ZenZefiCertificate> list = this.searchEngine.findCertificatesByType(ZenZefiCertificate.class, this.session
/*  480 */         .getCurrentUser(), type);
/*  481 */     list.sort(Comparator.<ZenZefiCertificate, Comparable>comparing(ZenZefiCertificate::isActiveForTesting).reversed());
/*  482 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Certificate> getActiveForTestingEnhanced() {
/*  489 */     return (List<Certificate>)this.repository
/*  490 */       .findActiveForTesting(ZenZefiCertificate.class, this.session.getCurrentUser(), CertificateType.ENHANCED_RIGHTS_CERTIFICATE)
/*      */       
/*  492 */       .stream().filter(cert -> !cert.isSecOCISCert()).collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String enableExtendedValidation(Enablement enablement) {
/*  502 */     User currentUser = this.session.getCurrentUser();
/*  503 */     Configuration extendedValidation = ConfigurationUtil.getConfigurationForUser(currentUser, CEBASProperty.VALIDATE_CERTS, this.logger, this.i18n);
/*      */     
/*  505 */     if (enablement.isEnable()) {
/*  506 */       extendedValidation.setConfigValue(Boolean.TRUE.toString());
/*  507 */       this.logger.log(Level.INFO, "000618", "Certificate validation set to ON", 
/*  508 */           getClass().getSimpleName());
/*  509 */       this.logger.log(Level.INFO, "000618", "Extended Validation on Import and Export was enabled", 
/*  510 */           getClass().getSimpleName());
/*  511 */       return "ON";
/*      */     } 
/*  513 */     extendedValidation.setConfigValue(Boolean.FALSE.toString());
/*  514 */     this.logger.log(Level.INFO, "000618", "Certificate validation set to OFF", 
/*  515 */         getClass().getSimpleName());
/*  516 */     this.logger.log(Level.INFO, "000618", "Extended Validation on Import and Export was disabled", 
/*  517 */         getClass().getSimpleName());
/*  518 */     return "OFF";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ActiveForTestingCertificates getActiveForTestingCertificates() {
/*  528 */     ActiveForTestingCertificates holder = new ActiveForTestingCertificates();
/*  529 */     User currentUser = this.session.getCurrentUser();
/*  530 */     List<String> rooCACertificates = this.repository.findActiveForTestingIds(currentUser, CertificateType.ROOT_CA_CERTIFICATE);
/*      */     
/*  532 */     List<String> rootLinkCertificate = this.repository.findActiveForTestingIds(currentUser, CertificateType.ROOT_CA_LINK_CERTIFICATE);
/*      */     
/*  534 */     List<String> backendCACertificates = this.repository.findActiveForTestingIds(currentUser, CertificateType.BACKEND_CA_CERTIFICATE);
/*      */     
/*  536 */     List<String> backendLinkCertificate = this.repository.findActiveForTestingIds(currentUser, CertificateType.BACKEND_CA_LINK_CERTIFICATE);
/*      */     
/*  538 */     List<String> diagnosticAuthenticationCertificates = this.repository.findActiveForTestingIds(currentUser, CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE);
/*      */     
/*  540 */     List<String> ecuCertificates = this.repository.findActiveForTestingIds(currentUser, CertificateType.ECU_CERTIFICATE);
/*  541 */     List<String> timeCertificates = this.repository.findActiveForTestingIds(currentUser, CertificateType.TIME_CERTIFICATE);
/*      */     
/*  543 */     List<String> variantCodeUserCertificate = this.repository.findActiveForTestingIds(currentUser, CertificateType.VARIANT_CODE_USER_CERTIFICATE);
/*      */     
/*  545 */     List<ZenZefiCertificate> findActiveForTestingEnhanced = this.repository.findActiveForTesting(ZenZefiCertificate.class, currentUser, CertificateType.ENHANCED_RIGHTS_CERTIFICATE);
/*      */ 
/*      */     
/*  548 */     holder.add(CertificateType.ROOT_CA_CERTIFICATE, rooCACertificates);
/*  549 */     holder.add(CertificateType.ROOT_CA_LINK_CERTIFICATE, rootLinkCertificate);
/*  550 */     holder.add(CertificateType.BACKEND_CA_CERTIFICATE, backendCACertificates);
/*  551 */     holder.add(CertificateType.BACKEND_CA_LINK_CERTIFICATE, backendLinkCertificate);
/*  552 */     holder.add(CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE, diagnosticAuthenticationCertificates);
/*  553 */     holder.add(CertificateType.ECU_CERTIFICATE, ecuCertificates);
/*  554 */     holder.add(CertificateType.ENHANCED_RIGHTS_CERTIFICATE, (List)findActiveForTestingEnhanced.stream()
/*  555 */         .filter(cert -> !cert.isSecOCISCert()).map(cert -> cert.getEntityId()).collect(Collectors.toList()));
/*  556 */     holder.add(CertificateType.TIME_CERTIFICATE, timeCertificates);
/*  557 */     holder.add(CertificateType.SEC_OC_IS, (List)findActiveForTestingEnhanced.stream().filter(cert -> cert.isSecOCISCert())
/*  558 */         .map(cert -> cert.getEntityId()).collect(Collectors.toList()));
/*  559 */     holder.add(CertificateType.VARIANT_CODE_USER_CERTIFICATE, variantCodeUserCertificate);
/*  560 */     return holder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CertificatesUseCaseHolder getActiveForTestingBasedOnUseCase(CertificateType type) {
/*  570 */     CertificatesUseCaseHolder useCaseHolder = new CertificatesUseCaseHolder();
/*  571 */     for (TestingUseCaseType testingUseCaseType : TestingUseCaseType.values()) {
/*  572 */       useCaseHolder.getData().put(testingUseCaseType, new ArrayList());
/*      */     }
/*  574 */     List<ZenZefiCertificate> findActiveForTesting = this.repository.findActiveForTesting(ZenZefiCertificate.class, this.session
/*  575 */         .getCurrentUser(), type);
/*  576 */     findActiveForTesting.forEach(certificate -> {
/*      */           if (certificate.isActiveForTesting()) {
/*      */             ((List<String>)useCaseHolder.getData().get(certificate.getTestingType())).add(certificate.getEntityId());
/*      */           }
/*      */         });
/*  581 */     return useCaseHolder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean fullCertificatesUpdate(String clientId) {
/*  591 */     if (!validateUpdateCertificatesClientId(clientId)) {
/*  592 */       return false;
/*      */     }
/*  594 */     this.session.getSystemIntegrityCheckResult().clear();
/*  595 */     this.updaterFactory.getUpdateCertificatesInstance(UpdateType.FULL).updateCertificates();
/*  596 */     return true;
/*      */   }
/*      */   
/*      */   public ExtendedCertificateWithSNResult getTimeCert(String backendCertSkid, String nonce, String vin, String ecu) {
/*      */     Certificate certificateResult;
/*      */     ExtendedCertificateWithSNResult timeCert;
/*  602 */     String METHOD_NAME = "getTimeCert";
/*  603 */     this.logger.entering(CLASS_NAME, "getTimeCert");
/*      */     
/*  605 */     CertificatesProcessValidation.validateGetTimeCert(backendCertSkid, nonce, vin, ecu, this.i18n, this.logger);
/*  606 */     User currentUser = this.session.getCurrentUser();
/*      */ 
/*      */     
/*  609 */     Configuration certSelectionConfiguration = ConfigurationUtil.getConfigurationForUser(this.session.getCurrentUser(), CEBASProperty.CERT_SELECTION, this.logger, this.i18n);
/*      */     
/*  611 */     if (ConfigurationUtil.isAutomaticSelection(certSelectionConfiguration)) {
/*  612 */       certificateResult = getTimeCertForAutomaticSelection(currentUser, backendCertSkid, nonce, vin, ecu);
/*      */     } else {
/*  614 */       certificateResult = this.searchEngine.findCertificateActiveForTesting(currentUser, CertificateType.TIME_CERTIFICATE);
/*      */     } 
/*      */ 
/*      */     
/*  618 */     if (null != certificateResult && certificateResult.getState().equals(CertificateState.ISSUED)) {
/*      */       
/*  620 */       CertificateWithSNResult certificateWithSNResult = new CertificateWithSNResult(this.factory.getCertificateBytes(certificateResult), CertificateParser.hexStringToByteArray(certificateResult.getSerialNo()));
/*  621 */       BackendContext backendContext = certificateResult.getBackendContext();
/*  622 */       timeCert = new ExtendedCertificateWithSNResult(certificateWithSNResult, backendContext.getZkNo());
/*      */       
/*  624 */       this.logger.log(Level.INFO, "000285", "Retrieved from user-specific certificate store Time Certificate with authority key identifier " + certificateResult
/*  625 */           .getAuthorityKeyIdentifier() + " serial number " + certificateResult.getSerialNo(), CLASS_NAME);
/*      */     } else {
/*      */       
/*  628 */       timeCert = new ExtendedCertificateWithSNResult(this.i18n.getMessage("noTimeCertFoundMatchingFilter"));
/*      */     } 
/*      */     
/*  631 */     this.logger.exiting(CLASS_NAME, "getTimeCert");
/*  632 */     return timeCert;
/*      */   }
/*      */   
/*      */   public ExtendedCertificateWithSNResult getSecOCISCert(ISecOCIsInput input) {
/*      */     Certificate certificateResult;
/*      */     ExtendedCertificateWithSNResult secOCISCert;
/*  638 */     String METHOD_NAME = "getSecOCISCert";
/*  639 */     this.logger.entering(CLASS_NAME, "getSecOCISCert");
/*      */     
/*  641 */     User currentUser = this.session.getCurrentUser();
/*  642 */     checkMandatoryParametersForSecOCIs(input);
/*  643 */     CertificatesProcessValidation.validateGetSecOCISCert(input, this.i18n, this.logger);
/*  644 */     Configuration certSelectionConfiguration = ConfigurationUtil.getConfigurationForUser(this.session.getCurrentUser(), CEBASProperty.CERT_SELECTION, this.logger, this.i18n);
/*      */ 
/*      */     
/*  647 */     if (ConfigurationUtil.isAutomaticSelection(certSelectionConfiguration)) {
/*  648 */       certificateResult = getSecOCISCertForAutomaticSelection(currentUser, input);
/*      */     } else {
/*  650 */       certificateResult = this.searchEngine.findSecOCISCertificateActiveForTesting(currentUser);
/*      */     } 
/*      */ 
/*      */     
/*  654 */     if (null != certificateResult && certificateResult.getState().equals(CertificateState.ISSUED)) {
/*      */       
/*  656 */       CertificateWithSNResult certificateWithSNResult = new CertificateWithSNResult(this.factory.getCertificateBytes(certificateResult), CertificateParser.hexStringToByteArray(certificateResult.getSerialNo().trim()));
/*  657 */       BackendContext backendContext = certificateResult.getBackendContext();
/*  658 */       secOCISCert = new ExtendedCertificateWithSNResult(certificateWithSNResult, backendContext.getZkNo());
/*      */       
/*  660 */       this.logger.logWithTranslation(Level.INFO, "000498", "secOcisFoundmatchingFilter", CLASS_NAME);
/*      */     } else {
/*  662 */       secOCISCert = new ExtendedCertificateWithSNResult(this.i18n.getMessage("noSecOcisFoundmatchingFilter"));
/*      */     } 
/*      */     
/*  665 */     this.logger.exiting(CLASS_NAME, "getSecOCISCert");
/*  666 */     return secOCISCert;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CertificateHexFormatOutput exportInMultipleFormat(String id) {
/*  677 */     String METHOD_NAME = "exportInMultipleFormat";
/*  678 */     this.logger.entering(CLASS_NAME, "exportInMultipleFormat");
/*  679 */     Optional<Certificate> certificateByIdOptional = this.searchEngine.findCertificateById(this.session.getCurrentUser(), id);
/*  680 */     if (certificateByIdOptional.isPresent()) {
/*  681 */       Certificate certificate = certificateByIdOptional.get();
/*  682 */       checkAndExecuteExtendedValidation(certificate);
/*  683 */       this.logger.log(Level.INFO, "000044", this.i18n
/*  684 */           .getEnglishMessage("certificateWithIDRetrievedFromUserStore", new String[] {
/*  685 */               certificate.getEntityId()
/*  686 */             }), CertificatesService.class.getName());
/*  687 */       byte[] certificateBytes = this.factory.getCertificateBytes(certificate);
/*  688 */       String configurator5 = CertificateExporter.exportToConfigurator5Format(certificateBytes);
/*  689 */       String cArray = CertificateExporter.exportToCArrayFormat(certificateBytes);
/*  690 */       String canOe = CertificateExporter.exportToCANoeFormat(certificateBytes);
/*  691 */       this.logger.exiting(CLASS_NAME, "exportInMultipleFormat");
/*  692 */       return new CertificateHexFormatOutput(configurator5, cArray, canOe);
/*      */     } 
/*      */     
/*  695 */     CertificateNotFoundException ex = new CertificateNotFoundException(this.i18n.getMessage("certificateNotFound"), "certificateNotFound");
/*  696 */     this.logger.logWithTranslation(Level.WARNING, "000059X", ex.getMessageId(), ex
/*  697 */         .getClass().getSimpleName());
/*  698 */     throw ex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UpdateCertificateMetrics getUpdateCertificatesMetrics() {
/*  707 */     return this.fetchUpdateMetricsTask.getUpdateCertificatesMetrics();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUpdateSessionActive() {
/*  716 */     return this.updateSession.isRunning();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void cancelUpdateSession() {
/*  723 */     this.updateSession.cancel();
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
/*      */   protected Certificate processCSRsSecocis(User currentUser, String serialNo, String targetVIN, String targetECU, Certificate diagCert, Certificate ecuCertificate) {
/*      */     Certificate csr;
/*  739 */     List<Certificate> csrList = this.searchEngine.findSecOCISCSRs(currentUser, serialNo, targetVIN, targetECU, ecuCertificate
/*  740 */         .getSubjectKeyIdentifier(), diagCert.getAuthorityKeyIdentifier());
/*      */     
/*  742 */     if (csrList.isEmpty()) {
/*  743 */       csr = createCSRForSecOCISCertificate(diagCert, targetVIN, targetECU, ecuCertificate
/*  744 */           .getSubjectKeyIdentifier());
/*      */     } else {
/*  746 */       csr = csrList.get(0);
/*  747 */       this.logger.log(Level.INFO, "000057", "Secocis CSR already exists:  Diagnostic SKI:" + diagCert
/*  748 */           .getSubjectKeyIdentifier() + " Target VIN: " + targetVIN + " Target ECU: " + targetECU + " Internal Id: " + csr
/*  749 */           .getEntityId(), CLASS_NAME);
/*      */     } 
/*      */     
/*  752 */     return csr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Certificate downloadTimeCertificate(Certificate timeCsr) {
/*  762 */     if (this.backendLogin.isBackendStateValid()) {
/*  763 */       PKICertificateRequest pkiCertRequest = CertificatesUpdaterFactory.getSimplePKIRequest(timeCsr);
/*      */       try {
/*  765 */         ImportResult importResult = this.downloadTimeCertificateTask.executeWithoutRetry(pkiCertRequest);
/*  766 */         if (importResult.isSuccess()) {
/*  767 */           return this.downloadTimeCertificateTask.findDownloadedTime(timeCsr);
/*      */         }
/*  769 */         throw new TimeException(this.i18n.getEnglishMessage("couldNotImportCertificateDownloadedFromPKI", new String[] { importResult
/*  770 */                 .getMessage() }));
/*      */       }
/*  772 */       catch (DownloadRightsException e) {
/*  773 */         LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/*  774 */         throw new CertificateNotFoundOnDownloadException(this.i18n
/*  775 */             .getMessage("noTimeCertFoundMatchingFilterCriteria") + "Reason: " + this.i18n
/*  776 */             .getMessage("invalidDownloadPermission", new String[] { "Time" }), e
/*  777 */             .getMessage(), Integer.toString(e.getPkiErrorCode()));
/*      */       } 
/*      */     } 
/*  780 */     this.logger.logWithTranslation(Level.WARNING, "000356", "userNotOnline", 
/*  781 */         getClass().getSimpleName());
/*  782 */     return timeCsr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Certificate downloadSecOCISCertificate(Certificate diagCert, Certificate secOCISCsr) {
/*  793 */     if (this.backendLogin.isBackendStateValid()) {
/*      */       
/*      */       try {
/*  796 */         String diagnosticParentSKI = diagCert.getParent().getSubjectKeyIdentifier().replace("-", "").toLowerCase();
/*  797 */         String pkcs10Content = secOCISCsr.getPkcs10Signature();
/*  798 */         String diagnosticAsBase64 = Base64.getEncoder().encodeToString(this.factory.getCertificateBytes(diagCert));
/*  799 */         String secocisType = secOCISCsr.getType().name();
/*  800 */         PKIEnhancedCertificateRequest pkiCertificateRequest = new PKIEnhancedCertificateRequest(pkcs10Content, diagnosticParentSKI, secocisType, diagnosticAsBase64);
/*      */         
/*  802 */         pkiCertificateRequest.setInternalCSRId(secOCISCsr.getEntityId());
/*  803 */         ImportResult result = this.downloadSecOCISCertificateTask.executeWithoutRetry(pkiCertificateRequest);
/*  804 */         if (result.isSuccess()) {
/*  805 */           return this.downloadSecOCISCertificateTask.findDownloadedSecocis(secOCISCsr);
/*      */         }
/*  807 */         throw new SecOCISException(this.i18n.getEnglishMessage("couldNotImportCertificateDownloadedFromPKI", new String[] { result
/*  808 */                 .getMessage() }));
/*      */       }
/*  810 */       catch (DownloadRightsException e) {
/*  811 */         LOG.log(Level.FINEST, e.getMessage(), (Throwable)e);
/*  812 */         throw new CertificateNotFoundOnDownloadException(this.i18n
/*  813 */             .getMessage("noSecOcisFoundMatchingFilterCriteria") + "Reason: " + this.i18n
/*  814 */             .getMessage("invalidDownloadPermission", new String[] { "Secocis" }), e
/*  815 */             .getMessage(), Integer.toString(e.getPkiErrorCode()));
/*      */       } 
/*      */     }
/*  818 */     this.logger.logWithTranslation(Level.WARNING, "000356", "userNotOnline", 
/*  819 */         getClass().getSimpleName());
/*  820 */     return secOCISCsr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean allowImportOfPrivateKeys() {
/*  827 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkMandatoryParametersForSecOCIs(ISecOCIsInput input) {
/*  836 */     if (input.isInvalid()) {
/*  837 */       SecOCISException secOCISException = new SecOCISException("Wrong input! Please check the mandatory fields. They cannot be empty!");
/*  838 */       this.logger.logWithExceptionByInfo("000501X", (CEBASException)secOCISException);
/*  839 */       throw secOCISException;
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
/*      */   private Certificate getTimeCertForAutomaticSelection(User currentUser, String backendCertSkid, String nonce, String vin, String ecu) {
/*      */     Certificate certificateResult;
/*  855 */     byte[] decodedNonce = Base64.getDecoder().decode(nonce);
/*  856 */     Optional<Certificate> certificate = this.searchEngine.findTimeCertificate(currentUser, backendCertSkid, decodedNonce, vin, ecu);
/*  857 */     if (certificate.isPresent()) {
/*  858 */       certificateResult = certificate.get();
/*      */     } else {
/*  860 */       List<Certificate> csrList = this.searchEngine.findTimeCSRs(currentUser, backendCertSkid, HexUtil.base64ToHex(nonce), vin, ecu);
/*      */       try {
/*      */         Certificate csrResult;
/*  863 */         if (csrList.isEmpty()) {
/*  864 */           if (this.session.isDefaultUser()) {
/*      */             
/*  866 */             CEBASCertificateException cannotCreateSecOCISCSRException = new CEBASCertificateException(this.i18n.getMessage("noTimeCSRCanBeCreatedUserIsNotLoggedIn"));
/*  867 */             this.logger.logWithExceptionByWarning("000277X", (CEBASException)cannotCreateSecOCISCSRException);
/*  868 */             throw cannotCreateSecOCISCSRException;
/*      */           } 
/*  870 */           Optional<Certificate> backendCert = this.searchEngine.findCertificateBySkiAndType(currentUser, backendCertSkid, CertificateType.BACKEND_CA_CERTIFICATE);
/*  871 */           if (backendCert.isPresent() && Boolean.FALSE.equals(((ZenZefiCertificate)backendCert.get()).getPkiKnown())) {
/*      */             
/*  873 */             CEBASCertificateException cannotCreateCSRException = new CEBASCertificateException(this.i18n.getMessage("noCSRCanBeCreatedForPKIUnknownBackend"));
/*  874 */             this.logger.logWithExceptionByWarning("000670X", (CEBASException)cannotCreateCSRException);
/*  875 */             throw cannotCreateCSRException;
/*      */           } 
/*  877 */           csrResult = MdcDecoratorCompletableFuture.supplyAsync(() -> createCSRForTimeCertificate(backendCertSkid, nonce, vin, ecu)).get();
/*      */         }
/*      */         else {
/*      */           
/*  881 */           csrResult = csrList.get(0);
/*  882 */           this.logger.log(Level.INFO, "000057", "Time CSR already exists:  Backend SKI:" + backendCertSkid + " Target VIN: " + vin + " Target ECU: " + ecu + " Nonce: " + 
/*      */               
/*  884 */               HexUtil.base64ToHex(nonce) + " Internal Id: " + csrResult.getEntityId(), CLASS_NAME);
/*      */         } 
/*      */         
/*  887 */         certificateResult = MdcDecoratorCompletableFuture.supplyAsync(() -> downloadTimeCertificate(csrResult)).get();
/*  888 */       } catch (ExecutionException e) {
/*  889 */         if (CEBASException.hasCEBASExceptionCause(e)) {
/*  890 */           throw (RuntimeException)e.getCause();
/*      */         }
/*  892 */         this.logger.log(Level.INFO, "000583", e.getCause().getMessage(), 
/*  893 */             getClass().getSimpleName());
/*  894 */         throw new CertificatesUpdateException(e.getCause().getMessage());
/*      */       }
/*  896 */       catch (InterruptedException e) {
/*  897 */         Thread.currentThread().interrupt();
/*  898 */         this.logger.log(Level.INFO, "000584", e.getCause().getMessage(), 
/*  899 */             getClass().getSimpleName());
/*  900 */         throw new CertificatesUpdateException(e.getCause().getMessage());
/*      */       } 
/*      */     } 
/*  903 */     return certificateResult;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Certificate getSecOCISCertForAutomaticSelection(User currentUser, ISecOCIsInput input) {
/*      */     Certificate certificateResult;
/*  915 */     String backendSubjectKeyIdentifier = HexUtil.base64ToHex(input.getBackendCertSubjKeyId());
/*  916 */     String serialNo = HexUtil.base64ToHex(input.getDiagCertSerialNumber());
/*  917 */     String targetVIN = input.getTargetVIN();
/*  918 */     String targetECU = input.getTargetECU();
/*  919 */     List<Certificate> diagnosticCertificates = this.searchEngine.findValidDiagnosticCertificates(currentUser, backendSubjectKeyIdentifier, serialNo);
/*      */     
/*  921 */     if (diagnosticCertificates.isEmpty())
/*  922 */       throw getAndLogCertificateNotFound(); 
/*  923 */     if (diagnosticCertificates.size() > 1) {
/*      */       
/*  925 */       CertificateMoreResultsFoundException certificateMoreResultsFoundException = new CertificateMoreResultsFoundException(this.i18n.getMessage("moreDiagCertFoundForCombo"));
/*  926 */       this.logger.logWithExceptionByInfo("000500X", (CEBASException)certificateMoreResultsFoundException);
/*  927 */       throw certificateMoreResultsFoundException;
/*      */     } 
/*      */     
/*  930 */     Certificate diagCert = diagnosticCertificates.get(0);
/*  931 */     Certificate ecuCertificate = this.factory.getCertificateFromBase64(input.getEcuCertificate());
/*  932 */     String targetSubjectKeyIdentifier = ecuCertificate.getSubjectKeyIdentifier();
/*  933 */     List<Certificate> secOCISCertificates = this.searchEngine.findSecOCISCertificates(currentUser, diagCert, targetVIN, targetECU, targetSubjectKeyIdentifier);
/*      */     
/*  935 */     if (secOCISCertificates.isEmpty()) {
/*  936 */       if (this.session.isDefaultUser()) {
/*      */         
/*  938 */         CEBASCertificateException cannotCreateSecOCISCSRException = new CEBASCertificateException(this.i18n.getMessage("noSecOCISCSRCanBeCreatedUserIsNotLoggedIn"));
/*  939 */         this.logger.logWithExceptionByWarning("000277X", (CEBASException)cannotCreateSecOCISCSRException);
/*  940 */         throw cannotCreateSecOCISCSRException;
/*      */       } 
/*  942 */       ZenZefiCertificate backendCert = (ZenZefiCertificate)diagCert.getParent();
/*  943 */       if (null != backendCert && Boolean.FALSE.equals(backendCert.getPkiKnown())) {
/*      */         
/*  945 */         CEBASCertificateException cannotCreateCSRException = new CEBASCertificateException(this.i18n.getMessage("noCSRCanBeCreatedForPKIUnknownBackend"));
/*  946 */         this.logger.logWithExceptionByWarning("000679X", (CEBASException)cannotCreateCSRException);
/*  947 */         throw cannotCreateCSRException;
/*      */       } 
/*  949 */       certificateResult = createCSRSecocis(currentUser, serialNo, targetVIN, targetECU, diagCert, ecuCertificate);
/*      */       
/*  951 */       certificateResult = downloadSecocis(diagCert, certificateResult);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  956 */       certificateResult = (secOCISCertificates.size() == 1) ? secOCISCertificates.get(0) : this.searchEngine.getCertificateByValidityAndSignatureAscending(secOCISCertificates);
/*  957 */       this.logger.log(Level.INFO, "000286", "Retrieved from user-specific certificate store Secocis Certificate with authority key identifier " + certificateResult
/*      */           
/*  959 */           .getAuthorityKeyIdentifier() + " serial number " + certificateResult
/*  960 */           .getSerialNo(), CLASS_NAME);
/*      */     } 
/*      */ 
/*      */     
/*  964 */     return certificateResult;
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
/*      */   private Certificate createCSRSecocis(User currentUser, String serialNo, String targetVIN, String targetECU, Certificate diagCert, Certificate ecuCertificate) {
/*      */     Certificate secOCISCsr;
/*      */     try {
/*  984 */       secOCISCsr = MdcDecoratorCompletableFuture.supplyAsync(() -> processCSRsSecocis(currentUser, serialNo, targetVIN, targetECU, diagCert, ecuCertificate)).get();
/*  985 */     } catch (ExecutionException e) {
/*  986 */       if (CEBASException.hasCEBASExceptionCause(e)) {
/*  987 */         throw (RuntimeException)e.getCause();
/*      */       }
/*  989 */       this.logger.log(Level.INFO, "000580", e.getCause().getMessage(), 
/*  990 */           getClass().getSimpleName());
/*  991 */       throw new CertificatesUpdateException(e.getCause().getMessage());
/*      */     }
/*  993 */     catch (InterruptedException e) {
/*  994 */       Thread.currentThread().interrupt();
/*  995 */       this.logger.log(Level.INFO, "000581", e.getCause().getMessage(), 
/*  996 */           getClass().getSimpleName());
/*  997 */       throw new CertificatesUpdateException(e.getCause().getMessage());
/*      */     } 
/*      */     
/* 1000 */     return secOCISCsr;
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
/*      */   private Certificate downloadSecocis(Certificate diagCert, Certificate secOCISCsr) {
/*      */     Certificate certificateResult;
/*      */     try {
/* 1014 */       certificateResult = MdcDecoratorCompletableFuture.supplyAsync(() -> downloadSecOCISCertificate(diagCert, secOCISCsr)).get();
/* 1015 */     } catch (ExecutionException e) {
/* 1016 */       if (CEBASException.hasCEBASExceptionCause(e)) {
/* 1017 */         throw (RuntimeException)e.getCause();
/*      */       }
/* 1019 */       this.logger.log(Level.INFO, "000582", e.getCause().getMessage(), 
/* 1020 */           getClass().getSimpleName());
/* 1021 */       throw new CertificatesUpdateException(e.getCause().getMessage());
/*      */     }
/* 1023 */     catch (InterruptedException e) {
/* 1024 */       Thread.currentThread().interrupt();
/* 1025 */       throw new CertificatesUpdateException(e.getCause().getMessage());
/*      */     } 
/* 1027 */     return certificateResult;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Certificate createCSRForTimeCertificate(String backendCertSkid, String nonce, String vin, String ecu) {
/* 1037 */     this.logger.log(Level.INFO, "000276", "Start Create Time CSR process.", CLASS_NAME);
/*      */     
/* 1039 */     Certificate backendCert = (Certificate)this.searchEngine.findCertificate(this.session.getCurrentUser(), backendCertSkid).orElseThrow(this.logger.logWithTranslationSupplier(Level.WARNING, "000055X", (CEBASException)new CertificateNotFoundException(this.i18n
/*      */             
/* 1041 */             .getMessage("noCertificateFoundforBackendSkidWhenCreatingCSR"), "noCertificateFoundforBackendSkidWhenCreatingCSR")));
/*      */     
/* 1043 */     Date maxDate = this.certificateProfileConfiguration.getCSRValidTo(this.logger, this.i18n);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1049 */     CertificateSignRequest csr = new CertificateSignRequest("CN=" + this.session.getCurrentUser().getUserName(), CertificateType.TIME_CERTIFICATE.name(), UserRole.AS_BASIC.getText(), ecu, vin, nonce, null, null, null, maxDate, backendCert.getEntityId(), this.session.getCurrentUser().getEntityId(), ObjectIdentifier.ALGORITHM_IDENTIFIER_OID.getOid(), "0.1", this.prodQualifierValue, Base64.getEncoder().encodeToString(HexUtil.hexStringToByteArray(backendCert.getSubjectKeyIdentifier())));
/* 1050 */     Certificate timeCSR = createCertificateInSignRequestState(csr, true);
/* 1051 */     this.logger.log(Level.INFO, "000057", "Stop Create Time CSR process.", CLASS_NAME);
/* 1052 */     return timeCSR;
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
/*      */   private void clearOthers(ZenZefiCertificate zenZefiCertificate, List<ZenZefiCertificate> existingActiveForTestingList) {
/* 1066 */     List<String> idsToSetOnFalse = (List<String>)existingActiveForTestingList.stream().filter(certificate -> !certificate.getEntityId().equals(zenZefiCertificate.getEntityId())).map(cert -> cert.getEntityId()).collect(Collectors.toList());
/* 1067 */     this.repository.setNotActiveForTestingIds(ZenZefiCertificate.class, idsToSetOnFalse);
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
/*      */   private Certificate createCSRForSecOCISCertificate(Certificate diagCert, String vin, String ecu, String targetSubjectKeyIdentifier) {
/* 1081 */     User currentUser = this.session.getCurrentUser();
/* 1082 */     Date maxDate = this.certificateProfileConfiguration.getCSRValidTo(this.logger, this.i18n);
/* 1083 */     String base64AuthKey = CertificateParser.hexToBase64(diagCert.getAuthorityKeyIdentifier());
/*      */ 
/*      */     
/* 1086 */     CertificateSignRequest csr = new CertificateSignRequest(CertificateType.ENHANCED_RIGHTS_CERTIFICATE.getText(), "", ecu, vin, "", "", "", "", targetSubjectKeyIdentifier, base64AuthKey, maxDate, diagCert.getEntityId(), currentUser.getEntityId(), ObjectIdentifier.ALGORITHM_IDENTIFIER_OID.getOid(), "0.1", this.prodQualifierValue, "");
/*      */     
/* 1088 */     return createCertificateInSignRequestState(csr, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkSelectionType() {
/* 1095 */     if (ConfigurationUtil.isAutomaticSelection(this.session.getCurrentUser(), this.logger, this.i18n)) {
/* 1096 */       this.logger.log(Level.INFO, "000590", this.i18n
/* 1097 */           .getMessage("cannotChangeActiveForTestingAutomaticMode"), CLASS_NAME);
/* 1098 */       throw new CEBASCertificateException(this.i18n
/* 1099 */           .getMessage("cannotChangeActiveForTestingAutomaticMode"));
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
/*      */   private boolean validateUpdateCertificatesClientId(String clientId) {
/* 1111 */     String[] activeProfiles = this.session.activeProfiles();
/* 1112 */     if (ProfileChecker.isAfterSales(activeProfiles) && !this.session.isDefaultUser()) {
/* 1113 */       return true;
/*      */     }
/* 1115 */     if (this.session.isDefaultUser()) {
/* 1116 */       this.logger.log(Level.WARNING, "000356", "Default user cannot start certificates update", 
/* 1117 */           getClass().getSimpleName());
/* 1118 */       return false;
/*      */     } 
/* 1120 */     return hasToken(clientId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean hasToken(String clientId) {
/* 1130 */     if (this.session.getToken() == null && ZenZefiConstants.getAvailableClients().contains(clientId)) {
/* 1131 */       throw getNoTokenException();
/*      */     }
/* 1133 */     return (this.session.getToken() != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CEBASCertificateException getNoTokenException() {
/* 1144 */     CEBASCertificateException zenzefiCertificateException = new CEBASCertificateException(this.i18n.getMessage("noAuthorizationTokenFound"), "noAuthorizationTokenFound");
/* 1145 */     this.logger.logWithTranslation(Level.WARNING, "000010X", zenzefiCertificateException
/* 1146 */         .getMessageId(), zenzefiCertificateException.getClass().getSimpleName());
/* 1147 */     return zenzefiCertificateException;
/*      */   }
/*      */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\certificates\control\ZenZefiCertificatesService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */