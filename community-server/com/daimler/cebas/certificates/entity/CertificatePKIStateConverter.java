/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.entity.CertificatePKIState
 *  javax.persistence.AttributeConverter
 */
package com.daimler.cebas.certificates.entity;

import com.daimler.cebas.certificates.entity.CertificatePKIState;
import javax.persistence.AttributeConverter;
import org.apache.commons.lang3.StringUtils;

public class CertificatePKIStateConverter
implements AttributeConverter<CertificatePKIState, String> {
    public String convertToDatabaseColumn(CertificatePKIState pkiState) {
        if (pkiState != null) return pkiState.getValue();
        return CertificatePKIState.NONE.getValue();
    }

    public CertificatePKIState convertToEntityAttribute(String value) {
        if (!StringUtils.isEmpty(value)) return CertificatePKIState.getFromValue((String)value);
        return CertificatePKIState.NONE;
    }
}
