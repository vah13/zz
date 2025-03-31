/*    */ package com.daimler.cebas.configuration.control;
/*    */ 
/*    */ import com.daimler.cebas.common.control.CEBASException;
/*    */ import com.daimler.cebas.common.control.ICEBASRecovery;
/*    */ import io.undertow.Undertow;
/*    */ import java.util.Optional;
/*    */ import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
/*    */ import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
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
/*    */ public class WebServerConfiguration
/*    */ {
/*    */   @Bean
/*    */   public UndertowServletWebServerFactory servletWebServerFactory(Optional<ICEBASRecovery> systemRecoveryOptional) {
/* 26 */     if (systemRecoveryOptional.isPresent()) {
/* 27 */       ICEBASRecovery cebasRecovery = systemRecoveryOptional.get();
/* 28 */       if (cebasRecovery.canAccessFiles()) {
/* 29 */         cebasRecovery.checkForRecovery();
/*    */       } else {
/* 31 */         throw new CEBASException("Files can't be accessed. It might be another process is using them");
/*    */       } 
/*    */     } 
/* 34 */     UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
/* 35 */     factory.addBuilderCustomizers(new UndertowBuilderCustomizer[] { builder -> {
/*    */             builder.setIoThreads(5);
/*    */             builder.setWorkerThreads(15);
/*    */           } });
/* 39 */     return factory;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\WebServerConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */