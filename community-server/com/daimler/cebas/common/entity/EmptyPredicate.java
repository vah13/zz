/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.common.entity;

public class EmptyPredicate {
    private final boolean canBeEmpty;

    public EmptyPredicate(boolean canBeEmpty) {
        this.canBeEmpty = canBeEmpty;
    }

    public boolean canBeEmpty() {
        return this.canBeEmpty;
    }
}
