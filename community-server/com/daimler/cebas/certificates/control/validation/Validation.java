/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.certificates.entity.Certificate
 */
package com.daimler.cebas.certificates.control.validation;

import com.daimler.cebas.certificates.entity.Certificate;
import java.util.function.Predicate;

@FunctionalInterface
public interface Validation {
    public Predicate<Certificate> getRule(Certificate var1);
}
