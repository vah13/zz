/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bouncycastle.cert.X509AttributeCertificateHolder
 */
package com.daimler.cebas.certificates.entity;

import java.io.Serializable;
import java.security.cert.X509Certificate;
import org.bouncycastle.cert.X509AttributeCertificateHolder;

public interface RawData
extends Serializable {
    public X509AttributeCertificateHolder getAttributesCertificateHolder();

    public X509Certificate getCert();

    public Object getExisting();

    public boolean isCertificate();

    public byte[] getOriginalBytes();
}
