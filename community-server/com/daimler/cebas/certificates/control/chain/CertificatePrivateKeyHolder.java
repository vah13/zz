/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.chain.ChainIdentifier
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateType
 */
package com.daimler.cebas.certificates.control.chain;

import com.daimler.cebas.certificates.control.chain.ChainIdentifier;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateType;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CertificatePrivateKeyHolder<T extends Certificate> {
    private T certificate;
    private String fileName;
    private Optional<PrivateKey> privateKey;
    private String internalId;
    private List<ChainIdentifier> possibleReplaceableCertificates;

    public CertificatePrivateKeyHolder(T certificate, Optional<PrivateKey> privateKey) {
        this.certificate = certificate;
        this.privateKey = privateKey;
    }

    public CertificatePrivateKeyHolder(String fileName, T certificate, Optional<PrivateKey> privateKey) {
        this(certificate, privateKey);
        this.fileName = fileName;
    }

    public CertificatePrivateKeyHolder(String fileName, T certificate, Optional<PrivateKey> privateKey, String friendlyName) {
        this(fileName, certificate, privateKey);
        if (certificate.getType() != CertificateType.BACKEND_CA_CERTIFICATE) return;
        certificate.setZkNo(friendlyName);
    }

    public T getCertificate() {
        return this.certificate;
    }

    public String getFileName() {
        return this.fileName;
    }

    public PrivateKey getPrivateKey() {
        if (!this.privateKey.isPresent()) return null;
        return this.privateKey.get();
    }

    public boolean hasPrivateKey() {
        return this.privateKey.isPresent();
    }

    public CertificateType getType() {
        return this.certificate.getType();
    }

    public void setInternalId(String entityId) {
        this.internalId = entityId;
    }

    public String getInternalId() {
        return this.certificate.getEntityId() != null ? this.certificate.getEntityId() : this.internalId;
    }

    public void addPossibleReplaceableCertificate(List<Certificate> certificates) {
        if (this.possibleReplaceableCertificates == null) {
            this.possibleReplaceableCertificates = new ArrayList<ChainIdentifier>();
        }
        List collect = certificates.stream().map(cert -> new ChainIdentifier(cert.getAuthorityKeyIdentifier(), cert.getSubjectKeyIdentifier(), cert.getSerialNo(), cert.getSubjectPublicKey())).collect(Collectors.toList());
        this.possibleReplaceableCertificates.addAll(collect);
    }

    public List<ChainIdentifier> getPossibleReplacedCertificates() {
        if (this.possibleReplaceableCertificates != null) return this.possibleReplaceableCertificates;
        return new ArrayList<ChainIdentifier>();
    }

    public int getLevel() {
        return this.certificate.getType().getLevel();
    }
}
