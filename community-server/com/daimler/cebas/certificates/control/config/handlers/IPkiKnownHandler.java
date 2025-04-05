/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.vo.ImportResult
 *  com.daimler.cebas.certificates.entity.Certificate
 */
package com.daimler.cebas.certificates.control.config.handlers;

import com.daimler.cebas.certificates.control.vo.ImportResult;
import com.daimler.cebas.certificates.entity.Certificate;
import java.util.List;

public interface IPkiKnownHandler<T extends Certificate> {
    public T updateBackendPkiKnown(Certificate var1);

    public boolean updateBackendPkiKnownOnIdentical(Certificate var1);

    public void updatePkiKnownForAllUnknownBackends(List<ImportResult> var1);

    public boolean isPKIKnown(Certificate var1);
}
