/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.DeleteCertificatesEngine
 *  com.daimler.cebas.certificates.control.SearchEngine
 *  com.daimler.cebas.certificates.control.chain.events.DeleteCertsFromKnownBackends
 *  com.daimler.cebas.certificates.control.vo.ImportResult
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.users.control.Session
 *  org.springframework.context.event.EventListener
 *  org.springframework.transaction.annotation.Transactional
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.DeleteCertificatesEngine;
import com.daimler.cebas.certificates.control.SearchEngine;
import com.daimler.cebas.certificates.control.chain.events.DeleteCertsFromKnownBackends;
import com.daimler.cebas.certificates.control.vo.ImportResult;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.users.control.Session;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

@CEBASControl
public class DeleteEventsForKnownPKIBackendsListener {
    private DeleteCertificatesEngine deleteCertificatesEngine;
    private SearchEngine searchEngine;
    private Session session;

    public DeleteEventsForKnownPKIBackendsListener(Session session, SearchEngine searchEngine, DeleteCertificatesEngine deleteCertificatesEngine) {
        this.deleteCertificatesEngine = deleteCertificatesEngine;
        this.searchEngine = searchEngine;
        this.session = session;
    }

    @EventListener
    @Transactional
    public void deleteUnderBackend(DeleteCertsFromKnownBackends event) {
        List backends = event.getBackends();
        backends.forEach(this::delete);
    }

    private void delete(ImportResult backend) {
        Optional backendCertOptional = this.searchEngine.findCertificateBySkiAkiAndType(this.session.getCurrentUser(), backend.getSubjectKeyIdentifier(), backend.getAuthorityKeyIdentifier(), CertificateType.BACKEND_CA_CERTIFICATE);
        if (!backendCertOptional.isPresent()) return;
        Certificate backendCertificate = (Certificate)backendCertOptional.get();
        List ids = backendCertificate.getChildren().stream().filter(this::noTime).filter(this::noVSMCertificate).map(Certificate::getEntityId).collect(Collectors.toList());
        if (ids.isEmpty()) {
            return;
        }
        this.deleteCertificatesEngine.deleteCertificates(ids);
    }

    private boolean noVSMCertificate(Certificate certificate) {
        return this.searchEngine.hasPrivateKey(certificate);
    }

    private boolean noTime(Certificate certificate) {
        return certificate.getType() != CertificateType.TIME_CERTIFICATE;
    }
}
