/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.system.control.exceptions.ConfigurationCheckException
 *  com.daimler.cebas.system.control.startup.CeBASStartupProperty
 *  com.daimler.cebas.system.control.startup.ImageConversion
 *  com.daimler.cebas.system.control.startup.readers.ReaderUtils
 *  com.daimler.cebas.system.control.startup.readers.SecretMap
 *  com.daimler.cebas.system.control.startup.readers.SecretReader
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 */
package com.daimler.cebas.system.control.startup.readers;

import com.daimler.cebas.system.control.exceptions.ConfigurationCheckException;
import com.daimler.cebas.system.control.startup.CeBASStartupProperty;
import com.daimler.cebas.system.control.startup.ImageConversion;
import com.daimler.cebas.system.control.startup.readers.ReaderUtils;
import com.daimler.cebas.system.control.startup.readers.SecretMap;
import com.daimler.cebas.system.control.startup.readers.SecretReader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DynamicSecretReader
extends SecretReader {
    protected static final Log LOG = LogFactory.getLog(DynamicSecretReader.class);
    public static final int MAXIMUM_SECRET_BYTES = 1500;
    protected static final String PROPERTY_DATA_PATH = "program.data.path";
    protected static final String PROPERTY_DB_NAME = "datasource.name";
    protected static final String DYNAMIC_FILE_EXTENSION = ".cache";
    protected static final String DB_EXTENSION = ".mv.db";
    private Properties properties;
    private String staticSecret;

    protected void prepare(Properties properties, SecretMap secret) {
        this.properties = properties;
        this.staticSecret = secret.get(CeBASStartupProperty.SECRET);
    }

    protected SecretMap getSecret() {
        File file = this.getSecretFileLocation();
        if (this.fileExists(file)) return this.getSecret(file);
        byte[] fileData = this.existingInstallation() ? this.createFileDataFromStaticSecret() : this.createFileDataFromDynamicSecret();
        this.createFile(file, fileData);
        return this.getSecret(file);
    }

    protected boolean overwriteHeader() {
        return true;
    }

    protected int maximumSecretSize() {
        return 1500;
    }

    private File getSecretFileLocation() {
        String dataPath = this.properties.getProperty(PROPERTY_DATA_PATH);
        String dbName = this.properties.getProperty(PROPERTY_DB_NAME);
        if (dataPath == null) throw new ConfigurationCheckException("The configuration is not valid. At least one of the following required properties is not set: program.data.path / datasource.name");
        if (dbName != null) return new File(dataPath, dbName + DYNAMIC_FILE_EXTENSION);
        throw new ConfigurationCheckException("The configuration is not valid. At least one of the following required properties is not set: program.data.path / datasource.name");
    }

    private boolean existingInstallation() {
        String dataPath = this.properties.getProperty(PROPERTY_DATA_PATH);
        String dbName = this.properties.getProperty(PROPERTY_DB_NAME);
        if (dataPath == null) {
            return false;
        }
        File folder = new File(dataPath);
        if (!folder.exists()) {
            return false;
        }
        File[] files = folder.listFiles();
        if (files == null) {
            return false;
        }
        File[] fileArray = files;
        int n = fileArray.length;
        int n2 = 0;
        while (n2 < n) {
            File file = fileArray[n2];
            if (file.isFile() && (dbName + DB_EXTENSION).equals(file.getName())) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    private byte[] createFileDataFromDynamicSecret() {
        BufferedImage randomImage = ImageConversion.createRandomImage();
        String randomSecretString = ReaderUtils.createNewRandomSecretString();
        SecretMap s = new SecretMap();
        s.put(CeBASStartupProperty.SECRET, randomSecretString);
        return this.hideSecret(s, randomImage);
    }

    private byte[] createFileDataFromStaticSecret() {
        BufferedImage randomImage = ImageConversion.createRandomImage();
        SecretMap s = new SecretMap();
        s.put(CeBASStartupProperty.SECRET, this.staticSecret);
        return this.hideSecret(s, randomImage);
    }
}
