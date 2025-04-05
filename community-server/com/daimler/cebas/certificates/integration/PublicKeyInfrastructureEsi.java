/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.PKCS12Manager
 *  com.daimler.cebas.certificates.control.exceptions.CertificatesUpdateException
 *  com.daimler.cebas.certificates.control.exceptions.Custom9xxHttpException
 *  com.daimler.cebas.certificates.control.exceptions.InvalidTokenException
 *  com.daimler.cebas.certificates.control.exceptions.PkiPayloadParsingException
 *  com.daimler.cebas.certificates.control.update.DefaultUpdateSession
 *  com.daimler.cebas.certificates.integration.QueryParamFactory
 *  com.daimler.cebas.certificates.integration.vo.BackendIdentifier
 *  com.daimler.cebas.certificates.integration.vo.NonVsmCertificateRequest
 *  com.daimler.cebas.certificates.integration.vo.PKICertificateRequest
 *  com.daimler.cebas.certificates.integration.vo.PKICertificateResponse
 *  com.daimler.cebas.certificates.integration.vo.PKIEcuCertificateResponse
 *  com.daimler.cebas.certificates.integration.vo.PKIEnhancedCertificateRequest
 *  com.daimler.cebas.certificates.integration.vo.Permission
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASService
 *  com.daimler.cebas.common.control.annotations.ObjectAdapterPattern
 *  com.daimler.cebas.common.control.http.HttpEntityFactory
 *  com.daimler.cebas.configuration.control.util.PkiPropertiesManager
 *  com.daimler.cebas.logs.control.Logger
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.http.HttpEntity
 *  org.springframework.http.HttpMethod
 *  org.springframework.http.MediaType
 *  org.springframework.http.ResponseEntity
 *  org.springframework.util.MultiValueMap
 *  org.springframework.web.client.HttpStatusCodeException
 *  org.springframework.web.client.ResourceAccessException
 *  org.springframework.web.client.RestTemplate
 *  org.springframework.web.util.UriComponentsBuilder
 */
package com.daimler.cebas.certificates.integration;

import com.daimler.cebas.certificates.control.PKCS12Manager;
import com.daimler.cebas.certificates.control.exceptions.CertificatesUpdateException;
import com.daimler.cebas.certificates.control.exceptions.Custom9xxHttpException;
import com.daimler.cebas.certificates.control.exceptions.InvalidTokenException;
import com.daimler.cebas.certificates.control.exceptions.PkiPayloadParsingException;
import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
import com.daimler.cebas.certificates.integration.QueryParamFactory;
import com.daimler.cebas.certificates.integration.vo.BackendIdentifier;
import com.daimler.cebas.certificates.integration.vo.NonVsmCertificateRequest;
import com.daimler.cebas.certificates.integration.vo.PKICertificateRequest;
import com.daimler.cebas.certificates.integration.vo.PKICertificateResponse;
import com.daimler.cebas.certificates.integration.vo.PKIEcuCertificateResponse;
import com.daimler.cebas.certificates.integration.vo.PKIEnhancedCertificateRequest;
import com.daimler.cebas.certificates.integration.vo.Permission;
import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASService;
import com.daimler.cebas.common.control.annotations.ObjectAdapterPattern;
import com.daimler.cebas.common.control.http.HttpEntityFactory;
import com.daimler.cebas.configuration.control.util.PkiPropertiesManager;
import com.daimler.cebas.logs.control.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@CEBASService
@ObjectAdapterPattern
public abstract class PublicKeyInfrastructureEsi {
    @Value(value="${feature.certificate.chain.exchange:false}")
    private boolean certificateChainFeatureEnabled;
    protected static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(PKCS12Manager.class.getSimpleName());
    private static final String CLASS_NAME = PublicKeyInfrastructureEsi.class.getName();
    protected static final String SPACE = " ";
    protected static final String PKI_REQUEST_FAILED_PAYLOAD = "PKI Request failed. Payload: ";
    protected static final String INVALID_AUTHORIZATION_ERROR = "Invalid Authorization token ";
    public static final String INVALID_TOKEN_ERROR = "Invalid token";
    private static final int MAXIMUM_REQ_RESP_LENGHT = 2000;
    private static final String EMPTY_PAYLOAD = "Empty payload";
    protected RestTemplate restTemplate;
    protected Logger logger;
    protected MetadataManager i18n;
    protected DefaultUpdateSession updateSession;

    @Autowired
    public PublicKeyInfrastructureEsi(RestTemplate restTemplate, Logger logger, MetadataManager i18n, DefaultUpdateSession updateSession) {
        this.restTemplate = restTemplate;
        this.logger = logger;
        this.i18n = i18n;
        this.updateSession = updateSession;
    }

    public List<BackendIdentifier> getBackendIdentifiers() {
        return this.getBackendIdentifiersInternal(true, null);
    }

    public List<BackendIdentifier> getPreactiveBackendIdentifiers(boolean onlyDuringUpdateSession, String preactiveCaId) {
        return this.getBackendIdentifiersInternal(onlyDuringUpdateSession, preactiveCaId);
    }

    public List<BackendIdentifier> getBackendIdentifiersWithoutSession() {
        return this.getBackendIdentifiersInternal(false, null);
    }

    private List<BackendIdentifier> getBackendIdentifiersInternal(boolean onlyDuringUpdateSession, String preactiveCaId) {
        String pkiBackendIdentifiersUrl = this.getPkiBackendIdentifiersUrl();
        try {
            if (!onlyDuringUpdateSession) return this.makeRequestWithoutSessionRunning(pkiBackendIdentifiersUrl, HttpMethod.GET, BackendIdentifier[].class, (MultiValueMap<String, String>)QueryParamFactory.createRootAndBackendIdentifierParam((String)preactiveCaId));
            return this.makeRequest(pkiBackendIdentifiersUrl, HttpMethod.GET, BackendIdentifier[].class, (MultiValueMap<String, String>)QueryParamFactory.createRootAndBackendIdentifierParam((String)preactiveCaId));
        }
        catch (HttpStatusCodeException httpEx) {
            LOG.log(Level.FINEST, httpEx.getMessage(), httpEx);
            String message = this.getErrorMessageFromHttpExceptionBody(this.getExceptionMessage(httpEx));
            this.handleHttpErrorCodeLogger(pkiBackendIdentifiersUrl, null, httpEx.getRawStatusCode(), httpEx.getResponseBodyAsString());
            if (this.isUnauthorized(httpEx)) {
                throw new InvalidTokenException(INVALID_TOKEN_ERROR);
            }
            this.logger.log(Level.INFO, "000575", message, this.getClass().getSimpleName());
            throw new CertificatesUpdateException(message);
        }
        catch (ResourceAccessException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            this.logCannotAccess(e, pkiBackendIdentifiersUrl);
            if (!this.updateSession.isRunning()) return Collections.emptyList();
            if (this.updateSession.getCurrentRetry() < this.updateSession.getMaxRetries()) {
                this.waitForNextRetry(pkiBackendIdentifiersUrl);
                return this.getBackendIdentifiersInternal(onlyDuringUpdateSession, preactiveCaId);
            }
            this.updateSession.setDidFailAllRetries(true);
            String errorPayload = e.getMessage();
            this.logger.log(Level.INFO, "000576", errorPayload, this.getClass().getSimpleName());
            throw new CertificatesUpdateException(errorPayload);
        }
    }

    public Set<String> getCertificatesChain(List<BackendIdentifier> backendIdentifiers) {
        String pkiCertificatesChainUrl = this.getPkiPropertiesManager().getPkiCertificatesChainUrl();
        try {
            return backendIdentifiers.stream().map(backendIdentifier -> this.makeRequest(pkiCertificatesChainUrl, HttpMethod.GET, String[].class, (MultiValueMap<String, String>)QueryParamFactory.createCertChainParam((String)backendIdentifier.getSubjectKeyIdentifier()))).flatMap(Collection::stream).collect(Collectors.toSet());
        }
        catch (HttpStatusCodeException httpEx) {
            LOG.log(Level.FINEST, httpEx.getMessage(), httpEx);
            this.handleHttpErrorCodeLogger(pkiCertificatesChainUrl, null, httpEx.getRawStatusCode(), httpEx.getResponseBodyAsString());
            if (this.isUnauthorized(httpEx)) {
                throw new InvalidTokenException(INVALID_TOKEN_ERROR);
            }
            String message = this.getErrorMessageFromHttpExceptionBody(this.getExceptionMessage(httpEx));
            this.logger.log(Level.INFO, "000577", message, this.getClass().getSimpleName());
            throw new CertificatesUpdateException(message);
        }
        catch (ResourceAccessException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            this.logCannotAccess(e, pkiCertificatesChainUrl);
            if (!this.updateSession.isRunning()) return Collections.emptySet();
            if (this.updateSession.getCurrentRetry() < this.updateSession.getMaxRetries()) {
                this.waitForNextRetry(pkiCertificatesChainUrl);
                return this.getCertificatesChain(backendIdentifiers);
            }
            this.updateSession.setDidFailAllRetries(true);
            throw new CertificatesUpdateException(e.getMessage());
        }
    }

    public List<Permission> getPermissions() {
        String pkiPermissionsUrl = this.getPkiPropertiesManager().getPkiPermissionsUrl();
        try {
            return this.makeRequest(pkiPermissionsUrl, HttpMethod.GET, Permission[].class);
        }
        catch (HttpStatusCodeException httpEx) {
            LOG.log(Level.FINEST, httpEx.getMessage(), httpEx);
            this.handleHttpErrorCodeLogger(pkiPermissionsUrl, null, httpEx.getRawStatusCode(), httpEx.getResponseBodyAsString());
            if (this.isUnauthorized(httpEx)) {
                throw new InvalidTokenException(INVALID_TOKEN_ERROR);
            }
            String message = this.getErrorMessageFromHttpExceptionBody(this.getExceptionMessage(httpEx));
            throw new CertificatesUpdateException(message);
        }
        catch (ResourceAccessException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            this.logCannotAccess(e, pkiPermissionsUrl);
            if (!this.updateSession.isRunning()) return Collections.emptyList();
            if (this.updateSession.getCurrentRetry() < this.updateSession.getMaxRetries()) {
                this.waitForNextRetry(pkiPermissionsUrl);
                return this.getPermissions();
            }
            this.updateSession.setDidFailAllRetries(true);
            throw new CertificatesUpdateException(e.getMessage());
        }
    }

    public abstract List<PKICertificateResponse> downloadCertificates(List<PKICertificateRequest> var1);

    public abstract List<PKIEcuCertificateResponse> downloadNonVsmCertificates(List<NonVsmCertificateRequest> var1, boolean var2);

    public abstract List<PKICertificateResponse> downloadEnhancedRightsCertificates(List<PKIEnhancedCertificateRequest> var1, boolean var2);

    protected <T> List<T> makeRequest(String url, HttpMethod method, Class<T[]> setType) {
        if (!this.updateSession.isRunning()) return Collections.emptyList();
        ResponseEntity pkiResponse = this.restTemplate.exchange(url, method, HttpEntityFactory.buildDefaultPKIRequestEntity((String)this.getToken()), String.class, new Object[0]);
        return this.deserializePKIResponse((ResponseEntity<String>)pkiResponse, setType);
    }

    protected <T> List<T> makeRequest(String url, HttpMethod method, Class<T[]> setType, MultiValueMap<String, String> queryParams) {
        if (!this.updateSession.isRunning()) return Collections.emptyList();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString((String)url).queryParams(queryParams);
        ResponseEntity pkiResponse = this.restTemplate.exchange(builder.build().encode().toUri(), method, HttpEntityFactory.buildDefaultPKIRequestEntity((String)this.getToken()), String.class);
        return this.deserializePKIResponse((ResponseEntity<String>)pkiResponse, setType);
    }

    protected <T> List<T> makeRequest(String url, HttpMethod method, Class<T[]> setType, String requestJson) {
        return this.makeRequest(url, method, setType, requestJson, MediaType.APPLICATION_JSON);
    }

    protected <T> List<T> makeRequest(String url, HttpMethod method, Class<T[]> setType, String requestJson, MediaType mediaType) {
        if (!this.updateSession.isRunning()) return Collections.emptyList();
        HttpEntity httpEntity = HttpEntityFactory.buildDefaultPKIRequestEntity((String)this.getToken(), (MediaType)mediaType, (String)requestJson);
        ResponseEntity pkiResponse = this.restTemplate.exchange(url, method, httpEntity, String.class, new Object[0]);
        return this.deserializePKIResponse((ResponseEntity<String>)pkiResponse, setType);
    }

    protected <T> List<T> makeRequestWithoutSessionRunning(String url, HttpMethod method, Class<T[]> setType) {
        ResponseEntity pkiResponse = this.restTemplate.exchange(url, method, HttpEntityFactory.buildDefaultPKIRequestEntity((String)this.getToken()), String.class, new Object[0]);
        return this.deserializePKIResponse((ResponseEntity<String>)pkiResponse, setType);
    }

    protected <T> List<T> makeRequestWithoutSessionRunning(String url, HttpMethod method, Class<T[]> setType, String requestJson) {
        return this.makeRequestWithoutSessionRunning(url, method, setType, requestJson, MediaType.APPLICATION_JSON);
    }

    protected <T> List<T> makeRequestWithoutSessionRunning(String url, HttpMethod method, Class<T[]> setType, String requestJson, MediaType mediaType) {
        HttpEntity httpEntity = HttpEntityFactory.buildDefaultPKIRequestEntity((String)this.getToken(), (MediaType)mediaType, (String)requestJson);
        ResponseEntity pkiResponse = this.restTemplate.exchange(url, method, httpEntity, String.class, new Object[0]);
        return this.deserializePKIResponse((ResponseEntity<String>)pkiResponse, setType);
    }

    protected <T> List<T> makeRequestWithoutSessionRunning(String url, HttpMethod method, Class<T[]> setType, MultiValueMap<String, String> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString((String)url).queryParams(queryParams);
        ResponseEntity pkiResponse = this.restTemplate.exchange(builder.build().encode().toUri(), method, HttpEntityFactory.buildDefaultPKIRequestEntity((String)this.getToken()), String.class);
        return this.deserializePKIResponse((ResponseEntity<String>)pkiResponse, setType);
    }

    private <T> List<T> deserializePKIResponse(ResponseEntity<String> pkiResponse, Class<T[]> setType) throws Custom9xxHttpException, PkiPayloadParsingException {
        if (this.is9xxStatusCode(pkiResponse.getStatusCodeValue())) {
            throw new Custom9xxHttpException(pkiResponse.getStatusCodeValue(), (String)pkiResponse.getBody());
        }
        if (null == pkiResponse.getBody()) return new ArrayList();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object[] body = (Object[])objectMapper.readValue((String)pkiResponse.getBody(), setType);
            return new ArrayList<Object>(Arrays.asList(body));
        }
        catch (JsonProcessingException e) {
            throw new PkiPayloadParsingException(pkiResponse.getStatusCode(), pkiResponse);
        }
    }

    protected void waitForNextRetry(String url) {
        if (!this.updateSession.isRunning()) return;
        Integer currentRetry = this.updateSession.retry(url);
        this.updateSession.setNextRetryTimeInterval();
        Integer nextRetryIntervalTime = this.updateSession.getNextRetryTimeInterval();
        this.logger.log(Level.INFO, "000234", "Retry no. " + currentRetry + " will be performed in " + nextRetryIntervalTime + " seconds for endpoint: " + url, CLASS_NAME);
        try {
            TimeUnit.MILLISECONDS.sleep((long)nextRetryIntervalTime.intValue() * 1000L);
        }
        catch (InterruptedException e) {
            CertificatesUpdateException exception = new CertificatesUpdateException(e.getMessage());
            this.logger.logWithException("000313X", "Task interrupted " + e.getMessage(), (CEBASException)exception);
            Thread.currentThread().interrupt();
            throw exception;
        }
    }

    public void handleHttpErrorCodeLogger(String url, HttpEntity<?> httpEntity, int httpStatus, String errorResponseMessage) {
        String requestLogMessage = "Error in PKI request on url: " + url + (httpEntity != null ? " : with payload: " + this.getTrunkOfMaximumChs(httpEntity.toString()) : "");
        String responseLogMessage = "Error PKI response message: " + this.getTrunkOfMaximumChs(errorResponseMessage) + " with HttpStatus: " + httpStatus;
        this.logger.log(Level.INFO, "000628X", requestLogMessage, this.getClass().getSimpleName());
        this.logger.log(Level.INFO, "000646X", responseLogMessage, this.getClass().getSimpleName());
    }

    private String getTrunkOfMaximumChs(String message) {
        if (StringUtils.isEmpty(message)) {
            return EMPTY_PAYLOAD;
        }
        String msg = message.length() > 2000 ? message.substring(0, 2000) + "[...]" : message;
        return msg.replace("\n", "").replace("\r", "");
    }

    protected abstract String getToken();

    protected abstract PkiPropertiesManager getPkiPropertiesManager();

    protected void logCannotAccess(ResourceAccessException e, String url) {
        String trimmedMessage = e.getCause() != null ? SPACE + e.getCause().getMessage() : "";
        this.logger.logWithException("000312X", "Exception while accessing PKI URI " + url + trimmedMessage, (CEBASException)new CertificatesUpdateException(e.getMessage()));
        this.logger.logToFileOnly(this.getClass().getSimpleName(), e.getMessage(), (Throwable)e);
    }

    protected boolean isUnauthorized(HttpStatusCodeException httpEx) {
        return httpEx.getRawStatusCode() == 401 || httpEx.getResponseBodyAsString().contains(INVALID_TOKEN_ERROR) || httpEx.getResponseBodyAsString().contains(INVALID_AUTHORIZATION_ERROR);
    }

    private boolean is9xxStatusCode(int statusCodeValue) {
        return statusCodeValue >= 900;
    }

    private String getErrorMessageFromHttpExceptionBody(String jsonText) {
        if (StringUtils.isEmpty(jsonText)) {
            return jsonText;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObj = mapper.readTree(jsonText);
            JsonNode messageNode = jsonObj.get("message");
            if (messageNode != null) return messageNode.textValue();
            return "Invalid error payload received: " + jsonText.replace("\n", "").replace("\r", "");
        }
        catch (IOException parseException) {
            return jsonText;
        }
    }

    private String getExceptionMessage(HttpStatusCodeException httpEx) {
        String exceptionMessage = httpEx.getResponseBodyAsString();
        if (!StringUtils.isEmpty(exceptionMessage)) return exceptionMessage;
        if (StringUtils.isEmpty(httpEx.getStatusText())) {
            return "";
        }
        exceptionMessage = httpEx.getStatusText();
        return exceptionMessage;
    }

    private String getPkiBackendIdentifiersUrl() {
        if (!this.certificateChainFeatureEnabled) return this.getPkiPropertiesManager().getPkiBackendIdentifiersUrl();
        return this.getPkiPropertiesManager().getPkiBackendIdentifiersUrlV2();
    }
}
