/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.StartupStatus
 *  com.daimler.cebas.configuration.control.gracefulshutdown.ShutdownHook
 *  com.secunet.ed25519_pkcs12support.EdDSAKeyInfoConverter
 *  com.secunet.ed25519ph.SecunetEdDSASecurityProvider
 *  com.secunet.provider.P12KeyStorePBES2Provider
 *  org.bouncycastle.asn1.ASN1ObjectIdentifier
 *  org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter
 *  org.bouncycastle.jce.provider.BouncyCastleProvider
 *  org.springframework.boot.Banner$Mode
 *  org.springframework.boot.SpringApplication
 *  org.springframework.context.ApplicationContextInitializer
 *  org.springframework.context.ConfigurableApplicationContext
 *  org.springframework.context.event.ContextClosedEvent
 */
package com.daimler.cebas.configuration.control.gracefulshutdown;

import com.daimler.cebas.common.control.StartupStatus;
import com.daimler.cebas.configuration.control.gracefulshutdown.ShutdownHook;
import com.secunet.ed25519_pkcs12support.EdDSAKeyInfoConverter;
import com.secunet.ed25519ph.SecunetEdDSASecurityProvider;
import com.secunet.provider.P12KeyStorePBES2Provider;
import java.security.Provider;
import java.security.Security;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

public class StartupWrapper
extends SpringApplication {
    public StartupWrapper(Class<?> ... primarySources) {
        super((Class[])primarySources);
    }

    public static ConfigurableApplicationContext run(Class<?> appClazz, boolean bannerModeOff, ApplicationContextInitializer<ConfigurableApplicationContext> initializer, String ... args) {
        BouncyCastleProvider bcProvider = new BouncyCastleProvider();
        bcProvider.addKeyInfoConverter(new ASN1ObjectIdentifier("1.3.6.1.4.1.2916.3.6.509.5.110"), (AsymmetricKeyInfoConverter)new EdDSAKeyInfoConverter());
        Security.addProvider((Provider)bcProvider);
        Security.addProvider((Provider)new SecunetEdDSASecurityProvider());
        Security.addProvider((Provider)new P12KeyStorePBES2Provider());
        StartupWrapper app = new StartupWrapper(appClazz);
        if (bannerModeOff) {
            app.setBannerMode(Banner.Mode.OFF);
        }
        app.addInitializers(new ApplicationContextInitializer[]{initializer});
        ConfigurableApplicationContext applicationContext = app.run(args);
        ShutdownHook hook = (ShutdownHook)applicationContext.getBean(ShutdownHook.class);
        Runtime.getRuntime().addShutdownHook(new Thread((Runnable)hook));
        StartupStatus.setStarted();
        return applicationContext;
    }

    public ConfigurableApplicationContext run(String ... args) {
        this.setRegisterShutdownHook(false);
        ConfigurableApplicationContext applicationContext = super.run(args);
        ShutdownHook hook = (ShutdownHook)applicationContext.getBean(ShutdownHook.class);
        hook.init(applicationContext);
        applicationContext.addApplicationListener(event -> {
            if (!(event instanceof ContextClosedEvent)) return;
            ShutdownHook registeredHook = (ShutdownHook)applicationContext.getBean(ShutdownHook.class);
            registeredHook.run();
        });
        return applicationContext;
    }
}
