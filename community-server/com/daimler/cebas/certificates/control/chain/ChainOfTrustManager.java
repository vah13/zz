/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.chain.BackendChainOfTrust
 *  com.daimler.cebas.certificates.control.chain.BackendLinkChainOfTrust
 *  com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder
 *  com.daimler.cebas.certificates.control.chain.ChainOfTrustManager$1
 *  com.daimler.cebas.certificates.control.chain.EnhancedRightsChainOfTrust
 *  com.daimler.cebas.certificates.control.chain.RootChainOfTrust
 *  com.daimler.cebas.certificates.control.chain.RootLinkChainOfTrust
 *  com.daimler.cebas.certificates.control.chain.UnderBackendChainOfTrust
 *  com.daimler.cebas.certificates.control.validation.ValidationError
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.logs.control.Logger
 *  org.springframework.beans.factory.annotation.Autowired
 */
package com.daimler.cebas.certificates.control.chain;

import com.daimler.cebas.certificates.control.chain.BackendChainOfTrust;
import com.daimler.cebas.certificates.control.chain.BackendLinkChainOfTrust;
import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
import com.daimler.cebas.certificates.control.chain.ChainOfTrustManager;
import com.daimler.cebas.certificates.control.chain.EnhancedRightsChainOfTrust;
import com.daimler.cebas.certificates.control.chain.RootChainOfTrust;
import com.daimler.cebas.certificates.control.chain.RootLinkChainOfTrust;
import com.daimler.cebas.certificates.control.chain.UnderBackendChainOfTrust;
import com.daimler.cebas.certificates.control.validation.ValidationError;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.logs.control.Logger;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@CEBASControl
public final class ChainOfTrustManager {
    private static final String CLASS_NAME = ChainOfTrustManager.class.getSimpleName();
    private Logger logger;
    private final RootChainOfTrust rootChainOfTrust;
    private final RootLinkChainOfTrust rootLinkChainOfTrust;
    private final BackendChainOfTrust backendChainOfTrust;
    private final BackendLinkChainOfTrust backendLinkChainOfTrust;
    private final UnderBackendChainOfTrust underBackendChainOfTrust;
    private final EnhancedRightsChainOfTrust enhancedRightsChainOfTrust;

    @Autowired
    public ChainOfTrustManager(RootChainOfTrust rootChainOfTrust, RootLinkChainOfTrust rootLinkChainOfTrust, BackendChainOfTrust backendChainOfTrust, BackendLinkChainOfTrust backendLinkChainOfTrust, UnderBackendChainOfTrust underBackendChainOfTrust, EnhancedRightsChainOfTrust enhancedRightsChainOfTrust, Logger logger) {
        this.rootChainOfTrust = rootChainOfTrust;
        this.rootLinkChainOfTrust = rootLinkChainOfTrust;
        this.backendChainOfTrust = backendChainOfTrust;
        this.backendLinkChainOfTrust = backendLinkChainOfTrust;
        this.underBackendChainOfTrust = underBackendChainOfTrust;
        this.enhancedRightsChainOfTrust = enhancedRightsChainOfTrust;
        this.logger = logger;
    }

    public final List<ValidationError> checkChainOfTrust(List<Certificate> userStoreRootCertificate, CertificatePrivateKeyHolder holder) {
        String METHOD_NAME = "checkChainOfTrust";
        this.logger.entering(CLASS_NAME, "checkChainOfTrust");
        ArrayList<ValidationError> errors = new ArrayList<ValidationError>();
        this.doChain(userStoreRootCertificate, holder, errors, false, false);
        this.logger.exiting(CLASS_NAME, "checkChainOfTrust");
        return errors;
    }

    public final List<ValidationError> addCertificateToUserStore(List<Certificate> userStoreRootCertificate, CertificatePrivateKeyHolder holder, boolean onlyFromPKI) {
        String METHOD_NAME = "addCertificateToUserStore";
        this.logger.entering(CLASS_NAME, "addCertificateToUserStore");
        ArrayList<ValidationError> errors = new ArrayList<ValidationError>();
        this.doChain(userStoreRootCertificate, holder, errors, onlyFromPKI, true);
        this.logger.exiting(CLASS_NAME, "addCertificateToUserStore");
        return errors;
    }

    private void doChain(List<Certificate> userStoreRootCertificate, CertificatePrivateKeyHolder holder, List<ValidationError> errors, boolean onlyFromPKI, boolean add) {
        switch (1.$SwitchMap$com$daimler$cebas$certificates$entity$CertificateType[holder.getType().ordinal()]) {
            case 1: {
                this.rootChainOfTrust.check(userStoreRootCertificate, holder, errors, onlyFromPKI, add);
                break;
            }
            case 2: {
                this.rootLinkChainOfTrust.check(userStoreRootCertificate, holder, errors, onlyFromPKI, add);
                break;
            }
            case 3: {
                this.backendChainOfTrust.check(userStoreRootCertificate, holder, errors, onlyFromPKI, add);
                break;
            }
            case 4: {
                this.backendLinkChainOfTrust.check(userStoreRootCertificate, holder, errors, onlyFromPKI, add);
                break;
            }
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: {
                this.underBackendChainOfTrust.check(userStoreRootCertificate, holder, errors, onlyFromPKI, add);
                break;
            }
            case 10: {
                this.enhancedRightsChainOfTrust.check(userStoreRootCertificate, holder, errors, onlyFromPKI, add);
                break;
            }
        }
    }
}
