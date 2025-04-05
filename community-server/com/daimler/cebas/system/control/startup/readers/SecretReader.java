/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.system.control.exceptions.ConfigurationCheckException
 *  com.daimler.cebas.system.control.startup.ImageConversion
 *  com.daimler.cebas.system.control.startup.readers.ReaderUtils
 *  com.daimler.cebas.system.control.startup.readers.SecretMap
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 */
package com.daimler.cebas.system.control.startup.readers;

import com.daimler.cebas.system.control.exceptions.ConfigurationCheckException;
import com.daimler.cebas.system.control.startup.ImageConversion;
import com.daimler.cebas.system.control.startup.readers.ReaderUtils;
import com.daimler.cebas.system.control.startup.readers.SecretMap;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class SecretReader {
    protected static final Log LOG = LogFactory.getLog(SecretReader.class);

    protected abstract void prepare(Properties var1, SecretMap var2);

    protected abstract SecretMap getSecret();

    protected abstract boolean overwriteHeader();

    protected abstract int maximumSecretSize();

    protected SecretMap getSecret(File file) {
        try {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            return this.getSecret(bytes);
        }
        catch (IOException e) {
            LOG.error((Object)e);
            throw new ConfigurationCheckException("Invalid DB cache file. Please delete: " + file + " and retry. " + e.getMessage());
        }
        catch (NullPointerException e) {
            LOG.error((Object)e);
            throw new ConfigurationCheckException("Invalid DB cache file. Please delete: " + file + " and retry. ");
        }
    }

    protected SecretMap getSecret(InputStream stream) {
        try {
            byte[] bytes = IOUtils.toByteArray(stream);
            SecretMap secretMap = this.getSecret(bytes);
            return secretMap;
        }
        catch (IOException e) {
            LOG.error((Object)e);
            throw new ConfigurationCheckException("Invalid configuration. Please try to reinstall. " + e.getMessage());
        }
        finally {
            try {
                stream.close();
            }
            catch (IOException e) {
                LOG.debug((Object)"Secret stream was not closed properly");
            }
        }
    }

    private SecretMap getSecret(byte[] bytes) {
        BufferedImage image = this.getImageFromBytes(bytes);
        if (image == null) throw new ConfigurationCheckException("Invalid DB cache file. Please delete, try to reinstall and retry. ");
        if (image.getWidth() == 0) throw new ConfigurationCheckException("Invalid DB cache file. Please delete, try to reinstall and retry. ");
        if (image.getHeight() == 0) {
            throw new ConfigurationCheckException("Invalid DB cache file. Please delete, try to reinstall and retry. ");
        }
        String read = ImageConversion.read((BufferedImage)image, (int)this.maximumSecretSize());
        return SecretMap.fromString((String)read);
    }

    protected final boolean fileExists(File file) {
        return !file.isDirectory() && file.exists();
    }

    protected final void createFile(File file, byte[] secret) {
        try {
            FileUtils.writeByteArrayToFile(file, secret);
        }
        catch (IOException e) {
            LOG.error((Object)e);
            throw new ConfigurationCheckException("Could not create the file: " + file + " - reason: " + e.getMessage());
        }
    }

    public final byte[] hideSecret(SecretMap secret, BufferedImage image) {
        String secretText = secret.createString(this.maximumSecretSize());
        BufferedImage secretImage = ImageConversion.write((BufferedImage)image, (String)secretText);
        return ReaderUtils.getBytesFromImage((BufferedImage)secretImage, (boolean)this.overwriteHeader());
    }

    protected final BufferedImage getImageFromBytes(byte[] imageInByte) {
        try {
            if (this.overwriteHeader()) {
                imageInByte[0] = 66;
                imageInByte[1] = 77;
            }
            ByteArrayInputStream in = new ByteArrayInputStream(imageInByte);
            return ImageIO.read(in);
        }
        catch (IOException e) {
            LOG.error((Object)e);
            throw new ConfigurationCheckException("It was not possible to write data to the property store: " + e.getMessage());
        }
    }
}
