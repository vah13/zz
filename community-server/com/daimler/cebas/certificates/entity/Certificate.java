/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.CertificateSignRequest
 *  com.daimler.cebas.certificates.entity.BackendContext
 *  com.daimler.cebas.certificates.entity.CertificatePKIState
 *  com.daimler.cebas.certificates.entity.CertificatePKIStateConverter
 *  com.daimler.cebas.certificates.entity.CertificateState
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.certificates.entity.CertificatesView$Detail
 *  com.daimler.cebas.certificates.entity.CertificatesView$Management
 *  com.daimler.cebas.certificates.entity.CertificatesView$SelectionEnhancedRightsView
 *  com.daimler.cebas.certificates.entity.CertificatesView$SelectionView
 *  com.daimler.cebas.certificates.entity.RawData
 *  com.daimler.cebas.certificates.entity.parsing.CertificateParser
 *  com.daimler.cebas.common.control.CEBASProperty
 *  com.daimler.cebas.common.control.HexUtil
 *  com.daimler.cebas.common.entity.AbstractEntity
 *  com.daimler.cebas.common.entity.Versioned
 *  com.daimler.cebas.configuration.control.AbstractConfigurator
 *  com.daimler.cebas.system.control.alerts.AlertMessage
 *  com.daimler.cebas.system.control.alerts.AlertMessageId
 *  com.daimler.cebas.users.entity.User
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.annotation.JsonProperty
 *  com.fasterxml.jackson.annotation.JsonView
 *  io.swagger.annotations.ApiModelProperty
 *  javax.persistence.CascadeType
 *  javax.persistence.Column
 *  javax.persistence.Convert
 *  javax.persistence.DiscriminatorValue
 *  javax.persistence.Entity
 *  javax.persistence.EnumType
 *  javax.persistence.Enumerated
 *  javax.persistence.Inheritance
 *  javax.persistence.InheritanceType
 *  javax.persistence.JoinColumn
 *  javax.persistence.ManyToOne
 *  javax.persistence.NamedQueries
 *  javax.persistence.NamedQuery
 *  javax.persistence.OneToMany
 *  javax.persistence.PostLoad
 *  javax.persistence.Table
 *  javax.persistence.Temporal
 *  javax.persistence.TemporalType
 *  javax.persistence.Transient
 *  javax.persistence.Version
 *  org.bouncycastle.cert.X509AttributeCertificateHolder
 */
package com.daimler.cebas.certificates.entity;

import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
import com.daimler.cebas.certificates.entity.BackendContext;
import com.daimler.cebas.certificates.entity.CertificatePKIState;
import com.daimler.cebas.certificates.entity.CertificatePKIStateConverter;
import com.daimler.cebas.certificates.entity.CertificateState;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.certificates.entity.CertificatesView;
import com.daimler.cebas.certificates.entity.RawData;
import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
import com.daimler.cebas.common.control.CEBASProperty;
import com.daimler.cebas.common.control.HexUtil;
import com.daimler.cebas.common.entity.AbstractEntity;
import com.daimler.cebas.common.entity.Versioned;
import com.daimler.cebas.configuration.control.AbstractConfigurator;
import com.daimler.cebas.system.control.alerts.AlertMessage;
import com.daimler.cebas.system.control.alerts.AlertMessageId;
import com.daimler.cebas.users.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bouncycastle.cert.X509AttributeCertificateHolder;

@Entity
@Table(name="r_certificate")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="Certificate")
@NamedQueries(value={@NamedQuery(name="getPossibleIdenticals", query="SELECT c FROM Certificate c where c.signature !=:signature and c.subject =:subject and c.type=:type and c.user =:user"), @NamedQuery(name="findParentUsingAKI", query="SELECT c FROM Certificate c where c.subjectKeyIdentifier =:ski and c.user =:user"), @NamedQuery(name="findECUIdenticals", query="SELECT COUNT(c) FROM Certificate c where c.subjectKeyIdentifier =:ski and c.authorityKeyIdentifier =:aki and c.subject =:subject and c.uniqueECUID =:uniqueECUID and c.specialECU =:specialECU and c.subjectPublicKey =:publicKey and c.type =:certType and c.user =:user")})
public abstract class Certificate
extends AbstractEntity
implements Versioned {
    protected static final String GREEN = "green";
    protected static final String YELLOW = "yellow";
    protected static final String RED = "red";
    protected static final String WARN = "warn";
    public static final String ENHH_RIGTHS_SUBJECT = "EnhRights Certificate";
    public static final String SEC_OCIS_SUBJECT = "SecOcIs Certificate";
    public static final String R_CERTIFICATE_PARENT = "r_certificate_parent";
    public static final String R_CERTIFICATE = "r_certificate";
    public static final String F_DATA = "f_data";
    public static final String F_SUBJECT = "f_subject";
    public static final String F_SERIAL_NO = "f_serial_no";
    public static final String F_ZK_NO = "f_zk_no";
    public static final String F_ISSUER = "f_issuer";
    public static final String F_CERTIFICATE_TYPE = "f_certificate_type";
    public static final String F_TARGET_VIN = "f_target_vin";
    public static final String F_SIGNATURE = "f_signature";
    public static final String F_ECU_ID = "f_ecu_id";
    public static final String F_VERSION = "f_version";
    public static final String F_SPECIAL_ECU = "f_special_ecu";
    public static final String F_SUBJECT_PUBLIC_KEY = "f_subject_public_key";
    public static final String F_BASE_CERTIFICATE_ID = "f_base_certificate_id";
    public static final String F_KEY_USAGE = "f_key_usage";
    public static final String F_BASIC_CONSTRAINTS = "f_basic_constraints";
    public static final String F_SUBJECT_KEY_IDENTIFIER = "f_subject_key_identifier";
    public static final String F_AUTHORITY_KEY_IDENTIFIER = "f_authority_key_identifier";
    public static final String F_SERVICES = "f_services";
    public static final String F_NONCE = "f_nonce";
    public static final String F_ALGORITHM_IDENTIFIER = "f_algorithm_identifier";
    public static final String F_PROD_QUALIFIER = "f_prod_qualifier";
    public static final String F_ISSUER_SERIAL_NUMBER = "f_issuer_serial_number";
    public static final String F_TARGET_SUBJECT_KEY_IDENTIFIER = "f_target_subject_key_identifier";
    public static final String F_USER_ROLE = "f_user_role";
    public static final String F_VALID_FROM = "f_valid_from";
    public static final String F_VALID_TO = "f_valid_to";
    public static final String F_TARGET_ECU = "f_target_ecu";
    public static final String F_PKCS10_SIGNATURE = "f_pkcs10_signature";
    public static final String F_USER_ID = "f_user_id";
    public static final String F_STATE = "f_state";
    public static final String F_PKI_STATE = "f_pki_state";
    public static final String ALL_ROOTS = "allRoots";
    public static final String ALL = "all";
    public static final String PARENT_FIELD = "parent";
    public static final String SUBJECT_KEY_IDENTIFIER = "subjectKeyIdentifier";
    public static final String SUBJECT_PUBLIC_KEY = "subjectPublicKey";
    public static final String TARGET_VIN = "targetVIN";
    public static final String TARGET_ECU = "targetECU";
    public static final String USER_ROLE = "userRole";
    public static final String SERIAL_NO = "serialNo";
    public static final String ISSUER_SERIAL_NO = "issuerSerialNumber";
    public static final String ISSUER_PARAM = "issuer";
    public static final String CERTIFICATE_TYPE = "type";
    public static final String AUTHORITY_KEY_IDENTIFIER = "authorityKeyIdentifier";
    public static final String STATE_FIELD = "state";
    public static final String SERVICES_COLUMN = "services";
    public static final String SUBJECT_COLUMN = "subject";
    public static final String SPECIAL_ECU = "specialECU";
    public static final String UNIQUE_ECU_ID = "uniqueECUID";
    public static final String TARGET_SUBJECT_KEY_IDENTIFIER = "targetSubjectKeyIdentifier";
    public static final String USER_FIELD = "user";
    public static final String SIGNATURE_FIELD = "signature";
    public static final String VALIDITY_SRENGTH_COLOR = "validityStrengthColor";
    public static final String ZKNO_COLUMN = "zkNo";
    public static final String ECU_WITH_PK = "ECU_WITH_PK";
    public static final String UPDATE_STATUS = "UPDATE_STATUS";
    public static final String FIND_IDENTICALS_QUERY = "SELECT c FROM Certificate c where c.signature !=:signature and c.subject =:subject and c.type=:type and c.user =:user";
    public static final String FIND_IDENTICALS = "getPossibleIdenticals";
    public static final String FIND_PARENT_QUERY = "SELECT c FROM Certificate c where c.subjectKeyIdentifier =:ski and c.user =:user";
    public static final String FIND_PARENT = "findParentUsingAKI";
    public static final String COUNT_ECU_IDENTICALS_QUERY = "SELECT COUNT(c) FROM Certificate c where c.subjectKeyIdentifier =:ski and c.authorityKeyIdentifier =:aki and c.subject =:subject and c.uniqueECUID =:uniqueECUID and c.specialECU =:specialECU and c.subjectPublicKey =:publicKey and c.type =:certType and c.user =:user";
    public static final String COUNT_ECU_IDENTICALS = "findECUIdenticals";
    public static final String QUERY_ECU_WITH_PK = "SELECT c.entityId FROM Certificate c, UserKeyPair kp where c.type = 'ECU_CERTIFICATE' AND c = kp.certificate";
    public static final String QUERY_C_FROM_CERTIFICATE = "SELECT c FROM Certificate c";
    public static final String QUERY_C_ROOTS_FROM_CERTIFICATES = "SELECT c FROM Certificate c where c.parent is null";
    public static final String QUERY_C_PARENT_CERTIFICATE = "SELECT c.parent FROM Certificate c where c.entityId= :entityId";
    public static final String IN_LIST = "inclList";
    public static final String IN_LIST_CERTIFICATES = "IN_LIST_CERTIFICATE";
    public static final String QUERY_CERTIFICATE_IN_LIST = "SELECT c FROM Certificate c where c.entityId in :inclList";
    private static final String EMPTY_VALUE_STRING = "";
    private static final String[] KEY_USAGE_VALUES = new String[]{"Digital signature", "Non repudiation", "Key encipherment", "Data encipherment", "Key agreement", "Key certificate signing", "CRL signing", "Encipher only", "Decipher only"};
    private static final long serialVersionUID = -5836086408463288185L;
    private static final String F_OPTIMISTIC_LOCKING_COLUMN = "F_OPTIMISTIC_LOCKING_VERSION";
    public static final String F_ECU_PACKAGE_TS = "f_ecu_package_ts";
    public static final String F_LINK_CERT_TS = "f_link_cert_ts";
    protected final transient Logger logger = Logger.getLogger(((Object)((Object)this)).getClass().getSimpleName());
    @Column(name="f_subject")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class})
    protected String subject;
    @Version
    @Column(name="F_OPTIMISTIC_LOCKING_VERSION")
    private Long optimisticLockingVersion;
    @Column(name="f_issuer")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class, CertificatesView.SelectionEnhancedRightsView.class})
    private String issuer;
    @Column(name="f_serial_no")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class})
    private String serialNo;
    @Column(name="f_signature")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    private String signature;
    @Column(name="f_version")
    @JsonView(value={CertificatesView.Detail.class})
    private String version;
    @Column(name="f_ecu_id")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class})
    private String uniqueECUID;
    @Column(name="f_special_ecu")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class})
    private String specialECU;
    @Column(name="f_subject_public_key")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    private String subjectPublicKey;
    @Column(name="f_base_certificate_id")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class, CertificatesView.SelectionEnhancedRightsView.class})
    private String baseCertificateID;
    @Column(name="f_subject_key_identifier")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class})
    private String subjectKeyIdentifier;
    @Column(name="f_key_usage")
    @JsonView(value={CertificatesView.Detail.class})
    private boolean[] keyUsage;
    @Column(name="f_basic_constraints")
    @JsonView(value={CertificatesView.Detail.class})
    private String basicConstraints;
    @Column(name="f_issuer_serial_number")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    private String issuerSerialNumber;
    @Column(name="f_authority_key_identifier")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    private String authorityKeyIdentifier;
    @Column(name="f_services")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class, CertificatesView.SelectionEnhancedRightsView.class})
    private String services;
    @Column(name="f_nonce")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class})
    private String nonce;
    @Column(name="f_algorithm_identifier")
    @JsonView(value={CertificatesView.Detail.class})
    private String algorithmIdentifier;
    @Column(name="f_prod_qualifier")
    @JsonView(value={CertificatesView.Detail.class})
    private String prodQualifier;
    @Column(name="f_target_subject_key_identifier")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class, CertificatesView.SelectionEnhancedRightsView.class})
    private String targetSubjectKeyIdentifier;
    @ManyToOne
    @JoinColumn(name="r_certificate_parent", referencedColumnName="f_id")
    @JsonIgnore
    private Certificate parent;
    @OneToMany(cascade={CascadeType.ALL}, orphanRemoval=true)
    @JoinColumn(name="r_certificate_parent")
    @JsonIgnore
    private List<Certificate> children;
    @Column(name="f_certificate_type")
    @Enumerated(value=EnumType.STRING)
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    private CertificateType type;
    @Column(name="f_target_vin")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class, CertificatesView.SelectionEnhancedRightsView.class})
    private String targetVIN;
    @Column(name="f_user_role")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class})
    private String userRole;
    @Column(name="f_target_ecu")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class, CertificatesView.SelectionEnhancedRightsView.class})
    private String targetECU;
    @Column(name="f_pkcs10_signature")
    @JsonIgnore
    private String pkcs10;
    @Column(name="f_state")
    @Enumerated(value=EnumType.STRING)
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    private CertificateState state;
    @Column(name="f_pki_state")
    @Convert(converter=CertificatePKIStateConverter.class)
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    private CertificatePKIState pkiState;
    @Temporal(value=TemporalType.TIMESTAMP)
    @Column(name="f_valid_from")
    @ApiModelProperty(dataType="java.lang.String")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    private Date validFrom;
    @Temporal(value=TemporalType.TIMESTAMP)
    @Column(name="f_valid_to")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    @ApiModelProperty(dataType="java.lang.String")
    private Date validTo;
    @Transient
    @JsonIgnore
    private int validityStrength;
    @Transient
    @JsonView(value={CertificatesView.Detail.class})
    private String keyUsageText;
    @Transient
    @JsonView(value={CertificatesView.Detail.class})
    private String basicConstraintsText;
    @Transient
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    private boolean hasChildren;
    @Transient
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    private String parentId;
    @Transient
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    protected String validityStrengthColor;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="f_user_id")
    private User user;
    @Column(name="f_zk_no")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    @JsonProperty(value="zkNo")
    private String zkNo;
    @Column(name="f_ecu_package_ts")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    private String ecuPackageTs;
    @Column(name="f_link_cert_ts")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    private String linkCertTs;

    public Certificate() {
    }

    public Certificate(X509AttributeCertificateHolder attributeHolder, User user) {
        byte[] originalBytes = this.getBytesFromAttributeCertificate(attributeHolder);
        this.update(attributeHolder, originalBytes);
        this.user = user;
    }

    public Certificate(X509AttributeCertificateHolder attributeHolder, byte[] originalBytes, User user) {
        this.update(attributeHolder, originalBytes);
        this.user = user;
    }

    public Certificate(X509Certificate certificate, User user) {
        byte[] originalBytes = null;
        try {
            originalBytes = certificate.getEncoded();
        }
        catch (CertificateEncodingException e) {
            this.logger.log(Level.FINEST, "Failed to get the encoded form of the certificate with serial no: " + certificate.getSerialNumber(), e);
        }
        this.update(certificate, originalBytes);
        this.user = user;
    }

    public Certificate(X509Certificate certificate, byte[] originalBytes, User user) {
        this.update(certificate, originalBytes);
        this.user = user;
    }

    public Certificate(String publicKey, CertificateSignRequest certificateSignRequest, User user) {
        this.user = user;
        this.userRole = certificateSignRequest.getUserRole() != null ? certificateSignRequest.getUserRole() : EMPTY_VALUE_STRING;
        this.type = CertificateType.valueFromString((String)certificateSignRequest.getCertificateType());
        this.specialECU = certificateSignRequest.getSpecialECU() != null ? certificateSignRequest.getSpecialECU() : EMPTY_VALUE_STRING;
        this.targetECU = certificateSignRequest.getTargetECU() != null ? certificateSignRequest.getTargetECU() : EMPTY_VALUE_STRING;
        this.uniqueECUID = certificateSignRequest.getUniqueECUID() != null ? certificateSignRequest.getUniqueECUID() : EMPTY_VALUE_STRING;
        this.targetVIN = certificateSignRequest.getTargetVIN() != null ? certificateSignRequest.getTargetVIN() : EMPTY_VALUE_STRING;
        this.nonce = certificateSignRequest.getNonce() != null ? HexUtil.bytesToHex((byte[])Base64.getDecoder().decode(certificateSignRequest.getNonce())) : EMPTY_VALUE_STRING;
        this.services = certificateSignRequest.getServices() != null ? certificateSignRequest.getServices() : EMPTY_VALUE_STRING;
        this.validTo = certificateSignRequest.getValidTo();
        this.validFrom = new Date();
        this.subjectPublicKey = publicKey;
        this.subject = "CN=" + certificateSignRequest.getUserId();
        this.baseCertificateID = EMPTY_VALUE_STRING;
        this.issuerSerialNumber = EMPTY_VALUE_STRING;
        this.basicConstraints = EMPTY_VALUE_STRING;
        this.subjectKeyIdentifier = EMPTY_VALUE_STRING;
        this.authorityKeyIdentifier = certificateSignRequest.getAuthorityKeyIdentifier() != null ? HexUtil.bytesToHex((byte[])Base64.getDecoder().decode(certificateSignRequest.getAuthorityKeyIdentifier())) : EMPTY_VALUE_STRING;
        this.prodQualifier = EMPTY_VALUE_STRING;
        this.algorithmIdentifier = certificateSignRequest.getAlgorithmIdentifier();
        this.serialNo = EMPTY_VALUE_STRING;
        this.signature = EMPTY_VALUE_STRING;
        this.version = certificateSignRequest.getVersion();
        this.issuer = EMPTY_VALUE_STRING;
        this.state = CertificateState.SIGNING_REQUEST;
        this.targetSubjectKeyIdentifier = certificateSignRequest.getTargetSubjectKeyIdentifier() != null ? certificateSignRequest.getTargetSubjectKeyIdentifier() : EMPTY_VALUE_STRING;
        this.validityStrengthColor = EMPTY_VALUE_STRING;
        this.pkcs10 = certificateSignRequest.getSignature();
    }

    public Certificate(String publicKey, CertificateSignRequest certificateSignRequest, X509AttributeCertificateHolder attributeHolder, User user) {
        this(publicKey, certificateSignRequest, user);
        byte[] originalBytes = this.getBytesFromAttributeCertificate(attributeHolder);
        this.fillRawData(attributeHolder, originalBytes);
        this.issuer = attributeHolder.getIssuer().getNames()[0].toString();
    }

    public static String[] getKeyUsageValues() {
        return KEY_USAGE_VALUES;
    }

    public static String extractKeyUsageText(boolean[] keyUsage) {
        String keyUsageSeparator = ", ";
        StringBuilder buffer = new StringBuilder();
        if (keyUsage == null) return EMPTY_VALUE_STRING;
        if (keyUsage.length == 0) {
            return EMPTY_VALUE_STRING;
        }
        int i = 0;
        while (true) {
            if (i >= keyUsage.length) {
                String keyUsageText = buffer.toString();
                if (keyUsageText.length() <= keyUsageSeparator.length()) return keyUsageText;
                keyUsageText = keyUsageText.substring(0, keyUsageText.length() - keyUsageSeparator.length());
                return keyUsageText;
            }
            if (keyUsage[i]) {
                buffer.append(Certificate.getKeyUsageValues()[i]).append(keyUsageSeparator);
            }
            ++i;
        }
    }

    public static List<String> extractKeyUsageTextForFiltering(boolean[] keyUsage) {
        ArrayList<String> keyUsageValues = new ArrayList<String>();
        if (keyUsage == null) return keyUsageValues;
        if (keyUsage.length == 0) {
            return keyUsageValues;
        }
        int i = 0;
        while (i < keyUsage.length) {
            if (keyUsage[i]) {
                keyUsageValues.add(Certificate.getKeyUsageValues()[i]);
            }
            ++i;
        }
        return keyUsageValues;
    }

    public static String extractBasicConstrants(String basicConstraints) {
        try {
            int basicConstraintsInt = Integer.parseInt(basicConstraints);
            if (basicConstraintsInt == -1) {
                return EMPTY_VALUE_STRING;
            }
            if (basicConstraintsInt > -1 && basicConstraintsInt < Integer.MAX_VALUE) {
                return "CA: TRUE, Path Length: " + basicConstraintsInt;
            }
            if (basicConstraintsInt != Integer.MAX_VALUE) return EMPTY_VALUE_STRING;
            return "CA: TRUE, Path Length: No Limit";
        }
        catch (NumberFormatException e) {
            return EMPTY_VALUE_STRING;
        }
    }

    @PostLoad
    public void initializeTransients() {
        this.initializeValidityStrength();
        this.initializeKeyUsage();
        this.initializeBasicConstraints();
        this.parentId = this.parent != null ? this.parent.getEntityId() : null;
    }

    public void initHasChildren() {
        this.hasChildren = !this.getChildren().isEmpty();
    }

    private void initializeValidityStrength() {
        if (this.validFrom == null) return;
        if (this.validTo == null) {
            return;
        }
        long currentMillis = new Date().getTime();
        this.validityStrength = CertificateParser.getValidityStrength((long)currentMillis, (long)this.validFrom.getTime(), (long)this.validTo.getTime());
    }

    private void initializeKeyUsage() {
        this.keyUsageText = Certificate.extractKeyUsageText(this.keyUsage);
    }

    private void initializeBasicConstraints() {
        this.basicConstraintsText = Certificate.extractBasicConstrants(this.basicConstraints);
    }

    public abstract void fillRawData(X509Certificate var1, byte[] var2);

    public abstract void fillRawData(X509AttributeCertificateHolder var1, byte[] var2);

    public abstract RawData getCertificateData();

    public String getSubject() {
        return this.subject;
    }

    public String getIssuer() {
        return this.issuer;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public String getSignature() {
        return this.signature;
    }

    @JsonIgnore
    public String getSignatureBase64() {
        return Base64.getEncoder().encodeToString(CertificateParser.hexStringToByteArray((String)this.signature));
    }

    public String getVersion() {
        return this.version;
    }

    public String getUniqueECUID() {
        return this.uniqueECUID;
    }

    public String getSpecialECU() {
        return this.specialECU;
    }

    public String getSubjectPublicKey() {
        return this.subjectPublicKey;
    }

    public String getBaseCertificateID() {
        return this.baseCertificateID;
    }

    @JsonIgnore
    public String getBaseCertificateIdSerialNo() {
        if (!StringUtils.isEmpty(this.baseCertificateID)) return this.baseCertificateID.split(" ")[0];
        return this.baseCertificateID;
    }

    public String getIssuerSerialNumber() {
        return this.issuerSerialNumber;
    }

    @JsonIgnore
    public boolean[] getKeyUsage() {
        return this.keyUsage;
    }

    @JsonIgnore
    public String getBasicConstraints() {
        return this.basicConstraints;
    }

    public String getSubjectKeyIdentifier() {
        return this.subjectKeyIdentifier.trim();
    }

    public String getAuthorityKeyIdentifier() {
        return this.authorityKeyIdentifier.trim();
    }

    public String getServices() {
        return this.services;
    }

    public String getNonce() {
        return this.nonce;
    }

    public String getProdQualifier() {
        return this.prodQualifier;
    }

    public void setProdQualifier(String prodQualifier) {
        this.prodQualifier = prodQualifier;
    }

    public String getAlgorithmIdentifier() {
        return this.algorithmIdentifier;
    }

    public void setBaseCertificateIDForEnh() {
        if (!this.getType().equals((Object)CertificateType.ENHANCED_RIGHTS_CERTIFICATE)) {
            if (!this.isSecOCISCert()) return;
        }
        this.baseCertificateID = this.parent.getSerialNo() + " " + this.parent.getSubject();
    }

    @JsonIgnore
    public Certificate getParent() {
        return this.parent;
    }

    public void setParent(Certificate parent) {
        this.parent = parent;
        this.issuerSerialNumber = parent != null ? (!this.getType().equals((Object)CertificateType.ENHANCED_RIGHTS_CERTIFICATE) ? parent.getSerialNo() : parent.getParent().getSerialNo()) : EMPTY_VALUE_STRING;
    }

    @JsonIgnore
    public List<Certificate> getChildren() {
        if (this.children != null) return this.children;
        this.children = new ArrayList<Certificate>();
        return this.children;
    }

    public void setChildren(List<Certificate> children) {
        this.children = children;
    }

    public CertificateType getType() {
        return this.type;
    }

    @JsonIgnore
    public Date getCreateTimestamp() {
        return super.getCreateTimestamp();
    }

    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    public String getPKIRole() {
        String role = EMPTY_VALUE_STRING;
        if (this.type == null) return role;
        role = this.type.getText();
        return role;
    }

    public String getTargetVIN() {
        return this.targetVIN;
    }

    public String getUserRole() {
        return this.userRole;
    }

    public String getTargetECU() {
        return this.targetECU;
    }

    @JsonIgnore
    public String getPkcs10Signature() {
        return this.pkcs10;
    }

    public Date getValidFrom() {
        return this.validFrom;
    }

    public Date getValidTo() {
        return this.validTo;
    }

    @JsonIgnore
    public int getValidityStrength() {
        return this.validityStrength;
    }

    public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
        Date dateToCheckValidity = this.getDateToCheckValidity();
        if (this.state.equals((Object)CertificateState.SIGNING_REQUEST)) {
            this.checkingValidToNotBeforeValidFrom();
        } else {
            this.checkingValidToNotBeforeValidFrom();
            if (!this.getCertificateData().isCertificate()) {
                boolean validOn = this.getCertificateData().getAttributesCertificateHolder().isValidOn(dateToCheckValidity);
                if (validOn) return;
                throw new CertificateExpiredException("NotAfter: " + this.validTo.toString());
            }
            this.getCertificateData().getCert().checkValidity();
        }
    }

    private Date getDateToCheckValidity() {
        if (!CertificateType.ENHANCED_RIGHTS_CERTIFICATE.equals((Object)this.type)) {
            if (!CertificateType.SEC_OC_IS.equals((Object)this.type)) return new Date();
        }
        try {
            String resourceFileName = null == System.getProperty("spring.profiles.active") ? "zenzefi.properties" : "sigmodul.properties";
            String timeTolerance = AbstractConfigurator.loadProperty((String)resourceFileName, (String)CEBASProperty.VALIDITY_CHECK_TIME_TOLERANCE.name());
            Date currentDate = new Date();
            Date dateWithToleranceTime = DateUtils.addMinutes(new Date(), Integer.parseInt(timeTolerance));
            if (this.validFrom.after(dateWithToleranceTime)) {
                AlertMessage.addMessageId((String)AlertMessageId.SYSTEM_CLOCK_IS_NOT_SYNCRONOUS_WITH_PKI.getValue());
                this.logger.log(Level.FINE, "Time difference between notBefore and current date exceeds the tolerance time: " + timeTolerance + " minutes");
            }
            if (!this.validFrom.after(currentDate)) return dateWithToleranceTime;
            if (!this.validFrom.before(dateWithToleranceTime)) return dateWithToleranceTime;
            this.logger.log(Level.FINE, " Time differs from the central time but still is within the defined tolerance level of " + timeTolerance + " minutes");
            return dateWithToleranceTime;
        }
        catch (NumberFormatException e) {
            this.logger.log(Level.FINE, "Invalid value of time tolerance property: " + e.getMessage());
        }
        return new Date();
    }

    public void checkingValidToNotBeforeValidFrom() throws CertificateExpiredException {
        if (!this.validTo.before(new Date())) return;
        throw new CertificateExpiredException("NotAfter: " + this.validTo.toString());
    }

    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class})
    public boolean isSecOCISCert() {
        if (!SEC_OCIS_SUBJECT.equals(this.subject)) return this.getType() == CertificateType.ENHANCED_RIGHTS_CERTIFICATE && CertificateParser.containsTargetSubjectKeyIdentifier((Object)this.getCertificateData().getAttributesCertificateHolder());
        return true;
    }

    public CertificateState getState() {
        return this.state;
    }

    public void setState(CertificateState state) {
        this.state = state;
    }

    public String getKeyUsageText() {
        return this.keyUsageText;
    }

    public String getBasicConstraintsText() {
        return this.basicConstraintsText;
    }

    @JsonIgnore
    public byte[] getSubjectPublicKeyRaw() {
        return CertificateParser.getSubjectPublicKeyBytes((Object)this.getCertificateData().getExisting());
    }

    @JsonIgnore
    public byte[] getAuthorityKeyIdentifierRaw() {
        return CertificateParser.getAuthorityKeyIdentifierRaw((Object)this.getCertificateData().getExisting());
    }

    @JsonIgnore
    public byte[] getSubjectKeyIdentifierRaw() {
        return CertificateParser.getSubjectKeyIdentifierRaw((Object)this.getCertificateData().getExisting());
    }

    @JsonIgnore
    public byte[] getTargetSubjectKeyIdentifierRaw() {
        return CertificateParser.hexStringToByteArray((String)this.targetSubjectKeyIdentifier);
    }

    @JsonIgnore
    public byte[] getSignatureRaw() {
        return CertificateParser.getSignatureRaw((Object)this.getCertificateData().getExisting());
    }

    @JsonIgnore
    public byte[] getPKIRoleRaw() {
        return CertificateParser.getPKIRoleRaw((Object)this.getCertificateData().getExisting());
    }

    @JsonIgnore
    public byte[] getProdQualifierRaw() {
        return CertificateParser.getProdQualifierRaw((Object)this.getCertificateData().getExisting());
    }

    @JsonIgnore
    public byte[] getUserRolesRaw() {
        return CertificateParser.getUserRoleRaw((Object)this.getCertificateData().getExisting());
    }

    @JsonIgnore
    public byte[] getSpecialEcuRaw() {
        return CertificateParser.getSpecialEcuRaw((Object)this.getCertificateData().getExisting());
    }

    @JsonIgnore
    public byte[] getNonceRaw() {
        return CertificateParser.getNonceRaw((Object)this.getCertificateData().getExisting());
    }

    public String getTargetSubjectKeyIdentifier() {
        return this.targetSubjectKeyIdentifier;
    }

    public void update(X509Certificate certificate, byte[] originalBytes) {
        this.fillRawData(certificate, originalBytes);
        this.validFrom = certificate.getNotBefore();
        this.validTo = certificate.getNotAfter();
        this.subject = certificate.getSubjectX500Principal().getName();
        this.targetECU = CertificateParser.getTargetEcus((Object)certificate);
        this.issuer = certificate.getIssuerX500Principal().getName();
        this.serialNo = CertificateParser.getSerialNumber((Object)certificate);
        this.type = CertificateParser.getPKIRole((Object)certificate);
        this.targetVIN = CertificateParser.getTargetVin((Object)certificate);
        this.userRole = CertificateParser.getUserRole((Object)certificate);
        this.signature = CertificateParser.getSignature((Object)certificate);
        this.version = CertificateParser.getVersion((Object)certificate);
        this.uniqueECUID = CertificateParser.getUniqueEcuIds((Object)certificate);
        this.specialECU = CertificateParser.getSpecialEcu((Object)certificate);
        this.subjectPublicKey = CertificateParser.getSubjectPublicKey((Object)certificate);
        this.baseCertificateID = EMPTY_VALUE_STRING;
        this.keyUsage = CertificateParser.getKeyUsage((Object)certificate);
        this.basicConstraints = CertificateParser.getBasicConstraints((Object)certificate);
        this.subjectKeyIdentifier = CertificateParser.getSubjectKeyIdentifier((Object)certificate);
        this.authorityKeyIdentifier = CertificateParser.getAuthorityKeyIdentifier((Object)certificate);
        this.services = CertificateParser.getServices((Object)certificate);
        this.nonce = CertificateParser.getNonce((Object)certificate);
        this.prodQualifier = CertificateParser.getProdQualifier((Object)certificate);
        this.algorithmIdentifier = CertificateParser.getSignAlgorithm((Object)certificate);
        this.state = CertificateState.ISSUED;
        if (this.type == CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE && CertificateParser.hasNonce((Object)certificate)) {
            this.type = CertificateType.TIME_CERTIFICATE;
        }
        this.issuerSerialNumber = this.issuerSerialNumber == null ? EMPTY_VALUE_STRING : this.issuerSerialNumber;
        this.targetSubjectKeyIdentifier = CertificateParser.getTargetSubjectKeyIdentifier((Object)certificate);
    }

    public void updateCSR(X509Certificate certificate, byte[] originalBytes) {
        this.update(certificate, originalBytes);
    }

    public void update(X509AttributeCertificateHolder attributeHolder, byte[] originalBytes) {
        this.fillRawData(attributeHolder, originalBytes);
        this.validFrom = attributeHolder.getNotBefore();
        this.validTo = attributeHolder.getNotAfter();
        this.targetECU = CertificateParser.getTargetEcus((Object)attributeHolder);
        this.issuer = attributeHolder.getIssuer().getNames()[0].toString();
        this.serialNo = HexUtil.bytesToHex((byte[])attributeHolder.getSerialNumber().toByteArray()).trim();
        this.type = CertificateParser.getPKIRole((Object)attributeHolder);
        this.targetVIN = CertificateParser.getTargetVin((Object)attributeHolder);
        this.userRole = EMPTY_VALUE_STRING;
        this.signature = HexUtil.bytesToHex((byte[])attributeHolder.getSignature());
        this.version = attributeHolder.getVersion() + EMPTY_VALUE_STRING;
        this.uniqueECUID = CertificateParser.getUniqueEcuIds((Object)attributeHolder);
        this.specialECU = CertificateParser.getSpecialEcu((Object)attributeHolder);
        this.subjectPublicKey = EMPTY_VALUE_STRING;
        this.baseCertificateID = HexUtil.bytesToHex((byte[])attributeHolder.getHolder().getSerialNumber().toByteArray());
        this.keyUsage = CertificateParser.getKeyUsage((Object)attributeHolder);
        this.basicConstraints = CertificateParser.getBasicConstraints((Object)attributeHolder);
        this.subjectKeyIdentifier = CertificateParser.getSubjectKeyIdentifier((Object)attributeHolder);
        this.authorityKeyIdentifier = CertificateParser.getAuthorityKeyIdentifier((Object)attributeHolder);
        this.services = CertificateParser.getServices((Object)attributeHolder);
        this.nonce = CertificateParser.getNonce((Object)attributeHolder);
        this.prodQualifier = CertificateParser.getProdQualifier((Object)attributeHolder);
        this.algorithmIdentifier = CertificateParser.getSignAlgorithm((Object)attributeHolder);
        this.state = CertificateState.ISSUED;
        this.targetSubjectKeyIdentifier = CertificateParser.getTargetSubjectKeyIdentifier((Object)attributeHolder);
        this.subject = this.isSecOCISCert() ? SEC_OCIS_SUBJECT : ENHH_RIGTHS_SUBJECT;
    }

    public void updateCSR(X509AttributeCertificateHolder attributeHolder, byte[] originalBytes) {
        this.update(attributeHolder, originalBytes);
    }

    @JsonIgnore
    public Stream<Certificate> flattened() {
        return Stream.concat(Stream.of(this), this.getChildren().stream().flatMap(Certificate::flattened));
    }

    @JsonIgnore
    public User getUser() {
        return this.user;
    }

    public boolean identicalWithDifference(Certificate certificate) {
        if (certificate.getSignature().equals(this.getSignature())) {
            return !StringUtils.equals(certificate.getSubjectPublicKey(), this.getSubjectPublicKey());
        }
        if (!certificate.getSubject().equals(this.subject)) return false;
        if (!certificate.getAuthorityKeyIdentifier().equals(this.authorityKeyIdentifier)) return false;
        if (!certificate.getBasicConstraints().equals(this.basicConstraints)) return false;
        if (!certificate.getIssuer().equals(this.issuer)) return false;
        if (!certificate.getAlgorithmIdentifier().equals(this.algorithmIdentifier)) return false;
        if (!StringUtils.equals(certificate.getKeyUsageText(), this.keyUsageText)) return false;
        if (!certificate.getNonce().equals(this.nonce)) return false;
        if (certificate.getType() != this.type) return false;
        if (!certificate.getProdQualifier().equals(this.prodQualifier)) return false;
        if (!certificate.getServices().equals(this.services)) return false;
        if (!certificate.getSpecialECU().equals(this.specialECU)) return false;
        if (!certificate.getTargetECU().equals(this.targetECU)) return false;
        if (!certificate.getTargetVIN().equals(this.targetVIN)) return false;
        if (!certificate.getUniqueECUID().equals(this.uniqueECUID)) return false;
        if (!certificate.getUserRole().equals(this.userRole)) return false;
        if (!certificate.getTargetSubjectKeyIdentifier().equals(this.targetSubjectKeyIdentifier)) return false;
        return !certificate.getValidFrom().equals(this.validFrom) || !certificate.getValidTo().equals(this.validTo) || !StringUtils.equals(certificate.getSubjectPublicKey(), this.subjectPublicKey) || !certificate.getSerialNo().equals(this.serialNo) || !StringUtils.equals(certificate.getSubjectKeyIdentifier(), this.subjectKeyIdentifier);
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String id) {
        this.parentId = id;
    }

    protected void setAuthorityKeyIdentifier(String akid) {
        this.authorityKeyIdentifier = akid;
    }

    public void setValidityColor(int yellowMinValue, int greenMinValue) {
        boolean redMinValue = false;
        this.validityStrengthColor = this.validityStrength >= greenMinValue ? GREEN : (this.validityStrength >= yellowMinValue ? YELLOW : (this.validityStrength > 0 ? RED : WARN));
    }

    public String getValidityStrengthColor() {
        return this.validityStrengthColor;
    }

    @JsonProperty
    public boolean hasChildren() {
        return this.hasChildren;
    }

    public Versioned toVersion(int version) {
        return this;
    }

    private byte[] getBytesFromAttributeCertificate(X509AttributeCertificateHolder attributeHolder) {
        byte[] originalBytes = null;
        try {
            originalBytes = attributeHolder.getEncoded();
        }
        catch (IOException e) {
            this.logger.log(Level.FINEST, "Failed to get the encoded form of the attrivute certificate with serial no: " + attributeHolder.getSerialNumber(), e);
        }
        return originalBytes;
    }

    @JsonIgnore
    public Date getUpdateTimestamp() {
        return super.getUpdateTimestamp();
    }

    @JsonProperty(value="id")
    @JsonView(value={CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class, CertificatesView.SelectionEnhancedRightsView.class})
    public String getEntityId() {
        return super.getEntityId();
    }

    @JsonIgnore
    public String getCreateUser() {
        return super.getCreateUser();
    }

    @JsonIgnore
    public boolean isEnhancedRights() {
        return this.type == CertificateType.ENHANCED_RIGHTS_CERTIFICATE;
    }

    @JsonIgnore
    public boolean isVsmEcu() {
        return this.getType() == CertificateType.ECU_CERTIFICATE && this.getSpecialECU().equals("1");
    }

    @JsonIgnore
    public boolean isNonVsmEcu() {
        return this.getType() == CertificateType.ECU_CERTIFICATE && !this.getSpecialECU().equals("1");
    }

    @JsonIgnore
    public boolean isLink() {
        return this.getType() == CertificateType.ROOT_CA_LINK_CERTIFICATE || this.getType() == CertificateType.BACKEND_CA_LINK_CERTIFICATE;
    }

    public String getZkNo() {
        if (this.zkNo != null) return this.zkNo;
        return EMPTY_VALUE_STRING;
    }

    public void setZkNo(String zkno) {
        this.zkNo = zkno;
    }

    public String getEcuPackageTs() {
        return this.ecuPackageTs;
    }

    public void setEcuPackageTs(String ecuPackageTs) {
        this.ecuPackageTs = ecuPackageTs;
    }

    public String getLinkCertTs() {
        return this.linkCertTs;
    }

    public void setLinkCertTs(String linkCertTs) {
        this.linkCertTs = linkCertTs;
    }

    public CertificatePKIState getPkiState() {
        return this.pkiState;
    }

    public void setPkiState(CertificatePKIState pkiState) {
        this.pkiState = pkiState;
    }

    @JsonIgnore
    public boolean isUnderBackend() {
        return this.getType() != CertificateType.ROOT_CA_CERTIFICATE && this.getType() != CertificateType.BACKEND_CA_CERTIFICATE && this.getType() != CertificateType.ROOT_CA_LINK_CERTIFICATE;
    }

    @JsonIgnore
    public BackendContext getBackendContext() {
        Certificate parent;
        String zkNo = EMPTY_VALUE_STRING;
        String ecuPackageTs = EMPTY_VALUE_STRING;
        String linkCertTs = EMPTY_VALUE_STRING;
        if (this.isUnderBackend() && (parent = this.getParent()) != null) {
            if (parent.getType() == CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE) {
                Certificate diagnosticParent = parent.getParent();
                zkNo = diagnosticParent.getZkNo();
                ecuPackageTs = diagnosticParent.getEcuPackageTs();
                linkCertTs = diagnosticParent.getLinkCertTs();
            } else {
                zkNo = parent.getZkNo();
                ecuPackageTs = parent.getEcuPackageTs();
                linkCertTs = parent.getLinkCertTs();
            }
        }
        if (this.getType() != CertificateType.BACKEND_CA_CERTIFICATE) return new BackendContext(zkNo, ecuPackageTs, linkCertTs);
        zkNo = this.getZkNo();
        ecuPackageTs = this.getEcuPackageTs();
        linkCertTs = this.getLinkCertTs();
        return new BackendContext(zkNo, ecuPackageTs, linkCertTs);
    }
}
