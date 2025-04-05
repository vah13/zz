/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.config.CertificatesConfiguration
 *  com.daimler.cebas.certificates.control.exceptions.BadFormatException
 *  com.daimler.cebas.certificates.entity.UserRole
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.logs.control.Logger
 */
package com.daimler.cebas.certificates.boundary;

import com.daimler.cebas.certificates.control.config.CertificatesConfiguration;
import com.daimler.cebas.certificates.control.exceptions.BadFormatException;
import com.daimler.cebas.certificates.entity.UserRole;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.logs.control.Logger;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractCertificatesResource {
    protected static final String MESSAGE = "message";
    protected static final String CERTIFICATES = "/certificates";
    protected static final String CERTIFICATES_REMOVE = "/certificates/remove";
    protected static final String LIST_CERTIFICATES = "/certificates/list";
    protected static final String LIST_CERTIFICATES_PAGINATED = "/certificates/listPaginated";
    protected static final String CERTIFICATES_SEARCH = "/certificates/search";
    protected static final String CHECK_SYSTEM_INTEGRITY = "/certificates/checkSystemIntegrity";
    protected static final String CHECK_SYSTEM_INTEGRITY_REPORT = "/certificates/checkSystemIntegrityReport";
    protected static final String CHECK_SYSTEM_INTEGRITY_XML_LOG = "/certificates/checkSystemIntegrityLog";
    protected static final String CERTIFICATES_RESTORE = "/certificates/restore";
    protected static final String CERTIFICATES_IMPORT_ENCRYPTED_PKCS_PACKAGE = "/certificates/importProductionCerts";
    protected static final String SYSTEM_INTEGRITY_LOG_EXISTANCE = "/certificates/checkSystemIntegrityLogExistance";
    protected static final String ENABLE_ROLLBACK_MODE = "/certificates/enableRollback";
    protected static final String DISABLE_ROLLBACK_MODE = "/certificates/disableRollback";
    protected static final String SIGN_ECU_REQUEST = "/certificates/signECU";
    protected static final String MORE_DETAILS = "/details";
    protected static final String UPDATE_CERTIFICATES_METRICS = "/certificates/update/metrics";
    protected static final String UPDATE_SESSION_ACTIVE = "/certificates/update/active";
    protected static final String CERTIFICATES_FULL_UPDATE = "/certificates/update/full";
    protected static final String CERTIFICATES_DIFFERENTIAL_UPDATE = "/certificates/update/differential";
    protected static final String CERTIFICATES_ROOT_BACKENDS = "/certificates/rootsAndBackends";
    protected Logger logger;
    protected CertificatesConfiguration certificateProfileConfiguration;
    protected MetadataManager requestMetaData;

    public AbstractCertificatesResource(Logger logger, CertificatesConfiguration certificateProfileConfiguration, MetadataManager requestMetaData) {
        this.logger = logger;
        this.certificateProfileConfiguration = certificateProfileConfiguration;
        this.requestMetaData = requestMetaData;
    }

    protected void validateUserRole(String userRole) {
        if (StringUtils.isEmpty(userRole)) return;
        if (StringUtils.isNumeric(userRole)) {
            if (!StringUtils.isNumeric(userRole)) return;
            if (Byte.valueOf(userRole) <= 8) return;
        }
        BadFormatException ex = new BadFormatException(this.requestMetaData.getMessage("invalidFormatForUserRole", new String[]{UserRole.getValuesMap().toString()}), "invalidFormatForUserRole");
        this.logger.logWithTranslation(Level.WARNING, "000141X", ex.getMessageId(), new String[]{UserRole.getValuesMap().toString()}, ex.getClass().getSimpleName());
        throw ex;
    }

    protected void addRequestMetaData(String locale) {
        this.requestMetaData.setLocale(locale);
    }
}
