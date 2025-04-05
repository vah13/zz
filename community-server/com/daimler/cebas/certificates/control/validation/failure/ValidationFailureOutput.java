/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.certificates.control.validation.failure;

import com.daimler.cebas.common.control.CEBASException;

@FunctionalInterface
public interface ValidationFailureOutput {
    public void outputFailure(CEBASException var1);

    public static void outputFailureWithThrow(CEBASException exception) {
        throw exception;
    }

    public static void skipeFailureOutput(CEBASException exception) {
    }
}
