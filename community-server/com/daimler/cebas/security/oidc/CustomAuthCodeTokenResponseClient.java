/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.core.convert.converter.Converter
 *  org.springframework.http.RequestEntity
 *  org.springframework.http.ResponseEntity
 *  org.springframework.http.converter.FormHttpMessageConverter
 *  org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient
 *  org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
 *  org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter
 *  org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler
 *  org.springframework.security.oauth2.core.OAuth2AuthorizationException
 *  org.springframework.security.oauth2.core.OAuth2Error
 *  org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse
 *  org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter
 *  org.springframework.stereotype.Component
 *  org.springframework.util.Assert
 *  org.springframework.util.CollectionUtils
 *  org.springframework.web.client.ResponseErrorHandler
 *  org.springframework.web.client.RestOperations
 *  org.springframework.web.client.RestTemplate
 */
package com.daimler.cebas.security.oidc;

import java.util.Arrays;
import java.util.Collection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomAuthCodeTokenResponseClient
implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
    private Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> requestEntityConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
    private RestOperations restOperations;

    public CustomAuthCodeTokenResponseClient() {
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), new OAuth2AccessTokenResponseHttpMessageConverter()));
        restTemplate.setErrorHandler((ResponseErrorHandler)new OAuth2ErrorResponseErrorHandler());
        this.restOperations = restTemplate;
    }

    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        ResponseEntity response;
        Assert.notNull((Object)authorizationCodeGrantRequest, (String)"authorizationCodeGrantRequest cannot be null");
        RequestEntity request = (RequestEntity)this.requestEntityConverter.convert((Object)authorizationCodeGrantRequest);
        try {
            response = this.restOperations.exchange(request, OAuth2AccessTokenResponse.class);
        }
        catch (Exception ex) {
            OAuth2Error oauth2Error = new OAuth2Error("invalid_token_response", "An error occurred while attempting to retrieve the OAuth 2.0 Access Token Response: " + ex.getMessage(), null);
            throw new OAuth2AuthorizationException(oauth2Error, (Throwable)ex);
        }
        OAuth2AccessTokenResponse tokenResponse = (OAuth2AccessTokenResponse)response.getBody();
        if (!CollectionUtils.isEmpty((Collection)tokenResponse.getAccessToken().getScopes())) return tokenResponse;
        tokenResponse = OAuth2AccessTokenResponse.withResponse((OAuth2AccessTokenResponse)tokenResponse).scopes(authorizationCodeGrantRequest.getClientRegistration().getScopes()).build();
        return tokenResponse;
    }

    public void setRequestEntityConverter(Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> requestEntityConverter) {
        Assert.notNull(requestEntityConverter, (String)"requestEntityConverter cannot be null");
        this.requestEntityConverter = requestEntityConverter;
    }

    public void setRestOperations(RestOperations restOperations) {
        Assert.notNull((Object)restOperations, (String)"restOperations cannot be null");
        this.restOperations = restOperations;
    }
}
