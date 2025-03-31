package com.daimler.cebas.common.swagger;

public final class SwaggerApiDescription {
  public static final String CORRELATION_ID = "Business id which identifies the request and business case.";
  
  public static final String CLIENT_ID = "Client id which identifies the client that makes a request to the server.";
  
  public static final String CHAIN_IDENTIFIER = "Unique identifier for looking up a certificate chain Root-Backend-Under Backend. It can be backend SKI or a ZK Number";
  
  public static final String ECU_CERT = "Ecu certificate as base64";
  
  public static final String LOCALE = "The language for the operation. E.g. en, de";
  
  public static final String ENDPOINT_DELETE_CERTIFICATES = "Deletes one or more certificates from current user-specific certificate store. The deletion is made by certificate ids, and each certificate id must be a valid UUID. The ids are comma separated.";
  
  public static final String PARAM_CERTIFICATE_IDS = "The ids of the certificates, comma separated. Must be valid UUIDs.";
  
  public static final String PARAM_ZKNO_BSKI = "The ZK Number or the BSKI. Type is determined from the value, therefore must either be a valid BSKI or a valid ZK number.";
  
  public static final String PARAM_CSR_ID = "The id of the CSR. Must be valid UUID.";
  
  public static final String PARAM_CERTIFICATE_TYPE = "The certificate type. E.g. BACKEND_CA_CERTIFICATE, DIAGNOSTIC_AUTHENTICATION_CERTIFICATE, etc.";
  
  public static final String PARAM_USER_ROLE = "The role of the user: 1=Supplier, 2=Development ENHANCED, 3=Production, 4=After-Sales ENHANCED, 5=After-Sales STANDARD, 6=After-Sales BASIC, 7=Internal Diagnostic Test Tool, 8=ePTI Test Tool";
  
  public static final String PARAM_TARGET_ECU = "The target ECU. Maximum size of the field is 30 bytes.";
  
  public static final String PARAM_TARGET_VIN = "The target VIN. The size of the field is 17 characters.";
  
  public static final String PARAM_NONCE = "The nonce. Must be Base64 encoded and have the length of 32 bytes.";
  
  public static final String PARAM_SERVICES = "The services.";
  
  public static final String PARAM_SERVICES_CSV = "The services - comma separated. Values in HEX (eg ff, ff01, ff010203).";
  
  public static final String PARAM_UNIQUE_ECU_ID = "Unique ECU ID. Maximum length is 30 bytes.";
  
  public static final String PARAM_UNIQUE_ECU_ID_NOT_BASE64_ENCODED = "Unique ECU ID, not base64 encoded. Maximum length is 30 bytes.";
  
  public static final String PARAM_PARENT_CERTIFICATE_ID = "The parent certificate ID. Must be an UUID.";
  
  public static final String PARAM_VALID_TO = "Valid to date. The date format is yyyy-MM-dd.";
  
  public static final String PARAM_VALID_FROM = "Valid from date. The date format is yyyy-MM-dd.";
  
  public static final String PARAM_TARGET_SUBJECT_KEY_IDENTIFIER = "The target subject key identifier.";
  
  public static final String PARAM_AUTHORITY_KEY_IDENTIFIER = "The authority key identifier. It is sent as Base64 encoded bytes, and the length must be 20 bytes.";
  
  public static final String PARAM_AUTHORITY_KEY_IDENTIFIER_WITH_ZK_NO = "The authority key identifier. It is sent as Base64 encoded bytes, and the length must be 20 bytes. ZK Number is also supported (10 characters, NOT Base64 encoded).";
  
  public static final String PARAM_SUBJECT = "The subject.";
  
  public static final String ENDPOINT_IMPORT_FILES = "Not working from Swagger UI and Swagger Generated API. Imports certificates, .crt or .p12 files into current logged in user-specific certificate store. Operation used in the ZenZefi UI for importing.";
  
  public static final String ENDPOINT_EXPORT_PUBLIC_KEY = "Retrieves a file with the certificate's public key.";
  
  public static final String PARAM_CERTIFICATE_ID = "The id of the certificate. Must be valid UUID.";
  
  public static final String ENDPOINT_ZKNO_MAPPING = "Returns for a given ZK number the corresponding BSKI, and vice versa";
  
  public static final String PARAM_FILE_NAME_DOWNLOAD = "Optional file name to be used in the download.";
  
  public static final String ENDPOINT_LIST_CERTIFICATES = "List certificates.";
  
  public static final String ENDPOINT_LIST_CERTIFICATES_PAGINATED = "List paginated certificates.";
  
  public static final String PARAM_LIST_ALL_CERTIFICATES = "If should also list CSRs alongside certificates.";
  
  public static final String PARAM_LIST_PAGINATED = "Import paginated results of certificates";
  
  public static final String ENDPOINT_IMPORT_CERTIFICATES_FROM_LOCAL = "Import certificates into current user-specific certificate store. For this endpoint the inputs are the certificate or PKCS12 file path, and password in the case of PKCS12.";
  
  public static final String PARAM_IMPORT_CER_FILE_NAME = "The file name of the certificate to be imported.";
  
  public static final String PARAM_IMPORT_CERT_ENCODED_BYTES = "The Base64 encoded bytes of the certificate to be imported.";
  
  public static final String ENDPOINT_ENCRYPT_PKCS_PACKAGE = "Return encrypted PKCS#12 packages that contain diagnosis certificates. For details about the parameters, please view the request model.";
  
  public static final String ENDPOINT_ENCRYPT_PKCS_PACKAGE_WITH_ECU = "Return encrypted PKCS#12 packages that contain ECU certificates. For details about the parameters, please view the request model.";
  
  public static final String PARAM_ZENZEFI_EXPORTED_PUBLIC_KEY = "The public key which was generated by ZenZefi, in order to encrypt pkcs packages from SigModul.";
  
  public static final String PARAM_VSM_IMPORT_SERIAL_NUMBER = "List of EZS-serialnumbers.";
  
  public static final String ENDPOINT_GET_CERT_BY_AUTH_KEY_IDENTIFIER_SERIAL_NUMBER = "Retrieve certificate by authority key identifier and serial number.";
  
  public static final String ENDPOINT_GET_CERT_BY_AUTH_KEY_IDENTIFIER_SERIAL_NUMBER_ZKNO_SUPPORT = "Retrieve certificate by authority key identifier (or ZK Number) and serial number.";
  
  public static final String ENDPOINT_GET_DIAG_CERT_ROLE_BY_AUTH_KEY_IDENTIFIER_SERIAL_NUMBER = "Retrieve diagnostic certificate role by authority key identifier and serial number.";
  
  public static final String ENDPOINT_GET_DIAG_CERT_ROLE_BY_AUTH_KEY_IDENTIFIER_SERIAL_NUMBER_WITH_ZK_NO = "Retrieve diagnostic certificate role by authority key identifier (or ZK Number) and serial number.";
  
  public static final String PARAM_SERIAL_NUMBER = "The serial number. It is sent as Base64 encoded bytes. The maximum length is 16 bytes.";
  
  public static final String ENDPOINT_ENABLE_ROLLBACK_ZK_NO_SUPPORT = "Enable rollback mode for a certificate. It support ZK Number instead of AKI. For details about the parameters, please view the request model.";
  
  public static final String PARAM_BACKEND_SUBJECT_KEY_IDENTIFIER = "The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes.";
  
  public static final String PARAM_BACKEND_SUBJECT_KEY_IDENTIFIER_WITH_ZK_NO = "The backend subject key identifier. It is sent as Base64 encoded bytes. The length is 20 bytes. ZK Number is also supported (as 10 character string, NOT Base64 encoded).";
  
  public static final String ENDPOINT_DISABLE_ROLLBACK_ZK_NO = "Disable rollback mode for a certificate. It support ZK Number instead of AKI. For details about the parameters, please view the request model.";
  
  public static final String ENDPOINT_GET_CERTS_BY_ID = "Returns a json representation of required certificates by id. The ids list is comma separated, and each id must be a valid UUID.";
  
  public static final String ENDPOINT_GET_CERT_BY_ID = "Returns a json representation of required certificate by id. The id must be a valid UUID.";
  
  public static final String ENDPOINT_REMOVE_CERTS_BY_BODY = "Remove certificates via POST json body instead of url ids. Enables deletion of long lists of ids that are not allowed anymore due to http url length restrictions.";
  
  public static final String ENDPOINT_CREATE_SIGNATURE_REQUEST = "Create a signature request. Endpoint provided only by SIGMODUL_SI_V1.";
  
  public static final String ENDPOINT_ZENZEFI_SIGNATURES = "Sign coding data for all backends with available Variant Coding User certificates.";
  
  public static final String PARAM_CODING_DATA_IDENTIFIER = "Unique identifier for the coding data.";
  
  public static final String PARAM_CODING_DATA = "Base64 representation of the coding data to be signed.";
  
  public static final String ENDPOINT_LIST_CERTIFICATES_CURRENT_USER = "Returns an array of certificates belonging to the current logged in user.";
  
  public static final String ENDPOINT_GET_FILTERED_CERTIFICATES = "Returns an array of filtered certificates belonging to the current logged in user. This endpoint is used in the ZenZefi UI, for filtering the certificates table.";
  
  public static final String PARAM_FILTER_PARAMETERS = "The filtering parameters, which also includes the limit for the query.";
  
  public static final String ENDPOINT_DELETE_CERT_AKI_SN = "Deletes all certificates from current user-specific certificate store, or by authority key identifier and serial number. For details about the parameters, please view the request model.";
  
  public static final String ENDPOINT_DELETE_CERT_AKI_SN_WITH_ZK_NO = "Deletes all certificates from current user-specific certificate store, or by authority key identifier (or ZK Number) and serial number. For details about the parameters, please view the request model.";
  
  public static final String PARAM_DELETE_ALL_CERT = "If set to true, deletes all certificates.";
  
  public static final String PARAM_IMPORT_FILE_PATH = "Path of the file to be imported. The file path has to provided with single slashes as separator, e.g. c:/folder/folder.ext - mind: single backslashes can not be used.";
  
  public static final String PARAM_IMPORT_FILE_PASS_WORD = "The certificate password, if needed.";
  
  public static final String ENDPOINT_GET_DIAG_BASED_ON_CRITERIA = "Get Diagnostic Certificate based on given criteria.";
  
  public static final String ENDPOINT_CHECK_ACTIVE_DIAG_CERT = "Checks whether a call to get diagnosis certificate will return the certificate with the serial number provided as parameter.";
  
  public static final String ENDPOINT_CHECK_OWNERSHIP = "Searches for a certificate with the backend subject key identifier and serial number from the request and calculates the signature for ECUChallenge. For details about the parameters, please view the request model.";
  
  public static final String ENDPOINT_CHECK_OWNERSHIP_WITH_ZK_NO = "Searches for a certificate with the backend subject key identifier (or ZK Number) and serial number from the request and calculates the signature for ECUChallenge. For details about the parameters, please view the request model.";
  
  public static final String PARAM_ECU_CHALLANGE = "ECU challenge, Base64 encoded";
  
  public static final String ENDPOINT_GET_TIME_CERT = "Retrieve a time certificate based on given criteria.";
  
  public static final String ENDPOINT_GET_ENHANCED_RIGHTS_CERT = "Retrieve an enhanced rights certificate based on given criteria.";
  
  public static final String ENDPOINT_GET_SEC_OCIS_CERT = "Retrieve a Sec OCIS certificate based on given criteria. For details about the parameters, please view the request model.";
  
  public static final String PARAM_CERTIFICATE = "Certificate bytes, Base64 encoded.";
  
  public static final String ENDPOINT_GET_VARIANT_CODING_CERT = "Retrieve a variant coding user certificate based on given criteria. For details about the parameters, please view the request model.";
  
  public static final String PARAM_DATA_TO_BE_SIGNED = "Base64 representation of the data to be signed.";
  
  public static final String ENDPOINT_SIGNATURE_CHECK = "Checks message signature, signed by the input certificate. For details about the parameters, please view the request model.";
  
  public static final String PARAM_SIGNATURE_CHECK_MESSAGE = "Base64 representation of the message which needs to be checked.";
  
  public static final String PARAM_SIGNATURE_CHECK_SIGNATURE = "Base64 representation of the signature.";
  
  public static final String ENDPOINT_CERTIFICATE_REPLACEMENT_PACKAGE = "Get certificate replacement package based on given criteria. For details about the parameters, please view the request model.";
  
  public static final String ENDPOINT_CHAIN_REPLACEMENT_PACKAGE = "Get chain replacement package based on given criteria. For details about the parameters, please view the request model.";
  
  public static final String PARAM_REPLACEMENT_TARGET = "Replacement target. E. g.: ECU, BACKEND, ROOT";
  
  public static final String ENDPOINT_CHECK_SYSTEM_INTEGRITY_WITHOUT_REPORT = "Check system integrity without downloading the XML report. Endpoint used only in the ZenZefi UI.";
  
  public static final String ENDPOINT_GET_SYSTEM_INTEGRITY_XML_REPORT = "Returns the system integrity check XML report. Endpoint used only in the ZenZefi UI.";
  
  public static final String ENDPOINT_CHECK_SYSTEM_INTEGRITY_REPORT_STILL_EXISTS = "Checks whether the system integrity report is still existent. Endpoint used only in the ZenZefi UI.";
  
  public static final String ENDPOINT_MAKE_SYSTEM_INTEGRITY_CHECK_DOWNLOAD_REPORT = "Makes system integrity check and downloads the report. Endpoint used only in the ZenZefi CLI.";
  
  public static final String ENDPOINT_UPDATE_ACTIVATE_FOR_TESTING_SWITCH_STATE = "Switches the state of active for testing  based on certificate's id and type. The type should be known by ZZ server.";
  
  public static final String ENDPOINT_UPDATE_ACTIVATE_FOR_TESTING = "Activating certificate to be used in testing mechanism. The path variable 'id' is ZenZefi internal id of the certificate.";
  
  public static final String ENDPOINT_UPDATE_DEACTIVATE_FOR_TESTING = "Deactivating certificate usage in testing mechanism. The path variable 'id' is Zenzefi internal id of the certificate.";
  
  public static final String ENDPOINT_UPDATE_DEACTIVATE_FOR_TESTING_USE_CASE = "Deactivating certificate usage in testing mechanism. The use case can be: G, VSM, RP. G=General use case, VSM= VSM use cases, RP = Replacement Package use case. The path variable 'id' is ZenZefi internal id of the certificate.";
  
  public static final String ENDPOINT_UPDATE_ACTIVATE_FOR_TESTING_USE_CASE = "Activating certificate to be used in testing mechanism. The use case can be: G, VSM, RP. G=General use case, VSM= VSM use cases, RP = Replacement Package use case. The path variable 'id' is ZenZefi internal id of the certificate.";
  
  public static final String ENDPOINT_ACTIVE_FOR_TESTING_UES_CASE_PARAM = "G=General use case, VSM= VSM use cases, RP = Replacement Package use case";
  
  public static final String ENDPOINT_RESTORE = "Restore root and backend certificates from application binaries.";
  
  public static final String ENDPOINT_IMPORT_ENCRYPTED_PKCS = "Import encrypted PKCS#12 packages. The packages are exported from SigModul, and encrypted with the public key generated by ZenZefi. The packages are Base64 encoded. For details about the parameters, please view the request model.";
  
  public static final String PARAM_ENCRYPTED_PKCS_PACKAGE = "Encrypted PKCS package generated by SigModul.";
  
  public static final String PARAM_AUTHORITY_KEY_IDENTIFIER_SERIAL_NUMBER_PAIRS = "Base64 encoded authority key identifier / serial number pairs.";
  
  public static final String PARAM_AUTHORITY_KEY_IDENTIFIER_SERIAL_NUMBER_PAIRS_ZKNO_SUPPORT = "Base64 encoded authority key identifier (or ZK Number) / serial number pairs.";
  
  public static final String PARAM_FILE_PATH_PASS_WORD = "File path / password pairs. The file path has to provided with single slashes as separator, e.g. c:/folder/folder.ext - mind: single backslashes can not be used.";
  
  public static final String PARAM_REPLACEMENT_PACKAGE_INPUT = "Base64 encoded certificate bytes, replacement target and target backend subject key identifier.";
  
  public static final String PARAM_REPLACEMENT_PACKAGE_INPUT_V2 = "Base64 encoded certificate bytes, replacement target, target backend subject key identifier and unique ECU ids";
  
  public static final String PARAM_REPLACEMENT_PACKAGE_INPUT_V3 = "Base64 encoded certificate bytes, target backend subject key identifier (or ZK Number), unique ECU ids and targetVIN (used to download ECU from PKI if doesn't exist in user store)";
  
  public static final String PARAM_CHAIN_REPLACEMENT_PACKAGE_INPUT = "Target backend subject key identifier (or ZK Number) and source backend subject key identifier (or ZK Number)";
  
  public static final String PARAM_CHECK_OWNERSHIP_INPUT = "Base64 encoded backend subject key identifier, Base64 encoded ecu challenge, Base64 encoded serial number.";
  
  public static final String PARAM_CHECK_OWNERSHIP_INPUT_WITH_ZK_NO = "Base64 encoded backend subject key identifier (or ZK Number), Base64 encoded ecu challenge, Base64 encoded serial number.";
  
  public static final String PARAM_SEC_OCIS_INPUT = "Base64 encoded backend subject key identifier, Base64 encoded diag serial number, Base64 encoded ECU certificate, target ECU, target VIN.";
  
  public static final String PARAM_SEC_OCIS_INPUT_WITH_ZK_NO = "Base64 encoded backend subject key identifier (or ZK Number), Base64 encoded diag serial number, Base64 encoded ECU certificate, target ECU, target VIN.";
  
  public static final String PARAM_SECURE_VARIANT_CODING_INPUT = "Base64 encoded backend subject key identifier, Base64 representation of the data to be signed, target ECU, target VIN.";
  
  public static final String PARAM_SECURE_VARIANT_CODING_INPUT_WITH_ZK_NO = "Base64 encoded backend subject key identifier (or ZK Number), Base64 representation of the data to be signed, target ECU, target VIN.";
  
  public static final String PARAM_IDS_TO_BE_DELETED = "Json Array of ids that should be deleted.";
  
  public static final String PARAM_SIGNATURE_CHECK_INPUT = "Base64 encoded ECU certificate, Base64 encoded message, Base64 encoded signature.";
  
  public static final String PARAM_ENCRYPTED_PKCS_INPUT = "Base64 encoded backend subject key identifier, encrypted PKCS package generated by SigModul.";
  
  public static final String PARAM_ROLLBACK_INPUT = "Base64 encoded backend subject key identifier (or ZK Number), base64 encoded serial number.";
  
  public static final String PARAM_IMPORT_VSM = "Array of EZS serialnumbers and a target Backend Indentifier (base 64 encoded BSKI or ZK Number).";
  
  public static final String PARAM_SIGMODUL_IMPORT_FILES = "Base64 encoded bytes of the certificate and the certificate file name.";
  
  public static final String PARAM_SIGNATURE_INPUT = "Base64 encoded backend subject key identifier(or ZK Number), Base64 encoded coding data, coding data identifier, target ECU and target VIN.";
  
  public static final String PARAM_ENCRYPTED_DIAGS_INPUT = "The public key which was generated by ZenZefi, in order to encrypt PKCS12 packages from SigModul and the optional fields: target ECU and target VIN.";
  
  public static final String PARAM_ZENZEFI_SIGNATURE_INPUT = "Base64 encoded coding data, ecu, vin";
  
  public static final String ENDPOINT_CONFIGURATIONS_GENERAL = "Returns an array of general configurations, which are attached to default user. Endpoint used in the ZenZefi UI.";
  
  public static final String ENDPOINT_CONFIGURATIONS = "Returns an array of configurations, which are attached to current user (Default or Registered). Endpoint used in the ZenZefi UI.";
  
  public static final String ENDPOINT_UPDATE_CONFIGURATIONS = "Update configuration. Endpoint used in the ZenZefi UI.";
  
  public static final String PARAM_CONFIGURATION_ID = "The id of the configuration. Must be a valid UUID.";
  
  public static final String PARAM_CONFIGURATION = "The configuration.";
  
  public static final String ENDPOINT_CONFIGURATIONS_USER_ROLES = "Returns an array of user roles, attached to current user (default or registered). Endpoint used in the ZenZefi UI.";
  
  public static final String ENDPOINT_CONFIGURATIONS_UPDATE_USER_ROLES = "Updates user roles configuration, for current user (default or registered). Endpoint used in the ZenZefi UI.";
  
  public static final String ENDPOINT_SET_DETAILS_PANE_STATE = "Sets the details pane state of the certificates table for the current logged user. Endpoint used in the ZenZefi UI.";
  
  public static final String PARAM_DETAILS_PANE_STATE = "The details pane state. Open or closed.";
  
  public static final String ENDPOINT_GET_DETAILS_PANE_STATE = "Gets the details pane state of the certificates table for the current logged user. Endpoint used in the ZenZefi UI.";
  
  public static final String ENDPOINT_GET_COLUMN_VISIBILITY = "Gets the column visibility of the certificates table for the current logged user. Endpoint used in the ZenZefi UI.";
  
  public static final String ENDPOINT_SET_COLUMN_VISIBILITY = "Sets the column visibility of the certificates table for the current logged user. Endpoint used in the ZenZefi UI.";
  
  public static final String PARAM_COLUMN_VISIBILITIES = "Column visibilities.";
  
  public static final String ENDPOINT_SET_CERTIFICATE_COLUMN_ORDER = "Sets the column order of the certificates table for the current logged user. Endpoint used in the ZenZefi UI.";
  
  public static final String PARAM_COLUMN_ORDER = "The column order.";
  
  public static final String ENDPOINT_GET_CERTIFICATE_COLUMN_ORDER = "Gets the column order of the certificates table for the current logged user. Endpoint used in the ZenZefi UI.";
  
  public static final String ENDPOINT_GET_PAGINATION_MAX_ROWS = "Gets the max rows for pagination. Endpoint used in the ZenZefi UI, in the logs table.";
  
  public static final String ENDPOINT_UPDATE_PAGINATION_MAX_ROWS = "Updates the max rows for pagination. Endpoint used in the ZenZefi UI, in the logs table.";
  
  public static final String PARAM_MAX_ROWS = "The maximum number of rows for pagination. Endpoint used in the ZenZefi UI, in the logs table. Valid values: integer values.";
  
  public static final String ENDPOINT_CONFIGURATIONS_GET_APPLICATION_VERSION = "Returns the application version.";
  
  public static final String ENDPOINT_CONFIGURATIONS_GET_PKI_LOGOUT_URL = "Returns the PKI Logout URL.";
  
  public static final String ENDPOINT_SET_PROXY = "Sets the server proxy.";
  
  public static final String ENDPOINT_GET_PROXY = "Gets the server proxy.";
  
  public static final String ENDPOINT_GET_PROXY_TYPE = "Gets the server proxy type.";
  
  public static final String ENDPOINT_SET_PROXY_TYPE = "Sets the server proxy type.";
  
  public static final String ENDPOINT_SET_PROXY_WITH_TYPE = "Sets the server proxy and the proxy type.";
  
  public static final String PARAM_PROXY = "Proxy value.";
  
  public static final String PARAM_PROXY_TYPE = "Proxy type value.";
  
  public static final String ENDPOINT_GET_LOGS = "Get file with logs created since the last call. First call will return all available log entries.";
  
  public static final String ENDPOINT_GET_APPLICATION_LOGGERS = "Get application loggers.";
  
  public static final String ENDPOINT_SET_LOGGING_LEVEL = "Set configured log level for all the specified application loggers.";
  
  public static final String ENDPOINT_GET_LOGGING_LEVEL = "Get configured log level for all the specified application loggers.";
  
  public static final String PARAM_LOGGING_LEVEL = "The logging level. Accepted values: FINE, FINER, FINEST, INFO.";
  
  public static final String ENDPOINT_LOGS_INTEGRITY = "Returns false if a single log entry was deleted from the database, true otherwise.";
  
  public static final String ENDPOINT_INSTALATION_DETAILS = "Returns the instalation details of the running ZenZefi server.";
  
  public static final String ENDPOINT_ALERT_MESSAGES = "Returns\\removes alert messages. Internal Endpoint.";
  
  public static final String ENDPOINT_ARCHIVE = "Archives and downloads logs.";
  
  public static final String PARAM_ARCHIVE_PATH = "The absolute path of where the archive should be saved.";
  
  public static final String ENDPOINT_LOGS = "Downloads all the logs. Endpoint used in the ZenZefi UI, logs table.";
  
  public static final String ENDPOINT_SIGMODUL_DCD_VERSION = "Gets SigModul DCD server version and api version.";
  
  public static final String ENDPOINT_LIST_PERFORMANCE_AUDIT_ENTRIES = "List performance audit entries.";
  
  public static final String ENDPOINT_SIGMODUL_SI_VERSION = "Gets SigModul SI server version and api version.";
  
  public static final String ENDPOINT_ZENZEFI_VERSION = "Gets ZenZefi server version and api version.";
  
  public static final String ENDPOINT_KEY_PAIR_GENERATION = "Key Pair generation. Used for communication with other systems. SigModul uses the generated public key to encrypt PKCS packages.";
  
  public static final String ENDPOINT_SYSTEM_PUBLIC_KEY = "System's public key. It is used in encrypted communication between systems";
  
  public static final String ENDPOINT_RANDOM_TESTER_NONCE = "Secure Random Nonce generated fro testers";
  
  public static final String ENDPOINT_GET_USERS = "Returns a list of users.";
  
  public static final String ENDPOINT_DELETE_USERS = "Deletes user accounts by id. The ids must valid UUIDs.";
  
  public static final String ENDPOINT_DELETE_USER_BY_NAME = "Deletes user account by username.";
  
  public static final String PARAM_USER_IDS = "Users ids, must be valid UUIDs, comma separated.";
  
  public static final String PARAM_USER = "The user.";
  
  public static final String PARAM_USER_NAME = "The user name. Length between 1 and 7 characters. Must be alpha numeric, non blank.";
  
  public static final String PARAM_FIRST_NAME = "The first name. Length between 1 and 100 characters.";
  
  public static final String PARAM_LAST_NAME = "The last name. Length between 1 and 100 characters.";
  
  public static final String PARAM_ORGANISATION = "The organisation. Length between 1 and 100 characters.";
  
  public static final String PARAM_USER_PASS_WORD = "The user password. Length between 9 and 100 characters. Must contain upper case, lower case, digit, and special characters.";
  
  public static final String PARAM_USER_PASS_WORD_BASE64 = "The user password as base64 encoded UTF-8 string. Length of the undecoded string must be between 9 and 100 characters. Must contain upper case, lower case, digit, and special characters.";
  
  public static final String ENDPOINT_USER_REGISTRATION = "Registers a new user in ZenZefi.";
  
  public static final String ENDPOINT_APP_TRIGGERED_LOGIN = "Application triggered login based on the provided username and password";
  
  public static final String ENDPOINT_USER_LOGIN = "Local login for an exiting user in based on the provided username and password.";
  
  public static final String ENDPOINT_LOGOUT_USER = "Log out a user from ZenZefi.";
  
  public static final String ENDPOINT_LOGOUT_USER_SWITCH = "Log out a user from ZenZefi if the username changes.";
  
  public static final String ENDPOINT_CURRENT_USER = "Checks if the current user is the default user. Endpoint used in the ZenZefi UI for automated logout.";
  
  public static final String ENDPOINT_RESET_USER_SESSION = "Resets the session timeout for the current user session and returns the user details.";
  
  public static final String ENDPOINT_UPDATE_AUTOLOGOUT = "Activates or deactivates automatically logout.";
  
  public static final String ENDPOINT_GET_FILTERED_LOGS = "Filters the ZenZefi logs table. Endpoint used only in the ZenZefi UI.";
  
  public static final String PARAM_LOG_CREATE_TIMESTAMP = "Log create timestamp, sent as from date and to date pair.";
  
  public static final String PARAM_LOG_FILTER_RANGE = "Log filter range. E.g. items=0-9";
  
  public static final String ENDPOINT_GET_DISTINCT_LOG_LEVELS = "Returns the distinct log levels which are in the database. Endpoint used for the filtering on the UI logs table.";
  
  public static final String ENDPOINT_SIGN_ECU_REQUEST = "Signs a challenge byte array with the private key of an ECU certificate to prove the ownership of the certificate.";
  
  public static final String ENDPOINT_CERTIFICATES_FULL_UPDATE = "Perform full update of the certificates. All certificates the user has rights for are requested independently of the certificate renewal time.";
  
  public static final String ENDPOINT_CERTIFICATES_DIFFERENTIAL_UPDATE = "Perform differential update of the certificates. Only those certificates in the user-specific certificate store.";
  
  public static final String ENDPOINT_CERTIFICATES_METRICS = "Returns the metrics during the certificates update process";
  
  public static final String ENDPOINT_ROOT_BACKENDS = "Returns the root and backend certificates for the currently logged in user.";
  
  public static final String PARAM_ROOT_BACKEND = "Which certificates should be retrieved: BACKEND, ROOT. Parameter is optional, if none is provided all are returned.";
  
  public static final String ENDPOINT_UPDATE_ACTIVE = "Returns whether or not the update operation is active.";
  
  public static final String PARAM_SIGN_ECU_BYTE_ARRAY = "Challenge byte array to be signed, Base64 encoded.";
  
  public static final String PARAM_SIGN_ECU_INPUT = "ECU sign request input.";
  
  public static final String FILTER_OPTIONS = "Filter options";
  
  public static final String FILTER_OPTION = "column for which the options are needed";
  
  public static final String ENDPOINT_DIAG_FOR_CENTRL_AUTHENTICATION = "Diagnostic Authentication certificates for central authentication are regular Diagnostic Authentication certificates, which however must not be restricted to a specific ECU and which must not contain certain user roles (individually configurable)";
  
  public static final String ENDPOINT_EXPORT_CSR = "Retrieves a file with the certificate sign request.";
  
  public static final String ENDPOINT_GET_ENH_RIGHT_ID = "Determine Enhanced Rights Certificates for dedicated services. Search for Enhanced Rights certificates in the user store containing specific services that can be used for Enhanced Rights Certificates to release these services in the ECU. If ZenZefi finds one or more results, i.e. Enhanced Rights certificates which match, the serial numbers of the holder certificates and the Enhanced Rights certificates will be returned.";
  
  public static final String PARAM_SUBJECT_KEY_IDENTIFIER = "The Subject Key Identifier. It is sent as Base64 encoded bytes, and the length must be 20 bytes.";
  
  public static final String ENDPOINT_ENABLE_EXTENDED_VALIDATION = "Enable/Disable extended validation";
  
  public static final String ACTUATOR_API_MESSAGE = "\nThe operation-handler endpoints link to the enabled Spring Boot Actuator endpoints. For more details please check:\nhttps://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html";
  
  public static final String PARAM_ZKNO = "The ZK number.";
  
  private static final String BYTE = " byte.";
  
  public static final String PARAM_SPECIAL_ECU = "Special ECU. Maximum size is 1 byte.";
  
  public static final String ENDPOINT_GET_PKI_SCOPE = "Gets the pki scope setting.";
  
  public static final String ENDPOINT_SET_PKI_SCOPE = "Sets the pki scope setting.";
  
  public static final String ENDPOINT_SIGN_PAYLOAD = "Create signature for input byte array";
}


/* Location:              C:\Users\admin\Downloads\Zenzefi\zenzefi-1.96.0.6.jar!\BOOT-INF\lib\common-server-1.96.0.6.jar!\com\daimler\cebas\common\swagger\SwaggerApiDescription.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */