/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.CertificateUtil
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateMoreResultsFoundException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException
 *  com.daimler.cebas.certificates.control.exceptions.CertificateSigningException
 *  com.daimler.cebas.certificates.control.repository.CertificateRepository
 *  com.daimler.cebas.certificates.control.validation.CertificatesValidator
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateState
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.entity.UserRole
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.CEBASProperty
 *  com.daimler.cebas.common.control.HexUtil
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.common.entity.EmptyPredicate
 *  com.daimler.cebas.common.entity.InPredicate
 *  com.daimler.cebas.configuration.control.ConfigurationUtil
 *  com.daimler.cebas.configuration.entity.Configuration
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  com.daimler.cebas.users.entity.User
 *  com.daimler.cebas.users.entity.UserKeyPair
 *  javax.persistence.Tuple
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.data.domain.Pageable
 *  org.springframework.util.CollectionUtils
 */
package com.daimler.cebas.certificates.control;

import com.daimler.cebas.certificates.control.CertificateUtil;
import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.control.exceptions.CertificateMoreResultsFoundException;
import com.daimler.cebas.certificates.control.exceptions.CertificateNotFoundException;
import com.daimler.cebas.certificates.control.exceptions.CertificateSigningException;
import com.daimler.cebas.certificates.control.repository.CertificateRepository;
import com.daimler.cebas.certificates.control.validation.CertificatesValidator;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateState;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.entity.UserRole;
import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.CEBASProperty;
import com.daimler.cebas.common.control.HexUtil;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.common.entity.EmptyPredicate;
import com.daimler.cebas.common.entity.InPredicate;
import com.daimler.cebas.configuration.control.ConfigurationUtil;
import com.daimler.cebas.configuration.entity.Configuration;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import com.daimler.cebas.users.entity.User;
import com.daimler.cebas.users.entity.UserKeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Tuple;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

@CEBASControl
public class SearchEngine {
    private static final String CLASS_NAME = SearchEngine.class.getSimpleName();
    private static final String FIND_DIAGNOSTIC_CERTIFICATES = "findDiagnosticCertificates";
    protected CertificateRepository certificateRepository;
    protected Logger logger;
    protected MetadataManager i18n;
    @Autowired
    protected Session session;

    @Autowired
    public SearchEngine(CertificateRepository certificateRepository, Logger logger, MetadataManager i18n) {
        this.certificateRepository = certificateRepository;
        this.logger = logger;
        this.i18n = i18n;
    }

    private static Map<String, Object> getDefaultQueryParams(User user) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user", user);
        params.put("state", CertificateState.ISSUED);
        return params;
    }

    public <T extends Certificate> List<T> getCertsByAKI(User user, List<String> authorityKeyIDs, Class<T> clazz) {
        String METHOD_NAME = "getCertsByAKI";
        this.logger.entering(CLASS_NAME, "getCertsByAKI");
        Map<String, Object> map = SearchEngine.getDefaultQueryParams(user);
        if (!CollectionUtils.isEmpty(authorityKeyIDs)) {
            map.put("authorityKeyIdentifier", new InPredicate(new Object[]{authorityKeyIDs}));
        }
        List certificates = this.certificateRepository.findWithQuery(clazz, map, -1);
        this.logger.exiting(CLASS_NAME, "getCertsByAKI");
        return certificates;
    }

    public List<Certificate> findDiagnosticCertificates(User user, String backendSubjectKeyIdentifier, String serialNo) {
        return this.findDiagnosticCertificates(user, backendSubjectKeyIdentifier, serialNo, Certificate.class);
    }

    public <T extends Certificate> List<T> findDiagnosticCertificates(User user, String backendSubjectKeyIdentifier, String serialNo, Class<T> type) {
        String METHOD_NAME = FIND_DIAGNOSTIC_CERTIFICATES;
        this.logger.entering(CLASS_NAME, FIND_DIAGNOSTIC_CERTIFICATES);
        serialNo = HexUtil.omitLeadingZeros((String)serialNo);
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("serialNo", serialNo);
        params.put("type", CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE);
        params.put("authorityKeyIdentifier", backendSubjectKeyIdentifier);
        this.logger.exiting(CLASS_NAME, FIND_DIAGNOSTIC_CERTIFICATES);
        return this.certificateRepository.findWithQuery(type, params, 0);
    }

    public List<Certificate> findDiagnosticCertificates(User user, Certificate backend) {
        String METHOD_NAME = FIND_DIAGNOSTIC_CERTIFICATES;
        this.logger.entering(CLASS_NAME, FIND_DIAGNOSTIC_CERTIFICATES);
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("type", CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE);
        params.put("parent", backend);
        this.logger.exiting(CLASS_NAME, FIND_DIAGNOSTIC_CERTIFICATES);
        return this.certificateRepository.findWithQuery(Certificate.class, params, 0);
    }

    public List<Certificate> findValidDiagnosticCertificates(User user, String backendSubjectKeyIdentifier, String serialNo) {
        List<Certificate> findDiagnosticCertificates = this.findDiagnosticCertificates(user, backendSubjectKeyIdentifier, serialNo);
        List<Certificate> validCertificates = this.getValidRegularCertificates(findDiagnosticCertificates);
        return this.getOnesWithValidPublicKeyAndPrivateKey(validCertificates);
    }

    public List<Certificate> findDiagnosticCertificatesByBackendSKIs(User user, List<String> backendSubjectKeyIdentifiers, String userRole, String targetECU, String targetVIN) {
        String METHOD_NAME = "findDiagnosticCertificatesByBackendSKIs";
        this.logger.entering(CLASS_NAME, "findDiagnosticCertificatesByBackendSKIs");
        List result = this.certificateRepository.findDiagnosticCertificatesForBackends(user, backendSubjectKeyIdentifiers, userRole, targetECU, targetVIN);
        this.logger.exiting(CLASS_NAME, "findDiagnosticCertificatesByBackendSKIs");
        return result;
    }

    public Optional<Certificate> findValidDiagOrTimeCertificate(User user, String backendSubjectKeyIdentifier, String serialNo) {
        String METHOD_NAME = "findValidDiagOrTimeCertificate";
        this.logger.entering(CLASS_NAME, "findValidDiagOrTimeCertificate");
        serialNo = HexUtil.omitLeadingZeros((String)serialNo);
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("serialNo", serialNo);
        params.put("type", CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE);
        params.put("authorityKeyIdentifier", backendSubjectKeyIdentifier);
        List repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        List<Certificate> result = this.getValidRegularCertificates(repoResult);
        result = this.getOnesWithValidPublicKeyAndPrivateKey(result);
        if (!result.isEmpty()) {
            if (result.size() != 1) throw this.logAndThrowMoreDiagOrTimeCertFound();
            this.logger.exiting(CLASS_NAME, "findValidDiagOrTimeCertificate");
            return Optional.of(result.get(0));
        }
        params.put("type", CertificateType.TIME_CERTIFICATE);
        result = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        result = this.getValidRegularCertificates(result);
        result = this.getOnesWithValidPublicKeyAndPrivateKey(result);
        if (result.isEmpty()) {
            this.logger.exiting(CLASS_NAME, "findValidDiagOrTimeCertificate");
            return Optional.empty();
        }
        if (result.size() != 1) throw this.logAndThrowMoreDiagOrTimeCertFound();
        this.logger.exiting(CLASS_NAME, "findValidDiagOrTimeCertificate");
        return Optional.of(result.get(0));
    }

    public <T extends Certificate> List<T> findCertByAuthKeyIdentAndSerialNo(User user, String authorityKeyIdentifier, String serialNo, Class<T> clazz) {
        String METHOD_NAME = "findCertByAuthKeyIdentAndSerialNo";
        this.logger.entering(CLASS_NAME, "findCertByAuthKeyIdentAndSerialNo");
        serialNo = HexUtil.omitLeadingZeros((String)serialNo);
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("authorityKeyIdentifier", authorityKeyIdentifier);
        params.put("serialNo", serialNo);
        this.logger.exiting(CLASS_NAME, "findCertByAuthKeyIdentAndSerialNo");
        return this.certificateRepository.findWithQuery(clazz, params, 0);
    }

    public <T extends Certificate> List<Tuple> findTuples(User user, Map<String, Object> queryParameters, List<String> columns, Class<T> clazz) {
        String METHOD_NAME = "findTuples";
        this.logger.entering(CLASS_NAME, "findTuples");
        Map<String, Object> defaultParams = SearchEngine.getDefaultQueryParams(user);
        defaultParams.putAll(queryParameters);
        List result = this.certificateRepository.findTupleWithQuery(clazz, defaultParams, columns, 0);
        this.logger.exiting(CLASS_NAME, "findTuples");
        return result;
    }

    public <T extends Certificate> List<? extends Certificate> findCertByAuthKeyIdentAndSerialNoValid(User user, String authorityKeyIdentifier, String serialNo, Class<T> clazz) {
        String METHOD_NAME = "findCertByAuthKeyIdentAndSerialNoValid";
        this.logger.entering(CLASS_NAME, "findCertByAuthKeyIdentAndSerialNoValid");
        serialNo = HexUtil.omitLeadingZeros((String)serialNo);
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("authorityKeyIdentifier", authorityKeyIdentifier);
        params.put("serialNo", serialNo);
        this.logger.exiting(CLASS_NAME, "findCertByAuthKeyIdentAndSerialNoValid");
        List ecus = this.certificateRepository.findWithQuery(clazz, params, 0);
        this.validateParent(ecus, false);
        return this.getValidCertificatesExtends(ecus);
    }

    public Optional<Certificate> findValidDiagnosticCertificate(User user, String backendSubjectKeyIdentifier, String targetVIN, String targetECU, String userRole) {
        List<Certificate> result;
        List repoResult;
        String METHOD_NAME = FIND_DIAGNOSTIC_CERTIFICATES;
        this.logger.entering(CLASS_NAME, FIND_DIAGNOSTIC_CERTIFICATES);
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("type", CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE);
        params.put("authorityKeyIdentifier", backendSubjectKeyIdentifier);
        if (!UserRole.NO_ROLE.getText().equals(userRole)) {
            params.put("userRole", userRole);
            repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
            this.validateParent(repoResult, false);
            List<Certificate> validCertificates = this.getValidRegularCertificates(repoResult);
            validCertificates = this.getOnesWithValidPublicKeyAndPrivateKey(validCertificates);
            result = this.getFilteredByTargetVINTargetECU(validCertificates, targetECU, targetVIN);
        } else {
            repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
            this.validateParent(repoResult, false);
            List<Certificate> validCertificates = this.getValidRegularCertificates(repoResult);
            validCertificates = this.getOnesWithValidPublicKeyAndPrivateKey(validCertificates);
            result = this.determineResultBasedOnRolePriorityAndTargetVIN(user, targetVIN, targetECU, validCertificates);
        }
        Certificate certificate = this.getCertificateByValidityAndSignatureAscending(result);
        this.logger.exiting(CLASS_NAME, FIND_DIAGNOSTIC_CERTIFICATES);
        return Optional.ofNullable(certificate);
    }

    private List<Certificate> determineResultBasedOnRolePriorityAndTargetVIN(User user, String targetVIN, String targetECU, List<Certificate> validCertificates) {
        Map.Entry<String, List<Certificate>> group;
        List<Certificate> groupCertificates;
        List<Certificate> result = new ArrayList<Certificate>();
        Iterator<Map.Entry<String, List<Certificate>>> iterator = this.getCertificatesGroupBaseOnRolesPriority(user, validCertificates).iterator();
        do {
            if (!iterator.hasNext()) return result;
        } while ((result = this.getFilteredByTargetVINTargetECU(groupCertificates = (group = iterator.next()).getValue(), targetECU, targetVIN)).isEmpty());
        return result;
    }

    public Optional<Certificate> getIssuerOfLinkCertificate(User user, Certificate linkCertificate) {
        String METHOD_NAME = "getIssuerOfLinkCertificate";
        this.logger.entering(CLASS_NAME, "getIssuerOfLinkCertificate");
        CertificateType type = linkCertificate.getType() == CertificateType.ROOT_CA_LINK_CERTIFICATE ? CertificateType.ROOT_CA_CERTIFICATE : CertificateType.BACKEND_CA_CERTIFICATE;
        Optional<Certificate> optional = this.findCertificateBySkiAndType(user, linkCertificate.getAuthorityKeyIdentifier(), type);
        this.logger.exiting(CLASS_NAME, "getIssuerOfLinkCertificate");
        return optional;
    }

    public Optional<Certificate> findTimeCertificate(User user, String backendSubjectKeyIdentifier, byte[] nonce, String targetVIN, String targetECU) {
        String METHOD_NAME = "findTimeCertificate";
        this.logger.entering(CLASS_NAME, "findTimeCertificate");
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("type", CertificateType.TIME_CERTIFICATE);
        params.put("authorityKeyIdentifier", backendSubjectKeyIdentifier);
        List<Certificate> repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0).stream().filter(c -> Arrays.equals(c.getNonceRaw(), nonce)).collect(Collectors.toList());
        this.validateParent(repoResult, false);
        List<Certificate> validCertificates = this.getValidRegularCertificates(repoResult);
        validCertificates = this.getOnesWithValidPublicKeyAndPrivateKey(validCertificates);
        List<Certificate> result = this.determineResultBasedOnRolePriorityAndTargetVIN(user, targetVIN, targetECU, validCertificates);
        Certificate certificate = this.getCertificateByValidityAndSignatureAscending(result);
        this.logger.exiting(CLASS_NAME, "findTimeCertificate");
        return Optional.ofNullable(certificate);
    }

    public List<Certificate> getValidRegularCertificates(List<Certificate> repoResult) {
        if (this.isExtendedValidation()) return repoResult.stream().filter(cert -> CertificatesValidator.isValidInChain((Certificate)cert, (MetadataManager)this.i18n, (Logger)this.logger)).collect(Collectors.toList());
        return repoResult;
    }

    public boolean isExtendedValidation() {
        User user = this.session.getCurrentUser();
        return ConfigurationUtil.hasUserExtendedValidation((User)user, (Logger)this.logger, (MetadataManager)this.i18n);
    }

    public List<? extends Certificate> getValidCertificatesExtends(List<? extends Certificate> repoResult) {
        if (this.isExtendedValidation()) return repoResult.stream().filter(cert -> CertificatesValidator.isValidInChain((Certificate)cert, (MetadataManager)this.i18n, (Logger)this.logger)).collect(Collectors.toList());
        return repoResult;
    }

    private List<Certificate> getOnesWithValidPublicKeyAndPrivateKey(List<Certificate> validResult) {
        if (this.isExtendedValidation()) return validResult.stream().filter(cert -> CertificatesValidator.checkPublicKeyMatchesTheOneGeneratedFromPrivateKey((Session)this.session, (Certificate)cert, (Logger)this.logger)).collect(Collectors.toList());
        return validResult;
    }

    public List<Certificate> findCSRsUnderBackend(User user, Certificate backend) {
        String METHOD_NAME = "findCSRsUnderBackend";
        this.logger.entering(CLASS_NAME, "findCSRsUnderBackend");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("state", CertificateState.SIGNING_REQUEST);
        params.put("user", user);
        params.put("parent", backend);
        List csrs = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        this.logger.exiting(CLASS_NAME, "findCSRsUnderBackend");
        return csrs;
    }

    public Optional<Certificate> findCsrById(User user, String id) {
        String METHOD_NAME = "findCsrById";
        this.logger.entering(CLASS_NAME, "findCsrById");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("state", CertificateState.SIGNING_REQUEST);
        params.put("user", user);
        params.put("entityId", id);
        List csrResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        this.logger.exiting(CLASS_NAME, "findCsrById");
        return !csrResult.isEmpty() ? Optional.of(csrResult.get(0)) : Optional.empty();
    }

    public Optional<Certificate> findCsrByPublicKey(User user, String publicKey) {
        String METHOD_NAME = "findCsrByPublicKey";
        this.logger.entering(CLASS_NAME, "findCsrByPublicKey");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("state", CertificateState.SIGNING_REQUEST);
        params.put("user", user);
        params.put("subjectPublicKey", publicKey);
        List csrResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        this.logger.exiting(CLASS_NAME, "findCsrByPublicKey");
        return !csrResult.isEmpty() ? Optional.of(csrResult.get(0)) : Optional.empty();
    }

    public Optional<Certificate> findCertificateById(User user, String id) {
        String METHOD_NAME = "findCsrById";
        this.logger.entering(CLASS_NAME, "findCsrById");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("state", CertificateState.ISSUED);
        params.put("user", user);
        params.put("entityId", id);
        List csrResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        this.logger.exiting(CLASS_NAME, "findCsrById");
        return !csrResult.isEmpty() ? Optional.of(csrResult.get(0)) : Optional.empty();
    }

    public List<Certificate> findCSRsUnderBackend(User user, String backendSubjectKeyIdentifier) {
        String METHOD_NAME = "findCSRsUnderBackend";
        this.logger.entering(CLASS_NAME, "findCSRsUnderBackend");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("state", CertificateState.SIGNING_REQUEST);
        params.put("user", user);
        params.put("authorityKeyIdentifier", backendSubjectKeyIdentifier);
        List csrs = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        this.logger.exiting(CLASS_NAME, "findCSRsUnderBackend");
        return csrs;
    }

    public List<String> findCsrIdsUnderBackend(User user, CertificateType csrType, String backendSubjectKeyIdentifier, List<String> targetEcu, List<String> targetVin) {
        String METHOD_NAME = "findCsrIdsUnderBackend";
        this.logger.entering(CLASS_NAME, "findCsrIdsUnderBackend");
        List csrs = this.certificateRepository.findCSRsWithTargetVinAndTargetEcu(user, csrType, backendSubjectKeyIdentifier, CertificateUtil.getPattern(targetEcu), CertificateUtil.getPattern(targetVin), Certificate.class);
        this.logger.exiting(CLASS_NAME, "findCsrIdsUnderBackend");
        return csrs.stream().filter(csr -> CertificateUtil.includesTargetECUsAndTargetVINs((Certificate)csr, (List)targetEcu, (List)targetVin)).map(Certificate::getEntityId).collect(Collectors.toList());
    }

    public List<Certificate> findTimeCSRs(User user, String backendSubjectKeyIdentifier, String nonce, String targetVIN, String targetECU) {
        String METHOD_NAME = "findTimeCSRs";
        this.logger.entering(CLASS_NAME, "findTimeCSRs");
        String ecu = targetECU == null ? "" : targetECU;
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("type", CertificateType.TIME_CERTIFICATE);
        params.put("state", CertificateState.SIGNING_REQUEST);
        params.put("user", user);
        this.logger.exiting(CLASS_NAME, "findTimeCSRs");
        return this.certificateRepository.findWithQuery(Certificate.class, params, 0).stream().filter(c -> c.getParent().getSubjectKeyIdentifier().equals(backendSubjectKeyIdentifier) && c.getTargetVIN().equals(targetVIN) && c.getTargetECU().equals(ecu) && c.getNonce().equals(nonce)).collect(Collectors.toList());
    }

    public List<Certificate> findSecOCISCSRs(User user, String diagCertSerialNo, String targetVIN, String targetECU, String targetSubjectKeyIdentifier, String authorityKeyIdentifier) {
        String METHOD_NAME = "findSecOCISCSRs";
        this.logger.entering(CLASS_NAME, "findSecOCISCSRs");
        String serialNo = HexUtil.omitLeadingZeros((String)diagCertSerialNo);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("type", CertificateType.ENHANCED_RIGHTS_CERTIFICATE);
        params.put("state", CertificateState.SIGNING_REQUEST);
        params.put("authorityKeyIdentifier", authorityKeyIdentifier);
        params.put("targetSubjectKeyIdentifier", targetSubjectKeyIdentifier);
        params.put("user", user);
        this.logger.exiting(CLASS_NAME, "findSecOCISCSRs");
        return this.certificateRepository.findWithQuery(Certificate.class, params, 0).stream().filter(c -> c.getParent().getSerialNo().trim().equals(serialNo) && (targetVIN != null && this.arrayContainsValue(c.getTargetVIN(), targetVIN) || targetVIN == null && c.getTargetVIN().isEmpty()) && (targetECU != null && this.arrayContainsValue(c.getTargetECU(), targetECU) || targetECU == null && c.getTargetECU().isEmpty())).collect(Collectors.toList());
    }

    public List<Certificate> findECUCertificatesByAKIAndECUId(User user, String authorityKeyIdentifier, String ecuId) {
        String METHOD_NAME = "findECUCertificatesByAKIAndECUId";
        this.logger.entering(CLASS_NAME, "findECUCertificatesByAKIAndECUId");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("type", CertificateType.ECU_CERTIFICATE);
        params.put("state", CertificateState.ISSUED);
        params.put("authorityKeyIdentifier", authorityKeyIdentifier);
        params.put("uniqueECUID", "%" + ecuId + "%");
        params.put("user", user);
        this.logger.exiting(CLASS_NAME, "findECUCertificatesByAKIAndECUId");
        return this.certificateRepository.findWithQuery(Certificate.class, params, 0);
    }

    public List<Certificate> findECUCertificatesByAKIAndECUIdValid(User user, String authorityKeyIdentifier, String ecuId) {
        String METHOD_NAME = "findECUCertificatesByAKIAndECUIdValid";
        this.logger.entering(CLASS_NAME, "findECUCertificatesByAKIAndECUIdValid");
        List<Certificate> ecus = this.findECUCertificatesByAKIAndECUId(user, authorityKeyIdentifier, ecuId);
        this.validateParent(ecus, false);
        this.logger.exiting(CLASS_NAME, "findECUCertificatesByAKIAndECUIdValid");
        return this.getValidRegularCertificates(ecus);
    }

    public List<Certificate> findValidEnhancedDiagnosticCertificates(User user, String backendSubjectKeyIdentifier, String diagCertSerialNo, String targetVIN, String targetECU) {
        String METHOD_NAME = "findValidEnhancedDiagnosticCertificates";
        this.logger.entering(CLASS_NAME, "findValidEnhancedDiagnosticCertificates");
        String serialNo = HexUtil.omitLeadingZeros((String)diagCertSerialNo);
        Map<String, Object> params = this.getEnhancedRightsQueryParams(user, backendSubjectKeyIdentifier);
        List repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        this.validateParent(repoResult, true);
        List<Certificate> validCertificates = this.getValidRegularCertificates(repoResult);
        List<Certificate> result = this.findEnhancedUnderDiag(serialNo, validCertificates);
        this.logger.exiting(CLASS_NAME, "findValidEnhancedDiagnosticCertificates");
        return this.getFilteredByTargetVINTargetECUCollectAllMataches(result, targetECU, targetVIN);
    }

    public List<Certificate> findEnhancedRightsCertificates(User user, String backendSubjectKeyIdentifier, String diagCertSerialNo) {
        String METHOD_NAME = "findEnhancedDiagnosticCertificates";
        this.logger.entering(CLASS_NAME, "findEnhancedDiagnosticCertificates");
        String serialNo = HexUtil.omitLeadingZeros((String)diagCertSerialNo);
        Map<String, Object> params = this.getEnhancedRightsQueryParams(user, backendSubjectKeyIdentifier);
        List repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        List<Certificate> result = this.findEnhancedUnderDiag(serialNo, repoResult);
        this.logger.exiting(CLASS_NAME, "findEnhancedDiagnosticCertificates");
        return result;
    }

    public List<Certificate> findEnhancedRightsCertificatesWithServices(User user, String backendSubjectKeyIdentifier, String services) {
        String METHOD_NAME = "findEnhancedDiagnosticCertificates";
        this.logger.entering(CLASS_NAME, "findEnhancedDiagnosticCertificates");
        ArrayList<Certificate> result = new ArrayList<Certificate>();
        Map<String, Object> params = this.getEnhancedRightsQueryParams(user, backendSubjectKeyIdentifier);
        params.put("services", new EmptyPredicate(false));
        List found = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        List<String> servicesSearched = this.serviceStringToList(services);
        Iterator iterator = found.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.logger.exiting(CLASS_NAME, "findEnhancedDiagnosticCertificates");
                return result;
            }
            Certificate c = (Certificate)iterator.next();
            String s = c.getServices();
            List<String> servicesFound = this.serviceStringToList(s);
            if (!servicesFound.containsAll(servicesSearched)) continue;
            result.add(c);
            this.logger.log(Level.INFO, "000535", this.i18n.getMessage("enhRightsFoundForService", new String[]{c.getSerialNo(), c.getParent().getSerialNo(), c.getParent().getSubjectKeyIdentifier()}), CLASS_NAME);
        }
    }

    private List<String> serviceStringToList(String s) {
        return Stream.of(s.split(",")).map(String::trim).map(String::toLowerCase).collect(Collectors.toList());
    }

    private Map<String, Object> getEnhancedRightsQueryParams(User user, String backendSubjectKeyIdentifier) {
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("type", CertificateType.ENHANCED_RIGHTS_CERTIFICATE);
        params.put("authorityKeyIdentifier", backendSubjectKeyIdentifier);
        return params;
    }

    public List<Certificate> findEnhancedDiagnosticCertificatesActiveForTest(User user) {
        String METHOD_NAME = "findEnhancedDiagnosticCertificatesActiveForTest";
        this.logger.entering(CLASS_NAME, "findEnhancedDiagnosticCertificatesActiveForTest");
        this.logger.exiting(CLASS_NAME, "findEnhancedDiagnosticCertificatesActiveForTest");
        return this.certificateRepository.findActiveForTesting(user, CertificateType.ENHANCED_RIGHTS_CERTIFICATE).stream().filter(c -> !c.isSecOCISCert()).collect(Collectors.toList());
    }

    public Certificate findCertificateActiveForTesting(User user, CertificateType type) {
        String METHOD_NAME = "findCertificateActiveForTesting";
        this.logger.entering(CLASS_NAME, "findCertificateActiveForTesting");
        List certificates = this.certificateRepository.findActiveForTesting(user, type);
        if (CollectionUtils.isEmpty((Collection)certificates)) {
            this.logger.log(Level.INFO, "000444", this.i18n.getMessage("certificateNotFoundManualSelection"), CLASS_NAME);
            throw new CertificateNotFoundException(this.i18n.getMessage("certificateNotFoundManualSelection"));
        }
        if (certificates.size() > 1) {
            throw new CEBASCertificateException(this.i18n.getMessage("foundMoreCertificatesActiveForTesting", new String[]{type.getText()}));
        }
        this.logger.exiting(CLASS_NAME, "findCertificateActiveForTesting");
        return (Certificate)certificates.get(0);
    }

    public <T extends Certificate> List<T> findCertificateActiveForTestingWithoutValidation(Class<T> theClass, User user, CertificateType type) {
        String METHOD_NAME = "findCertificateActiveForTesting";
        this.logger.entering(CLASS_NAME, "findCertificateActiveForTesting");
        List certificates = this.certificateRepository.findActiveForTesting(theClass, user, type);
        this.logger.exiting(CLASS_NAME, "findCertificateActiveForTesting");
        return certificates;
    }

    public List<Certificate> findCertificatesByType(User user, CertificateType type) {
        String METHOD_NAME = "findCertificateByType";
        this.logger.entering(CLASS_NAME, "findCertificateByType");
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("type", type);
        List certificates = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        this.logger.exiting(CLASS_NAME, "findCertificateByType");
        return certificates;
    }

    public <T extends Certificate> List<T> findCertificatesByType(Class<T> theType, User user, CertificateType type) {
        String METHOD_NAME = "findCertificateByType";
        this.logger.entering(CLASS_NAME, "findCertificateByType");
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("type", type);
        List certificates = this.certificateRepository.findWithQuery(theType, params, 0);
        this.logger.exiting(CLASS_NAME, "findCertificateByType");
        return certificates;
    }

    public List<Certificate> findSecOCISCertificates(User user, Certificate diagCert, String targetVIN, String targetECU, String subjectKeyIdentifier) {
        String METHOD_NAME = "findSecOCISCertificates";
        this.logger.entering(CLASS_NAME, "findSecOCISCertificates");
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("type", CertificateType.ENHANCED_RIGHTS_CERTIFICATE);
        params.put("issuerSerialNumber", diagCert.getIssuerSerialNumber());
        params.put("issuer", diagCert.getIssuer());
        params.put("targetECU", targetECU);
        List repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        this.validateParent(repoResult, true);
        List<Certificate> validCertificates = this.getValidRegularCertificates(repoResult);
        List<Certificate> result = validCertificates.stream().filter(c -> c.isSecOCISCert() && c.getTargetSubjectKeyIdentifier().equals(subjectKeyIdentifier) && c.getParent().getSerialNo().trim().equals(diagCert.getSerialNo())).collect(Collectors.toList());
        this.logger.exiting(CLASS_NAME, "findSecOCISCertificates");
        return this.getFilteredByTargetVINTargetECU(result, targetECU, targetVIN);
    }

    public Certificate findSecOCISCertificateActiveForTesting(User user) {
        String METHOD_NAME = "findSecOCISCertificateActiveForTesting";
        this.logger.entering(CLASS_NAME, "findSecOCISCertificateActiveForTesting");
        List certificates = this.certificateRepository.findActiveForTesting(user, CertificateType.ENHANCED_RIGHTS_CERTIFICATE).stream().filter(Certificate::isSecOCISCert).collect(Collectors.toList());
        if (certificates.size() > 1) {
            CEBASCertificateException cebasCertificateException = new CEBASCertificateException(this.i18n.getMessage("foundMoreSecOcisActiveForTesting"));
            this.logger.logWithExceptionByInfo("000478X", (CEBASException)cebasCertificateException);
            throw cebasCertificateException;
        }
        if (certificates.isEmpty()) {
            CertificateNotFoundException exc = new CertificateNotFoundException(this.i18n.getMessage("certificateNotFound"));
            this.logger.logWithExceptionByInfo("000479X", (CEBASException)exc);
            throw exc;
        }
        this.logger.exiting(CLASS_NAME, "findSecOCISCertificateActiveForTesting");
        return (Certificate)certificates.get(0);
    }

    public List<Certificate> findAllBackends(User user) {
        String METHOD_NAME = "findAllBackends";
        this.logger.entering(CLASS_NAME, "findAllBackends");
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("type", CertificateType.BACKEND_CA_CERTIFICATE);
        List certificates = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        this.logger.exiting(CLASS_NAME, "findAllBackends");
        return certificates;
    }

    public List<Certificate> findSecureVariantCodingUserCertificates(User user, String backendCertSkid, String targetVIN, String targetECU) {
        String METHOD_NAME = "findSecureVariantCodingUserCertificates";
        this.logger.entering(CLASS_NAME, "findSecureVariantCodingUserCertificates");
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("type", CertificateType.VARIANT_CODE_USER_CERTIFICATE);
        params.put("authorityKeyIdentifier", backendCertSkid);
        List repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        this.validateParent(repoResult, false);
        List<Certificate> validCertificates = this.getValidRegularCertificates(repoResult);
        validCertificates = this.getOnesWithValidPublicKeyAndPrivateKey(validCertificates);
        this.logger.exiting(CLASS_NAME, "findSecureVariantCodingUserCertificates");
        return this.getFilteredByTargetVINTargetECU(validCertificates, targetECU, targetVIN);
    }

    public <T extends Certificate> List<T> findCertificatesWithTargetVinAndTargetEcu(User user, CertificateType certificateType, String backendCertSkid, List<String> targetECUs, List<String> targetVINs, String minRemainingValidity, Class<T> type) {
        String METHOD_NAME = "findCertificatesWithTargetVinAndTargetEcu";
        this.logger.entering(CLASS_NAME, "findCertificatesWithTargetVinAndTargetEcu");
        List result = this.certificateRepository.findCertificatesWithTargetVinAndTargetEcu(user, certificateType, backendCertSkid, CertificateUtil.getPattern(targetECUs), CertificateUtil.getPattern(targetVINs), type);
        List validCertificates = result.stream().filter(cert -> CertificatesValidator.exceedMinRemainingValidity((Certificate)cert, (String)minRemainingValidity) && CertificateUtil.includesTargetECUsAndTargetVINs((Certificate)cert, (List)targetECUs, (List)targetVINs)).collect(Collectors.toList());
        this.logger.exiting(CLASS_NAME, "findCertificatesWithTargetVinAndTargetEcu");
        return validCertificates;
    }

    public <T extends Certificate> Optional<T> findSecureVariantCodingUserCertificate(User user, String backendCertSkid, List<String> targetECUs, List<String> targetVINs, Class<T> type) {
        String METHOD_NAME = "findSecureVariantCodingUserCertificate";
        this.logger.entering(CLASS_NAME, "findSecureVariantCodingUserCertificate");
        List result = this.certificateRepository.findCertificatesWithTargetVinAndTargetEcu(user, CertificateType.VARIANT_CODE_USER_CERTIFICATE, backendCertSkid, CertificateUtil.getPattern(targetECUs), CertificateUtil.getPattern(targetVINs), type);
        List validCertificates = result.stream().filter(cert -> !CertificatesValidator.isExpired((Certificate)cert, (Logger)this.logger, (MetadataManager)this.i18n) && CertificateUtil.includesTargetECUsAndTargetVINs((Certificate)cert, (List)targetECUs, (List)targetVINs)).collect(Collectors.toList());
        this.logger.exiting(CLASS_NAME, "findSecureVariantCodingUserCertificate");
        return !validCertificates.isEmpty() ? Optional.of(this.getCertificatesBasedOnValidityPeriod(validCertificates).get(0)) : Optional.empty();
    }

    public Optional<Certificate> findSecVarCodingWithoutTargetVinAndTargetEcu(User user, String backendCertSkid) {
        String METHOD_NAME = "findSecVarCodingWithoutTargetVinAndTargetEcu";
        this.logger.entering(CLASS_NAME, "findSecVarCodingWithoutTargetVinAndTargetEcu");
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("type", CertificateType.VARIANT_CODE_USER_CERTIFICATE);
        params.put("authorityKeyIdentifier", backendCertSkid);
        List result = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        List validCertificates = result.stream().filter(cert -> !CertificatesValidator.isExpired((Certificate)cert, (Logger)this.logger, (MetadataManager)this.i18n) && !this.hasTargetVinOrTargetEcu((Certificate)cert)).collect(Collectors.toList());
        this.logger.exiting(CLASS_NAME, "findSecVarCodingWithoutTargetVinAndTargetEcu");
        return !validCertificates.isEmpty() ? Optional.of(this.getCertificatesBasedOnValidityPeriod(validCertificates).get(0)) : Optional.empty();
    }

    private boolean hasTargetVinOrTargetEcu(Certificate certificate) {
        return StringUtils.isNotEmpty(certificate.getTargetVIN()) || StringUtils.isNotEmpty(certificate.getTargetECU());
    }

    public List<Map.Entry<String, List<Certificate>>> getCertificatesGroupsBasedOnRolesPriority(List<Certificate> certificates, Configuration rolesConfiguration) {
        List<String> rolesPriority = Arrays.asList(rolesConfiguration.getConfigValue().split(","));
        List<Object> validRolesCertificates = this.isExtendedValidation() ? certificates.stream().filter(cert -> !cert.getUserRole().equals(UserRole.NO_ROLE.getText())).collect(Collectors.toList()) : certificates;
        Map certsGroupedByUserRoles = validRolesCertificates.stream().collect(Collectors.groupingBy(Certificate::getUserRole, Collectors.toList()));
        return certsGroupedByUserRoles.entrySet().stream().sorted((e1, e2) -> rolesPriority.indexOf(e1.getKey()) - rolesPriority.indexOf(e2.getKey())).collect(Collectors.toList());
    }

    public Certificate getCertificateByValidityAndSignatureAscending(List<Certificate> certificates) {
        return this.getCertificateByValidityAndSignature(certificates, this::getCertificatesBasedOnValidityPeriod);
    }

    public Certificate getCertificateByNewestValidFromAndSignatureAscending(List<Certificate> certificates) {
        return this.getCertificateByValidityAndSignature(certificates, this::getCertificatesBasedOnNewestValidFrom);
    }

    public UserKeyPair getUserKeyPairForCertificate(User user, Certificate certificate) {
        String METHOD_NAME = "getUserKeyPairForCertificate";
        this.logger.entering(CLASS_NAME, "getUserKeyPairForCertificate");
        this.logger.exiting(CLASS_NAME, "getUserKeyPairForCertificate");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user", user);
        params.put("certificate", certificate);
        List userKeyPairs = this.certificateRepository.findWithQuery(UserKeyPair.class, params, 0);
        if (userKeyPairs.size() == 1) return (UserKeyPair)userKeyPairs.get(0);
        throw new CertificateSigningException(this.i18n.getMessage("noKeyPairFoundForCert", new String[]{certificate.getEntityId()}));
    }

    public boolean hasPrivateKey(Certificate certificate) {
        try {
            UserKeyPair userKeyPair = this.getUserKeyPairForCertificate(this.session.getCurrentUser(), certificate);
            if (!StringUtils.isEmpty(userKeyPair.getPrivateKey())) return true;
            return false;
        }
        catch (CertificateSigningException e) {
            return false;
        }
    }

    public Optional<Certificate> findCertByUserAndSignature(User user, String signature) {
        String METHOD_NAME = "findCertByUserAndSignature";
        this.logger.entering(CLASS_NAME, "findCertByUserAndSignature");
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("signature", signature.trim());
        List certificates = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        this.logger.exiting(CLASS_NAME, "findCertByUserAndSignature");
        return certificates.stream().findAny();
    }

    public <T extends Certificate> List<T> findCertificatesByUser(Class<T> type, User user, boolean all, Pageable pageable) {
        String METHOD_NAME = "findCertificatesByUser";
        this.logger.entering(CLASS_NAME, "findCertificatesByUser");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user", user);
        if (!all) {
            params.put("state", CertificateState.ISSUED);
        }
        List certificates = this.certificateRepository.findWithPageableQuery(type, params, 0, pageable);
        this.logger.exiting(CLASS_NAME, "findCertificatesByUser");
        return certificates;
    }

    public List<Certificate> getFilteredByTargetVINTargetECU(List<Certificate> certificates, String ecu, String vin) {
        String targetVIN;
        String METHOD_NAME = "getFilteredByTargetVINTargetECU";
        this.logger.entering(CLASS_NAME, "getFilteredByTargetVINTargetECU");
        String targetECU = ecu == null ? "" : ecu;
        String string = targetVIN = vin == null ? "" : vin;
        if (targetECU.isEmpty() && targetVIN.isEmpty()) {
            return this.getCertificatesForTargetECUTargetVINWildcards(certificates);
        }
        if (targetECU.isEmpty() && !targetVIN.isEmpty()) {
            return this.getCertificatesForTargetECUWildcard(certificates, targetVIN);
        }
        if (!targetECU.isEmpty() && targetVIN.isEmpty()) {
            return this.getCertificatesForTargetVINWildcard(certificates, targetECU);
        }
        if (!targetECU.isEmpty() && !targetVIN.isEmpty()) {
            return this.getCertificatesForTargetECUAndTargetVIN(certificates, targetECU, targetVIN);
        }
        this.logger.exiting(CLASS_NAME, "getFilteredByTargetVINTargetECU");
        return new ArrayList<Certificate>();
    }

    public List<Certificate> getFilteredByTargetVINTargetECUCollectAllMataches(List<Certificate> certificates, String ecu, String vin) {
        String METHOD_NAME = "getFilteredByTargetVINTargetECU";
        this.logger.entering(CLASS_NAME, "getFilteredByTargetVINTargetECU");
        String targetECU = ecu == null ? "" : ecu;
        String targetVIN = vin == null ? "" : vin;
        ArrayList<Certificate> result = new ArrayList<Certificate>();
        if (targetECU.isEmpty() && targetVIN.isEmpty()) {
            result.addAll(this.getCertificatesForTargetECUTargetVINWildcards(certificates));
        }
        if (targetECU.isEmpty() && !targetVIN.isEmpty()) {
            result.addAll(this.getMatch(certificates, c -> c.getTargetECU().isEmpty() && c.getTargetVIN().isEmpty()));
            result.addAll(this.getMatch(certificates, c -> c.getTargetECU().isEmpty() && this.arrayContainsValue(c.getTargetVIN(), targetVIN)));
        }
        if (!targetECU.isEmpty() && targetVIN.isEmpty()) {
            result.addAll(this.getMatch(certificates, c -> c.getTargetECU().isEmpty() && c.getTargetVIN().isEmpty()));
            result.addAll(this.getMatch(certificates, c -> c.getTargetVIN().isEmpty() && this.arrayContainsValue(c.getTargetECU(), targetECU)));
        }
        if (!targetECU.isEmpty() && !targetVIN.isEmpty()) {
            result.addAll(this.getMatch(certificates, c -> c.getTargetECU().isEmpty() && c.getTargetVIN().isEmpty()));
            result.addAll(this.getMatch(certificates, c -> c.getTargetECU().isEmpty() && this.arrayContainsValue(c.getTargetVIN(), targetVIN)));
            result.addAll(this.getMatch(certificates, c -> c.getTargetVIN().isEmpty() && this.arrayContainsValue(c.getTargetECU(), targetECU)));
            result.addAll(this.getMatch(certificates, c -> this.arrayContainsValue(c.getTargetECU(), targetECU) && this.arrayContainsValue(c.getTargetVIN(), targetVIN)));
        }
        this.logger.exiting(CLASS_NAME, "getFilteredByTargetVINTargetECU");
        return result;
    }

    public <T extends Certificate> List<T> getCertificatesBasedOnValidityPeriod(List<T> certificates) {
        String METHOD_NAME = "getCertificatesBasedOnValidityPeriod";
        this.logger.entering(CLASS_NAME, "getCertificatesBasedOnValidityPeriod");
        this.logger.exiting(CLASS_NAME, "getCertificatesBasedOnValidityPeriod");
        return this.getCertificatesBasedOnValidity(certificates, Certificate::getValidTo);
    }

    public <T extends Certificate> List<T> getCertificatesBasedOnNewestValidFrom(List<T> certificates) {
        String METHOD_NAME = "getCertificatesBasedOnNewestValidFrom";
        this.logger.entering(CLASS_NAME, "getCertificatesBasedOnNewestValidFrom");
        this.logger.exiting(CLASS_NAME, "getCertificatesBasedOnNewestValidFrom");
        return this.getCertificatesBasedOnValidity(certificates, Certificate::getValidFrom);
    }

    public Optional<Certificate> findCertificate(User user, String subjectKeyIdentifier) {
        return this.findCertificate(user, subjectKeyIdentifier, CertificateType.NO_TYPE);
    }

    public Optional<Certificate> findCertificate(User user, String subjectKeyIdentifier, CertificateType certificateType) {
        return this.findCertificate(user, subjectKeyIdentifier, certificateType, Certificate.class);
    }

    public <T extends Certificate> Optional<T> findCertificate(User user, String subjectKeyIdentifier, CertificateType certificateType, Class<T> theClass) {
        String METHOD_NAME = "findCertificate";
        this.logger.entering(CLASS_NAME, "findCertificate");
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("subjectKeyIdentifier", subjectKeyIdentifier);
        if (!certificateType.equals((Object)CertificateType.NO_TYPE)) {
            params.put("type", certificateType);
        }
        List certificates = this.certificateRepository.findWithQuery(theClass, params, 0);
        this.logger.exiting(CLASS_NAME, "findCertificate");
        return certificates.stream().findAny();
    }

    public Optional<Certificate> findValidCertificate(User user, String subjectKeyIdentifier, CertificateType certificateType) {
        String METHOD_NAME = "findCertificate";
        this.logger.entering(CLASS_NAME, "findCertificate");
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("subjectKeyIdentifier", subjectKeyIdentifier);
        if (!certificateType.equals((Object)CertificateType.NO_TYPE)) {
            params.put("type", certificateType);
        }
        List repoResult = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        boolean validatePublicKeyOfParent = certificateType == CertificateType.ENHANCED_RIGHTS_CERTIFICATE;
        this.validateParent(repoResult, validatePublicKeyOfParent);
        List<Certificate> validCertificates = this.getValidRegularCertificates(repoResult);
        if (this.isUnderBackend(certificateType)) {
            validCertificates = this.getOnesWithValidPublicKeyAndPrivateKey(validCertificates);
        }
        this.logger.exiting(CLASS_NAME, "findCertificate");
        return validCertificates.stream().findAny();
    }

    public boolean isUnderBackend(CertificateType certificateType) {
        return certificateType == CertificateType.VARIANT_CODING_DEVICE_CERTIFICATE || certificateType == CertificateType.TIME_CERTIFICATE || certificateType == CertificateType.VARIANT_CODE_USER_CERTIFICATE || certificateType == CertificateType.ECU_CERTIFICATE || certificateType == CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE;
    }

    public <T extends Certificate> List<T> findCertificatesUnderParent(User user, Certificate parentCertificate, Class<T> type) {
        String METHOD_NAME = "findCertificatesUnderParent";
        this.logger.entering(CLASS_NAME, "findCertificatesUnderParent");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user", user);
        params.put("state", CertificateState.ISSUED);
        params.put("authorityKeyIdentifier", parentCertificate.getSubjectKeyIdentifier());
        params.put("parent", parentCertificate);
        List certificates = this.certificateRepository.findWithQuery(type, params, 0);
        this.logger.exiting(CLASS_NAME, "findCertificatesUnderParent");
        return certificates;
    }

    public <T extends Certificate> List<T> findCertificatesUnderParentByType(User user, String parentSubjectKeyIdentifier, CertificateType certificateType, Class<T> type) {
        String METHOD_NAME = "findCertificatesUnderParentByType";
        this.logger.entering(CLASS_NAME, "findCertificatesUnderParentByType");
        Map<String, Object> queryParams = this.getFindUnderParentQueryMap(user, parentSubjectKeyIdentifier, certificateType);
        List certificates = this.certificateRepository.findWithQuery(type, queryParams, -1);
        this.logger.exiting(CLASS_NAME, "findCertificatesUnderParentByType");
        return certificates;
    }

    public <T extends Certificate> List<T> findVsmOrNonVsmCertificatesUnderParent(User user, String parentSKI, boolean isVSM, Class<T> type) {
        String METHOD_NAME = "findVsmOrNonVsmCertificatesUnderParent";
        this.logger.entering(CLASS_NAME, "findVsmOrNonVsmCertificatesUnderParent");
        Map<String, Object> queryParams = this.getFindUnderParentQueryMap(user, parentSKI, CertificateType.ECU_CERTIFICATE);
        queryParams.put("specialECU", isVSM ? "1" : "");
        List certificates = this.certificateRepository.findWithQuery(type, queryParams, -1);
        this.logger.exiting(CLASS_NAME, "findVsmOrNonVsmCertificatesUnderParent");
        return certificates;
    }

    public <T extends Certificate> List<T> findCertificatesUnderParentByTypeAndSubject(User user, String parentSubjectKeyIdentifier, CertificateType certificateType, String subject, Class<T> type) {
        String METHOD_NAME = "findCertificatesUnderParentByTypeAndSubject";
        this.logger.entering(CLASS_NAME, "findCertificatesUnderParentByTypeAndSubject");
        Map<String, Object> queryParams = this.getFindUnderParentQueryMap(user, parentSubjectKeyIdentifier, certificateType);
        queryParams.put("subject", subject);
        List certificates = this.certificateRepository.findWithQuery(type, queryParams, -1);
        this.logger.exiting(CLASS_NAME, "findCertificatesUnderParentByTypeAndSubject");
        return certificates;
    }

    private Map<String, Object> getFindUnderParentQueryMap(User user, String parentSubjectKeyIdentifier, CertificateType certificateType) {
        HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);
        queryParams.put("type", certificateType);
        queryParams.put("authorityKeyIdentifier", parentSubjectKeyIdentifier);
        queryParams.put("state", CertificateState.ISSUED);
        return queryParams;
    }

    public List<Certificate> findValidCertificatesUnderParentByTypeAndPublicKey(User user, String parentSubjectKeyIdentifier, CertificateType certificateType, String publicKey) {
        String METHOD_NAME = "findCertificatesUnderParentByType";
        this.logger.entering(CLASS_NAME, "findCertificatesUnderParentByType");
        Map<String, Object> queryParams = this.getFindUnderParentQueryMap(user, parentSubjectKeyIdentifier, certificateType);
        if (StringUtils.isNotEmpty(publicKey)) {
            queryParams.put("subjectPublicKey", publicKey);
        }
        List repoResult = this.certificateRepository.findWithQuery(Certificate.class, queryParams, -1);
        boolean validatePublicKeyOfParent = certificateType == CertificateType.ENHANCED_RIGHTS_CERTIFICATE;
        this.validateParent(repoResult, validatePublicKeyOfParent);
        this.logger.exiting(CLASS_NAME, "findCertificatesUnderParentByType");
        List<Certificate> validCertificates = this.getValidRegularCertificates(repoResult);
        if (!this.isUnderBackend(certificateType)) return validCertificates;
        validCertificates = this.getOnesWithValidPublicKeyAndPrivateKey(validCertificates);
        return validCertificates;
    }

    public <T extends Certificate> List<T> searchCertificates(Class<T> clazz, CertificateType type) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("state", CertificateState.ISSUED);
        params.put("type", type);
        return this.certificateRepository.findWithQuery(clazz, params, 0);
    }

    public Optional<Certificate> searchCertificateById(String id, User user) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("state", CertificateState.ISSUED);
        params.put("user", user);
        params.put("entityId", id);
        List result = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        if (!result.isEmpty()) return Optional.of(result.get(0));
        return Optional.empty();
    }

    public Optional<Certificate> findCertificateBySkiAkiAndType(User user, String subjectKeyIdentifier, String authorityKeyIdentifier, CertificateType type) {
        String METHOD_NAME = "findCertificateBySkiAkiAndType";
        this.logger.entering(CLASS_NAME, "findCertificateBySkiAkiAndType");
        Map<String, Object> params = this.getFindBySkiAkiTypeQueryMap(user, subjectKeyIdentifier, authorityKeyIdentifier, type);
        List certificates = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        this.logger.exiting(CLASS_NAME, "findCertificateBySkiAkiAndType");
        return certificates.stream().findAny();
    }

    public <T extends Certificate> Optional<T> findPKIKnownCertificateBySkiAkiAndType(User user, String subjectKeyIdentifier, String authorityKeyIdentifier, CertificateType type, Class<T> clazz) {
        String METHOD_NAME = "findPKIKnownCertificateBySkiAkiAndType";
        this.logger.entering(CLASS_NAME, "findPKIKnownCertificateBySkiAkiAndType");
        Map<String, Object> params = this.getFindBySkiAkiTypeQueryMap(user, subjectKeyIdentifier, authorityKeyIdentifier, type);
        params.put("pkiKnown", Boolean.TRUE);
        List certificates = this.certificateRepository.findWithQuery(clazz, params, 0);
        this.logger.exiting(CLASS_NAME, "findPKIKnownCertificateBySkiAkiAndType");
        return certificates.stream().findAny();
    }

    public <T extends Certificate> Optional<T> findPKIKnownBackendBySki(User user, String subjectKeyIdentifier, Class<T> clazz) {
        String METHOD_NAME = "findPKIKnownBackendBySki";
        this.logger.entering(CLASS_NAME, "findPKIKnownBackendBySki");
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("subjectKeyIdentifier", subjectKeyIdentifier);
        params.put("type", CertificateType.BACKEND_CA_CERTIFICATE);
        params.put("pkiKnown", Boolean.TRUE);
        List certificates = this.certificateRepository.findWithQuery(clazz, params, 0);
        this.logger.exiting(CLASS_NAME, "findPKIKnownBackendBySki");
        return certificates.stream().findAny();
    }

    private Map<String, Object> getFindBySkiAkiTypeQueryMap(User user, String subjectKeyIdentifier, String authorityKeyIdentifier, CertificateType type) {
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("subjectKeyIdentifier", subjectKeyIdentifier);
        params.put("authorityKeyIdentifier", authorityKeyIdentifier);
        params.put("type", type);
        return params;
    }

    public Optional<Certificate> findCertificateBySkiAndType(User user, String subjectKeyIdentifier, CertificateType type) {
        String METHOD_NAME = "findCertificateBySkiAndType";
        this.logger.entering(CLASS_NAME, "findCertificateBySkiAndType");
        Map<String, Object> params = this.getFindSkiTypeQueryMap(user, subjectKeyIdentifier, type);
        List certificates = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        this.logger.exiting(CLASS_NAME, "findCertificateBySkiAndType");
        return certificates.stream().findAny();
    }

    private Map<String, Object> getFindSkiTypeQueryMap(User user, String subjectKeyIdentifier, CertificateType type) {
        Map<String, Object> params = SearchEngine.getDefaultQueryParams(user);
        params.put("subjectKeyIdentifier", subjectKeyIdentifier);
        params.put("type", type);
        return params;
    }

    public <T extends Certificate> List<T> findCertificatesBySkiAndType(User user, String subjectKeyIdentifier, CertificateType type, Class<T> clazz) {
        String METHOD_NAME = "findCertificateBySkiAndType";
        this.logger.entering(CLASS_NAME, "findCertificateBySkiAndType");
        Map<String, Object> params = this.getFindSkiTypeQueryMap(user, subjectKeyIdentifier, type);
        List certificates = this.certificateRepository.findWithQuery(clazz, params, 0);
        this.logger.exiting(CLASS_NAME, "findCertificateBySkiAndType");
        return certificates;
    }

    public List<Certificate> findCSRsUnderDiag(User user, String diagAki) {
        String METHOD_NAME = "findCSRsUnderDiag";
        this.logger.entering(CLASS_NAME, "findCSRsUnderDiag");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user", user);
        params.put("state", CertificateState.SIGNING_REQUEST);
        params.put("authorityKeyIdentifier", diagAki);
        params.put("type", CertificateType.ENHANCED_RIGHTS_CERTIFICATE);
        List certificates = this.certificateRepository.findWithQuery(Certificate.class, params, 0);
        this.logger.exiting(CLASS_NAME, "findCSRsUnderDiag");
        return certificates;
    }

    public Optional<Certificate> findDiagCertForCentralAuthentication(List<String> excludedRolesForCentralAuth, User user, String backendSubjectKeyIdentifier, String targetVIN) {
        String METHOD_NAME = "findDiagCertForCentralAuthentication";
        this.logger.entering(CLASS_NAME, "findDiagCertForCentralAuthentication");
        List repoResult = this.certificateRepository.findDiagnosticCertificateForCentralAuthentication(user, backendSubjectKeyIdentifier, excludedRolesForCentralAuth);
        this.validateParent(repoResult, false);
        List<Certificate> validCertificates = this.getValidRegularCertificates(repoResult);
        validCertificates = this.getOnesWithValidPublicKeyAndPrivateKey(validCertificates);
        List<Certificate> result = this.determineResultBasedOnRolePriorityAndTargetVIN(user, targetVIN, null, validCertificates);
        Certificate certificate = this.getCertificateByValidityAndSignatureAscending(result);
        this.logger.exiting(CLASS_NAME, "findDiagCertForCentralAuthentication");
        return Optional.ofNullable(certificate);
    }

    public Certificate findOldestCertificate(List<Certificate> certs) {
        if (certs == null) throw new CEBASException("Empty or null list cannot be evaluated");
        if (!certs.isEmpty()) return Collections.min(certs, Comparator.comparing(Certificate::getValidFrom));
        throw new CEBASException("Empty or null list cannot be evaluated");
    }

    private void validateParent(List<? extends Certificate> certificates, boolean validatePrivateKey) {
        if (!this.isExtendedValidation()) {
            return;
        }
        if (certificates == null) return;
        if (certificates.isEmpty()) return;
        if (certificates.get(0).getType() == CertificateType.ROOT_CA_CERTIFICATE) {
            return;
        }
        Certificate parentCertificate = certificates.get(0).getParent();
        if (!CertificatesValidator.isValidInChain((Certificate)parentCertificate, (MetadataManager)this.i18n, (Logger)this.logger)) {
            CertificateNotFoundException certificateNotFoundException = new CertificateNotFoundException(this.i18n.getMessage("parentDoesNotPassExtendedValidation"));
            this.logger.logWithExceptionByInfo("000480X", (CEBASException)certificateNotFoundException);
            throw certificateNotFoundException;
        }
        if (!validatePrivateKey) return;
        if (CertificatesValidator.checkPublicKeyMatchesTheOneGeneratedFromPrivateKey((Session)this.session, (Certificate)parentCertificate, (Logger)this.logger)) return;
        CertificateNotFoundException certificateNotFoundException = new CertificateNotFoundException(this.i18n.getMessage("parentDoesNotPassExtendedValidation"));
        this.logger.logWithExceptionByInfo("000481X", (CEBASException)certificateNotFoundException);
        throw certificateNotFoundException;
    }

    public List<Certificate> findIdentical(Certificate cert) {
        return this.certificateRepository.getIdentical(cert);
    }

    protected List<Map.Entry<String, List<Certificate>>> getCertificatesGroupBaseOnRolesPriority(User user, List<Certificate> certificates) {
        Configuration rolesConfiguration = ConfigurationUtil.getConfigurationForUser((User)user, (CEBASProperty)CEBASProperty.USER_ROLE, (Logger)this.logger, (MetadataManager)this.i18n);
        return this.getCertificatesGroupsBasedOnRolesPriority(certificates, rolesConfiguration);
    }

    private List<Certificate> getCertificatesForTargetECUTargetVINWildcards(List<Certificate> certificates) {
        return this.getMatch(certificates, c -> c.getTargetECU().isEmpty() && c.getTargetVIN().isEmpty());
    }

    private List<Certificate> getCertificatesForTargetECUWildcard(List<Certificate> certificates, String targetVIN) {
        List<Certificate> wildcards = this.getMatch(certificates, c -> c.getTargetECU().isEmpty() && c.getTargetVIN().isEmpty());
        if (wildcards.isEmpty()) return this.getMatch(certificates, c -> c.getTargetECU().isEmpty() && this.arrayContainsValue(c.getTargetVIN(), targetVIN));
        return wildcards;
    }

    private List<Certificate> getCertificatesForTargetVINWildcard(List<Certificate> certificates, String targetECU) {
        List<Certificate> wildcards = this.getMatch(certificates, c -> c.getTargetECU().isEmpty() && c.getTargetVIN().isEmpty());
        if (wildcards.isEmpty()) return this.getMatch(certificates, c -> c.getTargetVIN().isEmpty() && this.arrayContainsValue(c.getTargetECU(), targetECU));
        return wildcards;
    }

    private List<Certificate> getCertificatesForTargetECUAndTargetVIN(List<Certificate> certificates, String targetECU, String targetVIN) {
        List<Certificate> wildcards = this.getMatch(certificates, c -> c.getTargetECU().isEmpty() && c.getTargetVIN().isEmpty());
        if (!wildcards.isEmpty()) {
            return wildcards;
        }
        List<Certificate> wildcardECU = this.getMatch(certificates, c -> c.getTargetECU().isEmpty() && this.arrayContainsValue(c.getTargetVIN(), targetVIN));
        if (!wildcardECU.isEmpty()) {
            return wildcardECU;
        }
        List<Certificate> wildcardVIN = this.getMatch(certificates, c -> c.getTargetVIN().isEmpty() && this.arrayContainsValue(c.getTargetECU(), targetECU));
        if (!wildcardVIN.isEmpty()) {
            return wildcardVIN;
        }
        List<Certificate> exactMatch = this.getMatch(certificates, c -> this.arrayContainsValue(c.getTargetECU(), targetECU) && this.arrayContainsValue(c.getTargetVIN(), targetVIN));
        if (exactMatch.isEmpty()) return new ArrayList<Certificate>();
        return exactMatch;
    }

    private <T extends Certificate> List<T> getCertificatesBasedOnValidity(List<T> certificates, Function<Certificate, Date> function) {
        Map certsGroupedByValidityPeriod = certificates.stream().collect(Collectors.groupingBy(function::apply, Collectors.toList()));
        Optional result = certsGroupedByValidityPeriod.entrySet().stream().sorted(Map.Entry.comparingByKey().reversed()).findFirst();
        return result.isPresent() ? (List)result.get().getValue() : new ArrayList();
    }

    private Certificate getCertificateByValidityAndSignature(List<Certificate> certificates, Function<List<Certificate>, List<Certificate>> function) {
        String METHOD_NAME = "getCertificateByValidityAndSignature";
        this.logger.entering(CLASS_NAME, "getCertificateByValidityAndSignature");
        Certificate certificate = null;
        if (certificates != null) {
            List<Certificate> resultByValidity = function.apply(certificates);
            if (resultByValidity.size() == 1) {
                certificate = resultByValidity.get(0);
            } else {
                Optional<Certificate> certificateOptional = resultByValidity.stream().sorted(Comparator.comparing(Certificate::getSignature)).findFirst();
                if (certificateOptional.isPresent()) {
                    certificate = certificateOptional.get();
                }
            }
        }
        this.logger.exiting(CLASS_NAME, "getCertificateByValidityAndSignature");
        return certificate;
    }

    private List<Certificate> getMatch(List<Certificate> certificates, Predicate<Certificate> predicate) {
        return certificates.stream().filter(predicate).collect(Collectors.toList());
    }

    private List<Certificate> findEnhancedUnderDiag(String serialNo, List<Certificate> repoResult) {
        return repoResult.stream().filter(c -> c.getIssuer().equals(c.getParent().getIssuer()) && c.getIssuer().equals(c.getParent().getParent().getSubject()) && c.getAuthorityKeyIdentifier().equals(c.getParent().getAuthorityKeyIdentifier()) && c.getAuthorityKeyIdentifier().equals(c.getParent().getParent().getSubjectKeyIdentifier()) && !c.isSecOCISCert() && c.getParent().getSerialNo().trim().equals(serialNo)).collect(Collectors.toList());
    }

    private CertificateMoreResultsFoundException logAndThrowMoreDiagOrTimeCertFound() {
        CertificateMoreResultsFoundException ex = new CertificateMoreResultsFoundException(this.i18n.getMessage("moreDiagCertFoundForCombo"), "moreDiagCertFoundForCombo");
        this.logger.logWithTranslation(Level.WARNING, "000143X", ex.getMessageId(), ex.getClass().getSimpleName());
        return ex;
    }

    private boolean arrayContainsValue(String source, String value) {
        String[] array;
        if (source.equals(value)) {
            return true;
        }
        String[] stringArray = array = source.split(", ");
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String current = stringArray[n2];
            if (current.equals(value)) {
                return true;
            }
            ++n2;
        }
        return false;
    }
}
