/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.control.hooks.ICertificateHooks
 *  com.daimler.cebas.certificates.entity.Certificate
 */
package com.daimler.cebas.certificates.control.hooks;

import com.daimler.cebas.certificates.control.hooks.ICertificateHooks;
import com.daimler.cebas.certificates.entity.Certificate;
import java.util.Optional;
import java.util.function.Consumer;

public class NoHook
implements ICertificateHooks {
    public Optional<Consumer<Certificate>> possibleHook() {
        return Optional.empty();
    }

    public void exec(Certificate cert) {
    }
}
