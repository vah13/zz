/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.security.core.Authentication
 *  org.springframework.security.oauth2.client.OAuth2AuthorizedClient
 *  org.springframework.security.oauth2.client.OAuth2AuthorizedClientId
 *  org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
 *  org.springframework.security.oauth2.client.registration.ClientRegistration
 *  org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
 *  org.springframework.util.Assert
 */
package com.daimler.cebas.security.oidc;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientId;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.Assert;

public final class CebasOAuth2AuthorizedClientService
implements OAuth2AuthorizedClientService {
    private final Map<OAuth2AuthorizedClientId, OAuth2AuthorizedClient> authorizedClients;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public CebasOAuth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
        Assert.notNull((Object)clientRegistrationRepository, (String)"clientRegistrationRepository cannot be null");
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizedClients = new ConcurrentHashMap<OAuth2AuthorizedClientId, OAuth2AuthorizedClient>();
    }

    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
        Assert.hasText((String)clientRegistrationId, (String)"clientRegistrationId cannot be empty");
        Assert.hasText((String)principalName, (String)"principalName cannot be empty");
        ClientRegistration registration = this.clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
        if (registration != null) return (T)this.authorizedClients.get(new OAuth2AuthorizedClientId(clientRegistrationId, principalName.toUpperCase(Locale.ENGLISH)));
        return null;
    }

    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        Assert.notNull((Object)authorizedClient, (String)"authorizedClient cannot be null");
        Assert.notNull((Object)principal, (String)"principal cannot be null");
        this.authorizedClients.put(new OAuth2AuthorizedClientId(authorizedClient.getClientRegistration().getRegistrationId(), principal.getName().toUpperCase(Locale.ENGLISH)), authorizedClient);
    }

    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        Assert.hasText((String)clientRegistrationId, (String)"clientRegistrationId cannot be empty");
        Assert.hasText((String)principalName, (String)"principalName cannot be empty");
        ClientRegistration registration = this.clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
        if (registration == null) return;
        this.authorizedClients.remove(new OAuth2AuthorizedClientId(clientRegistrationId, principalName.toUpperCase(Locale.ENGLISH)));
    }
}
