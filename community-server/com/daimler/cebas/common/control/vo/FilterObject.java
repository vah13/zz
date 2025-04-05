/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.common.control.vo;

public class FilterObject {
    private final boolean match;
    private final Object value;

    public FilterObject(boolean match, Object value) {
        this.match = match;
        this.value = value;
    }

    public boolean isMatch() {
        return this.match;
    }

    public Object getFilterValue() {
        return this.value;
    }
}
