/*      */ package com.daimler.cebas.common.control;
/*      */ 
/*      */ import com.daimler.cebas.common.control.annotations.CEBASControl;
/*      */ import java.util.Locale;
/*      */ import org.springframework.beans.factory.annotation.Autowired;
/*      */ import org.springframework.context.MessageSource;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @CEBASControl
/*      */ public class CEBASI18N
/*      */ {
/*      */   public static final String WRONG_SERVER_ADDRESS = "wrongServerAddress";
/*      */   public static final String NAME = "CEBASI18N";
/*      */   public static final String INVALID_CERT_TYPE = "invalidCertificateType";
/*      */   public static final String IMPORT_INVALID_USER_ROLE = "importInvalidUserRole";
/*      */   public static final String CERTIFICATE_EXPIRED = "certificateExpiredInvalidDates";
/*      */   public static final String INVALID_PUBLIC_KEY = "invalidPublicKey";
/*      */   public static final String INVALID_AUTH_KEY_IDENTIFIER = "invalidAuthKeyIdentifier";
/*      */   public static final String INVALID_SUBJ_KEY_IDENTIFIER = "invalidSubjKeyIdentifier";
/*      */   public static final String INVALID_INPUT_FOR_IDENTIFIER = "invalidInputForIdentifier";
/*      */   public static final String INVALID_SUBJECT = "invalidSubject";
/*      */   public static final String INVALID_KEY_USAGE = "invalidKeyUsage";
/*      */   public static final String INVALID_BASIC_CONSTRAINTS = "invalidBasicConstraints";
/*      */   public static final String INVALID_UNIQUE_ECU_ID = "invalidUniqueEcuId";
/*      */   public static final String INVALID_SPECIAL_ECU = "invalidSpecialEcu";
/*      */   public static final String INVALID_TARGET_ECU = "invalidTargetEcu";
/*      */   public static final String INVALID_TARGET_VIN = "invalidTargetVin";
/*      */   public static final String INVALID_USER_ROLE = "invalidUserRole";
/*      */   public static final String INVALID_NONCE = "invalidNonce";
/*      */   public static final String INVALID_SERVICES = "invalidServices";
/*      */   public static final String INVALID_BASE_CERTIFICATE_ID = "invalidBaseCertificateId";
/*      */   public static final String INVALID_ALG_IDENTIFIER = "invalidAlgIdentifier";
/*      */   public static final String INVALID_SIGNATURE = "invalidSignature";
/*      */   public static final String INVALID_PROD_QUALIFIER = "invalidProdQualifier";
/*      */   public static final String INVALID_PKI_ROLE = "invalidPKIRole";
/*      */   public static final String INVALID_ISSUER = "invalidIssuer";
/*      */   public static final String INVALID_ISSUER_SERIAL_NUMBER = "invalidIssuerSerialNumber";
/*      */   public static final String INVALID_VERSION = "invalidVersion";
/*      */   public static final String INVALID_SERIAL_NUMBER = "invalidSerialNumber";
/*      */   public static final String INVALID_FORMAT_FOR_USER_ROLE = "invalidFormatForUserRole";
/*      */   public static final String INVALID_INPUT_FOR_CHECK_OWNERSHIP = "invalidInputForCheckOwnership";
/*      */   public static final String MULTIPLE_CERT_DIF_SIGNATURE = "multipleCertificatesDifSig";
/*      */   public static final String CERT_ALREADY_EXISTS_WITH_SUBJ_KEY = "certAlreadyExistsWithSubjectKey";
/*      */   public static final String CERT_ALREADY_EXISTS_WITH_SUBJ_KEY_AND_ZK_NO = "certAlreadyExistsWithSubjectKeyAndZkNo";
/*      */   public static final String CERT_ALREADY_EXISTS_WITH_AUTH_KEY_AND_SN = "certAlreadyExistsWithAuthKeyAndSN";
/*      */   public static final String CERT_PUBLIC_KEY_DOESNT_MATCH_PUBLIC_KEY_FROM_PRIVATE_KEY = "certPublicKeyDoesntMatchPublicKeyFromPrivateKey";
/*      */   public static final String ROOT_NOT_FOUND_FOR_BACKEND_SUBJ_KEY = "rootNotFoundForBackendSubjKey";
/*      */   public static final String ROOT_NOT_FOUND_WHICH_COULD_HAVE_ISSUED_BACKEND = "rootNotFoundWhichCouldHaveIssuedBackend";
/*      */   public static final String SIG_VERIFICATION_FAILED_BACKEND_SUBJ_KEY = "sigVerificationFailedBackendSubjKey";
/*      */   public static final String BACKEND_NOT_FOUND_ASSOCIATED_INPUT_LINK = "backendNotFoundAssociatedInputLink";
/*      */   public static final String SIG_VERIFICATION_FAILED_INPUT_LINK_SUBJ_KEY = "sigVerificationFailedInputLinkSubjKey";
/*      */   public static final String BACKEND_NOT_FOUND_ISSUED_LINK_SUBJ_KEY = "backendNotFoundIssuedLinkSubjKey";
/*      */   public static final String ENH_RIGHTS_ALREADY_EXISTS_WITH_SIGNATURE = "enhRightsAlreadyExistsWithSignature";
/*      */   public static final String DIAG_NOT_FOUND_FOR_INPUT_ENH_RIGHTS = "diagNotFoundForInputEnhRights";
/*      */   public static final String SIG_VERIFICATION_FAILED_ENH_RIGHTS_SIG_KEY = "sigVerificationFailedForEnhRightsWithSigKey";
/*      */   public static final String BACKEND_NOT_FOUND_WHICH_ISSUED_DIAG_CERT_ENG_RIGHTS = "backendNotFoundIssuedDiagCertForEnhRights";
/*      */   public static final String SIG_VERIFICATION_FAILED_ROOT_SUBJ_KEY = "sigVerificationFailedRootSubjKey";
/*      */   public static final String ROOT_NOT_FOUND_ISSUED_LINK_SUBJ_KEY = "rootNotFoundIssuedLinkSubjKey";
/*      */   public static final String SIG_VERIFICATION_FAILED_ROOT_LINK_SUBJ_KEY = "sigVerificationFailedRootLinkSubjKey";
/*      */   public static final String ROOT_NOT_FOUND_ASSOCIATED_INPUT_LINK_SUBJ_KEY = "rootNotFoundAssociatedInputLinkSubjKey";
/*      */   public static final String CSR_AND_PRIVATE_KEY_NOT_FOUND_SUBJ_KEY = "csrAndPrivateKeyNotFoundForSubjKey";
/*      */   public static final String BOTH_CSR_AND_PRIVATE_KEY_FOUND_SUBJ_KEY = "csrAndPrivateKeyFoundForSubjKey";
/*      */   public static final String BACKEND_NOT_FOUND_SUBJ_KEY = "backendNotFoundSubjKey";
/*      */   public static final String BACKEND_NOT_FOUND_ISSUED_SUBJ_KEY = "backendNotFoundIssuedSubjKey";
/*      */   public static final String SIG_VERIFICATION_FAILED_FOR_SUBJ_KEY = "sigVerificationFailedSubjKey";
/*      */   public static final String INVALID_OR_MISSING_PRIVATE_KEY = "invalidOrMissingPrivateKey";
/*      */   public static final String UNKNOWN_CERTIFICATE_TYPE = "unknownCertificateType";
/*      */   public static final String CONFIG_NOT_FOUND_DELETING_CERT = "configNotFoundForDeleting";
/*      */   public static final String CONFIG_NOT_FOUND_PROXY = "configNotFoundForProxy";
/*      */   public static final String DIAG_CERT_ONLY_ENH_RIGHTS_CSR = "diagCertOnlyEnhRightsCSRUnderIt";
/*      */   public static final String PROVIDED_CERT_PARENT_DOES_NOT_EXIST = "providedCertParentNotExist";
/*      */   public static final String PARENT_INVALID_BACKEND_DIAG_SUPPORTED_ENH_RIGHTS = "parentCertInvalidBackendAndDiagSupportedForEnhRights";
/*      */   public static final String USER_NOT_ONLINE = "userNotOnline";
/*      */   public static final String REGISTERED_USER_NEEDS_TO_BE_LOGGED_IN = "registeredUserNeedsToBeLoggedIn";
/*      */   public static final String CERTIFICATE_DOES_NOT_EXIST = "certificateDoesNotExist";
/*      */   public static final String CANNOT_CHANGE_ACTIVE_FOR_TESTING_AUTOMATIC_MODE = "cannotChangeActiveForTestingAutomaticMode";
/*      */   public static final String NOT_FOUND_NEW_CERT_ACTIVE_FOR_TESTING = "notFoundNewCertActiveForTesting";
/*      */   public static final String NOT_FOUND_OLD_CERT_ACTIVE_FOR_TESTING = "notFoundOldCertActiveForTesting";
/*      */   public static final String CANNOT_SWITCH_ACTIVE_FOR_TESTING = "cannotSwitchActiveForTesting";
/*      */   public static final String ENHANCED_RIGHTS_SUPPORTS_MULTIPLE_SELECTION = "enhancedRightsSupportsMultipleSelection";
/*      */   public static final String CERTIFICATE_DOES_NOT_EXIST_IN_USER_STORE = "certificateDoesNotExistInUserStore";
/*      */   public static final String SEARCHED_CERTIFICATE_DOES_NOT_EXIST_IN_USER_STORE = "searchedCertificateDoesNotExistInUserStore";
/*      */   public static final String DELETE_CERTIFICATES_CALLED_RESULT_NOT_FOUND = "deleteCertificatesCalledResultNotFound";
/*      */   public static final String DELETE_ECU_CERTIFICATE = "deleteEcuCertificate";
/*      */   public static final String DELETED_USER_KEY_PAIR_FOR_PRODUCTION = "deletedUserKeyPairForProduction";
/*      */   public static final String CANNOT_READ_FILE = "cannotReadFile";
/*      */   public static final String INTEGRITY_REPORT_NOT_FOUND = "systemIntegrityReportNotFound";
/*      */   public static final String BACKEND_SUBJ_KEY_DATA_TO_BE_SIGNED_MANDATORY = "backendSubjKeyAndDataToBeSignedMandatory";
/*      */   public static final String SIGNATURE_CODING_DATA_IDENTIFIER_NOT_NULL_OR_EMPTY = "signatureCodingDataIdentifierNotNullOrEmpty";
/*      */   public static final String NO_CERTIFICATE_FOUND_FOR_SIGNING = "noCertificateFoundForSigning";
/*      */   public static final String CANNOT_DOWNLOAD_SYSTEM_INTEGRITY_REPORT = "cannotDownloadSystemIntegrityReport";
/*      */   public static final String NO_CERTIFICATE_FOUND_MATCHING_CRITERIA = "noCertificateFoundMatchingCriteria";
/*      */   public static final String NO_CERTIFICATE_FOUND_MATCHING_CRITERIA_SECURE_VARIANT_CODING = "noCertificateFoundMatchingCriteriaVariantCoding";
/*      */   public static final String NO_CERTIFICATE_FOUND_MATCHING_CRITERIA_BASIC = "noCertificateFoundMatchingCriteriaBasic";
/*      */   public static final String NO_CERTIFICATE_FOUND_MATCHING_CRITERIA_ENHANCED = "noCertificateFoundMatchingCriteriaEnhanced";
/*      */   public static final String NO_TIME_CERTIFICATE_FOUND_MATCHING_CRITERIA_CSR_CREATED = "noTimeCertFoundMatchingFilter";
/*      */   public static final String NO_TIME_CERTIFICATE_FOUND_MATCHING_FILTER_CRITERIA = "noTimeCertFoundMatchingFilterCriteria";
/*      */   public static final String NO_TIME_CSR_CAN_BE_CREATED_USER_IS_NOT_LOGGED_IN = "noTimeCSRCanBeCreatedUserIsNotLoggedIn";
/*      */   public static final String NO_SECOCIS_CSR_CAN_BE_CREATED_USER_IS_NOT_LOGGED_IN = "noSecOCISCSRCanBeCreatedUserIsNotLoggedIn";
/*      */   public static final String NO_CSR_CAN_BE_CREATED_FOR_PKI_UNKNOWN_BACKEND = "noCSRCanBeCreatedForPKIUnknownBackend";
/*      */   public static final String CERTIFICATE_REPLACEMENT_PACKAGE_NOT_FOUND = "certificateReplacementPackageNotFound";
/*      */   public static final String REPLACEMENT_PACKAGE_TARGET_DETERMINED = "replacementPackageTargetDetermined";
/*      */   public static final String REPLACEMENT_PACKAGE_TARGET_DETERMINATION_CERT_NOT_FOUND = "determiningTargetCertificateNotFound";
/*      */   public static final String INVALID_REPLACEMENT_PACKAGE_TAGET_VALUE = "invalidReplacementPackageTargetValue";
/*      */   public static final String MORE_DIAG_CERT_FOUND_FOR_COMBINATION = "moreDiagCertFoundForCombo";
/*      */   public static final String NO_SEC_OCIS_FOUND_MATCHING_FILTER = "noSecOcisFoundmatchingFilter";
/*      */   public static final String SEC_OCIS_FOUND_MATCHING_FILTER = "secOcisFoundmatchingFilter";
/*      */   public static final String NO_SEC_OCIS_FOUND_MATCHING_FILTER_CRITERIA = "noSecOcisFoundMatchingFilterCriteria";
/*      */   public static final String WRONG_INPUT_CREATING_CERTIFICATE = "wrongInputCreatingCertificate";
/*      */   public static final String PRIVATE_KEY_ALREADY_USED = "privateKeyInChainAreadyUsed";
/*      */   public static final String TARGET_ECU_NOT_MATCHES_PATTERN = "targetECUNotMatchPattern";
/*      */   public static final String TARGET_VIN_NOT_MATCHES_PATTERN = "targetVINNotMatchPattern";
/*      */   public static final String PARENT_CANNOT_BE_EMPTY = "parentCannotBeEmpty";
/*      */   public static final String USER_ROLE_CANNOT_BE_EMPTY = "userRoleCannotBeEmpty";
/*      */   public static final String SPECIAL_ECU_BYTES_MAX_SIZE = "specialECUBBytesMaxSize";
/*      */   public static final String UNIQUE_ECU_ID_BYTES_MAX_SIZE = "uniqueEcuIDMaxSize";
/*      */   public static final String TARGET_ECU_NOT_APPLY_TO_ECU = "targetECUNotApplyToECU";
/*      */   public static final String TARGET_VIN_NOT_APPLY_TO_ECU = "targetVINNotApplyToECU";
/*      */   public static final String NONCE_NOT_EMPTY_MATCH_PATTERN = "nonceCannotBeEmptyMatchPattern";
/*      */   public static final String NONCE_SET_ONLY_FOR_TIME_CERTIFICATES = "nonceSetOnlyForTimeCert";
/*      */   public static final String USER_ROLE_SET_DIAG_OR_TIME_CERT = "userRoleSetDiagCertificate";
/*      */   public static final String UNIQUE_ECU_ID_ONLY_FOR_ECU_CERT = "uniqueECUIDOnlyForECUCert";
/*      */   public static final String SPECIAL_ECU_ONLY_FOR_ECU_CERT = "specialECUCanBeOnlyECUCert";
/*      */   public static final String VALID_TO_NOT_EMPTY = "validToNotEmpty";
/*      */   public static final String SIGN_REQUEST_FAILED = "certificateSignReqFailed";
/*      */   public static final String FOUND_ROOT_FOLDER_WITHOUT_ROOT_CERT = "foundRootFolderEmpty";
/*      */   public static final String NOT_FOUND_ROOT_FOLDERS = "noRootFoldersFound";
/*      */   public static final String PARENT_SUBJ_KEY_NOT_MATCH_CHILD_AUTH_KEY = "parentSubjectKeyNotMatchChildAuthKey";
/*      */   public static final String PARENT_SUBJ_KEY_NOT_MATCH_CHILD_SUBJ_KEY = "parentSubjectKeyNotMatchChildSubjKey";
/*      */   public static final String FAILED_CERTIFICATE_TYPE_VARIANT_CODING_USER = "failedCertificateTypeVariantCodingDevice";
/*      */   public static final String CERTIFICATE_VALIDATED_SUCCESSFULLY = "certificateValidatesSuccessfully";
/*      */   public static final String CERTIFICATE_IMPORTED_SUCCESSFULLY = "certificateImportedSuccessfully";
/*      */   public static final String CERTIFICATE_IMPORTED_SUCCESSFULLY_DETAIL_MESSAGE = "certificateImportedSuccessfullyDetailMessage";
/*      */   public static final String IMPORTED_CERTIFICATE_NOT_X_509_COMPLIANT = "importedCertificateNotX509Compliant";
/*      */   public static final String IMPORT_NOT_ALLOWED_INVALID_CERTIFICATE_FIELD_PRESENT = "importNotAllowedInvalidFieldPresent";
/*      */   public static final String CERTIFICATE_WITH_ID_RETRIEVED_FROM_USER_STORE = "certificateWithIDRetrievedFromUserStore";
/*      */   public static final String RETRIEVED_ALL_CERTIFICATES = "retrievedAllCertificates";
/*      */   public static final String ROOT_CERTIFICATE_REMOVED_FROM_USER_STORE = "rootCertificateRemovedFromUserStore";
/*      */   public static final String CSR_CREATED_TYPE = "csrCreatedType";
/*      */   public static final String CANNOT_READ_FILE_FROM_PATH = "cannotReadFileFromPath";
/*      */   public static final String CANNOT_CREATE_CSR = "cannotCreateCSR";
/*      */   public static final String CANNOT_CREATE_CSR_FOR_PERMISSION = "cannotCreateCSRForPermission";
/*      */   public static final String ENHANCED_RIGHTS_EXISTS_ONLY_IN_CONTEXT_DIAG_AUTH = "enhRightsExistsOnlyInContextOfDiagAuth";
/*      */   public static final String ENHANCED_RIGHTS_FOUND_FOR_SERVICE = "enhRightsFoundForService";
/*      */   public static final String GET_ENHANCED_RIGHTS_FOR_SERVICE = "getEnhRightsForService";
/*      */   public static final String CANNOT_READ_THE_MULTIPART = "cannotReadTheMultipart";
/*      */   public static final String FAILED_TO_PROCESS_DIRECTORY_STREAM = "failedToProcessDirectoryStream";
/*      */   public static final String CANNOT_CLOSE_OUTPUT_STREAM_AFTER_GENERATE_PKCS12 = "cannotCloseOutputStreamAfterGeneratePkcs12";
/*      */   public static final String CANNOT_PROCESS_OUTPUT_STREAM_ON_GENERATE_PKCS12 = "cannotProcessOutputStreamOnGeneratePkcs12";
/*      */   public static final String ERROR_ADDING_PKCS_12_ENTRY = "errorAddingPKCS12Entry";
/*      */   public static final String CANNOT_ADD_EMPTY_PKCS12_ENTRY = "cannotAddEmptyPKCS12Entry";
/*      */   public static final String CANNOT_CLOSE_DIRECTORY_STREAM_AFTER_PROCESSING_VALIDATION = "cannotCloseDirectoryStreamAfterProcessingValidation";
/*      */   public static final String FAILED_TO_GET_DIRECTORY_STREAM = "failedToGetDirectoryStream";
/*      */   public static final String CANNOT_CLOSE_DIRECTORY_STREAM_AFTER_PROCESSING_IMPORT = "cannotCloseDirectoryStreamAfterProcessingImport";
/*      */   public static final String CANNOT_CLOSE_STREAM_ON_CERTIFICATE_OPERATIONS = "cannotCloseStreamOnCertificateOperations";
/*      */   public static final String FOUND_DIAG_CERT_WITH_PARAMS = "foundDiagCertWithParams";
/*      */   public static final String CHECK_OWNERSHIP_SUCCESS = "checkOwnershipSuccess";
/*      */   public static final String SECURE_VARIANT_CODING_SUCCESS = "secureVariantCodingSuccess";
/*      */   public static final String COMPUTE_SIGNATURES = "computeSignatures";
/*      */   public static final String SIGN_ECU = "signEcu";
/*      */   public static final String FOUND_DIAG_CERT_FOR_CRITERIA = "foundDiagCertificateForCriteria";
/*      */   public static final String FOUND_ENH_CERT_FOR_CRITERIA = "foundEnhCertificateForCriteria";
/*      */   public static final String CERT_NOT_FOUND_MATCHING_FILTER_CRITERIA_DIFF_SN = "noCertificateFoundMatchingTheFilterCriteriaDifferentSN";
/*      */   public static final String ACTIVE_DIAG_MANUAL_SELECTION = "activeDiagManualSelection";
/*      */   public static final String CERT_NOT_FOUND_WITH_GIVEN_SERIAL_NO = "noCertificateWithGivenSNFound";
/*      */   public static final String MORE_THAN_ONE_CERT_FOUND_FOR_SN = "moreThanOneCertificateFoundForThisSN";
/*      */   public static final String RETURN_OWNERSHIP_WITH_PARAMS = "returnOwnershipWithParams";
/*      */   public static final String CHECK_OWNERSHIP_FAILED = "checkOwnershipValidationFailed";
/*      */   public static final String SIGNING_CODING_STRING_FAILED = "signingCodingStringFailed";
/*      */   public static final String WRONG_INPUT_ALL_PARAMS_MANDATORY = "wrongInputAllParamsMandatory";
/*      */   public static final String COULD_NOT_VERIFY_SIGNATURE_FOR_MESSAGE = "couldNotVerifySignatureForMessage";
/*      */   public static final String COULD_NOT_VERIFY_SIGNATURE = "couldNotVerifySignature";
/*      */   public static final String COULD_NOT_GENERATE_HOLDER = "couldNotGenerateHolder";
/*      */   public static final String COULD_NOT_FIND_DIAGNOSTIC_CERTIFICATE = "couldNotFindDiagCert";
/*      */   public static final String WRITING_PUBLIC_KEY_FAILED = "writingPublicKeyToFileFailed";
/*      */   public static final String WRITING_CSR_TO_FILE_FAILED = "writingCsrToFileFailed";
/*      */   public static final String COULD_NOT_CHECK_SIGNATURE_OF_MESSAGE_FOR_ENH_RIGHTS = "couldNotCheckSignatureOfMessageForEngRights";
/*      */   public static final String COULD_NOT_CREATE_CERTIFICATE_FROM_BYTES = "couldNotCreateCertFormBytes";
/*      */   public static final String DELETE_EXPIRED_CERTIFICATE_CRON_TRIGGER = "deleteExpiredCertificateCronTrigger";
/*      */   public static final String DELETE_CERTIFICATE_USER_ACTION = "deleteCertificateUserAction";
/*      */   public static final String DELETE_CERTIFICATE_USER_ACTION_WITH_ZK_NO = "deleteCertificateUserActionWithPN";
/*      */   public static final String DELETE_CSR_USER_ACTION = "deleteCSRUserAction";
/*      */   public static final String DELETE_MORE_THAN_2_IDENTICAL_CERTIFICATES = "deleteMoreThan2IdenticalCertificates";
/*      */   public static final String DELETE_CERTIFICATE_AT_ROLLBACK = "deleteCertificateAtRollback";
/*      */   public static final String DELETE_CERTIFICATE_DURING_IMPORT = "deleteCertificateDuringImport";
/*      */   public static final String DELETE_ECU_CERTS_START = "deleteEcuCertsStart";
/*      */   public static final String DELETE_ECU_CERTS_END = "deleteEcuCertsEnd";
/*      */   public static final String DELETE_VARIANT_CERTS_START = "deleteVariantCertsStart";
/*      */   public static final String DELETE_VARIANT_CERTS_END = "deleteVariantCertsEnd";
/*      */   public static final String DELETE_DIAGNOSTIC_CERTS_START = "deleteDiagnosticCertsStart";
/*      */   public static final String DELETE_DIAGNOSTIC_CERTS_END = "deleteDiagnosticCertsEnd";
/*      */   public static final String NO_CERTIFICATE_FOUND_FOR_BACKEND_SKID_WHEN_CREATING_CSR = "noCertificateFoundforBackendSkidWhenCreatingCSR";
/*      */   public static final String COULD_NOT_PARSE_MAX_DATE_FOR_TIME_CERT = "couldNotParseMaxDateForTimeCert";
/*      */   public static final String COULD_NOT_PARSE_MAX_DATE_FOR_CERT = "couldNotParseMaxDateForCert";
/*      */   public static final String COULD_NOT_PARSE_MAX_DATE_FOR_SEC_OCIS = "couldNotParseMaxDateForSecOCIS";
/*      */   public static final String SIGN_CHALLANGE_WITH_PRIVATE_KEY_FAILED = "signChallangeWithPrivateKeyFailed";
/*      */   public static final String ERROR_GETTING_ENCODED_CERT_BYTES = "errorGettingEncodedCertificateBytes";
/*      */   public static final String NOT_ALLOWED_TO_IMPORT_PRIVATE_KEY = "notAllowedToImportPrivateKey";
/*      */   public static final String COULD_NOT_READ_PRIVATE_KEY = "couldNotReadPrivateKey";
/*      */   public static final String NO_CERTIFICATE_FOUND_GIVEN_AKI_SN = "noCertificateFoundWithGivenAKIAndSN";
/*      */   public static final String SIGNATURE_VERIFICATION_FAILED = "signatureVerificationFailed";
/*      */   public static final String FOUND_MULTIPLE_CERTIFICATES_TO_BE_REPLACED_SIGNATURE_PK = "foundMultipleCertificatesToBeReplacedBySignatureAndPK";
/*      */   public static final String CERTIFICATE_WITH_SIGNATURE_AND_PK_WILL_BE_REPLACED = "certificateWithSignatureAndPKReplaced";
/*      */   public static final String ISSUER_NOT_FOUND = "issuerNotFound";
/*      */   public static final String FOUND_MULTIPLE_CERTIFICATES_DIFF_SIGNATURE_SAME_PUBLIC_KEY = "foundMultipleCertificatesWithDiffSigButSamePublicKey";
/*      */   public static final String CSR_TYPE_NOT_MATCH_CERTIFICATE_TYPE = "csrTypeNotMatchCertType";
/*      */   public static final String CERTIFICATE_BYTES_CANNOT_BE_READ = "certificateBytesCannotBeRead";
/*      */   public static final String CSR_VALIDATION_FAILED = "csrValidationFailed";
/*      */   public static final String VALIDATION_FAILED = "validationFailed";
/*      */   public static final String DELETING_EXPIRED_CERTIFICATES_FOR_USER = "deletingExpiredCertificatesForUser";
/*      */   public static final String TRYING_TO_DELETE_EXPIRED_CERTS = "tryingToDeleteExpiredCerts";
/*      */   public static final String CHAIN_OF_TRUST_VALIDATION_FAILED = "chainOfTrustValidationFailed";
/*      */   public static final String CERTIFICATE_STRUCTURE_VALIDATION_FAILED = "certificateStructureValidationFailed";
/*      */   public static final String CHAIN_OF_TRUST_IMPORTING_FAILED = "chainOfTrustImportingFailed";
/*      */   public static final String FOUND_MORE_CERTIFICATES_ACTIVE_FOR_TESTING = "foundMoreCertificatesActiveForTesting";
/*      */   public static final String NOT_FOUND_CERTIFICATE = "notFoundCertificate";
/*      */   public static final String NOT_FOUND_ECU_CERTIFICATE = "notFoundECUCertificate";
/*      */   public static final String CERTIFICATE_NOT_FOUND = "certificateNotFound";
/*      */   public static final String CERTIFICATE_NOT_FOUND_MANUAL_SELECTION = "certificateNotFoundManualSelection";
/*      */   public static final String CERTIFICATE_OF_TYPE_NOT_FOUND = "certificateOfTypeNotFound";
/*      */   public static final String CERTIFICATE_OF_TYPE_NOT_FOUND_FOR_IDENTIFIER = "certificateOfTypeNotFoundForIdentifier";
/*      */   public static final String NO_CERTIFICATE_FOUND_WITH_ECU_VIN_AND_REMAINING_VALIDITY = "noCertificateFoundWithEcuVinAndRemainingValidity";
/*      */   public static final String NOT_RECEIVED_CERTIFICATE_BASED_ON_CSR = "notReceivedCertificateBasedOnCSR";
/*      */   public static final String CSR_NOT_FOUND = "csrNotFound";
/*      */   public static final String NOT_FOUND_LINK_CERTIFICATE = "notFoundLinkCertificate";
/*      */   public static final String COULD_NOT_FIND_CERTIFICATE_FROM_BYTES = "couldNotFindCertificateFromBytes";
/*      */   public static final String FOUND_MORE_SEC_OCIS_ACTIVE_FOR_TESTING = "foundMoreSecOcisActiveForTesting";
/*      */   public static final String NO_KEY_PAIR_FOUND_FOR_CERTIFICATE = "noKeyPairFoundForCert";
/*      */   public static final String CURRENT_USER_CONFIG_RETRIEVED = "currentUserConfigRetrieved";
/*      */   public static final String GENERAL_CONFIG_RETRIEVED = "generalConfigRetrieved";
/*      */   public static final String CONFIG_WITH_ID_UPDATED = "configWithIdUpdated";
/*      */   public static final String COULD_NOT_UPDATE_CONFIGURATION = "couldNotUpdateConfiguration";
/*      */   public static final String COULD_NOT_FIND_CONFIGURATION = "couldNotFindConfiguration";
/*      */   public static final String CONFIGURATION_MUST_NOT_BE_NULL = "configurationMustNotBeNull";
/*      */   public static final String NULL_CONFIGURATION_FOR_COPY_FOUND = "nullConfigForCopyFound";
/*      */   public static final String RESTORED_CERTIFICATES = "restoredCertificates";
/*      */   public static final String SYSTEM_INTEGRITY_CHECK_ERROR_NO_MACHINE_NAME_CONFIG = "systemIntegrityCheckFailedNoMachineNameConfig";
/*      */   public static final String SYSTEM_INTEGRITY_CHECK_ERROR_WRONG_MACHINE_NAME = "systemIntegrityCheckFailedWrongMachineName";
/*      */   public static final String LIMITED_CRYPTOGRAPHY_STRENGTH = "limitedCryptographyStrength";
/*      */   public static final String INVALID_BUSINESS_ENV_CONFIG = "invalidBusinessEnvConfig";
/*      */   public static final String INVALID_CERTIFICATE_TABLE_COLUMNS_CONFIG = "invalidCertificateTableColumnsConfig";
/*      */   public static final String BUSINESS_ENV_CONFIG = "businessEnvConfig";
/*      */   public static final String USER_ALREADY_EXISTS = "userAlreadyExists";
/*      */   public static final String DEFAULT_USER = "defaultUser";
/*      */   public static final String USER_WAS_REGISTERED = "userWasRegistered";
/*      */   public static final String UNKNOWN_USER = "unknownUser";
/*      */   public static final String USER_DOES_NOT_EXIST = "userDoesNotExist";
/*      */   public static final String USER_INVALID_FOR_REGISTRATION = "userInvalidForRegistration";
/*      */   public static final String USER_HAVE_INVALID_FIELD = "userHaveInvalidField";
/*      */   public static final String USER_ALREADY_LOGGED_IN = "userAlreadyLoggedIn";
/*      */   public static final String USER_LOGGED_IN = "userLoggedIn";
/*      */   public static final String USER_LOGIN_CREDENTIALS_NOT_MATCH = "userLoginPasswordsNotMatch";
/*      */   public static final String USER_LOGIN_FAILED_USER_ACCOUNT_DELETED = "loginFailedAccountDeleted";
/*      */   public static final String AUTOMATED_LOG_OUT_USER_IS = "automatedLogOutUserIs";
/*      */   public static final String LOG_OUT_USER_IS = "logOutUserIs";
/*      */   public static final String LOG_OUT_SWITCH_USER_SAME = "logOutUserSwitchUserSame";
/*      */   public static final String LOG_OUT_USER_ALREADY = "userAlreadyLoggedOut";
/*      */   public static final String DELETE_ACCOUNT = "deleteAccountWasCalled";
/*      */   public static final String TIMER_NOT_RESET_ON_USER_INTERACTION = "timerNotResetOnUserInteraction";
/*      */   public static final String TIMER_NOT_RESET_AFTER_LOGIN = "timerNotSetAfterLogin";
/*      */   public static final String FAILED_JAVA_ARCHITECTURE_FOR_XML_BINDING_CONTEXT = "failedJavaArchitectureForXmlBindingContext";
/*      */   public static final String SUCCESSFULLY_LOG_LEVEL_CONFIG = "successfullyLogLevelConfig";
/*      */   public static final String SUCCESSFULLY_WROTE_LOG_FILES = "successfullyWroteLogFiles";
/*      */   public static final String SUCCESSFULLY_WROTE_LOG_FILE = "successfullyWroteLogFile";
/*      */   public static final String FAILED_WRITE_LOGS_TO_ZIP_AND_OUTPUT_STREAM = "failedWriteLogsToZidAndOutputStream";
/*      */   public static final String FAILED_SET_CONFIGURED_LOG_LEVEL = "failedSetConfiguredLogLevel";
/*      */   public static final String CANNOT_CLOSE_STREAM_ON_CREATE_CERTIFICATE = "cannotCloseStreamOnCreateCertificate";
/*      */   public static final String FAILED_WRITE_LOGS_TO_OUTPUT_STREAM = "failedLogsToOutputStream";
/*      */   public static final String FAILED_TO_CLOSE_OUTPUT_STREAM_AFTER_WRITING_LOGS = "failedToCloseOutputStreamAfterWritingLogs";
/*      */   public static final String FAILED_TO_CLOSE_OUTPUT_STREAM_AFTER_WRITE_LOG_ARCHIVES_TO_ZIP = "failedToCloseOutputStreamAfterWriteLogArchivesToZip";
/*      */   public static final String FAILED_TO_CLOSE_OUTPUT_STREAM_AFTER_WRITE_SERVICE_LOG_FOLDER_TO_ZIP = "failedToCloseOutputStreamAfterWriteServiceLogFolderToZip";
/*      */   public static final String FAILED_TO_CLOSE_OUTPUT_STREAM_AFTER_WRITE_LOG_FILE_TO_ZIP = "failedToCloseOutputStreamAfterWriteLogFileToZip";
/*      */   public static final String FAILED_ON_WRITING_INSTALL_DETAIL_AT_JSON_GENERATING = "failedWriteInstallationDetailsOnJsonGeneratingProcess";
/*      */   public static final String FAILED_ON_WRITING_INSTALL_DETAIL_AT_JSON_MAPPING = "failedWriteInstallationDetailsOnJsonMappingProcess";
/*      */   public static final String FAILED_ON_WRITING_INSTALL_DETAIL_AT_OUTPUT = "failedWriteInstallationDetailsOnOutput";
/*      */   public static final String FAILED_ARCHIVE_LOGS_TO_ZIP = "failedArchiveLogsToZip";
/*      */   public static final String FAILED_ARCHIVE_LOGS_TO_CUSTOM_PATH = "failedArchiveLogsToACustomPath";
/*      */   public static final String IMPORT_LINK_FAILED = "importLinkCertificateFailed";
/*      */   public static final String FAILED_DOWNLOAD_LOG_FILES = "failedDownloadLogFiles";
/*      */   public static final String INVALID_TARGET_SUBJECT_KEY_IDENTIFIER = "invalidTargetSubjectKeyIdentifier";
/*      */   public static final String PARENT_BACKEND_NOT_FOUND = "backendNotFound";
/*      */   public static final String INVALID_VALIDATE_CERTS_CONFIG = "invalidValidateCertsConfig";
/*      */   public static final String INVALID_DELETE_EXPIRED_CERTS_CONFIG = "invalidDeleteExpiredCertsConfig";
/*      */   public static final String INVALID_DELETE_CERTS_SCHEDULE_CONFIG = "invalidDeleteCertsScheduleConfig";
/*      */   public static final String INVALID_CERT_SELECTION_CONFIG = "invalidCertSelectionConfig";
/*      */   public static final String INVALID_PROD_QUALIFIER_CONFIG = "invalidProdQualifierConfig";
/*      */   public static final String INVALID_USER_ROLE_CONFIG = "invalidUserRoleConfig";
/*      */   public static final String INVALID_SECRET_CONFIG = "invalidSecretConfig";
/*      */   public static final String INVALID_MAX_AGE_LOG_CONFIG = "invalidDMaxAgeLogConfig";
/*      */   public static final String INVALID_MAX_SIZE_LOG_CONFIG = "invalidMaxSizeLogConfig";
/*      */   public static final String INVALID_DETAILS_PANE_STATE_CONFIG = "invalidDetailsPaneStateConfig";
/*      */   public static final String INVALID_LOGOFF_NO_ACTION_CONFIG = "invalidLogOffNoActionConfig";
/*      */   public static final String INVALID_LOG_LEVEL_CONFIG = "invalidLogLevelConfig";
/*      */   public static final String INVALID_THRESH_HOLDS_CONFIG = "invalidThresholdsConfig";
/*      */   public static final String INVALID_SSL_VALIDATION_CONFIG = "invalidSSLValidationConfig";
/*      */   public static final String INVALID_SSL_TRUST_STORE_CONFIG = "invalidSSLTrustStoreConfig";
/*      */   public static final String INVALID_EXCLUDED_USER_ROLES_FOR_CENTRAL_AUTHENTICATION_CONFIG = "invalidExcludedUserRolesForAuthenticationConfig";
/*      */   public static final String INVALID_PERSISTENCE_AUDIT_ENTRIES_VALIDATION_CONFIG = "invalidPersistenceAuditEntriesValidationConfig";
/*      */   public static final String INVALID_AUTO_CERT_UPDATE_CONFIG = "invalidAutoCertUpdateConfig";
/*      */   public static final String INVALID_PAGINATION_MAX_ROWS_PER_PAGE = "invalidPaginationMaxRowsPerPage";
/*      */   public static final String INVALID_PAGINATION_MAX_ROWS_PER_PAGE_CHECK_MIN = "invalidPaginationMaxRowsPerPageCheckMin";
/*      */   public static final String INVALID_PAGINATION_MAX_ROWS_PER_PAGE_CHECK_MAX = "invalidPaginationMaxRowsPerPageCheckMax";
/*      */   public static final String INVALID_ECU_VIN_RESTRICTION_CONFIG = "invalidEcuVinRestrictionConfig";
/*      */   public static final String INVALID_VALIDITY_CHECK_TIME_TOLERANCE_CONFIG = "invalidValidityCheckTimeToleranceConfig";
/*      */   public static final String PORT_SET_IN_USER_PREFERENCES = "portSetInUserPreferences";
/*      */   public static final String PORT_COULD_NOT_BE_SET_IN_USER_PREFERENCES = "portCouldNotBeSetInUserPreferences";
/*      */   public static final String PORT_SET_IN_SYSTEM_PREFERENCES = "portSetInSystemPreferences";
/*      */   public static final String PORT_COULD_NOT_BE_SET_IN_SYSTEM_PREFERENCES_NODE_NOTAVAILABLE = "portCouldNotBeSetInSystemPreferencesNodeNotAvailable";
/*      */   public static final String PORT_COULD_NOT_BE_SET_IN_SYSTEM_PREFERENCES = "portCouldNotBeSetInSystemPreferences";
/*      */   public static final String DELETE_PORT_FROM_USER_PREFERENCES = "deletePortFromUserPreferences";
/*      */   public static final String DELETE_PORT_FROM_SYSTEM_PREFERENCES = "deletePortFromSystemPreferences";
/*      */   public static final String PORT_COULD_NOT_BE_WRITTEN = "portCouldNotBeWritten";
/*      */   public static final String SERVER_CONFIGURED_NOT_TO_WRITE_PORT = "serverConfiguredNotToWritePort";
/*      */   public static final String SERVER_CONFIGURED_NOT_TO_DELETE_PORT = "serverConfiguredNotToDeletePort";
/*      */   public static final String INVALID_LOGBACK_FILE = "invalidLogbackFile";
/*      */   public static final String INVALID_LOGBACK_ARCHIVE_PATTERN = "invalidLogbackArchivePattern";
/*      */   public static final String INTEGRITY_CHECK_REPORT_ERROR = "integrityCheckReportError";
/*      */   public static final String UNAUTHORIZED_OPERATION_FOR_REGISTERED_USER = "unAuthorizedOperationForRegisteredUser";
/*      */   public static final String SYSTEMS_PUBLIC_KEY_CANNOT_BE_RETRIVED_FOR_REGISTERED_USER = "publicKey401ForRegisterUser";
/*      */   public static final String NO_DIAGS_FOUND_SIGMODUL = "noDiagsFoundInSigModul";
/*      */   public static final String ENCRYPTED_DIAGS = "encryptedDiags";
/*      */   public static final String NO_ECU_FOUND_SIGMODUL = "noECUsFoundInSigModul";
/*      */   public static final String NO_LINK_CERTS_FOUND_SIGMODUL = "noLinkCertsFoundSigModul";
/*      */   public static final String NO_NON_VSM_IDENTIFIERS_SIGMODUL = "noNonVsmIdentifiersSigModul";
/*      */   public static final String DEFAULT_USER_NOT_LOGGED_IN = "defaultUserNotLoggedIn";
/*      */   public static final String DECRYPTION_PKCS_PACKAGE_FAILED = "decryptionOfPKCSPackageFailed";
/*      */   public static final String EXTRACTION_PKCS_PACKAGE_FAILED = "extractionOfPKCSPackageFailed";
/*      */   public static final String PRODUCTION_USER_KEY_PAIR_NOT_FOUND = "productionUserKeyPairNotFound";
/*      */   public static final String PKCS_IMPORT_RESULT = "pkcsImportResult";
/*      */   public static final String PKCS_DECODE_FAILED = "pkcsDecodeFailed";
/*      */   public static final String BACKEND_SUBJECT_KEY_IDENTIFIER_DECODING_FAILED = "backendSerialNumberDecryptionFailed";
/*      */   public static final String INVALID_INPUT_FOR_UUID = "invalidInputForUUID";
/*      */   public static final String INVALID_INPUT_FOR_SERIAL_NUMBER = "invalidInputForSerialNumber";
/*      */   public static final String INVALID_INPUT_FOR_AUTHORITY_KEY_IDENTIFIER = "invalidInputForAuthorityKeyIdentifier";
/*      */   public static final String INVALID_INPUT_FOR_SUBJECT_KEY_IDENTIFIER = "invalidInputForSubjectKeyIdentifier";
/*      */   public static final String INVALID_INPUT_FOR_TARGET_VIN = "invalidInputForTargetVIN";
/*      */   public static final String NULL_OR_EMPTY_TARGET_VIN = "nullOrEmptyTargetVIN";
/*      */   public static final String INVALID_INPUT_FOR_TARGET_ECU = "invalidInputForTargetECU";
/*      */   public static final String INVALID_INPUT_FOR_NONCE = "invalidInputForNonce";
/*      */   public static final String INVALID_INPUT_FOR_SUBJECT = "invalidInputForSubject";
/*      */   public static final String INVALID_INPUT_FOR_SPECIAL_ECU = "invalidInputForSpecialECU";
/*      */   public static final String INVALID_INPUT_FOR_UNIQUE_ECU_ID = "invalidInputForUniqueECUID";
/*      */   public static final String INVALID_INPUT_EMPTY_LIST = "invalidInputEmptyList";
/*      */   public static final String INVALID_INPUT_NOT_BASE64_ENCODED = "invalidInputNotBase64Encoded";
/*      */   public static final String INVALID_NUMBER_OF_FAILED_LOGIN_ATTEMPTS = "invalidNumberOfFailedLoginAttempts";
/*      */   public static final String INVALID_INPUT_FOR_CODING_DATA = "invalidInputCodingData";
/*      */   public static final String CERTIFICATE_ALREADY_ROLLED_BACK = "certificateAlreadyRolledBack";
/*      */   public static final String CERTIFICATE_CANNOT_ENABLE_ROLLBACK = "certificateCannotEnableRollback";
/*      */   public static final String CERTIFICATE_ROLLBACK_DELETE_OLD_CERTIFICATES = "certificateRollbackDeleteOldCertificates";
/*      */   public static final String CERTIFICATE_ROLLBACK_NOT_ALLOWED_FOR_TYPE = "certificateRollbackNotAllowedForType";
/*      */   public static final String CERTIFICATE_ROLLBACK_SUCCESSFUL = "certificateRollbackSuccessful";
/*      */   public static final String CERTIFICATE_IMPORT_NOT_POSSIBLE_ROLLBACK_ACTIVE = "certificateImportNotPossibleRollbackActive";
/*      */   public static final String CERTIFICATE_ROLLBACK_NOT_ACTIVE = "certificateRollbackNotActive";
/*      */   public static final String CERTIFICATE_ROLLBACK_DISABLE_SUCCESSFUL = "certificateRollbackDisableSuccessful";
/*      */   public static final String PROXY_SET = "proxySet";
/*      */   public static final String PROXY_GET = "proxyGet";
/*      */   public static final String ONLY_DEFAULT_USER_SET_PROXY = "setProxyOnlyForDefaultUser";
/*      */   public static final String PROXY_HTTPS = "proxyHttpsProtocol";
/*      */   public static final String INVALID_PROXY_TYPE = "invalidProxyType";
/*      */   public static final String MISSING_PROXY_HOST_PROPERTY = "missingProxyHostProperty";
/*      */   public static final String MISSING_PROXY_PORT_PROPERTY = "missingProxyPortProperty";
/*      */   public static final String INVALID_PROXY_PORT_NUMBER = "invalidProxyPortNumber";
/*      */   public static final String INVALID_PROXY_PORT_NUMBER_BOUNDS = "invalidProxyPortNumberBounds";
/*      */   public static final String PROPERTY_NOT_ALLOWED_TO_BE_OVERRIDEN_IN_ZENZEFI_GENERAL = "propertyNotAllowedToBeOverridenInGeneralProperties";
/*      */   public static final String ERROR_READING_ZENZEFI_GENERAL_PROPERTIES = "errorReadingZenZefiGeneralProperties";
/*      */   public static final String HOST_NAME_TAKEN_FROM_INET_ADDR = "hostNameTakenFromInetAddr";
/*      */   public static final String HOST_NAME_TAKEN_FROM_ENV_VAR = "hostNameTakenFromEnvVariables";
/*      */   public static final String HOST_NAME_TAKEN_FROM_NATIVE_CMD = "hostNameTakenFromNativeCommand";
/*      */   public static final String UNABLE_TO_RETRIEVE_HOST_NAME = "unableToRetrieveHostName";
/*      */   public static final String BACKEND_SUBJECT_KEY_IDENTIFIER_MANDATORY = "backendSubjectKeyIdentifierMandatory";
/*      */   public static final String CHALLANGE_BYTE_ARRAY_MANDATORY = "challangeByteArrayMandatory";
/*      */   public static final String UNKNOWN_UPDATE_TYPE = "unknownUpdateType";
/*      */   public static final String UPDATE_PROCESS_START = "updateProcessStart";
/*      */   public static final String UPDATE_PROCESS_END = "updateProcessEnd";
/*      */   public static final String UPDATE_BACKEND_PROCESS_START = "updateBackendProcessStart";
/*      */   public static final String UPDATE_BACKEND_PROCESS_END = "updateBackendProcessEnd";
/*      */   public static final String UPDATE_LOG_ROOT_MISSING = "updateInvalidBackendIdentifiers";
/*      */   public static final String UPDATE_NOT_DOWNLOADED_PREACTIVE_CA = "updateNotDownloadedPreactiveCa";
/*      */   public static final String UPDATE_DOWNLOAD_ROOT_BACKEND_START = "updateDownloadRootBackendStart";
/*      */   public static final String UPDATE_REQUEST_BACKEND_IDENTIFIERS = "updateRequestBackendIdentifiers";
/*      */   public static final String UPDATE_RECEIVE_BACKEND_IDENTIFIERS = "updateReceiveBackendIdentifiers";
/*      */   public static final String UPDATE_RECEIVED_BACKEND_IDENTIFIERS = "updateReceivedBackendIdentifiers";
/*      */   public static final String UPDATE_START_UPDATE_BACKEND_IDENTIFIERS = "updateStartUpdateBackendIdentifiers";
/*      */   public static final String UPDATE_STOP_UPDATE_BACKEND_IDENTIFIERS = "updateStopUpdateBackendIdentifiers";
/*      */   public static final String UPDATE_ECU_BACKEND_IDENTIFIERS_CHANGE = "updateEcuBackendIdentifiersChanged";
/*      */   public static final String UPDATE_ECU_BACKEND_IDENTIFIERS_EQUALS = "updateEcuBackendIdentifiersEquals";
/*      */   public static final String UPDATE_LINK_BACKEND_IDENTIFIERS_CHANGE = "updateLinkBackendIdentifiersChanged";
/*      */   public static final String UPDATE_LINK_BACKEND_IDENTIFIERS_EQUALS = "updateLinkBackendIdentifiersEquals";
/*      */   public static final String UPDATE_PROCESS_CONTINUE_WITHOUT_PERMISSIONS = "updateProcessContinueWithoutPermissions";
/*      */   public static final String ERROR_WHEN_DOWNLOADING_PERMISSIONS = "errorWhenDownloadingPermissions";
/*      */   public static final String UPDATE_REQUESTING_BACKEND_CERT_CHAIN = "updateRequestingBackendCertChain";
/*      */   public static final String UPDATE_RECEIVING_BACKEND_CERT_CHAIN = "updateReceivingBackendCertChain";
/*      */   public static final String UPDATE_RECEIVED_BACKEND_CERT_CHAIN = "updateReceivedBackendCertChain";
/*      */   public static final String UPDATE_DOWNLOAD_ROOT_BACKEND_STOP = "updateDownloadRootBackendStop";
/*      */   public static final String UPDATE_DOWNLOAD_USER_PERMISSIONS_START = "updateStartDownloadUserPermissions";
/*      */   public static final String UPDATE_REQUESTING_USER_PERMISSIONS = "updateRequestingUserPermissions";
/*      */   public static final String UPDATE_RECEIVING_USER_PERMISSIONS = "updateReceivingUserPermissions";
/*      */   public static final String UPDATE_RECEIVED_USER_PERMISSIONS = "updateReceivedUserPermissions";
/*      */   public static final String UPDATE_STOP_DOWNLOAD_USER_PERMISSIONS = "updateStopDownloadUserPermissions";
/*      */   public static final String UPDATE_START_CREATE_CSR_PROCESS = "updateStartCreateCSRsProcess";
/*      */   public static final String UPDATE_START_CREATE_CSR_PROCESS_WITH_BACKEND_TYPE = "updateStartCreateCSRsProcessWithBackendType";
/*      */   public static final String UPDATE_STOP_CREATE_CSR_PROCESS = "updateStopCreateCSRsProcess";
/*      */   public static final String UPDATE_START_DOWNLOAD_CERTIFICATES_PROCESS = "updateStartDownloadCertificates";
/*      */   public static final String UPDATE_STOP_DOWNLOAD_CERTIFICATES_PROCESS = "updateStopDownloadCertificates";
/*      */   public static final String UPDATE_START_CREATE_ENHANCED_CSR_PROCESS = "updateStartCreateEnhancedCSR";
/*      */   public static final String UPDATE_STOP_CREATE_ENHANCED_CSR_PROCESS = "updateStopCreateEnhancedCSR";
/*      */   public static final String UPDATE_START_DOWNLOAD_ENHANCED_CSR_PROCESS = "updateStartDownloadEnhancedCSR";
/*      */   public static final String UPDATE_START_DOWNLOAD_LINK_CERTIFICATES = "updateStartDownloadLinkCertificates";
/*      */   public static final String UPDATE_STOP_DOWNLOAD_LINK_CERTIFICATES = "updateStopDownloadLinkCertificates";
/*      */   public static final String NO_LINK_CERTS_FOUND_ZENZEFI = "noLinkCertsFoundZenZefi";
/*      */   public static final String UPDATE_RECEIVED_LINK_CERTIFICATES = "updateReceivedLinkCertificates";
/*      */   public static final String UPDATE_REQUESTING_ENHANCED_CSR_PROCESS = "updateRequestingEnhancedCSR";
/*      */   public static final String UPDATE_REQUEST_ENHANCED_CSR_PROCESS = "updateRequestEnhancedCSR";
/*      */   public static final String UPDATE_RECEIVING_ENHANCED_CERT_PROCESS = "updateReceivingEnhancedRightsCert";
/*      */   public static final String UPDATE_RECEIVED_ENHANCED_CERT_PROCESS = "updateReceivedEnhancedRightsCert";
/*      */   public static final String UPDATE_STOP_DOWNLOAD_ENHANCED_CERT_PROCESS = "updateStopDownloadEnhancedRightsCert";
/*      */   public static final String UPDATE_REQUESTING_DIAGNOSTIC_CERT_PROCESS = "updateRequestingDiagnosticCert";
/*      */   public static final String UPDATE_REQUESTING_DIAGNOSTIC_FOR_CSR = "updateRequestDiagForCSR";
/*      */   public static final String UPDATE_RECEIVE_DIAGNOSTIC_CERT = "updateReceiveDiagnosticCert";
/*      */   public static final String UPDATE_RECEIVED_DIAGNOSTIC_CERT = "updateReceivedDiagnosticCert";
/*      */   public static final String UPDATE_REQUESTING_CERT_PROCESS = "updateRequestingCert";
/*      */   public static final String UPDATE_REQUESTING_CERT_FOR_CSR = "updateRequestCertForCSR";
/*      */   public static final String UPDATE_RECEIVE_CERT = "updateReceiveCert";
/*      */   public static final String UPDATE_RECEIVED_CERT = "updateReceivedCert";
/*      */   public static final String UPDATE_REQUESTING_REMAINING_CERT = "updateRequestingRemainingCert";
/*      */   public static final String UPDATE_REQUESTING_DIAGS_FOR_CSRS = "updateRequestRemainingCertsForCSR";
/*      */   public static final String UPDATE_RECEIVING_REMAINING_CERT = "updateReceivingRemainingCert";
/*      */   public static final String UPDATE_RECEIVED_REMAINING_CERT = "updateReceivedRemainingCert";
/*      */   public static final String UPDATE_DELETE_CSR_UNDER_BACKEND = "updateDeleteCSRUnderBackendWithSKIAndSN";
/*      */   public static final String UPDATE_START_DELETING_CERTS_WHICH_ARE_NOT_REPLACED = "updateStartDeletingCertsWhichAreNotReplaced";
/*      */   public static final String UPDATE_END_DELETING_CERTS_WHICH_ARE_NOT_REPLACED = "updateEndDeletingCertsWhichAreNotReplaced";
/*      */   public static final String UPDATE_DELETING_CERT_FOR_WHICH_USER_DOES_NOT_HAVE_PERMISSION = "updateDeletingCertForWhichUserDoesNotHavePermission";
/*      */   public static final String UPDATE_DELETED_CERT_WITH_AKI_SN = "updateDeleteCertWithAKIAndSN";
/*      */   public static final String UPDATE_DELETED_CERT_MISMATCHED_ENROLLMENT_ID = "updateDeletedCertMismatchedEnrollmentId";
/*      */   public static final String UPDATE_DID_NOT_CREATE_CSR_CERT_STILL_VALID = "updateDidNotCreateCSRCertStillValid";
/*      */   public static final String UPDATE_CREATE_CSR_AKI_PKI_ROLE = "updateCreatedCSRWithAKIAndPKIRole";
/*      */   public static final String CREATE_CSR_AKI_PKI_ROLE_AD_HOC_MANNER = "createdCSRWithAKIAndPKIRoleAdHocManner";
/*      */   public static final String UPDATE_CREATE_CSR_AKI_PKI_ROLE_USER_ROLE = "updateCreatedCSRWithAKIPKIRoleUserRole";
/*      */   public static final String UPDATE_OPERATION_NOT_ALLOWED_WHILE_PROCESS_ACTIVE = "updateOperationNotAllowed";
/*      */   public static final String UPDATE_PROCESS_CANNOT_UPDATE = "updateProcessCannotUpdate";
/*      */   public static final String NO_AUTHORIZATION_TOKEN_FOUND = "noAuthorizationTokenFound";
/*      */   public static final String PERMISSIONS_ERROR = "permissionsError";
/*      */   public static final String NO_PERMISSIONS_ERROR = "noPermissionsError";
/*      */   public static final String NO_PERMISSIONS_ERROR_WITH_LINK = "noPermissionsErrorWithLink";
/*      */   public static final String PERMISSION_DOES_NOT_PROVIDE_ENROLLMENT_ID = "permissionDoesNotProvideEnrollmentId";
/*      */   public static final String IDENTIFIERS_ERROR = "identifiersError";
/*      */   public static final String CERTIFICATES_BACKENDS_DOWNLOAD_ERROR = "certificatesDownloadBackendsError";
/*      */   public static final String COLLECTING_TIME_AND_SECOCIS_CSRS_NOT_CREATED_BASED_ON_PERMISSIONS = "collectingTimeAndSecocisCSRsNotCreatedBasedOnPermissions";
/*      */   public static final String COLLECTING_TIME_AND_SECOCIS_CSRS_NOT_CREATED_BASED_ON_PERMISSIONS_STEP = "collectingTimeAndSecocisCSRsNotCreatedBasedOnPermissionsStep";
/*      */   public static final String DOWNLOAD_TIME_CERTIFICATE_USING_CSRS_NOT_CREATED_BASED_ON_PERMISSIONS = "dowloadingTimeCertificateUsingCSRsNotCreateBasedOnPermissions";
/*      */   public static final String DOWNLOAD_TIME_CERTIFICATE_USING_CSRS_NOT_CREATED_BASED_ON_PERMISSIONS_STEP_DETAILS = "dowloadingTimeCertificateUsingCSRsNotCreateBasedOnPermissionsStep";
/*      */   public static final String DOWNLOAD_SECOCIS_CERTIFICATE_USING_CSRS_NOT_CREATED_BASED_ON_PERMISSIONS = "dowloadingSecocisCertificateUsingCSRsNotCreateBasedOnPermissions";
/*      */   public static final String DOWNLOAD_SECOCIS_CERTIFICATE_USING_CSRS_NOT_CREATED_BASED_ON_PERMISSIONS_STEP_DETAILS = "dowloadingSecocisCertificateUsingCSRsNotCreateBasedOnPermissionsStep";
/*      */   public static final String COULD_NOT_IMPORT_CERTIFICATE_DOWNLOADED_FROM_PKI_REASON = "couldNotImportCertificateDownloadedFromPKI";
/*      */   public static final String PERMISSION_VALIDATION_FAILED = "permissionValidationFailed";
/*      */   public static final String INVALID_RENEWAL = "invalidRenewal";
/*      */   public static final String INVALID_DOWNLOAD_PERMISSIONS = "invalidDownloadPermission";
/*      */   public static final String WARNING_REMOVED_DURING_IMPORT = "warningRemovedDuringImport";
/*      */   public static final String PARENT_DOES_NOT_PASS_EXTENDED_VALIDATION = "parentDoesNotPassExtendedValidation";
/*      */   public static final String TRANSITION_FROM_OFFLINE_TO_ONLINE_NOT_POSSIBLE = "transitionNotPossible";
/*      */   public static final String TOKEN_WILL_BE_REVOKED_TRANSITION_NOT_POSSIBLE = "tokenWillBeRevokedTransitionNotPossible";
/*      */   public static final String OPERATION_NOT_ALLOWED_IMPORT_IS_RUNNING = "operationNotAllowedImportIsRunning";
/*      */   public static final String UPDATE_CREATED_CSR_WITH_DESCRIPTION = "updateCreatedCSR";
/*      */   public static final String UPDATE_START_DELETING_CSR_FOR_ROLLED_BACK_CERTIFICATE = "updateStartDeleteCSRForRolledBackCert";
/*      */   public static final String UPDATE_END_DELETING_CSR_FOR_ROLLED_BACK_CERTIFICATE = "updateEndDeleteCSRForRolledBackCert";
/*      */   public static final String UPDATE_START_DELETING_CSR_UNDER_BACKEND = "updateStartDeleteCSRUnderBackend";
/*      */   public static final String UPDATE_END_DELETING_CSR_UNDER_BACKEND = "updateEndDeleteCSRUnderBackend";
/*      */   public static final String UPDATE_END_DELETING_CSR_UNDER_BACKEND_NOTHING_FOUND = "updateEndDeleteCSRUnderBackendNothingFound";
/*      */   public static final String UPDATE_DELETE_MORE_THAN_2_IDENTICAL_CERTIFICATES = "updateDeleteMoreThanTwoIdenticalCerts";
/*      */   public static final String IMPORT_CERTIFICATE_SUBJECT_DOES_NOT_MATCH_CONFIG_SUBJ = "certSubjDoesNotMatchConfigSubj";
/*      */   public static final String UPDATE_CERTIFICATE_INVALID_TYPE = "updateCertInvalidType";
/*      */   public static final String LOGOUT_NO_USER_LOGGED_IN = "logoutNoUserLoggedIn";
/*      */   public static final String LOGOUT_SUCCESSFUL = "logoutSuccessful";
/*      */   public static final String LOGIN_STORE_OPEN = "loginStoreOpened";
/*      */   public static final String LOGIN_STORE_CREATED = "loginUserStoreCreated";
/*      */   public static final String LOGIN_SUCCESSFUL = "loginSuccessful";
/*      */   public static final String LOGIN_USER_DOES_NOT_EXIST = "loginUserDoesNotExist";
/*      */   public static final String LOGIN_WRONG_PASS = "loginWrongPassword";
/*      */   public static final String LOGIN_ERROR_OCCURRED = "loginErrorOccurred";
/*      */   public static final String LOGIN_CONFIGURATION_INVALID = "loginConfigurationInvalid";
/*      */   public static final String USER_ALREADY_LOGGED = "userAlreadyLogged";
/*      */   public static final String CONFIGURATION_NOT_FOUND = "configurationNotFound";
/*      */   public static final String THE_PUBLIC_KEY_IS_INVALID = "thePublicKeyIsInvalid";
/*      */   public static final String ID_WAS_NOT_PROVIDED = "idWasNotProvided";
/*      */   public static final String PROVIDE_ONE_ID = "provideOneID";
/*      */   public static final String MODE_SINGLE_CANNOT_WORK_WITH_TWO_IDS = "modeSingleCannotWorkWithTwoIDs";
/*      */   public static final String FRIENDLY_NAME_IS_INCONSISTENT = "friendlyNameIsInconsistent";
/*      */   public static final String FRIENDLY_NAME_FAILS_REGEX_CHECK = "friendlyNameFailsRegexCheck";
/*      */   public static final String FRIENDLY_NAME_MATCHES_SUBJECT_DN = "friendlyNameMatchesSubjectDN";
/*      */   public static final String GET_SERVER_VERSION = "serverVersionCheck";
/*      */   public static final String GET_API_VERSION = "apiVersionCheck";
/*      */   public static final String GET_PUBLIC_KEY = "returnPublicKey";
/*      */   public static final String GET_CHECK_SYSTEM_INTEGRITY = "checkSystemIntegrity";
/*      */   public static final String GET_CHECK_SYSTEM_INTEGRITY_LOG = "checkSystemIntegrityLog";
/*      */   public static final String GET_CERTIFICATE_LIST = "returnCertificateList";
/*      */   public static final String GENERATE_TESTER_NONCE = "generateTesterNonce";
/*      */   public static final String GET_CERTIFICATE = "getCertificate";
/*      */   public static final String MANDATORY_PARAM_MISSING = "mandatoryParamMissing";
/*      */   public static final String HTTP_METHOD_NOT_SUPPORTED = "httpMethodNotSupported";
/*      */   public static final String GENERIC_EXCEPTION_OCCURRED = "genericExceptionOccurred";
/*      */   public static final String TESTER_NONCE_GENERATED = "testerNonceGenerated";
/*      */   public static final String SYSTEM_PUBLIC_KEY_GENERATED = "systemPublicKeyGenerated";
/*      */   public static final String GET_CURRENT_USER_DETAILS = "getCurrentUserDetails";
/*      */   public static final String ENTIRE_MAPPING_RETURNED = "entireMappingReturned";
/*      */   public static final String ENTIRE_MAPPING_NOTHING_FOUND = "entireMappingNothingFound";
/*      */   public static final String ZK_NO_MAPPING_FOUND = "zkNumberMappingFound";
/*      */   public static final String GET_ROOT_AND_BACKEND_CERTS_START = "getRootAndBackendCertsStart";
/*      */   public static final String GET_ROOT_AND_BACKEND_CERTS_RESULT = "getRootAndBackendCertsResult";
/*      */   public static final String BACKEND_DOES_NOT_HAVE_ZK_NO = "backendWithoutZkNo";
/*      */   public static final String JSON_INPUT_PARSE_ERROR = "jsonInputParseError";
/*      */   public static final String SKIP_NON_VSM_CERTIFICATE_DOWNLOAD_FEATURE_DISABLED = "skipNonVsmFeatureDisabled";
/*      */   public static final String SKIP_LINK_CERTIFICATE_DOWNLOAD_FEATURE_DISABLED = "skipLinkFeatureDisabled";
/*      */   public static final String START_NON_VSM_CERTIFICATE_DOWNLOAD = "startNonVsmCertificatesDownload";
/*      */   public static final String NO_NON_VSM_IDENTIFIERS_FOUND_ZENZEFI = "noNonVsmIdentifiersZenzefi";
/*      */   public static final String STOP_NON_VSM_CERTIFICATE_DOWNLOAD = "stopNonVsmCertificatesDownload";
/*      */   public static final String CLEAN_UP_DELETE_MORE_THAN_2_IDENTICAL_CERTIFICATES = "cleanUpDeleteMoreThanTwoIdenticalCerts";
/*      */   public static final String NON_VSM_CERTIFICATE_DELETED = "deletedNonVsmCertificateZenzefi";
/*      */   public static final String NO_NON_VSM_CERTIFICATE_DELETED = "noDeletedNonVsmCertificateZenzefi";
/*      */   public static final String NO_VSM_IDENTIFIERS_RETRIEVED = "noVsmIdentifiersRetrieved";
/*      */   public static final String LIST_OF_NON_VSM_IDENTIFIERS_FOR_BACKEND = "vsmIdentifiersRetrievedForBackend";
/*      */   public static final String UPDATING_NON_VSM_CERTIFCIATES = "updatingNonVsmCertificatesForBackend";
/*      */   public static final String CONTENT_OF_NON_VSM_CERTIFICATES_FOR_BACKEND = "contentListOfNonVsmCertificates";
/*      */   public static final String RETRIEVE_PAGINATED_RESULT_BAD_REQUEST = "badRequestOnPaginatedResults";
/*      */   public static final String VSM_CERT_AUTO_DELETE_SKIPPED = "vsmCertAutoDeleteSkipped";
/*      */   public static final String RESET_MARKERS_ON_ECU_LINK = "resetMarkersOnEcuLink";
/*      */   public static final String NO_BACKEND_CERTIFICATES_FOUND = "noBackendCertificatesFound";
/*      */   public static final String NO_ROOT_CERTIFICATES_FOUND = "noRootCertificatesFound";
/*      */   public static final String MARKERS_RESET_SUCCESSFULLY = "markersResetSuccessfully";
/*      */   public static final String INVLAID_USER_SCOPE_FOR_PKI_SCOPE_CHANGE = "invalidUserScopeForPkiScopeChange";
/*      */   private MessageSource messageSource;
/* 1641 */   private String locale = "en";
/*      */   
/*      */   @Autowired
/*      */   public CEBASI18N(MessageSource messageSource) {
/* 1645 */     this.messageSource = messageSource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getMessage(String id, String[] args) {
/* 1655 */     return this.messageSource.getMessage(id, (Object[])args, new Locale(this.locale));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getEnglishMessage(String id, String[] args) {
/* 1665 */     return this.messageSource.getMessage(id, (Object[])args, new Locale("en"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getMessage(String id) {
/* 1674 */     return this.messageSource.getMessage(id, null, new Locale(this.locale));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getEnglishMessage(String id) {
/* 1683 */     return this.messageSource.getMessage(id, null, new Locale("en"));
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
/*      */ 
/*      */ 
/*      */   
/*      */   public String getMessage(String id, String[] params, String customLocale) {
/* 1698 */     return this.messageSource.getMessage(id, (Object[])params, new Locale(customLocale));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLocale(String locale) {
/* 1707 */     this.locale = locale;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getLocale() {
/* 1716 */     return this.locale;
/*      */   }
/*      */ }


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\control\CEBASI18N.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */