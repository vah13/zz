/*    */ package com.daimler.cebas.security.oidc;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.springframework.security.core.Authentication;
/*    */ import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
/*    */ import org.springframework.security.oauth2.client.OAuth2AuthorizedClientId;
/*    */ import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
/*    */ import org.springframework.security.oauth2.client.registration.ClientRegistration;
/*    */ import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ public final class CebasOAuth2AuthorizedClientService
/*    */   implements OAuth2AuthorizedClientService
/*    */ {
/*    */   private final Map<OAuth2AuthorizedClientId, OAuth2AuthorizedClient> authorizedClients;
/*    */   private final ClientRegistrationRepository clientRegistrationRepository;
/*    */   
/*    */   public CebasOAuth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
/* 21 */     Assert.notNull(clientRegistrationRepository, "clientRegistrationRepository cannot be null");
/* 22 */     this.clientRegistrationRepository = clientRegistrationRepository;
/* 23 */     this.authorizedClients = new ConcurrentHashMap<>();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
/* 29 */     Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
/* 30 */     Assert.hasText(principalName, "principalName cannot be empty");
/* 31 */     ClientRegistration registration = this.clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
/* 32 */     if (registration == null) {
/* 33 */       return null;
/*    */     }
/* 35 */     return (T)this.authorizedClients.get(new OAuth2AuthorizedClientId(clientRegistrationId, principalName.toUpperCase(Locale.ENGLISH)));
/*    */   }
/*    */ 
/*    */   
/*    */   public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
/* 40 */     Assert.notNull(authorizedClient, "authorizedClient cannot be null");
/* 41 */     Assert.notNull(principal, "principal cannot be null");
/* 42 */     this.authorizedClients
/* 43 */       .put(new OAuth2AuthorizedClientId(authorizedClient.getClientRegistration().getRegistrationId(), principal
/* 44 */           .getName().toUpperCase(Locale.ENGLISH)), authorizedClient);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
/* 49 */     Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
/* 50 */     Assert.hasText(principalName, "principalName cannot be empty");
/* 51 */     ClientRegistration registration = this.clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
/* 52 */     if (registration != null)
/* 53 */       this.authorizedClients.remove(new OAuth2AuthorizedClientId(clientRegistrationId, principalName
/* 54 */             .toUpperCase(Locale.ENGLISH))); 
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\security\oidc\CebasOAuth2AuthorizedClientService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */