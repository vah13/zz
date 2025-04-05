/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.DurationParser
 *  com.daimler.cebas.certificates.control.validation.CertificatesFieldsValidator
 *  com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput
 *  com.daimler.cebas.certificates.entity.PKIRole
 *  com.daimler.cebas.certificates.entity.UserRole
 *  com.daimler.cebas.certificates.integration.vo.Permission
 *  com.daimler.cebas.common.control.CEBASException
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.FacadePattern
 *  com.daimler.cebas.logs.control.Logger
 */
package com.daimler.cebas.certificates.control.validation;

import com.daimler.cebas.certificates.control.DurationParser;
import com.daimler.cebas.certificates.control.validation.CertificatesFieldsValidator;
import com.daimler.cebas.certificates.control.validation.failure.ValidationFailureOutput;
import com.daimler.cebas.certificates.entity.PKIRole;
import com.daimler.cebas.certificates.entity.UserRole;
import com.daimler.cebas.certificates.integration.vo.Permission;
import com.daimler.cebas.common.control.CEBASException;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.FacadePattern;
import com.daimler.cebas.logs.control.Logger;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;

@FacadePattern
public final class PermissionsValidator {
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(PermissionsValidator.class.getSimpleName());

    private PermissionsValidator() {
    }

    public static boolean validate(Permission permission, Logger logger, MetadataManager i18n) {
        ArrayList<String> errorMessages = new ArrayList<String>();
        PermissionsValidator.validatePKIRole(permission.getPkiRole(), errorMessages, i18n);
        PermissionsValidator.validateUserRole(permission.getUserRole(), errorMessages, i18n);
        PermissionsValidator.validateTargetECU(permission.getTargetECU(), errorMessages, logger, i18n);
        PermissionsValidator.validateTargetVIN(permission.getTargetVIN(), errorMessages, logger, i18n);
        PermissionsValidator.validateRenewal(permission.getRenewal(), errorMessages, i18n);
        PermissionsValidator.validateUniqueECUId(permission.getUniqueECUID(), errorMessages, logger, i18n);
        StringBuilder errorMessagesHolder = new StringBuilder();
        if (!errorMessages.isEmpty()) {
            errorMessages.forEach(errorMessagesHolder::append);
            logger.log(Level.WARNING, "000317X", "Ignoring invalid permission: " + permission.toString() + " Reason: " + errorMessagesHolder, PermissionsValidator.class.getSimpleName());
            permission.setValid(false);
            return false;
        }
        permission.setValid(true);
        return true;
    }

    private static void validatePKIRole(String role, List<String> errorMessagesIds, MetadataManager i18n) {
        if (StringUtils.isEmpty(role)) {
            errorMessagesIds.add(i18n.getEnglishMessage("invalidPKIRole"));
            return;
        }
        try {
            int pkiRole = Integer.decode(role);
            if (PKIRole.getRoles().get(pkiRole) != null) return;
            errorMessagesIds.add(i18n.getEnglishMessage("invalidPKIRole"));
        }
        catch (NumberFormatException e) {
            errorMessagesIds.add(i18n.getEnglishMessage("invalidPKIRole"));
        }
    }

    private static void validateUserRole(String role, List<String> errorMessagesIds, MetadataManager i18n) {
        if (StringUtils.isEmpty(role)) {
            return;
        }
        try {
            UserRole userRole = UserRole.getUserRoleFromByte((byte)Byte.decode(role));
            if (userRole != UserRole.NO_ROLE) return;
            errorMessagesIds.add(i18n.getEnglishMessage("invalidUserRole"));
        }
        catch (NumberFormatException e) {
            errorMessagesIds.add(i18n.getEnglishMessage("invalidUserRole"));
        }
    }

    private static void validateTargetECU(List<String> targetECU, List<String> errorMessagesIds, Logger logger, MetadataManager i18n) {
        if (targetECU == null) {
            return;
        }
        targetECU.forEach(ecu -> {
            try {
                CertificatesFieldsValidator.isTargetECULengthCorrentSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)ecu);
            }
            catch (CEBASException e) {
                LOG.log(Level.FINEST, e.getMessage(), e);
                errorMessagesIds.add(i18n.getEnglishMessage("invalidInputForTargetECU", new String[]{ecu}));
            }
        });
    }

    private static void validateUniqueECUId(List<String> uniqueEcuID, List<String> errorMessagesIds, Logger logger, MetadataManager i18n) {
        if (uniqueEcuID == null) {
            return;
        }
        uniqueEcuID.forEach(ecu -> {
            try {
                CertificatesFieldsValidator.isUniqueECUIDLengthCorrectSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)ecu);
            }
            catch (CEBASException e) {
                LOG.log(Level.FINEST, e.getMessage(), e);
                errorMessagesIds.add(i18n.getEnglishMessage("invalidInputForUniqueECUID", new String[]{ecu}));
            }
        });
    }

    private static void validateTargetVIN(List<String> targetVIN, List<String> errorMessagesIds, Logger logger, MetadataManager i18n) {
        if (targetVIN == null) {
            return;
        }
        targetVIN.forEach(vin -> {
            try {
                CertificatesFieldsValidator.isTargetVINLengthCorrectSingle((MetadataManager)i18n, (Logger)logger, ValidationFailureOutput::outputFailureWithThrow, (String)vin);
            }
            catch (CEBASException e) {
                LOG.log(Level.FINEST, e.getMessage(), e);
                errorMessagesIds.add(i18n.getEnglishMessage("invalidInputForTargetVIN", new String[]{vin}));
            }
        });
    }

    private static void validateRenewal(String renewal, List<String> errorMessagesIds, MetadataManager i18n) {
        if (StringUtils.isEmpty(renewal)) {
            errorMessagesIds.add(i18n.getEnglishMessage("invalidRenewal"));
            return;
        }
        try {
            DurationParser.parse((String)renewal);
        }
        catch (DateTimeParseException e) {
            LOG.log(Level.FINEST, e.getMessage(), e);
            errorMessagesIds.add(i18n.getEnglishMessage("invalidRenewal"));
        }
    }
}
