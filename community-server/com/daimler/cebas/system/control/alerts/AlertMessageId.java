/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.system.control.alerts;

public enum AlertMessageId {
    SYSTEM_CLOCK_IS_NOT_SYNCRONOUS_WITH_PKI("system_clock_is_not_synchronous_with_pki");

    private final String value;

    private AlertMessageId(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
