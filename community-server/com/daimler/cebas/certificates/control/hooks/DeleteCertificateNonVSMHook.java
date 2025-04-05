/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.hooks.ICertificateHooks
 *  com.daimler.cebas.certificates.entity.Certificate
 *  com.daimler.cebas.logs.control.Logger
 */
package com.daimler.cebas.certificates.control.hooks;

import com.daimler.cebas.certificates.control.hooks.ICertificateHooks;
import com.daimler.cebas.certificates.entity.Certificate;
import com.daimler.cebas.logs.control.Logger;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;

public class DeleteCertificateNonVSMHook
implements ICertificateHooks {
    private Logger logger;

    public DeleteCertificateNonVSMHook(Logger logger) {
        this.logger = logger;
    }

    public Optional<Consumer<Certificate>> possibleHook() {
        return Optional.of(this.createHook());
    }

    public Consumer<Certificate> createHook() {
        return this::exec;
    }

    public void exec(Certificate cert) {
        Certificate backend = cert.getParent();
        if (!cert.isNonVsmEcu()) return;
        if (backend == null) return;
        this.logger.log(Level.FINE, "000623", "Backend with SKI " + backend.getSubjectKeyIdentifier() + " had the ECU Package Ts: " + backend.getEcuPackageTs() + " before reset", this.getClass().getSimpleName());
        backend.setEcuPackageTs(null);
        this.logger.log(Level.INFO, "000623", "Deletion of NON-VSM ECU certificate triggered ECU Package timestamp reset for backend with SKI " + backend.getSubjectKeyIdentifier(), this.getClass().getSimpleName());
    }
}
