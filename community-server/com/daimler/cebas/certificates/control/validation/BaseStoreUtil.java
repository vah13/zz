/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.control.validation.StoreValidator
 *  com.daimler.cebas.certificates.entity.parsing.CertificateParser
 *  com.daimler.cebas.logs.control.Logger
 */
package com.daimler.cebas.certificates.control.validation;

import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.control.validation.StoreValidator;
import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
import com.daimler.cebas.logs.control.Logger;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Objects;
import java.util.logging.Level;

public final class BaseStoreUtil {
    private static final String CLASS_NAME = StoreValidator.class.getSimpleName();
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(CertificateParser.class.getName());
    private static Path baseStorePath;
    private static FileSystem fileSystem;

    private BaseStoreUtil() {
    }

    public static DirectoryStream<Path> getDirectoryStream(Path dir, DirectoryStream.Filter<Path> filter, Logger logger) throws IOException {
        String METHOD_NAME = "getDirectoryStream";
        logger.entering(CLASS_NAME, "getDirectoryStream");
        logger.exiting(CLASS_NAME, "getDirectoryStream");
        return Files.newDirectoryStream(dir, filter);
    }

    public static Path getBaseStoreTopPath(Class classs, Logger logger) {
        String METHOD_NAME = "getBaseStoreTopPath";
        logger.entering(CLASS_NAME, "getBaseStoreTopPath");
        try {
            URL resource = classs.getClassLoader().getResource("base_certificate_store");
            Objects.requireNonNull(resource, "Resource URL cannot be null");
            URI uri = resource.toURI();
            String scheme = uri.getScheme();
            if ("file".equals(scheme)) {
                return Paths.get(uri);
            }
            if (!"jar".equals(scheme)) {
                throw new IllegalArgumentException("Cannot convert to Path: " + uri);
            }
            String s = uri.toString();
            int separator = s.indexOf("!/");
            URI fileURI = URI.create(s.substring(0, separator));
            fileSystem = FileSystems.newFileSystem(fileURI, Collections.emptyMap());
            baseStorePath = fileSystem.getPath("/BOOT-INF/classes/base_certificate_store", new String[0]);
        }
        catch (IOException | URISyntaxException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException("Cannot create path from jar!");
        }
        logger.exiting(CLASS_NAME, "getBaseStoreTopPath");
        return baseStorePath;
    }

    public static void closeFileSystem() {
        if (fileSystem == null) return;
        try {
            fileSystem.close();
            fileSystem = null;
            baseStorePath = null;
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            throw new CEBASCertificateException("Cannot close base store file system.");
        }
    }

    public static boolean getRootDirectoriesFilter(Path path) {
        return Files.isDirectory(path, new LinkOption[0]);
    }

    public static boolean getRootFilesFilter(Path path) {
        return !Files.isDirectory(path, new LinkOption[0]);
    }

    public static String getRootCertificatePathId(Path rootPath) {
        String[] split = rootPath.getFileName().toString().split("_");
        String temp = split[split.length - 1];
        return temp.split("\\.")[0];
    }

    public static String getBackendCertificateRootCertificatePathId(Path backendPath) {
        String[] split = backendPath.getFileName().toString().split("_");
        return split[split.length - 2];
    }
}
