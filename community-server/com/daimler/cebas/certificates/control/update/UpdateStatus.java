/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.certificates.control.update;

public enum UpdateStatus {
    RUNNING("updateCertificatesStatusRunning"),
    NOT_RUNNING("updateCertificatesStatusNotRunning"),
    PAUSED("updateCertificatesStatusPause");

    private String text;

    private UpdateStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
