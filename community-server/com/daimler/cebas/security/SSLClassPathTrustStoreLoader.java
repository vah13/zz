/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.security.CompositeX509TrustManager
 *  org.springframework.core.io.Resource
 *  org.springframework.core.io.support.PathMatchingResourcePatternResolver
 */
package com.daimler.cebas.security;

import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.security.CompositeX509TrustManager;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class SSLClassPathTrustStoreLoader {
    private static final String DUMMY = "njqnA3dv%JodGR\"al&$C.NQD'xV44d`?";
    private static final String CERT_PATH = "sslcert/**";
    private static final String CERT_EXTENSION = ".cer";
    private Logger logger;

    public SSLClassPathTrustStoreLoader(Logger logger) {
        this.logger = logger;
    }

    public SSLContext provideSSLContext() throws NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, CertificateException, IOException {
        String defaultAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
        X509KeyManager jvmKeyManager = this.getKeyManager(defaultAlgorithm, null, null);
        X509TrustManager customTrustManager = this.getTrustManager("SunX509", this.createCustomKeyStore());
        X509TrustManager jvmTrustManager = this.getTrustManager(defaultAlgorithm, null);
        KeyManager[] keyManagers = new KeyManager[]{jvmKeyManager};
        TrustManager[] trustManagers = new TrustManager[]{new CompositeX509TrustManager(this.logger, new X509TrustManager[]{customTrustManager, jvmTrustManager})};
        SSLContext context = SSLContext.getInstance("SSL");
        context.init(keyManagers, trustManagers, null);
        return context;
    }

    private X509KeyManager getKeyManager(String algorithm, KeyStore keystore, char[] password) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
        KeyManager[] keyManagers;
        KeyManagerFactory factory = KeyManagerFactory.getInstance(algorithm);
        factory.init(keystore, password);
        KeyManager[] keyManagerArray = keyManagers = factory.getKeyManagers();
        int n = keyManagerArray.length;
        int n2 = 0;
        while (n2 < n) {
            KeyManager keyManager = keyManagerArray[n2];
            if (keyManager instanceof X509KeyManager) {
                return (X509KeyManager)keyManager;
            }
            ++n2;
        }
        return null;
    }

    private X509TrustManager getTrustManager(String algorithm, KeyStore keystore) throws KeyStoreException, NoSuchAlgorithmException {
        TrustManager[] trustManagers;
        TrustManagerFactory factory = TrustManagerFactory.getInstance(algorithm);
        factory.init(keystore);
        TrustManager[] trustManagerArray = trustManagers = factory.getTrustManagers();
        int n = trustManagerArray.length;
        int n2 = 0;
        while (n2 < n) {
            TrustManager trustManager = trustManagerArray[n2];
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager)trustManager;
            }
            ++n2;
        }
        return null;
    }

    private KeyStore createCustomKeyStore() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
        KeyStore myTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        myTrustStore.load(null, DUMMY.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(myTrustStore);
        List<Certificate> certs = this.loadCertificates();
        int i = 1;
        Iterator<Certificate> iterator = certs.iterator();
        while (iterator.hasNext()) {
            Certificate certificate = iterator.next();
            if (certificate instanceof X509Certificate) {
                X509Certificate x509 = (X509Certificate)certificate;
                myTrustStore.setCertificateEntry(x509.getSubjectDN().getName(), certificate);
                this.logger.log(Level.INFO, "000511", "Successfully imported SSL certificate " + x509.getSubjectDN().getName(), this.getClass().getSimpleName());
                continue;
            }
            myTrustStore.setCertificateEntry("cebas" + i++, certificate);
        }
        return myTrustStore;
    }

    private List<Certificate> loadCertificates() throws CertificateException, IOException {
        ArrayList<Certificate> result = new ArrayList<Certificate>();
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        List<Resource> certFiles = this.getCertFiles(CERT_PATH, CERT_EXTENSION);
        Iterator<Resource> iterator = certFiles.iterator();
        while (iterator.hasNext()) {
            Resource resource = iterator.next();
            InputStream fis = resource.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(fis);
            while (bis.available() > 0) {
                try {
                    Certificate cert = cf.generateCertificate(bis);
                    result.add(cert);
                }
                catch (Exception e) {
                    this.logger.log(Level.WARNING, "000512X", "Can not read and import SSL certificate " + resource.getFilename() + " because it may be possibly broken or malformed", this.getClass().getSimpleName());
                }
            }
            bis.close();
            fis.close();
        }
        return result;
    }

    protected List<Resource> getCertFiles(String resourcePath, String fileExtension) throws IOException {
        ArrayList<Resource> propertyFiles = new ArrayList<Resource>();
        try {
            Resource[] resources;
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resourceArray = resources = resolver.getResources(resourcePath);
            int n = resourceArray.length;
            int n2 = 0;
            while (n2 < n) {
                Resource resource = resourceArray[n2];
                if (resource.getFilename() != null && resource.getFilename().endsWith(fileExtension)) {
                    this.logger.log(Level.INFO, "000508", "Reading custom trust certificate: " + resource.getFilename(), this.getClass().getSimpleName());
                    propertyFiles.add(resource);
                }
                ++n2;
            }
            return propertyFiles;
        }
        catch (FileNotFoundException e) {
            this.logger.log(Level.INFO, "000521", "There are no custom certificates available, SSL handshake will be based on the default Java trust store only.", this.getClass().getSimpleName());
        }
        return propertyFiles;
    }
}
