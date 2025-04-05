/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.logs.control.Logger
 */
package com.daimler.cebas.security;

import com.daimler.cebas.logs.control.Logger;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.lang3.ArrayUtils;

public class CompositeX509TrustManager
implements X509TrustManager {
    private final List<X509TrustManager> trustManagers;
    private final Logger logger;
    private final String CLASS_NAME;

    public CompositeX509TrustManager(Logger logger, X509TrustManager ... trustManagers) {
        this.logger = logger;
        this.trustManagers = Arrays.asList(trustManagers);
        this.CLASS_NAME = this.getClass().getSimpleName();
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        ArrayList<CertificateException> exceptions = new ArrayList<CertificateException>();
        for (X509TrustManager trustManager : this.trustManagers) {
            try {
                trustManager.checkClientTrusted(chain, authType);
                return;
            }
            catch (CertificateException e) {
                exceptions.add(e);
            }
        }
        this.logger.log(Level.INFO, "000509X", "SSL client trust check did not succeed. Please see log for a list of all problems.", this.CLASS_NAME);
        Iterator<X509TrustManager> iterator = exceptions.iterator();
        while (iterator.hasNext()) {
            CertificateException e = (CertificateException)((Object)iterator.next());
            this.logger.logToFileOnly(this.CLASS_NAME, "SSL client trust check did not succeed.", (Throwable)e);
        }
        throw new CertificateException("None of the TrustManagers trust this certificate chain. See log file for details.");
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        ArrayList<CertificateException> exceptions = new ArrayList<CertificateException>();
        for (X509TrustManager trustManager : this.trustManagers) {
            try {
                trustManager.checkServerTrusted(chain, authType);
                return;
            }
            catch (CertificateException e) {
                exceptions.add(e);
            }
        }
        this.logger.log(Level.INFO, "000510X", "SSL server trust check did not succeed. Please see log for a list of all problems.", this.CLASS_NAME);
        Iterator<X509TrustManager> iterator = exceptions.iterator();
        while (iterator.hasNext()) {
            CertificateException e = (CertificateException)((Object)iterator.next());
            this.logger.logToFileOnly(this.CLASS_NAME, "SSL server trust check did not succeed.", (Throwable)e);
        }
        throw new CertificateException("None of the TrustManagers trust this certificate chain. See log file for details.");
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        X509Certificate[] certificates = new X509Certificate[]{};
        Iterator<X509TrustManager> iterator = this.trustManagers.iterator();
        while (iterator.hasNext()) {
            X509TrustManager trustManager = iterator.next();
            certificates = ArrayUtils.addAll(certificates, trustManager.getAcceptedIssuers());
        }
        return certificates;
    }
}
