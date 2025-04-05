/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.system.control.startup.CeBASStartupProperty
 */
package com.daimler.cebas.configuration.control;

import com.daimler.cebas.system.control.startup.CeBASStartupProperty;

public class OidcConstants {
    public static final String OIDC_CONTEXT = "gas";
    public static final String CLIENT_ID = CeBASStartupProperty.SECURITY_OAUTH2_CLIENT_CLIENT_ID.getProperty();
    public static final String PROVIDER = "spring.security.oauth2.client.registration.gas.provider";
    public static final String REDIRECT_URI = "spring.security.oauth2.client.registration.gas.redirect-uri";
    public static final String REVOKE_TOKEN_URI = "security.revoke-token-uri";
    public static final String TEST_CLIENT_ID = CeBASStartupProperty.TEST_OIDC_CLIENT_ID.getProperty();
    public static final String TEST_PROVIDER = "test.spring.security.oauth2.client.registration.gas.provider";
    public static final String TEST_REDIRECT_URI = "test.spring.security.oauth2.client.registration.gas.redirect-uri";
    public static final String TEST_REVOKE_TOKEN_URI = "test.security.revoke-token-uri";
    public static final String TEST_CLIENT_ACCESS_TOKEN_URI = "spring.security.oauth2.client.provider.TEST.token-uri";
    public static final String TEST_CLIENT_USER_AUTHORIZATION_URI = "spring.security.oauth2.client.provider.TEST.authorization-uri";
    public static final String TEST_RESOURCE_USER_INFO_URI = "spring.security.oauth2.client.provider.TEST.user-info-uri";
    public static final String TEST_RESOURCE_JWK_URI = "spring.security.oauth2.client.provider.TEST.jwk-set-uri";
    public static final String PROD_CLIENT_ID = CeBASStartupProperty.PROD_OIDC_CLIENT_ID.getProperty();
    public static final String PROD_PROVIDER = "prod.spring.security.oauth2.client.registration.gas.provider";
    public static final String PROD_REDIRECT_URI = "prod.spring.security.oauth2.client.registration.gas.redirect-uri";
    public static final String PROD_REVOKE_TOKEN_URI = "prod.security.revoke-token-uri";
    public static final String PROD_CLIENT_ACCESS_TOKEN_URI = "spring.security.oauth2.client.provider.PROD.token-uri";
    public static final String PROD_CLIENT_USER_AUTHORIZATION_URI = "spring.security.oauth2.client.provider.PROD.authorization-uri";
    public static final String PROD_RESOURCE_USER_INFO_URI = "spring.security.oauth2.client.provider.PROD.user-info-uri";
    public static final String PROD_RESOURCE_JWK_URI = "spring.security.oauth2.client.provider.PROD.jwk-set-uri";
}
