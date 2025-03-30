/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.security;
/*    */ 
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.security.MessageDigest;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.util.Base64;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
/*    */ import org.springframework.security.crypto.keygen.StringKeyGenerator;
/*    */ import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
/*    */ import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
/*    */ import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
/*    */ import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ZenzefiAuthRequestResolver
/*    */   implements OAuth2AuthorizationRequestResolver
/*    */ {
/*    */   private static final String CODE_CHALLENGE_METHOD = "S256";
/*    */   private static final String HASH_MESSAGE_DIGEST = "SHA-256";
/*    */   private static final int PKCE_KEY_LENGTH = 96;
/*    */   private OAuth2AuthorizationRequestResolver defaultResolver;
/* 33 */   private final StringKeyGenerator secureKeyGenerator = (StringKeyGenerator)new Base64StringKeyGenerator(
/* 34 */       Base64.getUrlEncoder().withoutPadding(), 96);
/*    */   
/*    */   public ZenzefiAuthRequestResolver(ClientRegistrationRepository repo, String authorizationRequestBaseUri) {
/* 37 */     this.defaultResolver = (OAuth2AuthorizationRequestResolver)new DefaultOAuth2AuthorizationRequestResolver(repo, authorizationRequestBaseUri);
/*    */   }
/*    */ 
/*    */   
/*    */   public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
/* 42 */     OAuth2AuthorizationRequest req = this.defaultResolver.resolve(request);
/* 43 */     if (req != null) {
/* 44 */       req = customizeAuthorizationRequest(req);
/*    */     }
/* 46 */     return req;
/*    */   }
/*    */ 
/*    */   
/*    */   public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
/* 51 */     OAuth2AuthorizationRequest req = this.defaultResolver.resolve(request, clientRegistrationId);
/* 52 */     if (req != null) {
/* 53 */       req = customizeAuthorizationRequest(req);
/*    */     }
/* 55 */     return req;
/*    */   }
/*    */   
/*    */   private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest req) {
/* 59 */     if (req == null) {
/* 60 */       return null;
/*    */     }
/*    */     
/* 63 */     Map<String, Object> attributes = new HashMap<>(req.getAttributes());
/* 64 */     Map<String, Object> additionalParameters = new HashMap<>(req.getAdditionalParameters());
/* 65 */     addPkceParameters(attributes, additionalParameters);
/* 66 */     addAcrValues(attributes, additionalParameters);
/*    */     
/* 68 */     return OAuth2AuthorizationRequest.from(req).attributes(attributes).additionalParameters(additionalParameters)
/* 69 */       .build();
/*    */   }
/*    */   
/*    */   private void addPkceParameters(Map<String, Object> attributes, Map<String, Object> additionalParameters) {
/* 73 */     String codeVerifier = this.secureKeyGenerator.generateKey();
/* 74 */     attributes.put("code_verifier", codeVerifier);
/*    */     try {
/* 76 */       String codeChallenge = createHash(codeVerifier);
/* 77 */       additionalParameters.put("code_challenge", codeChallenge);
/* 78 */       additionalParameters.put("code_challenge_method", "S256");
/* 79 */     } catch (NoSuchAlgorithmException e) {
/* 80 */       additionalParameters.put("code_challenge", codeVerifier);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void addAcrValues(Map<String, Object> attributes, Map<String, Object> additionalParameters) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static String createHash(String value) throws NoSuchAlgorithmException {
/* 96 */     MessageDigest md = MessageDigest.getInstance("SHA-256");
/* 97 */     byte[] digest = md.digest(value.getBytes(StandardCharsets.US_ASCII));
/* 98 */     return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\security\ZenzefiAuthRequestResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */