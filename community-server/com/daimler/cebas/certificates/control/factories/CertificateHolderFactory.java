/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder
 *  com.daimler.cebas.users.entity.User
 *  com.secunet.provider.pkcs12.AttributeCertificate
 */
package com.daimler.cebas.certificates.control.factories;

import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
import com.daimler.cebas.users.entity.User;
import com.secunet.provider.pkcs12.AttributeCertificate;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public interface CertificateHolderFactory {
    public CertificatePrivateKeyHolder createCertificateHolder(String var1, AttributeCertificate var2, User var3);

    public CertificatePrivateKeyHolder createCertificateHolder(String var1, X509Certificate var2, String var3, User var4);

    public CertificatePrivateKeyHolder createCertificateHolder(String var1, X509Certificate var2, PrivateKey var3, String var4, User var5);
}
