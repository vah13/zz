/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.security;
/*    */ 
/*    */ import com.daimler.cebas.zenzefi.security.ZenZefiOidcTokenValidator;
/*    */ import java.time.Duration;
/*    */ import java.util.function.Function;
/*    */ import org.springframework.security.oauth2.client.registration.ClientRegistration;
/*    */ import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
/*    */ import org.springframework.security.oauth2.core.OAuth2TokenValidator;
/*    */ import org.springframework.security.oauth2.jwt.Jwt;
/*    */ import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ZenZefiOidcIdTokenValidatorFactory
/*    */   implements Function<ClientRegistration, OAuth2TokenValidator<Jwt>>
/*    */ {
/*    */   private final Duration MAX_CLOCK_SKEW;
/*    */   
/*    */   public ZenZefiOidcIdTokenValidatorFactory(Duration maxClockSkew) {
/* 22 */     this.MAX_CLOCK_SKEW = maxClockSkew;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public OAuth2TokenValidator<Jwt> apply(ClientRegistration clientRegistration) {
/* 28 */     ZenZefiOidcTokenValidator tokenValidator = new ZenZefiOidcTokenValidator(clientRegistration, this.MAX_CLOCK_SKEW);
/* 29 */     return (OAuth2TokenValidator<Jwt>)new DelegatingOAuth2TokenValidator(new OAuth2TokenValidator[] { (OAuth2TokenValidator)new JwtTimestampValidator(this.MAX_CLOCK_SKEW), (OAuth2TokenValidator)tokenValidator });
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\security\ZenZefiOidcIdTokenValidatorFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */