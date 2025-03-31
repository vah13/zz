/*      */ package com.daimler.cebas.certificates.control;
/*      */ 
/*      */ import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
/*      */ import com.daimler.cebas.certificates.control.exceptions.CertificateMoreResultsFoundException;
/*      */ import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
/*      */ import com.daimler.cebas.certificates.control.exceptions.CertificateSigningException;
/*      */ import com.daimler.cebas.certificates.control.repository.CertificateRepository;
/*      */ import com.daimler.cebas.certificates.control.validation.CertificatesValidator;
/*      */ import com.daimler.cebas.certificates.entity.Certificate;
/*      */ import com.daimler.cebas.certificates.entity.CertificateState;
/*      */ import com.daimler.cebas.certificates.entity.CertificateType;
/*      */ import com.daimler.cebas.certificates.entity.UserRole;
/*      */ import com.daimler.cebas.common.control.CEBASException;
/*      */ import com.daimler.cebas.common.control.CEBASProperty;
/*      */ import com.daimler.cebas.common.control.HexUtil;
/*      */ import com.daimler.cebas.common.control.MetadataManager;
/*      */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*      */ import com.daimler.cebas.common.entity.EmptyPredicate;
/*      */ import com.daimler.cebas.common.entity.InPredicate;
/*      */ import com.daimler.cebas.configuration.control.ConfigurationUtil;
/*      */ import com.daimler.cebas.configuration.entity.Configuration;
/*      */ import com.daimler.cebas.logs.control.Logger;
/*      */ import com.daimler.cebas.users.control.Session;
/*      */ import com.daimler.cebas.users.entity.User;
/*      */ import com.daimler.cebas.users.entity.UserKeyPair;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.logging.Level;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import javax.persistence.Tuple;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ import org.springframework.beans.factory.annotation.Autowired;
/*      */ import org.springframework.data.domain.Pageable;
/*      */ import org.springframework.util.CollectionUtils;
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
/*      */ @CEBASControl
/*      */ public class SearchEngine
/*      */ {
/*   83 */   private static final String CLASS_NAME = SearchEngine.class.getSimpleName();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String FIND_DIAGNOSTIC_CERTIFICATES = "findDiagnosticCertificates";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CertificateRepository certificateRepository;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Logger logger;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected MetadataManager i18n;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Autowired
/*      */   protected Session session;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Autowired
/*      */   public SearchEngine(CertificateRepository certificateRepository, Logger logger, MetadataManager i18n) {
/*  119 */     this.certificateRepository = certificateRepository;
/*  120 */     this.logger = logger;
/*  121 */     this.i18n = i18n;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Map<String, Object> getDefaultQueryParams(User user) {
/*  129 */     Map<String, Object> params = new HashMap<>();
/*  130 */     params.put("user", user);
/*  131 */     params.put("state", CertificateState.ISSUED);
/*      */     
/*  133 */     return params;
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
/*      */   public <T extends Certificate> List<T> getCertsByAKI(User user, List<String> authorityKeyIDs, Class<T> clazz) {
/*  145 */     String METHOD_NAME = "getCertsByAKI";
/*  146 */     this.logger.entering(CLASS_NAME, "getCertsByAKI");
/*      */     
/*  148 */     Map<String, Object> map = getDefaultQueryParams(user);
/*  149 */     if (!CollectionUtils.isEmpty(authorityKeyIDs)) {
/*  150 */       map.put("authorityKeyIdentifier", new InPredicate(new Object[] { authorityKeyIDs }));
/*      */     }
/*  152 */     List<T> certificates = this.certificateRepository.findWithQuery(clazz, map, -1);
/*      */     
/*  154 */     this.logger.exiting(CLASS_NAME, "getCertsByAKI");
/*      */     
/*  156 */     return certificates;
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
/*      */   public List<Certificate> findDiagnosticCertificates(User user, String backendSubjectKeyIdentifier, String serialNo) {
/*  173 */     return findDiagnosticCertificates(user, backendSubjectKeyIdentifier, serialNo, Certificate.class);
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
/*      */   public <T extends Certificate> List<T> findDiagnosticCertificates(User user, String backendSubjectKeyIdentifier, String serialNo, Class<T> type) {
/*  190 */     String METHOD_NAME = "findDiagnosticCertificates";
/*  191 */     this.logger.entering(CLASS_NAME, "findDiagnosticCertificates");
/*  192 */     serialNo = HexUtil.omitLeadingZeros(serialNo);
/*  193 */     Map<String, Object> params = getDefaultQueryParams(user);
/*  194 */     params.put("serialNo", serialNo);
/*  195 */     params.put("type", CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE);
/*  196 */     params.put("authorityKeyIdentifier", backendSubjectKeyIdentifier);
/*  197 */     this.logger.exiting(CLASS_NAME, "findDiagnosticCertificates");
/*  198 */     return this.certificateRepository.findWithQuery(type, params, 0);
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
/*      */   public List<Certificate> findDiagnosticCertificates(User user, Certificate backend) {
/*  212 */     String METHOD_NAME = "findDiagnosticCertificates";
/*  213 */     this.logger.entering(CLASS_NAME, "findDiagnosticCertificates");
/*      */     
/*  215 */     Map<String, Object> params = getDefaultQueryParams(user);
/*  216 */     params.put("type", CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE);
/*  217 */     params.put("parent", backend);
/*      */     
/*  219 */     this.logger.exiting(CLASS_NAME, "findDiagnosticCertificates");
/*  220 */     return this.certificateRepository.findWithQuery(Certificate.class, params, 0);
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
/*      */   public List<Certificate> findValidDiagnosticCertificates(User user, String backendSubjectKeyIdentifier, String serialNo) {
/*  237 */     List<Certificate> findDiagnosticCertificates = findDiagnosticCertificates(user, backendSubjectKeyIdentifier, serialNo);
/*      */     
/*  239 */     List<Certificate> validCertificates = getValidRegularCertificates(findDiagnosticCertificates);
/*  240 */     return getOnesWithValidPublicKeyAndPrivateKey(validCertificates);
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
/*      */   public List<Certificate> findDiagnosticCertificatesByBackendSKIs(User user, List<String> backendSubjectKeyIdentifiers, String userRole, String targetECU, String targetVIN) {
/*  261 */     String METHOD_NAME = "findDiagnosticCertificatesByBackendSKIs";
/*  262 */     this.logger.entering(CLASS_NAME, "findDiagnosticCertificatesByBackendSKIs");
/*      */     
/*  264 */     List<Certificate> result = this.certificateRepository.findDiagnosticCertificatesForBackends(user, backendSubjectKeyIdentifiers, userRole, targetECU, targetVIN);
/*      */ 
/*      */     
/*  267 */     this.logger.exiting(CLASS_NAME, "findDiagnosticCertificatesByBackendSKIs");
/*  268 */     return result;
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
/*      */   public Optional<Certificate> findValidDiagOrTimeCertificate(User user, String backendSubjectKeyIdentifier, String serialNo) {
/*  284 */     String METHOD_NAME = "findValidDiagOrTimeCertificate";
/*  285 */     this.logger.entering(CLASS_NAME, "findValidDiagOrTimeCertificate");
/*  286 */     serialNo = HexUtil.omitLeadingZeros(serialNo);
/*  287 */     Map<String, Object> params = getDefaultQueryParams(user);
/*  288 */     params.put("serialNo", serialNo);
/*  289 */     params.put("type", CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE);
/*  290 */     params.put("authorityKeyIdentifier", backendSubjectKeyIdentifier);
/*      */     
/*  292 */     List<Certificate> repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/*  293 */     List<Certificate> result = getValidRegularCertificates(repoResult);
/*  294 */     result = getOnesWithValidPublicKeyAndPrivateKey(result);
/*  295 */     if (result.isEmpty()) {
/*  296 */       params.put("type", CertificateType.TIME_CERTIFICATE);
/*  297 */       result = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/*  298 */       result = getValidRegularCertificates(result);
/*  299 */       result = getOnesWithValidPublicKeyAndPrivateKey(result);
/*  300 */       if (result.isEmpty()) {
/*  301 */         this.logger.exiting(CLASS_NAME, "findValidDiagOrTimeCertificate");
/*  302 */         return Optional.empty();
/*  303 */       }  if (result.size() == 1) {
/*  304 */         this.logger.exiting(CLASS_NAME, "findValidDiagOrTimeCertificate");
/*  305 */         return Optional.of(result.get(0));
/*      */       } 
/*  307 */       throw logAndThrowMoreDiagOrTimeCertFound();
/*      */     } 
/*  309 */     if (result.size() == 1) {
/*  310 */       this.logger.exiting(CLASS_NAME, "findValidDiagOrTimeCertificate");
/*  311 */       return Optional.of(result.get(0));
/*      */     } 
/*  313 */     throw logAndThrowMoreDiagOrTimeCertFound();
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
/*      */   public <T extends Certificate> List<T> findCertByAuthKeyIdentAndSerialNo(User user, String authorityKeyIdentifier, String serialNo, Class<T> clazz) {
/*  333 */     String METHOD_NAME = "findCertByAuthKeyIdentAndSerialNo";
/*  334 */     this.logger.entering(CLASS_NAME, "findCertByAuthKeyIdentAndSerialNo");
/*  335 */     serialNo = HexUtil.omitLeadingZeros(serialNo);
/*  336 */     Map<String, Object> params = getDefaultQueryParams(user);
/*  337 */     params.put("authorityKeyIdentifier", authorityKeyIdentifier);
/*  338 */     params.put("serialNo", serialNo);
/*  339 */     this.logger.exiting(CLASS_NAME, "findCertByAuthKeyIdentAndSerialNo");
/*  340 */     return this.certificateRepository.findWithQuery(clazz, params, 0);
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
/*      */   public <T extends Certificate> List<Tuple> findTuples(User user, Map<String, Object> queryParameters, List<String> columns, Class<T> clazz) {
/*  361 */     String METHOD_NAME = "findTuples";
/*  362 */     this.logger.entering(CLASS_NAME, "findTuples");
/*      */     
/*  364 */     Map<String, Object> defaultParams = getDefaultQueryParams(user);
/*  365 */     defaultParams.putAll(queryParameters);
/*  366 */     List<Tuple> result = this.certificateRepository.findTupleWithQuery(clazz, defaultParams, columns, 0);
/*      */     
/*  368 */     this.logger.exiting(CLASS_NAME, "findTuples");
/*  369 */     return result;
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
/*      */   public <T extends Certificate> List<? extends Certificate> findCertByAuthKeyIdentAndSerialNoValid(User user, String authorityKeyIdentifier, String serialNo, Class<T> clazz) {
/*  388 */     String METHOD_NAME = "findCertByAuthKeyIdentAndSerialNoValid";
/*  389 */     this.logger.entering(CLASS_NAME, "findCertByAuthKeyIdentAndSerialNoValid");
/*  390 */     serialNo = HexUtil.omitLeadingZeros(serialNo);
/*  391 */     Map<String, Object> params = getDefaultQueryParams(user);
/*  392 */     params.put("authorityKeyIdentifier", authorityKeyIdentifier);
/*  393 */     params.put("serialNo", serialNo);
/*  394 */     this.logger.exiting(CLASS_NAME, "findCertByAuthKeyIdentAndSerialNoValid");
/*  395 */     List<? extends Certificate> ecus = this.certificateRepository.findWithQuery(clazz, params, 0);
/*  396 */     validateParent(ecus, false);
/*  397 */     return getValidCertificatesExtends(ecus);
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
/*      */   public Optional<Certificate> findValidDiagnosticCertificate(User user, String backendSubjectKeyIdentifier, String targetVIN, String targetECU, String userRole) {
/*      */     List<Certificate> result;
/*  418 */     String METHOD_NAME = "findDiagnosticCertificates";
/*  419 */     this.logger.entering(CLASS_NAME, "findDiagnosticCertificates");
/*      */     
/*  421 */     Map<String, Object> params = getDefaultQueryParams(user);
/*  422 */     params.put("type", CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE);
/*  423 */     params.put("authorityKeyIdentifier", backendSubjectKeyIdentifier);
/*  424 */     if (!UserRole.NO_ROLE.getText().equals(userRole)) {
/*  425 */       params.put("userRole", userRole);
/*  426 */       List<Certificate> repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/*  427 */       validateParent(repoResult, false);
/*  428 */       List<Certificate> validCertificates = getValidRegularCertificates(repoResult);
/*  429 */       validCertificates = getOnesWithValidPublicKeyAndPrivateKey(validCertificates);
/*  430 */       result = getFilteredByTargetVINTargetECU(validCertificates, targetECU, targetVIN);
/*      */     } else {
/*  432 */       List<Certificate> repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/*  433 */       validateParent(repoResult, false);
/*  434 */       List<Certificate> validCertificates = getValidRegularCertificates(repoResult);
/*  435 */       validCertificates = getOnesWithValidPublicKeyAndPrivateKey(validCertificates);
/*  436 */       result = determineResultBasedOnRolePriorityAndTargetVIN(user, targetVIN, targetECU, validCertificates);
/*      */     } 
/*  438 */     Certificate certificate = getCertificateByValidityAndSignatureAscending(result);
/*  439 */     this.logger.exiting(CLASS_NAME, "findDiagnosticCertificates");
/*  440 */     return Optional.ofNullable(certificate);
/*      */   }
/*      */   
/*      */   private List<Certificate> determineResultBasedOnRolePriorityAndTargetVIN(User user, String targetVIN, String targetECU, List<Certificate> validCertificates) {
/*  444 */     List<Certificate> result = new ArrayList<>();
/*  445 */     for (Map.Entry<String, List<Certificate>> group : getCertificatesGroupBaseOnRolesPriority(user, validCertificates)) {
/*      */       
/*  447 */       List<Certificate> groupCertificates = group.getValue();
/*  448 */       result = getFilteredByTargetVINTargetECU(groupCertificates, targetECU, targetVIN);
/*  449 */       if (!result.isEmpty()) {
/*      */         break;
/*      */       }
/*      */     } 
/*  453 */     return result;
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
/*      */   public Optional<Certificate> getIssuerOfLinkCertificate(User user, Certificate linkCertificate) {
/*  465 */     String METHOD_NAME = "getIssuerOfLinkCertificate";
/*  466 */     this.logger.entering(CLASS_NAME, "getIssuerOfLinkCertificate");
/*  467 */     CertificateType type = (linkCertificate.getType() == CertificateType.ROOT_CA_LINK_CERTIFICATE) ? CertificateType.ROOT_CA_CERTIFICATE : CertificateType.BACKEND_CA_CERTIFICATE;
/*      */     
/*  469 */     Optional<Certificate> optional = findCertificateBySkiAndType(user, linkCertificate.getAuthorityKeyIdentifier(), type);
/*  470 */     this.logger.exiting(CLASS_NAME, "getIssuerOfLinkCertificate");
/*  471 */     return optional;
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
/*      */   public Optional<Certificate> findTimeCertificate(User user, String backendSubjectKeyIdentifier, byte[] nonce, String targetVIN, String targetECU) {
/*  492 */     String METHOD_NAME = "findTimeCertificate";
/*  493 */     this.logger.entering(CLASS_NAME, "findTimeCertificate");
/*      */ 
/*      */     
/*  496 */     Map<String, Object> params = getDefaultQueryParams(user);
/*  497 */     params.put("type", CertificateType.TIME_CERTIFICATE);
/*  498 */     params.put("authorityKeyIdentifier", backendSubjectKeyIdentifier);
/*      */     
/*  500 */     List<Certificate> repoResult = (List<Certificate>)this.certificateRepository.findWithQuery(Certificate.class, params, 0).stream().filter(c -> Arrays.equals(c.getNonceRaw(), nonce)).collect(Collectors.toList());
/*  501 */     validateParent(repoResult, false);
/*  502 */     List<Certificate> validCertificates = getValidRegularCertificates(repoResult);
/*  503 */     validCertificates = getOnesWithValidPublicKeyAndPrivateKey(validCertificates);
/*  504 */     List<Certificate> result = determineResultBasedOnRolePriorityAndTargetVIN(user, targetVIN, targetECU, validCertificates);
/*  505 */     Certificate certificate = getCertificateByValidityAndSignatureAscending(result);
/*  506 */     this.logger.exiting(CLASS_NAME, "findTimeCertificate");
/*  507 */     return Optional.ofNullable(certificate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Certificate> getValidRegularCertificates(List<Certificate> repoResult) {
/*  518 */     if (!isExtendedValidation()) {
/*  519 */       return repoResult;
/*      */     }
/*  521 */     return (List<Certificate>)repoResult.stream()
/*  522 */       .filter(cert -> CertificatesValidator.isValidInChain(cert, this.i18n, this.logger))
/*  523 */       .collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isExtendedValidation() {
/*  532 */     User user = this.session.getCurrentUser();
/*  533 */     return ConfigurationUtil.hasUserExtendedValidation(user, this.logger, this.i18n);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<? extends Certificate> getValidCertificatesExtends(List<? extends Certificate> repoResult) {
/*  544 */     if (!isExtendedValidation()) {
/*  545 */       return repoResult;
/*      */     }
/*  547 */     return (List<? extends Certificate>)repoResult.stream()
/*  548 */       .filter(cert -> CertificatesValidator.isValidInChain(cert, this.i18n, this.logger))
/*  549 */       .collect(Collectors.toList());
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
/*      */   private List<Certificate> getOnesWithValidPublicKeyAndPrivateKey(List<Certificate> validResult) {
/*  561 */     if (!isExtendedValidation()) {
/*  562 */       return validResult;
/*      */     }
/*  564 */     return (List<Certificate>)validResult
/*  565 */       .stream()
/*  566 */       .filter(cert -> CertificatesValidator.checkPublicKeyMatchesTheOneGeneratedFromPrivateKey(this.session, cert, this.logger))
/*      */       
/*  568 */       .collect(Collectors.toList());
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
/*      */   public List<Certificate> findCSRsUnderBackend(User user, Certificate backend) {
/*  581 */     String METHOD_NAME = "findCSRsUnderBackend";
/*  582 */     this.logger.entering(CLASS_NAME, "findCSRsUnderBackend");
/*      */     
/*  584 */     Map<String, Object> params = new HashMap<>();
/*  585 */     params.put("state", CertificateState.SIGNING_REQUEST);
/*  586 */     params.put("user", user);
/*  587 */     params.put("parent", backend);
/*  588 */     List<Certificate> csrs = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/*      */     
/*  590 */     this.logger.exiting(CLASS_NAME, "findCSRsUnderBackend");
/*  591 */     return csrs;
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
/*      */   public Optional<Certificate> findCsrById(User user, String id) {
/*  604 */     String METHOD_NAME = "findCsrById";
/*  605 */     this.logger.entering(CLASS_NAME, "findCsrById");
/*  606 */     Map<String, Object> params = new HashMap<>();
/*  607 */     params.put("state", CertificateState.SIGNING_REQUEST);
/*  608 */     params.put("user", user);
/*  609 */     params.put("entityId", id);
/*  610 */     List<Certificate> csrResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/*  611 */     this.logger.exiting(CLASS_NAME, "findCsrById");
/*  612 */     return !csrResult.isEmpty() ? Optional.<Certificate>of(csrResult.get(0)) : Optional.<Certificate>empty();
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
/*      */   public Optional<Certificate> findCsrByPublicKey(User user, String publicKey) {
/*  625 */     String METHOD_NAME = "findCsrByPublicKey";
/*  626 */     this.logger.entering(CLASS_NAME, "findCsrByPublicKey");
/*      */     
/*  628 */     Map<String, Object> params = new HashMap<>();
/*  629 */     params.put("state", CertificateState.SIGNING_REQUEST);
/*  630 */     params.put("user", user);
/*  631 */     params.put("subjectPublicKey", publicKey);
/*      */     
/*  633 */     List<Certificate> csrResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/*      */     
/*  635 */     this.logger.exiting(CLASS_NAME, "findCsrByPublicKey");
/*  636 */     return !csrResult.isEmpty() ? Optional.<Certificate>of(csrResult.get(0)) : Optional.<Certificate>empty();
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
/*      */   public Optional<Certificate> findCertificateById(User user, String id) {
/*  649 */     String METHOD_NAME = "findCsrById";
/*  650 */     this.logger.entering(CLASS_NAME, "findCsrById");
/*  651 */     Map<String, Object> params = new HashMap<>();
/*  652 */     params.put("state", CertificateState.ISSUED);
/*  653 */     params.put("user", user);
/*  654 */     params.put("entityId", id);
/*  655 */     List<Certificate> csrResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/*  656 */     this.logger.exiting(CLASS_NAME, "findCsrById");
/*  657 */     return !csrResult.isEmpty() ? Optional.<Certificate>of(csrResult.get(0)) : Optional.<Certificate>empty();
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
/*      */   public List<Certificate> findCSRsUnderBackend(User user, String backendSubjectKeyIdentifier) {
/*  670 */     String METHOD_NAME = "findCSRsUnderBackend";
/*  671 */     this.logger.entering(CLASS_NAME, "findCSRsUnderBackend");
/*  672 */     Map<String, Object> params = new HashMap<>();
/*  673 */     params.put("state", CertificateState.SIGNING_REQUEST);
/*  674 */     params.put("user", user);
/*  675 */     params.put("authorityKeyIdentifier", backendSubjectKeyIdentifier);
/*  676 */     List<Certificate> csrs = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/*  677 */     this.logger.exiting(CLASS_NAME, "findCSRsUnderBackend");
/*  678 */     return csrs;
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
/*      */   public List<String> findCsrIdsUnderBackend(User user, CertificateType csrType, String backendSubjectKeyIdentifier, List<String> targetEcu, List<String> targetVin) {
/*  697 */     String METHOD_NAME = "findCsrIdsUnderBackend";
/*  698 */     this.logger.entering(CLASS_NAME, "findCsrIdsUnderBackend");
/*      */     
/*  700 */     List<Certificate> csrs = this.certificateRepository.findCSRsWithTargetVinAndTargetEcu(user, csrType, backendSubjectKeyIdentifier, CertificateUtil.getPattern(targetEcu), 
/*  701 */         CertificateUtil.getPattern(targetVin), Certificate.class);
/*      */     
/*  703 */     this.logger.exiting(CLASS_NAME, "findCsrIdsUnderBackend");
/*  704 */     return (List<String>)csrs.stream()
/*  705 */       .filter(csr -> CertificateUtil.includesTargetECUsAndTargetVINs(csr, targetEcu, targetVin))
/*  706 */       .map(Certificate::getEntityId)
/*  707 */       .collect(Collectors.toList());
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
/*      */   public List<Certificate> findTimeCSRs(User user, String backendSubjectKeyIdentifier, String nonce, String targetVIN, String targetECU) {
/*  728 */     String METHOD_NAME = "findTimeCSRs";
/*  729 */     this.logger.entering(CLASS_NAME, "findTimeCSRs");
/*  730 */     String ecu = (targetECU == null) ? "" : targetECU;
/*  731 */     Map<String, Object> params = new HashMap<>();
/*  732 */     params.put("type", CertificateType.TIME_CERTIFICATE);
/*  733 */     params.put("state", CertificateState.SIGNING_REQUEST);
/*  734 */     params.put("user", user);
/*  735 */     this.logger.exiting(CLASS_NAME, "findTimeCSRs");
/*  736 */     return (List<Certificate>)this.certificateRepository.findWithQuery(Certificate.class, params, 0).stream()
/*  737 */       .filter(c -> (c.getParent().getSubjectKeyIdentifier().equals(backendSubjectKeyIdentifier) && c.getTargetVIN().equals(targetVIN) && c.getTargetECU().equals(ecu) && c.getNonce().equals(nonce)))
/*      */ 
/*      */       
/*  740 */       .collect(Collectors.toList());
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
/*      */   
/*      */   public List<Certificate> findSecOCISCSRs(User user, String diagCertSerialNo, String targetVIN, String targetECU, String targetSubjectKeyIdentifier, String authorityKeyIdentifier) {
/*  763 */     String METHOD_NAME = "findSecOCISCSRs";
/*  764 */     this.logger.entering(CLASS_NAME, "findSecOCISCSRs");
/*  765 */     String serialNo = HexUtil.omitLeadingZeros(diagCertSerialNo);
/*  766 */     Map<String, Object> params = new HashMap<>();
/*  767 */     params.put("type", CertificateType.ENHANCED_RIGHTS_CERTIFICATE);
/*  768 */     params.put("state", CertificateState.SIGNING_REQUEST);
/*  769 */     params.put("authorityKeyIdentifier", authorityKeyIdentifier);
/*  770 */     params.put("targetSubjectKeyIdentifier", targetSubjectKeyIdentifier);
/*  771 */     params.put("user", user);
/*  772 */     this.logger.exiting(CLASS_NAME, "findSecOCISCSRs");
/*  773 */     return (List<Certificate>)this.certificateRepository.findWithQuery(Certificate.class, params, 0).stream()
/*  774 */       .filter(c -> (c.getParent().getSerialNo().trim().equals(serialNo) && ((targetVIN != null && arrayContainsValue(c.getTargetVIN(), targetVIN)) || (targetVIN == null && c.getTargetVIN().isEmpty())) && ((targetECU != null && arrayContainsValue(c.getTargetECU(), targetECU)) || (targetECU == null && c.getTargetECU().isEmpty()))))
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  779 */       .collect(Collectors.toList());
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
/*      */   public List<Certificate> findECUCertificatesByAKIAndECUId(User user, String authorityKeyIdentifier, String ecuId) {
/*  794 */     String METHOD_NAME = "findECUCertificatesByAKIAndECUId";
/*  795 */     this.logger.entering(CLASS_NAME, "findECUCertificatesByAKIAndECUId");
/*  796 */     Map<String, Object> params = new HashMap<>();
/*  797 */     params.put("type", CertificateType.ECU_CERTIFICATE);
/*  798 */     params.put("state", CertificateState.ISSUED);
/*  799 */     params.put("authorityKeyIdentifier", authorityKeyIdentifier);
/*  800 */     params.put("uniqueECUID", "%" + ecuId + "%");
/*  801 */     params.put("user", user);
/*  802 */     this.logger.exiting(CLASS_NAME, "findECUCertificatesByAKIAndECUId");
/*  803 */     return this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Certificate> findECUCertificatesByAKIAndECUIdValid(User user, String authorityKeyIdentifier, String ecuId) {
/*  814 */     String METHOD_NAME = "findECUCertificatesByAKIAndECUIdValid";
/*  815 */     this.logger.entering(CLASS_NAME, "findECUCertificatesByAKIAndECUIdValid");
/*  816 */     List<Certificate> ecus = findECUCertificatesByAKIAndECUId(user, authorityKeyIdentifier, ecuId);
/*  817 */     validateParent(ecus, false);
/*  818 */     this.logger.exiting(CLASS_NAME, "findECUCertificatesByAKIAndECUIdValid");
/*  819 */     return getValidRegularCertificates(ecus);
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
/*      */   public List<Certificate> findValidEnhancedDiagnosticCertificates(User user, String backendSubjectKeyIdentifier, String diagCertSerialNo, String targetVIN, String targetECU) {
/*  841 */     String METHOD_NAME = "findValidEnhancedDiagnosticCertificates";
/*  842 */     this.logger.entering(CLASS_NAME, "findValidEnhancedDiagnosticCertificates");
/*  843 */     String serialNo = HexUtil.omitLeadingZeros(diagCertSerialNo);
/*  844 */     Map<String, Object> params = getEnhancedRightsQueryParams(user, backendSubjectKeyIdentifier);
/*  845 */     List<Certificate> repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/*  846 */     validateParent(repoResult, true);
/*  847 */     List<Certificate> validCertificates = getValidRegularCertificates(repoResult);
/*  848 */     List<Certificate> result = findEnhancedUnderDiag(serialNo, validCertificates);
/*  849 */     this.logger.exiting(CLASS_NAME, "findValidEnhancedDiagnosticCertificates");
/*  850 */     return getFilteredByTargetVINTargetECUCollectAllMataches(result, targetECU, targetVIN);
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
/*      */   public List<Certificate> findEnhancedRightsCertificates(User user, String backendSubjectKeyIdentifier, String diagCertSerialNo) {
/*  862 */     String METHOD_NAME = "findEnhancedDiagnosticCertificates";
/*  863 */     this.logger.entering(CLASS_NAME, "findEnhancedDiagnosticCertificates");
/*      */     
/*  865 */     String serialNo = HexUtil.omitLeadingZeros(diagCertSerialNo);
/*  866 */     Map<String, Object> params = getEnhancedRightsQueryParams(user, backendSubjectKeyIdentifier);
/*  867 */     List<Certificate> repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/*  868 */     List<Certificate> result = findEnhancedUnderDiag(serialNo, repoResult);
/*      */     
/*  870 */     this.logger.exiting(CLASS_NAME, "findEnhancedDiagnosticCertificates");
/*  871 */     return result;
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
/*      */   public List<Certificate> findEnhancedRightsCertificatesWithServices(User user, String backendSubjectKeyIdentifier, String services) {
/*  893 */     String METHOD_NAME = "findEnhancedDiagnosticCertificates";
/*  894 */     this.logger.entering(CLASS_NAME, "findEnhancedDiagnosticCertificates");
/*  895 */     List<Certificate> result = new ArrayList<>();
/*      */     
/*  897 */     Map<String, Object> params = getEnhancedRightsQueryParams(user, backendSubjectKeyIdentifier);
/*  898 */     params.put("services", new EmptyPredicate(false));
/*  899 */     List<Certificate> found = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/*      */     
/*  901 */     List<String> servicesSearched = serviceStringToList(services);
/*      */     
/*  903 */     for (Certificate c : found) {
/*  904 */       String s = c.getServices();
/*  905 */       List<String> servicesFound = serviceStringToList(s);
/*  906 */       if (servicesFound.containsAll(servicesSearched)) {
/*  907 */         result.add(c);
/*  908 */         this.logger.log(Level.INFO, "000535", this.i18n
/*  909 */             .getMessage("enhRightsFoundForService", new String[] { c.getSerialNo(), c
/*  910 */                 .getParent().getSerialNo(), c.getParent().getSubjectKeyIdentifier() }), CLASS_NAME);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  915 */     this.logger.exiting(CLASS_NAME, "findEnhancedDiagnosticCertificates");
/*  916 */     return result;
/*      */   }
/*      */   
/*      */   private List<String> serviceStringToList(String s) {
/*  920 */     return (List<String>)Stream.<String>of(s.split(",")).map(String::trim).map(String::toLowerCase)
/*  921 */       .collect(Collectors.toList());
/*      */   }
/*      */   
/*      */   private Map<String, Object> getEnhancedRightsQueryParams(User user, String backendSubjectKeyIdentifier) {
/*  925 */     Map<String, Object> params = getDefaultQueryParams(user);
/*  926 */     params.put("type", CertificateType.ENHANCED_RIGHTS_CERTIFICATE);
/*  927 */     params.put("authorityKeyIdentifier", backendSubjectKeyIdentifier);
/*  928 */     return params;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Certificate> findEnhancedDiagnosticCertificatesActiveForTest(User user) {
/*  939 */     String METHOD_NAME = "findEnhancedDiagnosticCertificatesActiveForTest";
/*  940 */     this.logger.entering(CLASS_NAME, "findEnhancedDiagnosticCertificatesActiveForTest");
/*  941 */     this.logger.exiting(CLASS_NAME, "findEnhancedDiagnosticCertificatesActiveForTest");
/*  942 */     return (List<Certificate>)this.certificateRepository.findActiveForTesting(user, CertificateType.ENHANCED_RIGHTS_CERTIFICATE).stream()
/*  943 */       .filter(c -> !c.isSecOCISCert()).collect(Collectors.toList());
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
/*      */   public Certificate findCertificateActiveForTesting(User user, CertificateType type) {
/*  956 */     String METHOD_NAME = "findCertificateActiveForTesting";
/*  957 */     this.logger.entering(CLASS_NAME, "findCertificateActiveForTesting");
/*  958 */     List<Certificate> certificates = this.certificateRepository.findActiveForTesting(user, type);
/*  959 */     if (CollectionUtils.isEmpty(certificates)) {
/*  960 */       this.logger.log(Level.INFO, "000444", this.i18n
/*  961 */           .getMessage("certificateNotFoundManualSelection"), CLASS_NAME);
/*  962 */       throw new CertificateNotFoundException(this.i18n.getMessage("certificateNotFoundManualSelection"));
/*      */     } 
/*  964 */     if (certificates.size() > 1) {
/*  965 */       throw new CEBASCertificateException(this.i18n.getMessage("foundMoreCertificatesActiveForTesting", new String[] { type
/*  966 */               .getText() }));
/*      */     }
/*  968 */     this.logger.exiting(CLASS_NAME, "findCertificateActiveForTesting");
/*  969 */     return certificates.get(0);
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
/*      */   public <T extends Certificate> List<T> findCertificateActiveForTestingWithoutValidation(Class<T> theClass, User user, CertificateType type) {
/*  983 */     String METHOD_NAME = "findCertificateActiveForTesting";
/*  984 */     this.logger.entering(CLASS_NAME, "findCertificateActiveForTesting");
/*  985 */     List<T> certificates = this.certificateRepository.findActiveForTesting(theClass, user, type);
/*  986 */     this.logger.exiting(CLASS_NAME, "findCertificateActiveForTesting");
/*  987 */     return certificates;
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
/*      */   public List<Certificate> findCertificatesByType(User user, CertificateType type) {
/* 1000 */     String METHOD_NAME = "findCertificateByType";
/* 1001 */     this.logger.entering(CLASS_NAME, "findCertificateByType");
/* 1002 */     Map<String, Object> params = getDefaultQueryParams(user);
/* 1003 */     params.put("type", type);
/* 1004 */     List<Certificate> certificates = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/* 1005 */     this.logger.exiting(CLASS_NAME, "findCertificateByType");
/* 1006 */     return certificates;
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
/*      */   public <T extends Certificate> List<T> findCertificatesByType(Class<T> theType, User user, CertificateType type) {
/* 1019 */     String METHOD_NAME = "findCertificateByType";
/* 1020 */     this.logger.entering(CLASS_NAME, "findCertificateByType");
/* 1021 */     Map<String, Object> params = getDefaultQueryParams(user);
/* 1022 */     params.put("type", type);
/* 1023 */     List<T> certificates = this.certificateRepository.findWithQuery(theType, params, 0);
/* 1024 */     this.logger.exiting(CLASS_NAME, "findCertificateByType");
/* 1025 */     return certificates;
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
/*      */   public List<Certificate> findSecOCISCertificates(User user, Certificate diagCert, String targetVIN, String targetECU, String subjectKeyIdentifier) {
/* 1046 */     String METHOD_NAME = "findSecOCISCertificates";
/* 1047 */     this.logger.entering(CLASS_NAME, "findSecOCISCertificates");
/* 1048 */     Map<String, Object> params = getDefaultQueryParams(user);
/* 1049 */     params.put("type", CertificateType.ENHANCED_RIGHTS_CERTIFICATE);
/* 1050 */     params.put("issuerSerialNumber", diagCert.getIssuerSerialNumber());
/* 1051 */     params.put("issuer", diagCert.getIssuer());
/* 1052 */     params.put("targetECU", targetECU);
/* 1053 */     List<Certificate> repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/* 1054 */     validateParent(repoResult, true);
/* 1055 */     List<Certificate> validCertificates = getValidRegularCertificates(repoResult);
/*      */ 
/*      */ 
/*      */     
/* 1059 */     List<Certificate> result = (List<Certificate>)validCertificates.stream().filter(c -> (c.isSecOCISCert() && c.getTargetSubjectKeyIdentifier().equals(subjectKeyIdentifier) && c.getParent().getSerialNo().trim().equals(diagCert.getSerialNo()))).collect(Collectors.toList());
/* 1060 */     this.logger.exiting(CLASS_NAME, "findSecOCISCertificates");
/* 1061 */     return getFilteredByTargetVINTargetECU(result, targetECU, targetVIN);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Certificate findSecOCISCertificateActiveForTesting(User user) {
/* 1072 */     String METHOD_NAME = "findSecOCISCertificateActiveForTesting";
/* 1073 */     this.logger.entering(CLASS_NAME, "findSecOCISCertificateActiveForTesting");
/*      */ 
/*      */     
/* 1076 */     List<Certificate> certificates = (List<Certificate>)this.certificateRepository.findActiveForTesting(user, CertificateType.ENHANCED_RIGHTS_CERTIFICATE).stream().filter(Certificate::isSecOCISCert).collect(Collectors.toList());
/* 1077 */     if (certificates.size() > 1) {
/* 1078 */       CEBASCertificateException cebasCertificateException = new CEBASCertificateException(this.i18n.getMessage("foundMoreSecOcisActiveForTesting"));
/* 1079 */       this.logger.logWithExceptionByInfo("000478X", (CEBASException)cebasCertificateException);
/* 1080 */       throw cebasCertificateException;
/*      */     } 
/*      */     
/* 1083 */     if (certificates.isEmpty()) {
/*      */       
/* 1085 */       CertificateNotFoundException exc = new CertificateNotFoundException(this.i18n.getMessage("certificateNotFound"));
/* 1086 */       this.logger.logWithExceptionByInfo("000479X", (CEBASException)exc);
/* 1087 */       throw exc;
/*      */     } 
/* 1089 */     this.logger.exiting(CLASS_NAME, "findSecOCISCertificateActiveForTesting");
/* 1090 */     return certificates.get(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Certificate> findAllBackends(User user) {
/* 1101 */     String METHOD_NAME = "findAllBackends";
/* 1102 */     this.logger.entering(CLASS_NAME, "findAllBackends");
/* 1103 */     Map<String, Object> params = getDefaultQueryParams(user);
/* 1104 */     params.put("type", CertificateType.BACKEND_CA_CERTIFICATE);
/* 1105 */     List<Certificate> certificates = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/* 1106 */     this.logger.exiting(CLASS_NAME, "findAllBackends");
/* 1107 */     return certificates;
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
/*      */   public List<Certificate> findSecureVariantCodingUserCertificates(User user, String backendCertSkid, String targetVIN, String targetECU) {
/* 1126 */     String METHOD_NAME = "findSecureVariantCodingUserCertificates";
/* 1127 */     this.logger.entering(CLASS_NAME, "findSecureVariantCodingUserCertificates");
/* 1128 */     Map<String, Object> params = getDefaultQueryParams(user);
/* 1129 */     params.put("type", CertificateType.VARIANT_CODE_USER_CERTIFICATE);
/* 1130 */     params.put("authorityKeyIdentifier", backendCertSkid);
/* 1131 */     List<Certificate> repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/* 1132 */     validateParent(repoResult, false);
/* 1133 */     List<Certificate> validCertificates = getValidRegularCertificates(repoResult);
/* 1134 */     validCertificates = getOnesWithValidPublicKeyAndPrivateKey(validCertificates);
/* 1135 */     this.logger.exiting(CLASS_NAME, "findSecureVariantCodingUserCertificates");
/* 1136 */     return getFilteredByTargetVINTargetECU(validCertificates, targetECU, targetVIN);
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
/*      */ 
/*      */   
/*      */   public <T extends Certificate> List<T> findCertificatesWithTargetVinAndTargetEcu(User user, CertificateType certificateType, String backendCertSkid, List<String> targetECUs, List<String> targetVINs, String minRemainingValidity, Class<T> type) {
/* 1160 */     String METHOD_NAME = "findCertificatesWithTargetVinAndTargetEcu";
/* 1161 */     this.logger.entering(CLASS_NAME, "findCertificatesWithTargetVinAndTargetEcu");
/*      */     
/* 1163 */     List<T> result = this.certificateRepository.findCertificatesWithTargetVinAndTargetEcu(user, certificateType, backendCertSkid, 
/* 1164 */         CertificateUtil.getPattern(targetECUs), CertificateUtil.getPattern(targetVINs), type);
/*      */ 
/*      */ 
/*      */     
/* 1168 */     List<T> validCertificates = (List<T>)result.stream().filter(cert -> (CertificatesValidator.exceedMinRemainingValidity(cert, minRemainingValidity) && CertificateUtil.includesTargetECUsAndTargetVINs(cert, targetECUs, targetVINs))).collect(Collectors.toList());
/*      */     
/* 1170 */     this.logger.exiting(CLASS_NAME, "findCertificatesWithTargetVinAndTargetEcu");
/* 1171 */     return validCertificates;
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
/*      */   public <T extends Certificate> Optional<T> findSecureVariantCodingUserCertificate(User user, String backendCertSkid, List<String> targetECUs, List<String> targetVINs, Class<T> type) {
/* 1190 */     String METHOD_NAME = "findSecureVariantCodingUserCertificate";
/* 1191 */     this.logger.entering(CLASS_NAME, "findSecureVariantCodingUserCertificate");
/*      */     
/* 1193 */     List<T> result = this.certificateRepository.findCertificatesWithTargetVinAndTargetEcu(user, CertificateType.VARIANT_CODE_USER_CERTIFICATE, backendCertSkid, 
/* 1194 */         CertificateUtil.getPattern(targetECUs), CertificateUtil.getPattern(targetVINs), type);
/*      */ 
/*      */     
/* 1197 */     List<T> validCertificates = (List<T>)result.stream().filter(cert -> (!CertificatesValidator.isExpired(cert, this.logger, this.i18n) && CertificateUtil.includesTargetECUsAndTargetVINs(cert, targetECUs, targetVINs))).collect(Collectors.toList());
/*      */     
/* 1199 */     this.logger.exiting(CLASS_NAME, "findSecureVariantCodingUserCertificate");
/* 1200 */     return !validCertificates.isEmpty() ? 
/* 1201 */       Optional.<T>of(getCertificatesBasedOnValidityPeriod(validCertificates).get(0)) : 
/* 1202 */       Optional.<T>empty();
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
/*      */   public Optional<Certificate> findSecVarCodingWithoutTargetVinAndTargetEcu(User user, String backendCertSkid) {
/* 1215 */     String METHOD_NAME = "findSecVarCodingWithoutTargetVinAndTargetEcu";
/* 1216 */     this.logger.entering(CLASS_NAME, "findSecVarCodingWithoutTargetVinAndTargetEcu");
/*      */     
/* 1218 */     Map<String, Object> params = getDefaultQueryParams(user);
/* 1219 */     params.put("type", CertificateType.VARIANT_CODE_USER_CERTIFICATE);
/* 1220 */     params.put("authorityKeyIdentifier", backendCertSkid);
/* 1221 */     List<Certificate> result = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/*      */ 
/*      */ 
/*      */     
/* 1225 */     List<Certificate> validCertificates = (List<Certificate>)result.stream().filter(cert -> (!CertificatesValidator.isExpired(cert, this.logger, this.i18n) && !hasTargetVinOrTargetEcu(cert))).collect(Collectors.toList());
/*      */     
/* 1227 */     this.logger.exiting(CLASS_NAME, "findSecVarCodingWithoutTargetVinAndTargetEcu");
/* 1228 */     return !validCertificates.isEmpty() ? 
/* 1229 */       Optional.<Certificate>of(getCertificatesBasedOnValidityPeriod(validCertificates).get(0)) : 
/* 1230 */       Optional.<Certificate>empty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasTargetVinOrTargetEcu(Certificate certificate) {
/* 1241 */     return (StringUtils.isNotEmpty(certificate.getTargetVIN()) || StringUtils.isNotEmpty(certificate.getTargetECU()));
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
/*      */   public List<Map.Entry<String, List<Certificate>>> getCertificatesGroupsBasedOnRolesPriority(List<Certificate> certificates, Configuration rolesConfiguration) {
/*      */     List<Certificate> validRolesCertificates;
/* 1255 */     List<String> rolesPriority = Arrays.asList(rolesConfiguration.getConfigValue().split(","));
/*      */     
/* 1257 */     if (isExtendedValidation()) {
/*      */       
/* 1259 */       validRolesCertificates = (List<Certificate>)certificates.stream().filter(cert -> !cert.getUserRole().equals(UserRole.NO_ROLE.getText())).collect(Collectors.toList());
/*      */     } else {
/* 1261 */       validRolesCertificates = certificates;
/*      */     } 
/*      */ 
/*      */     
/* 1265 */     Map<String, List<Certificate>> certsGroupedByUserRoles = (Map<String, List<Certificate>>)validRolesCertificates.stream().collect(Collectors.groupingBy(Certificate::getUserRole, Collectors.toList()));
/* 1266 */     return (List<Map.Entry<String, List<Certificate>>>)certsGroupedByUserRoles.entrySet().stream()
/* 1267 */       .sorted((e1, e2) -> rolesPriority.indexOf(e1.getKey()) - rolesPriority.indexOf(e2.getKey()))
/* 1268 */       .collect(Collectors.toList());
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
/*      */   public Certificate getCertificateByValidityAndSignatureAscending(List<Certificate> certificates) {
/* 1281 */     return getCertificateByValidityAndSignature(certificates, this::getCertificatesBasedOnValidityPeriod);
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
/*      */   public Certificate getCertificateByNewestValidFromAndSignatureAscending(List<Certificate> certificates) {
/* 1293 */     return getCertificateByValidityAndSignature(certificates, this::getCertificatesBasedOnNewestValidFrom);
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
/*      */   public UserKeyPair getUserKeyPairForCertificate(User user, Certificate certificate) {
/* 1306 */     String METHOD_NAME = "getUserKeyPairForCertificate";
/* 1307 */     this.logger.entering(CLASS_NAME, "getUserKeyPairForCertificate");
/* 1308 */     this.logger.exiting(CLASS_NAME, "getUserKeyPairForCertificate");
/* 1309 */     Map<String, Object> params = new HashMap<>();
/* 1310 */     params.put("user", user);
/* 1311 */     params.put("certificate", certificate);
/* 1312 */     List<UserKeyPair> userKeyPairs = this.certificateRepository.findWithQuery(UserKeyPair.class, params, 0);
/* 1313 */     if (userKeyPairs.size() != 1) {
/* 1314 */       throw new CertificateSigningException(this.i18n.getMessage("noKeyPairFoundForCert", new String[] { certificate
/* 1315 */               .getEntityId() }));
/*      */     }
/* 1317 */     return userKeyPairs.get(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasPrivateKey(Certificate certificate) {
/*      */     try {
/* 1328 */       UserKeyPair userKeyPair = getUserKeyPairForCertificate(this.session.getCurrentUser(), certificate);
/* 1329 */       if (StringUtils.isEmpty(userKeyPair.getPrivateKey())) {
/* 1330 */         return false;
/*      */       }
/* 1332 */     } catch (CertificateSigningException e) {
/* 1333 */       return false;
/*      */     } 
/* 1335 */     return true;
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
/*      */   public Optional<Certificate> findCertByUserAndSignature(User user, String signature) {
/* 1348 */     String METHOD_NAME = "findCertByUserAndSignature";
/* 1349 */     this.logger.entering(CLASS_NAME, "findCertByUserAndSignature");
/* 1350 */     Map<String, Object> params = getDefaultQueryParams(user);
/* 1351 */     params.put("signature", signature.trim());
/* 1352 */     List<Certificate> certificates = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/* 1353 */     this.logger.exiting(CLASS_NAME, "findCertByUserAndSignature");
/* 1354 */     return certificates.stream().findAny();
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
/*      */   public <T extends Certificate> List<T> findCertificatesByUser(Class<T> type, User user, boolean all, Pageable pageable) {
/* 1369 */     String METHOD_NAME = "findCertificatesByUser";
/* 1370 */     this.logger.entering(CLASS_NAME, "findCertificatesByUser");
/* 1371 */     Map<String, Object> params = new HashMap<>();
/* 1372 */     params.put("user", user);
/* 1373 */     if (!all) {
/* 1374 */       params.put("state", CertificateState.ISSUED);
/*      */     }
/* 1376 */     List<T> certificates = this.certificateRepository.findWithPageableQuery(type, params, 0, pageable);
/* 1377 */     this.logger.exiting(CLASS_NAME, "findCertificatesByUser");
/* 1378 */     return certificates;
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
/*      */   public List<Certificate> getFilteredByTargetVINTargetECU(List<Certificate> certificates, String ecu, String vin) {
/* 1394 */     String METHOD_NAME = "getFilteredByTargetVINTargetECU";
/* 1395 */     this.logger.entering(CLASS_NAME, "getFilteredByTargetVINTargetECU");
/* 1396 */     String targetECU = (ecu == null) ? "" : ecu;
/* 1397 */     String targetVIN = (vin == null) ? "" : vin;
/*      */     
/* 1399 */     if (targetECU.isEmpty() && targetVIN.isEmpty())
/*      */     {
/* 1401 */       return getCertificatesForTargetECUTargetVINWildcards(certificates);
/*      */     }
/* 1403 */     if (targetECU.isEmpty() && !targetVIN.isEmpty())
/*      */     {
/* 1405 */       return getCertificatesForTargetECUWildcard(certificates, targetVIN);
/*      */     }
/* 1407 */     if (!targetECU.isEmpty() && targetVIN.isEmpty())
/*      */     {
/* 1409 */       return getCertificatesForTargetVINWildcard(certificates, targetECU);
/*      */     }
/* 1411 */     if (!targetECU.isEmpty() && !targetVIN.isEmpty())
/*      */     {
/* 1413 */       return getCertificatesForTargetECUAndTargetVIN(certificates, targetECU, targetVIN);
/*      */     }
/* 1415 */     this.logger.exiting(CLASS_NAME, "getFilteredByTargetVINTargetECU");
/* 1416 */     return new ArrayList<>();
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
/*      */   public List<Certificate> getFilteredByTargetVINTargetECUCollectAllMataches(List<Certificate> certificates, String ecu, String vin) {
/* 1433 */     String METHOD_NAME = "getFilteredByTargetVINTargetECU";
/* 1434 */     this.logger.entering(CLASS_NAME, "getFilteredByTargetVINTargetECU");
/* 1435 */     String targetECU = (ecu == null) ? "" : ecu;
/* 1436 */     String targetVIN = (vin == null) ? "" : vin;
/* 1437 */     List<Certificate> result = new ArrayList<>();
/* 1438 */     if (targetECU.isEmpty() && targetVIN.isEmpty())
/*      */     {
/* 1440 */       result.addAll(getCertificatesForTargetECUTargetVINWildcards(certificates));
/*      */     }
/* 1442 */     if (targetECU.isEmpty() && !targetVIN.isEmpty()) {
/*      */ 
/*      */       
/* 1445 */       result.addAll(getMatch(certificates, c -> (c.getTargetECU().isEmpty() && c.getTargetVIN().isEmpty())));
/*      */       
/* 1447 */       result.addAll(getMatch(certificates, c -> 
/* 1448 */             (c.getTargetECU().isEmpty() && arrayContainsValue(c.getTargetVIN(), targetVIN))));
/*      */     } 
/* 1450 */     if (!targetECU.isEmpty() && targetVIN.isEmpty()) {
/*      */ 
/*      */       
/* 1453 */       result.addAll(getMatch(certificates, c -> (c.getTargetECU().isEmpty() && c.getTargetVIN().isEmpty())));
/*      */       
/* 1455 */       result.addAll(getMatch(certificates, c -> 
/* 1456 */             (c.getTargetVIN().isEmpty() && arrayContainsValue(c.getTargetECU(), targetECU))));
/*      */     } 
/* 1458 */     if (!targetECU.isEmpty() && !targetVIN.isEmpty()) {
/*      */ 
/*      */       
/* 1461 */       result.addAll(getMatch(certificates, c -> (c.getTargetECU().isEmpty() && c.getTargetVIN().isEmpty())));
/*      */ 
/*      */       
/* 1464 */       result.addAll(getMatch(certificates, c -> 
/* 1465 */             (c.getTargetECU().isEmpty() && arrayContainsValue(c.getTargetVIN(), targetVIN))));
/*      */ 
/*      */       
/* 1468 */       result.addAll(getMatch(certificates, c -> 
/* 1469 */             (c.getTargetVIN().isEmpty() && arrayContainsValue(c.getTargetECU(), targetECU))));
/*      */ 
/*      */       
/* 1472 */       result.addAll(getMatch(certificates, c -> (arrayContainsValue(c.getTargetECU(), targetECU) && arrayContainsValue(c.getTargetVIN(), targetVIN))));
/*      */     } 
/*      */     
/* 1475 */     this.logger.exiting(CLASS_NAME, "getFilteredByTargetVINTargetECU");
/* 1476 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Certificate> List<T> getCertificatesBasedOnValidityPeriod(List<T> certificates) {
/* 1487 */     String METHOD_NAME = "getCertificatesBasedOnValidityPeriod";
/* 1488 */     this.logger.entering(CLASS_NAME, "getCertificatesBasedOnValidityPeriod");
/* 1489 */     this.logger.exiting(CLASS_NAME, "getCertificatesBasedOnValidityPeriod");
/* 1490 */     return getCertificatesBasedOnValidity(certificates, Certificate::getValidTo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Certificate> List<T> getCertificatesBasedOnNewestValidFrom(List<T> certificates) {
/* 1501 */     String METHOD_NAME = "getCertificatesBasedOnNewestValidFrom";
/* 1502 */     this.logger.entering(CLASS_NAME, "getCertificatesBasedOnNewestValidFrom");
/* 1503 */     this.logger.exiting(CLASS_NAME, "getCertificatesBasedOnNewestValidFrom");
/* 1504 */     return getCertificatesBasedOnValidity(certificates, Certificate::getValidFrom);
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
/*      */   public Optional<Certificate> findCertificate(User user, String subjectKeyIdentifier) {
/* 1517 */     return findCertificate(user, subjectKeyIdentifier, CertificateType.NO_TYPE);
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
/*      */   public Optional<Certificate> findCertificate(User user, String subjectKeyIdentifier, CertificateType certificateType) {
/* 1533 */     return findCertificate(user, subjectKeyIdentifier, certificateType, Certificate.class);
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
/*      */   public <T extends Certificate> Optional<T> findCertificate(User user, String subjectKeyIdentifier, CertificateType certificateType, Class<T> theClass) {
/* 1552 */     String METHOD_NAME = "findCertificate";
/* 1553 */     this.logger.entering(CLASS_NAME, "findCertificate");
/* 1554 */     Map<String, Object> params = getDefaultQueryParams(user);
/* 1555 */     params.put("subjectKeyIdentifier", subjectKeyIdentifier);
/* 1556 */     if (!certificateType.equals(CertificateType.NO_TYPE)) {
/* 1557 */       params.put("type", certificateType);
/*      */     }
/* 1559 */     List<T> certificates = this.certificateRepository.findWithQuery(theClass, params, 0);
/* 1560 */     this.logger.exiting(CLASS_NAME, "findCertificate");
/* 1561 */     return certificates.stream().findAny();
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
/*      */   public Optional<Certificate> findValidCertificate(User user, String subjectKeyIdentifier, CertificateType certificateType) {
/* 1577 */     String METHOD_NAME = "findCertificate";
/* 1578 */     this.logger.entering(CLASS_NAME, "findCertificate");
/* 1579 */     Map<String, Object> params = getDefaultQueryParams(user);
/* 1580 */     params.put("subjectKeyIdentifier", subjectKeyIdentifier);
/* 1581 */     if (!certificateType.equals(CertificateType.NO_TYPE)) {
/* 1582 */       params.put("type", certificateType);
/*      */     }
/* 1584 */     List<Certificate> repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/* 1585 */     boolean validatePublicKeyOfParent = (certificateType == CertificateType.ENHANCED_RIGHTS_CERTIFICATE);
/* 1586 */     validateParent(repoResult, validatePublicKeyOfParent);
/* 1587 */     List<Certificate> validCertificates = getValidRegularCertificates(repoResult);
/* 1588 */     if (isUnderBackend(certificateType)) {
/* 1589 */       validCertificates = getOnesWithValidPublicKeyAndPrivateKey(validCertificates);
/*      */     }
/* 1591 */     this.logger.exiting(CLASS_NAME, "findCertificate");
/* 1592 */     return validCertificates.stream().findAny();
/*      */   }
/*      */   
/*      */   public boolean isUnderBackend(CertificateType certificateType) {
/* 1596 */     return (certificateType == CertificateType.VARIANT_CODING_DEVICE_CERTIFICATE || certificateType == CertificateType.TIME_CERTIFICATE || certificateType == CertificateType.VARIANT_CODE_USER_CERTIFICATE || certificateType == CertificateType.ECU_CERTIFICATE || certificateType == CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE);
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
/*      */   public <T extends Certificate> List<T> findCertificatesUnderParent(User user, Certificate parentCertificate, Class<T> type) {
/* 1615 */     String METHOD_NAME = "findCertificatesUnderParent";
/* 1616 */     this.logger.entering(CLASS_NAME, "findCertificatesUnderParent");
/*      */     
/* 1618 */     Map<String, Object> params = new HashMap<>();
/* 1619 */     params.put("user", user);
/* 1620 */     params.put("state", CertificateState.ISSUED);
/* 1621 */     params.put("authorityKeyIdentifier", parentCertificate.getSubjectKeyIdentifier());
/* 1622 */     params.put("parent", parentCertificate);
/*      */     
/* 1624 */     List<T> certificates = this.certificateRepository.findWithQuery(type, params, 0);
/*      */     
/* 1626 */     this.logger.exiting(CLASS_NAME, "findCertificatesUnderParent");
/* 1627 */     return certificates;
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
/*      */   public <T extends Certificate> List<T> findCertificatesUnderParentByType(User user, String parentSubjectKeyIdentifier, CertificateType certificateType, Class<T> type) {
/* 1645 */     String METHOD_NAME = "findCertificatesUnderParentByType";
/* 1646 */     this.logger.entering(CLASS_NAME, "findCertificatesUnderParentByType");
/* 1647 */     Map<String, Object> queryParams = getFindUnderParentQueryMap(user, parentSubjectKeyIdentifier, certificateType);
/* 1648 */     List<T> certificates = this.certificateRepository.findWithQuery(type, queryParams, -1);
/* 1649 */     this.logger.exiting(CLASS_NAME, "findCertificatesUnderParentByType");
/* 1650 */     return certificates;
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
/*      */   public <T extends Certificate> List<T> findVsmOrNonVsmCertificatesUnderParent(User user, String parentSKI, boolean isVSM, Class<T> type) {
/* 1667 */     String METHOD_NAME = "findVsmOrNonVsmCertificatesUnderParent";
/* 1668 */     this.logger.entering(CLASS_NAME, "findVsmOrNonVsmCertificatesUnderParent");
/* 1669 */     Map<String, Object> queryParams = getFindUnderParentQueryMap(user, parentSKI, CertificateType.ECU_CERTIFICATE);
/* 1670 */     queryParams.put("specialECU", isVSM ? "1" : "");
/* 1671 */     List<T> certificates = this.certificateRepository.findWithQuery(type, queryParams, -1);
/* 1672 */     this.logger.exiting(CLASS_NAME, "findVsmOrNonVsmCertificatesUnderParent");
/* 1673 */     return certificates;
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
/*      */   public <T extends Certificate> List<T> findCertificatesUnderParentByTypeAndSubject(User user, String parentSubjectKeyIdentifier, CertificateType certificateType, String subject, Class<T> type) {
/* 1688 */     String METHOD_NAME = "findCertificatesUnderParentByTypeAndSubject";
/* 1689 */     this.logger.entering(CLASS_NAME, "findCertificatesUnderParentByTypeAndSubject");
/* 1690 */     Map<String, Object> queryParams = getFindUnderParentQueryMap(user, parentSubjectKeyIdentifier, certificateType);
/* 1691 */     queryParams.put("subject", subject);
/* 1692 */     List<T> certificates = this.certificateRepository.findWithQuery(type, queryParams, -1);
/* 1693 */     this.logger.exiting(CLASS_NAME, "findCertificatesUnderParentByTypeAndSubject");
/* 1694 */     return certificates;
/*      */   }
/*      */   
/*      */   private Map<String, Object> getFindUnderParentQueryMap(User user, String parentSubjectKeyIdentifier, CertificateType certificateType) {
/* 1698 */     Map<String, Object> queryParams = new HashMap<>();
/* 1699 */     queryParams.put("user", user);
/* 1700 */     queryParams.put("type", certificateType);
/* 1701 */     queryParams.put("authorityKeyIdentifier", parentSubjectKeyIdentifier);
/* 1702 */     queryParams.put("state", CertificateState.ISSUED);
/* 1703 */     return queryParams;
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
/*      */   public List<Certificate> findValidCertificatesUnderParentByTypeAndPublicKey(User user, String parentSubjectKeyIdentifier, CertificateType certificateType, String publicKey) {
/* 1721 */     String METHOD_NAME = "findCertificatesUnderParentByType";
/* 1722 */     this.logger.entering(CLASS_NAME, "findCertificatesUnderParentByType");
/*      */     
/* 1724 */     Map<String, Object> queryParams = getFindUnderParentQueryMap(user, parentSubjectKeyIdentifier, certificateType);
/* 1725 */     if (StringUtils.isNotEmpty(publicKey)) {
/* 1726 */       queryParams.put("subjectPublicKey", publicKey);
/*      */     }
/*      */     
/* 1729 */     List<Certificate> repoResult = this.certificateRepository.findWithQuery(Certificate.class, queryParams, -1);
/* 1730 */     boolean validatePublicKeyOfParent = (certificateType == CertificateType.ENHANCED_RIGHTS_CERTIFICATE);
/* 1731 */     validateParent(repoResult, validatePublicKeyOfParent);
/* 1732 */     this.logger.exiting(CLASS_NAME, "findCertificatesUnderParentByType");
/* 1733 */     List<Certificate> validCertificates = getValidRegularCertificates(repoResult);
/* 1734 */     if (isUnderBackend(certificateType)) {
/* 1735 */       validCertificates = getOnesWithValidPublicKeyAndPrivateKey(validCertificates);
/*      */     }
/*      */     
/* 1738 */     return validCertificates;
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
/*      */   public <T extends Certificate> List<T> searchCertificates(Class<T> clazz, CertificateType type) {
/* 1753 */     Map<String, Object> params = new HashMap<>();
/* 1754 */     params.put("state", CertificateState.ISSUED);
/* 1755 */     params.put("type", type);
/* 1756 */     return this.certificateRepository.findWithQuery(clazz, params, 0);
/*      */   }
/*      */   
/*      */   public Optional<Certificate> searchCertificateById(String id, User user) {
/* 1760 */     Map<String, Object> params = new HashMap<>();
/* 1761 */     params.put("state", CertificateState.ISSUED);
/* 1762 */     params.put("user", user);
/* 1763 */     params.put("entityId", id);
/* 1764 */     List<Certificate> result = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/* 1765 */     if (result.isEmpty()) {
/* 1766 */       return Optional.empty();
/*      */     }
/* 1768 */     return Optional.of(result.get(0));
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
/*      */   public Optional<Certificate> findCertificateBySkiAkiAndType(User user, String subjectKeyIdentifier, String authorityKeyIdentifier, CertificateType type) {
/* 1785 */     String METHOD_NAME = "findCertificateBySkiAkiAndType";
/* 1786 */     this.logger.entering(CLASS_NAME, "findCertificateBySkiAkiAndType");
/*      */     
/* 1788 */     Map<String, Object> params = getFindBySkiAkiTypeQueryMap(user, subjectKeyIdentifier, authorityKeyIdentifier, type);
/* 1789 */     List<Certificate> certificates = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/*      */     
/* 1791 */     this.logger.exiting(CLASS_NAME, "findCertificateBySkiAkiAndType");
/* 1792 */     return certificates.stream().findAny();
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
/*      */   public <T extends Certificate> Optional<T> findPKIKnownCertificateBySkiAkiAndType(User user, String subjectKeyIdentifier, String authorityKeyIdentifier, CertificateType type, Class<T> clazz) {
/* 1812 */     String METHOD_NAME = "findPKIKnownCertificateBySkiAkiAndType";
/* 1813 */     this.logger.entering(CLASS_NAME, "findPKIKnownCertificateBySkiAkiAndType");
/*      */     
/* 1815 */     Map<String, Object> params = getFindBySkiAkiTypeQueryMap(user, subjectKeyIdentifier, authorityKeyIdentifier, type);
/* 1816 */     params.put("pkiKnown", Boolean.TRUE);
/* 1817 */     List<T> certificates = this.certificateRepository.findWithQuery(clazz, params, 0);
/*      */     
/* 1819 */     this.logger.exiting(CLASS_NAME, "findPKIKnownCertificateBySkiAkiAndType");
/* 1820 */     return certificates.stream().findAny();
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
/*      */   public <T extends Certificate> Optional<T> findPKIKnownBackendBySki(User user, String subjectKeyIdentifier, Class<T> clazz) {
/* 1835 */     String METHOD_NAME = "findPKIKnownBackendBySki";
/* 1836 */     this.logger.entering(CLASS_NAME, "findPKIKnownBackendBySki");
/*      */     
/* 1838 */     Map<String, Object> params = getDefaultQueryParams(user);
/* 1839 */     params.put("subjectKeyIdentifier", subjectKeyIdentifier);
/* 1840 */     params.put("type", CertificateType.BACKEND_CA_CERTIFICATE);
/* 1841 */     params.put("pkiKnown", Boolean.TRUE);
/* 1842 */     List<T> certificates = this.certificateRepository.findWithQuery(clazz, params, 0);
/*      */     
/* 1844 */     this.logger.exiting(CLASS_NAME, "findPKIKnownBackendBySki");
/* 1845 */     return certificates.stream().findAny();
/*      */   }
/*      */ 
/*      */   
/*      */   private Map<String, Object> getFindBySkiAkiTypeQueryMap(User user, String subjectKeyIdentifier, String authorityKeyIdentifier, CertificateType type) {
/* 1850 */     Map<String, Object> params = getDefaultQueryParams(user);
/* 1851 */     params.put("subjectKeyIdentifier", subjectKeyIdentifier);
/* 1852 */     params.put("authorityKeyIdentifier", authorityKeyIdentifier);
/* 1853 */     params.put("type", type);
/* 1854 */     return params;
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
/*      */   public Optional<Certificate> findCertificateBySkiAndType(User user, String subjectKeyIdentifier, CertificateType type) {
/* 1870 */     String METHOD_NAME = "findCertificateBySkiAndType";
/* 1871 */     this.logger.entering(CLASS_NAME, "findCertificateBySkiAndType");
/* 1872 */     Map<String, Object> params = getFindSkiTypeQueryMap(user, subjectKeyIdentifier, type);
/* 1873 */     List<Certificate> certificates = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/* 1874 */     this.logger.exiting(CLASS_NAME, "findCertificateBySkiAndType");
/* 1875 */     return certificates.stream().findAny();
/*      */   }
/*      */   
/*      */   private Map<String, Object> getFindSkiTypeQueryMap(User user, String subjectKeyIdentifier, CertificateType type) {
/* 1879 */     Map<String, Object> params = getDefaultQueryParams(user);
/* 1880 */     params.put("subjectKeyIdentifier", subjectKeyIdentifier);
/* 1881 */     params.put("type", type);
/* 1882 */     return params;
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
/*      */   public <T extends Certificate> List<T> findCertificatesBySkiAndType(User user, String subjectKeyIdentifier, CertificateType type, Class<T> clazz) {
/* 1898 */     String METHOD_NAME = "findCertificateBySkiAndType";
/* 1899 */     this.logger.entering(CLASS_NAME, "findCertificateBySkiAndType");
/* 1900 */     Map<String, Object> params = getFindSkiTypeQueryMap(user, subjectKeyIdentifier, type);
/* 1901 */     List<T> certificates = this.certificateRepository.findWithQuery(clazz, params, 0);
/* 1902 */     this.logger.exiting(CLASS_NAME, "findCertificateBySkiAndType");
/* 1903 */     return certificates;
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
/*      */   public List<Certificate> findCSRsUnderDiag(User user, String diagAki) {
/* 1916 */     String METHOD_NAME = "findCSRsUnderDiag";
/* 1917 */     this.logger.entering(CLASS_NAME, "findCSRsUnderDiag");
/* 1918 */     Map<String, Object> params = new HashMap<>();
/* 1919 */     params.put("user", user);
/* 1920 */     params.put("state", CertificateState.SIGNING_REQUEST);
/* 1921 */     params.put("authorityKeyIdentifier", diagAki);
/* 1922 */     params.put("type", CertificateType.ENHANCED_RIGHTS_CERTIFICATE);
/* 1923 */     List<Certificate> certificates = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
/* 1924 */     this.logger.exiting(CLASS_NAME, "findCSRsUnderDiag");
/* 1925 */     return certificates;
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
/*      */   public Optional<Certificate> findDiagCertForCentralAuthentication(List<String> excludedRolesForCentralAuth, User user, String backendSubjectKeyIdentifier, String targetVIN) {
/* 1945 */     String METHOD_NAME = "findDiagCertForCentralAuthentication";
/* 1946 */     this.logger.entering(CLASS_NAME, "findDiagCertForCentralAuthentication");
/*      */     
/* 1948 */     List<Certificate> repoResult = this.certificateRepository.findDiagnosticCertificateForCentralAuthentication(user, backendSubjectKeyIdentifier, excludedRolesForCentralAuth);
/*      */     
/* 1950 */     validateParent(repoResult, false);
/* 1951 */     List<Certificate> validCertificates = getValidRegularCertificates(repoResult);
/* 1952 */     validCertificates = getOnesWithValidPublicKeyAndPrivateKey(validCertificates);
/* 1953 */     List<Certificate> result = determineResultBasedOnRolePriorityAndTargetVIN(user, targetVIN, null, validCertificates);
/* 1954 */     Certificate certificate = getCertificateByValidityAndSignatureAscending(result);
/* 1955 */     this.logger.exiting(CLASS_NAME, "findDiagCertForCentralAuthentication");
/* 1956 */     return Optional.ofNullable(certificate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Certificate findOldestCertificate(List<Certificate> certs) {
/* 1967 */     if (certs == null || certs.isEmpty())
/* 1968 */       throw new CEBASException("Empty or null list cannot be evaluated"); 
/* 1969 */     return Collections.<Certificate>min(certs, Comparator.comparing(Certificate::getValidFrom));
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
/*      */   private void validateParent(List<? extends Certificate> certificates, boolean validatePrivateKey) {
/* 1981 */     if (!isExtendedValidation()) {
/*      */       return;
/*      */     }
/* 1984 */     if (certificates == null || certificates.isEmpty() || ((Certificate)certificates
/* 1985 */       .get(0)).getType() == CertificateType.ROOT_CA_CERTIFICATE) {
/*      */       return;
/*      */     }
/* 1988 */     Certificate parentCertificate = ((Certificate)certificates.get(0)).getParent();
/* 1989 */     if (!CertificatesValidator.isValidInChain(parentCertificate, this.i18n, this.logger)) {
/*      */       
/* 1991 */       CertificateNotFoundException certificateNotFoundException = new CertificateNotFoundException(this.i18n.getMessage("parentDoesNotPassExtendedValidation"));
/* 1992 */       this.logger.logWithExceptionByInfo("000480X", (CEBASException)certificateNotFoundException);
/* 1993 */       throw certificateNotFoundException;
/*      */     } 
/*      */     
/* 1996 */     if (validatePrivateKey && !CertificatesValidator.checkPublicKeyMatchesTheOneGeneratedFromPrivateKey(this.session, parentCertificate, this.logger)) {
/*      */ 
/*      */       
/* 1999 */       CertificateNotFoundException certificateNotFoundException = new CertificateNotFoundException(this.i18n.getMessage("parentDoesNotPassExtendedValidation"));
/* 2000 */       this.logger.logWithExceptionByInfo("000481X", (CEBASException)certificateNotFoundException);
/* 2001 */       throw certificateNotFoundException;
/*      */     } 
/*      */   }
/*      */   
/*      */   public List<Certificate> findIdentical(Certificate cert) {
/* 2006 */     return this.certificateRepository.getIdentical(cert);
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
/*      */   protected List<Map.Entry<String, List<Certificate>>> getCertificatesGroupBaseOnRolesPriority(User user, List<Certificate> certificates) {
/* 2021 */     Configuration rolesConfiguration = ConfigurationUtil.getConfigurationForUser(user, CEBASProperty.USER_ROLE, this.logger, this.i18n);
/*      */     
/* 2023 */     return getCertificatesGroupsBasedOnRolesPriority(certificates, rolesConfiguration);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<Certificate> getCertificatesForTargetECUTargetVINWildcards(List<Certificate> certificates) {
/* 2034 */     return getMatch(certificates, c -> (c.getTargetECU().isEmpty() && c.getTargetVIN().isEmpty()));
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
/*      */   private List<Certificate> getCertificatesForTargetECUWildcard(List<Certificate> certificates, String targetVIN) {
/* 2049 */     List<Certificate> wildcards = getMatch(certificates, c -> 
/* 2050 */         (c.getTargetECU().isEmpty() && c.getTargetVIN().isEmpty()));
/* 2051 */     if (!wildcards.isEmpty()) {
/* 2052 */       return wildcards;
/*      */     }
/*      */     
/* 2055 */     return getMatch(certificates, c -> 
/* 2056 */         (c.getTargetECU().isEmpty() && arrayContainsValue(c.getTargetVIN(), targetVIN)));
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
/*      */   private List<Certificate> getCertificatesForTargetVINWildcard(List<Certificate> certificates, String targetECU) {
/* 2072 */     List<Certificate> wildcards = getMatch(certificates, c -> 
/* 2073 */         (c.getTargetECU().isEmpty() && c.getTargetVIN().isEmpty()));
/* 2074 */     if (!wildcards.isEmpty()) {
/* 2075 */       return wildcards;
/*      */     }
/*      */     
/* 2078 */     return getMatch(certificates, c -> 
/* 2079 */         (c.getTargetVIN().isEmpty() && arrayContainsValue(c.getTargetECU(), targetECU)));
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
/*      */   private List<Certificate> getCertificatesForTargetECUAndTargetVIN(List<Certificate> certificates, String targetECU, String targetVIN) {
/* 2095 */     List<Certificate> wildcards = getMatch(certificates, c -> 
/* 2096 */         (c.getTargetECU().isEmpty() && c.getTargetVIN().isEmpty()));
/* 2097 */     if (!wildcards.isEmpty()) {
/* 2098 */       return wildcards;
/*      */     }
/*      */     
/* 2101 */     List<Certificate> wildcardECU = getMatch(certificates, c -> 
/* 2102 */         (c.getTargetECU().isEmpty() && arrayContainsValue(c.getTargetVIN(), targetVIN)));
/* 2103 */     if (!wildcardECU.isEmpty()) {
/* 2104 */       return wildcardECU;
/*      */     }
/*      */     
/* 2107 */     List<Certificate> wildcardVIN = getMatch(certificates, c -> 
/* 2108 */         (c.getTargetVIN().isEmpty() && arrayContainsValue(c.getTargetECU(), targetECU)));
/* 2109 */     if (!wildcardVIN.isEmpty()) {
/* 2110 */       return wildcardVIN;
/*      */     }
/*      */     
/* 2113 */     List<Certificate> exactMatch = getMatch(certificates, c -> 
/* 2114 */         (arrayContainsValue(c.getTargetECU(), targetECU) && arrayContainsValue(c.getTargetVIN(), targetVIN)));
/*      */     
/* 2116 */     if (!exactMatch.isEmpty()) {
/* 2117 */       return exactMatch;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2123 */     return new ArrayList<>();
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
/*      */   private <T extends Certificate> List<T> getCertificatesBasedOnValidity(List<T> certificates, Function<Certificate, Date> function) {
/* 2135 */     Map<Date, List<T>> certsGroupedByValidityPeriod = (Map<Date, List<T>>)certificates.stream().collect(Collectors.groupingBy(function::apply, Collectors.toList()));
/*      */     
/* 2137 */     Optional<Map.Entry<Date, List<T>>> result = certsGroupedByValidityPeriod.entrySet().stream().sorted(Map.Entry.comparingByKey().reversed()).findFirst();
/* 2138 */     return result.isPresent() ? (List<T>)((Map.Entry)result.get()).getValue() : new ArrayList<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Certificate getCertificateByValidityAndSignature(List<Certificate> certificates, Function<List<Certificate>, List<Certificate>> function) {
/* 2149 */     String METHOD_NAME = "getCertificateByValidityAndSignature";
/* 2150 */     this.logger.entering(CLASS_NAME, "getCertificateByValidityAndSignature");
/* 2151 */     Certificate certificate = null;
/* 2152 */     if (certificates != null) {
/*      */       
/* 2154 */       List<Certificate> resultByValidity = function.apply(certificates);
/* 2155 */       if (resultByValidity.size() == 1) {
/* 2156 */         certificate = resultByValidity.get(0);
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 2161 */         Optional<Certificate> certificateOptional = resultByValidity.stream().sorted(Comparator.comparing(Certificate::getSignature)).findFirst();
/* 2162 */         if (certificateOptional.isPresent()) {
/* 2163 */           certificate = certificateOptional.get();
/*      */         }
/*      */       } 
/*      */     } 
/* 2167 */     this.logger.exiting(CLASS_NAME, "getCertificateByValidityAndSignature");
/* 2168 */     return certificate;
/*      */   }
/*      */   
/*      */   private List<Certificate> getMatch(List<Certificate> certificates, Predicate<Certificate> predicate) {
/* 2172 */     return (List<Certificate>)certificates.stream().filter(predicate).collect(Collectors.toList());
/*      */   }
/*      */   
/*      */   private List<Certificate> findEnhancedUnderDiag(String serialNo, List<Certificate> repoResult) {
/* 2176 */     return (List<Certificate>)repoResult.stream()
/* 2177 */       .filter(c -> (c.getIssuer().equals(c.getParent().getIssuer()) && c.getIssuer().equals(c.getParent().getParent().getSubject()) && c.getAuthorityKeyIdentifier().equals(c.getParent().getAuthorityKeyIdentifier()) && c.getAuthorityKeyIdentifier().equals(c.getParent().getParent().getSubjectKeyIdentifier()) && !c.isSecOCISCert() && c.getParent().getSerialNo().trim().equals(serialNo)))
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2182 */       .collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */   
/*      */   private CertificateMoreResultsFoundException logAndThrowMoreDiagOrTimeCertFound() {
/* 2187 */     CertificateMoreResultsFoundException ex = new CertificateMoreResultsFoundException(this.i18n.getMessage("moreDiagCertFoundForCombo"), "moreDiagCertFoundForCombo");
/*      */     
/* 2189 */     this.logger.logWithTranslation(Level.WARNING, "000143X", ex.getMessageId(), ex
/* 2190 */         .getClass().getSimpleName());
/* 2191 */     return ex;
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
/*      */   private boolean arrayContainsValue(String source, String value) {
/* 2204 */     if (source.equals(value)) {
/* 2205 */       return true;
/*      */     }
/* 2207 */     String[] array = source.split(", ");
/* 2208 */     for (String current : array) {
/* 2209 */       if (current.equals(value)) {
/* 2210 */         return true;
/*      */       }
/*      */     } 
/* 2213 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\control\SearchEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */