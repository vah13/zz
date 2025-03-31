/*    */ package com.daimler.cebas.configuration.control.gracefulshutdown;
/*    */ 
/*    */ import com.daimler.cebas.common.control.StartupStatus;
/*    */ import com.secunet.ed25519_pkcs12support.EdDSAKeyInfoConverter;
/*    */ import com.secunet.ed25519ph.SecunetEdDSASecurityProvider;
/*    */ import com.secunet.provider.P12KeyStorePBES2Provider;
/*    */ import java.security.Provider;
/*    */ import java.security.Security;
/*    */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*    */ import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;
/*    */ import org.bouncycastle.jce.provider.BouncyCastleProvider;
/*    */ import org.springframework.boot.Banner;
/*    */ import org.springframework.boot.SpringApplication;
/*    */ import org.springframework.context.ApplicationContextInitializer;
/*    */ import org.springframework.context.ApplicationEvent;
/*    */ import org.springframework.context.ConfigurableApplicationContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StartupWrapper
/*    */   extends SpringApplication
/*    */ {
/*    */   public StartupWrapper(Class<?>... primarySources) {
/* 30 */     super(primarySources);
/*    */   }
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
/*    */   
/*    */   public static ConfigurableApplicationContext run(Class<?> appClazz, boolean bannerModeOff, ApplicationContextInitializer<ConfigurableApplicationContext> initializer, String... args) {
/* 47 */     BouncyCastleProvider bcProvider = new BouncyCastleProvider();
/* 48 */     bcProvider.addKeyInfoConverter(new ASN1ObjectIdentifier("1.3.6.1.4.1.2916.3.6.509.5.110"), (AsymmetricKeyInfoConverter)new EdDSAKeyInfoConverter());
/* 49 */     Security.addProvider((Provider)bcProvider);
/* 50 */     Security.addProvider((Provider)new SecunetEdDSASecurityProvider());
/* 51 */     Security.addProvider((Provider)new P12KeyStorePBES2Provider());
/*    */     
/* 53 */     StartupWrapper app = new StartupWrapper(new Class[] { appClazz });
/* 54 */     if (bannerModeOff) {
/* 55 */       app.setBannerMode(Banner.Mode.OFF);
/*    */     }
/* 57 */     app.addInitializers(new ApplicationContextInitializer[] { initializer });
/* 58 */     ConfigurableApplicationContext applicationContext = app.run(args);
/* 59 */     ShutdownHook hook = (ShutdownHook)applicationContext.getBean(ShutdownHook.class);
/* 60 */     Runtime.getRuntime().addShutdownHook(new Thread(hook));
/*    */     
/* 62 */     StartupStatus.setStarted();
/*    */     
/* 64 */     return applicationContext;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ConfigurableApplicationContext run(String... args) {
/* 73 */     setRegisterShutdownHook(false);
/* 74 */     ConfigurableApplicationContext applicationContext = super.run(args);
/* 75 */     ShutdownHook hook = (ShutdownHook)applicationContext.getBean(ShutdownHook.class);
/* 76 */     hook.init(applicationContext);
/* 77 */     applicationContext.addApplicationListener(event -> {
/*    */           if (event instanceof org.springframework.context.event.ContextClosedEvent) {
/*    */             ShutdownHook registeredHook = (ShutdownHook)applicationContext.getBean(ShutdownHook.class);
/*    */ 
/*    */ 
/*    */             
/*    */             registeredHook.run();
/*    */           } 
/*    */         });
/*    */ 
/*    */     
/* 88 */     return applicationContext;
/*    */   }
/*    */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\configuration\control\gracefulshutdown\StartupWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */