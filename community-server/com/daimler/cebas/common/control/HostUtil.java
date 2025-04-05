/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.logs.control.Logger
 */
package com.daimler.cebas.common.control;

import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.logs.control.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.logging.Level;
import org.apache.commons.io.input.BoundedInputStream;
import org.apache.commons.lang3.StringUtils;

public final class HostUtil {
    private static final String EMPTY = "";
    private static final String CLASS_NAME = HostUtil.class.getSimpleName();
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(CLASS_NAME);
    private static final String HOSTNAME = "hostname";
    private static final String OS_NAME = "os.name";
    private static final String COMPUTERNAME = "COMPUTERNAME";
    private static final String WIN = "win";
    private static final String NUX = "nux";
    private static final String NIX = "nix";

    private HostUtil() {
    }

    public static String getMacAddress(Logger logger) {
        String METHOD_NAME = "getMacAddress";
        logger.entering(CLASS_NAME, "getMacAddress");
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                logger.exiting(CLASS_NAME, "getMacAddress");
                return "00-00-00-00-00-00";
            }
            byte[] mac = network.getHardwareAddress();
            if (mac == null) {
                logger.exiting(CLASS_NAME, "getMacAddress");
                return "00-00-00-00-00-00";
            }
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (true) {
                if (i >= mac.length) {
                    logger.exiting(CLASS_NAME, "getMacAddress");
                    return sb.toString();
                }
                sb.append(String.format("%02X%s", mac[i], i < mac.length - 1 ? "-" : EMPTY));
                ++i;
            }
        }
        catch (Exception e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASException(e.getMessage());
        }
    }

    public static String getMachineName(Logger logger, MetadataManager i18n) {
        String METHOD_NAME = "getMachineName";
        logger.entering(CLASS_NAME, "getMachineName");
        logger.exiting(CLASS_NAME, "getMachineName");
        return HostUtil.getHostName(logger, i18n);
    }

    private static String getHostName(Logger logger, MetadataManager i18n) {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostName = addr.getHostName();
            if (StringUtils.isEmpty(hostName)) return HostUtil.getHostNameFromSystemVariables(logger, i18n);
            logger.log(Level.INFO, "000218", i18n.getEnglishMessage("hostNameTakenFromInetAddr"), CLASS_NAME);
            return hostName;
        }
        catch (Exception e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            return HostUtil.getHostNameFromSystemVariables(logger, i18n);
        }
    }

    public static String getHostNameFromSystemVariables(Logger logger, MetadataManager i18n) {
        String hostName = EMPTY;
        try {
            String os = System.getProperty(OS_NAME).toLowerCase();
            if (os.contains(WIN)) {
                hostName = System.getenv(COMPUTERNAME);
            } else if (os.contains(NUX) || os.contains(NIX)) {
                hostName = System.getenv(HOSTNAME.toUpperCase());
            }
        }
        catch (SecurityException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            return HostUtil.getHostFromNativeCommand(logger, i18n);
        }
        if (StringUtils.isEmpty(hostName)) return HostUtil.getHostFromNativeCommand(logger, i18n);
        logger.log(Level.INFO, "000219", i18n.getEnglishMessage("hostNameTakenFromEnvVariables"), CLASS_NAME);
        return hostName;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     * Enabled unnecessary exception pruning
     */
    public static String getHostFromNativeCommand(Logger logger, MetadataManager i18n) {
        String hostName;
        block23: {
            BufferedReader reader;
            BoundedInputStream inputStream;
            block21: {
                String line;
                inputStream = null;
                reader = null;
                hostName = EMPTY;
                Process process = Runtime.getRuntime().exec(HOSTNAME);
                inputStream = new BoundedInputStream(process.getInputStream(), 2000L);
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                    result.append(" ");
                }
                hostName = result.toString().trim();
                if (reader == null) break block21;
                try {
                    reader.close();
                }
                catch (IOException e) {
                    LOG.log(Level.FINEST, e.getMessage(), e);
                    logger.log(Level.WARNING, "000221X", i18n.getEnglishMessage("unableToRetrieveHostName", new String[]{e.getMessage()}), CLASS_NAME);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    LOG.log(Level.FINEST, e.getMessage(), e);
                    logger.log(Level.WARNING, "000221X", i18n.getEnglishMessage("unableToRetrieveHostName", new String[]{e.getMessage()}), CLASS_NAME);
                }
            }
            break block23;
            catch (IOException e) {
                block22: {
                    try {
                        LOG.log(Level.FINEST, e.getMessage(), e);
                        logger.log(Level.WARNING, "000221X", i18n.getEnglishMessage("unableToRetrieveHostName", new String[]{e.getMessage()}), CLASS_NAME);
                        if (reader == null) break block22;
                    }
                    catch (Throwable throwable) {
                        if (reader != null) {
                            try {
                                reader.close();
                            }
                            catch (IOException e2) {
                                LOG.log(Level.FINEST, e2.getMessage(), e2);
                                logger.log(Level.WARNING, "000221X", i18n.getEnglishMessage("unableToRetrieveHostName", new String[]{e2.getMessage()}), CLASS_NAME);
                            }
                        }
                        if (inputStream == null) throw throwable;
                        try {
                            inputStream.close();
                            throw throwable;
                        }
                        catch (IOException e3) {
                            LOG.log(Level.FINEST, e3.getMessage(), e3);
                            logger.log(Level.WARNING, "000221X", i18n.getEnglishMessage("unableToRetrieveHostName", new String[]{e3.getMessage()}), CLASS_NAME);
                        }
                        throw throwable;
                    }
                    try {
                        reader.close();
                    }
                    catch (IOException e4) {
                        LOG.log(Level.FINEST, e4.getMessage(), e4);
                        logger.log(Level.WARNING, "000221X", i18n.getEnglishMessage("unableToRetrieveHostName", new String[]{e4.getMessage()}), CLASS_NAME);
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    }
                    catch (IOException e5) {
                        LOG.log(Level.FINEST, e5.getMessage(), e5);
                        logger.log(Level.WARNING, "000221X", i18n.getEnglishMessage("unableToRetrieveHostName", new String[]{e5.getMessage()}), CLASS_NAME);
                    }
                }
            }
        }
        if (!StringUtils.isEmpty(hostName)) {
            logger.log(Level.INFO, "000220", i18n.getEnglishMessage("hostNameTakenFromNativeCommand"), CLASS_NAME);
            return hostName;
        }
        logger.log(Level.WARNING, "000221X", i18n.getEnglishMessage("unableToRetrieveHostName", new String[]{EMPTY}), CLASS_NAME);
        return hostName;
    }
}
