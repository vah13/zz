/*
 * Decompiled with CFR 0.152.
 */
package com.daimler.cebas.common.control;

public class StartupStatus {
    private static boolean started = false;

    public static boolean isApplicationStarted() {
        return started;
    }

    public static void setStarted() {
        started = true;
    }
}
