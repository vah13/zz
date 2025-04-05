/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.vo.CEBASResult
 */
package com.daimler.cebas.certificates.control.vo;

import com.daimler.cebas.common.control.vo.CEBASResult;

public class CreateCSRResult
extends CEBASResult {
    private String internalID;
    private String subjectPublicKey;

    public CreateCSRResult() {
    }

    public CreateCSRResult(String internalID, String subjectPublicKey) {
        this.internalID = internalID;
        this.subjectPublicKey = subjectPublicKey;
    }

    public String getInternalID() {
        return this.internalID;
    }

    public String getSubjectPublicKey() {
        return this.subjectPublicKey;
    }
}
