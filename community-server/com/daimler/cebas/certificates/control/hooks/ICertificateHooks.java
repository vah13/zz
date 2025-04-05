/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.entity.Certificate
 */
package com.daimler.cebas.certificates.control.hooks;

import com.daimler.cebas.certificates.entity.Certificate;
import java.util.Optional;
import java.util.function.Consumer;

public interface ICertificateHooks {
    public Optional<Consumer<Certificate>> possibleHook();

    public void exec(Certificate var1);
}
