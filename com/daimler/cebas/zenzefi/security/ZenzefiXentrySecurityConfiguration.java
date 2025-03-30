/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.security;
/*    */ 
/*    */ import com.daimler.cebas.zenzefi.users.integration.ZenZefiLogoutHandler;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Profile;
/*    */ import org.springframework.security.config.annotation.SecurityBuilder;
/*    */ import org.springframework.security.config.annotation.web.builders.HttpSecurity;
/*    */ import org.springframework.security.config.annotation.web.builders.WebSecurity;
/*    */ import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
/*    */ import org.springframework.security.config.annotation.web.configurers.ChannelSecurityConfigurer;
/*    */ import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
/*    */ import org.springframework.security.web.authentication.logout.LogoutHandler;
/*    */ import org.springframework.security.web.session.HttpSessionEventPublisher;
/*    */ import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
/*    */ import org.springframework.security.web.util.matcher.RequestMatcher;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @Profile({"AFTERSALES"})
/*    */ public class ZenzefiXentrySecurityConfiguration
/*    */   extends WebSecurityConfigurerAdapter
/*    */ {
/*    */   private static final String SINGLE_PAGE = "/zenzefi/ui/index.html";
/*    */   @Autowired
/*    */   private ZenZefiLogoutHandler zenZefiLogoutHandler;
/*    */   
/*    */   protected void configure(HttpSecurity http) throws Exception {
/* 33 */     http.csrf().disable();
/*    */ 
/*    */     
/* 36 */     ((ChannelSecurityConfigurer.RequiresChannelUrl)http.requiresChannel().anyRequest()).requiresSecure();
/* 37 */     http.headers().frameOptions().sameOrigin();
/* 38 */     ((HttpSecurity)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)http.authorizeRequests().requestMatchers(new RequestMatcher[] { (RequestMatcher)EndpointRequest.to(new String[] { "shutdown", "restart" }) })).permitAll()
/* 39 */       .antMatchers(new String[] { "/", "/zenzefi/ui/index.html" })).permitAll()
/* 40 */       .antMatchers(new String[] { "/users/login/oauth" })).denyAll().and()).logout()
/* 41 */       .invalidateHttpSession(true).clearAuthentication(true)
/* 42 */       .logoutSuccessUrl("/zenzefi/ui/index.html")
/* 43 */       .logoutRequestMatcher((RequestMatcher)new AntPathRequestMatcher("/users/logout/oauth"))
/* 44 */       .deleteCookies(new String[] { "JSESSIONID" }).addLogoutHandler((LogoutHandler)this.zenZefiLogoutHandler);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void configure(WebSecurity web) {
/* 50 */     web.ignoring().antMatchers(new String[] { "/daimler/**", "/font-awesome-4.7.0/**" });
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public HttpSessionEventPublisher httpSessionEventPublisher() {
/* 55 */     return new HttpSessionEventPublisher();
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\security\ZenzefiXentrySecurityConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */