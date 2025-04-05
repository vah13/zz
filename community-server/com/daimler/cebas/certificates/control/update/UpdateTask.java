/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.CertificateUtil
 *  com.daimler.cebas.certificates.control.ImportCertificatesEngine
 *  com.daimler.cebas.certificates.control.update.DefaultUpdateSession
 *  com.daimler.cebas.certificates.control.update.UpdateType
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.logs.control.Logger
 */
package com.daimler.cebas.certificates.control.update;

import com.daimler.cebas.certificates.control.CertificateUtil;
import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
import com.daimler.cebas.certificates.control.update.UpdateType;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.logs.control.Logger;
import java.util.logging.Level;

public abstract class UpdateTask<T extends PublicKeyInfrastructureEsi> {
    protected T publicKeyInfrastructureEsi;
    protected ImportCertificatesEngine importCertificatesEngine;
    protected DefaultUpdateSession updateSession;
    protected Logger logger;
    protected MetadataManager i18n;

    public UpdateTask(T publicKeyInfrastructureEsi, ImportCertificatesEngine importCertificatesEngine, DefaultUpdateSession updateSession, Logger logger, MetadataManager i18n) {
        this.publicKeyInfrastructureEsi = publicKeyInfrastructureEsi;
        this.importCertificatesEngine = importCertificatesEngine;
        this.updateSession = updateSession;
        this.logger = logger;
        this.i18n = i18n;
    }

    protected boolean isCertificateValidityExceeded(UpdateType updateType, Certificate certificate, String renewal, String className) {
        boolean result = CertificateUtil.isCertificateValidityExceedingMinimumRenewal((Certificate)certificate, (String)renewal);
        if (result) return result;
        this.logger.log(Level.FINER, "000278", this.i18n.getEnglishMessage("updateDidNotCreateCSRCertStillValid", new String[]{updateType.name(), certificate.getAuthorityKeyIdentifier(), certificate.getSerialNo()}), className);
        return result;
    }
}
