/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.common.entity.AbstractEntity
 *  com.daimler.cebas.users.entity.User
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.FetchType
 *  javax.persistence.JoinColumn
 *  javax.persistence.ManyToOne
 *  javax.persistence.NamedQueries
 *  javax.persistence.NamedQuery
 *  javax.persistence.OneToOne
 *  javax.persistence.Table
 */
package com.daimler.cebas.users.entity;

import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.common.entity.AbstractEntity;
import com.daimler.cebas.users.entity.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="r_key_store")
@NamedQueries(value={@NamedQuery(name="findUserKeyPairByCertificate", query="Select kp from UserKeyPair kp JOIN kp.certificate c WHERE c =:certificate"), @NamedQuery(name="findUserKeyPairWithNullCertificate", query="SELECT kp from UserKeyPair kp where kp.user =:user and kp.certificate IS NULL")})
public class UserKeyPair
extends AbstractEntity {
    private static final long serialVersionUID = 4568854091921404325L;
    public static final String CERTIFICATE_FIELD = "certificate";
    public static final String FIND_KEY_PAIR_BY_CERTIFICATE = "findUserKeyPairByCertificate";
    public static final String FIND_KEY_PAIR_WITH_NULL_CERTIFICATE = "findUserKeyPairWithNullCertificate";
    public static final String QUERY_BY_CERTIFICATE = "Select kp from UserKeyPair kp JOIN kp.certificate c WHERE c =:certificate";
    public static final String QUERY_BY_NULL_CERTIFICATE = "SELECT kp from UserKeyPair kp where kp.user =:user and kp.certificate IS NULL";
    public static final String R_KEY_STORE = "r_key_store";
    public static final String F_PUBLIC_KEY = "f_public_key";
    public static final String F_PRIVATE_KEY = "f_private_key";
    @Column(name="f_public_key")
    private String publicKey;
    @Column(name="f_private_key")
    private String privateKey;
    @JoinColumn(name="r_certificate")
    @OneToOne(fetch=FetchType.LAZY)
    private Certificate certificate;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="r_user")
    private User user;

    public UserKeyPair() {
    }

    public UserKeyPair(String publicKey, String privateKey, User user) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.user = user;
    }

    public UserKeyPair(String publicKey, String privateKey, User user, Certificate certificate) {
        this(publicKey, privateKey, user);
        this.certificate = certificate;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public Certificate getCertificate() {
        return this.certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }
}
