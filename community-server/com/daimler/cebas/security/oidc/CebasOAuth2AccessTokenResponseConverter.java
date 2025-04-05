/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.users.control.Session
 *  org.springframework.core.convert.converter.Converter
 *  org.springframework.security.oauth2.core.OAuth2AccessToken$TokenType
 *  org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse
 *  org.springframework.util.StringUtils
 */
package com.daimler.cebas.security.oidc;

import com.daimler.cebas.users.control.Session;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.util.StringUtils;

public class CebasOAuth2AccessTokenResponseConverter
implements Converter<Map<String, String>, OAuth2AccessTokenResponse> {
    private static final Logger LOGGER = Logger.getLogger(CebasOAuth2AccessTokenResponseConverter.class.getName());
    private static final Set<String> TOKEN_RESPONSE_PARAMETER_NAMES = new HashSet<String>(Arrays.asList("access_token", "token_type", "expires_in", "refresh_token", "scope"));
    private Session session;

    public CebasOAuth2AccessTokenResponseConverter(Session session) {
        this.session = session;
    }

    public OAuth2AccessTokenResponse convert(Map<String, String> tokenResponseParameters) {
        String accessToken = tokenResponseParameters.get("access_token");
        OAuth2AccessToken.TokenType accessTokenType = null;
        if (OAuth2AccessToken.TokenType.BEARER.getValue().equalsIgnoreCase(tokenResponseParameters.get("token_type"))) {
            accessTokenType = OAuth2AccessToken.TokenType.BEARER;
        }
        long expiresIn = 0L;
        if (tokenResponseParameters.containsKey("expires_in")) {
            try {
                expiresIn = Long.parseLong(tokenResponseParameters.get("expires_in"));
            }
            catch (NumberFormatException ex) {
                LOGGER.warning("000001X - Parsing failed. Reason: " + ex.getMessage());
            }
        }
        Set scopes = Collections.emptySet();
        if (tokenResponseParameters.containsKey("scope")) {
            String scope = tokenResponseParameters.get("scope");
            scopes = new HashSet<String>(Arrays.asList(StringUtils.delimitedListToStringArray((String)scope, (String)" ")));
        }
        String refreshToken = tokenResponseParameters.get("refresh_token");
        this.session.setRefreshToken(refreshToken);
        LinkedHashMap<String, String> additionalParameters = new LinkedHashMap<String, String>();
        Iterator<Map.Entry<String, String>> iterator = tokenResponseParameters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (TOKEN_RESPONSE_PARAMETER_NAMES.contains(entry.getKey())) continue;
            additionalParameters.put(entry.getKey(), entry.getValue());
        }
        return OAuth2AccessTokenResponse.withToken((String)accessToken).tokenType(accessTokenType).expiresIn(expiresIn).scopes(scopes).additionalParameters(additionalParameters).build();
    }
}
