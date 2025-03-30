/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.security;
/*    */ 
/*    */ import java.time.Clock;
/*    */ import java.time.Duration;
/*    */ import java.time.Instant;
/*    */ import org.springframework.security.oauth2.client.oidc.authentication.OidcIdTokenValidator;
/*    */ import org.springframework.security.oauth2.client.registration.ClientRegistration;
/*    */ import org.springframework.security.oauth2.core.AbstractOAuth2Token;
/*    */ import org.springframework.security.oauth2.core.OAuth2Error;
/*    */ import org.springframework.security.oauth2.core.OAuth2TokenValidator;
/*    */ import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
/*    */ import org.springframework.security.oauth2.jwt.Jwt;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ public class ZenZefiOidcTokenValidator
/*    */   implements OAuth2TokenValidator<Jwt>
/*    */ {
/*    */   public static final String OAUTH2_CODE_WRONG_CLOCK = "invavlid_clock_setting";
/* 20 */   private Clock clock = Clock.systemUTC();
/*    */   private Duration clockSkew;
/*    */   private OidcIdTokenValidator delegate;
/*    */   
/*    */   public ZenZefiOidcTokenValidator(ClientRegistration clientRegistration, Duration clockSkew) {
/* 25 */     Assert.notNull(clientRegistration, "clientRegistration cannot be null");
/* 26 */     this.delegate = new OidcIdTokenValidator(clientRegistration);
/* 27 */     this.delegate.setClockSkew(clockSkew);
/* 28 */     this.clockSkew = clockSkew;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public OAuth2TokenValidatorResult validate(Jwt idToken) {
/* 34 */     if (Instant.now(this.clock).plus(this.clockSkew).isBefore(idToken.getIssuedAt())) {
/* 35 */       return OAuth2TokenValidatorResult.failure(new OAuth2Error[] { new OAuth2Error("invavlid_clock_setting", "The ID Token contains invalid claims: iat=" + idToken
/* 36 */               .getIssuedAt(), "https://openid.net/specs/openid-connect-core-1_0.html#IDTokenValidation") });
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 41 */     return this.delegate.validate(idToken);
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\security\ZenZefiOidcTokenValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */