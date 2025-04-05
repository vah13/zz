/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder
 *  com.daimler.cebas.certificates.control.vo.ImportResult
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.common.control.MetadataManager
 *  com.daimler.cebas.common.control.annotations.FactoryMethodPattern
 *  org.springframework.beans.factory.annotation.Autowired
 */
package com.daimler.cebas.certificates.control.factories;

import com.daimler.cebas.certificates.control.chain.CertificatePrivateKeyHolder;
import com.daimler.cebas.certificates.control.vo.ImportResult;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.common.control.MetadataManager;
import com.daimler.cebas.common.control.annotations.FactoryMethodPattern;
import org.springframework.beans.factory.annotation.Autowired;

@FactoryMethodPattern
public abstract class AbstractImportResultFactory<T extends Certificate> {
    protected MetadataManager i18n;

    @Autowired
    public AbstractImportResultFactory(MetadataManager i18n) {
        this.i18n = i18n;
    }

    public abstract ImportResult getImportResult(CertificatePrivateKeyHolder<T> var1, String var2, boolean var3);
}
