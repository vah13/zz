/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi.configuration.control;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.Profile;
/*     */ import springfox.documentation.builders.ApiInfoBuilder;
/*     */ import springfox.documentation.builders.PathSelectors;
/*     */ import springfox.documentation.service.ApiInfo;
/*     */ import springfox.documentation.spi.DocumentationType;
/*     */ import springfox.documentation.spring.web.plugins.Docket;
/*     */ import springfox.documentation.swagger2.annotations.EnableSwagger2;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Configuration
/*     */ @EnableSwagger2
/*     */ @Profile({"!AFTERSALES"})
/*     */ public class SwaggerConfiguration
/*     */ {
/*     */   private static final String ZEN_ZEFI_REST_API = "ZenZefi REST API";
/*     */   
/*     */   @Bean
/*     */   public Docket newsApi() {
/*  42 */     return (new Docket(DocumentationType.SWAGGER_2)).groupName("zenzefi-doc").useDefaultResponseMessages(false)
/*  43 */       .apiInfo(apiInfo()).select().paths(pathsLatest()).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Bean
/*     */   public Docket apiV1() {
/*  53 */     return (new Docket(DocumentationType.SWAGGER_2)).useDefaultResponseMessages(false).groupName("zenzefi-doc-v1")
/*  54 */       .select().paths(pathsV1()).build()
/*  55 */       .apiInfo((new ApiInfoBuilder()).version("1.0").title("ZenZefi REST API").description("This version is deprecated. Please switch to the newest version <span style='color:red'>(v3)</span> as soon as possible.\nZenZefi REST API V1 provides operations for resources certificate, configuration, log, user, system.")
/*     */ 
/*     */         
/*  58 */         .build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Bean
/*     */   public Docket apiV2() {
/*  68 */     return (new Docket(DocumentationType.SWAGGER_2)).useDefaultResponseMessages(false).groupName("zenzefi-doc-v2")
/*  69 */       .select().paths(pathsV2()).build()
/*  70 */       .apiInfo((new ApiInfoBuilder()).version("2.0").title("ZenZefi REST API").description("ZenZefi REST API V2 provides operations for resources certificate, configuration, log, user, system.\nPlease switch to the newest version <span style='color:red'>(v3)</span> as soon as possible.")
/*     */ 
/*     */         
/*  73 */         .build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Bean
/*     */   public Docket apiV3() {
/*  83 */     return (new Docket(DocumentationType.SWAGGER_2)).useDefaultResponseMessages(false).groupName("zenzefi-doc-v3")
/*  84 */       .select().paths(pathsV3()).build()
/*  85 */       .apiInfo((new ApiInfoBuilder()).version("3.0").title("ZenZefi REST API").description("ZenZefi REST API V3 provides operations for resources certificate, configuration, log, user, system.")
/*     */         
/*  87 */         .build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Bean
/*     */   public ApiInfo apiInfo() {
/*  97 */     return (new ApiInfoBuilder()).title("ZenZefi REST API").description("ZenZefi REST API provides operations for resources certificate, configuration, log, user, system.\nThe operation-handler endpoints link to the enabled Spring Boot Actuator endpoints. For more details please check:\nhttps://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html")
/*     */ 
/*     */       
/* 100 */       .version("Latest").build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Predicate<String> pathsLatest() {
/* 109 */     return Predicates.not(PathSelectors.regex("/shutdown.json|/health.json|/restart.json|/metrics/.*|/metrics.json|/resume.*|/pause.*|/refresh.*|/features.*|/auditevents.*|/error|/env.*|/heapdump.*|/autoconfig.*|/beans.*|/configprops.*|/dump.*|/flyway.*|/info.*|/mappings.*|/trace.*|/users/login/oauth|/users/register/oauth|/users/logout/oauth|/v1.*|/v2.*|/v3.*"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Predicate<String> pathsV1() {
/* 121 */     return PathSelectors.regex("/v1.*");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Predicate<String> pathsV2() {
/* 130 */     return PathSelectors.regex("/v2.*");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Predicate<String> pathsV3() {
/* 139 */     return PathSelectors.regex("/v3.*");
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\configuration\control\SwaggerConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */