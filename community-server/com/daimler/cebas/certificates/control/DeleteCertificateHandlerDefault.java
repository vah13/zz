/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.DeleteCertificatesEngine
 *  com.daimler.cebas.certificates.control.config.handlers.IDeleteCertificateHandler
 *  com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException
 *  com.daimler.cebas.certificates.control.validation.CertificateInStoreValidator
 *  com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo
 *  com.daimler.cebas.certificates.control.vo.DeleteCertificatesResult
 *  com.daimler.cebas.certificates.control.vo.ExtendedDeleteCertificatesResult
 *  com.daimler.cebas.certificates.entity.BackendContext
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.certificates.entity.CertificateState
 *  com.daimler.cebas.certificates.entity.CertificateType
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.logs.control.Logger
 *  com.daimler.cebas.users.entity.User
 */
package com.daimler.cebas.certificates.control;

import com.daimler.cebas.certificates.control.DeleteCertificatesEngine;
import com.daimler.cebas.certificates.control.config.handlers.IDeleteCertificateHandler;
import com.daimler.cebas.certificates.control.exceptions.CEBASCertificateException;
import com.daimler.cebas.certificates.control.validation.CertificateInStoreValidator;
import com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo;
import com.daimler.cebas.certificates.control.vo.DeleteCertificatesResult;
import com.daimler.cebas.certificates.control.vo.ExtendedDeleteCertificatesResult;
import com.daimler.cebas.certificates.entity.BackendContext;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.certificates.entity.CertificateState;
import com.daimler.cebas.certificates.entity.CertificateType;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.logs.control.Logger;
import com.daimler.cebas.users.entity.User;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;

public class DeleteCertificateHandlerDefault
implements IDeleteCertificateHandler {
    private DeleteCertificatesEngine engine;
    protected Logger logger;
    protected MetadataManager i18n;

    public DeleteCertificateHandlerDefault(DeleteCertificatesEngine engine, Logger logger, MetadataManager i18n) {
        this.engine = engine;
        this.logger = logger;
        this.i18n = i18n;
    }

    public void deleteCertificate(List<String> ids, boolean duringUpdateSession, User currentUser, List<DeleteCertificatesInfo> deleteCertificatesInfo, List<Certificate> roots, Certificate certificate) {
        if (certificate.getState().equals((Object)CertificateState.ISSUED)) {
            deleteCertificatesInfo.add(this.createDeleteCertificatesInfoForCertificate(certificate));
        } else {
            deleteCertificatesInfo.add(this.createDeleteCertificatesInfoForCSR(certificate));
        }
        if (CertificateInStoreValidator.isInStore((Certificate)certificate, (String)currentUser.getEntityId(), (Logger)this.logger)) {
            boolean parentExistInList = this.checkIfParentShouldBeDeleted(certificate, ids);
            if (parentExistInList) return;
            this.engine.deleteCertificateForUser(certificate, currentUser);
            if (certificate.getParent() != null) return;
            roots.add(certificate);
            return;
        }
        CEBASCertificateException zenzefiCertificateException = new CEBASCertificateException(this.i18n.getMessage("certificateDoesNotExistInUserStore", new String[]{certificate.getEntityId()}), "certificateDoesNotExistInUserStore");
        this.logger.log(Level.WARNING, "000104X", this.i18n.getEnglishMessage("certificateDoesNotExistInUserStore", new String[]{certificate.getEntityId()}), this.getClass().getSimpleName());
        throw zenzefiCertificateException;
    }

    public ExtendedDeleteCertificatesResult createSuccessDeleteCertificateResult(DeleteCertificatesInfo deleteCertificatesInfo) {
        DeleteCertificatesResult deleteCertificatesResult = new DeleteCertificatesResult(deleteCertificatesInfo.getCertificateId(), deleteCertificatesInfo.isCertificate(), deleteCertificatesInfo.getCertificateType(), true, this.createDeleteResultMessage(deleteCertificatesInfo), deleteCertificatesInfo.getSerialNo(), deleteCertificatesInfo.getSubjectKeyIdentifier(), deleteCertificatesInfo.getAuthKeyIdentifier());
        return new ExtendedDeleteCertificatesResult(deleteCertificatesResult, deleteCertificatesInfo.getZkNo(), deleteCertificatesInfo.getEcuPackageTs());
    }

    public String createDeleteResultMessage(DeleteCertificatesInfo entry) {
        CertificateType certificateType = CertificateType.getTypeForLogging((DeleteCertificatesInfo)entry);
        if (!StringUtils.isEmpty(entry.getZkNo())) return this.i18n.getEnglishMessage("deleteCertificateUserActionWithPN", new String[]{this.i18n.getEnglishMessage(certificateType.getLanguageProperty()), entry.getCertificateId(), entry.getSerialNo(), entry.getSubjectKeyIdentifier(), entry.getAuthKeyIdentifier(), entry.getZkNo()});
        return this.i18n.getEnglishMessage("deleteCertificateUserAction", new String[]{this.i18n.getEnglishMessage(certificateType.getLanguageProperty()), entry.getCertificateId(), entry.getSerialNo(), entry.getSubjectKeyIdentifier(), entry.getAuthKeyIdentifier()});
    }

    public String createDeleteCSRResultMessage(DeleteCertificatesInfo entry) {
        return this.i18n.getEnglishMessage("deleteCSRUserAction", new String[]{this.i18n.getEnglishMessage(entry.getCertificateType().getLanguageProperty()), entry.getCertificateId(), entry.getAuthKeyIdentifier(), entry.getPublicKey()});
    }

    public ExtendedDeleteCertificatesResult createFailDeleteCertificateByIdResult(String notFoundId) {
        DeleteCertificatesResult deleteCertificatesResult = new DeleteCertificatesResult(notFoundId, false, null, false, this.i18n.getEnglishMessage("certificateDoesNotExistInUserStore", new String[]{notFoundId}), null, null, null);
        return new ExtendedDeleteCertificatesResult(deleteCertificatesResult, null, null);
    }

    public ExtendedDeleteCertificatesResult createFailDeleteCertificateByAuthKeyAndSnResult(String currentAuthKeyIdentifier, String currentSerialNumber) {
        DeleteCertificatesResult deleteCertificatesResult = new DeleteCertificatesResult(null, false, null, false, this.i18n.getEnglishMessage("searchedCertificateDoesNotExistInUserStore", new String[]{currentAuthKeyIdentifier, currentSerialNumber}), currentSerialNumber, currentAuthKeyIdentifier, null);
        return new ExtendedDeleteCertificatesResult(deleteCertificatesResult, null, null);
    }

    public ExtendedDeleteCertificatesResult createSuccessDeleteCSRResult(DeleteCertificatesInfo deleteCertificatesInfo) {
        DeleteCertificatesResult deleteCertificatesResult = new DeleteCertificatesResult(deleteCertificatesInfo.getCertificateId(), deleteCertificatesInfo.isCertificate(), deleteCertificatesInfo.getCertificateType(), true, this.createDeleteCSRResultMessage(deleteCertificatesInfo), deleteCertificatesInfo.getAuthKeyIdentifier(), deleteCertificatesInfo.getPublicKey());
        return new ExtendedDeleteCertificatesResult(deleteCertificatesResult, deleteCertificatesInfo.getZkNo(), deleteCertificatesInfo.getEcuPackageTs());
    }

    protected DeleteCertificatesInfo createDeleteCertificatesInfoForCertificate(Certificate certificate) {
        BackendContext backendContext = certificate.getBackendContext();
        return new DeleteCertificatesInfo(certificate.getEntityId(), true, certificate.getType(), certificate.getSerialNo(), certificate.getSubjectKeyIdentifier(), certificate.getAuthorityKeyIdentifier(), certificate.getTargetSubjectKeyIdentifier(), backendContext.getZkNo(), backendContext.getEcuPackageTs());
    }

    private boolean checkIfParentShouldBeDeleted(Certificate certificate, List<String> ids) {
        Certificate crtCertificate = certificate;
        while (crtCertificate.getParent() != null) {
            if (ids.contains(crtCertificate.getParent().getEntityId())) {
                return true;
            }
            crtCertificate = crtCertificate.getParent();
        }
        return false;
    }

    private DeleteCertificatesInfo createDeleteCertificatesInfoForCSR(Certificate certificate) {
        return new DeleteCertificatesInfo(certificate.getEntityId(), false, certificate.getType(), certificate.getParent().getSubjectKeyIdentifier(), certificate.getSubjectPublicKey());
    }
}
