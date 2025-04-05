/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.ImportCertificatesEngine
 *  com.daimler.cebas.certificates.control.update.UpdateType
 *  com.daimler.cebas.certificates.control.update.task.UpdateBackendsTask
 *  com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.system.control.exceptions.UnauthorizedOperationException
 *  com.daimler.cebas.users.control.Session
 *  org.springframework.transaction.annotation.Propagation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.daimler.cebas.certificates.control.zkNoSupport;

import com.daimler.cebas.certificates.control.ImportCertificatesEngine;
import com.daimler.cebas.certificates.control.update.UpdateType;
import com.daimler.cebas.certificates.control.update.task.UpdateBackendsTask;
import com.daimler.cebas.certificates.integration.PublicKeyInfrastructureEsi;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.system.control.exceptions.UnauthorizedOperationException;
import com.daimler.cebas.users.control.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@CEBASControl
public class MappingsUpdater<T extends PublicKeyInfrastructureEsi> {
    private PublicKeyInfrastructureEsi esi;
    private UpdateBackendsTask<T> updateBackendTask;
    private ImportCertificatesEngine importEngine;
    private Session session;

    public MappingsUpdater(Session session, UpdateBackendsTask<T> updateBackendTask, ImportCertificatesEngine importEngine, PublicKeyInfrastructureEsi esi) {
        this.esi = esi;
        this.updateBackendTask = updateBackendTask;
        this.importEngine = importEngine;
        this.session = session;
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public boolean updateMappings() {
        if (this.session.isDefaultUser()) {
            throw new UnauthorizedOperationException("Only registered users can update mappings");
        }
        if (this.session.getToken() == null) {
            throw new UnauthorizedOperationException("Authentication with backend is required");
        }
        List backendIdentifiers = this.esi.getBackendIdentifiersWithoutSession();
        Set base64Certificates = this.esi.getCertificatesChain(backendIdentifiers);
        this.importEngine.importCertificatesFromBase64NewTransaction(new ArrayList(base64Certificates), true, false);
        this.updateBackendTask.updateBackends(UpdateType.ZK_NO_MAPPING, backendIdentifiers);
        return true;
    }
}
