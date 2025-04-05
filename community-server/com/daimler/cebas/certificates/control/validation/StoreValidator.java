/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.control.validation.BaseStoreUtil
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.logs.control.Logger
 */
package com.daimler.cebas.certificates.control.validation;

import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.control.validation.BaseStoreUtil;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.logs.control.Logger;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;

public final class StoreValidator {
    private static final String CLASS_NAME = StoreValidator.class.getSimpleName();
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(StoreValidator.class.getSimpleName());

    private StoreValidator() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     * Enabled unnecessary exception pruning
     */
    public static void validateBaseStore(Class classs, MetadataManager i18n, Logger logger) {
        String METHOD_NAME;
        block14: {
            DirectoryStream topDirectories;
            Path topDir;
            block12: {
                METHOD_NAME = "validateBaseStore";
                logger.entering(CLASS_NAME, METHOD_NAME);
                topDir = BaseStoreUtil.getBaseStoreTopPath((Class)classs, (Logger)logger);
                topDirectories = null;
                topDirectories = BaseStoreUtil.getDirectoryStream((Path)topDir, BaseStoreUtil::getRootDirectoriesFilter, (Logger)logger);
                ArrayList rootCertificatesPaths = new ArrayList();
                ArrayList rootCertificatesFolders = new ArrayList();
                topDirectories.forEach(pathParent -> {
                    rootCertificatesFolders.add(pathParent);
                    rootCertificatesPaths.clear();
                    StoreValidator.extractDirectoryStream(pathParent, rootCertificatesPaths, BaseStoreUtil::getRootFilesFilter, logger);
                    if (!rootCertificatesPaths.isEmpty()) return;
                    logger.log(Level.INFO, "000588X", "foundRootFolderEmpty", CLASS_NAME);
                    throw new CEBASCertificateException(i18n.getMessage("foundRootFolderEmpty"));
                });
                if (rootCertificatesFolders.isEmpty()) {
                    logger.log(Level.INFO, "000589X", "noRootFoldersFound", CLASS_NAME);
                    throw new CEBASCertificateException(i18n.getMessage("noRootFoldersFound"));
                }
                rootCertificatesPaths.clear();
                rootCertificatesFolders.clear();
                if (topDirectories == null) break block12;
                try {
                    topDirectories.close();
                }
                catch (IOException e) {
                    LOG.log(Level.FINEST, e.getMessage(), e);
                    logger.log(Level.WARNING, "000168X", i18n.getEnglishMessage("failedToGetDirectoryStream", new String[]{topDir.toString()}), CLASS_NAME);
                }
            }
            BaseStoreUtil.closeFileSystem();
            break block14;
            catch (IOException e) {
                block13: {
                    try {
                        LOG.log(Level.FINEST, e.getMessage(), e);
                        logger.log(Level.WARNING, "000170X", "failedToGetDirectoryStream", CLASS_NAME);
                        if (topDirectories == null) break block13;
                    }
                    catch (Throwable throwable) {
                        if (topDirectories != null) {
                            try {
                                topDirectories.close();
                            }
                            catch (IOException e2) {
                                LOG.log(Level.FINEST, e2.getMessage(), e2);
                                logger.log(Level.WARNING, "000168X", i18n.getEnglishMessage("failedToGetDirectoryStream", new String[]{topDir.toString()}), CLASS_NAME);
                            }
                        }
                        BaseStoreUtil.closeFileSystem();
                        throw throwable;
                    }
                    try {
                        topDirectories.close();
                    }
                    catch (IOException e3) {
                        LOG.log(Level.FINEST, e3.getMessage(), e3);
                        logger.log(Level.WARNING, "000168X", i18n.getEnglishMessage("failedToGetDirectoryStream", new String[]{topDir.toString()}), CLASS_NAME);
                    }
                }
                BaseStoreUtil.closeFileSystem();
            }
        }
        logger.exiting(CLASS_NAME, METHOD_NAME);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void extractDirectoryStream(Path path, List<Path> pathList, Predicate<Path> predicate, Logger logger) {
        Closeable directoryStream = null;
        try {
            directoryStream = BaseStoreUtil.getDirectoryStream((Path)path, predicate::test, (Logger)logger);
            directoryStream.forEach(pathList::add);
        }
        catch (IOException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            logger.log(Level.WARNING, "000166X", "failedToProcessDirectoryStream", CLASS_NAME);
        }
        finally {
            try {
                if (directoryStream != null) {
                    directoryStream.close();
                }
            }
            catch (IOException e) {
                LOG.log(Level.FINEST, e.getMessage(), e);
                logger.log(Level.WARNING, "000165X", "cannotCloseDirectoryStreamAfterProcessingValidation", CLASS_NAME);
            }
        }
    }
}
