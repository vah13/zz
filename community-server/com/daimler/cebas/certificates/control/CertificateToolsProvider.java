/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.CertificateSignRequestEngine
 *  com.daimler.cebas.certificates.control.DeleteCertificatesEngine
 *  com.daimler.cebas.certificates.control.ImportCertificatesEngine
 *  com.daimler.cebas.certificates.control.SearchEngine
 *  com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory
 *  com.daimler.cebas.certificates.control.validation.SystemIntegrityChecker
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  org.springframework.beans.factory.annotation.Autowired
 */
package com.daimler.cebas.certificates.control;

import com.daimler.cebas.certificates.control.CertificateSignRequestEngine;
import com.daimler.cebas.certificates.control.DeleteCertificatesEngine;
import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
import com.daimler.cebas.certificates.control.SearchEngine;
import com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory;
import com.daimler.cebas.certificates.control.validation.SystemIntegrityChecker;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import org.springframework.beans.factory.annotation.Autowired;

@CEBASControl
public class CertificateToolsProvider {
    private ImportCertificatesEngine importer;
    private SearchEngine searchEngine;
    private SystemIntegrityChecker systemIntegrityCheck;
    private AbstractCertificateFactory factory;
    private MetadataManager i18n;
    private DeleteCertificatesEngine deleteCertificatesEngine;
    private CertificateSignRequestEngine certificateSignRequestEngine;

    @Autowired
    public CertificateToolsProvider(ImportCertificatesEngine importer, SearchEngine searchEngine, SystemIntegrityChecker systemIntegrityCheck, AbstractCertificateFactory factory, MetadataManager i18n, DeleteCertificatesEngine deleteCertificatesEngine, CertificateSignRequestEngine certificateSignRequestEngine) {
        this.importer = importer;
        this.systemIntegrityCheck = systemIntegrityCheck;
        this.factory = factory;
        this.i18n = i18n;
        this.deleteCertificatesEngine = deleteCertificatesEngine;
        this.certificateSignRequestEngine = certificateSignRequestEngine;
        this.searchEngine = searchEngine;
    }

    public ImportCertificatesEngine getImporter() {
        return this.importer;
    }

    public SearchEngine getSearchEngine() {
        return this.searchEngine;
    }

    public SystemIntegrityChecker getSystemIntegrityCheck() {
        return this.systemIntegrityCheck;
    }

    public AbstractCertificateFactory getFactory() {
        return this.factory;
    }

    public MetadataManager getI18n() {
        return this.i18n;
    }

    public DeleteCertificatesEngine getDeleteCertificatesEngine() {
        return this.deleteCertificatesEngine;
    }

    public CertificateSignRequestEngine getCertificateSignRequestEngine() {
        return this.certificateSignRequestEngine;
    }
}
