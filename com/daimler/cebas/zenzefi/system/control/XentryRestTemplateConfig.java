/*    */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.system.control;
/*    */ 
/*    */ import com.daimler.cebas.logs.control.Logger;
/*    */ import com.daimler.cebas.zenzefi.configuration.control.ZenZefiGeneralPropertiesManager;
/*    */ import java.time.Duration;
/*    */ import java.util.logging.Level;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.boot.web.client.RestTemplateBuilder;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.context.annotation.Profile;
/*    */ import org.springframework.core.env.ConfigurableEnvironment;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @Profile({"AFTERSALES"})
/*    */ public class XentryRestTemplateConfig
/*    */ {
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
/* 43 */     return createRestTemplate();
/*    */   }
/*    */   
/*    */   @Bean({"noInterceptor"})
/*    */   public RestTemplate restTemplateWithoutInterceptors() {
/* 48 */     return createRestTemplate();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private RestTemplate createRestTemplate() {
/*    */     RestTemplate restTemplate;
/* 55 */     String pkiHttpConnectionTimeout = this.configurableEnvironment.getProperty("pki.http.connection.timeout");
/*    */     
/* 57 */     String pkiHttpReadTimeout = this.configurableEnvironment.getProperty("pki.http.read.timeout");
/*    */     
/* 59 */     if (StringUtils.isNumeric(pkiHttpReadTimeout) && StringUtils.isNumeric(pkiHttpConnectionTimeout)) {
/*    */       
/* 61 */       restTemplate = this.restTemplateBuilder.setReadTimeout(Duration.ofMillis(Integer.parseInt(pkiHttpReadTimeout))).setConnectTimeout(Duration.ofMillis(Integer.parseInt(pkiHttpConnectionTimeout))).build();
/*    */     } else {
/* 63 */       this.zenzefiLogger.log(Level.WARNING, "000379X", "Zezenfi found invalid PKI HTTP timeouts configuration. Defaults will be used", 
/*    */           
/* 65 */           getClass().getSimpleName());
/*    */       
/* 67 */       this.configurableEnvironment.getSystemProperties().put("pki.http.connection.timeout", Integer.valueOf(10000));
/* 68 */       this.configurableEnvironment.getSystemProperties().put("pki.http.read.timeout", Integer.valueOf(15000));
/* 69 */       this.zenZefiGeneralPropertiesManager.restTemplateConnectionTimeoutsFromEnv();
/*    */ 
/*    */       
/* 72 */       restTemplate = this.restTemplateBuilder.setReadTimeout(Duration.ofMillis(15000L)).setConnectTimeout(Duration.ofMillis(10000L)).build();
/*    */     } 
/*    */     
/* 75 */     return restTemplate;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\system\control\XentryRestTemplateConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */