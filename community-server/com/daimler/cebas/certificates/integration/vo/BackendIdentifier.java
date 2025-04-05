/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.annotation.JsonIgnoreProperties
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package com.daimler.cebas.certificates.integration.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class BackendIdentifier {
    @JsonIgnore
    public static final String ROOT_CA = "ROOT_CA";
    @JsonIgnore
    public static final String BACKEND_CA = "SUB_CA";
    @JsonProperty(value="ski")
    private String subjectKeyIdentifier;
    @JsonProperty(value="aki")
    private String authorityKeyIdentifier;
    @JsonProperty(value="zkNo")
    private String zkNo;
    @JsonProperty(value="ecuPackageTs")
    private String ecuPackageTs;
    @JsonProperty(value="type")
    private String type;
    @JsonProperty(value="state")
    private String pkiState;
    @JsonProperty(value="linkCertTs")
    private String linkCertTs;

    public BackendIdentifier() {
    }

    public BackendIdentifier(String subjectKeyIdentifier, String authorityKeyIdentifier, String zkNo, String ecuPackageTs) {
        this.subjectKeyIdentifier = subjectKeyIdentifier;
        this.authorityKeyIdentifier = authorityKeyIdentifier;
        this.zkNo = zkNo;
        this.ecuPackageTs = ecuPackageTs;
    }

    public BackendIdentifier(String subjectKeyIdentifier, String authorityKeyIdentifier, String zkNo, String ecuPackageTs, String type, String pkiState, String linkCertTs) {
        this.subjectKeyIdentifier = subjectKeyIdentifier;
        this.authorityKeyIdentifier = authorityKeyIdentifier;
        this.zkNo = zkNo;
        this.ecuPackageTs = ecuPackageTs;
        this.type = type;
        this.pkiState = pkiState;
        this.linkCertTs = linkCertTs;
    }

    public String getSubjectKeyIdentifier() {
        return this.subjectKeyIdentifier;
    }

    public String getAuthorityKeyIdentifier() {
        return this.authorityKeyIdentifier;
    }

    public String getZkNo() {
        return this.zkNo;
    }

    public String getEcuPackageTs() {
        return this.ecuPackageTs;
    }

    public String getType() {
        return this.type;
    }

    public String getPkiState() {
        return this.pkiState;
    }

    public String getLinkCertTs() {
        return this.linkCertTs;
    }

    public String toString() {
        return "BackendIdentifier{subjectKeyIdentifier='" + this.subjectKeyIdentifier + '\'' + ", authorityKeyIdentifier='" + this.authorityKeyIdentifier + '\'' + ", zkNo='" + this.zkNo + '\'' + ", ecuPackageTs='" + this.ecuPackageTs + '\'' + ", type='" + this.type + '\'' + ", pkiState='" + this.pkiState + '\'' + ", linkCertTs='" + this.linkCertTs + '\'' + '}';
    }
}
