/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bouncycastle.cert.X509AttributeCertificateHolder
 */
package com.daimler.cebas.certificates.entity;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.cert.X509AttributeCertificateHolder;

public abstract class AbstractCertificateData {
    protected transient X509Certificate cert;
    protected transient X509AttributeCertificateHolder attributesCertificateHolder;

    protected abstract Logger getLogger();

    protected void closeStream(InputStream stream) {
        try {
            if (stream == null) return;
            stream.close();
        }
        catch (IOException e) {
            this.getLogger().log(Level.FINEST, "Failed to close stream", e);
        }
    }
}
