/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.StartupInfoBuilder
 *  com.daimler.cebas.configuration.control.PkiUrlProperty
 *  com.daimler.cebas.system.control.exceptions.ConfigurationCheckException
 *  com.daimler.cebas.system.control.startup.CeBASStartupProperty
 *  com.daimler.cebas.system.control.startup.readers.SecretFactory
 *  com.daimler.cebas.system.control.startup.readers.SecretMap
 *  com.daimler.cebas.system.control.startup.readers.SecretReader
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 *  org.springframework.boot.ansi.AnsiOutput
 *  org.springframework.boot.ansi.AnsiOutput$Enabled
 *  org.springframework.context.ApplicationContextInitializer
 *  org.springframework.context.ConfigurableApplicationContext
 *  org.springframework.core.env.ConfigurableEnvironment
 *  org.springframework.core.env.MapPropertySource
 *  org.springframework.core.env.PropertiesPropertySource
 *  org.springframework.core.env.PropertySource
 */
package com.daimler.cebas.system.control.startup;

import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.StartupInfoBuilder;
import com.daimler.cebas.configuration.control.PkiUrlProperty;
import com.daimler.cebas.system.control.exceptions.ConfigurationCheckException;
import com.daimler.cebas.system.control.startup.CeBASStartupProperty;
import com.daimler.cebas.system.control.startup.readers.SecretFactory;
import com.daimler.cebas.system.control.startup.readers.SecretMap;
import com.daimler.cebas.system.control.startup.readers.SecretReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

public class PrepareStartupEngine
implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    protected static final Log LOG = LogFactory.getLog(PrepareStartupEngine.class);
    private Class<?> appClazz;
    private SecretMap secret;
    private Set<CeBASStartupProperty> startupProperties;
    private Properties fileProperties;
    private boolean isProdEnvironment;

    public PrepareStartupEngine(Class<?> appClazz, String propertyFile, String generalPropertyFile, Set<CeBASStartupProperty> startupProperties, SecretReader ... readers) {
        CeBASStartupProperty p;
        this.appClazz = appClazz;
        this.startupProperties = startupProperties;
        this.fileProperties = this.getProperties(propertyFile, generalPropertyFile);
        for (CeBASStartupProperty p2 : startupProperties) {
            if (this.fileProperties.containsKey(p2.name())) throw new ConfigurationCheckException("The property " + p2.name() + "/" + p2.getProperty() + " must not be overriden in any property files.");
            if (!this.fileProperties.containsKey(p2.getProperty())) continue;
            throw new ConfigurationCheckException("The property " + p2.name() + "/" + p2.getProperty() + " must not be overriden in any property files.");
        }
        this.isProdEnvironment = this.isProdEnvironment(this.fileProperties);
        SecretFactory f = SecretFactory.create((Properties)this.fileProperties);
        for (SecretReader reader : readers) {
            f.run(reader);
        }
        this.secret = f.finish();
        Iterator<CeBASStartupProperty> iterator = startupProperties.iterator();
        do {
            if (!iterator.hasNext()) {
                this.setAnsiColor();
                return;
            }
            p = (CeBASStartupProperty)iterator.next();
            if (!p.isLog()) continue;
            LOG.info((Object)("The property " + p.getProperty() + " is defined as: " + this.secret.get(p)));
        } while (!p.isRequired() || this.secret.contains(p));
        throw new CEBASException("The property " + p.getProperty() + " is required but was not provided within the secret context.");
    }

    private boolean isProdEnvironment(Properties properties) {
        String pkiEnvironment = properties.getProperty(PkiUrlProperty.PKI_ENVIRONMENT.getProperty());
        if ("PROD".equals(pkiEnvironment)) {
            return true;
        }
        if (StringUtils.isEmpty(pkiEnvironment)) return false;
        if (!"TEST".equals(pkiEnvironment)) throw new CEBASException("The property " + PkiUrlProperty.PKI_ENVIRONMENT.getProperty() + " must either be set to " + "PROD" + " or to " + "TEST" + " but was " + pkiEnvironment + " instead.");
        return false;
    }

    private void setAnsiColor() {
        boolean isWindows = System.getProperty("os.name").toLowerCase(Locale.ENGLISH).startsWith("win");
        if (!isWindows) return;
        String shell = System.getenv().get("SHELL");
        if (shell != null && shell.contains("bash")) {
            AnsiOutput.setEnabled((AnsiOutput.Enabled)AnsiOutput.Enabled.ALWAYS);
            return;
        }
        String conEmu = System.getenv().get("ConEmuANSI");
        if (conEmu == null) return;
        if (!conEmu.contains("ON")) return;
        AnsiOutput.setEnabled((AnsiOutput.Enabled)AnsiOutput.Enabled.ALWAYS);
    }

    public void initialize(ConfigurableApplicationContext applicationContext) {
        LOG.info((Object)new StartupInfoBuilder(this.appClazz).getConfigurationsInfo());
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        this.setSslProperty(environment);
        LinkedHashMap<String, Object> props = new LinkedHashMap<String, Object>();
        Iterator<CeBASStartupProperty> iterator = this.startupProperties.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.setTestProdProperty(props, "spring.security.oauth2.client.registration.gas.provider", this.isProdEnvironment);
                this.setTestProdProperty(props, "spring.security.oauth2.client.registration.gas.redirect-uri", this.isProdEnvironment);
                this.setTestProdProperty(props, "security.revoke-token-uri", this.isProdEnvironment);
                this.setTestProdProperty(props, PkiUrlProperty.PKI_LOGOUT_URL.getProperty(), this.isProdEnvironment);
                this.setTestProdProperty(props, PkiUrlProperty.PKI_BASE_URL.getProperty(), this.isProdEnvironment);
                MapPropertySource testProp = new MapPropertySource("startupProperties", props);
                environment.getPropertySources().addFirst((PropertySource)testProp);
                return;
            }
            CeBASStartupProperty p = iterator.next();
            if (environment.containsProperty(p.getProperty())) throw new ConfigurationCheckException("The property " + p.name() + "/" + p.getProperty() + " must not be overriden with environment properties.");
            if (environment.containsProperty(p.name())) {
                throw new ConfigurationCheckException("The property " + p.name() + "/" + p.getProperty() + " must not be overriden with environment properties.");
            }
            if (!this.secret.contains(p)) continue;
            if (this.isProdEnvironment && p.isProdEnvironmentRelevant()) {
                props.put(p.getProperty(), this.secret.get(p));
                continue;
            }
            if (this.isProdEnvironment || !p.isTestEnvironmentRelevant()) continue;
            props.put(p.getProperty(), this.secret.get(p));
        }
    }

    private void setTestProdProperty(Map<String, Object> props, String property, boolean prod) {
        String env = prod ? "prod." : "test.";
        props.put(property, this.fileProperties.get(env + property));
    }

    private void setSslProperty(ConfigurableEnvironment environment) {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if (dot != -1) {
                version = version.substring(0, dot);
            }
        }
        if (!version.equals("11") && !version.equals("12")) {
            if (!version.equals("13")) return;
        }
        Properties props = new Properties();
        props.put("server.ssl.enabled-protocols", "SSLv3, TLSv1, TLSv1.1, TLSv1.2");
        environment.getPropertySources().addFirst((PropertySource)new PropertiesPropertySource("ssl_property", props));
        LOG.info((Object)("Setting SSL enabled protocols due to incompatible java version: " + version));
    }

    private Properties getProperties(String propertyFile, String generalPropertyFile) {
        Throwable throwable;
        InputStream stream;
        Properties p = new Properties();
        try {
            stream = this.getClass().getResourceAsStream(propertyFile);
            throwable = null;
            try {
                p.load(stream);
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
            finally {
                if (stream != null) {
                    if (throwable != null) {
                        try {
                            stream.close();
                        }
                        catch (Throwable throwable3) {
                            throwable.addSuppressed(throwable3);
                        }
                    } else {
                        stream.close();
                    }
                }
            }
        }
        catch (IOException e) {
            LOG.warn((Object)e);
            throw new ConfigurationCheckException("It was not possible to read data from the static property file: " + e.getMessage());
        }
        try {
            stream = this.getClass().getResourceAsStream(generalPropertyFile);
            throwable = null;
            try {
                if (stream == null) return p;
                p.load(stream);
            }
            catch (Throwable throwable4) {
                throwable = throwable4;
                throw throwable4;
            }
            finally {
                if (stream != null) {
                    if (throwable != null) {
                        try {
                            stream.close();
                        }
                        catch (Throwable throwable5) {
                            throwable.addSuppressed(throwable5);
                        }
                    } else {
                        stream.close();
                    }
                }
            }
        }
        catch (IOException e) {
            LOG.warn((Object)e);
            throw new ConfigurationCheckException("It was not possible to read data from the general property file: " + e.getMessage());
        }
        return p;
    }
}
