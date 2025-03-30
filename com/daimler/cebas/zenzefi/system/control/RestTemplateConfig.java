/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control;
/*    */ 
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.zenzefi.configuration.control.ZenZefiGeneralPropertiesManager;
/*    */ import com.daimler.cebas.zenzefi.system.control.RefreshTokenClient;
/*    */ import com.daimler.cebas.zenzefi.system.control.RefreshTokenRequestInterceptor;
/*    */ import java.time.Duration;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.logging.Level;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.boot.web.client.RestTemplateBuilder;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Profile;
/*    */ import org.springframework.core.env.ConfigurableEnvironment;
/*    */ import org.springframework.http.client.ClientHttpRequestInterceptor;
/*    */ import org.springframework.util.CollectionUtils;
/*    */ import org.springframework.web.client.RestTemplate;
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
/*    */ @Configuration
/*    */ @Profile({"!AFTERSALES"})
/*    */ public class RestTemplateConfig
/*    */ {
/*    */   @Autowired
/*    */   private RefreshTokenClient tokenClient;
/*    */   @Autowired
/*    */   private RestTemplateBuilder restTemplateBuilder;
/*    */   @Autowired
/*    */   private ConfigurableEnvironment configurableEnvironment;
/*    */   @Autowired
/*    */   private ZenZefiGeneralPropertiesManager zenZefiGeneralPropertiesManager;
/*    */   @Autowired
/*    */   private Logger zenzefiLogger;
/*    */   
/*    */   @Bean
/*    */   public RestTemplate restTemplate() {
/* 48 */     RestTemplate restTemplate = createRestTemplate();
/*    */     
/* 50 */     List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
/* 51 */     if (CollectionUtils.isEmpty(interceptors)) {
/* 52 */       interceptors = new ArrayList<>();
/*    */     }
/* 54 */     interceptors.add(new RefreshTokenRequestInterceptor(this.tokenClient, this.zenzefiLogger, restTemplateWithoutInterceptors()));
/* 55 */     restTemplate.setInterceptors(interceptors);
/* 56 */     return restTemplate;
/*    */   }
/*    */   
/*    */   @Bean({"noInterceptor"})
/*    */   public RestTemplate restTemplateWithoutInterceptors() {
/* 61 */     return createRestTemplate();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private RestTemplate createRestTemplate() {
/*    */     RestTemplate restTemplate;
/* 68 */     String pkiHttpConnectionTimeout = this.configurableEnvironment.getProperty("pki.http.connection.timeout");
/*    */     
/* 70 */     String pkiHttpReadTimeout = this.configurableEnvironment.getProperty("pki.http.read.timeout");
/*    */     
/* 72 */     if (StringUtils.isNumeric(pkiHttpReadTimeout) && StringUtils.isNumeric(pkiHttpConnectionTimeout)) {
/*    */       
/* 74 */       restTemplate = this.restTemplateBuilder.setReadTimeout(Duration.ofMillis(Integer.parseInt(pkiHttpReadTimeout))).setConnectTimeout(Duration.ofMillis(Integer.parseInt(pkiHttpConnectionTimeout))).build();
/*    */     } else {
/* 76 */       this.zenzefiLogger.log(Level.WARNING, "000379X", "Zezenfi found invalid PKI HTTP timeouts configuration. Defaults will be used", 
/*    */           
/* 78 */           getClass().getSimpleName());
/*    */       
/* 80 */       this.configurableEnvironment.getSystemProperties().put("pki.http.connection.timeout", Integer.valueOf(10000));
/* 81 */       this.configurableEnvironment.getSystemProperties().put("pki.http.read.timeout", Integer.valueOf(15000));
/* 82 */       this.zenZefiGeneralPropertiesManager.restTemplateConnectionTimeoutsFromEnv();
/*    */ 
/*    */       
/* 85 */       restTemplate = this.restTemplateBuilder.setReadTimeout(Duration.ofMillis(15000L)).setConnectTimeout(Duration.ofMillis(10000L)).build();
/*    */     } 
/* 87 */     return restTemplate;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\RestTemplateConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */