/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.security;
/*     */ 
/*     */ import com.daimler.cebas.security.oidc.CebasOAuth2AccessTokenResponseConverter;
/*     */ import com.daimler.cebas.security.oidc.CebasOAuth2AuthorizedClientService;
/*     */ import com.daimler.cebas.security.oidc.CustomAuthCodeTokenResponseClient;
/*     */ import com.daimler.cebas.security.oidc.CustomTokenRequest;
/*     */ import com.daimler.cebas.users.control.Session;
/*     */ import com.daimler.cebas.zenzefi.configuration.control.util.ZenZefiPkiAndOidcPropertiesManager;
/*     */ import com.daimler.cebas.zenzefi.security.ZenZefiAuthenticationFailureHandler;
/*     */ import com.daimler.cebas.zenzefi.security.ZenZefiLogoutSuccessHandler;
/*     */ import com.daimler.cebas.zenzefi.security.ZenZefiOidcIdTokenValidatorFactory;
/*     */ import com.daimler.cebas.zenzefi.security.ZenzefiAuthRequestResolver;
/*     */ import com.daimler.cebas.zenzefi.users.integration.ZenZefiLogoutHandler;
/*     */ import java.time.Duration;
/*     */ import java.time.temporal.ChronoUnit;
/*     */ import java.util.Arrays;
/*     */ import java.util.function.Function;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Profile;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.http.converter.FormHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.security.config.annotation.SecurityBuilder;
/*     */ import org.springframework.security.config.annotation.web.builders.HttpSecurity;
/*     */ import org.springframework.security.config.annotation.web.builders.WebSecurity;
/*     */ import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
/*     */ import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
/*     */ import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
/*     */ import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
/*     */ import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
/*     */ import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
/*     */ import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
/*     */ import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
/*     */ import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
/*     */ import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
/*     */ import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
/*     */ import org.springframework.security.oauth2.client.oidc.authentication.OidcIdTokenDecoderFactory;
/*     */ import org.springframework.security.oauth2.client.registration.ClientRegistration;
/*     */ import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
/*     */ import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
/*     */ import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
/*     */ import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
/*     */ import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
/*     */ import org.springframework.security.oauth2.jwt.JwtDecoder;
/*     */ import org.springframework.security.oauth2.jwt.JwtDecoderFactory;
/*     */ import org.springframework.security.web.authentication.AuthenticationFailureHandler;
/*     */ import org.springframework.security.web.authentication.logout.LogoutHandler;
/*     */ import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
/*     */ import org.springframework.security.web.session.HttpSessionEventPublisher;
/*     */ import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
/*     */ import org.springframework.security.web.util.matcher.RequestMatcher;
/*     */ import org.springframework.web.client.ResponseErrorHandler;
/*     */ import org.springframework.web.client.RestOperations;
/*     */ import org.springframework.web.client.RestTemplate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @EnableWebSecurity
/*     */ @Profile({"!AFTERSALES"})
/*     */ public class ZenZefiSecurityConfiguration
/*     */   extends WebSecurityConfigurerAdapter
/*     */ {
/*     */   static final String LOGOUT_ENDPOINT = "/users/logout/oauth";
/*     */   static final String LOGIN_ENDPOINT = "/users/login/oauth";
/*     */   private static final String OAUTH_2_AUTHORIZATION = "/oauth2/authorization";
/*     */   @Autowired
/*     */   private ZenZefiLogoutHandler zenZefiLogoutHandler;
/*     */   @Autowired
/*     */   private CustomAuthCodeTokenResponseClient customTokenResponseClient;
/*     */   
/*     */   protected void configure(HttpSecurity http) throws Exception {
/*  80 */     ((HttpSecurity)((HttpSecurity)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((HttpSecurity)((HttpSecurity)((HttpSecurity)http.headers().frameOptions()
/*  81 */       .sameOrigin()
/*  82 */       .and())
/*  83 */       .httpBasic().disable())
/*  84 */       .csrf().disable())
/*  85 */       .antMatcher("/**").authorizeRequests()
/*  86 */       .antMatchers(new String[] { "/"
/*  87 */         })).permitAll()
/*  88 */       .antMatchers(new String[] { "/users/login/oauth"
/*  89 */         })).authenticated()
/*  90 */       .and())
/*  91 */       .logout()
/*  92 */       .logoutRequestMatcher((RequestMatcher)new AntPathRequestMatcher("/users/logout/oauth"))
/*  93 */       .invalidateHttpSession(true)
/*  94 */       .deleteCookies(new String[] { "JSESSIONID"
/*  95 */         }).addLogoutHandler((LogoutHandler)this.zenZefiLogoutHandler)
/*  96 */       .logoutSuccessHandler((LogoutSuccessHandler)this.zenZefiLogoutSuccessHandler)
/*  97 */       .and())
/*  98 */       .oauth2Login()
/*  99 */       .authorizationEndpoint()
/* 100 */       .authorizationRequestResolver((OAuth2AuthorizationRequestResolver)createCustomAuthRequestResolver())
/* 101 */       .and()
/* 102 */       .tokenEndpoint()
/* 103 */       .accessTokenResponseClient(accessTokenResponseClient())
/* 104 */       .and()
/* 105 */       .failureHandler((AuthenticationFailureHandler)this.zenZefiAuthenticationFailureHandler); } @Autowired
/*     */   private ClientRegistrationRepository clientRegistrationRepository; @Autowired
/*     */   private OAuth2AuthorizedClientService authClientService; @Autowired
/*     */   private ZenZefiLogoutSuccessHandler zenZefiLogoutSuccessHandler; @Autowired
/*     */   private ZenZefiAuthenticationFailureHandler zenZefiAuthenticationFailureHandler; @Autowired
/*     */   private ZenZefiPkiAndOidcPropertiesManager pkiAndOAuthPropertiesManager; @Autowired
/*     */   private Session session; public void configure(WebSecurity web) {
/* 112 */     web.ignoring().antMatchers(new String[] { "/daimler/**", "/font-awesome-4.7.0/**" });
/*     */   }
/*     */   
/*     */   @Bean
/*     */   public HttpSessionEventPublisher httpSessionEventPublisher() {
/* 117 */     return new HttpSessionEventPublisher();
/*     */   }
/*     */   
/*     */   @Bean
/*     */   public JwtDecoderFactory<ClientRegistration> jwtDecoderFactory() {
/* 122 */     return context -> {
/*     */         int oidcMaxTimeSkew = this.pkiAndOAuthPropertiesManager.getOidcMaxTimeSkew();
/*     */         Duration maxCloxkSkew = Duration.of(oidcMaxTimeSkew, ChronoUnit.SECONDS);
/*     */         OidcIdTokenDecoderFactory decoderFactory = new OidcIdTokenDecoderFactory();
/*     */         decoderFactory.setJwtValidatorFactory((Function)new ZenZefiOidcIdTokenValidatorFactory(maxCloxkSkew));
/*     */         return decoderFactory.createDecoder(context);
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   @Bean
/*     */   public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
/* 134 */     this.customTokenResponseClient.setRequestEntityConverter((Converter)new CustomTokenRequest());
/* 135 */     OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
/* 136 */     tokenResponseHttpMessageConverter.setTokenResponseConverter((Converter)new CebasOAuth2AccessTokenResponseConverter(this.session));
/* 137 */     RestTemplate restTemplate = new RestTemplate(Arrays.asList(new HttpMessageConverter[] { (HttpMessageConverter)new FormHttpMessageConverter(), (HttpMessageConverter)tokenResponseHttpMessageConverter }));
/* 138 */     restTemplate.setErrorHandler((ResponseErrorHandler)new OAuth2ErrorResponseErrorHandler());
/* 139 */     this.customTokenResponseClient.setRestOperations((RestOperations)restTemplate);
/*     */     
/* 141 */     return (OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>)this.customTokenResponseClient;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
/* 146 */     return (OAuth2AuthorizedClientService)new CebasOAuth2AuthorizedClientService(clientRegistrationRepository);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Bean
/*     */   public OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository) {
/* 154 */     OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder().authorizationCode().refreshToken().build();
/*     */     
/* 156 */     DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
/*     */     
/* 158 */     authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
/* 159 */     return (OAuth2AuthorizedClientManager)authorizedClientManager;
/*     */   }
/*     */   
/*     */   public OAuth2AuthorizedClient getOAuthClient(String userName) {
/* 163 */     return this.authClientService.loadAuthorizedClient("gas", userName);
/*     */   }
/*     */   
/*     */   private ZenzefiAuthRequestResolver createCustomAuthRequestResolver() {
/* 167 */     return new ZenzefiAuthRequestResolver(this.clientRegistrationRepository, "/oauth2/authorization");
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\security\ZenZefiSecurityConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */