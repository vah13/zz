/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.CEBASException
 */
package com.daimler.cebas.system.control.exceptions;

import com.daimler.cebas.common.control.CEBASException;

public class ConfigurationCheckException
extends CEBASException {
    private static final long serialVersionUID = -7206754217951794175L;

    public ConfigurationCheckException() {
        super("");
    }

    public ConfigurationCheckException(String message) {
        super(message);
    }
}
