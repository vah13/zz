/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.logs.control.Logger
 *  org.springframework.core.io.Resource
 *  org.springframework.core.io.ResourceLoader
 */
package com.daimler.cebas.configuration.control;

import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.logs.control.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public abstract class CeBASGeneralPropertiesManager {
    private final String CLASS_NAME = "CeBASGeneralPropertiesManager";
    private ResourceLoader resourceLoader;
    private Logger logger;
    private MetadataManager i18n;
    private Properties properties;

    public CeBASGeneralPropertiesManager(Logger logger, MetadataManager i18n, ResourceLoader resourceLoader, String fileName) {
        this.logger = logger;
        this.i18n = i18n;
        this.resourceLoader = resourceLoader;
        this.initializeGeneralProperties(fileName);
    }

    public String getProperty(String key) {
        return this.properties != null ? this.properties.getProperty(key) : null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void initializeGeneralProperties(String propertiesFileName) {
        InputStream fileInputStream = null;
        try {
            Resource resource = this.resourceLoader.getResource("classpath:" + propertiesFileName);
            if (resource == null) return;
            if (!resource.exists()) {
                return;
            }
            fileInputStream = resource.getInputStream();
            this.properties = new Properties();
            this.properties.load(fileInputStream);
        }
        catch (IOException e) {
            this.logCannotAccessGeneralResources(e);
        }
        finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                }
                catch (IOException e) {
                    this.logCannotAccessGeneralResources(e);
                }
            }
        }
    }

    public Set<Map.Entry<Object, Object>> getGeneralPropertiesEntries() {
        return this.properties != null ? this.properties.entrySet() : null;
    }

    public Properties getProperties() {
        return this.properties;
    }

    private void logCannotAccessGeneralResources(IOException e) {
        this.logger.log(Level.WARNING, "000216X", this.i18n.getEnglishMessage("errorReadingZenZefiGeneralProperties", new String[]{e.getMessage()}), "CeBASGeneralPropertiesManager");
    }
}
