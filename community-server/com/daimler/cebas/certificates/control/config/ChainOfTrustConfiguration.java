/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.SearchEngine
 *  com.daimler.cebas.certificates.control.chain.AddUnderBackendHandlerDefault
 *  com.daimler.cebas.certificates.control.chain.UnderBackendChainOfTrust
 *  com.daimler.cebas.certificates.control.config.handlers.IAddUnderBackendHandler
 *  com.daimler.cebas.certificates.control.config.handlers.IPkiKnownHandler
 */
package com.daimler.cebas.certificates.control.config;

import com.daimler.cebas.certificates.control.SearchEngine;
import com.daimler.cebas.certificates.control.chain.AddUnderBackendHandlerDefault;
import com.daimler.cebas.certificates.control.chain.UnderBackendChainOfTrust;
import com.daimler.cebas.certificates.control.config.handlers.IAddUnderBackendHandler;
import com.daimler.cebas.certificates.control.config.handlers.IPkiKnownHandler;

public interface ChainOfTrustConfiguration {
    public boolean shouldDoExtendedValidation();

    public boolean shouldReplaceECUCertificate();

    default public IAddUnderBackendHandler getAddBackendHandler(UnderBackendChainOfTrust chain, SearchEngine searchEngine) {
        return new AddUnderBackendHandlerDefault(chain, searchEngine);
    }

    public IPkiKnownHandler getPkiKnownHandler();
}
