/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.common.entity;

import java.util.Arrays;
import java.util.List;

public class InPredicate {
    private List<?> predicates;

    public InPredicate(Object ... predicates) {
        this.predicates = Arrays.asList(predicates);
    }

    public List<?> getPredicates() {
        return this.predicates;
    }
}
