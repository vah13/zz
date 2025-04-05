/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.CertificateCryptoEngine
 *  com.daimler.cebas.certificates.control.SearchEngine
 *  com.daimler.cebas.certificates.control.exceptions.CertificateSigningException
 *  com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.control.Session
 *  com.daimler.cebas.users.entity.UserKeyPair
 */
package com.daimler.cebas.certificates.control;

import com.daimler.cebas.certificates.CertificateCryptoEngine;
import com.daimler.cebas.certificates.control.SearchEngine;
import com.daimler.cebas.certificates.control.exceptions.CertificateSigningException;
import com.daimler.cebas.certificates.control.factories.AbstractCertificateFactory;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.control.Session;
import com.daimler.cebas.users.entity.UserKeyPair;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.logging.Level;

public class SignCodingDataEngine {
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(SignCodingDataEngine.class.getName());
    protected SearchEngine searchEngine;
    protected AbstractCertificateFactory factory;
    protected Session session;
    protected Logger logger;
    protected MetadataManager i18n;

    public SignCodingDataEngine(SearchEngine searchEngine, AbstractCertificateFactory factory, Session session, Logger logger, MetadataManager i18n) {
        this.searchEngine = searchEngine;
        this.factory = factory;
        this.session = session;
        this.logger = logger;
        this.i18n = i18n;
    }

    protected byte[] signDataWithCertificatePrivateKey(Certificate certificate, byte[] codingDataRaw) {
        UserKeyPair userKeyPair = this.searchEngine.getUserKeyPairForCertificate(this.session.getCurrentUser(), certificate);
        return this.signDataWithPrivateKey(userKeyPair, codingDataRaw);
    }

    public byte[] signDataWithPrivateKey(UserKeyPair userKeyPair, byte[] codingDataRaw) {
        byte[] decodedPrivateKey = new byte[]{};
        try {
            decodedPrivateKey = this.session.getCryptoEngine().decodePrivateKeyToByteArray(this.session.getContainerKey(), userKeyPair.getPrivateKey());
            byte[] byArray = CertificateCryptoEngine.signDataWithPrivateKey((byte[])decodedPrivateKey, (byte[])codingDataRaw);
            return byArray;
        }
        catch (GeneralSecurityException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            CertificateSigningException ex = new CertificateSigningException(this.i18n.getMessage("signChallangeWithPrivateKeyFailed"), "signChallangeWithPrivateKeyFailed");
            this.logger.logWithTranslation(Level.WARNING, "000074X", ex.getMessageId(), new String[]{e.getMessage()}, ex.getClass().getSimpleName());
            throw ex;
        }
        finally {
            Arrays.fill(decodedPrivateKey, (byte)0);
        }
    }
}
