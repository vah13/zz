/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.validation.failure.ValidationState
 *  com.fasterxml.jackson.annotation.JsonFormat
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 *  org.springframework.util.StringUtils
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.certificates.control.validation.failure.ValidationState;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import org.springframework.util.StringUtils;

@ApiModel
public class CertificateSignRequest
extends ValidationState {
    public static final String CSR_VERSION = "0.1";
    private static final String PROD_QUALIFIER_TEST_HEX_DEC = "0x00";
    private static final String PROD_QUALIFIER_TEST_TEXT = "Test";
    private static final String PROD_QUALIFIER_PRODUCTION_TEXT = "Production";
    @ApiModelProperty(value="The certificate type. E.g. BACKEND_CA_CERTIFICATE, DIAGNOSTIC_AUTHENTICATION_CERTIFICATE, etc.", required=true)
    private String certificateType;
    @ApiModelProperty(value="The role of the user: 1=Supplier, 2=Development ENHANCED, 3=Production, 4=After-Sales ENHANCED, 5=After-Sales STANDARD, 6=After-Sales BASIC, 7=Internal Diagnostic Test Tool, 8=ePTI Test Tool")
    private String userRole;
    @ApiModelProperty(value="The target ECU. Maximum size of the field is 30 bytes.")
    private String targetECU;
    @ApiModelProperty(value="The target VIN. The size of the field is 17 characters.")
    private String targetVIN;
    @ApiModelProperty(value="The nonce. Must be Base64 encoded and have the length of 32 bytes.")
    private String nonce;
    @ApiModelProperty(value="The services.")
    private String services;
    @ApiModelProperty(value="Unique ECU ID. Maximum length is 30 bytes.")
    private String uniqueECUID;
    @ApiModelProperty(value="Special ECU. Maximum size is 1 byte.")
    private String specialECU;
    @ApiModelProperty(value="The parent certificate ID. Must be an UUID.")
    private String parentId;
    @JsonIgnore
    private String userId;
    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value="Valid to date. The date format is yyyy-MM-dd.", required=true)
    private Date validTo;
    @ApiModelProperty(value="The target subject key identifier.")
    private String targetSubjectKeyIdentifier;
    @ApiModelProperty(value="The authority key identifier. It is sent as Base64 encoded bytes, and the length must be 20 bytes.", required=true)
    private String authorityKeyIdentifier;
    @ApiModelProperty(value="The subject.")
    private String subject;
    private String algorithmIdentifier;
    private String version;
    private String prodQualifier;
    private String signature;

    public CertificateSignRequest() {
    }

    public CertificateSignRequest(String certificateType, String userRole, String targetEcu, String targetVin, String nonce, String services, String uniqueECUID, String specialECU, Date validTo, String parentId, String userId, String algorithmIdentifier, String version, String prodQualifier, String signature) {
        this(certificateType, userRole, targetEcu, targetVin, nonce, services, uniqueECUID, specialECU, "", "", validTo, parentId, userId, algorithmIdentifier, version, prodQualifier, signature);
    }

    public CertificateSignRequest(String subject, String certificateType, String userRole, String targetEcu, String targetVin, String nonce, String services, String uniqueECUID, String specialECU, Date validTo, String parentId, String userId, String algorithmIdentifier, String version, String prodQualifier, String signature, String authorityKeyIdentifier) {
        this.subject = subject;
        this.certificateType = certificateType;
        this.userRole = userRole;
        this.targetECU = targetEcu;
        this.targetVIN = targetVin;
        this.nonce = nonce;
        this.services = services;
        this.uniqueECUID = uniqueECUID;
        this.specialECU = specialECU;
        this.validTo = validTo;
        this.parentId = parentId;
        this.userId = userId;
        this.algorithmIdentifier = algorithmIdentifier;
        this.version = StringUtils.isEmpty((Object)version) ? CSR_VERSION : version;
        this.prodQualifier = prodQualifier;
        this.signature = signature;
        this.authorityKeyIdentifier = authorityKeyIdentifier;
    }

    public CertificateSignRequest(String certificateType, String userRole, String targetEcu, String targetVin, String nonce, String services, String uniqueECUID, String specialECU, String targetSubjectKeyIdentifier, String authorityKeyIdentifier, Date validTo, String parentId, String userId, String algorithmIdentifier, String version, String prodQualifier, String signature) {
        this.certificateType = certificateType;
        this.userRole = userRole;
        this.targetECU = targetEcu;
        this.targetVIN = targetVin;
        this.nonce = nonce;
        this.services = services;
        this.uniqueECUID = uniqueECUID;
        this.specialECU = specialECU;
        this.targetSubjectKeyIdentifier = targetSubjectKeyIdentifier;
        this.authorityKeyIdentifier = authorityKeyIdentifier;
        this.validTo = validTo;
        this.parentId = parentId;
        this.userId = userId;
        this.algorithmIdentifier = algorithmIdentifier;
        this.version = CSR_VERSION;
        this.prodQualifier = prodQualifier;
        this.signature = signature;
        this.subject = "CN=";
    }

    public CertificateSignRequest(String subject, String certificateType, String userRole, String targetEcu, String targetVin, String nonce, String services, String uniqueECUID, String specialECU, Date validTo, String parentId, String userId, String algorithmIdentifier, String version, String prodQualifier, String authorityKeyIdentifier) {
        this(subject, certificateType, userRole, targetEcu, targetVin, nonce, services, uniqueECUID, specialECU, validTo, parentId, userId, algorithmIdentifier, version, prodQualifier, null, authorityKeyIdentifier);
    }

    public CertificateSignRequest(String subject, String certificateType, String userRole, String targetEcu, String targetVin, String nonce, String services, String uniqueECUID, String specialECU, String targetSubjectKeyIdentifier, String authorityKeyIdentifier, Date validTo, String parentId, String userId, String algorithmIdentifier, String version, String prodQualifier, String signature) {
        this(certificateType, userRole, targetEcu, targetVin, nonce, services, uniqueECUID, specialECU, targetSubjectKeyIdentifier, authorityKeyIdentifier, validTo, parentId, userId, algorithmIdentifier, version, prodQualifier, signature);
        this.subject = subject;
    }

    public String getCertificateType() {
        return this.certificateType;
    }

    public String getUserRole() {
        return this.userRole;
    }

    public String getTargetECU() {
        return this.targetECU;
    }

    public String getTargetVIN() {
        return this.targetVIN;
    }

    public String getNonce() {
        return this.nonce;
    }

    public String getServices() {
        return this.services;
    }

    public String getUniqueECUID() {
        return this.uniqueECUID;
    }

    public String getSpecialECU() {
        return this.specialECU;
    }

    public String getTargetSubjectKeyIdentifier() {
        return this.targetSubjectKeyIdentifier;
    }

    public String getAuthorityKeyIdentifier() {
        return this.authorityKeyIdentifier;
    }

    public Date getValidTo() {
        return this.validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getAlgorithmIdentifier() {
        return this.algorithmIdentifier;
    }

    public String getVersion() {
        return this.version;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return this.signature;
    }

    public String getProdQualifier() {
        return this.prodQualifier;
    }

    public Integer retrieveProdQualifierForCSR(CertificateSignRequest certificateSignRequest) {
        String prodQualifierStringValue = certificateSignRequest.getProdQualifier() != null ? certificateSignRequest.getProdQualifier() : PROD_QUALIFIER_TEST_HEX_DEC;
        if (prodQualifierStringValue.equals(PROD_QUALIFIER_TEST_TEXT)) return 0;
        if (prodQualifierStringValue.equals(PROD_QUALIFIER_TEST_HEX_DEC)) {
            return 0;
        }
        if (!prodQualifierStringValue.equals(PROD_QUALIFIER_PRODUCTION_TEXT)) return 0;
        return 1;
    }

    public String toString() {
        return "CertificateSignRequest [certificateType=" + this.certificateType + ", userRole=" + this.userRole + ", targetECU=" + this.targetECU + ", targetVIN=" + this.targetVIN + ", nonce=" + this.nonce + ", services=" + this.services + ", uniqueECUID=" + this.uniqueECUID + ", specialECU=" + this.specialECU + ", validTo=" + this.validTo + ", targetSubjectKeyIdentifier=" + this.targetSubjectKeyIdentifier + ", subject=" + this.subject + ", algorithmIdentifier=" + this.algorithmIdentifier + ", version=" + this.version + ", prodQualifier=" + this.prodQualifier + ", CSR=" + this.signature + "]";
    }
}
