/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo
 *  com.daimler.cebas.certificates.control.vo.ExtendedDeleteCertificatesResult
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.users.entity.User
 */
package com.daimler.cebas.certificates.control.config.handlers;

import com.daimler.cebas.certificates.control.vo.DeleteCertificatesInfo;
import com.daimler.cebas.certificates.control.vo.ExtendedDeleteCertificatesResult;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.users.entity.User;
import java.util.List;

public interface IDeleteCertificateHandler {
    public void deleteCertificate(List<String> var1, boolean var2, User var3, List<DeleteCertificatesInfo> var4, List<Certificate> var5, Certificate var6);

    public ExtendedDeleteCertificatesResult createSuccessDeleteCertificateResult(DeleteCertificatesInfo var1);

    public String createDeleteResultMessage(DeleteCertificatesInfo var1);

    public String createDeleteCSRResultMessage(DeleteCertificatesInfo var1);

    public ExtendedDeleteCertificatesResult createFailDeleteCertificateByIdResult(String var1);

    public ExtendedDeleteCertificatesResult createFailDeleteCertificateByAuthKeyAndSnResult(String var1, String var2);

    public ExtendedDeleteCertificatesResult createSuccessDeleteCSRResult(DeleteCertificatesInfo var1);
}
