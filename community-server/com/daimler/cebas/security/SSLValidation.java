/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.annotations.CEBASControl
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.security.SSLClassPathTrustStoreLoader
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 */
package com.daimler.cebas.security;

import com.daimler.cebas.common.control.annotations.CEBASControl;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.security.SSLClassPathTrustStoreLoader;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@CEBASControl
public class SSLValidation {
    private static final String CLASS_NAME = SSLValidation.class.getSimpleName();
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(CLASS_NAME);
    @Value(value="${security.disable.ssl.validation}")
    private String disableSSLValidation;
    @Value(value="${security.enable.ssl.truststore.internal}")
    private String enableInternatlSSLTruststore;
    private Logger logger;

    @Autowired
    public SSLValidation(Logger logger) {
        this.logger = logger;
    }

    public void configure() {
        if (Boolean.parseBoolean(this.disableSSLValidation)) {
            this.disableSSLValidation();
        } else {
            if (!Boolean.parseBoolean(this.enableInternatlSSLTruststore)) return;
            this.enableInternalSSLTruststore();
        }
    }

    private void enableInternalSSLTruststore() {
        this.logger.log(Level.INFO, "000505", "Enabling internal SSL truststore.", CLASS_NAME);
        try {
            SSLContext sslContext = new SSLClassPathTrustStoreLoader(this.logger).provideSSLContext();
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        }
        catch (IOException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | CertificateException e) {
            this.logger.logToFileOnly(CLASS_NAME, "Enabling internal SSL truststore failed", (Throwable)e);
            this.logger.log(Level.WARNING, "000506X", "Enabling internal SSL truststore failed - further SSLHandshake Exceptions could result due to this problem: " + e.getMessage(), CLASS_NAME);
        }
    }

    private void disableSSLValidation() {
        TrustManager[] trustAllCerts = new TrustManager[]{new /* Unavailable Anonymous Inner Class!! */};
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
        catch (KeyManagementException | NoSuchAlgorithmException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            this.logger.log(Level.WARNING, "000258X", "Could not ignore SSL certificates.", CLASS_NAME);
        }
    }

    static /* synthetic */ String access$000() {
        return CLASS_NAME;
    }

    static /* synthetic */ Logger access$100(SSLValidation x0) {
        return x0.logger;
    }
}
