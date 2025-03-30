/*     */ package BOOT-INF.classes.com.daimler.cebas.zenzefi;
/*     */ 
/*     */ import com.daimler.cebas.common.control.StartupInfoBuilder;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASRepository;
/*     */ import com.daimler.cebas.common.control.annotations.CEBASService;
/*     */ import com.daimler.cebas.configuration.control.gracefulshutdown.StartupWrapper;
/*     */ import com.daimler.cebas.system.control.startup.CeBASStartupProperty;
/*     */ import com.daimler.cebas.system.control.startup.PrepareStartupEngine;
/*     */ import com.daimler.cebas.system.control.startup.readers.DynamicSecretReader;
/*     */ import com.daimler.cebas.system.control.startup.readers.SecretReader;
/*     */ import com.daimler.cebas.system.control.startup.readers.StaticSecretReader;
/*     */ import java.util.logging.Logger;
/*     */ import org.springframework.boot.actuate.autoconfigure.audit.AuditEventsEndpointAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.beans.BeansEndpointAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.cloudfoundry.reactive.ReactiveCloudFoundryActuatorAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.condition.ConditionsReportEndpointAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.context.properties.ConfigurationPropertiesReportEndpointAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.jolokia.JolokiaEndpointAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.management.ThreadDumpEndpointAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.metrics.amqp.RabbitMetricsAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.metrics.export.atlas.AtlasMetricsExportAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.metrics.export.ganglia.GangliaMetricsExportAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.metrics.export.graphite.GraphiteMetricsExportAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.metrics.export.newrelic.NewRelicMetricsExportAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusMetricsExportAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.metrics.web.reactive.WebFluxMetricsAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.metrics.web.servlet.WebMvcMetricsAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.metrics.web.tomcat.TomcatMetricsAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.trace.http.HttpTraceAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.trace.http.HttpTraceEndpointAutoConfiguration;
/*     */ import org.springframework.boot.actuate.autoconfigure.web.reactive.ReactiveManagementContextAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.SpringBootApplication;
/*     */ import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.data.cassandra.CassandraRepositoriesAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseDataAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveDataAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveRepositoriesAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseRepositoriesAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.data.neo4j.Neo4jRepositoriesAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.domain.EntityScan;
/*     */ import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.hazelcast.HazelcastJpaDependencyAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.influx.InfluxDbAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.sendgrid.SendGridAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.websocket.reactive.WebSocketReactiveAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketMessagingAutoConfiguration;
/*     */ import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
/*     */ import org.springframework.boot.web.server.ErrorPage;
/*     */ import org.springframework.boot.web.server.WebServerFactoryCustomizer;
/*     */ import org.springframework.context.ApplicationContextInitializer;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.ComponentScan;
/*     */ import org.springframework.context.annotation.ComponentScan.Filter;
/*     */ import org.springframework.context.annotation.EnableAspectJAutoProxy;
/*     */ import org.springframework.context.annotation.FilterType;
/*     */ import org.springframework.context.annotation.PropertySource;
/*     */ import org.springframework.context.annotation.PropertySources;
/*     */ import org.springframework.context.support.ReloadableResourceBundleMessageSource;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.scheduling.annotation.EnableAsync;
/*     */ import org.springframework.scheduling.annotation.EnableScheduling;
/*     */ import org.springframework.security.core.context.SecurityContextHolder;
/*     */ import org.springframework.transaction.annotation.EnableTransactionManagement;
/*     */ import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
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
/*     */ @SpringBootApplication(exclude = {EmbeddedLdapAutoConfiguration.class, JmxAutoConfiguration.class, ProjectInfoAutoConfiguration.class, SpringDataWebAutoConfiguration.class, WebServicesAutoConfiguration.class, CouchbaseAutoConfiguration.class, CassandraAutoConfiguration.class, ActiveMQAutoConfiguration.class, RedisAutoConfiguration.class, ThymeleafAutoConfiguration.class, RabbitAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class, ElasticsearchRepositoriesAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class, Neo4jDataAutoConfiguration.class, FreeMarkerAutoConfiguration.class, GroovyTemplateAutoConfiguration.class, LiquibaseAutoConfiguration.class, KafkaAutoConfiguration.class, MustacheAutoConfiguration.class, SendGridAutoConfiguration.class, SolrAutoConfiguration.class, ArtemisAutoConfiguration.class, BatchAutoConfiguration.class, CacheAutoConfiguration.class, CassandraDataAutoConfiguration.class, GsonAutoConfiguration.class, CassandraRepositoriesAutoConfiguration.class, RepositoryRestMvcAutoConfiguration.class, LdapAutoConfiguration.class, CouchbaseDataAutoConfiguration.class, EmbeddedMongoAutoConfiguration.class, HazelcastAutoConfiguration.class, HazelcastJpaDependencyAutoConfiguration.class, CouchbaseRepositoriesAutoConfiguration.class, HypermediaAutoConfiguration.class, JooqAutoConfiguration.class, MailSenderAutoConfiguration.class, MailSenderValidatorAutoConfiguration.class, Neo4jRepositoriesAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, WebServicesAutoConfiguration.class, IntegrationAutoConfiguration.class, SendGridAutoConfiguration.class, ReactiveSecurityAutoConfiguration.class, ReactiveUserDetailsServiceAutoConfiguration.class, ReactiveWebServerFactoryAutoConfiguration.class, WebSocketReactiveAutoConfiguration.class, WebSocketMessagingAutoConfiguration.class, RedisReactiveAutoConfiguration.class, AtlasMetricsExportAutoConfiguration.class, RabbitMetricsAutoConfiguration.class, ReactiveCloudFoundryActuatorAutoConfiguration.class, ReactiveManagementContextAutoConfiguration.class, WebFluxMetricsAutoConfiguration.class, LdapAutoConfiguration.class, WebMvcMetricsAutoConfiguration.class, TomcatMetricsAutoConfiguration.class, ThreadDumpEndpointAutoConfiguration.class, NewRelicMetricsExportAutoConfiguration.class, PrometheusMetricsExportAutoConfiguration.class, GangliaMetricsExportAutoConfiguration.class, GraphiteMetricsExportAutoConfiguration.class, EmbeddedMongoAutoConfiguration.class, CouchbaseReactiveDataAutoConfiguration.class, CouchbaseReactiveRepositoriesAutoConfiguration.class, CouchbaseRepositoriesAutoConfiguration.class, CodecsAutoConfiguration.class, InfluxDbAutoConfiguration.class, BeansEndpointAutoConfiguration.class, AuditEventsEndpointAutoConfiguration.class, CompositeMeterRegistryAutoConfiguration.class, ConditionsReportEndpointAutoConfiguration.class, ConfigurationPropertiesReportEndpointAutoConfiguration.class, HttpTraceAutoConfiguration.class, JolokiaEndpointAutoConfiguration.class, HttpTraceEndpointAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
/*     */ @EnableTransactionManagement
/*     */ @PropertySources({@PropertySource({"classpath:zenzefi.properties"}), @PropertySource(ignoreResourceNotFound = true, value = {"classpath:zenzefi-general.properties"})})
/*     */ @EnableScheduling
/*     */ @EnableAspectJAutoProxy
/*     */ @EnableAsync
/*     */ @EntityScan(basePackages = {"com.daimler.cebas.certificates.entity", "com.daimler.cebas.zenzefi.certificates.entity", "com.daimler.cebas.system.entity", "com.daimler.cebas.configuration.entity", "com.daimler.cebas.logs.entity", "com.daimler.cebas.users.entity", "com.daimler.cebas.zenzefi.users.entity"})
/*     */ @ComponentScan(basePackages = {"com.daimler.cebas.certificates", "com.daimler.cebas.logs", "com.daimler.cebas.common", "com.daimler.cebas.security", "com.daimler.cebas.configuration", "com.daimler.cebas.users", "com.daimler.cebas.system", "com.daimler.cebas.zenzefi.certificates", "com.daimler.cebas.zenzefi.certificates.boundary", "com.daimler.cebas.zenzefi.certificates.control", "com.daimler.cebas.zenzefi.logs", "com.daimler.cebas.zenzefi.security", "com.daimler.cebas.zenzefi.configuration", "com.daimler.cebas.zenzefi.system", "com.daimler.cebas.zenzefi.system.boundary", "com.daimler.cebas.zenzefi.system.control", "com.daimler.cebas.zenzefi.users", "com.daimler.cebas.zenzefi.users.boundary", "com.daimler.cebas.zenzefi.users.control", "com.daimler.cebas.zenzefi.common", "com.daimler.cebas.zenzefi.common.control"}, includeFilters = {@Filter(type = FilterType.ANNOTATION, value = {CEBASControl.class}), @Filter(type = FilterType.ANNOTATION, value = {CEBASService.class}), @Filter(type = FilterType.ANNOTATION, value = {CEBASRepository.class})})
/*     */ public class ZenZefi
/*     */ {
/*     */   public static final String ZENZEFI_GENERAL_PROPERTIES_FILE_NAME = "zenzefi-general.properties";
/*     */   private static final String CERTIFICATES_PAGE = "redirect:/#/zenzefi/ui/certificates";
/*     */   private static final String LOGGING_PAGE = "redirect:/#/zenzefi/ui/logging";
/*     */   private static final String CONFIGURATIONS_PAGE = "redirect:/#/zenzefi/ui/configurations";
/*     */   private static final String ENVIRONMENT_PAGE = "redirect:/#/zenzefi/ui/environment";
/*     */   private static final String ERRORPAGE_401 = "/error/401";
/*     */   private static final String ERRORPAGE_500 = "/error/500";
/* 198 */   private static final Logger LOG = Logger.getLogger(com.daimler.cebas.zenzefi.ZenZefi.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   private static final String CLASS_NAME = com.daimler.cebas.zenzefi.ZenZefi.class.getSimpleName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 211 */     String METHOD_NAME = "main";
/* 212 */     LOG.entering(CLASS_NAME, "main");
/*     */     
/* 214 */     System.setProperty("jdk.tls.rejectClientInitiatedRenegotiation", "true");
/*     */     
/* 216 */     SecurityContextHolder.setStrategyName("MODE_GLOBAL");
/*     */     
/* 218 */     StartupWrapper.run(com.daimler.cebas.zenzefi.ZenZefi.class, true, (ApplicationContextInitializer)new PrepareStartupEngine(com.daimler.cebas.zenzefi.ZenZefi.class, "/zenzefi.properties", "/zenzefi-general.properties", 
/*     */           
/* 220 */           CeBASStartupProperty.getZenzefi(), new SecretReader[] { (SecretReader)new StaticSecretReader(), (SecretReader)new DynamicSecretReader() }), args);
/*     */ 
/*     */     
/* 223 */     LOG.info((new StartupInfoBuilder(com.daimler.cebas.zenzefi.ZenZefi.class)).getStartupMessage());
/* 224 */     LOG.exiting(CLASS_NAME, "main");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Bean
/*     */   public WebServerFactoryCustomizer<UndertowServletWebServerFactory> containerCustomizer() {
/* 234 */     return container -> {
/*     */         container.addErrorPages(new ErrorPage[] { new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401") });
/*     */         container.addErrorPages(new ErrorPage[] { new ErrorPage(HttpStatus.NOT_FOUND, "/error/404/404_error.html") });
/*     */         container.addErrorPages(new ErrorPage[] { new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500") });
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Bean
/*     */   public WebMvcConfigurer forwardToIndex() {
/* 248 */     return (WebMvcConfigurer)new Object(this);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Bean
/*     */   public ReloadableResourceBundleMessageSource messageSource() {
/* 289 */     ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
/* 290 */     messageSource.setBasenames(new String[] { "classpath:locale/messages-common", "classpath:locale/messages-zenzefi" });
/*     */ 
/*     */     
/* 293 */     messageSource.setFallbackToSystemLocale(false);
/* 294 */     messageSource.setUseCodeAsDefaultMessage(true);
/* 295 */     return messageSource;
/*     */   }
/*     */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\classes\com\daimler\cebas\zenzefi\ZenZefi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */