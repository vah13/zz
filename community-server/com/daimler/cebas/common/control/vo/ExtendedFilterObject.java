/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.common.control.vo.FilterObject
 */
package com.daimler.cebas.common.control.vo;

import com.daimler.cebas.common.control.vo.FilterObject;

public class ExtendedFilterObject
extends FilterObject {
    private String orSomethingElse;

    public ExtendedFilterObject(boolean match, Object value) {
        super(match, value);
    }

    public ExtendedFilterObject(boolean match, Object value, String orSomethingElse) {
        super(match, value);
        this.orSomethingElse = orSomethingElse;
    }

    public String getOrSomethingElse() {
        return this.orSomethingElse;
    }
}
