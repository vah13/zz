/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.DeleteCertificateHandlerDefault
 *  com.daimler.cebas.certificates.control.DeleteCertificatesEngine
 *  com.daimler.cebas.certificates.control.config.handlers.IDeleteCertificateHandler
 *  com.daimler.cebas.certificates.control.update.UpdateType
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.integration.vo.Permission
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.logs.control.Logger
 */
package com.daimler.cebas.certificates.control.config;

import com.daimler.cebas.certificates.control.DeleteCertificateHandlerDefault;
import com.daimler.cebas.certificates.control.DeleteCertificatesEngine;
import com.daimler.cebas.certificates.control.config.handlers.IDeleteCertificateHandler;
import com.daimler.cebas.certificates.control.update.UpdateType;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.integration.vo.Permission;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.logs.control.Logger;
import java.util.logging.Level;

public interface CertificatesDeleteConfiguration {
    default public IDeleteCertificateHandler getDeleteCertificatesHandler(DeleteCertificatesEngine engine, Logger logger, MetadataManager i18n) {
        return new DeleteCertificateHandlerDefault(engine, logger, i18n);
    }

    default public boolean shouldDeleteDuringCSRCreation(Certificate certificate) {
        return true;
    }

    default public boolean matchEnrollmentId(Certificate certificate, Permission permission) {
        return false;
    }

    default public void logDeletedUnauthorizedCertificate(Certificate certificate, UpdateType updateType, Logger logger, MetadataManager i18n) {
        logger.log(Level.INFO, "000231", i18n.getEnglishMessage("updateDeleteCertWithAKIAndSN", new String[]{updateType.name(), certificate.getAuthorityKeyIdentifier(), certificate.getSerialNo()}), this.getClass().getSimpleName());
    }
}
