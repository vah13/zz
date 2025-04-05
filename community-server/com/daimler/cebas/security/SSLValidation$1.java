/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.security.SSLValidation
 */
package com.daimler.cebas.security;

import com.daimler.cebas.security.SSLValidation;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import javax.net.ssl.X509TrustManager;

/*
 * Exception performing whole class analysis ignored.
 */
class SSLValidation.1
implements X509TrustManager {
    SSLValidation.1() {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
        SSLValidation.access$100((SSLValidation)SSLValidation.this).log(Level.INFO, "000689", "No check for server so that validation passes", SSLValidation.access$000());
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
        SSLValidation.access$100((SSLValidation)SSLValidation.this).log(Level.INFO, "000689", "No check for server so that validation passes", SSLValidation.access$000());
    }
}
