/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.SearchEngine
 *  com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder
 *  com.daimler.cebas.certificates.control.chain.UnderBackendChainOfTrust
 *  com.daimler.cebas.certificates.control.config.handlers.IAddUnderBackendHandler
 *  com.daimler.cebas.certificates.control.validation.ValidationError
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.users.entity.User
 */
package com.daimler.cebas.certificates.control.chain;

import com.daimler.cebas.certificates.control.SearchEngine;
import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
import com.daimler.cebas.certificates.control.chain.UnderBackendChainOfTrust;
import com.daimler.cebas.certificates.control.config.handlers.IAddUnderBackendHandler;
import com.daimler.cebas.certificates.control.validation.ValidationError;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.users.entity.User;
import java.util.List;
import java.util.Optional;

public class AddUnderBackendHandlerDefault<T extends Certificate>
implements IAddUnderBackendHandler<T> {
    protected UnderBackendChainOfTrust chain;
    protected SearchEngine searchEngine;

    public AddUnderBackendHandlerDefault(UnderBackendChainOfTrust chain, SearchEngine searchEngine) {
        this.chain = chain;
        this.searchEngine = searchEngine;
    }

    public boolean addUnderBackend(CertificatePrivateKeyHolder holder, List<ValidationError> errors, T backend) {
        boolean result = false;
        Certificate certificate = holder.getCertificate();
        User user = certificate.getUser();
        Optional csr = this.searchEngine.findCsrByPublicKey(user, certificate.getSubjectPublicKey());
        boolean extendedValidation = this.chain.profileConfiguration.shouldDoExtendedValidation();
        String authorityKeyIdentifier = certificate.getAuthorityKeyIdentifier();
        String serialNo = certificate.getSerialNo();
        if (!extendedValidation || this.chain.repo.checkUniqueAuthKeyAndSN(user, authorityKeyIdentifier, serialNo, backend)) {
            if (holder.hasPrivateKey() && csr.isPresent()) {
                this.chain.errorHasPrivateKeyAndFoundCSR(certificate, errors);
            } else {
                result = holder.hasPrivateKey() && !csr.isPresent() ? this.chain.addCertificateWithPrivateKey(holder, errors, backend, certificate, extendedValidation) : (!holder.hasPrivateKey() && csr.isPresent() ? this.chain.addCertificateWithCSR(holder, errors, certificate, (Certificate)csr.get(), extendedValidation) : this.chain.checkEcuCertAndCsrExists(errors, backend, certificate));
            }
        } else {
            this.chain.errorCertificateWithSameAuthKeyAndSerialNumber(certificate, errors);
        }
        return result;
    }
}
