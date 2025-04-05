/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.entity.Certificate
 */
package com.daimler.cebas.certificates.control.update.task;

import com.daimler.cebas.certificates.entity.Certificate;
import java.util.ArrayList;
import java.util.List;

public class UpdateRootAndBackendsResult {
    private List<Certificate> updatedRootAndBackends = new ArrayList<Certificate>();

    public void addUpdatedBackend(Certificate be) {
        this.updatedRootAndBackends.add(be);
    }

    public List<Certificate> getUpdatedRootAndBackends() {
        return this.updatedRootAndBackends;
    }
}
