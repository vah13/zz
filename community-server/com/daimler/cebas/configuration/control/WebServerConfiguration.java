/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.ICEBASRecovery
 *  org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer
 *  org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 */
package com.daimler.cebas.configuration.control;

import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.ICEBASRecovery;
import java.util.Optional;
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServerConfiguration {
    @Bean
    public UndertowServletWebServerFactory servletWebServerFactory(Optional<ICEBASRecovery> systemRecoveryOptional) {
        if (systemRecoveryOptional.isPresent()) {
            ICEBASRecovery cebasRecovery = systemRecoveryOptional.get();
            if (!cebasRecovery.canAccessFiles()) throw new CEBASException("Files can't be accessed. It might be another process is using them");
            cebasRecovery.checkForRecovery();
        }
        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
        factory.addBuilderCustomizers(new UndertowBuilderCustomizer[]{builder -> {
            builder.setIoThreads(5);
            builder.setWorkerThreads(15);
        }});
        return factory;
    }
}
