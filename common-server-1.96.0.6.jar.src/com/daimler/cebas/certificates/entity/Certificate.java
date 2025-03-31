/*      */ package com.daimler.cebas.certificates.entity;
/*      */ 
/*      */ import com.daimler.cebas.certificates.control.vo.CertificateSignRequest;
/*      */ import com.daimler.cebas.certificates.entity.parsing.CertificateParser;
/*      */ import com.daimler.cebas.common.control.CEBASProperty;
/*      */ import com.daimler.cebas.common.control.HexUtil;
/*      */ import com.daimler.cebas.common.entity.AbstractEntity;
/*      */ import com.daimler.cebas.common.entity.Versioned;
/*      */ import com.daimler.cebas.configuration.control.AbstractConfigurator;
/*      */ import com.daimler.cebas.system.control.alerts.AlertMessage;
/*      */ import com.daimler.cebas.system.control.alerts.AlertMessageId;
/*      */ import com.daimler.cebas.users.entity.User;
/*      */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*      */ import com.fasterxml.jackson.annotation.JsonProperty;
/*      */ import com.fasterxml.jackson.annotation.JsonView;
/*      */ import io.swagger.annotations.ApiModelProperty;
/*      */ import java.io.IOException;
/*      */ import java.security.cert.CertificateEncodingException;
/*      */ import java.security.cert.CertificateExpiredException;
/*      */ import java.security.cert.CertificateNotYetValidException;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Base64;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import java.util.stream.Stream;
/*      */ import javax.persistence.CascadeType;
/*      */ import javax.persistence.Column;
/*      */ import javax.persistence.Convert;
/*      */ import javax.persistence.DiscriminatorValue;
/*      */ import javax.persistence.Entity;
/*      */ import javax.persistence.EnumType;
/*      */ import javax.persistence.Enumerated;
/*      */ import javax.persistence.Inheritance;
/*      */ import javax.persistence.InheritanceType;
/*      */ import javax.persistence.JoinColumn;
/*      */ import javax.persistence.ManyToOne;
/*      */ import javax.persistence.NamedQueries;
/*      */ import javax.persistence.NamedQuery;
/*      */ import javax.persistence.OneToMany;
/*      */ import javax.persistence.PostLoad;
/*      */ import javax.persistence.Table;
/*      */ import javax.persistence.Temporal;
/*      */ import javax.persistence.TemporalType;
/*      */ import javax.persistence.Transient;
/*      */ import javax.persistence.Version;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ import org.apache.commons.lang3.time.DateUtils;
/*      */ import org.bouncycastle.cert.X509AttributeCertificateHolder;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Entity
/*      */ @Table(name = "r_certificate")
/*      */ @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
/*      */ @DiscriminatorValue("Certificate")
/*      */ @NamedQueries({@NamedQuery(name = "getPossibleIdenticals", query = "SELECT c FROM Certificate c where c.signature !=:signature and c.subject =:subject and c.type=:type and c.user =:user"), @NamedQuery(name = "findParentUsingAKI", query = "SELECT c FROM Certificate c where c.subjectKeyIdentifier =:ski and c.user =:user"), @NamedQuery(name = "findECUIdenticals", query = "SELECT COUNT(c) FROM Certificate c where c.subjectKeyIdentifier =:ski and c.authorityKeyIdentifier =:aki and c.subject =:subject and c.uniqueECUID =:uniqueECUID and c.specialECU =:specialECU and c.subjectPublicKey =:publicKey and c.type =:certType and c.user =:user")})
/*      */ public abstract class Certificate
/*      */   extends AbstractEntity
/*      */   implements Versioned
/*      */ {
/*      */   protected static final String GREEN = "green";
/*      */   protected static final String YELLOW = "yellow";
/*      */   protected static final String RED = "red";
/*      */   protected static final String WARN = "warn";
/*      */   public static final String ENHH_RIGTHS_SUBJECT = "EnhRights Certificate";
/*      */   public static final String SEC_OCIS_SUBJECT = "SecOcIs Certificate";
/*      */   public static final String R_CERTIFICATE_PARENT = "r_certificate_parent";
/*      */   public static final String R_CERTIFICATE = "r_certificate";
/*      */   public static final String F_DATA = "f_data";
/*      */   public static final String F_SUBJECT = "f_subject";
/*      */   public static final String F_SERIAL_NO = "f_serial_no";
/*      */   public static final String F_ZK_NO = "f_zk_no";
/*      */   public static final String F_ISSUER = "f_issuer";
/*      */   public static final String F_CERTIFICATE_TYPE = "f_certificate_type";
/*      */   public static final String F_TARGET_VIN = "f_target_vin";
/*      */   public static final String F_SIGNATURE = "f_signature";
/*      */   public static final String F_ECU_ID = "f_ecu_id";
/*      */   public static final String F_VERSION = "f_version";
/*      */   public static final String F_SPECIAL_ECU = "f_special_ecu";
/*      */   public static final String F_SUBJECT_PUBLIC_KEY = "f_subject_public_key";
/*      */   public static final String F_BASE_CERTIFICATE_ID = "f_base_certificate_id";
/*      */   public static final String F_KEY_USAGE = "f_key_usage";
/*      */   public static final String F_BASIC_CONSTRAINTS = "f_basic_constraints";
/*      */   public static final String F_SUBJECT_KEY_IDENTIFIER = "f_subject_key_identifier";
/*      */   public static final String F_AUTHORITY_KEY_IDENTIFIER = "f_authority_key_identifier";
/*      */   public static final String F_SERVICES = "f_services";
/*      */   public static final String F_NONCE = "f_nonce";
/*      */   public static final String F_ALGORITHM_IDENTIFIER = "f_algorithm_identifier";
/*      */   public static final String F_PROD_QUALIFIER = "f_prod_qualifier";
/*      */   public static final String F_ISSUER_SERIAL_NUMBER = "f_issuer_serial_number";
/*      */   public static final String F_TARGET_SUBJECT_KEY_IDENTIFIER = "f_target_subject_key_identifier";
/*      */   public static final String F_USER_ROLE = "f_user_role";
/*      */   public static final String F_VALID_FROM = "f_valid_from";
/*      */   public static final String F_VALID_TO = "f_valid_to";
/*      */   public static final String F_TARGET_ECU = "f_target_ecu";
/*      */   public static final String F_PKCS10_SIGNATURE = "f_pkcs10_signature";
/*      */   public static final String F_USER_ID = "f_user_id";
/*      */   public static final String F_STATE = "f_state";
/*      */   public static final String F_PKI_STATE = "f_pki_state";
/*      */   public static final String ALL_ROOTS = "allRoots";
/*      */   public static final String ALL = "all";
/*      */   public static final String PARENT_FIELD = "parent";
/*      */   public static final String SUBJECT_KEY_IDENTIFIER = "subjectKeyIdentifier";
/*      */   public static final String SUBJECT_PUBLIC_KEY = "subjectPublicKey";
/*      */   public static final String TARGET_VIN = "targetVIN";
/*      */   public static final String TARGET_ECU = "targetECU";
/*      */   public static final String USER_ROLE = "userRole";
/*      */   public static final String SERIAL_NO = "serialNo";
/*      */   public static final String ISSUER_SERIAL_NO = "issuerSerialNumber";
/*      */   public static final String ISSUER_PARAM = "issuer";
/*      */   public static final String CERTIFICATE_TYPE = "type";
/*      */   public static final String AUTHORITY_KEY_IDENTIFIER = "authorityKeyIdentifier";
/*      */   public static final String STATE_FIELD = "state";
/*      */   public static final String SERVICES_COLUMN = "services";
/*      */   public static final String SUBJECT_COLUMN = "subject";
/*      */   public static final String SPECIAL_ECU = "specialECU";
/*      */   public static final String UNIQUE_ECU_ID = "uniqueECUID";
/*      */   public static final String TARGET_SUBJECT_KEY_IDENTIFIER = "targetSubjectKeyIdentifier";
/*      */   public static final String USER_FIELD = "user";
/*      */   public static final String SIGNATURE_FIELD = "signature";
/*      */   public static final String VALIDITY_SRENGTH_COLOR = "validityStrengthColor";
/*      */   public static final String ZKNO_COLUMN = "zkNo";
/*      */   public static final String ECU_WITH_PK = "ECU_WITH_PK";
/*      */   public static final String UPDATE_STATUS = "UPDATE_STATUS";
/*      */   public static final String FIND_IDENTICALS_QUERY = "SELECT c FROM Certificate c where c.signature !=:signature and c.subject =:subject and c.type=:type and c.user =:user";
/*      */   public static final String FIND_IDENTICALS = "getPossibleIdenticals";
/*      */   public static final String FIND_PARENT_QUERY = "SELECT c FROM Certificate c where c.subjectKeyIdentifier =:ski and c.user =:user";
/*      */   public static final String FIND_PARENT = "findParentUsingAKI";
/*      */   public static final String COUNT_ECU_IDENTICALS_QUERY = "SELECT COUNT(c) FROM Certificate c where c.subjectKeyIdentifier =:ski and c.authorityKeyIdentifier =:aki and c.subject =:subject and c.uniqueECUID =:uniqueECUID and c.specialECU =:specialECU and c.subjectPublicKey =:publicKey and c.type =:certType and c.user =:user";
/*      */   public static final String COUNT_ECU_IDENTICALS = "findECUIdenticals";
/*      */   public static final String QUERY_ECU_WITH_PK = "SELECT c.entityId FROM Certificate c, UserKeyPair kp where c.type = 'ECU_CERTIFICATE' AND c = kp.certificate";
/*      */   public static final String QUERY_C_FROM_CERTIFICATE = "SELECT c FROM Certificate c";
/*      */   public static final String QUERY_C_ROOTS_FROM_CERTIFICATES = "SELECT c FROM Certificate c where c.parent is null";
/*      */   public static final String QUERY_C_PARENT_CERTIFICATE = "SELECT c.parent FROM Certificate c where c.entityId= :entityId";
/*      */   public static final String IN_LIST = "inclList";
/*      */   public static final String IN_LIST_CERTIFICATES = "IN_LIST_CERTIFICATE";
/*      */   public static final String QUERY_CERTIFICATE_IN_LIST = "SELECT c FROM Certificate c where c.entityId in :inclList";
/*      */   private static final String EMPTY_VALUE_STRING = "";
/*  425 */   private static final String[] KEY_USAGE_VALUES = new String[] { "Digital signature", "Non repudiation", "Key encipherment", "Data encipherment", "Key agreement", "Key certificate signing", "CRL signing", "Encipher only", "Decipher only" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final long serialVersionUID = -5836086408463288185L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String F_OPTIMISTIC_LOCKING_COLUMN = "F_OPTIMISTIC_LOCKING_VERSION";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String F_ECU_PACKAGE_TS = "f_ecu_package_ts";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String F_LINK_CERT_TS = "f_link_cert_ts";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  452 */   protected final transient Logger logger = Logger.getLogger(getClass().getSimpleName());
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_subject")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class})
/*      */   protected String subject;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Version
/*      */   @Column(name = "F_OPTIMISTIC_LOCKING_VERSION")
/*      */   private Long optimisticLockingVersion;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_issuer")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class, CertificatesView.SelectionEnhancedRightsView.class})
/*      */   private String issuer;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_serial_no")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class})
/*      */   private String serialNo;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_signature")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   private String signature;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_version")
/*      */   @JsonView({CertificatesView.Detail.class})
/*      */   private String version;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_ecu_id")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class})
/*      */   private String uniqueECUID;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_special_ecu")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class})
/*      */   private String specialECU;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_subject_public_key")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   private String subjectPublicKey;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_base_certificate_id")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class, CertificatesView.SelectionEnhancedRightsView.class})
/*      */   private String baseCertificateID;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_subject_key_identifier")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class})
/*      */   private String subjectKeyIdentifier;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_key_usage")
/*      */   @JsonView({CertificatesView.Detail.class})
/*      */   private boolean[] keyUsage;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_basic_constraints")
/*      */   @JsonView({CertificatesView.Detail.class})
/*      */   private String basicConstraints;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_issuer_serial_number")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   private String issuerSerialNumber;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_authority_key_identifier")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   private String authorityKeyIdentifier;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_services")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class, CertificatesView.SelectionEnhancedRightsView.class})
/*      */   private String services;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_nonce")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class})
/*      */   private String nonce;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_algorithm_identifier")
/*      */   @JsonView({CertificatesView.Detail.class})
/*      */   private String algorithmIdentifier;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_prod_qualifier")
/*      */   @JsonView({CertificatesView.Detail.class})
/*      */   private String prodQualifier;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_target_subject_key_identifier")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class, CertificatesView.SelectionEnhancedRightsView.class})
/*      */   private String targetSubjectKeyIdentifier;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @ManyToOne
/*      */   @JoinColumn(name = "r_certificate_parent", referencedColumnName = "f_id")
/*      */   @JsonIgnore
/*      */   private Certificate parent;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
/*      */   @JoinColumn(name = "r_certificate_parent")
/*      */   @JsonIgnore
/*      */   private List<Certificate> children;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_certificate_type")
/*      */   @Enumerated(EnumType.STRING)
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   private CertificateType type;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_target_vin")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class, CertificatesView.SelectionEnhancedRightsView.class})
/*      */   private String targetVIN;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_user_role")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class})
/*      */   private String userRole;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_target_ecu")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class, CertificatesView.SelectionEnhancedRightsView.class})
/*      */   private String targetECU;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_pkcs10_signature")
/*      */   @JsonIgnore
/*      */   private String pkcs10;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_state")
/*      */   @Enumerated(EnumType.STRING)
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   private CertificateState state;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_pki_state")
/*      */   @Convert(converter = CertificatePKIStateConverter.class)
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   private CertificatePKIState pkiState;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Temporal(TemporalType.TIMESTAMP)
/*      */   @Column(name = "f_valid_from")
/*      */   @ApiModelProperty(dataType = "java.lang.String")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   private Date validFrom;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Temporal(TemporalType.TIMESTAMP)
/*      */   @Column(name = "f_valid_to")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   @ApiModelProperty(dataType = "java.lang.String")
/*      */   private Date validTo;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Transient
/*      */   @JsonIgnore
/*      */   private int validityStrength;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Transient
/*      */   @JsonView({CertificatesView.Detail.class})
/*      */   private String keyUsageText;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Transient
/*      */   @JsonView({CertificatesView.Detail.class})
/*      */   private String basicConstraintsText;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Transient
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   private boolean hasChildren;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Transient
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   private String parentId;
/*      */ 
/*      */ 
/*      */   
/*      */   @Transient
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   protected String validityStrengthColor;
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   @ManyToOne
/*      */   @JoinColumn(name = "f_user_id")
/*      */   private User user;
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_zk_no")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   @JsonProperty("zkNo")
/*      */   private String zkNo;
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_ecu_package_ts")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   private String ecuPackageTs;
/*      */ 
/*      */ 
/*      */   
/*      */   @Column(name = "f_link_cert_ts")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   private String linkCertTs;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Certificate() {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Certificate(X509AttributeCertificateHolder attributeHolder, User user) {
/*  756 */     byte[] originalBytes = getBytesFromAttributeCertificate(attributeHolder);
/*  757 */     update(attributeHolder, originalBytes);
/*  758 */     this.user = user;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Certificate(X509AttributeCertificateHolder attributeHolder, byte[] originalBytes, User user) {
/*  769 */     update(attributeHolder, originalBytes);
/*  770 */     this.user = user;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Certificate(X509Certificate certificate, User user) {
/*  779 */     byte[] originalBytes = null;
/*      */     try {
/*  781 */       originalBytes = certificate.getEncoded();
/*  782 */     } catch (CertificateEncodingException e) {
/*  783 */       this.logger.log(Level.FINEST, "Failed to get the encoded form of the certificate with serial no: " + certificate
/*  784 */           .getSerialNumber(), e);
/*      */     } 
/*  786 */     update(certificate, originalBytes);
/*  787 */     this.user = user;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Certificate(X509Certificate certificate, byte[] originalBytes, User user) {
/*  798 */     update(certificate, originalBytes);
/*  799 */     this.user = user;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Certificate(String publicKey, CertificateSignRequest certificateSignRequest, User user) {
/*  809 */     this.user = user;
/*  810 */     this.userRole = (certificateSignRequest.getUserRole() != null) ? certificateSignRequest.getUserRole() : "";
/*      */     
/*  812 */     this.type = CertificateType.valueFromString(certificateSignRequest.getCertificateType());
/*  813 */     this.specialECU = (certificateSignRequest.getSpecialECU() != null) ? certificateSignRequest.getSpecialECU() : "";
/*      */     
/*  815 */     this.targetECU = (certificateSignRequest.getTargetECU() != null) ? certificateSignRequest.getTargetECU() : "";
/*      */     
/*  817 */     this.uniqueECUID = (certificateSignRequest.getUniqueECUID() != null) ? certificateSignRequest.getUniqueECUID() : "";
/*      */     
/*  819 */     this.targetVIN = (certificateSignRequest.getTargetVIN() != null) ? certificateSignRequest.getTargetVIN() : "";
/*      */     
/*  821 */     this
/*  822 */       .nonce = (certificateSignRequest.getNonce() != null) ? HexUtil.bytesToHex(Base64.getDecoder().decode(certificateSignRequest.getNonce())) : "";
/*      */     
/*  824 */     this.services = (certificateSignRequest.getServices() != null) ? certificateSignRequest.getServices() : "";
/*      */     
/*  826 */     this.validTo = certificateSignRequest.getValidTo();
/*  827 */     this.validFrom = new Date();
/*  828 */     this.subjectPublicKey = publicKey;
/*  829 */     this.subject = "CN=" + certificateSignRequest.getUserId();
/*  830 */     this.baseCertificateID = "";
/*  831 */     this.issuerSerialNumber = "";
/*  832 */     this.basicConstraints = "";
/*  833 */     this.subjectKeyIdentifier = "";
/*  834 */     this
/*  835 */       .authorityKeyIdentifier = (certificateSignRequest.getAuthorityKeyIdentifier() != null) ? HexUtil.bytesToHex(Base64.getDecoder().decode(certificateSignRequest.getAuthorityKeyIdentifier())) : "";
/*      */     
/*  837 */     this.prodQualifier = "";
/*  838 */     this.algorithmIdentifier = certificateSignRequest.getAlgorithmIdentifier();
/*  839 */     this.serialNo = "";
/*  840 */     this.signature = "";
/*  841 */     this.version = certificateSignRequest.getVersion();
/*  842 */     this.issuer = "";
/*  843 */     this.state = CertificateState.SIGNING_REQUEST;
/*  844 */     this
/*  845 */       .targetSubjectKeyIdentifier = (certificateSignRequest.getTargetSubjectKeyIdentifier() != null) ? certificateSignRequest.getTargetSubjectKeyIdentifier() : "";
/*      */     
/*  847 */     this.validityStrengthColor = "";
/*  848 */     this.pkcs10 = certificateSignRequest.getSignature();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Certificate(String publicKey, CertificateSignRequest certificateSignRequest, X509AttributeCertificateHolder attributeHolder, User user) {
/*  859 */     this(publicKey, certificateSignRequest, user);
/*  860 */     byte[] originalBytes = getBytesFromAttributeCertificate(attributeHolder);
/*  861 */     fillRawData(attributeHolder, originalBytes);
/*  862 */     this.issuer = attributeHolder.getIssuer().getNames()[0].toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] getKeyUsageValues() {
/*  871 */     return KEY_USAGE_VALUES;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String extractKeyUsageText(boolean[] keyUsage) {
/*  881 */     String keyUsageSeparator = ", ";
/*  882 */     StringBuilder buffer = new StringBuilder();
/*      */     
/*  884 */     if (keyUsage == null || keyUsage.length == 0) {
/*  885 */       return "";
/*      */     }
/*  887 */     for (int i = 0; i < keyUsage.length; i++) {
/*  888 */       if (keyUsage[i]) {
/*  889 */         buffer.append(getKeyUsageValues()[i]).append(keyUsageSeparator);
/*      */       }
/*      */     } 
/*  892 */     String keyUsageText = buffer.toString();
/*  893 */     if (keyUsageText.length() > keyUsageSeparator.length()) {
/*  894 */       keyUsageText = keyUsageText.substring(0, keyUsageText.length() - keyUsageSeparator.length());
/*      */     }
/*  896 */     return keyUsageText;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> extractKeyUsageTextForFiltering(boolean[] keyUsage) {
/*  906 */     List<String> keyUsageValues = new ArrayList<>();
/*  907 */     if (keyUsage == null || keyUsage.length == 0) {
/*  908 */       return keyUsageValues;
/*      */     }
/*  910 */     for (int i = 0; i < keyUsage.length; i++) {
/*  911 */       if (keyUsage[i]) {
/*  912 */         keyUsageValues.add(getKeyUsageValues()[i]);
/*      */       }
/*      */     } 
/*  915 */     return keyUsageValues;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String extractBasicConstrants(String basicConstraints) {
/*      */     try {
/*  926 */       int basicConstraintsInt = Integer.parseInt(basicConstraints);
/*  927 */       if (basicConstraintsInt == -1)
/*  928 */         return ""; 
/*  929 */       if (basicConstraintsInt > -1 && basicConstraintsInt < Integer.MAX_VALUE)
/*  930 */         return "CA: TRUE, Path Length: " + basicConstraintsInt; 
/*  931 */       if (basicConstraintsInt == Integer.MAX_VALUE) {
/*  932 */         return "CA: TRUE, Path Length: No Limit";
/*      */       }
/*  934 */     } catch (NumberFormatException e) {
/*  935 */       return "";
/*      */     } 
/*  937 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @PostLoad
/*      */   public void initializeTransients() {
/*  945 */     initializeValidityStrength();
/*  946 */     initializeKeyUsage();
/*  947 */     initializeBasicConstraints();
/*  948 */     this.parentId = (this.parent != null) ? this.parent.getEntityId() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initHasChildren() {
/*  955 */     this.hasChildren = !getChildren().isEmpty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initializeValidityStrength() {
/*  963 */     if (this.validFrom == null || this.validTo == null) {
/*      */       return;
/*      */     }
/*  966 */     long currentMillis = (new Date()).getTime();
/*  967 */     this.validityStrength = CertificateParser.getValidityStrength(currentMillis, this.validFrom.getTime(), this.validTo.getTime());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initializeKeyUsage() {
/*  974 */     this.keyUsageText = extractKeyUsageText(this.keyUsage);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initializeBasicConstraints() {
/*  986 */     this.basicConstraintsText = extractBasicConstrants(this.basicConstraints);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void fillRawData(X509Certificate paramX509Certificate, byte[] paramArrayOfbyte);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void fillRawData(X509AttributeCertificateHolder paramX509AttributeCertificateHolder, byte[] paramArrayOfbyte);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract RawData getCertificateData();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSubject() {
/* 1014 */     return this.subject;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getIssuer() {
/* 1021 */     return this.issuer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSerialNo() {
/* 1028 */     return this.serialNo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSignature() {
/* 1035 */     return this.signature;
/*      */   }
/*      */   
/*      */   @JsonIgnore
/*      */   public String getSignatureBase64() {
/* 1040 */     return Base64.getEncoder().encodeToString(CertificateParser.hexStringToByteArray(this.signature));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getVersion() {
/* 1047 */     return this.version;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUniqueECUID() {
/* 1054 */     return this.uniqueECUID;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSpecialECU() {
/* 1061 */     return this.specialECU;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSubjectPublicKey() {
/* 1068 */     return this.subjectPublicKey;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getBaseCertificateID() {
/* 1075 */     return this.baseCertificateID;
/*      */   }
/*      */   
/*      */   @JsonIgnore
/*      */   public String getBaseCertificateIdSerialNo() {
/* 1080 */     if (StringUtils.isEmpty(this.baseCertificateID)) return this.baseCertificateID; 
/* 1081 */     return this.baseCertificateID.split(" ")[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getIssuerSerialNumber() {
/* 1088 */     return this.issuerSerialNumber;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public boolean[] getKeyUsage() {
/* 1096 */     return this.keyUsage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public String getBasicConstraints() {
/* 1104 */     return this.basicConstraints;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSubjectKeyIdentifier() {
/* 1111 */     return this.subjectKeyIdentifier.trim();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getAuthorityKeyIdentifier() {
/* 1118 */     return this.authorityKeyIdentifier.trim();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getServices() {
/* 1125 */     return this.services;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getNonce() {
/* 1132 */     return this.nonce;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getProdQualifier() {
/* 1139 */     return this.prodQualifier;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProdQualifier(String prodQualifier) {
/* 1148 */     this.prodQualifier = prodQualifier;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getAlgorithmIdentifier() {
/* 1155 */     return this.algorithmIdentifier;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBaseCertificateIDForEnh() {
/* 1162 */     if (getType().equals(CertificateType.ENHANCED_RIGHTS_CERTIFICATE) || isSecOCISCert()) {
/* 1163 */       this.baseCertificateID = this.parent.getSerialNo() + " " + this.parent.getSubject();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public Certificate getParent() {
/* 1174 */     return this.parent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setParent(Certificate parent) {
/* 1183 */     this.parent = parent;
/* 1184 */     if (parent != null) {
/*      */       
/* 1186 */       if (!getType().equals(CertificateType.ENHANCED_RIGHTS_CERTIFICATE)) {
/* 1187 */         this.issuerSerialNumber = parent.getSerialNo();
/*      */       } else {
/* 1189 */         this.issuerSerialNumber = parent.getParent().getSerialNo();
/*      */       } 
/*      */     } else {
/* 1192 */       this.issuerSerialNumber = "";
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public List<Certificate> getChildren() {
/* 1203 */     if (this.children == null) {
/* 1204 */       this.children = new ArrayList<>();
/*      */     }
/* 1206 */     return this.children;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setChildren(List<Certificate> children) {
/* 1215 */     this.children = children;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CertificateType getType() {
/* 1224 */     return this.type;
/*      */   }
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public Date getCreateTimestamp() {
/* 1230 */     return super.getCreateTimestamp();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   public String getPKIRole() {
/* 1240 */     String role = "";
/* 1241 */     if (this.type != null) {
/* 1242 */       role = this.type.getText();
/*      */     }
/* 1244 */     return role;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTargetVIN() {
/* 1253 */     return this.targetVIN;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUserRole() {
/* 1262 */     return this.userRole;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTargetECU() {
/* 1271 */     return this.targetECU;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public String getPkcs10Signature() {
/* 1281 */     return this.pkcs10;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getValidFrom() {
/* 1290 */     return this.validFrom;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getValidTo() {
/* 1299 */     return this.validTo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public int getValidityStrength() {
/* 1309 */     return this.validityStrength;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
/* 1321 */     Date dateToCheckValidity = getDateToCheckValidity();
/* 1322 */     if (this.state.equals(CertificateState.SIGNING_REQUEST)) {
/* 1323 */       checkingValidToNotBeforeValidFrom();
/*      */     } else {
/* 1325 */       checkingValidToNotBeforeValidFrom();
/* 1326 */       if (getCertificateData().isCertificate()) {
/* 1327 */         getCertificateData().getCert().checkValidity();
/*      */       } else {
/* 1329 */         boolean validOn = getCertificateData().getAttributesCertificateHolder().isValidOn(dateToCheckValidity);
/* 1330 */         if (!validOn) {
/* 1331 */           throw new CertificateExpiredException("NotAfter: " + this.validTo.toString());
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Date getDateToCheckValidity() {
/* 1343 */     if (CertificateType.ENHANCED_RIGHTS_CERTIFICATE.equals(this.type) || CertificateType.SEC_OC_IS.equals(this.type)) {
/*      */       
/*      */       try {
/* 1346 */         String resourceFileName = (null == System.getProperty("spring.profiles.active")) ? "zenzefi.properties" : "sigmodul.properties";
/* 1347 */         String timeTolerance = AbstractConfigurator.loadProperty(resourceFileName, CEBASProperty.VALIDITY_CHECK_TIME_TOLERANCE.name());
/* 1348 */         Date currentDate = new Date();
/* 1349 */         Date dateWithToleranceTime = DateUtils.addMinutes(new Date(), Integer.parseInt(timeTolerance));
/* 1350 */         if (this.validFrom.after(dateWithToleranceTime)) {
/* 1351 */           AlertMessage.addMessageId(AlertMessageId.SYSTEM_CLOCK_IS_NOT_SYNCRONOUS_WITH_PKI.getValue());
/* 1352 */           this.logger.log(Level.FINE, "Time difference between notBefore and current date exceeds the tolerance time: " + timeTolerance + " minutes");
/*      */         } 
/* 1354 */         if (this.validFrom.after(currentDate) && this.validFrom.before(dateWithToleranceTime)) {
/* 1355 */           this.logger.log(Level.FINE, " Time differs from the central time but still is within the defined tolerance level of " + timeTolerance + " minutes");
/*      */         }
/* 1357 */         return dateWithToleranceTime;
/* 1358 */       } catch (NumberFormatException e) {
/* 1359 */         this.logger.log(Level.FINE, "Invalid value of time tolerance property: " + e.getMessage());
/*      */       } 
/*      */     }
/* 1362 */     return new Date();
/*      */   }
/*      */   
/*      */   public void checkingValidToNotBeforeValidFrom() throws CertificateExpiredException {
/* 1366 */     if (this.validTo.before(new Date())) {
/* 1367 */       throw new CertificateExpiredException("NotAfter: " + this.validTo.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class})
/*      */   public boolean isSecOCISCert() {
/* 1377 */     if ("SecOcIs Certificate".equals(this.subject)) {
/* 1378 */       return true;
/*      */     }
/* 1380 */     return (getType() == CertificateType.ENHANCED_RIGHTS_CERTIFICATE && 
/* 1381 */       CertificateParser.containsTargetSubjectKeyIdentifier(getCertificateData().getAttributesCertificateHolder()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CertificateState getState() {
/* 1390 */     return this.state;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setState(CertificateState state) {
/* 1399 */     this.state = state;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getKeyUsageText() {
/* 1406 */     return this.keyUsageText;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getBasicConstraintsText() {
/* 1413 */     return this.basicConstraintsText;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public byte[] getSubjectPublicKeyRaw() {
/* 1421 */     return CertificateParser.getSubjectPublicKeyBytes(getCertificateData().getExisting());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public byte[] getAuthorityKeyIdentifierRaw() {
/* 1431 */     return CertificateParser.getAuthorityKeyIdentifierRaw(getCertificateData().getExisting());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public byte[] getSubjectKeyIdentifierRaw() {
/* 1441 */     return CertificateParser.getSubjectKeyIdentifierRaw(getCertificateData().getExisting());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public byte[] getTargetSubjectKeyIdentifierRaw() {
/* 1451 */     return CertificateParser.hexStringToByteArray(this.targetSubjectKeyIdentifier);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public byte[] getSignatureRaw() {
/* 1461 */     return CertificateParser.getSignatureRaw(getCertificateData().getExisting());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public byte[] getPKIRoleRaw() {
/* 1471 */     return CertificateParser.getPKIRoleRaw(getCertificateData().getExisting());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public byte[] getProdQualifierRaw() {
/* 1481 */     return CertificateParser.getProdQualifierRaw(getCertificateData().getExisting());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public byte[] getUserRolesRaw() {
/* 1491 */     return CertificateParser.getUserRoleRaw(getCertificateData().getExisting());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public byte[] getSpecialEcuRaw() {
/* 1501 */     return CertificateParser.getSpecialEcuRaw(getCertificateData().getExisting());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public byte[] getNonceRaw() {
/* 1511 */     return CertificateParser.getNonceRaw(getCertificateData().getExisting());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTargetSubjectKeyIdentifier() {
/* 1520 */     return this.targetSubjectKeyIdentifier;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void update(X509Certificate certificate, byte[] originalBytes) {
/* 1530 */     fillRawData(certificate, originalBytes);
/* 1531 */     this.validFrom = certificate.getNotBefore();
/* 1532 */     this.validTo = certificate.getNotAfter();
/* 1533 */     this.subject = certificate.getSubjectX500Principal().getName();
/* 1534 */     this.targetECU = CertificateParser.getTargetEcus(certificate);
/* 1535 */     this.issuer = certificate.getIssuerX500Principal().getName();
/* 1536 */     this.serialNo = CertificateParser.getSerialNumber(certificate);
/* 1537 */     this.type = CertificateParser.getPKIRole(certificate);
/* 1538 */     this.targetVIN = CertificateParser.getTargetVin(certificate);
/* 1539 */     this.userRole = CertificateParser.getUserRole(certificate);
/* 1540 */     this.signature = CertificateParser.getSignature(certificate);
/* 1541 */     this.version = CertificateParser.getVersion(certificate);
/* 1542 */     this.uniqueECUID = CertificateParser.getUniqueEcuIds(certificate);
/* 1543 */     this.specialECU = CertificateParser.getSpecialEcu(certificate);
/* 1544 */     this.subjectPublicKey = CertificateParser.getSubjectPublicKey(certificate);
/* 1545 */     this.baseCertificateID = "";
/* 1546 */     this.keyUsage = CertificateParser.getKeyUsage(certificate);
/* 1547 */     this.basicConstraints = CertificateParser.getBasicConstraints(certificate);
/* 1548 */     this.subjectKeyIdentifier = CertificateParser.getSubjectKeyIdentifier(certificate);
/* 1549 */     this.authorityKeyIdentifier = CertificateParser.getAuthorityKeyIdentifier(certificate);
/* 1550 */     this.services = CertificateParser.getServices(certificate);
/* 1551 */     this.nonce = CertificateParser.getNonce(certificate);
/* 1552 */     this.prodQualifier = CertificateParser.getProdQualifier(certificate);
/* 1553 */     this.algorithmIdentifier = CertificateParser.getSignAlgorithm(certificate);
/* 1554 */     this.state = CertificateState.ISSUED;
/* 1555 */     if (this.type == CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE && 
/* 1556 */       CertificateParser.hasNonce(certificate)) {
/* 1557 */       this.type = CertificateType.TIME_CERTIFICATE;
/*      */     }
/* 1559 */     this.issuerSerialNumber = (this.issuerSerialNumber == null) ? "" : this.issuerSerialNumber;
/* 1560 */     this.targetSubjectKeyIdentifier = CertificateParser.getTargetSubjectKeyIdentifier(certificate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateCSR(X509Certificate certificate, byte[] originalBytes) {
/* 1570 */     update(certificate, originalBytes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void update(X509AttributeCertificateHolder attributeHolder, byte[] originalBytes) {
/* 1580 */     fillRawData(attributeHolder, originalBytes);
/* 1581 */     this.validFrom = attributeHolder.getNotBefore();
/* 1582 */     this.validTo = attributeHolder.getNotAfter();
/* 1583 */     this.targetECU = CertificateParser.getTargetEcus(attributeHolder);
/* 1584 */     this.issuer = attributeHolder.getIssuer().getNames()[0].toString();
/* 1585 */     this.serialNo = HexUtil.bytesToHex(attributeHolder.getSerialNumber().toByteArray()).trim();
/* 1586 */     this.type = CertificateParser.getPKIRole(attributeHolder);
/* 1587 */     this.targetVIN = CertificateParser.getTargetVin(attributeHolder);
/* 1588 */     this.userRole = "";
/* 1589 */     this.signature = HexUtil.bytesToHex(attributeHolder.getSignature());
/* 1590 */     this.version = attributeHolder.getVersion() + "";
/* 1591 */     this.uniqueECUID = CertificateParser.getUniqueEcuIds(attributeHolder);
/* 1592 */     this.specialECU = CertificateParser.getSpecialEcu(attributeHolder);
/* 1593 */     this.subjectPublicKey = "";
/* 1594 */     this
/* 1595 */       .baseCertificateID = HexUtil.bytesToHex(attributeHolder.getHolder().getSerialNumber().toByteArray());
/* 1596 */     this.keyUsage = CertificateParser.getKeyUsage(attributeHolder);
/* 1597 */     this.basicConstraints = CertificateParser.getBasicConstraints(attributeHolder);
/* 1598 */     this.subjectKeyIdentifier = CertificateParser.getSubjectKeyIdentifier(attributeHolder);
/* 1599 */     this.authorityKeyIdentifier = CertificateParser.getAuthorityKeyIdentifier(attributeHolder);
/* 1600 */     this.services = CertificateParser.getServices(attributeHolder);
/* 1601 */     this.nonce = CertificateParser.getNonce(attributeHolder);
/* 1602 */     this.prodQualifier = CertificateParser.getProdQualifier(attributeHolder);
/* 1603 */     this.algorithmIdentifier = CertificateParser.getSignAlgorithm(attributeHolder);
/* 1604 */     this.state = CertificateState.ISSUED;
/* 1605 */     this.targetSubjectKeyIdentifier = CertificateParser.getTargetSubjectKeyIdentifier(attributeHolder);
/* 1606 */     if (isSecOCISCert()) {
/* 1607 */       this.subject = "SecOcIs Certificate";
/*      */     } else {
/* 1609 */       this.subject = "EnhRights Certificate";
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateCSR(X509AttributeCertificateHolder attributeHolder, byte[] originalBytes) {
/* 1620 */     update(attributeHolder, originalBytes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public Stream<Certificate> flattened() {
/* 1630 */     return Stream.concat(Stream.of(this), getChildren().stream().flatMap(Certificate::flattened));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public User getUser() {
/* 1640 */     return this.user;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean identicalWithDifference(Certificate certificate) {
/* 1651 */     if (certificate.getSignature().equals(getSignature())) {
/* 1652 */       return !StringUtils.equals(certificate.getSubjectPublicKey(), getSubjectPublicKey());
/*      */     }
/* 1654 */     if (certificate.getSubject().equals(this.subject) && certificate
/* 1655 */       .getAuthorityKeyIdentifier().equals(this.authorityKeyIdentifier) && certificate
/* 1656 */       .getBasicConstraints().equals(this.basicConstraints) && certificate
/* 1657 */       .getIssuer().equals(this.issuer) && certificate
/* 1658 */       .getAlgorithmIdentifier().equals(this.algorithmIdentifier) && 
/* 1659 */       StringUtils.equals(certificate.getKeyUsageText(), this.keyUsageText) && certificate
/* 1660 */       .getNonce().equals(this.nonce) && certificate.getType() == this.type && certificate
/* 1661 */       .getProdQualifier().equals(this.prodQualifier) && certificate
/* 1662 */       .getServices().equals(this.services) && certificate
/* 1663 */       .getSpecialECU().equals(this.specialECU) && certificate
/* 1664 */       .getTargetECU().equals(this.targetECU) && certificate
/* 1665 */       .getTargetVIN().equals(this.targetVIN) && certificate
/* 1666 */       .getUniqueECUID().equals(this.uniqueECUID) && certificate
/* 1667 */       .getUserRole().equals(this.userRole) && certificate
/* 1668 */       .getTargetSubjectKeyIdentifier().equals(this.targetSubjectKeyIdentifier))
/*      */     {
/* 1670 */       return (!certificate.getValidFrom().equals(this.validFrom) || !certificate.getValidTo().equals(this.validTo) || 
/* 1671 */         !StringUtils.equals(certificate.getSubjectPublicKey(), this.subjectPublicKey) || 
/* 1672 */         !certificate.getSerialNo().equals(this.serialNo) || 
/* 1673 */         !StringUtils.equals(certificate.getSubjectKeyIdentifier(), this.subjectKeyIdentifier));
/*      */     }
/*      */ 
/*      */     
/* 1677 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getParentId() {
/* 1686 */     return this.parentId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setParentId(String id) {
/* 1693 */     this.parentId = id;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setAuthorityKeyIdentifier(String akid) {
/* 1703 */     this.authorityKeyIdentifier = akid;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setValidityColor(int yellowMinValue, int greenMinValue) {
/* 1713 */     int redMinValue = 0;
/*      */     
/* 1715 */     if (this.validityStrength >= greenMinValue) {
/* 1716 */       this.validityStrengthColor = "green";
/* 1717 */     } else if (this.validityStrength >= yellowMinValue) {
/* 1718 */       this.validityStrengthColor = "yellow";
/* 1719 */     } else if (this.validityStrength > 0) {
/* 1720 */       this.validityStrengthColor = "red";
/*      */     } else {
/* 1722 */       this.validityStrengthColor = "warn";
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getValidityStrengthColor() {
/* 1732 */     return this.validityStrengthColor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonProperty
/*      */   public boolean hasChildren() {
/* 1742 */     return this.hasChildren;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Versioned toVersion(int version) {
/* 1748 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private byte[] getBytesFromAttributeCertificate(X509AttributeCertificateHolder attributeHolder) {
/* 1758 */     byte[] originalBytes = null;
/*      */     try {
/* 1760 */       originalBytes = attributeHolder.getEncoded();
/* 1761 */     } catch (IOException e) {
/* 1762 */       this.logger.log(Level.FINEST, "Failed to get the encoded form of the attrivute certificate with serial no: " + attributeHolder
/* 1763 */           .getSerialNumber(), e);
/*      */     } 
/* 1765 */     return originalBytes;
/*      */   }
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public Date getUpdateTimestamp() {
/* 1771 */     return super.getUpdateTimestamp();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonProperty("id")
/*      */   @JsonView({CertificatesView.Management.class, CertificatesView.Detail.class, CertificatesView.SelectionView.class, CertificatesView.SelectionEnhancedRightsView.class})
/*      */   public String getEntityId() {
/* 1779 */     return super.getEntityId();
/*      */   }
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public String getCreateUser() {
/* 1785 */     return super.getCreateUser();
/*      */   }
/*      */   
/*      */   @JsonIgnore
/*      */   public boolean isEnhancedRights() {
/* 1790 */     return (this.type == CertificateType.ENHANCED_RIGHTS_CERTIFICATE);
/*      */   }
/*      */   
/*      */   @JsonIgnore
/*      */   public boolean isVsmEcu() {
/* 1795 */     return (getType() == CertificateType.ECU_CERTIFICATE && getSpecialECU().equals("1"));
/*      */   }
/*      */   
/*      */   @JsonIgnore
/*      */   public boolean isNonVsmEcu() {
/* 1800 */     return (getType() == CertificateType.ECU_CERTIFICATE && !getSpecialECU().equals("1"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @JsonIgnore
/*      */   public boolean isLink() {
/* 1810 */     return (getType() == CertificateType.ROOT_CA_LINK_CERTIFICATE || getType() == CertificateType.BACKEND_CA_LINK_CERTIFICATE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getZkNo() {
/* 1817 */     if (this.zkNo == null)
/* 1818 */       return ""; 
/* 1819 */     return this.zkNo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setZkNo(String zkno) {
/* 1827 */     this.zkNo = zkno;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getEcuPackageTs() {
/* 1834 */     return this.ecuPackageTs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEcuPackageTs(String ecuPackageTs) {
/* 1841 */     this.ecuPackageTs = ecuPackageTs;
/*      */   }
/*      */   
/*      */   public String getLinkCertTs() {
/* 1845 */     return this.linkCertTs;
/*      */   }
/*      */   
/*      */   public void setLinkCertTs(String linkCertTs) {
/* 1849 */     this.linkCertTs = linkCertTs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CertificatePKIState getPkiState() {
/* 1858 */     return this.pkiState;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPkiState(CertificatePKIState pkiState) {
/* 1868 */     this.pkiState = pkiState;
/*      */   }
/*      */   
/*      */   @JsonIgnore
/*      */   public boolean isUnderBackend() {
/* 1873 */     return (getType() != CertificateType.ROOT_CA_CERTIFICATE && getType() != CertificateType.BACKEND_CA_CERTIFICATE && 
/* 1874 */       getType() != CertificateType.ROOT_CA_LINK_CERTIFICATE);
/*      */   }
/*      */   
/*      */   @JsonIgnore
/*      */   public BackendContext getBackendContext() {
/* 1879 */     String zkNo = "";
/* 1880 */     String ecuPackageTs = "";
/* 1881 */     String linkCertTs = "";
/* 1882 */     if (isUnderBackend()) {
/* 1883 */       Certificate parent = getParent();
/* 1884 */       if (parent != null) {
/* 1885 */         if (parent.getType() == CertificateType.DIAGNOSTIC_AUTHENTICATION_CERTIFICATE) {
/* 1886 */           Certificate diagnosticParent = parent.getParent();
/* 1887 */           zkNo = diagnosticParent.getZkNo();
/* 1888 */           ecuPackageTs = diagnosticParent.getEcuPackageTs();
/* 1889 */           linkCertTs = diagnosticParent.getLinkCertTs();
/*      */         } else {
/* 1891 */           zkNo = parent.getZkNo();
/* 1892 */           ecuPackageTs = parent.getEcuPackageTs();
/* 1893 */           linkCertTs = parent.getLinkCertTs();
/*      */         } 
/*      */       }
/*      */     } 
/* 1897 */     if (getType() == CertificateType.BACKEND_CA_CERTIFICATE) {
/* 1898 */       zkNo = getZkNo();
/* 1899 */       ecuPackageTs = getEcuPackageTs();
/* 1900 */       linkCertTs = getLinkCertTs();
/*      */     } 
/* 1902 */     return new BackendContext(zkNo, ecuPackageTs, linkCertTs);
/*      */   }
/*      */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\certificates\entity\Certificate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */