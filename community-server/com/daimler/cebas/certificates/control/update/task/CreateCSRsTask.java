/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.CertificateSignRequestEngine
 *  com.daimler.cebas.certificates.control.CertificateToolsProvider
 *  com.daimler.cebas.certificates.control.DeleteCertificatesEngine
 *  com.daimler.cebas.certificates.control.SearchEngine
 *  com.daimler.cebas.certificates.control.update.DefaultUpdateSession
 *  com.daimler.cebas.certificates.control.update.UpdateTask
 *  com.daimler.cebas.certificates.control.update.UpdateType
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.UserRole
 *  com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  org.springframework.beans.factory.annotation.Autowired
 */
package com.daimler.cebas.certificates.control.update.task;

import com.daimler.cebas.certificates.control.CertificateSignRequestEngine;
import com.daimler.cebas.certificates.control.CertificateToolsProvider;
import com.daimler.cebas.certificates.control.DeleteCertificatesEngine;
import com.daimler.cebas.certificates.control.SearchEngine;
import com.daimler.cebas.certificates.control.update.DefaultUpdateSession;
import com.daimler.cebas.certificates.control.update.UpdateTask;
import com.daimler.cebas.certificates.control.update.UpdateType;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.UserRole;
import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@CEBASControl
public abstract class CreateCSRsTask<T extends PublicKeyInfrastructureEsi>
extends UpdateTask<T> {
    protected static final String EMPTY = "";
    protected static final String SPACE = " ";
    private static final String CLASS_NAME = CreateCSRsTask.class.getSimpleName();
    protected SearchEngine searchEngine;
    protected CertificateSignRequestEngine certificateSignRequestEngine;
    protected DeleteCertificatesEngine deleteCertificatesEngine;
    protected Session session;

    @Autowired
    public CreateCSRsTask(CertificateToolsProvider toolsProvider, T publicKeyInfrastructureEsi, SearchEngine searchEngine, Session session, DefaultUpdateSession updateSession, Logger logger) {
        super(publicKeyInfrastructureEsi, toolsProvider.getImporter(), updateSession, logger, toolsProvider.getI18n());
        this.searchEngine = searchEngine;
        this.certificateSignRequestEngine = toolsProvider.getCertificateSignRequestEngine();
        this.deleteCertificatesEngine = toolsProvider.getDeleteCertificatesEngine();
        this.session = session;
    }

    protected void deleteNonIdenticalCertificates(List<Certificate> certificatesUnderParent, Certificate createdCSR, UpdateType updateType) {
        createdCSR.initializeTransients();
        List certificatesIdsNotTheSame = certificatesUnderParent.stream().filter(cert -> !cert.identicalWithDifference(createdCSR)).map(Certificate::getEntityId).collect(Collectors.toList());
        if (certificatesIdsNotTheSame.isEmpty()) return;
        this.logger.log(Level.INFO, "000259", this.i18n.getEnglishMessage("updateStartDeletingCertsWhichAreNotReplaced", new String[]{updateType.name()}), CLASS_NAME);
        this.deleteCertificatesEngine.deleteCertificatesDifferentTransaction(certificatesIdsNotTheSame);
        this.logger.log(Level.INFO, "000260", this.i18n.getEnglishMessage("updateEndDeletingCertsWhichAreNotReplaced", new String[]{updateType.name()}), CLASS_NAME);
    }

    protected String getUserRoleFromPermission(String byteValue) {
        if (byteValue == null) return EMPTY;
        UserRole userRole = UserRole.getUserRoleFromByte((byte)Byte.decode(byteValue));
        return userRole.getText();
    }

    protected abstract String getProdQualifier();
}
